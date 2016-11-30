<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<base href="<%=basePath%>">
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<meta name="description" content="Xenon Boostrap Admin Panel" />
		<meta name="author" content="" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta name="renderer" content="webkit">
		
		<link rel="shortcut icon" href="/static/images/favicon1.png" />
		<link rel="stylesheet" href="static/css/fonts/linecons/css/linecons.css">
		<link rel="stylesheet" href="static/css/fonts/fontawesome/css/font-awesome.min.css">
		<link rel="stylesheet" href="static/css/bootstrap.css">
		<link rel="stylesheet" href="static/css/xenon-core.css">
		<link rel="stylesheet" href="static/css/xenon-skins.css">
		<link rel="stylesheet" href="static/css/custom.css">
		<link rel="stylesheet" href="static/css/xenon-forms.css">
		<link rel="stylesheet" href="static/css/xenon-components.css">
	    <script src="static/js/dateRange/jquery.min.js" type="text/javascript"></script>
	    <script src="static/js/highcharts/highcharts.js" type="text/javascript"></script>
		<script src="static/js/datatables/js/jquery.dataTables.js" type="text/javascript"></script> 
		<script src="static/js/jquery.validate.min.js" type="text/javascript"></script>
		<script src="static/js/un-common.js"></script>
		<title>云海书径-<sitemesh:title/></title>
		<sitemesh:head />
	</head>

	<body class="page-body">
		<div class="wrapper">  
			<%--页面部分--%>
			<sitemesh:body />
			
			<%--公共底部--%>
			<%@ include file="admin/footer.jsp" %>
		</div>
	</body>
</html>
