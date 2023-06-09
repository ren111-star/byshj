$(document).ready(function(){
	$.include("../js/manager/subjectmag/DesignProcessMag.js");
	$.include("../js/manager/subjectmag/DesignDocMag.js");
	$.include("../js/manager/subjectmag/ChangeSubject.js");
	
	$("#subjectmagdiv ul").tabs("#subjectmagpanes> div");
	
	code_getspeciality_select($("#subjectform select[name='specid']"));//专业列表
	code_getcode_select("jxdw","#subjectform [name='tdept']");//所属教研室
	code_getcode_select("ktzht","#subjectform [name='substatus']","4");//课题状态
	
	$specsel=$("#studentform select[name='specid']");
	code_getspeciality_select($specsel);//专业列表
	$specsel.change(function(){
		var specid=$(this).val();
		if(specid=="") return;
		$classsel=$("#studentform select[name='classname']");
		$classsel.empty();
		code_getclass_select(specid,$classsel);//班级列表
	});
	code_getcode_select("xshzht","#studentform [name='stustatus']");//学生选题状态
	
	
	/*ajax提交绑定*/
	/*课题列表form绑定*/
	var options_sub={
		beforeSubmit:  function(){
			$spec=$("#subjectform select[name='specid']");
			if($spec.val()==""){
				alert("专业不能为空，请选择！");
				$spec.focus();
				return false;
			}
			$loadingimg=$("#subjectform img[name='loadingimg']");
			$errmsg=$("#subjectform span[name='errmsg0']");
			$loadingimg.show();
			$errmsg.text("正在查询，请稍候……");
		},
		dataType: 'json',
		success:      subResponse
	};
	$('#subjectform').ajaxForm(options_sub);  
	
	//课题列表 "发布课题" 按钮事件绑定
	$("#subjectsdiv input[name='releasesub']").click(function(){
		//筛选选中的课题
		var specid=$("#subjectform select[name='specid']").val();
		var subspecarr=new Array();
		$tbody=$("#subjectsdiv tbody[name='tbsubjects']");
		$tbody.find("tr").has("input[type='checkbox']").each(function(index,item){
			var subid=$(this).children().eq(0).children().eq(0).val();
			var pickflag=$(this).find("input[type='checkbox']:checked").val();
			if(pickflag==undefined||pickflag=="") return;//若未选择，则跳出本次循环
			
			var subspec={specid:specid,subid:subid};
			subspecarr[index]=subspec;
		});
		//判断是否有被选中的
		if(subspecarr.length==0){
			alert("请选择待发布的课题！");
			return;
		}else{
			if(confirm("待发布课题数为"+subspecarr.length+"，您确定要发布吗？")==false) return;
		}
		//将javascript数组转换成json字符串
		var subspecjson=$.toJSON(subspecarr);
		//状态提示
		$loadingimg=$(this).nextAll().eq(0);
		$errmsg=$(this).nextAll().eq(1);
		$loadingimg.show();
		$errmsg.text("正在发布课题，请稍候……");
		//批量发布课题
		$.post("../SubjectServlet?mode=releasesubject",{subspecarr:subspecjson,releaseflag:"1"},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		   		$loadingimg.hide();
		        return;
		   	}
			if(data.errmsg!=""){
        		$errmsg.text("发布失败："+data.errmsg);
        		$loadingimg.hide();
        		return;
        	}
        	$loadingimg.hide();
        	$errmsg.text("发布成功！");
        	//刷新课题列表
        	$("#subjectform").submit();
        	
        },"json");
	});
	
	//绑定 全选、撤销全选 按钮操作
	$("#subjectsdiv input[value='全选']").click(function(){
		$tbody=$("#subjectsdiv tbody[name='tbsubjects']");
		$tbody.find("input[type='checkbox']").attr("checked","checked");
	});
	$("#subjectsdiv input[value='撤销全选']").click(function(){
		$tbody=$("#subjectsdiv tbody[name='tbsubjects']");
		$tbody.find("input[type='checkbox']").attr("checked","");
	});
	
	/*学生列表form绑定*/
	var options_stu={
		beforeSubmit:  function(){
			$spec=$("#studentform select[name='specid']");
			if($spec.val()==""){
				alert("专业不能为空，请选择！");
				$spec.focus();
				return false;
			}
			$loadingimg=$("#studentform img[name='loadingimg']");
			$errmsg=$("#studentform span[name='errmsg0']");
			$loadingimg.show();
			$errmsg.text("正在查询，请稍候……");
		},
		dataType: 'json',
		success:      stuResponse
	};
	$('#studentform').ajaxForm(options_stu);
	//绑定 导出课题明细表 按钮操作
	$("#studentform input[name='exportstusublist']").click(function(){
		$loadingimg=$("#studentform img[name='loadingimg']");
		$errmsg=$("#studentform span[name='errmsg0']");
		
		$errmsg.text("正在导出课题明细表，请稍候……");
		$loadingimg.show();
		var specid=$("#studentform [name='specid']").val();
		var classname=$("#studentform [name='classname']").val();
		$.getJSON("../ExcelServlet?mode=exportstusublist",{specid:specid,classname:classname},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		   		$loadingimg.hide();
		        return;
		   	}
			if(data.errmsg!=""){
        		$errmsg.text("导出失败："+data.errmsg);
        		$loadingimg.hide();
        		return;
        	}
        	$errmsg.html("导出成功！<a href='../FileDownloadServlet0?tempfilename=output.xls' title='点击打开/保存文件'>课题明细表</a>");
        	$loadingimg.hide();
        });
	});
});

