$(document).ready(function(){ 
	/*绑定code列表syscodeform  查询*/
	var optionscode={
		beforeSubmit:  codevalidate,
		dataType: 'json',
		success: codeResponse
	};
	$('#syscodeform').ajaxForm(optionscode);
	
	//绑定增加代码 操作
	$("#syscodemagui input[name='add']").click(function(){
		$this=$(this);
		$errmsg=$("#syscodemagui [name='errmsg']");
		var codeno=$this.parent().siblings().eq(1).find("input").val();
		var codename=$this.parent().siblings().eq(2).find("input").val();
		var codevalue=$this.parent().siblings().eq(3).find("input").val();
		var codecontent=$this.parent().siblings().eq(4).find("input").val();
		//有效性验证
		
		if(codeno==""){
			$errmsg.text("代码编号不能为空！");
			$this.parent().siblings().eq(1).find("input").focus();
			return;
		}
		if(codename==""){
			$errmsg.text("代码名称不能为空！");
			$this.parent().siblings().eq(2).find("input").focus();
			return;
		}
		if(codevalue==""){
			$errmsg.text("代码值不能为空！");
			$this.parent().siblings().eq(3).find("input").focus();
			return;
		}
		if(codecontent==""){
			$errmsg.text("代码内容不能为空！");
			$this.parent().siblings().eq(4).find("input").focus();
			return;
		}
		//组装结果
		var code={codeid:"",codeno:codeno,codename:codename,codevalue:codevalue,codecontent:codecontent};
		var codejson=$.toJSON(code);
		//
		$errmsg.text("正在增加"+codeno+"……");
		$.getJSON("../SyscodeServlet?mode=edt",{code:codejson},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
        		alert(data.errmsg);
        		$errmsg.text("");
        		return;
        	}
        	//增加成功后，刷新代码列表
        	$errmsg.text("");
        	//清空控件值  教研室select中值保持不变
        	$this.parent().siblings().eq(1).find("input").val("");
			$this.parent().siblings().eq(2).find("input").val("");
			$this.parent().siblings().eq(3).find("input").val("");
			$this.parent().siblings().eq(4).find("input").val("");
			
			$('#syscodeform').submit();
	    });
	});
});
/*代码列表 查询  有效性验证 响应*/
function codevalidate(){
	$("#syscodemagui [name='errmsg0']").text("查询进行中，请稍后……");;
	$("#syscodemagui [name='loadingimg']").show();
	$("#syscodemagui [name='codetb']").empty();
}
//响应
function codeResponse(data){
	$codetb=$("#syscodemagui [name='codetb']");
	$errmsg=$("#syscodemagui [name='errmsg0']");
	$loadmsg=$("#syscodemagui [name='loadingimg']");
	if(data==null){
   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
   		$loadmsg.hide();
		$codetb.empty();
        return;
   	}
	if(data.errmsg!=""){
		$errmsg.text("错误："+data.errmsg);
		$loadmsg.hide();
		$codetb.empty();
		return;
	}
	/*
	if(data.result==""){
		$errmsg.text("查询结果数：0");
		$codetb.empty();
		$loadmsg.hide();
		return;
	}*/
	//显示查询结果
	codesarr=eval(data.result);
	$errmsg.text("查询结果数："+codesarr.length);	
	$codetb.empty();
	$.each(codesarr,function(index,item){
		var xh=index+1;
		$codetb.append("<tr>"+
							"<td>"+xh+"</td>"+
							"<td>"+item.codeno+"</td>"+
							"<td>"+item.codename+"</td>"+
							"<td>"+item.codevalue+"</td>"+
							"<td>"+item.codecontent+"</td>"+
							"<td><input type='hidden' value='"+item.codeid+"'>"+
							    "<input type='button' value='修改' class='btn'/>"+
								"<input type='button' value='保存' class='btn' style='display:none'/>&nbsp;&nbsp;"+
							    "<input type='button' value='删除' class='btn'/>&nbsp;<span class='flag'></span>"+
							"</td>"+
						  "</tr>");
	});
	$codetb.tbhighligt();//高亮显示当前行
	$loadmsg.hide();
	//绑定 修改 操作
	$codetb.find("input[value='修改']").click(function(){
		$this=$(this);
		$thissiblings=$this.parent().siblings();
		//设置当前行控件可修改
		var codename=$thissiblings.eq(2).text();
		$thissiblings.eq(2).html("<input type='text' class='text' value='"+codename+"'/>");
		var codecontent=$thissiblings.eq(4).text();
		$thissiblings.eq(4).html("<input type='text' class='text' value='"+codecontent+"'/>");
				
		$this.hide();//隐藏修改按钮
		$this.next().show();//显示保存按钮
	});
	//绑定 保存 操作
	$codetb.find("input[value='保存']").click(function(){
		$this=$(this);
		$thissiblings=$this.parent().siblings();
		var codeid=$this.siblings().eq(0).val();
		var codeno=$thissiblings.eq(1).text();
		var codename=$thissiblings.eq(2).find("input").val();
		var codevalue=$thissiblings.eq(3).text();
		var codecontent=$thissiblings.eq(4).find("input").val();
	
		var code={codeid:codeid,codeno:codeno,codename:codename,codevalue:codevalue,codecontent:codecontent};
		var codejson=$.toJSON(code);
		//
		$prompt=$this.siblings().last();
		$prompt.text("正在保存……");
		$.getJSON("../SyscodeServlet?mode=edt",{code:codejson},function call(data){ 
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
			$thissiblings.eq(2).html(code.codename);
			$thissiblings.eq(4).html(code.codecontent);
			
			$this.hide();//隐藏保存按钮
			$this.prev().show();//显示修改按钮
			
			$prompt.text("");
	    });
	});
	//绑定 删除 操作
	$codetb.find("input[value='删除']").click(function(){
		$this=$(this);
		var codeno=$this.parent().siblings().eq(1).text();
		var codevalue=$this.parent().siblings().eq(3).text();
		var codeid=$this.siblings().eq(0).val();
		if(confirm("确定要删除代码“"+codeno+"”的值“"+codevalue+"”？")==false) return;
		//
		$prompt=$this.siblings().last();
		$prompt.text("正在删除……");
		$.getJSON("../SyscodeServlet?mode=del",{codeid:codeid},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
				$prompt.text("");
        		alert(data.errmsg);
        		return;
        	}
        	//删除成功后，刷新代码列表
			$('#syscodeform').submit();
	    });
	});
}
