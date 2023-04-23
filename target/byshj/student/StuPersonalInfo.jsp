<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%
	String stuid=request.getParameter("stuid");
	String sname=request.getParameter("sname"); 
%>
	<form id="studentform" action="../StudentServlet?mode=personaledt" method="post">
        <div class="content"> 
	 		<table class="mtable1" style="width:600px" align="center">
	 			<caption>个人信息</caption>
	 			<tr>
	 				<td>学&nbsp;&nbsp;&nbsp;&nbsp;号</td>
	 				<td><input name="stuid" type="text" class="text" value="<%=stuid%>" readonly/></td>
	 				<td>&nbsp;&nbsp;&nbsp;&nbsp;姓&nbsp;&nbsp;&nbsp;&nbsp;名</td>
	 				<td><input name="sname" type="text" class="text"  value="<%=sname%>" readonly/></td>
	 				<td>&nbsp;&nbsp;&nbsp;&nbsp;班&nbsp;&nbsp;&nbsp;&nbsp;级</td>
	 				<td><input name="classname" type="text" class="text" readonly></td>
	 				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;性&nbsp;&nbsp;&nbsp;&nbsp;别</td>
	 				<td>
	 					<select name="ssex"></select>
	 				</td>
	 			</tr>
	 			<tr><td><br></td></tr>
	 			<tr>
	 				
	 			</tr>
	 			<tr>
	 				<td>电子邮箱</td>
	 				<td colspan=3><input name="email" type="text" class="text"/></td>
	 				<td>&nbsp;&nbsp;&nbsp;&nbsp;手&nbsp;&nbsp;&nbsp;&nbsp;机</td>
	 				<td colspan=3><input name="telephone" type="text" class="text"/></td>
	 			</tr>
	 			
	 			<tr><td><br></td></tr>
	 			<tr>
	 				<td colspan=6 align="right">
	 				    <img src="../images/loading.gif" name="loading" style="display:none"></img>
	 				    <span name="errmsg" class="flag"></span>
	 					<input type="submit" value="保存"/>
	 					<input type="button" value="修改密码"/>
	 				</td>
	 			</tr>
	 		</table>
		</div> 
	</form>
	<jsp:include page="../ModifyUserPwd.jsp"  flush="true" >
		<jsp:param name="userid" value="<%=stuid%>"/>
	</jsp:include>