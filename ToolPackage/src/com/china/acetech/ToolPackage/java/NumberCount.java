package com.china.acetech.ToolPackage.java;

//每個階段比值由5開始，兩倍遞增。

//後續可以考慮使用另一種算法，在到位數進位時重置為 比如80下一個是160 重置成100.
public class NumberCount {

	public static final long BASE_NUMBER = 20;
	public static LeftNumberStruct getCeiling(long number){
		long basenumber = BASE_NUMBER;
		while ( number > basenumber )
			basenumber *= 2;
		
		
		
		
		return new LeftNumberStruct(basenumber, basenumber/4);
	}
	
	public static class LeftNumberStruct {

		private long maxNumber;
		private long ratio;
		private long current;
		
		LeftNumberStruct(long max, long ratioSet){
			maxNumber = max;
			ratio = ratioSet;
		}
		
		public void init(){
			current = ratio;
		}
		
		public long next(){
			long ret = current;
			if ( ret == maxNumber )	return ret;
			current += ratio;
			return ret;
		}
		
		public long getMax(){
			return maxNumber;
		}
	}
}
