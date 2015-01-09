package com.china.acetech.ToolPackage.funccontext.simple;

import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.china.acetech.ToolPackage.ble.services.BLEServiceManagerWorker;
import com.china.acetech.ToolPackage.funccontext.AbsFragment;

/**
 * 主界面的Fragment的抽象對象。<br/><br/>
 * 数据层。存放类的成员，以及成员函数。尽量保证所有的方法都是private或者protected类型<br/>
 *
 * 此目錄下為主界面的主體架構。具體描述如下：<br/>
 * {@link MainActivity}會實例化{@link MainInterfaceFragment_}。<br/>
 * 運動數據list整體是一個ViewPager({@code mPager})。Adapter使用的是{@link AllInfoPagerAdapter}。 
 * Adapter負責創建具體某一天的數據界面。也就是{@link SportInfoPagerFragment_}。<br/>
 * 運動數據會顯示在這個Fragment中的List,數據來源於{@link SportInfoListAdapter}。<br/>
 * 
 * @author bxc2010011
 *
 */
public abstract class MainInterfaceFragment extends AbsFragment{

	ViewPager mPager;
	
	ImageView	mHeadport;
	TextView	mName;
	TextView	mBirthday;
	TextView	mHeight;
	TextView	mWeight;
	
	TextView	mDate;
	
	ImageView	mDeviceState;
	
	
	BLEServiceManagerWorker mBLEManager;

	
	private void setViewPagerAdapter(){
		
//		AllInfoPagerAdapter adapter = new AllInfoPagerAdapter(this.getChildFragmentManager(), mDate);
//
//		mPager.setOnPageChangeListener(adapter);
//		mPager.setAdapter(adapter);
//		mPager.setCurrentItem(adapter.getCount()-1);
//		adapter.setReadFlag();//readFlag最開始是false，是為了防止在setAdapter時viewPager預讀第一個page。
	}

}
