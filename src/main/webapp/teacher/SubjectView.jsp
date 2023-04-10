<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../error.jsp"%>
<jsp:directive.page import="bpo.SubjectBpo"/>
<jsp:directive.page import="bean.SubjectBean"/>
<jsp:directive.page import="bean.ProgressBean"/>
<jsp:directive.page import="java.util.Iterator"/>
<jsp:directive.page import="java.util.List"/>
<jsp:directive.page import="com.SyscodeBpo"/>
<jsp:directive.page import="bpo.SpecialityBpo"/>
<%
//检查用户是否合法
//……
 %>
<%
	String subid=request.getParameter("subid");
	String errmsg="";
	SubjectBean subject=null;
	String subkind="";//课题性质
	String subsource="";//课题来源
	String subtype="";//课题类型
	String speciality="";//适合专业
	try{
		SubjectBpo subjectbpo=new SubjectBpo();
		subject=subjectbpo.getBysubid(subid);
		//获得课题性质、课题来源、课题类型、适合专业
		SyscodeBpo syscodebpo=new SyscodeBpo();
		subkind=syscodebpo.getcode("ktxz",subject.getSubkind()).getCodecontent();
		subsource=syscodebpo.getcode("ktly",subject.getSubsource()).getCodecontent();
		subtype=syscodebpo.getcode("ktlx",subject.getSubtype()).getCodecontent();
		SpecialityBpo specbpo=new SpecialityBpo();
		
		List<String> specs=subject.getListspec();
		Iterator it = specs.iterator();
		while (it.hasNext()) {
			String spec=(String) it.next();
			if(!speciality.equals("")) speciality=speciality+"<br>";
			speciality=speciality+specbpo.getByspecid(spec).getSpecname();
		}
	}catch(Exception e){
		subject=new SubjectBean();
		throw e;
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
		<title>查看课题信息-计算机学院毕业设计管理系统</title>
		<link rel="stylesheet" type="text/css" href="../css/style.css" />
		<link rel="stylesheet" type="text/css" href="../css/common.css" />
		<script src="../js/jquery-1.4.2.js" type="text/javascript"></script>
		<script type="text/javascript">
			$(document).ready(function(){
				//绑定“打印任务书”按钮操作
				$("input[value='打印任务书']").click(function(){
					$loadingimg=$("img[name='loadingimg']");
					$errmsg=$("span[name='errmsg0']");
					
					$errmsg.text("正在导出……");
					$loadingimg.show();
					var subid=$("#subid").val();
					$.getJSON("../ExcelServlet?mode=exporttaskbook",{subid:subid},function call(data){ 
						if(data.errmsg!=""){
			        		$errmsg.text("导出失败："+data.errmsg);
			        		$loadingimg.hide();
			        		return;
			        	}
			        	$errmsg.html("导出成功！<a href='../FileDownloadServlet0?tempfilename="+subid+"taskbookoutput.xls' title='点击打开/保存文件'>课题任务书</a>");
			        	$loadingimg.hide();
			        });
				});
			});
		</script>
	</head>
	<body>
		<div id="wrapper">
			<div id="header">
				<div class="logo">
				</div>
			</div>
			<div id="content">
				<table class="mtable2">
					<caption>
						<span id="subname">课题名：<%=subject.getSubname()%></span>
						<input id="subid" type="hidden" value=<%=subid%>></input>&nbsp;&nbsp; 
						指导教师：<span id=tutorname><%=subject.getTutor().getTname()%>&nbsp;&nbsp;<%=subject.getOthertutor().getTname()%> </span>
					 	&nbsp;&nbsp;<input type="button" value="打印任务书" class="btn"/>
							<img src="../images/loading.gif" name="loadingimg" style="display:none">
							<span name="errmsg0" class="flag"></span>
					 </caption>
		 			<tr>
		 				<td>课题性质</td><td><%=subkind %></td>
		 				<td>课题来源</td><td><%=subsource %></td>
		 				<td>课题类型</td><td><%=subtype %></td>
		 				<td>适合专业</td><td><%=speciality %></td>
		 			</tr>
		 			<tr><td>设计(论文)<br/>概述</td><td colspan=7><%=subject.getOldargu().replace("\n","<br>").replace(" ","&nbsp;") %></td></tr>
		 			<tr><td>设计(论文)<br/>工作内容</td><td colspan=7><%=subject.getContent().replace("\n","<br>").replace(" ","&nbsp;") %></td></tr>
		 			<tr><td>设计(论文)<br/>工作基本要求</td><td colspan=7><%=subject.getRequirement().replace("\n","<br>").replace(" ","&nbsp;") %></td></tr>
		 			<tr><td>设计(论文)<br/>工作日程</td><td colspan=7><%=subject.getSubprog().replace("\n","<br>").replace(" ","&nbsp;") %></td></tr>
		 			<tr><td>主要参考资料及<br/>文献</td><td colspan=7><%=subject.getRefpapers().replace("\n","<br>").replace(" ","&nbsp;") %></td></tr>			
		 		</table>
			</div>
		</div>
		
	</body>
</html>