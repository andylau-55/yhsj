<%@page import="com.yhsjedu.datacloud.core.entity.Users"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="page-container">
	<div class="sidebar-menu fixed">
		
		<div class="sidebar-menu-inner">	
			<header class="logo-env" style="padding: 10px 0px;">
				<div class="logo">
					<a href="/" class="logo-expanded">
						<img src="/static/images/favicon.png" width="120" alt="" style="float: left;margin-left: 30px;"/>
					</a>
					
					<a href="/" class="logo-collapsed">
						<img src="/static/images/favicon.png" width="30" alt="" />
					</a>
				</div>
				<div class="mobile-menu-toggle visible-xs" style="margin: 10px 20px 0px 10px;">
                    
                    <a href="#" data-toggle="mobile-menu">
                        <i class="fa-bars"></i>
                    </a>
                </div>
				<div class="mobile-menu-toggle visible-xs" style="margin: 10px 20px 0px 10px;">
					
					<a onclick="logout();" style="cursor:pointer">
						<i class="fa-sign-out"></i>
					</a>
				</div>
			</header>
			
			
			<ul id="main-menu" class="main-menu" style="margin-top:0px;">
				<li class="top-uk-menu opened">
					<a href="">
						<i class="fa-pagelines"></i>
						<span class="title">页面管理</span>
					</a>
					<ul>
					   <li class="uk-menu">
                            <a href="admin/index" name="menu">
                                <i class="fa-tachometer"></i>
                                <span class="title">菜单管理</span>
                            </a>
                        </li>
						<li class="uk-menu">
							<a href="admin/video" name="menu">
								 <i class="fa-video-camera"></i>
								<span class="title">视频教程</span>
							</a>
						</li>
						<li class="uk-menu">
							<a href="admin/picture" name="menu">
								<i class="fa-picture-o"></i>
								<span class="title">图文教程</span>
							</a>
						</li>
						<li class="uk-menu">
							<a href="admin/teacher" name="menu">
								<i class="fa-user-md"></i>
								<span class="title">老师阵容</span>
							</a>
						</li>
						<li class="uk-menu">
							<a href="admin/students" name="menu">
								<i class="fa-users"></i>
								<span class="title">学员作品</span>
							</a>
						</li>
						<li class="uk-menu">
							<a href="admin/course" name="menu">
								<i class="fa-th-list"></i>
								<span class="title">课程列表</span>
							</a>
						</li>
						<li class="uk-menu">
							<a href="admin/apply" name="menu">
								<i class="fa-heart"></i>
								<span class="title">立即报名</span>
							</a>
						</li>
						<li class="uk-menu">
							<a href="admin/about" name="menu">
								<i class="fa-yelp"></i>
								<span class="title">关于我们</span>
							</a>
						</li>
					</ul>
				</li>
				
				<li class="top-uk-menu opened">
					<a href="">
						<i class="fa-suitcase"></i>
						<span class="title">系统管理</span>
					</a>
					<ul>
					   <li class="uk-menu">
                            <a href="admin/user" name="menu">
                                <i class="fa-user"></i>
                                <span class="title">用户管理</span>
                            </a>
                        </li>
					</ul>
				</li>
			</ul>
					
		</div>
		
	</div>	
	<div class="main-content" id="main-content">
		<nav class="navbar user-info-navbar" role="navigation">
			
			<ul class="user-info-menu left-links list-inline list-unstyled">
				<li class="hidden-sm hidden-xs">
					<a href="#" class="wave in" data-toggle="sidebar" style="padding-top: 22px;padding-bottom: 14px;">
						<i class="fa-bars"></i>
					</a>
				</li>
				<li class="hidden-sm hidden-xs">
					<a class="fullscreen" id="fullscreen-toggler" href="#" style="padding-top: 22px;padding-bottom: 14px;">
                    	<i class="fa-arrows-alt"></i>
                	</a>
				</li>
			</ul>
			
			<ul class="user-info-menu right-links list-inline list-unstyled">
				<% Users user = (Users) request.getSession().getAttribute("users");
					if(user == null){%>
					<li>
		                <a href="/common/login" style="padding-bottom:5px;">
		                	<img src="/static/images/a.jpg"  class="img-circle img-inline userpic-32" width="28">
		                    &nbsp;&nbsp;登录
		                </a>
		            </li>
				  <%}else{
				  	String userDspName = user.getUserDspName();
				  	if(userDspName==null||userDspName.length()==0){
				  		userDspName = user.getUserName();
				  	}
				  %>
					<li>
		                <a style="padding-bottom:5px;">
		                	<img src="/static/images/a.jpg"  class="img-circle img-inline userpic-32" width="28">
		                    	欢迎您&nbsp;&nbsp;<%=userDspName %>
		                </a>
		            </li>
		            <li>
		                <a onclick="logout();" style="padding-top: 22px;padding-bottom: 14px;cursor:pointer;">
		                    <i class="linecons-lock"></i>&nbsp;&nbsp;注销
		                </a>
		            </li>
				  <%}
				%>
			</ul>
		</nav>
		<script type="text/javascript">
		
		function logout(){
			if(confirm("确定要退出吗？")){
				window.location.replace("/admin/user/logout");
			}else{
				return false;
			}
		}
		$(document).ready(function(){
		    resizeModal();
		});
		
		function resizeModal(){
		    $(".modal-body").css('height', $(window).height()-110);
		}
		
		</script>