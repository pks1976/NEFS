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
package com.netspective.medigy.model.contact;

import com.netspective.medigy.model.common.AbstractTopLevelEntity;
import com.netspective.medigy.model.health.HealthCareLicense;
import com.netspective.medigy.model.party.PostalAddressBoundary;
import com.netspective.medigy.reference.custom.GeographicBoundaryType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratorType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED )
public abstract class GeographicBoundary extends AbstractTopLevelEntity
{
    private Long geoId;
    private String name;
    private String abbreviation;
    private GeographicBoundaryType type;

    private Set<PostalAddressBoundary> postalAddressBoundary = new HashSet<PostalAddressBoundary>();
    //private Set<GeographicBoundaryAssociation> parentBoundaryAssociations = new HashSet<GeographicBoundaryAssociation>();
    //private Set<GeographicBoundaryAssociation> childBoundaryAssociations = new HashSet<GeographicBoundaryAssociation>();
    private Set<HealthCareLicense> licenses = new HashSet<HealthCareLicense>();

    public GeographicBoundary()
    {
    }

    public GeographicBoundary(String name, GeographicBoundaryType type)
    {
        this.name = name;
        this.type = type;
    }

    @Id(generate = GeneratorType.AUTO)
    public final Long getGeoId()
    {
        return geoId;
    }

    protected final void setGeoId(final Long geoId)
    {
        this.geoId = geoId;
    }

    @Column(length = 100, nullable = false)
    public final String getName()
    {
        return name;
    }

    public final void setName(final String name)
    {
        this.name = name;
        if (this.abbreviation == null)
            this.abbreviation = name;
    }

    @Column(length = 10)
    public final String getAbbreviation()
    {
        return abbreviation;
    }

    public final void setAbbreviation(final String abbreviation)
    {
        this.abbreviation = abbreviation;
    }

    @ManyToOne
    @JoinColumn(name = "geo_boundary_type_id", nullable = false)
    public GeographicBoundaryType getType()
    {
        return type;
    }

    public void setType(final GeographicBoundaryType type)
    {
        this.type = type;
    }

    @OneToMany
    @JoinColumn(name = "geo_id")
    public Set<PostalAddressBoundary> getPostalAddressBoundary()
    {
        return postalAddressBoundary;
    }

    public void setPostalAddressBoundary(final Set<PostalAddressBoundary> postalAddressBoundary)
    {
        this.postalAddressBoundary = postalAddressBoundary;
    }

    @OneToMany
    @JoinColumn(name = "geo_id")        
    public Set<HealthCareLicense> getLicenses()
    {
        return licenses;
    }

    public void setLicenses(Set<HealthCareLicense> licenses)
    {
        this.licenses = licenses;
    }

    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof GeographicBoundary))
            return false;
        else
            return getGeoId().equals(((GeographicBoundary) obj).getGeoId());
    }
}
