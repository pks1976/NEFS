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
 * $Id: BasicSchema.java,v 1.1 2003-03-13 18:25:40 shahid.shah Exp $
 */

package com.netspective.axiom.schema;

import java.io.Writer;
import java.io.IOException;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import com.netspective.axiom.schema.Column;
import com.netspective.axiom.schema.Schema;
import com.netspective.axiom.schema.Table;
import com.netspective.axiom.schema.Tables;
import com.netspective.axiom.schema.Columns;
import com.netspective.axiom.schema.table.BasicTable;
import com.netspective.axiom.schema.table.TablesCollection;
import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.commons.xdm.exception.DataModelException;
import com.netspective.commons.xml.template.TemplateProducerParent;
import com.netspective.commons.xml.template.TemplateProducer;
import com.netspective.commons.xml.template.TemplateProducers;

public class BasicSchema implements Schema, TemplateProducerParent, XmlDataModelSchema.ConstructionFinalizeListener
{
    private static final Log log = LogFactory.getLog(BasicSchema.class);
    public static final String TEMPLATEELEMNAME_DATA_TYPE = "data-type";
    public static final String TEMPLATEELEMNAME_TABLE_TYPE = "table-type";
    public static final String TEMPLATEELEMNAME_PRESENTATION = "presentation";
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options();

    static
    {
        XML_DATA_MODEL_SCHEMA_OPTIONS.setIgnorePcData(true);
    }

    private String name;
    private String xmlNodeName;
    private Tables tables = new TablesCollection();
    private TemplateProducers templateProducers;
    private Schema.TableTree structure;

    public BasicSchema()
    {
    }

    public String getPresentationTemplatesNameSpaceId()
    {
        return "/schema/" + getName() + "/presentation";
    }

    public String getDataTypesTemplatesNameSpaceId()
    {
        return "/schema/" + getName() + "/data-type";
    }

    public String getTableTypesTemplatesNameSpaceId()
    {
        return "/schema/" + getName() + "/table-type";
    }

    public TemplateProducers getTemplateProducers()
    {
        if(templateProducers == null)
        {
            templateProducers = new TemplateProducers();
            templateProducers.add(new TemplateProducer(getDataTypesTemplatesNameSpaceId(), TEMPLATEELEMNAME_DATA_TYPE, "name", "type", false));
            templateProducers.add(new TemplateProducer(getTableTypesTemplatesNameSpaceId(), TEMPLATEELEMNAME_TABLE_TYPE, "name", "type", false));
        }
        return templateProducers;
    }

    /**
     * This method will be called by the DataModelSchema automatically when all the children have been added. We call
     * the other methods "completeConstruction" so they are not called automatically by the DataModelSchema. We want to
     * do all construction finalization in this method only.
     */
    public void finalizeConstruction() throws DataModelException
    {
        getTables().finishConstruction();
    }

    public static String translateNameForMapKey(String name)
    {
        return name.toUpperCase();
    }

    public String getName()
    {
        return name;
    }

    public String getNameForMapKey()
    {
        return translateNameForMapKey(name);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getXmlNodeName()
    {
        return xmlNodeName == null ? name : xmlNodeName;
    }

    public void setXmlNodeName(String xmlNodeName)
    {
        this.xmlNodeName = xmlNodeName;
    }

    public void addTable(Table table)
    {
        tables.add(table);
    }

    public Table createTable()
    {
        return new BasicTable(this);
    }

    public Table createTable(Class cls) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
    {
        if(Table.class.isAssignableFrom(cls))
        {
            Constructor c = cls.getConstructor(new Class[] { Schema.class });
            return (Table) c.newInstance(new Object[] { this });
        }
        else
            throw new RuntimeException("Don't know what to do with with class: " + cls);
    }

    public Tables getTables()
    {
        return tables;
    }

    public Tables getApplicationTables()
    {
        Tables result = new TablesCollection();
        for(int i = 0; i < tables.size(); i++)
        {
            Table table = tables.get(i);
            if(table.isApplicationTable())
                result.add(table);
        }
        return result;
    }

    public Column getColumn(String tableName, String tableColumn)
    {
        Table table = tables.getByName(tableName);
        return table != null ? table.getColumns().getByName(tableColumn) : null;
    }

    public void generateGraphVizErd(Writer writer) throws IOException
    {
        writer.write("digraph ");
        writer.write(getName());
        writer.write("\n");
        writer.write("{\n");

        String indent = "    ";

        Tables tables = getTables();
        for(int i = 0; i < tables.size(); i++)
        {
            Table table = tables.get(i);
            if(table.isApplicationTable())
            {
                writer.write(indent);
                writer.write(table.getName());
                writer.write(";\n");
            }
        }

        writer.write("\n");

        for(int i = 0; i < tables.size(); i++)
        {
            Table table = tables.get(i);
            if(table.isApplicationTable())
            {
                Columns columns = table.getParentRefColumns();
                for(int c = 0; c < columns.size(); c++)
                {
                    Column source = columns.get(c);
                    Column referenced = source.getForeignKey().getReferencedColumn();

                    writer.write(indent);
                    writer.write(table.getName());
                    writer.write(" -> ");
                    writer.write(referenced.getTable().getName());
                    if(! source.getName().equalsIgnoreCase(referenced.getName()))
                        writer.write(" [label=\""+ source.getName() + "=" + referenced.getName() + "\"]");
                    writer.write(";\n");
                }
            }
        }

        writer.write("}\n");
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public String toString()
    {
        return tables.toString();
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public Schema.TableTree getStructure()
    {
        if(structure == null)
            structure = new BasicTableTree();

        return structure;
    }

    public DataAccessLayerGenerator generateDataAccessLayer(File rootDir, String rootNameSpace, String dalClassName) throws IOException
    {
        DataAccessLayerGenerator dalGen = new DataAccessLayerGenerator(getStructure(), rootDir, rootNameSpace, dalClassName);
        dalGen.generate();
        return dalGen;
    }

    protected class BasicTableTree implements TableTree
    {
        private List children = new ArrayList();

        public BasicTableTree()
        {
            Tables tables = getTables();
            for(int i = 0; i < tables.size(); i++)
            {
                Table table = tables.get(i);
                if(table.isApplicationTable() && ! table.isChildTable())
                    children.add(table.createTreeNode(this, null, 1));
            }
        }

        public Schema getSchema()
        {
            return BasicSchema.this;
        }

        public List getChildren()
        {
            return children;
        }

        public String toString()
        {
            StringBuffer sb = new StringBuffer();

            sb.append(getSchema().getName());
            sb.append("\n");

            for(int c = 0; c < children.size(); c++)
                sb.append(children.get(c));

            return sb.toString();
        }

        public void generateDataAccessLayer(DataAccessLayerGenerator generator)
        {
        }
    }
}
