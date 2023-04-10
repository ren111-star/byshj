<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../../error.jsp" %>
<!--审核管理界面-->
<br/>
<div class="menu">
    <div class="title">&nbsp;&nbsp;</div>
    <br>
    <ul id="paperreviewul">
        <li><a href="#">论文盲审管理</a></li>
        <li><a href="#">论文盲审查询</a></li>
    </ul>
</div>
<div id="paperreviewpanes" class="rightcontent">
    <div id="paperblindreviewMagdiv" style="display:none">
        <div class="caption">论文盲审管理&nbsp;&nbsp;</div>
        <div><br>&nbsp;&nbsp;论文盲审阶段&nbsp;&nbsp;<input type="button" value="分配盲审论文"
                                                            name="assignsubtoteacher"/> &nbsp;&nbsp;<input type="button"
                                                                                                           value="查询盲审情况"
                                                                                                           name="searchblindinfo"/>
            <img alt="" src="../../images/loading.gif" name="loadingimg" style="display:none">
            <span name="errmsg0" class="flag"></span>
        </div>
        <table class="mtable2">
            <tr>
                <th>序号</th>
                <th>工号</th>
                <th>教师名</th>
                <th>提交论文数</th>
                <th>需评阅论文数</th>
                <th>未评阅论文数</th>
            </tr>
            <tbody name="teachertb"></tbody>
        </table>
        <div>
            <img src="../../images/loading.gif" name="loadingimgtb" style="display:none">
            <span name="errmsgtb" class="flag"></span>
        </div>

    </div>
    <div id="paperblindreviewSelectdiv" style="display:none">
        <div class="caption">论文盲审查询&nbsp;&nbsp;</div>
        <form id="paperreviewlistform" action="../ReviewPaperServlet?mode=getPaperReviewInfos" method="post"
              style="margin:5px">
            专业<select name="specid"></select>&nbsp;&nbsp;
            班级<select name="classname">
            <option value=""></option>
        </select>&nbsp;&nbsp;
            姓名<input type="text" class="text" name="sname" style="width:120px;"/>&nbsp;&nbsp;
            <input type="submit" value="查询"/>&nbsp;&nbsp;
            <img alt="" src="../../images/loading.gif" name="loadingimg" style="display:none">
            <span name="errmsg0" class="flag"></span>
        </form>
        <table class="mtable2">
            <tr>
                <th style="width:40px">序号</th>
                <th style="width:60px">班级</th>
                <th style="width:40px">学号</th>
                <th style="width:40px">姓名</th>
                <th style="width:380px">毕业设计（论文）题目</th>
                <th style="width:80px">指导教师</th>
                <th style="width:50px">评阅人</th>
                <th>操作</th>
            </tr>
            <tbody name="tbstudents"></tbody>
        </table>
    </div>
</div>
