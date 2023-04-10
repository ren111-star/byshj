<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%
	String graduateweeknum=(String)session.getAttribute("graduateweeknum");//周总结数
%>
	<form id="subtempform" action="../SubjectLibServlet?mode=edt" method="post">
 		<div  class="titlebar"><span class="title" id="subedttitle"></span><span class="close" onclick="hideDialog('#subjecttempedt')" title="关闭">X</span></div>  
        <div class="content"> 
	 		<table class="mtable1">
	 			<caption>课题基本信息</caption>
	 			<tr>
	 				<td colspan=8><input type="hidden" name="subid"/>指导教师&nbsp;&nbsp;${username}&nbsp;&nbsp;
	 					<input type="hidden"  name="tutorid" value="${userid}"/>
	 				</td>
	 			</tr>
	 			<tr><td>课题名称</td><td colspan=7><input type="text" id="subname" name="subname" class="text"/></td></tr>
	 			<tr>
	 				<td>类别</td><td><select  name="subsort" style="width:80px"></select></td>
	 				<td>题目性质</td><td><select  name="subkind" style="width:80px"></select></td>
	 				<td>题目来源</td><td><select  name="subsource" style="width:80px"></select></td>
	 				<td>题目类型</td><td><select  name="subtype" style="width:80px"></select>&nbsp;课题地点
	 					<select  name="subaddress" style="width:80px">
	 						<option value="0" selected>校内</option>
	 						<option value="1" >校外</option>
	 					</select>
	 				</td>
	 			</tr>
	 			<tr><td align="center">设计<br/>(论文)<br/>概述<br/><span class="flagmin">[400字]</span></td><td colspan=7><textarea  name="oldargu" rows="10"></textarea></td></tr>
	 			<tr><td align="center">设计<br/>(论文)<br/>工作内容<br/><span class="flagmin">[400字]</span></td><td colspan=7><textarea  name="content" rows="10"></textarea></td></tr>
	 			<tr><td align="center">设计<br/>(论文)<br/>工作基本要求<br/><span class="flagmin">[50字]</span></td><td colspan=7><textarea  name="requirement" rows="6"></textarea></td></tr>
	 			<tr>
	 				<td align="center">设计<br/>(论文)<br/>工作日程</td>
	 				<td colspan=7>
	 					<textarea  name="subprog" rows="10" style="display:none"></textarea>
	 					<input type="text" name="graduateweeknum" style="display:none" value="<%=graduateweeknum %>"/>
	 					<table class="mtable2" name="progresstable">
	 					<tr><td width=150px align="center">时间（按两周填写）</td><td align="center">应完成的工作内容</td></tr>
	 					<%
	 						for(int i=0;i<Integer.valueOf(graduateweeknum);i=i+2){//两周一行
	 							out.println("<tr><td><input type='text' name='progresstime_"+i+"' class='text'/></td>"+
	 							"<td><textarea  rows='2' style='height:100%' name='progressitem_"+i+"'></textarea></td></tr>");
	 						}
	 					 %>
	 					 </table>
	 				</td>
	 			</tr>
	 			<tr><td width="8%" align="center">主要参考资料及文献</td><td colspan=7><textarea name="refpapers" rows="10"></textarea></td></tr>	
	 		</table>
	 		<hr>
	 		<div>
	 			<table class="mtable2">
	 				<tr>
	 					<td>课题方向：<select name="subdirection"></select>&nbsp;&nbsp;备注：<input type="text" name="remark"/></td>
	 					<td width=300px  style="text-align:right;vertical-align:bottom;">
	 						<span class="flag" name="errmsg"></span><hr>
	 						<input type="submit" value="保存" name="modifysavebtn">
	 						&nbsp;&nbsp;&nbsp;&nbsp;
	 						<input type="button" value="取消" onclick="hideDialog('#subjecttempedt')">&nbsp;&nbsp;
	 					</td>
	 				</tr>
	 			</table>
	 		</div>
		</div> 
	</form>