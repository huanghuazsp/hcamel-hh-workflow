package com.hh.wf.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.hh.system.service.impl.BaseService;
import com.hh.system.util.Check;
import com.hh.system.util.Convert;
import com.hh.system.util.base.BaseServiceAction;
import com.hh.system.util.dto.ParamFactory;
import com.hh.system.util.dto.ParamInf;
import com.hh.wf.bean.WFDef;
import com.hh.wf.bean.WFlowInfo;
import com.hh.wf.service.impl.WFDefService;
import com.hh.wf.service.impl.WFlowInfoService;

@SuppressWarnings("serial")
public class ActionWFlowInfo extends BaseServiceAction<WFlowInfo> {
	public BaseService<WFlowInfo> getService() {
		return workFlowInfoService;
	}

	@Autowired
	private WFlowInfoService workFlowInfoService;
	@Autowired
	private WFDefService wfDefService;


	public Object queryWfDefTreeList() {
		List<Map<String, Object>> returnMapList = new ArrayList<Map<String, Object>>();
		if ("workflow".equals(object.getIconSkin())) {
			List<WFDef> wfDefs = wfDefService.queryListByWFId(object.getNode());
			for (WFDef wfDef : wfDefs) {
				Map<String, Object> map = Convert.objectToMap(wfDef);
				map.put("leaf", 1);
				map.put("text", wfDef.getVersion()+"."+request.getParameter("params"));
				map.put("name", map.get("text"));
				map.put("workflowName", object.getText());
				map.remove("mxgraphXml");
				map.remove("img");
				map.remove("rootObject");
				map.remove("cellObjectList");
				map.put("isParent", 0);
				returnMapList.add(map);
			}
		} else {
			String node =Check.isEmpty(object.getNode()) ? "root" : object.getNode();
			ParamInf hqlParamList = ParamFactory.getParamHb();
			if (Check.isNoEmpty(object.getText()) && "root".equals(node)) {
				hqlParamList.like("text", object.getText());
			}else{
				hqlParamList.is("node", node);
			}
			List<WFlowInfo> wFlowInfoList = getService().queryList(hqlParamList);
			for (WFlowInfo wFlowInfo : wFlowInfoList) {
				Map<String, Object> map = Convert.objectToMap(wFlowInfo);
				if (wFlowInfo.getLeaf() == 1) {
					map.put("leaf", 0);
					// map.put("text", wFlowInfo.getText()+"（流程）");
					map.put("img", "/hhcommon/images/big/system/workflow.png");
					map.put("icon", "/hhcommon/images/myimage/org/org.png");
					
					map.put("iconSkin", "workflow");
				}
				map.put("isParent", 1);
				map.put("params", wFlowInfo.getText());
				
				returnMapList.add(map);
			}
		}
		return returnMapList;
	}
	
	public Object queryStartWfDef(){
		return workFlowInfoService.queryStartWfDef(object);
	}

	public Object queryTreeListType() {
		ParamInf hqlParamList = convertTreeParams();
		hqlParamList.is("leaf", 0);
		return getService().queryTreeList(hqlParamList);
	}
	

}
