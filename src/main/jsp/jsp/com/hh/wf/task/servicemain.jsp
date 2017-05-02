<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.hh.system.util.SystemUtil"%>
<%@page import="com.hh.system.util.pk.PrimaryKey"%>
<%=SystemUtil.getBaseDoctype()%>
<html>
<head>
<title>任务办理</title>
<%=SystemUtil.getBaseJs("layout","workflow")+SystemUtil.getUser()%>
<%
	String frameId = PrimaryKey.getUUID();
%>
<script type="text/javascript">
	var frameId = '<%=frameId%>';

	var params = $.hh.getIframeParams();
	var taskResult = params.taskResult;
	var callback = params.callback;
	var paramObject = params.param;
	var actionType = paramObject.actionType;

	var task = taskResult.task;
	var serviceId = task.serviceId;
	var nodeMap = taskResult.nodeMap;
	var currnode = nodeMap.currnode;

	var href = Request.getHref(currnode.href, {
		type : 'workflow',
		objectId : serviceId,
		actionType : actionType
	});
	
	var workflow = nodeMap.workflow;
	var iframe = null;
	function init() {
		iframe = $('<iframe id="'+frameId+'" name="'+frameId
				+'" frameborder=0	width=100% height=100% src="'+href+'"></iframe>');
		$.hh.setFrameParams(frameId, taskResult);
		if (actionType != 'manager') {
			$('#submit_btn').hide();
			$('#save_btn').hide();
		}
		
		$('[xtype=hh_content]').append(iframe);
		var frameWindow = window.frames[frameId];
		$.hh.iframeLoad(frameWindow,function(){
			var commentDiv = $('<fieldset style="width:700px;margin:0px auto;"><legend>意见</legend></fieldset>');
			
			loadComment(commentDiv);
			
			frameWindow.$('body').append(commentDiv);
		});
		
		renderUserList(taskResult.userList);
		

		
		if(workflow.describe){
			$('#workflow_describe').append('<div>流程描述：'+workflow.describe+'</div>');
		}
		if(currnode.describe && workflow.describe!=currnode.describe){
			$('#workflow_describe').append('<div>节点描述：'+currnode.describe+'</div>');
		}
		
		if(workflow.stepList){
			var stepListStr='';
			for(var i =0;i<workflow.stepList.length;i++){
				if(workflow.stepList[i]){
					stepListStr+=workflow.stepList[i]+'->';
				}
			}
			if(stepListStr){
				stepListStr=stepListStr.substr(0,stepListStr.length-2);
			}
			$('#workflow_describe').append('<div>流程步骤：'+stepListStr+'</div>');
		}
	}
	var commentTable = null;
	function loadComment(commentDiv){
		commentTable = $('<table xtype="form" style="width:100%;"></table>');
		
		
		Request.request('wf-WFComment-queryListByPiid', {
			data : {
				piid : task.piid
			},
			callback:function(commentList){
				var currComment = null;
				for(var i =0;i<commentList.length;i++){
					var comment = commentList[i];
					var tr = $('<tr><td style="text-align:right;width:100px;">'
					+(comment.actName||'')+'</td>'
							+'<td>'+
							(comment.comment||'')+'<br>'+
							$.hh.formatDate(comment.approveTime, 'yyyy-MM-dd HH:mm')+
							'-'
							+
							comment.userName
							+'</td>'
							+'</tr>');
					commentTable.append(tr);
					if(loginUser.id==comment.userId && task.id==comment.taskId){
						currComment = comment;
					}
				}
				var commentTr = $('<tr><td style="text-align:right;width:100px;">输入意见</td>'
						+'<td><input type="hidden" /><textarea style="height:70px;"></textarea></td>'
						+'</tr>');
				
				if(currComment){
					commentTr.find('input').val(currComment.id);
					commentTr.find('textarea').val(currComment.comment);
				}
				if (actionType == 'manager') {
					commentTable.append(commentTr);
				}
				
				
				commentTable.render();
				commentDiv.append(commentTable);
			}
		});
		
	}
	
	function userClick(){
			
	}
	
	function loadManagerUserList(){
		Request.request('wf-WFOper-queryListByTaskId', {
			data : {
				taskId : task.id
			},
			callback:function(userList){
				renderUserList(userList);
			}
		});
	}
	
	function renderUserList(userList){
		var dataList = [];
		for(var i=0;i<userList.length;i++){
			var userStr = '';
			//if(task.userId==userList[i].userId && currnode.jointlySign==false){
			//	userStr='[主办]';
			//}
			if(userList[i].isSubmit){
				userStr+='-已提交';
			}
			dataList.push({
				id:userList[i].userId,
				text:userList[i].userName+userStr,
				onClick:userClick,
				img : userList[i].read==1?'/hhcommon/images/icons/email/email_open.png':'/hhcommon/images/icons/email/email_close.gif'
			});
		}
		$('#userListSpan').render({
			data : dataList
		});
	}
	
	function save(close) {
		var iframe = window.frames[frameId];
		var formObject = iframe.save();
		if(formObject){
			var comment = commentTable.find('textarea').val();
			var commentId = commentTable.find('input').val();
			if(comment){
				Request.request('wf-WFComment-save', {
					async:false,
					data : {
						id : commentId,
						wfDefId : task.wfDefId,
						piid : task.piid,
						taskId : task.id,
						actName : task.actName,
						actId : task.actId,
						comment : comment
					},
					callback:function(commentObj){
						if(!commentObj.id){
							formObject=null;
							Dialog.infomsg('意见保存失败！');
						}
					}
				});
			}
		}
		if (close == null && formObject) {
			Dialog.close();
		}
		return formObject;
	}
	function submit() {
		var formObject = save(0);
		taskResult.serviceObject = formObject;
		
		if(currnode.jointlySign){
			Request.request('wf-WFOper-jointlySignSubmit', {
				data : {
					taskId : task.id
				},
				callback:function(jointlySignSubmitResult){
					if(jointlySignSubmitResult.length>0){
						Dialog.close();
					}else{
						submitPage();
					}
				}
			});
		}else{
			submitPage();
		}
	}
	
	function submitPage(){
		WF.openSubmitPage({
			taskResult : taskResult,
			callback : function() {
				if (callback) {
					callback();
				}
				Dialog.close();
			}
		});
	}
	
	function addManagerUser(){
		$.hh.selectUser.openUser({
			title:'添加经办人',
			callback : function(data) {
				Request.request('wf-WFOper-addUsers', {
					data : {
						wfDefId : task.wfDefId,
						taskId : task.id,
						userIds : data.id,
						userTexts : data.text,
						piid : task.piid,
						piName : task.piName,
						actName : task.actName
					},
					callback:function(){
						loadManagerUserList();
					}
				});
			},many:true
		});

	}
	var userListConfig={
			render :false,
			rightMenu : [ {
				text : '删除',
				img : $.hh.property.img_delete,
				onClick : function(resultObject) {
					if(resultObject.id==task.userId){
						Dialog.infomsg('主办人不能删除！');
						return;
					}
					Request.request('wf-WFOper-removeUsers', {
						data : {
							taskId : task.id,
							userIds : resultObject.id
						}
					},function(){
						loadManagerUserList();
					});
				}
			}  ]
	}
	
	function taskForward(){
		$.hh.selectUser.openUser({
			title:'任务转发',
			callback : function(data) {
				Request.request('wf-WFOper-taskForward', {
					data : {
						wfDefId : task.wfDefId,
						taskId : task.id,
						userIds : data.id,
						userTexts : data.text,
						piid : task.piid,
						piName : task.piName,
						actName : task.actName,
						actId : task.actId
					},
					callback:function(){
						if (callback) {
							callback();
						}
						Dialog.close();
					}
				});
			}
		});
	}
	
	function doPipic() {
		WF.showpic(task.piid);
	}
