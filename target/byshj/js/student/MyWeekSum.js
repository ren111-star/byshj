$(document).ready(function(){
	if($("#status").val()=='已选'){
		var stuid=$("#stuid").val();
		//当前周信息处理
		var currentweekorder=$("#myweeksum label[name='currentweeksumlabel']").next().val();//获得周序号
		getperweeksum(stuid,currentweekorder,$("#myweeksum label[name='currentweeksumlabel']"));
		//对其它标签做显示/隐藏处理
		$("#myweeksum label").filter(".weeksumwithcontent").click(function(){
			var $this=$(this);//获得当前<a>元素
			var weekorder=$this.next().val();//获得周序号
			var $tb=$("#weeksumtb"+weekorder);//获得连接对应的周总结内容（表）
			if($tb.css("display")=="none"){
				getperweeksum(stuid,weekorder,$this);
				$tb.show();
			}else{
				$tb.hide();
			}
		});
		
		//绑定“保存”按钮
		$("#myweeksum input[name='savebtn']").click(function(){
			var $promptmsg=$(this).prev();
			$promptmsg.show();
			
			var weekorder=$(this).next().val();
			var $thisweektb=$("#weeksumtb"+weekorder);
			var thiscontent=$thisweektb.find("[name='thiscontent']").val();
			var support=$thisweektb.find("[name='support']").val();
			var nextcontent=$thisweektb.find("[name='nextcontent']").val();
			//增加有效性判断20150331 wxh
			if(thiscontent==""){
				alert("本周工作总结不能为空，请输入！");
				$thisweektb.find("[name='thiscontent']").focus();	
				return false;				
			}
			if(nextcontent==""){
				alert("下周工作安排不能为空，请输入！");
				$thisweektb.find("[name='nextcontent']").focus();	
				return false;				
			}
			//////////////////////
			
			$.getJSON("../WeekSumServlet?mode=fillInWeekupForStu",{stuid:stuid,weekorder:weekorder,thiscontent:thiscontent,support:support,nextcontent:nextcontent},function call(data){
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
	}
});

/**获得学生每周总结信息*/
function getperweeksum(stuid,weekorder,$this){
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
       		alert("MyWeekSum.js:getperweeksum"+data.errmsg);
       		return;
       	}
       	var weeksum=eval(data.result);
       	
       	$("#weeksumtb"+weekorder+" textarea[name='thiscontent']").val(weeksum.thiscontent);
       	$("#weeksumtb"+weekorder+" textarea[name='support']").val(weeksum.support);
       	$("#weeksumtb"+weekorder+" textarea[name='nextcontent']").val(weeksum.nextcontent);
       	//
     	$("#weeksumtb"+weekorder+" span[name='thiscontent']").text(weeksum.thiscontent);
       	$("#weeksumtb"+weekorder+" span[name='support']").text(weeksum.support);
       	$("#weeksumtb"+weekorder+" span[name='nextcontent']").text(weeksum.nextcontent);
       	//
       	$("#weeksumtb"+weekorder+" [name='tutorreply']").text(weeksum.tutorreply);
    	$("#weeksumtb"+weekorder+" [name='tutorreview']").text(weeksum.tutorreview);
	});
}
