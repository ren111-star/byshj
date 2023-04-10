<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%
	String userid=request.getParameter("userid");
	String username=request.getParameter("username");
%>
	<form id="teacherform" action="../TeacherServlet?mode=personaledt" method="post">
        <div class="content"> 
	 		<table class="mtable1" style="width:600px" align="center">
	 			<caption>个人信息</caption>
	 			<tr>
	 				<td>教师编号</td>
	 				<td><input id="tid" name="tid" type="text" class="text" value="<%=userid%>" readonly/></td>
	 				<td>教师姓名</td>
	 				<td><input id="tname" name="tname" type="text" class="text"  value="<%=username%>" readonly/></td>
	 				<td>所属教研室</td>
	 				<td><input id="tdept" name="tdept" type="text" class="text" readonly></td>
	 			</tr>
	 			<tr><td><br></td></tr>
	 			<tr>
	 				<td>性&nbsp;&nbsp;&nbsp;&nbsp;别</td>
	 				<td>
	 					<select id="tsex" name="tsex"></select>
	 				</td>
	 				<td>职&nbsp;&nbsp;&nbsp;&nbsp;称</td>
	 				<td><select id="tpost" name="tpost"></select></td>
	 				<td>学&nbsp;&nbsp;&nbsp;&nbsp;位</td>
	 				<td><select id="tdegree" name="tdegree"></select></td>
	 				
	 			</tr>
	 			<tr><td><br></td></tr>
	 			<tr>
	 				<td>研究方向</td>
	 				<td colspan=5><input id="studydirect" name="studydirect" type="text" class="text"/></td>
	 			</tr>
	 			<tr>
	 				<td>电子邮箱</td>
	 				<td  colspan=3><input id="email" name="email" type="text" class="text"/></td>
	 				<td align="right">手&nbsp;&nbsp;&nbsp;&nbsp;机</td>
	 				<td><input id="telphone" name="telphone" type="text" class="text"/></td>
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
		<jsp:param name="userid" value="<%=userid%>"/>
	</jsp:include>