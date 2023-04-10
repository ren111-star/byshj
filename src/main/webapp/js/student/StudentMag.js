$(document).ready(function(){
	//导航tab
	$("#navigation ul").tabs("#content> div");
	//
	$("input[type='button'],input[type='reset'],input[type='submit']").addClass("btn");
	
	//若为未选题状态，则初始化可选课题列表
	if($("#unpicksubjectdiv")!=undefined) {
		$.include("../js/student/UnpickedSubject.js");
	}
	if($("#pickingsubjectdiv")!=undefined) {
		$.include("../js/student/PickingSubject.js");
	}
	if($("#pickedsubjectdiv")!=undefined) {
		$.include("../js/student/PickedSubject.js");
	}
	//初始化个人信息界面
	$.include("../js/student/StuPersonalInfo.js");
	//初始化周总结界面
	$.include("../js/student/MyWeekSum.js");
	//公共异常处理
	$.ajaxSetup({
        error:function(XMLHttpRequest,textStatus, errorThrown){
        	 alert("请求异常！0["+textStatus+"]["+errorThrown+"]");
            return false;
        }
    });
});

