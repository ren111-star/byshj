<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../error.jsp"%>
<!--通知管理界面-->
<script src="../js/manager/InformMag.js" type="text/javascript"></script>
<div id="informmagui">
	<div class="caption">通知列表&nbsp;&nbsp;</div>
	<form id="searchinformform" action="../InformServlet?mode=gets" method="post" style="margin:1px"> 
		&nbsp;&nbsp;标&nbsp;题&nbsp;&nbsp;<input type="text" class="text" name="wyxinform" id="wyxinform">&nbsp;
<input type="submit" value="查询"/>&nbsp;&nbsp;<input type="button" value="发布通知"/>
		<img alt="" src="../images/loading.gif" name="loadingimg" style="display:none">
		<span name="errmsg0" class="flag"></span>
	</form>
	<table class="mtable2">
		<tr><th style="width:40px">序号</th><th style="width:450px">标题</th><th>浏览次数</th><th>发布时间</th><th>修改时间</th><th>操作</th></tr>
		<tbody name="informtb" id="informtb"></tbody>
	</table>
	<div id="wyxshow" class="win">
			<table id='wyxshowtable' class="mtable3" width="95%">
				<tr><td style="width:30px">标题</td><td><input type="text" class="text" name="title" id="showtitle"/></td></tr>
				<tr><td style="width:30px">内容</td><td colspan=2><textarea name="content" rows="5" id="showcontent"></textarea></td></tr>
				<tr>
						<td colspan=2 align=right>
							<input type="button" value="取消" onclick="hideDialog('#wyxshow')">
						</td>
				</tr>
			</table>
	</div>
	<div id="informedt" class="win" >
	<div  class="titlebar"><span class="title" id="subedttitle"></span><span class="close" onclick="hideDialog('#informedt')" title="关闭">X</span></div>  
        <div class="content"> 
			<form id="edtinformform" action="../InformServlet?mode=add" method="post" enctype="multipart/form-data">
				<table class="mtable3" width="95%">
					<tr><td style="width:30px">标题</td><td><input type="text" class="text" name="title" id="title"/></td></tr>
					<tr><td style="width:30px">内容</td><td colspan=2><textarea name="content" rows="5" id="informcontent"></textarea></td></tr>
					<tr><input type="hidden"  name="infid" id="infid"/></tr>
				</table>
				<div id="affixlist" style="display:none">
					<table id="wyxtable" width="95%" >
					 <tr>
						<td width="15%">
				         已经上传的附件：
				        </td>
				        <td width="85%">
				        <div id="affixshow">
				        	<table width="100%" style="border:1px solid #000000;">
				        	<tr><th width="70%" align="center">附件</th><th width="30%" align="left">操作</th></tr>
		                    <tbody name="affixlisttb" id="affixlisttb"></tbody>
				        	</table>
				        </div>
				        </td>
				      </tr>
				      </table>
				</div>
				 <!--
			</form>
			<form id="informform2" action="" encoding="multipart/form-data">
			
			 <input type="file" name="file">
			 -->
				<table class="mtable3" width="95%">
				   
					<tr>
						<td>
							<a  href="#"  class= "addfile"> 
					 <input type="file" name="affixFile" id="myfile" size= "1" class= "addfile"  maxlength="80" title= "点击选择附件">
							</a>
							
							
						</td>
						<td>
							<div id= "files_list" class="files_list">
							</div>
							 <script  src='../js/multifile.js'> </script>
					    <script language="javascript"> 
						var multi_selector=new MultiSelector(document.getElementById("files_list"),5);
						multi_selector.addElement(document.getElementById("myfile"));
						//alert(document.getElementById("myfile"));
					</script>
						</td>
					</tr>
				
					<tr>
						<td colspan=2 align=right>
							<img alt="" src="../images/loading.gif" name="loadingimg" style="display:none">
							<span class="flag" name="errmsg"></span><hr>
							<input type="submit" value="提交" >&nbsp;&nbsp;
							<input type="button" value="取消" onclick="hideDialog('#informedt');$('#signid').val(null)">
						</td>
					</tr>
				</table>
				
			</form>
		</div>
	
	</div>
</div>