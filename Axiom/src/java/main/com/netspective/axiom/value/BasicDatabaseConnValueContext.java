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
 * $Id: BasicDatabaseConnValueContext.java,v 1.5 2003-08-31 22:42:16 shahid.shah Exp $
 */

package com.netspective.axiom.value;

import java.sql.SQLException;
import javax.naming.NamingException;

import org.apache.commons.discovery.tools.DiscoverSingleton;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import com.netspective.commons.value.DefaultValueContext;
import com.netspective.commons.acl.AccessControlListsManager;
import com.netspective.commons.config.ConfigurationsManager;
import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.ConnectionProvider;
import com.netspective.axiom.SqlManager;
import com.netspective.axiom.connection.JndiConnectionProvider;
import com.netspective.axiom.connection.AutoCommitConnectionContext;
import com.netspective.axiom.connection.TransactionConnectionContext;

public class BasicDatabaseConnValueContext extends DefaultValueContext implements DatabaseConnValueContext
{
    private static final Log log = LogFactory.getLog(BasicDatabaseConnValueContext.class);
    public static final ConnectionProvider DEFAULT_CONN_PROVIDER = (ConnectionProvider) DiscoverSingleton.find(ConnectionProvider.class, JndiConnectionProvider.class.getName());
    private ConnectionProvider provider = DEFAULT_CONN_PROVIDER;
	protected String defaultDataSource = DatabaseConnValueContext.DATASRCID_DEFAULT_DATA_SOURCE;

    public ConnectionContext getConnection(String dataSourceId, boolean transaction) throws NamingException, SQLException
    {
        ConnectionContext result = null;
        if(transaction)
            result = new TransactionConnectionContext(dataSourceId, this);
        else
            result = new AutoCommitConnectionContext(dataSourceId, this);

        if(log.isTraceEnabled())
            log.trace("Obtained " + result + " for data source '"+ result.getDataSourceId() +"'.");

        return result;
    }

    public ConnectionProvider getConnectionProvider()
    {
        return provider;
    }

    public void returnConnection(ConnectionContext cc) throws SQLException
    {
        if(log.isTraceEnabled())
            log.trace("Returned " + cc + " for data source '"+ cc.getDataSourceId() +"'.");
        cc.close();
    }

    public void setConnectionProvider(ConnectionProvider provider)
    {
        this.provider = provider;
    }

    public String translateDataSourceId(String dataSourceId)
    {
        return dataSourceId;
    }

    public AccessControlListsManager getAccessControlListsManager()
    {
        return getSqlManager();
    }

    public ConfigurationsManager getConfigurationsManager()
    {
        return getSqlManager();
    }

    public SqlManager getSqlManager()
    {
        return null;
    }

	/**
	 * Sets the default data source to a user-defined string.
	 * @param defaultDataSource A string representing the new data source
	 */
	public void setDefaultDataSource (String defaultDataSource)
	{
		this.defaultDataSource = defaultDataSource;
	}

	/**
	 * Retrieves the default data source
	 * @return A string representing the default data source
	 */
	public String getDefaultDataSource ()
	{
		return null == defaultDataSource ? DatabaseConnValueContext.DATASRCID_DEFAULT_DATA_SOURCE : defaultDataSource;
	}
}
