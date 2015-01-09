package com.china.acetech.ToolPackage.data.repo.greenDao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


import com.china.acetech.ToolPackage.data.domain.Entity;
import com.china.acetech.ToolPackage.data.repo.OperationForEntity;
import com.china.acetech.ToolPackage.data.repo.mapping.EntityMapper;
import com.china.acetech.ToolPackage.data.util.ArrayOperation;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.query.WhereCondition;

public abstract class AbstractEntityGreenDaoRepository<TEntity extends Entity, TDBEntity> extends OperationForEntity<TEntity> {

    private AbstractDao<TDBEntity, Long> entityDao;
    private EntityMapper<TEntity, TDBEntity> mapper;

    public AbstractEntityGreenDaoRepository() {
        configure(DaoFactory.getInstance().getDaoSession());
    }

    private Iterable<TEntity> prepareAll(Iterable<TEntity> appIterable) {
//	    Iterator<TEntity> localIterator = appIterable.iterator();
//	    while (localIterator.hasNext())
//	      prepareEntity(localIterator.next());

        for (TEntity entity : appIterable) {
            prepareEntity(entity);
        }
        return appIterable;
    }

    public void addAll(List<TEntity> appEntityList) {
        if ((appEntityList == null) || (appEntityList.size() == 0))
            return;
        prepareAll(appEntityList);
        List<TDBEntity> localList = toDbEntities(appEntityList, false);
        this.entityDao.insertInTx(localList);
        for (int i = 0; i < localList.size(); ++i)
            ( appEntityList.get(i)).setEntityID(getPkFrom(localList.get(i)));
        informListeners();
    }

    public void clear(boolean paramBoolean) {
//        不知道用途，暫時封掉
//        if (paramBoolean) {
//
//            new DeleteQueryExt(this.entityDao.queryBuilder().where(EntityProperties.EntityStatus.eq(Integer.valueOf(Entity.EntityStatus.SYNCED.getCode())), new WhereCondition[0]).buildDelete()).executeDeleteAndClearCache();
//            return;
//        }
        this.entityDao.deleteAll();
    }

    public void configure(DaoSession session) {
        if (this.mapper == null)
            this.mapper = createMapper(session);
        this.entityDao = getDaoFrom(session);
    }

    protected abstract EntityMapper<TEntity, TDBEntity> createMapper(DaoSession session);

    public void delete(TEntity APPEntity) {
        if (APPEntity == null)
            return;
        this.entityDao.deleteByKey(APPEntity.getEntityID());
        informListeners();
    }

    protected void doAdd(TEntity APPEntity) {
        if (APPEntity == null)
            return;
        TDBEntity localObject = this.mapper.toDbEntity(APPEntity);
        APPEntity.setEntityID(this.entityDao.insert(localObject));
    }

    protected List<TEntity> fromDbEntities(List<TDBEntity> DBEntityList) {
        ArrayList<TEntity> localArrayList = new ArrayList<TEntity>();
//        Iterator<TDBEntity> localIterator = DBEntityList.iterator();
//        while (localIterator.hasNext()) {
//            TDBEntity localObject = localIterator.next();
//            localArrayList.add(this.mapper.fromDbEntity(localObject));
//        }

        for ( TDBEntity entity : DBEntityList ){
            localArrayList.add(this.mapper.fromDbEntity(entity));
        }
        return localArrayList;
    }

    public List<TEntity> get(Entity.EntityStatus[] entityStatus) {
        return getEntitiesWhereAnd(EntityProperties.EntityStatus.in((Object[]) ArrayOperation.code2Object(entityStatus)), new WhereCondition[0]);
    }

    public List<TEntity> getAll() {
        return fromDbEntities(this.entityDao.loadAll());
    }

    public TEntity getById(long entityId) {
        return this.mapper.fromDbEntity(this.entityDao.load(entityId));
    }

    public TEntity getByServerId(long serverID) {
        return getDistinctEntityWhere(EntityProperties.ServerId.eq(serverID), new WhereCondition[0]);
    }

    public List<TEntity> getByServerId(List<Long> IDList) {
        return getEntitiesWhereAnd(EntityProperties.ServerId.in(IDList), new WhereCondition[0]);
    }

    public TEntity getByUUID(UUID uuid) {
        return getDistinctEntityWhere(EntityProperties.Uuid.eq(uuid.toString()), new WhereCondition[0]);
    }

    protected abstract AbstractDao<TDBEntity, Long> getDaoFrom(DaoSession session);

    protected TEntity getDistinctEntityWhere(WhereCondition whereCondition, WhereCondition[] whereConditions) {
        TDBEntity localObject = this.entityDao.queryBuilder().where(whereCondition, whereConditions).unique();
        return this.mapper.fromDbEntity(localObject);
    }

    protected List<TEntity> getEntitiesWhereAnd(Property property, WhereCondition paramWhereCondition, WhereCondition[] paramArrayOfWhereCondition) {
        return fromDbEntities(this.entityDao.queryBuilder().where(paramWhereCondition, paramArrayOfWhereCondition).orderAsc(new Property[]{property}).build().list());
    }

    protected List<TEntity> getEntitiesWhereAnd(WhereCondition paramWhereCondition, WhereCondition[] paramArrayOfWhereCondition) {
        return fromDbEntities(this.entityDao.queryBuilder().where(paramWhereCondition, paramArrayOfWhereCondition).build().list());
    }

    public AbstractDao<TDBEntity, Long> getEntityDao() {
        return this.entityDao;
    }

    public EntityMapper<TEntity, TDBEntity> getMapper() {
        return this.mapper;
    }

    public String getName() {
        return this.entityDao.getTablename();
    }

    public List<TEntity> getPendingEntries() {
        return getEntitiesWhereAnd(EntityProperties.EntityStatus.notEq(Entity.EntityStatus.SYNCED.getCode()), new WhereCondition[0]);
    }

    protected abstract Long getPkFrom(TDBEntity DBEntity);

    public void runInTransaction(Runnable runnable) {
        this.entityDao.getSession().runInTx(runnable);
    }

    public void save(TEntity APPEntity) {
        if (APPEntity == null)
            return;
        prepareEntity(APPEntity);
        TDBEntity localObject = this.mapper.toDbEntity(APPEntity, this.entityDao.load(APPEntity.getEntityID()));
        this.entityDao.update(localObject);
        informListeners();
    }

    public void saveAll(Iterable<TEntity> appEntityList) {
        if ((appEntityList == null) || (!appEntityList.iterator().hasNext()))
            return;
        List<TDBEntity> localList = toDbEntities(prepareAll(appEntityList), true);
        this.entityDao.updateInTx(localList);
        informListeners();
    }

    protected List<TDBEntity> toDbEntities(Iterable<TEntity> appEntityIterable, boolean paramBoolean) {
        ArrayList<TDBEntity> localArrayList = new ArrayList<TDBEntity>();
//        Iterator<TEntity> localIterator = appEntityIterable.iterator();
//        while (localIterator.hasNext()) {
//            TEntity localEntity = localIterator.next();
//            localArrayList.add(this.mapper.toDbEntity(localEntity));
//        }

        for ( TEntity entity : appEntityIterable ){
            localArrayList.add(this.mapper.toDbEntity(entity) );
        }
        return localArrayList;
    }
}
