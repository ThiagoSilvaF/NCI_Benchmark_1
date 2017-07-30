package ie.aomonitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ie.aomonitor.utils.SharedPreferencesUtils;

import static ie.aomonitor.Constants.FAST_CONNECTED;
import static ie.aomonitor.Constants.OFF_LINE_TESTED;
import static ie.aomonitor.Constants.SLOW_CONNECTED;

/**
 * Created by silvat on 24/05/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "monitor";

    // Contacts table name
    private static final String TABLE_PROCESS_INFO = "process_info";
    private static final String TABLE_CONFIG = "config";

    // Contacts Table Columns names
    private static final String KEY_ID          = "id";

    private static final String KEY_CONFIG_NAME = "method_name";
    private static final String KEY_CONFIG_VALUE= "method_value";

    private static final String KEY_BRAND       = "brand";
    private static final String KEY_DEVICE      = "device";
    private static final String KEY_HARDWARE    = "hardware";
    private static final String KEY_MODEL       = "model";
    private static final String KEY_PRODUCT     = "product";
    private static final String KEY_MANUFACTURER = "manufacturer";
    private static final String KEY_BOARD       = "board";
    private static final String KEY_START_TIME  = "start_time";
    private static final String KEY_END_TIME    = "end_time";
    private static final String KEY_MEMORY      = "memory";
    private static final String KEY_PROCESSOR   = "processor";
    private static final String KEY_APP         = "app_unique_key";
    private static final String KEY_METHOD_NAME = "method_name";
    private static final String KEY_ENVIRONMENT  = "environment";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

       /* context.deleteDatabase(DATABASE_NAME);
        SharedPreferencesUtils sharedPreferences = new SharedPreferencesUtils(context);
        sharedPreferences.setPreference(OFF_LINE_TESTED, null);
        sharedPreferences.setPreference(SLOW_CONNECTED, null);
        sharedPreferences.setPreference(FAST_CONNECTED, null);*/
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_PROCESS_INFO_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PROCESS_INFO + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_BRAND + " TEXT,"
                + KEY_DEVICE + " TEXT,"
                + KEY_HARDWARE + " TEXT,"
                + KEY_MODEL + " TEXT,"
                + KEY_PRODUCT + " TEXT,"
                + KEY_MANUFACTURER + " TEXT,"
                + KEY_BOARD + " TEXT,"
                + KEY_START_TIME + " INTEGER,"
                + KEY_END_TIME + " INTEGER,"
                + KEY_MEMORY + " TEXT,"
                + KEY_APP + " TEXT,"
                + KEY_METHOD_NAME + " TEXT,"
                + KEY_PROCESSOR + " TEXT, "
                + KEY_ENVIRONMENT + " TEXT )";
        db.execSQL(CREATE_PROCESS_INFO_TABLE);

        String CREATE_CONFIG_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CONFIG + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CONFIG_NAME + " TEXT,"
                + KEY_CONFIG_VALUE + " TEXT ) ";
        db.execSQL(CREATE_CONFIG_TABLE);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROCESS_INFO);

        // Create tables again
        onCreate(db);
    }


    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addProcessInfo( DeviceLog deviceLog) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BRAND, deviceLog.getBrand());
        values.put(KEY_DEVICE, deviceLog.getDevice());
        values.put(KEY_HARDWARE, deviceLog.getHardware());
        values.put(KEY_MODEL, deviceLog.getModel());
        values.put(KEY_PRODUCT, deviceLog.getProduct());
        values.put(KEY_MANUFACTURER, deviceLog.getManufacturer());
        values.put(KEY_BOARD, deviceLog.getBoard());
        values.put(KEY_START_TIME, deviceLog.getStartTime());
        values.put(KEY_END_TIME, deviceLog.getEndTime());
        values.put(KEY_MEMORY, deviceLog.getMemory());
        values.put(KEY_PROCESSOR, deviceLog.getProcessor());
        values.put(KEY_APP, deviceLog.getAppKey());
        values.put(KEY_METHOD_NAME, deviceLog.getMethodName());
        values.put(KEY_ENVIRONMENT, deviceLog.getEnvironment());

        // Inserting Row
        db.insert(TABLE_PROCESS_INFO, null, values);
        db.close(); // Closing database connection


    }

    // Adding new contact
    public void addConfigInfo( String name, String value) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CONFIG_NAME, name);
        values.put(KEY_CONFIG_VALUE, value);
        // Inserting Row
        db.insert(TABLE_CONFIG, null, values);
        db.close(); // Closing database connection

        //  deleteAll();
    }

    public int updateConfigInfo(String name, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(KEY_CONFIG_NAME, name);
        values.put(KEY_CONFIG_VALUE, value);
        // updating row
        return db.update(TABLE_CONFIG, values, KEY_CONFIG_NAME + " = ?",
                new String[] { String.valueOf(name) });
    }

    //0 - local
    //1 - remote
    public String getConfigByMethodName(String name) {
        List<ConfigLog> list = new ArrayList<ConfigLog>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CONFIG;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        ConfigLog dp = new ConfigLog();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                //dp.setID(Integer.parseInt(cursor.getString(0)));
                dp.setName(name);
                dp.setValue(cursor.getString(2));

            } while (cursor.moveToNext());
        }

        // return contact list
        return dp.getValue();



        /*
        Cursor cursor = null;
        try {
            String countQuery = "SELECT * FROM " + TABLE_CONFIG +
                " WHERE "+KEY_CONFIG_NAME+" = ? ";
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_CONFIG_NAME, name);
            cursor = db.rawQuery(countQuery, null);
            String ret = null;
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                ret = cursor.getString(cursor.getColumnIndex("1"));
            }
            return ret;
        }finally {
            cursor.close();
        }
*/
    }

    // Getting All DB Info
    public List<DeviceLog> getAllProcessInfo() {
        List<DeviceLog> list = new ArrayList<DeviceLog>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PROCESS_INFO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DeviceLog dp = new DeviceLog();
                //dp.setID(Integer.parseInt(cursor.getString(0)));
                dp.setBrand(cursor.getString(1));
                dp.setDevice(cursor.getString(2));
                dp.setHardware(cursor.getString(3));
                dp.setModel(cursor.getString(4));
                dp.setProduct(cursor.getString(5));
                dp.setManufacturer(cursor.getString(6));
                dp.setBoard(cursor.getString(7));
                dp.setStartTime(cursor.getLong(8));
                dp.setEndTime(cursor.getLong(9));
                dp.setMemory(cursor.getString(10));
                dp.setAppKey(cursor.getString(11));
                dp.setMethodName(cursor.getString(12));
                dp.setProcessor(cursor.getString(13));
                dp.setEnvironment(cursor.getString(14));


                // Adding contact to list
                list.add(dp);
            } while (cursor.moveToNext());
        }

        // return contact list
        return list;
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM "+TABLE_PROCESS_INFO;
        db.execSQL(delete);
    }



    // Deleting single contact
    /*public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }*/


    // Getting contacts Count
    /*public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }*/
}