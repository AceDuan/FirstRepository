package com.china.acetech.ToolPackage.data.repo;

import com.china.acetech.ToolPackage.data.domain.Entity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


public abstract class OperationForEntity<T extends Entity>
        implements DBOperationTemplate<T> {
    private final List<WeakReference<ag_Listener>> listeners = new ArrayList<WeakReference<ag_Listener>>();

    public void add(T entity) {
        prepareEntity(entity);
        doAdd(entity);
        informListeners();
    }

    public void addAll(List<T> entityList) {
//        Iterator<T> localIterator = entityList.iterator();
//        while (localIterator.hasNext())
//            add(localIterator.next());

        for ( T entity : entityList ){
            add( entity );
        }
    }

    public void addListener(ag_Listener paramag) {
        synchronized (this.listeners) {
//            Iterator<WeakReference<ag_Listener>> localIterator = this.listeners.iterator();
//            while (localIterator.hasNext()) {
//                ag_Listener localag = (ag_Listener) (localIterator.next()).get();
//                if ((localag != null) && (localag.equals(paramag)))
//                    return;
//            }

            for ( WeakReference<ag_Listener> listener : this.listeners ){
                ag_Listener temp = listener.get();
                if ( ( temp != null ) && (temp.equals(paramag) ) )
                    return;
            }
            this.listeners.add(new WeakReference<ag_Listener>(paramag));
        }
    }

    public void deleteAll(Iterable<T> entityIterable) {
//        Iterator<T> localIterator = entityIterable.iterator();
//        while (localIterator.hasNext())
//            delete(localIterator.next());

        for ( T entity : entityIterable ){
            delete( entity );
        }
    }

    protected abstract void doAdd(T entity);

    public T getById(long paramLong) {
        throw new UnsupportedOperationException();
    }

//public List<T> getPendingEntries()
//{
//  return l.a(getAll(), new l.a()
//  {
//    public boolean a(T paramT)
//    {
//      return (paramT.u() == Entity.EntityStatus.PENDING_OPERATION) || (paramT.u() == Entity.EntityStatus.PENDING_DELETE);
//    }
//  });
//}

    public void informListeners() {
        while (true) {
            ag_Listener localag;
            synchronized (this.listeners) {
                Iterator<WeakReference<ag_Listener>> localIterator = this.listeners.iterator();
                if (!localIterator.hasNext())
                    return;
                localag = (localIterator.next()).get();
                if (localag == null)
                    localIterator.remove();
            }
            if (localag != null)
                localag.b(getName());
        }
    }

    protected void prepareEntity(T entity) {
        if (entity.getUUID() == null)
            entity.setUUID(UUID.randomUUID());
        Date localDate = new Date();
        if (entity.getCreateTime() == null)
            entity.setCreateTime(localDate);
        if (entity.getUpdateTime() != null)
            return;
        entity.setUpdateTime(localDate);
    }

    public void removeListener(ag_Listener paramag) {
        synchronized (this.listeners) {
            Iterator<WeakReference<ag_Listener>> localIterator = this.listeners.iterator();
            ag_Listener localag;
            do {
                if (!localIterator.hasNext())
                    return;
                localag = localIterator.next().get();
            }
            while ((localag != null) && (localag != paramag));
            localIterator.remove();
        }
    }

    public void saveAll(Iterable<T> entityIterable) {
//        Iterator<T> localIterator = entityIterable.iterator();
//        while (localIterator.hasNext())
//            save(localIterator.next());

        for ( T entity : entityIterable ){
            save(entity);
        }
    }
}