package com.china.acetech.ToolPackage.data.repo.mapping;


import com.china.acetech.ToolPackage.data.domain.SportInfo_AP;
import com.china.acetech.ToolPackage.data.repo.greenDao.SportInfo;

public class SportInfoMapper implements EntityMapper<SportInfo_AP, SportInfo> {

    @Override
    public SportInfo_AP fromDbEntity(SportInfo DBEntity) {
        if (DBEntity == null)
            return null;

        SportInfo_AP info = new SportInfo_AP();

        info.setId(DBEntity.getId());
        info.setCalendar(DBEntity.getCalendar());
        info.setNumber(DBEntity.getNumber());
        info.setTimeStyle(DBEntity.getTimestyle());
        info.setSteps(DBEntity.getSteps());
        info.setDistance(DBEntity.getDistance());
        info.setCalories(DBEntity.getCalories());
        info.setActive(DBEntity.getActive());
        info.setFloor(DBEntity.getFloor());
        info.setSleepStatus(DBEntity.getSleepstatus());
        info.setLastsynctime(DBEntity.getLastsynctime());

        return info;
    }

    @Override
    public SportInfo toDbEntity(SportInfo_AP APPEntity) {
        return toDbEntity(APPEntity, null);
    }

    @Override
    public SportInfo toDbEntity(SportInfo_AP APPEntity, SportInfo DBEntity) {
        if (DBEntity == null)
            DBEntity = new SportInfo();

        DBEntity.setId(APPEntity.getId());
        DBEntity.setCalendar(APPEntity.getCalendar());
        DBEntity.setNumber(APPEntity.getNumber());
        DBEntity.setTimestyle(APPEntity.getTimeStyle());
        DBEntity.setSteps(APPEntity.getSteps());
        DBEntity.setDistance(APPEntity.getDistance());
        DBEntity.setCalories(APPEntity.getCalories());
        DBEntity.setActive(APPEntity.getActive());
        DBEntity.setFloor(APPEntity.getFloor());
        DBEntity.setSleepstatus(APPEntity.getSleepStatus());
        DBEntity.setLastsynctime(APPEntity.getLastsynctime());

        return DBEntity;
    }

}
