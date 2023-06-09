$(document).ready(function(){
	//初始化专业select
	var $specsel=$("#stulistform [name='speciality']");
	code_getspeciality_select($specsel);//专业列表
	//级联班级列表
	$specsel.change(function(){
		var specid=$(this).val();
		if(specid=="") return;
		$("#studentmagui select[name='classname']").empty();
		code_getclass_select(specid,"#studentmagui select[name='classname']");//班级列表
	});
	$("#studentmagui select[name='classname']").eq(0).change(function(){
		$("#studentmagui select[name='classname']").eq(1).val($(this).val());
	});
	
	/*绑定学生列表stulistform  查询*/
	var options={
		beforeSubmit:  stuvalidate,
		dataType: 'json',
		success:      stuResponse
	};
	$('#stulistform').ajaxForm(options);
	
	//绑定增加学生 操作
	$("#studentmagui input[name='add']").click(function(){
		$this=$(this);
		$errmsg=$("#studentmagui [name='errmsg']");
		var stuid=$this.parent().siblings().eq(1).find("input").val();
		var sname=$this.parent().siblings().eq(2).find("input").val();
		var classname=$this.parent().siblings().eq(3).find("select").val();
		var email=$this.parent().siblings().eq(4).find("input").val();
		var telphone=$this.parent().siblings().eq(5).find("input").val();
		//有效性验证
		if(stuid==""||stuid.length<10){
			if(stuid=="") $errmsg.text("学号不能为空！");
			if(stuid!=""&&stuid.length<10) {
				$errmsg.text("学号必须大于或等于10位字符！");
				$this.parent().siblings().eq(1).find("input").addClass("texthighlight");
				$this.parent().siblings().eq(1).find("input").change(function(){
					$(this).removeClass("texthighlight");
				});
			}
			$this.parent().siblings().eq(1).find("input").focus();
			return;
		}
		if(sname==""){
			$errmsg.text("姓名不能为空！");
			$this.parent().siblings().eq(2).find("input").focus();
			return;
		}
		if(classname==""){
			$errmsg.text("班级不能为空！");
			$this.parent().siblings().eq(3).find("select").focus();
			return;
		}
		if(email!=""){
			var emailvalidate = /^[_a-z0-9]+@([_a-z0-9]+\.)+[a-z0-9]{2,3}$/;//邮箱验证正则表达式
			if(!emailvalidate.test(email)) {
				$errmsg.text("邮箱无效，请重新输入！");
				$this.parent().siblings().eq(4).find("input").focus();
				return;
			}
		}
		if(telphone!=""){
			var telphonevalidate=/(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^0{0,1}13[0-9]{9}$)/;
			if(!telphonevalidate.test(telphone)) {
				$errmsg.text("电话号码无效，请重新输入！");
				$this.parent().siblings().eq(5).find("input").focus();
				return;
			}
		}
		
		
		//组装结果
		var student={stuid:stuid,sname:sname,classname:classname,email:email,telphone:telphone};
		var studentjson=$.toJSON(student);
		//
		$errmsg.text("正在增加"+stuid+"……");
		$.getJSON("../StudentServlet?mode=edt&flag=1",{student:studentjson},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
        		alert(data.errmsg);
        		$errmsg.text("");
        		return;
        	}
        	//增加成功后，刷新学生列表
        	$errmsg.text("");
        	//清空控件值  班级select中值保持不变
        	$this.parent().siblings().eq(1).find("input").val("");
			$this.parent().siblings().eq(2).find("input").val("");
			$this.parent().siblings().eq(4).find("input").val("");
			$this.parent().siblings().eq(5).find("input").val("");
			
        	//设置班级，刷新增加学生所在班级
        	$("#studentmagui select[name='classname']").eq(0).val(student.classname);
			$('#stulistform').submit();
	    });
	});
	
	/*导入excel学生信息文件 表单 绑定*/
	var optionsimport={
		target : '#upMessage', 
		url : '../ExcelServlet?mode=importstudents',
		beforeSubmit:  function(){
			var filename = $("#importstudiv input[type='file']").val();   
		    var m=parseInt(filename.toString().lastIndexOf("."))+1;   
		    var extVal=filename.toString().substr(m);   
		    if(extVal!="xls") {   
		        alert('文件类型必须为Excel文件！');   
		        return false;   
		    }   
		    $("#importstudiv [name='loadingimg']").show();   
		    $("#importstudiv [name='upmessage']").html('文件导入中，请稍候... ...');   
		    return true;
		},
		dataType: 'json',
		success:  function(data){
			$upmessage=$("#importstudiv [name='upmessage']");
			if(data.errmsg!=""){
				$("#importstudiv [name='loadingimg']").hide();
				$upmessage.html("导入文件错误："+data.errmsg);
				return;
			}
			$("#importstudiv [name='loadingimg']").hide();
			$upmessage.html("导入成功！");
		},
		resetForm:true
	};
	$('#importstudiv form').ajaxForm(optionsimport);
	//绑定 导入学生 操作
	$("#stulistform input[value='导入学生']").click(function(){
		$win=$("#importstudiv");
		var offset=$(this).offset();   
        var top=offset.top;   
        var left=+(offset.left)-400;  
        $win.css("width","400px");
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
/*学生列表 查询  有效性验证 响应*/
function stuvalidate(){
	var specid=$("#stulistform [name='speciality']").val();
	if(specid==""){
		alert("请选择专业！");
		$("#stulistform [name='speciality']").focus();
		return false;
	}
	
	$("#studentmagui [name='errmsg0']").text("查询进行中，请稍后……");;
	$("#studentmagui [name='loadingimg']").show();
	$("#studentmagui [name='studenttb']").empty();
}
//响应
function stuResponse(data){
	$studenttb=$("#studentmagui [name='studenttb']");
	$errmsg=$("#studentmagui [name='errmsg0']");
	$loadmsg=$("#studentmagui [name='loadingimg']");
	if(data==null){
   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
   		$loadmsg.hide();
		$studenttb.empty();
        return;
   	}
	if(data.errmsg!=""){
		$errmsg.text("错误："+data.errmsg);
		$loadmsg.hide();
		$studenttb.empty();
		return;
	}
	if(data.result==""){
		$errmsg.text("查询结果数：0");
		$studenttb.empty();
		$loadmsg.hide();
		return;
	}
	//显示查询结果
	studentsarr=eval(data.result);
	$errmsg.text("查询结果数："+studentsarr.length);	
	$studenttb.empty();
	$.each(studentsarr,function(index,item){
		var email=item.email;
		if(email==undefined) email="";
		var telphone=item.telphone;
		if(telphone==undefined) telphone="";
		var xh=index+1;
		$studenttb.append("<tr>"+
							"<td>"+xh+"</td>"+
							"<td>"+item.stuid+"</td>"+
							"<td>"+item.sname+"</td>"+
							"<td>"+item.classname+"</td>"+
							"<td><span>"+email+"</span>&nbsp;</td>"+
							"<td><span>"+telphone+"</span>&nbsp;</td>"+
							"<td><input type='button' value='修改' class='btn'/>"+
								"<input type='button' value='保存' class='btn' style='display:none'/>&nbsp;"+
							    "<input type='button' value='删除' class='btn'/>&nbsp;"+
							    "<input type='button' value='初始化密码' class='btn'/>&nbsp;<span class='flag'></span>"+
							"</td>"+
						  "</tr>");
	});
	$studenttb.tbhighligt();//高亮显示当前行
	$loadmsg.hide();
	//绑定 修改 操作
	$studenttb.find("input[value='修改']").click(function(){
		$this=$(this);
		//设置当前行控件可修改
		var sname=$this.parent().siblings().eq(2).text();
		$this.parent().siblings().eq(2).html("<input type='text' class='text' value='"+sname+"'/>");
		
		var classname=$this.parent().siblings().eq(3).text();
		$this.parent().siblings().eq(3).html("<select>"+$("#studentmagui select[name='classname']").eq(0).html()+"</select>");
		$this.parent().siblings().eq(3).find("select").val(classname);
		
		var email=$this.parent().siblings().eq(4).find("span").text();
		$this.parent().siblings().eq(4).html("<input type='text' class='text' value='"+email+"'/>");
		
		var telphone=$this.parent().siblings().eq(5).find("span").text();
		$this.parent().siblings().eq(5).html("<input type='text' class='text' value='"+telphone+"'/>");
				
		$this.hide();//隐藏修改按钮
		$this.next().show();//显示保存按钮
	});
	//绑定 保存 操作
	$studenttb.find("input[value='保存']").click(function(){
		$this=$(this);
		var stuid=$this.parent().siblings().eq(1).text();
		var sname=$this.parent().siblings().eq(2).find("input").val();
		var classname=$this.parent().siblings().eq(3).find("select").val();
		var email=$this.parent().siblings().eq(4).find("input").val();
		var telphone=$this.parent().siblings().eq(5).find("input").val();
		
		var student={stuid:stuid,sname:sname,classname:classname,email:email,telphone:telphone};
		var studentjson=$.toJSON(student);
		//
		$prompt=$this.siblings().last();
		$prompt.text("正在保存……");
		$.getJSON("../StudentServlet?mode=edt&flag=2",{student:studentjson},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
        		alert(data.errmsg);
        		return;
        	}
        	//保存成功后，恢复当前行各控件初始状态
			$this.parent().siblings().eq(2).html(student.sname);
			$this.parent().siblings().eq(3).html(student.classname);
			$this.parent().siblings().eq(4).html("<span>"+student.email+"</span>&nbsp;");
			$this.parent().siblings().eq(5).html("<span>"+student.telphone+"</span>&nbsp;");
			
			$this.hide();//隐藏保存按钮
			$this.prev().show();//显示修改按钮
			
			$prompt.text("");
	    });
	});
	//绑定 删除 操作
	$studenttb.find("input[value='删除']").click(function(){
		$this=$(this);
		var stuid=$this.parent().siblings().eq(1).text();
		if(confirm("确定要删除学号为：“"+stuid+"”的学生吗？")==false) return;
		//
		$prompt=$this.siblings().last();
		$prompt.text("正在删除……");
		$.getJSON("../StudentServlet?mode=del",{stuid:stuid},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
				$prompt.text("");
        		alert(data.errmsg);
        		return;
        	}
        	//删除成功后，刷新学生列表
			$('#stulistform').submit();
	    });
	});
	//绑定 初始化密码 操作
	$studenttb.find("input[value='初始化密码']").click(function(){
		$this=$(this);
		var stuid=$this.parent().siblings().eq(1).text();
		if(confirm("确定要为学号“"+stuid+"”的学生初始化密码吗？")==false) return;
		//
		$prompt=$this.siblings().last();
		$prompt.text("正在初始化密码……");
		$.getJSON("../UserServlet?mode=initializepwd",{userid:stuid},function call(data){ 
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
