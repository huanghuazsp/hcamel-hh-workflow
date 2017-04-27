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
			text : '流程设计',
			//img : '/hhcommon/images/extjsico/computer.gif',
			url : 'jsp-wf-wfinfo-page',
			onClick : onClick
		},{
			text : '已部署流程',
			//img : '/hhcommon/images/icons/application/application_osx.png',
			url : 'jsp-wf-wfdef-page',
			onClick : onClick
		},{
			text : '工作监控',
			//img : '/hhcommon/images/icons/application/application_osx.png',
			url : 'jsp-wf-pi-startlist',
			onClick : onClick
		}]
	};
	
	function onClick(){
		$('#system').attr('src',this.url);
	}
	
	
</script>
</head>
<body>
	<div xtype="border_layout">
		<div config="render : 'west',width:60,open:0">
			<span xtype=menu  configVar="emailMenu"></span>
		</div>
		<div style="overflow: visible;" id=centerdiv>
			<iframe id="system" name="system" width=100%
				height=100% frameborder=0 src="jsp-wf-wfinfo-page"></iframe>
		</div>
	</div>
</body>
</html>