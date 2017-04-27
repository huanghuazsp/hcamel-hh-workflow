<%@page import="com.hh.system.util.Json"%>
<%@page import="com.hh.wf.service.impl.WFOperService"%>
<%@page import="com.hh.system.service.impl.BeanFactoryHelper"%>
<%@page import="com.hh.wf.bean.WFHiTask"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.hh.system.util.SystemUtil"%>
<%=SystemUtil.getBaseDoctype()%>
<%
WFOperService wfOperService = BeanFactoryHelper.getBeanFactory().getBean(WFOperService.class);
String piid = request.getParameter("piid");
List<WFHiTask> wFHiTaskList =  wfOperService.queryHiTaskListByPiid(piid);

Map<String,Map<String,String>> actHiUserMap = new HashMap<String,Map<String,String>>();

for(WFHiTask wfHiTask : wFHiTaskList){
	if(actHiUserMap.get(wfHiTask.getActId())==null){
		Map<String,String> userMap = new HashMap<String,String>(); 
		userMap.put("id", wfHiTask.getUserId());
		userMap.put("text", wfHiTask.getUserName());
		actHiUserMap.put(wfHiTask.getActId(), userMap);
	}
}
%>
<html>
<head>
<title>任务提交</title>
<%=SystemUtil.getBaseJs("checkform")%>
<script type="text/javascript">
	var height = 350;
	var width = 600;
	var params = $.hh.getIframeParams();
	var taskResult = params.taskResult;
	var serviceObject = taskResult.serviceObject;
	var task = taskResult.task;
	var nodeMap = taskResult.nodeMap;
	var currnode = nodeMap.currnode;
	
	var actHiUserMap = <%=Json.toStr(actHiUserMap)%>;

	function init() {
		//console.log(nodeMap);
		initPage();
	}
	function initPage() {
		var nextNodeList = getNextNodeList(currnode);
		renderSelectItems(nextNodeList);
	}
	
	function submitTypeChange(value){
		var nextNodeList = [];
		if(value==1){
			nextNodeList = getUpperNodeList(currnode);
		}else{
			nextNodeList = getNextNodeList(currnode);
		}
		renderSelectItems(nextNodeList);
	}
	
	function renderSelectItems(nextNodeList){
		var div = $('#maindiv');
		div.empty();
		getSelectItems(div, nextNodeList);
		if (nextNodeList.length == 1) {
			var input = $('#' + div.attr('id')).find('input');
			var label = $('#' + div.attr('id')).find(
					'[for=' + input.attr('id') + ']');
			input.trigger('click');
			if (!label.text()) {
				$('#span_' + div.attr('id')).hide();
			}
		}
	}

	function getSelectItems(div, nextNodeList) {
		var $span = $('<span wfselectRadioSpan=true id="span_' + div.attr('id') + '"></span>');
		for (var i = 0; i < nextNodeList.length; i++) {
			var nextNode = nextNodeList[i];
			var sourceNode = nextNode.sourceNode;
			var type = 'radio';
			var sequenceFlowLabel = nextNode.sequenceFlowLabel || '';
			var text = nextNode.label || '';
			var id = nextNode.id;

			var inputHtml = ' name = "' + sourceNode.id + '" ';

			var buttonhtml = '<input  type="'+type+'"  id="'+id+'" value="'+id+'"  '+inputHtml+'  />';
			var button = $(buttonhtml);
			var labelhtml = '';
			if (text || sequenceFlowLabel) {
				if(text && sequenceFlowLabel){
					sequenceFlowLabel=sequenceFlowLabel+'&nbsp;>>>&nbsp;';
				}
				labelhtml = '&nbsp;' +sequenceFlowLabel+ text + '&nbsp;&nbsp;';
			}
			var textspanhtml = '<label for="'+id+'">' + labelhtml + '</label>';
			var textspan = $(textspanhtml);
			button.data('node', nextNode);
			button.data('sourceNode', sourceNode);
			button
					.click(function() {
						var sourceNode1 = $(this).data('sourceNode');
						var node1 = $(this).data('node');
						var id = sourceNode1.id + '_div';
						$('#' + id).remove();
						var div1 = $('<div id="'+id+'" style="padding: 10px 5px"></div>')
						if (node1.type == 'userTask') {
							getUserField(div1, $(this).data('node'));
						} else {
							var nextNodeList1 = getNextNodeList($(this).data(
									'node'));
							getSelectItems(div1, nextNodeList1);
							if (nextNodeList1.length == 1) {
								var input = $('#' + div.attr('id')).find('input');
								var label = $('#' + div.attr('id')).find(
										'[for=' + input.attr('id') + ']');
								input.trigger('click');
								if (!label.text()) {
									$('#span_' + div.attr('id')).hide();
								}
							}
						}
						div.append(div1);
					});
			$span.append(button).append(textspan);
		}
		div.append($span);
	}

	function getUserField2(div, node,userList) {
		var userIds = '';
		if(userList ){
			for(var i=0;i<userList.length;i++){
				var user = userList[i];
				userIds+=user.id+',';
			}
			if(userIds){
				userIds=userIds.substr(0,userIds.length-1);
			}
		}
		var userName = 'userMap.' + node.id;
		var html = '<span>下一步骤：' + node.label + '<br/><br/></span>';
		var user = $('<span xtype="selectUser" config="userIds : \''+userIds+'\', name : \''+userName+'\',required :true,many:true"></span>');
		user.render();
		
		if(userList && userList.length==1){
			if(userList[0].id!='nouser'){
				user.setValue({
					id:userList[0].id,
					text:userList[0].text
				});
			}
		}
		
		if(actHiUserMap[node.id]){
			user.setValue(actHiUserMap[node.id]);
		}
		
		var userdiv = $(html).append(user);
		div.append(userdiv);
	}
	
	function getUserField(div, node) {
		
		var userIds_ = serviceObject['submitUserIds'];
		var userTexts_ = serviceObject['submitUserIdsText'];
		
		if(userIds_ && userTexts_){
			getUserField2(div, node,[{
				id:userIds_,text:userTexts_
			}]);
		}else if(node.serviceUserConfig){
			var serviceUserConfigList = $.hh.toObject(node.serviceUserConfig);
			var idList = [];
			for(var i=0;i<serviceUserConfigList.length;i++){
				var userCofnig_ = serviceUserConfigList[i];
				var userIds_ = serviceObject[userCofnig_.id];
				var userTexts_ = serviceObject[userCofnig_.id+'Text'];
				if(userIds_){
					var userIdArr = userIds_.split(',');
					var userTextArr = userTexts_.split(',');
					for(var j =0;j<userIdArr.length;j++){
						idList.push({
							id:userIdArr[j],
							text:userTextArr[j]
						});
					}
				}
			}
			if(idList.length==0){
				idList.push({id:'nouser'});
			}
			getUserField2(div, node,idList);
		}else if(node.userConfig){
			Request.request('wf-WFOper-queryUserListByUserConfig', {
				data : {
					userConfig:node.userConfig
				}
			}, function(userList) {
				if(userList && userList.length>0){
					getUserField2(div, node,userList);
				}else{
					getUserField2(div, node,[{id:'nouser'}]);
				}
				
			});
		}else{
			getUserField2(div, node)
		}
	}

	function getNextNodeList(node) {
		var nextNodeList = [];
		var sequenceFlowList = node.sequenceFlowList;
		if (sequenceFlowList) {
			for (var i = 0; i < sequenceFlowList.length; i++) {
				var sequenceFlowId = sequenceFlowList[i];
				var sequenceFlow = nodeMap[sequenceFlowId];
				var nextNode = nodeMap[sequenceFlow.targetRef];
				nextNode.sourceNode = node;
				nextNode.sequenceFlowLabel=sequenceFlow.label;
				nextNodeList.push(nextNode);
			}
		}
		return nextNodeList;
	}
	
	function getUpperNodeList(node){
		var returnNodeList = [];
		getUpperNodeList2(node,returnNodeList)
		for(var i =0;i<returnNodeList.length;i++){
			returnNodeList[i].sourceNode = node;
		}
		return returnNodeList;
	}
	
	function getUpperNodeList2(node,returnNodeList){
		var previousNodeList = node.previousNodeList||[];
		for (var i = 0; i < previousNodeList.length; i++) {
			var previousNode = previousNodeList[i];
			var previousNodeObject = nodeMap[previousNode];
			if(previousNodeObject && previousNodeObject.type!='startEvent'){
				returnNodeList.push(previousNodeObject);
				getUpperNodeList2(previousNodeObject,returnNodeList);
			}
		}
	}

	function submit() {
		var object = $("#maindiv").getValue();
		
		var submitData = serviceObject || {};
		
		var serviceMapKeys = '';
		for ( var p in submitData) {
			serviceMapKeys += p + ',';
		}
		if (serviceMapKeys != '') {
			submitData.serviceMapKeys = serviceMapKeys.substr(0,
					serviceMapKeys.length - 1);
		}
		
		delete submitData.createTime;
		delete submitData.updateTime;
		
		var wfselectRadioSpans = $('[wfselectRadioSpan=true]');
		
		for(var i=0;i<wfselectRadioSpans.length;i++){
			var nnn = wfselectRadioSpans[i];
			if($(nnn).find('input').length>0){
				var value = $(nnn).find('input:radio:checked').val();
				if(value){
					submitData['userMap.'+value] = '';
				}else{
					Dialog.infomsg('请选择步骤！');
					return;
				}
			}
		}
		
		
		for ( var p in object) {
			if (p.indexOf('userMap.') > -1 && $('#span_userMap\\.'+p.replace(/userMap./g,'')).length>0 ) {
				var valueData = $('#span_userMap\\.'+p.replace(/userMap./g,'')).getValueData();
				if(valueData){
					submitData[p] = $.hh.toString(valueData);
				}
				if (submitData[p] == null || submitData[p] == '' || submitData[p] == '[]') {
					Dialog.infomsg('请选择用户！');
					return;
				}
			}
		}
		submitData.taskId = task.id;
		
		Request.request('wf-WFOper-submit', {
			data : submitData
		}, function(result) {
			if (result.success!=false) {
				if(params.callback){
					params.callback();
				}
				Dialog.close();
			}
		});
	}
</script>
</head>
<body>
	<div xtype="hh_content">
		<div style="padding: 8px;text-align:center;">
		<span xtype="radio" 
		config="name: 'submitType',onChange:submitTypeChange,value : 0, data :[{id:0,text:'正常流转'},{id:1,text:'退回'}]"></span>
		</div>
		<div id="maindiv" style="border: 1px solid #cccccc; padding: 8px;"></div>
	</div>
	<div xtype="toolbar">
		<span xtype="button" config="text:'提交' , onClick : submit ,itype:'submit' "></span>
	</div>
</body>
</html>