package auto.dal.db.dao;

import com.netspective.axiom.schema.Schema;
import com.netspective.axiom.schema.Row;
import com.netspective.axiom.schema.Rows;
import com.netspective.axiom.sql.dynamic.QueryDefnSelects;
import com.netspective.axiom.schema.Table;
import javax.naming.NamingException;
import java.sql.SQLException;
import com.netspective.axiom.ConnectionContext;
import com.netspective.axiom.sql.dynamic.QueryDefnSelect;
import com.netspective.axiom.schema.ColumnValues;
import com.netspective.axiom.schema.ForeignKey;
import com.netspective.axiom.schema.constraint.ParentForeignKey;
import com.netspective.axiom.sql.QueryResultSet;
import com.netspective.axiom.schema.PrimaryKeyColumnValues;
import com.netspective.axiom.schema.column.type.AutoIncColumn;
import com.netspective.axiom.schema.column.type.LongIntegerColumn;
import com.netspective.axiom.schema.column.type.TextColumn;

public final class BorrowerTable
{
    public static final int ACCESSORID_BY_PRIMARY_KEYS_EQUALITY = 0;
    public static final int ACCESSORID_BY_BORROWER_ID_EQUALITY = 1;
    public static final int ACCESSORID_BY_FIRST_NAME_EQUALITY = 2;
    public static final int ACCESSORID_BY_LAST_NAME_EQUALITY = 3;
    public static final int COLINDEX_BORROWER_ID = 0;
    public static final int COLINDEX_FIRST_NAME = 1;
    public static final int COLINDEX_LAST_NAME = 2;
    
    public BorrowerTable(Table table)
    {
        this.table = (com.netspective.axiom.schema.table.BasicTable)table;
        this.schema = table.getSchema();
        this.accessors = table.getQueryDefinition().getSelects();
    }
    
    public final BorrowerTable.Record createRecord()
    {
        return new BorrowerTable.Record(table.createRow());
    }
    
    public final QueryDefnSelect getAccessorByBorrowerIdEquality()
    {
        return accessors.get(ACCESSORID_BY_BORROWER_ID_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByFirstNameEquality()
    {
        return accessors.get(ACCESSORID_BY_FIRST_NAME_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByLastNameEquality()
    {
        return accessors.get(ACCESSORID_BY_LAST_NAME_EQUALITY);
    }
    
    public final QueryDefnSelect getAccessorByPrimaryKeysEquality()
    {
        return accessors.get(ACCESSORID_BY_PRIMARY_KEYS_EQUALITY);
    }
    
    public final BorrowerTable.Records getAccessorRecords(ConnectionContext cc, QueryDefnSelect accessor, Object[] bindValues)
    throws NamingException, SQLException
    {
        Rows rows = getTable().createRows();
        QueryResultSet qrs = accessor.execute(cc, bindValues, false);
        if(qrs != null) rows.populateDataByIndexes(qrs.getResultSet());
        qrs.close(false);
        return new Records(rows);
    }
    
    public final AutoIncColumn getBorrowerIdColumn()
    {
        return (AutoIncColumn)table.getColumns().get(COLINDEX_BORROWER_ID);
    }
    
    public final TextColumn getFirstNameColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_FIRST_NAME);
    }
    
    public final TextColumn getLastNameColumn()
    {
        return (TextColumn)table.getColumns().get(COLINDEX_LAST_NAME);
    }
    
    public final BorrowerTable.Record getRecord(Row row)
    {
        return new BorrowerTable.Record(row);
    }
    
    public final BorrowerTable.Record getRecordByPrimaryKey(ConnectionContext cc, java.lang.Long borrowerId)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, new Object[] { borrowerId }, null);
        Record result = row != null ? new BorrowerTable.Record(row) : null;
        return result;
    }
    
    public final BorrowerTable.Record getRecordByPrimaryKey(ConnectionContext cc, PrimaryKeyColumnValues pkValues)
    throws NamingException, SQLException
    {
        Row row = table.getRowByPrimaryKeys(cc, pkValues, null);
        Record result = row != null ? new BorrowerTable.Record(row) : null;
        return result;
    }
    
    public final com.netspective.axiom.schema.table.BasicTable getTable()
    {
        return table;
    }
    
    public final String toString()
    {
        return table.toString();
    }
    private QueryDefnSelects accessors;
    private Schema schema;
    private com.netspective.axiom.schema.table.BasicTable table;
    
    public final class Record
    {
        
