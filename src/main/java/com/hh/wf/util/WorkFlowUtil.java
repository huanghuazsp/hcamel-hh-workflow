package com.hh.wf.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hh.system.service.impl.BeanFactoryHelper;
import com.hh.system.util.Check;
import com.hh.system.util.Convert;
import com.hh.system.util.Json;
import com.hh.wf.bean.WFDef;
import com.hh.wf.service.inf.WFUserServiceInf;

public class WorkFlowUtil {

	private static WFUserServiceInf loginUserServiceInf;

	public static Map<String, NodeMap> defToNodeMap(WFDef wfDef) {
		if (wfDef == null) {
			return null;
		}
		Map<String, NodeMap> map = new HashMap<String, NodeMap>();

		Map<String, List<String>> sequenceFlowMap = new HashMap<String, List<String>>();

		Map<String, List<String>> inSequenceFlowMap = new HashMap<String, List<String>>();

		NodeMap rootMap = toMap(wfDef.getRootObject());
		String startUserTaskId = Convert.toString(rootMap.get("startEventTargetRefId"));
		String workflowName = Convert.toString(rootMap.get("label"));
		Set<String> rootSet = rootMap.keySet();
		rootSet.remove("label");
		rootSet.remove("startEventTargetRefId");
		rootSet.remove("data");
		rootSet.remove("description");
		List<NodeMap> cellMaps = toMapList(wfDef.getCellObjectList());
		for (NodeMap map2 : cellMaps) {
			for (String rootkey : rootSet) {
				if (Check.isEmpty(map2.get(rootkey))) {
					map2.put(rootkey, rootMap.get(rootkey));
				}
			}
			setValueConfig(map2);
			interfaceConfig(map2);

			map.put(Convert.toString(map2.get("id")), map2);
			if ("startEvent".equals(map2.get("type"))) {
				map.put("startEvent", map2);
			}
			if (Convert.toString(map2.get("id")).equals(startUserTaskId)) {
				map.put("startUserTask", map2);
			}
			if ("sequenceFlow".equals(map2.get("type"))) {
				String sourceRef = Convert.toString(map2.get("sourceRef"));
				if (sequenceFlowMap.get(sourceRef) == null) {
					sequenceFlowMap.put(sourceRef, new ArrayList<String>());
				}
				sequenceFlowMap.get(sourceRef).add(map2.getId());

				String targetRef = Convert.toString(map2.get("targetRef"));
				if (inSequenceFlowMap.get(targetRef) == null) {
					inSequenceFlowMap.put(targetRef, new ArrayList<String>());
				}
				inSequenceFlowMap.get(targetRef).add(map2.getId());

			}
		}
		rootMap.put("id", wfDef.getId());
		rootMap.put("wfconfigId", wfDef.getWfid());
		rootMap.put("label", workflowName);
		map.put("workflow", rootMap);

		Set<String> sequenceFlowMapKeySet = sequenceFlowMap.keySet();
		for (String key : sequenceFlowMapKeySet) {
			NodeMap nodeMap = map.get(key);
			if (nodeMap == null) {
				continue;
			}
			List<String> sequenceFlowMapList = sequenceFlowMap.get(key);
			for (String sequenceFlow : sequenceFlowMapList) {
				String targetRef = Convert.toString(map.get(sequenceFlow).get("targetRef"));

				if (nodeMap.get("nextNodeList") == null) {
					nodeMap.put("nextNodeList", new ArrayList<String>());
				}
				((List<String>) nodeMap.get("nextNodeList")).add(map.get(targetRef).getId());

			}
			nodeMap.put("sequenceFlowList", sequenceFlowMap.get(key));
		}

		Set<String> inSequenceFlowMapKeySet = inSequenceFlowMap.keySet();
		for (String key : inSequenceFlowMapKeySet) {
			NodeMap nodeMap = map.get(key);
			if (nodeMap == null) {
				continue;
			}
			List<String> inSequenceFlowMapList = inSequenceFlowMap.get(key);
			for (String sequenceFlow : inSequenceFlowMapList) {
				String sourceRef = Convert.toString(map.get(sequenceFlow).get("sourceRef"));
				if (nodeMap.get("previousNodeList") == null) {
					nodeMap.put("previousNodeList", new ArrayList<String>());
				}
				((List<String>) nodeMap.get("previousNodeList")).add(map.get(sourceRef).getId());

			}
		}

		List<String> stepList = new ArrayList<String>();
		List<String> stepIdList = new ArrayList<String>();
		stepList.add(map.get("startEvent").getText());
		stepIdList.add(map.get("startEvent").getId());
		List<NodeMap> nodeList = new ArrayList<NodeMap>();
		nodeList.add(map.get("startEvent"));
		addStep(map, nodeList, stepList, stepIdList);

		rootMap.put("stepList", stepList);

		return map;
	}

