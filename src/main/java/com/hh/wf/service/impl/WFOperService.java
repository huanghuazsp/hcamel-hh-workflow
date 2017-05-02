package com.hh.wf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hh.form.service.impl.MongoFormOperService;
import com.hh.hibernate.util.base.BaseEntitySimple;
import com.hh.message.service.SysMessageService;
import com.hh.system.service.impl.BaseService;
import com.hh.system.util.BeanUtils;
import com.hh.system.util.Check;
import com.hh.system.util.Convert;
import com.hh.system.util.Json;
import com.hh.system.util.MessageException;
import com.hh.system.util.date.DateFormat;
import com.hh.system.util.dto.PageRange;
import com.hh.system.util.dto.PagingData;
import com.hh.system.util.dto.ParamFactory;
import com.hh.system.util.pk.PrimaryKey;
import com.hh.usersystem.bean.usersystem.UsUser;
import com.hh.usersystem.service.impl.UsLeaderService;
import com.hh.wf.bean.WFDef;
import com.hh.wf.bean.WFHiProcinst;
import com.hh.wf.bean.WFHiTask;
import com.hh.wf.bean.WFRuTask;
import com.hh.wf.bean.WFRuTaskUser;
import com.hh.wf.bean.WFTaskForward;
import com.hh.wf.service.inf.WFUserServiceInf;
import com.hh.wf.util.NodeMap;
import com.hh.wf.util.WFUser;

@Service
public class WFOperService extends BaseService<BaseEntitySimple> {
	@Autowired
	private WFUserServiceInf userService;
	@Autowired
	private WFDefService wfDefService;
	@Autowired
	private MongoFormOperService mongoFormOperService;
	@Autowired
	private WFRuTaskService wFRuTaskService;
	@Autowired
	private WFHiTaskService wFHiTaskService;
	@Autowired
	private WFHiProcinstService wFHiProcinstService;

	@Autowired
	private WFRuTaskUserService wFRuTaskUserService;

	@Autowired
	private WFTaskForwardService wFTaskForwardService;

	@Autowired
	private SysMessageService sysMessageService;

	@Autowired
	private UsLeaderService usLeaderService;

	public WFDefService getWfDefService() {
		return wfDefService;
	}

	public void setWfDefService(WFDefService wfDefService) {
		this.wfDefService = wfDefService;
	}

	@Transactional
	public Map<String, Object> start(String wfconfigId, String serviceId, String workflowName,
			Map<String, Object> serviceMap) {
		WFDef wfDef = wfDefService.maxByWFId(wfconfigId);
		Map<String, NodeMap> nodeMap = wfDef.getNodeMap();
		if (workflowName == null) {
			WFUser wfUser = userService.findLoginUser();
			workflowName = wfUser.getText() + " " + nodeMap.get("workflow").getText() + " "
					+ DateFormat.getDate(DateFormat.dateTimeFormat);
		}

		WFHiProcinst wfHiProcinst = wFHiProcinstService.findObjectByProperty("serviceId", serviceId);
		WFRuTask wFRuTask = null;
		if (wfHiProcinst == null) {
			wfHiProcinst = insertPi(wfDef.getId(), serviceId, workflowName, serviceMap);
			wFRuTask = insertRuTask(wfHiProcinst, serviceMap, nodeMap.get("startUserTask"), 1);
		} else {
			wfHiProcinst.setServiceData(Json.toStr(serviceMap));
			wFRuTask = wFRuTaskService.findObjectByProperty("piid", wfHiProcinst.getId());
		}

		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("pi", wfHiProcinst);
		returnMap.put("task", wFRuTask);
		nodeMap.put("currnode", nodeMap.get(wFRuTask.getActId()));
		returnMap.put("nodeMap", nodeMap);
		return returnMap;
	}

	@Transactional
	public Map<String, Object> startByObject(String wfconfigId, String workflowName, Map<String, Object> serviceMap) {
		String serviceId = PrimaryKey.getUUID();
		if (Check.isNoEmpty(serviceMap.get("id"))) {
			serviceId = serviceMap.get("id").toString();
		}
		Map<String, Object> returnMap = start(wfconfigId, serviceId, workflowName, serviceMap);
		// 业务保存
		serviceMap.put("id", serviceId);
		if (serviceMap != null && Check.isNoEmpty(serviceMap.get("tableName"))) {
			mongoFormOperService.save(serviceMap);
		}
		return returnMap;
	}

