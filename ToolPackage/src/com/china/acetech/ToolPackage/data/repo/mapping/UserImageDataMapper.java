package com.china.acetech.ToolPackage.data.repo.mapping;


import com.china.acetech.ToolPackage.data.domain.UserImageData_AP;
import com.china.acetech.ToolPackage.data.repo.greenDao.UserImageData;

public class UserImageDataMapper implements EntityMapper<UserImageData_AP, UserImageData>{

	@Override
	public UserImageData_AP fromDbEntity(UserImageData DBEntity) {
		if (DBEntity == null )
			return null;
		
		UserImageData_AP info = new UserImageData_AP();
		
		info.setID(DBEntity.getId());
		info.setImageData(DBEntity.getImageData());
		
		return info;
	}

	@Override
	public UserImageData toDbEntity(UserImageData_AP APPEntity) {
		return toDbEntity(APPEntity, null);
	}

	@Override
	public UserImageData toDbEntity(UserImageData_AP APPEntity, UserImageData DBEntity) {
		if (DBEntity == null)
			DBEntity = new UserImageData();
		
		DBEntity.setId(APPEntity.getID());
		DBEntity.setImageData(APPEntity.getImageData());
		
		return DBEntity;
	}

}
