package com.simpleweather.app.activity;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simpleweather.app.R;
import com.simpleweather.app.model.RecyclerViewItem;
import com.simpleweather.app.util.DividerGridItemDecoration;
import com.simpleweather.app.util.HttpCallbackListener;
import com.simpleweather.app.util.HttpUtil;
import com.simpleweather.app.util.JsonUtility;
import com.simpleweather.app.util.RecyclerViewAdapter;

public class MainTab extends Fragment
{

	public String name="";
	public String id="";
	private RecyclerViewAdapter adapter ;
	private RecyclerView recyclerView;
    private ArrayList<RecyclerViewItem> items = new ArrayList<RecyclerViewItem>();
	private SwipeRefreshLayout mSwipeRefreshWidget;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		items.clear();
		View v=inflater.inflate(R.layout.main_tab, container, false);
		mSwipeRefreshWidget = (SwipeRefreshLayout)v. findViewById(R.id.main_tab);

		mSwipeRefreshWidget.setSize(SwipeRefreshLayout.LARGE);
		mSwipeRefreshWidget.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
            	queryFromServer(id);
            	mSwipeRefreshWidget.setRefreshing(false);

            }});
		
		initDatas();//��ʼ��items����
        initViews(v);//��ʼ��recyclerview
		SharedPreferences prefs = getActivity().getSharedPreferences(id,0);
		SharedPreferences.Editor editor = prefs.edit();

		if (prefs.getBoolean("isfirst", true)) {
			items.get(0).setTemp2("ͬ����...");
			queryFromServer(id);
			editor.putBoolean("isfirst", false);
			editor.commit();

		}
		else {
			showWeather();
			adapter.notifyDataSetChanged();

		}
        
