<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.hh.system.util.SystemUtil"%>
<%@page import="com.google.gson.Gson"%>
<%=SystemUtil.getBaseDoctype()%>
<html>
<head>
<title>已部署的流程</title>
<%=SystemUtil.getBaseJs("layout","ztree","gridviewService")%>
<%
	Gson gson = new Gson();
%>
<style type="text/css">
</style>
<script type="text/javascript">
	var queryAction = 'wf-WFlowInfo-queryWfDefTreeList';
	paramKeys = ['iconSkin','text','params'];
	function init() {
		loadData();
	}
	var gridViewConfig = {
		margin : 10,
		rightMenuCond : {leaf:1},
		rightMenu : [ {
				text : '删除',
				img : $.hh.property.img_delete,
				onClick : function(data) {
					Request.request('wf-WFDef-deleteByIds', {
						data : {
							ids : data.id
						}
					}, function(result) {
						if (result.success!=false) {
							loadData(result);
							$.hh.tree.refresh('tree');
						}
					});
				}
			}
		 ],
		onClick : treeClick,
		data : []
	};

	function querytree(){
		$('#span_tree').loadData({
			params : {text:$('#span_treeText').getValue()}
		});
	}
	function treeClick(data){
		if (data.leaf == 1) {
			var param = {
				workflowName : data.workflowName,
				saveUrl : 'wf-WFDef-save',
				findUrl : 'wf-WFDef-findObjectById',
				objectId : data.id,
				hipd:"2"
			};
			$.hh.addTab({
				id : data.id,
				text :  document.title+'-'+data.text,
				src : 'jsp-workflow-wfdes-workfloweditor?' + $.param(param)
			});
		} else {
			if(requestParams.node != data.id){
				requestParams.node = data.id;
				loadData();
			}else{
				refresh();
			}
		}
}
</script>
</head>
<body xtype="border_layout">
	<div config="render : 'west' ,width:230">
		<div style="padding:2px;">
			<table style="font-size: 12" width=100%  cellspacing="0" cellpadding="0" ><tr>
			<td >
			<span xtype="text" config=" name : 'treeText' ,enter: querytree"></span>
			</td>
			<td width="40px" style="text-align:right;">
			<span xtype="button" config=" icon :'hh_img_query' , onClick : querytree "></span>
			</td><tr></table>
		</div>
			<span xtype="tree" config=" id:'tree', url:  queryAction ,onClick:treeClick  "></span>
	</div>
	<div>
	<div xtype="toolbar" config="type:'head'">
		<span id="backbtn" xtype="button"
			config="onClick: doBack ,text:'后退' , icon :'hh_img_left' "></span>
		<span xtype="button" config=" itype: 'refresh' ,onClick: refresh ,text:'刷新' ,itype:'refresh' "></span>
	</div>
	<div style="padding: 25px;">
		<span id="gridView" xtype="gridView" configVar="gridViewConfig"></span>
	</div>
	</div>
</body>
</html>