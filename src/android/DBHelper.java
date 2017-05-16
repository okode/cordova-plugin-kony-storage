package kony.storage.plugin;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import org.json.JSONArray;

import com.konylabs.vm.LuaTable;

public class DBHelper {

    public static final String DATABASE_NAME = "/databases/localstorage.db";
    public static final String TABLE_NAME = "localstorage";
    public static final String COLUMN_KEY = "key";
    public static final String COLUMN_DATATYPE = "datatype";
    public static final String COLUMN_VALUE = "value";

    private Context context;
    private String konyDBPath;
    private SQLiteDatabase db;

    public DBHelper(Context context, String dbPath) {
        this.context = context;
        this.konyDBPath = dbPath;
        db = openDatabase();
    }

    public LocalStorage getData(String key) {

        if(db == null) {
            Log.d("KonyStorage", "Database File not found.");
            return null;
        }

        if(!existsTable(TABLE_NAME)) {
            Log.d("KonyStorage", "Table " + TABLE_NAME + " not found");
            return null;
        }

        Log.d("KonyStorage", "Trying to find key " + key);
        LocalStorage localStorage = null;
        Cursor rs =  db.rawQuery( "select * from " + TABLE_NAME + " where key ='" + key + "'", null );

        if (rs.moveToFirst() && rs.getCount() != 0) {
            Log.d("KonyStorage", "key entry exists");
            String keyResponse = rs.getString(rs.getColumnIndex(COLUMN_KEY));
            Integer typeResponse = rs.getInt(rs.getColumnIndex(COLUMN_DATATYPE));
            switch (typeResponse) {
                case LocalStorage.FLOAT:
                    Log.d("KonyStorage", "Float value");
                    Float floatValue = rs.getFloat(rs.getColumnIndex(COLUMN_VALUE));
                    localStorage = new LocalStorage(keyResponse, floatValue);
                    break;
                case LocalStorage.INTEGER:
                    Log.d("KonyStorage", "Integer value");
                    Integer integerValue = rs.getInt(rs.getColumnIndex(COLUMN_VALUE));
                    localStorage = new LocalStorage(keyResponse, integerValue);
                    break;
                case LocalStorage.BOOLEAN:
                    Log.d("KonyStorage", "Boolean value");
                    Integer intValue = rs.getInt(rs.getColumnIndex(COLUMN_VALUE));
                    if (intValue == 0) {
                        localStorage = new LocalStorage(keyResponse, false);
                    } else {
                        localStorage = new LocalStorage(keyResponse, true);
                    }
                    break;
                case LocalStorage.BLOB:
                    Log.d("KonyStorage", "Byte array value");
                    byte[] byteValue = rs.getBlob(rs.getColumnIndex(COLUMN_VALUE));
                    try {
                        localStorage = new LocalStorage(keyResponse, toLuaTable(byteValue));
                    } catch (IOException e) {
                        Log.d("KonyStorage", "IOException" + e.getCause().getMessage());
                    } catch (ClassNotFoundException e) {
                        Log.d("KonyStorage", "ClassNotFoundException" + e.getException().getMessage());
                    }
                    break;
                case LocalStorage.STRING:
                default:
                    Log.d("KonyStorage", "String value");
                    String stringValue = rs.getString(rs.getColumnIndex(COLUMN_VALUE));
                    localStorage = new LocalStorage(keyResponse, stringValue);
            }
        } else {
            Log.d("KonyStorage", "Key not found");
        }
        if (!rs.isClosed()) {
            rs.close();
        }
        return localStorage;
    }

    public JSONArray getKeySet() {
        if(db == null) {
            Log.d("KonyStorage", "Database File not found.");
            return new JSONArray();
        }

        if(!existsTable(TABLE_NAME)) {
            Log.d("KonyStorage", "Table " + TABLE_NAME + " not found");
            return new JSONArray();
        }

        Log.d("KonyStorage", "Trying to obtain key set");
        Cursor rs =  db.rawQuery( "select key from " + TABLE_NAME, null );
        ArrayList<String> keys = new ArrayList<String>();
        if (rs.moveToFirst() && rs.getCount() != 0) {
            String key = rs.getString(rs.getColumnIndex(COLUMN_KEY));
            keys.add(key);
            while(rs.moveToNext()) {
                key = rs.getString(rs.getColumnIndex(COLUMN_KEY));
                keys.add(key);
            }
        }

        if (!rs.isClosed()) {
            rs.close();
        }
        return new JSONArray(keys);
    }

    private boolean existsTable(String tableName) {

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    private SQLiteDatabase openDatabase() {
        Log.d("KonyStorage", "Trying to open data base file " + konyDBPath + DATABASE_NAME);

        File dbFile = new File(konyDBPath + DATABASE_NAME);

        if (!dbFile.exists()) {
            return null;
        }
        Log.d("KonyStorage", "Table path " + dbFile.getPath());
        Log.d("KonyStorage", "DB Size: " + dbFile.length());

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READONLY);
    }

    public static LuaTable toLuaTable(byte[] bytes) throws IOException, ClassNotFoundException {
        LuaTable obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = (LuaTable)ois.readObject();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (ois != null) {
                ois.close();
            }
        }
        return obj;
    }
}
