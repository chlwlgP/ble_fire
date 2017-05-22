package com.superdroid.test.zzafire;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class DBManager extends SQLiteOpenHelper {
	protected static final String FILEPATH = Environment.getExternalStorageDirectory().getPath();
	public static final String DATABASE;
	public static final String TABLE;
	public static final String col_1;
	public static final String col_2;
	public static final String col_3;
	public static final String col_4;
	public static final int VERSION;

	private static DBManager dbManager;

	private Context context;

	static {
		DATABASE = "kwon.db";
		TABLE = "Test";
		col_1 = "address";
		col_2 = "hrdata";
		col_3 = "year_month";
		col_4 =  "time";
		VERSION = 1;

		dbManager = null;
	}

	{
		context = null;
	}

	public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, Object o) {
		super(context, FILEPATH + '/' + name, factory, version);

		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String query = "CREATE TABLE " + this.TABLE + " ("
				+ this.col_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ this.col_2 + " TEXT, "
				+ this.col_3 + " TEXT);";
		db.execSQL(query);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//if (newVersion > oldVersion) {
		//	db.execSQL("ALTER TABLE " + TABLE + " ADD COLUMN " + "BP" + " INTEGER DEFAULT 0");
		//}
	}

	public static DBManager getDbManager(Context context) {
		if (dbManager == null) {
			dbManager = new DBManager(context, DATABASE, null, VERSION, null);
		}

		return dbManager;
	}

	// SELECT
	public Cursor query(String[] projection, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
		return getReadableDatabase().query(this.TABLE, projection, selection, selectionArgs, groupBy, having, orderBy);
	}

	// INSERT
	public long insert(ContentValues values) {
		return getWritableDatabase().insert(this.TABLE, null, values);
	}

	public int insertAll(ContentValues[] values) {
		SQLiteDatabase sqLiteDatabase = getWritableDatabase();

		sqLiteDatabase.beginTransaction();
		for (ContentValues v : values) {
			sqLiteDatabase.insert(this.TABLE, null, v);
		}
		sqLiteDatabase.setTransactionSuccessful();
		sqLiteDatabase.endTransaction();

		return values.length;
	}

	// UPDATE
	public int update(ContentValues values, String selection, String[] selectionArgs) {
		return getWritableDatabase().update(this.TABLE, values, selection, selectionArgs);
	}

	// DELETE
	public int delete(String selection, String[] selectionArgs) {
		return getWritableDatabase().delete(this.TABLE, selection, selectionArgs);
	}
}
