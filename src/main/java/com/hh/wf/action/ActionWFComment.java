package com.hh.wf.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.hh.system.service.inf.BaseServiceInf;
import com.hh.system.util.base.BaseServiceAction;
import com.hh.system.util.dto.ParamFactory;
import com.hh.wf.bean.WFComment;
import com.hh.wf.service.impl.WFCommentService;

@SuppressWarnings("serial")
public class ActionWFComment extends BaseServiceAction<WFComment> {

	@Autowired
	private WFCommentService wfCommentService;

	@Override
	public BaseServiceInf<WFComment> getService() {
		return wfCommentService;
	}

	public Object queryListByPiid() {
		return wfCommentService.queryList(ParamFactory.getParamHb().is("piid", object.getPiid()));
	}

}
