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
package com.netspective.medigy.model.invoice;

import com.netspective.medigy.model.DbUnitTestCase;
import com.netspective.medigy.model.TestCase;
import com.netspective.medigy.model.party.Party;
import com.netspective.medigy.reference.custom.invoice.InvoiceStatusType;
import com.netspective.medigy.reference.custom.invoice.InvoiceTermType;
import com.netspective.medigy.util.HibernateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

public class TestInvoice  extends DbUnitTestCase
{
    private static final Log log = LogFactory.getLog(TestInvoice.class);

    public void testInvoice()
    {
        final Invoice invoice = new Invoice();
        invoice.setDescription("New invoice");

        final InvoiceTermType termType = new InvoiceTermType();
        termType.setCode("PAYMENT");
        termType.setLabel("Payment - net days");
        termType.setParty(Party.Cache.SYS_GLOBAL_PARTY.getEntity());
        HibernateUtil.getSession().save(termType);

        final InvoiceTerm term = new InvoiceTerm();
        term.setInvoice(invoice);
        term.setType(termType);
        term.setTermValue(new Long(30));
        invoice.getInvoiceTerms().add(term);
        HibernateUtil.getSession().save(invoice);
        HibernateUtil.closeSession();
        
        final Invoice newInvoice = (Invoice) HibernateUtil.getSession().load(Invoice.class, invoice.getInvoiceId());
        assertEquals(1, newInvoice.getInvoiceTerms().size());
        InvoiceTerm savedInvoiceTerm = (InvoiceTerm) newInvoice.getInvoiceTerms().toArray()[0];
        assertEquals(savedInvoiceTerm.getTermValue(), new Long(30));
        assertEquals(savedInvoiceTerm.getInvoice().getInvoiceId(), invoice.getInvoiceId());

        final InvoiceStatus status = new InvoiceStatus();
        status.setType(InvoiceStatusType.Cache.SENT.getEntity());
        status.setDate(new Date());
        status.setInvoice(newInvoice);
        newInvoice.getInvoiceStatuses().add(status);

        final InvoiceStatus voidStatus = new InvoiceStatus();
        voidStatus.setType(InvoiceStatusType.Cache.VOID.getEntity());
        voidStatus.setDate(new Date());
        voidStatus.setInvoice(newInvoice);
        newInvoice.getInvoiceStatuses().add(voidStatus);
 
        HibernateUtil.getSession().flush();
        HibernateUtil.closeSession();

        final Long invoiceId = newInvoice.getInvoiceId();
        Invoice savedInvoice = (Invoice) HibernateUtil.getSession().load(Invoice.class, invoiceId);
        assertEquals("New invoice", savedInvoice.getDescription());
        log.info("VALID: Invoice");
        assertEquals(2, savedInvoice.getInvoiceStatuses().size());
        log.info("VALID: Invoice status count");
        //assertEquals(voidStatus.getInvoiceStatusId(), savedInvoice.getCurrentInvoiceStatus().getInvoiceStatusId());
        assertEquals(savedInvoice.getCurrentInvoiceStatus().getType(), InvoiceStatusType.Cache.VOID.getEntity());
        log.info("VALID: Current Invoice status type");
        assertEquals(1, savedInvoice.getInvoiceTerms().size());
        log.info("VALID: Invoice term count");
        assertEquals(((InvoiceTerm) savedInvoice.getInvoiceTerms().toArray()[0]).getType(), termType);
        log.info("VALID: Invoice term type");
        HibernateUtil.closeSession();
    }

    @Override
    public String getDataSetFile()
    {
        return "/com/netspective/medigy/model/invoice/TestInvoice.xml";
    }
}
