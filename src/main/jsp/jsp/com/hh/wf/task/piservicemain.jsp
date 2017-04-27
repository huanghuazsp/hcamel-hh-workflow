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
	var piResult = params.piResult;

	var hipi = piResult.hipi;
	var serviceId = hipi.serviceId;
	var nodeMap = piResult.nodeMap;

	var actionType = 'select';
	
	var href = Request.getHref(nodeMap.workflow.href, {
		type : 'workflow',
		objectId : serviceId,
		actionType : actionType
	});
	var iframe = null;
	function init() {
		iframe = $('<iframe id="'+frameId+'" name="'+frameId
				+'" frameborder=0	width=100% height=100% src="'+href+'"></iframe>');
		$.hh.setFrameParams(frameId, piResult);
		$('[xtype=hh_main_content]').append(iframe);
		var frameWindow = window.frames[frameId];
		$.hh.iframeLoad(frameWindow,function(){
			var commentDiv = $('<div style="margin-bottom:10px;"></div>');
			loadComment(commentDiv);
			frameWindow.$('body').append(commentDiv);
		});
	}
	var commentTable = null;
	function loadComment(commentDiv){
		commentTable = $('<table xtype="form" style="width:80%;margin:0px auto;"></table>');
		
		
		Request.request('wf-WFComment-queryListByPiid', {
			data : {
				piid : hipi.id
			},
			callback:function(commentList){
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
				}
				commentTable.render();
				commentDiv.append(commentTable);
			}
		});
		
	}
	
	
</script>
</head>
<body>

	<div xtype="hh_main_content"  style="overflow: hidden;">
		
	</div>
</body>
</html>