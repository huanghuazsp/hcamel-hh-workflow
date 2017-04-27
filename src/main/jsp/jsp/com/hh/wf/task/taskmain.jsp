<%@page import="com.hh.system.service.impl.BeanFactoryHelper"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.hh.system.util.SystemUtil"%>
<%=SystemUtil.getBaseDoctype()%>
<html>
<head>
<title>表单设计</title>
<%=SystemUtil.getBaseJs("layout")%>
<script type="text/javascript">
	var emailMenu = {
		data : [ {
			text : '新建工作',
			img : '/hhcommon/images/icons/application/application_form_add.png',
			url : 'jsp-wf-pi-startview',
			onClick : onClick
		}, {
			text : '我发起的',
			img : '/hhcommon/images/icons/table/table_multiple.png',
			url : 'jsp-wf-pi-startlist',
			onClick : onClick
		},  {
			text : '待办工作',
			img : '/hhcommon/images/extjsico/txt.gif',
			url : 'jsp-wf-task-rutasklist',
			onClick : onClick
		},{
			text : '已办任务',
			img : '/hhcommon/images/extjsico/16551842.gif',
			url : 'jsp-wf-task-hitasklist',
			onClick : onClick
		},{
			text : '我发送的',
			img : '/hhcommon/images/extjsico/16551853.gif',
			url : 'jsp-wf-task-ownertasklist',
			onClick : onClick
		},{
			text : '我转发的',
			img : '/hhcommon/images/icons/arrow/arrow_redo.png',
			url : 'jsp-wf-task-taskforwardlist',
			onClick : onClick
		}
		]
	};
	
	function onClick(){
		$('#system').attr('src',this.url);
	}
	
	
</script>
</head>
<body>
	<div xtype="border_layout">
		<div config="render : 'west',width:140">
			<span xtype=menu  configVar="emailMenu"></span>
		</div>
		<div style="overflow: visible;" id=centerdiv>
			<iframe id="system" name="system" width=100%
				height=100% frameborder=0 src="jsp-wf-task-rutasklist"></iframe>
		</div>
	</div>
</body>
</html>