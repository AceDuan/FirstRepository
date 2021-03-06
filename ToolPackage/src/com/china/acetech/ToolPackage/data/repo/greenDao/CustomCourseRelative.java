package com.china.acetech.ToolPackage.data.repo.greenDao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table CUSTOM_COURSE_RELATIVE.
 */
public class CustomCourseRelative {

    private Long id;
    private Integer actionID;
    private String courseID;
    private Integer actionOrder;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public CustomCourseRelative() {
    }

    public CustomCourseRelative(Long id) {
        this.id = id;
    }

    public CustomCourseRelative(Long id, Integer actionID, String courseID, Integer actionOrder) {
        this.id = id;
        this.actionID = actionID;
        this.courseID = courseID;
        this.actionOrder = actionOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getActionID() {
        return actionID;
    }

    public void setActionID(Integer actionID) {
        this.actionID = actionID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public Integer getActionOrder() {
        return actionOrder;
    }

    public void setActionOrder(Integer actionOrder) {
        this.actionOrder = actionOrder;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
