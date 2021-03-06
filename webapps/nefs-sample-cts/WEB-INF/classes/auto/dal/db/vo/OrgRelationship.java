package auto.dal.db.vo;


public interface OrgRelationship
{
    
    public java.lang.String getCrSessId();
    
    public java.util.Date getCrStamp();
    
    public java.lang.Long getParentId();
    
    public long getParentIdLong();
    
    public long getParentIdLong(long defaultValue);
    
    public java.lang.Integer getRecStatId();
    
    public int getRecStatIdInt();
    
    public int getRecStatIdInt(int defaultValue);
    
    public java.util.Date getRelBegin();
    
    public java.lang.String getRelDescr();
    
    public java.util.Date getRelEnd();
    
    public java.lang.Long getRelEntityId();
    
    public long getRelEntityIdLong();
    
    public long getRelEntityIdLong(long defaultValue);
    
    public java.lang.String getRelType();
    
    public java.lang.Integer getRelTypeId();
    
    public int getRelTypeIdInt();
    
    public int getRelTypeIdInt(int defaultValue);
    
    public java.lang.Long getSystemId();
    
    public long getSystemIdLong();
    
    public long getSystemIdLong(long defaultValue);
    
    public void setCrSessId(java.lang.String crSessId);
    
    public void setCrStamp(java.util.Date crStamp);
    
    public void setParentId(java.lang.Long parentId);
    
    public void setParentIdLong(long parentId);
    
    public void setRecStatId(java.lang.Integer recStatId);
    
    public void setRecStatIdInt(int recStatId);
    
    public void setRelBegin(java.util.Date relBegin);
    
    public void setRelDescr(java.lang.String relDescr);
    
    public void setRelEnd(java.util.Date relEnd);
    
    public void setRelEntityId(java.lang.Long relEntityId);
    
    public void setRelEntityIdLong(long relEntityId);
    
    public void setRelType(java.lang.String relType);
    
    public void setRelTypeId(java.lang.Integer relTypeId);
    
    public void setRelTypeIdInt(int relTypeId);
    
    public void setSystemId(java.lang.Long systemId);
    
    public void setSystemIdLong(long systemId);
}