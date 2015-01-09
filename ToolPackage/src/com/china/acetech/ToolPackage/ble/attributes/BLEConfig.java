package com.china.acetech.ToolPackage.ble.attributes;

import java.util.UUID;

public class BLEConfig {

	private static UUID mBraceletService = UUID.fromString("CDF98BD6-DD14-4B74-9AC2-4F686A3C60A8");
	private static UUID mSender = UUID.fromString("CDF98BD7-DD14-4B74-9AC2-4F686A3C60A8");
	private static UUID mReceiver = UUID.fromString("CDF98BD8-DD14-4B74-9AC2-4F686A3C60A8");
	
	private static UUID mBattaryService = UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb");
	private static UUID mBattary = UUID.fromString("00002A19-0000-1000-8000-00805f9b34fb");
	
	private static UUID mFactoryNameService = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
	private static UUID mFactoryNameCharacter = UUID.fromString("00002a29-0000-1000-8000-00805f9b34fb");
	
	public static UUID getSBServiceUUID(){
		return mBraceletService;
	}
	public static UUID getSenderUUID(){
		return mSender;
	}
	public static UUID getReceiverUUID(){
		return mReceiver;
	}
	
	public static UUID getBattaryServiceUUID(){
		return mBattaryService;
	}
	
	public static UUID getBattaryUUID(){
		return mBattary;
	}
	
	public static UUID getFactoryServiceUUID(){
		return mFactoryNameService;
	}
	
	public static UUID getFactoryUUID(){
		return mFactoryNameCharacter;
	}
	
}
