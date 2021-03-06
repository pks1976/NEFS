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
package com.netspective.sparx.value.source;

import java.io.File;

import com.netspective.axiom.ConnectionContext;
import com.netspective.commons.RuntimeEnvironmentFlags;
import com.netspective.commons.value.GenericValue;
import com.netspective.commons.value.PresentationValue;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueContext;
import com.netspective.commons.value.ValueSourceDocumentation;
import com.netspective.commons.value.ValueSourceSpecification;
import com.netspective.commons.value.exception.ValueSourceInitializeException;
import com.netspective.commons.value.source.AbstractValueSource;
import com.netspective.sparx.value.HttpServletValueContext;

public class ServletContextFileValueSource extends AbstractValueSource
{
    public static final String[] IDENTIFIERS = new String[]{"servlet-context-file", "servlet-context-path"};
    public static final ValueSourceDocumentation DOCUMENTATION = new ValueSourceDocumentation("Creates a file/directory path relative to the servlet context root.",
                                                                                              new ValueSourceDocumentation.Parameter[]
                                                                                              {
                                                                                                  new ValueSourceDocumentation.Parameter("relative-path", true, "The relative path of the file or directory.")
                                                                                              });

    private boolean root;

    public static String[] getIdentifiers()
    {
        return IDENTIFIERS;
    }

    public static ValueSourceDocumentation getDocumentation()
    {
        return DOCUMENTATION;
    }

    public void initialize(ValueSourceSpecification spec) throws ValueSourceInitializeException
    {
        super.initialize(spec);
        root = "/".equals(spec.getParams());
    }

    public PresentationValue getPresentationValue(ValueContext vc)
    {
        return new PresentationValue(getValue(vc));
    }

    public Value getValue(ValueContext vc)
    {
        final HttpServletValueContext svc = (HttpServletValueContext)
                (vc instanceof ConnectionContext ? ((ConnectionContext) vc).getDatabaseValueContext() :
                 vc);

        if(vc.getRuntimeEnvironmentFlags().flagIsSet(RuntimeEnvironmentFlags.ANT_BUILD))
        {
            String simulatingProp = System.getProperty("com.netspective.sparx.value.source.ServletContextPathValue.simulate");
            if(simulatingProp == null)
                throw new RuntimeException("Running inside Ant but no 'com.netspective.sparx.value.source.ServletContextPathValue.simulate' system property provided.");
            File simulatedPath = new File(simulatingProp);
            return new GenericValue(root ?
                                    simulatedPath.getAbsolutePath() :
                                    new File(simulatedPath, spec.getParams()).getAbsolutePath());
        }
        else
        {
            return new GenericValue(root ?
                                    svc.getHttpServlet().getServletContext().getRealPath("") :
                                    svc.getHttpServlet().getServletContext().getRealPath(spec.getParams()));
        }
    }

    public boolean hasValue(ValueContext vc)
    {
        return true;
    }
}