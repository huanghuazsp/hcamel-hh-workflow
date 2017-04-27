package com.hh.wf.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hh.system.util.Convert;

public class NodeMap extends HashMap<String, Object> {

	public String getText() {
		return Convert.toString(this.get("label"));
	}

	public String getId() {
		return Convert.toString(this.get("id"));
	}
	
	public String getType() {
		return Convert.toString(this.get("type"));
	}
	
	public List<String> getNextNodeList() {
		return ((List<String>) this.get("nextNodeList"));
	}
	
	public List<String> getSequenceFlowList() {
		return ((List<String>) this.get("sequenceFlowList"));
	}
	
	public double getX() {
		return Double.valueOf(Convert.toString(this.get("x")));
	}
	public double getY() {
		return Double.valueOf(Convert.toString(this.get("y")));
	}
	public double getWidth() {
		return Double.valueOf(Convert.toString(this.get("width")));
	}
	public double getHeight() {
		return Double.valueOf(Convert.toString(this.get("height")));
	}

	public String getHrefckeditor() {
		return Convert.toString(this.get("hrefckeditor"));
	}
	
	public List<Map<String, String>> getTaskEndSetValueConfig() {
		return (List<Map<String, String>>)this.get("taskEndSetValueConfig");
	}
	
	public List<Map<String, String>> getTaskStartSetValueConfig() {
		return (List<Map<String, String>>)this.get("taskStartSetValueConfig");
	}
	
	public List<Map<String, String>> getTaskEndInterfaceConfig() {
		return (List<Map<String, String>>)this.get("taskEndInterfaceConfig");
	}
	
	public List<Map<String, String>> getTaskStartInterfaceConfig() {
		return (List<Map<String, String>>)this.get("taskStartInterfaceConfig");
	}
}
