package com.hh.wf.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hh.system.util.StaticVar;
import com.hh.usersystem.bean.usersystem.SysMenu;
import com.hh.usersystem.util.steady.StaticProperties;

@Service
public class SetupInitializer {
	@Autowired
	private WFRuTaskService wFRuTaskService;

	@PostConstruct
	public void initialize() {
		StaticVar.loadDataTimeMap.put("jsp-wf-task-taskmain", wFRuTaskService);
		
		
		for (SysMenu hhXtCd : StaticProperties.sysMenuList) {
			if ("协同办公".equals(hhXtCd.getText())) {
				hhXtCd.getChildren().add(new SysMenu( "PVHXLiZ354OeWEDcxqy","我的工作",
						"jsp-wf-task-taskmain",
						"/hhcommon/images/big/apple/23.png","/hhcommon/images/extjsico/txt.gif", 0, 1));
				break;
			}
		}
		
		for (SysMenu hhXtCd : StaticProperties.sysMenuList) {
			if ("系统管理".equals(hhXtCd.getText())) {
				hhXtCd.getChildren().add(new SysMenu( "uHSoVgc5jIqo1Xvzqs0","流程系统",
						"jsp-wf-wfinfo-main", "/hhcommon/images/big/apple/23.png","/hhcommon/images/extjsico/computer.gif", 0, 1));
				break;
			}
		}
	}
}