package com.hh.wf.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hh.system.service.impl.BaseService;
import com.hh.system.util.MessageException;
import com.hh.wf.bean.WFComment;
import com.hh.wf.service.inf.WFUserServiceInf;
import com.hh.wf.util.WFUser;

@Service
public class WFCommentService extends BaseService<WFComment> {
	@Autowired
	private WFUserServiceInf loginService;

	@Override
	public WFComment save(WFComment entity) throws MessageException {
		WFUser user = loginService.findLoginUser();
		entity.setUserId(user.getId());
		entity.setUserName(user.getText());
		entity.setApproveTime(new Date());
		return super.save(entity);
	}

}