	@Transactional
	private WFHiProcinst insertPi(String defId, String serviceId, String workflowName, Map<String, Object> serviceMap) {
		WFUser wfUser = userService.findLoginUser();
		WFHiProcinst wfHiProcinst = new WFHiProcinst();
		wfHiProcinst.setServiceId(serviceId);
		wfHiProcinst.setWfDefId(defId);
		wfHiProcinst.setPiStartTime(new Date());
		wfHiProcinst.setStartUserId(wfUser.getId());
		wfHiProcinst.setStartUserName(wfUser.getText());
		wfHiProcinst.setPiName(workflowName);
		wfHiProcinst.setServiceData(Json.toStr(serviceMap));
		wFHiProcinstService.createEntity(wfHiProcinst);
		return wfHiProcinst;
	}

	@Transactional
	private WFRuTask insertRuTask(WFRuTask wfRuTask, Map<String, Object> serviceMap, NodeMap nodeMap,
			List<WFUser> wfUsers) {
		WFUser wfUser = userService.findLoginUser();
		WFRuTask wFRuTask = new WFRuTask();
		BeanUtils.copyProperties(wFRuTask, wfRuTask);
		BeanUtils.defautlPropertiesSetNull(wFRuTask);
		wFRuTask.setOwnerId(wfUser.getId());
		wFRuTask.setOwnerName(wfUser.getText());
		wFRuTask.setTaskStartTime(new Date());
		wFRuTask.setPiid(wfRuTask.getPiid());
		wFRuTask.setActId(nodeMap.getId());
		wFRuTask.setActType(nodeMap.getType());
		wFRuTask.setActName(nodeMap.getText());
		StringBuffer userIds = new StringBuffer();
		StringBuffer userNames = new StringBuffer();
		for (WFUser wfUser2 : wfUsers) {
			userIds.append(wfUser2.getId() + ",");
			userNames.append(wfUser2.getText() + ",");
		}
		wFRuTask.setUserIds(userIds.substring(0, userIds.length() - 1));
		wFRuTask.setUserNames(userNames.substring(0, userNames.length() - 1));
		wFRuTask.setUserId(wfUsers.size() == 1 ? wFRuTask.getUserIds() : "");
		wFRuTask.setUserName(wfUsers.size() == 1 ? wFRuTask.getUserNames() : "");
		wFRuTaskService.createTask(wFRuTask, serviceMap, nodeMap);
		return wFRuTask;
	}

	@Transactional
	public WFRuTask insertRuTask(WFHiProcinst wfHiProcinst, Map<String, Object> serviceMap, NodeMap nodeMap,
			int isRead) {
		WFUser wfUser = userService.findLoginUser();
		WFRuTask wFRuTask = new WFRuTask();
		BeanUtils.copyProperties(wFRuTask, wfHiProcinst);
		BeanUtils.defautlPropertiesSetNull(wFRuTask);
		wFRuTask.setOwnerId(wfUser.getId());
		wFRuTask.setOwnerName(wfUser.getText());
		wFRuTask.setUserId(wfUser.getId());
		wFRuTask.setUserName(wfUser.getText());
		wFRuTask.setTaskStartTime(new Date());
		wFRuTask.setPiid(wfHiProcinst.getId());
		wFRuTask.setActId(nodeMap.getId());
		wFRuTask.setActType(nodeMap.getType());
		wFRuTask.setActName(nodeMap.getText());
		wFRuTask.setUserIds(wfUser.getId());
		wFRuTask.setUserNames(wfUser.getText());
		wFRuTask.setRead(isRead);
		wFRuTaskService.createTask(wFRuTask, serviceMap, nodeMap);
		return wFRuTask;
	}

	public PagingData<WFHiProcinst> queryMyPIPage(PageRange pageRange) {
		return wFHiProcinstService.queryPagingData(pageRange,
				ParamFactory.getParamHb().is("startUserId", userService.findLoginUserId()).orderDesc("order"));
	}

