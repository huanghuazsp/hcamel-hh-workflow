package com.hh.wf.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hh.form.service.impl.MongoFormOperService;
import com.hh.hibernate.dao.inf.IHibernateDAO;
import com.hh.system.util.BaseSystemUtil;
import com.hh.system.util.Check;
import com.hh.system.util.ClassReflex;
import com.hh.system.util.Convert;
import com.hh.system.util.MessageException;

@Service
public class WorkFlowUtilService {
	@Autowired
	private MongoFormOperService mongoFormOperService;
	
	@Autowired
	private IHibernateDAO<Object> hibernateDao;

	public void updateServiceValue(Map<String, Object> serviceMap, List<Map<String, String>> setValueConfigMapList) {
		String tableName  = Convert.toString(serviceMap.get("tableName"));
		String id  = Convert.toString(serviceMap.get("id"));
		if (Check.isNoEmpty(tableName) && Check.isNoEmpty(id)) {
			if(setValueConfigMapList!=null){
				Map<String, Object> serviceMap1 = new HashMap<String, Object>();
				for (Map<String, String> map : setValueConfigMapList) {
					serviceMap1.put(map.get("field"), BaseSystemUtil.generatedValue(map.get("value")));
				}
				mongoFormOperService.update(tableName,id,serviceMap1);
			}
		}
	}

	public void execInf(Map<String, Object> workMap,Map<String, Object> serviceMap, List<Map<String, String>> interfaceConfigMapList1) {
		if(interfaceConfigMapList1!=null){
			workMap.put("serviceMap", serviceMap);
			for (Map<String, String> map : interfaceConfigMapList1) {
				if ("sql".equals(map.get("implType"))) {
					hibernateDao.executeSql(map.get("value"), serviceMap);
				}else{
					String value = map.get("value");
					try {
						ClassReflex.executeSpringClass(value.split("#")[0], value.split("#")[1], workMap);
					} catch (Throwable e) {
						throw new MessageException("接口调用异常："+e.toString());
					}
				}
			}
		}
	}
	
	public void test(HashMap<String, Object> workMap)  {
		System.out.println(workMap);
	}
}
