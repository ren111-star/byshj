$(document).ready(function(){
	//初始化系统管理菜单
	$("#systemmagleft ul").tabs("#systemmagcontent> div");
	//包含其他js文件
	$.include("../js/manager/systeminfo/StudentBaseInfoMag.js");
	$.include("../js/manager/systeminfo/TeacherBaseInfoMag.js");
	$.include("../js/manager/systeminfo/SyscodeMag.js");
	$.include("../js/manager/systeminfo/SysarguMag.js");
});