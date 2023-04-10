package servlet;

import bean.*;
import bpo.SubjectBpo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SubjectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        String result = "";
        String errmsg = "";
        String mode = request.getParameter("mode");
        if (mode.equals("edt")) {
            String subid = request.getParameter("subid");
            //System.out.println("subid："+subid);
            if (subid == null || subid.equals("自动编号")) {//增加新课题
                try {
                    //获得表单数据
                    SubjectBean subject = getsubject(request);
                    //操作数据库
                    SubjectBpo subjectbpo = new SubjectBpo();
                    result = String.valueOf(subjectbpo.addinfo(subject));
                } catch (Exception e) {
                    errmsg = e.getMessage();
                }
            } else {//修改课题
                try {
//					获得表单数据
                    SubjectBean subject = getsubject(request);
                    subject.setSubid(subid);

                    SubjectBpo subjectbpo = new SubjectBpo();
                    subjectbpo.modifyinfo(subject);
                    result = subid;
                } catch (Exception e) {
                    errmsg = e.getMessage();
                }
            }
        } else if (mode.equals("modifybaseinfo")) {//仅修改课题基本信息（除课题编号、适应专业外）
            String subid = request.getParameter("subid");
            try {
//				获得表单数据
                SubjectBean subject = getsubject(request);
                subject.setSubid(subid);

                SubjectBpo subjectbpo = new SubjectBpo();
                subjectbpo.modifybaseinfo(subject);
                result = subid;
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("get")) {
            String subid = request.getParameter("subid");
            try {
                SubjectBpo subjectbpo = new SubjectBpo();
                SubjectBean subject = subjectbpo.getBysubid(subid);
                Gson gson = new Gson();
                result = gson.toJson(subject);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("gets")) {//得到课题列表
            try {
                String tutorid = request.getParameter("tutorid");
                List<SubjectBean> subjects = new ArrayList<SubjectBean>();

                SubjectBpo subjectbpo = new SubjectBpo();
                subjects = subjectbpo.getAllinfo(tutorid);
                Gson gson = new Gson();
                result = gson.toJson(subjects);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
            //
        } else if (mode.equals("del")) {//删除课题（未提交课题才允许删除）
            String subid = request.getParameter("subid");
            try {
                SubjectBpo subjectbpo = new SubjectBpo();
                subjectbpo.deleteinfo(subid);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("delall")) {//删除课题（无条件删除）
            String subid = request.getParameter("subid");
            try {
                SubjectBpo subjectbpo = new SubjectBpo();
                subjectbpo.deleteinfoall(subid);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("submit")) {//提交课题
            String subid = request.getParameter("subid");
            try {
                SubjectBpo subjectbpo = new SubjectBpo();
                subjectbpo.submitsuject(subid);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("getOption")) {//审核不通过的，查看审核意见
            String subid = request.getParameter("subid");
            try {
                List<String> options = new ArrayList<String>();
                SubjectBpo subjectbpo = new SubjectBpo();
                options = subjectbpo.getOptionBysubid(subid);
                Gson gson = new Gson();
                result = gson.toJson(options);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("getstus")) {//取得选择指定课题的学生列表
            String subid = request.getParameter("subid");
            try {
                List<StudentBean> students = new ArrayList<StudentBean>();
                SubjectBpo subjectbpo = new SubjectBpo();
                students = subjectbpo.getStudentsBysubid(subid);
                Gson gson = new Gson();
                result = gson.toJson(students);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("pickstudent")) {//选定学生
            String subid = request.getParameter("subid");
            String stuid = request.getParameter("stuid");
            try {
                SubjectBpo subjectbpo = new SubjectBpo();
                subjectbpo.pickStudent(subid, stuid);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("unpickstudent")) {//弃选学生
            String subid = request.getParameter("subid");
            String stuid = request.getParameter("stuid");
            try {
                SubjectBpo subjectbpo = new SubjectBpo();
                subjectbpo.unpickStudent(subid, stuid);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("getDesign")) {//获得学生课题的设计情况
            //String subid=request.getParameter("subid");
            //String stuid=request.getParameter("stuid");
            try {
				/*
				 SubjectBpo subjectbpo=new SubjectBpo();
				 SubsubmitBean subsubmit=subjectbpo.getDesignDoc(subid,stuid);
				 Gson gson = new Gson();
				 result=gson.toJson(subsubmit);
				 */
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("getsubsbyspec")) {//按专业查看课题情况
            String specid = request.getParameter("specid");
            String tdept = request.getParameter("tdept");
            String tname = request.getParameter("tname");
            if (tname != null) tname = request.getParameter("tname");
            String substatus = request.getParameter("substatus");
            try {
                SubjectBpo subjectbpo = new SubjectBpo();
                List<SubjectBean> subjects = new ArrayList<SubjectBean>();
                subjects = subjectbpo.getSubsBySpec(specid, tdept, tname, substatus);
                Gson gson = new Gson();
                result = gson.toJson(subjects);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("submitaudit")) {//批量审核课题
            String subspecarr = request.getParameter("subspecarr");
            String auditflag = request.getParameter("auditflag");
            try {
                Type type = new TypeToken<List<SubspecBean>>() {
                }.getType();
                Gson gson = new Gson();
                List<SubspecBean> subspecs = gson.fromJson(subspecarr, type);
                SubjectBpo subjectbpo = new SubjectBpo();
                if (auditflag.equals("0")) {//审核没通过
                    subjectbpo.submitAuditBatchUnpass(subspecs);
                } else if (auditflag.equals("1")) {//审核通过
                    subjectbpo.submitAuditBatchPass(subspecs);
                } else {
                    throw new Exception("审核标志不正确！");
                }
                //System.out.println(subspecs.size());
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("releasesubject")) {//批量发布课题
            String subspecarr = request.getParameter("subspecarr");
            String releaseflag = request.getParameter("releaseflag");
            try {
                Type type = new TypeToken<List<SubspecBean>>() {
                }.getType();
                Gson gson = new Gson();
                List<SubspecBean> subspecs = gson.fromJson(subspecarr, type);
                SubjectBpo subjectbpo = new SubjectBpo();
                subjectbpo.releaseSubjectBatch(subspecs, releaseflag);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("getstusbyspec")) {//按专业查看学生情况
            String specid = request.getParameter("specid");
            String classname = request.getParameter("classname");
            String stustatus = request.getParameter("stustatus");
            try {
                SubjectBpo subjectbpo = new SubjectBpo();
                List<StudentBean> students = new ArrayList<StudentBean>();
                students = subjectbpo.getStusBySpec(specid, classname, stustatus);
                Gson gson = new Gson();
                result = gson.toJson(students);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("assignsubject")) {//专业负责人指派课题
            String subid = request.getParameter("subid");
            String stuid = request.getParameter("stuid");
            try {
                SubjectBpo subjectbpo = new SubjectBpo();
                subjectbpo.assignSubject(stuid, subid);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("getSubjectsReviewedByTid")) {//得到教师审核的课题信息（盲审）
            String tid = request.getParameter("tid");
            List<SubReviewBean> subreviews = new ArrayList<SubReviewBean>();
            try {
                SubjectBpo subbpo = new SubjectBpo();
                subreviews = subbpo.getSubjectsReviewedByTid(tid);
                Gson gson = new Gson();
                result = gson.toJson(subreviews);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("modifyReviewOpinionBytid")) {//按教师修改课题审核结果（盲审）
            String tid = request.getParameter("tid");
            String subreviewedjson = request.getParameter("subreviewedjson");
            try {
                Type type = new TypeToken<List<SubReviewBean>>() {
                }.getType();
                Gson gson = new Gson();
                List<SubReviewBean> subreviews = gson.fromJson(subreviewedjson, type);
                SubjectBpo subbpo = new SubjectBpo();
                subbpo.modifyReviewOpinionBytid(tid, subreviews);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("subsim")) {//题目相似度比较 test
            List<SubSimBean> ret = new ArrayList<SubSimBean>();
            try {
                SubjectBpo subbpo = new SubjectBpo();
                ret = subbpo.computesimilar("基于web的毕业设计文档管理子系统设计与实现", 0.8f);
                Iterator it = ret.iterator();
                while (it.hasNext()) {
                    SubSimBean temp = (SubSimBean) it.next();
                    System.out.println(temp.getSubname() + ":" + temp.getSimilard());
                }
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("getSubsBySpecAndName")) {//得到课题 课题盲审查询
            String specid = request.getParameter("specid");
            String subname = request.getParameter("subname");
            if (subname != null) subname = request.getParameter("subname");
            List<ReviewSubjectBean> subreviews = new ArrayList<ReviewSubjectBean>();
            try {
                SubjectBpo subbpo = new SubjectBpo();
                subreviews = subbpo.getSubsBySpecAndName(specid, subname);
                Gson gson = new Gson();
                result = gson.toJson(subreviews);
            } catch (Exception e) {
                errmsg = e.getMessage();
            }
        } else if (mode.equals("restoreSubjectStatus")) {//恢复课题状态到“审核未通过”
            String subid = request.getParameter("subid");
            try {
                SubjectBpo subbpo = new SubjectBpo();
                subbpo.restoreSubjectStatus(subid);
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

    private SubjectBean getsubject(HttpServletRequest request) throws Exception {//增加新课题
        SubjectBean subject = new SubjectBean();
        try {
            //取得课题基本信息
            subject.setSubname(request.getParameter("subname"));
            subject.setOldargu(request.getParameter("oldargu"));
            subject.getOthertutor().setTid(request.getParameter("othertid"));
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

            //取得适应专业
            //System.out.println(request.getParameter("speccount"));
            int speccount = Integer.valueOf(request.getParameter("speccount"));
            String specid = "";
            for (int i = 0; i < speccount; i++) {
                specid = request.getParameter("spec_" + String.valueOf(i));
                if (!(specid == null || specid.equals(""))) {
                    subject.getListspec().add(specid);
                }
            }
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
            subject.setProgress(progress);
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
