<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<base href="<%=basePath%>">
	<link rel="stylesheet" href="static/js/scrollbar/default.css">
	<title>手机软件</title>
</head>
<body>
	<div id="responsive" style="height:1615px;width:834px;margin:auto;min-height:615px;min-width:834px;">
	</div>

	<script src="static/js/jquery-1.11.1.min.js"></script>
	<script src="static/js/scrollbar/jquery-mousewheel.js"></script>
	<script src="static/js/scrollbar/jquery-drag.js"></script>
	<script src="static/js/scrollbar/jquery-scrollbar.js"></script>
	<script>
		$('#responsive').scrollbar({
			height: 500
		});
	</script>


</body></html>