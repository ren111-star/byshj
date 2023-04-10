package com;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SyscodeBpo {
    public SyscodeBpo() throws Exception {
    }

    public void addOrmod(SyscodeBean syscode) throws Exception {
        Connection con = DatabaseConn.getConnection();
        ///////////////////////////////////////////////////////////
        String vsql = "";
        PreparedStatement pstmt;
        ResultSet rst = null;
        String codeid = syscode.getCodeid();

        if (codeid.equals("")) {//新增代码记录
            //有效性验证
            vsql = "select * from tb_syscode where codeno='" + syscode.getCodeno() + "' and codevalue='" + syscode.getCodevalue() + "'";
            pstmt = con.prepareStatement(vsql);
            rst = pstmt.executeQuery();
            if (rst.next())
                throw new Exception(syscode.getCodename() + "中，代码值“" + syscode.getCodevalue() + "”已存在，不允许重复填加！");
            vsql = "insert into tb_syscode(codeno,codename,codevalue,codecontent) values(?,?,?,?)";
            pstmt = con.prepareStatement(vsql);
            pstmt.setString(1, syscode.getCodeno());
            pstmt.setString(2, syscode.getCodename());
            pstmt.setString(3, syscode.getCodevalue());
            pstmt.setString(4, syscode.getCodecontent());
            try {
                pstmt.execute();
            } catch (Exception e) {
                throw e;
            } finally {
                DatabaseConn.close(con, pstmt, rst);
            }


        } else {//修改代码记录
            vsql = "update tb_syscode set codename='" + syscode.getCodename()
                    + "',codecontent='" + syscode.getCodecontent() + "'" +
                    " where codeid=" + syscode.getCodeid();
            pstmt = con.prepareStatement(vsql);
            try {
                pstmt.execute();
            } catch (Exception e) {
                throw e;
            } finally {
                DatabaseConn.close(con, pstmt, rst);
            }
        }

    }

    public List<SyscodeBean> getAllinfo(String codename) throws Exception {
        Connection con = DatabaseConn.getConnection();
        String vsql = "select * from tb_syscode where codename like '" + codename + "%' order by codeno,codevalue";
        PreparedStatement pstmt = con.prepareStatement(vsql);

        ResultSet rst = pstmt.executeQuery();
        List<SyscodeBean> ret = new ArrayList<SyscodeBean>();
        while (rst.next()) {
            SyscodeBean temp = new SyscodeBean();
            temp.setCodeid(rst.getString("codeid"));
            temp.setCodeno(rst.getString("codeno"));
            temp.setCodename(rst.getString("codename"));
            temp.setCodevalue(rst.getString("codevalue"));
            temp.setCodecontent(rst.getString("codecontent"));
            ret.add(temp);
        }
        DatabaseConn.close(con, pstmt, rst);
        return ret;
    }

    public List<SyscodeBean> getcodeByno(String codeno) throws Exception {
        Connection con = DatabaseConn.getConnection();
        String vsql = "select * from tb_syscode where codeno ='" + codeno + "' order by codevalue";
        PreparedStatement pstmt = con.prepareStatement(vsql);
        ResultSet rst = pstmt.executeQuery();
        List<SyscodeBean> ret = new ArrayList<SyscodeBean>();
        while (rst.next()) {
            SyscodeBean temp = new SyscodeBean();
            temp.setCodeid(rst.getString("codeid"));
            temp.setCodeno(rst.getString("codeno"));
            temp.setCodename(rst.getString("codename"));
            temp.setCodevalue(rst.getString("codevalue"));
            temp.setCodecontent(rst.getString("codecontent"));
            ret.add(temp);
        }
        DatabaseConn.close(con, pstmt, rst);
        return ret;
    }

    public SyscodeBean getcodeByid(String codeid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        String vsql = "select * from tb_syscode where codeid =" + codeid;
        PreparedStatement pstmt = con.prepareStatement(vsql);
        ResultSet rst = pstmt.executeQuery();
        SyscodeBean temp = new SyscodeBean();
        while (rst.next()) {
            temp.setCodeid(rst.getString("codeid"));
            temp.setCodeno(rst.getString("codeno"));
            temp.setCodename(rst.getString("codename"));
            temp.setCodevalue(rst.getString("codevalue"));
            temp.setCodecontent(rst.getString("codecontent"));
        }
        DatabaseConn.close(con, pstmt, rst);
        return temp;
    }

    public SyscodeBean getcode(String codeno, String codevalue) throws Exception {
        Connection con = DatabaseConn.getConnection();
        SyscodeBean temp = new SyscodeBean();
        String vsql = "select * from tb_syscode where codeno ='" + codeno + "' and codevalue='" + codevalue + "'";
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            pstmt = con.prepareStatement(vsql);
            rst = pstmt.executeQuery();
            if (rst.next()) {
                temp.setCodeid(rst.getString("codeid"));
                temp.setCodeno(rst.getString("codeno"));
                temp.setCodename(rst.getString("codename"));
                temp.setCodevalue(rst.getString("codevalue"));
                temp.setCodecontent(rst.getString("codecontent"));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            DatabaseConn.close(con, pstmt, rst);
        }
        return temp;
    }

    public void deleteinfo(String codeid) throws Exception {
        Connection con = DatabaseConn.getConnection();
        //删除前应判断代码是否被使用，若已被使用则不允许删除
        //
        String vsql = "delete from tb_syscode where codeid =" + codeid;
        PreparedStatement pstmt = con.prepareStatement(vsql);
        pstmt.execute();
        DatabaseConn.close(con, pstmt, null);
    }

    /*获取web服务器相对路径*/
    public static String getServerPath() {
        String serverpath = "../webapps/databasejpkch";//tomcat
        //String serverpath="../deploy/jpkch";//resin
        //System.out.println(System.getProperty("user.dir"));/*显示的是%Tomcat_Home%/bin*/
        return serverpath;
    }
}
