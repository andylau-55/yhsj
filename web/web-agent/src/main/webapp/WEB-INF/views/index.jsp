<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" class="fsvs demo">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="Keywords" Content="云海书径,西天取经ps,三藏老师 ,设计大爆炸 ,大爆炸实验室 ">
		<meta name="description" itemprop="description" content="北京云海书径教育咨询有限公司是一家专业的在线教育培训机构，本机构致力于设计师职业技能方向的培训，我们的师资团队来自于各大互联网公司，例如腾讯、百度、网易、阿里、UC、微软等，本机构采用传承式教学模式，以服务学生为己任，培养业内优秀设计师。">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1" />
		<meta name="apple-mobile-web-app-capable" content="yes" />
    	<meta name="apple-mobile-web-app-status-bar-style" content="black" />
    	<title>云海书径</title>
        <link rel="stylesheet" href="/static/index/css/index.css">
		<link rel="stylesheet" href="/static/index/css/lrtk.css">
        <link rel="stylesheet" href="/static/scrollbar/default.css">
		<script src="/static/index/js/jquery.min.js"></script>
		<script src="/static/index/js/jquery.cookie.js"></script>
		<script src="/static/index/js/jquery-migrate-1.2.1.min.js"></script>
		<script src="/static/index/js/jquery.swipe-events.js"></script>
		<script src="/static/index/js/fsvs.js"></script>
		<script src="/static/index/js/main.js"></script>
		<script src="/static/scrollbar/jquery-mousewheel.js"></script>
		<script src="/static/scrollbar/jquery-drag.js"></script>
		<script src="/static/scrollbar/jquery-scrollbar.js"></script>
	</head>
	<body class="fsvs">
		<div class="top">
        	<div class="top_logo"><img src="/static/images/top_logo.png"></div>
            <div class="top_nav">
              <ul class="onepage-list">
              	<c:set value="0" var="sum0" /> 
              	<c:forEach var="replace" items="${replaces}">
              		<c:if test="${replace.type == 1}">
              			<c:set value="${sum0 + 1}" var="sum0" /> 
              			<style type="text/css">
							.top_nav .menu${replace.id}:hover  {
								color: ${replace.hoverColour} !important;
							}
							.top_nav .active .menu${replace.id}  {
								color: ${replace.hoverColour} !important;
							}
							.slide.nth-class-${sum0} {
								background: ${replace.content} !important;
							}
						</style>
              			<li class="onepage_li"><a id="${replace.id}" class="nav_menu active menu${replace.id}" target="_self" data-index="1"  style="color: ${replace.colour};" href="${replace.url}"><div class="onepage_div">${replace.title}</div></a></li>
              		</c:if>
              	</c:forEach>
             </ul>
            </div>
        </div>
        
		<div id="fsvs-body">
			<div class="slide">
				<div class="slide1">
					<c:forEach var="replace" items="${replaces}">
		           		<c:if test="${replace.type == 7 && replace.pageOrder == 11}">
				            <div class="slide1-back">
								<div class="slide1-back-img"><img src="${replace.img}"></div>
							</div>
				      	</c:if>
				      	<c:if test="${replace.type == 7 && replace.pageOrder == 12}">
				            <div class="slide1-back-font1"><img src="${replace.img}"></div>
				      	</c:if>
				      	<c:if test="${replace.type == 7 && replace.pageOrder == 13}">
				            <div class="slide1-back-font2"><img src="${replace.img}"></div>
				      	</c:if>
				      	
	            	</c:forEach>
				</div>
			</div>
		   
			<div class="slide">
				<div class="slide2">
					<div class="slide2-left">
						<div class="slide2-left-top" id="slide2-left-top">
						</div>
						<div class="slide2-left-boot" id="slide2-left-boot">
							<iframe id="video_iframe" class="video_iframe" src="" allowfullscreen="" frameborder="0" height="100%" width="100%"></iframe>
						</div>
					</div>
					
					<div class="slide2-right">
						<div class="slide2-right-top">
							<div id="video" class="li act" onclick="setHistory(1)">教程列表</div>
							<div id="history" class="li" onclick="setHistory(2)">最近播放</div>
						</div>
						<div id="videoList" class="slide2-right-boot">
							<div id="scrollList" class="menu-list">
								<ul id="menu-ul">
									<c:forEach var="replace" items="${replaces}">
										<c:if test="${replace.type == 2}">
			    							<li name="tagsname" title="${replace.title}" ><div onclick="setIframe(1,'${replace.title}','${replace.url}',this)" class="tags" title="${replace.title}"><a>${replace.title}</a></div></li>
			    						</c:if>
	    							</c:forEach>
	    						</ul>
							</div>
						</div>
						
						<div id="historyList" class="slide2-right-boot seahos">
							<div id="scrollList1" class="menu-list">
								<ul id="menu-ul1">
								
	    						</ul>
							</div>
						</div>
						
					</div>
					
				</div>
				
			</div>
            
            <div class="slide">
				<div class="slide3">
					<div class="slide3-main">
						<div class="slide3-top">
							<div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div>
							<div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div>
							<div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div>
							<div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div>
							<div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div>
							<div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div>
							<div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div>
							<div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div><div class="slide3-stripe"></div>
							<div class="slide3-stripe-last"></div>
						</div>
						<c:forEach var="replace" items="${replaces}">
							<c:if test="${replace.type == 3 && replace.pageOrder == 1}">
								<div class="slide3-mid">
							</c:if>
								<c:if test="${replace.type == 3 && replace.pageOrder <= 100}">
									<div class="slide3-mid-pic">
										<div class="slide3-mid-img"><a target="_blank" href="${replace.url}" title="${replace.title}"><img src="${replace.img}"></a></div>
										<div class="slide3-mid-font">
											<div class="slide3-mid-font">
												<a target="_blank" href="${replace.url}" title="${replace.title}"><div class="slide3-mid-font-top">${replace.title}</div></a>
												<div class="slide3-mid-font-main">
													${replace.content}
												</div>
											</div>
										</div>
									</div>
								</c:if>
							<c:if test="${replace.type == 3 && replace.pageOrder == 200}">
								</div>
						
								<div class="slide3-foot">
									<div class="slide3-foot-font"><img src="${replace.img}"></div>
									<a target="_blank" href="${replace.url}">
										<div class="slide3-foot-button">${replace.title}</div>
									</a>
								</div>
							</c:if>
						</c:forEach>
					</div>
				</div>
			</div>
            
            <div class="slide">
				<div class="slide4">
					<c:set value="0" var="sum4" /> 
					<c:forEach var="replace" items="${replaces}">
						<c:if test="${replace.type == 4}">
							<c:set value="${sum4 + 1}" var="sum4" /> 
							<div class="slide4-pic <c:if test="${sum4%4==0}">slide4-pic-last</c:if>">
								<a target="_blank" href="${replace.url}">
									<div class="slide4-por-img"><img src="${replace.img}"></div>
								</a>
								<a target="_blank" href="${replace.url}">
									<div class="slide4-por-head">
										${replace.title}
									</div>
								</a>
								<div class="slide4-desc">
									${replace.content}
								</div>
								<a target="_blank" href="${replace.url}">
									<div class="slide4-button">
									查看老师课程
									</div>
								</a>
							</div>
						</c:if>
	    			</c:forEach>
	    			
				</div>
			</div>
            
            <div class="slide">
				<div class="slide5">
					<c:set value="0" var="sum5" /> 
					<c:forEach var="replace" items="${replaces}">
						<c:if test="${replace.type == 5}">
							<c:set value="${sum5 + 1}" var="sum5" /> 
							<div class="slide5-pic <c:if test="${sum5%4==0}">slide5-pic-last</c:if>">
								<a target="_blank" href="${replace.url}" title="${replace.title}">
									<div class="slide5-pic-img"><img src="${replace.img}"></div>
								</a>
							</div>
						</c:if>
					</c:forEach>
				</div>
			</div>
            
            <div class="slide">
				<div class="slide6">
					<c:set value="0" var="sum6" /> 
					<c:forEach var="replace" items="${replaces}">
						<c:if test="${replace.type == 6}">
							<c:set value="${sum6 + 1}" var="sum6" /> 
							<div class="slide6-pic  <c:if test="${sum6%3==0}">slide6-pic-last</c:if>">
								<div class="slide6-pic-img"><img src="${replace.img}"></div>
								<div class="slide6-pic-font">
									<div class="slide6-pic-head">${replace.title}</div>
									<div class="slide6-pic-desc">
										${replace.content}
									</div>
									<a target="_blank" href="${replace.url}" >
										<div class="slide6-pic-button">立即学习</div>
									</a>
								</div>
							</div>
						</c:if>
					</c:forEach>
				</div>
			</div>
            
            <div class="slide">
            	<div class="slide7">
	            	<c:forEach var="replace" items="${replaces}">
	            	
		           		<c:if test="${replace.type == 7 && replace.pageOrder == 22}">
				            	<div class="slide7-back">
				            		<div class="slide7-back-font1"><img src="${replace.img}"></div>
				            	</div>
				      	</c:if>
				      		<c:if test="${replace.type == 7 && replace.pageOrder == 23}">
				            	<div class="slide7-back-font2"><img src="${replace.img}"></div>
				       		</c:if>
				       		
				       		<c:if test="${replace.type == 7 && replace.pageOrder == 24}">
			            		<style type="text/css">
									.slide7-font3-button:hover  {
										background-color: ${replace.hoverColour} !important;
									}
								</style>
			            		<div class="slide7-font3-button">
			            			<a style="color: ${replace.colour};" target="_blank" href="${replace.url}" >${replace.title}</a>
			            		</div>
				           	</c:if>
				           	
				            <c:if test="${replace.type == 7 && replace.pageOrder == 25}">
								<div class="slide7-back-font4" >
									${replace.content}
								</div>
							</c:if>
		            	
	            	</c:forEach>
            	</div>
			</div>
            
            <div class="slide">
            	<div class="slide8">
            		<c:forEach var="replace" items="${replaces}">
            		<c:if test="${replace.type == 8 && replace.pageOrder == 1}">
	            		<div class="slide8-left">
	            			<div class="slide8-img"><img src="${replace.img}"></div>
	            		</div>
	            		<div class="slide8-right">
	            			${replace.content}
	            		</div>
	            	</c:if>
            		</c:forEach>
            	</div>
            	<!-- <div class="recordcode">京ICP备16011450号</div> -->
			</div>

		</div>
		<script type="text/javascript">
			var video_height = $("#video_iframe").height();
			jQuery(document).ready(function($){
				var slidewidth = $(".slide2-right").width();
				$('#scrollList').scrollbar({
					height: video_height,
					width: slidewidth,
					speed:100,
					canCrossBoundary: false,
					duration:10
				});
				
				$("#menu-ul li>:first").click();
				getSeahos();
				
				$('#scrollList1').scrollbar({
					height: video_height,
					width: slidewidth,
					speed:100,
					canCrossBoundary: false,
					duration:10
				});
				$("#historyList").hide();
				window.onresize = resizeWindown;
            });
            
            function resizeWindown(){
            	var slidewidth = $(".slide2-right").width();
            	var slideheight = $("#video_iframe").height();
                $('#scrollList').scrollbar('render',{
					height: slideheight,
					width: slidewidth
				});
                $('#scrollList1').scrollbar('render',{
					height: slideheight,
					width: slidewidth
				});
            }
            
            function setIframe(flag,top,url,div){
            	if(flag==1){
            		setSeahos($(div).attr("title"),url);
            	}
            	$(".menu-list ul li .tags").removeClass('act');
            	$(div).addClass('act');
            	$('#slide2-left-top').html(top);
            	$("#video_iframe").attr("src",url);
            }
            
            function setHistory(flag){
            	if(flag==1){
            		$("#video").addClass('act');
            		$("#history").removeClass('act');
            		$("#videoList").show();
            		$("#historyList").hide();
            	}else{
            		$("#video").removeClass('act');
            		$("#history").addClass('act');
            		$("#videoList").hide();
            		$("#historyList").show();
            	}
            }
            
            function getSeahos(){
				if ($.cookie('seaKey') != undefined&&$.cookie('seaKey') != "null") {
					var arrcol = $.cookie('seaKey').split('$$');
					if (arrcol.length > 0 && arrcol != "") {
						var liststr = "";
						for (var i = 0; i < arrcol.length; i++) {
							liststr = liststr + '<li name="tagsname" title="'+arrcol[i].split('~~')[0]+'"><div class="tags"  onclick="setIframe(0,\''+arrcol[i].split('~~')[0]+'\',\''+arrcol[i].split('~~')[1]+'\',this)" title="'+arrcol[i].split('~~')[0]+'"><a>'+arrcol[i].split('~~')[0]+'</a></div><i onclick="delSeahos(\''+arrcol[i]+'\');">×</i></li>';
						}
						$("#menu-ul1").html(liststr);
					}else{
						$("#menu-ul1").html('<li class="act"><div><a>您暂未播放视频</a></div></li>');
					}
				}
			}
            
            function setSeahos(title,url){
            	var str = title + "~~" + url;
				if ($.cookie('seaKey') != undefined&&$.cookie('seaKey') != "null"&&$.cookie('seaKey').length>0) {
					var seaKey = $.cookie('seaKey');
					var arrcol = $.cookie('seaKey').split('$$');
					if(jQuery.inArray(str, arrcol)<0){
						str = str + "$$" + $.cookie('seaKey');
					}else{
						str = $.cookie('seaKey');
					}
				}
				$.cookie('seaKey',str);
			}
			
			function emptySeahos(){
				$.cookie('seaKey',null);
				getSeahos()
			}
			
			function delSeahos(str){
				if ($.cookie('seaKey') != undefined&&$.cookie('seaKey') != "null") {
					var seaKey = $.cookie('seaKey');
					var arrcol = $.cookie('seaKey').split('$$');
					var index = $.inArray(str, arrcol);
				    if (index >= 0){
				    	arrcol.splice(index, 1);
				    }
				}
				$.cookie('seaKey',arrcol.join('$$'));
				getSeahos();
				return false;
			}
				
		</script>
	</body>
</html>