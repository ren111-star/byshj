<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8"
         import="bpo.TeacherBpo,bean.TeacherBean,bpo.SpecialityBpo,bpo.SysarguBpo,bean.SpecialityBean"
         errorPage="../error.jsp" %>
<%
    //检查用户是否合法
    String userid = (String) session.getAttribute("userid");
    String usertype = (String) session.getAttribute("usertype");
    if (userid == null || "".equals(userid)) {
        response.sendRedirect("../UserLogin.jsp");
    }
    if (!usertype.equals("教师")) {
        response.sendRedirect("../UserLogin.jsp");
    }
    String specid = "";
    String username = "";
    String specname = "";
    String identity = "教师";
    String graduateweeknum = "0";
    try {
        TeacherBpo teacherbpo = new TeacherBpo();
        TeacherBean teacher = teacherbpo.getBytid(userid);
        username = teacher.getTname();
        identity = "教师";

        SpecialityBpo specbpo = new SpecialityBpo();
        SpecialityBean spec = specbpo.getspecmagBytid(userid);
        specid = spec.getSpecid();
        specname = spec.getSpecname();
        if (specid != null) identity = "专业负责人";

        SysarguBpo sysargubpo = new SysarguBpo();
        graduateweeknum = sysargubpo.getSysargu("graduateweeknum").getArguvalue();
        session.setAttribute("graduateweeknum", graduateweeknum);//周总结周数放入session中
    } catch (Exception e) {
        throw e;
    }
    //userbpo.getrolesbyuser(userid);wxh未改完
    request.setCharacterEncoding("utf-8");
    String usercount = application.getAttribute("userCount").toString();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>教师管理-计算机学院毕业设计管理系统</title>
    <link rel="stylesheet" type="text/css" href="../css/style.css"/>
    <link rel="stylesheet" type="text/css" href="../css/divwin.css"/>
    <link rel="stylesheet" type="text/css" href="../css/common.css"/>
    <link rel="stylesheet" type="text/css" href="../css/tabs.css"/>
    <link rel="stylesheet" type="text/css" href="../css/manager.css"/>
    <script src="../js/jquery-1.4.2.js" type="text/javascript"></script>
    <script src="../js/jquery.form.js" type="text/javascript"></script>
    <script src="../js/jquery.tools.min.tabs.js" type="text/javascript"></script>
    <script src="../js/jquerywin.js" type="text/javascript"></script>
    <script src="../js/jquery.mask.js" type="text/javascript"></script>
    <script src="../js/jquery.table.js" type="text/javascript"></script>
    <script src="../js/com/code.js" type="text/javascript"></script>
    <script src="../js/jquery.json.js" type="text/javascript"></script>
    <script src="../js/jquery.common.js" type="text/javascript"></script>
    <script src="../js/teacher/TeacherMag.js" type="text/javascript"></script>
