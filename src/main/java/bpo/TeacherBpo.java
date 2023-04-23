package bpo;

import bean.SubNumByTeaBean;
import bean.SubReviewBean;
import bean.TeacherBean;
import bean.TeacherSubNumBean;
import com.DatabaseConn;
import com.SyscodeBean;
import com.SyscodeBpo;
import com.TeaSubComparator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * @author wxh
 *
 */
public class TeacherBpo {
	public TeacherBpo()throws Exception
	{
	}
	/**
	 * @param teacher
	 * @throws Exception
	 * 增加教师基本信息，同时增加教师用户
	 */
	public void addinfo(TeacherBean teacher)throws Exception 
	{
		Connection con=DatabaseConn.getConnection();
//		有效性验证
		String vsql ="select * from tb_teacher where tid=?";
		PreparedStatement pstmt=con.prepareStatement(vsql);
		pstmt.setString(1, teacher.getTid());
		ResultSet rst=pstmt.executeQuery();
		while(rst.next())
		{
			if(teacher.getTid().equals(rst.getString("tid")))
			{ 
				throw new Exception(teacher.getTid()+"教师编号已存在！");
			}
		}
		
		con.setAutoCommit(false);
		PreparedStatement pstmt1=null;
		try{
			vsql="insert into tb_teacher(tid,tname,tsex,specid,tdept,tpost,tdegree,studydirect,email,telephone,remark) "+
			     "values(?,?,?,?,?,?,?,?,?,?,?)";
			String vsql1="insert into tb_user(usertype,userid,username,userpwd) values(?,?,?,?)";
			
			pstmt=con.prepareStatement(vsql);
			pstmt.setString(1,teacher.getTid());
			pstmt.setString(2,teacher.getTname());
			pstmt.setString(3,teacher.getTsex());
			pstmt.setString(4,teacher.getSpecbean().getSpecid());
			pstmt.setString(5,teacher.getTdept());
			pstmt.setString(6,teacher.getTpost());
			pstmt.setString(7,teacher.getTdegree());
			pstmt.setString(8,teacher.getStudydirect());
			pstmt.setString(9,teacher.getEmail());
			pstmt.setString(10,teacher.getTelephone());
			pstmt.setString(11,teacher.getRemark());
			pstmt.executeUpdate();
		
			pstmt1=con.prepareStatement(vsql1);
			pstmt1.setString(1,"教师");
			pstmt1.setString(2, teacher.getTid());
			pstmt1.setString(3, teacher.getTname());
			pstmt1.setString(4,"c56d0e9a7ccec67b4ea131655038d604");//新增用户默认密码为123456
			pstmt1.executeUpdate();
			
			con.commit();
		}catch(Exception e){
			con.rollback();
			throw e;
		}finally{
			con.setAutoCommit(true);
			DatabaseConn.close(null, pstmt1, null);
			DatabaseConn.close(con, pstmt, rst);
		}
	}
	/**
	 * @param teachers
	 * @throws Exception
	 * 批量录入教师基本信息（导入文件调用）
	 */
	public void addinfoBatch(List<TeacherBean> teachers)throws Exception 
	{
		Connection con=DatabaseConn.getConnection();
		PreparedStatement pstmt=null;
		PreparedStatement pstmt1=null;
		String vsql="insert into tb_teacher(tid,tname,tdept,tpost,tdegree) values(?,?,?,?,?)";
		String vsql1="insert into tb_user(usertype,userid,username,userpwd) values(?,?,?,?)";
		pstmt=con.prepareStatement(vsql);
		pstmt1=con.prepareStatement(vsql1);
		
		Iterator it = teachers.iterator();
		while (it.hasNext()) {
			TeacherBean temp=(TeacherBean)it.next();
			//教师有效性验证
			PreparedStatement pstmt0=null;
			ResultSet rst=null;
			/*有效性验证开始*/
			String vsql0 ="select * from tb_teacher where tid=?";
			try{
				pstmt0=con.prepareStatement(vsql0);
				pstmt0.setString(1, temp.getTid());
				rst=pstmt0.executeQuery();
				if(rst.next())
				{
					if(temp.getTid().equals(rst.getString("tid")))
					{ 
						throw new Exception(temp.getTid()+"职工号已存在！");
					}
				}
			}catch(Exception e){
				throw e;
			}finally{
				rst.close();
				pstmt0.close();
				DatabaseConn.close(null, pstmt0, rst);
			}
			//判断教研室、职称、学位code值在不为空的情况下是否有效
			try{
				SyscodeBean syscode=new SyscodeBean();
				SyscodeBpo syscodebpo=new SyscodeBpo();
				if(!temp.getTdept().equals("")){
					syscode=syscodebpo.getcode("jxdw", temp.getTdept());
					if(syscode.getCodeid().equals("")) throw new Exception(temp.getTid()+"的教研室代码'"+temp.getTdept()+"'不存在！");
				}
				if(!temp.getTpost().equals("")){
					syscode=syscodebpo.getcode("zhch", temp.getTpost());
					if(syscode.getCodeid().equals("")) throw new Exception(temp.getTid()+"的职称代码'"+temp.getTpost()+"'不存在！");
				}
				if(!temp.getTdegree().equals("")){
					syscode=syscodebpo.getcode("xw", temp.getTdegree());
					if(syscode.getCodeid().equals("")) throw new Exception(temp.getTid()+"的学位代码'"+temp.getTdegree()+"'不存在！");
				}
				
			}catch(Exception e){
				throw e;
			}
			/*验证结束，准备执行插入语句*/
			pstmt.setString(1,temp.getTid());
			pstmt.setString(2,temp.getTname());
			pstmt.setString(3,temp.getTdept());
			pstmt.setString(4,temp.getTpost());
			pstmt.setString(5,temp.getTdegree());
			pstmt.addBatch();
			
			pstmt1.setString(1,"教师");
			pstmt1.setString(2, temp.getTid());
			pstmt1.setString(3, temp.getTname());
			pstmt1.setString(4,"c56d0e9a7ccec67b4ea131655038d604");//新增用户默认密码为123456
			pstmt1.addBatch();
		}
		//批量执行操作
		try{
			con.setAutoCommit(false);
			pstmt.executeBatch();
			pstmt1.executeBatch();
			con.commit();
		}catch(Exception e){
			con.rollback();
			throw e;
		}finally{
			con.setAutoCommit(true);
			DatabaseConn.close(null, pstmt1, null);
			DatabaseConn.close(con, pstmt, null);
		}
	}
	
