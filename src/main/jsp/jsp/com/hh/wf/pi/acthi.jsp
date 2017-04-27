<%@page import="com.hh.system.util.SystemUtil"%>
<%@page import="com.hh.system.util.pk.PrimaryKey"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.google.gson.Gson"%>
<%=SystemUtil.getBaseDoctype()%>
<html>
<head>
<title>节点历史</title>
<%=SystemUtil.getBaseJs()%>
<%
	Gson gson = new Gson();
%>

<script type="text/javascript">
	var params = $.hh.getIframeParams();
	function init() {
		$('div').append(params.table);
	}

	
</script>
</head>
<body>
<div xtype="hh_content" style="overflow: visible;"></div>
</body>
</html>