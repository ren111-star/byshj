$(document).ready(function(){
	//导航tab
	$("#navigation ul").tabs("#content> div");
	//
	$("input[type='button'],input[type='reset'],input[type='submit']").addClass("btn");
	//包含其他js文件
	$.include('../js/manager/SubjectMag.js');
	$.include('../js/manager/SystemMag.js');
	$.include('../js/manager/BlindReviewMag.js');
	//$.include('../js/manager/InformMag.js');
	
	/*修改密码需引入文件*/
	$.include("../js/com/modifypwd.js");
	$.include("../js/md5.js");
	/**/
	//公共异常处理
	$.ajaxSetup({
        error:function(XMLHttpRequest, textStatus, errorThrown){
        	 alert("请求异常！0["+textStatus+":"+errorThrown+"]");
            return false;
        }
    });
});