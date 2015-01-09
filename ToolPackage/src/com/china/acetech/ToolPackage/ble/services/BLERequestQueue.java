package com.china.acetech.ToolPackage.ble.services;

import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.ble.bletool.ErrorCodeGroup;
import com.china.acetech.ToolPackage.customwidget.tool.PropertyRegisterable;

import java.util.ArrayList;
import java.util.List;


public class BLERequestQueue {

	List<WorkState> mlist;
	RequestProcess processer;
	
	public BLERequestQueue(){
		mlist = new ArrayList<WorkState>();
	}
	
	public void setProcesser(RequestProcess listener){
		processer = listener;
	}
	/**
	 * 藍牙消息的處理中心，Queue會把發送的消息交給隊頭的requestProcesser處理
	 * @param order NULL表示隊列任務的啟動信號。 或者是BLE接口收到的消息
	 * @return 返回需要處理的結果。如果是NULL表示所有的消息已經全部處理完畢。
	 * 
	 */
	public void process(byte[] order){

		if ( mlist.size() == 0 ) return;
		
		
		ProcessResult result;
		
		result = processForOneOrder(order);
		
		switch( result.getWorkState() ){
		case ProcessResult.WORK_OVER://workover
			processForWorkOver();
			break;
		case ProcessResult.WORK_ERROR:
			processForError(result);
			break;
		default:
			break;
		}
				
		byte[] resultOrder = result.getResultOrder();
		if ( processer != null && resultOrder != null )
			processer.process(resultOrder);
		
		//return resultOrder;
	}
	
	private ProcessResult processForOneOrder(byte[] order){
		
		WorkState current = mlist.get(0);
		ProcessResult result;
		
		result = current.process(ProcessSender.getSender(order));
		
		if ( result.getWorkState() == ProcessResult.WORK_OVER ){
			mlist.remove(current);
			if ( mlist.size() != 0 ){
				current = mlist.get(0);
				result = current.process(ProcessSender.startWorkSender());
			}
			else {
				result = new ProcessResult(null, ProcessResult.WORK_OVER);
			}
			
		}
		
		return result;
	}
	
	private void processForWorkOver(){
		//如果工作結束了的話，就需要檢查隊列是否全部處理完畢，如果是，則表示任務完成。
		if ( mlist.size() <= 1 ){
			//全部流程完畢
			mlist.clear();
			
			//向本地或者界面發送通知，表明同步完畢
			if ( MyApplication.getRegisterable() != null )
				MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.BLE_SYNC_OVER,
					null, null);
		}
		
	}
	
	private void processForError(ProcessResult result){
		
		//如果消息需要進行網絡通信，那麼就會發送一個沒有做完工作，但是order為null的結果。
		//暫時避開對ble的操作。等待網絡處理結束以後，再向Queue發送繼續處理的消息。
		if ( result.getErrorState() == ErrorCodeGroup.WEB_SYNC_FAIL){
			mlist.clear();
			
			//向本地數據庫或者界面發送通知，表明同步失敗
			if ( MyApplication.getRegisterable() != null ){
				MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.BLE_SYNC_OVER,
						null, null);
			}
		}
		else if ( result.getErrorState() == ErrorCodeGroup.MATCHKEY_WRONG ){
			mlist.clear();

			//匹配失敗，發送匹配結束命令
			if ( MyApplication.getRegisterable() != null )
				MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.BLE_SYNC_OVER,
					null, null);
			
			//於此同時需要斷開與設備的連接。
			if ( MyApplication.getRegisterable() != null )
				MyApplication.getRegisterable().firePropertyChange(PropertyRegisterable.BLE_DISCONNECT_WITH_WRONG_DE, null, null);
		}
	}
	
	public void addRequestToQueue(WorkState request){
		mlist.add(request);
	}
	
	public void addRequestToQueue(WorkState request, int position){
		mlist.add(position, request);
	}
	
	public boolean isEmpty(){
		return mlist.isEmpty();
	}
	
	public void clearQueue(){
		mlist.clear();
	}
	
	public static interface RequestProcess{
		public void process(byte[] order);
	}
	
	
	public static interface WorkState{
		/**
		 * 消息處理對象對藍牙設備的相應消息進行回應，如果任務完成則直接返回NULL，isWorkOver=false
		 * @param sender
		 * @return
		 */
		public ProcessResult process(ProcessSender sender);
	}
	
	
//	private static abstract class ProcessPackage{
//		protected final byte[] mOrder;
//		protected final boolean mProcessFlag;
//		
//		protected ProcessPackage(byte[] order, boolean flag){
//			mOrder = order;
//			mProcessFlag = flag;
//		}
//	}
	public static class ProcessSender{
		protected final byte[] mOrder;
		protected final boolean mProcessFlag;
		
		public ProcessSender( byte[] order, boolean isStartWork ){
			mOrder = order;
			mProcessFlag = isStartWork;
		}
		
		public byte[] getSendOrder(){
			return mOrder;
		}
		public boolean isStartWork(){
			return mProcessFlag;
		}
		
		public static ProcessSender startWorkSender(){
			return new ProcessSender(null, true);
		}
		public static ProcessSender getSender(byte[] order){
			if ( order == null ){
				return startWorkSender();
			}
			else{
				return new ProcessSender(order, false);
			}
		}
	}
	
	public static class ProcessResult{
		protected final byte[] mOrder;
		//protected final boolean mProcessFlag;
		protected final int mWorkState;
		protected final ErrorCodeGroup.ErrorCode mErrorState;
		
		public static final int WORK_OVER = 1;
		public static final int WORK_START = 2;
		public static final int WORK_ERROR = 3;
		
		public ProcessResult( byte[] order, int workState ){
			mOrder = order;
			mWorkState = workState;
			mErrorState = ErrorCodeGroup.ErrorCode.NULL;
		}
		
		public ProcessResult( ErrorCodeGroup.ErrorCode errorState){
			mOrder = null;
			mWorkState = WORK_ERROR;
			mErrorState = errorState;
		}
		
		public byte[] getResultOrder(){
			return mOrder;
		}
		public int getWorkState(){
			return mWorkState;
		}
		
		public ErrorCodeGroup.ErrorCode getErrorState(){
			return mErrorState;
		}
	}
	
	
}
