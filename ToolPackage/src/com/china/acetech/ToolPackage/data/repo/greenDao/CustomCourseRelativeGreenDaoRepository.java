package com.china.acetech.ToolPackage.data.repo.greenDao;

import java.util.List;


import com.china.acetech.ToolPackage.data.domain.CustomCourseRelative_AP;
import com.china.acetech.ToolPackage.data.repo.mapping.CustomCourseRelativeMapper;
import com.china.acetech.ToolPackage.data.repo.mapping.EntityMapper;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

public class CustomCourseRelativeGreenDaoRepository extends AbstractEntityGreenDaoRepository<CustomCourseRelative_AP, CustomCourseRelative>{

	@Override
	protected EntityMapper<CustomCourseRelative_AP, CustomCourseRelative> createMapper(
			DaoSession session) {
		return new CustomCourseRelativeMapper();
	}

	@Override
	protected AbstractDao<CustomCourseRelative, Long> getDaoFrom(DaoSession session) {
		return session.getCustomCourseRelativeDao();
	}

	@Override
	protected Long getPkFrom(CustomCourseRelative DBEntity) {
		return DBEntity.getId();
	}

	public List<CustomCourseRelative_AP> getByCourseID(String CourseID){
		QueryBuilder<CustomCourseRelative> qb = getEntityDao().queryBuilder();
		return fromDbEntities(
				qb.where(CustomCourseRelativeDao.Properties.CourseID.eq(CourseID), new WhereCondition[]{})
				.orderAsc(CustomCourseRelativeDao.Properties.ActionOrder)
				.list());
		//return getEntitiesWhereAnd(CustomCourseDao.Properties.CourseID.eq(CourseID), new WhereCondition[]{});
	}
	
}
