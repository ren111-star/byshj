package bpo;

import bean.RoleBean;
import bean.UserBean;
import com.DatabaseConn;
import com.MD5Util;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserBpo {
    public UserBpo() {
    }

    public void addinfo(UserBean user) throws Exception {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            con = DatabaseConn.getConnection();
            //��Ч����֤
            String vsql = "select * from tb_user where userid=?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, user.getUserid());
            rst = pstmt.executeQuery();
            while (rst.next()) {
                if (user.getUserid().equals(rst.getString("userid"))) {
                    throw new Exception("�û��Ѵ��ڣ�");
                }
            }
            ///////////////////////////////////////////////////////////
            vsql = "insert into tb_user(usertype,userid,username,userpwd) values(?,?,?,?)";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, user.getUsertype());
            pstmt.setString(2, user.getUserid());
            pstmt.setString(3, user.getUsername());
            pstmt.setString(4, "c56d0e9a7ccec67b4ea131655038d604");//�����û�Ĭ������Ϊ123456 ԭʼ��c56d0e9a7ccec67b4ea131655038d604

            pstmt.execute();
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
    }

    public List<UserBean> getAllinfo() throws Exception {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        List<UserBean> ret = new ArrayList<UserBean>();
        try {
            con = DatabaseConn.getConnection();
            String vsql = "select * from tb_user order by usertype,userid";
            pstmt = con.prepareStatement(vsql);
            rst = pstmt.executeQuery();

            while (rst.next()) {
                UserBean temp = new UserBean();
                temp.setUsertype(rst.getString("usertype"));
                temp.setUserid(rst.getString("userid"));
                temp.setUserpwd(rst.getString("userpwd"));
                temp.setUsername(rst.getString("username"));
                ret.add(temp);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return ret;
    }

    public List<UserBean> getSysusers(String userid) throws Exception {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        List<UserBean> ret = new ArrayList<UserBean>();
        try {
            con = DatabaseConn.getConnection();
            String vsql = "select * from tb_user where userid like ?" +
                    " and usertype not in('��ʦ','ѧ��')" +
                    " order by usertype,userid";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, userid + "%");
            rst = pstmt.executeQuery();

            while (rst.next()) {
                UserBean temp = new UserBean();
                temp.setUsertype(rst.getString("usertype"));
                temp.setUserid(rst.getString("userid"));
                //temp.setUserpwd(rst.getString("userpwd"));
                temp.setUsername(rst.getString("username"));
                ret.add(temp);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return ret;
    }

    public UserBean getinfo_userid(String userid) throws Exception {
        if (null == userid) {
            userid = "";
        }
        UserBean temp = new UserBean();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;

        try {
            con = DatabaseConn.getConnection();
            String vsql = "select * from tb_user where userid like ?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, userid + "%");
            rst = pstmt.executeQuery();
            while (rst.next()) {
                temp.setUsertype(rst.getString("usertype"));
                temp.setUserid(rst.getString("userid"));
                temp.setUserpwd(rst.getString("userpwd"));
                temp.setUsername(rst.getString("username"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return temp;
    }

    public void modifyinfo(UserBean user) throws Exception {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DatabaseConn.getConnection();
            //��Ч����֤
            ///////////////////////////////////////////////////////////
            String vsql = "update tb_user set usertype=?,username=? where userid=? ";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, user.getUsertype());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getUserid());
            pstmt.execute();
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, null);
        }
    }

    public void modifyPwd(String userid, String oldpwd, String newpwd) throws Exception {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        UserBean user = new UserBean();
        try {
            con = DatabaseConn.getConnection();
            //������MD5����
            user.setUserid(userid);
            user.setUserpwd(oldpwd);

            String vsql;
            vsql = "select * from tb_user where userid=?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, userid);
            rst = pstmt.executeQuery();
            if (!rst.next()) {
                throw new Exception("��¼��Ϣ����");
            }//�û������ڣ�
            if (!(rst.getString("userpwd").equals(user.getUserpwd()))) {
                throw new Exception("��¼��Ϣ����");//�������
            }
            ///////////////////////////////////////////////////////////
            vsql = "update tb_user set userpwd=? where userid=?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, newpwd);
            pstmt.setString(2, userid);
            pstmt.execute();
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
    }

    /**
     * @param userid
     * @throws Exception ��ʼ���û�����Ϊ123456(c56d0e9a7ccec67b4ea131655038d604) 3��md5���ܣ�ǰ��1�㣬���2��
     */
    public void initializepwd(String userid) throws Exception {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DatabaseConn.getConnection();
            String vsql = "update tb_user set userpwd='c56d0e9a7ccec67b4ea131655038d604' where userid=?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, userid);
            pstmt.execute();
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, null);
        }
    }

    public void deleteinfo(String struserid) throws Exception {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DatabaseConn.getConnection();
            String vsql = "delete from tb_user where userid=?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, struserid);
            pstmt.execute();
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, null);
        }
    }

    public void isexisted(UserBean user) throws Exception {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            con = DatabaseConn.getConnection();
            if (user == null) {
                throw new Exception("�û�δ��¼����Ȩ���ʸ�ҳ�棡");
            }
            //��Ч����֤
            String vsql = "select * from tb_user where userid=?";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, user.getUserid());
            rst = pstmt.executeQuery();
            String errmsg = "��¼��Ϣ����";
            if (!rst.next()) {
                throw new Exception(errmsg);
            }//�û������ڣ�
            if (!(rst.getString("usertype").equals(user.getUsertype()))) {
                throw new Exception("�û����ʹ���");//�û����ʹ���
            }
            if (!(rst.getString("userpwd").equals(user.getUserpwd()))) {
                throw new Exception(errmsg);//�������
            }
        } catch (Exception e) {
//			e.printStackTrace();
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
    }

    //����û���Ӧ��ɫ
    List<RoleBean> getrolesbyuser(String userid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        List<RoleBean> ret = new ArrayList<RoleBean>();
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        String vsql = "select roleid,rolename,roledesc from tb_role where " +
                "roleid in(select roleid from tb_userrole where userid=?)";
        try {
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, userid);
            rst = pstmt.executeQuery();
            while (rst.next()) {
                RoleBean temp = new RoleBean();
                temp.setRoleid(rst.getString("roleid"));
                temp.setRolename(rst.getString("rolename"));
                temp.setRoledesc(rst.getString("roledesc"));
                ret.add(temp);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return ret;
    }

    @Test
    public void test() throws Exception {
        List<UserBean> users = this.getAllinfo();
        for (UserBean user : users) {
            String newpwd = MD5Util.md5Mix(user.getUserpwd());
            this.modifyPwd(user.getUserid(), user.getUserpwd(), newpwd);
        }
    }
}
