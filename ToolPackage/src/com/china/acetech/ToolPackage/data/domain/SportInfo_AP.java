package com.china.acetech.ToolPackage.data.domain;

import com.china.acetech.ToolPackage.MyApplication;
import com.china.acetech.ToolPackage.java.CalendarToolForSync;

import java.util.Date;

/**
 * Entity mapped to table SPORT_INFO.
 */
public class SportInfo_AP extends Entity{

    private Long id;
    private Date calendar;
    private Integer number;
    private Integer timestyle;
    private Integer steps;
    private Integer distance;
    private Integer calories;
    private Long active;
    private Integer floor;
    private Integer sleepstatus;
    private Date lastsynctime;

    public static final int HOUR_STATUSTIC = 1;
    public static final int DAY_STATUSTIC = 2;
    public static final int MONTH_STATUSTIC = 3;
    
    public static final int REAL_TIME = 10;
    
    public SportInfo_AP() {
    }
    
    public SportInfo_AP(Date date){
    	calendar = date;
    	
    	steps = 0;
    	distance = 0;
    	calories = 0;
    	active = 0l;
    	floor = 0;
    	timestyle = 0;
    	sleepstatus = 0;
    	lastsynctime = CalendarToolForSync.getZeroTime();
    }

    @Override
	public String getEntityName() {
		return "SportInfo";
	}
    
    public SportInfo_AP(Long id, Date calendar) {
        this.id = id;
        this.calendar = calendar;
    }

    public SportInfo_AP(Long id, Date calendar, Integer number, Integer timestyle, Integer steps, Integer distance, Integer calories, Long active, Integer floor, Integer sleepstatus, Date lastsynctime) {
        this.id = id;
        this.calendar = calendar;
        this.number = number;
        this.timestyle = timestyle;
        this.steps = steps;
        this.distance = distance;
        this.calories = calories;
        this.active = active;
        this.floor = floor;
        this.sleepstatus = sleepstatus;
        this.lastsynctime = lastsynctime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCalendar() {
        return calendar;
    }

    public void setCalendar(Date calendar) {
        this.calendar = calendar;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
    
    public Integer getTimeStyle() {
        return timestyle;
    }

    public void setTimeStyle(Integer timestyle) {
        this.timestyle = timestyle;
    }
    
    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }
    
    public Integer getSleepStatus() {
        return sleepstatus;
    }

    public void setSleepStatus(Integer sleepstatus) {
        this.sleepstatus = sleepstatus;
    }

    public Date getLastsynctime() {
        return lastsynctime;
    }

    public void setLastsynctime(Date lastsynctime) {
        this.lastsynctime = lastsynctime;
    }


}
