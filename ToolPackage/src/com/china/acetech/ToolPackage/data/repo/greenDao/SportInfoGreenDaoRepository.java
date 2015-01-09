package com.china.acetech.ToolPackage.data.repo.greenDao;

import com.china.acetech.ToolPackage.data.domain.SportInfo_AP;
import com.china.acetech.ToolPackage.data.repo.mapping.EntityMapper;
import com.china.acetech.ToolPackage.data.repo.mapping.SportInfoMapper;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.WhereCondition;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SportInfoGreenDaoRepository extends AbstractEntityGreenDaoRepository<SportInfo_AP, SportInfo> {

    @Override
    protected EntityMapper<SportInfo_AP, SportInfo> createMapper(
            DaoSession session) {
        return new SportInfoMapper();
    }

    @Override
    protected AbstractDao<SportInfo, Long> getDaoFrom(DaoSession session) {
        return session.getSportInfoDao();
    }

    @Override
    protected Long getPkFrom(SportInfo DBEntity) {
        return DBEntity.getId();
    }


    public SportInfo_AP getDailySportInfo(Date calendar) {
        return getDistinctEntityWhere(SportInfoDao.Properties.Calendar.eq(calendar), new WhereCondition[]{SportInfoDao.Properties.Timestyle.eq(SportInfo_AP.DAY_STATUSTIC)});
    }

    public boolean initInfoIfnoDataOf(Date calendar) {
        boolean ret = false;
        SportInfo_AP entity = getDailySportInfo(calendar);
        if (entity == null) {
            entity = new SportInfo_AP(calendar);
            entity.setTimeStyle(SportInfo_AP.DAY_STATUSTIC);
            //RepositoryManager.getInstance().getSportInfo().add(entity);
            ret = true;
        }

        return ret;
    }

    public List<SportInfo_AP> getInfoList(int timeStyle, Calendar sDate, Calendar eDate) {
        return getEntitiesWhereAnd(SportInfoDao.Properties.Timestyle.eq(timeStyle), new WhereCondition[]{SportInfoDao.Properties.Calendar.between(sDate.getTime(), eDate.getTime())});
    }

    public SportInfo_AP getSportInfo(int timeStyle, Calendar Date) {
        List<SportInfo_AP> list = getEntitiesWhereAnd(SportInfoDao.Properties.Timestyle.eq(timeStyle), new WhereCondition[]{SportInfoDao.Properties.Calendar.eq(Date.getTime())});
        if (list.size() == 0)
            return null;
        else
            return list.get(0);
    }

    @Override
    public void addAll(List<SportInfo_AP> appEntityList) {
        long startTime = Long.MAX_VALUE;
        long endTime = 0;

        int timeStyle = 0;
        for (SportInfo_AP entity : appEntityList) {
            timeStyle = entity.getTimeStyle();

            long date = entity.getCalendar().getTime();
            if (startTime > date)
                startTime = date;
            if (endTime < date)
                endTime = date;
        }
        getEntityDao()
                .queryBuilder()
                .where(SportInfoDao.Properties.Calendar.between(new Date(startTime), new Date(endTime)),
                        SportInfoDao.Properties.Timestyle.eq(timeStyle))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();

        super.addAll(appEntityList);
    }

    @Override
    protected void doAdd(SportInfo_AP APPEntity) {
        super.doAdd(APPEntity);
    }


    public SportInfo_AP getRealTimeInfo() {
        List<SportInfo_AP> list = getEntitiesWhereAnd(SportInfoDao.Properties.Timestyle.eq(SportInfo_AP.REAL_TIME), new WhereCondition[]{});
        if (list != null) {
            if (list.size() != 0)
                return list.get(0);
        }
        return null;
    }
}
