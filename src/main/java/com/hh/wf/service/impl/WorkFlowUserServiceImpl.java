package com.hh.wf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hh.system.util.BaseSystemUtil;
import com.hh.usersystem.bean.usersystem.UsUser;
import com.hh.usersystem.service.impl.LoginUserService;
import com.hh.usersystem.service.impl.UserService;
import com.hh.wf.service.inf.WFUserServiceInf;
import com.hh.wf.util.WFUser;

@Service
public class WorkFlowUserServiceImpl implements WFUserServiceInf {

	@Autowired
	private LoginUserService loginUserUtilService;

	@Autowired
	private UserService userService;

	@Override
	public String findLoginUserOrgId() {
		return findLoginUser().getOrgId();
	}

	@Override
	public List<WFUser> queryUserListByIds(List<String> useridList) {
		List<WFUser> WFUserList = new ArrayList<WFUser>();
		List<UsUser> hhXtYhList = userService.queryListByIds(useridList);
		for (UsUser hhXtYh : hhXtYhList) {
			WFUserList.add(gzUser(hhXtYh));
		}
		return WFUserList;
	}

	@Override
	public WFUser findLoginUser() {
		UsUser hhxtyh = loginUserUtilService.findLoginUser();
		WFUser wfUser = gzUser(hhxtyh);
		return wfUser;
	}

	private WFUser gzUser(UsUser hhxtyh) {
		WFUser wfUser = new WFUser();
		wfUser.setId(hhxtyh.getId());
		wfUser.setText(hhxtyh.getText());
		wfUser.setOrgId(hhxtyh.getOrgId());
		wfUser.setOrgText(hhxtyh.getOrgText());
		wfUser.setJobId(hhxtyh.getJobId());
		wfUser.setJobText(hhxtyh.getJobText());
		wfUser.setDeptId(hhxtyh.getDeptId());
		wfUser.setDeptText(hhxtyh.getDeptText());
		wfUser.setTheme(hhxtyh.getTheme());
		return wfUser;
	}

	@Override
	public String findLoginUserId() {
		return findLoginUser().getId();
	}

	public String getBaseDoctype() {
		return BaseSystemUtil.getBaseDoctype();
	}

	public String getBaseJs(String... args) {
		String theme = getTheme();
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("theme", theme);
		return BaseSystemUtil.getBaseJs(paramsMap, args);
	}

	public String getTheme() {
		WFUser user = findLoginUser();
		if (user == null) {
			return "base";
		}
		return user.getTheme();
	}

	@Override
	public List<UsUser> queryUserListByOrgId(String orgId) {
		return userService.queryListByOrganId(orgId);
	}

	@Override
	public List<UsUser> queryUserListByDeptId(String deptId) {
		return userService.queryListByDeptId(deptId);
	}

	@Override
	public List<UsUser> queryUserListByOrgIdAndRole(String orgId, String roleId) {
		return userService.queryUserListByOrgIdAndRole(orgId,roleId);
	}

	@Override
	public List<UsUser> queryUserListByDeptIdAndRole(String deptId, String roleId) {
		return userService.queryUserListByDeptIdAndRole(deptId, roleId);
	}

	@Override
	public List<UsUser> queryUserListByRole(String id) {
		return userService.queryUserListByRole(id);
	}

	@Override
	public List<UsUser> queryUserListByUserIds(List<String> useridList) {
		return  userService.queryListByIds(useridList);
	}

	@Override
	public List<UsUser> queryUserListBySysGroupId(String id) {
		return userService.queryUserListBySysGroupId(id);
	}

	@Override
	public List<UsUser> queryUserListByUsGroupId(String id) {
		return userService.queryUserListByUsGroupId(id);
	}

}
