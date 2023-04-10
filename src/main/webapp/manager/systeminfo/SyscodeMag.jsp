<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../../error.jsp"%>
<!--系统代码管理界面-->

<div class="caption">代码管理&nbsp;&nbsp;</div>
<form id="syscodeform" action="../SyscodeServlet?mode=gets" method="post" style="margin:1px">
	代码名称&nbsp;&nbsp;<input type="text" class="text" style="width:160px" name="codename"/>&nbsp;&nbsp;
       <input type="submit" value="查询"/>
	<img alt="" src="../../images/loading.gif" name="loadingimg" style="display:none">
	<span name="errmsg0" class="flag"></span>
</form>
<table class="mtable2">
	<tr><th>序号</th><th>代码编号</th><th>代码名称</th><th>代码值</th><th>代码内容</th><th>操作</th></tr>
	<tbody name="codetb"></tbody>
	<tr><td style="width:30px"><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td style="width:80px"><br><input type="text" name="codeno" class="text"/></td>
		<td style="width:160px"><br><input type="text" name="codename" class="text"/></td>
		<td style="width:80px"><br><input type="text" name="codevalue" class="text"/></td>
		<td style="width:200px"><br><input type="text" name="codecontent" class="text"/></td>
		<td><br><input type="button" name="add" value="增加"/>&nbsp;&nbsp;<span name="errmsg" class="flag"></span></td>
	</tr>
</table>