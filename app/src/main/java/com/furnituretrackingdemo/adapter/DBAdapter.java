package com.furnituretrackingdemo.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	
	private static final String TAG = "DBAdapter";
	
	private static final String DATABASE_NAME = "furnituretrack.db";

	private static final int DATABASE_VERSION = 1;
	
	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	
	private static final String TN_ITEM_TRACK = "item_track";
	
	public static final String KEY_ROWID = "_ID";
	public static final String KEY_NAME = "NAME";
	public static final String KEY_DESC = "DESCRIPTION";
	public static final String KEY_IMAGE = "IMAGE_PATH";
	public static final String KEY_LOCATION = "LOCATION";
	public static final String KEY_COST = "COST";


	private static final String ITEM_TRACK_QUERY = "create table item_track (_ID integer primary key autoincrement, " +
			"NAME text, DESCRIPTION text, IMAGE_PATH text, LOCATION text, COST integer);";

	
	public DBAdapter(Context ctx) {
		this.context = ctx;
		
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			
			db.execSQL(ITEM_TRACK_QUERY);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");

			db.execSQL("DROP TABLE IF EXISTS " + TN_ITEM_TRACK);

			onCreate(db);
		}
	}

	// ---opens the database---
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}
	
    
    public long addItem(String name, String desc, String image_path, String location, int cost) 
	{
		ContentValues initialValues = new ContentValues();

		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_DESC, desc);
		initialValues.put(KEY_IMAGE, image_path);
		initialValues.put(KEY_LOCATION, location);
		initialValues.put(KEY_COST, cost);

		return db.insert(TN_ITEM_TRACK, null, initialValues);
	}
	
    public Cursor getItem() throws SQLException
    {
		    Cursor mCursor = db.rawQuery("select * from "+TN_ITEM_TRACK, null);
		    return mCursor;
    }

	public void updateItem(int item_id, String name, String desc, String image_path, String location, int cost)
	{
		ContentValues initialValues = new ContentValues();

		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_DESC, desc);
		initialValues.put(KEY_IMAGE, image_path);
		initialValues.put(KEY_LOCATION, location);
		initialValues.put(KEY_COST, cost);

		db.update(TN_ITEM_TRACK, initialValues, KEY_ROWID + "=" + item_id, null);
	}
    
    public void deleteItem(int item_id)
    {
		db.execSQL("delete from "+TN_ITEM_TRACK+" where _ID="+item_id);
	}
}
