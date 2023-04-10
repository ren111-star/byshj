package servlet;

import bean.SysarguBean;
import bpo.SysarguBpo;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SysarguServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        String result = "";
        String errmsg = "";
        String mode = request.getParameter("mode");
        if (mode.equals("getarguByname")) {//按照arguname获得相应系统参数条目
            String arguname = request.getParameter("arguname");
            SysarguBean sysargu = new SysarguBean();
            try {
                SysarguBpo sysargubpo = new SysarguBpo();
                sysargu = sysargubpo.getSysargu(arguname);
                Gson gson = new Gson();
                result = gson.toJson(sysargu);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("gets")) {//获得系统参数表中的所有参数条目
            List<SysarguBean> argus = new ArrayList<SysarguBean>();
            try {
                SysarguBpo sysargubpo = new SysarguBpo();
                argus = sysargubpo.getAllsysargu();
                Gson gson = new Gson();
                result = gson.toJson(argus);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("edt")) {//修改argu
            String arguname = request.getParameter("arguname");
            String arguvalue = request.getParameter("arguvalue");
            try {
                SysarguBpo sysargubpo = new SysarguBpo();
                sysargubpo.modifyArguByName(arguname, arguvalue);
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
