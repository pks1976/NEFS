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
 * $Id: QueryDefnSelect.java,v 1.1 2003-03-13 18:25:44 shahid.shah Exp $
 */

package com.netspective.axiom.sql.dynamic;

import java.util.List;
import java.sql.SQLException;

import javax.naming.NamingException;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.axiom.sql.Query;
import com.netspective.axiom.sql.dynamic.exception.QueryDefinitionException;
import com.netspective.axiom.sql.dynamic.exception.QueryDefnFieldNotFoundException;
import com.netspective.axiom.ConnectionContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class QueryDefnSelect extends Query
{
    private static final Log log = LogFactory.getLog(Query.class);
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options(Query.XML_DATA_MODEL_SCHEMA_OPTIONS);
    static
    {
        XML_DATA_MODEL_SCHEMA_OPTIONS.setIgnorePcData(true);
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreAttributes(new String[] { "always-dirty", "is-dirty" });
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreNestedElements(new String[] { "params" });
    }

    private QueryDefinition queryDefn;
    private boolean distinctRows;
    private boolean isDirty;
    private boolean alwaysDirty;
    private QueryDefnFields displayFields = new QueryDefnFields();
    private QueryDefnSortFieldReferences orderByFieldRefs = new QueryDefnSortFieldReferences();
    private QueryDefnFields groupByFields = new QueryDefnFields();
    private QueryDefnConditions conditions = new QueryDefnConditions(null);
    private QueryDefnSqlWhereExpressions whereExprs = new QueryDefnSqlWhereExpressions();
    private List bindParams;
    private String whereClauseSql;

    public QueryDefnSelect(QueryDefinition queryDefn)
    {
        super(queryDefn);
        this.isDirty = true;
        this.distinctRows = true;
        this.queryDefn = queryDefn;
    }

    public String getQualifiedName()
    {
        return getName();
    }

    public void setAlwaysDirty(boolean flag)
    {
        alwaysDirty = flag;
    }

    public void setDistinct(boolean distinctRows)
    {
        this.distinctRows = distinctRows;
    }

    public boolean distinctRowsOnly()
    {
        return distinctRows;
    }

    public QueryDefinition getQueryDefn()
    {
        return queryDefn;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public QueryDefnFieldReference createGroupBy()
    {
        return new QueryDefnFieldReference(this.queryDefn);
    }

    public void addGroupBy(QueryDefnFieldReference fieldRef) throws QueryDefinitionException
    {
        QueryDefnField field = fieldRef.findFieldInstance();
        if(field == null)
            throw new QueryDefnFieldNotFoundException(this.queryDefn, fieldRef.getField(), "Field '"+ fieldRef.getField() +"' not found in "+ this.getClass().getName() +" <group-by> tag");
        groupByFields.add(field);
        isDirty = true;
    }

    public QueryDefnFields getGroupByFields()
    {
        return groupByFields;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public QueryDefnSortFieldReference createOrderBy()
    {
        return new QueryDefnSortFieldReference(this.queryDefn);
    }

    public void addOrderBy(QueryDefnSortFieldReference field)
    {
        orderByFieldRefs.add(field);
        if(field.isStatic())
            isDirty = true;
        else
            alwaysDirty = true;
    }

    public QueryDefnSortFieldReferences getOrderByFieldRefs()
    {
        return orderByFieldRefs;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public QueryDefnFieldReference createDisplay()
    {
        return new QueryDefnFieldReference(this.queryDefn);
    }

    public void addDisplay(QueryDefnFieldReference fieldRef) throws QueryDefinitionException
    {
        QueryDefnField field = fieldRef.findFieldInstance();
        if(field == null)
            throw new QueryDefnFieldNotFoundException(this.queryDefn, fieldRef.getField(), "Field '"+ fieldRef.getField() +"' not found in "+ this.getClass().getName() +" <display> tag");
        displayFields.add(field);
    }

    public QueryDefnFields getDisplayFields()
    {
        return displayFields;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public QueryDefnSqlWhereExpression createWhereExpr()
    {
        return new QueryDefnSqlWhereExpression(this.queryDefn);
    }

    public void addWhereExpr(QueryDefnSqlWhereExpression expr)
    {
        whereExprs.add(expr);
        isDirty = true;
    }

    public QueryDefnSqlWhereExpressions getWhereExprs()
    {
        return whereExprs;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public QueryDefnCondition createCondition()
    {
        return new QueryDefnCondition(queryDefn);
    }

    public void addCondition(QueryDefnCondition condition)
    {
        if(condition.removeIfValueIsNull())
            alwaysDirty = true;

        conditions.add(condition);
        isDirty = true;
    }

    public QueryDefnConditions getConditions()
    {
        return conditions;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public List getBindParams()
    {
        return bindParams;
    }

    /* ------------------------------------------------------------------------------------------------------------- */

    public String getWhereClauseSql()
    {
        return whereClauseSql;
    }

    public String getSqlText(ConnectionContext cc) throws NamingException, SQLException
    {
        if(isDirty || alwaysDirty)
        {
            QueryDefnSelectStmtGenerator selectStmt = cc.getDatabasePolicy().createSelectStatementGenerator(this);
            try
            {
                setSqlText(selectStmt.generateSql(cc));
                whereClauseSql = selectStmt.getWhereClauseSql();
            }
            catch (QueryDefinitionException e)
            {
                log.error("Unable to generate a valid SQL statement", e);
                return null;
            }

            bindParams = selectStmt.getBindParams();
            isDirty = false;
        }
        return super.getSqlText(cc);
    }

}