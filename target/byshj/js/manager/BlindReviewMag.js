$(document).ready(function(){
	$("#blindreviewmagdiv >ul").tabs("#blindreviewmagpanes >div");
	//增加课题盲审管理
	$.include('../js/manager/SubjectReviewMag.js');
	//增加论文盲审管理
	$.include('../js/manager/PaperReviewMag.js');
});
