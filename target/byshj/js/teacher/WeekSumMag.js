$(document).ready(function(){
	//绑定“管理周总结”按钮单击事件
	$("#weeksummagtb").find("input[name='weeksummag']").click(function(){
		var $divshow=$(this).siblings("div");
		if($divshow.css("display")=="none"){
			$divshow.show();
		}else{
			$divshow.hide();
		}
	});
	//绑定“生成进程表”按钮单击事件
	$("#weeksummagtb").find("input[name='generateprogress']").click(function(){
		var stuid=$(this).prev().val();
		var $promptmsg=$(this).prev().prev();
		var $errmsg=$(this).next();
		$errmsg.text("正在导出进程表，请稍候……");
		$.getJSON("../ExcelServlet?mode=exportProgressTableByStu",{stuid:stuid},function call(data){
			$promptmsg.hide();
			if(data==null){
	       		alert("请求异常！[ExcelServlet?mode=exportProgressTableByStu]1");
	            return;
	       	}
			if(data.errmsg!=""){
	       		alert(data.errmsg);
	       		return;
	       	}
			alert("已成功生成进程表！");
			$errmsg.html("&nbsp;&nbsp;<a href='../FileDownloadServlet0?tempfilename="+stuid+"progresstable.xls' title='点击下载文件'>下载进程表</a>");
		});
	});
	//
	$("#weeksummagtb label").filter(".weeksumwithcontent").click(function(){
		var $this=$(this);//获得当前<a>元素
		var weekorder=$this.next().val();//获得周序号
		var stuid=$this.next().next().val();//获得学号
		var $tb=$this.parent().parent().next().find("table");//获得连接对应的周总结内容（表）
		if($tb.css("display")=="none"){
			getperweeksum(stuid,weekorder,$this,$tb);
			$tb.show();
		}else{
			$tb.hide();
		}
	});
	//绑定“保存”按钮
	$("#weeksummagtb input[name='savebtn']").click(function(){
		var $promptmsg=$(this).prev();
		$promptmsg.show();
		
		var weekorder=$(this).next().val();
		var stuid=$(this).next().next().val();//获得学号
		var $thisweektb=$(this).parent().parent().parent();
		var tutorreply=$thisweektb.find("[name='tutorreply']").val();
		var tutorreview=$thisweektb.find("[name='tutorreview']").val();
		
		$.getJSON("../WeekSumServlet?mode=fillInWeekupForTea",{stuid:stuid,weekorder:weekorder,tutorreply:tutorreply,tutorreview:tutorreview},function call(data){
			$promptmsg.hide();
			if(data==null){
	       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	            return;
	       	}
			if(data.errmsg!=""){
	       		alert(data.errmsg);
	       		return;
	       	}
			alert("保存成功！");
		});
	});
	
});
/**获得学生每周总结信息*/
function getperweeksum(stuid,weekorder,$this,$tb){
	var $promptmsg=$this.prev();
	//在显示周总结前加载数据库中的周总结信息
	$promptmsg.show();
	$.getJSON("../WeekSumServlet?mode=getWeekupByWeek",{stuid:stuid,weekorder:weekorder},function call(data){   
		$promptmsg.hide();
		if(data==null){
       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
            return;
       	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		return;
       	}
       	var weeksum=eval(data.result);
       	//
    	$tb.find("span[name='thiscontent']").text(weeksum.thiscontent);
    	$tb.find("span[name='support']").text(weeksum.support);
    	$tb.find("span[name='nextcontent']").text(weeksum.nextcontent);
       	//
    	$tb.find("span[name='tutorreply']").text(weeksum.tutorreply);
    	$tb.find("span[name='tutorreview']").text(weeksum.tutorreview);
    	
    	$tb.find("textarea[name='tutorreply']").val(weeksum.tutorreply);
    	$tb.find("textarea[name='tutorreview']").val(weeksum.tutorreview);
	});
}