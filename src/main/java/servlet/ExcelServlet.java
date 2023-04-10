package servlet;

import bpo.ExcelBpo;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class ExcelServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        String result = "";
        String errmsg = "";
        String mode = request.getParameter("mode");
        try {
            ExcelBpo excelbpo = new ExcelBpo();
            if (mode.equals("importstudents")) {//导入学生基本信息
                InputStream stream = null;
                try {
                    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                    if (isMultipart == true) {
                        //首先得到文件的输入流，并不上传文件
                        ServletFileUpload upload = new ServletFileUpload();
                        upload.setHeaderEncoding("UTF-8");//解决文件名中文乱码
                        FileItemIterator iter = upload.getItemIterator(request);
                        if (iter.hasNext()) {
                            FileItemStream item = iter.next();
                            stream = item.openStream();
                            excelbpo.importstudents(stream);
                        }
                    } else {
                        throw new Exception("导入文件表单属性应为enctype='multipart/form-data'");
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    if (stream != null) stream.close();
                }
            } else if (mode.equals("exportstusublist")) {//导出学生课题明细表
                try {
                    String specid = request.getParameter("specid");
                    String classname = request.getParameter("classname");
                    excelbpo.exportstusublist(specid, classname);
                } catch (Exception e) {
                    throw e;
                }
            } else if (mode.equals("exportBlindReviewContentBySpec")) {//导出盲审意见
                try {
                    String specid = request.getParameter("specid");
                    String classname = request.getParameter("classname");
                    String sname = request.getParameter("sname");
                    excelbpo.exportBlindReviewContentBySpec(specid, classname, sname);
                } catch (Exception e) {
                    throw e;
                }
            } else if (mode.equals("exportProgressTableBySpec")) {//导出进程表
                try {
                    String specid = request.getParameter("specid");
                    String classname = request.getParameter("classname");
                    String sname = request.getParameter("sname");

                    excelbpo.exportProgressTableBySpec(specid, classname, sname);
                } catch (Exception e) {
                    throw e;
                }
            } else if (mode.equals("exportProgressTableByStu")) {//导出进程表(学生个人)
                try {
                    String stuid = request.getParameter("stuid");
                    excelbpo.exportProgressTableByStu(stuid);
                } catch (Exception e) {
                    throw e;
                }
            } else if (mode.equals("exporttaskbook")) {//导出课题任务书
                try {
                    String subid = request.getParameter("subid");
                    excelbpo.exporttaskbook(subid);
                } catch (Exception e) {
                    throw e;
                }
            } else if (mode.equals("exporttaskbooksBytid")) {//导出指定教师全部任务书
                try {
                    String tid = request.getParameter("tid");
                    excelbpo.exporttaskbooksBytid(tid);
                } catch (Exception e) {
                    throw e;
                }
            } else if (mode.equals("importteachers")) {//导入教师基本信息
                InputStream stream = null;
                try {
                    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                    if (isMultipart == true) {
                        //首先得到文件的输入流，并不上传文件
                        ServletFileUpload upload = new ServletFileUpload();
                        upload.setHeaderEncoding("UTF-8");//解决文件名中文乱码
                        FileItemIterator iter = upload.getItemIterator(request);
                        if (iter.hasNext()) {

                            FileItemStream item = iter.next();
                            stream = item.openStream();
                            excelbpo.importteachers(stream);
                        }
                    } else {
                        throw new Exception("导入文件表单属性应为enctype='multipart/form-data'");
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    if (stream != null) stream.close();
                }
            } else {
                throw new Exception("调用方法'" + mode + "'不存在！");
            }
        } catch (Exception e) {
            errmsg = e.getMessage();
        }

        //响应内容
        if (result.equals("")) {
            response.getWriter().write("{\"errmsg\":\"" + errmsg + "\"}");
        } else {
            response.getWriter().write("{\"result\":" + result + ",\"errmsg\":\"" + errmsg + "\"}");
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}