	public PagingData<WFHiProcinst> queryPIPage(PageRange pageRange) {
		return wFHiProcinstService.queryPagingData(pageRange, ParamFactory.getParamHb().orderDesc("order"));
	}

	public PagingData<WFHiTask> queryHiTaskPage(PageRange pageRange) {
		WFUser wfUser = userService.findLoginUser();
		PagingData<WFHiTask> wfruPagingData = wFHiTaskService.queryPagingData(pageRange,
				ParamFactory.getParamHb().is("userId", wfUser.getId()).is("isEnd", 1).orderDesc("order"));
		return wfruPagingData;
	}

	public PagingData<Map<String, Object>> queryRuTaskPage(PageRange pageRange) {
		String userId = userService.findLoginUserId();

		String hql = "select a.id as id ,b.read as read,b.isSubmit as isSubmit,a.taskStartTime as taskStartTime,a.piName as piName,a.ownerName as ownerName,a.startUserName as startUserName,a.actName as actName,a.piid as piid from "
				+ WFRuTask.class.getName() + " a , " + WFRuTaskUser.class.getName()
				+ " b  where a.id=b.taskId and b.userId=:userId ";
		String hqlCount = "select count(b) from " + WFRuTask.class.getName() + " a , " + WFRuTaskUser.class.getName()
				+ " b  where a.id=b.taskId and b.userId=:userId";

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		String whereSql = "";
		// if (Check.isNoEmpty(object.getTitle())) {
		// paramMap.put("title","%"+ object.getTitle()+"%");
		// whereSql+=" and a.title like :title ";
		// }

		PagingData<Map<String, Object>> page = dao.queryPagingDataByHql(hql + whereSql + " ORDER BY b.updateTime DESC",
				hqlCount + whereSql, paramMap, pageRange);

		return page;
	}

	public List<WFHiTask> queryHiTaskListByPiid(String piid) {
		return wFHiTaskService.queryList(ParamFactory.getParamHb().is("piid", piid).orderDesc("taskStartTime"));
	}

	public List<WFRuTask> queryRuTaskListByPiid(String piid) {
		return wFRuTaskService.queryList(ParamFactory.getParamHb().is("piid", piid).orderDesc("taskStartTime"));
	}

	@Transactional
	public Map<String, Object> manager(String taskId) {
		WFUser wfUser = userService.findLoginUser();
		WFRuTask wfRuTask = wFRuTaskService.findObjectById(taskId);
		if (wfRuTask == null) {
			throw new MessageException("任务已经办理或任务不存在。");
		}

		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("userId", wfUser.getId());
		paramsMap.put("userName", wfUser.getText());
		paramsMap.put("taskId", taskId);
		dao.updateEntity(
				"update " + WFRuTaskUser.class.getName() + " set read=1 where userId=:userId and taskId=:taskId ",
				paramsMap);

		List<WFRuTaskUser> wfRuTaskUserList = queryListByTaskId(taskId);
		boolean asManager = false;
		for (WFRuTaskUser wfRuTaskUser : wfRuTaskUserList) {
			if (wfUser.getId().equals(wfRuTaskUser.getUserId())) {
				asManager = true;
			}
		}
		if (asManager == false) {
			throw new MessageException("您不是任务的班里人，不能办理任务。");
		}

		if (Check.isEmpty(wfRuTask.getUserId()) || Check.isEmpty(wfRuTask.getUserName())) {
			dao.updateEntity(
					"update " + WFRuTask.class.getName() + " set  userId=:userId,userName=:userName where id=:taskId ",
					paramsMap);
			dao.updateEntity(
					"update " + WFHiTask.class.getName() + " set  userId=:userId,userName=:userName where id=:taskId ",
					paramsMap);
		}

		Map<String, NodeMap> map = wfDefService.findObjectById(wfRuTask.getWfDefId()).getNodeMap();
		map.put("currnode", map.get(wfRuTask.getActId()));
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("task", wfRuTask);
		returnMap.put("nodeMap", map);

		returnMap.put("userList", wfRuTaskUserList);
		return returnMap;
	}

	public Object showFormByPiid(String piid) {
		WFHiProcinst wfHiProcinst = wFHiProcinstService.findObjectById(piid);
		WFDef wfDef = wfDefService.findObjectById(wfHiProcinst.getWfDefId());
		Map<String, NodeMap> nodeMap = wfDef.getNodeMap();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("hipi", wfHiProcinst);
		returnMap.put("nodeMap", nodeMap);
		return returnMap;
	}

