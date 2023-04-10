$(document).ready(function(){
	var $designprocessmagdiv=$("#processmagdiv");//过程管理div
	
	
});
/**结束学生选题操作*/
function setassignsubject(){
	var $vid=$("#processmagdiv input[name='setassignsubjectbtn']");
	$promptmsg=$vid.next();
	if(confirm("确定要设置指派课题吗？")==false) return;
	$promptmsg.show();
	$.getJSON("../StusubServlet?mode=setassignsubject",function call(data){   
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
       	alert("设置指派课题成功！共有"+data.result+"个学生需要专业负责人指派课题。");
       	$promptmsg.hide();
	});
}

/**生成周总结*/
function startgraduate(){
	var $this=$("#processmagdiv input[name='startgraduatebtn']");
	$promptmsg=$this.next();
	if(confirm("在开始毕业设计前，请先确定系统参数中的“毕业设计周数”以及“毕业设计开始时间”是否正确。确定要开始毕业设计吗？")==false) return;
	$promptmsg.show();
	$.getJSON("../WeekSumServlet?mode=initStartGraduate",function call(data){   
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
       	alert("成功开始毕业设计！");
       	$promptmsg.hide();
	});
}
/**系统初始化*/
function initsystem(){
	var $this=$("#processmagdiv");
	$promptmsg=$this.find("[name='initsystembtn']").next();
	var retrieveyear=$this.find("[name='retrieveyear']").val();
	
	if(confirm("初始化操作将清空当前库中的所有信息，确定要初始化？")==false) return;
	$promptmsg.show();
	$.getJSON("../SubjectLibServlet?mode=sysinit",{retrieveyear:retrieveyear},function call(data){   
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
       	alert("系统初始化成功！");
       	$promptmsg.hide();
	});
}