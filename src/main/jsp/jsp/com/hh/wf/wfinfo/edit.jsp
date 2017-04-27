<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.hh.system.util.SystemUtil"%>
<%=SystemUtil.getBaseDoctype()%>
<html>
<head>
<title>流程编辑</title>
<%=SystemUtil.getBaseJs("checkform")%>
<script type="text/javascript">
	var params = $.hh.getIframeParams();
	var width = 600;
	var height = 350;
	
	var queryData = null;
	
	function save() {
		$.hh.validation.check('form', function(formData) {
			if(queryData){
				$.extend(queryData,formData);
				delete queryData.createTime;
				delete queryData.updateTime;
				formData=queryData;
			}
			formData.id=params.id;
			//formData.node=params.node;
			Request.request('wf-WFlowInfo-saveTree', {
				data : formData,
				callback : function(result) {
					if(result.success!=false){
						params.callback(result);
						Dialog.close();
					}
				}
			});
		});
	}
	
	function findData(id){
		Request.request('wf-WFlowInfo-findObjectById', {
			data : {id:params.id},
			callback : function(result) {
				queryData=result;
				$('#form').setValue(result);
			}
		});
	}
	
	function init(){
		
		if(params.node){
			$('#node_span').setValue({
				id:params.node
			});
		}
		if(params.id){
			findData(params.id);
		}
	}
</script>
</head>
<body>
	<div xtype="hh_content">
		<form id="form" xtype="form" class="form">
			<table xtype="form">
				<tbody>
					<tr>
						<td xtype="label">上级节点：</td>
						<td><span id="node_span" xtype="selectTree"
							config="name: 'node' , findTextAction : 'wf-WFlowInfo-findObjectById' , url : 'wf-WFlowInfo-queryTreeListType' "></span>
						</td>
					</tr>
					<tr>
						<td xtype="label">名称：</td>
						<td><span xtype="text" config=" name : 'text',required :true"></span></td>
					</tr>
					<tr>
						<td xtype="label">类型：</td>
						<td><span id="leafspan" xtype="radio"
							config="name: 'leaf' ,value : 0, data :[{id:1,text:'流程'},{id:0,text:'类别'}]"></span></td>
					</tr>
					<tr>
					<td xtype="label">是否展开：</td>
						<td><span xtype="radio"
							config="name: 'expanded' ,value : 0,  data :[{id:1,text:'是'},{id:0,text:'否'}]"></span></td>
					</tr>
					<tr>
					<td xtype="label">是否隐藏：</td>
						<td><span xtype="radio"
							config="name: 'isHide' ,value : 0,  data :[{id:1,text:'是'},{id:0,text:'否'}]"></span></td>
					</tr>				
				</tbody>
			</table>
		</form>
	</div>
	<div xtype="toolbar">
		<span xtype="button" config="text:'保存' , onClick : save , itype :'save'  "></span>
		<span xtype="button" config="text:'取消' , onClick : Dialog.close , itype :'close' "></span>
	</div>
</body>
</html>