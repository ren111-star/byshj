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
     * Ϊ��ʦ����ä������
     */
    public String assignPaperToTeaForReview() throws Exception {
        //����ҵ����Ƿ��Ѿ���ʼ�����ѿ�ʼ�򷵻�
        SysarguBpo sysargubpo = new SysarguBpo();
        if (!sysargubpo.ifStartGraduate()) {
            throw new Exception("��ҵ��ƻ�δ��ʼ��������ä������!");
        }
        /////
        String assignresult = "";
        ArrayList<ReviewPaperBean> subreviews = new ArrayList<ReviewPaperBean>();
        HashMap<String, String> teanumforreview = new HashMap<String, String>();//����ä��Ľ�ʦ����
        Connection con = DatabaseConn.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            for (int i = 1; i <= 3; i++) {
                //ͳ��ÿ����ʦ�ѱ�ѡ�Ŀ�����Ŀ�����Ͽ��ⷽ��i������¼ÿ����ʦä����Ŀ
                ArrayList<TeacherSubNumBean> teasubnums = new ArrayList<TeacherSubNumBean>();
                //����ʱ��¼��Щ��ʦ����Ŀ�Ѿ��������
                List<TeacherSubNumBean> teasubnumarrtemp = new ArrayList<TeacherSubNumBean>();
                String vsql = "select tutorid,subid from tb_subject  where subdirection=? and submitflag='1' " +
                        "and subid in(select subid from tb_stusub where pickflag='1')" +
                        " order by tutorid desc";
                pstmt = con.prepareStatement(vsql);
                pstmt.setString(1, String.valueOf(i));
                rst = pstmt.executeQuery();

                String lasttutorid = null;
                //������ʦ��
                TeacherSubNumBean teacher = null;
                //�����м����ģ���Ҫ����new,��ֹһ����������
                TeacherSubNumBean temp = null;
                while (rst.next()) {
                    String tutorid = rst.getString("tutorid");
                    String subid = rst.getString("subid");

                    if (tutorid == null || subid == null) {
                        throw new Exception("��ʦ��Ż��߿γ̱��Ϊ�գ�����");//���쳣
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
                //��ʼ�������
                int teasum = teasubnums.size();
                if (teasum == 1) {
                    throw new Exception("ֻ��һ��ѧ��ѡ�˿��ⷽ�����Ϊ" + String.valueOf(i) + "�Ŀ��⣬�޷����з��䣡");
                } //һ���˲��÷���;
                TeaSubComparator comparator = new TeaSubComparator();
                Collections.sort(teasubnums, comparator);  //�ȷ�����Ŀ�����ʦ

                for (int j = 0; j < teasum; j++) {
                    //��ÿ����ʦ����ѭ������
                    TeacherSubNumBean teasub = teasubnums.get(j);
                    String tid = teasub.getTid();//ä���ʦ
                    int subsum = teasub.getSubsum();

                    //��temp�����а�����δ����Ŀ�������С��������
                    Collections.sort(teasubnumarrtemp, comparator);

                    boolean canadd = true;
                    while (canadd && subsum > 0) {
                        canadd = false;
                        for (int k = teasubnumarrtemp.size() - 1; k >= 0; k--) {
                            temp = teasubnumarrtemp.get(k);
                            if (temp.getTid().equals(tid)) {//ͬһ����
                                continue;
                            }
                            String subid = temp.getSubjects().get(0); //�϶�Ӧ���е�
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
                //������ʣ����⣬���ʣ�����һ��ֻ����һ����ʦ��������ʦƽ������
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
                            continue;//һ����
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
            //���������д�����ݿ�
            PreparedStatement pstmt1 = null, pstmt2 = null, pstmt3 = null;
            try {
                con.setAutoCommit(false);
                String vsql = "delete from tb_reviewpaper";//���ä���
                pstmt1 = con.prepareStatement(vsql);
                pstmt1.executeUpdate();

                String vsql3 = "update tb_subsubmit set paperblindstatus='2' where paperblindstatus='3'";//����tb_subsubmit�е�ä���ĵ�����ä��״̬ 3���2
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
        assignresult = "�ѷ���Ŀ�������Ϊ " + String.valueOf(subreviews.size() + " ,����ä��Ľ�ʦ��Ϊ " + teanumforreview.size());
        return assignresult;
    }

    /**
     * ����ä�����
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
                //������ä���־
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
     * ��������
     */
    public void cancelPaperReview(String subid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        con.setAutoCommit(false);
        PreparedStatement pstmt0 = null;
        PreparedStatement pstmt1 = null;
        try {
            //��Ϊ�ȴ�ä��
            pstmt0 = con.prepareStatement("update tb_subsubmit set paperblindstatus='2' where stuid=(select stuid from tb_stusub where pickflag='1' and subid=?)");
            pstmt0.setString(1, subid);
            pstmt1 = con.prepareStatement("update tb_reviewpaper set submitflag='0' where subid=?");//�����ύ��־Ϊ����״̬
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
     * �鿴ä�����
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
                    reviewpaper.setReviewtime(Date_String.getStringDate1());///��ǰʱ��yyyy-mm-dd
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

    //��ѯ��ʦָ��������Ŀ
    public List<SubNumByTeaBean> getAllpapernum() throws Exception {
        //����ҵ����Ƿ��Ѿ���ʼ�����ѿ�ʼ�򷵻�
        SysarguBpo sysargubpo = new SysarguBpo();
        if (!sysargubpo.ifStartGraduate()) {
            throw new Exception("��ҵ��ƻ�δ��ʼ��������ä������!");
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
     * �õ���ʦä���������Ϣ
     */
    public List<ReviewPaperBean> getPapersReviewedByTid(String tid) throws Exception {
        //����ҵ����Ƿ��Ѿ���ʼ�����ѿ�ʼ�򷵻�
        SysarguBpo sysargubpo = new SysarguBpo();
        SubSubmitBpo subsubmitbpo = new SubSubmitBpo();
        if (!sysargubpo.ifStartGraduate()) {
            throw new Exception("��ҵ��ƻ�δ��ʼ��������ä������!");
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
     * �� ѧ�� ��ѯ�����������
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
