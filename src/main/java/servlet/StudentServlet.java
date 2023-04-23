package servlet;

import bean.StudentBean;
import bpo.StudentBpo;
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

public class StudentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        String result = "";
        String errmsg = "";
        String mode = request.getParameter("mode");
        if (mode.equals("gets")) {//获得学生列表
            String specid = request.getParameter("speciality");
            String classname = request.getParameter("classname");
            String sname = request.getParameter("sname");
            try {
                List<StudentBean> students = new ArrayList<StudentBean>();
                StudentBpo studentbpo = new StudentBpo();
                students = studentbpo.getAllinfo(specid, classname, sname);
                Gson gson = new Gson();
                result = gson.toJson(students);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("get")) {//根据学号获得学生基本信息
            String stuid = request.getParameter("stuid");
            try {
                StudentBpo studentbpo = new StudentBpo();
                StudentBean student = studentbpo.getBystuid(stuid);
                Gson gson = new Gson();
                result = gson.toJson(student);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("personaledt")) {//学生修改个人信息
            StudentBean student = new StudentBean();
            student.setStuid(request.getParameter("stuid"));
            student.setEmail(request.getParameter("email"));
            student.setTelephone(request.getParameter("telephone"));
            student.setSsex(request.getParameter("ssex"));
            try {
                StudentBpo studentbpo = new StudentBpo();
                studentbpo.modifypersonalinfo(student);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("edt")) {//增加学生
            String studentjson = request.getParameter("student");
            String flag = request.getParameter("flag");//1为增加，2修改
            try {
                Type type = new TypeToken<StudentBean>() {
                }.getType();
                Gson gson = new Gson();
                StudentBean student = gson.fromJson(studentjson, type);
                StudentBpo studentbpo = new StudentBpo();
                if (flag.equals("1")) {
                    studentbpo.addinfo(student);
                } else {
                    studentbpo.modifyinfo(student);
                }
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("del")) {
            String stuid = request.getParameter("stuid");
            try {
                StudentBpo studentbpo = new StudentBpo();
                studentbpo.deleteinfo(stuid);
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
