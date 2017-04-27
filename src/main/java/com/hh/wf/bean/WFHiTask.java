package com.hh.wf.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(name = "WF_HI_TASK")
public class WFHiTask extends WFBaseRuTask {
	private Date taskEndTime;
	
	private String managerOrgName;
	private int isEnd = 0;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TASK_END_TIME", length = 7)
	public Date getTaskEndTime() {
		return taskEndTime;
	}

	public void setTaskEndTime(Date taskEndTime) {
		this.taskEndTime = taskEndTime;
	}

	

	@Column(name = "MANAGER_ORG_NAME", length = 256)
	public String getManagerOrgName() {
		return this.managerOrgName;
	}

	public void setManagerOrgName(String managerOrgName) {
		this.managerOrgName = managerOrgName;
	}

	
	@Column(name = "IS_END")
	public int getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(int isEnd) {
		this.isEnd = isEnd;
	}
	
	
}
