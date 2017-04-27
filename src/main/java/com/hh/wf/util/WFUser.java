package com.hh.wf.util;

import com.hh.system.util.Check;
import com.hh.usersystem.IUser;

public class WFUser implements IUser {
	private String id;
	private String text;
	private String orgId;
	private String orgText;
	private String jobId;
	private String jobText;
	private String deptId;
	private String deptText;
	private String theme;

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getOrgText() {
		return orgText;
	}

	public void setOrgText(String orgText) {
		this.orgText = orgText;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobText() {
		return jobText;
	}

	public void setJobText(String jobText) {
		this.jobText = jobText;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptText() {
		return deptText;
	}

	public void setDeptText(String deptText) {
		this.deptText = deptText;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getOrgDeptText() {
		if (Check.isNoEmpty(getOrgText()) && Check.isNoEmpty(getDeptText())) {
			return getOrgText() + "【" + getDeptText() + "】";
		} else if (Check.isNoEmpty(getOrgText())) {
			return getOrgText();
		} else if (Check.isNoEmpty(getDeptText())) {
			return getDeptText();
		}
		return "";
	}

}
