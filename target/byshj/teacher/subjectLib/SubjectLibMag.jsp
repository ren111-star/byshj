<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!--课题库管理，包括历史库、暂存库、当前库-->
<div id="sublibmagleft" class="menu">
	<div class="title">课题库管理</div><br>
	<ul>
		<li><a href="#">历史课题</a></li>
		<li><a href="#">暂存课题</a></li>
	</ul>
</div>
<div id="sublibmagcontent" class="rightcontent">
	<div id="subhismagui" style="display:none">
		<jsp:include flush="true" page="SubjectHisMag.jsp"/>
	</div>
	<div id="subtmpmagui" style="display:none">
		<jsp:include flush="true" page="SubjectTempMag.jsp"/>
	</div>
</div>