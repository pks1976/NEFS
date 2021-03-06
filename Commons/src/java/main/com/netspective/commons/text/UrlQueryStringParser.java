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
package com.netspective.commons.text;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UrlQueryStringParser
{
    private final Map result = new HashMap();
    private final InputStream stream;
    private int nextChar;

    public UrlQueryStringParser(InputStream stream) throws IOException
    {
        this.stream = stream;
        nextChar = stream.read();
    }

    public Map parseArgs() throws IOException
    {
        while(hasAnotherCharacter())
            parseNameValuePair();

        return result;
    }

    private void parseNameValuePair() throws IOException
    {
        final String name = readUpTo('=');
        final String value = readUpTo('&');
        result.put(name, value);
    }

    private String readUpTo(char boundaryCharacter) throws IOException
    {
        String word = "";

        while(hasAnotherCharacter())
        {
            final char character = readCharacter();

            if(character == boundaryCharacter)
                return word;
            else if(character == '%')
                word += readHexEncodedCharacter();
            else if(character == '+')
                word += " ";
            else
                word += character;
        }
        return word;
    }

    private String readHexEncodedCharacter() throws IOException
    {
        final int sixteens = readHexDigit();
        final int ones = readHexDigit();
        if((sixteens < 0) || (ones < 0)) return "";

        final char character = (char) ((16 * sixteens) + ones);
        return "" + character;
    }

    private int readHexDigit() throws IOException
    {
        if(!hasAnotherCharacter()) return -1;

        return Character.digit(readCharacter(), 16);
    }

    private boolean hasAnotherCharacter()
    {
        return (nextChar >= 0);
    }

    private char readCharacter() throws IOException
    {
        if(!hasAnotherCharacter()) throw new IllegalStateException("assertion failed");

        final char result = (char) nextChar;
        nextChar = stream.read();
        return result;
    }
}