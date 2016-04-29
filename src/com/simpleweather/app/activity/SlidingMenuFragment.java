package com.simpleweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.simpleweather.app.R;
import com.simpleweather.app.model.County;
import com.simpleweather.app.util.SlidingMenuListAdapter;

public class SlidingMenuFragment extends Fragment{

	private View mView;  
    private ListView mListView;  
    public List<County> mCountys= new ArrayList<County>();;  
    public SlidingMenuListAdapter mAdapter;  
 
    


	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {  
        mView = inflater.inflate(R.layout.slidingmenu,null);  
        initView(mView);
        
        return mView;  
    }  
  
    private void initView(View mView)  
    {  
        mListView = (ListView) mView.findViewById(R.id.exist_list);  

        mAdapter = new SlidingMenuListAdapter(getActivity(),R.layout.slidingmenu_item,mCountys);

        mListView.setAdapter(mAdapter);  
        mListView.setOnItemClickListener(new OnItemClickListener() {
        	@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

        		if (getActivity() instanceof MainActivity) {  
                    MainActivity tmp = (MainActivity) getActivity();  
                    tmp.selectItem(position);  
                    ((MainActivity)getActivity()).toggle();
                }  
 
			}
		});
        
    }  

	
}  
