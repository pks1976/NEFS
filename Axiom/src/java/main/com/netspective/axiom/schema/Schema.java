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
 * $Id: Schema.java,v 1.1 2003-03-13 18:25:41 shahid.shah Exp $
 */

package com.netspective.axiom.schema;

import com.netspective.axiom.schema.constraint.ParentForeignKey;

import java.io.Writer;
import java.io.IOException;
import java.io.File;
import java.util.List;

public interface Schema
{
    public String getName();
    public String getNameForMapKey();
    public String getXmlNodeName();

    public String getPresentationTemplatesNameSpaceId();
    public String getDataTypesTemplatesNameSpaceId();
    public String getTableTypesTemplatesNameSpaceId();

    public void setName(String name);
    public void setXmlNodeName(String xmlNodeName);

    /**
     * Factory method to construct a new table instance in this schema.
     */
    public Table createTable();

    /**
     * Factory method to register a table instance.
     */
    public void addTable(Table table);

    /**
     * Returns all tables registered in the schema
     */
    public Tables getTables();

    /**
     * Returns all tables registered in the schema that are designed to store application data. This would include
     * all tables except enumeration and reference tables.
     */
    public Tables getApplicationTables();

    /**
     * Generate the graphvit DOT file that can be used to prepare an ERD
     */
    public void generateGraphVizErd(Writer writer) throws IOException;

    /**
     * Obtains the table structure of this schema -- while tables already understand parent/child relationships,
     * because tables can have multiple parents dealing with the relationships within tables becomes cumbersome. The
     * TableTree structure returned by this method is far more convenient because a single tree with all parent/child
     * and ancestor relationships are returned and appropriate references to the actual underlying tables are made
     * available.
     */
    public TableTree getStructure();

    /**
     * Using the structure of this schema, generate a data access layer (lightweight data access objects) that wraps
     * the tables, columns, and foreign keys in the schema.
     * @param rootDir The physical location of the .java class files
     * @param dalClassPackageName The package name of the main data access layer class
     * @param dalClassName The class name of the main data access layer class
     * @return The generator class used to generate the data access layer
     * @throws IOException
     */
    public DataAccessLayerGenerator generateDataAccessLayer(File rootDir, String dalClassPackageName, String dalClassName) throws IOException;

    public interface TableTreeNode
    {
        /**
         * Return the table for which structural information was gathered
         */
        public Table getTable();

        /**
         * Return the parent node for this node
         */
        public TableTreeNode getParentNode();

        /**
         * Get all the children for this node -- each item in the list is a TableTreeNode
         */
        public List getChildren();

        /**
         * Get all the ancestors for this node -- each item in the list is a TableTreeNode
         */
        public List getAncestors();

        /**
         * Returns a string with the names of all the ancestor tables delimited by the given delimiter.
         */
        public String getAncestorTableNames(String delimiter);

        /**
         * Returns a the foreign key reference if this tree node is connected to its parent by a ParentForeignKey. If
         * the relationship with the parent is only a hierarchical relationship (meaning no real column is connecting
         * the data) then this method will return null.
         */
        public ParentForeignKey getParentForeignKey();

        /**
         * Returns true if this node has any children.
         */
        public boolean hasChildren();

        /**
         * Returns true if this node has any children have have any children.
         */
        public boolean hasGrandchildren();
    }

    public interface TableTree
    {
        /**
         * Return the schema for which structural information was gathered
         */
        public Schema getSchema();

        /**
         * Get all the children for the tree -- each item in the list is a TableTreeNode that comprise the "main"
         * tables (entry points) for the hierarchy.
         */
        public List getChildren();

        /**
         * Generate the DataAccessLayer for this tree
         */
        public void generateDataAccessLayer(DataAccessLayerGenerator generator);
    }
}
