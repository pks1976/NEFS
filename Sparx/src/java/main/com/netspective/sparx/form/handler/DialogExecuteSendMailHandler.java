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
 * @author Shahid N. Shah
 */

/**
 * $Id: DialogExecuteSendMailHandler.java,v 1.1 2003-08-31 03:11:00 shahid.shah Exp $
 */

package com.netspective.sparx.form.handler;

import java.io.Writer;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

import javax.mail.Transport;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.MessagingException;
import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.AddressException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.template.TemplateProcessor;
import com.netspective.sparx.template.freemarker.FreeMarkerTemplateProcessor;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.source.StaticValueSource;

public class DialogExecuteSendMailHandler extends DialogExecuteDefaultHandler
{
    private static final Log log = LogFactory.getLog(DialogExecuteSendMailHandler.class);
    private static ValueSource LOCAL_HOST = new StaticValueSource("localhost");

    protected static class Header
    {
        private String name;
        private ValueSource value;

        public Header()
        {
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public ValueSource getValue()
        {
            return value;
        }

        public void setValue(ValueSource value)
        {
            this.value = value;
        }
    }

    protected static class Headers
    {
        private List headers = new ArrayList();

        public Headers()
        {
        }

        public List getHeaders()
        {
            return headers;
        }

        public Header createHeader()
        {
            return new Header();
        }

        public void addHeader(Header header)
        {
            headers.add(header);
        }
    }

    private ValueSource host = LOCAL_HOST;
    private Headers headers;
    private ValueSource from;
    private ValueSource to;
    private ValueSource replyTo;
    private ValueSource cc;
    private ValueSource bcc;
    private ValueSource subject;
    private TemplateProcessor body;

    public DialogExecuteSendMailHandler()
    {
    }

    public ValueSource getBcc()
    {
        return bcc;
    }

    public void setBcc(ValueSource bcc)
    {
        this.bcc = bcc;
    }

    public ValueSource getCc()
    {
        return cc;
    }

    public void setCc(ValueSource cc)
    {
        this.cc = cc;
    }

    public ValueSource getFrom()
    {
        return from;
    }

    public void setFrom(ValueSource from)
    {
        this.from = from;
    }

    public ValueSource getReplyTo()
    {
        return replyTo;
    }

    public void setReplyTo(ValueSource replyTo)
    {
        this.replyTo = replyTo;
    }

    public ValueSource getHost()
    {
        return host;
    }

    public void setHost(ValueSource host)
    {
        this.host = host;
    }

    public ValueSource getTo()
    {
        return to;
    }

    public void setTo(ValueSource to)
    {
        this.to = to;
    }

    public ValueSource getSubject()
    {
        return subject;
    }

    public void setSubject(ValueSource subject)
    {
        this.subject = subject;
    }

    public TemplateProcessor createBody()
    {
        return new FreeMarkerTemplateProcessor();
    }

    public TemplateProcessor getBody()
    {
        return body;
    }

    public void addBody(TemplateProcessor templateProcessor)
    {
        body = templateProcessor;
    }

    public Headers createHeaders()
    {
        return new Headers();
    }

    public void addHeaders(Headers headers)
    {
        this.headers = headers;
    }

    protected Address[] getAddresses(Value value) throws AddressException, MessagingException
    {
        if(value.isListValue())
        {
            String[] addressTexts = value.getTextValues();
            Address[] result = new Address[addressTexts.length];
            for(int i = 0; i < addressTexts.length; i++)
                result[i] = new InternetAddress(addressTexts[i]);
            return result;
        }
        else
            return new InternetAddress[] { new InternetAddress(value.getTextValue()) };
    }

    public void executeDialog(Writer writer, DialogContext dc) throws IOException, DialogExecuteException
    {
        if(from == null)
        {
            dc.getDialog().getLog().error("No FROM address provided.");
            return;
        }

        if(to == null && cc == null && bcc == null)
        {
            dc.getDialog().getLog().error("No TO, CC, or BCC addresses provided.");
            return;
        }

        Properties props = System.getProperties();
        props.put("mail.smtp.host", host.getTextValue(dc));

        Session mailSession = Session.getDefaultInstance(props, null);

        try
        {
            MimeMessage message = new MimeMessage(mailSession);

            if(headers != null)
            {
                List headersList = headers.getHeaders();
                for(int i = 0; i < headersList.size(); i++)
                {
                    Header header = (Header) headersList.get(i);
                    message.setHeader(header.getName(), header.getValue().getTextValue(dc));
                }
            }

            message.setFrom(new InternetAddress(from.getTextValue(dc)));

            if(replyTo != null)
                message.setReplyTo(getAddresses(replyTo.getValue(dc)));

            if(to != null)
                message.setRecipients(Message.RecipientType.TO, getAddresses(to.getValue(dc)));

            if(cc != null)
                message.setRecipients(Message.RecipientType.CC, getAddresses(cc.getValue(dc)));

            if(bcc != null)
                message.setRecipients(Message.RecipientType.BCC, getAddresses(bcc.getValue(dc)));

            if(subject != null)
                message.setSubject(subject.getTextValue(dc));

            if(body != null)
            {
                StringWriter messageText = new StringWriter();
                body.process(messageText, dc, null);
                message.setText(messageText.toString());
            }

            Transport.send(message);
        }
        catch (MessagingException e)
        {
            log.error("unable to send e-mail", e);
            throw new DialogExecuteException(e);
        }
    }
}
