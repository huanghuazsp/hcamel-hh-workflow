package com.hh.wf.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(name = "WF_HI_PROCINST")
public class WFHiProcinst extends WFBaseHiProcinst {
	private Date piEndTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PI_END_TIME", length = 7)
	public Date getPiEndTime() {
		return piEndTime;
	}

	public void setPiEndTime(Date piEndTime) {
		this.piEndTime = piEndTime;
	}

}
