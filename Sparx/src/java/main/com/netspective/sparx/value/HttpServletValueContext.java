/*
 * Copyright (c) 2000-2004 Netspective Communications LLC. All rights reserved.
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
 *    used to endorse or appear in products derived from The Software without written consent of Netspective.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF IT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 */
package com.netspective.sparx.value;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.netspective.commons.activity.Activity;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogsManager;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.security.HttpLoginManager;
import com.netspective.sparx.theme.Theme;

public interface HttpServletValueContext extends ServletValueContext, Activity
{
    public DialogsManager getDialogsManager();

    /**
     * Retrieve the active servlet (page scope).
     */
    public HttpServlet getHttpServlet();

    /**
     * Retrive the active servlet request (request scope).
     */
    public HttpServletRequest getHttpRequest();

    /**
     * Retrive the active servlet response.
     */
    public HttpServletResponse getHttpResponse();

    public HttpLoginManager getActiveLoginManager();

    public Theme getActiveTheme();

    public String getRootUrl();

    public String getServletRootUrl();

    /**
     * Return the active navigation context if available
     */
    public NavigationContext getNavigationContext();

    public void setNavigationContext(NavigationContext navigationContext);

    /**
     * Return the active dialog context if available
     */
    public DialogContext getDialogContext();

    public void setDialogContext(DialogContext dialogContext);

    /**
     * Take the given URL and ensure that the current page's retain params are added to it
     *
     * @param url The complete URL to use
     *
     * @return The given url plus any of our current page's retin params
     */
    public String constructAppUrl(String url);

    /**
     * Redirect the page to another page. This ends up calling the response sendRedirect() but also sets a flag
     * so that the navigation system knows about it.
     *
     * @param url The URL to redirect to (it is automatically encoded).
     */
    public void sendRedirect(String url) throws IOException;

    /**
     * Ascertain whether the sendRedirect method was called
     *
     * @return True if send redirect was called, false if no redirection occurred
     */
    public boolean isRedirected();
}
