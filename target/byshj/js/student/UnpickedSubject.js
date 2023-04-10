$(document).ready(function(){
	code_getcode_select("jxdw","#subbyspecform [name='tdept']");//所属教研室
	/*课题列表form绑定*/
	var options_subbyspecform={
		beforeSubmit:function(){
			$("#unpicksubjectdiv [name='loading']").show();
			$("#unpicksubjectdiv [name='searchresult']").text("查询进行中，请稍候……");
		},
		dataType: 'json',
		success:      subbyspecResponse
	};
	$('#subbyspecform').ajaxForm(options_subbyspecform);
	//$('#subbyspecform').submit();  
	
	//绑定“提交选题志愿”操作
	$("#unpicksubjectdiv input[value='提交选题志愿']").click(function(){
		var stuid=$("#stuid").val();
		var pickedsubarr=new Array();
		
		$("#unpicksubjectdiv [name='firstpicktb']").children().each(function(index){
			var subid=$(this).children().eq(1).children().eq(0).val();
			var pickorder=$(this).children().eq(0).text();
			
			var stusub={subid:subid,pickorder:pickorder};
			pickedsubarr[index]=stusub;
		});
		var assignflag=$("#unpicksubjectdiv [name='assignflag']:checked").val();
		//检查是否已初步选题
		if(pickedsubarr.length==0&&assignflag!=1){
			alert("您还未选题，请从'可选课题列表'中选择课题；或者直接选中“落选后，需要专业负责人指派课题”");
			return;
		}
		//检查是否指派
		
		if(assignflag=="1"){
			var stusub={subid:"",pickorder:""};
			pickedsubarr[pickedsubarr.length]=stusub;
		}
		//将javascript数组转换成json字符串
		var pickedsubjson=$.toJSON(pickedsubarr);
		//确定是否提交选题结果
		if(confirm("确定要提交初选结果？")==false) return;
		//提交初选结果
		$.getJSON("../StusubServlet?mode=submitpickresultfirst",{stuid:stuid,pickedsubarr:pickedsubjson},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
        		alert(data.errmsg);
        		return;
        	}
        	alert("初选课题完成！");
        	//学生状态转变为“已初选”，需刷新当前页面
        	window.location.reload();
        });
	});
});
/*课题列表 查询 响应*/
function subbyspecResponse(data){
	if(data==null){
   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
   		$("#unpicksubjectdiv [name='loading']").hide();
        return;
   	}
	if(data.errmsg!=""){
   		$("#unpicksubjectdiv [name='searchresult']").text(data.errmsg);
   		$("#unpicksubjectdiv [name='loading']").hide();
   		return;
   	}
   	$tbody=$("#unpicksubjectdiv [name='unpickedsubtb']");
   	if(data.result==""){
   		$("#unpicksubjectdiv [name='searchresult']").text("结果数：0");
   		$tbody.empty();
   		$("#unpicksubjectdiv [name='loading']").hide();
   		return;
   	}
    var subjectarr=eval(data.result);
    
    $tbody.empty();
    var xh=0;
    $.each(subjectarr,function(index,item){
    	var statusarr=item.status.split("/");
    	var status="";
    	if(statusarr[0]=="已初选"||statusarr[0]=="已发布-等待选题"){
    		xh=xh+1;
    		if(statusarr[0]=="已初选") status="初选人数:"+statusarr[1];
    		if(statusarr[0]=="已发布-等待选题") status="未选";
    		var othertname=item.othertutor.tname;
            if(othertname==undefined)othertname="";
            var tpostname=item.tutor.tpostname;
            if(tpostname==undefined)tpostname="";
            //已经被选择的课题，在重新查询时显示“撤销选中”按钮
            var strtr="<tr>"+
    		               "<td>"+xh+"</td><td width='400px'>("+item.subid+")&nbsp;<a href='../teacher/SubjectView.jsp?subid="+item.subid+"' target='_blank' title='点击查看任务书'>"+item.subname+"</a></td>"+
    		               "<td>"+status+"</td>"+"<td>"+item.tutor.tname+"&nbsp;&nbsp;"+othertname+"</td>"+
    		               "<td>&nbsp;"+tpostname+"</td>"+"<td>"+item.tutor.tdeptname+"</td>"+
                           "<td  style='width:80px'><input type='hidden' value='"+item.subid+"'/><input type='hidden' value='"+item.subname+"'/>"+
                           "<input type='button' value='选中' class='btn'/><input type='button' value='撤销选中' class='btn' style='display:none'/></td></tr>";
            $("#unpicksubjectdiv [name='firstpicktb']").find("tr").each(function(index){
            	var $this=$(this);
            	var subid=$this.children().eq(1).find("input").val();
            	if(subid==item.subid){
            		strtr="<tr>"+
    		               "<td>"+xh+"</td><td width='400px'  class='highlight'>("+item.subid+")&nbsp;<a href='../teacher/SubjectView.jsp?subid="+item.subid+"' target='_blank'>"+item.subname+"</a></td>"+
    		               "<td>"+status+"</td>"+"<td>"+item.tutor.tname+"&nbsp;&nbsp;"+othertname+"</td>"+
    		               "<td>&nbsp;"+tpostname+"</td>"+"<td>"+item.tutor.tdeptname+"</td>"+
                           "<td  style='width:80px'><input type='hidden' value='"+item.subid+"'/><input type='hidden' value='"+item.subname+"'/>"+
                           "<input type='button' value='选中' class='btn' style='display:none'/><input type='button' value='撤销选中' class='btn' /></td></tr>";
            	};
            });
            //
    		$tbody.append(strtr);
    	}
    });
    $tbody.tbhighligt();//高亮显示当前行
    $("#unpicksubjectdiv [name='searchresult']").text("结果数："+xh);
    $("#unpicksubjectdiv [name='loading']").hide();
    //绑定“选中”操作
    $("#unpicksubjectdiv [name='unpickedsubtb'] input[value='选中']").click(function(){
		//判断已选志愿数是否为最多
		$firsttbody=$("#unpicksubjectdiv [name='firstpicktb']");
		var maxsubject=$("#maxsubject").val();//每人可选最多志愿数
		var curcount=$firsttbody.children().length;//已选志愿数
		if(curcount>=maxsubject){
			alert("每人最多可选"+maxsubject+"个课题志愿，您的志愿数已满！");
			return;
		}
		//继续选择志愿
		var subid=$(this).siblings().eq(0).val();
		var subname=$(this).siblings().eq(1).val();
		var tutor=$(this).parent().siblings().eq(3).text();
		var xh=1;//志愿顺序
		if($firsttbody.text()!=""){
			xh=+($firsttbody.children().last().children().first().text())+1;
		}
		$firsttbody.append("<tr><td>"+xh+"</td><td><input type='hidden' value='"+subid+"'/>("+subid+")"+subname+"</td><td>"+tutor+"</td>"+
		                    "<td><input type='button' value='向上' class='btn'/></td>"+
		                   "</tr>");
		//绑定调整志愿的“向上”操作,加.last()避免重复绑定
		$("#unpicksubjectdiv [name='firstpicktb'] input[value='向上']").last().click(function(event){
			var xh=$(this).parent().siblings().eq(0).text();
			if(xh==1){ 
				alert("已是第一志愿！");
				return;
			}
			//调换当前行和前一行的内容，志愿顺序列不变
			var curtd1=$(this).parent().siblings().eq(1).html();
			var curtd2=$(this).parent().siblings().eq(2).html();
			
			var prevtd1=$(this).parent().parent().prev().children().eq(1).html();
			var prevtd2=$(this).parent().parent().prev().children().eq(2).html();
			
			$(this).parent().siblings().eq(1).html(prevtd1);
			$(this).parent().siblings().eq(2).html(prevtd2);
			
			$(this).parent().parent().prev().children().eq(1).html(curtd1);
			$(this).parent().parent().prev().children().eq(2).html(curtd2);
			
		});
		
		//隐藏“选中”按钮，显示”撤销选中“按钮
		$(this).hide();
		$(this).next().show();
		//突出选中行
		$(this).parent().siblings().eq(1).addClass("highlight");
		
	});
	
	//绑定“撤销选中”操作
	$("#unpicksubjectdiv [name='unpickedsubtb'] input[value='撤销选中']").click(function(){
		var subid=$(this).siblings().eq(0).val();
		$("#unpicksubjectdiv [name='firstpicktb'] tr").each(function(){
			var subid0=$(this).find("input").eq(0).val();
			if(subid0==subid) $(this).remove();
		});
		//调整志愿顺序
		$("#unpicksubjectdiv [name='firstpicktb'] tr").each(function(index){
			$(this).children().eq(0).text(index+1);
		});
		
		//显示“选中”按钮，隐藏”撤销选中“按钮
		$(this).hide();
		$(this).prev().show();
		//消除突出选中行
		$(this).parent().siblings().eq(1).removeClass("highlight");
	});
}
