<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../../error.jsp"%>
<!--系统参数设置界面-->

<div class="caption">参数管理&nbsp;&nbsp;</div>
<form id="sysarguform" action="../SysarguServlet?mode=gets" method="post" style="margin:1px">
	<input type="submit" value="查询"/>
	<img src="../../images/loading.gif" name="loadingimg" style="display:none">
	<span name="errmsg0" class="flag"></span>
</form>
<table class="mtable2">
	<tr><th>参数编号</th><th>参数名称</th><th>参数类型</th><th>参数值</th><th>备注</th><th>操作</th></tr>
	<tbody name="argutb"></tbody>
</table>