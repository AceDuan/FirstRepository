package com.china.acetech.ToolPackage.funccontext.simple;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.china.acetech.ToolPackage.R;
import com.china.acetech.ToolPackage.funccontext.AbsFragment;


/**
 * Android 中 ContextMenu功能的实例
 */
public abstract class ContextMenuSimpleFragment extends AbsFragment {

	protected ListView mInfoList;	
	protected BaseAdapter mAdapter;
	protected Calendar mDate;
	
	
	private static final int EDIT_GOAL		= 1;
	private static final int MOVE_UP		= 2;
	private static final int MOVE_DOWN		= 3;
	private static final int HIDE			= 4;
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		AdapterView.AdapterContextMenuInfo realMenuInfo = (AdapterView.AdapterContextMenuInfo)menuInfo;
		//int titleID = MainInterfaceSetting.getInstance().getSportInfoList().get(realMenuInfo.position).getContextMenuTitleStringID();
		//menu.setHeaderTitle(titleID);
		//menu.add(0, EDIT_GOAL, Menu.NONE, "");
		if ( realMenuInfo.position != 0 )
			menu.add(0, MOVE_UP, Menu.NONE, R.string.context_menu_move_up);
		
		if ( realMenuInfo.position != mAdapter.getCount()-1 )
			menu.add(0, MOVE_DOWN, Menu.NONE, R.string.context_menu_move_down);
		
		long showList = 1;//MySavedState.LocalSaveInfo.getShowingList();
		if (showList >= 10 )
			menu.add(0, HIDE, Menu.NONE, R.string.context_menu_move_Hide);
	}

	


	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int position = ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
		switch (item.getItemId() ){
		case EDIT_GOAL:
			break;
		case MOVE_UP:
			break;
		case MOVE_DOWN:
			break;
		case HIDE:
			if ( mAdapter.getCount() == 1 ){
				new AlertDialog.Builder(getActivity() )
				.setTitle(R.string.dialog_main_hide_warning_title)
				.setMessage(R.string.dialog_main_hide_warning_message)
				.setPositiveButton(R.string.dialog_yes,null)
				.show();
			}
			else{
			}
			
			break;
		default:
			return super.onContextItemSelected(item);
		}
		
		return true;
	}


	public void notifyDataSetChanged(){
		mAdapter.notifyDataSetChanged();
	}


	protected void init(){
		
		mInfoList = null;//(ListView)findViewById(R.id.dashboard_list);

		mAdapter = new BaseAdapter() {
			@Override
			public int getCount() {
				return 0;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				return null;
			}
		};
//		mAdapter = new SportInfoListAdapter(this.getActivity()){
//
//			@Override
//			public Calendar getCurrentCalendar() {
//				return mDate;
//			}
//
//		};
		
//		AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(mAdapter);
//        alphaInAnimationAdapter.setAbsListView(mInfoList);
//        alphaInAnimationAdapter.setInitialDelayMillis(100);;
//        if ( DebugTool.isDebug() ){
//            ((SportInfoListAdapter)mAdapter).mTitleFactory.cancelAnimation();
//            mInfoList.setAdapter(mAdapter);
//        }
//        else
//        	mInfoList.setAdapter(alphaInAnimationAdapter);
        
        //mInfoList.setAdapter(new SportInfoListAdapter());
		
        mInfoList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
//				if ( MainInterfaceSetting.getInstance().getSportInfoList()
//						.get(position).isHaveStatisticChild() ){
//					ExpandableListItemAdapter.ViewHolder holder = (ExpandableListItemAdapter.ViewHolder)view.getTag();
//
//					View.OnClickListener l = (View.OnClickListener)holder.listener;
//					l.onClick(view);
//				}
//				else{
//					Class<?> nextWindow = MainInterfaceSetting.getInstance().getSportInfoList().get(position).getNextChildWindow();
//					if ( nextWindow != null ){
//
//						Intent intent = new Intent(getActivity(), nextWindow);
//						intent.putExtra("calendar", mDate.getTimeInMillis());
//						startActivity(intent);
//					}
//				}
				
			}
        	
        });
        
//        mInfoList.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				System.out.println("pass long press");
//				return false;
//			}
//		});
        
        registerForContextMenu(mInfoList);
	}
}