	@Transactional
	public void submit(String taskId, Map<String, String> userMap, Map<String, Object> serviceMap)
			throws MessageException {
		WFRuTask wfRuTask = wFRuTaskService.findObjectById(taskId);
		WFDef wfDef = wfDefService.findObjectById(wfRuTask.getWfDefId());
		Map<String, NodeMap> nodeMap = wfDef.getNodeMap();

		NodeMap currNodeMap = nodeMap.get(wfRuTask.getActId());

		// 插入已办
		wFHiTaskService.updateEndHiTask((NodeMap) nodeMap.get(wfRuTask.getActId()), wfRuTask, serviceMap);

		Set<String> keySet = userMap.keySet();
		structureUserMap(currNodeMap, userMap);
		List<String> endActIdList = new ArrayList<String>();
		for (String actId : keySet) {
			String userids = userMap.get(actId);
			if (Check.isNoEmpty(userids)) {
				List<Map<String, Object>> userMapList = Json.toMapList(userids);
				List<WFUser> wfUsers = new ArrayList<WFUser>();
				for (Map<String, Object> map : userMapList) {
					WFUser wFUser = new WFUser();
					wFUser.setId(Convert.toString(map.get("id")));
					wFUser.setText(Convert.toString(map.get("text")));
					wfUsers.add(wFUser);
				}
				WFRuTask wFRuTask = insertRuTask(wfRuTask, serviceMap, nodeMap.get(actId), wfUsers);
			} else {
				if ("endEvent".equals(nodeMap.get(actId).getType())
						|| "endEventError".equals(nodeMap.get(actId).getType())) {
					endActIdList.add(actId);
					wFHiTaskService.insertHiTask(wfRuTask, nodeMap.get(actId), serviceMap, 1);
				}
			}
		}
		// 删除待办
		wFRuTaskService.deleteTask(taskId);
		if (endActIdList.size() > 0) {
			wFHiProcinstService.checkEndPi(wfRuTask.getPiid(), nodeMap, endActIdList);
		}
	}

	private void structureUserMap(NodeMap currNodeMap, Map<String, String> userMap) {
		if (userMap.size() == 0) {
			List<String> nextNodeMaps = currNodeMap.getNextNodeList();
			for (String nodeMap2 : nextNodeMaps) {
				userMap.put(nodeMap2, null);
			}
		}
	}

	public PagingData<WFHiTask> queryOwnerTaskPage(PageRange pageRange) {
		WFUser wfUser = userService.findLoginUser();
		PagingData<WFHiTask> wfruPagingData = wFHiTaskService.queryPagingData(pageRange,
				ParamFactory.getParamHb().is("ownerId", wfUser.getId()).orderDesc("order"));
		return wfruPagingData;
	}

	@Transactional
	public void addUsers(String wfDefId, String piid, String piName, String actName, String taskId, String userIds,
			String userTexts) {

		Map<String, WFRuTaskUser> wfRuTaskUserMap = wFRuTaskUserService.queryTaskUserMapByTaskId(taskId);
		StringBuffer infoMessage = new StringBuffer();
		List<String> userIdList = Convert.strToList(userIds);
		for (String string : userIdList) {
			WFRuTaskUser WFRuTaskUser = wfRuTaskUserMap.get(string);
			if (WFRuTaskUser != null) {
				infoMessage.append(WFRuTaskUser.getUserName() + ",");
			}
		}
		if (Check.isNoEmpty(infoMessage)) {
			infoMessage.append("已经是任务的经办人,请重新选择。");
			throw new MessageException(infoMessage.toString());
		}

		wFRuTaskUserService.addUserByTask(wfDefId, piid, piName, actName, taskId, userIds, userTexts);

		updateUserNames(taskId);
	}

