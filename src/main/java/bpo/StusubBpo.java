package bpo;

import bean.StudentBean;
import bean.StusubBean;
import com.DatabaseConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StusubBpo {
	public StusubBpo() throws Exception {
	}
	//�ύѧ������ѡ����
	public void submitPickResultFirst(String stuid,List<StusubBean> pickedsubs)throws Exception {
		Connection con=DatabaseConn.getConnection();
		if(pickedsubs==null||pickedsubs.size()==0)return;
		PreparedStatement pstmt=null;
		ResultSet rst=null;
		try{
			String vsql0="select * from tb_stusub where stuid=?";
			try{
				pstmt = con.prepareStatement(vsql0);
				pstmt.setString(1, stuid);
				rst=pstmt.executeQuery();
				if(rst.next())throw new Exception("ѧ��"+stuid+"��ѡ���Ѵ��ڣ��������ٴ�ѡ��");
			}catch(Exception e){
				throw e;
			}finally{
				DatabaseConn.close(null, pstmt, rst);
			}
			StudentBpo studentbpo=new StudentBpo();
			StudentBean student=studentbpo.getBystuid(stuid);
			String email=student.getEmail();
			String telphone=student.getTelphone();
			if(email.equals("")||telphone.equals("")){
				throw new Exception("����ʧ�ܣ��㻹û������������ֻ��ţ�Ϊ����ָ����ʦ������ϵ�����ڡ�������Ϣ��ҳ���벢������١��ύѡ��־Ը����");
			}
			con.setAutoCommit(false);
			String vsql="insert into tb_stusub(stuid,subid,pickorder,pickflag) values(?,?,?,?)";
			pstmt = con.prepareStatement(vsql);
			Iterator it = pickedsubs.iterator();
			while (it.hasNext()) {
				StusubBean temp=(StusubBean)it.next();
				pstmt.setString(1,stuid);
				String subid=temp.getSubid();
				if(subid.equals("")){
					pstmt.setNull(2, Types.BIGINT);
				}else{
					pstmt.setInt(2, Integer.valueOf(subid));
				}
				String pickorder=temp.getPickorder();
				if(pickorder.equals("")){
					pstmt.setNull(3, Types.SMALLINT);
				}else{
					pstmt.setInt(3, Integer.valueOf(pickorder));
				}
				if(this.isPicked(subid)){
					pstmt.setString(4, "0");
				}else{
					pstmt.setNull(4, Types.CHAR);
				}
				pstmt.addBatch();
			}
			try{
				pstmt.executeBatch();
				con.commit();
				pstmt.clearBatch();
			}catch(Exception e){
				con.rollback();
				throw e;
			}
		}catch(Exception e){
			throw e;
		}finally{
			con.setAutoCommit(true);
			DatabaseConn.close(con, pstmt, null);
		}
	}
	//ȷ�������Ƿ�ѡ��
	private boolean isPicked(String subid)throws Exception{
		Connection con=DatabaseConn.getConnection();
		PreparedStatement pstmt=null;
		ResultSet rst=null;
		String vsql="";
		try{
			vsql="select * from tb_stusub where subid=? and pickflag='1'";
			pstmt = con.prepareStatement(vsql);
			pstmt.setString(1, subid);
			rst=pstmt.executeQuery();
			if(rst.next()) return true;
		}catch(Exception e){
			throw e;
		}finally{
			DatabaseConn.close(con, pstmt, rst);
		}
		return false;
	}
	//���ѧ����ѡ�����б�
	public List<StusubBean> getPickedsSubsbyStu(String stuid)throws Exception {
		Connection con=DatabaseConn.getConnection();
		if(stuid==null)stuid="";
		List<StusubBean> pickedsubs=new ArrayList<StusubBean>();
		PreparedStatement pstmt=null;
		ResultSet rst=null;
		try{
			SubjectBpo subjectbpo=new SubjectBpo();
			String vsql="select * from tb_stusub where stuid=?";
			pstmt=con.prepareStatement(vsql);
			pstmt.setString(1, stuid);
			rst=pstmt.executeQuery();
			while(rst.next()){
				StusubBean temp=new StusubBean();
				temp.setStuid(rst.getString("stuid"));
				String subid=String.valueOf(rst.getInt("subid"));//��Ϊnull���򷵻�ֵΪ0
				temp.setSubid(subid);
				if(!subid.equals("0")) temp.setSubject(subjectbpo.getBysubid(subid));
				temp.setPickorder(String.valueOf(rst.getInt("pickorder")));
				temp.setPickflag(rst.getString("pickflag"));
				pickedsubs.add(temp);
			}
		}catch(Exception e){
			throw e;
		}finally{
			DatabaseConn.close(con, pstmt, rst);
		}
		return pickedsubs;
	}
	//ɾ��ѧ����ѡ����Ϣ
	public void delSubByStu(String stuid)throws Exception {
		Connection con=DatabaseConn.getConnection();
		if(stuid==null)stuid="";
		PreparedStatement pstmt=null;
		ResultSet rst=null;
		String vsql="select * from tb_stusub where pickflag='1' and stuid=?";
		try{
			pstmt = con.prepareStatement(vsql);
			pstmt.setString(1, stuid);
			rst = pstmt.executeQuery();
			if(rst.next()){
				throw new Exception("���ѡ���ѱ���ʦȷ�ϣ�����������ѡ��");
			}
		}catch(Exception e){
			throw e;
		}finally{
			DatabaseConn.close(null, pstmt, rst);
		}
		try{
			vsql="delete from tb_stusub where stuid=?";
			pstmt=con.prepareStatement(vsql);
			pstmt.setString(1, stuid);
			pstmt.executeUpdate();
		}catch(Exception e){
			throw e;
		}finally{
			DatabaseConn.close(con, pstmt, null);
		}
	}
	//����ָ��ѡ�⣬������ѡ��״̬ѧ���⣬����ѧ��״̬����Ϊ����ָ�ɡ���������רҵ������Ϊ��ָ�ɿ���
	public int setAssignSubject()throws Exception {
		int stucount=0;
		Connection con=DatabaseConn.getConnection();
		PreparedStatement pstmt=null;
		con.setAutoCommit(false);
		String vsql1="delete from tb_stusub where IFNULL(pickflag,'')<>'1'";
		String vsql2="insert into tb_stusub(stuid,subid,pickorder) "+
		             "select stuid,NULL,NULL from tb_student where stuid not in(select stuid from tb_stusub)";
		try{
			pstmt=con.prepareStatement(vsql1);
			pstmt.executeUpdate();
			pstmt=con.prepareStatement(vsql2);
			pstmt.executeUpdate();
			stucount=pstmt.getUpdateCount();
			con.commit();
		}finally{
			con.setAutoCommit(true);
			DatabaseConn.close(con, pstmt, null);
		}
		return stucount;
	}
	/**��ý�ʦָ����ѧ����Ϣ*/
	public List<StusubBean> getStusubBytid(String tid) throws Exception {
		List<StusubBean> stusubs=new ArrayList<StusubBean>();
		Connection con=DatabaseConn.getConnection();
		PreparedStatement pstmt=con.prepareStatement("select stuid,sname,subname from v_stusubinfo where tutorid=?");
		pstmt.setString(1, tid);
		ResultSet rst=null;
		try{
			rst=pstmt.executeQuery();
			while(rst.next()){
				StusubBean stusub=new StusubBean();
				stusub.setStuid(rst.getString("stuid"));
				stusub.setSname(rst.getString("sname"));
				stusub.setSubname(rst.getString("subname"));
				stusubs.add(stusub);
			}
		}catch(Exception e){
			throw e;
		}finally{
			DatabaseConn.close(con, pstmt, rst);
		}
		return stusubs;
	}
	/**��ñ�ѧ��ѡ�еĿ���*/
	public String getSubidForStu(String stuid)throws Exception{
		Connection con=DatabaseConn.getConnection();
		PreparedStatement pstmt=con.prepareStatement("select subid from tb_stusub where stuid=? and pickflag='1'");
		pstmt.setString(1, stuid);
		ResultSet rst=null;
		String subid="";
		try{
			rst=pstmt.executeQuery();
			if(rst.next()){
				subid=String.valueOf(rst.getInt("subid"));
			}
		}catch(Exception e){
			throw e;
		}finally{
			DatabaseConn.close(con, pstmt, rst);
		}
		return subid;
	}
	/**ѧ������ʦ*/
	public void changeTutorForStu(String stuid,String tutorid) throws Exception{
		if(stuid.equals("")||stuid==null||tutorid.equals("")||tutorid==null) throw new Exception("ѧ�źͽ�ʦ��ž�����Ϊ�գ�");
		
		//ȷ����ʦ��Ŵ���
		TeacherBpo teacherbpo=new TeacherBpo();
		if(teacherbpo.getBytid(tutorid).getTid()==null) throw new Exception("��ʦ���["+tutorid+"]�����ڣ�");
		//ȷ��ѧ���Ѿ�ѡ��
		String subid=this.getSubidForStu(stuid);
		if(subid.equals("")) throw new Exception("ѧ��["+stuid+"]��û��ȷ���Ŀ��⣬����޷�����ָ����ʦ��");
		
		Connection con=DatabaseConn.getConnection();
		PreparedStatement pstmt=con.prepareStatement("update tb_subject set tutorid=? where subid=?");
		pstmt.setString(1, tutorid);
		pstmt.setString(2, subid);
		try{
			pstmt.executeUpdate();
		}catch(Exception e){
			throw e;
		}finally{
			DatabaseConn.close(con, pstmt, null);
		}
	}
}
