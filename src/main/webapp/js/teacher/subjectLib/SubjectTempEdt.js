//课题基本信息编辑界面
$(document).ready(function(){
	//根据code表初始化下拉列表框(需同步，未完成)
    $subtmpedt=$("#subtempform");
	code_getcode_select("ktlb","#subtempform [name='subsort']");//课题类别
	code_getcode_select("ktxz","#subtempform [name='subkind']");//课题性质
	code_getcode_select("ktly","#subtempform [name='subsource']");//课题来源
	code_getcode_select("ktlx","#subtempform [name='subtype']");//课题类型
	code_getcode_select("ktfx","#subtempform [name='subdirection']");//课题方向（软件、硬件、网络）
	//
	var options={
		beforeSubmit:  subtempvalidate,
		dataType: 'json',
		success:      subtempResponse
	};
	$('#subtempform').ajaxForm(options);
	
});
//表单有效性验证
function subtempvalidate(){
	var subname=$("#subtempform [name='subname']").val();
	
	if(subname==""){
		alert("课题名称不能为空，请输入！");
		$("#subtempform [name='subname']").focus();	
		return false;				
	}
	
	var oldargulen=$("#subtempform [name='oldargu']").val().length;//概述（原名原始参数），不少于400字
	var workcontentlen=$("#subtempform [name='content']").val().length;//工作内容，不少于400字
	var requirementlen=$("#subtempform [name='requirement']").val().length;//基本要求，不少于50字
	
	if(oldargulen<400){
		if(confirm("概述当前的字数为"+oldargulen+"，少于400字，确定要暂时保存？")==false) {
			$("#subtempform [name='oldargu']").focus();	
			return false;
		}
	    //alert("概述应不少于400字，当前的字数为"+oldargulen+"，请输入！");
	}
	if(workcontentlen<400){
		if(confirm("工作内容当前的字数为"+workcontentlen+"，少于400字，确定要暂时保存？")==false) {
			$("#subtempform [name='content']").focus();	
			return false;
		}
	    //alert("工作内容应不少于400字，当前的字数为"+workcontentlen+"，请输入！");
	}
	if(requirementlen<50){
		if(confirm("工作基本要求当前的字数为"+requirementlen+"，少于50字，确定要暂时保存？")==false) {
			$("#subtempform [name='requirement']").focus();	
			return false;
		}
	    //alert("工作基本要求应不少于50字，当前的字数为"+requirementlen+"，请输入！");
	}
	/*增加内容字数限制结束*/
	////
	$errmsg=$("#subtempform [name='errmsg']");
	$errmsg.text("正在保存课题信息，请稍后！");
	$errmsg.prepend("<img src='../images/loading.gif'></img>&nbsp;&nbsp;");
}
//异步提交成功后的响应
function subtempResponse(data){
	$errmsg=$("#subtempform [name='errmsg']");
	if(data==null){
   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
        return;
   	}
	if(data.errmsg!=""){
		//提示出错
		$errmsg.text(data.errmsg);
	}else{
		var subid=data.result;
		//$("#highlighttr").val(subid);
		//隐藏编辑窗口，刷新父窗口
		$errmsg.text("");
		hideDialog("#subjecttempedt");
		$('#subtemplistform').submit();
	}
}

//打开课题新增、修改窗口
function subjecttemp_showwin(vsubid){
	var $errmsg=$("#subtemplistform name[errmsg0]");
	if(vsubid==""){
		$("#subtempform").resetForm();//jquery.form.js中方法
		$("#subtempform [name='subid']").val("");
		$errmsg.text("");
		showDialog("#subjecttempedt",'增加新课题（暂存库）');
		//$("#progtbody").empty();
	}else{
		$.getJSON("../SubjectLibServlet?mode=gettemp",{subid:vsubid},function call(data){ 
			if(data==null){
	       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	            return;
	       	}
			if(data.errmsg!=""){
	    		alert(data.errmsg);
	    		return;
	    	}
	    	var vsubject=data.result;
	    	$("#subtempform [name='subid']").val(vsubject.subid);
	    	$("#subtempform [name='subname']").val(vsubject.subname);
	    	$("#subtempform [name='subsort']").val(vsubject.subsort);
	    	$("#subtempform [name='subkind']").val(vsubject.subkind);
	    	$("#subtempform [name='subsource']").val(vsubject.subsource);
	    	$("#subtempform [name='subtype']").val(vsubject.subtype);
	    	$("#subtempform [name='subdirection']").val(vsubject.subdirection);
	    	$("#subtempform [name='subaddress']").val(vsubject.isoutschool);
	    	$("#subtempform [name='oldargu']").val(vsubject.oldargu);
	    	$("#subtempform [name='content']").val(vsubject.content);
	    	$("#subtempform [name='requirement']").val(vsubject.requirement);
	    	$("#subtempform [name='refpapers']").val(vsubject.refpapers);
	    	$("#subtempform [name='remark']").val(vsubject.remark);
	    	//清空进程表
	    	$("#subtempform [name^='progresstime_']").val("");
	    	$("#subtempform [name^='progressitem_']").val("");
	    	//进程表
	    	$("#subtempform [name='subprog']").val(vsubject.subprog);
	    	var progressarray=eval(vsubject.progress);
	    	$.each(progressarray,function(index,item){
	    		$("#subtempform [name='progresstime_"+item.inorder+"']").val(item.startenddate);
	    		$("#subtempform [name='progressitem_"+item.inorder+"']").val(item.content);
	    	});
	    	showDialog("#subjecttempedt",'修改课题（暂存库）');
	    	//隐藏提示信息
	    	
	    	$errmsg.text("");
		});
	}
	
}

