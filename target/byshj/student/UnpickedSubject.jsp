<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../error.jsp" %>
<% 
	String specid=request.getParameter("specid");
%>
<!-- 可选课题列表 -->
	<form id="subbyspecform" action="../SubjectServlet?mode=getsubsbyspec&specid=<%=specid%>" method="post" style="margin:1px">
		<div class="caption" width="100%">
			<span>可选课题列表&nbsp;&nbsp;</span>
			<span style="font-weight:normal">
				所属教研室&nbsp;<select name="tdept"></select>&nbsp;&nbsp;指导教师&nbsp;<input type="text" class="text" name="tname" style="width:120px;"/>
		        <input type="submit" value="查询"/>
				&nbsp;&nbsp;<img alt="" src="../images/loading.gif" name="loading" style="display:none" />
				<span name="searchresult" class="flag"></span>
			</span>
		</div>
	</form>
	<div style="height:280px;overflow-y:scroll">
	<table class="mtable2" style="width:97%">
		<tr><th>序号</th><th>(课题编号)课题名</th><th>课题状态</th><th>指导教师</th><th>职称</th><th>所属教研室</th><th>操作</th></tr>
		<tbody name="unpickedsubtb"></tbody>
	</table>
</div>
<!-- 已初选课题列表 操作-->
<hr>
<div>
	<table class="mtable2">
	    <caption>初选课题志愿列表</caption>
		<tr><th>志愿顺序</th><th>(课题编号)课题名</th><th>指导教师</th><th>操作</th></tr>
		<tbody name="firstpicktb"></tbody>
		<tr>
			<td colspan=2 align="right"><br>
				<input type="checkbox" name="assignflag" value="1"/><font color='red'>落选后，需要专业负责人指派课题</font>
			</td>
			<td colspan=3 align="right"><br>
				<span name="errmsg"></span>&nbsp;&nbsp;
				<input type="button" value="提交选题志愿"/>&nbsp;
			</td>
		</tr>
	</table>
	
	<div id="divprocessprompt">
		<jsp:include page="PickSubjectProcess.jsp"  flush="true" />
	</div>
	<script type="text/javascript">
		$("#divprocessprompt table").find("tr:eq(0)").children().eq(0).css("color","red");
	</script>
</div>