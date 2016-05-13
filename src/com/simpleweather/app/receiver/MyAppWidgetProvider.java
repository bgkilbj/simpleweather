package com.simpleweather.app.receiver;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.simpleweather.app.R;
import com.simpleweather.app.util.HttpCallbackListener;
import com.simpleweather.app.util.HttpUtil;
import com.simpleweather.app.util.JsonUtility;

public class MyAppWidgetProvider extends AppWidgetProvider {
	
	private static int index=1;//当前页面
	private static int count=1;//城市总数
	//private static final String SWITCH_CLICK = "my.action.SWITCH_CLICK";
	public static int tp=0;
	@Override
	public void onReceive(final Context context, Intent intent) {
		String action = intent.getAction();  
        Log.d("AppWidgetProvider", "action:"+action); 

          
//	     if(SWITCH_CLICK.equals(action))
//	        {
	     if (intent.hasCategory(Intent.CATEGORY_ALTERNATIVE))
	     {
	    	 Uri data = intent.getData();  
	         int buttonId = Integer.parseInt(data.getSchemeSpecificPart());  
	         switch (buttonId) {  
	            case R.id.widget_refreshbutton:  

	                Log.d("AppWidgetProvider", "index:"+index); 

	            	updateWeather(context,index);
	                break;  
	            case R.id.widget_switchbutton:  
	            	index=index%count+1;	
	            	load(context,index);
	                break;  

//	    	 new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					Log.d("AppWidgetProvider", "count"+count);
//	            	index=index%count+1;	
//	            	load(context,index);					
//				}
//			}).start();
	            	
	            }  
	        }  
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		final int counter=appWidgetIds.length;
		Log.d("AppWidgetProvider", "counter"+counter);
		for(int i=0;i<counter;i++)
		{
			int appWidgetId=appWidgetIds[i];
		    pushUpdate(context,appWidgetManager,appWidgetId); 

		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {

		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onEnabled(Context context) {
//		Intent intent = new Intent(context, AutoUpdateService.class);
//	    intent.addCategory(Intent.CATEGORY_ALTERNATIVE);  

//		context.startService(intent);
		SharedPreferences prefs = context.getSharedPreferences("main",0);
		count = prefs.getInt("count", 0);
		if (count!=0) {
			load(context, index);

		}
		super.onEnabled(context);
	}

	@Override
	public void onDisabled(Context context) {

		super.onDisabled(context);
	}
	
	private void pushUpdate(Context context,AppWidgetManager appWidgetManager,int appWidgetId) {  
		Log.d("AppWidgetProvider", "appWidgetId"+appWidgetId);

        RemoteViews remoteView = new RemoteViews(context.getPackageName(),R.layout.widget_layout);  
        //将按钮与点击事件绑定  
        remoteView.setOnClickPendingIntent(R.id.widget_refreshbutton,getPendingIntent(context, R.id.widget_refreshbutton));  
        remoteView.setOnClickPendingIntent(R.id.widget_switchbutton,getPendingIntent(context, R.id.widget_switchbutton));  

        //ComponentName componentName = new ComponentName(context,MyAppWidgetProvider.class);  
        appWidgetManager.updateAppWidget(appWidgetId, remoteView);  
    } 
	
	private PendingIntent getPendingIntent(Context context, int buttonId) {  
        Intent intent = new Intent(context,MyAppWidgetProvider.class);  
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);  
        intent.setData(Uri.parse("AppWidgetProvider:" + buttonId));  
//        intent.setAction(SWITCH_CLICK);
        PendingIntent pi = PendingIntent.getBroadcast(context, tp++, intent,  PendingIntent.FLAG_UPDATE_CURRENT);  
        return pi;  
    }
	
	private void updateWeather(final Context context,final int index) {
		SharedPreferences prefs = context.getSharedPreferences("main",0);

		String id = prefs.getString(""+index, "");
		String address = "https://api.heweather.com/x3/weather?cityid=" +id + "&key=2b72bcc479834b52adc4462baab69e00";

		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				try {
					JsonUtility.handleWeatherResponse(context, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
				finally
				{
	            	load(context,index);
	        		Log.d("AppWidgetProvider", "finally");

				}
			}

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}

		});
	}
	
