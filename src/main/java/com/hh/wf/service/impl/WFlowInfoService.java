package com.hh.wf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hh.system.service.impl.BaseService;
import com.hh.system.util.Check;
import com.hh.system.util.dto.ParamFactory;
import com.hh.system.util.dto.ParamInf;
import com.hh.wf.bean.WFDef;
import com.hh.wf.bean.WFlowInfo;

@Service
public class WFlowInfoService extends BaseService<WFlowInfo>{
	@Autowired
	private WFConfigService wFConfigService;
	@Autowired
	private WFDefService wfDefService;
	@Override
	public List<String> deleteTreeByIds(String ids) {
		List<String> deleteIds =  super.deleteTreeByIds(ids);
		wFConfigService.deleteByIds(deleteIds);
		return deleteIds;
	}
	
	public List<WFlowInfo> queryStartWfDef(WFlowInfo wFlowInfo) {
		
		String node =Check.isEmpty(wFlowInfo.getNode()) ? "root" : wFlowInfo.getNode();
		ParamInf hqlParamList = ParamFactory.getParamHb();
		if (Check.isNoEmpty(wFlowInfo.getText()) && "root".equals(node)) {
			hqlParamList.like("text", wFlowInfo.getText());
		}else{
			hqlParamList.is("node", node);
		}
		
		hqlParamList.is("isHide", 0);
		
		List<WFlowInfo> list =  this.queryTreeList(hqlParamList);
		List<WFlowInfo> returnlist = new ArrayList<WFlowInfo>();
		
		for (WFlowInfo wFlowInfo2 : list) {
			if (wFlowInfo2.getLeaf()==1) {
				List<WFDef> wfDefs = wfDefService.queryListByWFId(wFlowInfo2.getId());
				if (wfDefs.size()==0) {
					continue;
				}
			}
			returnlist.add(wFlowInfo2);
		}
		
		return returnlist;
	}

}
