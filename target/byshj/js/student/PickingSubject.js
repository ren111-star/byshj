$(document).ready(function(){
	var stuid=$("#stuid").val();
	//刷新课题状态 操作绑定
	$("#pickingsubjectdiv input[value='刷新']").click(function(){
		$.getJSON("../StusubServlet?mode=getpickedsubsbystu",{stuid:stuid},function call(data){   
			$firsttbody=$("#pickingsubjectdiv [name='firstpicktb']");
			if(data==null){
	       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	       		$firsttbody.empty();
	            return;
	       	}
	       	if(data.errmsg!=""){
	       		alert(data.errmsg);
	       		$firsttbody.empty();
	       		return;
	       	}
	       	if(data.result==""){
	       		alert("学生"+stuid+"还没有选题！");
	       		$firsttbody.empty();
	       		return;
	       	}
	       	pickedsubarr=eval(data.result);
	       	$firsttbody.empty();
	       	var assignstr="";
	       	var assignflag="";
	       	var unpickedcount=0;//落选课题数
	       	$.each(pickedsubarr,function(index,item){
       			var subid=item.subid;
       			if(subid!="0"){
       				var othertname=item.subject.othertutor.tname;
	       			if(othertname==undefined)othertname="";
	       			var pickflag=item.pickflag;
	       			var status="";
	       			if(pickflag=="0"){
	       				status="落选";
	       				unpickedcount=unpickedcount+1;
	       			}else if(pickflag==""||pickflag==null){
	       				status="等待教师确认";
	       			}
	       			$firsttbody.append("<tr><td>"+item.pickorder+"</td>"+
       			   				    "<td>("+item.subject.subid+")"+item.subject.subname+"</td>"+
       			   				    "<td>"+item.subject.tutor.tname+"&nbsp;&nbsp;"+othertname+"</td>"+
				                    "<td>"+status+"</td>"+
				                   "</tr>");
       			}else{
       				assignflag="1";
       			}
	       	});
	        var pickedcount=pickedsubarr.length;
	       	if(assignflag=="1"){
	       		pickedcount=pickedcount-1;
	       		if(pickedcount==0) {
	       			assignstr="请等待专业负责人指派课题！";
	       			$("#pickingsubjectdiv input[value='重新选择课题']").hide();
	       		}else{
	       			if(pickedcount==unpickedcount){
		       			assignstr="你选择的课题均未被选中，请等待专业负责人指派课题！";
		       			$("#pickingsubjectdiv input[value='重新选择课题']").hide();
		       		}else{
		       			assignstr="若你选择的课题均未被选中，则由专业负责人指派课题！";
		       			$("#pickingsubjectdiv input[value='重新选择课题']").show();
		       		}
		       	}
	       	}else{
	       		if(pickedcount==unpickedcount){//均落选，且没有选择指派，则提示重新选题
		       		assignstr="你选择的课题均未被选中，请";
		       		$("#pickingsubjectdiv input[value='重新选择课题']").show();
				}
	       	}
	       	$firsttbody.next().find("span").text(assignstr);
	       	//重新选择课题 操作绑定
			$("#pickingsubjectdiv input[value='重新选择课题']").click(function(){
				$.getJSON("../StusubServlet?mode=delsubsbystu",{stuid:stuid},function call(data){
					if(data==null){
			       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
			            return;
			       	}
					if(data.errmsg!=""){
			       		alert(data.errmsg);
			       		return;
			       	}
			       	//状态转变为“未选”，刷新当前页面
			       	window.location.reload();
				});
			});
        });
	});
	
	//页面加载时，触发刷新
	$("#pickingsubjectdiv input[value='刷新']").click();
});
