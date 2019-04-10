package id.havanah.app.dietonline.helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by farhan at 08:52
 * on 31/03/2019.
 * Havanah Team, ID.
 */
public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "dion";

    // Login table name
    private static final String TABLE_USER = "userdata";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_UID = "uid";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_CITY = "city";
    private static final String KEY_SUBDISTRICT = "subdistrict";
    private static final String KEY_NAME = "name";
    private static final String KEY_NICKNAME = "nickname";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_BIRTH_DATE = "birth_date";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_PROHIBITION = "prohibition";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_UID + " TEXT,"
                + KEY_USERNAME + " TEXT UNIQUE," + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_CITY + " TEXT," + KEY_SUBDISTRICT + " TEXT,"
                + KEY_NAME + " TEXT," + KEY_NICKNAME + " TEXT,"
                + KEY_ADDRESS + " TEXT," + KEY_PHONE + " TEXT,"
                + KEY_BIRTH_DATE + " TEXT," + KEY_GENDER + " TEXT,"
                + KEY_WEIGHT + " TEXT," + KEY_HEIGHT + " TEXT," + KEY_PROHIBITION + " TEXT,"
                + KEY_CREATED_AT + " TEXT," + KEY_UPDATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     */
    public void addUser(String uid, String username, String email, String city, String subdistrict, String name, String nickname, String address, String phone, String birth_date, String gender, String created_at, String updated_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_UID, uid); // UID
        values.put(KEY_USERNAME, username); // Username
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_CITY, city); // City
        values.put(KEY_SUBDISTRICT, subdistrict); // Subdistrict
        values.put(KEY_NAME, name); // Name
        values.put(KEY_NICKNAME, nickname); // Nickname
        values.put(KEY_ADDRESS, address); // Email
        values.put(KEY_PHONE, phone); // Phone
        values.put(KEY_BIRTH_DATE, birth_date); // Birth date
        values.put(KEY_GENDER, gender); // Gender
        values.put(KEY_CREATED_AT, created_at); // Created At
        values.put(KEY_UPDATED_AT, updated_at); // Updated At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void updatePersonalInfo(String name, String nickname, String city, String subdistrict, String address, String phone, String birth_date, String gender, String updated_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name;
        values.put(KEY_NICKNAME, nickname); // Nickname
        values.put(KEY_CITY, city); // City
        values.put(KEY_SUBDISTRICT, subdistrict); // Subdistrict
        values.put(KEY_ADDRESS, address); // Email
        values.put(KEY_PHONE, phone); // Phone
        values.put(KEY_BIRTH_DATE, birth_date); // Birth date
        values.put(KEY_GENDER, gender); // Gender
        values.put(KEY_UPDATED_AT, updated_at); // Updated at

        // Inserting Row
        long id = db.update(TABLE_USER, values, null, null);
        db.close(); // Closing database connection

        Log.d(TAG, "Update info inserted into sqlite: " + id);
    }

    public void updateAccountInfo(String username, String email, String updated_at) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_USERNAME, username);
        values.put(KEY_EMAIL, email);
        values.put(KEY_UPDATED_AT, updated_at);
        long id = db.update(TABLE_USER, values, null, null);
        db.close();

        Log.d(TAG, "Update info inserted into sqlite: " + id);
    }

    /*
     * User add the weight and height data
     */
    public void updateMedicalInfo(String weight, String height, String prohibition, String updated_at) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_WEIGHT, weight);
        values.put(KEY_HEIGHT, height);
        values.put(KEY_PROHIBITION, prohibition);
        values.put(KEY_UPDATED_AT, updated_at);
        long id = db.update(TABLE_USER, values, null, null);
        db.close();
        Log.d(TAG, "User weight and height inserted into sqlite: " + id);

    }


    /**
     * Getting user data from database
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("uid", cursor.getString(1));
            user.put("username", cursor.getString(2));
            user.put("email", cursor.getString(3));
            user.put("city", cursor.getString(4));
            user.put("subdistrict", cursor.getString(5));
            user.put("name", cursor.getString(6));
            user.put("nickname", cursor.getString(7));
            user.put("address", cursor.getString(8));
            user.put("phone", cursor.getString(9));
            user.put("birth_date", cursor.getString(10));
            user.put("gender", cursor.getString(11));
            user.put("weight", cursor.getString(12));
            user.put("height", cursor.getString(13));
            user.put("prohibition", cursor.getString(14));
            user.put("created_at", cursor.getString(15));
            user.put("updated_at", cursor.getString(16));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Getting user login_view status return true if rows are there in table
     */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }
}
