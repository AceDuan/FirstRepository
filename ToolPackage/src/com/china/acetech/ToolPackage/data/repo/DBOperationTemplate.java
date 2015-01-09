package com.china.acetech.ToolPackage.data.repo;

import com.china.acetech.ToolPackage.data.domain.Entity;

import java.util.List;
import java.util.UUID;



public abstract interface DBOperationTemplate<T extends Entity>
{
  public abstract void add(T paramT);

  public abstract void addAll(List<T> paramList);

  public abstract void addListener(ag_Listener paramag);

  public abstract void clear(boolean paramBoolean);

  public abstract void delete(T paramT);

  public abstract void deleteAll(Iterable<T> paramIterable);

  public abstract List<T> get(Entity.EntityStatus[] paramArrayOfEntityStatus);

  public abstract List<T> getAll();

  public abstract T getById(long paramLong);

  public abstract T getByServerId(long paramLong);

  public abstract List<T> getByServerId(List<Long> paramList);

  public abstract T getByUUID(UUID paramUUID);

  public abstract String getName();

  public abstract List<T> getPendingEntries();

  public abstract void removeListener(ag_Listener paramag);

  public abstract void runInTransaction(Runnable paramRunnable);

  public abstract void save(T paramT);

  public abstract void saveAll(Iterable<T> paramIterable);
}


