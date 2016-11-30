//var initParams = {["startdate":"2015-06-10","enddate":"2015-07-10"]};

var mytitle = document.title;

$("#main-menu .title").each(function(){
	if(mytitle.indexOf($(this).text()) > 0 ){
		$(".uk-menu").removeClass('active').filter($(this).parent().parent("li.uk-menu")).addClass('active');
		$(".top-uk-menu").removeClass('active').filter($(this).parent().parent().parent().parent(".top-uk-menu")).addClass('active');
	}
});

//ajax后success函数执行判断代码
function ajaxSuccessJudge(data){
	if(data.status == "ERROR"){
		if(typeof(data.msg)!="undefined"&&data.msg!=null&&data.msg!=""){
			toastr.warning(data.ajaxmsg+data.msg);
			return false;
		}
		if(typeof(data.errMsgs)!="undefined"&&data.errMsgs!=null&&data.errMsgs!=""){
			toastr.warning(data.ajaxmsg+data.errMsgs);
			return false;
		}
	}else{
		return true;
	}
}

//全局的ajax设置
$.ajaxSetup({
	aysnc: false , // 默认同步加载
	contentType:"application/x-www-form-urlencoded;charset=utf-8",
　　　//请求失败遇到异常触发
　　　error: function (XMLHttpReques, textStatus, errorMsg) {
		$(".page-loading-overlay").addClass("loaded");
		toastr.error( '发送AJAX请求到"' + this.url + '"时出错[' + XMLHttpReques.status + ']：' + errorMsg);
	},
　　　//完成请求后触发。即在success或error触发后触发
    complete:function(XMLHttpRequest,textStatus){
      var sessionstatus=XMLHttpRequest.getResponseHeader("users");
          if(sessionstatus=="timeout"){
              alert("登录超时,请重新登录！");
              window.location.replace("login");   
          }   
	}   
});
var isGetData = false;


//对传输的数据进行改造，方便后台接收数据
function transitionRequest(request){
	
	var columns = request.columns;
	var orders = request.order;
	var search = request.search.value;
	
	var columnsdata = new Array();
	var orderColumn = new Array();
	var orderDir = new Array();
	
	for(var i=0;i<columns.length;i++){
		columnsdata[i] = columns[i].data;
	}
	
	for(var i=0;i<orders.length;i++){
		orderColumn[i] = orders[i].column;
		orderDir[i] = orders[i].dir;
	}
	
	request.columnsdata = columnsdata;
	request.orderColumn = orderColumn;
	request.orderDir = orderDir;
	request.search = search;
	return request;
}

