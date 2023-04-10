$(document).ready(function(){
	//绑定查询表单的ajax提交
	var options={
		beforeSubmit:  subtemplistvalidate,
		dataType: 'json',
		success:      subtemplistResponse
	};
	$('#subtemplistform').ajaxForm(options);
//	$('#subtemplistform').submit();
	
	$.include("../js/teacher/subjectLib/SubjectTempEdt.js");
});
function subtemplistvalidate(){
	$("#subtmpmagui [name='errmsg0']").text("查询进行中，请稍后……");;
	$("#subtmpmagui [name='loadingimg']").show();
	$("#subtmpmagui [name='tbody']").empty();
}
function subtemplistResponse(data){
	$subtmplisttb=$("#subtmpmagui [name='tbody']");
	$errmsg=$("#subtmpmagui [name='errmsg0']");
	$loadmsg=$("#subtmpmagui [name='loadingimg']");
	if(data==null){
   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
   		$loadmsg.hide();
   		$subtmplisttb.empty();
        return;
   	}
	if(data.errmsg!=""){
		$errmsg.text("错误："+data.errmsg);
		$loadmsg.hide();
		$subtmplisttb.empty();
		return;
	}
	if(data.result==""){
		$errmsg.text("查询结果数：0");
		$subtmplisttb.empty();
		$loadmsg.hide();
		return;
	}
	//显示查询结果
	subtmpsarr=eval(data.result);
	$errmsg.text("查询结果数："+subtmpsarr.length);	
	$subtmplisttb.empty();
	$.each(subtmpsarr,function(index,item){
		var xh=index+1;
		$subtmplisttb.append("<tr>"+
							"<td>"+xh+"</td>"+
							"<td>"+item.subname+"</td>"+
							"<td>"+item.remark+"</td>"+
							"<td>"+
							"<input type='hidden' value='"+item.subid+"'/>" +
							"<input type='hidden' value='"+item.subname+"'/>" +
							"<input type='button' value='转移到当前库' class='btn' name='tranferfromtmp'/>&nbsp;&nbsp;"+
							"<input type='button' value='修改' class='btn' name='modify'/>&nbsp;&nbsp;"+
							"<input type='button' value='删除' class='btn' name='delete'/>&nbsp;&nbsp;"+
							"&nbsp;<span class='flag'></span>"+
							"</td>"+
						  "</tr>");
	});
	$subtmplisttb.tbhighligt();//高亮显示当前行
	$loadmsg.hide();
	//绑定 转移到当前库 操作
	$subtmplisttb.find("input[name='tranferfromtmp']").click(function(){
		$this=$(this);
		var subid=$this.siblings().eq(0).val();
		var subname=$this.siblings().eq(1).val();
		if(confirm("确定要转移课题<"+subname+">到当前库？")==false) return;
		//
		$prompt=$this.siblings().last();
		$prompt.text("正在转移……");
		$.getJSON("../SubjectLibServlet?mode=tranferfromtemp",{subid:subid},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
				$prompt.text("");
        		alert(data.errmsg);
        		return;
        	}
			alert("转移成功！");
			$prompt.text("");
			$('#subtemplistform').submit();
			$('#mysubrefresh').trigger('click');
	    });
	});
	//绑定 删除 操作
	$subtmplisttb.find("input[name='delete']").click(function(){
		$this=$(this);
		var subid=$this.siblings().eq(0).val();
		var subname=$this.siblings().eq(1).val();
		if(confirm("确定要删除课题<"+subname+">？")==false) return;
		//
		$prompt=$this.siblings().last();
		$prompt.text("正在删除……");
		$.getJSON("../SubjectLibServlet?mode=del",{subid:subid},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
				$prompt.text("");
        		alert(data.errmsg);
        		return;
        	}
			alert("删除成功！");
			$prompt.text("");
			$('#subtemplistform').submit();
			
	    });
	});
	//绑定 修改 操作
	$subtmplisttb.find("input[name='modify']").click(function(){
		$this=$(this);
		var subid=$this.siblings().eq(0).val();
		subjecttemp_showwin(subid);
	});
}