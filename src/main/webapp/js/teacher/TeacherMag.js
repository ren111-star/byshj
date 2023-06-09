$(document).ready(function(){
	//导航tab
	$("#navigation ul").tabs("#content> div");
	//
	$("input[type='button'],input[type='reset'],input[type='submit']").addClass("btn");
	//设置最多申报课题数
	$("#maxsubnum").text("10");
	//教师课题列表
	var tutorid=$("#tutorid").val();
	//绑定刷新按钮
	$("#mysubrefresh").click(function(){
		$("#highlighttr").val("");
		getsubBytid(tutorid);
	});
	$("#mysubrefresh").click();
	//getsubBytid(tutorid);
	//ajax异常处理
	//初始化课题编辑界面
	$.include("../js/teacher/SubjectEdt.js");
	//初始化盲审课题界面
	$.include("../js/teacher/ReviewSubject.js");
	//初始化盲审论文界面
	$.include("../js/teacher/ReviewPaper.js");
	//初始化个人信息界面
	$.include("../js/teacher/TeaPersonalInfo.js");
	//初始化专业管理界面
	$.include("../js/teacher/SpecSubMag.js");
	//初始化周总结管理界面
	$.include("../js/teacher/WeekSumMag.js");
	$.include("../js/teacher/DesignDocMag.js");
	$.include("../js/teacher/subjectLib/SubjectLibMag.js");
	//异常处理
	$.ajaxSetup({
        error:function(XMLHttpRequest, textStatus, errorThrown){
            alert("请求异常！0["+textStatus+":"+errorThrown+"]");
            return false;
        }
    });
});

