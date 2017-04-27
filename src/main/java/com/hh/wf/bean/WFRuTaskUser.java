package com.hh.wf.bean;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

import com.hh.hibernate.util.base.BaseEntitySimple;

@Entity(name = "WF_RU_TASK_USER")
@Table(indexes = { @Index(name = "WF_RU_TASK_USER_INDEX_USER_ID_TASK_ID", columnNames = { "USER_ID", "TASK_ID" }) }, appliesTo = "WF_RU_TASK_USER")
public class WFRuTaskUser extends BaseEntitySimple {

	private String wfDefId;
	private String piid;
	private String taskId;
	private String userId;
	private String userName;
	private int read;
	private int isSubmit;

	@Column(name = "USER_ID", length = 36)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "USER_NAME", length = 128)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "READ_")
	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	@Column(name = "TASK_ID", length = 36)
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Column(name = "PIID", length = 36)
	public String getPiid() {
		return piid;
	}

	public void setPiid(String piid) {
		this.piid = piid;
	}

	
	@Column(name = "IS_SUBMIT_",columnDefinition = "int default 0")
	public int getIsSubmit() {
		return isSubmit;
	}

	public void setIsSubmit(int isSubmit) {
		this.isSubmit = isSubmit;
	}
	
	@Column(name="WFDEF_ID",length=36)
	public String getWfDefId() {
		return wfDefId;
	}

	public void setWfDefId(String wfDefId) {
		this.wfDefId = wfDefId;
	}
}
