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
 * $Id: DialogDirector.java,v 1.9 2003-06-11 03:33:59 aye.thu Exp $
 */

package com.netspective.sparx.form;

import java.io.IOException;
import java.io.Writer;

import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSources;
import com.netspective.commons.xdm.XdmEnumeratedAttribute;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.sparx.form.field.type.DirectorNextActionsSelectField;
import com.netspective.sparx.form.field.DialogField;

public class DialogDirector extends DialogField
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);
    private static final String[] STYLE_ENUM_VALUES = new String[] { "data", "confirm", "acknowledge" };

    public static class DialogDirectorStyle extends XdmEnumeratedAttribute
    {
        public static final int DATA = 0;
        public static final int CONFIRM = 1;
        public static final int ACKNOWLEDGE = 2;

        public DialogDirectorStyle()
        {
        }

        public DialogDirectorStyle(int valueIndex)
        {
            super(valueIndex);
        }

        public String[] getValues()
        {
            return STYLE_ENUM_VALUES;
        }
    }

    private ValueSource submitCaption;
    private ValueSource cancelCaption;
    private ValueSource pendingCaption;
    private ValueSource submitActionUrl;
    private ValueSource cancelActionUrl;
    private ValueSource pendingActionUrl;
    private DialogDirectorStyle style;
    private DirectorNextActionsSelectField nextActionsField;

    public DialogDirector()
    {
        setName("director");
        setSubmitCaption(ValueSources.getInstance().getValueSourceOrStatic("   OK   "));
        setCancelCaption(ValueSources.getInstance().getValueSourceOrStatic(" Cancel "));
    }

    public DialogDirectorStyle getStyle()
    {
        return style;
    }

    public void setStyle(DialogDirectorStyle style)
    {
        this.style = style;
        switch(this.style.getValueIndex())
        {
            case DialogDirectorStyle.DATA:
                setSubmitCaption(ValueSources.getInstance().getValueSourceOrStatic("  Save  "));
                break;

            case DialogDirectorStyle.CONFIRM:
                setSubmitCaption(ValueSources.getInstance().getValueSourceOrStatic("  Yes  "));
                setCancelCaption(ValueSources.getInstance().getValueSourceOrStatic("  No   "));
                break;

            case DialogDirectorStyle.ACKNOWLEDGE:
                setCancelCaption(null);
                setCancelActionUrl(null);
                break;
        }
    }

    public ValueSource getCancelActionUrl()
    {
        return cancelActionUrl;
    }

    public void setCancelActionUrl(ValueSource cancelActionUrl)
    {
        this.cancelActionUrl = cancelActionUrl;
    }

    public ValueSource getCancelCaption()
    {
        return cancelCaption;
    }

    public void setCancelCaption(ValueSource cancelCaption)
    {
        this.cancelCaption = cancelCaption;
    }

    public ValueSource getPendingActionUrl()
    {
        return pendingActionUrl;
    }

    public void setPendingActionUrl(ValueSource pendingActionUrl)
    {
        this.pendingActionUrl = pendingActionUrl;
    }

    public ValueSource getPendingCaption()
    {
        return pendingCaption;
    }

    public void setPendingCaption(ValueSource pendingCaption)
    {
        this.pendingCaption = pendingCaption;
    }

    public ValueSource getSubmitActionUrl()
    {
        return submitActionUrl;
    }

    public void setSubmitActionUrl(ValueSource submitActionUrl)
    {
        this.submitActionUrl = submitActionUrl;
    }

    public ValueSource getSubmitCaption()
    {
        return submitCaption;
    }

    public void setSubmitCaption(ValueSource submitCaption)
    {
        this.submitCaption = submitCaption;
    }

    public DirectorNextActionsSelectField getNextActionsField()
    {
        return nextActionsField;
    }

    public void setNextActionsField(DirectorNextActionsSelectField nextActionsField)
    {
        this.nextActionsField = nextActionsField;
    }

    public String getNextActionUrl(DialogContext dc)
    {
        return nextActionsField == null ? null : nextActionsField.getSelectedActionUrl(dc);
    }

    public void addNextActions(DirectorNextActionsSelectField nextActionsField)
    {
        this.nextActionsField = nextActionsField;
        this.nextActionsField.setParent(this);
    }

    public void makeStateChanges(DialogContext dc, int stage)
    {
        super.makeStateChanges(dc, stage);
        if(nextActionsField != null)
            nextActionsField.makeStateChanges(dc, stage);
    }

    public void renderControlHtml(Writer writer, DialogContext dc) throws IOException
    {
        Dialog dialog = dc.getDialog();
        String attrs = dc.getSkin().getDefaultControlAttrs();

        String submitCaption = (this.submitCaption != null) ? this.submitCaption.getTextValue(dc) : null;
        String cancelCaption = (this.cancelCaption != null) ? this.cancelCaption.getTextValue(dc) : null;

        int dataCmd = (int) dc.getDataCommands().getFlags();
        switch(dataCmd)
        {
            case DialogDataCommands.ADD:
            case DialogDataCommands.EDIT:
                submitCaption = " Save ";
                break;

            case DialogDataCommands.DELETE:
                submitCaption = " Delete ";
                break;

            case DialogDataCommands.CONFIRM:
                submitCaption = "  Yes  ";
                cancelCaption = "  No   ";
                break;
        }

        writer.write("<center>");

        if(nextActionsField != null && nextActionsField.isAvailable(dc))
        {
            //TODO: String caption = nextActionsField.getCaption(dc);
            String caption = null;
            if(caption != null && !nextActionsField.isInputHidden(dc))
                writer.write(caption);

            nextActionsField.renderControlHtml(writer, dc);

            if(caption != null && !nextActionsField.isInputHidden(dc))
                writer.write("&nbsp;&nbsp;");
        }

        writer.write("<input type='submit' name='"+ dialog.getSubmitDataParamName() +"' class=\"dialog-button\" value='");
        writer.write(submitCaption);
        writer.write("' ");
        writer.write(attrs);
        writer.write(">&nbsp;&nbsp;");

        if(pendingCaption != null)
        {
            writer.write("<input type='submit' class=\"dialog-button\" name='"+ dialog.getPendDataParamName() +"' value='");
            writer.write(pendingCaption.getTextValue(dc));
            writer.write("' ");
            writer.write(attrs);
            writer.write(">&nbsp;&nbsp;");
        }
        if (cancelCaption != null)
        {
            writer.write("<input type='button' class=\"dialog-button\" value='");
            writer.write(cancelCaption);
            writer.write("' ");
            if(cancelActionUrl == null)
            {
                String referer = dc.getOriginalReferer();
                if(referer != null)
                {
                    writer.write("onclick=\"document.location = '");
                    writer.write(referer);
                    writer.write("'\" ");
                }
                else
                {
                    writer.write("onclick=\"history.back()\" ");
                }
            }
            else
            {
                String cancelStr = cancelActionUrl != null ? cancelActionUrl.getTextValue(dc) : null;
                if("back".equals(cancelStr))
                {
                    writer.write("onclick=\"history.back()\" ");
                }
                else if(cancelStr != null && cancelStr.startsWith("javascript:"))
                {
                    writer.write("onclick=\"");
                    writer.write(cancelStr);
                    writer.write("\" ");
                }
                else
                {
                    writer.write("onclick=\"document.location = '");
                    writer.write(cancelStr);
                    writer.write("'\" ");
                }
            }
            writer.write(attrs);
            writer.write(">");
        }
        writer.write("</center>");
    }
}