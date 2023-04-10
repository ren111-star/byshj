package servlet;

import bean.StusubBean;
import bpo.StusubBpo;
import bpo.UserBpo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StusubServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        String result = "";
        String errmsg = "";
        String mode = request.getParameter("mode");
        if (mode.equals("submitpickresultfirst")) {//提交学生初选结果
            String pickedsubarr = request.getParameter("pickedsubarr");
            String stuid = request.getParameter("stuid");
            try {
                Type type = new TypeToken<List<StusubBean>>() {
                }.getType();
                Gson gson = new Gson();
                List<StusubBean> pickedsubs = gson.fromJson(pickedsubarr, type);
                StusubBpo stusubbpo = new StusubBpo();
                stusubbpo.submitPickResultFirst(stuid, pickedsubs);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("getpickedsubsbystu")) {//获得学生已初选的课题列表
            String stuid = request.getParameter("stuid");
            try {
                List<StusubBean> pickedsubs = new ArrayList<StusubBean>();

                StusubBpo stusubbpo = new StusubBpo();
                pickedsubs = stusubbpo.getPickedsSubsbyStu(stuid);
                Gson gson = new Gson();
                result = gson.toJson(pickedsubs);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("delsubsbystu")) {//删除学生选题（未确认选题）
            String stuid = request.getParameter("stuid");
            try {
                StusubBpo stusubbpo = new StusubBpo();
                stusubbpo.delSubByStu(stuid);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("changeTutorForStu")) {//学生换导师
            String stuid = request.getParameter("stuid");
            String tutorid = request.getParameter("tutorid");
            try {
                StusubBpo stusubbpo = new StusubBpo();
                stusubbpo.changeTutorForStu(stuid, tutorid);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("setassignsubject")) {//结束选题，只有管理员才能使用该功能
            HttpSession session = request.getSession(true);
            String userid = (String) session.getAttribute("userid");
            try {
                UserBpo userbpo = new UserBpo();
                if (!userbpo.getinfo_userid(userid).getUsertype().equals("管理员")) {
                    throw new Exception("只有管理员才能使用该功能！");
                }
                StusubBpo stusubbpo = new StusubBpo();
                int stucount = stusubbpo.setAssignSubject();
                result = String.valueOf(stucount);
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
