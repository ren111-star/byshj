package bpo;

import bean.*;
import com.DatabaseConn;
import com.SimiComparator;
import com.Similarity;
import com.SyscodeBpo;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SubjectBpo {
    /**
     * 保存前校验 2015.11.24 wxh增加
     */
    public void validNewSubject(SubjectBean subject) throws Exception {
        Connection con = DatabaseConn.getConnection();
        String vsql = "";
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        /**判断课题数是否超过10个，若超过，则不允许新增*/
        try {
            vsql = "select count(*) from tb_subject where tutorid=?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subject.getTutor().getTid());
            rst = pstmt.executeQuery();
            if (rst.next()) {
                int count = rst.getInt(1);
                if (count >= 10) throw new Exception("您当前库中的课题数已经10个，不允许再次增加！");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(null, pstmt, rst);
        }
        /** 判断课题是否重复*/
        try {
            vsql = "select * from tb_subject where tutorid=? and subname=?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subject.getTutor().getTid());
            pstmt.setString(2, subject.getSubname());
            rst = pstmt.executeQuery();
            if (rst.next()) {
                throw new Exception("您的课题：'" + subject.getSubname() + "'在当前库中已存在，不允许重复！");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        /**题目相似度若超过0.9，则认定为重复  2014.10.31 wxh*/
        try {
            List<SubSimBean> subsims = this.computesimilar(subject.getSubname(), 0.9f);
            if (subsims.size() != 0) {//数据库中存在相似的题目
                String errstr = "";
                for (Iterator<SubSimBean> it = subsims.iterator(); it.hasNext(); ) {
                    SubSimBean subsim = it.next();
                    errstr = errstr + "/" + subsim.getTutorname() + ":" + subsim.getSubname() + "/";
                }
                if (!errstr.equals("")) {
                    errstr = "[提交失败]系统中已存在" + subsims.size() + "个相同的课题:" + "[" + errstr + "]请修改课题名后再提交！";
                    throw new Exception(errstr);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 增加一个新课题 2015.11.24 wxh修改
     */
    public int addinfo(SubjectBean subject) throws Exception {
        Connection con = DatabaseConn.getConnection();
        String vsql = "";
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        int subid = 0;
        this.validNewSubject(subject);
        /**保存课题*/
        try {
            con.setAutoCommit(false);
            // 保存课题基本信息
            vsql = "insert into tb_subject"
                    + "(subname,usedyear,oldargu,content,requirement,refpapers,subkind,subsource,subtype,isoutschool,tutorid,othertid,operatedtime,subprog,subdirection,subsort) values(?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?)";
            pstmt = con.prepareStatement(vsql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, subject.getSubname());
            pstmt.setString(2, subject.getUsedyear());
            pstmt.setString(3, subject.getOldargu());
            pstmt.setString(4, subject.getContent());
            pstmt.setString(5, subject.getRequirement());
            pstmt.setString(6, subject.getRefpapers());
            pstmt.setString(7, subject.getSubkind());
            pstmt.setString(8, subject.getSubsource());
            pstmt.setString(9, subject.getSubtype());
            pstmt.setInt(10, subject.getIsoutschool());
            pstmt.setString(11, subject.getTutor().getTid());
            pstmt.setString(12, subject.getOthertutor().getTid());
            pstmt.setString(13, subject.getSubprog());
            pstmt.setString(14, subject.getSubdirection());
            pstmt.setString(15, subject.getSubsort());
            pstmt.execute();

            rst = pstmt.getGeneratedKeys();//获得带有返回值的resultset
            rst.next();
            subid = rst.getInt(1);//拿到id
            //System.out.println(subid);

            // 保存课题适合专业信息
            for (String s : subject.getListspec()) {
                String specid = String.valueOf(s);
                vsql = "insert into tb_subspec(subid,specid,operatedtime) values(?,?,now())";
                pstmt = con.prepareStatement(vsql);
                pstmt.setInt(1, subid);
                pstmt.setString(2, specid);
                pstmt.execute();
            }
            //保存课题进程
            List<ProgressBean> progress = subject.getProgress();
            for (ProgressBean progresstemp : progress) {
                String inorder = progresstemp.getInorder();
                String content = progresstemp.getContent();
                String startenddate = progresstemp.getStartenddate();
                String vsqlprog = "insert into tb_progress(subid,inorder,content,startenddate) values(?,?,?,?)";
                pstmt = con.prepareStatement(vsqlprog);
                pstmt.setInt(1, subid);
                pstmt.setString(2, inorder);
                pstmt.setString(3, content);
                pstmt.setString(4, startenddate);
                pstmt.execute();
            }
            con.commit();
        } catch (Exception e) {
            con.rollback();
            Exception ex = new Exception("保存课题信息失败：SubjectBpo.addinfo-"
                    + e.getMessage());
            throw ex;
        } finally {
            con.setAutoCommit(true);
            DatabaseConn.close(con, pstmt, rst);
        }
        return subid;
    }

    /**
     * 查询所有课题
     */
    public List<SubjectBean> getAllinfo() throws Exception {
        Connection con = DatabaseConn.getConnection();
        String vsql = "select * from tb_subject order by usedyear,tutorid,subname";
        PreparedStatement pstmt = con.prepareStatement(vsql);
        ResultSet rst = pstmt.executeQuery();
        List<SubjectBean> ret = new ArrayList<SubjectBean>();
        TeacherBpo teabpo = new TeacherBpo();
        while (rst.next()) {
            SubjectBean temp = new SubjectBean();
            String subid = rst.getString("subid");
            temp.setSubid(subid);
            temp.setReviewopinion(this.getReviewBySubid(subid));
            temp.setSubprog(rst.getString("subprog"));
            temp.setSubname(rst.getString("subname"));
            temp.setUsedyear(rst.getString("usedyear"));
            temp.setOldargu(rst.getString("oldargu"));
            temp.setContent(rst.getString("content"));
            temp.setRequirement(rst.getString("requirement"));
            temp.setRefpapers(rst.getString("refpapers"));
            temp.setSubkind(rst.getString("subkind"));
            temp.setSubsort(rst.getString("subsort"));
            temp.setSubsource(rst.getString("subsource"));
            temp.setSubtype(rst.getString("subtype"));
            temp.setSubdirection(rst.getString("subdirection"));
            temp.setIsoutschool(rst.getInt("isoutschool"));
            String tid = rst.getString("tutorid");
            temp.setTutor(teabpo.getBytid(tid));
            tid = rst.getString("othertid");
            temp.setOthertutor(teabpo.getBytid(tid));
            temp.setSubmitflag(rst.getString("submitflag"));
            ret.add(temp);
        }
        DatabaseConn.close(con, pstmt, rst);
        return ret;
    }

    //获取教师对应每个课题的状态及可进行的操作情况
    public List<SubjectBean> getAllinfo(String tutorid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        List<SubjectBean> ret = new ArrayList<SubjectBean>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            TeacherBpo teabpo = new TeacherBpo();
            if (tutorid == null) tutorid = "";
            String vsql = "select othertid,tutorid,subid,subname,isoutschool,submitflag from tb_subject where tutorid like ? order by usedyear,tutorid,subname";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, tutorid + "%");
            rst = pstmt.executeQuery();
            while (rst.next()) {
                SubjectBean temp = new SubjectBean();
                String subid = rst.getString("subid");
                String reviewopinion = this.getReviewBySubid(subid);
                temp.setSubid(subid);
                String tutorid0 = rst.getString("tutorid");
                String subname = rst.getString("subname");
                temp.setSubname(subname);
                temp.setSimSubsInHis(this.computesimilarWithOldSubs(tutorid0, subname, 0.9f));//计算相似的历史课题数

                String othertid = rst.getString("othertid");
                if (othertid != null && !othertid.equals("")) {
                    temp.setOthertutor(teabpo.getBytid(othertid));
                }
                temp.setTutor(teabpo.getBytid(tutorid0));

                temp.setReviewopinion(reviewopinion);
                temp.setIsoutschool(rst.getInt("isoutschool"));
                String submitflag = rst.getString("submitflag");
                String strspecname = "";
                //获得课题对应专业
                List<SpecialityBean> specs = getspecssub(subid);
                Iterator it = specs.iterator();
                while (it.hasNext()) {
                    SpecialityBean temp0 = (SpecialityBean) it.next();
                    strspecname = strspecname + " " + temp0.getSpecname();
                }
                temp.setSpecname(strspecname);

                if (submitflag == null || submitflag.equals("")) {
                    temp.setStatus("未提交");
                    temp.getOperations().add("修改");
                    temp.getOperations().add("删除");
                    temp.getOperations().add("提交");
                    temp.getOperations().add("转移到暂存库");
                } else if (submitflag.equals("1")) {//已提交
                    //判断是否被选中
                    vsql = "select * from tb_stusub where pickflag='1' and subid=?";
                    PreparedStatement pstmt1 = null;
                    ResultSet rst1 = null;

                    try {
                        pstmt1 = con.prepareStatement(vsql);
                        pstmt1.setString(1, subid);
                        rst1 = pstmt1.executeQuery();
                        if (rst1.next()) {
                            String status = "已选," + rst1.getString("stuid");
                            temp.setStatus(status);
                            /*需增加判断，若允许修改，则显示‘修改任务书’按钮，否则不显示
                             * */
                            SysarguBpo sysargubpo = new SysarguBpo();
                            String modifyflag = sysargubpo.getSysargu("modifytaskbookflag").getArguvalue();
                            if (modifyflag.equals("1")) {
                                temp.getOperations().add("修改任务书");
                                temp.getOperations().add("复制到暂存库");
                            }
                            temp.getOperations().add("查看设计情况");
                        } else {//课题没有被选中，查看是否已有学生初选
                            vsql = "select count(*) from tb_student where stuid in("
                                    + "select stuid from tb_stusub a where subid=? and ifnull(pickflag,'')=''  and pickorder=("
                                    + "select min(pickorder) from tb_stusub where stuid=a.stuid and ifnull(pickflag,'')='')"
                                    + ") order by classname,stuid";
                            PreparedStatement pstmt2 = null;
                            ResultSet rst2 = null;
                            int stucount;
                            try {
                                pstmt2 = con.prepareStatement(vsql);
                                pstmt2.setString(1, subid);
                                rst2 = pstmt2.executeQuery();
                                rst2.next();
                                stucount = rst2.getInt(1);
                            } catch (Exception e) {
                                throw e;
                            } finally {
                                DatabaseConn.close(null, pstmt2, rst2);
                            }
                            if (stucount != 0) {
                                String status = "初选学生数：" + String.valueOf(stucount);
                                temp.setStatus(status);
                                temp.getOperations().add("选择学生");
                            } else {//课题没有人选，判断是否已审核、已发布
                                vsql = "select specname,auditflag,releaseflag from tb_subspec a,tb_speciality b where a.specid=b.specid and subid=?";
                                PreparedStatement pstmt3 = null;
                                ResultSet rst3 = null;
                                String status = "";
                                boolean showoptionflag = false;//是否显示“查看审核意见”操作
                                try {
                                    pstmt3 = con.prepareStatement(vsql);
                                    pstmt3.setString(1, subid);
                                    rst3 = pstmt3.executeQuery();
                                    while (rst3.next()) {
                                        String specname = rst3.getString("specname");
                                        String auditflag = rst3.getString("auditflag");
                                        String releaseflag = rst3.getString("releaseflag");
                                        String reviewstr = "<hr>[盲审意见：" + reviewopinion + "]";
                                        if (auditflag == null || auditflag.equals("")) {//没有审核
                                            if (reviewopinion.equals("")) reviewstr = "<hr>[等待盲审]";
                                            if (status.equals("")) {
                                                status = "(" + specname + ")审核中…" + reviewstr;
                                            } else {
                                                status = status + "<br>" + "(" + specname + ")审核中…" + reviewstr;
                                            }
                                        } else if (auditflag.equals("0")) {//审核没通过
                                            if (status.equals("")) {
                                                status = "(" + specname + ")审核没通过" + reviewstr;
                                            } else {
                                                status = status + "<br>" + "(" + specname + ")审核没通过" + reviewstr;
                                            }
                                            showoptionflag = true;//操作中增加查看审核意见
                                        } else {
                                            if (releaseflag == null || releaseflag.equals("")) {//还未发布
                                                if (status.equals("")) {
                                                    status = "(" + specname + ")审核通过";
                                                } else {
                                                    status = status + "<br>" + "(" + specname + ")审核通过";
                                                }
                                            } else {//课题已发布，但是还没有学生选
                                                SysarguBpo sysargubpo = new SysarguBpo();
                                                String endpickingflag = sysargubpo.getSysargu("endpickingflag").getArguvalue();
                                                if (endpickingflag.equals("")) {
                                                    System.out.println("--------------------------");
                                                }
                                                if (endpickingflag.equals("1")) {
                                                    status = "选题已结束";
                                                } else {
                                                    if (status.equals("")) {
                                                        status = "(" + specname + ")学生选题中…";
                                                    } else {
                                                        status = status + "<br>" + "(" + specname + ")学生选题中…";
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    throw e;
                                } finally {
                                    DatabaseConn.close(null, pstmt3, rst3);
                                }
                                //设置课题状态及操作
                                temp.setStatus(status);
                                if (showoptionflag) temp.getOperations().add("查看审核意见");
                            }
                        }
                    } catch (Exception e) {
                        throw e;
                    } finally {
                        DatabaseConn.close(null, pstmt1, rst1);
                    }
                } else {//所有专业审核未通过时，submitflag="0",auditflag="0"
                    temp.setStatus("审核没通过" + "<hr>[盲审意见：" + reviewopinion + "]");
                    temp.getOperations().add("查看审核意见");
                    temp.getOperations().add("修改");
                    temp.getOperations().add("删除");
                    temp.getOperations().add("提交");
                    temp.getOperations().add("转移到暂存库");
                }
                ret.add(temp);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return ret;
    }

    /**
     * 按专业、课题名 查询课题审核信息 2014.11.11 wxh
     */
    public List<ReviewSubjectBean> getSubsBySpecAndName(String specid, String subname) throws Exception {
        Connection con = DatabaseConn.getConnection();
        List<ReviewSubjectBean> ret = new ArrayList<ReviewSubjectBean>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        if (subname == null) subname = "";
        TeacherBpo teabpo = new TeacherBpo();
        try {
            if (specid == null || specid.equals("")) throw new Exception("专业编号不能为空");
            String vsql = "select subid, subname, tutorid, othertid, reviewerid, ifnull(reviewopinion,'') as reviewopinion from v_subjectreview " +
                    "where specid=? and subname like ?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, specid);
            pstmt.setString(2, "%" + subname + "%");
            rst = pstmt.executeQuery();
            while (rst.next()) {
                ReviewSubjectBean temp = new ReviewSubjectBean();
                String subid = rst.getString("subid");
                temp.setSubid(subid);
                temp.setSubname(rst.getString("subname"));
                String tutorid = rst.getString("tutorid");
                String othertid = rst.getString("othertid");
                String reviewerid = rst.getString("reviewerid");
                String tutorname = teabpo.getBytid(tutorid).getTname();
                if (!othertid.equals("")) {
                    tutorname = tutorname + "/" + teabpo.getBytid(othertid).getTname();
                }
                temp.setTutornames(tutorname);
                String reviewername = teabpo.getBytid(reviewerid).getTname();
                temp.setReviewername(reviewername);
                temp.setReviewopinion(rst.getString("reviewopinion"));

                ret.add(temp);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }

        return ret;
    }

    /**
     * 按专业查看所有课题情况,其中参数specid不能为null或”“
     */
    public List<SubjectBean> getSubsBySpec(String specid, String tdept, String tname, String substatus) throws Exception {
        Connection con = DatabaseConn.getConnection();
        List<SubjectBean> ret = new ArrayList<SubjectBean>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        if (tdept == null) tdept = "";
        if (tname == null) tname = "";
        if (substatus == null) substatus = "";
        TeacherBpo teabpo = new TeacherBpo();

        try {
            if (specid == null || specid.equals("")) throw new Exception("按专业查询课题时，专业编号不能为空");

            String vsql = "select b.subid,b.submitflag,b.isoutschool,b.subname,b.tutorid,b.othertid,a.auditoption,a.auditflag,a.releaseflag " +
                    "from tb_subspec a,tb_subject b " +
                    "where a.specid=? and a.subid=b.subid and b.tutorid in(" +
                    "select tid from tb_teacher where ifnull(tdept,'') like ? and tname like ?) " +
                    "order by b.tutorid,b.subname";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, specid);
            pstmt.setString(2, tdept + "%");
            pstmt.setString(3, tname + "%");
            rst = pstmt.executeQuery();
            while (rst.next()) {
                SubjectBean temp = new SubjectBean();
                String subid = rst.getString("subid");
                temp.setSubid(subid);
                String reviewopinion = this.getReviewBySubid(subid);
                temp.setReviewopinion(reviewopinion);
                String subname = rst.getString("subname");
                temp.setSubname(subname);
                temp.setIsoutschool(rst.getInt("isoutschool"));
                temp.setSubmitflag(rst.getString("submitflag"));
                //指导教师信息
                String tid = rst.getString("tutorid");
                temp.setTutor(teabpo.getBytid(tid));
                String othertid = rst.getString("othertid");
                temp.setOthertutor(teabpo.getBytid(othertid));
                //查询课题历史，确定重复的课题 2015.12.4
                temp.setSimSubsInHis(this.computesimilarWithOldSubs(tid, subname, 0.9f));
                ///////////////////
                //
                String auditflag = rst.getString("auditflag");
                String releaseflag = rst.getString("releaseflag");
                String auditoption = rst.getString("auditoption");

                String submitflag = temp.getSubmitflag();

                if (submitflag == null || submitflag.equals("")) {
                    temp.setStatus("未提交");
                } else if (submitflag.equals("1")) {//已提交
                    //判断是否被选中
                    vsql = "select * from tb_stusub where pickflag='1' and subid=?";
                    PreparedStatement pstmt1 = null;
                    ResultSet rst1 = null;
                    try {
                        pstmt1 = con.prepareStatement(vsql);
                        pstmt1.setString(1, subid);
                        rst1 = pstmt1.executeQuery();
                        if (rst1.next()) {
                            String status = "已选/" + rst1.getString("stuid");
                            temp.setStatus(status);
                        } else {//课题没有被选中，查看是否已有学生初选
                            vsql = "select count(*) from tb_student where stuid in("
                                    + "select stuid from tb_stusub a where subid=? and ifnull(pickflag,'')=''  and pickorder=("
                                    + "select min(pickorder) from tb_stusub where stuid=a.stuid and ifnull(pickflag,'')='')"
                                    + ") order by classname,stuid";
                            PreparedStatement pstmt2 = null;
                            ResultSet rst2 = null;
                            int stucount;
                            try {
                                pstmt2 = con.prepareStatement(vsql);
                                pstmt2.setString(1, subid);
                                rst2 = pstmt2.executeQuery();
                                rst2.next();
                                stucount = rst2.getInt(1);
                            } catch (Exception e) {
                                throw e;
                            } finally {
                                DatabaseConn.close(null, pstmt2, rst2);
                            }
                            if (stucount != 0) {
                                String status = "已初选/" + String.valueOf(stucount);
                                temp.setStatus(status);
                            } else {//课题没有人选，判断是否已审核、已发布
                                String reviewstr = "[盲审意见：" + reviewopinion + "]";
                                if (auditflag == null || auditflag.equals("")) {//没有审核
                                    if (reviewopinion.equals("")) reviewstr = "[等待盲审]";
                                    temp.setStatus("已提交-等待审核" + reviewstr);
                                } else if (auditflag.equals("0")) {//审核没通过
                                    temp.setStatus("审核没通过/" + auditoption + "[盲审意见：" + reviewopinion + "]");
                                } else {
                                    if (releaseflag == null || releaseflag.equals("")) {//还未发布
                                        temp.setStatus("审核通过-等待发布");
                                    } else {//课题已发布，但是还没有学生选
                                        temp.setStatus("已发布-等待选题");
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        throw e;
                    } finally {
                        DatabaseConn.close(null, pstmt1, rst1);
                    }
                } else {//所有专业审核未通过时，submitflag="0",auditflag="0"
                    temp.setStatus("审核没通过/" + auditoption + "[盲审意见：" + reviewopinion + "]");
                }
                ret.add(temp);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        //根据参数substatus值从ret中筛选满足条件的
        List<SubjectBean> ret0 = new ArrayList<SubjectBean>();
        if (substatus.equals("")) {
            ret0 = ret;
        } else {
            SyscodeBpo syscodebpo = new SyscodeBpo();
            String substatuscodename = syscodebpo.getcode("ktzht", substatus).getCodecontent();
            Iterator it = ret.iterator();
            while (it.hasNext()) {
                SubjectBean temp = (SubjectBean) it.next();
                String tempstr = temp.getStatus().split("/")[0];
                if (tempstr.length() >= 8 && tempstr.substring(0, 8).equals("已提交-等待审核"))
                    tempstr = tempstr.substring(0, 8);
                if (tempstr.equals(substatuscodename)) {
                    ret0.add(temp);
                }
            }
        }
        return ret0;
    }

    //批量审核课题(审核通过)
    public void submitAuditBatchPass(List<SubspecBean> subspecs) throws Exception {
        Connection con = DatabaseConn.getConnection();
        if (subspecs == null || subspecs.size() == 0) return;
        PreparedStatement pstmt = null;
        con.setAutoCommit(false);
        try {
            String vsql = "update tb_subspec set auditflag='1',operatedtime=now() where specid=? and subid=?";
            pstmt = con.prepareStatement(vsql);
            Iterator it = subspecs.iterator();
            while (it.hasNext()) {
                SubspecBean temp = (SubspecBean) it.next();
                pstmt.setString(1, temp.getSpecid());
                pstmt.setInt(2, Integer.valueOf(temp.getSubid()));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            DatabaseConn.close(con, pstmt, null);

        }
    }

    //批量审核课题(审核未通过)
    public void submitAuditBatchUnpass(List<SubspecBean> subspecs) throws Exception {
        Connection con = DatabaseConn.getConnection();
        if (subspecs == null || subspecs.size() == 0) return;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt0 = null;
        con.setAutoCommit(false);
        try {
            String vsql = "update tb_subspec set auditflag='0',auditoption=?,operatedtime=now() where specid=? and subid=?";
            String vsql0 = "update tb_subject set submitflag='0' where subid=? and " +
                    "not exists(select * from tb_subspec where subid=? and specid<>? and auditflag<>'0')";
            pstmt = con.prepareStatement(vsql);
            pstmt0 = con.prepareStatement(vsql0);//若不存在其他专业未审核或审核已通过的情况，则更新课题submitflag='0'
            Iterator it = subspecs.iterator();
            while (it.hasNext()) {
                SubspecBean temp = (SubspecBean) it.next();
                pstmt.setString(1, temp.getAuditoption());
                pstmt.setString(2, temp.getSpecid());
                pstmt.setInt(3, Integer.valueOf(temp.getSubid()));
                pstmt.addBatch();
                //
                pstmt0.setInt(1, Integer.valueOf(temp.getSubid()));
                pstmt0.setInt(2, Integer.valueOf(temp.getSubid()));
                pstmt0.setString(3, temp.getSpecid());
                pstmt0.addBatch();
            }
            pstmt.executeBatch();
            pstmt0.executeBatch();
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            DatabaseConn.close(null, pstmt0, null);
            con.setAutoCommit(true);
            DatabaseConn.close(con, pstmt, null);
        }
    }

    //	批量发布课题
    public void releaseSubjectBatch(List<SubspecBean> subspecs, String releaseflag) throws Exception {
        Connection con = DatabaseConn.getConnection();
        if (subspecs == null || subspecs.size() == 0) return;
        if (releaseflag == null) releaseflag = "";
        PreparedStatement pstmt = null;
        con.setAutoCommit(false);
        try {
            String vsql = "update tb_subspec set releaseflag=?,operatedtime=now() where specid=? and subid=?";
            pstmt = con.prepareStatement(vsql);
            Iterator it = subspecs.iterator();
            while (it.hasNext()) {
                SubspecBean temp = (SubspecBean) it.next();
                pstmt.setString(1, releaseflag);
                pstmt.setString(2, temp.getSpecid());
                pstmt.setInt(3, Integer.valueOf(temp.getSubid()));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            DatabaseConn.close(con, pstmt, null);
        }
    }

    /**
     * @param specid
     * @return specid对应的课题专业
     * @throws Exception
     */
    public List<SubspecBean> getsubspecs(String specid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        String vsql = "select * from tb_subspec where specid like ?";
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        List<SubspecBean> ret = new ArrayList<SubspecBean>();
        try {
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, specid + "%");
            rst = pstmt.executeQuery();
            while (rst.next()) {
                SubspecBean temp = new SubspecBean();
                temp.setSpecid(rst.getString("specid"));
                temp.setSubid(rst.getString("subid"));
                temp.setAuditflag(rst.getString("auditflag"));
                temp.setAuditoption(rst.getString("auditoption"));
                temp.setReleaseflag(rst.getString("releaseflag"));
                ret.add(temp);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return ret;
    }

    /**
     * @param subid
     * @return subid对应的专业列表
     */
    public List<SpecialityBean> getspecssub(String subid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        String vsql = "select * from tb_speciality where specid " +
                "in(select specid from tb_subspec where subid=?)";
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        List<SpecialityBean> ret = new ArrayList<SpecialityBean>();
        try {
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subid);
            rst = pstmt.executeQuery();
            while (rst.next()) {
                SpecialityBean temp = new SpecialityBean();
                temp.setSpecid(rst.getString("specid"));
                temp.setSpecname(rst.getString("specname"));
                temp.setSpecmagtid(rst.getString("specmagtid"));
                ret.add(temp);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return ret;
    }

    /**
     * 根据课题编号subid得到课题基本信息
     */
    public SubjectBean getBysubid(String subid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        String vsql = "select * from tb_subject where subid=?";
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        SubjectBean temp = new SubjectBean();
        TeacherBpo teacherbpo = new TeacherBpo();
        SyscodeBpo syscodebpo = new SyscodeBpo();
        try {
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subid);
            rst = pstmt.executeQuery();
            while (rst.next()) {
                temp.setSubid(subid);
                temp.setProgress(this.getProgresstable(subid));
                temp.setSubprog(rst.getString("subprog"));
                temp.setSubname(rst.getString("subname"));
                temp.setUsedyear(rst.getString("usedyear"));
                temp.setOldargu(rst.getString("oldargu"));
                temp.setContent(rst.getString("content"));
                temp.setRequirement(rst.getString("requirement"));
                temp.setRefpapers(rst.getString("refpapers"));
                temp.setIsoutschool(rst.getInt("isoutschool"));

                String subsort = rst.getString("subsort");
                temp.setSubsort(subsort);
                if (!(subsort == null || subsort.equals(""))) {
                    temp.setSubsortname(syscodebpo.getcode("ktlb", subsort).getCodecontent());
                }
                String subkind = rst.getString("subkind");
                temp.setSubkind(subkind);
                if (!(subkind == null || subkind.equals(""))) {
                    temp.setSubkindname(syscodebpo.getcode("ktxz", subkind).getCodecontent());
                }
                String subsource = rst.getString("subsource");
                temp.setSubsource(subsource);
                if (!(subsource == null || subsource.equals(""))) {
                    temp.setSubsourcename(syscodebpo.getcode("ktly", subsource).getCodecontent());
                }
                String subtype = rst.getString("subtype");
                temp.setSubtype(subtype);
                if (!(subtype == null || subtype.equals(""))) {
                    temp.setSubtypename(syscodebpo.getcode("ktlx", subtype).getCodecontent());
                }
                String subdirection = rst.getString("subdirection");
                temp.setSubdirection(subdirection);
                if (!(subtype == null || subtype.equals(""))) {
                    temp.setSubdirectionname(syscodebpo.getcode("ktfx", subdirection).getCodecontent());
                }
                String tid = rst.getString("tutorid");
                temp.setTutor(teacherbpo.getBytid(tid));
                tid = rst.getString("othertid");
                temp.setOthertutor(teacherbpo.getBytid(tid));
                temp.setSubmitflag(rst.getString("submitflag"));
                vsql = "select specid from tb_subspec where subid=" + subid;
                PreparedStatement pstmt1 = null;
                ResultSet rst1 = null;
                try {
                    pstmt1 = con.prepareStatement(vsql);
                    rst1 = pstmt1.executeQuery();
                    while (rst1.next()) {
                        temp.getListspec().add(rst1.getString("specid"));
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    DatabaseConn.close(null, pstmt1, rst1);
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return temp;
    }

    //根据课题编号subid，得到其进程基本信息
    public List<ProgressBean> getProgresstable(String subid) throws Exception {
        List<ProgressBean> progress = new ArrayList<ProgressBean>();
        String stuid = this.getStudentBysubid(subid).getStuid();
        WeekSumBpo weeksumbpo = new WeekSumBpo();
        Connection con = null;
        String vsql = "select * from tb_progress where subid=?";
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            con = DatabaseConn.getConnection();
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subid);
            rst = pstmt.executeQuery();
            while (rst.next()) {
                ProgressBean temp = new ProgressBean();
                temp.setProgid(rst.getString("progid"));
                int inorder = rst.getInt("inorder");
                temp.setInorder(String.valueOf(inorder));
                temp.setContent(rst.getString("content"));
                temp.setStartenddate(rst.getString("startenddate"));
                temp.setSubid(rst.getString("subid"));
                //从周总结中获得检查意见
                temp.setCheckopinion(weeksumbpo.getCheckOpinion(stuid, inorder));
                ///
                progress.add(temp);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return progress;
    }

    /**
     * 得到每个课题的进程（json格式）
     */
    public String getProgressJson(String subid) throws Exception {
        List<ProgressBean> progs = this.getProgresstable(subid);
        for (int i = 0; i < progs.size(); i++) {
            ProgressBean tmp = progs.get(i);
            tmp.setProgid(null);
            tmp.setSubid(null);
            tmp.setCheckopinion(null);
        }
        Gson gson = new Gson();
        String result = gson.toJson(progs);
        return result;
    }

    /***/
    public List<String> getOptionBysubid(String subid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        List<String> ret = new ArrayList<String>();
        String vsql = "select specname,auditoption from tb_subspec a,tb_speciality b where a.specid=b.specid and auditflag='0' and subid=?";
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subid);
            rst = pstmt.executeQuery();
            while (rst.next()) {
                String specname = rst.getString("specname");
                String auditoption = rst.getString("auditoption");
                if (auditoption == null) {
                    auditoption = "";
                }
                ret.add(specname + "|" + auditoption);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return ret;
    }

    public List<StudentBean> getStudentsBysubidUnabled(String subid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        List<StudentBean> ret = new ArrayList<StudentBean>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String vsql = "select stuid,sname,classname from tb_student where stuid in("
                + "select stuid from tb_stusub a where subid=? and ifnull(pickflag,'')=''  and pickorder<>("
                + "select min(pickorder) from tb_stusub where stuid=a.stuid and ifnull(pickflag,'')='' and pickorder is not null)"
                + ") order by classname,stuid";
        try {
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subid);
            rst = pstmt.executeQuery();
            while (rst.next()) {
                StudentBean temp = new StudentBean();
                temp.setStuid(rst.getString("stuid"));
                temp.setSname(rst.getString("sname"));
                String classname = rst.getString("classname");
                ClassBpo classbpo = new ClassBpo();
                temp.setClassbean(classbpo.getByclassname(classname));
                //设置学生是否能够被选择的状态，置student.status（可选/志愿顺序；不可选/志愿顺序）
                ret.add(temp);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return ret;
    }

    /**
     * @param subid
     * @return StudentBean 已确定的学生
     * @throws Exception
     */
    public StudentBean getStudentBysubid(String subid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        StudentBean student = new StudentBean();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String vsql = "select stuid,sname,classname from tb_student where stuid in("
                + "select stuid from tb_stusub a where subid=? and pickflag='1')";
        try {
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subid);
            rst = pstmt.executeQuery();
            while (rst.next()) {
                student.setStuid(rst.getString("stuid"));
                student.setSname(rst.getString("sname"));
                String classname = rst.getString("classname");
                student.setClassname(classname);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return student;
    }

    /**
     * @param subid
     * @return List<StudentBean> 初选课题学生结合
     * @throws Exception
     */
    public List<StudentBean> getStudentsBysubid(String subid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        List<StudentBean> ret = new ArrayList<StudentBean>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String vsql = "select stuid,sname,classname from tb_student where stuid in("
                + "select stuid from tb_stusub a where subid=? and ifnull(pickflag,'')=''  and pickorder=("
                + "select min(pickorder) from tb_stusub where stuid=a.stuid and ifnull(pickflag,'')='' and pickorder is not null)"
                + ") order by classname,stuid";
        try {
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subid);
            rst = pstmt.executeQuery();
            while (rst.next()) {
                StudentBean temp = new StudentBean();
                temp.setStuid(rst.getString("stuid"));
                temp.setSname(rst.getString("sname"));
                String classname = rst.getString("classname");
                ClassBpo classbpo = new ClassBpo();
                temp.setClassbean(classbpo.getByclassname(classname));
                ret.add(temp);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return ret;
    }

    //选择学生
    public void pickStudent(String subid, String stuid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        if (subid == null) subid = "";
        if (stuid == null) stuid = "";
        //判断课题未被选，且学生也未选题
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String vsql = "select * from tb_stusub where pickflag='1' and (subid=? or stuid=?)";
        try {
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subid);
            pstmt.setString(2, stuid);
            rst = pstmt.executeQuery();
            if (rst.next()) {
                throw new Exception("课题已被选或者学生已选中课题，不允许再次选择！");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(null, pstmt, rst);
        }
        //修改数据库
        con.setAutoCommit(false);
        try {
            //对选中的学生置选中标志“1”
            vsql = "update tb_stusub set pickflag='1',operatedtime=now() where subid=? and stuid=?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subid);
            pstmt.setString(2, stuid);
            pstmt.executeUpdate();
            DatabaseConn.close(null, pstmt, null);
            //落选的学生置选中标志为“0”
            vsql = "update tb_stusub set pickflag='0',operatedtime=now() where subid=? and stuid<>?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subid);
            pstmt.setString(2, stuid);
            pstmt.executeUpdate();
            DatabaseConn.close(null, pstmt, null);
            //删除选中学生的其他选题记录
            vsql = "delete from tb_stusub where ifnull(subid,0)<>? and stuid=?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subid);
            pstmt.setString(2, stuid);
            pstmt.executeUpdate();
            DatabaseConn.close(null, pstmt, null);
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            DatabaseConn.close(con, pstmt, null);
        }
    }

    //弃选学生
    public void unpickStudent(String subid, String stuid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        if (subid == null) subid = "";
        if (stuid == null) stuid = "";
        PreparedStatement pstmt = null;
        try {
            String vsql = "update tb_stusub set pickflag='0',operatedtime=now() where subid=? and stuid=?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subid);
            pstmt.setString(2, stuid);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, null);
        }
    }

    //按专业获得学生当前选题状态
    public List<StudentBean> getStusBySpec(String specid, String classname, String stustatus) throws Exception {
        Connection con = DatabaseConn.getConnection();
        List<StudentBean> ret = new ArrayList<StudentBean>();

        if (classname == null) classname = "";
        if (stustatus == null) stustatus = "";

        PreparedStatement pstmt = null;
        ResultSet rst = null;

        try {
            if (specid == null || specid.equals("")) throw new Exception("专业编号不能为空!");

            String vsql = "select * from tb_student where classname like ? and classname in(select classname from tb_class where specid=?)";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, classname + "%");
            pstmt.setString(2, specid);
            rst = pstmt.executeQuery();
            while (rst.next()) {
                StudentBean temp = new StudentBean();
                String stuid = rst.getString("stuid");
                temp.setStuid(stuid);
                temp.setSname(rst.getString("sname"));
                String classname0 = rst.getString("classname");
                temp.setClassname(classname0);
                ClassBpo classbpo = new ClassBpo();
                temp.setClassbean(classbpo.getByclassname(classname0));
                temp.setEmail(rst.getString("email"));
                temp.setTelephone(rst.getString("telephone"));
                temp.setSsex(rst.getString("ssex"));

                //判断学生选题状态
                StudentBpo studentbpo = new StudentBpo();
                String status = studentbpo.getStuStatus(stuid);
                temp.setStatus(status);
                if (status.split("/")[0].equals("已选")) temp.setSubject(getBysubid(status.split("/")[1]));
                ret.add(temp);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        //根据参数stustatus值从ret中筛选满足条件的
        List<StudentBean> ret0 = new ArrayList<StudentBean>();
        if (stustatus.equals("")) {
            ret0 = ret;
        } else {
            SyscodeBpo syscodebpo = new SyscodeBpo();
            String codecontent = syscodebpo.getcode("xshzht", stustatus).getCodecontent();
            Iterator it = ret.iterator();
            while (it.hasNext()) {
                StudentBean temp = (StudentBean) it.next();
                if (temp.getStatus().split("/")[0].equals(codecontent)) {
                    ret0.add(temp);
                }
            }
        }
        return ret0;
    }

    //给学生指派课题
    public void assignSubject(String stuid, String subid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        //判断是否需指派课题
        PreparedStatement pstmt0 = null;
        PreparedStatement pstmt = null;
        con.setAutoCommit(false);
        //指派课题：先删除选题表中非指派课题（subid is null）的所有其他记录,然后更新指派课题选题行
        String vsql0 = "delete from tb_stusub where stuid=? and subid is not null";
        String vsql = "update tb_stusub set subid=?,pickflag='1',operatedtime=now() where stuid=? and subid is null and ifnull(pickflag,'') =''";
        try {
            if (subid == null || subid.equals("")) throw new Exception("课题编号为空，不能指派课题！");
            if (stuid == null || stuid.equals("")) throw new Exception("学号为空，不能指派课题！");
            pstmt0 = con.prepareStatement(vsql0);
            pstmt0.setString(1, stuid);
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subid);
            pstmt.setString(2, stuid);
            pstmt0.executeUpdate();
            pstmt.executeUpdate();
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            DatabaseConn.close(null, pstmt0, null);
            DatabaseConn.close(con, pstmt, null);
        }
    }

    //
    //
    public void modifyinfo(SubjectBean subject) throws Exception {
        Connection con = DatabaseConn.getConnection();
        PreparedStatement pstmt = null;
        con.setAutoCommit(false);
        try {
            //更新 课题基本信息
            String vsql = "update tb_subject set subname=?,usedyear=?,oldargu=?,content=?,requirement=?,refpapers=?"
                    + ",subkind=?,subsource=?,subtype=?,isoutschool=?,tutorid=?,othertid=?,subprog=?,subdirection=?,subsort=?,operatedtime=now() where subid=?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subject.getSubname());
            pstmt.setString(2, subject.getUsedyear());
            pstmt.setString(3, subject.getOldargu());
            pstmt.setString(4, subject.getContent());
            pstmt.setString(5, subject.getRequirement());
            pstmt.setString(6, subject.getRefpapers());
            pstmt.setString(7, subject.getSubkind());
            pstmt.setString(8, subject.getSubsource());
            pstmt.setString(9, subject.getSubtype());
            pstmt.setInt(10, subject.getIsoutschool());
            pstmt.setString(11, subject.getTutor().getTid());
            pstmt.setString(12, subject.getOthertutor().getTid());
            pstmt.setString(13, subject.getSubprog());
            pstmt.setString(14, subject.getSubdirection());
            pstmt.setString(15, subject.getSubsort());
            pstmt.setString(16, subject.getSubid());
            pstmt.executeUpdate();
            DatabaseConn.close(null, pstmt, null);

            //删除课题专业对应
            vsql = "delete from tb_subspec where subid=?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subject.getSubid());
            pstmt.executeUpdate();
            DatabaseConn.close(null, pstmt, null);

            //保存课题适合专业信息
            Iterator it = subject.getListspec().iterator();
            while (it.hasNext()) {
                String specid = String.valueOf(it.next());
                vsql = "insert into tb_subspec(subid,specid,operatedtime) values(?,?,now())";
                pstmt = con.prepareStatement(vsql);
                pstmt.setInt(1, Integer.valueOf(subject.getSubid()));
                pstmt.setString(2, specid);
                pstmt.executeUpdate();
                DatabaseConn.close(null, pstmt, null);
            }
            //删除进程信息
            vsql = "delete from tb_progress where subid=?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subject.getSubid());
            pstmt.setString(1, subject.getSubid());
            pstmt.executeUpdate();
            DatabaseConn.close(null, pstmt, null);
            //保存新进程信息
            for (Iterator<ProgressBean> itp = subject.getProgress().iterator(); itp.hasNext(); ) {
                ProgressBean progresstemp = itp.next();
                String inorder = progresstemp.getInorder();
                String content = progresstemp.getContent();
                String startenddate = progresstemp.getStartenddate();
                vsql = "insert into tb_progress(subid,inorder,content,startenddate) values(?,?,?,?)";
                pstmt = con.prepareStatement(vsql);
                pstmt.setInt(1, Integer.valueOf(subject.getSubid()));
                pstmt.setInt(2, Integer.valueOf(inorder));
                pstmt.setString(3, content);
                pstmt.setString(4, startenddate);
                pstmt.execute();
            }
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            DatabaseConn.close(con, pstmt, null);
        }
    }

    //仅修改课题基本信息（除课题编号、适应专业外）
    public void modifybaseinfo(SubjectBean subject) throws Exception {
        SysarguBpo sysargubpo = new SysarguBpo();
        String modifyflag = sysargubpo.getSysargu("modifytaskbookflag").getArguvalue();
        if (modifyflag.equals("0")) throw new Exception("当前不允许修改任务书，若要修改请与系统管理员联系！");
        Connection con = DatabaseConn.getConnection();
        PreparedStatement pstmt = null;
        try {
            //更新 课题基本信息
            String vsql = "update tb_subject set subname=?,usedyear=?,oldargu=?,content=?,requirement=?,refpapers=?"
                    + ",subkind=?,subsource=?,subtype=?,isoutschool=?,tutorid=?,othertid=?,subprog=?,subdirection=?,subsort=?,modifytime=now() where subid=?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subject.getSubname());
            pstmt.setString(2, subject.getUsedyear());
            pstmt.setString(3, subject.getOldargu());
            pstmt.setString(4, subject.getContent());
            pstmt.setString(5, subject.getRequirement());
            pstmt.setString(6, subject.getRefpapers());
            pstmt.setString(7, subject.getSubkind());
            pstmt.setString(8, subject.getSubsource());
            pstmt.setString(9, subject.getSubtype());
            pstmt.setInt(10, subject.getIsoutschool());
            pstmt.setString(11, subject.getTutor().getTid());
            pstmt.setString(12, subject.getOthertutor().getTid());
            pstmt.setString(13, subject.getSubprog());
            pstmt.setString(14, subject.getSubdirection());
            pstmt.setString(15, subject.getSubsort());
            pstmt.setString(16, subject.getSubid());
            pstmt.executeUpdate();

            //删除进程信息
            vsql = "delete from tb_progress where subid=?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subject.getSubid());
            pstmt.executeUpdate();
            DatabaseConn.close(null, pstmt, null);
            //保存新进程信息
            for (Iterator<ProgressBean> itp = subject.getProgress().iterator(); itp.hasNext(); ) {
                ProgressBean progresstemp = itp.next();
                String inorder = progresstemp.getInorder();
                String content = progresstemp.getContent();
                String startenddate = progresstemp.getStartenddate();
                vsql = "insert into tb_progress(subid,inorder,content,startenddate) values(?,?,?,?)";
                pstmt = con.prepareStatement(vsql);
                pstmt.setInt(1, Integer.valueOf(subject.getSubid()));
                pstmt.setInt(2, Integer.valueOf(inorder));
                pstmt.setString(3, content);
                pstmt.setString(4, startenddate);
                pstmt.execute();
            }

        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, null);
        }
    }

    /**
     * 课题提交前判断每项是否有效（包括字数限制）  2014.11.09 wxh 2015.11.24 wxh修改
     */
    public void validateSubBeforeSubmit(String subid) throws Exception {
        SubjectBean subject = this.getBysubid(subid);
        //必填项
        if (subject.getSubname().equals("")) throw new Exception("课题名不能为空！");
        if (subject.getSubsort().equals("")) throw new Exception("课题类别不能为空！");
        if (subject.getSubkind().equals("")) throw new Exception("课题性质不能为空！");
        if (subject.getSubsource().equals("")) throw new Exception("课题来源不能为空！");
        if (subject.getSubtype().equals("")) throw new Exception("课题类型不能为空！");
        if (subject.getListspec().size() == 0) throw new Exception("课题专业不能为空！");
        //判断课题进度
        if (subject.getProgress().size() == 0) throw new Exception("课题进程表不能为空！");
        for (int i = 0; i < subject.getProgress().size(); i++) {
            ProgressBean prog = subject.getProgress().get(i);
            if (prog.getContent().equals("") || prog.getStartenddate().equals(""))
                throw new Exception("课题进程表中的每一项都不能为空！");
        }
        ////////////////////////////
        if (subject.getSubdirection().equals("")) throw new Exception("课题方向不能为空！");

        //字数限制
        if (subject.getOldargu().length() < 400) throw new Exception("概述当前字数少于400，不允许提交！");
        if (subject.getContent().length() < 400) throw new Exception("工作内容当前字数少于400，不允许提交！");
        if (subject.getRequirement().length() < 50) throw new Exception("工作基本要求当前字数少于50，不允许提交！");
    }

    /**
     * 教师提交申报的课题  2015.11.24 wxh修改
     */
    public void submitsuject(String subid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        if (subid == null) subid = "";
        //校验课题各项限制
        try {
            this.validateSubBeforeSubmit(subid);
        } catch (Exception e) {
            throw new Exception("[提交失败] " + e.getMessage());
        }
        //维护提交标志
        String vsql1 = "update tb_subject set submitflag='1',operatedtime=now() where subid=?";
        String vsql2 = "update tb_subspec set auditflag='',auditoption='',operatedtime=now() where subid=?";
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        try {
            con.setAutoCommit(false);
            pstmt1 = con.prepareStatement(vsql1);
            pstmt1.setString(1, subid);
            pstmt2 = con.prepareStatement(vsql2);
            pstmt2.setString(1, subid);
            pstmt1.executeUpdate();
            pstmt2.executeUpdate();
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            DatabaseConn.close(null, pstmt1, null);
            DatabaseConn.close(con, pstmt2, null);
        }
    }

    /**
     * 按subid删除课题
     */
    public void deleteinfo(String subid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        if (subid == null) subid = "";
        //判断课题是否提交，若课题已提交则不允许删除
        String vsql = "select * from tb_subject where submitflag='1' and subid=" + subid;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            pstmt = con.prepareStatement(vsql);
            rst = pstmt.executeQuery();
            if (rst.next()) {
                throw new Exception("课题<" + rst.getString("subname") + ">已提交不允许删除!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DatabaseConn.close(null, pstmt, rst);
        }
        // 删除对应专业、基本信息
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        String vsql1 = "delete from tb_subspec where subid=?";
        String vsql2 = "delete from tb_progress where subid=?";
        String vsql3 = "delete from tb_subject where subid=?";
        try {
            con.setAutoCommit(false);
            pstmt1 = con.prepareStatement(vsql1);
            pstmt1.setString(1, subid);
            pstmt2 = con.prepareStatement(vsql2);
            pstmt2.setString(1, subid);
            pstmt3 = con.prepareStatement(vsql3);
            pstmt3.setString(1, subid);
            pstmt1.executeUpdate();
            pstmt2.executeUpdate();
            pstmt3.executeUpdate();
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            DatabaseConn.close(null, pstmt1, null);
            DatabaseConn.close(null, pstmt2, null);
            DatabaseConn.close(con, pstmt3, null);
        }
    }

    public void deleteinfoall(String subid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        if (subid == null) subid = "";
        // 删除对应专业、基本信息
        PreparedStatement pstmt = null;
        String vsql1 = "delete from tb_stusub where subid=?";
        String vsql2 = "delete from tb_subspec where subid=?";
        String vsql3 = "delete from tb_progress where subid=?";
        String vsql4 = "delete from tb_subject where subid=?";
        try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(vsql1);
            pstmt.setString(1, subid);
            pstmt.addBatch();
            pstmt = con.prepareStatement(vsql2);
            pstmt.setString(1, subid);
            pstmt.addBatch();
            pstmt = con.prepareStatement(vsql3);
            pstmt.setString(1, subid);
            pstmt.addBatch();
            pstmt = con.prepareStatement(vsql4);
            pstmt.setString(1, subid);
            pstmt.addBatch();
            pstmt.executeUpdate();
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            DatabaseConn.close(con, pstmt, null);
        }
    }

    /*题目相似度比较
     * param subname为被比较的课题名
     * param threshold 相似度的阈值
     * return List<SubSimBean> 为与subname比较的题目及相似度，其中相似度大于threshold
     * 2013年
     * 2014.10.31 modify
     * */
    public List<SubSimBean> computesimilar(String subname, float threshold) throws Exception {
        Connection con = DatabaseConn.getConnection();
        List<SubSimBean> ret = new ArrayList<SubSimBean>();
        if (subname == null) subname = "";
        String vsql = "select tutorid, subname from tb_subject";
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        try {
            pstmt = con.prepareStatement(vsql);
            rst = pstmt.executeQuery();
            while (rst.next()) {
                String subname0 = rst.getString("subname");
                Float simd = Similarity.calculatesimilar(subname0, subname);
                if (simd < threshold) continue;
                String tutorid = rst.getString("tutorid");
                TeacherBpo teabpo = new TeacherBpo();
                String tutorname = teabpo.getBytid(tutorid).getTname();
                SubSimBean temp = new SubSimBean();
                temp.setSubname(subname0);
                temp.setSimilard(simd);
                temp.setTutorname(tutorname);
                ret.add(temp);
            }
            SimiComparator comparator = new SimiComparator();
            Collections.sort(ret, comparator);
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return ret;
    }

    /**
     * 与教师往年课题（近三年）进行相似度比较
     * 返回相似度大于等于阈值的课题名
     */
    public List<SubSimBean> computesimilarWithOldSubs(String tutorid, String subname, float threshold) throws Exception {
        Connection con = DatabaseConn.getConnection();
        List<SubSimBean> simsubs = new ArrayList<SubSimBean>();
        if (subname == null) subname = "";
        if (tutorid == null) tutorid = "";
        //获得当前毕业设计年份
        SysarguBpo sysargubpo = new SysarguBpo();
        String currentyear = sysargubpo.getSysargu("startdate").getArguvalue().substring(0, 3);
        String longyear = String.valueOf(Integer.valueOf(currentyear) - 3);//参与比较的最久年份

        String vsql = "select usedyear,subname from tb_subjecthis where tutorid=? and usedyear>?";
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        try {
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, tutorid);
            pstmt.setString(2, longyear);
            rst = pstmt.executeQuery();
            while (rst.next()) {
                String subname0 = rst.getString("subname");
                Float simd = Similarity.calculatesimilar(subname0, subname);
                if (simd < threshold) continue;
                SubSimBean temp = new SubSimBean();
                temp.setSubname(subname0);
                temp.setSimilard(simd);
                temp.setUsedyear(rst.getString("usedyear"));
                simsubs.add(temp);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return simsubs;
    }

    /**
     * 得到教师审核的课题信息（盲审）
     */
    public List<SubReviewBean> getSubjectsReviewedByTid(String tid) throws Exception {
        //检查毕业设计是否已经开始，若已开始则返回
        SysarguBpo sysargubpo = new SysarguBpo();
        if (sysargubpo.ifStartGraduate()) {
            throw new Exception("毕业设计已经开始，不允许再审核课题!");
        }
        //
        List<SubReviewBean> subreviews = new ArrayList<SubReviewBean>();
        Connection con = DatabaseConn.getConnection();
        String vsql = "select a.tid, a.subid,b.subname,reviewopinion from tb_reviewsubject a, tb_subject b where a.subid=b.subid and a.tid=?";
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, tid);
            rst = pstmt.executeQuery();
            while (rst.next()) {
                SubReviewBean temp = new SubReviewBean();
                temp.setTid(tid);
                String subid = rst.getString("subid");
                temp.setSubid(subid);
                String subname = rst.getString("subname");
                temp.setSubname(subname);
                temp.setReviewopinion(rst.getString("reviewopinion"));

                /**与自己的课题历史相似的课题数*/
                String tutorid = this.getBysubid(subid).getTutor().getTid();
                temp.setSimsubcount(this.computesimilarWithOldSubs(tutorid, subname, 0.9f).size());
                subreviews.add(temp);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return subreviews;
    }

    /**
     * 按教师修改课题审核结果（盲审）
     */
    public void modifyReviewOpinionBytid(String tid, List<SubReviewBean> subreviews) throws Exception {
        if (subreviews.size() == 0) throw new Exception("当前没有需要修改的审核意见！");
        Connection con = DatabaseConn.getConnection();
        PreparedStatement pstmt = null;
        con.setAutoCommit(false);
        try {
            for (Iterator<SubReviewBean> it = subreviews.iterator(); it.hasNext(); ) {
                SubReviewBean temp = it.next();
                String subid = temp.getSubid();
                String reviewopinion = temp.getReviewopinion();
                String vsql = "update tb_reviewsubject set reviewopinion=? where tid=? and subid=?";
                pstmt = con.prepareStatement(vsql);
                pstmt.setString(1, reviewopinion);
                pstmt.setString(2, tid);
                pstmt.setString(3, subid);
                pstmt.executeUpdate();
            }
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            DatabaseConn.close(con, pstmt, null);
        }
    }

    /**
     * 设置教师审核的课题（盲审）
     */
    public void addReviewOpinionBytid(String tid, List<SubReviewBean> subreviews) throws Exception {
        Connection con = DatabaseConn.getConnection();
        PreparedStatement pstmt = null;
        con.setAutoCommit(false);
        try {
            //第一步 删除tid的待审核课题
            String vsql0 = "delete from tb_reviewsubject where tid=?";
            pstmt = con.prepareStatement(vsql0);
            pstmt.setString(1, tid);
            pstmt.addBatch();
            //第二步 添加待审核的新课题
            for (Iterator<SubReviewBean> it = subreviews.iterator(); it.hasNext(); ) {
                SubReviewBean temp = it.next();
                String subid = temp.getSubid();
                String reviewopinion = temp.getReviewopinion();
                String vsql = "insert into tb_reviewsubject(tid,subid,reviewopinion) values(?,?,?)";
                pstmt = con.prepareStatement(vsql);
                pstmt.setString(1, tid);
                pstmt.setString(2, subid);
                pstmt.setString(3, reviewopinion);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            DatabaseConn.close(con, pstmt, null);
        }
    }

    /**
     * 查找每个课题的审核意见（盲审）
     */
    public String getReviewBySubid(String subid) throws Exception {
        String reviewopinion = "";
        Connection con = DatabaseConn.getConnection();
        String vsql = "select ifnull(reviewopinion,'') from tb_reviewsubject where subid=?";
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, subid);
            rst = pstmt.executeQuery();
            if (rst.next()) {
                reviewopinion = rst.getString(1);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return reviewopinion;
    }

    /**
     * 查询被学生选中的课题
     */
    public SubjectBean getSubjectByStuPicked(String stuid) throws Exception {
        StusubBpo stusubbpo = new StusubBpo();
        String subid = stusubbpo.getSubidForStu(stuid);
        if (subid.equals("")) return null;
        return this.getBysubid(subid);
    }

    /**
     * 恢复课题状态到“审核未通过”
     */
    public void restoreSubjectStatus(String subid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        String vsql1 = "update tb_subspec set auditflag='0',releaseflag=null where subid=?";
        String vsql2 = "update tb_subject set submitflag='0'where subid=?";
//    	String vsql3="delete from tb_stusub where subid=?";

        PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null;
        try {

            pstmt1 = con.prepareStatement(vsql1);
            pstmt1.setString(1, subid);

            pstmt2 = con.prepareStatement(vsql2);
            pstmt2.setString(1, subid);

//    		pstmt3 = con.prepareStatement(vsql3);
//    		pstmt3.setString(1, subid);

            con.setAutoCommit(false);
            pstmt1.execute();
            pstmt2.execute();
//			pstmt3.execute();
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            DatabaseConn.close(null, pstmt1, null);
            DatabaseConn.close(con, pstmt2, null);
        }
    }

}
