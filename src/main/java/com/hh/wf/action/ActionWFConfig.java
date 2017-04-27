package com.hh.wf.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.hh.system.service.impl.BaseService;
import com.hh.system.util.MessageException;
import com.hh.system.util.base.BaseServiceAction;
import com.hh.system.util.model.ReturnModel;
import com.hh.wf.bean.WFConfig;
import com.hh.wf.service.impl.WFConfigService;

@SuppressWarnings("serial")
public class ActionWFConfig extends BaseServiceAction<WFConfig> {
	public BaseService<WFConfig> getService() {
		return wfConfigService;
	}

	@Autowired
	private WFConfigService wfConfigService;

	private String imgxml;
	private int deploy;

	private int imageHeight;
	private int imageWidth;

	public Object save() {
		try {
			String imgxmlstr = imgxml.replaceAll(request.getContextPath(), "");
			WFConfig wFConfig = wfConfigService.save(this.object, imgxmlstr,
					deploy, imageHeight, imageWidth);
		} catch (MessageException e) {
			return e;
		}
		return null;
	}

	public Object findObjectById() {
		WFConfig WFConfig = wfConfigService.findObjectById(object.getId());
		if (WFConfig!=null) {
			WFConfig.setMxgraphXml(WFConfig.getMxgraphXml()
					.replaceAll("!gt;", "&gt;").replaceAll("!lt;", "&lt;")
					.replaceAll("!quot;", "&quot;").replaceAll("!amp;", "&amp;"));
		}
		return WFConfig;
	}

	public String getImgxml() {
		return imgxml;
	}

	public void setImgxml(String imgxml) {
		this.imgxml = imgxml;
	}

	public int getDeploy() {
		return deploy;
	}

	public void setDeploy(int deploy) {
		this.deploy = deploy;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

}
