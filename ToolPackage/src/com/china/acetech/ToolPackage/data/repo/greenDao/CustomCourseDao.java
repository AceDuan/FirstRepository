package com.china.acetech.ToolPackage.data.repo.greenDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table CUSTOM_COURSE.
*/
public class CustomCourseDao extends AbstractDao<CustomCourse, Long> {

    public static final String TABLENAME = "CUSTOM_COURSE";

    /**
     * Properties of entity CustomCourse.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property CourseID = new Property(1, String.class, "courseID", false, "COURSE_ID");
        public final static Property CourseName = new Property(2, String.class, "courseName", false, "COURSE_NAME");
        public final static Property CategoryID = new Property(3, Integer.class, "categoryID", false, "CATEGORY_ID");
        public final static Property TrainSegmentID = new Property(4, Integer.class, "trainSegmentID", false, "TRAIN_SEGMENT_ID");
        public final static Property SyncTime = new Property(5, java.util.Date.class, "syncTime", false, "SYNC_TIME");
        public final static Property CountOfSum = new Property(6, Long.class, "CountOfSum", false, "COUNT_OF_SUM");
        public final static Property CountInWeek = new Property(7, Long.class, "CountInWeek", false, "COUNT_IN_WEEK");
        public final static Property Level = new Property(8, Integer.class, "Level", false, "LEVEL");
        public final static Property IsFavorite = new Property(9, Boolean.class, "isFavorite", false, "IS_FAVORITE");
        public final static Property FirstPicturePath = new Property(10, String.class, "firstPicturePath", false, "FIRST_PICTURE_PATH");
    };


    public CustomCourseDao(DaoConfig config) {
        super(config);
    }
    
    public CustomCourseDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'CUSTOM_COURSE' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'COURSE_ID' TEXT," + // 1: courseID
                "'COURSE_NAME' TEXT," + // 2: courseName
                "'CATEGORY_ID' INTEGER," + // 3: categoryID
                "'TRAIN_SEGMENT_ID' INTEGER," + // 4: trainSegmentID
                "'SYNC_TIME' INTEGER," + // 5: syncTime
                "'COUNT_OF_SUM' INTEGER," + // 6: CountOfSum
                "'COUNT_IN_WEEK' INTEGER," + // 7: CountInWeek
                "'LEVEL' INTEGER," + // 8: Level
                "'IS_FAVORITE' INTEGER," + // 9: isFavorite
                "'FIRST_PICTURE_PATH' TEXT);"); // 10: firstPicturePath
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'CUSTOM_COURSE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, CustomCourse entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String courseID = entity.getCourseID();
        if (courseID != null) {
            stmt.bindString(2, courseID);
        }
 
        String courseName = entity.getCourseName();
        if (courseName != null) {
            stmt.bindString(3, courseName);
        }
 
        Integer categoryID = entity.getCategoryID();
        if (categoryID != null) {
            stmt.bindLong(4, categoryID);
        }
 
        Integer trainSegmentID = entity.getTrainSegmentID();
        if (trainSegmentID != null) {
            stmt.bindLong(5, trainSegmentID);
        }
 
        java.util.Date syncTime = entity.getSyncTime();
        if (syncTime != null) {
            stmt.bindLong(6, syncTime.getTime());
        }
 
        Long CountOfSum = entity.getCountOfSum();
        if (CountOfSum != null) {
            stmt.bindLong(7, CountOfSum);
        }
 
        Long CountInWeek = entity.getCountInWeek();
        if (CountInWeek != null) {
            stmt.bindLong(8, CountInWeek);
        }
 
        Integer Level = entity.getLevel();
        if (Level != null) {
            stmt.bindLong(9, Level);
        }
 
        Boolean isFavorite = entity.getIsFavorite();
        if (isFavorite != null) {
            stmt.bindLong(10, isFavorite ? 1l: 0l);
        }
 
        String firstPicturePath = entity.getFirstPicturePath();
        if (firstPicturePath != null) {
            stmt.bindString(11, firstPicturePath);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public CustomCourse readEntity(Cursor cursor, int offset) {
        CustomCourse entity = new CustomCourse( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // courseID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // courseName
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // categoryID
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // trainSegmentID
            cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)), // syncTime
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6), // CountOfSum
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7), // CountInWeek
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // Level
            cursor.isNull(offset + 9) ? null : cursor.getShort(offset + 9) != 0, // isFavorite
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // firstPicturePath
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, CustomCourse entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCourseID(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCourseName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCategoryID(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setTrainSegmentID(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setSyncTime(cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)));
        entity.setCountOfSum(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
        entity.setCountInWeek(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
        entity.setLevel(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setIsFavorite(cursor.isNull(offset + 9) ? null : cursor.getShort(offset + 9) != 0);
        entity.setFirstPicturePath(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(CustomCourse entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(CustomCourse entity) {
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
