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
package com.netspective.sparx.report.tabular;

import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.source.StaticValueSource;
import com.netspective.commons.xdm.XdmEnumeratedAttribute;
import com.netspective.sparx.panel.HtmlPanelAction;

/**
 * @version $Id: HtmlReportAction.java,v 1.11 2004-08-15 02:27:28 shahid.shah Exp $
 */
public class HtmlReportAction extends HtmlPanelAction
{
    public static class Type extends XdmEnumeratedAttribute
    {
        public static final short RECORD_ADD = 0;
        public static final short RECORD_EDIT = 1;
        public static final short RECORD_DELETE = 2;
        public static final short RECORD_SELECT = 3;

        // this is a more generic report action that can be used to display multiple buttons
        // in the report. For example, a DELETE button. The skin used needs to interpret this
        // as a button and generate  buttons and a form around it. This should only be used
        // for single-page reports.
        public static final short REPORT_SUBMIT = 4;


        public static final String values[] = {"add", "edit", "delete", "select", "submit"};

        public Type()
        {
        }

        public Type(int valueIndex)
        {
            super(valueIndex);
        }

        public static String getValue(int index)
        {
            return values[index];
        }

        public String[] getValues()
        {
            return values;
        }
    }

    private Type type;

    /**
     * Returns the caption of the report action. If there is no caption defined, then the
     * type description is used as the caption.
     */
    public ValueSource getCaption()
    {
        return (super.getCaption() == null ? new StaticValueSource(type.getValue()) : super.getCaption());
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    /**
     * Creates an instance of the class
     */
    public HtmlPanelAction createAction()
    {
        return new HtmlReportAction();
    }

    /**
     * Gets the mouse over title of the action.
     * <p/>
     * NOTE: This method is the same as the getHint() method. This became redundant once
     * the class was refactored to extend from the HtmlPanelAction class.
     *
     * @deprecated
     */
    public ValueSource getTitle()
    {
        return getHint();
    }

    /**
     * Sets the mouseover title of the action
     * <p/>
     * NOTE: This method is the same as the setHint() method. This became redundant once
     * the class was refactored to extend from the HtmlPanelAction class.
     *
     * @deprecated
     */
    public void setTitle(ValueSource title)
    {
        setHint(title);
    }
}
