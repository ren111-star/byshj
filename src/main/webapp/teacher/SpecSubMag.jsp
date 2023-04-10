<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%
	String specid=request.getParameter("specid");
%>
<input id="specid" value="<%=specid %>" type="hidden">
<ul class="tabs">
	<li><a href="#">审核课题</a></li>
	<li><a href="#">选题明细</a></li>
</ul>
<div id="specsubmagpanes">
	<div id="subsbyspec">
		<form id="subbyspecform" action="../SubjectServlet?mode=getsubsbyspec&specid=<%=specid%>" method="post" style="margin:5px">
			所属教研室<select name="tdept"></select>指导教师<input type="text" class="text" name="tname" style="width:120px;"/>
			课题状态<select name="substatus"></select><input type="submit" value="查询"/>&nbsp;&nbsp;
			<img alt="" src="../images/loading.gif" name="loadingimg" style="display:none">
			<span name="errmsg0" class="flag"></span>
		</form>
		<table class="mtable2">
			<tr><th>序号</th><th>所属教研室</th><th>指导教师</th><th>(课题编号)课题名</th><th>往年相似课题</th><th>课题状态</th><th>选中标志</th></tr>
			<tbody id="tbsubsbyspec"></tbody>
			<tr><td colspan=4 style="text-align:right"><br>
					审核标志：&nbsp;&nbsp;<input type='radio' name='auditflag' value='1'/>审核通过&nbsp;&nbsp;
    				<input type='radio' name='auditflag' value='0'/>审核没通过&nbsp;&nbsp;
    				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    				<span style='display:none' name='auditoptionspan'>
    				<hr>
    					审核意见：<textarea name="auditoption" rows="2" style="width:60%"></textarea>
    				</span>
    			</td>
    			<td colspan=2 style="text-align:right"><br>
					<input type="button" value="全选"/>&nbsp;
					<input type="button" value="撤销全选"/>&nbsp;
					<input type="button" value="提交审核结果" name="submitaudit"/>
				</td>
			</tr>
		</table>
	</div>
	<div id="stusbyspec" style="display:none;">
		<form id="stubyspecform" action="../SubjectServlet?mode=getstusbyspec&specid=<%=specid%>" method="post" style="margin:5px">
			班级<select name="classname"></select>&nbsp;
			学生状态<select name="stustatus"></select>
			<input type="submit" value="查询"/>
			&nbsp;&nbsp;
			<img src="../images/loading.gif" name="loadingimg" style="display:none">
			<span name="errmsg0" class="flag"></span>
		</form>
		<table class="mtable2">
			<tr><th>序号</th><th>班级</th><th>学号</th><th>姓名</th><th>毕业设计（论文）题目</th><th>指导教师</th><th>学生状态</th><th>操作</th></tr>
			<tbody id="tbstusbyspec"></tbody>
		</table>
		<!--指派课题窗口-->
		<div id="assignsubject" class="win">
			<div  class="titlebar">
				<span class="title">可选课题列表&nbsp;&nbsp;（<span name="stuid"></span>）<span name="sname"></span></span>
				<span class="close" onclick="hideDialog('#assignsubject')" title="关闭">X</span>
			</div>  
			<div class="content"> 
				<table class="mtable2">
					<tr><th>序号</th><th>课题名</th><th>指导教师</th><th>操作</th></tr>
					<tbody id="tbsubjects"></tbody>
				</table>
     		</div>
		</div>
	</div>
</div>
<!--查看往年相似课题-->
<div id="viewsimsubshis" class="win">
	<div  class="titlebar">
		<span class="title">往年相似课题</span>
		<span class="close" onclick="hideDialog('#viewsimsubshis')" title="关闭">X</span>
	</div>  
	<div class="content"></div> 
</div>
