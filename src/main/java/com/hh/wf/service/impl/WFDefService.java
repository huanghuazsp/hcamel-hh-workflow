package com.hh.wf.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hh.system.service.impl.BaseService;
import com.hh.system.util.BeanUtils;
import com.hh.system.util.MessageException;
import com.hh.system.util.dto.ParamFactory;
import com.hh.system.util.dto.ParamInf;
import com.hh.system.util.dto.ParamList;
import com.hh.system.util.pk.PrimaryKey;
import com.hh.wf.bean.WFConfig;
import com.hh.wf.bean.WFDef;
import com.hh.wf.util.NodeMap;

@Service
public class WFDefService extends BaseService<WFDef> {
	@Autowired
	public WFConfigService wfConfigService;

	public void deploy(String wfCfgId) {
		WFConfig wFConfig = wfConfigService.findObjectById(wfCfgId);
		deploy(wFConfig);
	}

	public WFDef maxByWFId(String wfid) {
		WFDef maxWfDef = dao.max(WFDef.class,ParamFactory.getParamHb().is("wfid", wfid).orderDesc(
				"version"));
		return maxWfDef;
	}
	
	public Map<String, NodeMap> maxNodeMap(String wfid) {
		return maxByWFId(wfid).getNodeMap();
	}

	@Transactional
	public WFDef deploy(WFConfig wFConfig) {
		WFDef wfDef = new WFDef();
		BeanUtils.copyProperties(wfDef, wFConfig);
		BeanUtils.defautlPropertiesSetNull(wfDef);
		wfDef.setWfid(wFConfig.getId());
		WFDef maxWfDef = dao.max(WFDef.class,ParamFactory.getParamHb().is("wfid", wfDef.getWfid())
				.orderDesc("version"));
		if (maxWfDef == null) {
			wfDef.setVersion(1);
		} else {
			wfDef.setVersion(maxWfDef.getVersion() + 1);
		}
		wfDef.setId(PrimaryKey.getUUID());
		createEntity(wfDef);
		return wfDef;
	}

	public List<WFDef> queryListByWFId(String wfid) {
		return queryList(ParamFactory.getParamHb().is("wfid", wfid).orderDesc("version"));
	}

	public WFDef save(WFDef object, String imgxmlstr, int imageHeight,
			int imageWidth) throws MessageException {
		Map<String, Object> map = wfConfigService.convetMxgraph(imgxmlstr,
				imageHeight, imageWidth, object.getMxgraphXml());
		String mxgraphXml = (String) map.get("mxgraphXml");
		byte[] imageByte = (byte[]) map.get("imageByte");
		// object.setImg(imageByte);
		// object.setMxgraphXml(mxgraphXml);

		WFDef wfDef = this.findObjectById(object.getId());
		wfDef.setMxgraphXml(mxgraphXml);
		wfDef.setImg(imageByte);
		wfDef.setRootObject(object.getRootObject());
		wfDef.setCellObjectList(object.getCellObjectList());
		return this.save(wfDef);
	}
}
