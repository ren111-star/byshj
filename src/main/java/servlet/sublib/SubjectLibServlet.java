package servlet.sublib;

import bean.ProgressBean;
import bean.SubjectBean;
import bean.sublib.SubjectHisBean;
import bean.sublib.SubjectTempBean;
import bpo.sublib.SubjectLibBpo;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubjectLibServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        String result = "";
        String errmsg = "";
        String mode = request.getParameter("mode");

        SubjectLibBpo subjectbpo = new SubjectLibBpo();
        Gson gson = new Gson();

        if (mode.equals("edt")) {//增加、修改临时库中的课题
            String subid = request.getParameter("subid");
            //System.out.println("subid："+subid);
            try {
                //获得表单数据
                SubjectTempBean subject = getsubjectTemp(request);
                //操作数据库
                if (subid == null || subid.equals("自动编号") || subid.equals("")) {//增加
                    subjectbpo.addTempSubject(subject);
                } else {//修改
                    subject.setSubid(subid);
                    subjectbpo.updateTempSubject(subject);
                }

            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("del")) {//删除临时库中的课题
            String subid = request.getParameter("subid");
            try {
                subjectbpo.delTempSubject(subid);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("getshis")) {//查询历史库课题列表
            String tutorid = request.getParameter("tutorid");
            String usedyear = request.getParameter("usedyear");
            String subname = request.getParameter("subname");
            if (subname != null) subname = request.getParameter("subname");
            List<SubjectHisBean> subjects = null;
            try {
                subjects = subjectbpo.getSubjectHis(tutorid, usedyear, subname);
                result = gson.toJson(subjects);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("gethis")) {//查询一个历史课题，为方便显示使用subjectBean
            String usedyear = request.getParameter("usedyear");
            String subid = request.getParameter("subid");
            SubjectBean subject = null;
            try {
                subject = subjectbpo.getSubjectInfoFromHis(usedyear, subid);
                result = gson.toJson(subject);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
            //
        } else if (mode.equals("copyfromhis")) {//拷贝历史课题到当前库
            String usedyear = request.getParameter("usedyear");
            String subid = request.getParameter("subid");
            try {
                subjectbpo.copyFromHisToCurrent(subid, usedyear);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("getstemp")) {//查询暂存库课题列表
            String tutorid = request.getParameter("tutorid");
            List<SubjectTempBean> subjects = null;
            try {
                subjects = subjectbpo.getSubjectTemp(tutorid);
                result = gson.toJson(subjects);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("gettemp")) {//查询一个暂存课题
            String subid = request.getParameter("subid");
            SubjectBean subject = null;
            try {
                subject = subjectbpo.getSubjectInfoFromTemp(subid);
                result = gson.toJson(subject);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("copyfromtemp")) {//从临时库拷贝课题到当前库
            String subid = request.getParameter("subid");
            try {
                subjectbpo.copyFromTempToCurrent(subid);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("tranferfromtemp")) {//从临时库转移课题到当前库
            String subid = request.getParameter("subid");
            try {
                subjectbpo.TranferFromTempToCurrent(subid);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("copyfromcurrent")) {//从当前库拷贝课题到临时库
            String subid = request.getParameter("subid");
            try {
                subjectbpo.CopyFromCurrentToTemp(subid);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("transferfromcurrent")) {//从当前库转移课题到临时库
            String subid = request.getParameter("subid");
            try {
                subjectbpo.TransferFromCurrentToTemp(subid);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("sysinit")) {//系统初始化
            String retrieveyear = request.getParameter("retrieveyear");
            try {
                subjectbpo.sysInit(retrieveyear);
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

    private SubjectTempBean getsubjectTemp(HttpServletRequest request) throws Exception {//增加新课题（暂存，没有专业信息）
        SubjectTempBean subject = new SubjectTempBean();
        try {
            //取得课题基本信息
            subject.setSubid(request.getParameter("subid"));
            subject.setSubname(request.getParameter("subname"));
            subject.setOldargu(request.getParameter("oldargu"));
            subject.setRequirement(request.getParameter("requirement"));
            subject.setSubsort(request.getParameter("subsort"));
            subject.setSubkind(request.getParameter("subkind"));
            subject.setSubsource(request.getParameter("subsource"));
            subject.setSubtype(request.getParameter("subtype"));
            subject.setSubdirection(request.getParameter("subdirection"));
            subject.setIsoutschool(Integer.valueOf(request.getParameter("subaddress")));
            subject.setRefpapers(request.getParameter("refpapers"));
            subject.setContent(request.getParameter("content"));
            subject.getTutor().setTid(request.getParameter("tutorid"));
            subject.setRemark(request.getParameter("remark"));

            //取得课题进程
            int graduateweeknum = Integer.valueOf(request.getParameter("graduateweeknum"));
            List<ProgressBean> progress = new ArrayList<ProgressBean>();
            String subprog = "";
            for (int i = 0; i < graduateweeknum; i = i + 2) {
                String progresstime = request.getParameter("progresstime_" + String.valueOf(i));
                String progressitem = request.getParameter("progressitem_" + String.valueOf(i));
                subprog = subprog + progresstime + ":" + progressitem + "\r\n";
                String inorder = String.valueOf(i);
                ProgressBean progtemp = new ProgressBean();
                progtemp.setStartenddate(progresstime);
                progtemp.setContent(progressitem);
                progtemp.setInorder(inorder);
                progress.add(progtemp);
            }

            Gson gson = new Gson();
            String subprogs = gson.toJson(progress);
            subject.setSubprogs(subprogs);
            subject.setSubprog(subprog);

        } catch (Exception e) {
            throw e;
        }
        return subject;
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);

    }
}
