package com.simpleweather.app.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.simpleweather.app.receiver.AutoUpdateReceiver;
import com.simpleweather.app.util.HttpCallbackListener;
import com.simpleweather.app.util.HttpUtil;
import com.simpleweather.app.util.JsonUtility;

public class AutoUpdateService extends Service {

	public  final int ANHOUR=1 * 60 * 60 * 1000; // 这是1小时的毫秒数
	//public  final int ANHOUR=5 * 1000; // 这是5s的毫秒数
	private AlarmManager manager=null;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				updateWeather();
			}
		}).start();
		manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		//int anHour = 20 * 1000; // 这是20s的毫秒数
		SharedPreferences prefs = AutoUpdateService.this.getSharedPreferences("setting",0);
		int interval = prefs.getInt("interval", 3);

		long triggerAtTime = SystemClock.elapsedRealtime() + interval*ANHOUR;
		Intent i = new Intent(this, AutoUpdateReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 更新天气信息。
	 */
	private void updateWeather() {
		SharedPreferences prefs = AutoUpdateService.this.getSharedPreferences("main",0);

		String id = prefs.getString("1", "");
		String address = "https://api.heweather.com/x3/weather?cityid=" +id + "&key=2b72bcc479834b52adc4462baab69e00";

		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				try {
					JsonUtility.handleWeatherResponse(AutoUpdateService.this, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}

		});
	}

	@Override
	public void onDestroy() {
		stopAlarm();
		super.onDestroy();
		Log.d("SettingTab", "stopServiceSuccess");

	}
	
	public void stopAlarm() {
		manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AutoUpdateReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        if(null != alarmIntent){
        	manager.cancel(alarmIntent);
        }
		Log.d("SettingTab", "stopAlarmSuccess");

	}
	
	
}
