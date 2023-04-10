package servlet;

import bean.UserBean;
import bpo.UserBpo;
import com.MD5Util;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        //获得form参数
        String usertype = request.getParameter("usertype");
        String userid = request.getParameter("userid");
        String userpwd = MD5Util.md5Mix(request.getParameter("userpwd"));
//		usertype=new String(usertype.getBytes("ISO-8859-1"),"utf-8");//解决中文乱码
        //响应内容
        String result = "";
        HttpSession session = request.getSession();
        UserBean user = new UserBean();
        user.setUsertype(usertype);
        user.setUserid(userid);
        user.setUserpwd(userpwd);
        try {
            UserBpo userbpo = new UserBpo();
            userbpo.isexisted(user);

            session.setAttribute("userid", user.getUserid());
            session.setAttribute("usertype", user.getUsertype());
            session.setAttribute("username", userbpo.getinfo_userid(user.getUserid()).getUsername());
        } catch (Exception e) {
            result = e.getMessage();
        }

        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");

        Gson gson = new Gson();
        String json = gson.toJson(result);
        response.getWriter().write(json);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);

    }
}
