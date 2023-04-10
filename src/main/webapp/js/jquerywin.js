//打开关闭窗口 wxh 2011-01-17
function showDialog(vid,vtitle)
{
	$(vid+" .title").text(vtitle);
	$(vid).fadeIn("fast");
	//遮罩父窗口
	$.mask({ 
		'opacity': 0.6, 
		'bgcolor': '#F4FFE4', 
		'z': 10 
	}); 
}
function showDialogForSet($vid,width,height,top,left)
{
	$vid.css("width",width);
	$vid.css("height",height);
	$vid.css("top",top);
	$vid.css("left",left);
  	//显示窗口
	$vid.fadeIn("fast");
	//遮罩父窗口
	$.mask({ 
		'opacity': 0.6, 
		'bgcolor': '#F4FFE4', 
		'z': 10 
	});  
}
function hideDialog(vid)
{
	$(vid).fadeOut("fast");
	//撤銷遮罩
	$.unmask();
}
function movewin(vid){
	//跟随鼠标移动div窗口
	$(vid).mousedown(function(event){   
        var offset=$(vid).offset();   
        x1=event.clientX-offset.left;   
        y1=event.clientY-offset.top;   
        $(vid).mousemove(function(event){   
            $(vid).css("left",(event.clientX-x1)+"px");   
            $(vid).css("top",(event.clientY-y1)+"px");   
        });   
        $(vid).mouseup(function(event){   
            $(vid).unbind("mousemove");   
        });   
    }); 
}
