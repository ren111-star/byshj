<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../../error.jsp"%>
<!--历史课题管理界面-->

<div class="caption">历史课题管理&nbsp;&nbsp;</div>
<form id="subhislistform" action="../SubjectLibServlet?mode=getshis" method="post" style="margin:1px">
	使用年份<input type="text" name="usedyear"/>&nbsp;&nbsp;
	课题名<input type="text" name="subname"/>&nbsp;&nbsp;
	<input type="hidden" name="tutorid" value="${userid}"/>
       <input type="submit" value="查询"/>
	<img alt="" src="../../images/loading.gif" name="loadingimg" style="display:none"><span name="errmsg0" class="flag"></span>
</form>
<table class="mtable2">
	<tr><th>序号</th><th>使用年份</th><th>课题名</th><th>学号</th><th>姓名</th><th>操作</th></tr>
	<tbody name="tbody"></tbody>
</table>
