function render_flow_chart(dataString){
  /***************
   * name：flowchart
   ****************/
   if(dataString == "[]" || dataString == ""|| typeof(dataString.pageId) == "undefined" || dataString.pageId == "")
   {
     return;
   }
   
    window.Flow = function(prop,canvas){
	 	this.prop ={
	 		width : 100,
            paddWidth:-5
	 	};
	 	this.canvas = null;
	 	this.canvasContext = null;
	 	this.canvasProp = {
			width : 600,
			height : 800,       
	   		lineWidth : 6,//线宽
	   		mainLineWidth : 6,//线宽
	   		activeColor:'#6987c4',
	   		mainColor:'#1BE106',
	   		inactiveColor :'#eccb9a'
	 	};
	 	 $.extend(this.prop,prop||{});
	 	 $.extend(this.canvasProp,canvas||{});
	 }
   window.Flow.prototype = {
	     initUI : function(data){
	       // G_vmlCanvasManager_.init();
	       this.changeCanvasSize();
	       var topdata = this.getData(data);
	       $('div.rec1 .contenttop').text(topdata.topPage.pageName).attr('title',data.pageId);
	       $("#toptable").text("");
	       $("#toptable").append('<tbody><tr><th>访问次数</th><td>'+topdata.topPage.sessionNum+'</td></tr>'+
			'<tr><th>访问人数</th><td>'+topdata.topPage.activeUser+'</td></tr>'+
			'<tr><th>访问时长</th><td>'+formatValue(4,topdata.topPage.userAvgTime)+'</td></tr></tbody>');
			
	       this.createChart($('div.rec1'),topdata);
	       if($('.rec[level=1]').length>1){
	    	   var level1 = $('.rec[level=1]').filter('.has_child').eq(0);
		       data.pageId = level1.attr("mytitle");
		       this.createChart(level1,this.getData(data));
	       }
	       if($('.rec[level=2]').length>1){
	    	   var	level2 = $('.rec[level=2]').filter('.has_child').eq(0);
		       data.pageId = level2.attr("mytitle");
		       this.createChart(level2,this.getData(data));
	       }
	       if($('.rec[level=3]').length>1){
	    	   var	level3 = $('.rec[level=3]').filter('.has_child').eq(0);
		       data.pageId = level3.attr("mytitle");
		       this.createChart(level3,this.getData(data));
	       }
	    		   
	     },
            //生成分支主体框
           createChart : function(parent,data){
            	if(data&&parent){
            		var mod1top = $('.mod1').offset().top;
            	 	var mod1left = $('.mod1').offset().left;
            		var mod1scrolltop = $('.mod1').scrollTop();
            	 	var mod1scrollleft = $('.mod1').scrollLeft();
					$('#flowchart').css("position","relative");
            		var level = parseInt(parent.attr("level"));
            		var top = parent.offset().top - $('.mod1').offset().top + $('.mod1').scrollTop() + (parent.height())/2,
            		left = parent.offset().left -  $('.mod1').offset().left + $('.mod1').scrollLeft() + (parent.width()+this.prop.paddWidth),
					flowchartwidth = 300+200*(level+1),
            		dotData = [],
            		charts = '<div class="clear line">';
					$('#flowchart').css("width",flowchartwidth);
					this.resize(flowchartwidth-50,$('#flowchart').height());
            		dotData[0]={x:left,y:top,level:level};
            		dotData[1]=[];
            		//获得主线
            		/*for (var i = 0,count=data.length;i<count;i++){
            			if (data[i][3]&&parent.hasClass('mainline')){
            				dotData[0].mainLine = i;
            				break;
            			}
            			
            		}*/
            		this.height = $('#flowchart').height();
            		var pageDatas = data.pageDatas;
            		for ( i = 0 ,count = pageDatas.length; i<count;i++){
						var bfbdata,parentIndex,parentData;
						if(level > 0){
							parentIndex = parent.attr('index');
							parentData = parseFloat($('#flowchart .level'+(level-1)).eq(parentIndex).text());
							bfbdata = formatValue(2,(pageDatas[i].accessRatio*(parentData/100))*100);
						}else{
							bfbdata = formatValue(2,pageDatas[i].accessRatio*100);
						}
						
            			charts +='<div id="rec'+(level+1)+i+
						'" class="'+ ((pageDatas[i].pageId != "离开应用")?"has_child active ":" no_active ") + 
						'rec' + count+'-'+ (i+1) +
						(level==3?'':'') +' rec  rec'+ count +'" level='+(level+1)+
            			' index='+ i +
						' mytitle="'+ pageDatas[i].pageId + 
						'" style = "margin-top:'+(100/count)+'%;"><div class="contenttop" title="'+ pageDatas[i].pageId +'">'+pageDatas[i].pageName+'</div><div class="content">'+
						'<table><tbody><tr><th>访问占比</th><td class="level'+level+'">'+bfbdata+'%</td></tr>'+
						'</tbody></table></div>'+
						'<div class="spandiv"><span class="fa-map-marker setstat" title="设为起始页"></span><span class="fa-list setlist dropdown-toggle" data-toggle="dropdown"></span>'+
						'<ul class="dropdown-menu" role="menu"><li><a>访问次数:'+pageDatas[i].sessionNum+'</a></li>'+
						'<li><a>访问人数:'+pageDatas[i].activeUser+'</a></li>'+
						'<li><a>访问时长:'+formatValue(4,pageDatas[i].userAvgTime)+'</a></li>'+
						'</div></div>';
            		}
            		
            		parent.parent().nextAll('div.line').remove();
            		$('#flowchart').append(charts+'</div>');
					
					for ( i = 0 ,count = pageDatas.length; i<count;i++){
            			var rec = $('#rec'+(level+1)+i);
						dotData[1][i]={};
						dotData[1][i].y = rec.offset().top - $('.mod1').offset().top + $('.mod1').scrollTop() + (rec.height())/2,
						dotData[1][i].x = rec.offset().left - $('.mod1').offset().left + $('.mod1').scrollLeft(),
            			dotData[1][i].data = pageDatas[i].accessRatio;
            			if(pageDatas[i].pageId == "离开应用"){
            				dotData[1][i].active = false;
            			}else{
            				dotData[1][i].active = true;
            			}
            		}
					
            		//this.drawDataBody(parent,dotData);
            		
            		this.drawLine(dotData);
            	}
            	
            	
            },
            //画背景线
            drawLine : function(dotData){
            	  startX = dotData[0].x;
            	  startY = dotData[0].y;
            	  this.clearCanvas(startX-35,0);
            	  for(var i = 0 ,count = dotData[1].length;i<count;i++){
				 	  endY = dotData[1][i].y;
                      endX = dotData[1][i].x;
				 	  if(dotData[0].mainLine!=undefined&&i == dotData[0].mainLine){
					     this.canvasContext.lineWidth = this.canvasProp.mainLineWidth;
				 	  	 this.canvasContext.strokeStyle = this.canvasProp.mainColor;
				 	  }else{
					     this.canvasContext.lineWidth = this.canvasProp.lineWidth;
				 	  	 this.canvasContext.strokeStyle = dotData[1][i].active?this.canvasProp.activeColor:this.canvasProp.inactiveColor;
				 	  	 
				 	  }
				 	  this.canvasContext.beginPath();
					  this.canvasContext.moveTo(startX+14,startY+2);
					  this.canvasContext.bezierCurveTo(startX+60,startY,startX+50,endY+8,endX-3,endY+8);
					  this.canvasContext.lineTo(endX-3,endY-8);
					  this.canvasContext.bezierCurveTo(startX+50,endY-8,startX+60,startY,startX+14,startY-2);
					  this.canvasContext.fillStyle = dotData[1][i].active?"rgba(218, 236, 255, 1)":"#faece3";
					  this.canvasContext.fill();
					  this.canvasContext.closePath();
					  
					  this.canvasContext.beginPath();
					  this.canvasContext.moveTo(endX,endY+10);   
					  this.canvasContext.lineTo(endX,endY-10);   
					  this.canvasContext.stroke();
					  this.canvasContext.closePath();
					  
					  this.canvasContext.lineWidth = 34;
					  this.canvasContext.strokeStyle = "#89b7e8";
					  this.canvasContext.beginPath();
					  this.canvasContext.moveTo(startX,startY-8);   
					  this.canvasContext.lineTo(startX,startY+8);   
					  this.canvasContext.stroke();
					  this.canvasContext.closePath();
					  
				  }
            },
            getData : function(pagedata){
            	var mydata;
            	$.ajax({
        			type : "post",
        			url :"getAccessPathData",
        			data : pagedata,
        			async : false,
        			success : function(data){
        				mydata = data[0];
        			}
        		})
        		
        		return mydata;
            },
            
            clearCanvas : function(x,y){
            	this.canvasContext.clearRect(x,y,$('#flowchart').width(),$('#flowchart').height());
            },
            changeCanvasSize : function(){
				this.canvas = document.getElementById('backline');
				this.canvas.width = $('#flowchart').width();
				this.canvas.height = $('#flowchart').height();
				this.canvasContext=this.canvas.getContext('2d');
			},
			resize : function (w,h){
				var imgData = this.canvasContext.getImageData(0,0,this.canvas.width,this.canvas.height);
				this.canvas.width = w;
				this.canvas.height = h;
				this.canvasContext.putImageData(imgData,0,0);
			}
        };
        // 应用举例 这里是随机 最大支持5个子分支 --begin
        var myFlow = new Flow(),
        qurydata = dataString;
        myFlow.initUI(qurydata);
        $("#flowchart .clear .rec .content").live("click",function(){
        	if ($(this).hasClass('no_active')){
    			return false;
    		}else{
    			qurydata.pageId = $(this).parent().attr("mytitle");
				myFlow.createChart($(this).parent(),myFlow.getData(qurydata));
    			
    		}
        	
        });
        $('#flowchart .rec .setstat').live("click",function(e){
        		qurydata.pageId = $(this).parent().parent().attr('mytitle');
        		myFlow.initUI(qurydata);
        		return false;//阻止冒泡
        });
}