	private void updateUserNames(String taskId) {
		List<WFRuTaskUser> wfRuTaskUserList = queryListByTaskId(taskId);
		StringBuffer userTextsStr = new StringBuffer();
		for (WFRuTaskUser wfRuTaskUser : wfRuTaskUserList) {
			userTextsStr.append(wfRuTaskUser.getUserName() + ",");
		}
		if (userTextsStr.length() > 0) {
			wFRuTaskService.update(taskId, "userNames", userTextsStr.substring(0, userTextsStr.length() - 1));
			wFHiTaskService.update(taskId, "userNames", userTextsStr.substring(0, userTextsStr.length() - 1));
		} else {
			wFRuTaskService.update(taskId, "userNames", "");
			wFHiTaskService.update(taskId, "userNames", "");
		}

	}

	public List<WFRuTaskUser> queryListByTaskId(String taskId) {
		return wFRuTaskUserService.queryList(ParamFactory.getParamHb().is("taskId", taskId).orderDesc("updateTime"));
	}

	@Transactional
	public void removeUsers(String taskId, String userIds) {
		wFRuTaskUserService.removeUsers(taskId, userIds);
		updateUserNames(taskId);
	}

	@Transactional
	public void taskForward(String wfDefId, String piid, String piName, String actId, String actName, String taskId,
			String userIds, String userTexts) {
		WFUser wfUser = userService.findLoginUser();
		WFTaskForward entity = new WFTaskForward();
		entity.setActId(actId);
		entity.setActName(actName);
		entity.setTaskId(taskId);
		entity.setPiid(piid);
		entity.setPiName(piName);
		entity.setOwnerId(wfUser.getId());
		entity.setOwnerName(wfUser.getText());
		entity.setUserId(userIds);
		entity.setUserName(userTexts);
		wFTaskForwardService.save(entity);

		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("userId", entity.getOwnerId());
		paramsMap.put("userName", entity.getOwnerName());

		paramsMap.put("toUserId", entity.getUserId());
		paramsMap.put("toUserName", entity.getUserName());

		paramsMap.put("taskId", taskId);
		dao.updateEntity("update " + WFRuTaskUser.class.getName() + " "
				+ " set read=0,isSubmit=0,userId=:toUserId,userName=:toUserName "
				+ " where userId=:userId and taskId=:taskId", paramsMap);

		dao.updateEntity("update " + WFRuTask.class.getName() + " set "
				+ " userId=:toUserId,userName=:toUserName where id=:taskId", paramsMap);
		dao.updateEntity("update " + WFHiTask.class.getName() + " set "
				+ " userId=:toUserId,userName=:toUserName where id=:taskId", paramsMap);

		updateUserNames(taskId);

		WFRuTaskUser sysEmailUser = new WFRuTaskUser();
		sysEmailUser.setTaskId(taskId);
		sysEmailUser.setWfDefId(wfDefId);
		sysEmailUser.setPiid(piid);
		sysEmailUser.setUserId(entity.getUserId());
		sysEmailUser.setUserName(entity.getUserName());
		wFRuTaskUserService.addMessage("任务转发", piName, actName, taskId, sysEmailUser);
	}

	public void withdraw(String id) {
		WFTaskForward wfTaskForward = wFTaskForwardService.findObjectById(id);
		WFUser wfUser = userService.findLoginUser();

		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("userId", wfTaskForward.getUserId());
		paramsMap.put("userName", wfTaskForward.getUserName());

		paramsMap.put("toUserId", wfUser.getId());
		paramsMap.put("toUserName", wfUser.getText());

		paramsMap.put("taskId", wfTaskForward.getTaskId());
		dao.updateEntity("update " + WFRuTaskUser.class.getName() + " "
				+ " set read=0,isSubmit=0,userId=:toUserId,userName=:toUserName "
				+ " where userId=:userId and taskId=:taskId", paramsMap);

		dao.updateEntity("update " + WFRuTask.class.getName() + " set "
				+ " userId=:toUserId,userName=:toUserName where id=:taskId", paramsMap);
		dao.updateEntity("update " + WFHiTask.class.getName() + " set "
				+ " userId=:toUserId,userName=:toUserName where id=:taskId", paramsMap);

		updateUserNames(wfTaskForward.getTaskId());

		wFTaskForwardService.deleteByIds(id);

		sysMessageService.deleteByParamsAndToUserId(wfTaskForward.getTaskId(), wfTaskForward.getUserId());
	}

