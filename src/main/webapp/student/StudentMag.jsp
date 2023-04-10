<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"
		 import="bpo.StudentBpo,bean.StudentBean" errorPage="../error.jsp" %>
<%
	//检查用户是否合法
  	String userid=(String)session.getAttribute("userid");
  	String usertype=(String)session.getAttribute("usertype");
  	if(userid==null||"".equals(userid)) {
  		response.sendRedirect("../UserLogin.jsp");
  	}
  	if(!usertype.equals("学生")){
  		response.sendRedirect("../UserLogin.jsp");
  	}
  	String stuid=userid;//学号
  	String specid="";//专业编号
  	String classname="";//班级名
  	String sname="";//姓名
  	String identity="学生";//身份表示
  	String status="";//选题状态
  	String subid="";//学生最终确认课题编号
  	try{
	  	StudentBpo studentbpo=new StudentBpo();
	  	StudentBean student=studentbpo.getBystuid(stuid);
	  	specid=student.getClassbean().getSpeciality().getSpecid();
	  	classname=student.getClassbean().getClassname();
	  	sname=student.getSname();
	  	String allstatus=studentbpo.getStuStatus(stuid);
	  	status=allstatus.split("/")[0];
	  	if(status.equals("已选")) subid=allstatus.split("/")[1];
  	}catch(Exception e){
  		throw e;
  	}
  	request.setCharacterEncoding("utf-8");
  	String usercount=application.getAttribute("userCount").toString();
  	String maxsubject="2";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>学生管理-计算机学院毕业设计管理系统</title>
		<link rel="stylesheet" type="text/css" href="../css/style.css" />
		<link rel="stylesheet" type="text/css" href="../css/divwin.css" />
		<link rel="stylesheet" type="text/css" href="../css/common.css" />
		<link rel="stylesheet" type="text/css" href="../css/tabs.css" />
		<script src="../js/jquery-1.4.2.js" type="text/javascript"></script>
		<script src="../js/jquery.form.js" type="text/javascript"></script>
		<script src="../js/jquery.tools.min.tabs.js" type="text/javascript"></script>
		<script src="../js/jquerywin.js" type="text/javascript"></script>
		<script src="../js/jquery.mask.js" type="text/javascript"></script>
		<script src="../js/jquery.table.js" type="text/javascript"></script>
		<script src="../js/com/code.js" type="text/javascript"></script>
		<script src="../js/jquery.json.js" type="text/javascript"></script>
		<script src="../js/jquery.common.js" type="text/javascript"></script>
		<script src="../js/student/StudentMag.js" type="text/javascript"></script>
	</head>
	<body>
		<div id="wrapper">
			<div id="header">
				<div class="logo">
				</div>
			</div>
			<div id="navigation">
				<span>[<%=identity%>]&nbsp;<%=sname%>(<%=stuid%>)&nbsp;&nbsp;<%=classname%>&nbsp;
				[<a href="javascript:location.href='../ExitLogin.jsp'">退出</a>]&nbsp;当前在线&nbsp;<%=usercount%>&nbsp;人
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="../uploadfiles/help/student.pdf" title="点击打开" style="color:red;font-weight:bold;font-size:14px" target="_blank">
					系统使用说明
					</a>
				</span>
				<ul>
					<li><a href="#">我的课题</a></li>
					<%if(status.equals("已选")) {%><li><a href="#">我的周总结</a></li><%} %>
					<li><a href="#">个人信息</a></li>
				</ul>
			</div>
			<div id="content">
				<div id="mysubject">
				<!-- 有用的隐藏信息 (含最多选题志愿数)-->
					<input id="specid" value="<%=specid %>" type="hidden"/>
					<input id="stuid" value="<%=stuid %>" type="hidden"/>
					<input id="maxsubject" value="<%=maxsubject%>" type="hidden"/>
					<input id="status" value="<%=status%>" type="hidden"/>
				<!-- 不同状态显示不同界面 开始 -->
				<%if(status.equals("未选")) {%>
					<div id="unpicksubjectdiv">
						<jsp:include page="UnpickedSubject.jsp"  flush="true" >
						<jsp:param name="specid" value="<%=specid%>"/>
						</jsp:include>
					</div>
				<%} %>
				<%if(!(status.equals("未选")||status.equals("已选"))) {//其他情况均显示已初选课题状态%>
					<div id="pickingsubjectdiv">
						<jsp:include page="PickingSubject.jsp"  flush="true" />
					</div>
				<%} %>
				<%if(status.equals("已选")) {%>
					<div id="pickedsubjectdiv">
						<jsp:include page="PickedSubject.jsp"  flush="true" >
							<jsp:param name="subid" value="<%=subid%>"/>
							<jsp:param name="stuid" value="<%=stuid%>"/>
						</jsp:include>
					</div>
				<%} %>
				</div>
				<!-- 已选状态下显示我的周总结界面 结束 -->
				<%if(status.equals("已选")) {%>
					<div id="myweeksum" style="display:none">
						<jsp:include page="MyWeekSum.jsp"  flush="true" >
							<jsp:param name="subid" value="<%=subid%>"/>
						</jsp:include>
					</div>
				<%} %>
				<div id="selfinfo" style="display:none">
					<jsp:include page="StuPersonalInfo.jsp"  flush="true" >
						<jsp:param name="stuid" value="<%=stuid%>"/>
						<jsp:param name="sname" value="<%=sname%>"/>
					</jsp:include>
				</div>
			</div>
			<jsp:include page="../footer.jsp"  flush="true"/>
		</div>
	</body>
</html>