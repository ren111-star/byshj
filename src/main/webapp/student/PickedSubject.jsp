<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../error.jsp"
		 import="bpo.SubjectBpo,bean.SubjectBean"%>
<%
	String stuid=request.getParameter("stuid");
	String subid=request.getParameter("subid");
	String subkind="";//课题性质
	String subname="";
	String tname="";
	try{
		SubjectBpo subjectbpo=new SubjectBpo();
		SubjectBean subject=subjectbpo.getBysubid(subid);
		subkind=subject.getSubkind();
		subname=subject.getSubname();
		tname=subject.getTutor().getTname()+"&nbsp;&nbsp;"+subject.getOthertutor().getTname();
		//取文档上传信息
		
	}catch(Exception e){
		throw e;
	}
%>
<div>(编号)课题名称：<a href="../teacher/SubjectView.jsp?subid=<%=subid%>" target="_blank">(<%=subid %>)<%=subname %></a>
	&nbsp;&nbsp;&nbsp;&nbsp;指导教师：<%=tname%>
</div>
<table class="mtable2" id="submitdoctb">
	<tr><th>序号</th><th>文档名称</th><th>状态</th><th>操作</th></tr>
	<tr><td>1</td><td><a href="#"><%%>if(subkind.equals("1")){毕业设计说明书}else{毕业设计论文}<% %>(盲审)</a></td>
		<td></td>
		<td>
		     <input type="hidden" value="<%=stuid%>" name="stuid"/>
		     <input type="hidden" value="paperblind" name="doctype"/>
			 <form name="paperblindform" action="../SubSubmitServlet?mode=updateDoc&&stuid=<%=stuid %>&&doctype=paperblind" method="POST" enctype="multipart/form-data">
		        <input type="file" name="file" class="text" style="width:200px"/>   
		        <input type="submit" value="上传" name="uploadbtn" /> 
		        <img alt="" src="../images/loading.gif" name="loadingimg" style="display:none"><span name="upmessage" class="flag"></span>
		        <br>
		        <span>文件最大不能超过200M，格式必须为<span class="flag">pdf</span></span>
	    	</form> 
		</td>
	</tr>
	<tr><td>2</td><td><a href="#">毕业设计说明书(论文)</a></td>
		<td></td>
		<td><input type="hidden" value="<%=stuid%>" name="stuid"/>
		     <input type="hidden" value="paper" name="doctype"/>
			 <form name="paperform" action="../SubSubmitServlet?mode=updateDoc&&stuid=<%=stuid %>&&doctype=paper" method="POST" enctype="multipart/form-data">
		        <input type="file" name="file" class="text" style="width:200px"/>   
		        <input type="submit" value="上传" name="uploadbtn"/> 
		        <img alt="" src="../images/loading.gif" name="loadingimg" style="display:none"><span name="upmessage" class="flag"></span>
		        <br>
		        <span>文件最大不能超过200M，格式必须为<span class="flag">pdf</span></span> 
	    	</form> 
		</td>
	</tr>
	<tr><td>3</td><td><a href="#">外文文献及翻译</a></td>
		<td></td>
		<td><input type="hidden" value="<%=stuid%>" name="stuid"/>
		     <input type="hidden" value="translation" name="doctype"/>
			 <form name="translationform" action="../SubSubmitServlet?mode=updateDoc&&stuid=<%=stuid %>&&doctype=translation" method="POST" enctype="multipart/form-data">
		        <input type="file" name="file" class="text" style="width:200px"/>   
		        <input type="submit" value="上传" name="uploadbtn"/> 
		        <img alt="" src="../images/loading.gif" name="loadingimg" style="display:none"><span name="upmessage" class="flag"></span>
		        <br>
		        <span>文件最大不能超过200M，格式必须为<span class="flag">pdf</span></span>
	    	</form> 
		</td>
	</tr>
	<tr><td>4</td><td><a href="#">开题报告/程序源代码</a></td>
		<td></td>
		<td><input type="hidden" value="<%=stuid%>" name="stuid"/>
		     <input type="hidden" value="sourcecode" name="doctype"/>
			 <form name="sourcecodeform" action="../SubSubmitServlet?mode=updateDoc&&stuid=<%=stuid %>&&doctype=sourcecode" method="POST" enctype="multipart/form-data">
		        <input type="file" name="file" class="text" style="width:200px"/>   
		        <input type="submit" value="上传" name="uploadbtn"/> 
		        <img alt="" src="../images/loading.gif" name="loadingimg" style="display:none"><span name="upmessage" class="flag"></span>
		        <br>
		        <span>文件最大不能超过200M，格式必须为<span class="flag">rar</span></span>
	    	</form> 
		</td>
	</tr>
</table>

