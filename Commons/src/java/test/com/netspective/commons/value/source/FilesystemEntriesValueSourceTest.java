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
package com.netspective.commons.value.source;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.netspective.commons.value.PresentationValue;
import com.netspective.commons.value.Value;
import com.netspective.commons.value.ValueSource;
import com.netspective.commons.value.ValueSourceSpecification;
import com.netspective.commons.value.ValueSources;

import junit.framework.TestCase;

public class FilesystemEntriesValueSourceTest extends TestCase
{
    public void testGetValue()
    {
        File[] roots = File.listRoots();
        assertNotNull(roots);
        assertTrue(roots.length > 0);

        String rootPath = null;
        for(int i = 0; i < roots.length; i++)
        {
            rootPath = roots[i].getAbsolutePath();
            if(new File(rootPath).exists())
                break;
        }
        assertTrue(rootPath + " does not seem to exist.", new File(rootPath).exists());

        String unescapedRootPath = rootPath;
        rootPath = ValueSourceSpecification.escapeFirstDelim(rootPath);

        ValueSource vs = ValueSources.getInstance().getValueSource("filesystem-entries:" + rootPath, ValueSources.VSNOTFOUNDHANDLER_THROW_EXCEPTION);
        FilesystemEntriesValueSource fsVS = new FilesystemEntriesValueSource();
        fsVS.setRootPath(rootPath);
        Value value = vs.getValue(null);

        assertEquals(unescapedRootPath, fsVS.getRootPath().getTextValue(null));
        // Verify the presence of the default filter...
        assertEquals("/.*/", fsVS.getFilter());
        assertTrue(0 < vs.getTextValues(null).length);
        assertNotNull(vs.getTextValueOrBlank(null));

        String[] theValue = value.getTextValues();
        PresentationValue.Items altValueItems = fsVS.getPresentationValue(null).getItems();

        assertEquals(theValue.length, altValueItems.size());
        assertFalse(fsVS.isPathInSelection());
        for(int i = 0; i < theValue.length; i++)
        {
            assertFalse(altValueItems.contains(theValue[i]));
        }

        fsVS.setIncludePathInSelection(true);
        PresentationValue.Items altValueItemsWithPath = fsVS.getPresentationValue(null).getItems();

        assertEquals(theValue.length, altValueItemsWithPath.size());
        assertTrue(fsVS.isPathInSelection());
        for(int i = 0; i < theValue.length; i++)
        {
            assertEquals(theValue[i], altValueItemsWithPath.getItem(i).getValue());
            assertEquals(altValueItems.getItem(i).getValue(), altValueItemsWithPath.getItem(i).getCaption());
        }

        // Try the same tests with a filter...
        // only files/dirs that contain the letter s in them...
        fsVS.setFilter("[sS]");
        assertEquals("/[sS]/", fsVS.getFilter());
        assertTrue(fsVS.isPathInSelection());

        PresentationValue.Items filterValueItems = fsVS.getPresentationValue(null).getItems();
        assertTrue(filterValueItems.size() <= altValueItems.size());

        List altValueWithPathList = new ArrayList();
        for(int i = 0; i < altValueItemsWithPath.size(); i++)
            altValueWithPathList.add(altValueItemsWithPath.getItem(i).getValue());

        assertEquals(altValueItemsWithPath.size(), altValueWithPathList.size());

        for(int i = 0; i < filterValueItems.size(); i++)
            assertTrue(altValueWithPathList.contains(filterValueItems.getItem(i).getValue()));


        vs = ValueSources.getInstance().getValueSource("filesystem-entries:" + rootPath + ",exe$", ValueSources.VSNOTFOUNDHANDLER_THROW_EXCEPTION);
        value = vs.getValue(null);
    }
}
