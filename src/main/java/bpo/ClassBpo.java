package bpo;

import bean.ClassBean;
import com.DatabaseConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClassBpo {
    public ClassBpo() throws Exception {
    }

    public void addinfo(ClassBean clas) throws Exception {
        Connection con = DatabaseConn.getConnection();
        //验证班级名是否已存在
        String vsql = "select * from byxxxt.tb_class where classname=?";
        PreparedStatement pstmt = con.prepareStatement(vsql);
        pstmt.setString(1, clas.getClassname());
        ResultSet rst = pstmt.executeQuery();
        if (rst.next()) throw new Exception("班级”" + clas.getClassname() + "“已存在，不允许重复添加！");
        //增加到数据库
        rst.close();
        vsql = "insert into byxxxt.tb_class(classname,specid,enrolyear,gradyear) values(?,?,?,?)";
        pstmt = con.prepareStatement(vsql);
        pstmt.setString(1, clas.getClassname());
        pstmt.setString(2, clas.getSpecid());
        pstmt.setString(3, clas.getEnrolyear());
        pstmt.setString(4, clas.getGradyear());

        pstmt.execute();
        DatabaseConn.close(con, pstmt, rst);
    }

    public List<ClassBean> getAllinfo(String specid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        if (specid == null) specid = "";
        String vsql = "select * from tb_class where specid like ? order by specid,classname";
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        List<ClassBean> ret = new ArrayList<ClassBean>();
        try {
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, specid + "%");
            rst = pstmt.executeQuery();
            while (rst.next()) {
                ClassBean temp = new ClassBean();
                temp.setClassid(rst.getString("classid"));
                temp.setClassname(rst.getString("classname"));
                String strspecid = rst.getString("specid");
                SpecialityBpo specbpo = new SpecialityBpo();
                temp.setSpeciality(specbpo.getByspecid(strspecid));
                temp.setEnrolyear(rst.getString("enrolyear"));
                temp.setGradyear(rst.getString("gradyear"));
                ret.add(temp);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return ret;
    }

    public ClassBean getByclassname(String classname) throws Exception {
        Connection con = DatabaseConn.getConnection();
        ClassBean temp = new ClassBean();
        if (null == classname) {
            classname = "";
        }
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            String vsql = "select * from tb_class where classname like ?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, classname + "%");
            rst = pstmt.executeQuery();
            while (rst.next()) {
                temp.setClassid(rst.getString("classid"));
                temp.setClassname(rst.getString("classname"));
                String specid = rst.getString("specid");
                SpecialityBpo specbpo = new SpecialityBpo();
                temp.setSpeciality(specbpo.getByspecid(specid));
                temp.setEnrolyear(rst.getString("enrolyear"));
                temp.setGradyear(rst.getString("gradyear"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return temp;
    }

    public void modifyinfo(ClassBean clas) throws Exception {
        Connection con = DatabaseConn.getConnection();
        ///////////////////////////////////////////////////////////
        String vsql = "update tb_class set classname=?,specid=?,enrolyear=?,gradyear=? where classid=? ";
        PreparedStatement pstmt = con.prepareStatement(vsql);
        pstmt.setString(1, clas.getClassname());
        pstmt.setString(2, clas.getSpecid());
        pstmt.setString(3, clas.getEnrolyear());
        pstmt.setString(4, clas.getGradyear());
        pstmt.setString(5, clas.getClassid());

        pstmt.execute();
        DatabaseConn.close(con, pstmt, null);
    }

    public void deleteinfo(String classname) throws Exception {
        Connection con = DatabaseConn.getConnection();
        //若该班级有学生，则不允许删除
        String vsql = "select * from tb_student where classname=?";
        PreparedStatement pstmt = con.prepareStatement(vsql);
        pstmt.setString(1, classname);
        ResultSet rst = pstmt.executeQuery();
        if (rst.next()) throw new Exception("班级”" + classname + "“有学生信息，不允许删除！");
        //……

        //直接删除
        vsql = "delete from tb_class where classname=?";
        pstmt = con.prepareStatement(vsql);
        pstmt.setString(1, classname);
        pstmt.execute();
        DatabaseConn.close(con, pstmt, null);
    }

}
