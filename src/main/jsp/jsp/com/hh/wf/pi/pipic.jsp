<%@page import="com.hh.wf.bean.WFTaskForward"%>
<%@page import="com.hh.wf.service.impl.WFTaskForwardService"%>
<%@page import="com.hh.wf.util.WorkFlowUtil"%>
<%@page import="com.hh.wf.util.WFUser"%>
<%@page import="com.hh.system.util.Convert"%>
<%@page import="com.hh.system.util.Json"%>
<%@page import="com.hh.wf.bean.WFRuTask"%>
<%@page import="com.hh.system.util.Check"%>
<%@page import="com.hh.wf.util.NodeMap"%>
<%@page import="com.hh.wf.bean.WFDef"%>
<%@page import="com.hh.wf.bean.WFHiTask"%>
<%@page import="com.hh.system.service.impl.BeanFactoryHelper"%>
<%@page import="com.hh.wf.service.impl.WFOperService"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.hh.system.util.SystemUtil"%>
<%=SystemUtil.getBaseDoctype()%>
<html>
<head>
<title>流程图</title>
<%=SystemUtil.getBaseJs("layout")%>
<%
WFOperService wfOperService = BeanFactoryHelper.getBeanFactory().getBean(WFOperService.class);
WFTaskForwardService wfTaskForwardService = BeanFactoryHelper.getBean(WFTaskForwardService.class);

String piid = request.getParameter("piid");

List<WFHiTask> wFHiTaskList =  wfOperService.queryHiTaskListByPiid(piid);
List<WFTaskForward> wfTaskForwardList =  wfTaskForwardService.queryTaskForwardListByPiid(piid);

WFHiTask wFHiTask1 = wFHiTaskList.get(0);
String defid = wFHiTask1.getWfDefId();
WFDef wfDef = wfOperService.getWfDefService().findObjectById(defid);
Map<String,NodeMap> nodeMap =  wfDef.getNodeMap();
%>
<script type="text/javascript">
	var params = $.hh.getIframeParams();

	var hiTaskList =<%=Json.toStr(wFHiTaskList)%>	;
	var wfTaskForwardList =<%=Json.toStr(wfTaskForwardList)%>	;


	function getUserNameByUserList(task) {
		var userNames = task.userNames;
		var userName = task.userName;
		if(userName){
			userNames=userNames.replace(userName+',','').replace(','+userName,'').replace(userName,'');
			if(userNames){
				userNames='<font color=red>'+userName+'</font>,'+userNames;
			}else{
				userNames='<font color=red>'+userName+'</font>';
			}
			
		}
		return userNames;
	}

	$(function() {
		
		var wfTaskForwardMap = {};
		for (var i = 0; i < wfTaskForwardList.length; i++) {
			var wfTaskForward = wfTaskForwardList[i];
			if(wfTaskForwardMap[wfTaskForward.actId] ==null){
				wfTaskForwardMap[wfTaskForward.actId]=[];
			}
			wfTaskForwardMap[wfTaskForward.actId].push(wfTaskForward);
		}
		
		var tasklist = [];
		for (var i = 0; i < hiTaskList.length; i++) {
			tasklist.push(hiTaskList[i]);
		}
		var html = '<table xtype="list">';
		for (var i = 0; i < tasklist.length; i++) {
			var task = tasklist[i];
			var userId = task.userId;
			var endtime = task.taskEndTime == null ? new Date() : new Date(
					task.taskEndTime);
			var taskStartTime = new Date(task.taskStartTime);
			var durationInMillis = $.hh.millisecondTOHHMMSS(endtime
					.getTime()
					- taskStartTime.getTime());
			var userName = (task.userName || '');
			var endTimeStr = $.hh.formatDate(task.taskEndTime,'yyyy-MM-dd HH:mm:ss');
			var ruImg = '';
			if (!endTimeStr) {
				ruImg = '<img title="办理中" src="/hhcommon/opensource/mxgraph/examples/editors/wfimg/userTask_ru.gif" style="width:16px;height:16px;" >&nbsp;';
				//userName = getUserName(userId);
			}
			var handleUserName = getUserNameByUserList(task);
			
			var wfTaskForwardMapList = wfTaskForwardMap[task.actId];
			var taskForwardStr = '';
			if(wfTaskForwardMapList && wfTaskForwardMapList.length>0){
				taskForwardStr+='<tr><td xtype="label" colspan=2 style="text-align:center">转发列表</td></tr>';
				taskForwardStr+='<tr><td colspan=2 style="text-align:center">';
				for (var j = 0; j < wfTaskForwardMapList.length; j++) {
					taskForwardStr+=wfTaskForwardMapList[j].ownerName+'>>>'+wfTaskForwardMapList[j].userName+'<br>';
				}
				taskForwardStr+='</td></tr>';
			}
			
			
			
			var table = '<table xtype="form" tableType="'+task.actId+'">'
					+ '<tr>	<td xtype="label" config=" width:\'65px\'"  >'
					+ ruImg
					+ '节点：</td><td>'
					+ (task.actName || '')
					+ '</td></tr>'
					+ '<tr>	<td xtype="label">办理人：</td><td style="word-break:break-all;">'
					+ handleUserName + '</td></tr>'
					+ '<tr>	<td xtype="label">创建时间：</td><td>'
					+ $.hh.formatDate(taskStartTime,'yyyy-MM-dd HH:mm:ss') + '</td></tr>'
					+ '<tr>	<td xtype="label">结束时间：</td><td>' + endTimeStr
					+ '</td></tr>' + '<tr>	<td xtype="label">耗时：</td><td>'
					+ durationInMillis + '</td></tr>' +taskForwardStr+ '</table>';
			html += '<tr><td>' + table + '</td></tr>';
		}
		html += '</table>';
		var table = $(html);
		table.render();
		table.find('[xtype=form]').render();
		$('#taskListdiv').append(table);
	});

	function imgclick(actid) {
		var tableHtml = '';
		var findhtml = '[tableType]';
		if (actid) {
			findhtml = '[tableType=' + actid + ']';
		}
		$(findhtml).each(function() {
			tableHtml += this.outerHTML;
		});
		Dialog.open({
			url : 'jsp-wf-pi-acthi',
			height : 500,
			width : 400,
			params : {
				table : tableHtml
			}
		});
	}
