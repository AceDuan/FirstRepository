package com.china.acetech.ToolPackage.data.repo;


/**
 * 看起来应该是记录表的访问记录的listener。在所有更新操作中都会出现
 * 使用时传递参数只有表名
 * @author BXC2010011
 *
 */
public interface ag_Listener {
	public abstract void b(String paramString);
}
