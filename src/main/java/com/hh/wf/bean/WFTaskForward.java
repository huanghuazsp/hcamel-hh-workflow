package com.hh.wf.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.transaction.annotation.Transactional;

import com.hh.hibernate.dao.inf.Order;
import com.hh.hibernate.util.base.BaseEntity;

@Order
@SuppressWarnings("serial")
@Entity
@Table(name = "WF_TASK_FORWARD")
public class WFTaskForward extends BaseEntity {
	private String ownerId;
	private String ownerName;
	private String piid;
	private String piName;
	
	private String actId;
	private String actName;

	private String userId;
	private String userName;
	
	private String taskId;
	
	private int isWithdraw;


	@Column(name = "USER_ID", length = 36)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "ACT_ID", length = 36)
	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	@Column(name = "ACT_NAME", length = 64)
	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}


	@Column(name = "OWNER_ID", length = 36)
	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	@Column(name = "OWNER_NAME", length = 36)
	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	@Column(name = "PIID", length = 36)
	public String getPiid() {
		return piid;
	}

	public void setPiid(String piid) {
		this.piid = piid;
	}

	@Column(name = "USER_NAME", length = 64)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name="PI_NAME",length=256)
	public String getPiName() {
		return piName;
	}

	public void setPiName(String piName) {
		this.piName = piName;
	}

	@Column(name="TASK_ID",length=36)
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Transient
	public int getIsWithdraw() {
		return isWithdraw;
	}

	public void setIsWithdraw(int isWithdraw) {
		this.isWithdraw = isWithdraw;
	}
	
	
}
