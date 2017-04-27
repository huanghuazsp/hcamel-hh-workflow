package com.hh.wf.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hh.hibernate.util.base.BaseEntity;

@SuppressWarnings("serial")
@MappedSuperclass
public class WFBaseHiProcinst extends BaseEntity {
	private String serviceId;
	private String wfDefId;
	private Date piStartTime;
	private String startUserId;
	private String startUserName;
	private String piName;
	private String serviceData;

	@Column(name="SERVICE_ID",length=36)
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	@Column(name="WFDEF_ID",length=36)
	public String getWfDefId() {
		return wfDefId;
	}

	public void setWfDefId(String wfDefId) {
		this.wfDefId = wfDefId;
	}

	@Column(name="START_USERID",length=36)
	public String getStartUserId() {
		return startUserId;
	}

	public void setStartUserId(String startUserId) {
		this.startUserId = startUserId;
	}

	@Column(name="START_USERNAME",length=36)
	public String getStartUserName() {
		return startUserName;
	}

	public void setStartUserName(String startUserName) {
		this.startUserName = startUserName;
	}

	@Lob
	@Column(name="SERVICE_DATA")
	public String getServiceData() {
		return serviceData;
	}

	public void setServiceData(String serviceData) {
		this.serviceData = serviceData;
	}

	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME", length = 7,updatable=false)
	public Date getPiStartTime() {
		return piStartTime;
	}

	public void setPiStartTime(Date piStartTime) {
		this.piStartTime = piStartTime;
	}

	@Column(name="PI_NAME",length=256)
	public String getPiName() {
		return piName;
	}

	public void setPiName(String piName) {
		this.piName = piName;
	}

	
}
