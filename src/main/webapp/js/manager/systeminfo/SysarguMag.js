$(document).ready(function(){ 
	/*绑定sysarguform  查询*/
	$('#sysarguform').ajaxForm({
		beforeSubmit:  arguvalidate,
		dataType: 'json',
		success: arguResponse
	});
	$('#sysarguform').submit();
});
/*参数列表 查询  有效性验证 响应*/
function arguvalidate(){
	$("#sysargumagui [name='errmsg0']").text("查询进行中，请稍后……");;
	$("#sysargumagui [name='loadingimg']").show();
	$("#sysargumagui [name='argutb']").empty();
}
//响应
function arguResponse(data){
	$argutb=$("#sysargumagui [name='argutb']");
	$errmsg=$("#sysargumagui [name='errmsg0']");
	$loadmsg=$("#sysargumagui [name='loadingimg']");
	if(data==null){
   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
   		$loadmsg.hide();
		$argutb.empty();
        return;
   	}
	if(data.errmsg!=""){
		$errmsg.text("错误："+data.errmsg);
		$loadmsg.hide();
		$argutb.empty();
		return;
	}
	/*
	if(data.result==""){
		$errmsg.text("查询结果数：0");
		$argutb.empty();
		$loadmsg.hide();
		return;
	}*/
	//显示查询结果
	argusarr=eval(data.result);
	$errmsg.text("查询结果数："+argusarr.length);	
	$argutb.empty();
	$.each(argusarr,function(index,item){
		var xh=index+1;
		$argutb.append("<tr>"+
							"<td>"+item.arguid+"</td>"+
							"<td>"+item.arguname+"</td>"+
							"<td>"+item.argutype+"</td>"+
							"<td>"+item.arguvalue+"</td>"+
							"<td>"+item.remark+"</td>"+
							"<td><input type='hidden' value='"+item.arguname+"'>"+
							    "<input type='button' value='修改' class='btn'/>"+
								"<input type='button' value='保存' class='btn' style='display:none'/>&nbsp;&nbsp;"+
							    "<span class='flag'></span>"+
							"</td>"+
						  "</tr>");
	});
	$argutb.tbhighligt();//高亮显示当前行
	$loadmsg.hide();
	//绑定 修改 操作
	$argutb.find("input[value='修改']").click(function(){
		$this=$(this);
		$thissiblings=$this.parent().siblings();
		//设置当前行控件可修改
		var arguvalue=$thissiblings.eq(3).text();
		$thissiblings.eq(3).html("<input type='text' class='text' value='"+arguvalue+"'/>");
				
		$this.hide();//隐藏修改按钮
		$this.next().show();//显示保存按钮
	});
	//绑定 保存 操作
	$argutb.find("input[value='保存']").click(function(){
		$this=$(this);
		$thissiblings=$this.parent().siblings();
		var arguname=$thissiblings.eq(1).text();
		var arguvalue=$thissiblings.eq(3).find("input").val();
		//
		$prompt=$this.siblings().last();
		$prompt.text("正在保存……");
		$.getJSON("../SysarguServlet?mode=edt",{arguname:arguname,arguvalue:arguvalue},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
        		$prompt.text("");
        		alert(data.errmsg);
        		return;
        	}
        	//保存成功后，恢复当前行各控件初始状态
			$thissiblings.eq(3).html(arguvalue);
			
			$this.hide();//隐藏保存按钮
			$this.prev().show();//显示修改按钮
			
			$prompt.text("");
	    });
	});
}
