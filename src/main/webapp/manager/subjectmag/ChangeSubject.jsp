<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../../error.jsp"%>
<!-- 毕业设计文档管理 -->
<form name="changesubjectform" action="../StusubServlet?mode=changeTutorForStu" method="post" style="margin:5px">
	学号<input type="text" name="stuid" value="" class="text" style="width:120px;"/>&nbsp;&nbsp;
	新教师编号<input type="text" name="tutorid" value="" class="text" style="width:120px;"/>&nbsp;&nbsp;
	<input type="submit" value="变换导师"/>&nbsp;&nbsp;
	<img alt="" src="../../images/loading.gif" name="loadingimg" style="display:none">
	<span name="errmsg0" class="flag"></span>
</form>
