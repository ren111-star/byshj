package servlet;

import bean.StudentBean;
import bean.SubSubmitBean;
import bpo.StudentBpo;
import bpo.SubSubmitBpo;
import bpo.SysarguBpo;
import com.FileUtil;
import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SubSubmitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        String result = "";
        String errmsg = "";
        String mode = request.getParameter("mode");//操作类型
        String stuid = request.getParameter("stuid");
        String doctype = request.getParameter("doctype");
        SubSubmitBpo subsubmitbpo = new SubSubmitBpo();

        if (mode.equals("updateDoc")) {//上传文件时更新数据库
            try {
                //上传文件前，判断文件状态
                String[] docstatus = subsubmitbpo.getUploadstatus(stuid, doctype).split("|");
                if (docstatus[0].equals("2") || docstatus[0].equals("3")) {//已归档或等待盲审状态
                    throw new Exception("文档当前状态下不允许重新提交!");
                }
                //获得公共文件路径
                String commonfilepath = "";
                SysarguBpo sysargubpo = new SysarguBpo();
                commonfilepath = sysargubpo.getSysargu("commonfilepath").getArguvalue();
                StudentBean student = null;

                StudentBpo studentbpo = new StudentBpo();
                student = studentbpo.getBystuid(stuid);

                //判断上传的文件后缀名
                String uploadsuffix = "rar";
                if (doctype.equals("paperblind") || doctype.equals("paper") || doctype.equals("translation"))
                    uploadsuffix = "pdf";

                //根据doctype确定文件路径
                String filedir = "";
                String newfilename = stuid;
                if (doctype.equals("paperblind")) {//盲审论文
                    filedir = commonfilepath + "paperblind";
                } else {
                    filedir = commonfilepath + student.getClassname() + "-" + stuid + "-" + student.getSname();
                    if (doctype.equals("paper") || doctype.equals("translation")) {
                        filedir = filedir + "/毕业设计说明书及译文";
                        if (doctype.equals("paper")) {
                            newfilename = "说明书(论文)";
                        } else {
                            newfilename = "译文";
                        }
                    } else {
                        filedir = filedir + "/程序代码";
                        newfilename = "程序代码";
						 /*try{//20150618修改，subname改由服务器端取出.若由浏览器传递，则包含特殊字符的课题名将会导致参数错误。
							 SubjectBpo subbpo=new SubjectBpo();
							 newfilename=subbpo.getSubjectByStuPicked(stuid).getSubname();
						 }catch(Exception e){
							 errmsg=e.getMessage();
						 }*/
                        //newfilename=new String(request.getParameter("subname").getBytes("ISO-8859-1"),"utf-8");

                    }
                }
                //接收上传文件
                boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                //首先得到文件
                if (isMultipart == true) {
                    DiskFileItemFactory factory = new DiskFileItemFactory();//
                    //最大缓存
                    factory.setSizeThreshold(5 * 1024);
                    //设置临时文件目录
                    factory.setRepository(new File("D://temp"));
                    ServletFileUpload upload = new ServletFileUpload(factory);
                    //文件最大上限(200M)
                    upload.setSizeMax(200 * 1024 * 1024);

                    upload.setHeaderEncoding("UTF-8");//解决文件名中文乱码
                    Iterator<FileItem> iter = upload.parseRequest(request).iterator();
                    if (iter.hasNext()) {
                        FileItem item = iter.next();
                        FileUtil.MkdirWithoutIfExisted(filedir);
                        String filename = item.getName();
                        String fileext = filename.substring(filename.lastIndexOf(".") + 1, filename.length());//文件后缀名

                        if (!fileext.equals(uploadsuffix)) throw new Exception("上传文件后缀名不正确！");

                        newfilename = newfilename + "." + fileext;

                        File savedFile = new File(filedir, newfilename);
                        item.write(savedFile);
                    }
                } else {
                    throw new Exception("上传文件表单属性应为enctype='multipart/form-data'");
                }
                //再修改数据库
                subsubmitbpo.updateDoc(stuid, filedir + "/" + newfilename, doctype);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("getUploadstatus")) {//获得每个文档的上传状态
            try {
                result = subsubmitbpo.getUploadstatus(stuid, doctype);
                Gson gson = new Gson();
                result = gson.toJson(result);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("submitDocForTea")) {//将文档提交归档、提交盲审或设置已盲审标志
            String status = request.getParameter("status");//将要设置的文档状态
            try {
                subsubmitbpo.submitDocForTea(stuid, doctype, status);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("getStuDocsBySpec")) {//按专业查看学生提交文档情况
            String specid = request.getParameter("specid");
            String classname = request.getParameter("classname");
            String sname = request.getParameter("sname");
            try {
                List<SubSubmitBean> subsubmits = new ArrayList<SubSubmitBean>();
                subsubmits = subsubmitbpo.getStuDocsBySpec(specid, classname, sname);
                Gson gson = new Gson();
                result = gson.toJson(subsubmits);
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
        // TODO Auto-generated method stub
        doGet(request, response);
    }
}


