$(document).ready(function(){
	//初始化课题库管理菜单
	$("#sublibmagleft ul").tabs("#sublibmagcontent> div");
	$.include("../js/teacher/subjectLib/SubjectHisMag.js");
	$.include("../js/teacher/subjectLib/SubjectTempMag.js");
});
