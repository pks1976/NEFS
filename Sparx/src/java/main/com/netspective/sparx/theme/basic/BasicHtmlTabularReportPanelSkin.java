/*
 * Copyright (c) 2000-2002 Netspective Corporation -- all rights reserved
 *
 * Netspective Corporation permits redistribution, modification and use
 * of this file in source and binary form ("The Software") under the
 * Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the
 * canonical license and must be accepted before using The Software. Any use of
 * The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright
 *    notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only
 *    (as Java .class files or a .jar file containing the .class files) and only
 *    as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software
 *    development kit, other library, or development tool without written consent of
 *    Netspective Corporation. Any modified form of The Software is bound by
 *    these same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of
 *    The License, normally in a plain ASCII text file unless otherwise agreed to,
 *    in writing, by Netspective Corporation.
 *
 * 4. The names "Sparx" and "Netspective" are trademarks of Netspective
 *    Corporation and may not be used to endorse products derived from The
 *    Software without without written consent of Netspective Corporation. "Sparx"
 *    and "Netspective" may not appear in the names of products derived from The
 *    Software without written consent of Netspective Corporation.
 *
 * 5. Please attribute functionality to Sparx where possible. We suggest using the
 *    "powered by Sparx" button or creating a "powered by Sparx(tm)" link to
 *    http://www.netspective.com for each application using Sparx.
 *
 * The Software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE CORPORATION AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A RESULT OF USING OR DISTRIBUTING
 * THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THE SOFTWARE, EVEN IF HE HAS BEEN ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGES.
 *
 * @author Shahid N. Shah
 */

/**
 * $Id: BasicHtmlTabularReportPanelSkin.java,v 1.13 2003-06-28 05:23:48 aye.thu Exp $
 */

package com.netspective.sparx.theme.basic;

import java.io.IOException;
import java.io.Writer;

import com.netspective.sparx.panel.HtmlPanelValueContext;
import com.netspective.sparx.panel.HtmlPanel;
import com.netspective.commons.report.tabular.TabularReportColumns;
import com.netspective.commons.report.tabular.TabularReportColumn;
import com.netspective.commons.report.tabular.TabularReportValueContext;
import com.netspective.commons.report.tabular.TabularReportColumnState;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.theme.Theme;
import com.netspective.sparx.report.tabular.HtmlTabularReportValueContext;
import com.netspective.sparx.report.tabular.HtmlTabularReportSkin;
import com.netspective.sparx.report.tabular.HtmlTabularReportDataSource;
import com.netspective.sparx.report.tabular.HtmlTabularReportDataSourceScrollState;
import com.netspective.sparx.command.RedirectCommand;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.lang.ClassPath;
import com.netspective.commons.xdm.XdmBitmaskedFlagsAttribute;
import com.netspective.commons.command.Command;

public class BasicHtmlTabularReportPanelSkin extends BasicHtmlPanelSkin implements HtmlTabularReportSkin
{
    public static class Flags extends BasicHtmlPanelSkin.Flags
    {
        public static final int SHOW_HEAD_ROW = BasicHtmlPanelSkin.Flags.STARTCUSTOM;
        public static final int SHOW_FOOT_ROW = SHOW_HEAD_ROW * 2;
        public static final int ALLOW_TREE_EXPAND_COLLAPSE = SHOW_FOOT_ROW * 2;
        public static final int STARTCUSTOM = ALLOW_TREE_EXPAND_COLLAPSE * 2;

        public static final XdmBitmaskedFlagsAttribute.FlagDefn[] FLAGDEFNS = new XdmBitmaskedFlagsAttribute.FlagDefn[BasicHtmlPanelSkin.Flags.FLAGDEFNS.length + 2];