</head>
<body>
<div id="wrapper">
    <div id="header">
        <div class="logo">
            <div style="float:right">
                <%if (identity.equals("专业负责人")) {%>
                <a href="../uploadfiles/help/specteacher.pdf" title="点击打开"
                   style="color:red;font-weight:bold;font-size:14px" target="_blank">
                        <%} else{%>
                    <a href="../uploadfiles/help/teacher.pdf" title="点击打开"
                       style="color:red;font-weight:bold;font-size:14px" target="_blank">
                        <%}%>
                        系统使用说明
                    </a>
            </div>
        </div>
    </div>
    <div id="navigation">
				<span><%=username%>&nbsp;[<%=identity%>]<%=userid%>&nbsp;
					[<a href="javascript:location.href='../ExitLogin.jsp'">退出</a>]&nbsp;在线&nbsp;<%=usercount%>&nbsp;人
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</span>
        <ul>
            <li><a href="#">我的课题</a></li>
            <%if (identity.equals("专业负责人")) {%>
            <li><a href="#">专业管理</a></li>
            <%} %>
            <li><a href="#">盲审课题</a></li>
            <li><a href="#">盲审论文</a></li>
            <li><a href="#">周总结管理</a></li>
            <li><a href="#">个人信息</a></li>
            <li><a href="#">课题库</a></li>
        </ul>
    </div>
    <%--			<label for="tutorid"><input style="display: none" id="tutorid" value='<%=userid%>>'/></label>--%>
    <div id="content">
        <div id="mysubject">
            <table class="mtable2">
                <caption>我的课题列表&nbsp;&nbsp;<input type="button" value="刷新" id="mysubrefresh"/><img
                        src="../images/loading.gif" id="loading"/></caption>
                <tr>
                    <th>序号</th>
                    <th>课题名</th>
                    <th>指导教师</th>
                    <th>课题地点</th>
                    <th>适合专业</th>
                    <th>课题状态</th>
                    <th>操作</th>
                </tr>
                <tbody id="tbmysubject"></tbody>
            </table>
            <br><input type="hidden" id="highlighttr" value=""/>
            <input type="button" onclick="subject_showwin('',1)" value="申报新课题"/>&nbsp;&nbsp;您目前课题数为<span
                id="currsubnum"></span>，最多可以申报<span id="maxsubnum"></span>个课题。
            &nbsp;&nbsp;&nbsp;&nbsp;
            <input type="button" onclick="printtaskbooks('<%=userid%>')" value="打印全部任务书" id="printtaskbooks"/>
            <img src="../images/loading.gif" style="display:none">
            <span class="flag"></span>
            <div id="subjectedt" class="win">
                <jsp:include page="SubjectEdt.jsp" flush="true">
                    <jsp:param name="userid" value="<%=userid%>"/>
                    <jsp:param name="username" value="<%=username%>"/>
                    <jsp:param name="graduateweeknum" value="<%=graduateweeknum%>"/>
                </jsp:include>
            </div>
            <!--查看审核意见-->
            <div id="viewoptions" class="win">
                <div class="titlebar">
                    <span class="title">查看专业审核意见</span>
                    <span class="close" onclick="hideDialog('#viewoptions')" title="关闭">X</span>
                </div>
                <div class="content">
                </div>
            </div>
            <!--选择学生窗口-->
            <div id="pickstudent" class="win">
                <div class="titlebar">
                    <span class="title">可选学生列表</span>
                    <span class="close" onclick="hideDialog('#pickstudent')" title="关闭">X</span>
                </div>
                <div>课题名：<span id="subname0"></span></div>
                <div class="content">
                    <table class="mtable2">
                        <!--<tr><th>序号</th><th>班级</th><th>学号</th><th>姓名</th><th>操作</th></tr>-->
                        <tbody id="tbstudents"></tbody>
                    </table>
                </div>
            </div>
            <!--查看设计情况-->
            <div id="viewdesign" class="win">
                <div class="titlebar">
                    <span class="title"><span id="stuid"></span>毕业设计文档</span>
                    <span class="close" onclick="hideDialog('#viewdesign')" title="关闭">X</span>
                </div>
                <div class="content">
                    <table class="mtable2">
                        <tbody id="tbdesign">
                        <tr name="paperblind">
                            <td>1</td>
                            <td><a href="#">毕业设计说明书(论文)[盲审]</a></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr name="paper">
                            <td>2</td>
                            <td><a href="#">毕业设计说明书(论文)</a></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr name="translation">
                            <td>3</td>
                            <td><a href="#">外文文献及翻译<a href="#"></td>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr name="sourcecode">
                            <td>4</td>
                            <td><a href="#">程序源代码（开题报告）<a href="#"></td>
                            <td></td>
                            <td></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <%if (identity.equals("专业负责人")) {%>
        <div id="specsubmag" style="display:none">
            <jsp:include page="SpecSubMag.jsp" flush="true">
                <jsp:param name="specid" value="<%=specid%>"/>
            </jsp:include>
        </div>
        <%} %>
        <!-- 显示盲审课题窗口 -->
        <div id="reviewsubjectdiv" style="display:none">
            <jsp:include page="ReviewSubject.jsp" flush="true"/>
        </div>
        <!-- 显示盲审论文窗口 -->
        <div id="reviewpaperdiv" style="display:none">
            <jsp:include page="ReviewPaper.jsp" flush="true"/>
        </div>
        <!-- 显示周总结管理窗口 -->
        <div id="weeksummanage" style="display:none">
            <jsp:include page="WeekSumMag.jsp" flush="true">
                <jsp:param name="userid" value="<%=userid%>"/>
            </jsp:include>
        </div>
        <!-- 显示修改个人信息窗口 -->
        <div id="selfinfo" style="display:none">
            <jsp:include page="TeaPersonalInfo.jsp" flush="true">
                <jsp:param name="userid" value="<%=userid%>"/>
                <jsp:param name="username" value="<%=username%>"/>
            </jsp:include>
        </div>
        <!-- 课题库管理（课题历史+暂存课题）窗口 -->
        <div id="subjectLibnfo" style="display:none">
            <jsp:include page="subjectLib/SubjectLibMag.jsp" flush="true">
                <jsp:param name="userid" value="<%=userid%>"/>
            </jsp:include>
        </div>
    </div>
    <jsp:include page="../footer.jsp" flush="true"/>
</div>
</body>
</html>