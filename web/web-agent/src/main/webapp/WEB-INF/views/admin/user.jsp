<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html lang="en">
	<head>
		<title>用户管理</title>
		<script type="text/javascript" src="static/js/md5.js"></script>
	</head>
	<body>
	<div class="row box-shadow">
		<div class="page-title mod_header">
			<div class="title-env col-sm-12">
				<div class="input-group input-text" style="float: left;">
				
					<div class="input-group input-group-minimal col-sm-6" style="float: left;">
		                <input type="text" class="form-control" id="s_userName" name="s_userName" onkeydown="if(event.keyCode==13)showTable();" placeholder="用户名查询" style="padding: 0px;padding-left: 12px;">
		                <span class="input-group-addon" onclick="showTable()">
		                    <i class="linecons-search" style="padding-left: 12px;"></i>
		                </span>
		            </div>
		            
		            <div class="input-search" style="margin-left: 10px;float: left;">
                   		<input type="hidden" class="form-control" name="s_state" id="s_state"/>
               		</div>
		            
	            </div>
	           
	            <div class="input-text input-btn" style="margin-left: 30px;float: left;min-width: 100px;">
	            	<button class="btn btn-info btn-block" onclick="add()"style="height: 34px;"><span class="fa-plus"></span>  添加用户</button>
	            </div>
	            
			</div>
		</div>

		<div class="panel" style="padding: 0px 30px 20px 30px;">
			<table id="example" class="table table-striped table-bordered table-small-font" cellspacing="0" width="100%">
		        <thead>
		            <tr>
		                <th>用户名</th>
		                <th>用户真实姓名</th>
		                <th>创建时间</th>
		                <th>用户状态</th>
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
	                            <input type="text" class="form-control" id="userId" name="userId">
	                            <input type="hidden" class="form-control" id="password" name="password">
	                        </div>
	                    </div>
	                    <div class="row">
	                        <div class="col-xs-2 form-label">
	                            <label class="control-label" for="tagsinput-1">用户名：</label>
	                        </div>
	                        <div class="col-xs-6 form-group">
	                            <input type="text" class="form-control" id="userName" name="userName">
	                        </div>
	                    </div>
	                    <div class="row">
	                        <div class="col-xs-2 form-label">
	                            <label class="control-label" for="tagsinput-1">真实姓名：</label>
	                        </div>
	                        <div class="col-xs-6 form-group">
	                            <input type="text" class="form-control" id="userDspName" name="userDspName"/>
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
	                        <div class="col-xs-2 form-label">
	                            <label class="control-label" for="tagsinput-1">密码：</label>
	                        </div>
	                        <div class="col-xs-6 form-group">
	                            <input type="password" class="form-control" id="pwd" name="pwd">
	                        </div>
	                    </div>
	                    <div class="row">
	                        <div class="col-xs-2 form-label">
	                            <label class="control-label" for="tagsinput-1">确认密码：</label>
	                        </div>
	                        <div class="col-xs-6 form-group">
	                        	<input type="password" class="form-control" id="pwd2" name="pwd2"/>
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
		var dataobj= {};
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
	        showTable();
	        $("#editForm").validate({
	            onKeyup : false,
	            onfocusout : false,
	            rules : {
	                userName: {
	                    required: true
	                },
	                pwd: {
		                required: true,
                		minlength: 6
		            },
		            pwd2: {
		            	required: true,
                		minlength: 6,
		                equalTo: "#pwd"
		            },
	                userDspName: {
	                    required: true
	                },
	                state: {
	                    required: true
	                }
	            },
	            messages : {
	                userName: {
	                    required: "用户名不能为空"
	                },
	                pwd: {
		                required: "请输入新密码",
		                minlength: "密码不能少于6位"
		            },
		            pwd2: {
		            	required: "请再次输入新密码",
                		minlength: "密码不能少于6位",
		                equalTo: "两次输入的密码不一致，请再次确认"
		            },
		            userDspName: {
	                    required: "真实姓名不能为空"
	                },
	                state: {
	                    required: "请选择状态"
	                }
	            },
	            errorPlacement : function(error, element) {
	                Main_SetPageItemErrMsg(error.text(), element);
	            },
	            success : function(element){
	                Main_SetPageItemErrMsg(null, element);
	            },
	            submitHandler : function(form){
	            	if($("#password").val()!=$("#pwd").val()){
	            		$("#password").val(faultylabs.MD5($("#pwd").val()));
	            	}
	            	
	                var data= $('#editForm').serialize();
	                $.ajax({
	                    url     : "/admin/user/saveUsers",
	                    type    : "post",
	                    dataType: "json",
	                    data    : data,
	                    success : function(result) { if(Main_ExcuteFormJquerySbumitResult(result)){showTable();$("#myModal").modal("hide");}; },
	                    error   : function(result) { alert("系统异常，保存失败！"); }
	                });
	                return false;
	            }
	        });
	        
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
			var s_userName = $("#s_userName").val();
			if(s_userName.length>0){
				dataobj.userName ="%" + s_userName + "%";
			}else{
				delete dataobj["userName"];
			}
			var dataConfig = {
				"sScrollY": $(window).height() - 250,
				"order": [[2, 'desc']],
				"ajax": $.fn.dataTable.pipeline({
					url: '/admin/user/getAllUsers',
					data: dataobj
				}),
				"columns": [
		            { "data": "userName"},
		            { "data": "userDspName"},
		            { "data": "createData"},
		            { "data": "state",
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
		            {
		            	"data": "userId",
		            	"render": function(a, b, c, d) {
		            		return "<a style='cursor:pointer;margin-right:5px;color:#95cd62' onclick='edit(" +JSON.stringify(c) +  ")'>修改</a>"+
		            			   "<a style='cursor:pointer;margin-right:5px;color:#ab2d32' onclick='deleteRep("+c.userId+")'>删除</a>";
		                },
		                "orderable": false
		            }
				]
			};
			
			showTableData(dataConfig);
		}
		
		/**修改方法**/
		function edit(data) {
			$("#myModalLabel").text("修改用户信息");
	        $("#userId").val(data.userId);
	        $("#userName").val(data.userName);
	        $("#password").val(data.password);
	        $("#userDspName").val(data.userDspName);
	        $("#pwd").val(data.password);
	        $("#pwd2").val(data.password);
		    $("#state").val(data.state);
		    initTypeCombo();
		    Main_clearForm("editForm");
	        $("#myModal").modal("show");
		}
		
		
		/**添加方法**/
		function add() {
	        $("#myModalLabel").text("添加用户信息");
	        $("#userId").val("");
	        $("#userName").val("");
	        $("#password").val("");
	        $("#userDspName").val("");
	        $("#pwd").val("");
	        $("#pwd2").val("");
		    $("#state").val("1");
		    initTypeCombo();
		    Main_clearForm("editForm");
	        $("#myModal").modal("show");
		} 
	     
	     /**删除方法**/
		function deleteRep(id) {
			if (confirm("确认删除该条记录吗?"))  {  
				$.ajax({
					type : "POST",
					url : '/admin/user/deleteUsers',
					data : "userId=" + id,
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