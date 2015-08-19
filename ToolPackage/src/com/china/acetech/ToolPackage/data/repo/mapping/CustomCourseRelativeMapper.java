package com.china.acetech.ToolPackage.data.repo.mapping;


import com.china.acetech.ToolPackage.data.domain.CustomCourseRelative_AP;
import com.china.acetech.ToolPackage.data.repo.greenDao.CustomCourseRelative;

public class CustomCourseRelativeMapper implements EntityMapper<CustomCourseRelative_AP, CustomCourseRelative>{

	@Override
	public CustomCourseRelative_AP fromDbEntity(CustomCourseRelative DBEntity) {
		if (DBEntity == null )
			return null;
		
		CustomCourseRelative_AP info = new CustomCourseRelative_AP();
		
		info.setId(DBEntity.getId());
		info.setActionID(DBEntity.getActionID());
		info.setCourseID(DBEntity.getCourseID());
		info.setActionOrder(DBEntity.getActionOrder());
		
	    return info;
	}

	@Override
	public CustomCourseRelative toDbEntity(CustomCourseRelative_AP APPEntity) {
		return toDbEntity(APPEntity, null);
	}

	@Override
	public CustomCourseRelative toDbEntity(CustomCourseRelative_AP APPEntity, CustomCourseRelative DBEntity) {
		if (DBEntity == null)
			DBEntity = new CustomCourseRelative();
		
		DBEntity.setId(APPEntity.getId());
		DBEntity.setActionID(APPEntity.getActionID());
		DBEntity.setCourseID(APPEntity.getCourseID());
		DBEntity.setActionOrder(APPEntity.getActionOrder());
		
		return DBEntity;
	}

}
