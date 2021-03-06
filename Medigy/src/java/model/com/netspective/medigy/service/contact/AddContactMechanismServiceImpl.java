/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
 *
 * Netspective Communications LLC ("Netspective") permits redistribution, modification and use of this file in source
 * and binary form ("The Software") under the Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the canonical license and must be accepted
 * before using The Software. Any use of The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only (as Java .class files or a .jar file
 *    containing the .class files) and only as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software development kit, other library,
 *    or development tool without written consent of Netspective. Any modified form of The Software is bound by these
 *    same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of The License, normally in a plain
 *    ASCII text file unless otherwise agreed to, in writing, by Netspective.
 *
 * 4. The names "Netspective", "Axiom", "Commons", "Junxion", and "Sparx" are trademarks of Netspective and may not be
 *    used to endorse products derived from The Software without without written consent of Netspective. "Netspective",
 *    "Axiom", "Commons", "Junxion", and "Sparx" may not appear in the names of products derived from The Software
 *    without written consent of Netspective.
 *
 * 5. Please attribute functionality where possible. We suggest using the "powered by Netspective" button or creating
 *    a "powered by Netspective(tm)" link to http://www.netspective.com for each application using The Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF HE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 */
package com.netspective.medigy.service.contact;

import com.netspective.medigy.dto.party.AddEmailParameters;
import com.netspective.medigy.dto.party.AddPhoneParameters;
import com.netspective.medigy.dto.party.AddPostalAddressParameters;
import com.netspective.medigy.dto.party.NewPostalAddress;
import com.netspective.medigy.model.contact.City;
import com.netspective.medigy.model.contact.Country;
import com.netspective.medigy.model.contact.County;
import com.netspective.medigy.model.contact.PostalCode;
import com.netspective.medigy.model.contact.Province;
import com.netspective.medigy.model.contact.State;
import com.netspective.medigy.model.party.ContactMechanism;
import com.netspective.medigy.model.party.ElectronicAddress;
import com.netspective.medigy.model.party.Party;
import com.netspective.medigy.model.party.PartyContactMechanism;
import com.netspective.medigy.model.party.PhoneNumber;
import com.netspective.medigy.model.party.PostalAddress;
import com.netspective.medigy.reference.custom.party.ContactMechanismPurposeType;
import com.netspective.medigy.service.ServiceInvocationException;
import com.netspective.medigy.service.ServiceLocator;
import com.netspective.medigy.service.common.ReferenceEntityLookupService;
import com.netspective.medigy.service.common.UnknownReferenceTypeException;
import com.netspective.medigy.util.HibernateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;

public class AddContactMechanismServiceImpl implements AddContactMechanismService
{
    private static final Log log = LogFactory.getLog(AddContactMechanismServiceImpl.class);

    public void validatePostalAddress(final AddPostalAddressParameters param)
    {
        assert (param.getStreet1() != null && param.getStreet1().length() > 0) : "Street Address value cannot be empty.";
        assert (param.getCity() != null && param.getCity().length() > 0) : "City value cannot be  empty";
        assert (param.getState() != null && param.getState().length() > 0) : "State value cannot be empty";
        assert (param.getPostalCode() != null && param.getPostalCode().length() > 0) : "Postal Code value cannot be empty";
        assert (param.getCountry() != null && param.getCountry().length() > 0) : "Country value cannot be empty";
    }


