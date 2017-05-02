<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.hh.system.util.SystemUtil"%>
<%=SystemUtil.getBaseDoctype()%>
<html>
<head>
<%=SystemUtil.getBaseJs("workflow")%>
<script type="text/javascript">
	function readrender(value,data) {
		var img = value == 0 ? '<img src="'+$.hh.property.img_email_close+'" />'
				: '<img src="'+$.hh.property.img_email_open+'" />';
		var managerType = data.managerType=='main'?'主办':'经办';
		if(data.isSubmit){
			managerType='<font color=green>'+managerType+'</font>';
		}
		return '<table><tr><td>'+img+'</td><td>'+managerType+'</td></tr></table>';
	}
	function daterender(value,data){
		var durationInMillis = $.hh.millisecondTOHHMMSS($.hh.getDate().getTime()-$.hh.stringToDate(data.taskStartTime).getTime());
		return durationInMillis;
	}
	function doRefresh() {
		$("#pagelist").loadData();
	}
	function doManager(actionType) {
		var row = $.hh.pagelist.getSelectData("pagelist");
		if (row) {
			WF.manager({
				id : row.id,
				actionType : actionType,
				callback : doRefresh
			});
		} else {
			Dialog.infomsg("请选择一条任务办理！");
		}
	}

	function doPipic() {
		$.hh.pagelist.callRow("pagelist", function(row) {
			WF.showpic(row.piid);
		});
	}
</script>
</head>
<body>
	<div xtype="toolbar" config="type:'head'">
		<span xtype="button"
			config="onClick:doManager,text:'办理' , params : 'manager'  ,itype:'manager'"></span> <span
			xtype="button" config="onClick:doManager,text:'查看', params : 'select' ,itype:'view' "></span>
		<span xtype="button" config="onClick:doPipic,text:'查看流程图' , icon : 'hh_img_image' "></span>
		<span xtype="button" config="onClick:doRefresh,text:'刷新'  ,itype: 'refresh' "></span>
	</div>
	<div id="pagelist" xtype="pagelist"
		config=" radio:true, url: 'wf-WFOper-queryRuTaskPage' ,column : [
		{
			name : 'read' ,
			text : '状态',
			width:60,
			render : 'readrender'
		},{
			name : 'piName' ,
			text : '流程名称',
			width : '60%'
		},{
			name : 'actName' ,
			text : '节点名称',
			width : '40%'
		},{
			name : 'startUserName' ,
			text : '发起人',
			width : 100
		},{
			name : 'ownerName' ,
			text : '发送人',
			width : 100
		},{
			name : 'taskStartTime' ,
			text : '任务创建时间',
			render : 'datetime',
			width : 150
		},{
			name : 'taskStartTime1' ,
			text : '耗时',
			render :  daterender,
			width : 100
		}
	]">

	</div>
</body>
</html>