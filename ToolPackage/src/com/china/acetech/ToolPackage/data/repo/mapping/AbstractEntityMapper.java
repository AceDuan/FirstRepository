package com.china.acetech.ToolPackage.data.repo.mapping;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEntityMapper<TEntity, TDBEntity>
        implements EntityMapper<TEntity, TDBEntity> {
    public List<TEntity> fromDbEntities(List<TDBEntity> paramList) {
        ArrayList<TEntity> localArrayList = new ArrayList<TEntity>();
//  Iterator<TDBEntity> localIterator = paramList.iterator();
//  while (localIterator.hasNext())
//    localArrayList.add(fromDbEntity(localIterator.next()));

        for (TDBEntity entity : paramList) {
            localArrayList.add(fromDbEntity(entity));
        }
        return localArrayList;
    }

    public List<TDBEntity> toDbEntitites(List<TEntity> paramList) {
        ArrayList<TDBEntity> localArrayList = new ArrayList<TDBEntity>();
//        Iterator<TEntity> localIterator = paramList.iterator();
//        while (localIterator.hasNext())
//            localArrayList.add(toDbEntity(localIterator.next()));

        for (TEntity entity : paramList) {
            localArrayList.add(toDbEntity(entity));
        }
        return localArrayList;
    }
}
