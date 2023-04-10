<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../error.jsp"%>
<!--审核管理界面-->
<ul class="tabs">
	<li><a href="#">课题盲审</a></li>
	<li><a href="#">论文盲审</a></li>
</ul>
<div id="blindreviewmagpanes">
    <div id="subjectblindreviewdiv" style="display:none">
		<jsp:include flush="true" page="blindReviewMag/SubjectReview.jsp"/>
	</div>
	<div id="paperblindreviewdiv" style="display:none">
		<jsp:include flush="true" page="blindReviewMag/PaperReview.jsp"/>
	</div>
</div>
