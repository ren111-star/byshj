<%@ page import="java.lang.Integer.parseInt" %>
<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%
	String userid=request.getParameter("userid");
	String username=request.getParameter("username");
	int graduateweeknum= Integer.parseInt(request.getParameter("graduateweeknum"));
%>
	<form id="subform" action="../SubjectServlet?mode=edt" method="post">
 		<div  class="titlebar"><span class="title" id="subedttitle"></span><span class="close" onclick="hideDialog('#subjectedt')" title="关闭">X</span></div>  
        <div class="content"> 
	 		<table class="mtable1">
	 			<caption>课题基本信息</caption>
	 			<tr>
	 				<td>课题编号</td>
	 				<td><input id="subid" name="subid" type="text" class="text" style="width:80px" readonly/></td>
	 				<td colspan=7>指导教师&nbsp;&nbsp;<span id="tutorname" name="tutorname">&nbsp;&nbsp;<%=username%></span>
	 					<input type="hidden" id="tutorid" name="tutorid" value="<%=userid%>"></input>
	 				其他指导教师编号&nbsp;<input type="text" id="othertid" class="text" name="othertid" style="width:80px"/>
	 				姓名&nbsp;<input type="text" id="othertname" class="text" style="width:100px"/>
		 				<input type="button" value="…" title="按姓名模糊查找" id="searchbtn"/>
	 					<div class="searchwin" id="searchteacher">
	 						<div class="titlebar0"><span class="title0">双击选择教师</span><span class="close0" title="关闭">X</span></div>
	 						<div class="content">
		 						<table class="mtable2">
		 							<tr><th>教师编号</th><th>教师名</th></tr>
		 							<tbody  id="teacherlist"></tbody>
		 						</table>
	 						</div>
	 					</div>
	 				</td>
	 			</tr>
	 			<tr><td>课题名称</td><td colspan=7><input type="text" id="subname" name="subname" class="text"/></td></tr>
	 			<tr>
	 				<td>类别</td><td><select id="subsort" name="subsort" style="width:80px"></select></td>
	 				<td>题目性质</td><td><select id="subkind" name="subkind" style="width:80px"></select></td>
	 				<td>题目来源</td><td><select id="subsource" name="subsource" style="width:80px"></select></td>
	 				<td>题目类型</td><td><select id="subtype" name="subtype" style="width:80px"></select>&nbsp;课题地点
	 					<select id="subaddress" name="subaddress" style="width:80px">
	 						<option value="0" selected>校内</option>
	 						<option value="1" >校外</option>
	 					</select>
	 				</td>
	 			</tr>
	 			<tr><td align="center">设计<br/>(论文)<br/>概述<br/><span class="flagmin">[400字]</span><br/><span class="flagmin"></span></td><td colspan=7><textarea id="oldargu" name="oldargu" rows="10"></textarea></td></tr>
	 			<tr><td align="center">设计<br/>(论文)<br/>工作内容<br/><span class="flagmin">[400字]</span><br/><span class="flagmin"></span></td><td colspan=7><textarea id="workcontent" name="content" rows="10"></textarea></td></tr>
	 			<tr><td align="center">设计<br/>(论文)<br/>工作基本要求<br/><span class="flagmin">[50字]</span><br/><span class="flagmin"></span></td><td colspan=7><textarea id="requirement" name="requirement" rows="6"></textarea></td></tr>
	 			<tr>
	 				<td align="center">设计<br/>(论文)<br/>工作日程</td>
	 				<td colspan=7>
	 					<textarea id="subprog" name="subprog" rows="10" style="display:none"></textarea>
	 					<input type="text" id="graduateweeknum" name="graduateweeknum" style="display:none" value="<%=graduateweeknum %>"/>
	 					<table class="mtable2" id="progresstable">
	 					<tr><td width=150px align="center">时间（按两周填写）</td><td align="center">应完成的工作内容</td></tr>
	 					<%
	 						for(int i=0;i<graduateweeknum;i=i+2){//两周一行
	 							out.println("<tr><td><input type='text' id='progresstime_"+i+"' name='progresstime_"+i+"' class='text'/></td>"+
	 							"<td><textarea id='progressitem_"+i+"' rows='2' style='height:100%' name='progressitem_"+i+"'></textarea></td></tr>");
	 						}
	 					 %>
	 					 </table>
	 				</td>
	 			</tr>
	 			<tr><td width="8%" align="center">主要参考资料及文献</td><td colspan=7><textarea id="refpapers" name="refpapers" rows="10"></textarea></td></tr>	
	 		</table>
	 		<hr>
	 		<div>
	 			<table class="mtable2">
	 				<tr><td width=100px>课题适合专业</td>
	 					<td>
	 						<div id="speciality"><input type="hidden" name="speccount" id="speccount" value="5"></div>
	 					</td>
	 					<td>课题方向：<select id="subdirectionselect" name="subdirection"></select></td>
	 					<td width=300px  style="text-align:right;vertical-align:bottom;">
	 						<span class="flag" id="errmsg"></span><hr>
	 						<input type="submit" value="保存" id="modifysavebtn">
	 						<input type="button" value="保存" id="modifybasesavebtn" style="display:none">
	 						&nbsp;&nbsp;&nbsp;&nbsp;
	 						<input type="button" value="取消" onclick="hideDialog('#subjectedt')">&nbsp;&nbsp;
	 					</td>
	 				</tr>
	 			</table>
	 		</div>
		</div> 
	</form>