/*dataTable实现前台和后天同时分页*/
$.fn.dataTable.pipeline = function ( opts ) {
    // Configuration options
    var conf = $.extend( {
        pages: 3,     // number of pages to cache
        url: '',      // script url
        data: null,   // function or object with parameters to send to the server
                      // matching how `ajax.data` works in DataTables
        method: 'GET' // Ajax HTTP method
    }, opts );
 
    // Private variables for storing the cache
    var cacheLower = -1;
    var cacheUpper = null;
    var cacheLastRequest = null;
    var cacheLastJson = null;
 
    return function ( request, drawCallback, settings ) {
        var ajax          = false;
        var requestStart  = request.start;
        var drawStart     = request.start;
        var requestLength = request.length;
        var requestEnd    = requestStart + requestLength;
         
        if ( settings.clearCache ) {
            // API requested that the cache be cleared
            ajax = true;
            settings.clearCache = false;
        }else if ( cacheLower < 0 || requestStart < cacheLower || requestEnd > cacheUpper ) {
            // outside cached data - need to make a request
            ajax = true;
        }else if ( JSON.stringify( request.order )   !== JSON.stringify( cacheLastRequest.order ) ||
                  JSON.stringify( request.columns ) !== JSON.stringify( cacheLastRequest.columns ) ||
                  JSON.stringify( request.search )  !== JSON.stringify( cacheLastRequest.search )
        ) {
            // properties changed (ordering, columns, searching)
            ajax = true;
        }
         
        // Store the request for checking next time around
        cacheLastRequest = $.extend( true, {}, request );
 
        if ( ajax ) {
            // Need data from the server
            if ( requestStart < cacheLower ) {
                requestStart = requestStart - (requestLength*(conf.pages-1));
 
                if ( requestStart < 0 ) {
                    requestStart = 0;
                }
            }
             
            cacheLower = requestStart - (requestStart%(requestLength*conf.pages));
            cacheUpper = requestStart + (requestLength * conf.pages) - (requestStart%(requestLength*conf.pages));
 
            request.start = requestStart;
            request.length = requestLength*conf.pages;
 
            // Provide the same `data` options as DataTables.
            if ( $.isFunction ( conf.data ) ) {
                // As a function it is executed with the data object as an arg
                // for manipulation. If an object is returned, it is used as the
                // data object to submit
                var d = conf.data( request );
                if ( d ) {
                    $.extend( request, d );
                };
            }else if ( $.isPlainObject( conf.data ) ) {
                // As an object, the data given extends the default
                $.extend( request, conf.data );
            }
            //对传人的参数进行改造方便后台解析
            request = transitionRequest(request);
            //alert(JSON.stringify(request));
            settings.jqXHR = $.ajax( {
                "type":     conf.method,
                "url":      conf.url,
                "data":     request,
                "dataType": "json",
                "cache":    false,
                "success":  function (json){
                	if(json == null ||json == {} ||json == ""){
                		drawCallback({data:[]}); // 按照空数据处理页面
                		return false;
                	}
                    
                    // 处理返回值
                    if(!Main_ExcuteFormJquerySbumitResult(json)){
                    	drawCallback({data:[]}); // 按照空数据处理页面
                    	isGetData = false; // 设置全局变量，保证下次可以检索
                    	return false;
                    } else {
                    	cacheLastJson = $.extend(true, {}, json.data);
                    	 
                        if ( cacheLower != drawStart ) {
                            json.data.data.splice( 0, drawStart-cacheLower );
                        }
                        json.data.data.splice( requestLength, json.data.data.length );
                        
                    	drawCallback( json.data );
                    }
                    isGetData = false; // 设置全局变量，保证下次可以检索
                }
            });
        } else {
            json = $.extend( true, {}, cacheLastJson );
            json.draw = request.draw; // Update the echo for each response
            json.data.splice( 0, requestStart-cacheLower );
            json.data.splice( requestLength, json.data.length );
            // 处理返回值
            if(!Main_ExcuteFormJquerySbumitResult(json)){
            	drawCallback({data:[]}); // 按照空数据处理页面
            	return false;
            } else {
            	drawCallback( json );
            }
        };
    };
};


//加载表格数据
function showTableData(dataConfig){
	if(isGetData){
		toastr.warning("正在检索数据，请稍后操作");
		return; // 正在检索的时候，不响应页面的检索动作
	} else {
		isGetData = true;
	}
	if(table!=null && table!=undefined){
		table.destroy();
	}
	var defaultConfig = {
		"processing": true,
		"serverSide": true,
		"searching":false,
		"responsive": true,
		"tableTools": {
            sSwfPath: "static/js/datatables/copy_csv_xls_pdf.swf",
            "aButtons": []
        },
		"lengthMenu": [
			[10, 25, 50, 100], [10, 25, 50, 100]
		],
		"ordering": true,
	 	"order": [[0, 'asc']],
	 	"language": {
	 		"processing": "玩命加载中...",
	 	 	"sSearch": "搜索 : ",
			"lengthMenu": "每页  _MENU_ 条记录",
			"zeroRecords": "没有找到记录",
			"sInfo": "（共 _TOTAL_ 条记录 ,第 _PAGE_ 页,共 _PAGES_ 页） ",
			"infoEmpty": "无记录",
			"infoFiltered": "(从 _MAX_ 条记录过滤)",
			"oPaginate": {
				"sFirst": "<i class='fa-angle-double-left'></i>",
				"sPrevious": "<i class='fa-angle-left'></i>",
				"sNext": "<i class='fa-angle-right'></i>",
				"sLast": "<i class='fa-angle-double-right'></i>"
 			}
		},
		"stateSave": false,
		"stateLoaded": function (settings, data) {
			$(".page-loading-overlay").addClass("loaded");
		}
	};
	
	var config = $.extend({}, defaultConfig, dataConfig);
	table = $('#example').DataTable(config);
}


