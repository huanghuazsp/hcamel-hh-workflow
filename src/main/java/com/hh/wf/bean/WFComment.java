package com.hh.wf.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hh.hibernate.dao.inf.Order;
import com.hh.hibernate.util.base.BaseEntity;


@SuppressWarnings("serial")
@Order(sorts="asc")
@Entity
@Table(name = "WF_COMMENT")
public class WFComment extends BaseEntity {

	private String wfDefId;
	private String piid;
	private String taskId;
	private String actId;
	private String actName;
	private String comment;
	private String userName;
	private String userId;
	private Date approveTime;
	
	
	@Column(name="WFDEF_ID",length=36)
	public String getWfDefId() {
		return wfDefId;
	}

	public void setWfDefId(String wfDefId) {
		this.wfDefId = wfDefId;
	}

	@Column(name = "PIID", length = 36)
	public String getPiid() {
		return piid;
	}

	public void setPiid(String piid) {
		this.piid = piid;
	}

	@Column(name = "TASK_ID", length = 36)
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
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

	@Lob
	@Column(name="COMMENT_")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPROVE_TIME", length = 7, updatable = false)
	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}
	
	
}
