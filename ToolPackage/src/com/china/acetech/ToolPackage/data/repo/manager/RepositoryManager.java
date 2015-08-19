package com.china.acetech.ToolPackage.data.repo.manager;


import com.china.acetech.ToolPackage.data.repo.greenDao.BraceletInfoGreenDaoRepository;
import com.china.acetech.ToolPackage.data.repo.greenDao.CustomCourseGreenDaoRepository;
import com.china.acetech.ToolPackage.data.repo.greenDao.CustomCourseRelativeGreenDaoRepository;
import com.china.acetech.ToolPackage.data.repo.greenDao.UserImageDataGreenDaoRepository;

public class RepositoryManager {

	private static RepositoryManager instance = null;
	public static RepositoryManager getInstance(){
		if (instance == null){
			instance = new RepositoryManager();
		}
		
		return instance;
	}
	
	private RepositoryManager(){
		
	}
	
	public static void clearUserInfo(){
		RepositoryManager manager = getInstance();
		manager.getCustomCourseRelative().clear(true);
		manager.getUserImageData().clear(true);
		manager.getBracelet().clear(true);
	}
	

	
	private CustomCourseGreenDaoRepository customCourse;
	
	public CustomCourseGreenDaoRepository getCustomCourse(){
		if ( customCourse == null ){
			customCourse = new CustomCourseGreenDaoRepository();
		}
		
		return customCourse;
	}
	
	private CustomCourseRelativeGreenDaoRepository customCourseRelative;
	
	public CustomCourseRelativeGreenDaoRepository getCustomCourseRelative(){
		if ( customCourseRelative == null ){
			customCourseRelative = new CustomCourseRelativeGreenDaoRepository();
		}
		
		return customCourseRelative;
	}

	
	private UserImageDataGreenDaoRepository userImageData;
	
	public UserImageDataGreenDaoRepository getUserImageData(){
		if ( userImageData == null ){
			userImageData = new UserImageDataGreenDaoRepository();
		}
		
		return userImageData;
	}
	
	private BraceletInfoGreenDaoRepository braceletInfo;
	
	public BraceletInfoGreenDaoRepository getBracelet(){
		if ( braceletInfo == null ){
			braceletInfo = new BraceletInfoGreenDaoRepository();
		}
		
		return braceletInfo;
	}
}
