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
 * $Id: TextUtils.java,v 1.1 2003-03-13 18:33:11 shahid.shah Exp $
 */

package com.netspective.commons.text;

import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;

import org.apache.oro.text.perl.Perl5Util;

public class TextUtils
{
    /**
     * Return the name of the given cls that is different from the relativeTo class. Basically, this chops off the
     * package name of the cls that is equivalent to that of the relativeTo class.
     * @param relativeTo
     * @param cls
     * @return
     */
    static public String getRelativeClassName(Class relativeTo, Class cls)
    {
        String className = cls.getName();
        String relativeToPkg = relativeTo.getPackage().getName();
        if(className.startsWith(relativeToPkg))
            return className.substring(relativeToPkg.length()+1);
        else
            return className;
    }

    static public String getClassNameWithoutPackage(String pkgAndClassName, char sep)
    {
        int classNameDelimPos = pkgAndClassName.lastIndexOf(sep);
        return classNameDelimPos != -1 ? pkgAndClassName.substring(classNameDelimPos+1) : pkgAndClassName;
    }

    static public String getPackageName(String pkgAndClassName, char sep)
    {
        int classNameDelimPos = pkgAndClassName.lastIndexOf(sep);
        return classNameDelimPos != -1 ? pkgAndClassName.substring(0, classNameDelimPos) : null;
    }

