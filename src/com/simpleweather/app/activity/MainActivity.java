package com.simpleweather.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.simpleweather.app.R;
import com.simpleweather.app.model.County;
import com.simpleweather.app.util.SysApplication;

public class MainActivity extends SlidingFragmentActivity {

	private ImageButton mTabBtnWeather;
	private ImageButton mTabBtnSettings;
	//private ImageButton mTabBtnChange;
	private ImageButton mChooseButton;
	private ImageButton mShowButton;
	//private int i = 1;

	private TextView mCityNameTextView;// 放在MainFragment根据滑动取值
	public MainFragment mMainFragment = new MainFragment();
	SlidingMenuFragment mSlidingMenuFragment = new SlidingMenuFragment();

	SettingTab mSettingTabFragment = new SettingTab();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_main);

		mTabBtnWeather = (ImageButton) findViewById(R.id.btn_tab_bottom_weather);
		mTabBtnSettings = (ImageButton) findViewById(R.id.btn_tab_bottom_setting);
		//mTabBtnChange = (ImageButton) findViewById(R.id.btn_tab_bottom_change);

		mChooseButton = (ImageButton) findViewById(R.id.choose_button);
		mShowButton = (ImageButton) findViewById(R.id.show_button);
		mCityNameTextView = (TextView) findViewById(R.id.city_name_textview);
		showSlidingMenu();

		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.main_fragment, mMainFragment)
				.commit();

		mShowButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				toggle();

			}
		});

		mChooseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						ChooseActivity.class);
				intent.putExtra("from_main_activity", true);

				startActivity(intent);
			}
		});

//		mTabBtnChange.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				resetBtn();
//				((ImageButton) mTabBtnChange
//						.findViewById(R.id.btn_tab_bottom_change))
//						.setImageResource(R.drawable.change_background_select);
//
//				int tmp = i % 32;
//				if (tmp==0) {
//					tmp++;
//				}
//
//				String imgname = "y" + tmp;
//				int imgid = getResources().getIdentifier(imgname, "drawable",
//						"com.simpleweather.app");
//				findViewById(R.id.main_activity).setBackgroundResource(imgid);
//				i++;
//
//			}
//		});

		mTabBtnSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resetBtn();
				mCityNameTextView.setVisibility(View.INVISIBLE);
				FragmentManager fm = getSupportFragmentManager();
				((ImageButton) mTabBtnSettings
						.findViewById(R.id.btn_tab_bottom_setting))
						.setImageResource(R.drawable.setting_small_select);
				FragmentTransaction transaction = fm.beginTransaction();
				hideFragments(transaction);
				// if (mSettingTabFragment == null)
				// {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				mSettingTabFragment = new SettingTab();
				transaction.add(R.id.main_fragment, mSettingTabFragment);
				// } else
				// {
				// // 如果MessageFragment不为空，则直接将它显示出来
				// transaction.show(mSettingTabFragment);
				// }
				transaction.commit();
				//
				// }
			}
		});
		//
		mTabBtnWeather.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resetBtn();
				mCityNameTextView.setVisibility(View.VISIBLE);

				FragmentManager fm = getSupportFragmentManager();
				((ImageButton) mTabBtnWeather
						.findViewById(R.id.btn_tab_bottom_weather))
						.setImageResource(R.drawable.weather_small_select);
				FragmentTransaction transaction = fm.beginTransaction();
				hideFragments(transaction);
				// if (mMainFragment == null)
				// {
				// // 如果MessageFragment为空，则创建一个并添加到界面上
				// mMainFragment=new MainFragment();
				// transaction.add(R.id.main_fragment, mMainFragment);
				// } else
				// {
				// // 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(mMainFragment);
				// }
				transaction.commit();
			}
		});

	}

	public void deleteMenuList(int pos) {
		mSlidingMenuFragment.mCountys.remove(pos);
		mSlidingMenuFragment.mAdapter.notifyDataSetChanged();
	}

	public void addMenuList(String name) {
		County mCounty = new County();
		mCounty.setCountyName(name);
		mSlidingMenuFragment.mCountys.add(mCounty);
		mSlidingMenuFragment.mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// single task模式的activity必须重载此方法以得到新的intent
		super.onNewIntent(intent);
		setIntent(intent);
		getIntent().putExtras(intent);
	}

	private void resetBtn() {
//		((ImageButton) mTabBtnChange.findViewById(R.id.btn_tab_bottom_change))
//				.setImageResource(R.drawable.change_background_normal);
		((ImageButton) mTabBtnWeather.findViewById(R.id.btn_tab_bottom_weather))
				.setImageResource(R.drawable.weather_small_normal);
		((ImageButton) mTabBtnSettings
				.findViewById(R.id.btn_tab_bottom_setting))
				.setImageResource(R.drawable.setting_small_normal);
	}

	public void showLeftMenu(View view) {
		getSlidingMenu().showMenu();
	}

	public void toggle() {
		getSlidingMenu().toggle();
	}

	private void showSlidingMenu() {
		setBehindContentView(R.layout.slidingmenu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, mSlidingMenuFragment).commit();
		SlidingMenu menu = getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT);
		// 设置触摸屏幕的模式
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);

		// 设置滑动菜单视图的宽度
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		menu.setFadeDegree(0.35f);
		/**
		 * SLIDING_WINDOW will include the Title/ActionBar in the content
		 * section of the SlidingMenu, while SLIDING_CONTENT does not.
		 */

	}

	public void changeMode() {
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

	}

	public void resetMode() {
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

	}

	@Override
	public void onBackPressed() {
		SysApplication.getInstance().exit();

	}

	private void hideFragments(FragmentTransaction transaction) {
		if (mMainFragment != null) {
			transaction.hide(mMainFragment);
		}
		if (mSettingTabFragment != null) {
			transaction.hide(mSettingTabFragment);
		}
	}

	public void selectItem(int position) {

		mMainFragment.mViewPager.setCurrentItem(position);
	}
	
//	 <LinearLayout
//     android:id="@+id/id_tab_bottom_change"
//     android:layout_width="0dp"
//     android:layout_height="fill_parent"
//     android:layout_weight="1"
//     android:descendantFocusability="beforeDescendants"
//     android:gravity="center"
//     android:orientation="vertical" >
//
//     <ImageButton
//         android:id="@+id/btn_tab_bottom_change"
//         android:layout_width="wrap_content"
//         android:layout_height="wrap_content"
//         android:background="#0000"
//         android:clickable="false"
//         android:src="@drawable/change_background_normal" />
//
//     <TextView
//         android:layout_width="wrap_content"
//         android:layout_height="wrap_content"
//         android:text="背景" 
//         android:textSize="14sp"
//         android:textColor="@android:color/white"
//         />
// </LinearLayout>

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
}
