<%@page import="com.hh.system.util.Check"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.hh.system.util.SystemUtil"%>
<%=SystemUtil.getBaseDoctype()%>
<html>
<head>
<%=SystemUtil.getBaseJs("workflow")%>
<%
String piUrl = "wf-WFOper-queryMyPIPage";
String isALL = request.getParameter("all");
if(Check.isNoEmpty(isALL)){
	piUrl = "wf-WFOper-queryPIPage";
}
%>
<script type="text/javascript">

	function doPipic() {
		$.hh.pagelist.callRow("pagelist", function(row) {
			WF.showpic(row.id);
		});
	}
	
	function selectForm(){
		$.hh.pagelist.callRow("pagelist", function(row) {
			WF.showForm(row.id);
		});
	}

	function doRefresh() {
		$("#pagelist").loadData();
	}
	
	function doDelete(){
		$.hh.pagelist.deleteData({
			pageid : 'pagelist',
			action : 'wf-WFOper-deletePiByIds'
		});
	}
	
	function renderStatus(value,data) {
		var html = data.piEndTime==null?"<font class=hh_red>办理中</font>":"<font class=hh_green>已结束</font>";
		return html;
	}
	function daterender(value,data){
		var durationInMillis = '';
		if(data.piEndTime){
			durationInMillis = $.hh.millisecondTOHHMMSS($.hh.stringToDate(data.piEndTime).getTime()-$.hh.stringToDate(data.piStartTime).getTime());
			durationInMillis='<font class=hh_green>'+durationInMillis+'</font>';
		}else{
			durationInMillis = $.hh.millisecondTOHHMMSS($.hh.getDate().getTime()-$.hh.stringToDate(data.piStartTime).getTime());
			durationInMillis='<font class=hh_red>'+durationInMillis+'</font>';
		}
		return durationInMillis;
	}
</script>
</head>
<body>
	<div xtype="toolbar" config="type:'head'">
		 <span
			xtype="button" config="onClick: selectForm ,text:'查看' ,itype:'view'  "></span>
		<span xtype="button" config="onClick:doPipic,text:'查看流程图' ,icon:'hh_img_image' "></span>
		<span xtype="button" config="onClick:doDelete,text:'删除' ,itype:'delete' "></span>
		<span xtype="button" config="onClick:doRefresh,text:'刷新' ,itype:'refresh' "></span>
	</div>
	<div id="pagelist" xtype="pagelist"
		config=" url: '<%=piUrl %>' ,column : [
		{
			name : 'piEndTime1' ,
			text : '状态',
			width : 60,
			render : renderStatus
		},{
			name : 'piName' ,
			text : '流程名称'
		},{
			name : 'startUserName' ,
			text : '发起人',
			width : 100
		},{
			name : 'piStartTime' ,
			text : '创建时间',
			render : 'datetime',
			width : 130
		},{
			name : 'piEndTime' ,
			text : '结束时间',
			render : 'datetime',
			width : 130
		},{
			name : 'taskStartTime' ,
			text : '耗时',
			render :  daterender,
			width : 120
		}
	]">

	</div>
</body>
</html>