    public NewPostalAddress addPostalAddress(final AddPostalAddressParameters param)  throws ServiceInvocationException
    {
        final Party party = (Party) HibernateUtil.getSession().load(Party.class, param.getPartyId());

        final PostalAddress address = new PostalAddress();
        address.setAddress1(param.getStreet1());
        address.setAddress2(param.getStreet2());
        HibernateUtil.getSession().save(address);

        final String countryName = param.getCountry();
        boolean newCountry = false;
        Country country = (Country) HibernateUtil.getSession().createCriteria(Country.class).add(Restrictions.eq("name", countryName).ignoreCase()).uniqueResult();
        if (country == null)
        {
            country =  new Country();
            country.setCountryName(countryName);
            // if country is new then assume that the rest of the geo boundaries are new too!
            newCountry = true;
        }
        address.addPostalAddressBoundary(country);
        // country exists so check the state/province
        if (param.getState() != null)
        {
            boolean newState = false;
            State state = newCountry ? null : country.getStateByName(param.getState());
            if (state == null)
            {
                // this is a new state for this country
                state = new State();
                state.setStateName(param.getState());
                state.setCountry(country);
                country.addState(state);
                newState = true;
            }
            address.addPostalAddressBoundary(state);
            if (param.getCounty() != null)
            {
                County county = newState ? null : state.getCountyByName(param.getCounty());
                if (county == null)
                {
                    county = new County();
                    county.setCountyName(param.getCounty());
                    county.setState(state);
                    state.addCounty(county);
                }
                address.addPostalAddressBoundary(county);
            }
            // now add the city
            City city = newState ? null :  state.getCityByName(param.getCity());
            if (city == null)
            {
                city = new City();
                city.setCityName(param.getCity());
                city.setState(state);
                state.addCity(city);
            }
            address.addPostalAddressBoundary(city);
            PostalCode postalCode = newState ? null : state.getPostalCodeByValue(param.getPostalCode());
            if (postalCode == null)
            {
                postalCode = new PostalCode();
                postalCode.setCodeValue(param.getPostalCode());
                postalCode.setState(state);
                state.addPostalCode(postalCode);
            }
            address.addPostalAddressBoundary(postalCode);

        }
        else if (param.getProvince() != null)
        {
            boolean newProvince = false;
            Province province = newCountry ? null : country.getProvinceByName(param.getProvince());
            if (province == null)
            {
                // this is a new province for this country
                province = new Province();
                province.setProvinceName(param.getProvince());
                province.setCountry(country);
                country.addProvince(province);
                newProvince = true;
            }
            address.addPostalAddressBoundary(province);
            City city = newProvince ? null : province.getCityByName(param.getCity());
            if (city == null)
                province.addCity(param.getCity());
            address.addPostalAddressBoundary(city);
        }
        if (newCountry)
            HibernateUtil.getSession().save(country);
        else
            HibernateUtil.getSession().flush();

        // NOTE: using the PostalAddress as the contact mechanism introduces multiple
        // PartyContactMechanisms so don't use the PostalAddress, instead use ContactMechanism
        ContactMechanism cm = (ContactMechanism) HibernateUtil.getSession().load(ContactMechanism.class, address.getContactMechanismId());

        addPartyContactMechanism(cm, party, param.getPurpose());
        return new NewPostalAddress()
        {
            public Serializable getPostalAddressId()
            {
                return address.getContactMechanismId();
            }

            public AddPostalAddressParameters getAddPostalAddressParameters()
            {
                return param;
            }
        };
    }

    protected void addPartyContactMechanism(final ContactMechanism cm,
                                            final Party party, final String purposeType)
    {
        final ReferenceEntityLookupService referenceEntityService =
                (ReferenceEntityLookupService) ServiceLocator.getInstance().getService(ReferenceEntityLookupService.class);
        // now create the relationship entry between party and the postal address
        final PartyContactMechanism mech = new PartyContactMechanism();
        mech.setParty(party);
        final ContactMechanismPurposeType contactMechanismPurposeType;
        try
        {
            contactMechanismPurposeType =
                    referenceEntityService.getContactMechanismPurposeType(purposeType);
            mech.addPurpose(contactMechanismPurposeType);
            mech.setContactMechanism(cm);
            HibernateUtil.getSession().save(mech);
        }
        catch (UnknownReferenceTypeException e)
        {
            log.error(e);
            throw new ServiceInvocationException(e);
        }
    }

    public void addEmail(AddEmailParameters param) throws ServiceInvocationException
    {
        final Party party = (Party) HibernateUtil.getSession().load(Party.class, param.getPartyId());
        final ElectronicAddress email = new ElectronicAddress();
        email.setElectronicAddress(param.getEmail());
        HibernateUtil.getSession().save(email);

        ContactMechanism cm = (ContactMechanism) HibernateUtil.getSession().load(ContactMechanism.class,
                email.getContactMechanismId());
        addPartyContactMechanism(cm, party, param.getPurpose());
    }

    public void addPhone(AddPhoneParameters param) throws ServiceInvocationException
    {
        final Party party = (Party) HibernateUtil.getSession().load(Party.class, param.getPartyId());
        final PhoneNumber phone = new PhoneNumber();
        phone.setCountryCode(param.getCountryCode());
        phone.setNumber(param.getNumber());
        phone.setAreaCode(param.getAreaCode());
        phone.setExtension(param.getExtension());
        HibernateUtil.getSession().save(phone);

        ContactMechanism cm = (ContactMechanism) HibernateUtil.getSession().load(ContactMechanism.class,
                phone.getContactMechanismId());
        addPartyContactMechanism(cm, party, param.getPurpose());
    }
}
