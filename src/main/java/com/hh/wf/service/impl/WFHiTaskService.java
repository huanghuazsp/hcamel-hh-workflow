package com.hh.wf.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hh.system.service.impl.BaseService;
import com.hh.system.util.BeanUtils;
import com.hh.wf.bean.WFHiProcinst;
import com.hh.wf.bean.WFHiTask;
import com.hh.wf.bean.WFRuTask;
import com.hh.wf.service.inf.WFUserServiceInf;
import com.hh.wf.util.NodeMap;
import com.hh.wf.util.WFUser;

@Service
public class WFHiTaskService extends BaseService<WFHiTask> {
	@Autowired
	private WFUserServiceInf loginService;
	
	@Autowired
	private WorkFlowUtilService workFlowUtilService;

	public void createTask(WFHiTask entity, NodeMap nodeMap, Map<String, Object> serviceMap) {
		createEntity(entity);
//		wfRuTaskUserService.addUserByTask(entity);
	}

	@Transactional
	public WFHiTask updateEndHiTask(NodeMap nodeMap, WFRuTask wFRuTask,Map<String, Object> serviceMap) {
		WFUser wfUser = loginService.findLoginUser();
		String userId = wfUser.getId();
		String userName = wfUser.getText();

		WFHiTask wFHiTask = new WFHiTask();
		BeanUtils.copyProperties(wFHiTask, wFRuTask);

		wFHiTask.setUserId(userId);
		wFHiTask.setUserName(userName);
		wFHiTask.setTaskEndTime(new Date());
		wFHiTask.setManagerOrgName(wfUser.getOrgDeptText());
		wFHiTask.setIsEnd(1);
		updateEntity(wFHiTask);
		
		Map<String, Object> workMap = new HashMap<String, Object>();
		workMap.put("ruTask", wFRuTask);
		workMap.put("hiTask", wFHiTask);
		
		List<Map<String, String>> setValueConfigMapList = nodeMap.getTaskEndSetValueConfig();
		workFlowUtilService.updateServiceValue(serviceMap, setValueConfigMapList);
		
		
		List<Map<String, String>> interfaceConfigMapList = nodeMap.getTaskEndInterfaceConfig();
		workFlowUtilService.execInf(workMap,serviceMap, interfaceConfigMapList);
		return wFHiTask;
	}
	
	@Transactional
	public WFHiTask insertHiTaskStart(WFHiProcinst wfHiProcinst, NodeMap nodeMap, Map<String, Object> serviceMap, int isRead) {
		WFUser wfUser = loginService.findLoginUser();
		WFHiTask wFRuTask = new WFHiTask();
		BeanUtils.copyProperties(wFRuTask, wfHiProcinst);
		BeanUtils.defautlPropertiesSetNull(wFRuTask);
		wFRuTask.setOwnerId(wfUser.getId());
		wFRuTask.setOwnerName(wfUser.getText());
		wFRuTask.setUserId(wfUser.getId());
		wFRuTask.setUserName(wfUser.getText());
		wFRuTask.setTaskStartTime(new Date());
		wFRuTask.setPiid(wfHiProcinst.getId());
		wFRuTask.setActId(nodeMap.getId());
		wFRuTask.setActName(nodeMap.getText());
		wFRuTask.setActType(nodeMap.getType());

		wFRuTask.setUserIds(wfUser.getId());
		wFRuTask.setUserNames(wfUser.getText());
		wFRuTask.setRead(isRead);
		createTask(wFRuTask,nodeMap,serviceMap);
		return wFRuTask;
	}
	
	@Transactional
	public WFHiTask insertHiTask(WFRuTask wFRuTask, NodeMap nodeMap, Map<String, Object> serviceMap) {
		return insertHiTask(wFRuTask, nodeMap, serviceMap, 0);
	}

	@Transactional
	public WFHiTask insertHiTask(WFRuTask wfRuTask, NodeMap nodeMap, Map<String, Object> serviceMap,int isEnd) {
		WFHiTask wFHiTask = new WFHiTask();
		BeanUtils.copyProperties(wFHiTask, wfRuTask);
		if(isEnd==1){
			BeanUtils.defautlPropertiesSetNull(wFHiTask);
		}

		
		Map<String, Object> workMap = new HashMap<String, Object>();
		workMap.put("ruTask", wfRuTask);
		workMap.put("hiTask", wFHiTask);
		
		List<Map<String, String>> setValueConfigMapList = nodeMap.getTaskStartSetValueConfig();
		workFlowUtilService.updateServiceValue(serviceMap, setValueConfigMapList);
		
		List<Map<String, String>> interfaceConfigMapList = nodeMap.getTaskStartInterfaceConfig();
		workFlowUtilService.execInf(workMap,serviceMap, interfaceConfigMapList);
		if(isEnd==1){
			wFHiTask.setActId(nodeMap.getId());
			wFHiTask.setActName(nodeMap.getText());
			wFHiTask.setActType(nodeMap.getType());
			wFHiTask.setTaskStartTime(new Date());
			wFHiTask.setTaskEndTime(new Date());
			wFHiTask.setIsEnd(1);
			
			
			List<Map<String, String>> setValueConfigMapList1 = nodeMap.getTaskEndSetValueConfig();
			workFlowUtilService.updateServiceValue(serviceMap, setValueConfigMapList1);
			
			List<Map<String, String>> interfaceConfigMapList1 = nodeMap.getTaskEndInterfaceConfig();
			workFlowUtilService.execInf(workMap,serviceMap, interfaceConfigMapList1);
		}
		
		createTask(wFHiTask,  nodeMap,  serviceMap);
		return wFHiTask;
	}
}
