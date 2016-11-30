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
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta name="renderer" content="webkit">
		<meta name="author" content="" />
		
		<link rel="shortcut icon" href="/static/images/favicon1.png" />
		<link rel="stylesheet" href="static/css/fonts/linecons/css/linecons.css">
		<link rel="stylesheet" href="static/css/fonts/fontawesome/css/font-awesome.min.css">
		<link rel="stylesheet" href="static/css/bootstrap.css">
		<link rel="stylesheet" href="static/css/xenon-core.css">
		<link rel="stylesheet" href="static/css/xenon-skins.css">
		<link rel="stylesheet" href="static/css/custom.css">
		<link rel="stylesheet" href="static/css/xenon-forms.css">
		<link rel="stylesheet" href="static/css/xenon-components.css">
		<link rel="stylesheet" href="static/css/uikit.almost-flat.addons.css">
		<link rel="stylesheet" href="static/css/gri.controls.css" rel="stylesheet" type="text/css" />
	    <script src="static/js/dateRange/jquery.min.js" type="text/javascript"></script>
	    <script src="static/js/dateRange/gri.dateRange.js" type="text/javascript"></script>
	    <script src="static/js/highcharts/exporting.js" type="text/javascript"></script>
	    <script src="static/js/highcharts/highcharts.js" type="text/javascript"></script>
		<link rel="stylesheet" href="static/js/datatables/dataTables.bootstrap.css">
		<link rel="stylesheet" href="static/js/datatables/css/dataTables.responsive.css">
		<script src="static/js/datatables/js/jquery.dataTables.js" type="text/javascript"></script> 
	    <script src="static/js/datatables/dataTables.bootstrap.js"></script>
	    <script src="static/js/datatables/dataTables.tableTools.min.js" type="text/javascript" charset="utf-8"></script>
	    <script src="static/js/datatables/js/dataTables.responsive.min.js"></script>
	    <link rel="stylesheet" href="static/css/flowchart.css">
		<script src="static/js/flowchart.js"></script>
	    <link rel="stylesheet" href="static/js/multiselect/multiselect.css">
		<script src="static/js/multiselect/multiselect.js" type="text/javascript"></script>
		<script src="static/js/jquery.validate.min.js" type="text/javascript"></script>
		<script src="static/js/bootstrap-paginator.js"></script>
		<script src="/static/js/handlebars-v3.0.1.js" type="text/javascript"></script>
		
		<link rel="stylesheet" href="/static/js/select2/select2.css">
		<link rel="stylesheet" href="/static/js/select2/select2-bootstrap.css">
		<script type="text/javascript" src="/static/js/toastr.min.js"></script>
		
		<script type="text/javascript" src="/static/js/select2/select2.min.js"></script>
		
		<title>云海书径-<sitemesh:title/></title>
		<sitemesh:head />
	</head>

	<body class="page-body user-info-navbar-skin-aero">
		<%--公共头部--%>
		<%@ include file="admin/head.jsp" %>
		
		<%--页面部分--%>
		<sitemesh:body />
		
		<%--公共底部--%>
		<%@ include file="admin/footer.jsp" %>
		
	</body>
</html>
