<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!-- 显示待审核的题目 -->
<input type="button" value="查询"/>
<img alt="" src="../images/loading.gif" name="loadingimg" style="display:none">
<span name="errmsg0" class="flag"></span>
<table class="mtable2">
	<tr><th>序号</th><th>题目</th><th>审核意见</th></tr>
	<tbody name="teachertb"></tbody>
	<tr>
		<td colspan=3 align="right">
			<img alt="" src="../images/loading.gif" name="loadingimg1" style="display:none">
			<span name="errmsg1" class="flag"></span>
			<input type="button" value="修改" style="display:none"/>
			<input type="button" value="保存" style="display:none"/>
			<input type="button" value="返回" style="display:none"/>
		</td>
	</tr>
</table>
