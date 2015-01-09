package com.china.acetech.ToolPackage.funccontext;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

public abstract class AbsFragment extends Fragment {

	abstract protected View getRootView();
	abstract public boolean onBackPressed();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =  super.onCreateView(inflater, container, savedInstanceState);
		if ( view == null )
			view = getRootView();
		
		return view;
	}
	
	protected final View findViewById(int viewId){
		if ( getRootView() == null )
			return null;
		return getRootView().findViewById(viewId);
	}
	@Override
	public void onPause() {
		super.onPause();
        InputMethodManager imm =(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if ( getRootView() != null )
        	imm.hideSoftInputFromWindow(getRootView().getWindowToken(),0);
	}
	
	
}
