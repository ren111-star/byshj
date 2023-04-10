package bpo;

import bean.MyWeekSumBean;
import com.DatabaseConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class WeekSumBpo {
    public WeekSumBpo() {
    }

    ////为学生stuid生成空白周总结,共毕业设计周数个
    public void generateBlankWeekup(String stuid) throws Exception {
        //获得毕业设计周数
        SysarguBpo argubpo = new SysarguBpo();
        int weeknum = Integer.parseInt(argubpo.getSysargu("graduateweeknum").getArguvalue());
        //为学生stuid生成空白周总结
        Connection con = DatabaseConn.getConnection();
        con.setAutoCommit(false);
        PreparedStatement pstmt1 = con.prepareStatement("delete from byxxxt.tb_weekup where stuid=?");
        pstmt1.setString(1, stuid);
        PreparedStatement pstmt = con.prepareStatement("insert into tb_weekup(stuid,weekorder) values(?,?)");

        for (int i = 1; i < weeknum + 1; i++) {
            pstmt.setString(1, stuid);
            pstmt.setInt(2, i);
            pstmt.addBatch();
        }
        try {
            pstmt1.execute();
            pstmt.executeBatch();
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

    //为所有学生生成周总结,初始化提交文档表
    public void initStartGraduate() throws Exception {
        //判断毕业设计是否已经开始，若已开始，不允许重复初始化
        SysarguBpo sysargubpo = new SysarguBpo();
        if (sysargubpo.ifStartGraduate()) {
            throw new Exception("毕业设计已经开始，不允重复“开始毕业设计”!");
        }
        //获得毕业设计周数
        SysarguBpo argubpo = new SysarguBpo();
        int weeknum = Integer.parseInt(argubpo.getSysargu("graduateweeknum").getArguvalue());
        Connection con = DatabaseConn.getConnection();
        PreparedStatement pstmt = con.prepareStatement("select stuid from tb_student");
        ResultSet rst = null;
        try {
            rst = pstmt.executeQuery();
        } catch (Exception e) {
            throw e;
        }

        con.setAutoCommit(false);
        PreparedStatement pstmt1 = con.prepareStatement("delete from tb_weekup");
        PreparedStatement pstmt2 = con.prepareStatement("insert into tb_weekup(stuid,weekorder) values(?,?)");
        //初始化文档提交表
        PreparedStatement pstmt3 = con.prepareStatement("delete from tb_subsubmit");
        PreparedStatement pstmt4 = con.prepareStatement("insert into tb_subsubmit(stuid) values(?)");
        while (rst.next()) {
            String stuid = rst.getString("stuid");
            //周总结
            for (int i = 1; i < weeknum + 1; i++) {
                pstmt2.setString(1, stuid);
                pstmt2.setInt(2, i);
                pstmt2.addBatch();
            }
            //文档提交
            pstmt4.setString(1, stuid);
            pstmt4.addBatch();
        }
        try {
            pstmt1.executeUpdate();
            pstmt2.executeBatch();
            pstmt3.executeUpdate();
            pstmt4.executeBatch();

            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            DatabaseConn.close(null, pstmt, rst);
            DatabaseConn.close(null, pstmt2, null);
            DatabaseConn.close(null, pstmt3, null);
            DatabaseConn.close(null, pstmt4, null);
            DatabaseConn.close(con, pstmt1, null);
        }
    }

    //学生填写周总结
    public void fillInWeekupForStu(MyWeekSumBean weeksum) throws Exception {
        Connection con = DatabaseConn.getConnection();
        String vsql = "update tb_weekup set thiscontent=?,support=?,nextcontent=? where stuid=? and weekorder=?";
        PreparedStatement pstmt = con.prepareStatement(vsql);
        pstmt.setString(1, weeksum.getThiscontent());
        pstmt.setString(2, weeksum.getSupport());
        pstmt.setString(3, weeksum.getNextcontent());
        pstmt.setString(4, weeksum.getStuid());
        pstmt.setInt(5, weeksum.getWeekorder());
        try {
            int rowcount = pstmt.executeUpdate();
            if (rowcount == 0) {
                throw new Exception("没有相应的空白周总结，保存失败！");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, null);
        }
    }

    //教师填写周总结意见
    public void fillInWeekupForTea(MyWeekSumBean weeksum) throws Exception {
        Connection con = DatabaseConn.getConnection();
        String vsql = "update tb_weekup set tutorreply=?,tutorreview=? where stuid=? and weekorder=?";
        PreparedStatement pstmt = con.prepareStatement(vsql);
        pstmt.setString(1, weeksum.getTutorreply());
        pstmt.setString(2, weeksum.getTutorreview());
        pstmt.setString(3, weeksum.getStuid());
        pstmt.setInt(4, weeksum.getWeekorder());
        try {
            pstmt.execute();
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, null);
        }
    }

    //按周次获得指定学生的周总结
    public MyWeekSumBean getWeekupByWeek(String stuid, int weekorder) throws Exception {
        MyWeekSumBean weeksumbean = new MyWeekSumBean();
        Connection con = null;
        String vsql = "select * from tb_weekup where stuid=? and weekorder=?";
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            con = DatabaseConn.getConnection();
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, stuid);
            pstmt.setString(2, String.valueOf(weekorder));
            rst = pstmt.executeQuery();
            if (rst.next()) {
                weeksumbean.setThiscontent(rst.getString("thiscontent"));
                weeksumbean.setSupport(rst.getString("support"));
                weeksumbean.setNextcontent(rst.getString("nextcontent"));
                weeksumbean.setTutorreply(rst.getString("tutorreply"));
                weeksumbean.setTutorreview(rst.getString("tutorreview"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return weeksumbean;
    }

    /**
     * 得到每个课题的检查意见，两周一次
     */
    public String getCheckOpinion(String stuid, int inorder) throws Exception {
        int weekorder1 = inorder + 1;
        int weekorder2 = inorder + 2;
        String checkopinion = this.getWeekupByWeek(stuid, weekorder1).getTutorreview() + "\n" + this.getWeekupByWeek(stuid, weekorder2).getTutorreview();

        return checkopinion;
    }

    /**
     * 按学生 获得已完成周总结份数  [所有项都已完善，包括教师评语]
     */
    public int getWeekupNum(String stuid) throws Exception {
        int weekupnum = 0;
        Connection con = null;
        String vsql = "select validweekupnum from v_weekupnum where stuid=?";
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            con = DatabaseConn.getConnection();
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, stuid);
            rst = pstmt.executeQuery();
            if (rst.next()) {
                weekupnum = rst.getInt("validweekupnum");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return weekupnum;
    }
    /**周总结份数统计*/
    //public

}
