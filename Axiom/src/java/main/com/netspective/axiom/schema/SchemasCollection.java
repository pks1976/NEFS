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
 * $Id: SchemasCollection.java,v 1.2 2003-06-28 00:47:06 shahid.shah Exp $
 */

package com.netspective.axiom.schema;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.netspective.axiom.schema.Schemas;
import com.netspective.axiom.schema.Schema;
import com.netspective.axiom.schema.BasicSchema;
import com.netspective.axiom.schema.table.BasicTable;

public class SchemasCollection implements Schemas
{
    private List schemas = new ArrayList();
    private Map mapByName = new TreeMap();
    private Map mapByNameOrXmlNodeName = new TreeMap();
    private boolean modified;

    public void add(Schema schema)
    {
        schemas.add(schema);
        mapByName.put(schema.getNameForMapKey(), schema);
        mapByNameOrXmlNodeName.put(schema.getNameForMapKey(), schema);
        mapByNameOrXmlNodeName.put(BasicTable.translateTableNameForMapKey(schema.getXmlNodeName()), schema);
        modified = true;
    }

    public void remove(Schema schema)
    {
        schemas.remove(schema);
        mapByName.remove(schema.getNameForMapKey());
        mapByNameOrXmlNodeName.remove(BasicTable.translateTableNameForMapKey(schema.getXmlNodeName()));
        modified = true;
    }

    public boolean isModified()
    {
        return modified;
    }

    public Schema get(int i)
    {
        return (Schema) schemas.get(i);
    }

    public Schema getByName(String name)
    {
        return (Schema) mapByName.get(BasicSchema.translateNameForMapKey(name));
    }

    public Schema getByNameOrXmlNodeName(String name)
    {
        return (Schema) mapByNameOrXmlNodeName.get(BasicSchema.translateNameForMapKey(name));
    }

    public int size()
    {
        return schemas.size();
    }

    public Set getNames()
    {
        return mapByName.keySet();
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < schemas.size(); i++)
        {
            sb.append(schemas.get(i).toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