    static public String[] split(String source, String delimiter, boolean trim)
    {
        if(source == null)
            return null;

        List list = new ArrayList();
        StringTokenizer st = new StringTokenizer(source, delimiter);
        if(trim)
        {
            while(st.hasMoreTokens())
                list.add(st.nextToken().trim());
        }
        else
        {
            while(st.hasMoreTokens())
                list.add(st.nextToken());
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    static public String join(String[] source, String delimiter)
    {
        if(source == null) return null;

        StringBuffer result = new StringBuffer();
        for(int i = 0; i < source.length; i++)
        {
            result.append(source[i]);
            if(i < source.length -1)
                result.append(delimiter);
        }
        return result.toString();
    }

    /**
     * Perform a simple string replacement of findStr to replStr in origStr and returns the result. All instances
     * of findStr are replaced to replStr (regardless of how many there are). Not optimized for performance.
     * @param originalText The source text
     * @param findText The text to locate
     * @param replaceText The text to replace for each findStr
     */
    static public String replaceTextValues(final String originalText, final String findText, final String replaceText)
    {
        if (originalText == null || findText == null || replaceText == null)
            return null;

        String activeText = originalText;
        int findLoc = activeText.indexOf(findText);
        while(findLoc >= 0)
        {
            StringBuffer sb = new StringBuffer(activeText);
            sb.replace(findLoc, findLoc + findText.length(), replaceText);
            activeText = sb.toString();
            findLoc = activeText.indexOf(findText);
        }

        return activeText;
    }

    /**
     * returns the boolean equivalent of a string, which is considered true
     * if either "on", "true", or "yes" is found, ignoring case.
     */
    public static boolean toBoolean(String s)
    {
        return (s.equalsIgnoreCase("yes") ||
                s.equalsIgnoreCase("true") ||
                s.equalsIgnoreCase("on") ||
                s.equalsIgnoreCase("1"));
    }

    /**
     * Given a text string that defines a SQL table name or column name or other SQL identifier,
     * return a string that would be suitable for that string to be used as a caption or plain text.
     */
    public static String sqlIdentifierToText(String original, boolean uppercaseEachWord)
    {
        if (original == null || original.length() == 0)
            return original;

        StringBuffer text = new StringBuffer();
        text.append(Character.toUpperCase(original.charAt(0)));
        boolean wordBreak = false;
        for (int i = 1; i < original.length(); i++)
        {
            char ch = original.charAt(i);
            if (ch == '_')
            {
                text.append(' ');
                wordBreak = true;
            }
            else if (wordBreak)
            {
                text.append(uppercaseEachWord ? Character.toUpperCase(ch) : Character.toLowerCase(ch));
                wordBreak = false;
            }
            else
                text.append(Character.toLowerCase(ch));
        }
        return text.toString();
    }

    /**
     * Given a method name, return a string that would be suitable for that string to be used
     * as a xml node name. Basically, what this does is allows something like setAbcDef() to
     * match both "abcDef" and "abc-def" as node names in XML. It turns a java identifier into
     * a reasonable xml node name.
     */
    public static String javaIdentifierToXmlNodeName(final String javaIdentifier)
    {
        if(javaIdentifier == null || javaIdentifier.length() == 0)
            return javaIdentifier;

        StringBuffer nodeName = new StringBuffer();
        nodeName.append(javaIdentifier.charAt(0));
        for(int i = 1; i < javaIdentifier.length(); i++)
        {
            char ch = javaIdentifier.charAt(i);
            if(Character.isLowerCase(ch))
                nodeName.append(ch);
            else
            {
                nodeName.append('-');
                nodeName.append(ch);
            }
        }

        return nodeName.toString();
    }

    /**
     * Given a text string, return a string that would be suitable for that string to be used
     * as a Java identifier (as a variable or method name). Depending upon whether ucaseInitial
     * is set, the string starts out with a lowercase or uppercase letter. Then, the rule is
     * to convert all periods into underscores and title case any words separated by
     * underscores. This has the effect of removing all underscores and creating mixed case
     * words. For example, Person_Address becomes personAddress or PersonAddress depending upon
     * whether ucaseInitial is set to true or false. Person.Address would become Person_Address.
     */
    public static String xmlTextToJavaIdentifier(String xml, boolean ucaseInitial)
    {
        if(xml == null || xml.length() == 0)
            return xml;

        StringBuffer identifier = new StringBuffer();
        char ch = xml.charAt(0);
        identifier.append(ucaseInitial ? Character.toUpperCase(ch) : Character.toLowerCase(ch));

        boolean uCase = false;
        for(int i = 1; i < xml.length(); i++)
        {
            ch = xml.charAt(i);
            if(ch == '.')
            {
                identifier.append('_');
            }
            else if(ch != '_' && Character.isJavaIdentifierPart(ch))
            {
                identifier.append(Character.isUpperCase(ch) ? ch : (uCase ? Character.toUpperCase(ch) : Character.toLowerCase(ch)));
                uCase = false;
            }
            else
                uCase = true;
        }
        return identifier.toString();
    }

    /**
     * Given a text string, return a string that would be suitable for that string to be used
     * as a Java constant (public static final XXX). The rule is to basically take every letter
     * or digit and return it in uppercase and every non-letter or non-digit as an underscore.
     */
    public static String xmlTextToJavaConstant(String xml)
    {
        if(xml == null || xml.length() == 0)
            return xml;

        StringBuffer constant = new StringBuffer();
        for(int i = 0; i < xml.length(); i++)
        {
            char ch = xml.charAt(i);
            constant.append(Character.isJavaIdentifierPart(ch) ? Character.toUpperCase(ch) : '_');
        }
        return constant.toString();
    }

    /**
     * Given a text string, return a string that would be suitable for that string to be used
     * as a Java constant (public static final XXX). The rule is to basically take every letter
     * or digit and return it in uppercase and every non-letter or non-digit as an underscore.
     * This trims all non-letter/digit characters from the beginning of the string.
     */
    public static String xmlTextToJavaConstantTrimmed(String xml)
    {
        if(xml == null || xml.length() == 0)
            return xml;

        boolean stringStarted = false;
        StringBuffer constant = new StringBuffer();
        for(int i = 0; i < xml.length(); i++)
        {
            char ch = xml.charAt(i);
            if(Character.isJavaIdentifierPart(ch))
            {
                stringStarted = true;
                constant.append(Character.toUpperCase(ch));
            }
            else if(stringStarted)
                constant.append('_');
        }
        return constant.toString();
    }

    /**
     * Given a text string, return a string that would be suitable for an XML element name. For example,
     * when given Person_Address it would return person-address. The rule is to basically take every letter
     * or digit and return it in lowercase and every non-letter or non-digit as a dash.
     */
    public static String xmlTextToNodeName(String xml)
    {
        if(xml == null || xml.length() == 0)
            return xml;

        StringBuffer constant = new StringBuffer();
        for(int i = 0; i < xml.length(); i++)
        {
            char ch = xml.charAt(i);
            constant.append(Character.isLetterOrDigit(ch) ? Character.toLowerCase(ch) : '-');
        }
        return constant.toString();
    }

    /**
     * Return the given text unindented by whatever the first line is indented by
     * @param text The original text
     * @return Unindented text or original text if not indented
     */
    public static String getUnindentedText(String text)
    {
        /*
         * if the string is indented, find out how far the first line is indented
         */
        StringBuffer replStr = new StringBuffer();
        for(int i = 0; i < text.length(); i++)
        {
            char ch = text.charAt(i);
            if(Character.isWhitespace(ch))
                replStr.append(ch);
            else
                break;
        }

        /*
         * If the first line is indented, unindent all the lines the distance of just the first line
         */
        Perl5Util perlUtil = new Perl5Util();

        if(replStr.length() > 0)
            return perlUtil.substitute("s/" + replStr + "/\n/g", text).trim();
        else
            return text;
    }

    /**
     * Return the given block of text indented by the given string.
     * @param text The original text
     * @return Unindented text or original text if not indented
     */
    public static String getIndentedText(String text, String indent, boolean appendNewLine)
    {
        text = getUnindentedText(text);

        /*
         * If the first line is indented, unindent all the lines the distance of just the first line
         */
        Perl5Util perlUtil = new Perl5Util();
        text = perlUtil.substitute("s/^/"+ indent +"/gm", text);
        return appendNewLine ? text + "\n" : text;
    }

    /* make the table name title cased (cap each letter after _) */
    public static String fixupTableNameCase(String tableNameOrig)
    {
        StringBuffer tableNameBuf = new StringBuffer(tableNameOrig.toLowerCase());
        boolean capNext = false;
        for (int i = 0; i < tableNameBuf.length(); i++)
        {
            if (tableNameBuf.charAt(i) == '_')
                capNext = true;
            else
            {
                if (i == 0 || capNext)
                {
                    tableNameBuf.setCharAt(i, Character.toUpperCase(tableNameBuf.charAt(i)));
                    capNext = false;
                }
            }
        }
        return tableNameBuf.toString();
    }
}
