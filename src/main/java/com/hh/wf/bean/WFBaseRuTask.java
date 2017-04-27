package com.hh.wf.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@MappedSuperclass
public class WFBaseRuTask extends WFBaseHiProcinst {
	private String ownerId;
	private String ownerName;

	private String actId;
	private String actName;
	private String actType;
	private Date taskStartTime;
	private String piid;

	private String userId;

	private String userIds;

	private int read = 0;
	private String userNames;
	
	private String sendOrgName;

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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TASK_START_TIME", length = 7, updatable = false)
	public Date getTaskStartTime() {
		return taskStartTime;
	}

	public void setTaskStartTime(Date taskStartTime) {
		this.taskStartTime = taskStartTime;
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

	@Transient
	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	@Column(name = "ACT_TYPE", length = 36)
	public String getActType() {
		return actType;
	}

	public void setActType(String actType) {
		this.actType = actType;
	}

	@Transient
	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	@Lob
	@Column(name = "USER_NAMES")
	public String getUserNames() {
		return userNames;
	}

	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}
	
	@Column(name="SEND_ORG_NAME", length=256)
	public String getSendOrgName() {
		return this.sendOrgName;
	}

	public void setSendOrgName(String sendOrgName) {
		this.sendOrgName = sendOrgName;
	}

	
	private String userName;
	@Column(name = "USER_NAME", length = 64)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
