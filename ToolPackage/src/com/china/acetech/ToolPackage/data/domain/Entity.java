package com.china.acetech.ToolPackage.data.domain;


import com.china.acetech.ToolPackage.java.CalendarTool;
import com.china.acetech.ToolPackage.data.util.ClassDescription;
import com.china.acetech.ToolPackage.data.util.TempTimeZone;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public abstract class Entity implements ClassDescription, Cloneable {
	public static final int j = -1;
	private Long id;
	private long serverId = -1L;
	private EntityStatus status = EntityStatus.PENDING_OPERATION;
	private SyncListener syncListener;
	private Date timeCreated;
	private Date timeUpdated;
	private UUID uuid;

	/**
	 * 
	 * 这个函数用于设定更新时间，这里的处理是为了增加14小时？ 未知原因。
	 * @param string   给定的字串
	 * @param preTime		之前的时间
	 * @param currTime		当前时间
	 * @return 计算出来的时间
	 */
	protected static Date getRealUpdateTime(String string, Date preTime, Date currTime) {
		if ((preTime != null)
				&& (currTime != null)
				&& (((currTime.before(preTime)) || (currTime
						.equals(preTime))))) {
			Date date = CalendarTool.add(preTime, Calendar.HOUR, 14);
			StringBuilder builder = new StringBuilder();
			builder.append("hackTimeUpdated, current: ");
			builder.append(TempTimeZone.getFormatDateString(preTime));
			builder.append("; got ");
			builder.append(TempTimeZone.getFormatDateString(currTime));
			builder.append("; setting ");
			builder.append(TempTimeZone.getFormatDateString(date));
			// c.a(string, localStringBuilder.toString()); 這個是日誌信息記錄，暫時不管
			// logging.c
			currTime = date;
		}
		return currTime;
	}

	public void setEntityStatus(EntityStatus status) {
		this.status = status;
	}

	public void setSyncListener(SyncListener listener) {
		this.syncListener = listener;
	}

	public void setEntityID(Long entityId) {
		this.id = entityId;
	}

	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	public void setServerID(long serverId) {
		this.serverId = serverId;
	}

	public void setCreateTime(Date date) {
		this.timeCreated = date;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public void setUpdateTime(Date date) {
		this.timeUpdated = getRealUpdateTime(getEntityName(), this.timeUpdated, date);
	}

	public boolean equals(Object target) {

		if (this == target)
			return true;
		Entity temp;
		if (!(target instanceof Entity))
			return false;
		temp = (Entity) target;

		return uuid.equals(temp.uuid);
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public Date getCreateTime() {
		return this.timeCreated;
	}

	public Date getUpdateTime() {
		return this.timeUpdated;
	}

	public long getServerID() {
		return this.serverId;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("class: ");
		builder.append(getEntityName()); // 原代碼未發現此項
		builder.append(" id: ");
		builder.append(getEntityID());
		builder.append("/");
		builder.append(getServerID());
		builder.append(" status: ");
		builder.append(getEntityStatus());

		return builder.toString();
	}

	public EntityStatus getEntityStatus() {
		return this.status;
	}

	public Long getEntityID() {
		return this.id;
	}

	public boolean isEmptyID() {
		return this.id == null;
	}

	protected void sync() {
		if (this.syncListener == null)
			return;
		this.syncListener.sync();
	}

	public static enum EntityStatus implements CodeInterFace {
		SYNCED(0), PENDING_OPERATION(1), PENDING_DELETE(2);

		private final int code;

		@Override
		public int getCode() {
			return code;
		}

		private EntityStatus(int code) {
			this.code = code;
		}

	}

	/**
	 * @author BXC2010011
	 *这个是用来进行同步操作的，当entity有变化需要进行同步时，调用此接口启动同步机制。
	 */
	public static interface SyncListener {
		public void sync();
		
		public void others();
	}
	

}
