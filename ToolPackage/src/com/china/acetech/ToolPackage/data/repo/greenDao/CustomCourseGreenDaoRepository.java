package com.china.acetech.ToolPackage.data.repo.greenDao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import com.china.acetech.ToolPackage.data.domain.CustomCourse_AP;
import com.china.acetech.ToolPackage.data.repo.mapping.CustomCourseMapper;
import com.china.acetech.ToolPackage.data.repo.mapping.EntityMapper;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

public class CustomCourseGreenDaoRepository extends AbstractEntityGreenDaoRepository<CustomCourse_AP, CustomCourse>{

	@Override
	protected EntityMapper<CustomCourse_AP, CustomCourse> createMapper(
			DaoSession session) {
		return new CustomCourseMapper();
	}

	@Override
	protected AbstractDao<CustomCourse, Long> getDaoFrom(DaoSession session) {
		return session.getCustomCourseDao();
	}

	@Override
	protected Long getPkFrom(CustomCourse DBEntity) {
		return DBEntity.getId();
	}

	public List<CustomCourse_AP> getAllByOrder(){
		QueryBuilder<CustomCourse> qb = getEntityDao().queryBuilder();
		qb.orderDesc(CustomCourseDao.Properties.SyncTime);
		return fromDbEntities(qb.list());
	}
	
	public List<CustomCourse_AP> getAllFavoriteByOrder(){
		QueryBuilder<CustomCourse> qb = getEntityDao().queryBuilder();
		qb.where(CustomCourseDao.Properties.IsFavorite.eq(true), new WhereCondition[]{});
		qb.orderDesc(CustomCourseDao.Properties.SyncTime);
		return fromDbEntities(qb.list());
	}
	
	public List<CustomCourse_AP> getByCourseID(String CourseID){
		QueryBuilder<CustomCourse> qb = getEntityDao().queryBuilder();
		return fromDbEntities(
				qb.where(CustomCourseDao.Properties.CourseID.eq(CourseID), new WhereCondition[]{})
				.build().list());
		//return getEntitiesWhereAnd(CustomCourseDao.Properties.CourseID.eq(CourseID), new WhereCondition[]{});
	}
	
	public long getBodypartCount(int bodypartType){
		List<CustomCourse_AP> list;
		if ( bodypartType == CustomCourse_AP.ConstValue.BODYPART_LowerLimb){
			list = getEntitiesWhereOr(CustomCourseDao.Properties.TrainSegmentID.eq(CustomCourse_AP.ConstValue.BODYPART_LowerLimb),
					CustomCourseDao.Properties.TrainSegmentID.eq(CustomCourse_AP.ConstValue.BODYPART_UpperLimb_LowerLimb_Trunk),
					new WhereCondition[]{
							CustomCourseDao.Properties.TrainSegmentID.eq(CustomCourse_AP.ConstValue.BODYPART_Trunk_LowerLimb),
							CustomCourseDao.Properties.TrainSegmentID.eq(CustomCourse_AP.ConstValue.BODYPART_UpperLimb_LowerLimb),
							
										} 
					);
		}
		else if ( bodypartType == CustomCourse_AP.ConstValue.BODYPART_Trunk ){
			list = getEntitiesWhereOr(CustomCourseDao.Properties.TrainSegmentID.eq(CustomCourse_AP.ConstValue.BODYPART_Trunk), 
					CustomCourseDao.Properties.TrainSegmentID.eq(CustomCourse_AP.ConstValue.BODYPART_UpperLimb_LowerLimb_Trunk),
					new WhereCondition[]{
							CustomCourseDao.Properties.TrainSegmentID.eq(CustomCourse_AP.ConstValue.BODYPART_Trunk_LowerLimb),
							CustomCourseDao.Properties.TrainSegmentID.eq(CustomCourse_AP.ConstValue.BODYPART_UpperLimb_Trunk),
							} 
					);
		}
		else{
			list = getEntitiesWhereOr(CustomCourseDao.Properties.TrainSegmentID.eq(CustomCourse_AP.ConstValue.BODYPART_UpperLimb),
					CustomCourseDao.Properties.TrainSegmentID.eq(CustomCourse_AP.ConstValue.BODYPART_UpperLimb_LowerLimb_Trunk), 
					new WhereCondition[]{
							CustomCourseDao.Properties.TrainSegmentID.eq(CustomCourse_AP.ConstValue.BODYPART_UpperLimb_LowerLimb),
							CustomCourseDao.Properties.TrainSegmentID.eq(CustomCourse_AP.ConstValue.BODYPART_UpperLimb_Trunk),
							} 
					);
		}
		
		return list.size();
	}
	/**
	 * 此函數將實現本地與網絡的課程信息同步工作。流程如下：<br/>
	 * 1. 本地和服務器端各自擁有一個課程信息表。兩張表均已按照同步時間排好序。<br/>
	 * 2. 從兩個表的表頭取得entityA。比較并得到同步時間更晚的一項。并從原表中移出。<br/>
	 * 3. 在另一張表中查找是否有同一courseID的entityB。查找時按courseID進行查找。
	 * 若找到(另一entityB的同步時間必定不晚於此entityA)，則將entityB移出表。若無則不操作。<br/>
	 * 4. 如果entityA是webList中的一項。需要將此entity插入/保存至本地中，使用何種方式取決於步驟3的結果<br/>
	 * 5. 將entityA移出結果隊列中<br/>
	 * 
	 * <br/>
	 * PS：由於此項同步操作會導致ap的ID無限制增大。有可能使得某一時刻超過long型的最大值。
	 * 所以一定時間間隔后，同步完成后刪除本地的所有課程信息并重新下載比較好。<br/>
	 *
	 */
	public List<CustomCourse_AP> refreshCourseInfo(List<CustomCourse_AP> webCourseList){
		List<CustomCourse_AP> localList = getAllByOrder();
		List<CustomCourse_AP> resultList = new ArrayList<CustomCourse_AP>();
		
		localList = new LinkedList<CustomCourse_AP>(localList);
		webCourseList = new LinkedList<CustomCourse_AP>(webCourseList);

		while ( localList.size() != 0 && webCourseList.size() != 0 ){		
			CustomCourse_AP localEntity = localList.get(0);
			CustomCourse_AP webEntity = webCourseList.get(0);
			
			if ( localEntity.getSyncTime().after(webEntity.getSyncTime()) ){
				removeEntityFromList(webCourseList, localEntity);
				resultList.add(localList.remove(0));
			}
			else{
				removeEntityFromList(localList, webEntity);
				resultList.add(webCourseList.remove(0));
			}
		}
		
		return resultList;
	}
	
	public static CustomCourse_AP removeEntityFromList(List<CustomCourse_AP> list, CustomCourse_AP anotherEntity){
		CustomCourse_AP result = null;
		for (  int i = 0; i < list.size(); i++ ){
			CustomCourse_AP entity = list.get(i);
			
			if ( entity.getCourseID().equals(anotherEntity.getCourseID())){
				list.remove(entity);
				result = entity;
				break;
			}
		}
		
		return result;
	}
}
