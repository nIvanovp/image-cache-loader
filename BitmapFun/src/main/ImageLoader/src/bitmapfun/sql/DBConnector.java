package bitmapfun.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import bitmapfun.model.RemoteObject;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DBConnector {

    // Данные базы данных и таблиц
    private String DATABASE_NAME = "videoviewer.db";
    private static final int DATABASE_VERSION = 1;
    private String TABLE_NAME;

    // Название столбцов
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_KEY = "Key";
    private static final String COLUMN_VALUE = "Value";
    private String[] ALL_COLUMN = { COLUMN_ID,
            COLUMN_KEY, COLUMN_VALUE};

    // Номера столбцов
    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_KEY = 1;
    private static final int NUM_COLUMN_VALUE = 2;

    private SQLiteDatabase mDataBase;

    OpenHelper mOpenHelper;
    public DBConnector(Context context) {
        TABLE_NAME = "sample";
        DATABASE_NAME = "sample.db";
        mOpenHelper = new OpenHelper(context);
        //mDataBase = mOpenHelper.getWritableDatabase();
    }

    public DBConnector openDB(){
        try{
//            if(this.isOpen())
            mDataBase = mOpenHelper.getWritableDatabase();
        } catch(Exception ex){
            ex.printStackTrace();
        }
        return this;
    }

    public void closeDB(){
        try{
//            if(this.isOpen())
            mDataBase.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public boolean isOpen(){
        return mDataBase.isOpen();
    }

    public long insert(RemoteObject remoteObject) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_KEY, remoteObject.getKey());
        cv.put(COLUMN_VALUE, remoteObject.getValue());

        long result =  mDataBase.insert(TABLE_NAME, null, cv);
        return result;
    }

    byte [] getBitesFromBitmap(Bitmap bitmap){
        if(bitmap != null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        }
        return null;
    }

    public int update(RemoteObject remoteObject, long index) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_KEY, remoteObject.getKey());
        cv.put(COLUMN_VALUE, remoteObject.getValue());
        return mDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[] { String.valueOf(index) });
    }

    public void deleteAll() {
        mDataBase.delete(TABLE_NAME, null, null);
    }

    public void delete(long id) {
        mDataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public RemoteObject select(long id) {
        Cursor cursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[] { String.valueOf(id) }, null, null, COLUMN_KEY);

        cursor.moveToFirst();
        String name = cursor.getString(NUM_COLUMN_KEY);
        String description = cursor.getString(NUM_COLUMN_VALUE);
        RemoteObject video = new RemoteObject(id, name, description);
        cursor.close();
        return video;
    }

    Bitmap byteArrayToBitmap(byte [] bytes){
        if(bytes != null)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return  null;
    }

    public ArrayList<RemoteObject> selectAll() {
        Cursor cursor = mDataBase.query(TABLE_NAME, ALL_COLUMN, null, null, null, null, null);

        ArrayList<RemoteObject> arr = new ArrayList<RemoteObject>();
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                long id = cursor.getLong(NUM_COLUMN_ID);
                String name = cursor.getString(NUM_COLUMN_KEY);
                String description = cursor.getString(NUM_COLUMN_VALUE);
                arr.add(new RemoteObject(id, name, description));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return arr;
    }

    public String getValueByKey(String key){
        Cursor cursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, COLUMN_KEY);

        key = key.trim().toLowerCase();
        String currentremoteObjectKey;

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) do {
            currentremoteObjectKey = (cursor.getString(NUM_COLUMN_KEY).trim()).toLowerCase();

            if (currentremoteObjectKey.equals(key)){
                String value = cursor.getString(NUM_COLUMN_VALUE);
                cursor.close();
                return value;
            }

        } while (cursor.moveToNext());
        cursor.close();
        return null;
    }

    public long getRemoteObjectId(RemoteObject remoteObject) {
        Cursor cursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, COLUMN_KEY);

        String remoteObjectKey = (remoteObject.getKey().trim()).toLowerCase();
        String currentremoteObjectKey;

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) do {
            currentremoteObjectKey = (cursor.getString(NUM_COLUMN_KEY).trim()).toLowerCase();
            long id = cursor.getLong(NUM_COLUMN_ID);

            if (remoteObjectKey.equals(currentremoteObjectKey) ||
                    currentremoteObjectKey.contains(remoteObjectKey)){
                cursor.close();
                return id;
            }

        } while (cursor.moveToNext());
        cursor.close();
        return -1;
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {              //todo
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_KEY + " TEXT, " +
                    COLUMN_VALUE + " TEXT ); ";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}