Highcharts.setOptions({
    lang:{
        months: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
        shortMonths: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
        weekdays: ['星期天', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
        resetZoom: '查看全图',
        resetZoomTitle: '查看全图',
        downloadPNG: '下载PNG',
        downloadJPEG: '下载JPEG',
        downloadPDF: '下载PDF',
        downloadSVG: '下载SVG',
        exportButtonTitle: '导出成图片',
        printButtonTitle: '打印图表', 
        loading: '数据加载中，请稍候...',
        thousandsSep: ','
    }
});
//加载图表
function showChartData(dataConfig){
	var dataFormat = (typeof(dataConfig.dataFormat) == "undefined"||dataConfig.dataFormat == "") ? 1:dataConfig.dataFormat;
	if(typeof(dataConfig.xAxis) != "undefined"&&dataConfig.xAxis != ""){
		dataConfig.xAxis.startOnTick = false;
		dataConfig.xAxis.endOnTick = true;
		dataConfig.xAxis.labels =dataConfig.xAxis.labels ||  {};
		dataConfig.xAxis.tickmarkPlacement= 'on';
	}
	
	var maxLen = 0;	//类别名的最大长度，用于自动计算x轴步长

    for(var i in dataConfig.xAxis.categories){
        var cate = dataConfig.xAxis.categories[i].toString();
        maxLen < cate.length && (maxLen = cate.length);
    }
    
    //智能判断x轴的标签步长
    var labelWidth = maxLen * 6 + 20;			//每个标签所占的宽度
    var interval = dataConfig.xAxis.tickInterval || 1;
    dataConfig.xAxis.labels.step = Math.ceil(dataConfig.xAxis.categories.length / ($("#linecontainer").css('width').replace(/[^\d\.]/g,'') / labelWidth) / interval);
    
	var defaultConfig = {
		chart: {
			renderTo: 'linecontainer',  
            type: 'line',
            style:{
                fontFamily: 'Tahoma, "microsoft yahei", 微软雅黑, 宋体;'
            }
        },
        colors: ['#5D9CEC', '#62C87F', '#6ED5E6', '#FC863F', '#7053B6', '#FFCE55', '#F15755', '#F57BC1', '#DCB186', '#647C9D'],
		title: {
       		text: '',
			x: 0
   		},
   		plotOptions: {
            line: {
                marker: {
                    enabled: false,
                    symbol: 'circle',
                    radius: 2,
                    states: {
                        hover: {
                            enabled: true
                        }
                    }
                }
            }
        },
        yAxis: {
        	title: {
           		text: ''
       		},
			startOnTick: false, 
			endOnTick: false,
			min: 0,
            plotLines: [{  
                value: 0,  
                width: 1,  
                color: '#808080'  
            }]  
        },
        tooltip: {
            borderColor: '#666',
            borderWidth: 1,
            borderRadius: 2,
            backgroundColor: 'rgba(255, 255, 255, 0.7)',
            useHTML: true,
            formatter: function () {
                var s = '<div style="padding:5px;"><b>' + this.x + '</b></div><table style="width: 150px">';
                $.each(this.points, function () {
                	if((this.series.name).indexOf("Series") >= 0 ){
                		s += '<td style="text-align: left;padding: 2px 5px">' + formatValue(dataFormat, this.y) + ' </td></tr>';
                	}else{
                		s += '<tr><td style="text-align: left;padding: 2px 5px" >' + this.series.name + ' </td>' 
					  		+ '<td style="text-align: right;padding-left:15px">' + formatValue(dataFormat, this.y) + ' </td></tr>';
                	}
                });
                s += '</table>';
                return s;
            },
            crosshairs: {
                color: '#7ac943',
                dashStyle: 'shortdot',
                width:1
            },
            shared: true
        },
    };
	
	var config = $.extend({}, defaultConfig, dataConfig);
	var chart = new Highcharts.Chart(config);
	
	if(typeof(dataConfig.xAxis) == "undefined"||dataConfig.xAxis == ""){
		chart.destroy();
		return;
	}
}

/*让筛选条件固定起来*/
var modHeaderSticky = {
	set: function () {
		if (this.isSticky) {
			return true;
		}
		var $modHeader = $('.mod_header');
		$modHeader.find('.panel').addClass('collapsed');
		$('.mod_header_substitute').css('height', $modHeader.outerHeight());

		$modHeader.css('width', $modHeader.width()).addClass('mod_header_sticky');
		this.isSticky = true;
		//$("#topToggle").css('display', 'none');
		//$("#topToggle").slideUp();
	},
	remove: function () {
		
		var $modHeader = $('.mod_header');

		$modHeader.css('width', 'auto').removeClass('mod_header_sticky');
		$modHeader.find('.panel').removeClass('collapsed');
		//$("#topToggle").css('display', 'block');
		//$("#topToggle").slideDown();
		$('.mod_header_substitute').css('height', 0);
		this.isSticky = false;
	},
	resize: function () {
		if(!this.isSticky) return;
		this.remove();
		this.set();
	},
	init: function () {
		var that = this;
		var $target = $('.mod_header');
		var origOffsetY = $target.offset().top;

		$('.mod_header').after('<div class="mod_header_substitute"></div>');

		$(window).scroll(function (event) {
			$(window).scrollTop() > origOffsetY ? that.set() : that.remove();
		});
	

		if (!document.all) {// 避免IE8的BUG
			$(window).resize(function () {
				that.resize();
			});
		}
	}
};

//格式化数据 数据类型，1：整数，2：浮点数  3：百分比，仅在后面追加百分号  4：时间格式 HH:MM:SS	5：百分比，显示时会乘上100  6：不处理，保留原有格式 7: 当是整数时 format为整数，小数就显示浮点 
function formatValue(dataFormat, value){

    dataFormat = parseInt(dataFormat);
    switch(dataFormat){
        case 1:
            value =  Highcharts.numberFormat(value, 0);
            break;
        case 2:
            value =  Highcharts.numberFormat(value, 2);
            break;
        case 3:
            value =  Highcharts.numberFormat(value, 2) + '%';
            break;
        case 4:
            var toTimeDesc = function(t){
                var h = parseInt(t / 3600);
                var m = '00' + parseInt((t % 3600) / 60);
                var s = '00' + parseInt(t % 3600 % 60);
                m = m.substr(m.length - 2, 2);
                s = s.substr(s.length - 2, 2);

                return h + ':' + m + ':' + s;
            };

            value = toTimeDesc(value);						//处理时间格式为时分秒格式H:mm:ss
            break;
        case 5:
            value =  Highcharts.numberFormat(value * 100, 2) + '%';
            break;
        case 7:
            if(value >= 1 || value <= -1){
                value = Highcharts.numberFormat(value, 0);
            }else{
                value = Highcharts.numberFormat(value, 2);
            }                
    }

    return value;
}
function Main_ExcuteFormJquerySbumitResult(result){
	if(result && result.status == "1") {
        if (result.error && result.errMsgsCnt > 0){
        	// 按照项目设置错误提示
        	for(var i = 0;i < result.errMsgsCnt; i++) {
        		var msg = result.errMsgs[i];
        		Main_SetPageItemErrMsg(msg.msg, $("#" + msg.fieldNm));
            }
        } else if (result.msg && result.msg != ""){
        	Main_ShowErrMsg(result.msg);
        } else {
            alert("发生未知错误!");
        }
        return false;
    } else {
    	if(result.msg && result.msg != ""){
    		Main_ShowSuccessMsg(result.msg);
    	}
    	return true;
    }
}

function Main_SetPageItemErrMsg(msg, element){
	element.parent().find(".validate-has-errorspan").remove();
	if(msg){
		element.parent().addClass("validate-has-error")
		var error = "<span class='validate-has-errorspan'>" + msg + "</span>";
		element.parent().append(error);
		/*if(element.is(":radio") ){
			error.appendTo(element.parent().next().next());
		} else if(element.is(":checkbox")){
			error.appendTo (element.next());
		} else {
			error.appendTo(element.parent().next());
		}*/
		// toastr.error(msg);
	    element.change(function(){Main_SetPageItemErrMsg(null,element);});
        // element.parent().append("<label class=\"errorTip\">" + msg + "</label>");
        // element.parent().find(".errorTip").fadeIn();
    } else {
    	element.parent().removeClass("validate-has-error");
    	element.unbind("change");
    }
}

function Main_ShowSuccessMsg(msg){
	toastr.success(msg);
}

function Main_ShowErrMsg(msg){
	toastr.error(msg);
}
function Main_ShowAll(obj){
	$.each(obj, function(key, val) {
		alert( key + " = " + val);
	})
}
function Main_clearForm(formName){
	$("#"+formName).find(".validate-has-error").removeClass("validate-has-error");
	$("#"+formName).find(".error").removeClass("error");
    $("#"+formName).find(".validate-has-errorspan").remove();
}
$("#fullscreen-toggler").on("click", function() {
    var n = document.documentElement;
    if($("body").hasClass("full-screen")) {
    	 $("body").removeClass("full-screen");
    	 $("#fullscreen-toggler").removeClass("active");
    	 document.exitFullscreen ? document.exitFullscreen() : document.mozCancelFullScreen ? document.mozCancelFullScreen() : document.webkitExitFullscreen && document.webkitExitFullscreen();
    	 /*$("#main-menu a").each(function(){
     		var thishref = $(this).attr('href');
     		thishref=thishref.replace("?full=false","");
     		thishref=thishref.replace("?full=true",""); 
     		$(this).attr("href",thishref + "?full=false");
     	});*/
    	 return false;
    }else{
    	$("body").addClass("full-screen");
    	$("#fullscreen-toggler").addClass("active");
    	n.requestFullscreen ? n.requestFullscreen() : n.mozRequestFullScreen ? n.mozRequestFullScreen() : n.webkitRequestFullscreen ? n.webkitRequestFullscreen() : n.msRequestFullscreen && n.msRequestFullscreen();
    	return false;
    }
});

$(".page-loading-overlay").addClass("loaded");

function resizeWindownMain(){
	
}


//图片上传
function initImgUpload(imgPickDivId, imgUrlItem, width, height, uploadTypeId){
  var $imgPick = $('#' + imgPickDivId),// 图片显示区域
      $imgUrl = $('#' + imgUrlItem),
      // 优化retina, 在retina下这个值是2
      ratio = window.devicePixelRatio || 1,
      // 缩略图大小
      thumbnailWidth = width * ratio,
      thumbnailHeight = height * ratio,
      // Web Uploader实例
      uploader,
      isLoading;
  
  // 防止一次上传多个文件
  isLoading = false;
  
  // 初始化Web Uploader
  var uploader = WebUploader.create({
      // 自动上传。
      auto: true,
      // swf文件路径
      swf: global_ctx + '../webuploader/Uploader.swf',
      // 文件接收服务端。
      server: 'uploadImgCommon',
      // 选择文件的按钮。可选。
      // 内部根据当前运行是创建，可能是input元素，也可能是flash.
      pick: $imgPick,
      // 只允许选择文件，可选。
      accept: {
          title: '选择图片',
          extensions: 'gif,jpg,jpeg,bmp,png',
          mimeTypes: 'image/*'
      },
      formData: {//上传图片时附带的参数
          uploadTypeId: uploadTypeId
      },
  });
  
  // 多个文件被添加的时候，提示错误
  uploader.on( 'beforeFileQueued', function( file ) {
      // 清除错误信息
      $imgPick.find(".imgErrTip").remove();
      if(isLoading){
      	toastr.warning("只能上传一个图片。");
          return false;
      } 
  });

  // 当有文件添加进来的时候
  uploader.on( 'fileQueued', function( file ) {
      var $img = $imgPick.find('img');
      
      // 创建缩略图
      uploader.makeThumb( file, function( error, src ) {
          if ( error ) {
              $img.replaceWith('<span>不能预览</span>');
              return;
          }
          $img.attr( 'src', src );
      }, 200, 200 );
  });

  // 文件上传过程中创建进度条实时显示。
  uploader.on( 'uploadProgress', function( file, percentage ) {
      var $percent = $imgPick.find('.progress span');

      // 避免重复创建
      if ( !$percent.length ) {
          $percent = $('<p class="progress"><span></span></p>')
                  .appendTo( $imgPick )
                  .find('span');
      }

      $percent.css( 'width', percentage * 100 + '%' );
  });

  // 文件上传成功，给item添加成功class, 用样式标记上传成功。
  uploader.on( 'uploadSuccess', function( file, result ) {
      if(Main_ExcuteFormJquerySbumitResult(result)){
          $imgUrl.val(result.data.url);
      }
      toastr.success("图片上传成功！");
  });

  // 文件上传失败，现实上传出错。
  uploader.on( 'uploadError', function( file ) {
      $imgPick.find(".imgErrTip").remove();
      $imgPick.append("<label class=\"imgErrTip\">上传失败</label>");
      $imgPick.find(".errorTip").fadeIn();
  });

  // 完成上传完了，成功或者失败，先删除进度条。
  uploader.on( 'uploadComplete', function( file ) {
      isLoading = false;
      $imgPick.find('.progress').remove();
  });
  
  return uploader;
}

//刷新图片
function freshImgWithUrl(imgPickDivId, imgUrlItem){
	var imgPath = $("#" + imgUrlItem).val();
	if(!imgPath || "" == imgPath){
		imgPath = "static/images/upload.jpg";
	}
	$("#" + imgPickDivId).find('img').attr( 'src', imgPath);
}

Date.prototype.format = function(format) { 
    var date = { 
           "M+": this.getMonth() + 1, 
           "d+": this.getDate(), 
           "h+": this.getHours(), 
           "m+": this.getMinutes(), 
           "s+": this.getSeconds(), 
           "q+": Math.floor((this.getMonth() + 3) / 3), 
           "S+": this.getMilliseconds() 
    }; 
    if (/(y+)/i.test(format)) { 
           format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length)); 
    } 
    for (var k in date) { 
           if (new RegExp("(" + k + ")").test(format)) { 
                  format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? date[k] : ("00" + date[k]).substr(("" + date[k]).length)); 
           } 
    } 
    return format; 
} 