	public List<TeacherBean> getAllinfo()throws Exception
	{
		Connection con=DatabaseConn.getConnection();
		String vsql="select * from tb_teacher order by tid,specid,tdept";
		PreparedStatement pstmt=con.prepareStatement(vsql);
		ResultSet rst=pstmt.executeQuery();
		List<TeacherBean> ret=new ArrayList<TeacherBean>();
		SpecialityBpo specbpo=new SpecialityBpo();
		UserBpo userbpo=new UserBpo();
		while(rst.next())
		{
			TeacherBean temp=new TeacherBean();
			temp.setTid(rst.getString("tid"));
			temp.setTname(rst.getString("tname"));
			temp.setTsex(rst.getString("tsex"));
			String specid=rst.getString("specid");
			temp.setSpecbean(specbpo.getByspecid(specid));
			temp.setTdept(rst.getString("tdept"));
			temp.setTpost(rst.getString("tpost"));
			temp.setTdegree(rst.getString("tdegree"));
			temp.setStudydirect(rst.getString("studydirect"));
			temp.setEmail(rst.getString("email"));
			temp.setTelephone(rst.getString("telephone"));
			temp.setRemark(rst.getString("remark"));
			temp.setListrole(userbpo.getrolesbyuser(rst.getString("tid")));
			ret.add(temp);
		}
		DatabaseConn.close(con, pstmt, rst);
		return ret;
	}
	
