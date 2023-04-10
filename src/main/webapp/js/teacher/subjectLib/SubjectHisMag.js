$(document).ready(function(){
	//绑定查询表单的ajax提交
	var options={
		beforeSubmit:  subhislistvalidate,
		dataType: 'json',
		success:      subhislistResponse
	};
	$('#subhislistform').ajaxForm(options);
});
function subhislistvalidate(){
	$("#subhismagui [name='errmsg0']").text("查询进行中，请稍后……");;
	$("#subhismagui [name='loadingimg']").show();
	$("#subhismagui [name='tbody']").empty();
}
function subhislistResponse(data){
	$subhislisttb=$("#subhismagui [name='tbody']");
	$errmsg=$("#subhismagui [name='errmsg0']");
	$loadmsg=$("#subhismagui [name='loadingimg']");
	if(data==null){
   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
   		$loadmsg.hide();
   		$subhislisttb.empty();
        return;
   	}
	if(data.errmsg!=""){
		$errmsg.text("错误："+data.errmsg);
		$loadmsg.hide();
		$subhislisttb.empty();
		return;
	}
	if(data.result==""){
		$errmsg.text("查询结果数：0");
		$subhislisttb.empty();
		$loadmsg.hide();
		return;
	}
	//显示查询结果
	subhissarr=eval(data.result);
	$errmsg.text("查询结果数："+subhissarr.length);	
	$subhislisttb.empty();
	$.each(subhissarr,function(index,item){
		var xh=index+1;
		$subhislisttb.append("<tr>"+
							"<td>"+xh+"</td>"+
							"<td>"+item.usedyear+"</td>"+
							"<td><a href='subjectLib/SubjectHisView.jsp?subid="+item.subid+"&usedyear="+item.usedyear+"' target='_blank'>"+item.subname+"</a></td>"+
							"<td>"+item.stuid+"</td>"+
							"<td>"+item.sname+"</td>"+
							"<td><input type='button' value='复制到当前库' class='btn' name='copyfromhisbtn'/>" +
							"<input type='hidden' value='"+item.usedyear+"'/>" +
							"<input type='hidden' value='"+item.subid+"'/>" +
							"<input type='hidden' value='"+item.subname+"'/>" +
							"&nbsp;<span class='flag'></span>"+
							"</td>"+
						  "</tr>");
	});
	$subhislisttb.tbhighligt();//高亮显示当前行
	$loadmsg.hide();
	//绑定 复制到当前库 操作
	$subhislisttb.find("input[name='copyfromhisbtn']").click(function(){
		$this=$(this);
		var usedyear=$this.siblings().eq(0).val();
		var subid=$this.siblings().eq(1).val();
		var subname=$this.siblings().eq(2).val();
		if(confirm("确定要复制该课题<"+subname+">到当前库？")==false) return;
		//
		$prompt=$this.siblings().last();
		$prompt.text("正在复制……");
		$.getJSON("../SubjectLibServlet?mode=copyfromhis",{usedyear:usedyear,subid:subid},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
				$prompt.text("");
        		alert(data.errmsg);
        		return;
        	}
			alert("复制成功！");
			$prompt.text("");
	    });
	});
}