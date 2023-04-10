<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../error.jsp" %>

<table class="mtable2">
    <caption>已初选课题状态列表&nbsp;&nbsp;<input type="button" value="刷新"/></caption>
	<tr><th>志愿顺序</th><th>(课题编号)课题名</th><th>指导教师</th><th>选题状态</th></tr>
	<tbody name="firstpicktb"></tbody>
	<tr><td colspan=4 align="right"><span class="flag"></span>&nbsp;&nbsp;<input type='button' value='重新选择课题' class='btn'/>&nbsp;&nbsp;</td></tr>
</table>
<div id="divprocessprompt1">
		<jsp:include page="PickSubjectProcess.jsp"  flush="true" />
	</div>
	<script type="text/javascript">
		$("#divprocessprompt1 table").find("tr:eq(0)").children().eq(2).css("color","red");
	</script>

