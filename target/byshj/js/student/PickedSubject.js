$(document).ready(function(){
	if($("#status").val()=='已选'){
		//设置上传表单的ajax提交
		bindingajaxforupload("paperblindform","pdf");
		bindingajaxforupload("paperform","pdf");
		bindingajaxforupload("translationform","pdf");
		bindingajaxforupload("sourcecodeform","rar");
		//刷新各文档状态
		getdocstatus($("#submitdoctb form[name='paperblindform']"));
		getdocstatus($("#submitdoctb form[name='paperform']"));
		getdocstatus($("#submitdoctb form[name='translationform']"));
		getdocstatus($("#submitdoctb form[name='sourcecodeform']"));
	}
});
/**ajax绑定*/
function bindingajaxforupload(formname,exttype){
	$("#submitdoctb form[name='"+formname+"']").ajaxForm({
		dataType:"json",
		beforeSubmit:function(){return beforesubmit($("#submitdoctb form[name='"+formname+"']"),exttype);},
		success:function(data){successreponse($("#submitdoctb form[name='"+formname+"']"),data);},
		resetForm:true
	});
}
/**上传前检查*/
function beforesubmit($thisform,extvalrequired){
	var $inputfile=$thisform.find("input[type='file']");
	var filename = $inputfile.val(); 
	if(filename==""){
		 alert("请选择文件");   
		 $inputfile.focus();
	     return false;
	}
    var m=parseInt(filename.toString().lastIndexOf("."))+1;   
    var extVal=filename.toString().substr(m);   
    if(extVal!=extvalrequired) {   
        alert('文件后缀名必须为“'+extvalrequired+'”');   
        $inputfile.focus();
        return false;   
    }   
    $thisform.find("[name='loadingimg']").show();   
    $thisform.find("[name='upmessage']").html('文件上传中，请稍候... ...'); 
    return true;
}
/**上传成功后需处理*/
function successreponse($thisform,data){
	$upmessage=$thisform.find("[name='upmessage']");
	$upmessage.html("");
	if(data==null){
   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
        return;
   	}
	
	if(data.errmsg!=""){
		$thisform.find("[name='loadingimg']").hide();
		$upmessage.html("上传文件错误："+data.errmsg);
		return;
	}
	$thisform.find("[name='loadingimg']").hide();
	alert("上传成功！");
	//查询并更新文档状态
	getdocstatus($thisform);
}
/**查询并更新文档状态*/
function getdocstatus($thisform){
	var stuid=$thisform.siblings("input[name='stuid']").val();
	var doctype=$thisform.siblings("input[name='doctype']").val();
	var $uploadbtn=$thisform.find("input[name='uploadbtn']");
	var $upmessage=$thisform.find("[name='upmessage']");
	var $status=$thisform.parent().prev();//html
	var $docpath=$thisform.parent().prev().prev().find("a");//href,title
	$upmessage.html("更新文档状态……");
	$.getJSON("../SubSubmitServlet?mode=getUploadstatus",{stuid:stuid,doctype:doctype},function call(data){ 
		$upmessage.html("");
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
		}else if(resultarr[0]=="1"){//已上传，允许重新上传
			$status.html("已上传");
			$uploadbtn.val("重新上传");
			
			$docpath.attr("href","../FileDownloadServlet0?filenamewithallpath="+resultarr[1]);
			$docpath.attr("title","下载文件");
		}else if(resultarr[0]=="2"){//等待盲审或已归档，不允许重新上传
			if(doctype=="paperblind"){
				$status.html("等待盲审");
				$thisform.hide();
			}else{
				$status.html("已归档");
				$thisform.hide();
			}
			
			$docpath.attr("href","../FileDownloadServlet0?filenamewithallpath="+resultarr[1]);
			$docpath.attr("title","下载文件");
		}else if(resultarr[0]=="3"){//已盲审，可查看盲审结果（只paperblind可用）
			$status.text("已盲审");
			$thisform.hide();
			//增加“查看盲审意见”操作按钮
			$thisform.parent().append("<a href='PaperBlindOptionShow.jsp?stuid="+stuid+"' target='_blank'>查看盲审意见"+"</a>");
			
			$docpath.attr("href","../FileDownloadServlet0?filenamewithallpath="+resultarr[1]);
			$docpath.attr("title","下载文件");
		}
	});
}
