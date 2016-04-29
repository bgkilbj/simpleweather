package com.simpleweather.app.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

import com.simpleweather.app.model.RecyclerViewItem;

public class JsonUtility {

	/**
	 * 解析服务器返回的JSON数据，并将解析出的数据存储到本地。
	 */
	
	
	public static void handleWeatherResponse(Context context, String response)throws Exception  {
		try {
			JSONObject jsonObject0=new JSONObject(response);//整个是一个JSONObject
			JSONArray jsonArray1=jsonObject0.getJSONArray("HeWeather data service 3.0");//第二层是一个JSONArray
			JSONObject jsonObject1=jsonArray1.getJSONObject(0);//第三层是第二层的唯一一个元素，即一个JSONObject
			
			//JSONObject aqi=jsonObject1.getJSONObject("aqi");//七大块，按key获取

			String[] result=new String[12];
			String[] weathercode=new String[5];
			JSONObject basic=jsonObject1.getJSONObject("basic");
			String countyid=basic.getString("id");
			result[0] = basic.getString("city");//城市名
			JSONObject update=basic.getJSONObject("update");
			result[1]=update.getString("loc");//发布时间

			JSONArray daily_forecast=jsonObject1.getJSONArray("daily_forecast");
			ArrayList<RecyclerViewItem> items = new ArrayList<RecyclerViewItem>();
			for(int i=0;i<4;i++)
			{
				JSONObject day0=daily_forecast.getJSONObject(i);
				JSONObject day0cond=day0.getJSONObject("cond");
				RecyclerViewItem item=new RecyclerViewItem();
				item.setDescription(day0cond.getString("txt_d"));//天气情况
				weathercode[i+1]=day0cond.getString("code_d");//天气代码
				item.setTitle(getWeek(day0.getString("date")));//周几
				JSONObject day0tmp=day0.getJSONObject("tmp");
				item.setTemp1(day0tmp.getString("min"));//
				item.setTemp2(day0tmp.getString("max"));//
				items.add(item);
			}
			//JSONArray hourly_forecast=jsonObject1.getJSONArray("hourly_forecast");

			JSONObject now=jsonObject1.getJSONObject("now");
			JSONObject cond=now.getJSONObject("cond");
			result[2]=cond.getString("txt");//当前天气情况
			weathercode[0]=cond.getString("code");
			result[3]=now.getString("tmp");//当前温度
			JSONObject wind=now.getJSONObject("wind");
			result[4]=wind.getString("dir");//当前风向
			result[5]=wind.getString("sc");//当前风力

			//JSONObject status=jsonObject1.getJSONObject("status");

			JSONObject suggestion=jsonObject1.getJSONObject("suggestion");
			JSONObject trav=suggestion.getJSONObject("trav");
			result[6]=trav.getString("brf");//旅游指数
			JSONObject cw=suggestion.getJSONObject("cw");
			result[7]=cw.getString("brf");//洗车指数
			JSONObject drsg=suggestion.getJSONObject("drsg");
			result[8]=drsg.getString("brf");//穿衣指数
			JSONObject flu=suggestion.getJSONObject("flu");
			result[9]=flu.getString("brf");//感冒指数
			JSONObject sport=suggestion.getJSONObject("sport");
			result[10]=sport.getString("brf");//运动指数
			JSONObject uv=suggestion.getJSONObject("uv");
			result[11]=uv.getString("brf");//紫外线指数
			

			saveWeatherInfo(context, result,weathercode, items,countyid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
	 */
	public static void saveWeatherInfo(Context context, String[] result,String[] weathercode,ArrayList<RecyclerViewItem> items,String countyid) 
	{
		SharedPreferences prefs = context.getSharedPreferences(countyid,0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("city_name", result[0]);
		editor.putString("loc", result[1]);
		editor.putString("txt", result[2]);
		editor.putString("tmp", result[3]);
		editor.putString("dir", result[4]);
		editor.putString("sc", result[5]);
		editor.putString("travbrf", result[6]);
		editor.putString("cwbrf", result[7]);
		editor.putString("drsgbrf", result[8]);
		editor.putString("flubrf", result[9]);
		editor.putString("sportbrf", result[10]);
		editor.putString("uvbrf", result[11]);
		editor.putString("day0title", items.get(0).getTitle());
		editor.putString("day0temp1", items.get(0).getTemp1());
		editor.putString("day0temp2", items.get(0).getTemp2());
		editor.putString("day0description", items.get(0).getDescription());
		editor.putString("day1title", items.get(1).getTitle());
		editor.putString("day1temp1", items.get(1).getTemp1());
		editor.putString("day1temp2", items.get(1).getTemp2());
		editor.putString("day1description", items.get(1).getDescription());
		editor.putString("day2title", items.get(2).getTitle());
		editor.putString("day2temp1", items.get(2).getTemp1());
		editor.putString("day2temp2", items.get(2).getTemp2());
		editor.putString("day2description", items.get(2).getDescription());
		editor.putString("day3title", items.get(3).getTitle());
		editor.putString("day3temp1", items.get(3).getTemp1());
		editor.putString("day3temp2", items.get(3).getTemp2());
		editor.putString("day3description", items.get(3).getDescription());
		editor.putString("0c",weathercode[0]);
		editor.putString("1c",weathercode[1]);
		editor.putString("2c",weathercode[2]);
		editor.putString("3c",weathercode[3]);
		editor.putString("4c",weathercode[4]);



		editor.commit();
	}
	
	/**
	  * 将短时间格式字符串转换为时间 yyyy-MM-dd 
	  * 
	  * @param strDate
	  * @return
	  */
	public static Date strToDate(String strDate) throws Exception  {
	  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	  Date strtodate = formatter.parse(strDate);
	  return strtodate;
	}
	
	/**
	  * 根据一个日期，返回是星期几的字符串
	  * 
	  * @param sdate
	  * @return
	  */
	public static String getWeek(String sdate) throws Exception {
	  // 再转换为时间
	  Date date = strToDate(sdate);
	  Calendar c = Calendar.getInstance();
	  c.setTime(date);
	  String[] WEEK = {
		  "周日",
		  "周一",
		  "周二",
		  "周三",
		  "周四",
		  "周五",
		  "周六"
		};
	  int dayIndex =c.get(Calendar.DAY_OF_WEEK);
	  return WEEK[dayIndex - 1];
	  }
}
