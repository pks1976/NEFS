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

package com.netspective.commons.report.tabular.column;

import com.netspective.commons.report.tabular.TabularReportValueContext;
import com.netspective.commons.report.tabular.TabularReportDataSource;
import com.netspective.commons.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Repor column class for use with database CLOB column values
 */
public class ClobColumn  extends GeneralColumn
{

    /**
     * Gets the formatted data for the column. The data will be html-safe if the &quot;escape-html&quot; flag was set on the column.
     *
     * @param rc        report context
     * @param ds        data source object
     * @param flags
     * @return          String value and null if an error occurred.
     */
    public String getFormattedData(TabularReportValueContext rc, TabularReportDataSource ds, int flags)
    {
        Object oData = ds.getActiveRowColumnData(getColIndex(), flags);
        String data = null;
        if (oData instanceof java.sql.Clob)
        {
            try
            {
                data = getClobAsString((java.sql.Clob) oData);
            }
            catch (Exception e)
            {
                return null;
            }
        }
        else
            data = oData == null ? "" : oData.toString();

        if(getFlags().flagIsSet(Flags.ESCAPE_HTML) && data != null)
            data = TextUtils.getInstance().escapeHTML(data);

        return data;
    }

    /**
     * Converts a SQL Clob object into a string
     *
     * @param clob      SQL Clob object
     * @return          clob converted to a string
     * @throws SQLException
     * @throws IOException
     */
    public static String getClobAsString(java.sql.Clob clob) throws SQLException, IOException
    {
        if (clob == null)
            return null;
        byte[] buf = new byte[1024];
        ByteArrayOutputStream outputStream = null;
        InputStream in = clob.getAsciiStream();
        int length = -1;
        long read = 0;
        outputStream = new ByteArrayOutputStream();
        while ((length = in.read(buf)) != -1)
        {
            outputStream.write(buf, 0, length);
            read += length;
        }
        in.close();


        return outputStream != null ? new String(outputStream.toByteArray()) : null;
    }

}
