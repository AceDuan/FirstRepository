package com.china.acetech.ToolPackage.data.repo.greenDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table BRACELET_INFO.
*/
public class BraceletInfoDao extends AbstractDao<BraceletInfo, Long> {

    public static final String TABLENAME = "BRACELET_INFO";

    /**
     * Properties of entity BraceletInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Bracelet = new Property(1, String.class, "bracelet", false, "BRACELET");
        public final static Property Version = new Property(2, String.class, "version", false, "VERSION");
        public final static Property MD5 = new Property(3, String.class, "MD5", false, "MD5");
        public final static Property MacAddress = new Property(4, String.class, "MacAddress", false, "MAC_ADDRESS");
        public final static Property FileName = new Property(5, String.class, "fileName", false, "FILE_NAME");
    };


    public BraceletInfoDao(DaoConfig config) {
        super(config);
    }
    
    public BraceletInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'BRACELET_INFO' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'BRACELET' TEXT," + // 1: bracelet
                "'VERSION' TEXT," + // 2: version
                "'MD5' TEXT," + // 3: MD5
                "'MAC_ADDRESS' TEXT," + // 4: MacAddress
                "'FILE_NAME' TEXT);"); // 5: fileName
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'BRACELET_INFO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, BraceletInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String bracelet = entity.getBracelet();
        if (bracelet != null) {
            stmt.bindString(2, bracelet);
        }
 
        String version = entity.getVersion();
        if (version != null) {
            stmt.bindString(3, version);
        }
 
        String MD5 = entity.getMD5();
        if (MD5 != null) {
            stmt.bindString(4, MD5);
        }
 
        String MacAddress = entity.getMacAddress();
        if (MacAddress != null) {
            stmt.bindString(5, MacAddress);
        }
 
        String fileName = entity.getFileName();
        if (fileName != null) {
            stmt.bindString(6, fileName);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public BraceletInfo readEntity(Cursor cursor, int offset) {
        BraceletInfo entity = new BraceletInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // bracelet
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // version
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // MD5
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // MacAddress
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // fileName
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, BraceletInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBracelet(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setVersion(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMD5(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setMacAddress(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setFileName(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(BraceletInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(BraceletInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
