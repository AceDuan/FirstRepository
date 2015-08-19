package com.china.acetech.ToolPackage.data.domain;


/**
 * Entity mapped to table USER_IMAGE_DATA.
 */
public class UserImageData_AP extends Entity{

	private long id;
    private String ImageData;
    public static final int INFO_ID = 2243;

    public UserImageData_AP() {
    }


    public UserImageData_AP(Long id, String ImageData) {
    	setEntityID(id);
        this.ImageData = ImageData;
    }

    public static UserImageData_AP getEmptyEntity(){
    	UserImageData_AP entity = new UserImageData_AP();
    	entity.setID(INFO_ID);
    	entity.setImageData("");
    	
    	return entity;
    }
    
	@Override
	public String getEntityName() {
		return "UserImageData";
	}
	
	public void setID(long s_id){
		id = s_id;
	}
	public long getID(){
		return id;
	}

    public String getImageData() {
        return ImageData;
    }

    public void setImageData(String ImageData) {
        this.ImageData = ImageData;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
