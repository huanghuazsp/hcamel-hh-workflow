package com.hh.wf.service.inf;

import java.util.List;

import com.hh.usersystem.bean.usersystem.UsUser;
import com.hh.wf.util.WFUser;

public interface WFUserServiceInf {

	public String findLoginUserOrgId();

	public WFUser findLoginUser();

	public String findLoginUserId();

	public String getBaseDoctype();

	public String getBaseJs(String... args);

	public String getTheme();

	public List<WFUser> queryUserListByIds(List<String> useridList);

	public List<UsUser> queryUserListByOrgId(String orgId);

	public List<UsUser> queryUserListByDeptId(String deptId);

	public List<UsUser> queryUserListByOrgIdAndRole(String orgId, String roleId);

	public List<UsUser> queryUserListByDeptIdAndRole(String deptId, String roleId);

	public List<UsUser> queryUserListByRole(String id);
	
	public List<UsUser> queryUserListByUserIds(List<String> useridList);

	public List<UsUser> queryUserListBySysGroupId(String id);

	public List<UsUser> queryUserListByUsGroupId(String id);
}
