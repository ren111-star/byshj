package servlet;

import com.SyscodeBean;
import com.SyscodeBpo;
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

public class SyscodeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        String result = "";
        String errmsg = "";
        String mode = request.getParameter("mode");
        if (mode.equals("getcodeByno")) {//按照codeno获得相应下拉列表框的值
            String codeno = request.getParameter("codeno");
            List<SyscodeBean> codes = new ArrayList<SyscodeBean>();
            try {
                SyscodeBpo syscode = new SyscodeBpo();
                codes = syscode.getcodeByno(codeno);
                Gson gson = new Gson();
                result = gson.toJson(codes);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("gets")) {//获得代码表中的所有代码值
            String codename = request.getParameter("codename");
            List<SyscodeBean> codes = new ArrayList<SyscodeBean>();
            try {
                SyscodeBpo syscode = new SyscodeBpo();
                codes = syscode.getAllinfo(codename);
                Gson gson = new Gson();
                result = gson.toJson(codes);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("edt")) {//增加、修改code
            String codejson = request.getParameter("code");
            try {
                Type type = new TypeToken<SyscodeBean>() {
                }.getType();
                Gson gson = new Gson();
                SyscodeBean code = gson.fromJson(codejson, type);
                SyscodeBpo codebpo = new SyscodeBpo();
                codebpo.addOrmod(code);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("del")) {//删除code
            String codeid = request.getParameter("codeid");
            try {
                SyscodeBpo codebpo = new SyscodeBpo();
                codebpo.deleteinfo(codeid);
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