	public List<Map<String, Object>> queryUserListByUserConfig(String userConfig) {
		WFUser wfUser = userService.findLoginUser();
		List<Map<String, Object>> userConfigList = Json.toMapList(userConfig);
		List<Map<String, Object>> userMapList = new ArrayList<Map<String, Object>>();
		int i = 0;
		for (Map<String, Object> map : userConfigList) {
			String type = Convert.toString(map.get("type"));
			String id = Convert.toString(map.get("id"));
			
			String roleId = Convert.toString(map.get("roleId"));
			
			if (i == 0) {
				map.put("cond", "or");
			}
			List<UsUser> userList = new ArrayList<UsUser>();
			if ("custom".equals(type)) {
				if ("currManager".equals(id)) {
					userList = usLeaderService.queryLeaderList(wfUser.getId());
				} else if ("currOrgManager".equals(id)) {
					userList = usLeaderService.queryLeaderList(wfUser.getOrgId());
				} else if ("currDeptManager".equals(id)) {
					userList = usLeaderService.queryLeaderList(wfUser.getDeptId());
				} else if ("currManagerUser".equals(id)) {
					userList = usLeaderService.queryManagerList(wfUser.getId());
				} else if ("currOrg".equals(id)) {
					if (Check.isEmpty(roleId)) {
						userList = userService.queryUserListByOrgId(wfUser.getOrgId());
					}else{
						userList = userService.queryUserListByOrgIdAndRole(wfUser.getOrgId(),roleId);
					}
				} else if ("currDept".equals(id)) {
					if (Check.isEmpty(roleId)) {
						userList = userService.queryUserListByDeptId(wfUser.getDeptId());
					}else{
						userList = userService.queryUserListByDeptIdAndRole(wfUser.getDeptId(),roleId);
					}
				}

			}else if ("org".equals(type)) {
				if (Check.isEmpty(roleId)) {
					userList = userService.queryUserListByOrgId(id);
				}else{
					userList = userService.queryUserListByOrgIdAndRole(id,roleId);
				}
			}else if ("dept".equals(type)) {
				if (Check.isEmpty(roleId)) {
					userList = userService.queryUserListByDeptId(id);
				}else{
					userList = userService.queryUserListByDeptIdAndRole(id,roleId);
				}
			}else if ("role".equals(type)) {
				userList = userService.queryUserListByRole(id);
			}else if ("sysgroup".equals(type)) {
				userList = userService.queryUserListBySysGroupId(id);
			}else if ("usgroup".equals(type)) {
				userList = userService.queryUserListByUsGroupId(id);
			}else if ("user".equals(type)) {
				userList = userService.queryUserListByUserIds(Convert.strToList(id));
			}
			userMapList = convertUserList(userMapList, userList, Convert.toString(map.get("cond")));
			i++;
		}
		return userMapList;
	}

	public List<Map<String, Object>> convertUserList(List<Map<String, Object>> userMapList, List<UsUser> userList,
			String cond) {

		if ("and".equals(cond)) {
			List<Map<String, Object>> returnUserMapList = new ArrayList<Map<String, Object>>();
			List<String> userIdList = new ArrayList<String>();
			for (UsUser user : userList) {
				userIdList.add(user.getId());
			}
			for (Map<String, Object> userMap : userMapList) {
				if (userIdList.contains(userMap.get("id"))) {
					returnUserMapList.add(userMap);
				}
			}
			userMapList = returnUserMapList;
		} else {
			for (UsUser user : userList) {
				Map<String, Object> userMap = new HashMap<String, Object>();
				userMap.put("id", user.getId());
				userMap.put("text", user.getText());
				userMapList.add(userMap);
			}
		}
		return userMapList;
	}

	public List<WFRuTaskUser> jointlySignSubmit(String taskId) {
		WFUser wfUser = userService.findLoginUser();
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("userId", wfUser.getId());
		paramsMap.put("userName", wfUser.getText());
		paramsMap.put("taskId", taskId);
		dao.updateEntity(
				"update " + WFRuTaskUser.class.getName() + " set isSubmit=1 where userId=:userId and taskId=:taskId ",
				paramsMap);
		
		return wFRuTaskUserService.queryList(ParamFactory.getParamHb().is("isSubmit", 0).is("taskId", taskId));
	}
}