</script>
</head>
<body>
	<div xtype="border_layout">
		<div config="render : 'west' ,width:250" id="taskListdiv"></div>
		<div id="picdiv">
			<img src="wf-WFDef-showPic?id=<%=defid%>"
				onClick="javascript:imgclick();" />
			<%
				for (int i=wFHiTaskList.size()-1;i>-1;i--) {
					WFHiTask wFHiTask = wFHiTaskList.get(i);
					String actId = wFHiTask.getActId();
					NodeMap node = nodeMap.get(actId);
					Double x = node.getX();
					Double y = node.getY();
					Double width = node.getWidth();
					Double height = node.getHeight();
					String src = "";
					
					if("userTask".equals(wFHiTask.getActType())){
						if(wFHiTask.getIsEnd()==1){
							src = "/hhcommon/opensource/mxgraph/examples/editors/wfimg/userTask.gif";
						}else{
							src="/hhcommon/opensource/mxgraph/examples/editors/wfimg/userTask_ru.gif";
						}
					}else if("endEvent".equals(wFHiTask.getActType())){
						if(wFHiTask.getIsEnd()==1){
							src="/hhcommon/opensource/mxgraph/examples/editors/wfimg/end.gif";
						}
					}else if("endEventError".equals(wFHiTask.getActType())){
						if(wFHiTask.getIsEnd()==1){
							src="/hhcommon/opensource/mxgraph/examples/editors/wfimg/enderror.gif";
						}
					}
					
			%>
			<div
				style='position:absolute; border:0px solid ;left:<%=x%>px;top:<%=y%>px;width:<%=width%>px;height:<%=height%>px;'>
				<img onClick="javascript:imgclick('<%=actId%>');"
					style='cursor: pointer;' src="<%=src%>" />
			</div>
			<%
				}
			%>
		</div>
	</div>
</body>
</html>