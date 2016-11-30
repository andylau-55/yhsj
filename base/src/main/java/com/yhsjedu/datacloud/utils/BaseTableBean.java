package com.yhsjedu.datacloud.utils;

import java.util.Date;

public abstract class BaseTableBean {

	public abstract Date getCreateTime();

	public abstract void setCreateTime(Date createTime);

	public abstract void setModifyTime(Date modifyTime);

	public void initDate() {
		if (CommonUtil.isEmpty(getCreateTime())) {
			setCreateTime(DateUtility.getSystemDate());
		}
		setModifyTime(DateUtility.getSystemDate());
	}
}