//根据教师编号获得该教师的课题列表
function getsubBytid(tutorid){
	$.getJSON("../SubjectServlet?mode=gets",{tutorid:tutorid},function call(data){   
       	if(data==null){
       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
       		$("#loading").hide();
            return;
       	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		$("#loading").hide();
       		return;
       	}
       	var subarray=eval(data.result);
       	
       	$("#currsubnum").text(subarray.length);//当前课题数
       	$("#tbmysubject").empty();
       	$.each(subarray,function(index,item){
       		var statusarray=item.status.split(",");
       		var status=statusarray[0];//状态
       		var stuid="";//学号
       		if(statusarray.length>1) {//已选
       			stuid=statusarray[1];
       			status="(<a href='#' name='a_showstuinfo' title='点击查看联系方式'>"+stuid+"</a>)&nbsp;"+status+"<div name='div_stuinfo'></div>";
       		}
       		var isoutschool=item.isoutschool;
       		var straddress="";
       		if(isoutschool==0){
       			straddress="校内";
       		}else if(isoutschool==1){
       			straddress="<font color='blue'>校外</font>";
       		};
       		var operations=item.operations;
       		var stroperations="<input type='hidden' value='"+item.subid+"'><input type='hidden' value='"+stuid+"'>";
       		var opnum=operations.length;
       		$.each(operations,function(index0,item0){
       			stroperations=stroperations+"<input type='button' value='"+item0+"' class='btn'/>&nbsp;"
       			if(item0.length>5&&opnum>1&&index0!=(opnum-1)) stroperations=stroperations+"<br>";
       		});
       		var reviewopinion=item.reviewopinion;
       		if(reviewopinion==undefined)reviewopinion="";
       		var xh=index+1;
       		//计算历史课题数
       		var historysubcount=item.simSubsInHis.length;
       		var historysubcountstr="";
       		if(historysubcount>0){
       			historysubcountstr="<span class='flag'>[历史课题]</span>&nbsp;";
       		}
       		var othertname=item.othertutor.tname;
    	    if(othertname==undefined)othertname="";
       		$("#tbmysubject").append("<tr><td>"+xh+"</td>" +
       				"<td>"+historysubcountstr+"<a href='SubjectView.jsp?subid="+item.subid+"' target='_blank'>"+item.subname+"</a></td>" +
       				"<td>"+item.tutor.tname+"&nbsp;&nbsp;"+othertname+"</td>" +
       				"<td>"+straddress+"</td>" +
       				"<td>"+item.specname+"</td>" +
       				"<td>"+status+"</td>" +
       				"<td>"+stroperations+"&nbsp;</td>"
       				+"</tr>");
       		//标注需要高亮显示的行
       		var subid0=$("#highlighttr").val();
       		if(subid0!=""){
       			if(subid0==item.subid) {
       			    //alert("subid0:"+subid0);
       			    $tr=$("#tbmysubject").children().last();
       				$("#tbmysubject").children().last().addClass("highlighttr");
       			}
       		}
       	});
       	$("#tbmysubject").tbhighligt();
       	//绑定查看已选课题学生信息
       	$("#tbmysubject a[name='a_showstuinfo']").click(function(){
			var stuid=$(this).text();
			selstudentinfo(this,stuid);
		});
       	//绑定课题操作
		$("#tbmysubject input[value='删除']").click(function(){
			var subid=$(this).siblings().eq(0).val();
			var subname=$(this).parent().siblings().eq(1).children().text();
			delsubject(subid,subname);
		});
		$("#tbmysubject input[value='提交']").click(function(){
			var subid=$(this).siblings().eq(0).val();
			submitsubject(subid);
		});
		/**2015-11-26增加 wxh*/
		//绑定 转移到暂存库 操作
		$("#tbmysubject input[value='转移到暂存库']").click(function(){
			var subid=$(this).siblings().eq(0).val();
			var subname=$(this).parent().siblings().eq(1).children().text();
			transfersubtotmp(subid,subname);
		});
		//绑定 复制到暂存库 操作
		$("#tbmysubject input[value='复制到暂存库']").click(function(){
			var subid=$(this).siblings().eq(0).val();
			var subname=$(this).parent().siblings().eq(1).children().text();
			copysubtotmp(subid,subname);
		});
		/***/
		$("#tbmysubject input[value='修改']").click(function(){
			var subid=$(this).siblings().eq(0).val();
			subject_showwin(subid,0);
		});
		$("#tbmysubject input[value='查看审核意见']").click(function(){
			var subid=$(this).siblings().eq(0).val();
			viewoptions(this,subid);
		});
		$("#tbmysubject input[value='选择学生']").click(function(){
			var subid=$(this).siblings().eq(0).val();
			var subname=$(this).parent().siblings().eq(1).children().text();
			$("#subname0").text(subname);
			showpickedstudent(this,subid);
		});
		$("#tbmysubject input[value='修改任务书']").click(function(){
			var subid=$(this).siblings().eq(0).val();
			subject_showwin(subid,2);//1增加课题0修改课题2修改课题基本信息
		});
		$("#tbmysubject input[value='查看设计情况']").click(function(){
			$.ajaxSetup({async: false});
			var stuid=$(this).siblings().eq(1).val();
			//DesignDocMag.js(viewdesign)
			viewdesign($(this),stuid);
			$.ajaxSetup({async: true});
		});
		//隐藏正在加载提示图片
		$("#loading").hide();
      });
}
//根据学号查看学生信息
function selstudentinfo(vid,stuid){
	$divstuinfo=$(vid).siblings();
	if($divstuinfo.html()==""){//没有内容，需从数据库中取
		$("#loading").show();
		$.getJSON("../StudentServlet?mode=get",{stuid:stuid},function call(data){   
			if(data==null){
	       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	       		$("#loading").hide();
	            return;
	       	}
			if(data.errmsg!=""){
	       		alert(data.errmsg);
	       		$("#loading").hide();
	       		return;
	       	}
	       	//在学号连接下增加div显示学生班级、姓名、邮箱、电话
	       var stu=eval(data.result);
	       $divstuinfo.html(stu.classname+"&nbsp;&nbsp;"+stu.sname
	       	                      +"<br>Email:"+stu.email
	       	                      +"<br>Tel:"+stu.telphone);
	       //隐藏正在加载提示图片
	       $("#loading").hide();
		});
	}else{
		$divstuinfo.empty();//清空
	}
}
//根据课题号subid，删除课题
function delsubject(subid,subname){
	if(confirm("确定要删除课题<"+subname+">？")==false) return;
	$("#loading").show();
	$.getJSON("../SubjectServlet?mode=del",{subid:subid},function call(data){   
		if(data==null){
       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
       		$("#loading").hide();
            return;
       	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		$("#loading").hide();
       		return;
       	}
       	//刷新课题列表
       	var tutorid=$("#tutorid").val();
		getsubBytid(tutorid);
	});
}
//提交课题
function submitsubject(subid){
	$("#loading").show();
	$.getJSON("../SubjectServlet?mode=submit",{subid:subid},function call(data){   
		if(data==null){
       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
       		$("#loading").hide();
            return;
       	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		$("#loading").hide();
       		return;
       	}
       	//刷新课题列表
       	var tutorid=$("#tutorid").val();
		getsubBytid(tutorid);
	});
}
//查看审核意见
function viewoptions(vid,subid){
	$("#loading").show();
	$.getJSON("../SubjectServlet?mode=getOption",{subid:subid},function call(data){   
		if(data==null){
       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
       		$("#loading").hide();
            return;
       	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		$("#loading").hide();
       		return;
       	}
       	//显示审核意见
       	$optionwin=$("#viewoptions");
       	$optioncontent=$("#viewoptions .content");
       	$optioncontent.empty();
       	var optionarr=eval(data.result);
       	$.each(optionarr,function(index,item){
       		var oparr=item.split("|");
       		var specname=oparr[0];//专业名
       		var option=oparr[1];//意见
       		if($optioncontent.text()!="") $optioncontent.append("<br><br>");
       		$optioncontent.append("<span style='font-weight:bold,font-size:13px'>"+specname+"</span><hr><span>"+option+"</span>");
       	});
       	//设置窗口样式
       	var offset=$(vid).offset();   
        viewtop=offset.top+28;   
        viewleft=+(offset.left)-200;  
        $optionwin.css("width","200px");
        $optionwin.css("height","100px");
        $optionwin.css("top",viewtop+"px");
        $optionwin.css("left",viewleft+"px");
      	//显示窗口
      	$optionwin.show();
      	$("#loading").hide();
	});
}
//被选择学生列表
function showpickedstudent(vid,subid){
	$("#loading").show();
	$.getJSON("../SubjectServlet?mode=getstus",{subid:subid},function call(data){   
		if(data==null){
       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
       		$("#loading").hide();
            return;
       	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		$("#loading").hide();
       		return;
       	}
       	//显示学生列表
       	var stuarr=eval(data.result);
       	$tbody=$("#tbstudents");
       	$tbody.empty();
       	$.each(stuarr,function(index,item){
       		var xh=index+1;
       		$tbody.append("<tr><td>"+xh+"</td><td>"+item.classbean.classname+"</td><td>"+item.stuid
       		              +"</td><td>"+item.sname+"</td><td><input type='button' class='btn' value='选中'/>&nbsp;&nbsp;<input type='button' class='btn' value='弃选'/></td></tr>");
       	});
       	//绑定选中操作
		$("#tbstudents input[value='选中']").click(function(){
			//var subid=$("#subid0").text();
			var stuid=$(this).parent().siblings().eq(2).text();
			pickstudent(subid,stuid);
		});
	    //绑定弃选 操作
		$("#tbstudents input[value='弃选']").click(function(){
			//var subid=$("#subid0").text();
			var stuid=$(this).parent().siblings().eq(2).text();
			unpickstudent($(this).parent().parent(),subid,stuid);
		});
       	//设置窗口样式
       	var offset=$(vid).offset();   
        viewtop=offset.top+28;   
        viewleft=+(offset.left)-400;  
       	$stuswin=$("#pickstudent");
        $stuswin.css("width","400px");
        $stuswin.css("height","auto");
        $stuswin.css("top",viewtop+"px");
        $stuswin.css("left",viewleft+"px");
       	//显示窗口
      	$stuswin.show();
      	$("#loading").hide();
      	$stuswin.tbhighligt();
      });
}
//选择学生
function pickstudent(subid,stuid){
	$("#loading").show();
	$.getJSON("../SubjectServlet?mode=pickstudent",{subid:subid,stuid:stuid},function call(data){   
		if(data==null){
       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
       		$("#loading").hide();
            return;
       	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		$("#loading").hide();
       		return;
       	}
       	$("#pickstudent").hide();
       	//刷新课题列表
       	var tutorid=$("#tutorid").val();
		getsubBytid(tutorid);
     });
}
//弃选学生
function unpickstudent(vrow,subid,stuid){
	$("#loading").show();
	$.getJSON("../SubjectServlet?mode=unpickstudent",{subid:subid,stuid:stuid},function call(data){   
		if(data==null){
       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
       		$("#loading").hide();
            return;
       	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		$("#loading").hide();
       		return;
       	}
       	//删除 弃选 行
        vrow.remove(); 
        if(vrow.parent().length==0) $("#pickstudent").hide();
        //刷新课题列表
       	var tutorid=$("#tutorid").val();
		getsubBytid(tutorid);   	
     });
}
//选题后，修改任务书
function showmodifybaseinfowin(vid,subid){
		
}

