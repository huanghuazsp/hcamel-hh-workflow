package com.hh.wf.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hh.system.service.impl.BaseService;
import com.hh.system.service.inf.LoadDataTime;
import com.hh.system.util.dto.ParamFactory;
import com.hh.system.util.pk.PrimaryKey;
import com.hh.wf.bean.WFRuTask;
import com.hh.wf.bean.WFRuTaskUser;
import com.hh.wf.service.inf.WFUserServiceInf;
import com.hh.wf.util.NodeMap;
import com.hh.wf.util.WFUser;

@Service
public class WFRuTaskService extends BaseService<WFRuTask> implements LoadDataTime {
	@Autowired
	private WFUserServiceInf loginService;
	@Autowired
	private WFRuTaskUserService wfRuTaskUserService;
	
	@Autowired
	private WFHiTaskService wfHiTaskService;
	

	@Override
	public Map<String, Object> load() {
		WFUser wfUser = loginService.findLoginUser();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("userId", wfUser.getId());
		
		String sjhqlCount = "select count(b) from " + WFRuTask.class.getName() + " a , " + WFRuTaskUser.class.getName()
				+ " b  where  a.id=b.taskId and b.userId=:userId";

		int count = dao.findCount(sjhqlCount, paramsMap);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", count);
		map.put("vsj", "jsp-wf-task-taskmain");
		map.put("id", "c4ef89f5-b94a-4737-a935-e49bfce0105e");
		return map;
	}

	public void createTask(WFRuTask entity,Map<String, Object> serviceMap, NodeMap nodeMap) {
		WFUser wfUser = loginService.findLoginUser();
		entity.setSendOrgName(wfUser.getOrgDeptText());
		String uuid = PrimaryKey.getUUID();
		entity.setId(uuid);
		wfHiTaskService.insertHiTask(entity,nodeMap,serviceMap);
		createEntity(entity);
		wfRuTaskUserService.addUserByTask(entity);
	}

	public void deleteTask(String taskId) {
		deleteByIds(taskId);
	}
	
	public int findRuTaskCountByPiid(String piid) {
		return findCount(ParamFactory.getParamHb().is("piid", piid));
	}

}
