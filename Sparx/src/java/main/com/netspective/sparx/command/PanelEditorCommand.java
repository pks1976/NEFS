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
 * @author Aye Thu
 */

package com.netspective.sparx.command;

import com.netspective.commons.command.CommandDocumentation;
import com.netspective.commons.command.CommandException;
import com.netspective.sparx.form.DialogState;
import com.netspective.sparx.navigate.NavigationContext;
import com.netspective.sparx.panel.HtmlPanel;
import com.netspective.sparx.panel.PanelEditor;
import com.netspective.sparx.theme.Theme;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.StringTokenizer;

/**
 * Class for handling the record-editor-panel command
 *
 *
 * @version $Id: PanelEditorCommand.java,v 1.4 2004-03-03 22:10:49 aye.thu Exp $
 */
public class PanelEditorCommand extends AbstractHttpServletCommand
{
    private static final Log log = LogFactory.getLog(PanelEditorCommand.class);


    public static final String PANEL_EDITOR_COMMAND_REQUEST_PARAM_NAME = "panel-editor";
    public static final String[] IDENTIFIERS = new String[] { "panel-editor" };

    public static final CommandDocumentation DOCUMENTATION = new CommandDocumentation(
            "Allows automatic add, delete, and edit functionality to records in a report panel.",
            new CommandDocumentation.Parameter[]
            {
                new CommandDocumentation.Parameter("panel-editor-name", true, "The fully qualified name of the panel editor (package-name.panel-name)."),
                new CommandDocumentation.Parameter("command-action", false, "The action to perform for the selected record."),
                new CommandDocumentation.Parameter("record-key", false, "The primary key of the selected record."),
            }
    );

    public static String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public static CommandDocumentation getDocumentation()
    {
        return DOCUMENTATION;
    }

    /* the name of the record editor panel */
    private String panelEditorName;
    private String recordKey;
    /* the action to perform */
    private String panelMode;

    /**
     * Sole constructor
     */
    public PanelEditorCommand()
    {
        super();
    }

    /**
     * Gets the parameters for the command
     *
     * @return
     */
    public String getParameters()
    {
        String delim = getParametersDelimiter();
        StringBuffer sb = new StringBuffer(panelEditorName);
        sb.append(delim);
        sb.append(panelMode);
        sb.append(delim);
        sb.append(recordKey);

        return sb.toString();
    }

    /**
     * Sets the parameters for the command
     *
     * @param params
     */
    public void setParameters(StringTokenizer params)
    {
        panelEditorName = params.nextToken();

        if (params.hasMoreTokens())
            panelMode = params.nextToken();
        if (params.hasMoreTokens())
            recordKey = params.nextToken();
    }

    public String getPanelEditorName()
    {
        return panelEditorName;
    }

    public String getPanelMode()
    {
        return panelMode;
    }

    /**
     * Calculate the mode the record editor panel is in
     *
     * @return
     */
    public int calculateEditorPanelMode(NavigationContext nc)
    {
        int mode = PanelEditor.UNKNOWN_MODE;
        if (panelMode == null)
        {
            mode = PanelEditor.DEFAULT_DISPLAY_MODE;
        }
        else if (panelMode.equals("add"))
        {
            mode = PanelEditor.ADD_RECORD_DISPLAY_MODE;
            nc.getRequest().setAttribute(DialogState.PARAMNAME_PERSPECTIVE, "add");
        }
        else if (panelMode.equals("edit") && recordKey != null)
        {
            mode = PanelEditor.EDIT_RECORD_DISPLAY_MODE;
            nc.getRequest().setAttribute(DialogState.PARAMNAME_PERSPECTIVE, "edit");
        }
        else if (panelMode.equals("delete") && recordKey != null)
        {
            mode = PanelEditor.DELETE_RECORD_DISPLAY_MODE;
            nc.getRequest().setAttribute(DialogState.PARAMNAME_PERSPECTIVE, "delete");
        }
        else if (panelMode.equals("manage"))
        {
            mode = PanelEditor.MANAGE_RECORDS_DISPLAY_MODE;
        }

        return mode;
    }

    /**
     *
     *
     * @param writer
     * @param nc
     * @param unitTest
     * @throws CommandException
     * @throws IOException
     */
    public void handleCommand(Writer writer, NavigationContext nc, boolean unitTest) throws CommandException, IOException
    {
        PanelEditor ePanel = nc.getProject().getPanelEditor(getPanelEditorName());
        if (ePanel == null)
        {
             throw new RuntimeException("Record editor panel '"+ getPanelEditorName() + "' not found in "+ this +".");
        }
        Theme theme = nc.getActiveTheme();
        int mode = calculateEditorPanelMode(nc);

        if (mode == PanelEditor.UNKNOWN_MODE)
        {
            log.error("Unexpected mode encountered for the record editor panel '" + getPanelEditorName() + "'.");
            throw new RuntimeException("Unexpected mode encountered for the record editor panel '" + getPanelEditorName() + "'.");
        }
        // TODO: maybe these context settings should be changed to methods in the PanelEditor 
        nc.setAttribute(PanelEditor.DISPLAY_MODE_CONTEXT_ATTRIBUTE, new Integer(mode));
        if (recordKey != null)
            nc.setAttribute(PanelEditor.POPULATE_KEY_CONTEXT_ATTRIBUTE, recordKey);
        ePanel.render(writer, nc, theme, HtmlPanel.RENDERFLAGS_DEFAULT);

    }


}