/*课题列表form绑定，ajax函数*/
function subResponse(data){
	$tbody=$("#subjectsdiv tbody[name='tbsubjects']");
	$loadingimg=$("#subjectform img[name='loadingimg']");
	$errmsg=$("#subjectform span[name='errmsg0']");
	if(data==null){
   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
   		$loadingimg.hide();
   		$tbody.empty();
        return;
   	}
	if(data.errmsg!=""){
       		$errmsg.text("查询失败："+data.errmsg);
       		$loadingimg.hide();
       		$tbody.empty();
       		return;
    }
    if(data.result==""){
		$loadingimg.hide();
		$errmsg.text("查询结果数：0");
		$tbody.empty();
		return;
	}
    var subjectarr=eval(data.result);
    $tbody.empty();
    
    $.each(subjectarr,function(index,item){
    	var xh=index+1;
    	var statusarr=item.status.split("/");
    	var checkflag="";
    	if(statusarr[0]=="审核通过-等待发布"){
    		checkflag="<input type='checkbox' value='1'>";
    	}
    	var otherinfo="&nbsp;&nbsp;";
    	if(statusarr[0]=="已选"||statusarr[0]=="已初选"){
    		otherinfo=otherinfo+"["+statusarr[1]+"]";
    	}
    	if(statusarr[0]=="审核没通过-需重新提交"){
    		otherinfo=otherinfo+"<hr>["+statusarr[1]+"]";
    	}
    	var othertname=item.othertutor.tname;
    	if(othertname==undefined)othertname="";
    	//显示历史课题数
    	var historysubcount=item.simSubsInHis.length;
   		var historysubcountstr="";
   		if(historysubcount>0){
   			historysubcountstr="<span class='flag'>[历史课题]</span>&nbsp;";
   		}
   		
      	$tbody.append("<tr><td><input type='hidden' value='"+item.subid+"'/>"+xh+"</td><td>"+item.tutor.tdeptname+"</td><td>"+item.tutor.tname+"&nbsp;&nbsp;"+othertname
                     +"</td><td  width='400px'>"+historysubcountstr+"<a href='../teacher/SubjectView.jsp?subid="+
                     item.subid+"' target='_blank'>"+item.subname+"</a></td><td  width='200px'>"+
                     statusarr[0]+otherinfo+"</td><td>&nbsp;"+checkflag+
                     "</td><td><img src='../images/i.p.delete.gif' title='删除' onclick=\"delsubject(this,'"+item.subid+"','"+item.subname+"')\"/>"+
                     "<img src=\"../images/loading.gif\"style=\"display:none\">&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\"restoresubjectstatus(this,'"+item.subid+"','"+item.subname+"')\" title='将课题恢复到审核没通过的状态' >撤销</a><img src=\"../images/loading.gif\"style=\"display:none\"></td></tr>");
    
    });
    
    $tbody.tbhighligt();//高亮显示当前行
    
    $loadingimg.hide();
	$errmsg.text("查询结果数："+subjectarr.length);
}

