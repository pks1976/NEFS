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
package com.netspective.commons.acl;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.netspective.commons.xdm.XmlDataModelSchema;

public class Permission
{
    public static final XmlDataModelSchema.Options XML_DATA_MODEL_SCHEMA_OPTIONS = new XmlDataModelSchema.Options().setIgnorePcData(true);

    private AccessControlList owner;
    private Permission parent;
    private int id = -1;
    private int level = 0;
    private String name;
    private String qualifiedName;
    private BitSet childPermissions = new BitSet();
    private List children = new ArrayList();

    public Permission(AccessControlList owner)
    {
        setOwner(owner);
        setId(getOwner().getHighestPermissionId());
    }

    public Permission(Permission parent)
    {
        setParent(parent);
        setId(getOwner().getHighestPermissionId());
    }

    public void unionChildPermissions(Permission perm)
    {
        childPermissions.or(perm.getChildPermissions());
        if(getParent() != null) getParent().unionChildPermissions(this);
    }

    protected void setOwner(AccessControlList owner)
    {
        this.owner = owner;
    }

    public AccessControlList getOwner()
    {
        return owner;
    }

    protected void setParent(Permission parent)
    {
        this.parent = parent;
        if(parent != null)
        {
            setOwner(parent.getOwner());
            setLevel(parent.getLevel() + 1);
        }
    }

    public Permission getParent()
    {
        return parent;
    }

    public int getId()
    {
        return id;
    }

    protected void setId(int id)
    {
        this.id = id;
        childPermissions.set(id);
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getQualifiedName()
    {
        if(null == qualifiedName)
        {
            String qName = AccessControlList.NAME_SEPARATOR + getName();
            if(parent != null)
                qName = parent.getQualifiedName() + qName;
            else
                qName = owner.getQualifiedName() + qName;
            setQualifiedName(qName);
        }

        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }

    public BitSet getChildPermissions()
    {
        return childPermissions;
    }

    public Permission createPermission()
    {
        return new Permission(this);
    }

    public void addPermission(Permission childPerm)
    {
        children.add(childPerm);
        unionChildPermissions(childPerm);
        getOwner().registerPermission(childPerm);
        childPermissions.set(childPerm.getId());
    }

    public List getChildren()
    {
        return children;
    }

    public int getAncestorsCount()
    {
        int result = 0;
        Permission parent = getParent();
        while(parent != null)
        {
            result++;
            parent = parent.getParent();
        }
        return result;
    }

    public List getAncestorsList()
    {
        List result = new ArrayList();
        Permission parent = getParent();
        while(parent != null)
        {
            if(result.size() == 0)
                result.add(parent);
            else
                result.add(0, parent);
            parent = parent.getParent();
        }
        return result;
    }

    public String toString()
    {
        int depth = getAncestorsCount();

        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < depth; i++)
            sb.append("  ");

        sb.append(getQualifiedName());
        sb.append(" = ");
        sb.append(getId());
        sb.append(" ");
        sb.append(childPermissions);
        sb.append("\n");

        for(int i = 0; i < children.size(); i++)
        {
            Permission perm = (Permission) children.get(i);
            sb.append(perm.toString());
        }

        return sb.toString();
    }
}