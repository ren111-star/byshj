<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%
	String userid=request.getParameter("userid");
%>
<div id="divmodifypwd" class="win">
	<div  class="titlebar">
		<span class="title">修改个人密码</span>
		<span class="close" onclick="hideDialog('#divmodifypwd')" title="关闭">X</span>
	</div>  
	<table class="mtable1" align="center">
		<tr>
			<td width="40%">用户名</td>
			<td><input name="userid" type="text" class="text" value="<%=userid%>" readonly/></td>
		</tr>
		<tr>
			<td width="40%">原密码</td>
			<td><input name="oldpwd" type="password" class="text"/>
			</td>
		</tr>
		<tr>
			<td width="40%">新密码</td>
			<td><input name="newpwd" type="password" class="text"/>
			</td>
		</tr>
		<tr>
			<td width="40%">确认密码</td>
			<td><input name="confirmpwd" type="password" class="text"></td>
		</tr>
		<tr>
			<td colspan=2 align="center">
				<br>
				<input type="button" value="提交"/>&nbsp;&nbsp;<input type="button" value="取消" onclick="hideDialog('#divmodifypwd')"/>
			</td>
		</tr>
		<tr><td colspan=2><span class="flag">密码是由英文字母和数字组成的不少于6位的字符串</span></td></tr>
	</table>
</div>
