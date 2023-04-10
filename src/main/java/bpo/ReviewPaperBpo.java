package bpo;

import bean.*;
import com.DatabaseConn;
import com.Date_String;
import com.TeaSubComparator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class ReviewPaperBpo {
    /**
     * 为教师分配盲审论文
     */
    public String assignPaperToTeaForReview() throws Exception {
        //检查毕业设计是否已经开始，若已开始则返回
        SysarguBpo sysargubpo = new SysarguBpo();
        if (!sysargubpo.ifStartGraduate()) {
            throw new Exception("毕业设计还未开始，不允许盲审论文!");
        }
        /////
        String assignresult = "";
        ArrayList<ReviewPaperBean> subreviews = new ArrayList<ReviewPaperBean>();
        HashMap<String, String> teanumforreview = new HashMap<String, String>();//参与盲审的教师数量
        Connection con = DatabaseConn.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            for (int i = 1; i <= 3; i++) {
                //统计每个教师已被选的课题数目（符合课题方向i），记录每个老师盲审题目
                ArrayList<TeacherSubNumBean> teasubnums = new ArrayList<TeacherSubNumBean>();
                //分配时记录哪些老师的题目已经被分配掉
                List<TeacherSubNumBean> teasubnumarrtemp = new ArrayList<TeacherSubNumBean>();
                String vsql = "select tutorid,subid from tb_subject  where subdirection=? and submitflag='1' " +
                        "and subid in(select subid from tb_stusub where pickflag='1')" +
                        " order by tutorid desc";
                pstmt = con.prepareStatement(vsql);
                pstmt.setString(1, String.valueOf(i));
                rst = pstmt.executeQuery();

                String lasttutorid = null;
                //生成老师的
                TeacherSubNumBean teacher = null;
                //生成中间结果的，需要重新new,防止一个对象引用
                TeacherSubNumBean temp = null;
                while (rst.next()) {
                    String tutorid = rst.getString("tutorid");
                    String subid = rst.getString("subid");

                    if (tutorid == null || subid == null) {
                        throw new Exception("教师编号或者课程编号为空！！！");//抛异常
                    }
                    if (tutorid.equals(lasttutorid)) {
                        teacher.setSubsum(teacher.getSubsum() + 1);
                        temp.setSubsum(temp.getSubsum() + 1);
                        temp.getSubjects().add(subid);
                    } else {
                        lasttutorid = tutorid;
                        teacher = new TeacherSubNumBean();
                        teacher.setTid(tutorid);
                        temp = new TeacherSubNumBean();
                        temp.setTid(tutorid);
                        teasubnums.add(teacher);
                        teasubnumarrtemp.add(temp);

                        teacher.setSubsum(1);
                        temp.setSubsum(1);
                        temp.getSubjects().add(subid);
                    }
                }
                //开始分配课题
                int teasum = teasubnums.size();
                if (teasum == 1) {
                    throw new Exception("只有一个学生选了课题方向代码为" + String.valueOf(i) + "的课题，无法进行分配！");
                } //一个人不用分配;
                TeaSubComparator comparator = new TeaSubComparator();
                Collections.sort(teasubnums, comparator);  //先分审题目多的老师

                for (int j = 0; j < teasum; j++) {
                    //对每个老师进行循环分配
                    TeacherSubNumBean teasub = teasubnums.get(j);
                    String tid = teasub.getTid();//盲审教师
                    int subsum = teasub.getSubsum();

                    //将temp过程中按仍尚未分配的课题数从小到大排序
                    Collections.sort(teasubnumarrtemp, comparator);

                    boolean canadd = true;
                    while (canadd && subsum > 0) {
                        canadd = false;
                        for (int k = teasubnumarrtemp.size() - 1; k >= 0; k--) {
                            temp = teasubnumarrtemp.get(k);
                            if (temp.getTid().equals(tid)) {//同一个人
                                continue;
                            }
                            String subid = temp.getSubjects().get(0); //肯定应该有的
                            temp.getSubjects().remove(0);
                            temp.setSubsum(temp.getSubsum() - 1);
                            ReviewPaperBean subrevtemp = new ReviewPaperBean();
                            subrevtemp.setReviewerid(tid);
                            subrevtemp.setSubid(subid);
                            subreviews.add(subrevtemp);
                            if (temp.getSubsum() == 0) {
                                teasubnumarrtemp.remove(k);
                            }
                            canadd = true;
                            subsum--;
                            if (subsum <= 0) break;
                        }
                    }

                }
                //若还有剩余课题，则该剩余课题一定只属于一个教师，其余老师平均分配
                if (teasubnumarrtemp.size() > 0) {
                    temp = teasubnumarrtemp.get(0);
                    int left = temp.getSubsum();
                    int teachercursor = -1;
                    while (left > 0) {
                        if (teachercursor >= teasubnums.size() - 1) {
                            teachercursor = -1;
                        }
                        teachercursor++;
                        if (teasubnums.get(teachercursor).getTid().equals(temp.getTid())) {
                            continue;//一个人
                        }

                        String sid = temp.getSubjects().get(0);
                        temp.getSubjects().remove(0);
                        temp.setSubsum(temp.getSubsum() - 1);
                        ReviewPaperBean subrevtemp = new ReviewPaperBean();
                        subrevtemp.setReviewerid(teasubnums.get(teachercursor).getTid());
                        subrevtemp.setSubid(sid);
                        subreviews.add(subrevtemp);
                        left--;
                    }
                }
            }
            //将分配情况写入数据库
            PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null;
            try {
                con.setAutoCommit(false);
                String vsql = "delete from tb_reviewpaper";//清空盲审表
                pstmt1 = con.prepareStatement(vsql);
                pstmt1.executeUpdate();

                String vsql3 = "update tb_subsubmit set paperblindstatus='2' where paperblindstatus='3'";//撤销tb_subsubmit中的盲审文档的已盲审状态 3变成2
                pstmt3 = con.prepareStatement(vsql3);
                pstmt3.executeUpdate();

                String vsqltemp = "insert into tb_reviewpaper(reviewerid,subid) values(?,?)";
                pstmt2 = con.prepareStatement(vsqltemp);

                int revsum = subreviews.size();
                for (int i1 = 0; i1 < revsum; i1++) {
                    ReviewPaperBean subrevtemp = subreviews.get(i1);
                    String tid = subrevtemp.getReviewerid();
                    teanumforreview.put(tid, tid);
                    String subid = subrevtemp.getSubid();

                    pstmt2.setString(1, tid);
                    pstmt2.setString(2, subid);
                    pstmt2.addBatch();
                }
                pstmt2.executeBatch();
                con.commit();
                pstmt2.clearBatch();
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
                DatabaseConn.close(null, pstmt1, null);
                DatabaseConn.close(null, pstmt3, null);
                DatabaseConn.close(null, pstmt2, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        assignresult = "已分配的课题总数为 " + String.valueOf(subreviews.size() + " ,参与盲审的教师数为 " + teanumforreview.size());
        return assignresult;
    }

    /**
     * 设置盲审意见
     */
    public void setReviewOpinion(ReviewPaperBean reviewpaper) throws Exception {
        Connection con = DatabaseConn.getConnection();
        con.setAutoCommit(false);
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement("update tb_reviewpaper set significance=?,designcontent=?,composeability=?," +
                    "translationlevel=?,innovative=?,reviewopinion=?,reviewtime=?, submitflag=?" +
                    "where subid=?");
            pstmt.setFloat(1, reviewpaper.getSignificance());
            pstmt.setFloat(2, reviewpaper.getDesigncontent());
            pstmt.setFloat(3, reviewpaper.getComposeability());
            pstmt.setFloat(4, reviewpaper.getTranslationlevel());
            pstmt.setFloat(5, reviewpaper.getInnovative());
            pstmt.setString(6, reviewpaper.getReviewopinion());
            Date reviewtime = new Date();
            pstmt.setTimestamp(7, new java.sql.Timestamp(reviewtime.getTime()));
            pstmt.setString(8, reviewpaper.getSubmitflag());
            pstmt.setString(9, reviewpaper.getSubid());
            pstmt.executeUpdate();
            if (reviewpaper.getSubmitflag().equals("1")) {
                //设置已盲审标志
                pstmt1 = con.prepareStatement("update tb_subsubmit set paperblindstatus='3' where stuid=(select stuid from tb_stusub where pickflag='1' and subid=?)");
                pstmt1.setString(1, reviewpaper.getSubid());
                pstmt1.executeUpdate();
            }
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            DatabaseConn.close(null, pstmt1, null);
            DatabaseConn.close(con, pstmt, null);
        }
    }

    /**
     * 撤销评阅
     */
    public void cancelPaperReview(String subid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        con.setAutoCommit(false);
        PreparedStatement pstmt0 = null;
        PreparedStatement pstmt1 = null;
        try {
            //改为等待盲审
            pstmt0 = con.prepareStatement("update tb_subsubmit set paperblindstatus='2' where stuid=(select stuid from tb_stusub where pickflag='1' and subid=?)");
            pstmt0.setString(1, subid);
            pstmt1 = con.prepareStatement("update tb_reviewpaper set submitflag='0' where subid=?");//设置提交标志为保存状态
            pstmt1.setString(1, subid);

            pstmt0.executeUpdate();
            pstmt1.executeUpdate();
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            DatabaseConn.close(null, pstmt0, null);
            DatabaseConn.close(con, pstmt1, null);
        }
    }

    /**
     * 查看盲审意见
     */
    public ReviewPaperBean getReviewOpinion(String subid) throws Exception {
        ReviewPaperBean reviewpaper = new ReviewPaperBean();
        Connection con = DatabaseConn.getConnection();
        PreparedStatement pstmt = con.prepareStatement("select * from tb_reviewpaper where subid=?");
        pstmt.setString(1, subid);
        ResultSet rst = null;
        try {
            rst = pstmt.executeQuery();
            if (rst.next()) {
                reviewpaper.setSubid(rst.getString("subid"));
                reviewpaper.setSignificance(rst.getFloat("significance"));
                reviewpaper.setDesigncontent(rst.getFloat("designcontent"));
                reviewpaper.setComposeability(rst.getFloat("composeability"));
                reviewpaper.setTranslationlevel(rst.getFloat("translationlevel"));
                reviewpaper.setInnovative(rst.getFloat("innovative"));
                reviewpaper.setSumgrade(rst.getFloat("significance") + rst.getFloat("designcontent") + rst.getFloat("composeability") + rst.getFloat("translationlevel") + rst.getFloat("innovative"));
                reviewpaper.setReviewopinion(rst.getString("reviewopinion"));
                if (rst.getTimestamp("reviewtime") != null) {
                    reviewpaper.setReviewtime(Date_String.getStringDate1(rst.getTimestamp("reviewtime")));///
                } else {
                    reviewpaper.setReviewtime(Date_String.getStringDate1());///当前时间yyyy-mm-dd
                }
                reviewpaper.setSubmitflag(rst.getString("submitflag"));
                reviewpaper.setReviewerid(rst.getString("reviewerid"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return reviewpaper;
    }

    //查询教师指导论文数目
    public List<SubNumByTeaBean> getAllpapernum() throws Exception {
        //检查毕业设计是否已经开始，若已开始则返回
        SysarguBpo sysargubpo = new SysarguBpo();
        if (!sysargubpo.ifStartGraduate()) {
            throw new Exception("毕业设计还未开始，不允许盲审论文!");
        }
        /////
        List<SubNumByTeaBean> subnumarr = new ArrayList<SubNumByTeaBean>();
        Connection con = DatabaseConn.getConnection();
        String vsql = "select tid,tname,submitpapernum,reviewpapernum,unrevnum  from v_papernumbytea where submitpapernum!=0 order by tid";
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            pstmt = con.prepareStatement(vsql);
            rst = pstmt.executeQuery();
            while (rst.next()) {
                SubNumByTeaBean temp = new SubNumByTeaBean();
                temp.setTid(rst.getString("tid"));
                temp.setTname(rst.getString("tname"));
                temp.setSubmitsubnum(rst.getInt("submitpapernum"));
                temp.setReviewsubnum(rst.getInt("reviewpapernum"));
                temp.setUnrevnum(rst.getInt("unrevnum"));
                subnumarr.add(temp);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }

        return subnumarr;
    }

    /**
     * 得到教师盲审的论文信息
     */
    public List<ReviewPaperBean> getPapersReviewedByTid(String tid) throws Exception {
        //检查毕业设计是否已经开始，若已开始则返回
        SysarguBpo sysargubpo = new SysarguBpo();
        SubSubmitBpo subsubmitbpo = new SubSubmitBpo();
        if (!sysargubpo.ifStartGraduate()) {
            throw new Exception("毕业设计还未开始，不允许盲审论文!");
        }
        //
        List<ReviewPaperBean> paperreviews = new ArrayList<ReviewPaperBean>();
        Connection con = DatabaseConn.getConnection();
        String vsql = "select a.reviewerid, a.subid,b.subname,a.submitflag,(significance+designcontent+composeability+translationlevel+innovative) "
                + "as sumgrade from tb_reviewpaper a, tb_subject b where a.subid=b.subid and a.reviewerid=?";
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, tid);
            rst = pstmt.executeQuery();
            while (rst.next()) {
                ReviewPaperBean temp = new ReviewPaperBean();
                temp.setReviewerid(tid);
                String subid = rst.getString("subid");
                String docstatus = subsubmitbpo.getUploadstatusBySubid(subid, "paperblind");
                temp.setSubid(subid);
                temp.setDocstatus(docstatus);
                temp.setSubname(rst.getString("subname"));
                temp.setSubmitflag(rst.getString("submitflag"));
                temp.setSumgrade(rst.getFloat("sumgrade"));
                paperreviews.add(temp);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return paperreviews;
    }

    /**
     * 按 学生 查询论文评阅情况
     */
    public List<ReviewPaperBaseInfoBean> getPaperReviewInfos(String specid, String classname, String sname) throws Exception {
        List<ReviewPaperBaseInfoBean> ret = new ArrayList<ReviewPaperBaseInfoBean>();
        StudentBpo stubpo = new StudentBpo();
        SubjectBpo subbpo = new SubjectBpo();
        TeacherBpo teabpo = new TeacherBpo();
        List<StudentBean> students = stubpo.getAllinfo(specid, classname, sname);
        for (Iterator<StudentBean> it = students.iterator(); it.hasNext(); ) {
            ReviewPaperBaseInfoBean temp = new ReviewPaperBaseInfoBean();
            StudentBean student = it.next();
            String stuid = student.getStuid();
            temp.setClassname(student.getClassname());
            temp.setStuid(stuid);
            temp.setSname(student.getSname());

            SubjectBean subject = subbpo.getSubjectByStuPicked(stuid);
            String subid = subject.getSubid();
            temp.setSubid(subid);
            temp.setSubname(subject.getSubname());

            String tutornames = subject.getTutor().getTname();
            String othertname = subject.getOthertutor().getTname();
            if (!othertname.equals("")) {
                tutornames = tutornames + "/" + othertname;
            }
            temp.setTutornames(tutornames);
            ReviewPaperBean reviewpaper = this.getReviewOpinion(subid);
            String reviewerid = reviewpaper.getReviewerid();
            temp.setReviewername(teabpo.getBytid(reviewerid).getTname());

            temp.setSubmitflag(reviewpaper.getSubmitflag());
            ret.add(temp);
        }

        return ret;
    }
}
