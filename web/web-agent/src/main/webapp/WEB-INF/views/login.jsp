<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>登录</title>
    <script type="text/javascript" src="/static/js/md5.js"></script>
    <style type="text/css">
		@media screen and (max-width: 768px) {
			.control-label{width: 100%;text-align: left;}
			.input-group{width: 100%;}
			.part{display: none;}
		}
		@media screen and (min-width: 768px){
			.modal-dialog {
				width: 600px;
				margin: 0 auto;
				margin-top: 30px;
			}
		}
		.form-label {
			text-align: right !important;
			vertical-align: middle !important;
			right: 5px !important;
			width: 16.66666667% !important;
			float: left !important;
		}
	</style>
</head>
<body>
<div style="position: fixed;top: 0;left: 0;right: 0;bottom: 0;width: 100%;height: 100%;background: url('/static/js/login/1.jpg') repeat top center;overflow: auto;">
	<div id="canvas-wrapper" style="position: fixed;top: 0;left: 0;right: 0;bottom: 0;width: 100%;height: 100%;">
	    <canvas id="demo-canvas"></canvas>
	</div>
    <div class="modal-dialog login-modal-dialog" style="margin-top: 10%;">
        <div class="col-xs-12" style="margin-bottom: 10px;">
            <img src="/static/images/favicon.png"  width="146" title="Logo" style="float: left;">
        </div>
        <div class="col-xs-12 modal-content">
            <form id="userinfo-form">
                <div class="modal-header" style="border-top: 10px solid #4fc1e9;overflow: auto;font-size: 18px;line-height: 36px;">
                    <i class="fa-group"></i>
					<span>用户登录</span>
                </div>
                <div class="modal-body" style="background-color: #FAFAFA;padding: 30px;overflow: overlay;">
               		<div>
	                	<label class="control-label col-xs-2 form-label" for="tagsinput-1" style="font-weight: 400;font-size: 18px;line-height: 30px;">用户名：</label>
	                	
	                	<div class="input-group input-group-sm input-group-minimal col-xs-8 form-group form-text" style="background-color: #ffffff">
							<span class="input-group-addon">
								<i class="fa-user-md" style="color: #BBB;"></i>
							</span>
							<input id="userName" name="userName" type="text" class="form-control col-xs-12" placeholder="请输入用户名">
						</div>
                	</div>
                	
                	<div style="margin-bottom: 10px;margin-top:20px;">
	                    <label class="control-label col-xs-2 form-label" for="tagsinput-1" style="font-weight: 400;font-size: 18px;line-height: 30px;">密码：</label>
	                    
	                    <div class="input-group input-group-sm input-group-minimal col-xs-8 form-group form-text" style="background-color: #ffffff">
							<span class="input-group-addon">
								<i class="fa-lock" style="color: #BBB;"></i>
							</span>
							<input id="password" name="password" type="password" class="form-control col-xs-12" placeholder="请输入密码">
						</div>
					</div>
					<input id="password1" name="password1" type="hidden">
                </div>
                <div class="modal-footer" style="background-color: #f2f2f2;">
                    <div class="col-xs-12">
                        <div class="col-xs-6" style="float: right;" id="save-div">
                        	<button type="submit" class="btn btn-info btn-icon-standalone" style="width: 120px;margin-bottom:0px;" id="save">
								<i class="fa-arrow-right"></i>
								<span>登 录</span>
							</button>
                        </div>
                	</div>
                </div>
            </form>
        </div>
    </div>
</div>
         
<script type="text/javascript">
//检验
$(document).ready(function(){
    $("#userinfo-form").validate({
        onKeyup : false,
        onfocusout : false,
        rules : {
            userName: {
                required: true
            },
            password: {
                required: true
            }
        },
        messages : {
            userName: {
                required: " "
            },
            password: {
                required: " "
            }
        },
        errorPlacement : function(error, element) {
            Main_SetPageItemErrMsg(error.text(), element);
        },
        success : function(element){
            Main_SetPageItemErrMsg(null, element);
        },
        submitHandler : function(form){
        	initPwd();
            var data= $('#userinfo-form').serialize();
            $.ajax({
                url     : "/admin/user/userLogin",
                type    : "post",
                dataType: "json",
                data    : data,
                success : function(result) {
                	if(result == 0){
						window.location.replace("admin/index");
					}else if(result == 1){
						toastr.success("用户名不存在");
						resetPwd();
						document.getElementById("userName").value = "";
					}else if(result == 2){
						toastr.success("密码错误");
						resetPwd();
					}else{
						toastr.success("登录失败");
						resetPwd();
					}
                },
                error   : function(result) { alert("系统发生错误！"); }
            });
            return false;
        }
    });
});


function initPwd(){
    var obj2 = document.getElementById("password1");
    var obj = document.getElementById("password");

    obj2.id = "password";
    obj2.name = "password";
    obj2.value = faultylabs.MD5(obj.value);
    
    obj.id = "********";
    obj.name = "********";
    var length = obj.value.length;
    var str = "";
    for(var i=0;i<length;i++){
        str += "*"
    }
    obj.value = str;
}

function resetPwd(){
    var obj2 = document.getElementById("password");
    var obj = document.getElementById("********");

    obj2.id = "password1";
    obj2.name = "password1";
    obj2.value = "";
    
    obj.id = "password";
    obj.name = "password";
    obj.value = "";
}
</script>
<script src="static/js/login/EasePack.min.js" type="text/javascript"></script>
<script src="static/js/login/TweenLite.min.js" type="text/javascript"></script>
<script src="static/js/login/login.js" type="text/javascript"></script>
<script src="static/js/xenon-custom.js"></script>
<script type="text/javascript">
    jQuery(document).ready(function() {
        // Init CanvasBG and pass target starting location
        CanvasBG.init({
            Loc: {
                x: window.innerWidth,
                y: window.innerHeight
            },
        });
    });
</script>
</body>
</html>