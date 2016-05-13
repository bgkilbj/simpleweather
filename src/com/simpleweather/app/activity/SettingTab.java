package com.simpleweather.app.activity;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simpleweather.app.R;
import com.simpleweather.app.service.AutoUpdateService;

public class SettingTab extends Fragment {
	private TextView mUpdateTextView;
	private TextView mIntervalTextView;
	private TextView mNoteTextView;
	private ButtonOnClick buttonOnClick = new ButtonOnClick(1);
	private Boolean isAutoUpdate;
	private Boolean isNote;
	private int interval;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.setting_tab, container, false);
		mUpdateTextView = (TextView) v.findViewById(R.id.setting_auto_update);
		mIntervalTextView = (TextView) v.findViewById(R.id.setting_interval);
		mNoteTextView = (TextView) v.findViewById(R.id.setting_notification);
		final SharedPreferences prefs = getActivity().getSharedPreferences(
				"setting", 0);
		isAutoUpdate = prefs.getBoolean("isautoupdate", false);
		isNote = prefs.getBoolean("isnote", false);
		interval = prefs.getInt("interval", 3);

		loadSetting();

		mUpdateTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isAutoUpdate) {
					isAutoUpdate = true;
					SharedPreferences.Editor editor = prefs.edit();
					editor.putBoolean("isautoupdate", isAutoUpdate);
					editor.commit();
					setOn();
					startService();
				} else {
					isAutoUpdate = false;
					SharedPreferences.Editor editor = prefs.edit();
					editor.putBoolean("isautoupdate", isAutoUpdate);
					editor.commit();
					setOff();
					stopService();
				}
			}
		});

		mIntervalTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showAlertDialog();
			}
		});

		mNoteTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isNote) {
					isNote = true;
					SharedPreferences.Editor editor = prefs.edit();
					editor.putBoolean("isnote", isNote);
					editor.commit();
					noteOn();
					sendNotification();
				}
				else {
					isNote = false;
					SharedPreferences.Editor editor = prefs.edit();
					editor.putBoolean("isnote", isNote);
					editor.commit();
					noteOff();
					NotificationManager manager = (NotificationManager)
							getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
							manager.cancel(1);
				}

			}
		});

		return v;

	}

	private void startService() {
		Intent intent = new Intent(getActivity(), AutoUpdateService.class);
	   // intent.addCategory(Intent.CATEGORY_ALTERNATIVE);  

		getActivity().startService(intent);
		Log.d("SettingTab", "startService");
	}

	private void stopService() {
		Intent intent = new Intent(getActivity(), AutoUpdateService.class);
		getActivity().stopService(intent);
		Log.d("SettingTab", "stopService");
	}

	private void loadSetting() {

		if (isAutoUpdate) {
			setOn();
		} else {
			setOff();
		}
		if (isNote) {
			noteOn();

		} else {
			noteOff();
		}

	}

	private void noteOn() {
		Drawable toggleOn = getResources().getDrawable(R.drawable.toggle_on);
		toggleOn.setBounds(0, 0, toggleOn.getMinimumWidth(),
				toggleOn.getMinimumHeight());
		mNoteTextView.setCompoundDrawables(null, null, toggleOn, null);
	}

	private void noteOff() {
		Drawable toggleOff = getResources().getDrawable(R.drawable.toggle_off);
		toggleOff.setBounds(0, 0, toggleOff.getMinimumWidth(),
				toggleOff.getMinimumHeight());
		mNoteTextView.setCompoundDrawables(null, null, toggleOff, null);

	}

	private void setOn() {
		Drawable toggleOn = getResources().getDrawable(R.drawable.toggle_on);
		toggleOn.setBounds(0, 0, toggleOn.getMinimumWidth(),
				toggleOn.getMinimumHeight());
		mUpdateTextView.setCompoundDrawables(null, null, toggleOn, null);
		mIntervalTextView.setClickable(true);
		changeHour(interval);
	}

	private void setOff() {
		Drawable toggleOff = getResources().getDrawable(R.drawable.toggle_off);
		toggleOff.setBounds(0, 0, toggleOff.getMinimumWidth(),
				toggleOff.getMinimumHeight());
		mUpdateTextView.setCompoundDrawables(null, null, toggleOff, null);
		mIntervalTextView.setClickable(false);
		mIntervalTextView.setCompoundDrawables(null, null, null, null);
	}

	private void sendNotification() {
		SharedPreferences mainprefs = getActivity().getSharedPreferences(
				"main", 0);
		String tmpid=mainprefs.getString("1", "");
		
		SharedPreferences cityprefs = getActivity().getSharedPreferences(
				tmpid, 0);
		String cityname=cityprefs.getString("city_name", "");
		String description=cityprefs.getString("txt", "");
		String temp=cityprefs.getString("tmp", "")+"°";
		String wind=cityprefs.getString("dir", "")+":"+cityprefs.getString("sc", "")+"级";
		
		NotificationManager manager = (NotificationManager) getActivity()
				.getSystemService(Context.NOTIFICATION_SERVICE);
//		Notification notification = new Notification(R.drawable.ic_launcher,
//				"Simple Weather", System.currentTimeMillis());
//		notification.setLatestEventInfo(getActivity(), cityname+"天气",
//				description+" 温度:"+temp+" "+wind
//				, null);
//		manager.notify(1, notification);
		
		 PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0,  
                 new Intent(getActivity(),MainActivity.class)  
                 , PendingIntent.FLAG_UPDATE_CURRENT); 
         Builder builder = new Notification.Builder(getActivity());  
         builder.setContentIntent(contentIntent)  
                 .setSmallIcon(R.drawable.ic_launcher)  
                 .setWhen(System.currentTimeMillis()).setAutoCancel(false)  
                 .setContentTitle(cityname+"天气").setContentText(
         				description+" 温度:"+temp+" "+wind);  
         manager.notify(1, builder.build());
		
		
		
	}

	private void changeHour(int interval) {
		int tmp = R.drawable.hour3;
		switch (interval) {
		case 1:
			tmp = R.drawable.hour1;
			break;
		case 2:
			tmp = R.drawable.hour2;
			break;
		case 3:
			tmp = R.drawable.hour3;
			break;
		case 4:
			tmp = R.drawable.hour4;
			break;

		}
		Drawable hour = getResources().getDrawable(tmp);
		hour.setBounds(0, 0, hour.getMinimumWidth(), hour.getMinimumHeight());
		mIntervalTextView.setCompoundDrawables(null, null, hour, null);
	}

	private void saveInterval(int tmp) {
		SharedPreferences sprefs = getActivity().getSharedPreferences(
				"setting", 0);
		SharedPreferences.Editor editor = sprefs.edit();
		editor.putInt("interval", tmp);
		editor.commit();
		interval = tmp;
	}

	private void showAlertDialog() {
		new AlertDialog.Builder(getActivity())
				.setTitle("更新间隔")

				.setSingleChoiceItems(
						new String[] { "1小时", "2小时", "3小时", "4小时" }, 0,
						buttonOnClick).setNegativeButton("取消", buttonOnClick)
				.show();
	}

	private class ButtonOnClick implements DialogInterface.OnClickListener {
		private int index; // 表示选项的索引

		public ButtonOnClick(int index) {
			this.index = index;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// which表示单击的按钮索引，所有的选项索引都是大于0，按钮索引都是小于0的。
			if (which >= 0) {

				index = which + 1;
				changeHour(index);
				saveInterval(index);
				stopService();

				startService();

				dialog.dismiss();
			} else {
				dialog.dismiss();

			}
		}
	}

}
