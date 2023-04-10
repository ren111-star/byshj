<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../error.jsp" %>
<%
	//检查用户是否合法
  	String userid=(String)session.getAttribute("userid");
  	String usertype=(String)session.getAttribute("usertype");
  	if(userid==null||"".equals(userid)) {
  		response.sendRedirect("../UserLogin.jsp");
  	}
  	if(!usertype.equals("管理员")){
  		response.sendRedirect("../UserLogin.jsp");
  	}
  	String username=(String)session.getAttribute("username");
  	request.setCharacterEncoding("utf-8");
  	String usercount=application.getAttribute("userCount").toString();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>管理员管理-计算机学院毕业设计管理系统</title>
		<link rel="stylesheet" type="text/css" href="../css/style.css" />
		<link rel="stylesheet" type="text/css" href="../css/divwin.css" />
		<link rel="stylesheet" type="text/css" href="../css/common.css" />
		<link rel="stylesheet" type="text/css" href="../css/tabs.css" />
		<link rel="stylesheet" type="text/css" href="../css/manager.css" />
		<script src="../js/jquery-1.4.2.js" type="text/javascript"></script>
		<script src="../js/jquery.form.js" type="text/javascript"></script>
		<script src="../js/jquery.tools.min.tabs.js" type="text/javascript"></script>
		<script src="../js/jquerywin.js" type="text/javascript"></script>
		<script src="../js/jquery.mask.js" type="text/javascript"></script>
		<script src="../js/jquery.table.js" type="text/javascript"></script>
		<script src="../js/com/code.js" type="text/javascript"></script>
		<script src="../js/jquery.json.js" type="text/javascript"></script>
		<script src="../js/jquery.common.js" type="text/javascript"></script>
		<script src="../js/manager/ManagerMag.js" type="text/javascript"></script>
	</head>
	<body>
		<div id="wrapper">
			<div id="header">
				<div class="logo">
				</div>
			</div>
			<div id="navigation">
				<span>[管理员]&nbsp;<%=username%>(<%=userid%>)
				&nbsp;&nbsp;[<a id="modifypwda" href="javascript:modifypwd('#modifypwda','<%=userid%>')">修改密码</a>]&nbsp;&nbsp;
				[<a href="javascript:location.href='../ExitLogin.jsp'">退出</a>]&nbsp;当前在线&nbsp;<%=usercount%>&nbsp;人</span>
				<ul>
					<li><a href="#">课题管理</a></li>
					<li><a href="#">盲审管理</a></li>
					<li><a href="#">系统管理</a></li>
				</ul>
			</div>
			<div id="content">
				<div id="subjectmagdiv" style="display:none">
					<jsp:include page="SubjectMag.jsp"  flush="true" />
				</div>
				<div id="blindreviewmagdiv" style="display:none">
					<jsp:include page="BlindReviewMag.jsp"  flush="true" />
				</div>
				<div id="systemmagdiv" style="display:none">
					<jsp:include page="SystemMag.jsp"  flush="true" />
				</div>
			</div>
			<jsp:include page="../footer.jsp"  flush="true"/>
		</div>
		<!--修改密码div,默认隐藏 -->
		<jsp:include page="../ModifyUserPwd.jsp"  flush="true" >
			<jsp:param name="userid" value="<%=userid%>"/>
		</jsp:include>
		<div class='divwin' id="loading">
		</div>
	</body>
</html>