$(document).ready(function(){
	$("#specsubmag ul").tabs("#specsubmagpanes> div");
	
	code_getcode_select("jxdw","#subbyspecform [name='tdept']");//所属教研室
	code_getcode_select("ktzht","#subbyspecform [name='substatus']");//课题状态
	code_getcode_select("xshzht","#stubyspecform [name='stustatus']");//学生选题状态
	var specid=$("#specid").val();
	code_getclass_select(specid,"#stubyspecform [name='classname']");//班级列表
	
	/*ajax提交绑定*/
	/*课题列表form绑定*/
	var options_subbyspecform={
		beforeSubmit:  function (){
			$loadingimg=$("#subbyspecform img[name='loadingimg']");
			$errmsg=$("#subbyspecform span[name='errmsg0']");
				
			$errmsg.text("正在查询，请稍候……");
			$loadingimg.show();
			return true;
		},
		dataType: 'json',
		success:      subbyspecResponse
	};
	$('#subbyspecform').ajaxForm(options_subbyspecform);  
	
	//审核通过标志切换
	$("#subsbyspec [name='auditflag']").change(function(){
		if($(this).val()=='0'){
			$("#subsbyspec [name='auditoptionspan']").show();
		}else{
			$("#subsbyspec [name='auditoption']").val("");
			$("#subsbyspec [name='auditoptionspan']").hide();
		}
	});
	
	//课题列表 "提交审核结果" 按钮事件绑定
	$("#subsbyspec [name='submitaudit']").click(function(){
		//检查审核标志
		var auditflag=$("#subsbyspec [name='auditflag']:checked").val();
		if(auditflag==undefined){
			alert("请选择审核标志！");
			$("#subsbyspec [name='auditflag']").focus();
			return;
		}
		var auditoption=$("#subsbyspec [name='auditoption']").val();
		
		if(auditflag=="0"&&auditoption==""){
			alert("请填写审核意见！");
			$("#subsbyspec [name='auditoption']").focus();
			return;
		}
		//筛选选中的课题
		var specid=$("#specid").val();
		var subspecarr=new Array();
		var index0=0;
		$("#tbsubsbyspec tr").has("input[type='checkbox']").each(function(index,item){
			var subid=$(this).children().eq(0).children().eq(0).val();
			var pickflag=$(this).find("input[type='checkbox']:checked").val();
			if(pickflag==undefined||pickflag=="") return;//若未选择审核标志，则跳出本次循环
			var subspec={specid:specid,subid:subid,auditflag:auditflag,auditoption:auditoption};
			subspecarr[index0]=subspec;
			index0=index0+1;
		});
		//判断是否有被选中的
		if(subspecarr.length==0){
			alert("请选择课题！");
			return;
		}else{
			if(confirm("选中课题数为"+subspecarr.length+"，您确定要提交审核结果？")==false) return;
		}
		//将javascript数组转换成json字符串
		var subspecjson=$.toJSON(subspecarr);
		//批量维护审核结果
		$.post("../SubjectServlet?mode=submitaudit",{subspecarr:subspecjson,auditflag:auditflag},function call(data){ 
			if(data==null){
	       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	            return;
	       	}
			if(data.errmsg!=""){
        		alert(data.errmsg);
        		return;
        	}
        	//刷新课题列表
        	$("#subbyspecform").submit();
        	//重置审核标志
        	$("#subsbyspec [name='auditflag']").attr("checked","");
        	$("#subsbyspec [name='auditoption']").val("");
			$("#subsbyspec [name='auditoptionspan']").hide();
        },"json");
	});
	//绑定 全选、撤销全选 按钮操作
	$("#subsbyspec input[value='全选']").click(function(){
		$tbody=$("#tbsubsbyspec");
		$tbody.find("input[type='checkbox']").attr("checked","checked");
	});
	$("#subsbyspec input[value='撤销全选']").click(function(){
		$tbody=$("#tbsubsbyspec");
		$tbody.find("input[type='checkbox']").attr("checked","");
	});
	/*学生列表form绑定*/
	var options_stubyspecform={
		beforeSubmit:  function (){
							$loadingimg=$("#stubyspecform img[name='loadingimg']");
							$errmsg=$("#stubyspecform span[name='errmsg0']");
								
							$errmsg.text("正在查询，请稍候……");
							$loadingimg.show();
							return true;
						},
		dataType: 'json',
		success:      stubyspecResponse
	};
	$('#stubyspecform').ajaxForm(options_stubyspecform);
});
/*课题列表form绑定，ajax函数*/
function subbyspecResponse(data){
	if(data==null){
   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
        return;
   	}
	$loadingimg=$("#subbyspecform img[name='loadingimg']");
	$errmsg=$("#subbyspecform span[name='errmsg0']");
	if(data.errmsg!=""){
       		$errmsg.text("查询失败："+data.errmsg);
       		$loadingimg.hide();
       		return;
    }
    $tbody=$("#tbsubsbyspec");
    if(data.result==""){
		$errmsg.text("查询结果数：0");
		$loadingimg.hide();
		$tbody.empty();
		return;
	}
    var subjectarr=eval(data.result);
    $tbody.empty();
   
    $.each(subjectarr,function(index,item){
    	var xh=index+1;
    	var statusarr=item.status.split("/");
    	var checkflag="";
    	if(statusarr[0].substring(0,8)=="已提交-等待审核"){
    		checkflag="<input type='checkbox' value='1'>";
    	}
    	var otherinfo="&nbsp;&nbsp;";
    	if(statusarr[0]=="已选"||statusarr[0]=="已初选"){
    		otherinfo=otherinfo+"["+statusarr[1]+"]";
    	}
    	if(statusarr[0]=="审核没通过"){
    		otherinfo=otherinfo+"<hr>["+statusarr[1]+"]";
    	}
    	var othertname=item.othertutor.tname;
    	if(othertname==undefined)othertname="";
    	//往年相似课题 2015.12.4
    	var simsubsarr=item.simSubsInHis;
    	var simsubs="";
    	var simsubscount=simsubsarr.length;
    	if(simsubscount>0){
    		var simsubsinfo="";
    		$.each(simsubsarr,function(index0,item0){
        		if(index0=="0") {
        			simsubs="["+item0.usedyear+"]"+item0.subname+"&nbsp;&nbsp;[相似度："+item0.similard+"]";
        		}else{
        			simsubs=simsubs+"<br/>"+"["+item0.usedyear+"]"+item0.subname+"&nbsp;&nbsp;[相似度："+item0.similard+"]";
        		}
        	});
    		simsubs="["+simsubscount+"]个相似&nbsp;&nbsp;<a href='javascript:void(0);' title='查看详情' onclick='viewsimsubshis(this,\""+simsubs+"\")'>详情</a>";
    	}
    	///////////////////////////////////
      	$tbody.append("<tr><td><input type='hidden' value='"+item.subid+"'/>"+xh+"</td><td>"+item.tutor.tdeptname+"</td><td>"+item.tutor.tname+"&nbsp;&nbsp;"+othertname
                     +"</td><td  width='400px'>("+item.subid+")&nbsp;<a href='SubjectView.jsp?subid="+item.subid+"' target='_blank'>"+item.subname+"</a></td>"
                     +"<td>"+simsubs+"</td>"
                     +"<td  width='200px'>"+statusarr[0]+otherinfo+"</td><td>&nbsp;"+checkflag+"</td></tr>");
    });
    
    $tbody.tbhighligt();//高亮显示当前行
    
    $errmsg.text("查询结果数："+subjectarr.length);
	$loadingimg.hide();
}

