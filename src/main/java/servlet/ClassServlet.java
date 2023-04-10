package servlet;

import bean.ClassBean;
import bpo.ClassBpo;
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

public class ClassServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        String result = "";
        String errmsg = "";
        String mode = request.getParameter("mode");
        if (mode.equals("edt")) {//管理员增加、修改班级信息
            String clasjson = request.getParameter("clas");
            String flag = request.getParameter("flag");//1为增加，2修改
            try {
                Type type = new TypeToken<ClassBean>() {
                }.getType();
                Gson gson = new Gson();
                ClassBean clas = gson.fromJson(clasjson, type);
                ClassBpo classbpo = new ClassBpo();
                if (flag.equals("1")) {
                    classbpo.addinfo(clas);
                } else {
                    classbpo.modifyinfo(clas);
                }
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("gets")) {//得到班级列表
            try {
                String specid = request.getParameter("specid");
                List<ClassBean> clas = new ArrayList<ClassBean>();

                ClassBpo classbpo = new ClassBpo();
                clas = classbpo.getAllinfo(specid);
                Gson gson = new Gson();
                result = gson.toJson(clas);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("del")) {//删除班级
            String classname = request.getParameter("classname");
            try {
                ClassBpo classbpo = new ClassBpo();
                classbpo.deleteinfo(classname);
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
