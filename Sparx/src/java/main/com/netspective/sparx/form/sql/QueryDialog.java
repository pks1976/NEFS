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
 * $Id: QueryDialog.java,v 1.2 2003-05-23 02:18:41 shahid.shah Exp $
 */

package com.netspective.sparx.form.sql;

import java.io.Writer;
import java.io.IOException;

import com.netspective.sparx.form.Dialog;
import com.netspective.sparx.form.DialogsPackage;
import com.netspective.sparx.form.DialogContext;
import com.netspective.sparx.form.DialogSkin;
import com.netspective.sparx.form.DialogExecuteException;
import com.netspective.sparx.form.field.DialogField;
import com.netspective.sparx.form.field.type.TextField;
import com.netspective.sparx.form.field.type.IntegerField;
import com.netspective.sparx.form.field.type.PanelsField;
import com.netspective.sparx.report.tabular.HtmlTabularReportSkin;
import com.netspective.sparx.report.tabular.HtmlTabularReport;
import com.netspective.sparx.panel.HtmlLayoutPanel;
import com.netspective.sparx.panel.HtmlPanel;
import com.netspective.sparx.console.panel.data.sql.QueryDbmsSqlTextsPanel;
import com.netspective.sparx.console.panel.data.sql.QueryDetailPanel;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.sql.Query;
import com.netspective.axiom.sql.QueryParameter;
import com.netspective.axiom.sql.QueryParameters;
import com.netspective.commons.value.source.StaticValueSource;

public class QueryDialog extends Dialog
{
    private Query query;
    private HtmlTabularReport report;
    private HtmlTabularReportSkin reportSkin;
    private String[] urlFormats;
    private int rowsPerPage;

    public QueryDialog()
    {
    }

    public QueryDialog(DialogsPackage pkg)
    {
        super(pkg);
    }

    public Query getQuery()
    {
        return query;
    }

    public void setQuery(Query query)
    {
        this.query = query;
    }

    public void createParamFields()
    {
        PanelsField pfield = new PanelsField();
        HtmlLayoutPanel panels = pfield.createPanels();
        panels.addPanel(new QueryDbmsSqlTextsPanel());
        addField(pfield);

        QueryParameters params = query.getParams();
        if(params != null)
        {
            for(int i = 0; i < query.getParams().size(); i++)
            {
                QueryParameter param = query.getParams().get(i);
                DialogField field = new TextField();
                field.setName("param_" + i);
                field.setCaption(new StaticValueSource(param.getName() != null ? param.getName() : ("Parameter " + i)));
                field.setDefault(param.getValue());
                addField(field);
            }
        }

        DialogField field = new IntegerField();
        field.setName("rows_per_page");
        field.setCaption(new StaticValueSource("Rows per page"));
        field.setDefault(new StaticValueSource("10"));
        addField(field);
    }

    public DialogContext createContext(NavigationContext nc, DialogSkin skin)
    {
        DialogContext dc = super.createContext(nc, skin);
        dc.getRequest().setAttribute(QueryDetailPanel.REQPARAMNAME_QUERY, query.getQualifiedName());
        return dc;
    }

    public HtmlTabularReport getReport()
    {
        return report;
    }

    public void setReport(HtmlTabularReport report)
    {
        this.report = report;
    }

    public HtmlTabularReportSkin getReportSkin()
    {
        return reportSkin;
    }

    public void setReportSkin(HtmlTabularReportSkin reportSkin)
    {
        this.reportSkin = reportSkin;
    }

    public int getRowsPerPage()
    {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage)
    {
        this.rowsPerPage = rowsPerPage;
    }

    public String[] getUrlFormats()
    {
        return urlFormats;
    }

    public void setUrlFormats(String[] urlFormats)
    {
        this.urlFormats = urlFormats;
    }

    public void execute(Writer writer, DialogContext dc) throws IOException, DialogExecuteException
    {
        query.getPresentation().getDefaultPanel().render(writer, dc, dc.getActiveTheme(), HtmlPanel.RENDERFLAGS_DEFAULT);
    }
}
