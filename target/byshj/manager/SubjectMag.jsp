<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../error.jsp"%>
<!--课题管理界面-->
<ul class="tabs">
	<li><a href="#">发布课题</a></li>
	<li><a href="#">选题明细</a></li>
	<li><a href="#">调整课题</a></li>
	<li><a href="#">查看设计文档</a></li>
	<li><a href="#">过程管理</a></li>
</ul>
<div id="subjectmagpanes">
	<div id="subjectsdiv">
		<form id="subjectform" action="../SubjectServlet?mode=getsubsbyspec" method="post" style="margin:5px">
			专业<select name="specid"></select>&nbsp;&nbsp;
			教研室<select name="tdept"></select>&nbsp;&nbsp;
			指导教师<input type="text" class="text" name="tname" style="width:120px;"/>
			课题状态<select name="substatus"></select><input type="submit" value="查询"/>&nbsp;&nbsp;
			<img src="../images/loading.gif" name="loadingimg" style="display:none">
			<span name="errmsg0" class="flag"></span>
		</form>
		<table class="mtable2">
			<tr><th>序号</th><th>所属教研室</th><th>指导教师</th><th>(课题编号)课题名</th><th>课题状态</th><th>选中标志</th><th>操作</th></tr>
			<tbody name="tbsubjects"></tbody>
			<tr>
				<td colspan=6 align="right"><br>
					<input type="button" value="全选">&nbsp;
					<input type="button" value="撤销全选">&nbsp;
					<input type="button" value="发布课题" name="releasesub">
					<img src="../images/loading.gif" name="loadingimg" style="display:none"/>
					<span name="errmsg0" class="flag"></span>
				</td>
			</tr>
		</table>
	</div>
	<div id="studentsdiv" style="display:none;">
		<form id="studentform" action="../SubjectServlet?mode=getstusbyspec" method="post" style="margin:5px">
			专业<select name="specid"></select>&nbsp;&nbsp;
			班级<select name="classname"><option value=""></option></select>&nbsp;&nbsp;
			学生状态<select name="stustatus"></select>&nbsp;&nbsp;
			<input type="submit" value="查询"/>&nbsp;&nbsp;<input type="button" value="导出课题明细表" name="exportstusublist"/>
			<img src="../images/loading.gif" name="loadingimg" style="display:none">
			<span name="errmsg0" class="flag"></span>
		</form>
		<table class="mtable2">
			<tr><th style="width:40px">序号</th><th style="width:40px">学号</th><th style="width:60px">姓名</th><th style="width:80px">专业</th><th style="width:80px">班级</th>
			<th>毕业设计（论文）题目</th><th>题目类型</th><th>题目性质</th><th>题目来源</th>
			<th>指导教师</th><th>职称/学位</th><th>选题状态</th></tr>
			<tbody name="tbstudents"></tbody>
		</table>
	</div>
	<div id="changesubjectsdiv" style="display:none;">
	 	<jsp:include page="subjectmag/ChangeSubject.jsp"  flush="true" />
	</div>
	<!-- 查看设计文档 -->
	<div id="viewdesigndocsdiv" style="display:none;">
		<jsp:include page="subjectmag/DesignDocMag.jsp"  flush="true" />
	</div>
	<!-- 设计过程管理 -->
	<div id="processmagdiv" style="display:none;">
		<jsp:include page="subjectmag/DesignProcessMag.jsp"  flush="true" />
	</div>
</div>
