package utils.sqlite;

/**
 * RagnSells
 * Created by Andreas on 24.05.2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import model.Trashcan;
import model.User;

public class UserHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ragnsells";
    private static final String TABLE_USERS = "users";
    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";
    private static final String KEY_ROLE = "role";
    private static final String KEY_PHONE_NR = "phone_nr";

    public UserHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USER_ID + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_CREATED_AT + " TEXT,"
                + KEY_UPDATED_AT + " TEXT,"
                + KEY_ROLE + " TEXT,"
                + KEY_PHONE_NR + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, user.getId());
        values.put(KEY_NAME, user.getName());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_CREATED_AT, user.getCreated_at());
        values.put(KEY_UPDATED_AT, user.getUpdated_at());
        values.put(KEY_ROLE, user.getRole());
        values.put(KEY_PHONE_NR, user.getPhone_nr());
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID,
                        KEY_USER_ID,
                        KEY_NAME, KEY_EMAIL,
                        KEY_CREATED_AT, KEY_UPDATED_AT,
                        KEY_ROLE, KEY_PHONE_NR}, KEY_EMAIL + "=?",
                new String[] { email }, null, null, null, null);
        User user = new User();

        if (cursor != null)
            cursor.moveToFirst();
        user.setId(cursor.getInt(1));
        user.setName(cursor.getString(2));
        user.setEmail(cursor.getString(3));
        user.setCreated_at(cursor.getString(4));
        user.setUpdated_at(cursor.getString(5));
        user.setRole(cursor.getString(6));
        user.setPhone_nr(cursor.getString(7));

        return user;
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> userList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(0));
                user.setName(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setCreated_at(cursor.getString(3));
                user.setUpdated_at(cursor.getString(4));
                user.setRole(cursor.getString(5));
                user.setPhone_nr(cursor.getString(6));
                userList.add(user);
            } while (cursor.moveToNext());
        }

        return userList;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_USER_ID, user.getId());
        values.put(KEY_NAME, user.getName());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_CREATED_AT, user.getCreated_at());
        values.put(KEY_UPDATED_AT, user.getUpdated_at());
        values.put(KEY_ROLE, user.getRole());
        values.put(KEY_PHONE_NR, user.getPhone_nr());

        return db.update(TABLE_USERS, values, KEY_USER_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
    }

    public void deleteUser(Trashcan trashcan) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_USERS, KEY_USER_ID + " = ?",
                new String[] { String.valueOf(trashcan.getId()) });
        db.close();
    }

    public void removeUsers() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_USERS, null, null);
        db.close();
    }

    public int getUsersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        cursor.close();

        return cursor.getCount();
    }

}