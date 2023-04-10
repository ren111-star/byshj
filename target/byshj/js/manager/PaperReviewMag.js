//论文盲审操作
$(document).ready(function(){
	$("#paperreviewul").tabs("#paperreviewpanes >div");
	//绑定论文盲审按钮
	var $errmsg=$("#paperblindreviewMagdiv [name='errmsg0']");
	var $loadmsg=$("#paperblindreviewMagdiv [name='loadingimg']");
	//绑定查询盲审情况按钮
	$("#paperblindreviewMagdiv input[name='searchblindinfo']").click(function(){
		var $teachertb=$("#paperblindreviewMagdiv [name='teachertb']");
		$teachertb.empty();
		var $errmsgtb=$("#paperblindreviewMagdiv [name='errmsgtb']");
		var $loadmsgtb=$("#paperblindreviewMagdiv [name='loadingimgtb']");$errmsgtb.text("正在查询论文分配情况……");
		$loadmsgtb.show();
		$.getJSON("../ReviewPaperServlet?mode=getAllpapernum",null,function call(data){
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
	$("#paperblindreviewMagdiv input[name='assignsubtoteacher']").click(function(){
		if(confirm("确定要分配盲审论文吗？")==false) return;
		$errmsg.text("正在为教师分配盲审论文，这个过程可能需要几分钟，请耐心等待……");
		$loadmsg.show();
		$.getJSON("../ReviewPaperServlet?mode=assignPaperToTeaForReview",null,function call(data){ 
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
			
			$("#paperblindreviewMagdiv input[name='searchblindinfo']").trigger("click");
		});
	});
	/**论文盲审查询*/
	SelectPaperReviews();
});
/**论文盲审查询*/
function SelectPaperReviews(){
	//获取code
	var $specsel=$("#paperreviewlistform select[name='specid']");
	code_getspeciality_select($specsel);//专业列表
	//级联班级列表
	$specsel.change(function(){
		var specid=$(this).val();
		if(specid=="") return;
		$("#paperreviewlistform select[name='classname']").empty();
		code_getclass_select(specid,"#paperreviewlistform select[name='classname']");//班级列表
	});
	//绑定查询表单#paperreviewlistform
	var options_paperreviewselect={
			beforeSubmit:  function(){
				$spec=$("#paperreviewlistform select[name='specid']");
				if($spec.val()==""){
					alert("专业不能为空，请选择！");
					$spec.focus();
					return false;
				}
				$loadingimg=$("#paperreviewlistform img[name='loadingimg']");
				$errmsg=$("#paperreviewlistform span[name='errmsg0']");
				$loadingimg.show();
				$errmsg.text("正在查询，请稍候……");
			},
			dataType: 'json',
			success:  SelectPaperReviewsResponse
		};
		$('#paperreviewlistform').ajaxForm(options_paperreviewselect);
}
/**论文盲审查询 响应信息*/
function SelectPaperReviewsResponse(data){
	$tbody=$("#paperblindreviewSelectdiv tbody[name='tbstudents']");
	$loadingimg=$("#paperblindreviewSelectdiv img[name='loadingimg']");
	$errmsg=$("#paperreviewlistform span[name='errmsg0']");
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
    var paperreviewsarr=eval(data.result);
    $tbody.empty();
    
    $.each(paperreviewsarr,function(index,item){
    	var xh=index+1;
    	/*序号</th><th style="width:80px">班级</th><th style="width:40px">学号</th><th style="width:60px">姓名</th>
			<th>毕业设计（论文）题目</th><th>指导教师</th><th>评阅人</th><th>操作</th>*/
    	var submitflag=item.submitflag;
    	var operationstr="<label class='flag'>未评阅</label>";
    	if(submitflag=="1"){
    		operationstr="<a href='../student/PaperBlindOptionShow.jsp?stuid="+item.stuid+"' target='_blank'>查看评阅</a>&nbsp;"+
                     "<input type='hidden' value='"+item.subid+"'/><input type='button' value='撤销' class='btn' name='cancel'/><span class='flag'></span>";
    	}
      	$tbody.append("<tr>"+
      			      "<td>"+xh+"</td>"+
      			      "<td>"+item.classname+"</td>"+
      			      "<td>"+item.stuid+"</td>"+
      			    "<td>"+item.sname+"</td>"+
      			    "<td>("+item.subid+")&nbsp;<a href='../teacher/SubjectView.jsp?subid="+item.subid+"' target='_blank'>"+item.subname+"</a></td>"+
                     "<td>"+item.tutornames+"</td><td width='40px'>"+item.reviewername+"</td>"+
                     "<td>"+operationstr+"</td>"+
                     "</tr>");
    });
    
    $tbody.tbhighligt();//高亮显示当前行
    $loadingimg.hide();
	$errmsg.text("查询结果数："+paperreviewsarr.length);
	//绑定  撤销评阅操作
	$tbody.find("input[name='cancel']").click(function(){
		if(confirm("确定要撤销评阅意见吗？")==false) return;
		$this=$(this);
		var subid=$this.prev().val();
		//
		$prompt=$this.siblings().last();
		$prompt.text("正在撤销……");
		$.getJSON("../ReviewPaperServlet?mode=cancelPaperReview",{subid:subid},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
        		alert(data.errmsg);
        		return;
        	}
        	//撤销成功后，设置为“未评阅”状态
			$this.parent().html("<label class='flag'>未评阅</label>");
			
			$prompt.text("");
	    });
	});
}

