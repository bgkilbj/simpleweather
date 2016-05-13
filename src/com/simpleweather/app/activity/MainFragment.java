package com.simpleweather.app.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simpleweather.app.R;

public class MainFragment extends Fragment{

	public List<MainTab> mFragments= new ArrayList<MainTab>();
	public ViewPager mViewPager;
	private FragmentStatePagerAdapter mAdapter;
	private List<ImageView> mDots;//底部小圆点的集合
	private LinearLayout mLinearLayout;
	private TextView mCityNameTextView;//MainActivty的TextView

	//private SlidingMenu menu;
	
	@Override
	public void onResume() {
		super.onResume();
		judge();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//mFragments.clear();
		View v=inflater.inflate(R.layout.main_fragment, container, false);
		mViewPager=(ViewPager)v.findViewById(R.id.id_viewpager);
        mLinearLayout= (LinearLayout)v.findViewById(R.id.viewpager_linerlayout);

		mAdapter = new FragmentStatePagerAdapter(getChildFragmentManager())
		{

			@Override
			public int getCount()
			{
				return mFragments.size();
			}

			
			@Override
			public int getItemPosition(Object object) {
				return  POSITION_NONE;
			}


			@Override
			public Fragment getItem(int arg0)
			{
				return mFragments.get(arg0);
			}
			
			@Override
		    public void destroyItem(View collection, int position, Object o) {
		        View view = (View)o;
		        ((ViewPager) collection).removeView(view);
		        view = null;
		    }


			@Override
			public void notifyDataSetChanged() {
				super.notifyDataSetChanged();
				
			}
			
		};
		mViewPager.setAdapter(mAdapter);
		

		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
		
		
		@Override
		public void onPageSelected(int arg0) {
			
			if (arg0==0) {
				((MainActivity)getActivity()).changeMode();

			}
			else{
				((MainActivity)getActivity()).resetMode();

			}
			
			for (int i=0;i<mFragments.size();i++){
                //将所有的圆点设置为为选中时候的图片
                mDots.get(i).setImageResource(R.drawable.dot_normal);
            }
            //将被选中的图片中的圆点设置为被选中的时候的图片
            mDots.get(arg0).setImageResource(R.drawable.dot_selected);
        	mCityNameTextView=(TextView)getActivity().findViewById(R.id.city_name_textview);
        	mCityNameTextView.setText(mFragments.get(arg0).name);


		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}
	});
		return v;	
	}
	


	public void judge()//1.初次新增,onCreate中调用;2.之后新增,onResume中调用;3.之后进入，onCreate中调用
	{
		SharedPreferences prefs = getActivity().getSharedPreferences("main", 0);
		SharedPreferences.Editor editor = prefs.edit();
		Intent intent=getActivity().getIntent();
		String name=intent.getStringExtra(ChooseActivity.NAME);
		String id=intent.getStringExtra(ChooseActivity.ID);
    	mCityNameTextView=(TextView)getActivity().findViewById(R.id.city_name_textview);


		if(name!=null)//新增城市，1和2
		{		
			//判断城市是否与现有的重复
			if(mFragments.size()==0)//情况1
			{
				int count=prefs.getInt("count", 0);
				editor.putInt("count", ++count);
				editor.putString(String.valueOf(count), id);
				editor.putString("0"+String.valueOf(count), name);
				editor.commit();
				addFragment(name, id);//新增
	        	mCityNameTextView.setText(name);
				((MainActivity)getActivity()).changeMode();
				((MainActivity)getActivity()).addMenuList(name);
			}
			//情况2
			else {
				for(int i=0;i<mFragments.size();i++)
				{	
					if(name.equals(mFragments.get(i).name))
					{
						mViewPager.setCurrentItem(i);//跳转该页面
						break;
					}
					if(i==mFragments.size()-1)
					{
						int count=prefs.getInt("count", 0);
						editor.putInt("count", ++count);
						editor.putString(String.valueOf(count), id);
						editor.putString("0"+String.valueOf(count), name);
						editor.commit();

						addFragment(name, id);//新增
						((MainActivity)getActivity()).addMenuList(name);
					}
				}
			}
		}
		//情况3,读取sharedpreference main文件,其中存储城市数及城市ID
		else{

			int count=prefs.getInt("count", 0);//已有城市数
			if(count!=mFragments.size())//防止程序退到后台重新进入时再次读取
			{
			for(int i=1;i<(count+1);i++)
			{
				id=prefs.getString(String.valueOf(i), "");//城市ID
				name=prefs.getString("0"+String.valueOf(i), "");//城市ID
				addFragment(name, id);//新增			
				if(i==1)
				{
					mCityNameTextView.setText(name);
					((MainActivity)getActivity()).changeMode();
				}
				((MainActivity)getActivity()).addMenuList(name);

			}
			
		}}
	}
	
	public void removeFragment(int pos)
	{
		mFragments.remove(pos);//删除fragment

		if(mFragments.size()!=0)
		{
			mAdapter.notifyDataSetChanged();
			initDots();
			mViewPager.setCurrentItem(0);//跳转第一页
			mCityNameTextView.setText(mFragments.get(0).name);
    	}

		((MainActivity)getActivity()).deleteMenuList(pos);//更新menulist
		((MainActivity)getActivity()).toggle();//收起menu
		
		//更新sharedpreference中main的count和详细,pos之后的前移一位		
		SharedPreferences prefs = getActivity().getSharedPreferences("main", 0);
		SharedPreferences.Editor editor = prefs.edit();
		int count=prefs.getInt("count", 0);
		editor.putInt("count", count-1);
		String tmpid=prefs.getString(String.valueOf(pos+1), "");//待删除的xml文件名
		for (int i = (pos+2); i < (count+1); i++) {
			String tmp=prefs.getString(String.valueOf(i), "");
			String tmp2=prefs.getString("0"+String.valueOf(i), "");

			editor.putString(String.valueOf(i-1), tmp);//id
			editor.putString("0"+String.valueOf(i-1), tmp2);//name	
		}
		editor.commit();
		//删除最后1位重复
		SharedPreferences.Editor editor2 = getActivity().getSharedPreferences("main", 0).edit();
		editor2.remove(String.valueOf(count));
		editor2.remove("0"+String.valueOf(count));
		editor2.commit();

		//删除id对应的xml
		File file= new File("/data/data/"+getActivity().getPackageName().toString()+"/shared_prefs",tmpid+".xml");
		if(file.exists()){
		file.delete();
		}
		
		//没有城市的话跳转ChooseActivity
		if (mFragments.size()==0) {
			Intent intent=new Intent(getActivity(),ChooseActivity.class);
			startActivity(intent);
			mAdapter.notifyDataSetChanged();

		}
	}
	
	public void addFragment(String name,String id)
	{
		MainTab tab = new MainTab();
		tab.name=name;
		tab.id=id;

//		Bundle bundle=new Bundle();
//		bundle.putString(ChooseActivity.NAME, name);
//		bundle.putString(ChooseActivity.ID, id);
//		tab.setArguments(bundle);
		//http://www.itstrike.cn/Question/12234ede-37fe-4a72-9341-b2a27c05f80d.html
		mFragments.add(tab);
		initDots();
		mAdapter.notifyDataSetChanged();

	}
	private void initDots() {
		mDots = new ArrayList<ImageView>();// 底部圆点集合的初始化
		mLinearLayout.removeAllViews();
		for (int i = 0; i < mFragments.size(); i++) {// 根据界面数量动态添加圆点
			ImageView imageView = new ImageView(getActivity());
			imageView.setLayoutParams(new ViewGroup.LayoutParams(35, 35));// 设置ImageView的宽度和高度
			imageView.setPadding(0, 0, 0, 0);// 设置圆点的Padding，与周围的距离
			imageView.setImageResource(R.drawable.dot_normal);// 设置图片
			mDots.add(imageView);// 将该图片添加到圆点集合中
			mLinearLayout.addView(imageView);// 将图片添加到LinearLayout中
			mLinearLayout.setBackgroundColor(Color.parseColor("#40000000"));
		}
		mDots.get(0).setImageResource(R.drawable.dot_selected);//dot_selected1
	}
	
}
