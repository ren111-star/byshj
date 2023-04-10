<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../../error.jsp"%>
<!--教师管理界面-->

<div class="caption">教师基本信息管理&nbsp;&nbsp;</div>
<form id="tealistform" action="../TeacherServlet?mode=gets" method="post" style="margin:1px">
	教研室<select name="tdept"></select>&nbsp;&nbsp;
	姓名<input type="text" class="text" style="width:80px" name="tname"/>&nbsp;&nbsp;
       <input type="submit" value="查询"/>
	<input type="button" value="导入教师"/>&nbsp;&nbsp;
	<img src="../../images/loading.gif" name="loadingimg" style="display:none">
	<span name="errmsg0" class="flag"></span>
	<div style="display:none">
		<select name="tpost"></select><!-- 在表中修改信息用 -->
		<select name="tdegree"></select>
	</div>
</form>
<table class="mtable2">
	<tr><th>序号</th><th>职工号</th><th>姓名</th><th>教研室</th><th>职称</th><th>学位</th><th>操作</th></tr>
	<tbody name="teachertb"></tbody>
	<tr><td style="width:30px"><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td style="width:85px"><br><input type="text" name="tid" class="text"/></td>
		<td style="width:80px"><br><input type="text" name="tname" class="text"/></td>
		<td style="width:80px"><br><select name="tdept"></select></td>
		<td style="width:160px"><br><select name="tpost"></select></td>
		<td style="width:100px"><br><select name="tdegree"></select></td>
		<td><br><input type="button" name="add" value="增加"/>&nbsp;&nbsp;<span name="errmsg" class="flag"></span></td>
	</tr>
</table>

<!--导入excel文件 教师基本信息（学号、姓名、班级）-->
<div id="importteadiv" class="win">
	<div  class="titlebar">
		<span class="title">教师基本信息导入</span>
		<span class="close" title="关闭">X</span>
	</div>  
	<div class="content"> 
		<form name="importform" method="POST" enctype="multipart/form-data">
			<br>   
	        <input type="file" name="file" class="text" style="width:300px"/>   
	        <input type="submit" value="导入文件" /><br>   
	        <img src="../../images/loading.gif" name="loadingimg" style="display:none"><span name="upmessage" class="flag"></span>
    	</form> 
    	<div>
    		<span class="flag"><br>
	    		导入excel文件有5列，顺序：职工号、姓名、教研室代码、职称代码、学位代码<br>
    		</span>
    	</div>
	</div>
</div>

