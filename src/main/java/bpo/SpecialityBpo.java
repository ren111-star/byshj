package bpo;

import bean.SpecialityBean;
import com.DatabaseConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SpecialityBpo {
    private Connection con;

    public SpecialityBpo() throws Exception {
    }

    public void addinfo(SpecialityBean speciality) throws Exception {
        Connection con = DatabaseConn.getConnection();
        //有效性验证
        String vsql = "select * from tb_speciality where specid=?";
        PreparedStatement pstmt = con.prepareStatement(vsql);
        pstmt.setString(1, speciality.getSpecid());
        ResultSet rst = pstmt.executeQuery();
        while (rst.next()) {
            if (speciality.getSpecid().equals(rst.getString("specid"))) {
                throw new Exception(speciality.getSpecid() + "专业编号已存在！");
            }
        }

        vsql = "insert into tb_speciality values(?,?,?)";
        pstmt = con.prepareStatement(vsql);
        pstmt.setString(1, speciality.getSpecid());
        pstmt.setString(2, speciality.getSpecname());
        pstmt.setString(3, speciality.getSpecmagtid());

        try {
            pstmt.execute();
        } catch (Exception e) {
            throw e;
        }
        DatabaseConn.close(con, pstmt, rst);
    }

    public List<SpecialityBean> getAllinfo() throws Exception {
        Connection con = DatabaseConn.getConnection();
        String vsql = "select * from tb_speciality order by specid";
        PreparedStatement pstmt = con.prepareStatement(vsql);
        ResultSet rst = pstmt.executeQuery();
        List<SpecialityBean> ret = new ArrayList<SpecialityBean>();
        TeacherBpo teacherbpo = new TeacherBpo();
        while (rst.next()) {
            SpecialityBean temp = new SpecialityBean();

            temp.setSpecid(rst.getString("specid"));
            temp.setSpecname(rst.getString("specname"));
            String vspecmagtid = rst.getString("specmagtid");
            temp.setSpecmagtid(vspecmagtid);
            temp.setSpecmagtname(teacherbpo.getBytid(vspecmagtid).getTname());
            ret.add(temp);
        }
        DatabaseConn.close(con, pstmt, rst);
        return ret;
    }

    public List<SpecialityBean> getAllinfo(String specid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        if (specid == null) specid = "";
        String vsql = "select * from tb_speciality where specid like ? order by specid";
        PreparedStatement pstmt = con.prepareStatement(vsql);
        pstmt.setString(1, specid + "%");
        ResultSet rst = pstmt.executeQuery();
        List<SpecialityBean> ret = new ArrayList<SpecialityBean>();
        while (rst.next()) {
            SpecialityBean temp = new SpecialityBean();
            temp.setSpecid(rst.getString("specid"));
            temp.setSpecname(rst.getString("specname"));
            temp.setSpecmagtid(rst.getString("specmagtid"));
            ret.add(temp);
        }
        DatabaseConn.close(con, pstmt, rst);
        return ret;
    }

    public SpecialityBean getByspecid(String specid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        SpecialityBean temp = new SpecialityBean();
        try {
            if (null == specid) {
                specid = "";
            }
            String vsql = "select * from tb_speciality where specid like ?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, specid + "%");
            rst = pstmt.executeQuery();
            while (rst.next()) {
                temp.setSpecid(rst.getString("specid"));
                temp.setSpecname(rst.getString("specname"));
                temp.setSpecmagtid(rst.getString("specmagtid"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return temp;
    }

    /**
     * @param tid
     * @return tid管理的专业信息
     * @throws Exception
     */
    public SpecialityBean getspecmagBytid(String tid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        if (null == tid) {
            tid = "";
        }
        String vsql = "select * from tb_speciality where specmagtid =?";
        PreparedStatement pstmt = con.prepareStatement(vsql);
        pstmt.setString(1, tid);
        ResultSet rst = pstmt.executeQuery();
        SpecialityBean temp = new SpecialityBean();
        while (rst.next()) {
            temp.setSpecid(rst.getString("specid"));
            temp.setSpecname(rst.getString("specname"));
            temp.setSpecmagtid(rst.getString("specmagtid"));
        }
        DatabaseConn.close(con, pstmt, rst);
        return temp;
    }

    public void modifyinfo(SpecialityBean speciality) throws Exception {
        Connection con = DatabaseConn.getConnection();
        //有效性验证
        PreparedStatement pstmt = null;
        String vsql;
        ///////////////////////////////////////////////////////////
        vsql = "update tb_speciality set specname=?,specmagtid=? where specid=? ";
        pstmt = con.prepareStatement(vsql);
        pstmt.setString(1, speciality.getSpecname());
        pstmt.setString(2, speciality.getSpecmagtid());
        pstmt.setString(3, speciality.getSpecid());

        pstmt.execute();
        DatabaseConn.close(con, pstmt, null);
    }

    /**
     * @param specid
     * @param specmagtid 修改后的专业负责人id
     * @throws Exception
     */
    public void modifySpecMag(String specid, String specmagtid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        //有效性验证 specmagtid是否存在
        String vsql = "select * from tb_teacher where tid=?";
        PreparedStatement pstmt = con.prepareStatement(vsql);
        pstmt.setString(1, specmagtid);
        ResultSet rst = pstmt.executeQuery();
        if (!rst.next()) throw new Exception("教师编号“" + specmagtid + "”不存在！");
        //修改信息
        vsql = "update tb_speciality set specmagtid=? where specid=?";
        pstmt = con.prepareStatement(vsql);
        pstmt.setString(1, specmagtid);
        pstmt.setString(2, specid);
        pstmt.execute();
        DatabaseConn.close(con, pstmt, rst);
    }

    public void deleteinfo(String specid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        //……

        String vsql = "delete from tb_speciality where specid=?";
        PreparedStatement pstmt = con.prepareStatement(vsql);
        pstmt.setString(1, specid);
        pstmt.execute();
        DatabaseConn.close(con, pstmt, null);
    }

}
