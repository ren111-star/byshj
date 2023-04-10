package servlet;

import bean.UserBean;
import bpo.UserBpo;
import com.MD5Util;
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

public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        String result = "";
        String errmsg = "";
        String mode = request.getParameter("mode");

        if (mode.equals("edt")) {
            String userjson = request.getParameter("user");
            String flag = request.getParameter("flag");//1为增加，2修改
            try {
                Type type = new TypeToken<UserBean>() {
                }.getType();
                Gson gson = new Gson();
                UserBean user = gson.fromJson(userjson, type);
                UserBpo userbpo = new UserBpo();
                if (flag.equals("1")) {
                    userbpo.addinfo(user);
                } else {
                    userbpo.modifyinfo(user);
                }
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("modifypwd")) {//修改个人密码
            //接收输入的修改数据
            String userid = request.getParameter("userid");
            String oldpwd = MD5Util.md5Mix(request.getParameter("oldpwd"));
            String newpwd = MD5Util.md5Mix(request.getParameter("newpwd"));
            try {
                UserBpo userbpo = new UserBpo();
                userbpo.modifyPwd(userid, oldpwd, newpwd);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("get")) {
            String userid = request.getParameter("userid");
            try {
                UserBpo userbpo = new UserBpo();
                UserBean user = userbpo.getinfo_userid(userid);
                Gson gson = new Gson();
                result = gson.toJson(user);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("getSysusers")) {//
            String userid = request.getParameter("userid");
            List<UserBean> users = new ArrayList<UserBean>();
            try {
                UserBpo userbpo = new UserBpo();
                users = userbpo.getSysusers(userid);
                Gson gson = new Gson();
                result = gson.toJson(users);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("del")) {
            String userid = request.getParameter("userid");
            try {
                UserBpo userbpo = new UserBpo();
                userbpo.deleteinfo(userid);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("initializepwd")) {//将密码初始化为123456
            String userid = request.getParameter("userid");
            try {
                UserBpo userbpo = new UserBpo();
                userbpo.initializepwd(userid);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("test")) {//测试用 用完删掉
            try {
                UserBpo userbpo = new UserBpo();
                userbpo.test();
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
