package com.hh.wf.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.hh.system.service.impl.BaseService;
import com.hh.system.util.base.BaseServiceAction;
import com.hh.wf.bean.WFTaskForward;
import com.hh.wf.service.impl.WFTaskForwardService;

@SuppressWarnings("serial")
public class ActionWFTaskForward extends BaseServiceAction<WFTaskForward> {
	public BaseService<WFTaskForward> getService() {
		return wfConfigService;
	}

	@Autowired
	private WFTaskForwardService wfConfigService;



}
