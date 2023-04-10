package servlet;

import bpo.SysarguBpo;
import com.jspsmart.upload.SmartUpload;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class FileDownloadServlet0 extends HttpServlet {
    private ServletConfig config;

    final public void init(ServletConfig config) throws ServletException {
        this.config = config;
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        SysarguBpo sysarg = new SysarguBpo();
        String tempfilepath = "";
        try {
            tempfilepath = sysarg.getSysargu("tempfilepath").getArguvalue();
            if (tempfilepath.equals("")) {
                response.setContentType("text/html;charset=utf-8");
                response.setCharacterEncoding("utf-8");
                PrintWriter out = response.getWriter();
                out.println("<script>parent.alert(\"未定义临时文件路径！\");</script>");
            }
        } catch (Exception e) {
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script>parent.alert(\"" + e.getMessage() + "\");</script>");
        }

        //得到全路径的文件名
        String filenamewithallpath = "";
        if (request.getParameter("filenamewithallpath") != null) {
            filenamewithallpath = request.getParameter("filenamewithallpath");//带有全路径的文件名
        }
        if (request.getParameter("tempfilename") != null) {
            filenamewithallpath = tempfilepath + request.getParameter("tempfilename");//带有全路径的文件名
        }
        SmartUpload mySmartUpload = new SmartUpload();
        try {
            mySmartUpload.initialize(config, request, response);
            mySmartUpload.setContentDisposition(null);
            mySmartUpload.downloadFile(filenamewithallpath);
        } catch (java.io.FileNotFoundException e) {
            //服务器上未找到要下载的文件
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();
            // TODO: 非法的结束
//            out.println("<script>parent.alert(\"未找到要下载的文件\");</script>");
        } catch (Exception e) {
            //ClientAbortException:  java.net.SocketException
            //用户点击取消下载按钮后激发ClientAbortException,无法单独捕获这个异常，只好捕获Exception。
        }
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}