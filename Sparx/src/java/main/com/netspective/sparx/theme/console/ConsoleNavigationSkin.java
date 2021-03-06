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
package com.netspective.sparx.theme.console;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.netspective.commons.security.AuthenticatedUser;
import com.netspective.commons.value.ValueSource;
import com.netspective.sparx.ProjectComponent;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.navigate.NavigationControllerServlet;
import com.netspective.sparx.navigate.NavigationPage;
import com.netspective.sparx.navigate.NavigationPath;
import com.netspective.sparx.navigate.NavigationPathFlags;
import com.netspective.sparx.navigate.NavigationSkin;
import com.netspective.sparx.navigate.NavigationTree;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.theme.basic.AbstractThemeSkin;
import com.netspective.sparx.util.HttpUtils;

public class ConsoleNavigationSkin extends AbstractThemeSkin implements NavigationSkin
{
    static public final String HEADING_ACTION_IMAGE = "action-icon";

    private boolean renderPageIconsWithHeading = true;
    private boolean renderSidebarAsAnchors;
    private int sidebarWidth = 125;
    private boolean showAuthenticatedUser = true;
    private boolean showErrorHeader;
    private Resources resources = createResources();

    public ConsoleNavigationSkin(Theme theme, String name)
    {
        super(theme, name);

        resources.registerThemeStyleSheet("/css/general.css");
        resources.registerThemeStyleSheet("/css/navigation.css");
        resources.registerThemeStyleSheet("/css/panel-input.css");
        resources.registerThemeStyleSheet("/css/panel-output.css");
        resources.registerThemeStyleSheet("/css/panel-content-dialog.css");
        resources.registerThemeStyleSheet("/css/panel-content-report.css");
        resources.registerThemeStyleSheet("/css/panel-content-text.css");
        resources.registerThemeStyleSheet("/css/syntax-highlight.css");
        resources.registerThemeStyleSheet("/jscalendar-0.9.6/calendar-win2k-1.css");
        resources.registerThemeStyleSheet("/css/docbook.css");
        resources.registerThemeStyleSheet("/css/capxous.css");

        resources.registerThemeScript("/scripts/panel.js");
        resources.registerThemeScript("/scripts/dialog.js");
        resources.registerThemeScript("/scripts/popup.js");
    }

    public boolean isRenderSidebarAsAnchors()
    {
        return renderSidebarAsAnchors;
    }

    public void setRenderSidebarAsAnchors(boolean renderSidebarAsAnchors)
    {
        this.renderSidebarAsAnchors = renderSidebarAsAnchors;
    }

    public boolean isRenderPageIconsWithHeading()
    {
        return renderPageIconsWithHeading;
    }

    public void setRenderPageIconsWithHeading(boolean renderPageIconsWithHeading)
    {
        this.renderPageIconsWithHeading = renderPageIconsWithHeading;
    }

    public int getSidebarWidth()
    {
        return sidebarWidth;
    }

    public void setSidebarWidth(int sidebarWidth)
    {
        this.sidebarWidth = sidebarWidth;
    }

    public boolean isShowAuthenticatedUser()
    {
        return showAuthenticatedUser;
    }

    public void setShowAuthenticatedUser(boolean showAuthenticatedUser)
    {
        this.showAuthenticatedUser = showAuthenticatedUser;
    }

    public boolean isShowErrorHeader()
    {
        return showErrorHeader;
    }

    public void setShowErrorHeader(boolean showErrorHeader)
    {
        this.showErrorHeader = showErrorHeader;
    }

    public NavigationContext createContext(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response, NavigationTree tree, String navTreeId)
    {
        return new NavigationContext(tree, servlet, request, response, this, navTreeId);
    }

    public Resources createResources()
    {
        return new Resources();
    }

    public Resources getResources()
    {
        return resources;
    }

    public void addResources(Resources resources)
    {
        // not really required, just there for XDM
    }

    public void renderPageMetaData(Writer writer, NavigationContext nc) throws IOException
    {
        NavigationPage activePage = nc.getActivePage();

        writer.write("<!-- Application Header Begins -->\n");
        writer.write("<html>\n");
        writer.write("<head>\n");
        writer.write("<title>" + (activePage != null ? nc.getPageTitle() : "") + "</title>\n");

        final List resources = this.resources.getResources();

        for(int r = 0; r < resources.size(); r++)
            ((Resource) resources.get(r)).render(writer, nc);

        if(this.resources != null)

        if(activePage.getCustomCssFile() != null)
                writer.write("	<link rel=\"stylesheet\" href=\"" + activePage.getCustomCssFile().getTextValue(nc) + "\" type=\"text/css\">\n");
        if(activePage.getCustomJsFile() != null)
            writer.write("  <script src=\"" + activePage.getCustomJsFile().getTextValue(nc) + "\" language=\"JavaScript1.2\"></script>\n");
        writer.write("</head>\n");
    }

