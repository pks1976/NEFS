package com.netspective.support.fmpp;

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

/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

import java.util.List;
import java.io.File;

import com.netspective.commons.xdm.XdmComponent;
import com.netspective.commons.xdm.XdmComponentFactory;
import com.netspective.sparx.ProjectComponent;

import fmpp.tdd.DataLoader;
import fmpp.Engine;

public class SparxProjectLoader implements DataLoader
{
    public Object load(final Engine engine, final List args) throws Exception
    {
        final int argCount = args.size();
        if (argCount != 1)
            throw new IllegalArgumentException("SparxProjectLoader(projectFileName) needs 1 parameter.");

        final Object obj = args.get(0);
        if (!(obj instanceof String))
            throw new IllegalArgumentException(
                    "The first parameter to SparxProjectLoader(projectFileName) must be a string.");

        final String path = (String) obj;
        File projectFile = new File(path);
        if (!projectFile.isAbsolute())
            projectFile = new File(engine.getDataRoot(), path);

        XdmComponent component = null;
        int flags = XdmComponentFactory.XDMCOMPFLAG_ALLOWRELOAD | XdmComponentFactory.XDMCOMPFLAG_INSIDE_ANT;

        if(projectFile != null)
            component = XdmComponentFactory.get(ProjectComponent.class, projectFile, flags);

        System.out.println("Loaded " + projectFile + " with " + component.getErrors().size() + " errors.");

        final List errors = component.getErrors();
        if(errors.size() > 0)
        {
            for(int i = 0; i < errors.size(); i++)
            {
                final Object error = errors.get(i);
                if(error instanceof Exception)
                    throw (Exception) error;
            }
        }

        return component;
    }
}
