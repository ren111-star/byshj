$(document).ready(function(){
	//绑定 查询 按钮 开始
	$("#reviewpaperdiv input[name='searchbtn']").click(function(){
		var $teachertb=$("#reviewpaperdiv [name='teachertb']");
		var $errmsg=$("#reviewpaperdiv [name='errmsg0']");
    	var $loadmsg=$("#reviewpaperdiv [name='loadingimg']");
    	var tid=$("#tid").val();
    	
		$.getJSON("../ReviewPaperServlet?mode=getPapersReviewedByTid",{tid:tid},function call(data){
			if(data==null){
	       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	       		$loadmsg.hide();
				$teachertb.empty();
	            return;
	       	}
			if(data.errmsg!=""){
				$errmsg.text(data.errmsg);
				$loadmsg.hide();
				$teachertb.empty();
				return;
	       	}
			var reviewsubjectsarr=eval(data.result);
			if(data.result==""){
				$errmsg.text("查询结果数：0");
				$teachertb.empty();
				$loadmsg.hide();
				return;
			}
			$errmsg.text("查询结果数："+reviewsubjectsarr.length);	
			$teachertb.empty();
			$.each(reviewsubjectsarr,function(index,item){
				var xh=index+1;
				var subname=item.subname;
				var subid=item.subid;
				var submitflag=item.submitflag;
				var reviewstatus="";
				var operation="";
				var docstatusarr= item.docstatus.split("|");//[status,path]
				var docstatuscode=docstatusarr[0];
				var docstatustr="";
				if(docstatuscode=='0'){
					docstatustr="<font color='red'>[未上传]</font>";
				}
				
				if(submitflag=='1'){//已提交评阅意见
					reviewstatus="已提交["+item.sumgrade+"分]";
					operation="<input type='button' class='btn' value='查看评阅结果' onclick=\"showReviewPaperWinOnlyRead('"+subid+"','"+subname+"')\"/>";
				}else{//未提交评阅意见
					reviewstatus="未提交["+item.sumgrade+"分]";
					operation="<input type='button' class='btn' value='评阅' onclick=\"showReviewPaperWin('"+subid+"','"+subname+"')\"/>";
				}
				
				$teachertb.append("<tr>"+
						"<td>"+xh+"</td>"+
						"<td><a href='SubjectViewForReview.jsp?subid="+subid+"' target='_blank' title='查看任务书'>[任务书]</a>&nbsp;&nbsp;<a href='../FileDownloadServlet0?filenamewithallpath="+docstatusarr[1]+"' title='下载文件'>"+subname+docstatustr+"</a></td>"+
						"<td name='statustd"+subid+"'>"+reviewstatus+"</td>"+
						"<td>"+operation+"</td>"+
					  "</tr>");
			});
			//
			$teachertb.tbhighligt();//高亮显示当前行
			$loadmsg.hide();
		});
	});
	// 绑定 查询 按钮 结束
	$("#reviewpaperdiv input[name='searchbtn']").trigger("click");
	
	var $reviewpaperform=$("#reviewpaperdiv form[name='reviewpaperform']");
	/**绑定input的change事件*/
	reviewpaperchange($reviewpaperform);
	//绑定“保存”评审结果按钮
	$("#reviewpaperdiv input[name='savebtn']").click(function(){
		saveorsubmitreviewpaper($reviewpaperform,'0');
	});
	//绑定“提交”评审结果按钮
	$("#reviewpaperdiv input[name='submitbtn']").click(function(){
		if(confirm("提交后将不能再修改评阅的任何信息，确定要提交？")==false) return;
		saveorsubmitreviewpaper($reviewpaperform,'1');
	});
});
/**评审结果保存或提交*/
function saveorsubmitreviewpaper($reviewpaperform,submitflag){
	var subid=$reviewpaperform.find("input[name='subid']").val();
	$reviewpaperform.ajaxSubmit({
	 	beforeSubmit:  checkreviewpaperresbefore,
		dataType: 'json',
		url:"../ReviewPaperServlet?mode=setReviewOpinion&&submitflag="+submitflag+"&&subid="+subid,
		success:      function(data){
			if(data==null){
	       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	            return;
	       	}
			if(data.errmsg!=""){
				alert("错误："+data.errmsg);
				return;
	       	}
			//
			alert("操作成功！");
			
			hideDialog("#reviewpaperdiv [name='reviewpaperwin']");
			//刷新状态
			$("#reviewpaperdiv input[name='searchbtn']").trigger("click");
		}
	 });
}
//获得每个课题的评阅结果
function getreviewpaperresult(subid,$div){
	$.ajaxSetup({async: false});//改为同步
	$.getJSON("../ReviewPaperServlet?mode=getReviewOpinion",{subid:subid},function call(data){
		if(data==null){
       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
            return;
       	}
		if(data.errmsg!=""){
			alert("错误："+data.errmsg);
			return;
       	}
		var result=eval(data.result);
		$div.find("[name='significance']").val(result.significance);
		$div.find("[name='designcontent']").val(result.designcontent);
		$div.find("[name='composeability']").val(result.composeability);
		$div.find("[name='translationlevel']").val(result.translationlevel);
		$div.find("[name='innovative']").val(result.innovative);
		$div.find("[name='sumgrade']").val(result.sumgrade);
		if(result.reviewopinion==undefined){
			$div.find("[name='reviewopinion']").val("");
		}else{
			$div.find("[name='reviewopinion']").val(result.reviewopinion);
		}
		$div.find("[name='reviewtime']").val(result.reviewtime);
	});
	$.ajaxSetup({async: true});//改为异步
}
/**显示评阅窗口*/
function showReviewPaperWin(subid,subname){
	
	//显示评阅窗口
	var $reviewwindiv=$("#reviewpaperdiv [name='reviewpaperwin']");
	$reviewwindiv.find("input[name='subid']").val(subid);
	$("#reviewpaperdiv [name='subname']").text(subname);
	//设置窗口样式(width,height,top,left)
	showDialogForSet($reviewwindiv,"600px","650px","10px","400px");//jquerywin.js
	///////////
	////从数据库中取得具体值
	getreviewpaperresult(subid,$reviewwindiv);
	//使输入文本框和按钮有效
	var $reviewwindiv=$("#reviewpaperdiv [name='reviewpaperwin']");
	$reviewwindiv.find("input").removeAttr("disabled");
	$reviewwindiv.find("textarea").removeAttr("disabled");
	$reviewwindiv.find("input[type='button']").show();
	
}
/**显示“查看评阅结果”窗口*/
function showReviewPaperWinOnlyRead(subid,subname){
	//显示评阅窗口
	showReviewPaperWin(subid,subname);
	//使输入文本框和按钮失效
	var $reviewwindiv=$("#reviewpaperdiv [name='reviewpaperwin']");
	$reviewwindiv.find("input").attr("disabled","disabled");
	$reviewwindiv.find("textarea").attr("disabled","disabled");
	$reviewwindiv.find("input[type='button']").hide();
	$reviewwindiv.find("span[name='notice']").hide();
}
/**返回评审对象*/
function checkreviewpaperresbefore(){
	var $div=$("#reviewpaperdiv");
	var significance=$div.find("[name='significance']").val();
	if(parseFloat(significance)<0||parseFloat(significance)>2){
		alert("选题意义必须取0-2范围的值");
		$div.find("[name='significance']").focus();
		return false;
	}
	var designcontent=$div.find("[name='designcontent']").val();
	if(parseFloat(designcontent)<0||parseFloat(designcontent)>10){
		alert("毕业设计内容必须取0-10范围的值");
		$div.find("[name='designcontent']").focus();
		return false;
	}
	var composeability=$div.find("[name='composeability']").val();
	if(parseFloat(composeability)<0||parseFloat(composeability)>3){
		alert("设计说明书撰写能力必须取0-3范围的值");
		$div.find("[name='composeability']").focus();
		return false;
	}
	var translationlevel=$div.find("[name='translationlevel']").val();
	if(parseFloat(translationlevel)<0||parseFloat(translationlevel)>3){
		alert("文献查阅外文翻译必须取0-3范围的值");
		$div.find("[name='translationlevel']").focus();
		return false;
	}
	var innovative=$div.find("[name='innovative']").val();
	if(parseFloat(innovative)<0||parseFloat(innovative)>2){
		alert("创新性必须取0-2范围的值");
		$div.find("[name='innovative']").focus();
		return false;
	}
	var reviewopinion=$div.find("[name='reviewopinion']").val();
	if(reviewopinion==""||reviewopinion==null){
		alert("评阅意见不能为空");
		$div.find("[name='reviewopinion']").focus();
		return false;
	}
	var reviewtime=$div.find("[name='reviewtime']").val();
	if(reviewtime==""||reviewtime==null){
		alert("评阅时间不能为空");
		$div.find("[name='reviewtime']").focus();
		return false;
	}
	return true;
}
/**绑定input的change事件*/
function reviewpaperchange($reviewpaperform){
	//改变事件
	$reviewpaperform.find("input[type='text']").change(function(){
		updatesumgrade($reviewpaperform);
	});
	//响应回车转移焦点
	$reviewpaperform.find("[name='significance']").keypress(function(event){
		if(event.keyCode != 13) return;
		if(parseFloat($(this).val())<0||parseFloat($(this).val())>2){
			alert("选题意义必须取0-2范围的值");
			$(this).focus();
			return;
		}
		updatesumgrade($reviewpaperform);
		$reviewpaperform.find("[name='designcontent']").focus();
	});
	$reviewpaperform.find("[name='designcontent']").keypress(function(event){
		if(event.keyCode != 13) return;
		if(parseFloat($(this).val())<0||parseFloat($(this).val())>10){
			alert("毕业设计内容必须取0-10范围的值");
			$(this).focus();
			return;
		}
		updatesumgrade($reviewpaperform);
		$reviewpaperform.find("[name='composeability']").focus();
	});
	$reviewpaperform.find("[name='composeability']").keypress(function(event){
		if(event.keyCode != 13) return;
		if(parseFloat($(this).val())<0||parseFloat($(this).val())>3){
			alert("设计说明书撰写能力必须取0-3范围的值");
			$(this).focus();
			return;
		}
		updatesumgrade($reviewpaperform);
		$reviewpaperform.find("[name='translationlevel']").focus();
	});
	$reviewpaperform.find("[name='translationlevel']").keypress(function(event){
		if(event.keyCode != 13) return;
		if(parseFloat($(this).val())<0||parseFloat($(this).val())>3){
			alert("文献查阅外文翻译必须取0-3范围的值");
			$(this).focus();
			return;
		}
		updatesumgrade($reviewpaperform);
		$reviewpaperform.find("[name='innovative']").focus();
	});
	$reviewpaperform.find("[name='innovative']").keypress(function(event){
		if(event.keyCode != 13) return;
		if(parseFloat($(this).val())<0||parseFloat($(this).val())>2){
			alert("创新性必须取0-2范围的值");
			$(this).focus();
			return;
		}
		updatesumgrade($reviewpaperform);
		$reviewpaperform.find("[name='reviewopinion']").focus();
	});
	$reviewpaperform.find("[name='reviewopinion']").keypress(function(event){
		if(event.keyCode != 13) return;
		updatesumgrade($reviewpaperform);
		//$reviewpaperform.find("[name='reviewtime']").focus();
	});
	$reviewpaperform.find("[name='reviewtime']").keypress(function(event){
		if(event.keyCode != 13) return;
		updatesumgrade($reviewpaperform);
		$reviewpaperform.find("[name='savebtn']").focus();
	});
}
/**单项改变，自动更新汇总项 设置评阅结果*/
function updatesumgrade($reviewpaperform){
	var significance=$reviewpaperform.find("[name='significance']").val();
	var designcontent=$reviewpaperform.find("[name='designcontent']").val();
	var composeability=$reviewpaperform.find("[name='composeability']").val();
	var translationlevel=$reviewpaperform.find("[name='translationlevel']").val();
	var innovative=$reviewpaperform.find("[name='innovative']").val();
	var totalgrade=parseFloat(significance)+parseFloat(designcontent)+parseFloat(composeability)+parseFloat(translationlevel)+parseFloat(innovative);
	
	$reviewpaperform.find("[name='sumgrade']").val(totalgrade);
	$pass=$reviewpaperform.find("[name='pass']");
	$updatepass=$reviewpaperform.find("[name='updatepass']");
	$unpass=$reviewpaperform.find("[name='unpass']");
	//设置评阅结果	
	if(totalgrade>=16&&totalgrade<=20){
		$pass.show();
		$updatepass.hide();
		$unpass.hide();
	}
    if(totalgrade<16&&totalgrade>=12){
    	$pass.hide();
		$updatepass.show();
		$unpass.hide();
	}
    if(totalgrade<12){
    	$pass.hide();
		$updatepass.hide();
		$unpass.show();
	}
}