	private void load(Context context,int index)
	{
		SharedPreferences prefs = context.getSharedPreferences("main",0);
		String id = prefs.getString(""+index, "");
		SharedPreferences cprefs = context.getSharedPreferences(id,0);

		RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.widget_layout);  
        
        remoteViews.setTextViewText(R.id.widget_city, cprefs.getString("city_name", ""));  
        remoteViews.setTextViewText(R.id.widget_week, "星期"+(cprefs.getString("day0title", "")).substring(1, 2));  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        remoteViews.setTextViewText(R.id.widget_date, sdf.format(new Date()));  

        remoteViews.setTextViewText(R.id.widget_current_description, cprefs.getString("txt", "")+" "+cprefs.getString("tmp", "")+"℃");  
        remoteViews.setTextViewText(R.id.widget_current_wind, cprefs.getString("dir", "")+cprefs.getString("sc", "")+"级");  
        remoteViews.setTextViewText(R.id.widget_publishtime, "发布时间: "+(cprefs.getString("loc", "")).substring(5,(cprefs.getString("loc", "")).length()));  

        String imgname = "hc"+cprefs.getString("0c", "");
		int imgid = context.getResources().getIdentifier(imgname, "drawable", "com.simpleweather.app");
        remoteViews.setImageViewResource(R.id.widget_current_image, imgid);

        remoteViews.setTextViewText(R.id.widget_day0_temp, cprefs.getString("day0temp1", "")+"/"+cprefs.getString("day0temp2", "")+"℃");  
        remoteViews.setTextViewText(R.id.widget_day1_temp, cprefs.getString("day1temp1", "")+"/"+cprefs.getString("day1temp2", "")+"℃");  
        remoteViews.setTextViewText(R.id.widget_day2_temp, cprefs.getString("day2temp1", "")+"/"+cprefs.getString("day2temp2", "")+"℃");  
        remoteViews.setTextViewText(R.id.widget_day3_temp, cprefs.getString("day3temp1", "")+"/"+cprefs.getString("day3temp2", "")+"℃");  

        remoteViews.setTextViewText(R.id.widget_day0_week, "今天");  
        remoteViews.setTextViewText(R.id.widget_day1_week, cprefs.getString("day1title", ""));  
        remoteViews.setTextViewText(R.id.widget_day2_week, cprefs.getString("day2title", ""));  
        remoteViews.setTextViewText(R.id.widget_day3_week, cprefs.getString("day3title", ""));  

        String imgname0 = "mhc"+cprefs.getString("1c", "");
		int imgid0 = context.getResources().getIdentifier(imgname0, "drawable", "com.simpleweather.app");
        remoteViews.setImageViewResource(R.id.widget_day0_image, imgid0);  
        
        String imgname1 = "mhc"+cprefs.getString("2c", "");
		int imgid1 = context.getResources().getIdentifier(imgname1, "drawable", "com.simpleweather.app");
        remoteViews.setImageViewResource(R.id.widget_day1_image, imgid1); 
        
        String imgname2 = "mhc"+cprefs.getString("3c", "");
		int imgid2 = context.getResources().getIdentifier(imgname2, "drawable", "com.simpleweather.app");
        remoteViews.setImageViewResource(R.id.widget_day2_image, imgid2); 
        
        String imgname3 = "mhc"+cprefs.getString("4c", "");
		int imgid3 = context.getResources().getIdentifier(imgname3, "drawable", "com.simpleweather.app");
        remoteViews.setImageViewResource(R.id.widget_day3_image, imgid3); 
        
        AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);  
        ComponentName componentName=new ComponentName(context,MyAppWidgetProvider.class);  
        appWidgetManager.updateAppWidget(componentName, remoteViews);



	}

}
