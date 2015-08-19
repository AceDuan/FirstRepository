package com.china.acetech.ToolPackage.data.repo.mapping;


import com.china.acetech.ToolPackage.data.domain.CustomCourse_AP;
import com.china.acetech.ToolPackage.data.repo.greenDao.CustomCourse;

public class CustomCourseMapper implements EntityMapper<CustomCourse_AP, CustomCourse>{

	@Override
	public CustomCourse_AP fromDbEntity(CustomCourse DBEntity) {
		if (DBEntity == null )
			return null;
		
		CustomCourse_AP info = new CustomCourse_AP();
	    
		info.setId(DBEntity.getId());
		info.setCourseID(DBEntity.getCourseID());
		info.setCourseName(DBEntity.getCourseName());
		info.setCategoryID(DBEntity.getCategoryID());
		info.setTrainSegmentID(DBEntity.getTrainSegmentID());
		info.setCountOfSum(DBEntity.getCountOfSum());
		info.setCountInWeek(DBEntity.getCountInWeek());
		info.setLevel(DBEntity.getLevel());
		info.setIsFavorite(DBEntity.getIsFavorite());
		info.setSyncTime(DBEntity.getSyncTime());
		info.setFirstPicturePath(DBEntity.getFirstPicturePath());

		return info;
	}

	@Override
	public CustomCourse toDbEntity(CustomCourse_AP APPEntity) {
		return toDbEntity(APPEntity, null);
	}

	@Override
	public CustomCourse toDbEntity(CustomCourse_AP APPEntity, CustomCourse DBEntity) {
		if (DBEntity == null)
			DBEntity = new CustomCourse();
		
		DBEntity.setId(APPEntity.getId());
		DBEntity.setCourseID(APPEntity.getCourseID());
		DBEntity.setCourseName(APPEntity.getCourseName());
		DBEntity.setCategoryID(APPEntity.getCategoryID());
		DBEntity.setTrainSegmentID(APPEntity.getTrainSegmentID());
		DBEntity.setCountOfSum(APPEntity.getCountOfSum());
		DBEntity.setCountInWeek(APPEntity.getCountInWeek());
		DBEntity.setLevel(APPEntity.getLevel());
		DBEntity.setIsFavorite(APPEntity.getIsFavorite());
		DBEntity.setSyncTime(APPEntity.getSyncTime());
		DBEntity.setFirstPicturePath(APPEntity.getFirstPicturePath());
		
		return DBEntity;
	}

}
