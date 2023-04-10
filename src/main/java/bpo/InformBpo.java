package bpo;

import bean.InformBean;
import com.DatabaseConn;
import com.FileUtil;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class InformBpo {
	//导入学生基本信息
	private Connection con;
	public InformBpo()throws Exception
	{
		this.con=DatabaseConn.getConnection();
	}
	//王宜晓增加的代码
	//发布新信息功能
	public void AddInform(String title,String content,String time,String admin,String affixFilePath) throws Exception
	{
		String sql="insert into tb_inform(title,content,author,releasetime,affixpath) values('"+title+"','"+content+"','"+admin+"','"+time+"','"+affixFilePath+"')";
		try
		{
			PreparedStatement ps=con.prepareStatement(sql);
			ps.execute();
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			//con.close();
		}
		
	}
	//修改信息功能
	public void UpdateInform(int id,String title,String content,String time,String admin) throws Exception
	{
		String sql="update tb_inform  set title='"+title+"',content='"+content+"',author='"+admin+"',modifytime='"+time+"' where infid="+id;
		try
		{
			PreparedStatement ps=con.prepareStatement(sql);
			ps.execute();
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			//con.close();
			
		}
		
		
	}
	public List<InformBean> getAllinfo()throws Exception
	{
		String vsql="select * from tb_inform order by infid";
		PreparedStatement pstmt=con.prepareStatement(vsql);
		ResultSet rst=pstmt.executeQuery();
		List<InformBean> ret=new ArrayList<InformBean>();
		while(rst.next())
		{
			InformBean temp=new InformBean();
			temp.setInfid(rst.getInt("infid"));
			temp.setTitle(rst.getString("title"));
			ret.add(temp);
		}
        rst.close();
        pstmt.close();
		return ret;
	}
	//根据信息编号获得信息内容
	public List<InformBean> getInformByid(int id) throws Exception
	{
		List<InformBean> informs=new ArrayList<InformBean>();
		String sql="select * from tb_inform where infid="+id;
		ResultSet rs=null;
		PreparedStatement pstmt=con.prepareStatement(sql);
		rs=pstmt.executeQuery();
		if(rs.next())
		{
			InformBean informbean=new InformBean();
			informbean.setInfid(rs.getInt("infid"));
			informbean.setTitle(rs.getString("title"));
			informbean.setAuthor(rs.getString("author"));
			informbean.setReleasetime(rs.getString("releasetime"));
			informbean.setModifytime(rs.getString("modifytime"));
			informbean.setViewtimes(rs.getInt("viewtimes"));
			informs.add(informbean);
			}
		rs.close();
		pstmt.close();
		return informs;
	}
	public InformBean getInformById(int id,String rootDir) throws Exception
	{
		InformBean inform=new InformBean();
		String strAffixPath;
		String sql="select * from tb_inform where infid="+id;
		ResultSet rs=null;
		PreparedStatement pstmt=con.prepareStatement(sql);
		rs=pstmt.executeQuery();
		if(rs.next())
		{
			inform.setInfid(rs.getInt("infid"));
			inform.setTitle(rs.getString("title"));
			inform.setAuthor(rs.getString("author"));
			inform.setReleasetime(rs.getString("releasetime"));
			inform.setModifytime(rs.getString("modifytime"));
			inform.setViewtimes(rs.getInt("viewtimes"));
			inform.setTitle(rs.getString("title"));
			strAffixPath=rs.getString("affixpath");
			if(!(strAffixPath==null||strAffixPath.equals(""))){
				inform.setAffixpath(strAffixPath);
				strAffixPath=rootDir+strAffixPath;
				File dir = new File(strAffixPath);
				boolean wyx=dir.isDirectory();
				if(wyx){
					//将附件名称放入bean的AffixFiles中
					inform.setAffixFiles(FileUtil.getFilesinDirectory(strAffixPath));
				}
			}
			}
		rs.close();
		pstmt.close();
		return inform;
	}
	//获得所有信息内容
	public List<InformBean> getAllInform() throws Exception
	{
		List<InformBean> informs=new ArrayList<InformBean>();
		String sql="select * from tb_inform order by releasetime desc,modifytime desc";
		PreparedStatement ps=con.prepareStatement(sql);
		ResultSet rs=ps.executeQuery();
		while(rs.next())
		{
			InformBean bean=new InformBean();
			bean.setInfid(rs.getInt("infid"));
			bean.setTitle(rs.getString("title"));
			bean.setAuthor(rs.getString("author"));
			bean.setReleasetime(rs.getString("releasetime"));
		    bean.setModifytime(rs.getString("modifytime"));
			bean.setViewtimes(rs.getInt("viewtimes"));
			informs.add(bean);
		}
		rs.close();
		ps.close();
		return informs;
	}
	//获得附件的存储路径
	public String getPathbyId(int id) throws SQLException
	{
		String sql="select affixpath from tb_inform where infid="+id;
		String repath=null;
		PreparedStatement ps=con.prepareStatement(sql);
		ResultSet rs=ps.executeQuery();
		if(rs.next())
		{
			repath=rs.getString("affixpath");
		}
		rs.close();
		ps.close();
		return repath;
		
	}
	//点击次数的增加
	public void Addviewtimes(int id) throws Exception
	{
		String sql="update tb_inform set viewtimes=viewtimes+1 where infid="+id;
		try
		{
		PreparedStatement ps=con.prepareStatement(sql);
		ps.execute();
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	public boolean delbyid(int id) throws Exception
	{
		boolean rs=false;
		String sql="delete from tb_inform where infid="+id;
		try
		{
			PreparedStatement ps=con.prepareStatement(sql);
			ps.execute();
			rs=true;
		}
		catch(Exception e)
		{
			throw e;
		}
		return rs;
	}
}
