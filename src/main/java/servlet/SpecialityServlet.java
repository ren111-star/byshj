package servlet;

import bean.SpecialityBean;
import bpo.SpecialityBpo;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpecialityServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        String result = "";
        String errmsg = "";
        String mode = request.getParameter("mode");
        if (mode.equals("gets")) {
            List<SpecialityBean> specs = new ArrayList<SpecialityBean>();
            try {
                SpecialityBpo specbpo = new SpecialityBpo();
                specs = specbpo.getAllinfo();
                Gson gson = new Gson();
                result = gson.toJson(specs);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("edtspecmag")) {
            String specid = request.getParameter("specid");
            String specmagtid = request.getParameter("specmagtid");
            try {
                SpecialityBpo specbpo = new SpecialityBpo();
                specbpo.modifySpecMag(specid, specmagtid);
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
