<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.hh.system.util.SystemUtil"%>
<%=SystemUtil.getBaseDoctype()%>
<html>
<head>
<title>流程图</title>
</head>
<body>
	<img src="wf-WFDef-showPic?id=<%=request.getParameter("id")%>" />
</body>
</html>