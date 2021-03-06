package auto.dal.db.vo.impl;


public class OrgIdentifierVO
implements auto.dal.db.vo.OrgIdentifier
{
    
    public java.lang.String getCrSessId()
    {
        return crSessId;
    }
    
    public java.util.Date getCrStamp()
    {
        return crStamp;
    }
    
    public java.lang.String getIdentifier()
    {
        return identifier;
    }
    
    public java.lang.String getIdentifierType()
    {
        return identifierType;
    }
    
    public java.lang.Integer getIdentifierTypeId()
    {
        return identifierTypeId;
    }
    
    public int getIdentifierTypeIdInt()
    {
        return getIdentifierTypeIdInt(-1);
    }
    
    public int getIdentifierTypeIdInt(int defaultValue)
    {
        return identifierTypeId != null ? identifierTypeId.intValue() : defaultValue;
    }
    
    public java.lang.Long getOrgId()
    {
        return orgId;
    }
    
    public long getOrgIdLong()
    {
        return getOrgIdLong(-1);
    }
    
    public long getOrgIdLong(long defaultValue)
    {
        return orgId != null ? orgId.longValue() : defaultValue;
    }
    
    public java.lang.Integer getRecStatId()
    {
        return recStatId;
    }
    
    public int getRecStatIdInt()
    {
        return getRecStatIdInt(-1);
    }
    
    public int getRecStatIdInt(int defaultValue)
    {
        return recStatId != null ? recStatId.intValue() : defaultValue;
    }
    
    public java.lang.String getSystemId()
    {
        return systemId;
    }
    
    public void setCrSessId(java.lang.String crSessId)
    {
        this.crSessId = crSessId;
    }
    
    public void setCrStamp(java.util.Date crStamp)
    {
        this.crStamp = crStamp;
    }
    
    public void setIdentifier(java.lang.String identifier)
    {
        this.identifier = identifier;
    }
    
    public void setIdentifierType(java.lang.String identifierType)
    {
        this.identifierType = identifierType;
    }
    
    public void setIdentifierTypeId(java.lang.Integer identifierTypeId)
    {
        this.identifierTypeId = identifierTypeId;
    }
    
    public void setIdentifierTypeIdInt(int identifierTypeId)
    {
        this.identifierTypeId = new java.lang.Integer(identifierTypeId);
    }
    
    public void setOrgId(java.lang.Long orgId)
    {
        this.orgId = orgId;
    }
    
    public void setOrgIdLong(long orgId)
    {
        this.orgId = new java.lang.Long(orgId);
    }
    
    public void setRecStatId(java.lang.Integer recStatId)
    {
        this.recStatId = recStatId;
    }
    
    public void setRecStatIdInt(int recStatId)
    {
        this.recStatId = new java.lang.Integer(recStatId);
    }
    
    public void setSystemId(java.lang.String systemId)
    {
        this.systemId = systemId;
    }
    private java.lang.String crSessId;
    private java.util.Date crStamp;
    private java.lang.String identifier;
    private java.lang.String identifierType;
    private java.lang.Integer identifierTypeId;
    private java.lang.Long orgId;
    private java.lang.Integer recStatId;
    private java.lang.String systemId;
}