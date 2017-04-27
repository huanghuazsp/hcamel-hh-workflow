<%@page import="com.hh.system.util.SystemUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.google.gson.Gson"%>
<%=SystemUtil.getBaseDoctype()%>
<html>
<head>
<title>流程设计</title>
<%=SystemUtil.getBaseJs("layout","ztree", "ztree_edit","gridviewService")%>
<%
	Gson gson = new Gson();
%>
<style type="text/css">
</style>
<script type="text/javascript">
	var queryAction = 'wf-WFlowInfo-queryList';
	function init() {
		loadData();
	}
	function refreshGridAndTree(data1){
		loadData(data1);
		$.hh.tree.refresh('tree');
	}
	var gridViewConfig = {
		margin : 10,
		rightMenu : [ {
			text : '编辑',
			img : $.hh.property.img_edit,
			onClick : function(data) {
				Dialog.open({
					url : 'jsp-wf-wfinfo-edit',
					params : {
						callback:refreshGridAndTree,
						node : data.node,
						id : data.id
					}
				});
			}
		}, {
			text : '删除',
			img : $.hh.property.img_delete,
			onClick : function(data) {
				Request.request('wf-WFlowInfo-deleteTreeByIds', {
					data : {
						ids : data.id
					}
				}, function(result) {
					if (result.success!=false) {
						refreshGridAndTree(result); 
					}
				});
			}
		} ],
		onClick : treeClick,
		update : function(id) {
			Request.request('wf-WFlowInfo-orderAll', {
				data : {
					ids : id
				}
			});
		},
		data : []
	};
	
	function treeClick(data){
			if (data.leaf == 1) {
				var param = {
					workflowName : data.text,
					saveUrl : 'wf-WFConfig-save',
					findUrl : 'wf-WFConfig-findObjectById',
					objectId : data.id
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

	function addWfType() {
		Dialog.open({
			url : 'jsp-wf-wfinfo-edit',
			params : {
				callback : refreshGridAndTree,
				node : requestParams.node
			}
		});
	}
	function querytree(){
		$('#span_tree').loadData({
			params : {text:$('#span_treeText').getValue()}
		});
	}

</script>
</head>
<body xtype="border_layout">
	<div config="render : 'west' ,width:230" style="overflow :hidden; ">
		<div xtype="toolbar" config="type:'head'">
			<table style="font-size: 12" width=100%  cellspacing="0" cellpadding="0" ><tr>
			<td >
			<span xtype="text" config=" name : 'treeText' ,enter: querytree"></span>
			</td>
			<td width="40px" style="text-align:right;">
			<span xtype="button" config=" icon :'hh_img_query' , onClick : querytree "></span>
			</td><tr></table>
		
		<span xtype="button"
					config="onClick: $.hh.tree.doUp , params:{treeid:'tree',action:'wf-WFlowInfo-order'}  , text:'上移' ,icon : 'hh_up' "></span>
		<span xtype="button"
					config="onClick: $.hh.tree.doDown , params:{treeid:'tree',action:'wf-WFlowInfo-order'} , text:'下移' ,icon : 'hh_down' "></span>
		
		</div>
			<span xtype="tree" config=" id:'tree', url:  queryAction ,onClick:treeClick,nheight:62   "></span>
	</div>
	<div>
	<div xtype="toolbar" config="type:'head'">
		<span id="backbtn" xtype="button"
			config="onClick: doBack ,text:'后退' , icon :'hh_img_left' "></span>
		<span xtype="button" config="onClick: addWfType ,text:'添加' , itype :'add' "></span>
		<span xtype="button" config="  itype : 'refresh' ,onClick: refresh ,text:'刷新'"></span>
	</div>
	<div style="padding: 25px;">
		<span id="gridView" xtype="gridView" configVar="gridViewConfig"></span>
	</div>
	
	</div>
</body>
</html>