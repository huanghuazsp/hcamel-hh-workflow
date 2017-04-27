package com.hh.wf.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hh.form.bean.FormInfo;
import com.hh.form.service.impl.FormInfoService;
import com.hh.form.service.impl.MongoFormOperService;
import com.hh.system.service.impl.BaseService;
import com.hh.system.util.Check;
import com.hh.system.util.Convert;
import com.hh.wf.bean.WFDef;
import com.hh.wf.bean.WFHiProcinst;
import com.hh.wf.util.NodeMap;

@Service
public class WFHiProcinstService extends BaseService<WFHiProcinst> {
	@Autowired
	private FormInfoService formInfoService;
	@Autowired
	private WFRuTaskService wFRuTaskService;
	@Autowired
	private WFHiTaskService wFHiTaskService;
	@Autowired
	private WFRuTaskUserService wfRuTaskUserService;
	@Autowired
	private WFDefService wfDefService;
	@Autowired
	private MongoFormOperService mongoFormOperService;
	@Autowired
	private WFTaskForwardService wfTaskForwardService;
	@Autowired
	private WFCommentService wfCommentService;
	@Transactional
	public void deletePiByIds(String ids) {
		List<String> idList = Convert.strToList(ids);
		for (String piid : idList) {
			WFHiProcinst wfHiProcinst = dao.findEntityByPK(WFHiProcinst.class, piid);
			WFDef wfDef = wfDefService.findObjectById(wfHiProcinst.getWfDefId());
			if (wfDef!=null) {
				Map<String, NodeMap> nodeMap = wfDef.getNodeMap();
				String hrefckeditor = nodeMap.get("workflow").getHrefckeditor();
				if (Check.isNoEmpty(hrefckeditor)) {
					FormInfo formInfo = formInfoService.findObjectById(hrefckeditor);
					if (Check.isNoEmpty(formInfo)) {
						mongoFormOperService.deleteByIds(wfHiProcinst.getServiceId(), formInfo.getTableName());
					}
				}
			}
		}
		deleteByIds(idList);
		wFRuTaskService.deleteByProperty("piid", idList);
		wFHiTaskService.deleteByProperty("piid", idList);
		wfRuTaskUserService.deleteByProperty("piid", idList);
		wfTaskForwardService.deleteByProperty("piid", idList);
		wfCommentService.deleteByProperty("piid", idList);
	}
	
	@Transactional
	public void checkEndPi(String piid, Map<String, NodeMap> nodeMap, List<String> endActIdList) {
		int ruCount = wFRuTaskService.findRuTaskCountByPiid(piid);
		if (ruCount == 0) {
			WFHiProcinst wfHiProcinst = findObjectById( piid);
//			for (String actId : endActIdList) {
//				wFHiTaskService.insertHiTaskStart(wfHiProcinst, nodeMap.get(actId), 1);
//			}
			wfHiProcinst.setPiEndTime(new Date());
			updateEntity(wfHiProcinst);
			wfRuTaskUserService.deleteByProperty("piid", wfHiProcinst.getId());
		}
	}
}
