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
		return '<table><tr><td>'+img+'</td><td>'+managerType+'</td></tr></table>';
	}
	
	function daterender(value,data){
		var durationInMillis = $.hh.millisecondTOHHMMSS($.hh.stringToDate(data.taskEndTime).getTime()-$.hh.stringToDate(data.taskStartTime).getTime());
		return durationInMillis;
	}
	
	
	function doRefresh() {
		$("#pagelist").loadData();
	}

	function doPipic() {
		$.hh.pagelist.callRow("pagelist", function(row) {
			WF.showpic(row.piid);
		});
	}
	function selectForm(){
		$.hh.pagelist.callRow("pagelist", function(row) {
			WF.showForm(row.piid);
		});
	}
</script>
</head>
<body>
	<div xtype="toolbar" config="type:'head'">
		 <span
			xtype="button" config="onClick: selectForm ,text:'查看', params : 'select' ,itype:'view' "></span>
		<span xtype="button" config="onClick:doPipic,text:'查看流程图' , icon : 'hh_img_image' "></span>
		<span xtype="button" config="onClick:doRefresh,text:'刷新' ,itype: 'refresh' "></span>
	</div>
	<div id="pagelist" xtype="pagelist"
		config=" radio:true, url: 'wf-WFOper-queryHiTaskPage' ,column : [
		{
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
			name : 'taskEndTime' ,
			text : '任务结束时间',
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