	/**
	 * @param tdept
	 * @param tname
	 * @return List<TeacherBean>
	 * @throws Exception
	 */
	public List<TeacherBean> getAllinfo(String tdept,String tname)throws Exception
	{
		Connection con=DatabaseConn.getConnection();
		List<TeacherBean> ret=new ArrayList<TeacherBean>();
		SpecialityBpo specbpo=new SpecialityBpo();
		UserBpo userbpo=new UserBpo();
		SyscodeBpo syscodebpo=new SyscodeBpo();
		PreparedStatement pstmt=null;
		ResultSet rst=null;
		try{
			if(tname==null) tname="";
			String vsql="select * from tb_teacher where ifnull(tdept,'') like ? and tname like ? order by tdept,tid";
			pstmt=con.prepareStatement(vsql);
			pstmt.setString(1, tdept+"%");
			pstmt.setString(2, tname+"%");
			rst=pstmt.executeQuery();
			while(rst.next())  // t
			{
				TeacherBean temp=new TeacherBean();
				temp.setTid(rst.getString("tid"));
				temp.setTname(rst.getString("tname"));
				temp.setTsex(rst.getString("tsex"));
				String specid=rst.getString("specid");
				temp.setSpecbean(specbpo.getByspecid(specid));
				String tdept0=rst.getString("tdept");
				temp.setTdept(tdept0);
				//从code表中取名称jxdw,zhch,xw
				if(tdept0!=null&&!tdept0.equals("")){
					temp.setTdeptname(syscodebpo.getcode("jxdw",tdept0).getCodecontent());
				}
				String tpost0=rst.getString("tpost");
				temp.setTpost(tpost0);
				if(tpost0!=null&&!tpost0.equals("")){
					temp.setTpostname(syscodebpo.getcode("zhch",tpost0).getCodecontent());
				}
				String tdegree0=rst.getString("tdegree");
				temp.setTdegree(tdegree0);
				if(tdegree0!=null&&!tdegree0.equals("")){
					temp.setTdegreename(syscodebpo.getcode("xw",tdegree0).getCodecontent());
				}
				temp.setStudydirect(rst.getString("studydirect"));
				temp.setEmail(rst.getString("email"));
				temp.setTelephone(rst.getString("telephone"));
				temp.setRemark(rst.getString("remark"));
				temp.setListrole(userbpo.getrolesbyuser(rst.getString("tid")));
				ret.add(temp);
			}
	     }catch(Exception e){
	    	 throw e;
	     }finally{
				DatabaseConn.close(con, pstmt, rst);
		 }
		return ret;
	}
	public TeacherBean getBytid(String tid)throws Exception
	{
		Connection con=DatabaseConn.getConnection();
		if(null == tid){
			tid="";
		}	
		String vsql="select * from tb_teacher where tid = ?";
		PreparedStatement pstmt=null;
		ResultSet rst=null;
		TeacherBean temp=new TeacherBean();
		SpecialityBpo specbpo=new SpecialityBpo();
		UserBpo userbpo=new UserBpo();
		SyscodeBpo syscodebpo=new SyscodeBpo();
		try{
			pstmt=con.prepareStatement(vsql);
			pstmt.setString(1, tid);
			rst=pstmt.executeQuery();
			while(rst.next())
			{
				temp.setTid(rst.getString("tid"));
				temp.setTname(rst.getString("tname"));
				String tsex=rst.getString("tsex");
				temp.setTsex(tsex);
				if(!(tsex==null||tsex.equals(""))){
					temp.setTsexname(syscodebpo.getcode("xb", tsex).getCodecontent());
				}
				String specid=rst.getString("specid");
				temp.setSpecbean(specbpo.getByspecid(specid));
				String tdept=rst.getString("tdept");
				temp.setTdept(tdept);
				if(!(tdept==null||tdept.equals(""))){
					temp.setTdeptname(syscodebpo.getcode("jxdw", tdept).getCodecontent());
				}
				String tpost=rst.getString("tpost");
				temp.setTpost(tpost);
				if(!(tpost==null||tpost.equals(""))){
					temp.setTpostname(syscodebpo.getcode("zhch", tpost).getCodecontent());
				}
				String tdegree=rst.getString("tdegree");
				temp.setTdegree(tdegree);
				if(!(tdegree==null||tdegree.equals(""))){
					temp.setTdegreename(syscodebpo.getcode("xw", tdegree).getCodecontent());
				}
				temp.setStudydirect(rst.getString("studydirect"));
				temp.setEmail(rst.getString("email"));
				temp.setTelephone(rst.getString("telephone"));
				temp.setRemark(rst.getString("remark"));
				temp.setListrole(userbpo.getrolesbyuser(tid));
			}
		}catch(Exception e){
			throw e;
		}finally{
			DatabaseConn.close(con, pstmt, rst);
		}
		return temp;
	}
	public void modifyinfo(TeacherBean teacher)throws Exception
	{
		Connection con=DatabaseConn.getConnection();
		//有效性验证
		PreparedStatement pstmt=null;
		String vsql;
		///////////////////////////////////////////////////////////
		vsql="update tb_teacher set tname=?,tsex=?,specid=?,tdept=?,tpost=?,tdegree=?,studydirect=?,email=?,telephone=?,remark=? where tid=? ";
	    pstmt=con.prepareStatement(vsql);
		pstmt.setString(1,teacher.getTname());
		pstmt.setString(2,teacher.getTsex());
		pstmt.setString(3,teacher.getSpecbean().getSpecid());
		pstmt.setString(4,teacher.getTdept());
		pstmt.setString(5,teacher.getTpost());
		pstmt.setString(6,teacher.getTdegree());
		pstmt.setString(7,teacher.getStudydirect());
		pstmt.setString(8,teacher.getEmail());
		pstmt.setString(9,teacher.getTelephone());
		pstmt.setString(10,teacher.getRemark());
		pstmt.setString(11,teacher.getTid());
		
		pstmt.execute();
		DatabaseConn.close(con, pstmt, null);
	}
	
