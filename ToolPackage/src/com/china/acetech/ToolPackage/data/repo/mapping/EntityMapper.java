package com.china.acetech.ToolPackage.data.repo.mapping;

public abstract interface EntityMapper<TEntity, TDBEntity> {
    public abstract TEntity fromDbEntity(TDBEntity DBEntity);

    public abstract TDBEntity toDbEntity(TEntity APPEntity);

    public abstract TDBEntity toDbEntity(TEntity APPEntity, TDBEntity DBEntity);
}
