<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.hh.system.util.SystemUtil"%>
<%=SystemUtil.getBaseDoctype()%>
<html>
<head>
<%=SystemUtil.getBaseJs("workflow")%>
<script type="text/javascript">
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
	
	function renderUser(value,data){
		return data.ownerName+'>>>'+data.userName;
	}
	function renderOper(value,data){
		if(value==1){
			return '<a href="javascript:Withdraw(\''+data.id+'\');">撤回</a>';
		}else{
			return '';
		}
	}
	function Withdraw(value){
		Request.request('wf-WFOper-withdraw', {
			data : {
				id : value
			},
			callback:function(result){
				doRefresh();
			}
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
		config=" radio:true, url: 'wf-WFTaskForward-queryPagingData' ,column : [
		{
			name : 'piName' ,
			text : '流程名称',
			width : '40%'
		},{
			name : 'actName' ,
			text : '节点名称',
			width : '30%'
		},{
			name : 'ownerName' ,
			text : '转发人',
			width : '30%',
			render : renderUser
		},{
			name : 'isWithdraw' ,
			text : '操作',
			width : 40,
			render : renderOper
		}
	]">

	</div>
</body>
</html>