/*打印全部任务书*/
function printtaskbooks(tid){
	var $loadingimg=$("#printtaskbooks").next();
	var $errmsg=$("#printtaskbooks").next().next();
	
	$errmsg.text("正在导出……");
	$loadingimg.show();
	$.getJSON("../ExcelServlet?mode=exporttaskbooksBytid",{tid:tid},function call(data){   
		if(data==null){
       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
       		$("#loading").hide();
            return;
       	}
		if(data.errmsg!=""){
       		$errmsg.text("导出失败："+data.errmsg);
			$loadingimg.hide();
       		return;
       	}
       	$errmsg.html("导出成功！<a href='../FileDownloadServlet0?tempfilename="+tid+"taskbookoutput.xls' title='点击打开/保存文件'>课题任务书</a>");
	    $loadingimg.hide();
     });
}
/*转移课题到暂存库 2015-11-26 wxh*/
function transfersubtotmp(subid,subname){
	if(confirm("确定要转移课题<"+subname+">？")==false) return;
	$("#loading").show();
	$.getJSON("../SubjectLibServlet?mode=transferfromcurrent",{subid:subid},function call(data){   
		if(data==null){
       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
       		$("#loading").hide();
            return;
       	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		$("#loading").hide();
       		return;
       	}
		alert("转移成功！");
       	//刷新课题列表
       	var tutorid=$("#tutorid").val();
       	$('#mysubrefresh').trigger('click');
		$('#subtemplistform').submit();
	});
}
/*复制课题到暂存库 2015-11-26 wxh*/
function copysubtotmp(subid,subname){
	if(confirm("确定要复制课题<"+subname+">？")==false) return;
	$("#loading").show();
	$.getJSON("../SubjectLibServlet?mode=copyfromcurrent",{subid:subid},function call(data){   
		if(data==null){
       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
       		$("#loading").hide();
            return;
       	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		$("#loading").hide();
       		return;
       	}
		alert("复制成功！");
       	//刷新课题列表
       	var tutorid=$("#tutorid").val();
       	$('#mysubrefresh').trigger('click');
		$('#subtemplistform').submit();
	});
}