	/**
	 * @param teacher 教师个人能修改除编号、姓名、所属教研室之外的其他信息
	 * @throws Exception
	 */
	public void modifypersonalinfo(TeacherBean teacher)throws Exception
	{
		Connection con=DatabaseConn.getConnection();
		PreparedStatement pstmt=null;
		String vsql;
		///////////////////////////////////////////////////////////
		try {
			vsql="update tb_teacher set tsex=?,tpost=?,studydirect=?,email=?,telephone=?,tdegree=? where tid=? ";
			pstmt=con.prepareStatement(vsql);
			pstmt.setString(1,teacher.getTsex());
			pstmt.setString(2,teacher.getTpost());
			pstmt.setString(3,teacher.getStudydirect());
			pstmt.setString(4,teacher.getEmail());
			pstmt.setString(5,teacher.getTelephone());
			pstmt.setString(6,teacher.getTdegree());
			pstmt.setString(7,teacher.getTid());
			
			pstmt.execute();
		} catch (Exception e) {
			throw e;
		}finally{
			DatabaseConn.close(con, pstmt, null);
		}
	}
	public void deleteinfo(String tid)throws Exception
	{
		Connection con=DatabaseConn.getConnection();
		//若教师已申报课题，则不允许删除该教师信息
		PreparedStatement pstmt=null;
		PreparedStatement pstmt1=null;
		ResultSet rst=null;
		String vsql="";
		try{
			vsql="select * from tb_subject where tutorid=? or othertid=?";
			pstmt=con.prepareStatement(vsql);
			pstmt.setString(1, tid);
			pstmt.setString(2, tid);
			rst=pstmt.executeQuery();
			if(rst.next())throw new Exception("系统中存在教师“"+tid+"”的课题记录，不允许删除！");
		}catch(Exception e){
			throw e;
		}finally{
			DatabaseConn.close(null, pstmt, rst);
		}
		//删除教师信息和用户信息
		con.setAutoCommit(false);
		try{
			vsql="delete from tb_teacher where tid=?";
			String vsql1="delete from tb_user where userid=?";
			pstmt=con.prepareStatement(vsql);
			pstmt.setString(1, tid);
			pstmt1=con.prepareStatement(vsql1);
			pstmt1.setString(1, tid);
			pstmt.executeUpdate();
			pstmt1.executeUpdate();
			
			con.commit();
		}catch(Exception e){
			con.rollback();
			throw e;
		}finally{
			con.setAutoCommit(true);
			DatabaseConn.close(null, pstmt1, null);
			DatabaseConn.close(con, pstmt, null);
		}
	}
	//得到带课题方向的教师信息
	public List<TeacherBean> getAllinfoWithSubdir(String tdept,String tname)throws Exception
	{
		Connection con=DatabaseConn.getConnection();
		List<TeacherBean> ret=new ArrayList<TeacherBean>();
		SpecialityBpo specbpo=new SpecialityBpo();
		SyscodeBpo syscodebpo=new SyscodeBpo();
		PreparedStatement pstmt=null;
		ResultSet rst=null;
		try{
			if(tname==null) tname="";
			String vsql="select * from tb_teacher where ifnull(tdept,'') like ? and tname like ? order by tdept,tid";
			pstmt=con.prepareStatement(vsql);
			pstmt.setString(1, tdept+"%");
			pstmt.setString(2, tname+"%");
			rst=pstmt.executeQuery();
			while(rst.next())
			{
				TeacherBean temp=new TeacherBean();
				String tid=rst.getString("tid");
				temp.setTid(tid);
				temp.setTname(rst.getString("tname"));
				String specid=rst.getString("specid");
				temp.setSpecbean(specbpo.getByspecid(specid));
				String tdept0=rst.getString("tdept");
				temp.setTdept(tdept0);
				//从code表中取名称jxdw,zhch,xw
				if(tdept0!=null&&!tdept0.equals("")){
					temp.setTdeptname(syscodebpo.getcode("jxdw",tdept0).getCodecontent());
				}
				String tpost0=rst.getString("tpost");
				temp.setTpost(tpost0);
				if(tpost0!=null&&!tpost0.equals("")){
					temp.setTpostname(syscodebpo.getcode("zhch",tpost0).getCodecontent());
				}
				temp.setSubdirections(this.getSubdirBytid(tid));
				ret.add(temp);
			}
	     }catch(Exception e){
	    	 throw e;
	     }finally{
				DatabaseConn.close(con, pstmt, rst);
		 }
		return ret;
	}
	/**得到每个教师的课题方向*/
	public ArrayList<SyscodeBean> getSubdirBytid(String tid)throws Exception
	{
		Connection con=DatabaseConn.getConnection();
		ArrayList<SyscodeBean> subdirs=new ArrayList<SyscodeBean>();
		SyscodeBpo syscodebpo=new SyscodeBpo();
		PreparedStatement pstmt=null;
		ResultSet rst=null;
		try{
			String vsql="select subdircodevalue from tb_teasubdirection where tid=?";
			pstmt=con.prepareStatement(vsql);
			pstmt.setString(1, tid);
			rst=pstmt.executeQuery();
			while(rst.next())
			{
				String codevaluetemp=rst.getString("subdircodevalue");
				SyscodeBean syscode=syscodebpo.getcode("ktfx",codevaluetemp);
				subdirs.add(syscode);
			}
		}catch(Exception e){
	    	 throw e;
	     }finally{
				DatabaseConn.close(con, pstmt, rst);
		 }
		return subdirs;
	}
	/**设置教师的课题方向，先删除后增加（一个事务）*/
	public void edtSubDirByTid(String tid, List<String> subdircodes) throws Exception{
		Connection con=DatabaseConn.getConnection();
		PreparedStatement pstmt=null;
		try{
			con.setAutoCommit(false);
			String vsql="delete from tb_teasubdirection where tid=?";
			pstmt = con.prepareStatement(vsql);
			pstmt.setString(1, tid);
			pstmt.addBatch();
			for(Iterator<String> it=subdircodes.iterator(); it.hasNext();){
				String subdircodetemp=it.next();
				String vsqltemp="insert into tb_teasubdirection values(?,?)";
				pstmt.setString(1, tid);
				pstmt.setString(2, subdircodetemp);
				pstmt.addBatch(vsqltemp);
			}
			pstmt.executeBatch();
			con.commit();
		}catch (Exception e) {
			con.rollback();
			Exception ex = new Exception("设置教师课题方向失败：TeacherBpo.edtSubDirByTid-"
					+ e.getMessage());
			throw ex;
		}finally{
			con.setAutoCommit(true);
			DatabaseConn.close(con, pstmt, null);
		}
	}
	/**为评审教师分配课题（题目审核时用）*/
	public String assignSubToTeaForReview() throws Exception{
		//检查毕业设计是否已经开始，若已开始则返回
    	SysarguBpo sysargubpo=new SysarguBpo();
    	if(sysargubpo.ifStartGraduate()){
    		throw new Exception("毕业设计已经开始，不允许再审核课题!");
    	}
    	/////
		String assignresult="";
		ArrayList<SubReviewBean> subreviews=new ArrayList<SubReviewBean>();
		HashMap<String,String> teanumforreview=new HashMap<String,String>();//参与盲审的教师数量
		Connection con=DatabaseConn.getConnection();
		PreparedStatement pstmt=null;
		ResultSet rst=null;
		try{
			for(int i=1;i<=3;i++){
				//统计每个教师申报课题数目（符合课题方向i），记录每个老师盲审题目
				ArrayList<TeacherSubNumBean> teasubnums=new ArrayList<TeacherSubNumBean>();
				//分配时记录哪些老师的题目已经被分配掉
				List<TeacherSubNumBean> teasubnumarrtemp=new ArrayList<TeacherSubNumBean>();
				String vsql="select tutorid,subid from tb_subject  where subdirection=? and submitflag='1' order by tutorid desc";
				pstmt=con.prepareStatement(vsql);
				pstmt.setString(1, String.valueOf(i));
				rst=pstmt.executeQuery();
				
				String lasttutorid =null;
				//生成老师的
				TeacherSubNumBean teacher=null;	
				//生成中间结果的，需要重新new,防止一个对象引用
				TeacherSubNumBean temp=null;
				while(rst.next()){
					String tutorid=rst.getString("tutorid");
					String subid = rst.getString("subid");
					
					if (tutorid ==null||subid==null){
						throw new Exception("教师编号或者课程编号为空！！！");//抛异常
					}
					if(tutorid.equals(lasttutorid)){
						teacher.setSubsum(teacher.getSubsum()+1);
						temp.setSubsum(temp.getSubsum()+1);
						temp.getSubjects().add(subid);
					}else{
						lasttutorid =tutorid;
						teacher=new TeacherSubNumBean();
						teacher.setTid(tutorid);
						temp=new TeacherSubNumBean();
						temp.setTid(tutorid);
						teasubnums.add(teacher);
						teasubnumarrtemp.add(temp);
						
						teacher.setSubsum(1);
						temp.setSubsum(1);
						temp.getSubjects().add(subid);
					}
				}
				//开始分配课题
				int teasum=teasubnums.size();
				if (teasum==1){throw new Exception("只有一个教师申请了课题方向代码为"+String.valueOf(i)+"的课题，无法进行分配！");} //一个人不用分配;
				TeaSubComparator comparator=new TeaSubComparator();
		        Collections.sort(teasubnums, comparator);  //先分审题目多的老师
		        
				for(int j=0;j<teasum;j++){
					//对每个老师进行循环分配
					TeacherSubNumBean teasub=teasubnums.get(j);
					String tid=teasub.getTid();//盲审教师
					int subsum = teasub.getSubsum();
					
					//将temp过程中按仍尚未分配的课题数从小到大排序
			        Collections.sort(teasubnumarrtemp, comparator);
					
			        boolean canadd = true;
			        while(canadd&&subsum>0){
			        	canadd = false;
			        	for (int k = teasubnumarrtemp.size()-1; k >=0 ; k--) {
			        		temp = teasubnumarrtemp.get(k);
			        		if(temp.getTid().equals(tid)){//同一个人
			        			continue;
			        		}
			        		String subid = temp.getSubjects().get(0); //肯定应该有的
			        		temp.getSubjects().remove(0);
			        		temp.setSubsum(temp.getSubsum()-1);
			        		SubReviewBean subrevtemp=new SubReviewBean();
							subrevtemp.setTid(tid);
							subrevtemp.setSubid(subid);
							subreviews.add(subrevtemp);
			        		if(temp.getSubsum()==0){
			        			teasubnumarrtemp.remove(k);
			        		}
			        		canadd=true;
			        		subsum--;
			        		if(subsum <=0) break;
						}
			        }
			        
				}
				//若还有剩余课题，则该剩余课题一定只属于一个教师，其余老师平均分配
				if (teasubnumarrtemp.size()>0){
					temp = teasubnumarrtemp.get(0);
					int left = temp.getSubsum();
					int teachercursor=-1;
					while (left>0){
						if(teachercursor>=teasubnums.size()-1){
							teachercursor =-1;
						}
						teachercursor++;
						if(teasubnums.get(teachercursor).getTid().equals(temp.getTid())){
							continue;//一个人
						}
						
						String sid = temp.getSubjects().get(0);
						temp.getSubjects().remove(0);
						temp.setSubsum(temp.getSubsum()-1);
						SubReviewBean subrevtemp=new SubReviewBean();
						subrevtemp.setTid(teasubnums.get(teachercursor).getTid());
						subrevtemp.setSubid(sid);
						subreviews.add(subrevtemp);
						left --;
					}
				}
			}
			//将分配情况写入数据库
			PreparedStatement pstmt1=null,pstmt2=null;
			try{
				con.setAutoCommit(false);
				String vsql="delete from tb_reviewsubject";//清空盲审表
				pstmt1=con.prepareStatement(vsql);
				pstmt1.executeUpdate();
				
				String vsqltemp="insert into tb_reviewsubject(tid,subid) values(?,?)";
				pstmt2=con.prepareStatement(vsqltemp);
				
				int revsum=subreviews.size();
				for(int i1=0;i1<revsum;i1++){
					SubReviewBean subrevtemp=subreviews.get(i1);
					String tid=subrevtemp.getTid();
					teanumforreview.put(tid, tid);
					String subid=subrevtemp.getSubid();
					
					pstmt2.setString(1, tid);
					pstmt2.setString(2, subid);
					pstmt2.addBatch();
				}
				pstmt2.executeBatch();
				con.commit();
				pstmt2.clearBatch();
			}catch(Exception e){
				con.rollback();
		        throw e;
		    }finally{
		    	con.setAutoCommit(true);
		    	DatabaseConn.close(null, pstmt1, null);
		    	DatabaseConn.close(null, pstmt2, null);
			}
		}catch(Exception e){
	        throw e;
	    }finally{
			DatabaseConn.close(con, pstmt, rst);
		}
		assignresult="已分配的课题总数为 "+String.valueOf(subreviews.size()+" ,参与盲审的教师数为 "+teanumforreview.size());
		return assignresult;
	}
	//查询教师课题数目
	public List<SubNumByTeaBean> getAllsubnum() throws Exception{
		//检查毕业设计是否已经开始，若已开始则返回
    	SysarguBpo sysargubpo=new SysarguBpo();
    	if(sysargubpo.ifStartGraduate()){
    		throw new Exception("毕业设计已经开始，不允许再审核课题!");
    	}
    	/////
		List<SubNumByTeaBean> subnumarr=new ArrayList<SubNumByTeaBean>();
		Connection con=null;
		String vsql=null;
		PreparedStatement pstmt=null;
		ResultSet rst=null;
		try {
			con=DatabaseConn.getConnection();
			vsql="select tid,tname,submitsubnum,reviewsubnum,unrevnum  from v_subnumbytea where submitsubnum!=0 order by tid";
			pstmt=con.prepareStatement(vsql);
			rst=pstmt.executeQuery();
			while(rst.next())
			{
				SubNumByTeaBean temp=new SubNumByTeaBean();
				temp.setTid(rst.getString("tid"));
				temp.setTname(rst.getString("tname"));
				temp.setSubmitsubnum(rst.getInt("submitsubnum"));
				temp.setReviewsubnum(rst.getInt("reviewsubnum"));
				temp.setUnrevnum(rst.getInt("unrevnum"));
				subnumarr.add(temp);
			}
		} catch (Exception e) {
			throw e;
		}finally{
			DatabaseConn.close(con, pstmt, rst);
		}
		return subnumarr;
	}
}
