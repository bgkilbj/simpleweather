package com.simpleweather.app.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.simpleweather.app.R;
import com.simpleweather.app.model.County;
import com.simpleweather.app.util.StopWatch;;

public class DataBaseOperator {

	/**
	* 数据库名
	*/
	public static final String DB_NAME = "SimpleWeather.db";
	/**
	* 数据库版本
	*/
	public static final int VERSION = 1;
	private static DataBaseOperator mDataBaseOperator;
	private SQLiteDatabase db;
	/**
	* 将构造方法私有化
	*/
	private DataBaseOperator(Context context) {
	DataBaseOpenHelper dbHelper = new DataBaseOpenHelper(context,
	DB_NAME, null, VERSION);
	db = dbHelper.getWritableDatabase();
	}
	/**
	* 获取DataBaseOperator的实例。
	*/
	public synchronized static DataBaseOperator getInstance(Context context) {
	if (mDataBaseOperator == null) {
		mDataBaseOperator = new DataBaseOperator(context);
	}
	return mDataBaseOperator;
	}
	
	public Cursor rawQuery(String sql)
	{
		 Cursor mCursor=db.rawQuery(sql, null);
		return mCursor;
	}
	public void insert(ContentValues contentValues)
	{
		db.insert("County", null, contentValues);

	}
	public ArrayList<County> queryInfo(String countyname) {
		ArrayList<County> resultArray = new ArrayList<County>();
		Cursor cursor = null;

		try {
			// 创建模糊查询的条件
		String sql="select * from county where county_name like '"+ countyname+"%'";
		//String sql="select * from county where id like '"	+ countyname+"%'";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst()) {
				do {
					County tempCounty=new County();
					tempCounty.setId(cursor.getString(cursor.getColumnIndex("id")));
					tempCounty.setEname(cursor.getString(cursor.getColumnIndex("ename")));
					tempCounty.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
					tempCounty.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
					tempCounty.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
					resultArray.add(tempCounty);
				}
		
			while (cursor.moveToNext()); 
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.toString();
		} 

		return resultArray;
	}
	
	
	public boolean isExist(){  
        boolean result = true;  

       String sql = "select count(*) count from county";  
    
       Cursor cursor = db.rawQuery(sql, null);  
        
       if (cursor.moveToNext() ) {  
           int count = cursor.getInt(0);  
           if(count>0){//存在数据  
                result = false;  
           }  
           cursor.close();  
      //     db.close();  
       }  
       return result;  
          
   }  
	
	public void initInsert(Context mcontext) throws IOException {  
	     
		 InputStreamReader reader = new InputStreamReader( mcontext.getResources().openRawResource(R.raw.a)); //R.raw.intial 
		    BufferedReader br = new BufferedReader(reader);  
		    String s1 = "";  
		     //通过事务，进行批量插入  
		    long a=System.currentTimeMillis();
		    StopWatch watch=new StopWatch();
		    watch.start();
		    db.beginTransaction();  
		 
		    while ((s1 = br.readLine()) != null) {  
		        String sql = s1;  
		        db.execSQL(sql);  
		    }  
		     
		    db.setTransactionSuccessful();  
		    db.endTransaction();  
		    watch.stop();
		    Log.i("time1", watch.prettyPrint());

		    long b=System.currentTimeMillis();
		    Log.i("time2", String.valueOf(b-a));
		    br.close();  
		    reader.close();  
		   // db.close();  
		 
		}  
	
	
}
