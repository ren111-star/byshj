/**
 * 
 */
package bpo;

import bean.SysarguBean;
import com.DatabaseConn;
import com.Date_String;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author wyx
 * wxh2011-12-31修改
 *
 */
public class SysarguBpo {
	/*按名字获得系统参数条目*/
	public SysarguBean getSysargu(String arguname) throws SQLException
	{
		Connection con=DatabaseConn.getConnection();
		PreparedStatement pstmt=null;
		ResultSet rst=null;
		SysarguBean sysargu=new SysarguBean();
		String sql="select * from tb_sysargu where arguname=?";
		try{
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, arguname);
			rst=pstmt.executeQuery();
			if(rst.next())
			{
				sysargu.setArguid(rst.getInt("arguid"));
				sysargu.setArguname(rst.getString("arguname"));
				sysargu.setArgutype(rst.getString("argutype"));
				sysargu.setArguvalue(rst.getString("arguvalue"));
				sysargu.setRemark(rst.getString("remark"));
			}
		}finally{
			DatabaseConn.close(con, pstmt, rst);
		}
		
		return sysargu;
	}
	/*得到系统参数列表*/
	public List<SysarguBean> getAllsysargu() throws SQLException
	{
		Connection con=DatabaseConn.getConnection();
		PreparedStatement pstmt=null;
		ResultSet rst=null;
		String sql="select * from tb_sysargu";
		List<SysarguBean> syslist=new ArrayList<SysarguBean>();
		try{
			pstmt=con.prepareStatement(sql);
			rst=pstmt.executeQuery();
			while(rst.next())
			{
				SysarguBean sb=new SysarguBean();
				sb.setArguid(rst.getInt("arguid"));
				sb.setArguname(rst.getString("arguname"));
				sb.setArgutype(rst.getString("argutype"));
				sb.setArguvalue(rst.getString("arguvalue"));
				sb.setRemark(rst.getString("remark"));
				syslist.add(sb);
			}
		}finally{
			DatabaseConn.close(con, pstmt, rst);
		}
		return syslist;
	}
	/*按照参数名修改参数值*/
	public void modifyArguByName(String arguname,String arguvalue) throws Exception
	{
		Connection con=DatabaseConn.getConnection();
		PreparedStatement pstmt=null;
		String vsql="";
		//修改前应先判断，参数值是否符合参数类型
		try{
			String argutype=this.getSysargu(arguname).getArgutype();
			if(argutype.equals("数值")){
				try{
					Integer.valueOf(arguvalue);
				}catch(Exception e){
					throw new Exception("参数值类型错误:"+arguname+"的值应为数值型！");
				}
			}
			if(argutype.equals("01开关数值")){
				if(!arguvalue.equals("0")&&!arguvalue.equals("1"))
					throw new Exception("参数值类型错误:"+arguname+"的值应为0或者1！");
			}
		}catch(Exception e){
			throw e;
		}
		//修改数据库
		vsql="update tb_sysargu set arguvalue=? where arguname=?";
		try{
			pstmt=con.prepareStatement(vsql);
			pstmt.setString(1, arguvalue);
			pstmt.setString(2, arguname);
			pstmt.executeUpdate();
		}finally{
			DatabaseConn.close(con, pstmt, null);
		}
	}
	/**检查毕业设计是否已经开始，即当前时间是否在设置的毕业设计开始时间之后*/
	public boolean ifStartGraduate()throws Exception{
    	String startdate=this.getSysargu("startdate").getArguvalue();
    	
    	Date currentdate=new Date();
    	java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
    	Date gradstartdate=format.parse(startdate);
    	long day=currentdate.getTime()-gradstartdate.getTime();//相隔天数
    	if(day<0){
    		return false;
    	}
		return true;
	}
	/**取两周的最后一天，进程表签字用*/
	public String getFillinDate(int inorder)throws Exception{
		String startdate=this.getSysargu("startdate").getArguvalue();
		java.text.SimpleDateFormat sd = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
		Date gradstartdate=sd.parse(startdate);
		
		Calendar c=Calendar.getInstance();
		c.setTime(gradstartdate);
		c.add(Calendar.DATE, (inorder/2)*14+13);
		Date date=c.getTime();
		
        return Date_String.getStringDate1(date);//yyyy-MM-dd
	}
}
