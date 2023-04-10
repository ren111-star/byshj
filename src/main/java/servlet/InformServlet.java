package servlet;

import bean.InformBean;
import bpo.InformBpo;
import bpo.SysarguBpo;
import com.Date_String;
import com.FileUtil;
import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class InformServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        request.setCharacterEncoding("GBK");
        String rootDir = "";//SyscodeBpo.getServerPath();
        String errmsg = "";
        String result = "";
        String mode = request.getParameter("mode");
        HttpSession session = request.getSession();
        /*�ļ��ϴ�����*/
        String strfilePath = "uploadFiles/informAffix/";//D:/uploadFiles/informAffix/
        boolean pathsign = false;//���Ŀ¼�Ƿ��Ѿ�����
        String strExt = "";
        //��������ط�Ҫ�����޸�//////////////////////////////////////////////////////////////
        String affixFilePath = "";//�����ļ�·��
        int filecount = 0;//�ϴ��ļ���
        int filecount1 = 0;//ʵ���ϴ��ĸ����ļ���
        String serverpath = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort() + "/";
        try {
            FileUtil fu = new FileUtil();
            SysarguBpo sysargubpo = new SysarguBpo();
            rootDir = sysargubpo.getSysargu("filepath").getArguvalue();
            affixFilePath = rootDir + strfilePath;
            InformBpo informbpo = new InformBpo();
            InformBean bean = new InformBean();
            if (mode.equals("add")) {
                boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                String title = null;
                String content = null;
                String signid = null;
                String oldaffix = null;
                String Sqlpath = strfilePath;//�ϴ��ļ������ݿ��д�ŵ�·��
                List oldfile = new ArrayList();
                String time = new Date().toLocaleString();
                Object admin = session.getAttribute("username");
                if (isMultipart == true) {//������ͨ�ı���Ϣ�������ϴ��ļ�
                    FileItemFactory factory = new DiskFileItemFactory();
                    ServletFileUpload upload = new ServletFileUpload(factory);
                    upload.setHeaderEncoding("UTF-8");//����ļ�����������
                    List items = upload.parseRequest(request);
                    //List<FileItem> items=parseRequest;
                    Iterator iter = items.iterator();
                    affixFilePath += new Date_String().getStringDate();
                    Sqlpath += new Date_String().getStringDate();
                    int recNum = 0;//���ڱ���Ƿ�ִ�й������ݿ�Ĳ���
                    boolean old = false;//�ж��ļ����ǲ���Ҫ���½���
                    while (iter.hasNext()) {
                        FileItem item = (FileItem) iter.next();
                        if (item.isFormField()) {
                            String name0 = item.getFieldName();
                            if (name0.equals("title")) {
                                title = item.getString();
                            }
                            if (name0.equals("signid")) {
                                signid = item.getString();
                            }
                            if (name0.equals("content")) {
                                content = item.getString();
                            }
                        } else {
                            //ֻҪ�и���,�Ҹ���Ŀ¼�����ڣ���Ϊ��֪ͨ��������Ŀ¼
                            if (recNum == 0) {
                                if (signid.equals("") || signid.equals("null")) {
                                    informbpo.AddInform(title, content, time, admin.toString(), Sqlpath);
                                    recNum++;
                                } else {
                                    int id = Integer.parseInt(signid);
                                    affixFilePath = "D:/" + informbpo.getPathbyId(id);
                                    informbpo.UpdateInform(id, title, content, time, admin.toString());
                                    recNum++;
                                    old = true;
                                }
                            }
                            String name = item.getName();
                            if (name.equals("")) {
                                continue;
                            }

                            if (!pathsign && !old) {

                                try {
                                    FileUtil.Mkdir(affixFilePath);
                                    pathsign = true;
                                } catch (Exception e) {
                                    errmsg = "����Ŀ¼'" + affixFilePath + "'����ʧ�ܣ�" + e.getMessage();
                                }
                            }
                            File fullFile = new File(item.getName());
                            File savedFile = new File(affixFilePath, fullFile.getName());
                            item.write(savedFile);
                        }
                    }
                } else {
                    errmsg = "�����ļ�������ӦΪenctype='multipart/form-data'";
                }
            } else if (mode.equals("gets")) {

                String informid = request.getParameter("wyxinform");
                //String method=request.getParameter("method");
                List<InformBean> informs = new ArrayList<InformBean>();
                try {
                    if (informid.equals("0") || informid.equals("null")) {
                        informs = informbpo.getAllInform();
                    } else {
                        int id = Integer.parseInt(informid);
                        informs = informbpo.getInformByid(id);
                    }
                } catch (Exception e) {
                    errmsg = e.getMessage();
                }
                Gson gson = new Gson();
                result = gson.toJson(informs);
                //String unionjson="{\"informs\":"+informsjson+",\"errmsg\":\""+errmsg+"\"}";
                //response.getWriter().write(unionjson);
            } else if (mode.equals("refresh")) {
                String lookid = request.getParameter("lookid");
                //String method=request.getParameter("method");
                List<InformBean> reinforms = new ArrayList<InformBean>();
                try {
                    if (lookid.equals("0") || lookid.equals("null")) {
                        reinforms = informbpo.getAllInform();
                    } else {
                        int id = Integer.parseInt(lookid);
                        reinforms = informbpo.getInformByid(id);
                    }
                } catch (Exception e) {
                    errmsg = e.getMessage();
                }
                Gson gson = new Gson();
                result = gson.toJson(reinforms);
            } else if (mode.equals("get")) {
                String infoid = request.getParameter("infoid").toString();
                try {
                    int id = Integer.parseInt(infoid);
                    bean = informbpo.getInformById(id, rootDir);
						/*/��Ŀ�긽�����ļ��У���ӵ�����������
						serverpath=serverpath+bean.getAffixpath();
						Collection wyx=bean.getAffixFiles();
						Iterator wx=wyx.iterator();
						while(wx.hasNext())
						{
							serverpath=serverpath+((FileItem)wx.next()).getName();
							//File serverFile=new File();
						}
						*/
                } catch (Exception e) {
                    errmsg = e.getMessage();
                }
                Gson gson = new Gson();
                result = gson.toJson(bean);
            } else if (mode.equals("del")) {
                String infoid = request.getParameter("infoid");
                try {
                    int id = Integer.parseInt(infoid);
                    String delaffixfilepath = "D:/" + informbpo.getPathbyId(id);
                    fu.deleteDirectory(delaffixfilepath);
                    boolean rs = informbpo.delbyid(id);
                    if (rs) {
                        result = "true";
                    } else {
                        result = "false";
                    }
                } catch (Exception e) {
                    errmsg = e.getMessage();
                }
            } else if (mode.equals("delfile")) {
                String delfilepath = "D:/" + request.getParameter("uploadpath");
                //File myDelFile = new File(delfilepath);
                try {
                    fu.deleteFile(delfilepath);
                    //myDelFile.delete();//.delete()
                } catch (Exception e) {
                    errmsg = "ɾ���ļ���������" + e.getMessage();
                    //e.printStackTrace();
                }
            } else {
                throw new Exception("���÷���'" + mode + "'�����ڣ�");
            }
        } catch (Exception e) {
            errmsg = e.getMessage();
        }
        //��Ӧ����
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
