package servlet;

import bean.MyWeekSumBean;
import bean.StusubBean;
import bean.SubjectBean;
import bean.TeacherBean;
import bpo.*;
import com.FileUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class WeekSumServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        String result = "";
        String errmsg = "";
        String mode = request.getParameter("mode");

        if (mode.equals("generateBlankWeekup")) {//为每个学生生成空白周总结
            String stuid = request.getParameter("stuid");
            try {
                WeekSumBpo weeksumbpo = new WeekSumBpo();
                weeksumbpo.generateBlankWeekup(stuid);

            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("initStartGraduate")) {//开始毕业设计操作（生成周总结、初始化文档提交表）
            try {
                WeekSumBpo weeksumbpo = new WeekSumBpo();
                weeksumbpo.initStartGraduate();
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("fillInWeekupForStu")) {//学生填写周总结
            //接收输入的修改数据
            String stuid = request.getParameter("stuid");
            int weekorder = Integer.valueOf(request.getParameter("weekorder"));
            String thiscontent = request.getParameter("thiscontent");
            String support = request.getParameter("support");
            String nextcontent = request.getParameter("nextcontent");
            MyWeekSumBean myweeksum = new MyWeekSumBean();
            myweeksum.setStuid(stuid);
            myweeksum.setWeekorder(weekorder);
            myweeksum.setThiscontent(thiscontent);
            myweeksum.setSupport(support);
            myweeksum.setNextcontent(nextcontent);
            try {
                WeekSumBpo weeksumbpo = new WeekSumBpo();
                weeksumbpo.fillInWeekupForStu(myweeksum);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("fillInWeekupForTea")) {//教师批阅
            String stuid = request.getParameter("stuid");
            int weekorder = Integer.valueOf(request.getParameter("weekorder"));
            String tutorreply = request.getParameter("tutorreply");
            String tutorreview = request.getParameter("tutorreview");
            MyWeekSumBean myweeksum = new MyWeekSumBean();
            myweeksum.setStuid(stuid);
            myweeksum.setWeekorder(weekorder);
            myweeksum.setTutorreply(tutorreply);
            myweeksum.setTutorreview(tutorreview);
            try {
                WeekSumBpo weeksumbpo = new WeekSumBpo();
                weeksumbpo.fillInWeekupForTea(myweeksum);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("getWeekupByWeek")) {//按周次获得指定学生的周总结
            String stuid = request.getParameter("stuid");
            int weekorder = Integer.valueOf(request.getParameter("weekorder"));
            try {
                WeekSumBpo weeksumbpo = new WeekSumBpo();
                MyWeekSumBean myweeksum = weeksumbpo.getWeekupByWeek(stuid, weekorder);
                Gson gson = new Gson();
                result = gson.toJson(myweeksum);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("exportalltask")) {//导出所有任务书
            try {
                ExcelBpo exbpo = new ExcelBpo();
                TeacherBpo tbpo = new TeacherBpo();
                SubjectBpo subbpo = new SubjectBpo();
                List<TeacherBean> teachers = tbpo.getAllinfo();
                for (TeacherBean teacher : teachers) {
                    String tid = teacher.getTid();
                    List<SubjectBean> subjects = subbpo.getAllinfo(tid);
                    if (subjects.size() == 0) continue;
                    exbpo.exporttaskbooksBytid(tid);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (mode.equals("exportallprocess")) {//导出进程表
            try {
                ExcelBpo exbpo = new ExcelBpo();
                TeacherBpo tbpo = new TeacherBpo();
                StusubBpo subbpo = new StusubBpo();
                List<TeacherBean> teachers = tbpo.getAllinfo();
                for (TeacherBean teacher : teachers) {
                    String tid = teacher.getTid();
                    List<StusubBean> subjects = subbpo.getStusubBytid(tid);
                    if (subjects.size() == 0) continue;

                    String processpath = "D://bysj/temp/" + tid + "/";
                    FileUtil.Mkdir(processpath);
                    for (StusubBean sub : subjects) {
                        String stuid = sub.getStuid();
                        if (stuid == null || stuid.equals("")) continue;
                        exbpo.exportProgressTableByStu0(processpath, stuid);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            errmsg = mode + "未定义";
        }
        if (result.equals("")) {
            response.getWriter().write("{\"errmsg\":\"" + errmsg + "\"}");
        } else {
            response.getWriter().write("{\"result\":" + result + ",\"errmsg\":\"" + errmsg + "\"}");
        }
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);

    }
}