    /**
     * Render the authenticated user information and the logout navigation link
     */
    public void renderAuthenticatedUser(Writer writer, NavigationContext nc) throws IOException
    {
        AuthenticatedUser authUser = nc.getAuthenticatedUser();

        String personId = authUser != null ? authUser.getUserId().toString() : "Not logged in";
        String personName = authUser != null ? authUser.getUserName() : "Not logged in";

        if(authUser != null && authUser.isRemembered())
            personName += " (remembered)";

        Theme theme = getTheme();

        writer.write("<!-- Active User Begins -->\n");
        writer.write("<table class=\"active-user-table\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
        writer.write("<tr>\n");
        writer.write("	<td><img src=\"" + theme.getResourceUrl("/images/spacer.gif") + "\" alt=\"\" height=\"100%\" width=\"10\" border=\"0\"></td>\n");
        writer.write("	<td valign=\"middle\" nowrap >\n");
        writer.write("		<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
        writer.write("			<tr>\n");
        writer.write("				<td class=\"active-user-anchor\"><img class=\"active-user-anchor\" src=\"" + theme.getResourceUrl("/images/spacer.gif") + "\" alt=\"\" " +
                     "height=\"100%\" width=\"100%\" border=\"0\"></td>\n");
        writer.write("				<td nowrap><span class=\"active-user-heading\">&nbsp;User&nbsp;</span></td>\n");
        writer.write("				<td nowrap><a class=\"active-user\" href=\"" + nc.getRootUrl() + "/person/summary.jsp?person_id=" + personId + "\">&nbsp;&nbsp;" +
                     personName + "</a></td>\n");
        writer.write("			</tr>\n");
        writer.write("		</table>\n");
        writer.write("	</td>\n");

        ProjectComponent projectManager = nc.getProjectComponent();
        boolean haveErrors = projectManager.hasErrorsOrWarnings();

        writer.write("	<td><img src=\"" + theme.getResourceUrl("/images/spacer.gif") + "\" alt=\"\" height=\"100%\" width=\"20\" border=\"0\"></td>\n");
        writer.write(haveErrors ? "	<td>\n" : "	<td width=100%>\n");
        writer.write("		<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
        writer.write("			<tr>\n");
        writer.write("				<td class=\"active-user-anchor\"><img class=\"active-user-anchor\" src=\"" + theme.getResourceUrl("/images/spacer.gif") + "\" alt=\"\" height=\"100%\" width=\"100%\" border=\"0\"></td>\n");
        writer.write("				<td nowrap><span class=\"active-user-heading\">&nbsp;App&nbsp;</span></td>\n");
        writer.write("				<td nowrap><a class=\"active-user\" href=\"" + nc.getHttpRequest().getContextPath() + "\">&nbsp;&nbsp;" +
                     nc.getHttpRequest().getContextPath() + " (" + nc.getHttpServlet().getServletContext().getServerInfo() + ")</a></td>\n");
        writer.write("			</tr>\n");
        writer.write("		</table>\n");
        writer.write("	</td>\n");

        if(haveErrors)
        {
            int errorsCount = projectManager.getErrors().size() + projectManager.getWarnings().size();

            writer.write("	<td><img src=\"" + theme.getResourceUrl("/images/spacer.gif") + "\" alt=\"\" height=\"100%\" width=\"20\" border=\"0\"></td>\n");
            writer.write("	<td width=\"100%\">\n");

            writer.write("		<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
            writer.write("			<tr>\n");
            writer.write("				<td class=\"error-alert-anchor\"><img class=\"error-alert-anchor\" src=\"" + theme.getResourceUrl("/images/spacer.gif") + "\" alt=\"\" height=\"100%\" width=\"100%\" border=\"0\"></td>\n");
            writer.write("				<td nowrap><a class=\"error-alert\" href=\"" + nc.getServletRootUrl() + "/project/input-source#errors\"><span class=\"error-alert-heading\">&nbsp;Errors/Warnings&nbsp;</span></a></td>\n");
            writer.write("				<td nowrap><a class=\"error-alert\" href=\"" + nc.getServletRootUrl() + "/project/input-source#errors\">&nbsp;&nbsp;" +
                         errorsCount + "</a></td>\n");
            writer.write("			</tr>\n");
            writer.write("		</table>\n");
            writer.write("	</td>\n");
        }

        writer.write("	<td nowrap width=\"50\" >\n");
        writer.write("		<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
        writer.write("			<tr>\n");
        writer.write("				<td class=\"active-user-anchor\"><img class=\"active-user-anchor\" src=\"" +
                     theme.getResourceUrl("/images/spacer.gif") + "\" alt=\"\" height=\"100%\" width=\"100%\" border=\"0\"></td>\n");
        writer.write("				<td nowrap><span class=\"active-user-heading\">&nbsp;Action&nbsp;</span></td>\n");
        writer.write("				<td nowrap><a class=\"active-user\" href=\"" + nc.getRootUrl() + "/console?_logout=yes\">&nbsp;&nbsp;Logout&nbsp;</a></td>\n");
        writer.write("			</tr>\n");
        writer.write("		</table>\n");
        writer.write("	</td>\n");
        writer.write("	<td><img src=\"" + theme.getResourceUrl("/images/spacer.gif") + "\" alt=\"\" height=\"100%\" width=\"20\" border=\"0\"></td>\n");
        writer.write("</tr>\n");
        writer.write("</table>\n");

        writer.write("<!-- Active User Ends -->\n");
    }

    public void renderPageMasthead(Writer writer, NavigationContext nc) throws IOException
    {
        writer.write("<body leftmargin=\"0\" marginheight=\"0\" marginwidth=\"0\" topmargin=\"0\" onload=\"initializeBody()\">\n");
        ValueSource baseAttrs = nc.getActivePage().getBaseAttributes();
        if(baseAttrs != null)
            writer.write("<base " + baseAttrs.getTextValue(nc) + "></base>");

        if(isShowAuthenticatedUser())
            renderAuthenticatedUser(writer, nc);

        Theme theme = getTheme();

        writer.write("<!-- Master Header Begins -->\n");

        writer.write("<table class=\"masthead\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
        writer.write("	<tr>\n");
        writer.write("	<td class=\"masthead-left\" align=\"left\" ><img src=\"" + theme.getResourceUrl("/images/spacer.gif") + "\" alt=\"\" height=\"65\" width=\"351\" border=\"0\"></td>\n");
        writer.write("	<td class=\"masthead-top-right\" align=\"right\" ><img src=\"" + theme.getResourceUrl("/images/spacer.gif") + "\" alt=\"\" height=\"65\" width=\"244\" border=\"0\"></td>\n");
        writer.write("  </tr>\n");
        writer.write("</table>\n");

        writer.write("<table class=\"masthead-bottom\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
        writer.write("	<tr>\n");
        writer.write("	    <td valign=\"bottom\">\n");
        writer.write("	        <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
        writer.write("<!-- Masthead Ends -->\n");
        writer.write("              <tr>\n");
        writer.write("                  <td align=\"left\" valign=\"bottom\">\n");
    }

    /**
     * Render the Level one menu
     */
    public void renderPageMenusLevelOne(Writer writer, NavigationContext nc) throws IOException
    {
        Theme theme = getTheme();

        writer.write("<!-- Level 1 Begins -->\n");
        NavigationPath activePath = nc.getActivePage();
        String html = generateLevelOneHtml(activePath.getLevel() == 1
                                           ? activePath
                                           : (NavigationPath) nc.getActivePage().getAncestorsList().get(1), nc);
        writer.write(html);
        writer.write("<!-- Level 1 Ends -->\n");

        writer.write("                    </td>\n");
        writer.write("				</tr>\n");
        writer.write("			</table>\n");
        writer.write("		</td>\n");
        writer.write("		<td class=\"masthead-right\" align=\"right\" valign=\"bottom\" width=\"100%\">" +
                     "<img src=\"" + theme.getResourceUrl("/images/spacer.gif") + "\" width=\"100%\" height=\"18\"></td>\n");
        writer.write("	</tr>\n");
        writer.write("</table>\n");
    }

    /**
     * Generates the level two HTML
     */
    public void renderPageMenusLevelTwo(Writer writer, NavigationContext nc) throws IOException
    {
        NavigationPath activePath = nc.getActivePage();
        if(activePath == null)
            return;

        switch(activePath.getLevel())
        {
            case 1:
                List activePathChildren = activePath.getChildrenList();
                if(activePath.getMaxChildLevel() > 1 && activePathChildren.size() > 0)
                {
                    writer.write(generateLevelTwoHtml((NavigationPath) activePath.getChildrenList().get(0), nc));
                }
                else
                {
                    // even if there are no level two menu items display the level two background bar
                    writer.write("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" height=\"20\">");
                    writer.write("	<tr>");
                    writer.write("	<td class=\"menu-level-2-table\" align=\"left\" valign=\"middle\">");
                    writer.write("	</td>");
                    writer.write("	</tr>");
                    writer.write("</table>");
                }
                break;

            case 2:
                writer.write(generateLevelTwoHtml(activePath, nc));
                break;

            case 3:
            case 4:
                writer.write(generateLevelTwoHtml((NavigationPath) activePath.getAncestorsList().get(2), nc));
                break;

        }
        writer.write("<!-- Level Two Ends -->");
    }

    public void renderPageMenusLevelThreeAsVerticalAnchors(Writer writer, NavigationContext nc) throws IOException
    {
        NavigationPath activePath = nc.getActivePage();
        if(activePath == null)
            return;

        if(activePath.getLevel() > 2)
        {
            final List ancestorsList = activePath.getAncestorsList();
            if(ancestorsList.size() > 2)
            {
                NavigationPath level3Root = (NavigationPath) ancestorsList.get(2);
                List activePathChildren = level3Root.getChildrenList();
                if(activePath.getMaxChildLevel() > 2 && activePathChildren.size() > 0)
                {
                    StringWriter sw = new StringWriter();
                    int rendered = renderVerticalCssMenus(sw, nc, activePathChildren, true);
                    if(rendered > 1)
                    {
                        writer.write("      <td class=\"level-3-vertical-menu-container\" align=\"left\" valign=\"top\">\n");
                        writer.write(sw.toString());
                        writer.write("      </td>\n");
                    }
                }
            }
        }
    }

    /**
     * @param writer
     * @param nc
     *
     * @throws IOException
     */
    public void renderPageMenusLevelThree(Writer writer, NavigationContext nc) throws IOException
    {
        if(renderSidebarAsAnchors)
        {
            renderPageMenusLevelThreeAsVerticalAnchors(writer, nc);
            return;
        }

        NavigationPath activePath = nc.getActivePage();
        if(activePath == null)
            return;

        Theme theme = getTheme();
        switch(activePath.getLevel())
        {
            case 2:
                List activePathChildren = activePath.getChildrenList();
                if(activePath.getMaxChildLevel() > 2 && activePathChildren.size() > 0)
                {
                    writer.write("  <tr>\n");
                    writer.write("      <td class=\"menu-level-3-separator\" align=\"left\" valign=\"top\" width=\"" + sidebarWidth + "\">" +
                                 "<img src=\"" + theme.getResourceUrl("/images/spacer-big.gif") + "\" alt=\"\" width=\"" + sidebarWidth + "\" height=\"12\" border=\"0\"></td>\n");
                    writer.write("      <td class=\"body-top-left\" width=\"12\"><img src=\"" + theme.getResourceUrl("/images/spacer-big.gif") + "\" " +
                                 "alt=\"\" width=\"12\" height=\"12\" border=\"0\"></td>\n");
                    writer.write("      <td align=\"left\" valign=\"top\"><img src=\"" + theme.getResourceUrl("/images/spacer-big.gif") + "\" alt=\"\" height=\"12\" width=\"100%\" border=\"0\"></td>\n");
                    writer.write("  </tr>\n");
                    writer.write("  <tr>\n");
                    writer.write("      <td class=\"menu-table\" align=\"left\" valign=\"top\" width=\"" + sidebarWidth + "\" height=\"100%\">\n");
                    writer.write(generateLevelThreeHtml((NavigationPath) activePath.getChildrenList().get(0), nc));
                    writer.write("      </td>\n");
                }
                break;

            case 3:
                writer.write("  <tr>\n");
                writer.write("      <td class=\"menu-level-3-separator\" align=\"left\" valign=\"top\" width=\"" + sidebarWidth + "\">" +
                             "<img src=\"" + theme.getResourceUrl("/images/spacer-big.gif") + "\" alt=\"\" width=\"" + sidebarWidth + "\" height=\"12\" border=\"0\"></td>\n");
                writer.write("      <td class=\"body-top-left\" width=\"12\"><img src=\"" + theme.getResourceUrl("/images/spacer-big.gif") + "\" " +
                             "alt=\"\" width=\"12\" height=\"12\" border=\"0\"></td>\n");
                writer.write("      <td align=\"left\" valign=\"top\"><img src=\"" + theme.getResourceUrl("/images/spacer-big.gif") + "\" alt=\"\" height=\"12\" width=\"100%\" border=\"0\"></td>\n");
                writer.write("  </tr>\n");
                writer.write("  <tr>\n");
                writer.write("      <td class=\"menu-table\" align=\"left\" valign=\"top\" width=\"" + sidebarWidth + "\" height=\"100%\">\n");
                writer.write(generateLevelThreeHtml(activePath, nc));
                writer.write("      </td>\n");
                break;

            case 4:
                writer.write("  <tr>\n");
                writer.write("      <td class=\"menu-level-3-separator\" align=\"left\" valign=\"top\" width=\"" + sidebarWidth + "\">" +
                             "<img src=\"" + theme.getResourceUrl("/images/spacer-big.gif") + "\" alt=\"\" width=\"" + sidebarWidth + "\" height=\"12\" border=\"0\"></td>\n");
                writer.write("      <td class=\"body-top-left\" width=\"12\"><img src=\"" + theme.getResourceUrl("/images/spacer-big.gif") + "\" " +
                             "alt=\"\" width=\"12\" height=\"12\" border=\"0\"></td>\n");
                writer.write("      <td align=\"left\" valign=\"top\"><img src=\"" + theme.getResourceUrl("/images/spacer-big.gif") + "\" alt=\"\" height=\"12\" width=\"100%\" border=\"0\"></td>\n");
                writer.write("  </tr>\n");
                writer.write("  <tr>\n");
                writer.write("      <td class=\"menu-table\" align=\"left\" valign=\"top\" width=\"" + sidebarWidth + "\" height=\"100%\">\n");
                writer.write(generateLevelThreeHtml((NavigationPath) activePath.getAncestorsList().get(3), nc));
                writer.write("      </td>\n");
                break;

            default:
                writer.write("  <tr>\n");
                writer.write("      <td colspan=\"3\"><img src=\"" + theme.getResourceUrl("/images/spacer-big.gif") + "\" " +
                             "alt=\"\" width=\"100%\" height=\"12\" border=\"0\"></td>\n");
                writer.write("  </tr>\n");
                writer.write("  <tr>\n");
                break;

        }
    }

    /**
     * @param writer
     * @param nc
     *
     * @throws IOException
     */
    public void renderPageHeader(Writer writer, NavigationContext nc) throws IOException
    {
        // in case any errors or messages need to appear, they'll show up at the top of our app
        if(isShowErrorHeader())
            HttpUtils.renderDevelopmentEnvironmentHeader(writer, nc);

        if(nc.getActiveState().getFlags().flagIsSet(NavigationPage.Flags.IS_POPUP_MODE | NavigationPage.Flags.IS_PRINT_MODE))
            return;

        renderPageMasthead(writer, nc);
        renderPageMenusLevelOne(writer, nc);
        renderPageMenusLevelTwo(writer, nc);
        renderPageHeading(writer, nc);

        writer.write("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" height=\"100%\">\n");

        renderPageMenusLevelThree(writer, nc);

        writer.write("      <td align=\"left\" valign=\"top\" width=\"12\" height=\"100%\"></td>\n");
        writer.write("      <td align=\"left\" valign=\"top\">\n");
        writer.write("          <div class=\"page-body-content\">\n");
    }

    /**
     * Renders the page heading if one exists
     */
    protected void renderPageHeading(Writer writer, NavigationContext nc) throws IOException
    {
        Theme theme = getTheme();

        NavigationPage page = nc.getActivePage();
        String heading = page != null ? nc.getPageHeading() : "-";

        if(!heading.equals("-"))
        {
            String pageHeadingImageUrl = renderPageIconsWithHeading ? theme.getResourceUrl("/images/page-icons" + (page != null
                                                                                                                   ? (page.getQualifiedName() + "/page-heading.gif")
                                                                                                                   : "/page-heading.gif"))
                                         : null;

            writer.write("<!-- Page Header Begins -->\n");
            writer.write("<table class=\"page-heading-table\" height=\"36\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
            writer.write("    <tr>\n");
            writer.write("        <td align=\"left\" valign=\"middle\">\n");
            writer.write("            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
            writer.write("                <tr>\n");
            if(pageHeadingImageUrl != null)
                writer.write("                    <td align=\"right\" class=\"page-heading-icon\"><img class=\"page-icon\" src=\"" +
                             pageHeadingImageUrl + "\" alt=\"\" height=\"22\" width=\"22\" border=\"0\"></td>\n");
            writer.write("                    <td class=\"page-heading\">" + heading + "</td>\n");
            writer.write("                </tr>\n");
            writer.write("            </table>\n");
            writer.write("        </td>\n");
            writer.write("    </tr>\n");
            renderPageSubHeading(writer, nc);
            writer.write("</table>\n");
            writer.write("<!-- Page Header Ends -->\n");
        }
    }

    /**
     * Render the sub heading in the page content
     */
    protected void renderPageSubHeading(Writer writer, NavigationContext nc) throws IOException
    {
        NavigationPage page = nc.getActivePage();
        if(page == null)
            return;

        String subHeading = nc.getPageSubheading();
        if(subHeading != null && subHeading.length() > 0)
        {
            writer.write("    <tr>\n");
            writer.write("        <td class=\"page-sub-heading-table\" align=\"left\" valign=\"top\">\n");

            writer.write("            <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
            writer.write("                <tr>\n");
            writer.write("                    <td align=\"left\" valign=\"top\">\n");
            writer.write("                        <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
            writer.write("                            <tr>\n");
            writer.write("                                <td align=\"left\" valign=\"middle\">\n");
            writer.write("                                    <p><span class=\"page-sub-heading\">" + subHeading + "</p>\n");
            writer.write("                                </td>\n");
            writer.write("                            </tr>\n");
            writer.write("                        </table>\n");
            writer.write("                    </td>\n");
            writer.write("                </tr>\n");
            writer.write("            </table>\n");
            writer.write("        </td>\n");
            writer.write("    </tr>\n");
        }
    }

    /**
     * Render the page content footer
     */
    public void renderPageFooter(Writer writer, NavigationContext nc) throws IOException
    {
        NavigationPathFlags flags = nc.getActiveState().getFlags();
        if(flags.flagIsSet(NavigationPage.Flags.SHOW_RENDER_TIME))
        {
            Long startTime = (Long) nc.getRequest().getAttribute(NavigationControllerServlet.REQATTRNAME_RENDER_START_TIME);
            writer.write("<p align=right>Render time: " + (startTime != null
                                                           ? (Long.toString((System.currentTimeMillis() - startTime.longValue())) + " milliseconds&nbsp;&nbsp;")
                                                           : "unknown&nbsp;&nbsp;"));
        }

        if(flags.flagIsSet(NavigationPage.Flags.IS_POPUP_MODE | NavigationPage.Flags.IS_PRINT_MODE))
            return;

        writer.write("	        </div>\n");
        writer.write("		</td>\n");
        writer.write("	</tr>\n");
        writer.write("</table>\n");
        writer.write("</body>");
    }

    /**
     * Generates the HTML for the Level one navigation
     */
    protected String generateLevelOneHtml(NavigationPath currentNavTree, NavigationContext nc) throws IOException
    {
        Theme theme = getTheme();
        StringBuffer buffer = new StringBuffer();
        List tabElements = currentNavTree.getSibilingList();

        if(tabElements == null || tabElements.isEmpty())
        {
            return "";
        }
        buffer.append("            <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">\n");
        buffer.append("                <tr>\n");
        buffer.append("                <td class=\"menu-level-1-start\" valign=\"bottom\" nowrap><img src=\"" +
                      theme.getResourceUrl("/images/login/spacer.gif") + "\" width=\"10\" height=\"10\"></td>");
        for(int i = 0; i < tabElements.size(); i++)
        {
            NavigationPage tabElement = (NavigationPage) tabElements.get(i);
            NavigationPage.Flags flags = (NavigationPage.Flags) nc.getState(tabElement).getFlags();
            boolean hidden = flags.isHidden() || (flags.isHiddenUnlessActive() && !tabElement.isInActivePath(nc));
            if(!hidden)
            {
                if(i == 0)
                {
                    if(tabElement.isInActivePath(nc))
                    {
                        buffer.append("                <td class=\"menu-level-1-active-start\" valign=\"bottom\" nowrap>" +
                                      "<span class=\"menu-level-1\">&nbsp;&nbsp;&nbsp;&nbsp;</span></td>");
                        buffer.append("                    <td class=\"menu-level-1-table-active\" valign=\"bottom\" nowrap>");
                        buffer.append("<a class=\"menu-level-1-active\" " +
                                      tabElement.constructAnchorAttributes(nc) + ">" + tabElement.getCaption(nc) + "&nbsp;&nbsp;&nbsp;&nbsp;</a></td>\n");
                    }
                    else
                    {
                        buffer.append("                <td class=\"menu-level-1-table-start\" valign=\"bottom\" nowrap>" +
                                      "<span class=\"menu-level-1\">&nbsp;&nbsp;&nbsp;</span></td>");
                        buffer.append("                    <td class=\"menu-level-1-table\" valign=\"bottom\" nowrap>");
                        buffer.append("<a class=\"menu-level-1\" " +
                                      tabElement.constructAnchorAttributes(nc) + ">" + tabElement.getCaption(nc) + "&nbsp;&nbsp;&nbsp;</a></td>\n");
                    }
                }
                else
                {
                    if(tabElement.isInActivePath(nc))
                    {
                        buffer.append("                    <td class=\"menu-level-1-table-active-end\" valign=\"bottom\" nowrap>");
                        buffer.append("<a class=\"menu-level-1-active\" " +
                                      tabElement.constructAnchorAttributes(nc) + ">&nbsp;&nbsp;&nbsp;&nbsp;</a></td>\n");

                        buffer.append("                    <td class=\"menu-level-1-table-active\" valign=\"bottom\" nowrap>");
                        buffer.append("<a class=\"menu-level-1-active\" " +
                                      tabElement.constructAnchorAttributes(nc) + ">" + tabElement.getCaption(nc) + "&nbsp;&nbsp;&nbsp;&nbsp;</a></td>\n");
                    }
                    else
                    {
                        buffer.append("                    <td class=\"menu-level-1-table-end\" valign=\"bottom\" nowrap><span class=\"menu-level-1\">" +
                                      "&nbsp;&nbsp;&nbsp;</span></td>\n");

                        buffer.append("                    <td class=\"menu-level-1-table\" valign=\"bottom\" nowrap>");
                        buffer.append("<a class=\"menu-level-1\" " +
                                      tabElement.constructAnchorAttributes(nc) + ">" + tabElement.getCaption(nc) + "&nbsp;&nbsp;&nbsp;</a></td>\n");
                    }
                }
            }
        }
        buffer.append("              <td class=\"menu-level-1-end\" valign=\"bottom\" nowrap><span>&nbsp;&nbsp;</span></td>");
        buffer.append("              <td class=\"menu-level-1-fill\" width=\"100%\" valign=\"bottom\" nowrap>" +
                      "<span>&nbsp;&nbsp;</span></td>");
        buffer.append("               </tr>\n");
        buffer.append("           </table>\n");

        return buffer.toString();
    }

    /**
     * Generates the HTML for the level two navigation level
     */
    protected String generateLevelTwoHtml(NavigationPath currentNavTree, NavigationContext nc) throws IOException
    {
        StringBuffer writer = new StringBuffer();
        List tabElements = currentNavTree.getSibilingList();

        if(tabElements == null || tabElements.isEmpty())
        {
            return "";
        }

        writer.append("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" height=\"20\">\n");
        writer.append("<tr>\n");
        writer.append("	<td class=\"menu-level-2-table\" align=\"left\" valign=\"middle\">\n");
        writer.append("		<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
        writer.append("			<tr>\n");
        writer.append("			    <td align=\"center\" nowrap><a class=\"menu-level-2\">&nbsp;&nbsp;</a></td>\n");
        int size = tabElements.size();
        for(int i = 0; i < size; i++)
        {
            NavigationPage tabElement = (NavigationPage) tabElements.get(i);
            NavigationPage.Flags flags = (NavigationPage.Flags) nc.getState(tabElement).getFlags();
            boolean hidden = flags.isHidden() || (flags.isHiddenUnlessActive() && !tabElement.isInActivePath(nc));

            if(!hidden)
            {
                if(tabElement.isInActivePath(nc))
                {
                    writer.append("                    <td class=\"menu-level-2-table-active-end\" valign=\"bottom\" nowrap>");
                    writer.append("<a class=\"menu-level-2-active\" " +
                                  tabElement.constructAnchorAttributes(nc) + ">&nbsp;&nbsp;</a></td>\n");

                    writer.append("                    <td class=\"menu-level-2-table-active\" valign=\"bottom\" nowrap>");
                    writer.append("<a class=\"menu-level-2-active\" " +
                                  tabElement.constructAnchorAttributes(nc) + ">" + tabElement.getCaption(nc) + "&nbsp;&nbsp;</a></td>\n");
                }
                else
                {
                    writer.append("<td nowrap align=\"center\" " + (i != size - 1
                                                                    ? "class=\"menu-level-2-separator\"" : "") + ">");
                    writer.append("<a class=\"menu-level-2\" " +
                                  tabElement.constructAnchorAttributes(nc) + ">&nbsp;&nbsp;" + tabElement.getCaption(nc) + "&nbsp;&nbsp;</a></TD>\n");
                }
            }
        }
        writer.append("            </tr>\n");
        writer.append("        </table>\n");
        writer.append("    </td>\n");
        writer.append("</tr>\n");
        writer.append("</table>\n");
        return writer.toString();
    }

    public int renderVerticalCssMenus(Writer writer, NavigationContext nc, List children, boolean root) throws IOException
    {
        if(children.size() == 0)
            return 0;

        if(root)
            writer.write("<table class='vertical-menu-root' cellspacing=0>");
        else
            writer.write("<table class='vertical-menu' cellspacing=0>");

        int rendered = 0;
        for(int i = 0; i < children.size(); i++)
        {
            NavigationPage activePage = (NavigationPage) children.get(i);
            final NavigationPage.Flags flags = (NavigationPage.Flags) activePage.getFlags();
            boolean hidden = flags.isHidden() || (flags.isHiddenUnlessActive() && !activePage.isInActivePath(nc));
            if(hidden)
                continue;

            boolean activePathSelected = activePage == nc.getActivePage();
            boolean activePathLeaf = activePage.getChildrenList().size() == 0;
            final String className =
                    root ? (activePathSelected ? "menu-root-active" : "menu-root") :
                    activePathLeaf ?
                    (activePathSelected ? "menu-leaf-active" : "menu-leaf") :
                    (activePathSelected ? "menu-active" : "menu");

            writer.write("<tr valign=top class='" + className + "'><td class='" + className + "-prefix'>&nbsp;</td><td class='" + className + "'>");
            if(flags.isRejectFocus())
                writer.write(activePage.getCaption(nc));
            else
            {
                writer.write("<a href='" + activePage.getUrl(nc) + "' class='" + className + "'>");
                writer.write(activePage.getCaption(nc));
                writer.write("</a>");
            }
            rendered += renderVerticalCssMenus(writer, nc, activePage.getChildrenList(), false);
            writer.write("</td><td class='" + className + "-suffix'>&nbsp;</td></tr>");

            rendered++;
        }
        writer.write("</table>");
        return rendered;
    }

    /**
     * Generates the html for the third navigation level
     */
    protected String generateLevelThreeHtml(NavigationPath currentNavTree, NavigationContext nc) throws IOException
    {
        Theme theme = getTheme();

        StringBuffer writer = new StringBuffer();
        List sideBarElements = currentNavTree.getSibilingList();
        if(sideBarElements == null || sideBarElements.isEmpty())
        {
            return "";
        }

        writer.append("      <!-- Level 3 Begins -->\n");
        writer.append("      <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" height=\"100%\" >\n");

        for(int i = 0; i < sideBarElements.size(); i++)
        {
            NavigationPage sideBarElement = (NavigationPage) sideBarElements.get(i);
            NavigationPage.Flags flags = (NavigationPage.Flags) nc.getState(sideBarElement).getFlags();
            boolean hidden = flags.isHidden() || (flags.isHiddenUnlessActive() && !sideBarElement.isInActivePath(nc));
            if(!hidden)
            {
                if(sideBarElement.isInActivePath(nc))
                {
                    writer.append("      <tr>\n");
                    writer.append("      	<td>\n");
                    writer.append("      		<table width=\"" + sidebarWidth + "\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" >\n");
                    writer.append("     		 <tr>\n");
                    writer.append("                    <td class=\"menu-level-3-table-active-end\" valign=\"bottom\" nowrap>");
                    writer.append("<a class=\"menu-level-3-active\" " +
                                  sideBarElement.constructAnchorAttributes(nc) + "></a></td>\n");

                    writer.append("                    <td class=\"menu-level-3-table-active\" valign=\"bottom\" nowrap>");
                    writer.append("<a class=\"menu-level-3-active\" " +
                                  sideBarElement.constructAnchorAttributes(nc) + ">" + sideBarElement.getCaption(nc) + "&nbsp;</a></td>\n");
                    writer.append("      		</tr>\n");
                    writer.append("      		</table>\n");
                    writer.append("     	</td>\n");
                    writer.append("      </tr>\n");
                }
                else
                {
                    writer.append("      <tr>\n");
                    writer.append("          <td class=\"menu-level-3-table\" align=\"left\" valign=\"middle\">" +
                                  "<a class=\"menu-level-3\" " + sideBarElement.constructAnchorAttributes(nc) + "><nobr>" +
                                  sideBarElement.getCaption(nc) + "</nobr></a></td>\n");
                    writer.append("      </tr>\n");
                }
            }
        }
        writer.append("          <tr height=\"100%\">\n");
        writer.append("              <td class=\"menu-table-end\" align=\"left\" valign=\"top\" height=\"100%\">" +
                      "<img src=\"" + theme.getResourceUrl("/images/spacer-big.gif") + "\" height=\"100%\" width=\"100%\"></td>\n");
        writer.append("          </tr>\n");
        writer.append("      </table>\n");
        writer.append("      <!-- Level 3 Ends -->\n");
        return writer.toString();
    }
}
