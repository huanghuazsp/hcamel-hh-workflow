package com.hh.wf.action;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;

import com.hh.system.service.impl.BaseService;
import com.hh.system.util.MessageException;
import com.hh.system.util.base.BaseServiceAction;
import com.hh.system.util.model.ResultFile;
import com.hh.wf.bean.WFConfig;
import com.hh.wf.bean.WFDef;
import com.hh.wf.service.impl.WFDefService;

@SuppressWarnings("serial")
public class ActionWFDef extends BaseServiceAction<WFDef>  {
	public BaseService<WFDef> getService() {
		return wfDefService;
	}

	@Autowired
	private WFDefService wfDefService;
	private String imgxml;
	private int imageHeight;
	private int imageWidth;

	public Object save() {
		try {
			String imgxmlstr = imgxml.replaceAll(request.getContextPath(), "");
			WFDef wfDef = wfDefService.save(this.object, imgxmlstr, imageHeight, imageWidth);
		} catch (MessageException e) {
			return e;
		}
		return null;
	}
	
	public Object findObjectById() {
		WFDef WFDef = wfDefService.findObjectById(object.getId());
		if (WFDef!=null) {
			WFDef.setMxgraphXml(WFDef.getMxgraphXml()
					.replaceAll("!gt;", "&gt;").replaceAll("!lt;", "&lt;")
					.replaceAll("!quot;", "&quot;").replaceAll("!amp;", "&amp;"));
		}
		return WFDef;
	}

	public Object findMaxWFDef() {
		return wfDefService.maxNodeMap(object.getWfid());
	}

	public Object showPic() {
		ResultFile resultFile = new ResultFile("",
				new ByteArrayInputStream(wfDefService.findObjectById(object.getId()).getImg()), "image/png", true);
		return resultFile;
	}

	public String getImgxml() {
		return imgxml;
	}

	public void setImgxml(String imgxml) {
		this.imgxml = imgxml;
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

	private byte[] bytes = null;

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	private String contentType = "image/png";

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
