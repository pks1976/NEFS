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
 * $Id: ColumnValue.java,v 1.1 2003-03-13 18:25:40 shahid.shah Exp $
 */

package com.netspective.axiom.schema;

import java.sql.SQLException;
import javax.naming.NamingException;

import org.inxar.jenesis.ClassDeclaration;

import com.netspective.axiom.sql.DbmsSqlText;
import com.netspective.axiom.sql.DbmsSqlTexts;
import com.netspective.axiom.schema.table.type.EnumerationTableRow;
import com.netspective.axiom.ConnectionContext;
import com.netspective.commons.value.Value;

public interface ColumnValue extends Value
{
    /**
     * Sets the value that is used by the application for processing by copying the given value.
     */
    public void setValue(ColumnValue value);

    /**
     * Returns the Column for which this value is being defined
     */
    public Column getColumn();

    /**
     * Returns the first row associated with the foreign key of this value
     */
    public Row getReferencedForeignKeyRow(ConnectionContext cc) throws NamingException, SQLException;

    /**
     * Returns all the rows associated with the foreign key of this value
     */
    public Rows getReferencedForeignKeyRows(ConnectionContext cc) throws NamingException, SQLException;

    /**
     * Wrapper method for getForeignKeyRow() that works for enumeration tables.
     */
    public EnumerationTableRow getReferencedEnumRow();

    /**
     * Ascertains whether this column value is a SQL expression that should be placed in the output as-is or a real
     * value that should be bound to a statement.
     */
    public boolean isSqlExpr();

    /**
     * Asks the value provider to create an SQL expression
     */
    public DbmsSqlText createSqlExpr();

    /**
     * Adds a SQL expression to the value object
     */
    public void addSqlExpr(DbmsSqlText sqlText);

    /**
     * Provides the SQL expressions defined
     */
    public DbmsSqlTexts getSqlExprs();
}