/*学生列表form绑定，ajax函数*/
function stuResponse(data){
	$tbody=$("#studentsdiv tbody[name='tbstudents']");
	$loadingimg=$("#studentform img[name='loadingimg']");
	$errmsg=$("#studentform span[name='errmsg0']");
	if(data==null){
   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
   		$loadingimg.hide();
   		$tbody.empty();
        return;
   	}
	if(data.errmsg!=""){
       		$errmsg.text("查询失败："+data.errmsg);
       		$loadingimg.hide();
       		$tbody.empty();
       		return;
    }
    if(data.result==""){
		$loadingimg.hide();
		$errmsg.text("查询结果数：0");
		$tbody.empty();
		return;
	}
    var studentarr=eval(data.result);
    $tbody.empty();
    $.each(studentarr,function(index,item){
    	var xh=index+1;
    	var statusarr=item.status.split("/");
    	var otherinfo="&nbsp;&nbsp;";
    	var subname="&nbsp;";
    	var subtypename="&nbsp;";
    	var subkindname="&nbsp;";
    	var subsourcename="&nbsp;";
    	var tutorname="&nbsp;";
    	var tpostdegree="&nbsp;";
    	if(statusarr[0]=="已选"){
    		subname=item.subject.subname;
    		subtypename=item.subject.subtypename;
    		subkindname=item.subject.subkindname;
    		subsourcename=item.subject.subsourcename;
    		tutorname=item.subject.tutor.tname;
    		tpostdegree=((item.subject.tutor.tpostname==undefined)? "": item.subject.tutor.tpostname)+"/"
    		            +((item.subject.tutor.tdegreename==undefined)? "": item.subject.tutor.tdegreename);
    		var othertname=item.subject.othertutor.tname;
    		if(othertname!=undefined && othertname!=""){
    			tutorname=tutorname+"、"+othertname;
    			tpostdegree=tpostdegree+"、"
    			            +((item.subject.othertutor.tpostname==undefined)? "": item.subject.othertutor.tpostname)+"/"
    		            +((item.subject.othertutor.tdegreename==undefined)? "": item.subject.othertutor.tdegreename);
    		}
    	}
    	    	
      	$tbody.append("<tr><td>"+xh+"</td><td>"+item.stuid+"</td><td>"+item.sname+"</td>"
                     +"<td>"+item.classbean.speciality.specname+"</td>"
                     +"<td>"+item.classname+"</td>"+
                     "<td>"+subname+"</td>"+
                     "<td>"+subtypename+"</td>"+
                     "<td>"+subkindname+"</td>"+
                     "<td>"+subsourcename+"</td>"+
                     "<td>"+tutorname+"</td>"+
                     "<td>"+tpostdegree+"</td>"+
                     "<td>"+statusarr[0]+"</td>");
    });
    
    $tbody.tbhighligt();//高亮显示当前行
    
    $loadingimg.hide();
	$errmsg.text("查询结果数："+studentarr.length);
}
function delsubject(vid,subid,subname){
	$promptmsg=$(vid).next();
	if(confirm("确定要删除课题<"+subname+">？这将删除该课题在数据库中的所有信息，包括基本信息和选题信息")==false) return;
	$promptmsg.show();
	$.getJSON("../SubjectServlet?mode=delall",{subid:subid},function call(data){   
		if(data==null){
	   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	   		$promptmsg.hide();
	        return;
	   	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		$promptmsg.hide();
       		return;
       	}
       	$promptmsg.hide();
       	//刷新课题列表
       	$('#subjectform').submit();
	});
}
/***/
function restoresubjectstatus(vid,subid,subname){
	$promptmsg=$(vid).next();
	if(confirm("确定要将课题<"+subname+">恢复到[审核没通过]状态？")==false) return;
	$promptmsg.show();
	$.getJSON("../SubjectServlet?mode=restoreSubjectStatus",{subid:subid},function call(data){   
		if(data==null){
	   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	   		$promptmsg.hide();
	        return;
	   	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		$promptmsg.hide();
       		return;
       	}
       	$promptmsg.hide();
       	//刷新课题列表
       	$('#subjectform').submit();
	});
}