        static
        {
            for(int i = 0; i < BasicHtmlPanelSkin.Flags.FLAGDEFNS.length; i++)
                FLAGDEFNS[i] = BasicHtmlPanelSkin.Flags.FLAGDEFNS[i];
            FLAGDEFNS[BasicHtmlPanelSkin.Flags.FLAGDEFNS.length + 0] = new FlagDefn(ACCESS_XDM, "SHOW_HEAD_ROW", SHOW_HEAD_ROW);
            FLAGDEFNS[BasicHtmlPanelSkin.Flags.FLAGDEFNS.length + 1] = new FlagDefn(ACCESS_XDM, "SHOW_FOOT_ROW", SHOW_FOOT_ROW);
            FLAGDEFNS[BasicHtmlPanelSkin.Flags.FLAGDEFNS.length + 1] = new FlagDefn(ACCESS_XDM, "ALLOW_TREE_EXPAND_COLLAPSE", ALLOW_TREE_EXPAND_COLLAPSE);
        }

        public XdmBitmaskedFlagsAttribute.FlagDefn[] getFlagsDefns()
        {
            return FLAGDEFNS;
        }
    }

    public BasicHtmlTabularReportPanelSkin(Theme theme, String panelClassNamePrefix, String panelResourcesPrefix, boolean fullWidth)
    {
        super(theme, panelClassNamePrefix, panelResourcesPrefix, fullWidth);
        flags.setFlag(Flags.SHOW_HEAD_ROW | Flags.SHOW_FOOT_ROW);
    }

    public BasicHtmlPanelSkin.Flags createFlags()
    {
        return new Flags();
    }

    public String constructClassRef(Class cls)
    {
        String className = cls.getName();
        if(className.startsWith("com.netspective"))
            className = "~" + className.substring("com.netspective".length());
        return "<span title=\""+ ClassPath.getClassFileName(cls) +"\">" + className + "</span>";
    }

    public String constructRedirect(TabularReportValueContext rc, Command command, String label, String hint, String target)
    {
        if(command instanceof RedirectCommand)
        {
            StringBuffer sb = new StringBuffer();
            sb.append("<a href=\"" + ((RedirectCommand) command).getLocation().getTextValue(rc) + "\"");
            if(hint != null)
                sb.append(" title=\"" + hint + "\"");
            if(target != null)
                sb.append(" target=\"" + target + "\"");
            sb.append(">" + label + "</a");
            return sb.toString();
        }
        return this.getClass().getName() + ".constructAction(Action action, String label, String hint) not implemented.";
    }

    public String getBlankValue()
    {
        return "&nbsp;";
    }

    public String getFileExtension()
    {
        return ".html";
    }

    public void render(Writer writer, TabularReportValueContext rc, TabularReportDataSource ds) throws IOException
    {
        renderPanelRegistration(writer, (HtmlPanelValueContext) rc);

        int panelRenderFlags = ((HtmlTabularReportValueContext) rc).getPanelRenderFlags();
        if((panelRenderFlags & HtmlPanel.RENDERFLAG_NOFRAME) == 0)
        {
            renderFrameBegin(writer, (HtmlPanelValueContext) rc);
            writer.write("    <table class=\"report\" width=\"100%\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\">\n");
        }
        else
            writer.write("    <table id=\""+ ((HtmlPanelValueContext) rc).getPanel().getIdentifier() +"_content\" class=\"report_no_frame\" width=\"100%\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\">\n");

        if(flags.flagIsSet(Flags.SHOW_HEAD_ROW) && !rc.getReport().getFlags().flagIsSet(HtmlTabularReport.Flags.HIDE_HEADING))
            produceHeadingRow(writer, (HtmlTabularReportValueContext) rc, (HtmlTabularReportDataSource) ds);
        produceDataRows(writer, (HtmlTabularReportValueContext) rc, (HtmlTabularReportDataSource) ds);

        if(flags.flagIsSet(Flags.SHOW_FOOT_ROW) && rc.getCalcsCount() > 0)
            produceFootRow(writer, (HtmlTabularReportValueContext) rc);

        writer.write("    </table>\n");

        if((panelRenderFlags & HtmlPanel.RENDERFLAG_NOFRAME) == 0)
            renderFrameEnd(writer, (HtmlPanelValueContext) rc);
    }

    private int getTableColumnsCount(TabularReportValueContext rc)
    {
        return (rc.getVisibleColsCount() * 2) +
               (getRowDecoratorPrependColsCount(rc) * 2) +
               (getRowDecoratorAppendColsCount(rc) * 2) +
               + 1; // each column has "spacer" in between, first column as spacer before too
    }

