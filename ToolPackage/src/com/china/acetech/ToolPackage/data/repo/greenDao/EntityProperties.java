package com.china.acetech.ToolPackage.data.repo.greenDao;

import de.greenrobot.dao.Property;

import java.util.Date;

public class EntityProperties {
    public static final Property EntityStatus;
    public static final Property Id = new Property(0, Long.class, "id", true, "_id");
    public static final Property ServerId = new Property(1, Long.class, "serverId", false, "SERVER_ID");
    public static final Property TimeCreated;
    public static final Property TimeUpdated;
    public static final Property Uuid = new Property(2, String.class, "uuid", false, "UUID");

    static {
        TimeCreated = new Property(3, Date.class, "timeCreated", false, "TIME_CREATED");
        TimeUpdated = new Property(4, Date.class, "timeUpdated", false, "TIME_UPDATED");
        EntityStatus = new Property(5, Integer.class, "entityStatus", false, "ENTITY_STATUS");
    }
}

/* Location:           H:\backupforG\Projects\FA_Jack\apk\dex2jar-0.0.9.15\classes-dex2jar.jar
 * Qualified Name:     com.fitbit.data.repo.greendao.EntityProperties
 * JD-Core Version:    0.5.4
 */