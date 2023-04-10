<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../../error.jsp"%>
<!--暂存课题管理界面-->

<div class="caption">暂存课题管理&nbsp;&nbsp;</div>
<form id="subtemplistform" action="../SubjectLibServlet?mode=getstemp" method="post" style="margin:1px">
	<input type="hidden" name="tutorid" value="${userid}"/>
       <input type="submit" value="查询"/>
	<img src="../../images/loading.gif" name="loadingimg" style="display:none"><span name="errmsg0" class="flag"></span>
	<div style="float:right"><input type="button" onclick="subjecttemp_showwin('')" value="增加新课题"/>&nbsp;&nbsp;&nbsp;&nbsp;</div>
</form>
<table class="mtable2">
	<tr><th>序号</th><th>课题名</th><th>备注</th><th>操作</th></tr>
	<tbody name="tbody"></tbody>
</table>
<div id="subjecttempedt" class="win">
	<jsp:include page="SubjectTempEdt.jsp"  flush="true"/>
</div>