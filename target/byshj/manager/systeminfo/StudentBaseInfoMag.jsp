<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../../error.jsp"%>
<!--学生管理界面-->

<div class="caption">学生基本信息管理&nbsp;&nbsp;</div>
<form id="stulistform" action="../StudentServlet?mode=gets" method="post" style="margin:1px">
	专业<select name="speciality"></select>&nbsp;&nbsp;
	班级<select name="classname"></select>&nbsp;&nbsp;
	姓名<input type="text" class="text" style="width:80px" name="sname"/>&nbsp;&nbsp;
       <input type="submit" value="查询"/>
	<input type="button" value="导入学生"/>&nbsp;&nbsp;
	<img src="../../images/loading.gif" name="loadingimg" style="display:none"><span name="errmsg0" class="flag"></span>
</form>
<table class="mtable2">
	<tr><th>序号</th><th>学号</th><th>姓名</th><th>班级</th><th>邮箱</th><th>电话</th><th>操作</th></tr>
	<tbody name="studenttb"></tbody>
	<tr><td style="width:30px"><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td style="width:85px"><br><input type="text" name="stuid" class="text"/></td>
		<td style="width:80px"><br><input type="text" name="sname" class="text"/></td>
		<td style="width:80px"><br><select name="classname"/><option value=""></option></td>
		<td style="width:160px"><br><input type="text" name="email" class="text"/></td>
		<td style="width:100px"><br><input type="text" name="telephone" class="text"/></td>
		<td><br><input type="button" name="add" value="增加"/>&nbsp;&nbsp;<span name="errmsg" class="flag"></span></td>
	</tr>
</table>

<!--导入excel文件 学生基本信息（学号、姓名、班级）-->
<div id="importstudiv" class="win">
	<div  class="titlebar">
		<span class="title">学生基本信息导入</span>
		<span class="close" title="关闭">X</span>
	</div>  
	<div class="content"> 
		<form name="importform" method="POST" enctype="multipart/form-data">
			<br>   
	        <input type="file" name="file" class="text" style="width:260px"/>   
	        <input type="submit" value="导入文件" /><br>   
	        <img src="../../images/loading.gif" name="loadingimg" style="display:none"><span name="upmessage" class="flag"></span>
    	</form> 
	</div>
	<div>
    		<span class="flag"><br>
	    		导入excel文件有3列，顺序：学号、姓名、班级<br>
    		</span>
    	</div>
</div>

