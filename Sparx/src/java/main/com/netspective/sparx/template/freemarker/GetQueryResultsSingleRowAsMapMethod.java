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
package com.netspective.sparx.template.freemarker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.axiom.sql.Query;
import com.netspective.axiom.sql.QueryResultSet;
import com.netspective.axiom.sql.ResultSetUtils;
import com.netspective.axiom.value.DatabaseConnValueContext;

import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.StringModel;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class GetQueryResultsSingleRowAsMapMethod implements TemplateMethodModel
{
    private static final Log log = LogFactory.getLog(GetQueryResultsSingleRowAsMapMethod.class);
    private final boolean useLabelAsKey;

    public GetQueryResultsSingleRowAsMapMethod(boolean useLabelAsKey)
    {
        this.useLabelAsKey = useLabelAsKey;
    }

    public Object exec(List args) throws TemplateModelException
    {
        if(args.size() < 1)
            throw new TemplateModelException("Wrong arguments: expected query identifier.");

        Environment env = Environment.getCurrentEnvironment();
        StringModel model = null;
        try
        {
            model = (StringModel) env.getDataModel().get("vc");
        }
        catch(TemplateModelException e)
        {
            log.error(e);
        }


        DatabaseConnValueContext vc = (DatabaseConnValueContext) model.getWrappedObject();
        String queryName = (String) args.get(0);

        Query query = vc.getSqlManager().getQuery(queryName);
        if(query == null)
            return null;

        Object[] params = null;
        if(args.size() > 1)
        {
            params = new Object[args.size() - 1];
            for(int i = 1; i < args.size(); i++)
                params[i - 1] = args.get(i);
        }

        try
        {
            Map map = null;
            QueryResultSet qrs = query.execute(vc, params, false);
            if(qrs != null)
            {
                map = ResultSetUtils.getInstance().getResultSetSingleRowAsMap(qrs.getResultSet(), useLabelAsKey);
                qrs.close(true);
            }
            if(map == null)
                map = new HashMap();
            return BeansWrapper.getDefaultInstance().wrap(map);
        }
        catch(Exception e)
        {
            throw new TemplateModelException(e);
        }
    }
}