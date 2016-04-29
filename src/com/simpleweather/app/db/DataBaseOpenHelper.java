package com.simpleweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseOpenHelper extends SQLiteOpenHelper {

//	public static final int VERSION = 1;// 版本号
//	    // 数据库名称
//	public static final String DB_NAME = "SimpleWeather.db";
//		//	SDBHelper.DB_DIR + File.separator  + "ZiHao.db";
	public static final String CREATE_COUNTY = "CREATE TABLE IF NOT EXISTS county ("
			+ "id VARCHAR(50) primary key , "
			+ "ename VARCHAR(50), "
			+ "county_name VARCHAR(50), "
			+ "city_name VARCHAR(50), "
			+ "province_name VARCHAR(50))";
	
	
	public DataBaseOpenHelper(Context context, String name, CursorFactory
			factory, int version) {
			super(context, name, factory, version);
			}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_COUNTY); // 创建County表
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
