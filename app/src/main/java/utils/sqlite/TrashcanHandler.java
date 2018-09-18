package utils.sqlite;

/**
 * RagnSells
 * Created by Andreas on 24.05.2017.
 */

import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import model.Trashcan;

public class TrashcanHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ragnsells";
    private static final String TABLE_TRASHCANS = "trashcans";
    private static final String KEY_ID = "id";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";
    private static final String KEY_ISSUE = "issue";
    private static final String KEY_USER_ID_FK = "user_id_fk";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";
    private static final String KEY_TRASHCAN_ID = "trashcan_id";
    private static final String KEY_PLACE_NAME = "place_name";

    public TrashcanHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TRASHCANS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TRASHCANS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TRASHCAN_ID + " TEXT,"
                + KEY_LONGITUDE + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_CREATED_AT + " TEXT,"
                + KEY_UPDATED_AT + " TEXT,"
                + KEY_ISSUE + " TEXT,"
                + KEY_USER_ID_FK + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_PLACE_NAME + " TEXT" + ")";
        db.execSQL(CREATE_TRASHCANS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRASHCANS);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    public void addTrashcan(Trashcan trashcan) {
        SQLiteDatabase db = this.getWritableDatabase();

        onCreate(db);

        ContentValues values = new ContentValues();
        values.put(KEY_TRASHCAN_ID, trashcan.getId());
        values.put(KEY_LATITUDE, trashcan.getLatitude());
        values.put(KEY_LONGITUDE, trashcan.getLongitude());
        values.put(KEY_CREATED_AT, trashcan.getCreated_at());
        values.put(KEY_UPDATED_AT, trashcan.getUpdated_at());
        values.put(KEY_ISSUE, trashcan.getIssue());
        values.put(KEY_USER_ID_FK, trashcan.getUser_id_fk());
        values.put(KEY_EMAIL, trashcan.getEmail());
        values.put(KEY_NAME, trashcan.getName());
        values.put(KEY_PLACE_NAME, trashcan.getPlace_name());

        // Inserting Row
        db.insert(TABLE_TRASHCANS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single contact
    Trashcan getTrashcan(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TRASHCANS, new String[] { KEY_ID,
                        KEY_TRASHCAN_ID,
                        KEY_LONGITUDE, KEY_LATITUDE,
                        KEY_CREATED_AT, KEY_UPDATED_AT,
                        KEY_ISSUE, KEY_USER_ID_FK,
                        KEY_EMAIL, KEY_NAME}, KEY_TRASHCAN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Trashcan trashcan = new Trashcan();
        trashcan.setId(cursor.getInt(1));
        trashcan.setLongitude(cursor.getDouble(2));
        trashcan.setLatitude(cursor.getDouble(3));
        trashcan.setCreated_at(cursor.getString(4));
        trashcan.setUpdated_at(cursor.getString(5));
        trashcan.setIssue(cursor.getString(6));
        trashcan.setUser_id_fk(cursor.getInt(7));
        trashcan.setEmail(cursor.getString(8));
        trashcan.setName(cursor.getString(9));
        trashcan.setPlace_name(cursor.getString(10));
        return trashcan;
    }

    // code to get all contacts in a list view
    public ArrayList<Trashcan> getAllTrashcans() {
        ArrayList<Trashcan> trashcanList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRASHCANS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Trashcan trashcan = new Trashcan();
                trashcan.setId(cursor.getInt(1));
                trashcan.setLongitude(cursor.getDouble(2));
                trashcan.setLatitude(cursor.getDouble(3));
                trashcan.setCreated_at(cursor.getString(4));
                trashcan.setUpdated_at(cursor.getString(5));
                trashcan.setIssue(cursor.getString(6));
                trashcan.setUser_id_fk(cursor.getInt(7));
                trashcan.setEmail(cursor.getString(8));
                trashcan.setName(cursor.getString(9));
                trashcan.setPlace_name(cursor.getString(10));
                // Adding contact to list
                trashcanList.add(trashcan);
            } while (cursor.moveToNext());
        }

        // return contact list
        return trashcanList;
    }

    // code to update the single contact
    public int updateTrashcan(Trashcan trashcan) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, trashcan.getLatitude());
        values.put(KEY_LONGITUDE, trashcan.getLongitude());
        values.put(KEY_CREATED_AT, trashcan.getCreated_at());
        values.put(KEY_UPDATED_AT, trashcan.getUpdated_at());
        values.put(KEY_ISSUE, trashcan.getIssue());
        values.put(KEY_USER_ID_FK, trashcan.getUser_id_fk());
        values.put(KEY_EMAIL, trashcan.getEmail());
        values.put(KEY_NAME, trashcan.getName());
        values.put(KEY_PLACE_NAME, trashcan.getPlace_name());

        // updating row
        return db.update(TABLE_TRASHCANS, values, KEY_TRASHCAN_ID + " = ?",
                new String[] { String.valueOf(trashcan.getId()) });
    }

    // Deleting single trashcan
    public void deleteTrashcan(Trashcan trashcan) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRASHCANS, KEY_TRASHCAN_ID + " = ?",
                new String[] { String.valueOf(trashcan.getId()) });
        db.close();
    }

    // Deleting all trashcans
    public void deleteTrashcans() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+TABLE_TRASHCANS+"'", null);
        if(cursor.getCount() > 0) {
            db.delete(TABLE_TRASHCANS, null, null);
        }
        db.close();
    }

    // Getting contacts Count
    public int getTrashcansCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TRASHCANS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}