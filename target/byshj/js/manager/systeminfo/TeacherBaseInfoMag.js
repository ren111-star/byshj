$(document).ready(function(){
	//初始化code列表
	code_getcode_select("jxdw","#teachermagui [name='tdept']");//教研室
	code_getcode_select("zhch","#teachermagui [name='tpost']");//职称
	code_getcode_select("xw","#teachermagui [name='tdegree']");//学位
	
	//选择条件和新增的教研室name相同，取值也级联相同
	$tdept=$("#teachermagui select[name='tdept']");
	$tdept.eq(0).change(function(){
		$tdept.eq(1).val($(this).val());
	});
	
	/*绑定教师列表tealistform  查询*/
	var options={
		beforeSubmit:  teavalidate,
		dataType: 'json',
		success:      teaResponse
	};
	$('#tealistform').ajaxForm(options);
	
	//绑定增加教师 操作
	$("#teachermagui input[name='add']").click(function(){
		$this=$(this);
		$errmsg=$("#teachermagui [name='errmsg']");
		var tid=$this.parent().siblings().eq(1).find("input").val();
		var tname=$this.parent().siblings().eq(2).find("input").val();
		var tdept=$this.parent().siblings().eq(3).find("select").val();
		var tpost=$this.parent().siblings().eq(4).find("select").val();
		var tdegree=$this.parent().siblings().eq(5).find("select").val();
		//有效性验证
		if(tid==""||tid.length!=6){
			if(tid=="") $errmsg.text("职工号不能为空！");
			if(tid!=""&&tid.length!=10) {
				$errmsg.text("职工号必须为6位字符！");
				$this.parent().siblings().eq(1).find("input").addClass("texthighlight");
				$this.parent().siblings().eq(1).find("input").change(function(){
					$(this).removeClass("texthighlight");
				});
			}
			$this.parent().siblings().eq(1).find("input").focus();
			return;
		}
		if(tname==""){
			$errmsg.text("姓名不能为空！");
			$this.parent().siblings().eq(2).find("input").focus();
			return;
		}
		if(tdept==""){
			$errmsg.text("教研室不能为空！");
			$this.parent().siblings().eq(3).find("select").focus();
			return;
		}
		//组装结果
		var teacher={tid:tid,tname:tname,tdept:tdept,tpost:tpost,tdegree:tdegree};
		var teacherjson=$.toJSON(teacher);
		//
		$errmsg.text("正在增加"+tid+"……");
		$.getJSON("../TeacherServlet?mode=edt&flag=1",{teacher:teacherjson},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
        		alert(data.errmsg);
        		$errmsg.text("");
        		return;
        	}
        	//增加成功后，刷新教师列表
        	$errmsg.text("");
        	//清空控件值  教研室select中值保持不变
        	$this.parent().siblings().eq(1).find("input").val("");
			$this.parent().siblings().eq(2).find("input").val("");
			$this.parent().siblings().eq(4).find("input").val("");
			$this.parent().siblings().eq(5).find("input").val("");
			
        	//设置教研室，刷新增加教师所在教研室
        	$("#teachermagui select[name='tdept']").eq(0).val(teacher.tdept);
			$('#tealistform').submit();
	    });
	});
	
	/*导入excel教师信息文件 表单 绑定*/
	var optionsimport={
		target : '#upMessage', 
		url : '../ExcelServlet?mode=importteachers',
		beforeSubmit:  function(){
			var filename = $("#importteadiv input[type='file']").val();   
		    var m=parseInt(filename.toString().lastIndexOf("."))+1;   
		    var extVal=filename.toString().substr(m);   
		    if(extVal!="xls") {   
		        alert('文件类型必须为Excel文件！');   
		        return false;   
		    }   
		    $("#importteadiv [name='loadingimg']").show();   
		    $("#importteadiv [name='upmessage']").html('文件导入中，请稍候... ...');   
		    return true;
		},
		dataType: 'json',
		success:  function(data){
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			$upmessage=$("#importteadiv [name='upmessage']");
			if(data.errmsg!=""){
				$("#importteadiv [name='loadingimg']").hide();
				$upmessage.html("导入文件错误："+data.errmsg);
				return;
			}
			$("#importteadiv [name='loadingimg']").hide();
			$upmessage.html("导入成功！");
		},
		resetForm:true
	};
	$('#importteadiv form').ajaxForm(optionsimport);
	//绑定 导入教师 操作
	$("#tealistform input[value='导入教师']").click(function(){
		$win=$("#importteadiv");
		var offset=$(this).offset();   
        var top=offset.top;   
        var left=+(offset.left)-400;  
        $win.css("width","500px");
        $win.css("height","150px");
        $win.css("top",top+"px");
        $win.css("left",left+"px");
      	//绑定窗口的关闭 操作
      	$win.find(".close").click(function(){
      		$win.hide();
      	});
      	//显示窗口
      	$win.show();
	});
});
/*教师列表 查询  有效性验证 响应*/
function teavalidate(){
	$("#teachermagui [name='errmsg0']").text("查询进行中，请稍后……");;
	$("#teachermagui [name='loadingimg']").show();
	$("#teachermagui [name='teachertb']").empty();
}
//响应
function teaResponse(data){
	if(data==null){
   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
        return;
   	}
	$teachertb=$("#teachermagui [name='teachertb']");
	$errmsg=$("#teachermagui [name='errmsg0']");
	$loadmsg=$("#teachermagui [name='loadingimg']");
	if(data.errmsg!=""){
		$errmsg.text("错误："+data.errmsg);
		$loadmsg.hide();
		$teachertb.empty();
		return;
	}
	if(data.result==""){
		$errmsg.text("查询结果数：0");
		$teachertb.empty();
		$loadmsg.hide();
		return;
	}
	//显示查询结果
	teachersarr=eval(data.result);
	$errmsg.text("查询结果数："+teachersarr.length);	
	$teachertb.empty();
	$.each(teachersarr,function(index,item){
		var tpost=item.tpost;
		if(tpost==undefined) tpost="";
		var tpostname=item.tpostname;
		if(tpostname==undefined) tpostname="";
		var tdegree=item.tdegree;
		if(tdegree==undefined) tdegree="";
		var tdegreename=item.tdegreename;
		if(tdegreename==undefined) tdegreename="";
		var xh=index+1;
		$teachertb.append("<tr>"+
							"<td>"+xh+"</td>"+
							"<td>"+item.tid+"</td>"+
							"<td>"+item.tname+"</td>"+
							"<td>"+item.tdeptname+"<span style='display:none'>"+item.tdept+"</span></td>"+
							"<td>"+tpostname+"&nbsp;<span style='display:none'>"+tpost+"</span></td>"+
							"<td>"+tdegreename+"&nbsp;<span style='display:none'>"+tdegree+"</span></td>"+
							"<td><input type='button' value='修改' class='btn'/>"+
								"<input type='button' value='保存' class='btn' style='display:none'/>&nbsp;&nbsp;"+
							    "<input type='button' value='删除' class='btn'/>&nbsp;"+
							    "<input type='button' value='初始化密码' class='btn'/>&nbsp;<span class='flag'></span>"+
							"</td>"+
						  "</tr>");
	});
	$teachertb.tbhighligt();//高亮显示当前行
	$loadmsg.hide();
	//绑定 修改 操作
	$teachertb.find("input[value='修改']").click(function(){
		$this=$(this);
		//设置当前行控件可修改
		var tname=$this.parent().siblings().eq(2).text();
		$this.parent().siblings().eq(2).html("<input type='text' class='text' value='"+tname+"'/>");
		
		var tdept=$this.parent().siblings().eq(3).find("span").text();
		$this.parent().siblings().eq(3).html("<select>"+$("#teachermagui select[name='tdept']").eq(0).html()+"</select>");
		$this.parent().siblings().eq(3).find("select").val(tdept);
		
		var tpost=$this.parent().siblings().eq(4).find("span").text();
		$this.parent().siblings().eq(4).html("<select>"+$("#teachermagui select[name='tpost']").html()+"</select>");
		$this.parent().siblings().eq(4).find("select").val(tpost);
		
		var tdegree=$this.parent().siblings().eq(5).find("span").text();
		$this.parent().siblings().eq(5).html("<select>"+$("#teachermagui select[name='tdegree']").html()+"</select>");
		$this.parent().siblings().eq(5).find("select").val(tdegree);
				
		$this.hide();//隐藏修改按钮
		$this.next().show();//显示保存按钮
	});
	//绑定 保存 操作
	$teachertb.find("input[value='保存']").click(function(){
		$this=$(this);
		var tid=$this.parent().siblings().eq(1).text();
		var tname=$this.parent().siblings().eq(2).find("input").val();
		var tdept=$this.parent().siblings().eq(3).find("select").val();
		var tpost=$this.parent().siblings().eq(4).find("select").val();
		var tdegree=$this.parent().siblings().eq(5).find("select").val();
	
		var teacher={tid:tid,tname:tname,tdept:tdept,tpost:tpost,tdegree:tdegree};
		var teacherjson=$.toJSON(teacher);
		//
		$prompt=$this.siblings().last();
		$prompt.text("正在保存……");
		$.getJSON("../TeacherServlet?mode=edt&flag=2",{teacher:teacherjson},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
        		alert(data.errmsg);
        		return;
        	}
        	//select中被选中的option显示内容
			var tdeptname=$this.parent().siblings().eq(3).find("select option:selected").text();
			var tpostname=$this.parent().siblings().eq(4).find("select option:selected").text();
			var tdegreename=$this.parent().siblings().eq(5).find("select option:selected").text();
        	//保存成功后，恢复当前行各控件初始状态
			$this.parent().siblings().eq(2).html(teacher.tname);
			$this.parent().siblings().eq(3).html(tdeptname+"<span style='display:none'>"+teacher.tdept+"</span>");
			$this.parent().siblings().eq(4).html(tpostname+"<span style='display:none'>"+teacher.tpost+"</span>&nbsp;");
			$this.parent().siblings().eq(5).html(tdegreename+"<span style='display:none'>"+teacher.tdegree+"</span>&nbsp;");
			
			$this.hide();//隐藏保存按钮
			$this.prev().show();//显示修改按钮
			
			$prompt.text("");
	    });
	});
	//绑定 删除 操作
	$teachertb.find("input[value='删除']").click(function(){
		$this=$(this);
		var tid=$this.parent().siblings().eq(1).text();
		if(confirm("确定要删除编号“"+tid+"”的教师吗？")==false) return;
		//
		$prompt=$this.siblings().last();
		$prompt.text("正在删除……");
		$.getJSON("../TeacherServlet?mode=del",{tid:tid},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
				$prompt.text("");
        		alert(data.errmsg);
        		return;
        	}
        	//删除成功后，刷新教师列表
			$('#tealistform').submit();
	    });
	});
	//绑定 初始化密码 操作
	$teachertb.find("input[value='初始化密码']").click(function(){
		$this=$(this);
		var tid=$this.parent().siblings().eq(1).text();
		if(confirm("确定要为编号“"+tid+"”的教师初始化密码吗？")==false) return;
		//
		$prompt=$this.siblings().last();
		$prompt.text("正在初始化密码……");
		$.getJSON("../UserServlet?mode=initializepwd",{userid:tid},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
				$prompt.text("");
        		alert(data.errmsg);
        		return;
        	}
        	//删除成功后，提示初始化密码成功！
        	$prompt.text("");
			alert("初始化密码成功！");
	    });
	});
}
