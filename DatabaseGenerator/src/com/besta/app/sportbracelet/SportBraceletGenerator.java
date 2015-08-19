package com.besta.app.sportbracelet;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class SportBraceletGenerator {


    private static final String DB_package = "com.china.acetech.ToolPackage.data.repo.greenDao";
    private static final int DB_version = 1;
    private static final String DB_sourcefile_path = "./ToolPackage/src";

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Schema schema = new Schema(DB_version, DB_package);

        schema.enableKeepSectionsByDefault();

        addUserInfo(schema);


        try {
            new DaoGenerator().generateAll(schema, DB_sourcefile_path);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private static void addUserInfo(Schema schema) {
        Entity info;

        info = schema.addEntity("CustomCourse");
        info.addIdProperty().autoincrement();;
        info.addStringProperty("courseID");
        info.addStringProperty("courseName");
        info.addIntProperty("categoryID");
        info.addIntProperty("trainSegmentID");
        info.addDateProperty("syncTime");
        info.addLongProperty("CountOfSum");
        info.addLongProperty("CountInWeek");
        info.addIntProperty("Level");
        info.addBooleanProperty("isFavorite");
        info.addStringProperty("firstPicturePath");

        info = schema.addEntity("CustomCourseRelative");
        info.addIdProperty().autoincrement();;
        info.addIntProperty("actionID");
        info.addStringProperty("courseID");
        info.addIntProperty("actionOrder");


        info = schema.addEntity("UserImageData");
        info.addIdProperty();
        info.addStringProperty("ImageData");

        info = schema.addEntity("BraceletInfo");
        info.addIdProperty();
        info.addStringProperty("bracelet");
        info.addStringProperty("version");
        info.addStringProperty("MD5");
        info.addStringProperty("MacAddress");
        info.addStringProperty("fileName");

        /*Entity sportInfo = schema.addEntity("SportInfo");
        sportInfo.addIdProperty().autoincrement();
        sportInfo.addDateProperty("calendar");
        sportInfo.addIntProperty("number");
        sportInfo.addIntProperty("timestyle");
        sportInfo.addIntProperty("steps");
        sportInfo.addIntProperty("distance");
        sportInfo.addIntProperty("calories");
        sportInfo.addLongProperty("active");
        sportInfo.addIntProperty("floor");
        sportInfo.addIntProperty("sleepstatus");
        sportInfo.addDateProperty("lastsynctime");
        */

  /*      Entity info = schema.addEntity("UserInfo");
        info.addIdProperty();
        info.addStringProperty("account").notNull();
        info.addStringProperty("name");
        info.addDateProperty("birthday");
        info.addDateProperty("registerdate");
        info.addDateProperty("lastsynctime");
        info.addLongProperty("persionHeight");
        info.addLongProperty("persionWeight");
        info.addStringProperty("lengthUnit");
        info.addStringProperty("weightUnit");
        info.addStringProperty("volumeUnit");
        info.addStringProperty("foodLocal");
        info.addStringProperty("country");
        info.addStringProperty("timeZone");
        info.addStringProperty("sex");

        Entity goal = schema.addEntity("SportGoal");
        goal.addIdProperty();
        goal.addIntProperty("steps");
        goal.addIntProperty("distance");
        goal.addIntProperty("calories");
        goal.addLongProperty("active");
        goal.addIntProperty("floor");
        goal.addLongProperty("targetWeight");
        goal.addIntProperty("WaterVolumes");
        goal.addDateProperty("lastsynctime");


        Entity weightLog = schema.addEntity("WeightLog");
        weightLog.addIdProperty().autoincrement();
        weightLog.addDateProperty("calendar");
        weightLog.addLongProperty("weightChange");
        weightLog.addLongProperty("bodyfat");
        weightLog.addDateProperty("lastsynctime");

        //activeTime ���ȬO�ରlong�H�᪺�ȡC��K�p��M�s�x�C
        Entity activeLog = schema.addEntity("ActiveLog");
        activeLog.addIdProperty().autoincrement();
        activeLog.addDateProperty("calendar");
        activeLog.addDateProperty("startTime");
        activeLog.addLongProperty("activeTime");
        activeLog.addStringProperty("sportName");
        activeLog.addIntProperty("caloriePerUnit");
        activeLog.addDateProperty("lastsynctime");

        Entity waterLog = schema.addEntity("WaterLog");
        waterLog.addIdProperty().autoincrement();
        waterLog.addDateProperty("calendar");
        waterLog.addLongProperty("waterGet");
        waterLog.addDateProperty("lastsynctime");

        Entity sleepLog = schema.addEntity("SleepLog");
        sleepLog.addIdProperty().autoincrement();
        sleepLog.addDateProperty("calendar");
        sleepLog.addDateProperty("startTime");
        sleepLog.addLongProperty("sleepTime");
        sleepLog.addDateProperty("lastsynctime");

        Entity foodLog = schema.addEntity("FoodLog");
        foodLog.addIdProperty().autoincrement();
        foodLog.addDateProperty("calendar");
        foodLog.addStringProperty("foodName");
        foodLog.addLongProperty("eatNumber");
        foodLog.addIntProperty("calories");
        foodLog.addIntProperty("mealTime");
        foodLog.addDateProperty("lastsynctime");

        Entity deviceInfo = schema.addEntity("DeviceInfo");
        deviceInfo.addIdProperty();
        deviceInfo.addStringProperty("mainGoal");
        deviceInfo.addStringProperty("wristPlacement");
        deviceInfo.addIntProperty("statsDisplay");
        deviceInfo.addIntProperty("clockDisplay");
        deviceInfo.addDateProperty("syncDate");
        deviceInfo.addDateProperty("lastsynctime");

        Entity clockLog = schema.addEntity("AlarmClockLog");
        clockLog.addIdProperty();
        clockLog.addBooleanProperty("isRepeat");
        clockLog.addIntProperty("dayOfWeek");
        clockLog.addDateProperty("calendar");
        clockLog.addIntProperty("AlarmState");
        clockLog.addIntProperty("AlarmTime");
        clockLog.addDateProperty("lastsynctime");

        Entity foodData = schema.addEntity("FoodListLog");
        foodData.addIdProperty();
        foodData.addStringProperty("foodName");
        foodData.addLongProperty("calories");
        foodData.addBooleanProperty("isFavorite");
        foodData.addIntProperty("usingTimes");
        foodData.addDateProperty("lastsynctime");

        Entity sportData = schema.addEntity("SportListLog");
        sportData.addIdProperty();
        sportData.addStringProperty("sportName");
        sportData.addLongProperty("calories");
        sportData.addBooleanProperty("isFavorite");
        sportData.addIntProperty("usingTimes");
        sportData.addDateProperty("lastsynctime");

        Entity sleepStatusData = schema.addEntity("SleepStatus");
        sleepStatusData.addIdProperty().autoincrement();
        sleepStatusData.addDateProperty("calendar");
        sleepStatusData.addLongProperty("startDate");
        sleepStatusData.addLongProperty("endDate");
        sleepStatusData.addIntProperty("sleepDtatus");
        sleepStatusData.addDateProperty("lastsynctime");

        Entity sleepStatisticData = schema.addEntity("SleepStatistic");
        sleepStatisticData.addIdProperty().autoincrement();
        sleepStatisticData.addDateProperty("calendar");
        sleepStatisticData.addIntProperty("timeStyle");
        sleepStatisticData.addIntProperty("sleepTime");
        sleepStatisticData.addDateProperty("lastsynctime");

        Entity foodandsportsavelistData = schema.addEntity("SleepAndFoodSavingList");
        foodandsportsavelistData.addIdProperty().autoincrement();
        foodandsportsavelistData.addIntProperty("listStyle");
        foodandsportsavelistData.addStringProperty("dataName");
        foodandsportsavelistData.addIntProperty("dataValuePerUnit");
        foodandsportsavelistData.addIntProperty("dataPickTimes");
        foodandsportsavelistData.addDateProperty("lastsynctime");

        Entity FitnessPlan = schema.addEntity("FitnessPlan");
        FitnessPlan.addIdProperty();
        FitnessPlan.addIntProperty("initialWeight");
        FitnessPlan.addIntProperty("targetWeight");
        FitnessPlan.addIntProperty("fitnessMethod");
        FitnessPlan.addDateProperty("completionDate");
        FitnessPlan.addDateProperty("lastsynctime");*/
    }

//    private static void addCustomerOrder(Schema schema) {  
//        Entity customer = schema.addEntity("Customer");  
//        customer.addIdProperty();  
//        customer.addStringProperty("name").notNull();  
//  
//        Entity order = schema.addEntity("Order");  
//        order.setTableName("ORDERS"); // "ORDER" is a reserved keyword  
//        order.addIdProperty();  
//        Property orderDate = order.addDateProperty("date").getProperty();  
//        Property customerId = order.addLongProperty("customerId").notNull().getProperty();  
//        order.addToOne(customer, customerId);  
//  
//        ToMany customerToOrders = customer.addToMany(order, customerId);  
//        customerToOrders.setName("orders");  
//        customerToOrders.orderAsc(orderDate);  
//    }  

}
