$(document).ready(function(){
	$("#subjectreviewul").tabs("#subjectreviewpanes> div");
	
	
	//绑定盲审按钮
	var $errmsg=$("#subjectreviewMagdiv [name='errmsg0']");
	var $loadmsg=$("#subjectreviewMagdiv [name='loadingimg']");
	//绑定查询盲审情况按钮
	$("#subjectreviewMagdiv input[name='searchblindinfo']").click(function(){
		var $teachertb=$("#subjectreviewMagdiv [name='teachertb']");
		$teachertb.empty();
		var $errmsgtb=$("#subjectreviewMagdiv [name='errmsgtb']");
		var $loadmsgtb=$("#subjectreviewMagdiv [name='loadingimgtb']");$errmsgtb.text("正在查询课题分配情况……");
		$loadmsgtb.show();
		$.getJSON("../TeacherServlet?mode=getAllsubnum",null,function call(data){
			if(data==null){
	       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	            return;
	       	}
			if(data.errmsg!=""){
				$errmsgtb.text(data.errmsg);
				$loadmsgtb.hide();
				$teachertb.empty();
        		return;
        	}
			subnumarr=eval(data.result);
			$.each(subnumarr,function(index,item){
				var xh=index+1;
				var submitsubnum=item.submitsubnum;
				var reviewsubnum=item.reviewsubnum;
				var unrevnum=item.unrevnum;
				if(submitsubnum!=reviewsubnum){
					submitsubnum="<font color='red'>"+submitsubnum+"</font>";
					reviewsubnum="<font color='red'>"+reviewsubnum+"</font>";
				}
				if(unrevnum!=0){
					unrevnum="<font color='red'>"+unrevnum+"</font>";
				}
				$teachertb.append("<tr>"+
						"<td>"+xh+"</td>"+
						"<td>"+item.tid+"</td>"+
						"<td>"+item.tname+"</td>"+
						"<td>"+submitsubnum+"</td>"+
						"<td>"+reviewsubnum+"</td>"+
						"<td>"+unrevnum+"</td>"+
					  "</tr>");
			});
			$teachertb.tbhighligt();//高亮显示当前行
			$errmsgtb.text("");
			$loadmsgtb.hide();
		});
	});
	//绑定分配盲审课题按钮
	$("#subjectreviewMagdiv input[name='assignsubtoteacher']").click(function(){
		if(confirm("确定要分配盲审课题吗？")==false) return;
		$errmsg.text("正在为教师分配盲审课题，这个过程可能需要几分钟，请耐心等待……");
		$loadmsg.show();
		$.getJSON("../TeacherServlet?mode=assignSubToTeaForReview",null,function call(data){ 
			if(data==null){
	       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	       		$loadmsg.hide();
	            return;
	       	}
			if(data.errmsg!=""){
				$errmsg.text(data.errmsg);
				$loadmsg.hide();
        		return;
        	}

			$errmsg.text("分配完成！"+data.result);
			$loadmsg.hide();
			
			$("#subjectreviewMagdiv input[name='searchblindinfo']").trigger("click");
		});
	});
	/**课题盲审查询*/
	SelectSubjectReviews();
});
/**课题盲审查询*/
function SelectSubjectReviews(){
	//获取code
	code_getspeciality_select($("#subjectreviewlistform select[name='specid']"));//专业列表
	code_getcode_select("jxdw","#subjectreviewlistform [name='tdept']");//所属教研室
	//绑定查询表单#subjectreviewlistform
	var options_subreviewselect={
			beforeSubmit:  function(){
				$spec=$("#subjectreviewlistform select[name='specid']");
				if($spec.val()==""){
					alert("专业不能为空，请选择！");
					$spec.focus();
					return false;
				}
				$loadingimg=$("#subjectreviewlistform img[name='loadingimg']");
				$errmsg=$("#subjectreviewlistform span[name='errmsg0']");
				$loadingimg.show();
				$errmsg.text("正在查询，请稍候……");
			},
			dataType: 'json',
			success:  SelectSubReviewsResponse
		};
		$('#subjectreviewlistform').ajaxForm(options_subreviewselect);
}
/**课题盲审查询 响应信息*/
function SelectSubReviewsResponse(data){
	$tbody=$("#subjectreviewSelectdiv tbody[name='tbsubreviews']");
	$loadingimg=$("#subjectreviewSelectdiv img[name='loadingimg']");
	$errmsg=$("#subjectreviewSelectdiv span[name='errmsg0']");
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
    var subreviewsarr=eval(data.result);
    $tbody.empty();
    
    $.each(subreviewsarr,function(index,item){
    	var xh=index+1;
    	/*<th>序号</th><th>(课题编号)课题名</th><th>指导教师</th><th>审核人</th><th>审核意见</th>*/
      	$tbody.append("<tr>"+
      			      "<td>"+xh+"</td>"+"<td  width='400px'>("+item.subid+")&nbsp;<a href='../teacher/SubjectView.jsp?subid="+item.subid+"' target='_blank'>"+item.subname+"</a></td>"+
                     "<td  width='100px'>"+item.tutornames+"</td><td width='60px'>"+item.reviewername+"</td>"+
                     "<td>"+item.reviewopinion+"</td>"+
                     "</tr>");
    });
    
    $tbody.tbhighligt();//高亮显示当前行
    
    $loadingimg.hide();
	$errmsg.text("查询结果数："+subreviewsarr.length);
}
