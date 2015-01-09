package com.china.acetech.ToolPackage.data.repo.greenDao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import com.china.acetech.ToolPackage.MyApplication;
import de.greenrobot.dao.DbUtils;
import de.greenrobot.dao.identityscope.IdentityScopeType;

import java.io.File;

public class DaoFactory {
    private static final String TAG = "DaoFactory";
    private static DaoFactory instance = null;
    //private DaoMaster daoMaster;
    private ProductionOpenHelper helper;
    private DaoSession session;

    public static DaoFactory getInstance() {
        if (instance == null)
            instance = new DaoFactory();
        return instance;
    }

    @SuppressLint({"NewApi"})
    private static String getPathForExport() {
        if (Build.VERSION.SDK_INT >= 8) {
            File localFile = MyApplication.getTopApp().getExternalFilesDir(null);
            if (localFile != null)
                return localFile.getAbsolutePath() + "/";
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    }

    public void cleanUpStore() {
        getDaoSession().runInTx(new Runnable() {
            public void run() {
                DaoMaster.dropAllTables(DaoFactory.this.helper.getWritableDatabase(), true);
                DaoMaster.createAllTables(DaoFactory.this.helper.getWritableDatabase(), true);
            }
        });
    }

    public DaoSession getDaoSession() {
        if (helper == null) {
            helper = new ProductionOpenHelper(MyApplication.getTopApp(), "fitbit-db", null);

            //Replace to local visible
            DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
            session = daoMaster.newSession(IdentityScopeType.Session);
        }

        return session;
    }

    public void makeVacuum() {
        SQLiteDatabase localSQLiteDatabase = this.helper.getWritableDatabase();
        if (localSQLiteDatabase == null)
            return;
        DbUtils.vacuum(localSQLiteDatabase);
    }

    public static class ProductionOpenHelper extends DaoMaster.OpenHelper {


        public ProductionOpenHelper(Context paramContext, String paramString, SQLiteDatabase.CursorFactory paramCursorFactory) {
            super(paramContext, paramString, paramCursorFactory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG, "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            DaoMaster.dropAllTables(db, true);
            onCreate(db);

        }


    }
}

/* Location:           H:\backupforG\Projects\FA_Jack\apk\dex2jar-0.0.9.15\classes-dex2jar.jar
 * Qualified Name:     com.fitbit.data.repo.greendao.DaoFactory
 * JD-Core Version:    0.5.4
 */