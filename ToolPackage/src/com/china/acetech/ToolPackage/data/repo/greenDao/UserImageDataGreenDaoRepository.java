package com.china.acetech.ToolPackage.data.repo.greenDao;


import com.china.acetech.ToolPackage.data.domain.UserImageData_AP;
import com.china.acetech.ToolPackage.data.repo.mapping.EntityMapper;
import com.china.acetech.ToolPackage.data.repo.mapping.UserImageDataMapper;
import de.greenrobot.dao.AbstractDao;

public class UserImageDataGreenDaoRepository extends AbstractEntityGreenDaoRepository<UserImageData_AP, UserImageData>{

	@Override
	protected EntityMapper<UserImageData_AP, UserImageData> createMapper(
			DaoSession session) {
		return new UserImageDataMapper();
	}

	@Override
	protected AbstractDao<UserImageData, Long> getDaoFrom(DaoSession session) {
		return session.getUserImageDataDao();
	}

	@Override
	protected Long getPkFrom(UserImageData DBEntity) {
		return DBEntity.getId();
	}

	public UserImageData_AP getEntity(){
		UserImageData_AP entity = getById(UserImageData_AP.INFO_ID);	
		if ( entity == null ){
			entity = UserImageData_AP.getEmptyEntity();
		}
		return entity;
	}
}
