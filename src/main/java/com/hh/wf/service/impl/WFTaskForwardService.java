package com.hh.wf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hh.system.service.impl.BaseService;
import com.hh.system.util.dto.PageRange;
import com.hh.system.util.dto.PagingData;
import com.hh.system.util.dto.ParamFactory;
import com.hh.wf.bean.WFRuTask;
import com.hh.wf.bean.WFRuTaskUser;
import com.hh.wf.bean.WFTaskForward;
import com.hh.wf.service.inf.WFUserServiceInf;
import com.hh.wf.util.WFUser;

@Service
public class WFTaskForwardService extends BaseService<WFTaskForward> {
	@Autowired
	private WFUserServiceInf loginService;
	@Autowired
	private WFRuTaskUserService wfRuTaskUserService;
	@Autowired
	private WFRuTaskService wfRuTaskService;

	@Override
	public PagingData<WFTaskForward> queryPagingData(WFTaskForward entity, PageRange pageRange) {
		WFUser wfUser = loginService.findLoginUser();
		PagingData<WFTaskForward> page = this.queryPagingData(pageRange,
				ParamFactory.getParamHb().is("ownerId", wfUser.getId()));

		List<String> taskIdList = new ArrayList<String>();
		for (WFTaskForward WFTaskForward : page.getItems()) {
			taskIdList.add(WFTaskForward.getTaskId());
		}
		if (taskIdList.size()>0) {
			List<WFRuTaskUser> wfRuTaskUserList = wfRuTaskUserService.queryListByProperty("taskId", taskIdList);
			List<WFRuTask> wfRuTaskList = wfRuTaskService.queryListByProperty("id", taskIdList);

			Map<String, WFRuTaskUser> WFRuTaskUserMap = new HashMap<String, WFRuTaskUser>();
			for (WFRuTaskUser wfRuTaskUser : wfRuTaskUserList) {
				WFRuTaskUserMap.put(wfRuTaskUser.getTaskId() + wfRuTaskUser.getUserId(), wfRuTaskUser);
			}
			Map<String, WFRuTask> WFRuTaskMap = new HashMap<String, WFRuTask>();
			for (WFRuTask wfRuTask : wfRuTaskList) {
				WFRuTaskMap.put(wfRuTask.getId(), wfRuTask);
			}

			for (WFTaskForward WFTaskForward : page.getItems()) {
				WFRuTaskUser WFRuTaskUser = WFRuTaskUserMap.get(WFTaskForward.getTaskId() + WFTaskForward.getUserId());
				if (WFRuTaskUser!=null && WFRuTaskUser.getRead() == 0 && WFRuTaskUser.getIsSubmit() == 0
						&& WFRuTaskMap.get(WFTaskForward.getTaskId()) != null) {
					WFTaskForward.setIsWithdraw(1);
				}
			}
		}
		return page;
	}

	public List<WFTaskForward> queryTaskForwardListByPiid(String piid) {
		return this.queryList(ParamFactory.getParamHb().is("piid", piid));
	}
}