    public void produceHeadingRow(Writer writer, HtmlTabularReportValueContext rc, HtmlTabularReportDataSource ds) throws IOException
    {
        TabularReportColumns columns = rc.getColumns();
        TabularReportColumnState[] states = rc.getStates();
        int dataColsCount = columns.size();

        Theme theme = rc.getActiveTheme();
        String imgPath = rc.getThemeImagesRootUrl(theme) + "/" + panelResourcesPrefix;

        String sortAscImgTag = " <img src=\""+ imgPath + "/column-sort-ascending.gif\" border=0>";
        String sortDescImgTag = " <img src=\""+ imgPath + "/column-sort-descending.gif\" border=0>";

        writer.write("<tr>");

        for(int i = 0; i < dataColsCount; i++)
        {
            TabularReportColumnState rcs = rc.getState(i);
            if(! states[i].isVisible())
                continue;

            String colHeading = ds.getHeadingRowColumnData(i);
            if(colHeading != null)
            {
                if(rcs.getFlags().flagIsSet(TabularReportColumn.Flags.SORTED_ASCENDING))
                    colHeading += sortAscImgTag;
                if(rcs.getFlags().flagIsSet(TabularReportColumn.Flags.SORTED_DESCENDING))
                    colHeading += sortDescImgTag;

                writer.write("        <td class=\"report-column-heading\" nowrap>" + colHeading  + "</td>");
            }
            else
                writer.write("        <td class=\"report-column-heading\" nowrap>&nbsp;&nbsp;</td>");
        }

        writer.write("</tr>");
    }

