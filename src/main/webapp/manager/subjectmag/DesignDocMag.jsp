<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../../error.jsp"%>
<!-- 毕业设计文档管理 -->
<form name="designdocform" action="../SubSubmitServlet?mode=getStuDocsBySpec" method="post" style="margin:5px">
	专业<select name="specid"></select>&nbsp;&nbsp;
	班级<select name="classname"><option value=""></option></select>&nbsp;&nbsp;
	姓名<input type="text" name="sname" value="" class="text" style="width:120px;"/>&nbsp;&nbsp;
	<input type="submit" value="查询"/>&nbsp;&nbsp;<input type="button" value="导出盲审意见" name="exportreviewpaperopinion"/>
	&nbsp;&nbsp;<input type="button" value="导出进程表" name="exportprogresstable"/>
	<img src="../../images/loading.gif" name="loadingimg" style="display:none">
	<span name="errmsg0" class="flag"></span>
</form>
<table class="mtable2">
	<tr>
		<th style="width:40px">序号</th><th style="width:80px">班级</th><th style="width:40px">学号</th><th style="width:60px">姓名</th>
		<th>设计说明书（论文）[盲审]</th><th>设计说明书（论文）</th><th>外文翻译</th><th>程序源代码</th><th>有效周总结(数)</th>
	</tr>
	<tbody name="studocs"></tbody>
</table>