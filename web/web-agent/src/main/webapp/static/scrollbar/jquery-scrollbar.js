(function(win,udf){'use strict';var datakey='jquery-scrollbar',$=win.$,body=win.document.body,isPlaceholderScroll=true,reRenderEvent='DOMSubtreeModified DOMNodeInserted DOMNodeRemoved DOMNodeRemovedFromDocument DOMNodeInsertedIntoDocument DOMAttrModified DOMCharacterDataModified',defaults={width:'auto',height:'auto',mousewheelAsix:'y',duration:345,speed:30,minWidth:100,minHeight:100,canCrossBoundary:!0,onscrollx:$.noop,onscrolly:$.noop};$.fn.scrollbar=function(settings){var run=$.type(settings)==='string',args=[].slice.call(arguments,1),options=$.extend({},defaults),$element,instance;if(run&&run[0]!=='_'){if(!this.length)return;$element=$(this[0]);instance=$element.data(datakey);if(!instance)$element.data(datakey,instance=new Constructor($element[0],options)._init());return Constructor.prototype[settings]?Constructor.prototype[settings].apply(instance,args):udf}else if(!run){options=$.extend(options,settings)}return this.each(function(){var element=this,instance=$(element).data(datakey);if(!instance){$(element).data(datakey,instance=new Constructor(element,options)._init())}})};function Constructor(element,options){var the=this;the.$element=$(element);the.options=options}Constructor.prototype={_init:function(){var the=this,$element=the.$element,elementTagName=$element[0].tagName,$container,$content,$trackX,$trackY,$thumbX,$thumbY;if(elementTagName==='TBODY'){$element=$element.closest('table').clone().insertAfter($element.closest('table')).empty().append($element)}$element.wrap('<div class="'+datakey+'-container"/>');$container=$element.parent();if(isPlaceholderScroll){$element.wrap('<div class="'+datakey+'-content"/>');$content=$element.parent();$trackX=$('<div class="'+datakey+'-track-x"/>').appendTo($container);$trackY=$('<div class="'+datakey+'-track-y"/>').appendTo($container);$thumbX=$('<div class="'+datakey+'-thumb-x"/>').appendTo($trackX);$thumbY=$('<div class="'+datakey+'-thumb-y"/>').appendTo($trackY)}the.$container=$container;the.$content=$content;the.$trackX=$trackX;the.$trackY=$trackY;the.$thumbX=$thumbX;the.$thumbY=$thumbY;the.thumbX=0;the.thumbY=0;$element.bind(reRenderEvent,$.proxy(the.render,the));the.render();return the},_drag:function(){var the=this,$trackX=the.$trackX,$trackY=the.$trackY,$thumbX=the.$thumbX,$thumbY=the.$thumbY;$thumbX.drag({axis:'x',cursor:!1,min:{left:0},max:{left:the.maxX},ondragstart:function(){$trackX.addClass('active')},ondrag:function(){the.thumbX=parseInt($thumbX.css('left'));the._scroll('x',!0)},ondragend:function(){$trackX.removeClass('active')}});$thumbY.drag({axis:'y',cursor:!1,min:{top:0},max:{top:the.maxY},ondragstart:function(){$trackY.addClass('active')},ondrag:function(){the.thumbY=parseInt($thumbY.css('top'));the._scroll('y',!0)},ondragend:function(){$trackY.removeClass('active')}})},_wheel:function(){if(!isPlaceholderScroll)return;var the=this,options=the.options,$container=the.$container,$track=options.mousewheelAsix==='x'?the.$trackX:the.$trackY;$container.mousewheel({onmousewheeltart:function(){$track.addClass('active')},onmousewheel:function(dir){if(options.mousewheelAsix==='x'){the.thumbX+=-dir*the.speedX;if(dir>0&&the.thumbX<=0||dir<0&&the.thumbX>=the.maxX){if(options.canCrossBoundary)$container.mousewheel('preventDefault',!1)}else{if(options.canCrossBoundary)$container.mousewheel('preventDefault',!0)}the._scroll('x')}else{the.thumbY+=-dir*the.speedY;if(dir>0&&the.thumbY<=0||dir<0&&the.thumbY>=the.maxY){if(options.canCrossBoundary)$container.mousewheel('preventDefault',!1)}else{if(options.canCrossBoundary)$container.mousewheel('preventDefault',!0)}the._scroll('y')}},onmousewheelend:function(){$track.removeClass('active')}})},_click:function(){if(!isPlaceholderScroll)return;var the=this,$trackX=the.$trackX,$trackY=the.$trackY;$trackX.click(function(e){if(e.target===this){var clickX=e.pageX-$(this).offset().left,minX=the.thumbX,maxX=minX+　the.thumbW;the.thumbX=clickX<minX?clickX:minX+clickX-maxX;the._scroll('x',the.options.duration)}return!1});$trackY.click(function(e){if(e.target===this){var clickY=e.pageY-$(this).offset().top,minY=the.thumbY,maxY=minY+　the.thumbH;the.thumbY=clickY<minY?clickY:minY+clickY-maxY;the._scroll('y',the.options.duration)}return!1})},_scroll:function(axis,durationORdontToggleClass,callback){var the=this,options=the.options,left,top,duration=0,dontToggleClass=durationORdontToggleClass===true;duration=dontToggleClass?0:(durationORdontToggleClass||0);callback=callback||$.noop;if(~axis.indexOf('x')){if(the.thumbX<0)the.thumbX=0;if(the.thumbX>the.maxX)the.thumbX=the.maxX;left=the.maxX?the.thumbX*the.maxW/the.maxX:0;if(isPlaceholderScroll){if(!dontToggleClass)the.$trackX.addClass('active');the.$thumbX.animate({left:the.thumbX},duration);the.$content.animate({left:-left},duration,function(){if(!dontToggleClass)the.$trackX.removeClass('active');callback.call(this)})}else{the.$container.animate({scrollLeft:left},duration,callback)}options.onscrollx.call(the.$element[0],the.thumbX/the.maxX)}if(~axis.indexOf('y')){if(the.thumbY<0)the.thumbY=0;if(the.thumbY>the.maxY)the.thumbY=the.maxY;top=the.maxY?the.thumbY*the.maxH/the.maxY:0;if(isPlaceholderScroll){if(!dontToggleClass)the.$trackY.addClass('active');the.$thumbY.animate({top:the.thumbY},duration);the.$content.animate({top:-top},duration,function(){if(!dontToggleClass)the.$trackY.removeClass('active');callback.call(this)})}else{the.$container.animate({scrollTop:top},duration,callback)}options.onscrolly.call(the.$element[0],the.thumbY/the.maxY)}if(axis==='xy'&&isPlaceholderScroll&&!dontToggleClass){the.$trackX.removeClass('active');the.$trackY.removeClass('active')}},options:function(key,val){if($.type(key)==='string'&&val===udf)return this.options[key];var map={};if($.type(key)==='object')map=key;else map[key]=val;this.options=$.extend(this.options,map)},render:function(settings){if(settings)this.options=$.extend(this.options,settings);var the=this,options=the.options,$element=the.$element,$container=the.$container,outerWidth=$element.outerWidth(),outerHeight=$element.outerHeight(),containerWidth=options.width==='auto'?$container.innerWidth():options.width,containerHeight=options.height==='auto'?$container.innerHeight():options.height;if(!isPlaceholderScroll){$container.css({width:containerWidth,height:containerHeight,'overflow-scrolling':'touch',overflow:'auto'});the.maxX=the.maxW=outerWidth-$container[0].clientWidth;the.maxY=the.maxH=outerHeight-$container[0].clientHeight;if(!the.has1stRender){$container.scroll(function(){the.thumbX=this.scrollLeft;the.thumbY=this.scrollTop})}the.has1stRender=!0;return the}var $trackX=the.$trackX,$trackY=the.$trackY,$thumbX=the.$thumbX,$thumbY=the.$thumbY,barWidth=options.width*containerWidth/outerWidth,barHeight=options.height*containerHeight/outerHeight,lastRatioX,lastRatioY;$container.css({width:containerWidth,height:containerHeight});if(barWidth<options.minWidth)barWidth=options.minWidth;$thumbX.css('width',barWidth);if(barHeight<options.minHeight)barHeight=options.minHeight;$thumbY.css('height',barHeight);the.maxW=outerWidth-containerWidth;the.maxH=outerHeight-containerHeight;if(the.maxW>0)$trackX.show();else $trackX.hide();if(the.maxH>0)$trackY.show();else $trackY.hide();lastRatioX=the.maxX?the.thumbX/the.maxX:0;lastRatioY=the.maxY?the.thumbY/the.maxY:0;the.thumbW=barWidth;the.thumbH=barHeight;the.maxX=containerWidth-barWidth;the.maxY=containerHeight-barHeight;if(the.maxX<0)the.maxX=0;if(the.maxY<0)the.maxY=0;the.speedX=the.maxX*options.speed/the.maxW;the.speedY=the.maxY*options.speed/the.maxH;if(the.has1stRender){the.thumbX=lastRatioX*the.maxX;the.thumbY=lastRatioY*the.maxY;the._scroll('xy')}else{the._click()}if($.fn.drag){the._drag();$thumbX.drag('options',{max:{left:the.maxX,}});$thumbY.drag('options',{max:{top:the.maxY,}})}if($.fn.mousewheel){the._wheel()}the.has1stRender=!0;return the},x:function(target,fn){var the=this,options=the.options,$element=the.$element,$target;if(target===udf)return the.maxX?the.thumbX/the.maxX:0;if($.type(target)==='number'){the.thumbX=target*the.maxX;the._scroll('x',options.duration,fn)}else{$target=$(target,$element);if($target.length){the.thumbX=the.maxX*($target.offset().left-$element.offset().left)/the.maxW;the._scroll('x',options.duration,fn)}}},y:function(target,fn){var the=this,options=the.options,$element=the.$element,$target;if(target===udf)return the.maxY?the.thumbY/the.maxY:0;if($.type(target)==='number'){the.thumbY=target*the.maxY;the._scroll('y',options.duration,fn)}else{$target=$(target,$element);if($target.length){the.thumbY=the.maxY*($target.offset().top-$element.offset().top)/the.maxH;the._scroll('y',options.duration,fn)}}},top:function(fn){return this.y(0,fn)},right:function(fn){return this.x(1,fn)},bottom:function(fn){return this.y(1,fn)},left:function(fn){return this.x(0,fn)},};function _isPlaceholderScroll(){var $iframe=$('<iframe>').appendTo(body),$div=$('<div/>').appendTo($iframe.contents()[0].body),clientWidth;$div.css({width:100,height:100,position:'absolute',top:-999999,left:-999999,padding:0,margin:0,overflow:'scroll'});clientWidth=$div[0].clientWidth;$iframe.remove();return clientWidth<100}})(this);