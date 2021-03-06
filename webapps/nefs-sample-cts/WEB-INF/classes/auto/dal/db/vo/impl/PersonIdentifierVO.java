package auto.dal.db.vo.impl;


public class PersonIdentifierVO
implements auto.dal.db.vo.PersonIdentifier
{
    
    public java.lang.String getCrSessId()
    {
        return crSessId;
    }
    
    public java.util.Date getCrStamp()
    {
        return crStamp;
    }
    
    public java.lang.String getIdType()
    {
        return idType;
    }
    
    public java.lang.String getIdTypeId()
    {
        return idTypeId;
    }
    
    public java.lang.String getIdentifier()
    {
        return identifier;
    }
    
    public java.lang.String getNotes()
    {
        return notes;
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
    
    public java.lang.Long getPersonId()
    {
        return personId;
    }
    
    public long getPersonIdLong()
    {
        return getPersonIdLong(-1);
    }
    
    public long getPersonIdLong(long defaultValue)
    {
        return personId != null ? personId.longValue() : defaultValue;
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
    
    public java.lang.String getSourceType()
    {
        return sourceType;
    }
    
    public java.lang.String getSourceTypeId()
    {
        return sourceTypeId;
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
    
    public void setIdType(java.lang.String idType)
    {
        this.idType = idType;
    }
    
    public void setIdTypeId(java.lang.String idTypeId)
    {
        this.idTypeId = idTypeId;
    }
    
    public void setIdentifier(java.lang.String identifier)
    {
        this.identifier = identifier;
    }
    
    public void setNotes(java.lang.String notes)
    {
        this.notes = notes;
    }
    
    public void setOrgId(java.lang.Long orgId)
    {
        this.orgId = orgId;
    }
    
    public void setOrgIdLong(long orgId)
    {
        this.orgId = new java.lang.Long(orgId);
    }
    
    public void setPersonId(java.lang.Long personId)
    {
        this.personId = personId;
    }
    
    public void setPersonIdLong(long personId)
    {
        this.personId = new java.lang.Long(personId);
    }
    
    public void setRecStatId(java.lang.Integer recStatId)
    {
        this.recStatId = recStatId;
    }
    
    public void setRecStatIdInt(int recStatId)
    {
        this.recStatId = new java.lang.Integer(recStatId);
    }
    
    public void setSourceType(java.lang.String sourceType)
    {
        this.sourceType = sourceType;
    }
    
    public void setSourceTypeId(java.lang.String sourceTypeId)
    {
        this.sourceTypeId = sourceTypeId;
    }
    
    public void setSystemId(java.lang.String systemId)
    {
        this.systemId = systemId;
    }
    private java.lang.String crSessId;
    private java.util.Date crStamp;
    private java.lang.String idType;
    private java.lang.String idTypeId;
    private java.lang.String identifier;
    private java.lang.String notes;
    private java.lang.Long orgId;
    private java.lang.Long personId;
    private java.lang.Integer recStatId;
    private java.lang.String sourceType;
    private java.lang.String sourceTypeId;
    private java.lang.String systemId;
}