$(document).ready(function(){
	/*绑定user列表userform  查询*/
	$("#usermagui form[name='userform']").ajaxForm({beforeSubmit:uservalidate,dataType:'json',success:userResponse});
	$("#usermagui form[name='userform']").submit();
	/*按钮单击绑定出现在ready函数中才有用*/
	$("#usermagui input[name='add']").click(function(){
		var $errmsg=$("#usermagui span[name='errmsg']");//查询错误提示
		var $usertype=$(this).parent().siblings().eq(1).find("select");
		var $userid=$(this).parent().siblings().eq(2).find("input");
		var $username=$(this).parent().siblings().eq(3).find("input");
		/*有效性验证*/
		if($usertype.val()==""){
			$errmsg.text("用户类型不能为空！");
			$usertype.focus();
			return;
		}
		if($userid.val()==""){
			$errmsg.text("用户id不能为空！");
			$userid.focus();
			return;
		}
		if($username.val()==""){
			$errmsg.text("用户真实姓名不能为空！");
			$username.focus();
			return;
		}
		/*组装结果*/
		var user={usertype:$usertype.val(),userid:$userid.val(),username:$username.val()};
		var userjson=$.toJSON(user);
		/**/
		$errmsg.text("正在增加"+$userid.val()+"……");
		$.getJSON("../UserServlet?mode=edt&flag=1",{user:userjson},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
	       		alert(data.errmsg);
	       		$errmsg.text("");
	       		return;
	       	}
	       	//增加成功后，刷新代码列表
	       	$errmsg.text("");
	       	//清空控件值  教研室select中值保持不变
	       	$usertype.val("");
			$userid.val("");
			$username.val("");
			
			$("#usermagui form[name='userform']").submit();
	    });
	});
});


/*用户列表 查询  有效性验证 响应*/
function uservalidate(){
	$("#usermagui span[name='errmsg0']").text("查询进行中，请稍后……");;
	$("#usermagui img[name='loadingimg']").show();
	$("#usermagui [name='usertb']").empty();
}
//响应
function userResponse(data){
	if(data==null){
   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
        return;
   	}
	$usertb=$("#usermagui [name='usertb']");
	$errmsg=$("#usermagui span[name='errmsg0']");
	$loadmsg=$("#usermagui img[name='loadingimg']");
	if(data.errmsg!=""){
		$errmsg.text("错误："+data.errmsg);
		$loadmsg.hide();
		$usertb.empty();
		return;
	}
	if(data.result==""){
		$errmsg.text("查询结果数：0");
		$usertb.empty();
		$loadmsg.hide();
		return;
	}
	//显示查询结果
	
	usersarr=eval(data.result);
	$errmsg.text("查询结果数："+usersarr.length);	
	$usertb.empty();
	$.each(usersarr,function(index,item){
		var xh=index+1;
		$usertb.append("<tr>"+
							"<td>"+xh+"</td>"+
							"<td>"+item.usertype+"</td>"+
							"<td>"+item.userid+"</td>"+
							"<td>"+item.username+"</td>"+
							"<td><input type='hidden' value='"+item.userid+"'>"+
							    "<input type='button' value='修改' class='btn'/>"+
								"<input type='button' value='保存' class='btn' style='display:none'/>&nbsp;&nbsp;"+
							    "<input type='button' value='删除' class='btn'/>&nbsp;"+
							    "<input type='button' value='初始化密码' class='btn'/>&nbsp;<span class='flag'><span class='flag'></span>"+
							"</td>"+
						  "</tr>");
	});
	$usertb.tbhighligt();//高亮显示当前行
	$loadmsg.hide();
	//绑定 修改 操作
	$usertb.find("input[value='修改']").click(function(){
		$this=$(this);
		$thissiblings=$this.parent().siblings();
		//设置当前行控件可修改
		var usertype=$thissiblings.eq(1).text();
		$thissiblings.eq(1).html("<select>"+$("#usermagui select[name='usertype']").html()+"</select>");
		$thissiblings.eq(1).find("select").val(usertype);
		var username=$thissiblings.eq(3).text();
		$thissiblings.eq(3).html("<input type='text' class='text' value='"+username+"'/>");
				
		$this.hide();//隐藏修改按钮
		$this.next().show();//显示保存按钮
	});
	//绑定 保存 操作
	$usertb.find("input[value='保存']").click(function(){
		$this=$(this);
		$thissiblings=$this.parent().siblings();
		var usertype=$thissiblings.eq(1).find("select").val();
		var userid=$thissiblings.eq(2).text();
		var username=$thissiblings.eq(3).find("input").val();
			
		var user={usertype:usertype,userid:userid,username:username};
		var userjson=$.toJSON(user);
		//
		$prompt=$this.siblings().last();
		$prompt.text("正在保存……");
		$.getJSON("../UserServlet?mode=edt&flag=2",{user:userjson},function call(data){ 
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
			$thissiblings.eq(1).html(user.usertype);
			$thissiblings.eq(3).html(user.username);
			
			$this.hide();//隐藏保存按钮
			$this.prev().show();//显示修改按钮
			
			$prompt.text("");
	    });
	});
	//绑定 删除 操作
	$usertb.find("input[value='删除']").click(function(){
		$this=$(this);
		var userid=$this.parent().siblings().eq(2).text();
		if(confirm("确定要删除用户“"+userid+"”？")==false) return;
		//
		$prompt=$this.siblings().last();
		$prompt.text("正在删除……");
		$.getJSON("../UserServlet?mode=del",{userid:userid},function call(data){ 
			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
			if(data.errmsg!=""){
				$prompt.text("");
        		alert(data.errmsg);
        		return;
        	}
        	//删除成功后，刷新代码列表
			$("#usermagui form[name='userform']").submit();
	    });
	});
	//绑定 初始化密码 操作
	$usertb.find("input[value='初始化密码']").click(function(){
		$this=$(this);
		var userid=$this.parent().siblings().eq(2).text();
		if(confirm("确定要为编号“"+userid+"”的初始化密码吗？")==false) return;
		//
		$prompt=$this.siblings().last();
		$prompt.text("正在初始化密码……");
		$.getJSON("../UserServlet?mode=initializepwd",{userid:userid},function call(data){ 
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
