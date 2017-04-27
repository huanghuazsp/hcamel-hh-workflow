package com.hh.wf.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hh.system.service.impl.BaseService;
import com.hh.system.util.MessageException;
import com.hh.wf.bean.WFConfig;
import com.hh.wf.util.ImageUtil;

@Service
public class WFConfigService extends BaseService<WFConfig> {

	@Autowired
	private WFDefService wFDefService;

	public WFConfig save(WFConfig workFlowInfo, String imgxml, int deploy,
			int imageHeight, int imageWidth) throws MessageException {
		Map<String, Object> map = convetMxgraph(imgxml, imageHeight, imageWidth,
				workFlowInfo.getMxgraphXml());
		String mxgraphXml = (String)map.get("mxgraphXml") ;
		byte[] imageByte =(byte[]) map.get("imageByte") ;
		workFlowInfo.setImg(imageByte);
		workFlowInfo.setMxgraphXml(mxgraphXml);
		dao.saveOrUpdateEntity(workFlowInfo);
		if (deploy == 1) {
			wFDefService.deploy(workFlowInfo);
		}
		return workFlowInfo;
	}

	public Map<String, Object> convetMxgraph(String imgxml, int imageHeight,
			int imageWidth, String mxgraphXml) {
		if (!mxgraphXml.endsWith("</mxGraphModel>")) {
			mxgraphXml = "<mxGraphModel>" + mxgraphXml + "</mxGraphModel>";
		}
		HttpServletRequest request = ServletActionContext.getRequest();
		String path = request.getContextPath();
		if (!imgxml.endsWith("</output>")) {
			imgxml = "<output>" + imgxml + "</output>";
		}
		byte[] imageByte = ImageUtil.exportImage(imageWidth, imageHeight,
				imgxml.replaceAll(path + "/jsp/", ""));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("imageByte", imageByte);
		map.put("mxgraphXml", mxgraphXml);
		return map;
	}


}
