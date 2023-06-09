//课题基本信息编辑界面
$(document).ready(function(){
	//根据code表初始化下拉列表框(需同步，未完成)
	code_getcode_select("ktlb","#subsort");//课题类别
	code_getcode_select("ktxz","#subkind");//课题性质
	code_getcode_select("ktly","#subsource");//课题来源
	code_getcode_select("ktlx","#subtype");//课题类型
	code_getcode_select("ktfx","#subdirectionselect");//课题方向（软件、硬件、网络）
	code_getspeciality("#speciality");//适合专业checkbox
	//
	var options={
		beforeSubmit:  subvalidate,
		dataType: 'json',
		success:      subResponse
	};
	$('#subform').ajaxForm(options);
	//绑定修改信息的‘保存’按钮
	$("#modifybasesavebtn").click(function(){
		 $("#subform").ajaxSubmit({
		 	beforeSubmit:  subvalidate,
			dataType: 'json',
			url:"../SubjectServlet?mode=modifybaseinfo",
			success:      subResponse
		 });
	});  
	//查询教师按钮绑定click事件
	$("#searchbtn").click(function(event){//显示查找窗口
			$("#searchteacher").fadeIn("slow");
			//据输入的部分姓名，模糊查询符合条件的教师信息
			var othertname=$("#othertname").val();
			$.getJSON("../TeacherServlet?mode=gets",{tname:othertname,tdept:""},function call(data){
				if(data==null){
		       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		            return;
		       	}
				if(data.errmsg!=""){
		       		alert(data.errmsg);
		       		return;
		       	}
		       	var tarray=eval(data.result);
		       	var $tbody=$("#teacherlist");
		       	$tbody.empty();
		       	$.each(tarray,function(index,item){
		       		$tbody.append("<tr><td width='40%'>"+item.tid+"</td><td>"+item.tname+"</td></tr>");
		       	});
		       	$tbody.tbhighligt();//高亮显示当前行
		       	$("#teacherlist tr").dblclick(function(){
		       		$("#othertid").val($(this).children().eq(0).text());
		       		$("#othertname").val($(this).children().eq(1).text());
		       		$("#searchteacher").hide();
		       	});
			});
		});
	//隐藏查询教师窗口	
	$(".searchwin .close0").click(function(event){
		$("#searchteacher").fadeOut("slow");
	});
	
	//绑定输入框的change事件
	changeInputforsub();
});
//绑定输入框的change事件
function changeInputforsub(){
	var $hipoldargu=$("#oldargu").parent().prev().children().last();
	var $hipworkcontent=$("#workcontent").parent().prev().children().last();
	var $hiprequirement=$("#requirement").parent().prev().children().last();
	$("#oldargu").change(function(event){
		$hipoldargu.text($("#oldargu").val().length);
	});
	$("#workcontent").change(function(event){
		$hipworkcontent.text($("#workcontent").val().length);
	});
	$("#requirement").change(function(event){
		$hiprequirement.text($("#requirement").val().length);
	});
}
//表单有效性验证
function subvalidate(){
	var othertid=$("#othertid").val();
	var othertname=$("#othertname").val();
	if(othertid==""&&othertname!=""){
		$("#errmsg").text("请选择其他指导教师！");
		$("#searchbtn").focus();	
		return false;
	}
	var subname=$("#subname").val();
	var subsort=$("#subsort").val();
	var subkind=$("#subkind").val();
	var subsource=$("#subsource").val();
	var subtype=$("#subtype").val();
	
	if(subname==""){
		alert("课题名称不能为空，请输入！");
		$("#subname").focus();	
		return false;				
	}
	if(subsort==""){
		alert("课题类别不能为空，请输入！");
		$("#subsort").focus();	
		return false;				
	}
	if(subkind==""){
		alert("课题性质不能为空，请输入！");
		$("#subkind").focus();	
		return false;				
	}
	if(subsource==""){
		alert("课题来源不能为空，请输入！");
		$("#subsource").focus();	
		return false;				
	}
	if(subtype==""){
		alert("课题类型不能为空，请输入！");
		$("#subtype").focus();	
		return false;				
	}
	/*2012/12/10 wxh增加内容字数限制开始*/
	/*2014/11/09 修改若字数不够允许暂存 wxh*/
	var oldargulen=$("#oldargu").val().length;//概述（原名原始参数），不少于400字
	var workcontentlen=$("#workcontent").val().length;//工作内容，不少于400字
	var requirementlen=$("#requirement").val().length;//基本要求，不少于50字
	
	if(oldargulen<400){
		if(confirm("概述当前的字数为"+oldargulen+"，少于400字，确定要暂时保存？")==false) {
			$("#oldargu").focus();	
			return false;
		}
	    //alert("概述应不少于400字，当前的字数为"+oldargulen+"，请输入！");
	}
	if(workcontentlen<400){
		if(confirm("工作内容当前的字数为"+workcontentlen+"，少于400字，确定要暂时保存？")==false) {
			$("#workcontent").focus();	
			return false;
		}
	    //alert("工作内容应不少于400字，当前的字数为"+workcontentlen+"，请输入！");
	}
	if(requirementlen<50){
		if(confirm("工作基本要求当前的字数为"+requirementlen+"，少于50字，确定要暂时保存？")==false) {
			$("#requirement").focus();	
			return false;
		}
	    //alert("工作基本要求应不少于50字，当前的字数为"+requirementlen+"，请输入！");
	}
	/*增加内容字数限制结束*/
	//课题适合专业不能为空
	if($("input:checked").length==0){
		alert("请选择课题适合专业！");
		$('input[name^="spec_"]').focus();
		return false;
	}
	//每个课题只能对应一个专业
	if($("input:checked").length>1){
		alert("每个课题只对应一个适合专业，请重新选择！");
		$('input[name^="spec_"]').focus();
		return false;
	}
	//课题方向不能为空
	var subdirection=$("#subdirectionselect").val();
	if(subdirection==""){
		alert("课题方向不能为空，请输入！");
		$("#subdirectionselect").focus();	
		return false;				
	}
	////
	$("#errmsg").text("正在提交课题信息，请稍后！");
	$("#errmsg").prepend("<img src='../images/loading.gif'></img>&nbsp;&nbsp;");
}
//异步提交成功后的响应
function subResponse(data){
	if(data==null){
   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
        return;
   	}
	if(data.errmsg!=""){
		//提示出错
		$("#errmsg").text(data.errmsg);
	}else{
		var subid=data.result;
		$("#highlighttr").val(subid);
		//隐藏编辑窗口，刷新父窗口
		$("#errmsg").text("");
		hideDialog("#subjectedt");
		var tutorid=$("#tutorid").val();
		getsubBytid(tutorid);//刷新父窗口的课题列表信息
		
	}
}
//重置课题表单中的信息
function resetsub(){
	$("#subform").resetForm();//jquery.form.js中方法
	$("#progtbody").empty();
	//隐藏提示信息
	$("#errmsg").text("");
	//$("input[type='submit']").attr("disabled","false");
}
//打开课题新增、修改窗口
function subject_showwin(vsubid,vtype){
	//2015-12-1 字数提示
	var $hipoldargu=$("#oldargu").parent().prev().children().last();
	var $hipworkcontent=$("#workcontent").parent().prev().children().last();
	var $hiprequirement=$("#requirement").parent().prev().children().last();
	///
	if(vtype==1){//新增课题
		//判断是否可以继续申请课题
		var currsubnum=$("#currsubnum").text();
		var maxsubnum=$("#maxsubnum").text();
		if(currsubnum==""){
			alert("正在获取课题列表，请稍后…");
			return;
		}
		if(+(currsubnum)>=+(maxsubnum)){//转换成数值后再进行比较
			alert("您目前已经申请了10课题，不允许继续申请。");
			return;
		}
		$("#errmsg").text("");
		resetsub();//重置课题表单中的信息
		$("#subid").val("自动编号");
		showDialog("#subjectedt",'申报新课题');
		//movewin(vid);
	}else{//修改课题时，充实窗口内容
		$.getJSON("../SubjectServlet?mode=get",{subid:vsubid},function call(data){ 
			if(data==null){
	       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	            return;
	       	}
			if(data.errmsg!=""){
        		alert(data.errmsg);
        		return;
        	}
        	var vsubject=data.result;
        	$("#subid").val(vsubject.subid);
        	$("#subname").val(vsubject.subname);
        	$("#subsort").val(vsubject.subsort);
        	$("#subkind").val(vsubject.subkind);
        	$("#subsource").val(vsubject.subsource);
        	$("#subtype").val(vsubject.subtype);
        	$("#subdirectionselect").val(vsubject.subdirection);
        	$("#subaddress").val(vsubject.isoutschool);
        	$("#oldargu").val(vsubject.oldargu);
        	$("#workcontent").val(vsubject.content);
        	$("#requirement").val(vsubject.requirement);
        	$("#refpapers").val(vsubject.refpapers);
        	var othertid=vsubject.othertutor.tid;
        	if(othertid==undefined){
        		$("#othertid").val("");
        		$("#othertname").val("");
        	}else{
        		$("#othertid").val(vsubject.othertutor.tid);
        		$("#othertname").val(vsubject.othertutor.tname);
        	}
        	//进程表
        	$("#subprog").val(vsubject.subprog);
        	var progressarray=eval(vsubject.progress);
        	$.each(progressarray,function(index,item){
        		$("#progresstime_"+item.inorder).val(item.startenddate);
        		$("#progressitem_"+item.inorder).val(item.content);
        	});
        	//适应专业
        	var specarray=eval(vsubject.listspec);
        	$("#speciality input[type='checkbox']").attr("checked",false);
        	$.each(specarray,function(index,item){
        		$("#speciality input[type='checkbox'][value='"+item+"']").attr("checked",true);
        	});
        	showDialog("#subjectedt",'修改课题');
        	
        	//隐藏提示信息
			$("#errmsg").text("");
			if(vtype==2){//只修改基本信息，不修改所属专业，则置专业为disabled
				$("#speciality input[type='checkbox']").attr("disabled","disabled");
				$("#modifybasesavebtn").show();
				$("#modifysavebtn").hide();
			}else{
				$("#speciality input[type='checkbox']").removeAttr("disabled");
				$("#modifysavebtn").show();
				$("#modifybasesavebtn").hide();
			}
        	//movewin(vid);
		});
	}
	//2015-12-1 
	$hipoldargu.text($("#oldargu").val().length);
	$hipworkcontent.text($("#workcontent").val().length);
	$hiprequirement.text($("#requirement").val().length);
	//////////////////
}

