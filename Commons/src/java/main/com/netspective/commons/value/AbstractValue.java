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
 * $Id: AbstractValue.java,v 1.4 2003-05-13 19:51:51 shahid.shah Exp $
 */

package com.netspective.commons.value;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import com.netspective.commons.value.Value;
import com.netspective.commons.value.exception.ValueException;

public abstract class AbstractValue implements Value
{
    public static final String BLANK_STRING = "";
    private int listType;
    private Object value;

    public AbstractValue()
    {
    }

    public AbstractValue(int listType)
    {
        this.listType = listType;
    }

    public Class getValueHolderClass()
    {
        return Object.class;
    }

    public Class getBindParamValueHolderClass()
    {
        return getValueHolderClass();
    }

    public boolean hasValue()
    {
        return value != null;
    }

    public boolean isListValue()
    {
        return listType != VALUELISTTYPE_NONE;
    }

    public int getListValueType()
    {
        return listType;
    }

    public Object getValueForSqlBindParam()
    {
        return value;
    }

    public String getTextValue()
    {
        switch(listType)
        {
            case VALUELISTTYPE_NONE:
                return value != null ? value.toString() : null;

            case VALUELISTTYPE_STRINGARRAY:
                return value != null ? ((String[]) value)[0] : null;

            case VALUELISTTYPE_LIST:
                if(value != null)
                {
                    Object v = ((List) value).get(0);
                    return v != null ? v.toString() : null;
                }
                return null;

            default:
                return null;
        }
    }

    public int getIntValue()
    {
        return Integer.parseInt(getTextValue());
    }

    public double getDoubleValue()
    {
        return Double.parseDouble(getTextValue());
    }

    public String getTextValueOrBlank()
    {
        String value = getTextValue();
        return value == null ? BLANK_STRING : value;
    }

    public String getTextValueOrDefault(String defaultText)
    {
        String value = getTextValue();
        return value == null ? defaultText : value;
    }

    public Object getValue()
    {
        return value;
    }

    public void appendText(String text)
    {
        String existing = getTextValue();
        if(existing != null)
            setTextValue(existing + text);
        else
            setTextValue(text);
    }

    public void setValue(Object value) throws ValueException
    {
        this.value = value;
    }

    public void setValue(String[] value)
    {
        listType = VALUELISTTYPE_STRINGARRAY;
        setValue((Object) value);
    }

    public void setValue(List value)
    {
        listType = VALUELISTTYPE_LIST;
        setValue((Object) value);
    }

    public void setValueFromSqlResultSet(ResultSet rs, int rowNum, int colIndex) throws SQLException, ValueException
    {
        setValue(rs.getObject(colIndex));
    }

    public void setTextValue(String value) throws ValueException
    {
        setValue(value);
    }

    public String[] getTextValues()
    {
        switch(listType)
        {
            case VALUELISTTYPE_NONE:
                String text = getTextValue();
                if(text != null)
                    return new String[] { text };
                else
                    return null;

            case VALUELISTTYPE_STRINGARRAY:
                return (String[]) getValue();

            case VALUELISTTYPE_LIST:
                List list = (List) getValue();
                if(list == null)
                    return null;
                String[] array = new String[list.size()];
                for(int i = 0; i < list.size(); i++)
                {
                    Object item = list.get(i);
                    array[i] = item == null ? null : item.toString();
                }
                return array;

            default:
                return null;
        }
    }

    public List getListValue()
    {
        switch(listType)
        {
            case VALUELISTTYPE_NONE:
                String text = getTextValue();
                if(text != null)
                {
                    List list = new ArrayList();
                    list.add(text);
                    return list;
                }
                else
                    return null;

            case VALUELISTTYPE_STRINGARRAY:
                String[] array = (String[]) getValue();
                if(array == null)
                    return null;
                List list = new ArrayList();
                for(int i = 0; i < array.length; i++)
                    list.add(array[i]);
                return list;

            case VALUELISTTYPE_LIST:
                return (List) getValue();

            default:
                return null;
        }
    }

    public int size()
    {
        switch(listType)
        {
            case VALUELISTTYPE_NONE:
                return hasValue() ? 1 : 0;

            case VALUELISTTYPE_STRINGARRAY:
                String[] array = (String[]) getValue();
                return array == null ? 0 : array.length;

            case VALUELISTTYPE_LIST:
                List list = (List) getValue();
                return list == null ? 0 : list.size();

            default:
                return 0;
        }
    }

    /*
     TODO:
    public void importFromXml(Element fieldElem)
    {
        String valueType = fieldElem.getAttribute("value-type");
        if(valueType.equals("strings"))
        {
            NodeList valuesNodesList = fieldElem.getElementsByTagName("values");
            if(valuesNodesList.getLength() > 0)
            {
                NodeList valueNodesList = ((Element) valuesNodesList.item(0)).getElementsByTagName("value");
                int valuesCount = valueNodesList.getLength();
                if(valuesCount > 0)
                {
                    values = new String[valuesCount];
                    for(int i = 0; i < valuesCount; i++)
                    {
                        Element valueElem = (Element) valueNodesList.item(i);
                        if(valueElem.getChildNodes().getLength() > 0)
                            values[i] = valueElem.getFirstChild().getNodeValue();
                    }
                }
            }
        }
        else
        {
            NodeList valueList = fieldElem.getElementsByTagName("value");
            if(valueList.getLength() > 0)
            {
                Element valueElem = (Element) valueList.item(0);
                if(valueElem.getChildNodes().getLength() > 0)
                    value = valueElem.getFirstChild().getNodeValue();
            }
        }
    }

    public void exportToXml(Element parent)
    {
        Document doc = parent.getOwnerDocument();
        Element fieldElem = doc.createElement("field");
        fieldElem.setAttribute("name", field.getQualifiedName());
        if(values != null)
        {
            fieldElem.setAttribute("value-type", "strings");
            Element valuesElem = doc.createElement("values");
            for(int i = 0; i < values.length; i++)
            {
                Element valueElem = doc.createElement("value");
                valueElem.appendChild(doc.createTextNode(values[i]));
                valuesElem.appendChild(valueElem);
            }
            fieldElem.appendChild(valuesElem);
            parent.appendChild(fieldElem);
        }
        else if(value != null && value.length() > 0)
        {
            fieldElem.setAttribute("value-type", "string");
            Element valueElem = doc.createElement("value");
            valueElem.appendChild(doc.createTextNode(value));
            fieldElem.appendChild(valueElem);
            parent.appendChild(fieldElem);
        }
    }
   */
}
