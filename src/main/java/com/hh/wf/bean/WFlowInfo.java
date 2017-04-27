package com.hh.wf.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.hh.hibernate.dao.inf.Comment;
import com.hh.hibernate.dao.inf.Order;
import com.hh.hibernate.util.base.BaseEntityTree;

@SuppressWarnings("serial")
@Entity
@Table(name = "WF_INFO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Order(sorts = "asc")
public class WFlowInfo extends BaseEntityTree<WFlowInfo> {
	@Column(name = "LEAF", length = 1)
	public int getLeaf() {
		return leaf;
	}
	
	private int isHide;

	
	@Comment("是否隐藏")
	@Column(name="IS_HIDE",columnDefinition = "int default 0")
	public int getIsHide() {
		return isHide;
	}

	public void setIsHide(int isHide) {
		this.isHide = isHide;
	}
	
	
}
