package com.china.acetech.ToolPackage.data.repo.greenDao;


import com.china.acetech.ToolPackage.data.domain.BraceletInfo_AP;
import com.china.acetech.ToolPackage.data.repo.mapping.BraceletInfoMapper;
import com.china.acetech.ToolPackage.data.repo.mapping.EntityMapper;
import de.greenrobot.dao.AbstractDao;

public class BraceletInfoGreenDaoRepository extends AbstractEntityGreenDaoRepository<BraceletInfo_AP, BraceletInfo>{

	@Override
	protected EntityMapper<BraceletInfo_AP, BraceletInfo> createMapper(
			DaoSession session) {
		return new BraceletInfoMapper();
	}

	@Override
	protected AbstractDao<BraceletInfo, Long> getDaoFrom(DaoSession session) {
		return session.getBraceletInfoDao();
	}

	@Override
	protected Long getPkFrom(BraceletInfo DBEntity) {
		return DBEntity.getId();
	}

	public BraceletInfo_AP getEntity(){
		BraceletInfo_AP entity = getById(BraceletInfo_AP.INFO_ID);	
		if ( entity == null ){
			entity = BraceletInfo_AP.getEmptyEntity();
			entity.setID(BraceletInfo_AP.INFO_ID);
		}
		return entity;
	}
	
	public BraceletInfo_AP getUserEntity(){
		BraceletInfo_AP entity = getById(BraceletInfo_AP.USER_INFO_ID);	
		if ( entity == null ){
			entity = BraceletInfo_AP.getEmptyEntity();
			entity.setID(BraceletInfo_AP.USER_INFO_ID);
		}
		return entity;
	}
	
	public void clearUserEntity(){
		BraceletInfo_AP entity = getById(BraceletInfo_AP.USER_INFO_ID);	
		if ( entity != null ){
			entity = BraceletInfo_AP.getEmptyEntity();
			save(entity);
		}
	}
	
	public boolean isNeedDownloadBraceletData(){
		BraceletInfo_AP userEntity = getUserEntity();
		BraceletInfo_AP entity = getEntity();
		
		boolean ret = false;
		try{
			if ( userEntity.getVersion().length() != 0 && entity.getVersion().length() != 0 ){
//				if ( !userEntity.getBracelet().equals("Caf05") )
//					return false;
				
				VersionHolder user = new VersionHolder(userEntity.getVersion());
				VersionHolder curr = new VersionHolder(entity.getVersion());
				if ( curr.isFresherThan(user) )
					ret = true;
			}
		}
		catch(Exception e){e.printStackTrace();}
		
		return ret;
	}
	
	public void updateSuccess(){
		BraceletInfo_AP userEntity = getUserEntity();
		BraceletInfo_AP entity = getEntity();
		
		try{
			if ( userEntity.getVersion().length() != 0 && entity.getVersion().length() != 0 ){
//				if ( !userEntity.getBracelet().equals("Caf05") )
//					return ;
				
				userEntity.setVersion(entity.getVersion());
				save(userEntity);
			}
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	public static class VersionHolder{
		int first;
		int second;
		int third;
		long longNum;
		public VersionHolder(String version){
			version = version.substring(1);
			version = version.replace(".", ",");
			String[] array = version.split(",");
			first = Integer.valueOf(array[0]);
			second = Integer.valueOf(array[1]);
			third = Integer.valueOf(array[2]);
			longNum = Long.valueOf(array[3]);
		}
		
		public boolean isFresherThan(VersionHolder holder){
			if ( first > holder.first || second > holder.second || third > holder.third
					|| longNum > holder.longNum )
				return true;
			else
				return false;
		}
	}
}
