$(document).ready(function(){
	$("#specclassmagui ul").tabs("#specclassmagui [name='content']> div");
	code_getspeciality_select($("#specclassmagui select[name='specid']"));//专业列表
	/*显示专业列表*/
	getspecs("#specmagui tbody[name='spectb']");
	/*显示班级列表*/
	//选择条件和新增的教研室name相同，取值也级联相同
	$spec=$("#classmagui select[name='specid']");
	$spec.eq(0).change(function(){
		$spec.eq(1).val($(this).val());
	});
	
	/*绑定教师列表tealistform  查询*/
	var options={
		beforeSubmit:  classvalidate,
		dataType: 'json',
		success:      classResponse
	};
	$("#classmagui form[name='classform']").ajaxForm(options);
	//绑定增加班级 操作
	$("#classmagui input[name='add']").click(function(){
		$this=$(this);
		$errmsg=$("#classmagui [name='errmsg']");
		var specid=$this.parent().siblings().eq(1).find("select").val();
		var classname=$this.parent().siblings().eq(2).find("input").val();
		var enrolyear=$this.parent().siblings().eq(3).find("input").val();
		var gradyear=$this.parent().siblings().eq(4).find("input").val();
		//有效性验证
		if(specid==""){
			$errmsg.text("专业不能为空，请选择！");
			$this.parent().siblings().eq(1).find("select").focus();
			return;
		}
		if(classname==""){
			$errmsg.text("班级名不能为空！");
			$this.parent().siblings().eq(2).find("input").focus();
			return;
		}
		//组装结果
		var clas={specid:specid,classname:classname,enrolyear:enrolyear,gradyear:gradyear};
		var clasjson=$.toJSON(clas);
		//
		$errmsg.text("正在增加"+classname+"……");
		$.getJSON("../ClassServlet?mode=edt&flag=1",{clas:clasjson},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
        		alert(data.errmsg);
        		$errmsg.text("");
        		return;
        	}
        	//增加成功后，刷新班级列表
        	$errmsg.text("");
        	//清空控件值  专业select中值保持不变
        	$this.parent().siblings().eq(2).find("input").val("");
			$this.parent().siblings().eq(3).find("input").val("");
			$this.parent().siblings().eq(4).find("input").val("");
			
        	//设置专业，刷新增加班级所在专业
        	$("#classmagui select[name='specid']").eq(0).val(specid);
			$("#classmagui form[name='classform']").submit();
	    });
	});
});
function getspecs(vid){
	$spectb=$(vid);
	/*初始化专业列表（表格显示）*/
	$.getJSON("../SpecialityServlet?mode=gets",null,function call(data){
		if(data==null){
	   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	        return;
	   	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		return;
       	}
       	$spectb.empty();
       	var specarray=eval(data.result);
       	$.each(specarray,function(index,item){
       		var specmagtid=item.specmagtid;
       		var specmagtname=item.specmagtname;
       		if(specmagtid==undefined){
       			specmagtid="";
       			specmagtname="";
       		};
       		$spectb.append("<tr><td>"+item.specid+"</td><td>"+item.specname+"</td><td>"+
       					   specmagtid+"</td><td>"+specmagtname+"</td>"+
       					   "<td><input type='button' value=\"修改\" class='btn'/><input type='button' value=\"保存\" style='display:none' class='btn'/>"+
       					   "&nbsp;<span class='flag'><span class='flag'></span></td></tr>");
       	});
       	/*绑定修改操作，只修改专业负责人编号，负责人名自动显示*/
	     $spectb.find("input[value='修改']").click(function(){
			$this=$(this);
			$thissiblings=$this.parent().siblings();
			//设置当前行控件可修改
			var specmagtid=$thissiblings.eq(2).text();
			$thissiblings.eq(2).html("<input type='text' class='text' value='"+specmagtid+"'/>");
					
			$this.hide();//隐藏修改按钮
			$this.next().show();//显示保存按钮
		});
		//绑定 保存 操作
		$spectb.find("input[value='保存']").click(function(){
			$this=$(this);
			$thissiblings=$this.parent().siblings();
			var specid=$thissiblings.eq(0).text();
			var specmagtid=$thissiblings.eq(2).find("input").val();
			//有效性验证
			if(specmagtid.length!=6){
				alert("负责人编号应为6位数字，请重新输入");
				$thissiblings.eq(2).find("input").focus();
				return;
			}
			//
			$prompt=$this.siblings().last();
			$prompt.text("正在保存……");
			$.getJSON("../SpecialityServlet?mode=edtspecmag",{specid:specid,specmagtid:specmagtid},function call(data){ 
				if(data==null){
			   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
			        return;
			   	}
				if(data.errmsg!=""){
	        		$prompt.text("");
	        		alert(data.errmsg);
	        		return;
	        	}
	        	//保存成功后，恢复当前行各控件初始状态
				$thissiblings.eq(2).html(specmagtid);
				//查询教师姓名，并显示
				$.getJSON("../TeacherServlet?mode=get",{tid:specmagtid},function call(data){
					if(data==null){
				   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
				        return;
				   	}
					if(data.errmsg!=""){
		        		$prompt.text("");
		        		alert(data.errmsg);
		        		return;
	        		}
	        		var teacher=eval(data.result);
	        		$thissiblings.eq(3).text(teacher.tname);
				});
				$this.hide();//隐藏保存按钮
				$this.prev().show();//显示修改按钮
				
				$prompt.text("");
		    });
		});
     });
}
function classvalidate(){
	$("#classmagui [name='errmsg0']").text("查询进行中，请稍后……");;
	$("#classmagui [name='loadingimg']").show();
	$("#classmagui [name='classtb']").empty();
}
function classResponse(data){
	$classtb=$("#classmagui [name='classtb']");
	$errmsg=$("#classmagui [name='errmsg0']");
	$loadmsg=$("#classmagui [name='loadingimg']");
	if(data==null){
   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
   		$loadmsg.hide();
		$classtb.empty();
        return;
   	}
	if(data.errmsg!=""){
		$errmsg.text("错误："+data.errmsg);
		$loadmsg.hide();
		$classtb.empty();
		return;
	}
	if(data.result==""){
		$errmsg.text("查询结果数：0");
		$classtb.empty();
		$loadmsg.hide();
		return;
	}
	//显示查询结果
	clasarr=eval(data.result);
	$errmsg.text("查询结果数："+clasarr.length);	
	$classtb.empty();
	$.each(clasarr,function(index,item){
		var enrolyear=item.enrolyear;
		if(enrolyear==undefined) enrolyear="";
		var gradyear=item.gradyear;
		if(gradyear==undefined) gradyear="";
		var xh=index+1;
		$classtb.append("<tr>"+
							"<td>"+xh+"</td>"+
							"<td>"+item.speciality.specname+"</td>"+
							"<td>"+item.classname+"</td>"+
							"<td>"+enrolyear+"</td>"+
							"<td>"+gradyear+"</td>"+
							"<td><input type='hidden' value='"+item.speciality.specid+"'/><input type='hidden' value='"+item.classid+"'/><input type='button' value='修改' class='btn'/>"+
								"<input type='button' value='保存' class='btn' style='display:none'/>&nbsp;&nbsp;"+
							    "<input type='button' value='删除' class='btn'/>&nbsp;<span class='flag'><span class='flag'></span>"+
							"</td>"+
						  "</tr>");
	});
	$classtb.tbhighligt();//高亮显示当前行
	$loadmsg.hide();
	//绑定 修改 操作
	$classtb.find("input[value='修改']").click(function(){
		$this=$(this);
		//设置当前行控件可修改
		var specid=$this.siblings().first().val();
		$this.parent().siblings().eq(1).html("<select>"+$("#classmagui select[name='specid']").eq(0).html()+"</select>");
		$this.parent().siblings().eq(1).find("select").val(specid);
		
		var classname=$this.parent().siblings().eq(2).text();
		$this.parent().siblings().eq(2).html("<input type='text' class='text' value='"+classname+"'/>");
		var enrolyear=$this.parent().siblings().eq(3).text();
		$this.parent().siblings().eq(3).html("<input type='text' class='text' value='"+enrolyear+"'/>");
		var gradyear=$this.parent().siblings().eq(4).text();
		$this.parent().siblings().eq(4).html("<input type='text' class='text' value='"+gradyear+"'/>");
					
		$this.hide();//隐藏修改按钮
		$this.next().show();//显示保存按钮
	});
	//绑定 保存 操作
	$classtb.find("input[value='保存']").click(function(){
		$this=$(this);
		var classid=$this.siblings().eq(1).val();
		var specid=$this.parent().siblings().eq(1).find("select").val();
		var classname=$this.parent().siblings().eq(2).find("input").val();
		var enrolyear=$this.parent().siblings().eq(3).find("input").val();
		var gradyear=$this.parent().siblings().eq(4).find("input").val();
	
		var clas={classid:classid,specid:specid,classname:classname,enrolyear:enrolyear,gradyear:gradyear};
		var clasjson=$.toJSON(clas);
		//
		$prompt=$this.siblings().last();
		$prompt.text("正在保存……");
		
		$.getJSON("../ClassServlet?mode=edt&flag=2",{clas:clasjson},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
        		alert(data.errmsg);
        		return;
        	}
        	//select中被选中的option显示内容
			var specname=$this.parent().siblings().eq(1).find("select option:selected").text();
        	//保存成功后，恢复当前行各控件初始状态
			$this.parent().siblings().eq(1).html(specname);
			$this.parent().siblings().eq(2).html(classname);
			$this.parent().siblings().eq(3).html(enrolyear);
			$this.parent().siblings().eq(4).html(gradyear);
			
			$this.hide();//隐藏保存按钮
			$this.prev().show();//显示修改按钮
			
			$prompt.text("");
	    });
	});
	//绑定 删除 操作
	$classtb.find("input[value='删除']").click(function(){
		$this=$(this);
		var classname=$this.parent().siblings().eq(2).text();
		if(confirm("确定要删除“"+classname+"”吗？")==false) return;
		//
		$prompt=$this.siblings().last();
		$prompt.text("正在删除……");
		$.getJSON("../ClassServlet?mode=del",{classname:classname},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
				$prompt.text("");
        		alert(data.errmsg);
        		return;
        	}
        	//删除成功后，刷新班级列表
			$("#classmagui form[name='classform']").submit();
	    });
	});
}
