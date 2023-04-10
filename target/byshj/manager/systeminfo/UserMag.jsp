<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../../error.jsp"%>
<script src="../../js/manager/systeminfo/UserMag.js" type="text/javascript"></script>
<div id="usermagui">
	<div class="caption">用户管理</div>
	<form name="userform" action="../UserServlet?mode=getSysusers" method="post" style="margin:1px">
		用户id&nbsp;&nbsp;<input type="text" class="text" style="width:160px" name="userid"/>&nbsp;&nbsp;
	       <input type="submit" value="查询"/>
		<img src="../../images/loading.gif" name="loadingimg" style="display:none">
		<span name="errmsg0" class="flag"></span>
	</form>
	<table name="usertable" class="mtable2">
		<tr><th>序号</th><th>用户类型</th><th>用户id</th><th>真实姓名</th><th>操作</th></tr>
		<tbody name="usertb"></tbody>
		<tr><td style="width:30px"><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td style="width:80px"><br><select name="usertype"><option value="管理员">管理员</option><option value="其他">其他</option></select></td>
			<td style="width:160px"><br><input type="text" name="userid" class="text"/></td>
			<td style="width:80px"><br><input type="text" name="username" class="text"/></td>
			<td><br><input type="button" name="add" value="增加"/>&nbsp;&nbsp;<span name="errmsg" class="flag"></span></td>
		</tr>
	</table>
</div>
