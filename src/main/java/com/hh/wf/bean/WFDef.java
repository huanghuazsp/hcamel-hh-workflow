package com.hh.wf.bean;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hh.hibernate.util.base.BaseEntity;
import com.hh.wf.util.NodeMap;
import com.hh.wf.util.WorkFlowUtil;

@SuppressWarnings("serial")
@Entity
@Table(name = "WF_DEF")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WFDef extends BaseEntity {
	private String mxgraphXml;
	private byte[] img;
	private String rootObject;
	private String cellObjectList;
	private int version;
	private String wfid;
	
	
	@Lob
	@Column(name="MXGRAPH_XML")
	public String getMxgraphXml() {
		return mxgraphXml;
	}

	public void setMxgraphXml(String mxgraphXml) {
		this.mxgraphXml = mxgraphXml;
	}

	@Lob
	@Column(name="ROOT_OBJECT")
	public String getRootObject() {
		return rootObject;
	}

	public void setRootObject(String rootObject) {
		this.rootObject = rootObject;
	}

	
	@Lob
	@Column(name="CELL_OBJECT_LIST")
	public String getCellObjectList() {
		return cellObjectList;
	}

	public void setCellObjectList(String cellObjectList) {
		this.cellObjectList = cellObjectList;
	}

	@Lob
	@Column(name="IMG_")
	public byte[] getImg() {
		return img;
	}

	public void setImg(byte[] img) {
		this.img = img;
	}

	@Column(name="WF_ID",length=36)
	public String getWfid() {
		return wfid;
	}

	public void setWfid(String wfid) {
		this.wfid = wfid;
	}

	@Column(name="VERSION_",length=9)
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	@Transient
	public Map<String, NodeMap> getNodeMap(){
		return WorkFlowUtil.defToNodeMap(this);
	}
}
