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
package com.netspective.axiom.sql.dynamic;

import com.netspective.axiom.sql.dynamic.exception.QueryDefinitionException;
import com.netspective.axiom.sql.dynamic.exception.QueryDefnJoinNotFoundException;
import com.netspective.commons.xdm.XmlDataModelSchema;

/**
 * Class handling the Selectable Fields, in a Query-Definition, as defined by &lt;field&gt; tag.
 */
public class QueryDefnField
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options();

    {
        XML_DATA_MODEL_SCHEMA_OPTIONS.setIgnorePcData(true);
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreAttributes(new String[]{"select-clause-expr-and-label"});
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreNestedElements(new String[]{"presentation"});
    }

    private QueryDefinition owner;
    private String name;
    private String caption;
    private String column;
    private String selectClauseExpr;
    private String selectClauseExprAndLabel;
    private String whereClauseExpr;
    private String orderByClauseExpr;
    private String join;
    private QueryDefnJoin joinDefn;
    private boolean allowDisplay = true;

    public QueryDefnField(QueryDefinition owner)
    {
        this.owner = owner;
    }

    public String getName()
    {
        return name;
    }

    public String getCaption()
    {
        return caption;
    }

    public String getColumn()
    {
        return column;
    }

    public String getColumnAlias()
    {
        return name;
    }

    public String getColumnLabel()
    {
        return caption == null ? name : caption;
    }

    public String getQualifiedColName() throws QueryDefinitionException
    {
        String tableAlias = getTableAlias();
        return tableAlias != null ? (tableAlias + "." + getColumn()) : getColumn();
    }

    public String getTableName() throws QueryDefinitionException
    {
        QueryDefnJoin join = getJoin();
        return join != null ? join.getTable() : null;
    }

    public String getTableAlias() throws QueryDefinitionException
    {
        QueryDefnJoin join = getJoin();
        return join != null ? join.getName() : null;
    }

    public String getSelectClauseExprAndLabel() throws QueryDefinitionException
    {
        return allowDisplay
               ? (selectClauseExprAndLabel != null
                  ? selectClauseExprAndLabel : (getColumnExpr() + " as \"" + getColumnLabel() + "\""))
               : null;
    }

    public String getColumnExpr() throws QueryDefinitionException
    {
        return allowDisplay ? (selectClauseExpr != null ? selectClauseExpr : getQualifiedColName()) : null;
    }

    public String getWhereExpr() throws QueryDefinitionException
    {
        return whereClauseExpr != null ? whereClauseExpr : getQualifiedColName();
    }

    public String getOrderByExpr() throws QueryDefinitionException
    {
        return orderByClauseExpr != null ? orderByClauseExpr : getQualifiedColName();
    }

    public QueryDefnJoin getJoin() throws QueryDefinitionException
    {
        if(join != null && joinDefn == null)
        {
            joinDefn = owner.getJoins().get(join);
            if(joinDefn == null)
                throw new QueryDefnJoinNotFoundException(owner, join, "join '" + join + "' not found in field '" + getName() + "'");
        }
        return joinDefn;
    }

    /**
     * Used to define a different name for the schema table field (column) that is
     * being used as a selectable field.
     *
     * @param name new name string for the schema table field (column)
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Sets the caption for a query-defn selectable field.
     *
     * @param caption caption for the field as displayed in the select dialog
     */
    public void setCaption(String caption)
    {
        this.caption = caption;
    }

    /**
     * Sets the field name, for a query-defn selectable field, as it is defined
     * in the schema table.
     */
    public void setColumn(String columnName)
    {
        this.column = columnName;
    }

    public void setColumnExpr(String selectClauseExpr)
    {
        this.selectClauseExpr = selectClauseExpr;
    }

    public void setSelectClauseExprAndLabel(String selectClauseExprAndLabel)
    {
        this.selectClauseExprAndLabel = selectClauseExprAndLabel;
    }

    public void setWhereExpr(String whereClauseExpr)
    {
        this.whereClauseExpr = whereClauseExpr;
    }

    public void setOrderByExpr(String orderByClauseExpr)
    {
        this.orderByClauseExpr = orderByClauseExpr;
    }

    /**
     * Sets a reference to a join tag which is defined later in the query-defn.
     *
     * @param join name of a join tag defined within a query-defn
     */
    public void setJoin(String join)
    {
        this.join = join;
    }

    public void setJoinDefn(QueryDefnJoin joinDefn)
    {
        this.joinDefn = joinDefn;
    }

    public void setAllowDisplay(boolean allowDisplay)
    {
        this.allowDisplay = allowDisplay;
    }
}