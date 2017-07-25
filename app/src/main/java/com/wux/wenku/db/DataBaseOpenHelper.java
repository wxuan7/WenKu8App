package com.wux.wenku.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WuX on 2017/5/18.
 */

public class DataBaseOpenHelper extends SQLiteOpenHelper {
    private static DataBaseOpenHelper helper = null;
    private final static Object _Lock = new Object();

    private DataBaseOpenHelper(Context context) {
        super(context, "wenku.db", null, 1);
    }

    public static DataBaseOpenHelper getInstance(Context context) {
        if (helper == null) {
            helper = new DataBaseOpenHelper(context);
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Map<String, String>> select(String sql, String[] args) throws Exception {
        synchronized (_Lock) {
            List<Map<String, String>> list = new ArrayList<>();
            SQLiteDatabase db = null;
            Cursor cursor = null;
            try {
                db = helper.getReadableDatabase();
                cursor = db.rawQuery(sql, args);
                if (cursor.moveToNext()) {
                    Map<String, String> map = new HashMap<>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        map.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                    if (map.size() > 0) {
                        list.add(map);
                    }
                }
            } catch (Exception e) {
                throw e;
            } finally {
                if (cursor != null)
                    cursor.close();
                if (db != null)
                    db.close();
            }
            return list;
        }
    }

    /**
     * 批量修改
     *
     * @param sql
     * @param list
     * @throws Exception
     */
    public void updata(String sql, List<Object[]> list) throws Exception {
        synchronized (_Lock) {
            SQLiteDatabase db = null;
            try {

                db = helper.getWritableDatabase();
                db.beginTransaction();
                for (Object[] objs : list) {
                    db.execSQL(sql, objs);
                }

                db.setTransactionSuccessful();

            } catch (Exception e) {
                throw e;
            } finally {
                if (db != null) {
                    db.endTransaction();
                    db.close();
                }
            }
        }
    }

    /**
     * 单独修改
     * 适用于修改增加删除单条sql
     *
     * @param sql
     * @param obj
     * @throws Exception
     */
    public void updata(String sql, Object[] obj) throws Exception {
        synchronized (_Lock) {
            SQLiteDatabase db = null;
            try {
                db = helper.getWritableDatabase();
                db.beginTransaction();
                db.execSQL(sql, obj);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                throw e;
            } finally {
                if (db != null) {
                    db.endTransaction();
                    db.close();
                }
            }
        }
    }

    private void createNovel(SQLiteDatabase db) {
        String sql = "";
        db.execSQL(sql);
    }
}
