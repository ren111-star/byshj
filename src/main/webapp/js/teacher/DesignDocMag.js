//查看设计情况
function viewdesign($vid,stuid){
	$("#loading").show();
	//设置窗口样式
   	var offset=$vid.offset();   
    viewtop=offset.top+28;   
    viewleft=+(offset.left)-250;  
    $designwin=$("#viewdesign");
    $designwin.css("width","600px");
    $designwin.css("height","130px");
    $designwin.css("top",viewtop+"px");
    $designwin.css("left","400px");
  	//显示窗口
  	$designwin.show();
  //遮罩父窗口
	$.mask({ 
		'opacity': 0.6, 
		'bgcolor': '#F4FFE4', 
		'z': 10 
	});
  	$("#loading").hide();
  
  	getdocstatus(stuid,"paperblind");
  	getdocstatus(stuid,"paper");
  	getdocstatus(stuid,"translation");
  	getdocstatus(stuid,"sourcecode");
}
/**查询并更新文档状态*/
function getdocstatus(stuid,doctype){
	var $designdoctb=$("#tbdesign");
	var $thistr=$designdoctb.children("tr[name='"+doctype+"']");
	
	var $docpath=$thistr.children("td:eq(1)").find("a");//href,title
	var $status=$thistr.children("td:eq(2)");//td[html]
	var $operation=$thistr.children("td:eq(3)");//td[html]
	$.getJSON("../SubSubmitServlet?mode=getUploadstatus",{stuid:stuid,doctype:doctype},function call(data){ 
		if(data==null){
	   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	        return;
	   	}
		if(data.errmsg!=""){
    		alert(data.errmsg);
    		return;
    	}
		
		var resultarr= data.result.split("|");
		var status="";
		if(resultarr[0]=="0"){//文档status.未上传
			$status.html("未上传");
			$operation.html("");
			$docpath.attr("href","#");
			$docpath.attr("title","");
		}else if(resultarr[0]=="1"){//已上传，允许重新上传
			if(doctype=="paperblind"){
				$status.html("已上传需提交盲审");
				$operation.html("<input type='button' class='btn' value='提交盲审' onclick=\"submitdoc('"+stuid+"','"+doctype+"','2')\"/>");
			}else{
				$status.html("已上传需归档");
				$operation.html("<input type='button' class='btn' value='提交归档' onclick=\"submitdoc('"+stuid+"','"+doctype+"','2')\"/>");
			}
			
			$docpath.attr("href","../FileDownloadServlet0?filenamewithallpath="+resultarr[1]);
			$docpath.attr("title","下载文件");
		}else if(resultarr[0]=="2"){//等待盲审或已归档，不允许重新上传
			if(doctype=="paperblind"){
				$status.html("等待盲审");
				$operation.html("<input type='button' class='btn' value='撤销盲审' onclick=\"submitdoc('"+stuid+"','"+doctype+"','1')\"/>");
			}else{
				$status.html("已归档");
				$operation.html("<input type='button' class='btn' value='撤销归档' onclick=\"submitdoc('"+stuid+"','"+doctype+"','1')\"/>");
			}
			
			$docpath.attr("href","../FileDownloadServlet0?filenamewithallpath="+resultarr[1]);
			$docpath.attr("title","下载文件");
		}else if(resultarr[0]=="3"){//已盲审，可查看盲审结果（只paperblind可用）
			$status.text("已盲审");
			//增加“查看盲审意见”操作按钮
			$operation.html("<a href='../student/PaperBlindOptionShow.jsp?stuid="+stuid+"' target='_blank'>查看盲审意见"+"</a>");
			
			$docpath.attr("href","../FileDownloadServlet0?filenamewithallpath="+resultarr[1]);
			$docpath.attr("title","下载文件");
		}
	});
}
/**提交盲审或提交归档*/
function submitdoc(stuid,doctype,status){
	if(status=='2'){
		if(confirm("在提交前请检查上传文档是否符合要求，提交后将无法再修改，确定要提交？")==false) return;
	}else{
		if(confirm("确定要撤销归档？")==false) return;
	}
	
	$.getJSON("../SubSubmitServlet?mode=submitDocForTea",{stuid:stuid,doctype:doctype,status:status},function call(data){ 
		if(data==null){
	   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	        return;
	   	}
		if(data.errmsg!=""){
    		alert(data.errmsg);
    		return;
    	}
		if(doctype=="paperblind"){
			if(status=='2'){
				alert("已成功提交盲审！");
			}else{
				alert("已成功撤销盲审！");
			}
		}else{
			if(status=='2'){
				alert("已成功提交归档！");
			}else{
				alert("已成功撤销归档！");
			}
		}
		getdocstatus(stuid,doctype);
	});
}