//		if (getArguments() != null)
// 
//		{
//			name = getArguments().getString(ChooseActivity.NAME);
//			id = getArguments().getString(ChooseActivity.ID);
//			//����header�޸�����
//	        items.get(0).setTemp2("ͬ����...");
//			queryFromServer(id);
//			//adapter.notifyDataSetChanged();
//
//		}

		return  v;
	
	}
	
	
	/**
	 * ���ݴ���ĵ�ַ������ȥ���������ѯ�������Ż���������Ϣ��
	 */
	private void queryFromServer(String id) {

		String address = "https://api.heweather.com/x3/weather?cityid=" +id + "&key=2b72bcc479834b52adc4462baab69e00";
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
	
			@Override
	
			public void onFinish(final String response) {

				// ������������ص�������Ϣ
				try {
					JsonUtility.handleWeatherResponse(getActivity(),response);

				} catch (Exception e) {
					e.printStackTrace();
				}
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showWeather();
						adapter.notifyDataSetChanged();

					}
				});

			}

			@Override
			public void onError(Exception e) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
				        items.get(0).setTemp2("ͬ��ʧ��...");
					}
				});
			}
		});
	}
	/**
	 * ��SharedPreferences�ļ��ж�ȡ�洢��������Ϣ������ʾ�������ϡ�
	 */
	private void showWeather() {
		SharedPreferences prefs = getActivity().getSharedPreferences(id,0);

		//name=prefs.getString("city_name", "");
		//header����
		String str=prefs.getString("loc", "");
		items.get(0).setTemp2("����ʱ��: "+str.substring(5,str.length()));//����ʱ��
		items.get(0).setDescription(prefs.getString("txt", ""));//�������
		if((prefs.getString("txt", "")).equals("����"))//���������ı�mainactivity�ı���
		{
			(getActivity().findViewById(R.id.main_activity)).setBackgroundResource(R.drawable.rain_night);
			
		}
		items.get(0).setTitle(prefs.getString("tmp", "")+"��");//�¶�
		items.get(0).setTemp1(prefs.getString("dir", "")+":"+prefs.getString("sc", "")+"��");//���򼰷���

		//middler��������
		items.get(1).setTemp2(prefs.getString("day0temp2", "")+"��");//����¶�
		items.get(1).setDescription(prefs.getString("day0description", ""));//�������
		items.get(1).setTitle("����");//���ڣ���Ϊ����
		items.get(1).setTemp1(prefs.getString("day0temp1", "")+"/");//����¶�

		//middler��������
		items.get(2).setTemp2(prefs.getString("day1temp2", "")+"��");//����¶�
		items.get(2).setDescription(prefs.getString("day1description", ""));//�������
		items.get(2).setTitle(prefs.getString("day1title", ""));//���ڣ���Ϊ�ܼ�
		items.get(2).setTemp1(prefs.getString("day1temp1", "")+"/");//����¶�

		//middler��������
		items.get(3).setTemp2(prefs.getString("day2temp2", "")+"��");//����¶�
		items.get(3).setDescription(prefs.getString("day2description", ""));//�������
		items.get(3).setTitle(prefs.getString("day2title", ""));//���ڣ���Ϊ�ܼ�
		items.get(3).setTemp1(prefs.getString("day2temp1", "")+"/");//����¶�
	
		//middler���������
		items.get(4).setTemp2(prefs.getString("day3temp2", "")+"��");//����¶�
		items.get(4).setDescription(prefs.getString("day3description", ""));//�������
		items.get(4).setTitle(prefs.getString("day3title", ""));//���ڣ���Ϊ�ܼ�
		items.get(4).setTemp1(prefs.getString("day3temp1", "")+"/");//����¶�
	
		//item����ָ��
		items.get(5).setDescription(prefs.getString("drsgbrf", ""));
		//itemϴ��ָ��
		items.get(6).setDescription(prefs.getString("cwbrf", ""));
		//item�˶�ָ��
		items.get(7).setDescription(prefs.getString("sportbrf", ""));
		//item������ָ��
		items.get(8).setDescription(prefs.getString("uvbrf", ""));		
		//item����ָ��
		items.get(9).setDescription(prefs.getString("travbrf", ""));
		//item��ðָ��
		items.get(10).setDescription(prefs.getString("flubrf", ""));	
		String[] weathercode=new String[5];
		weathercode[0]=prefs.getString("0c", "");
		weathercode[1]=prefs.getString("1c", "");
		weathercode[2]=prefs.getString("2c", "");
		weathercode[3]=prefs.getString("3c", "");
		weathercode[4]=prefs.getString("4c", "");

		
		changeImg(weathercode);
	}
	
	 private void initDatas() {

		    RecyclerViewItem headerItem = new RecyclerViewItem();
		    headerItem.setType(0);
		    RecyclerViewItem middlerItem1 = new RecyclerViewItem();
		    middlerItem1.setTitle("����");

		    RecyclerViewItem middlerItem2 = new RecyclerViewItem();
		    RecyclerViewItem middlerItem3 = new RecyclerViewItem();
		    RecyclerViewItem middlerItem4 = new RecyclerViewItem();
		    middlerItem1.setType(1);
		    middlerItem2.setType(1);
		    middlerItem3.setType(1);
		    middlerItem4.setType(1);
		    middlerItem1.setImg(R.drawable.weather_small_normal);
		    middlerItem2.setImg(R.drawable.weather_small_normal);
		    middlerItem3.setImg(R.drawable.weather_small_normal);
		    middlerItem4.setImg(R.drawable.weather_small_normal);


		    items.add(headerItem);
		    items.add(middlerItem1);
		    items.add(middlerItem2);
		    items.add(middlerItem3);
		    items.add(middlerItem4);

		 
		    RecyclerViewItem item = new RecyclerViewItem();
	        item.setImg(R.drawable.drsg_white);
	        item.setTitle("����");
	        item.setType(2);
	        items.add(item);

	        RecyclerViewItem item2 = new RecyclerViewItem();
	        item2.setImg(R.drawable.cw_white);
	        item2.setTitle("ϴ��");
	        item2.setType(2);

	        items.add(item2);

	        RecyclerViewItem item3 = new RecyclerViewItem();
	        item3.setImg(R.drawable.sport_white);
	        item3.setTitle("�˶�");
	        item3.setType(2);

	        items.add(item3);

	        RecyclerViewItem item4 = new RecyclerViewItem();
	        item4.setImg(R.drawable.ug_white);
	        item4.setTitle("������");
	        item4.setType(2);

	        items.add(item4);
	        
	        RecyclerViewItem item5 = new RecyclerViewItem();
	        item5.setImg(R.drawable.trav_white);
	        item5.setTitle("����");
	        item5.setType(2);

	        items.add(item5);

	        RecyclerViewItem item6 = new RecyclerViewItem();
	        item6.setImg(R.drawable.flu_white);
	        item6.setTitle("��ð");
	        item6.setType(2);

	        items.add(item6);

	    }
	 
	 private void changeImg(String[] weathercode)
	 {
		 for(int i=0;i<5;i++)
		 {
			 String imgname = "c"+weathercode[i];
			 int imgid = getResources().getIdentifier(imgname, "drawable", "com.simpleweather.app");
			 items.get(i).setImg(imgid);
		 }
			 
	 }
	 
	 private void initViews(View v) {

	        recyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
	        //Ĭ�϶���Ч��
	        recyclerView.setItemAnimator(new DefaultItemAnimator());
	        //���ò��ֹ�����������������Ϊ�Ƿ����򲼾�
	        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));
	        //����ÿһ���װ�Σ������������ָ���
	        recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));
	        //�������Ч��	         
	        recyclerView.setHasFixedSize(true);
	        //����������
	        adapter= new RecyclerViewAdapter(items);
	        recyclerView.setAdapter(adapter);


	    }


}
