package com.china.acetech.ToolPackage.data.repo.mapping;


import com.china.acetech.ToolPackage.data.domain.BraceletInfo_AP;
import com.china.acetech.ToolPackage.data.repo.greenDao.BraceletInfo;

public class BraceletInfoMapper implements EntityMapper<BraceletInfo_AP, BraceletInfo>{

	@Override
	public BraceletInfo_AP fromDbEntity(BraceletInfo DBEntity) {
		if (DBEntity == null )
			return null;
		
		BraceletInfo_AP info = new BraceletInfo_AP();
		
		info.setID(DBEntity.getId());
		info.setBracelet(DBEntity.getBracelet());
		info.setVersion(DBEntity.getVersion());
		info.setMD5(DBEntity.getMD5());
		info.setMacAddress(DBEntity.getMacAddress());
		info.setFileName(DBEntity.getFileName());
		
		return info;
	}

	@Override
	public BraceletInfo toDbEntity(BraceletInfo_AP APPEntity) {
		return toDbEntity(APPEntity, null);
	}

	@Override
	public BraceletInfo toDbEntity(BraceletInfo_AP APPEntity, BraceletInfo DBEntity) {
		if (DBEntity == null)
			DBEntity = new BraceletInfo();
		
		DBEntity.setId(APPEntity.getID());
		DBEntity.setBracelet(APPEntity.getBracelet());
		DBEntity.setVersion(APPEntity.getVersion());
		DBEntity.setMD5(APPEntity.getMD5());
		DBEntity.setMacAddress(APPEntity.getMacAddress());
		DBEntity.setFileName(APPEntity.getFileName());
		
		return DBEntity;
	}

}
