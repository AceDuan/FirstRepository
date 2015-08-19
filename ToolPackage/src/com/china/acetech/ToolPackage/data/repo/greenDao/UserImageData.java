package com.china.acetech.ToolPackage.data.repo.greenDao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table USER_IMAGE_DATA.
 */
public class UserImageData {

    private Long id;
    private String ImageData;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public UserImageData() {
    }

    public UserImageData(Long id) {
        this.id = id;
    }

    public UserImageData(Long id, String ImageData) {
        this.id = id;
        this.ImageData = ImageData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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