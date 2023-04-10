<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../../error.jsp"%>
<!--审核管理界面-->
<br/>
<div  class="menu">
	<div class="title">&nbsp;&nbsp;</div><br>
	<ul id="subjectreviewul">
		<li><a href="#">课题盲审管理</a></li>
		<li><a href="#">课题盲审查询</a></li>
	</ul>
</div>
<div id="subjectreviewpanes" class="rightcontent">
	<div id="subjectreviewMagdiv" style="display:none">
		<div class="caption">课题盲审管理&nbsp;&nbsp;</div>
		<div><br>&nbsp;&nbsp;课题审核阶段&nbsp;&nbsp;<input type="button" value="分配盲审题目" name="assignsubtoteacher"/> &nbsp;&nbsp;<input type="button" value="查询盲审情况" name="searchblindinfo"/>
			<img src="../images/loading.gif" name="loadingimg" style="display:none">
			<span name="errmsg0" class="flag"></span>
		</div>
		<table class="mtable2">
			<tr><th>序号</th><th>工号</th><th>教师名</th><th>提交课题数</th><th>需审核课题数</th><th>未审核课题数</th></tr>
			<tbody name="teachertb"></tbody>
		</table>
		<div>
			<img src="../images/loading.gif" name="loadingimgtb" style="display:none">
			<span name="errmsgtb" class="flag"></span>
		</div>
	</div>
    <div id="subjectreviewSelectdiv" style="display:none">
        <div class="caption">课题盲审查询&nbsp;&nbsp;</div>
		<form id="subjectreviewlistform" action="../SubjectServlet?mode=getSubsBySpecAndName" method="post" style="margin:5px">
			专业<select name="specid"></select>&nbsp;&nbsp;
			课题名称<input type="text" class="text" name="subname" style="width:120px;"/>&nbsp;&nbsp;<input type="submit" value="查询"/>&nbsp;&nbsp;
			<img src="../images/loading.gif" name="loadingimg" style="display:none">
			<span name="errmsg0" class="flag"></span>
		</form>
		<table class="mtable2">
			<tr><th>序号</th><th>(课题编号)课题名</th><th>指导教师</th><th>审核人</th><th>审核意见</th></tr>
			<tbody name="tbsubreviews"></tbody>
		</table>
	</div>
	
</div>
