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

public class Download extends HttpServlet {

    /**
     * Constructor of the object.
     */
    public Download() {
        super();
    }

    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    private ServletConfig config;

    final public void init(ServletConfig config) throws ServletException {
        this.config = config;
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        //得到全路径的文件名
        String temp_p = request.getParameter("downloadFileName");
        byte[] temp_t = temp_p.getBytes("ISO8859_1");
        String fileName = new String(temp_t, "GBK");
        String rootDir = "";
        try {
            SmartUpload mySmartUpload = new SmartUpload();
            SysarguBpo sb = new SysarguBpo();
            rootDir = sb.getSysargu("filepath").getArguvalue();
            fileName = rootDir + fileName;
            mySmartUpload.initialize(config, request, response);
            mySmartUpload.setContentDisposition(null);
            mySmartUpload.downloadFile(fileName);
        } catch (java.io.FileNotFoundException e) {
            //服务器上未找到要下载的文件
            response.setContentType("text/html;charset=gb2312");
            //response.se.setCharacterEncoding("gb2312");
            PrintWriter out = response.getWriter();
            // TODO: 非法的类型
//            out.println("<script>parent.alert(\"未找到要下载的文件\");</script>");
        } catch (Exception e) {
            //ClientAbortException:  java.net.SocketException
            //用户点击取消下载按钮后激发ClientAbortException,无法单独捕获这个异常，只好捕获Exception。
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}
