package com.simpleweather.app.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.simpleweather.app.R;
import com.simpleweather.app.db.DataBaseOperator;
import com.simpleweather.app.model.County;
import com.simpleweather.app.util.CountyListAdapter;
import com.simpleweather.app.util.SysApplication;

public class ChooseActivity extends Activity {

	private EditText mEditText;
	private ListView mListView;
	//private Button mSearchButton;
	private CountyListAdapter mcountyListAdapter;
	private List<County> testArray = new ArrayList<County>();
	
	private DataBaseOperator mDataBaseOperator;
	private ProgressDialog progressDialog;

	public static final String NAME="name";
	public static final String ID="id";
	private boolean isFromMainActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

        SysApplication.getInstance().addActivity(this);

		isFromMainActivity = getIntent().getBooleanExtra(
				"from_main_activity", false);
		SharedPreferences prefs = getSharedPreferences("main", 0);

		// �Ѿ�ѡ���˳����Ҳ��Ǵ�MainActivity��ת�������Ż�ֱ����ת��MainActivity
		if ((prefs.getInt("count", 0) != 0) && !isFromMainActivity) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
			return;
		}	
	
		
		
		setContentView(R.layout.choose_county);
		mDataBaseOperator=DataBaseOperator.getInstance(this);
		initData(mDataBaseOperator);

		initView();

	}
	

	
	
	private void initData(DataBaseOperator mDataBaseOperator)
	{ 
  
		                 
		    if (mDataBaseOperator.isExist()) {// ����������Ϊ�գ���ִ�в��롣  
		        try {  
		        	//showProgressDialog();
		        	mDataBaseOperator.initInsert(this);// �����ʼ������  
		        	//closeProgressDialog();
		        } catch (IOException e) {  
		            e.printStackTrace();  
		        }  
		    }  
//		ContentValues values = new ContentValues();
//		values.put("id", "CN101220605");
//		values.put("ename", "huaining");
//		values.put("county_name", "����");
//		values.put("city_name", "����");
//		values.put("province_name", "����");
//		mDataBaseOperator.insert(values);
//		values.clear();
//		values.put("id", "CN101220604");
//		values.put("ename", "qianshan");
//		values.put("county_name", "Ǳɽ");
//		values.put("city_name", "����");
//		values.put("province_name", "����");
//		mDataBaseOperator.insert(values);
//		values.clear();
//		values.put("id", "CN101220603");
//		values.put("ename", "taihu");
//		values.put("county_name", "̫��");
//		values.put("city_name", "����");
//		values.put("province_name", "����");
//		mDataBaseOperator.insert(values);

	}
	
	private void initView() {

		mEditText=(EditText)findViewById(R.id.input_edit);
		mListView=(ListView)findViewById(R.id.county_list);
		//mSearchButton=(Button)findViewById(R.id.search_button);

		mcountyListAdapter = new CountyListAdapter(this,R.layout.county_item,testArray);
		mListView.setAdapter(mcountyListAdapter);// ����Adapter����ʼֵΪ��

		mListView.setOnItemClickListener(new OnItemClickListener() {// listView����¼�

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						County mcounty=mcountyListAdapter.getItem(position);
						mEditText.setText(mcounty.getCountyName()+" , "+mcounty.getCityName()+" , "+mcounty.getProvinceName());

						mListView.setVisibility(View.GONE);
						Intent i=new Intent(ChooseActivity.this,MainActivity.class);
						i.putExtra(ID, mcounty.getId());
						i.putExtra(NAME, mcounty.getCountyName());
						startActivity(i);
					}
				});

		mEditText.addTextChangedListener(new TextWatcher() {// EditText�仯����

			/**
			 * ��������
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				testArray = new ArrayList<County>();// ÿ�������ʱ�����³�ʼ�������б�

				if (!TextUtils.isEmpty(mEditText.getText().toString())) {// �ж����������Ƿ�Ϊ�գ�Ϊ��������

					testArray = mDataBaseOperator.queryInfo((mEditText.getText()).toString());
				}

				mcountyListAdapter.refreshData(testArray);// Adapterˢ������
				mListView.setVisibility(View.VISIBLE);
			}

			/**
			 * ����֮ǰ
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			/**
			 * ����֮��
			 */
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		
//		mSearchButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				try {
//					getcountyList(mDataBaseOperator);
//					mcountyListAdapter.notifyDataSetChanged();
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			
//		});
	}
	
//	/**
//	 * ��ʾ���ȶԻ���
//	 */
//	private void showProgressDialog() {
//		if (progressDialog == null) {
//			progressDialog = new ProgressDialog(this);
//			progressDialog.setMessage("���ڼ���...");
//			progressDialog.setCanceledOnTouchOutside(false);
//		}
//		progressDialog.show();
//	}
//
//	/**
//	 * �رս��ȶԻ���
//	 */
//	private void closeProgressDialog() {
//		if (progressDialog != null) {
//			progressDialog.dismiss();
//		}
//	}
	
	@Override
	public void onBackPressed()
	{

		SharedPreferences prefs = getSharedPreferences("main", 0);
		if(prefs.getInt("count", 0) == 0)
		{
			SysApplication.getInstance().exit();
		}
		else {
			super.onBackPressed();
		}

	}



//	public void getcountyList(DataBaseOperator mDataBaseOperator) throws UnsupportedEncodingException
//	{   
//
//        String sql = "SELECT * FROM county ";  
//
//		//String sql = "SELECT DISTINCT(province_name) province_name FROM county ";  
//        Cursor cursor = mDataBaseOperator.rawQuery(sql);  
//          
//          
//       if (cursor.getCount() != 0) {// ���������  
//           while (cursor.moveToNext()) {  
//               County county = new County();  
//                
//               county.setId(cursor.getString(0));  
//               county.setCountyName(cursor.getString(2));
//               
//               testArray.add(county);  
//           }  
//               cursor.close();  
//             //  sqliteDatabase.close();//�ر����ݿ�  
//            
//       } else {  
//    	   testArray = null;  
//       }  
//        //return testArray;  
//}  

}
