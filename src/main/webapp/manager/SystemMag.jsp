<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../error.jsp"%>

<!--系统管理包括：学生、教师、专业班级、用户的管理及代码维护  参数设置  角色设置(只对教师设置角色，包含在教师信息维护中)功能-->
<div id="systemmagleft" class="menu">
	<div class="title">系统管理功能</div><br>
	<ul>
		<li><a href="#">学生管理</a></li>
		<li><a href="#">教师管理</a></li>
		<li><a href="#">专业班级管理</a></li>
		<li><a href="#">用户管理</a></li>
		<li><a href="#">代码管理</a></li>
		<li><a href="#">参数维护</a></li>
	</ul>
</div>
<div id="systemmagcontent" class="rightcontent">
	<div id="studentmagui" style="display:none">
		<jsp:include flush="true" page="systeminfo/StudentBaseInfoMag.jsp"/>
	</div>
	<div id="teachermagui" style="display:none">
		<jsp:include flush="true" page="systeminfo/TeacherBaseInfoMag.jsp"/>
	</div>
	<div style="display:none">
		<jsp:include flush="true" page="systeminfo/SpecClassMag.jsp"/>
	</div>
	<div style="display:none">
		<jsp:include flush="true" page="systeminfo/UserMag.jsp"/>
	</div>
	<div id="syscodemagui" style="display:none">
		<jsp:include flush="true" page="systeminfo/SyscodeMag.jsp"/>
	</div>
	<div id="sysargumagui" style="display:none">
		<jsp:include flush="true" page="systeminfo/SysarguMag.jsp"/>
	</div>
</div>