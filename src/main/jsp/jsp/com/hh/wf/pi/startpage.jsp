<%@page import="com.hh.system.util.pk.PrimaryKey"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.hh.system.util.SystemUtil"%>
<%@page import="com.google.gson.Gson"%>
<%=SystemUtil.getBaseDoctype()%>
<html>
<head>
<title>新建流程</title>
<%=SystemUtil.getBaseJs("workflow","layout")%>
<%
	Gson gson = new Gson();
	String frameId = PrimaryKey.getUUID();
%>

<script type="text/javascript">
	var frameId = '<%=frameId%>';
	var params = $.hh.getIframeParams();
	var nodeMap = params.nodeMap;
	var startEvent = nodeMap.startEvent;
	var workflowName = params.workflowName;
	var workflow = nodeMap.workflow;
	function init() {
		var href = startEvent.href;
		//$.hh.setFrameParams(frameId, startEvent);
		$.hh.setFrameParams(frameId, params);
		if (href) {
			$('#' + frameId).attr('src', Request.getHref(href, {
				type : 'workflow'
			}));
		}
		
		if(workflow.describe){
			$('#workflow_describe').append('<div>流程描述：'+workflow.describe+'</div>');
		}
		if(startEvent.describe && startEvent.describe!=workflow.describe){
			$('#workflow_describe').append('<div>节点描述：'+startEvent.describe+'</div>');
		}
		
		if(workflow.stepList){
			var stepListStr='';
			for(var i =0;i<workflow.stepList.length;i++){
				if(workflow.stepList[i]){
					stepListStr+=workflow.stepList[i]+'->';
				}
			}
			if(stepListStr){
				stepListStr=stepListStr.substr(0,stepListStr.length-2);
			}
			$('#workflow_describe').append('<div>流程步骤：'+stepListStr+'</div>');
		}
	}

	function save() {
		var iframe = window.frames[frameId];
		var formObject = iframe.save('start');
		if (formObject) {
			if(!formObject.id){
				formObject.id = frameId;
			}
			var serviceMapKeys = '';
			for ( var p in formObject) {
				serviceMapKeys += p + ',';
			}
			if (serviceMapKeys != '') {
				formObject.serviceMapKeys = serviceMapKeys.substr(0,
						serviceMapKeys.length - 1);
			}
			formObject.wfconfigId = workflow.wfconfigId;
			delete formObject.createTime;
			delete formObject.updateTime;
			Request.request('wf-WFOper-start', {
				data : formObject
			}, function(taskResult) {
				taskResult.serviceObject = formObject;
				WF.openSubmitPage({
					taskResult : taskResult,
					callback : function() {
						Dialog.close();
					}
				});
			});
		}
	}
	function showpic() {
		$.hh.addTab({
			id : 'pic_'+workflow.id,
			text : '流程图',
			src : 'jsp-wf-wfdef-defpic?id=' + workflow.id
		});
	}
</script>
</head>
<body  xtype="border_layout">
	<div config="render : 'west' ,width:190">
		<div id="workflow_describe" style="padding:5px;">
		</div>
	</div>
	<div>
	<div xtype="hh_content" style="overflow: hidden;">
		<iframe id="<%=frameId%>" name="<%=frameId%>" width=100% height=100%
			frameborder=0 src=""></iframe>
	</div>
	<div xtype="toolbar" >
		<span xtype="button"
			config=" id:'startBtn',text:'新建工作' , onClick : save  ,itype:'submit' "></span> <span
			xtype="button"
			config=" id:'showpicBtn',text:'查看流程图' , onClick : showpic , icon :'hh_img_image'  "></span>
	</div>
	</div>
</body>
</html>