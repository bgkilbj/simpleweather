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
	private List<ImageView> mDots;//�ײ�СԲ��ļ���
	private LinearLayout mLinearLayout;
	private TextView mCityNameTextView;//MainActivty��TextView

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
                //�����е�Բ������ΪΪѡ��ʱ���ͼƬ
                mDots.get(i).setImageResource(R.drawable.dot_normal);
            }
            //����ѡ�е�ͼƬ�е�Բ������Ϊ��ѡ�е�ʱ���ͼƬ
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
	


	public void judge()//1.��������,onCreate�е���;2.֮������,onResume�е���;3.֮����룬onCreate�е���
	{
		SharedPreferences prefs = getActivity().getSharedPreferences("main", 0);
		SharedPreferences.Editor editor = prefs.edit();
		Intent intent=getActivity().getIntent();
		String name=intent.getStringExtra(ChooseActivity.NAME);
		String id=intent.getStringExtra(ChooseActivity.ID);
    	mCityNameTextView=(TextView)getActivity().findViewById(R.id.city_name_textview);


		if(name!=null)//�������У�1��2
		{		
			//�жϳ����Ƿ������е��ظ�
			if(mFragments.size()==0)//���1
			{
				int count=prefs.getInt("count", 0);
				editor.putInt("count", ++count);
				editor.putString(String.valueOf(count), id);
				editor.putString("0"+String.valueOf(count), name);
				editor.commit();
				addFragment(name, id);//����
	        	mCityNameTextView.setText(name);
				((MainActivity)getActivity()).changeMode();
				((MainActivity)getActivity()).addMenuList(name);
			}
			//���2
			else {
				for(int i=0;i<mFragments.size();i++)
				{	
					if(name.equals(mFragments.get(i).name))
					{
						mViewPager.setCurrentItem(i);//��ת��ҳ��
						break;
					}
					if(i==mFragments.size()-1)
					{
						int count=prefs.getInt("count", 0);
						editor.putInt("count", ++count);
						editor.putString(String.valueOf(count), id);
						editor.putString("0"+String.valueOf(count), name);
						editor.commit();

						addFragment(name, id);//����
						((MainActivity)getActivity()).addMenuList(name);
					}
				}
			}
		}
		//���3,��ȡsharedpreference main�ļ�,���д洢������������ID
		else{

			int count=prefs.getInt("count", 0);//���г�����
			if(count!=mFragments.size())//��ֹ�����˵���̨���½���ʱ�ٴζ�ȡ
			{
			for(int i=1;i<(count+1);i++)
			{
				id=prefs.getString(String.valueOf(i), "");//����ID
				name=prefs.getString("0"+String.valueOf(i), "");//����ID
				addFragment(name, id);//����			
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
		mFragments.remove(pos);//ɾ��fragment

		if(mFragments.size()!=0)
		{
			mAdapter.notifyDataSetChanged();
			initDots();
			mViewPager.setCurrentItem(0);//��ת��һҳ
			mCityNameTextView.setText(mFragments.get(0).name);
    	}

		((MainActivity)getActivity()).deleteMenuList(pos);//����menulist
		((MainActivity)getActivity()).toggle();//����menu
		
		//����sharedpreference��main��count����ϸ,pos֮���ǰ��һλ		
		SharedPreferences prefs = getActivity().getSharedPreferences("main", 0);
		SharedPreferences.Editor editor = prefs.edit();
		int count=prefs.getInt("count", 0);
		editor.putInt("count", count-1);
		String tmpid=prefs.getString(String.valueOf(pos+1), "");//��ɾ����xml�ļ���
		for (int i = (pos+2); i < (count+1); i++) {
			String tmp=prefs.getString(String.valueOf(i), "");
			String tmp2=prefs.getString("0"+String.valueOf(i), "");

			editor.putString(String.valueOf(i-1), tmp);//id
			editor.putString("0"+String.valueOf(i-1), tmp2);//name	
		}
		editor.commit();
		//ɾ�����1λ�ظ�
		SharedPreferences.Editor editor2 = getActivity().getSharedPreferences("main", 0).edit();
		editor2.remove(String.valueOf(count));
		editor2.remove("0"+String.valueOf(count));
		editor2.commit();

		//ɾ��id��Ӧ��xml
		File file= new File("/data/data/"+getActivity().getPackageName().toString()+"/shared_prefs",tmpid+".xml");
		if(file.exists()){
		file.delete();
		}
		
		//û�г��еĻ���תChooseActivity
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
		mDots = new ArrayList<ImageView>();// �ײ�Բ�㼯�ϵĳ�ʼ��
		mLinearLayout.removeAllViews();
		for (int i = 0; i < mFragments.size(); i++) {// ���ݽ���������̬���Բ��
			ImageView imageView = new ImageView(getActivity());
			imageView.setLayoutParams(new ViewGroup.LayoutParams(35, 35));// ����ImageView�Ŀ�Ⱥ͸߶�
			imageView.setPadding(0, 0, 0, 0);// ����Բ���Padding������Χ�ľ���
			imageView.setImageResource(R.drawable.dot_normal);// ����ͼƬ
			mDots.add(imageView);// ����ͼƬ��ӵ�Բ�㼯����
			mLinearLayout.addView(imageView);// ��ͼƬ��ӵ�LinearLayout��
			mLinearLayout.setBackgroundColor(Color.parseColor("#40000000"));
		}
		mDots.get(0).setImageResource(R.drawable.dot_selected);//dot_selected1
	}
	
}