/*学生列表form绑定，ajax函数*/
function stubyspecResponse(data){
	if(data==null){
   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
        return;
   	}
	$loadingimg=$("#stubyspecform img[name='loadingimg']");
	$errmsg=$("#stubyspecform span[name='errmsg0']");
	
	if(data.errmsg!=""){
       		$errmsg.text("查询失败："+data.errmsg);
       		$loadingimg.hide();
       		return;
    }
    
    $tbody=$("#tbstusbyspec");
	if(data.result==""){
		$errmsg.text("查询结果数：0");
       	$loadingimg.hide();
		$tbody.empty();
		return;
	}
    var studentarr=eval(data.result);
    
    $tbody.empty();
    $.each(studentarr,function(index,item){
    	var xh=index+1;
    	var statusarr=item.status.split("/");
    	var operations="&nbsp;";
    	if(statusarr[0]=="需指派"||statusarr[0]=="落选-需指派"){
    		operations=operations+"<input type='button' value='指派课题' class='btn'/>";
    	}
    	var subname="";
    	var tutorname="";
    	if(statusarr[0]=="已选"){
    		subname=item.subject.subname;
    		tutorname=item.subject.tutor.tname;
    		var othertname=item.subject.othertutor.tname;
    		if(othertname!=undefined && othertname!=""){
    			tutorname=tutorname+"、"+othertname;
    		}
    	}
    	    	
      	$tbody.append("<tr><td>"+xh+"</td><td>"+item.classbean.classname+"</td><td>"+item.stuid+"</td>"
                     +"<td>"+item.sname+"</td><td><a href='SubjectView.jsp?subid="+statusarr[1]+"' target='_blank'>"+subname+"</a>&nbsp;</td>"
                     +"<td>"+tutorname+"&nbsp;</td>"
                     +"<td>"+statusarr[0]+"</td>"
                     +"<td>"+operations+"</td>");
    });
    
    $tbody.tbhighligt();//高亮显示当前行
    
    $errmsg.text("查询结果数："+studentarr.length);
    $loadingimg.hide();
    //绑定指派课题操作
    $("#tbstusbyspec input[value='指派课题']").click(function(){
    	//设置指派窗口中学号和姓名
    	var stuid=$(this).parent().siblings().eq(2).text();
    	var sname=$(this).parent().siblings().eq(3).text();
    	$("#assignsubject [name='stuid']").text(stuid);
    	$("#assignsubject [name='sname']").text(sname);
    	var vid=this;
    	//显示可选课题列表
    	$.getJSON("../SubjectServlet?mode=getsubsbyspec",{specid:$("#specid").val(),tdept:"",tname:"",substatus:"5"},function call(data){   
    		if(data==null){
	       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	            return;
	       	}
    		if(data.errmsg!=""){
	       		alert(data.errmsg);
	       		return;
	       	}
	       	if(data.result==""){
	       		alert("目前没有可选课题，无法进行指派课题操作！");
	       		return;
	       	}
	       	var subarr=eval(data.result);
	       	$tbody=$("#tbsubjects");
	       	$tbody.empty();
	       	$.each(subarr,function(index,item){
	       		var xh=index+1;
	       		var othertname=item.othertutor.tname;
	            if(othertname==undefined)othertname="";
	       		$tbody.append("<tr><td>"+xh+"</td><td>"+item.subname+"</td><td>"+item.tutor.tname
	       						+"&nbsp;&nbsp;"+othertname
	       		              	+"</td><td><input type='hidden' value='"+item.subid+"'><input type='button' class='btn' value='选中'/></td></tr>");
	       	});
	       	//绑定选中操作
			$("#tbsubjects input[value='选中']").click(function(){
				var stuid=$("#assignsubject [name='stuid']").text();
				var subid=$(this).prev().val();
				assignsubject(stuid,subid);
			});
	       	//设置窗口样式
	       	var offset=$(vid).offset();   
	        viewtop=offset.top;   
	        viewleft=+(offset.left)-600;  
	       	$subswin=$("#assignsubject");
	        $subswin.css("width","600px");
	        $subswin.css("height","auto");
	        $subswin.css("top",viewtop+"px");
	        $subswin.css("left",viewleft+"px");
        
	        //遮罩父窗口
			$.mask({ 
				'opacity': 0.6, 
				'bgcolor': '#F4FFE4', 
				'z': 10 
			}); 
	       	//显示窗口
	      	$subswin.show();
	      	
	      	$subswin.tbhighligt();
      	});
      
    });
}
//指派课题
function assignsubject(stuid,subid){
	$.getJSON("../SubjectServlet?mode=assignsubject",{stuid:stuid,subid:subid},function call(data){   
		if(data==null){
       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
            return;
       	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		return;
       	}
       	//关闭选择课题窗口
       	hideDialog("#assignsubject");
       	//刷新学生状态
       	$("#stubyspecform").submit();
    });
}
//显示往年相似课题 2015.12.4
function viewsimsubshis(vid,simsubs){
	$subhisswin=$("#viewsimsubshis");
	$subhisswin.find(".content").html(simsubs);
	//窗口定位
	var offset=$(vid).offset();   
    viewtop=offset.top+20;   
    viewleft=+(offset.left)-600;  
   	
   	$subhisswin.css("width","400px");
   	$subhisswin.css("height","100px");
   	$subhisswin.css("top",viewtop+"px");
   	$subhisswin.css("left","600px");

    //遮罩父窗口
	$.mask({ 
		'opacity': 0.6, 
		'bgcolor': '#F4FFE4', 
		'z': 10 
	}); 
   	//显示窗口
	$subhisswin.show();
}
