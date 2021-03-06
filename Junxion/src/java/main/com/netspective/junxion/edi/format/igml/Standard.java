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
package com.netspective.junxion.edi.format.igml;

import com.netspective.commons.xdm.XmlDataModelSchema;
import com.netspective.junxion.edi.format.igml.attributes.StandardAuthority;
import com.netspective.junxion.edi.format.igml.util.TextContainer;
import com.netspective.junxion.edi.format.igml.util.TextList;

public class Standard implements TextContainer
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options();

    static
    {
        XML_DATA_MODEL_SCHEMA_OPTIONS.addIgnoreNestedElements(new String[]{"private"});
    }

    /**
     * Identifies the release version of the igML file to which the implementation guideline conforms.
     */
    private String igmlVersion;

    /**
     * The name of the standard.
     */
    private String id;

    /**
     * The version number.
     * ASC X12: positions 1-3 of element 480
     * UN/EDIFACT: element 0122
     */
    private String version;

    /**
     * The release and subrelease.
     * ASC X12: positions 4-6 of element 480
     * UN/EDIFACT: element 0124
     */
    private String release;

    /**
     * The implementation convention reference or the message implementation guideline identification.
     * ASC X12: element 1705
     * UN/EDIFACT: element 0121
     */
    private String implementation;

    /**
     * The responsible agency code.
     */
    private StandardAuthority authority;

    /**
     * The standard on which this implementation guideline is based. The default value for DerivedFrom is "N/A"
     * and when used specifies that the igML file represents the base standard.
     */
    private String derivedFrom;

    /**
     * The date of time of the last modification to the standard in ISO 8601 format: CCYY-MM-DDThh:mm:ss.
     * See ISO 8601:1988(E) 5.4.1 for more information.
     */
    private String lastModified;

    /**
     * The list of <Text> elements contained in this element.
     */
    private TextList texts = new TextList();

    /**
     * The <MessageDirectory> element.
     */
    private MessageDirectory messageDirectory = new MessageDirectory();

    /**
     * The <SegmentDictionary> element.
     */
    private SegmentDictionary segmentDictionary = new SegmentDictionary();

    /**
     * The <CompositeDictionary> element.
     */
    private CompositeDictionary compositeDictionary = new CompositeDictionary();

    /**
     * The <ElementDictionary> element.
     */
    private ElementDictionary elementDictionary = new ElementDictionary();

    public Standard()
    {
    }

    /**
     * Returns the release version of the igML file to which the implementation guideline conforms.
     */
    public String getIgmlVersion()
    {
        return igmlVersion;
    }

    /**
     * Sets the release version of the igML file to which the implementation guideline conforms.
     */
    public void setIgmlVersion(String igmlVersion)
    {
        this.igmlVersion = igmlVersion;
    }

    /**
     * Returns the name of the standard.
     */
    public String getId()
    {
        return id;
    }

    /**
     * Sets the name of the standard.
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * Returns the version number.
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * Sets the version number.
     */
    public void setVersion(String version)
    {
        this.version = version;
    }

    /**
     * Returns the release and subrelease.
     */
    public String getRelease()
    {
        return release;
    }

    /**
     * Sets the release and subrelease.
     */
    public void setRelease(String release)
    {
        this.release = release;
    }

    /**
     * Returns the implementation convention reference or the message implementation guideline identification.
     */
    public String getImplementation()
    {
        return implementation;
    }

    /**
     * Sets the implementation convention reference or the message implementation guideline identification.
     */
    public void setImplementation(String implementation)
    {
        this.implementation = implementation;
    }

    /**
     * Returns the responsible agency code.
     */
    public StandardAuthority getAuthority()
    {
        return authority;
    }

    /**
     * Sets the responsible agency code.
     */
    public void setAuthority(StandardAuthority authority)
    {
        this.authority = authority;
    }

    /**
     * Returns the standard on which this implementation guideline is based. The default value for DerivedFrom is "N/A"
     * and when used specifies that the igML file represents the base standard.
     */
    public String getDerivedFrom()
    {
        return derivedFrom;
    }

    /**
     * Sets the standard on which this implementation guideline is based. The default value for DerivedFrom is "N/A"
     * and when used specifies that the igML file represents the base standard.
     */
    public void setDerivedFrom(String derivedFrom)
    {
        this.derivedFrom = derivedFrom;
    }

    /**
     * Gets the date of time of the last modification to the standard.
     */
    public String getLastModified()
    {
        return lastModified;
    }

    /**
     * Sets the date of time of the last modification to the standard in ISO 8601 format: CCYY-MM-DDThh:mm:ss.
     * See ISO 8601:1988(E) 5.4.1 for more information.
     */
    public void setLastModified(String lastModified)
    {
        this.lastModified = lastModified;
    }

    /**
     * Used by the DataModel to create the <MessageDirectory> element.
     */
    public MessageDirectory createMessageDirectory()
    {
        return messageDirectory;
    }

    public MessageDirectory getMessageDirectory()
    {
        return messageDirectory;
    }

    public void setMessageDirectory(MessageDirectory messageDirectory)
    {
        this.messageDirectory = messageDirectory;
    }

    /**
     * Adds a new <Text> element to the list already present.
     */
    public void addText(Text text)
    {
        texts.add(text);
    }

    /**
     * Gets the list of all <Text> elements
     */
    public TextList getTexts()
    {
        return texts;
    }

    /**
     * Replaces the existing TextList with one provided.
     */
    public void setTexts(TextList texts)
    {
        this.texts = texts;
    }

    public SegmentDictionary createSegmentDictionary()
    {
        return segmentDictionary;
    }

    public SegmentDictionary getSegmentDictionary()
    {
        return segmentDictionary;
    }

    public void setSegmentDictionary(SegmentDictionary segmentDictionary)
    {
        this.segmentDictionary = segmentDictionary;
    }

    public CompositeDictionary createCompositeDictionary()
    {
        return compositeDictionary;
    }

    public CompositeDictionary getCompositeDictionary()
    {
        return compositeDictionary;
    }

    public void setCompositeDictionary(CompositeDictionary compositeDictionary)
    {
        this.compositeDictionary = compositeDictionary;
    }

    public ElementDictionary createElementDictionary()
    {
        return elementDictionary;
    }

    public ElementDictionary getElementDictionary()
    {
        return elementDictionary;
    }

    public void setElementDictionary(ElementDictionary elementDictionary)
    {
        this.elementDictionary = elementDictionary;
    }
}