	private static void addStep(Map<String, NodeMap> map, List<NodeMap> nodeMapList, List<String> stepList,
			List<String> stepIdList) {
		List<String> nextNodeList = new ArrayList<String>();
		for (NodeMap nodeMap : nodeMapList) {
			if (nodeMap != null && nodeMap.getNextNodeList() != null) {
				nextNodeList.addAll(nodeMap.getNextNodeList());
			}
		}
		if (nextNodeList.size() > 0) {
			List<NodeMap> nodeMapList2 = new ArrayList<NodeMap>();
			String str = "";
			for (String nextNode : nextNodeList) {
				NodeMap nodeMap = map.get(nextNode);
				if (nodeMap != null) {
					if (!stepIdList.contains(nodeMap.getId()) || nodeMap.getType().indexOf("endEvent") > -1) {
						stepIdList.add(nodeMap.getId());
						str += nodeMap.getText() + ",";
						nodeMapList2.add(nodeMap);
					}
				}
			}
			if (Check.isNoEmpty(str)) {
				str = str.substring(0, str.length() - 1);
				stepList.add(str);
				addStep(map, nodeMapList2, stepList, stepIdList);
			}
		}
	}

	private static void setValueConfig(NodeMap map2) {
		String setValueConfig = Convert.toString(map2.get("setValueConfig"));
		if (Check.isNoEmpty(setValueConfig)) {
			List<Map<String, Object>> setValueConfigMapList = Json.toMapList(setValueConfig);
			List<Map<String, String>> startsetValueConfigMapList = new ArrayList<Map<String, String>>();
			List<Map<String, String>> endsetValueConfigMapList = new ArrayList<Map<String, String>>();
			for (Map<String, Object> map3 : setValueConfigMapList) {
				String type = Convert.toString(map3.get("type"));
				if ("taskEnd".equals(type)) {
					endsetValueConfigMapList.add((Map) map3);
				} else {
					startsetValueConfigMapList.add((Map) map3);
				}
			}
			if (startsetValueConfigMapList.size() > 0) {
				map2.put("taskStartSetValueConfig", startsetValueConfigMapList);
			}
			if (endsetValueConfigMapList.size() > 0) {
				map2.put("taskEndSetValueConfig", endsetValueConfigMapList);
			}
		}
	}

	private static void interfaceConfig(NodeMap map2) {
		String setValueConfig = Convert.toString(map2.get("interfaceConfig"));
		if (Check.isNoEmpty(setValueConfig)) {
			List<Map<String, Object>> setValueConfigMapList = Json.toMapList(setValueConfig);
			List<Map<String, String>> startsetValueConfigMapList = new ArrayList<Map<String, String>>();
			List<Map<String, String>> endsetValueConfigMapList = new ArrayList<Map<String, String>>();
			for (Map<String, Object> map3 : setValueConfigMapList) {
				String type = Convert.toString(map3.get("type"));
				if ("taskEnd".equals(type)) {
					endsetValueConfigMapList.add((Map) map3);
				} else {
					startsetValueConfigMapList.add((Map) map3);
				}
			}
			if (startsetValueConfigMapList.size() > 0) {
				map2.put("taskStartInterfaceConfig", startsetValueConfigMapList);
			}
			if (endsetValueConfigMapList.size() > 0) {
				map2.put("taskEndInterfaceConfig", endsetValueConfigMapList);
			}
		}
	}

	public static NodeMap toMap(String objectStr) {
		NodeMap map = new NodeMap();
		if (objectStr == null) {
			return map;
		}
		try {
			JSONObject jsonObject = new JSONObject(objectStr);
			String[] names = JSONObject.getNames(jsonObject);
			for (String name : names) {
				map.put(name, jsonObject.get(name));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static List<NodeMap> toMapList(String objectArrStr) {
		List<NodeMap> mapList = new ArrayList<NodeMap>();
		if (objectArrStr == null) {
			return mapList;
		}
		try {
			JSONArray array = new JSONArray(objectArrStr);
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				String[] names = JSONObject.getNames(jsonObject);
				NodeMap map = new NodeMap();
				for (String name : names) {
					map.put(name, jsonObject.get(name));
				}
				mapList.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mapList;
	}

	public static WFUserServiceInf getUserService() {
		if (loginUserServiceInf == null) {
			loginUserServiceInf = BeanFactoryHelper.getBeanFactory().getBean(WFUserServiceInf.class);
		}
		return loginUserServiceInf;
	}
}
