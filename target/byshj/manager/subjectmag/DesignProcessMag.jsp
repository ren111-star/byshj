<%@page import="bpo.SysarguBpo" %>
<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../../error.jsp" %>
<%
    SysarguBpo sysargubpo = new SysarguBpo();
    boolean startgraduateflag = sysargubpo.ifStartGraduate();
    String startdate = sysargubpo.getSysargu("startdate").getArguvalue();
%>
<!--过程管理界面-->
<table class="mtable2">
    <tr>
        <th>过程名</th>
        <th>操作</th>
        <th>说明</th>
    </tr>
    <tr>
        <td>系统初始化</td>
        <td>归档年份：<input type="text" name="retrieveyear" style="width:40px"/> <span class="flag">* yyyy</span><br/>
            <input type="button" name="initsystembtn" value="开始初始化" onclick="initsystem()"/>
            <img src="../../images/loading.gif" style="display:none"/>
        </td>
        <td>对已选题的课题转入历史，未选题的转入暂存<br/>
            将进程表维护到历史库和暂存库<br/>
            清空当前库<br/>
            配置系统参数（归档年份和毕业设计开始时间）<br/>
        </td>
    </tr>
    <tr>
        <td>教师申报课题</td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>专业负责人审核课题</td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>教学秘书发布课题</td>
        <td></td>
        <td></td>
    </tr>
    <%if (!startgraduateflag) {%>
    <tr>
        <td>学生-教师双选课题</td>
        <td><input type="button" name="setassignsubjectbtn" value="设置指派课题" onclick="setassignsubject()"/>
            <img alt="" src="../../images/loading.gif" style="display:none"/>
        </td>
        <td>结束选题成功后，除“已选”状态的学生外，其余学生都将被设置成“需指派”状态，即由专业负责人为其指派选题。</td>
    </tr>
    <tr>
        <td>开始毕业设计</td>

        <td><input type="button" name="startgraduatebtn" value="开始毕业设计" onclick="startgraduate()"/>
            <img alt="" src="../../images/loading.gif" style="display:none"/>
        </td>
        <td>为参加毕业设计的每个学生生成若干份空白周总结，份数为系统参数中的“毕业设计周数”；</br>
            初始化毕业设计文档提交表。<span class="flag">[毕业设计开始前一周使用此功能！]</span>
        </td>
    </tr>
    <%} else {%>
    <tr>
    <tr>
        <td>学生-教师双选课题</td>
        <td></td>
        <td>选题已结束</td>
    </tr>
    <tr>
        <td>开始毕业设计</td>
        <td></td>
        <td>毕业设计已经开始。开始时间为[<%=startdate%>]</td>
        <%}%>
    </tr>
</table>
		

