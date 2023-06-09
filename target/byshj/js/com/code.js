//获得代码值
function code_getcode_select(vcodeno,vid){
	$.getJSON("../SyscodeServlet?mode=getcodeByno",{codeno:vcodeno},function call(data){   
		if(data==null){
	   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	        return;
	   	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		return;
       	}
       	var codearray=eval(data.result);
       	$(vid).prepend("<option value=''>[请选择]</option>");
       	$.each(codearray,function(index,item){
       		$(vid).append("<option value='"+item.codevalue+"'>"+item.codecontent+"</option>");
       	});
      });
}
//取得所有专业check
function code_getspeciality(vid){
	$.getJSON("../SpecialityServlet?mode=gets",null,function call(data){   
		if(data==null){
	   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	        return;
	   	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		return;
       	}
       	var specarray=eval(data.result);
       	$("#speccount").val(specarray.length);
       	$.each(specarray,function(index,item){
       		$(vid).append("<input value='"+item.specid+"' type='checkbox' name='spec_"+index+"'/>"+item.specname+"<br>");
       	});
     });
}
//取得所有课题方向check
function code_getsubjectdirection(vid){
	$.getJSON("../SyscodeServlet?mode=getcodeByno",{codeno:"ktfx"},function call(data){   
		if(data==null){
	   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	        return;
	   	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		return;
       	}
       	var directionarray=eval(data.result);
       	$.each(directionarray,function(index,item){
       		$(vid).append("<input value='"+item.codevalue+"' type='checkbox' name='direction_"+index+"'/>&nbsp;<label>"+item.codecontent+"</label>&nbsp;");
       	});
     });
}
//取得所有专业select
function code_getspeciality_select(vid){
	$.getJSON("../SpecialityServlet?mode=gets",null,function call(data){   
		if(data==null){
	   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	        return;
	   	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		return;
       	}
       	var specarray=eval(data.result);
       	  $(vid).prepend("<option value=''>[请选择]</option>");
       	$.each(specarray,function(index,item){
       		$(vid).append("<option value='"+item.specid+"'>"+item.specname+"</option>");
       	});
     });
}
//取得专业中的所有班级select
function code_getclass_select(specid,vid){
	$.getJSON("../ClassServlet?mode=gets",{specid:specid},function call(data){   
		if(data==null){
	   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
	        return;
	   	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		return;
       	}
       	var classarr=eval(data.result);
        $(vid).prepend("<option value=''>[请选择]</option>");
       	$.each(classarr,function(index,item){
       		$(vid).append("<option value='"+item.classname+"'>"+item.classname+"</option>");
       	});
     });
}