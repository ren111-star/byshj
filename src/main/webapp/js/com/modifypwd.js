/*
    修改个人密码js（除管理员外的其他类型用户）
*/
//vid为修改密码按钮id
function modifypwd(vid,userid){
	var $modifypwdwin=$("#divmodifypwd");
	//设置窗口样式
    var offset=$(vid).offset();   
    viewtop=offset.top+28;   
    viewleft=+(offset.left)-100;  
    $modifypwdwin.css("width","180px");
    $modifypwdwin.css("height","230px");
    $modifypwdwin.css("top",viewtop+"px");
    $modifypwdwin.css("left",viewleft+"px");
   	//显示窗口
   	$modifypwdwin.show();
   	//绑定提交操作
   	$modifypwdwin.find("input[value='提交']").click(function(){
   		$oldpwd=$modifypwdwin.find("input[name='oldpwd']");
   		$newpwd=$modifypwdwin.find("input[name='newpwd']");
   		$confirmpwd=$modifypwdwin.find("input[name='confirmpwd']");
   		
   		var oldpwd=$oldpwd.val();
   		var newpwd=$newpwd.val();
   		var confirmpwd=$confirmpwd.val();
   		
   		if(oldpwd==""){
   			alert("原密码不允许为空");
   			$oldpwd.focus();
   			return;
   		}
   		if(newpwd==""){
   			alert("新密码不允许为空");
   			$newpwd.focus();
   			return;
   		}
   		//密码规则验证
   		if(newpwd.length<6 || newpwd.length>20){
   			alert("密码位数为6~20，请重新输入！");
   			$newpwd.focus();
   			return;
   		}
   		if(confirmpwd==""){
   			alert("确认密码不允许为空");
   			$confirmpwd.focus();
   			return;
   		}
   		if(newpwd!=confirmpwd){
   			alert("新密码和确认密码不一致，请重新输入！");
   			$confirmpwd.focus();
   			return;
   		}
   		//提交到服务器修改
   		$.getJSON("../UserServlet?mode=modifypwd",{userid:userid,oldpwd:hex_md5(oldpwd),newpwd:hex_md5(newpwd)},function call(data){   
   			if(data==null){
		   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
		        return;
		   	}
   			if(data.errmsg!=""){
	       		alert(data.errmsg);
	       		return;
	       	}
	       	//提示修改成功
	       	alert("密码修改成功！");
	       	$modifypwdwin.find("input[value='取消']").trigger("click");
	       	$oldpwd.val("");
	       	$newpwd.val("");
	       	$confirmpwd.val("");
	    });
   	});
}