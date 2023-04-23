$(document).ready(function(){
	//引入修改密码js
	$.include("../js/com/modifypwd.js");
	$.include("../js/md5.js");
	//根据code表初始化下拉列表框(需同步，完成)
	$.ajaxSetup({async: false});//改为同步加载下面信息（全局）
	code_getcode_select("xb","#tsex");//性别
	code_getcode_select("zhch","#tpost");//职称
	code_getcode_select("xw","#tdegree");//学位
	$.ajaxSetup({async: true});//改回异步加载（全局）
	
	//取得教师基本信息
	var tid=$("#tid").val();
	getteacherBytid(tid);
    //教师信息form ajax请求
	var options={
		beforeSubmit:  teachervalidate,
		dataType: 'json',
		success:      teacherResponse,
		timeout:   2000,
		error:function(xml,status,e){
			//alert("ajax请求出错");
		}
	};
	$("#teacherform").ajaxForm(options);  
	//绑定“修改密码”按钮
	$("#teacherform input[value='修改密码']").click(function(){
		modifypwd(this,tid);//modifypwd.js
	});
});
//取得教师基本信息
function getteacherBytid(tid){
	$.getJSON("../TeacherServlet?mode=get",{tid:tid},function call(data){   
		if(data==null){
       		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
            return;
       	}
		if(data.errmsg!=""){
       		alert(data.errmsg);
       		return;
       	}
       	//获得教师基本信息，并将教师的除编号姓名之外的其他信息填到教师个人信息界面
       	var teacher=eval(data.result);
       	$("#teacherform [name='tdept']").val(teacher.tdeptname);
       	$("#teacherform [name='tsex']").val(teacher.tsex);
       	$("#teacherform [name='tpost']").val(teacher.tpost);
       	$("#teacherform [name='tdegree']").val(teacher.tdegree);
       	$("#teacherform [name='studydirect']").val(teacher.studydirect);
       	$("#teacherform [name='email']").val(teacher.email);
       	$("#teacherform [name='telephone']").val(teacher.telephone);
    });
}
//教师信息有效性验证
function teachervalidate(){
	//验证邮箱有效性
	$email=$("#teacherform [name='email']");
	var myReg = /^[_a-z0-9]+@([_a-z0-9]+\.)+[a-z0-9]{2,3}$/;
	if($email.val()!='' && !myReg.test($email.val())) {
		alert("邮箱格式错误，请重新输入！");
		$email.focus();
		return false;
	}
	//验证手机号有效性
	$loading=$("#teacherform img[name='loading']");
	$loading.show();
}
//ajax请求成功后的响应
function teacherResponse(data){
	if(data==null){
   		alert("请求异常！请与系统管理员联系，确定服务器是否运行正常！1");
        return;
   	}
	$loading=$("#teacherform img[name='loading']");
	$errmsg=$("#teacherform [name='errmsg']");
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