        public Record(Row row)
        {
            if(row.getTable() != table) throw new ClassCastException("Attempting to assign row from table "+ row.getTable().getName() +" to "+ this.getClass().getName() +" (expecting a row from table "+ table.getName() +").");
            this.row = row;
            this.values = row.getColumnValues();
        }
        
        public final boolean dataChangedInStorage(ConnectionContext cc)
        throws NamingException, SQLException
        {
            return table.dataChangedInStorage(cc, row);
        }
        
        public final void delete(ConnectionContext cc, String whereCond, Object[] whereCondBindParams)
        throws NamingException, SQLException
        {
            table.delete(cc, row, whereCond, whereCondBindParams);
        }
        
        public final void delete(ConnectionContext cc)
        throws NamingException, SQLException
        {
            table.delete(cc, row);
        }
        
        public final LongIntegerColumn.LongIntegerColumnValue getBorrowerId()
        {
            return (LongIntegerColumn.LongIntegerColumnValue)values.getByColumnIndex(COLINDEX_BORROWER_ID);
        }
        
        public final TextColumn.TextColumnValue getFirstName()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_FIRST_NAME);
        }
        
        public final TextColumn.TextColumnValue getLastName()
        {
            return (TextColumn.TextColumnValue)values.getByColumnIndex(COLINDEX_LAST_NAME);
        }
        
        public final Row getRow()
        {
            return row;
        }
        
        public final auto.dal.db.vo.Borrower getValues()
        {
            return getValues(new auto.dal.db.vo.impl.BorrowerVO());
        }
        
        public final auto.dal.db.vo.Borrower getValues(auto.dal.db.vo.Borrower valueObject)
        {
            Object autoIncBorrowerIdValue = values.getByColumnIndex(COLINDEX_BORROWER_ID).getValue();
            valueObject.setBorrowerId(autoIncBorrowerIdValue instanceof Integer ? new Long(((Integer) autoIncBorrowerIdValue).intValue()) : (Long) autoIncBorrowerIdValue);
            valueObject.setFirstName((java.lang.String) values.getByColumnIndex(COLINDEX_FIRST_NAME).getValue());
            valueObject.setLastName((java.lang.String) values.getByColumnIndex(COLINDEX_LAST_NAME).getValue());
            return valueObject;
        }
        
        public final void insert(ConnectionContext cc)
        throws NamingException, SQLException
        {
            table.insert(cc, row);
        }
        
        public final void refresh(ConnectionContext cc)
        throws NamingException, SQLException
        {
            table.refreshData(cc, row);
        }
        
        public final void setBorrowerId(com.netspective.commons.value.Value value)
        {
            getBorrowerId().copyValueByReference(value);
        }
        
        public final void setFirstName(com.netspective.commons.value.Value value)
        {
            getFirstName().copyValueByReference(value);
        }
        
        public final void setLastName(com.netspective.commons.value.Value value)
        {
            getLastName().copyValueByReference(value);
        }
        
        public final void setValues(auto.dal.db.vo.Borrower valueObject)
        {
            values.getByColumnIndex(COLINDEX_BORROWER_ID).setValue(valueObject.getBorrowerId());
            values.getByColumnIndex(COLINDEX_FIRST_NAME).setValue(valueObject.getFirstName());
            values.getByColumnIndex(COLINDEX_LAST_NAME).setValue(valueObject.getLastName());
        }
        
        public final String toString()
        {
            return row.toString();
        }
        
        public final void update(ConnectionContext cc, String whereCond, Object[] whereCondBindParams)
        throws NamingException, SQLException
        {
            table.update(cc, row, whereCond, whereCondBindParams);
        }
        
        public final void update(ConnectionContext cc)
        throws NamingException, SQLException
        {
            table.update(cc, row);
        }
        private Row row;
        private ColumnValues values;
    }
    
    public final class Records
    {
        
        public Records(Rows rows)
        {
            this.rows = rows;
            this.cache = new Record[rows.size()];
        }
        
        public final void delete(ConnectionContext cc)
        throws NamingException, SQLException
        {
            for(int i = 0; i < cache.length; i++)get(i).delete(cc);
        }
        
        public final BorrowerTable.Record get(int i)
        {
            if(cache[i] == null) cache[i] = new Record(rows.getRow(i));
            return cache[i];
        }
        
        public final int size()
        {
            return rows.size();
        }
        
        public final String toString()
        {
            return rows.toString();
        }
        private Record[] cache;
        private Rows rows;
    }
}