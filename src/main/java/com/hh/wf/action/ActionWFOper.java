package com.hh.wf.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.hh.hibernate.util.base.BaseEntitySimple;
import com.hh.system.service.impl.BaseService;
import com.hh.system.util.Check;
import com.hh.system.util.Convert;
import com.hh.system.util.MessageException;
import com.hh.system.util.base.BaseServiceAction;
import com.hh.wf.bean.WFRuTaskUser;
import com.hh.wf.service.impl.WFHiProcinstService;
import com.hh.wf.service.impl.WFOperService;

@SuppressWarnings("serial")
public class ActionWFOper extends BaseServiceAction<BaseEntitySimple> {
	private String wfconfigId;
	private String serviceId;
	private String taskId;
	private String wfDefId;
	private String piid;
	private String piName;
	private String actId;
	private String actName;
	private String workflowName;
	private String serviceMapKeys;
	
	private String userIds;
	private String userTexts;
	
	private String userConfig;
	
	private Map<String, String> userMap = new HashMap<String, String>();
	@Autowired
	private WFOperService wfOperService;
	@Autowired
	private WFHiProcinstService wFHiProcinstService;
	public BaseService<BaseEntitySimple> getService() {
		return wfOperService;
	}

	public Object start() {
		Map<String, Object> serviceMap = generateService();
		return wfOperService.startByObject(wfconfigId, workflowName,
				serviceMap);
	}

	private Map<String, Object> generateService() {
		Map<String, Object> serviceMap = new HashMap<String, Object>();
		List<String> keyList = Convert.strToList(serviceMapKeys);
		for (String key : keyList) {
			if (Check.isNoEmpty(key)) {
				serviceMap.put(key, request.getParameter(key));
			}
		}
		return serviceMap;
	}
	
	public Object manager() {
		try {
			return wfOperService.manager(taskId);
		} catch (MessageException e) {
			return e;
		}
	}
	
	public Object queryListByTaskId() {
		return wfOperService.queryListByTaskId(taskId);
	}
	
	public Object showFormByPiid() {
		return wfOperService.showFormByPiid(piid);
	}
	
	public Object queryRuTaskPage() {
		return wfOperService.queryRuTaskPage(this.getPageRange());
	}
	
	public Object queryHiTaskPage() {
		return wfOperService.queryHiTaskPage(this.getPageRange());
	}
	
	public Object queryMyPIPage() {
		return wfOperService.queryMyPIPage(this.getPageRange());
	}
	
	public Object queryPIPage() {
		return wfOperService.queryPIPage(this.getPageRange());
	}
	
	
	
	public Object queryOwnerTaskPage() {
		return wfOperService.queryOwnerTaskPage(this.getPageRange());
	}
	
	
	
	public Object submit() {
		try {
			Map<String, Object> serviceMap = generateService();
			wfOperService.submit(taskId, userMap,serviceMap);
		} catch (MessageException e) {
			return e;
		}
		return null;
	}
	public Object jointlySignSubmit() {
		List<WFRuTaskUser> userList = wfOperService.jointlySignSubmit(taskId);
		return userList;
	}
	
	
	
	public Object addUsers() {
		try {
			wfOperService.addUsers(wfDefId,piid,piName,actName,taskId, userIds,userTexts);
			return null;
		} catch (MessageException e) {
			return e;
		}
	}
	public Object removeUsers() {
		try {
			wfOperService.removeUsers(taskId, userIds);
			return null;
		} catch (MessageException e) {
			return e;
		}
	}
	
	public Object taskForward() {
		try {
			wfOperService.taskForward(wfDefId,piid,piName,actId, actName,taskId, userIds,userTexts);
			return null;
		} catch (MessageException e) {
			return e;
		}
	}
	
	public Object withdraw() {
		try {
			wfOperService.withdraw(object.getId());
			return null;
		} catch (MessageException e) {
			return e;
		}
	}
	
	public Object queryUserListByUserConfig(){
		return wfOperService.queryUserListByUserConfig(userConfig);
	}
	
	
	
	public void deletePiByIds() {
		wFHiProcinstService.deletePiByIds(this.getIds());
	}
	
	
	

	public String getUserConfig() {
		return userConfig;
	}

	public void setUserConfig(String userConfig) {
		this.userConfig = userConfig;
	}

	public String getWfconfigId() {
		return wfconfigId;
	}

	public void setWfconfigId(String wfconfigId) {
		this.wfconfigId = wfconfigId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getServiceMapKeys() {
		return serviceMapKeys;
	}

	public void setServiceMapKeys(String serviceMapKeys) {
		this.serviceMapKeys = serviceMapKeys;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Map<String, String> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<String, String> userMap) {
		this.userMap = userMap;
	}

	public String getPiid() {
		return piid;
	}

	public void setPiid(String piid) {
		this.piid = piid;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getUserTexts() {
		return userTexts;
	}

	public void setUserTexts(String userTexts) {
		this.userTexts = userTexts;
	}

	public String getPiName() {
		return piName;
	}

	public void setPiName(String piName) {
		this.piName = piName;
	}

	public String getActName() {
		return actName;
	}

	public void setActName(String actName) {
		this.actName = actName;
	}

	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	public String getWfDefId() {
		return wfDefId;
	}

	public void setWfDefId(String wfDefId) {
		this.wfDefId = wfDefId;
	}
	
	

}
