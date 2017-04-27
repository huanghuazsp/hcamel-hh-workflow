package com.hh.wf.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hh.message.bean.SysMessage;
import com.hh.message.service.MessageThread;
import com.hh.message.service.SysMessageService;
import com.hh.system.service.impl.BaseService;
import com.hh.system.util.Check;
import com.hh.system.util.Convert;
import com.hh.system.util.MessageException;
import com.hh.system.util.ThreadUtil;
import com.hh.system.util.dto.ParamFactory;
import com.hh.wf.bean.WFBaseRuTask;
import com.hh.wf.bean.WFRuTaskUser;

@Service
public class WFRuTaskUserService extends BaseService<WFRuTaskUser> {

	@Autowired
	private SysMessageService sysMessageService;

	public void addUserByTask(String wfDefId,String piid, String piName, String actName, String taskId, String userIds,
			String userTexts, int isRead) {
		List<String> ids = Convert.strToList(userIds);
		List<String> names = Convert.strToList(userTexts);
		if (ids.size() != names.size()) {
			throw new MessageException("接收人数据异常！");
		}
		for (int i = 0; i < ids.size(); i++) {
			WFRuTaskUser sysEmailUser = new WFRuTaskUser();
			sysEmailUser.setWfDefId(wfDefId);
			sysEmailUser.setTaskId(taskId);
			sysEmailUser.setPiid(piid);
			sysEmailUser.setUserId(ids.get(i));
			sysEmailUser.setUserName(names.get(i));
			if (isRead == 1) {
				sysEmailUser.setRead(1);
			}
			dao.createEntity(sysEmailUser);
			if (isRead != 1) {
				addMessage("",piName, actName, taskId, sysEmailUser);
			}
		}
	}

	public void addMessage(String typeName,String piName, String actName, String taskId, WFRuTaskUser sysEmailUser) {
		String content = piName + "[" + actName + "]";
		if (Check.isNoEmpty(typeName)) {
			content="["+typeName+"]"+content;
		}
		SysMessage message = sysMessageService.findMessage(12, content, sysEmailUser.getUserId(),
				sysEmailUser.getUserName(), "");
		message.setObjectId("task");
		message.setObjectName("任务");
		// message.setObjectHeadpic("/hhcommon/images/extjsico/txt.gif");
		message.setParams(taskId);
		ThreadUtil.getThreadPool().execute(new MessageThread(message, 0));
	}

	public void addUserByTask(WFBaseRuTask entity) {
		addUserByTask(entity.getWfDefId(),entity.getPiid(), entity.getPiName(), entity.getActName(), entity.getId(), entity.getUserIds(),
				entity.getUserNames(), entity.getRead());

	}

	public void addUserByTask(String wfDefId,String piid, String piName, String actName, String taskId, String userIds,
			String userTexts) {
		addUserByTask(wfDefId,piid, piName, actName, taskId, userIds, userTexts,0);
	}
	
	public List<WFRuTaskUser> queryTaskUserByTaskId(String taskId){
		return queryList(ParamFactory.getParamHb().is("taskId", taskId));
	}
	
	public Map<String,WFRuTaskUser> queryTaskUserMapByTaskId(String taskId){
		Map<String,WFRuTaskUser> WFRuTaskUserMap = new HashMap<String, WFRuTaskUser>();
		List<WFRuTaskUser> WFRuTaskUserList= queryTaskUserByTaskId(taskId);
		for (WFRuTaskUser wfRuTaskUser : WFRuTaskUserList) {
			WFRuTaskUserMap.put(wfRuTaskUser.getUserId(), wfRuTaskUser);
		}
		return WFRuTaskUserMap;
	}

	public void removeUsers(String taskId, String userIds) {
		Map<String,Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("taskId", taskId);
		paramsMap.put("userId", Convert.strToList(userIds));
		dao.deleteEntity("delete from " + WFRuTaskUser.class.getName()+" where taskId=:taskId and userId in (:userId)", paramsMap);
	}

}
