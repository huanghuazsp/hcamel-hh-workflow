package com.hh.wf.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hh.hibernate.util.base.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "WF_CONFIG")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WFConfig extends BaseEntity {
	private String mxgraphXml;
	private byte[] img;
	private String rootObject;
	private String cellObjectList;

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

}
