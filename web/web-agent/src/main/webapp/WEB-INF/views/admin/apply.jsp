<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html lang="en">
	<head>
		<title>立即报名</title>
		<link rel="stylesheet" href="static/webuploader/webuploader.css">
    	<script type="text/javascript" src="static/webuploader/webuploader.js"></script>
    	<%
	        String path = request.getContextPath();
	        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	    %>
	    <script type="text/javascript">
	    var global_ctx = "<%=basePath%>";
	    </script>
	    
		<style type="text/css">
		    table.dataTable th, table.dataTable td {
				white-space: normal !important;
			}
		</style>
	</head>
	<body>
	<div class="row box-shadow">
		<div class="page-title mod_header">
			<div class="title-env col-sm-12">
				<div class="input-group input-text" style="float: left;">
				
					<div class="input-group input-group-minimal col-sm-6" style="float: left;">
		                <input type="text" class="form-control" id="s_title" name="s_title" onkeydown="if(event.keyCode==13)showTable();" placeholder="标题查询" style="padding: 0px;padding-left: 12px;">
		                <span class="input-group-addon" onclick="showTable()">
		                    <i class="linecons-search" style="padding-left: 12px;"></i>
		                </span>
		            </div>
		            
		            <div class="input-search" style="margin-left: 10px;float: left;">
                   		<input type="hidden" class="form-control" name="s_state" id="s_state" value="1"/>
               		</div>
		            
	            </div>
	           
	            <div class="input-text input-btn" style="margin-left: 30px;float: left;min-width: 100px;">
	            	<button class="btn btn-info btn-block" onclick="add()"style="height: 34px;"><span class="fa-plus"></span>  添加</button>
	            </div>
	            
			</div>
		</div>

		<div class="panel" style="padding: 0px 30px 20px 30px;">
			<table id="example" class="table table-striped table-bordered table-small-font" cellspacing="0" width="100%">
		        <thead>
		            <tr>
		                <th>排序</th>
		                <th>图片</th>
		                <th>标题</th>
		                <th>内容</th>
		                <th>跳转链接</th>
		                <th>颜色</th>
		                <th>激活后颜色</th>
		                <th>状态</th>
		                <th>创建时间</th>
		                <th>操作</th>
		            </tr>
		        </thead>
		    </table>
		</div>
	</div>
	
	<!-- Modal -->
	<div class="modal fade" id="myModal">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <form id="editForm">
	                <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal"  aria-hidden="true" aria-label="Close"><span
	                            aria-hidden="true">&times;</span></button>
	                    <h4 class="modal-title" id="myModalLabel">添加/修改</h4>
	                </div>
	                <div class="modal-body">
	                    <div class="row" id="id-div" style="display:none; ">
	                        <div class="col-xs-2 form-label">
	                            <label class="control-label col-xs-2" for="tagsinput-1">ID：</label>
	                        </div>
	                        <div class="col-xs-6 form-group">
	                            <input type="text" class="form-control" id="id" name="id">
	                            <input type="hidden" class="form-control" id="type" name="type">
	                        </div>
	                    </div>
	                    
	                    <div class="row">
	                        <div class="col-xs-2 form-label">
	                            <label class="control-label" for="tagsinput-1">图片：</label>
	                        </div>
	                        <div class="col-xs-6 form-group">
	                            <div id="storeImgUrl-div" style="width:200px;height:150px;"><img width="200px" height="150px"></div>
	                            <div class="col-xs-12" style="margin-top: 10px;">
	                            	<input type="text" class="form-control" name="img" id="img" value="" />
	                        	</div>
	                        </div>
	                    </div>
	                    <div class="row">
	                        <div class="col-xs-2 form-label">
	                            <label class="control-label" for="tagsinput-1">标题：</label>
	                        </div>
	                        <div class="col-xs-6 form-group">
	                            <input type="text" class="form-control" id="title" name="title">
	                        </div>
	                    </div>
	                    <div class="row">
	                        <div class="col-xs-2 form-label">
	                            <label class="control-label" for="tagsinput-1">跳转链接：</label>
	                        </div>
	                        <div class="col-xs-6 form-group">
	                            <input type="text" class="form-control" id="url" name="url">
	                        </div>
	                    </div>
	                    <div class="row">
	                        <div class="col-xs-2 form-label">
	                            <label class="control-label" for="tagsinput-1">颜色：</label>
	                        </div>
	                        <div class="col-xs-6 form-group">
	                        	<input type="text" class="form-control colorpicker" id="colour" name="colour" value=""/>
	                        </div>
	                    </div>
	                    <div class="row">
	                        <div class="col-xs-2 form-label">
	                            <label class="control-label" for="tagsinput-1">鼠标经过颜色：</label>
	                        </div>
	                        <div class="col-xs-6 form-group">
	                            <input type="text" class="form-control colorpicker" id="hoverColour" name="hoverColour"/>
	                        </div>
	                    </div>
	                    <div class="row">
	                        <div class="col-xs-2 form-label">
	                            <label class="control-label" for="tagsinput-1">排序：</label>
	                        </div>
	                        <div class="col-xs-6 form-group">
	                            <div class="input-group input-group-sm input-group-minimal">
									<span class="input-group-addon">
										<i class="fa-sort-numeric-desc"></i>
									</span>
									<input type="text" class="form-control" data-mask="decimal" id="pageOrder" name="pageOrder" />
								</div>
	                        </div>
	                    </div>
	                    <div class="row">
	                        <div class="col-xs-2 form-label">
	                            <label class="control-label" for="tagsinput-1">状态：</label>
	                        </div>
	                        <div class="col-xs-6 form-group">
	                            <input type="text" class="form-control" id="state" name="state">
	                        </div>
	                    </div>
	                    
	                    <div class="row">
							<div class="col-xs-12">
								<div class="col-xs-2 form-label">
									<label class="control-label">内容： </label>
								</div>
								<div class="col-xs-10 form-group">
									<input type="hidden" class="form-control" id="content" name="content">
									<!-- 加载编辑器的容器 -->
									<script id="a_container" name="a_content" type="text/plain" style="width: 95%;height: 200px;"></script>
									<script>
							        	var UEDITOR_HOME_URL = "/static/ueditor/";　　//从项目的根目录开始
							    	</script>
									<!-- 配置文件 -->
									<script type="text/javascript" src="/static/ueditor/ueditor.config.js"></script>
									<!-- 编辑器源码文件 -->
									<script type="text/javascript" src="/static/ueditor/ueditor.all.min.js"></script>
									<!-- 实例化编辑器 -->
									<script type="text/javascript">
							        var ue = UE.getEditor('a_container');
							    </script>
								</div>
							</div>
						</div>
	                </div>
	                <div class="modal-footer">
					    <div class="row" >
							<div class="col-xs-10">
								<div class="col-xs-push-1 col-xs-2" id="save-div">
									<button type="submit" class="btn btn-info btn-block" id="save">保存</button>
								</div>
								<div class="col-xs-push-2 col-xs-2" id="close-div">
									<button type="button" class="btn btn-white btn-block" data-dismiss="modal">关闭</button>
								</div>
							</div>
						</div>
					</div>
	            </form>
	        </div>
	    </div>
	</div>
	
	
	
	<script type="text/javascript">
		var dataobj= {"state":1,"type":7};
		var table;//表格对象
		
		jQuery(document).ready(function($){
			
			$("#s_state").select2({
	        	placeholder: "选择状态",
	            width: "150px",
	            data: [{'id':-1,'text':'全部状态'},{'id':1,'text':'正常'},{'id':0,'text':'禁用'}],
	            minimumResultsForSearch: -1,
	            allowClear: true,
	            formatResult: function(obj) {
	                return "<div class='select2-user-result'>" + obj.text + "</div>"; 
	            },
	            formatSelection: function(obj) {
	            	if(obj.id==-1){
	            		delete dataobj["state"]; 
	            	}else{
	            		dataobj.state = obj.id;
	            	}
					showTable();
	                return  obj.text; 
	            }
	        });
	        
	        $("#editForm").validate({
	            onKeyup : false,
	            onfocusout : false,
	            rules : {
	                title: {
	                    required: true
	                },
	                state: {
	                    required: true
	                },
	                pageOrder: {
	                    required: true,
	                    number: true,
                    	maxlength: 10,
                    	min: 1
	                }
	            },
	            messages : {
	                title: {
	                    required: "标题不能为空"
	                },
	                state: {
	                    required: "请选择状态"
	                },
	                pageOrder: {
	                    required: "请输入排序",
                    	number: "请输入数字",
                   	 	maxlength: "排序数字应小于10位",
                    	min: "排序数字应大于0"
	                }
	            },
	            errorPlacement : function(error, element) {
	                Main_SetPageItemErrMsg(error.text(), element);
	            },
	            success : function(element){
	                Main_SetPageItemErrMsg(null, element);
	            },
	            submitHandler : function(form){
	            	$("#content").val(ue.getContent());
	                var data= $('#editForm').serialize();
	                $.ajax({
	                    url     : "/admin/replace/saveReplace",
	                    type    : "post",
	                    dataType: "json",
	                    data    : data,
	                    success : function(result) { if(Main_ExcuteFormJquerySbumitResult(result)){showTable();$("#myModal").modal("hide");}; },
	                    error   : function(result) { alert("系统异常，保存失败！"); }
	                });
	                return false;
	            }
	        });
	        
	        initImgUpload("storeImgUrl-div", "img", 200, 150, "Store");
	        window.onresize = resizeWindown;
	        
        });
        
        // 页面调整的时候，调整table大小
	    function resizeWindown(){
	    	resizeModal();
	        if(table){
	            $('.dataTables_scrollBody').css('height', $(window).height() - 250);
	        }
	    }
        
		function showTable(){
			$.ajaxSettings.traditional = true;
			var s_title = $("#s_title").val();
			if(s_title.length>0){
				dataobj.title ="%" + s_title + "%";
			}else{
				delete dataobj["title"];
			}
			var dataConfig = {
				"responsive": false,
				"sScrollY": $(window).height() - 250,
				"ajax": $.fn.dataTable.pipeline({
					url: '/admin/replace/getReplacePageList',
					data: dataobj
				}),
				"columns": [
					{ "data": "pageOrder","width":"40px"},
					{ "data": "img",
						"render": function (a, b, c, d) {
							if(c.img){
							    var html = "<img height='40px' src='" + c.img + "'>";
							    return html;
							} else {
								return "";
							}
	                    },
		                "width":"40px"
		            },
		            { "data": "title","width":"100px"},
		            { "data": "content","width":"250px"},
		            { "data": "url","width":"150px"},
		            { "data": "colour","width":"50px"},
		            { "data": "hoverColour","width":"150px"},
		            { "data": "state","width":"50px",
		            	"render": function(a, b, c, d) {
		            		if(c.state==1){
		            			return "正常";
		            		}else if(c.state==0){
		            			return "<div style='background-color: #FF0303;color: #fff;'>禁用</div>";
		            		}else{
		            			return "未设置";
		            		}
		                }
		            },
		            { "data": "createTimeStr","width":"100px"},
		            {
		            	"data": "id","width":"100px",
		            	"render": function(a, b, c, d) {
		            		return "<a style='cursor:pointer;margin-right:5px;color:#95cd62' onclick='edit(" +JSON.stringify(c) +  ")'>修改</a>"+
		            			   "<a style='cursor:pointer;margin-right:5px;color:#ab2d32' onclick='deleteRep("+c.id+")'>删除</a>";
		                },
		                "orderable": false
		            }
				]
			};
			
			showTableData(dataConfig);
		}
		
		/**修改方法**/
		function edit(data) {
			$("#myModalLabel").text("修改信息");
	        $("#id").val(data.id);
	        $("#type").val(data.type);
	        $("#title").val(data.title);
	        $("#content").val(data.content);
	        if(ue.queryCommandState('source')==1){
	        	ue.execCommand( 'source');
	        }
	        ue.setContent(data.content);
	        $("#url").val(data.url);
	        $("#colour").val(data.colour);
	        $("#hoverColour").val(data.hoverColour);
	        $("#pageOrder").val(data.pageOrder);
		    $("#state").val(data.state);
		    initTypeCombo();
		    Main_clearForm("editForm");
		    $("#img").val(data.img);
	        freshImgWithUrl("storeImgUrl-div", "img");
	        $("#myModal").modal("show");
		}
		
		
		/**添加方法**/
		function add() {
	        $("#myModalLabel").text("添加信息");
	        $("#id").val("");
	        $("#type").val("7");
	        $("#title").val("");
	        $("#content").val("");
	        if(ue.queryCommandState('source')==1){
	        	ue.execCommand( 'source');
	        }
	        ue.setContent("");
	        $("#url").val("");
	        $("#colour").val("");
	        $("#hoverColour").val("");
	        $("#pageOrder").val("");
		    $("#state").val("1");
		    initTypeCombo();
		    Main_clearForm("editForm");
		    $("#img").val("");
	        freshImgWithUrl("storeImgUrl-div", "img");
	        $("#myModal").modal("show");
		} 
	     
	     /**删除方法**/
		function deleteRep(id) {
			if (confirm("确认删除该条记录吗?"))  {  
				$.ajax({
					type : "POST",
					url : '/admin/replace/deleteReplaceById',
					data : "id=" + id,
					success : function(result) {
						if(Main_ExcuteFormJquerySbumitResult(result)){
							showTable();
						};
					}
				});
			}
		}
		
		function initTypeCombo(){
			$("#state").select2({
	        	minimumResultsForSearch: -1,
	        	data: [{'id':1,'text':'正常'},{'id':0,'text':'禁用'}],
	        	formatResult: function(obj) { return "<div class='select2-user-result'>" + obj.text + "</div>"; },
	           	formatSelection: function(obj) {return  obj.text;}
	        });
	    }
	</script>
	</body>
</html>