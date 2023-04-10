package servlet;

import bean.TeacherBean;
import bpo.TeacherBpo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TeacherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        String result = "";
        String errmsg = "";
        String mode = request.getParameter("mode");

        if (mode.equals("edt")) {//管理员增加、修改个人基本信息
            String teacherjson = request.getParameter("teacher");
            String flag = request.getParameter("flag");//1为增加，2修改
            try {
                Type type = new TypeToken<TeacherBean>() {
                }.getType();
                Gson gson = new Gson();
                TeacherBean teacher = gson.fromJson(teacherjson, type);
                TeacherBpo teacherbpo = new TeacherBpo();
                if (flag.equals("1")) {
                    teacherbpo.addinfo(teacher);
                } else {
                    teacherbpo.modifyinfo(teacher);
                }
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("personaledt")) {//教师修改个人信息
            //接收表单中的教师数据
            TeacherBean teacher = new TeacherBean();
            teacher.setTid(request.getParameter("tid"));
            teacher.setEmail(request.getParameter("email"));
            teacher.setTelphone(request.getParameter("telphone"));
            teacher.setTsex(request.getParameter("tsex"));
            teacher.setStudydirect(request.getParameter("studydirect"));
            teacher.setTdegree(request.getParameter("tdegree"));
            teacher.setTpost(request.getParameter("tpost"));
            try {
                TeacherBpo teacherbpo = new TeacherBpo();
                teacherbpo.modifypersonalinfo(teacher);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("get")) {//教师获得个人信息
            String tid = request.getParameter("tid");
            try {
                TeacherBpo teacherbpo = new TeacherBpo();
                TeacherBean teacher = teacherbpo.getBytid(tid);
                Gson gson = new Gson();
                result = gson.toJson(teacher);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("gets")) {//管理员按教学单位和姓名查询教师
            String tname = request.getParameter("tname");
            String tdept = request.getParameter("tdept");
            List<TeacherBean> teachers = new ArrayList<TeacherBean>();
            try {
                TeacherBpo teacherbpo = new TeacherBpo();
                teachers = teacherbpo.getAllinfo(tdept, tname);
                Gson gson = new Gson();
                result = gson.toJson(teachers);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("getAllinfoWithSubdir")) {//管理员按教学单位和姓名查询教师课题方向
            String tname = request.getParameter("tname");
            String tdept = request.getParameter("tdept");
            List<TeacherBean> teachers = new ArrayList<TeacherBean>();
            try {
                TeacherBpo teacherbpo = new TeacherBpo();
                teachers = teacherbpo.getAllinfoWithSubdir(tdept, tname);
                Gson gson = new Gson();
                result = gson.toJson(teachers);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("edtSubDirByTid")) {//设置教师课题方向
            String tid = request.getParameter("tid");
            String subdirectionsjson = request.getParameter("subdirections");
            try {
                Type type = new TypeToken<List<String>>() {
                }.getType();
                Gson gson = new Gson();
                List<String> subdirections = gson.fromJson(subdirectionsjson, type);
                TeacherBpo teacherbpo = new TeacherBpo();
                teacherbpo.edtSubDirByTid(tid, subdirections);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("del")) {//管理员删除个人信息
            String tid = request.getParameter("tid");
            try {
                TeacherBpo teacherbpo = new TeacherBpo();
                teacherbpo.deleteinfo(tid);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("assignSubToTeaForReview")) {//为评审教师分配课题（题目审核时用）
            try {
                TeacherBpo teacherbpo = new TeacherBpo();
                Gson gson = new Gson();
                result = gson.toJson(teacherbpo.assignSubToTeaForReview());
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("getAllsubnum")) {//查询教师课题数目
            try {
                TeacherBpo teacherbpo = new TeacherBpo();
                Gson gson = new Gson();
                result = gson.toJson(teacherbpo.getAllsubnum());
            } catch (Exception e) {
                errmsg = e.getMessage();
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
