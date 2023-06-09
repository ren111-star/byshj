$(document).ready(function(){
	//引入修改密码js
	$.include("../js/com/modifypwd.js");
	$.include("../js/md5.js");
	//根据code表初始化下拉列表框(需同步，完成)
	$.ajaxSetup({async: false});//改为同步加载下面信息（全局）
	code_getcode_select("xb","#studentform select[name='ssex']");//性别
	$.ajaxSetup({async: true});//改回异步加载（全局）
	
	//取得教师基本信息
	var stuid=$("#stuid").val();
	getstudentByid(stuid);
    //教师信息form ajax请求
	var options={
		beforeSubmit:  studentvalidate,
		dataType: 'json',
		success:      studentResponse,
		timeout:   2000,
		error:function(xml,status,e){
			//alert("ajax请求出错");
		}
	};
	$("#studentform").ajaxForm(options);  
	//绑定“修改密码”按钮
	$("#studentform input[value='修改密码']").click(function(){
		modifypwd(this,stuid);//modifypwd.js
	});
});
//取得教师基本信息
function getstudentByid(stuid){
	$.getJSON("../StudentServlet?mode=get",{stuid:stuid},function call(data){   
		if(data==null){
       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
            return;
       	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		return;
       	}
       	//获得教师基本信息，并将教师的除编号姓名之外的其他信息填到教师个人信息界面
       	var student=eval(data.result);
       	$("#studentform [name='classname']").val(student.classname);
       	$("#studentform [name='ssex']").val(student.ssex);
       	$("#studentform [name='email']").val(student.email);
       	$("#studentform [name='telphone']").val(student.telphone);
    });
}
//教师信息有效性验证
function studentvalidate(){
	//验证邮箱有效性
	$email=$("#studentform [name='email']");
	var myReg = /^[_a-z0-9]+@([_a-z0-9]+\.)+[a-z0-9]{2,3}$/;
	if($email.val()!='' && !myReg.test($email.val())) {
		alert("邮箱格式错误，请重新输入！");
		$email.focus();
		return false;
	}
	//验证手机号有效性
	//……
	$loading=$("#studentform img[name='loading']");
	$loading.show();
}
//ajax请求成功后的响应
function studentResponse(data){
	$loading=$("#studentform img[name='loading']");
	$errmsg=$("#studentform [name='errmsg']");
	if(data==null){
   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
   		$loading.hide();
        return;
   	}
	if(data.errmsg!=""){
		//提示出错
		$loading.hide();
		$errmsg.text(data.errmsg);
	}else{
		//提示保存成功
		$loading.hide();
		$errmsg.text("保存成功！");
	}
}
