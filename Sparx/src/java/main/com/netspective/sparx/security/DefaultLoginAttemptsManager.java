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
 * $Id: DefaultLoginAttemptsManager.java,v 1.4 2003-11-19 02:27:32 shahid.shah Exp $
 */

package com.netspective.sparx.security;

import java.io.Writer;
import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.ServletRequest;

import com.netspective.commons.template.TemplateProcessor;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.StaticValueSource;

public class DefaultLoginAttemptsManager implements HttpLoginAttemptsManager
{
    public static final String SESSATTRNAME_LOGIN_ATTEMPT_COUNT = DefaultLoginAttemptsManager.class.getName() + ".LOGIN_ATTEMPT_COUNT";
    public static final String SESSATTRNAME_MAX_LOGIN_ATTEMPTS_EXCEEDED = DefaultLoginAttemptsManager.class.getName() + ".MAX_ATTEMPTS_EXCEEDED";
    public static final ValueSource DEFAULT_MAX_ATTEMPTS_EXCEEDED_MESSAGE = new StaticValueSource("Maximum number of login attempts exceeded.");

    private ValueSource maxLoginAttemptsExceededMessage = DEFAULT_MAX_ATTEMPTS_EXCEEDED_MESSAGE;
    private int maxLoginAttempts = 5;
    private TemplateProcessor denialBody;

    public int getMaxLoginAttempts()
    {
        return maxLoginAttempts;
    }

    public void setMaxLoginAttempts(int maxLoginAttempts)
    {
        this.maxLoginAttempts = maxLoginAttempts;
    }

    public ValueSource getMaxLoginAttemptsExceededMessage()
    {
        return maxLoginAttemptsExceededMessage;
    }

    public void setMaxLoginAttemptsExceededMessage(ValueSource maxLoginAttemptsExceededMessage)
    {
        this.maxLoginAttemptsExceededMessage = maxLoginAttemptsExceededMessage;
    }

    public TemplateProcessor createBody()
    {
        return new com.netspective.sparx.template.freemarker.FreeMarkerTemplateProcessor();
    }

    public void addBody(TemplateProcessor templateProcessor)
    {
        denialBody = templateProcessor;
    }

    public TemplateProcessor getDenialBody()
    {
        return denialBody;
    }

    public boolean allowLoginAttempt(HttpLoginManager loginManager, LoginDialogContext loginDialogContext)
    {
        HttpSession session = loginDialogContext.getHttpRequest().getSession();
        if(session.getAttribute(SESSATTRNAME_MAX_LOGIN_ATTEMPTS_EXCEEDED) != null)
            return false;

        Integer attemptCountTemp = (Integer) session.getAttribute(SESSATTRNAME_LOGIN_ATTEMPT_COUNT);
        int attemptCount = (attemptCountTemp == null ? 0 : attemptCountTemp.intValue()) + 1;

        // only store the attempt count increase if the dialog is past "input" mode (meaning it was submitted)
        if(loginDialogContext.getDialogState().getRunSequence() > 1)
        {
            Integer saveAttemptCount = new Integer(attemptCount);

            // make sure that we don't double-count the login attempt -- once we have stored the count for this
            // request we save the counter in a request attribute
            final ServletRequest request = loginDialogContext.getRequest();
            if(request.getAttribute(SESSATTRNAME_LOGIN_ATTEMPT_COUNT) == null)
            {
                request.setAttribute(SESSATTRNAME_LOGIN_ATTEMPT_COUNT, saveAttemptCount);
                session.setAttribute(SESSATTRNAME_LOGIN_ATTEMPT_COUNT, saveAttemptCount);
            }
        }

        if(attemptCount > getMaxLoginAttempts())
        {
            maxLoginAttemptsExceeeded(loginManager, loginDialogContext);
            return false;
        }

        return true;
    }

    public void maxLoginAttemptsExceeeded(HttpLoginManager loginManager, LoginDialogContext loginDialogContext)
    {
        loginDialogContext.getHttpRequest().getSession().setAttribute(SESSATTRNAME_MAX_LOGIN_ATTEMPTS_EXCEEDED, Boolean.TRUE);
    }

    public void renderLoginAttemptDeniedHtml(Writer writer, HttpLoginManager loginManager, LoginDialogContext loginDialogContext) throws IOException
    {
        if(denialBody != null)
            denialBody.process(writer, loginDialogContext.getNavigationContext(), null);
        else
            writer.write("<p>&nbsp;<p>&nbsp;<p>&nbsp;<p>&nbsp;<center>You have exceeded your maximum login attempts.</center>");
    }
}