    public int produceDataRows(Writer writer, HtmlTabularReportValueContext rc, HtmlTabularReportDataSource ds) throws IOException
    {
        HtmlTabularReport defn = ((HtmlTabularReport) rc.getReport());
        TabularReportColumns columns = rc.getColumns();
        TabularReportColumnState[] states = rc.getStates();

        int rowsWritten = 0;
        int dataColsCount = columns.size();
        int tableColsCount = getTableColumnsCount(rc);

        boolean hiearchical = ds.isHierarchical();
        boolean allowTreeExpandCollapse = getFlags().flagIsSet(Flags.ALLOW_TREE_EXPAND_COLLAPSE);
        String panelId = rc.getPanel().getIdentifier();

        StringBuffer treeScript = null;
        if(hiearchical && allowTreeExpandCollapse)
        {
            treeScript = new StringBuffer();
            treeScript.append("var activeTree = new Tree(\""+ panelId +"\");\n");
            treeScript.append("var activeNode;\n");
        }

        //TODO: Sparx 2.x conversion required
        //ResultSetScrollState scrollState = rc.getScrollState();
        //boolean paging = scrollState != null;
        HtmlTabularReportDataSourceScrollState scrollState = (HtmlTabularReportDataSourceScrollState) rc.getScrollState();
        boolean paging = scrollState != null;

        boolean isOddRow = false;
        while(ds.next())
        {
            isOddRow = ! isOddRow;

            int hiearchyCol = 0;
            int activeLevel = 0;
            int activeParent = -1;

            if(hiearchical)
            {
                TabularReportDataSource.Hierarchy activeHierarchy = ds.getActiveHierarchy();
                hiearchyCol = activeHierarchy.getColumn();
                activeLevel = activeHierarchy.getLevel();
                activeParent = activeHierarchy.getParentRow();

                if(allowTreeExpandCollapse)
                {
                    if(activeParent == -1)
                        treeScript.append("activeNode = activeTree.newNode(null, \"node_"+ rowsWritten +"\");\n");
                    else
                        treeScript.append("activeNode = activeTree.newNode(\"node_"+ activeHierarchy.getParentRow() +"\", \"node_"+ rowsWritten +"\");\n");
                    writer.write("<tr id=\""+ panelId + "_node_" + rowsWritten + "\">");
                }
                else
                    writer.write("<tr>");
            }
            else
                writer.write("<tr>");

            for(int i = 0; i < dataColsCount; i++)
            {

                TabularReportColumn column = columns.getColumn(i);
                TabularReportColumnState state = states[i];

                if(! state.isVisible())
                    continue;

                String data =
                        state.getFlags().flagIsSet(TabularReportColumn.Flags.HAS_OUTPUT_PATTERN) ?
                        state.getOutputFormat() :
                        column.getFormattedData(rc, ds, TabularReportColumn.GETDATAFLAG_DO_CALC);

                String style = state.getCssStyleAttrValue();
                if(hiearchical && (hiearchyCol == i) && activeLevel > 0)
                {
                    style += "padding-left:" + (activeLevel * 15) + ";";
                    if(allowTreeExpandCollapse)
                        data = "<span id=\""+ panelId + "_node_" + rowsWritten + "_controller\" onclick=\"TREES.toggleNodeExpansion('"+ panelId +"', 'node_"+ rowsWritten +"')\" class=\"panel-output-tree-collapse\">&nbsp;</span>" + data;
                }

                String singleColumn =
                        "<td class=\"" +
                            (ds.isActiveRowSelected() ? "report-column-selected" : (isOddRow ? "report-column-even" : "report-column-odd")) + "\" style=\"" + style + "\">" +
                        data +
                        "&nbsp;</td>";

                writer.write(defn.replaceOutputPatterns(rc, ds, singleColumn));
            }

            writer.write("</tr>");
            rowsWritten++;
            //TODO: Sparx 2.x conversion required
            // check to see if this row should be the last
            if (paging && rowsWritten == scrollState.getRowsPerPage())
                break;

            //if(paging && rc.endOfPage())
            //    break;
        }

        if (rowsWritten == 0)
        {
            // no rows were written out that means that there was no data in the result set
            ValueSource noDataFoundMsgSrc = ds.getNoDataFoundMessage();
            String noDataFoundMsg = noDataFoundMsgSrc != null ? noDataFoundMsgSrc.getTextValue(rc) : null;
            // add the 'no data found' message
            if(noDataFoundMsg != null)
                writer.write("<tr><td class=\"report-column-summary\" colspan='" + tableColsCount + "'>"+ noDataFoundMsg +"</td></tr>");
            //TODO: Sparx 2.x conversion required
            if(paging)
                scrollState.setNoMoreRows();
        }
        //TODO: Sparx 2.x conversion required
        else if (paging)
        {
            // record the number of rows written to the total number ofrows already displayed
            scrollState.accumulateRowsProcessed(rowsWritten);
            // if the total number of rows written is less than the scroll state's number of rows per page setting,
            // this must be the last page
            if(rowsWritten < scrollState.getRowsPerPage())
                scrollState.setNoMoreRows();
        }

        if(hiearchical && allowTreeExpandCollapse)
        {
            treeScript.append("TREES.registerTree(activeTree)\n");
            treeScript.append("activeTree.initialize()\n");

            writer.write("<script>\n");
            writer.write(treeScript.toString());
            writer.write("</script>");
        }


        return rowsWritten;
    }

    public void produceFootRow(Writer writer, HtmlTabularReportValueContext rc) throws IOException
    {
        int calcsCount = rc.getCalcsCount();
        if(calcsCount == 0)
            return;

        TabularReportColumnState[] states = rc.getStates();
        TabularReportColumns columns = rc.getColumns();
        int dataColsCount = columns.size();

        writer.write("<tr>");
        for(int i = 0; i < dataColsCount; i++)
        {
            TabularReportColumn column = columns.getColumn(i);
            if(! states[i].isVisible())
                continue;

            String summary = column.getFormattedData(rc, states[i].getCalc());
            if(summary == null)
                summary = "&nbsp;";

            writer.write("<td class=\"report-column-summary\" style=\""+ states[i].getCssStyleAttrValue() +"\">" + summary + "</td>");
        }
        writer.write("</tr>");
    }

    protected int getRowDecoratorPrependColsCount(com.netspective.commons.report.tabular.TabularReportValueContext rc)
    {
        return 0;
    }

    protected int getRowDecoratorAppendColsCount(com.netspective.commons.report.tabular.TabularReportValueContext rc)
    {
        return 0;
    }
}