</script>
</head>
<body  xtype="border_layout">

	<div config="render : 'west' ,width:190">
		<div xtype="toolbar" config="type:'head'">
				<span xtype="button"
					config="width:'60%',text : '添加经办人' , onClick : addManagerUser  " ></span>
				<span xtype="button"
					config="width:'36%',text : '转发' , onClick : taskForward  " ></span>
		</div>
		<div xtype="toolbar" config="type:'head'">
				<span xtype="button" config="width:'100%',onClick:doPipic,text:'流程图'  "></span>
		</div>
		
		
		<span id="userListSpan" xtype=menu  configVar=" userListConfig "></span>
		
		<div id="workflow_describe" style="padding:5px;">
		</div>
		
	</div>
	<div>
	<div xtype="hh_content"  style="overflow: hidden;">
		
	</div>
	<div xtype="toolbar">
		<!-- <span xtype=menu    config=" id:'menuOper', data : [ 
		{ text : '添加经办人' , onClick : addManagerUser }
		]"></span>
		<span xtype="button"
					config=" text:'更多操作',icon : 'ui-icon-triangle-1-s' ,menuId:'menuOper' "></span> -->
	
	
		<span id="submit_btn" xtype="button"
			config="text:'提交' , onClick : submit ,itype:'submit' "></span> <span id="save_btn"
			xtype="button" config="text:'保存' , onClick : save ,itype:'save' "></span> <span
			id="cancel_btn" xtype="button"
			config="text:'取消' , onClick : Dialog.close ,itype:'close'  "></span>
	</div>
	</div>
</body>
</html>