package bpo;

import bean.StudentBean;
import bean.StusubBean;
import com.DatabaseConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StudentBpo {
    public StudentBpo() throws Exception {
    }

    public void addinfo(StudentBean student) throws Exception {
        Connection con = DatabaseConn.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        //有效性验证
        String vsql = "select * from tb_student where stuid=?";
        try {
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, student.getStuid());
            rst = pstmt.executeQuery();
            if (rst.next()) {
                if (student.getStuid().equals(rst.getString("stuid"))) {
                    throw new Exception(student.getStuid() + "学号已存在！");
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(null, pstmt, rst);
        }
        //判断班级名是否有效
        try {
            vsql = "select * from tb_class where classname=?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, student.getClassname());
            rst = pstmt.executeQuery();
            if (!rst.next()) throw new Exception(student.getStuid() + "的班级名称不存在！");
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(null, pstmt, rst);
        }
        con.setAutoCommit(false);
        PreparedStatement pstmt1 = null;
        try {
            vsql = "insert into tb_student(stuid,sname,classname,email,telphone) values(?,?,?,?,?)";
            String vsql1 = "insert into tb_user(usertype,userid,username,userpwd) values(?,?,?,?)";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, student.getStuid());
            pstmt.setString(2, student.getSname());
            pstmt.setString(3, student.getClassname());
            pstmt.setString(4, student.getEmail());
            pstmt.setString(5, student.getTelphone());
            pstmt.executeUpdate();

            pstmt1 = con.prepareStatement(vsql1);
            pstmt1.setString(1, "学生");
            pstmt1.setString(2, student.getStuid());
            pstmt1.setString(3, student.getSname());
            pstmt1.setString(4, "c56d0e9a7ccec67b4ea131655038d604");//新增用户默认密码为123456
            pstmt1.executeUpdate();

            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            DatabaseConn.close(null, pstmt, rst);
            DatabaseConn.close(con, pstmt1, null);
        }
    }

    //批量录入学生基本信息（导入文件调用）
    public void addinfoBatch(List<StudentBean> students) throws Exception {
        Connection con = DatabaseConn.getConnection();
        PreparedStatement pstmt = null;
        PreparedStatement pstmt1 = null;
        con.setAutoCommit(false);
        try {
            Iterator it = students.iterator();
            String vsql = "insert into tb_student(stuid,sname,classname) values(?,?,?)";
            String vsql1 = "insert into tb_user(usertype,userid,username,userpwd) values(?,?,?,?)";

            pstmt = con.prepareStatement(vsql);
            pstmt1 = con.prepareStatement(vsql1);

            while (it.hasNext()) {
                StudentBean temp = (StudentBean) it.next();
                //学生有效性验证
                PreparedStatement pstmt0 = null;
                ResultSet rst = null;
                /*有效性验证开始*/
                String vsql0 = "select * from tb_student where stuid=?";
                try {
                    pstmt0 = con.prepareStatement(vsql0);
                    pstmt0.setString(1, temp.getStuid());
                    rst = pstmt0.executeQuery();
                    if (rst.next()) {
                        if (temp.getStuid().equals(rst.getString("stuid"))) {
                            throw new Exception(temp.getStuid() + "学号已存在！");
                        }
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    DatabaseConn.close(null, pstmt0, rst);
                }
                //判断班级名是否有效
                try {
                    vsql0 = "select * from tb_class where classname=?";
                    pstmt0 = con.prepareStatement(vsql0);
                    pstmt0.setString(1, temp.getClassname());
                    rst = pstmt0.executeQuery();
                    if (!rst.next())
                        throw new Exception(temp.getStuid() + "的班级名称'" + temp.getClassname() + "'不存在！");
                } catch (Exception e) {
                    throw e;
                } finally {
                    DatabaseConn.close(null, pstmt0, rst);
                }
                /*验证结束*/
                pstmt.setString(1, temp.getStuid());
                pstmt.setString(2, temp.getSname());
                pstmt.setString(3, temp.getClassname());
                pstmt.addBatch();

                pstmt1.setString(1, "学生");
                pstmt1.setString(2, temp.getStuid());
                pstmt1.setString(3, temp.getSname());
                pstmt1.setString(4, "c56d0e9a7ccec67b4ea131655038d604");//新增用户默认密码为123456
                pstmt1.addBatch();

            }

            pstmt.executeBatch();
            pstmt1.executeBatch();

            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            DatabaseConn.close(null, pstmt, null);
            DatabaseConn.close(con, pstmt1, null);
        }
    }

    //
    public List<StudentBean> getAllinfo(String specid, String classname, String sname) throws Exception {
        Connection con = DatabaseConn.getConnection();
        if (classname == null) classname = "";
        if (sname == null) sname = "";
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        List<StudentBean> ret = new ArrayList<StudentBean>();
        try {
            if (specid == null || specid.equals("")) throw new Exception("专业不能为空！");
            String vsql = "select * from tb_student where classname like ? and sname like ? " +
                    " and classname in (select classname from tb_class where specid=?) " +
                    "order by classname,stuid";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, classname + "%");
            pstmt.setString(2, sname + "%");
            pstmt.setString(3, specid);
            rst = pstmt.executeQuery();

            while (rst.next()) {
                StudentBean temp = new StudentBean();
                temp.setStuid(rst.getString("stuid"));
                temp.setSname(rst.getString("sname"));
                temp.setSsex(rst.getString("ssex"));
                String classname0 = rst.getString("classname");
                temp.setClassname(classname0);
                ClassBpo classbpo = new ClassBpo();
                temp.setClassbean(classbpo.getByclassname(classname0));
                temp.setEmail(rst.getString("email"));
                temp.setTelphone(rst.getString("telphone"));
                temp.setRemark(rst.getString("remark"));
                ret.add(temp);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return ret;
    }

    public StudentBean getBystuid(String stuid) throws Exception {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        StudentBean temp;
        try {
            con = DatabaseConn.getConnection();
            String vsql = "select * from tb_student where stuid = ?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, stuid);
            rst = pstmt.executeQuery();
            temp = new StudentBean();
            while (rst.next()) {
                temp.setStuid(rst.getString("stuid"));
                temp.setSname(rst.getString("sname"));
                temp.setSsex(rst.getString("ssex"));
                String classname = rst.getString("classname");
                temp.setClassname(classname);
                ClassBpo classbpo = new ClassBpo();
                temp.setClassbean(classbpo.getByclassname(classname));
                temp.setEmail(rst.getString("email"));
                temp.setTelphone(rst.getString("telphone"));
                temp.setRemark(rst.getString("remark"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }

        return temp;
    }

    //检查学生选题状态
    public String getStuStatus(String stuid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        String status = "";
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            String vsql1 = "select * from tb_stusub where stuid=?";
            pstmt = con.prepareStatement(vsql1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstmt.setString(1, stuid);
            rst = pstmt.executeQuery();
//            while (rst.next()) {
//                System.out.println(rst.getString("subid"));
//                System.out.println("-------------------");
//            }
            rst.last();
            int row = rst.getRow();
            rst.beforeFirst();
            if (row == 0) {
                status = "未选";
                return status;
            }
            int unpasssubcount = 0;//落选课题数
            boolean assignflag = false;//需指派标志
            while (rst.next()) {
                String pickflag = rst.getString("pickflag");
                int subid = rst.getInt("subid");
                if (pickflag == null || pickflag.equals("")) {
                    if (subid != 0) {
                        status = "已初选";
                        break;
                    } else {
                        assignflag = true;
                        break;
                    }
                } else if (pickflag.equals("1")) {
                    status = "已选/" + String.valueOf(subid);
                    break;
                } else if (pickflag.equals("0")) {
                    unpasssubcount++;
                }
            }
            if (assignflag) {
                if (row == 1) {
                    status = "需指派";
                } else if ((row - 1) == unpasssubcount) {
                    status = "落选-需指派";
                }
            } else {
                if (row == unpasssubcount) status = "落选-需重选";
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return status;
    }

    //
    public void modifyinfo(StudentBean student) throws Exception {
        Connection con = DatabaseConn.getConnection();
        //有效性验证
        PreparedStatement pstmt = null;
        try {
            String vsql = "update tb_student set sname=?,ssex=?,classname=?,email=?,telphone=?,remark=? where stuid=? ";
            pstmt = con.prepareStatement(vsql);

            pstmt.setString(1, student.getSname());
            pstmt.setString(2, student.getSsex());
            pstmt.setString(3, student.getClassname());
            pstmt.setString(4, student.getEmail());
            pstmt.setString(5, student.getTelphone());
            pstmt.setString(6, student.getRemark());
            pstmt.setString(7, student.getStuid());

            pstmt.execute();
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, null);
        }
    }

    /**
     * @param student 学生个人能修改除编号、姓名、班级之外的其他信息
     * @throws Exception
     */
    public void modifypersonalinfo(StudentBean student) throws Exception {
        Connection con = DatabaseConn.getConnection();
        PreparedStatement pstmt = null;
        String vsql;

        ///////////////////////////////////////////////////////////
        try {
            vsql = "update tb_student set ssex=?,email=?,telphone=? where stuid=? ";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, student.getSsex());
            pstmt.setString(2, student.getEmail());
            pstmt.setString(3, student.getTelphone());
            pstmt.setString(4, student.getStuid());

            pstmt.execute();
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, null);
        }
    }

    public void deleteinfo(String stuid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String vsql = "";
        //学生存在已选中的课题，则不允许删除
        try {
            vsql = "select * from tb_stusub where stuid=?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, stuid);
            rst = pstmt.executeQuery();
            if (rst.next()) throw new Exception("系统中存在学生" + stuid + "的选题记录，不允许删除！");
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(null, pstmt, rst);
        }
        //删除学生信息和用户信息
        PreparedStatement pstmt1 = null;
        con.setAutoCommit(false);
        try {
            vsql = "delete from tb_student where stuid=?";
            String vsql1 = "delete from tb_user where userid=?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, stuid);
            pstmt1 = con.prepareStatement(vsql1);
            pstmt1.setString(1, stuid);
            pstmt.executeUpdate();
            pstmt1.executeUpdate();

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

}
