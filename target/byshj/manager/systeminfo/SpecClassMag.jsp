<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" errorPage="../../error.jsp" %>
<!--专业班级管理界面-->
<script src="../../js/manager/systeminfo/SpecClassMag.js" type="text/javascript"></script>
<div id="specclassmagui">
    <ul class="tabs">
        <li><a href="#">专业管理</a></li>
        <li><a href="#">班级管理</a></li>
    </ul>
    <div name="content">
        <div id="specmagui">
            <table name="spectable" class="mtable2">
                <tr>
                    <th>专业号</th>
                    <th>专业名</th>
                    <th>负责人编号</th>
                    <th>负责人名</th>
                    <th>操作</th>
                </tr>
                <tbody name="spectb"></tbody>
                <tbody name="spectb1"></tbody>
            </table>
        </div>
        <div id='classmagui'>
            <br>
            <form name="classform" action="../ClassServlet?mode=gets" method="post" style="margin:1px">
                专业<select name="specid"></select>&nbsp;&nbsp;
                <input type="submit" value="查询"/>
                <img alt="" src="../../images/loading.gif" name="loadingimg" style="display:none">
                <span name="errmsg0" class="flag"></span>
            </form>
            <table name="classtable" class="mtable2">
                <tr>
                    <th>序号</th>
                    <th>专业名</th>
                    <th>班级名</th>
                    <th>入学年份</th>
                    <th>毕业年份</th>
                    <th>操作</th>
                </tr>
                <tbody name="classtb"></tbody>
                <tr>
                    <td style="width:30px"><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                    <td style="width:80px"><br><select name="specid"></select></td>
                    <td style="width:100px"><br><input type="text" name="userid" class="text"/></td>
                    <td style="width:80px"><br><input type="text" name="userid" class="text"/></td>
                    <td style="width:80px"><br><input type="text" name="username" class="text"/></td>
                    <td><br><input type="button" name="add" value="增加"/>&nbsp;&nbsp;<span name="errmsg"
                                                                                            class="flag"></span></td>
                </tr>
            </table>
        </div>
    </div>
</div>
