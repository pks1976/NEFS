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
 * $Id: TabularReportFrame.java,v 1.1 2003-04-02 22:53:51 shahid.shah Exp $
 */

package com.netspective.sparx.report.tabular;

import com.netspective.commons.value.ValueSource;
import com.netspective.commons.xdm.XdmBitmaskedFlagsAttribute;
import com.netspective.sparx.report.tabular.TabularReportAction;
import com.netspective.sparx.report.tabular.TabularReportActions;

public class TabularReportFrame
{
    public static class Flags extends XdmBitmaskedFlagsAttribute
    {
        public static final int HAS_HEADING = 1;
        public static final int HAS_HEADING_EXTRA = HAS_HEADING * 2;
        public static final int HAS_FOOTING = HAS_HEADING_EXTRA * 2;
        public static final int IS_SELECTABLE = HAS_FOOTING * 2;
        public static final int ALLOW_COLLAPSE = IS_SELECTABLE * 2;
        public static final int IS_COLLAPSED = ALLOW_COLLAPSE * 2;

        public static final XdmBitmaskedFlagsAttribute.FlagDefn[] FLAGDEFNS = new XdmBitmaskedFlagsAttribute.FlagDefn[]
        {
            new XdmBitmaskedFlagsAttribute.FlagDefn("HAS_HEADING", HAS_HEADING),
            new XdmBitmaskedFlagsAttribute.FlagDefn("HAS_HEADING_EXTRA", HAS_HEADING_EXTRA),
            new XdmBitmaskedFlagsAttribute.FlagDefn("HAS_FOOTING", HAS_FOOTING),
            new XdmBitmaskedFlagsAttribute.FlagDefn("IS_SELECTABLE", IS_SELECTABLE),
            new XdmBitmaskedFlagsAttribute.FlagDefn("ALLOW_COLLAPSE", ALLOW_COLLAPSE),
            new XdmBitmaskedFlagsAttribute.FlagDefn("IS_COLLAPSED", IS_COLLAPSED),
        };

        public XdmBitmaskedFlagsAttribute.FlagDefn[] getFlagsDefns()
        {
            return FLAGDEFNS;
        }
    }

    private ValueSource heading;
    private ValueSource footing;
    private ValueSource allowSelect;
    private Flags flags = new Flags();
    private TabularReportActions actions = new TabularReportActions();

    public TabularReportFrame()
    {
    }

    public Flags getFlags()
    {
        return flags;
    }

    public void setFlags(Flags flags)
    {
        this.flags.setFlag(flags.getFlags());
    }

    public ValueSource getAllowSelect()
    {
        return allowSelect;
    }

    public void setAllowSelect(ValueSource vs)
    {
        allowSelect = vs;
        flags.updateFlag(Flags.IS_SELECTABLE, allowSelect != null);
    }

    public boolean hasHeadingOrFooting()
    {
        return heading != null || footing != null;
    }

    public ValueSource getHeading()
    {
        return heading;
    }

    public void setHeading(ValueSource vs)
    {
        heading = vs;
        flags.updateFlag(Flags.HAS_HEADING, heading != null);
    }

    public ValueSource getFooting()
    {
        return footing;
    }

    public void setFooting(ValueSource footing)
    {
        this.footing = footing;
        flags.updateFlag(Flags.HAS_FOOTING, footing != null);
    }

    public TabularReportActions getActions()
    {
        return actions;
    }

    public TabularReportAction createAction()
    {
        return new TabularReportAction();
    }

    public void addAction(TabularReportAction item)
    {
        actions.add(item);
    }
}