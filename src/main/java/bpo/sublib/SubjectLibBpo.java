package bpo.sublib;

import bean.ProgressBean;
import bean.SubjectBean;
import bean.sublib.SubjectHisBean;
import bean.sublib.SubjectTempBean;
import bpo.SubjectBpo;
import bpo.SysarguBpo;
import bpo.TeacherBpo;
import com.DatabaseConn;
import com.Date_String;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**������������ 2015.11.24 wxh*/
public class SubjectLibBpo {
	/**��ѯ������ʷ�б�*/
	public List<SubjectHisBean> getSubjectHis(String tutorid, String usedyear, String subname)throws Exception
	{
		Connection con=DatabaseConn.getConnection();
		String vsql="select * from tb_subjecthis where tutorid like ? and usedyear like ? "
				+ "and subname like ? order by usedyear, subname";
		PreparedStatement pstmt=con.prepareStatement(vsql);
		pstmt.setString(1, "%"+tutorid+"%");
		pstmt.setString(2,"%"+usedyear+"%");
		pstmt.setString(3,"%"+subname+"%");
		ResultSet rst=pstmt.executeQuery();
		List<SubjectHisBean> ret=new ArrayList<SubjectHisBean>();
		
		while(rst.next())
		{
			SubjectHisBean temp=new SubjectHisBean();
			temp.setSubid(String.valueOf(rst.getInt("subid")));
			temp.setSubname(rst.getString("subname"));
			temp.setUsedyear(rst.getString("usedyear"));
			temp.setStuid(rst.getString("stuid"));
			temp.setSname(rst.getString("sname"));
			ret.add(temp);
		}
		DatabaseConn.close(con, pstmt, rst);
		return ret;
	}
	/**���ݽ�κͿ����ţ�����ʷ�л�ÿ��������Ϣ*/
	public SubjectBean getSubjectInfoFromHis(String usedyear, String subid)throws Exception{
		SubjectBean subject=new SubjectBean();
		Connection con=DatabaseConn.getConnection();
		String vsql="";
		PreparedStatement pstmt=null;
		ResultSet rst=null;
		TeacherBpo teabpo=new TeacherBpo();
		try{
			vsql="select * from tb_subjecthis where usedyear=? and subid=?";
			pstmt = con.prepareStatement(vsql);
			pstmt.setString(1, usedyear);
			pstmt.setString(2, subid);
			rst=pstmt.executeQuery();
			if(rst.next()){
				subject.setSubname(rst.getString("subname"));
				subject.setSubprog(rst.getString("subprog"));
				subject.setOldargu(rst.getString("oldargu"));
				subject.setContent(rst.getString("content"));
				subject.setRequirement(rst.getString("requirement"));
				subject.setRefpapers(rst.getString("refpapers"));
				subject.setSubkind(rst.getString("subkind"));
				subject.setSubsort(rst.getString("subsort"));
				subject.setSubsource(rst.getString("subsource"));
				subject.setSubtype(rst.getString("subtype"));
				subject.setSubdirection(rst.getString("subdirection"));
				subject.setIsoutschool(rst.getInt("isoutschool"));
				String tid=rst.getString("tutorid");
				subject.setTutor(teabpo.getBytid(tid));
				tid=rst.getString("othertid");
				subject.setOthertutor(teabpo.getBytid(tid));
				
				//�õ����̱�
				String subprogs=rst.getString("subprogs");
				if(subprogs!=null){
					Type type = new TypeToken<List<ProgressBean>>(){}.getType();  
					Gson gson = new Gson();
					List<ProgressBean> processes=gson.fromJson(subprogs,type);
					subject.setProgress(processes);
				}else{
					subject.setProgress(new ArrayList<ProgressBean>());
				}
			}
		}catch(Exception e){
			throw new Exception("����ʷ�в�ѯ����ʧ�ܣ�"+e.getMessage());
		}finally{
			DatabaseConn.close(con, pstmt, rst);
		}
		return subject;
	}
	/**������ʷ���⵽��ǰ��*/
	public void copyFromHisToCurrent(String subid, String usedyear) throws Exception {
		if(subid==null) throw new Exception("�����Ų���Ϊ�գ�");
		
		try {
			SubjectBean subject=this.getSubjectInfoFromHis(usedyear, subid);
			SubjectBpo subjectbpo=new SubjectBpo();
			subjectbpo.addinfo(subject);
		} catch (Exception e) {
			throw new Exception("[����ʧ��]"+e.getMessage());
		}
	}
	/**��ѯ�ݴ�����б�*/
	public List<SubjectTempBean> getSubjectTemp(String tutorid)throws Exception
	{
		Connection con=DatabaseConn.getConnection();
		String vsql="";
		PreparedStatement pstmt=null;
		ResultSet rst=null;
		List<SubjectTempBean> ret=new ArrayList<SubjectTempBean>();
		try{
			vsql="select * from tb_subjecttemp where tutorid like ? order by subname";
			pstmt=con.prepareStatement(vsql);
			pstmt.setString(1, "%"+tutorid+"%");
			rst=pstmt.executeQuery();
			while(rst.next())
			{
				SubjectTempBean temp=new SubjectTempBean();
				temp.setSubid(String.valueOf(rst.getInt("subid")));
				temp.setSubname(rst.getString("subname"));
				temp.setSubprog(rst.getString("subprog"));
				temp.setOldargu(rst.getString("oldargu"));
				temp.setContent(rst.getString("content"));
				temp.setRequirement(rst.getString("requirement"));
				temp.setRefpapers(rst.getString("refpapers"));
				temp.setSubkind(rst.getString("subkind"));
				temp.setSubsort(rst.getString("subsort"));
				temp.setSubsource(rst.getString("subsource"));
				temp.setSubtype(rst.getString("subtype"));
				temp.setSubdirection(rst.getString("subdirection"));
				temp.setIsoutschool(rst.getInt("isoutschool"));
				temp.setRemark(rst.getString("remark"));
				ret.add(temp);
			}
		}catch (Exception e) {
			throw e;
		}finally{
			DatabaseConn.close(con, pstmt, rst);
		}
		
		return ret;
	}
	/**���ӿ��⵽��ʱ��*/
	public void addTempSubject(SubjectTempBean subject)throws Exception{
		Connection con=DatabaseConn.getConnection();
		String vsql="";
		PreparedStatement pstmt=null;
		ResultSet rst=null;
		try{
			vsql = "insert into tb_subjecttemp"
					+ "(subname,oldargu,content,requirement,refpapers,subkind,subsource,subtype,isoutschool,tutorid,othertid,operatedtime,subprog,subdirection,subsort,subprogs,remark) values(?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?,?,?)";
			pstmt = con.prepareStatement(vsql,PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, subject.getSubname());
			pstmt.setString(2, subject.getOldargu());
			pstmt.setString(3, subject.getContent());
			pstmt.setString(4, subject.getRequirement());
			pstmt.setString(5, subject.getRefpapers());
			pstmt.setString(6, subject.getSubkind());
			pstmt.setString(7, subject.getSubsource());
			pstmt.setString(8, subject.getSubtype());
			pstmt.setInt(9, subject.getIsoutschool());
			pstmt.setString(10, subject.getTutor().getTid());
			pstmt.setString(11, subject.getOthertutor().getTid());
			pstmt.setString(12,subject.getSubprog());
			pstmt.setString(13,subject.getSubdirection());
			pstmt.setString(14,subject.getSubsort());
			pstmt.setString(15,subject.getSubprogs());
			pstmt.setString(16,subject.getRemark());
			pstmt.execute();
		}catch(Exception e){
			throw new Exception("[�ݴ����ʧ��]"+e.getMessage());
		}finally{
			DatabaseConn.close(con, pstmt, rst);
		}
	}
	
	/**ɾ����ʱ���еĿ���*/
	public void delTempSubject(String subid)throws Exception{
		Connection con=DatabaseConn.getConnection();
		String vsql="";
		PreparedStatement pstmt=null;
		try{
			// ɾ�����������Ϣ
			vsql = "delete from tb_subjecttemp where subid=?";
			pstmt = con.prepareStatement(vsql);
			pstmt.setString(1, subid);
			pstmt.execute();
		}catch(Exception e){
			throw new Exception("[ɾ������ʧ��]"+e.getMessage());
		}finally{
			DatabaseConn.close(con, pstmt, null);
		}
	}
	/**�޸���ʱ���еĿ���*/
	public void updateTempSubject(SubjectTempBean subject)throws Exception{
		Connection con=DatabaseConn.getConnection();
		String vsql="";
		PreparedStatement pstmt=null;
		ResultSet rst=null;
		try{
			//���� ���������Ϣ
			vsql="update tb_subjecttemp set subname=?,oldargu=?,content=?,requirement=?,refpapers=?"
				+",subkind=?,subsource=?,subtype=?,isoutschool=?,tutorid=?,othertid=?,subprog=?,subdirection=?,subsort=?,subprogs=?,operatedtime=now(),"
				+ "remark=? where subid=?";
			pstmt=con.prepareStatement(vsql);
			pstmt.setString(1, subject.getSubname());
			pstmt.setString(2, subject.getOldargu());
			pstmt.setString(3, subject.getContent());
			pstmt.setString(4, subject.getRequirement());
			pstmt.setString(5, subject.getRefpapers());
			pstmt.setString(6, subject.getSubkind());
			pstmt.setString(7, subject.getSubsource());
			pstmt.setString(8, subject.getSubtype());
			pstmt.setInt(9,subject.getIsoutschool());
			pstmt.setString(10, subject.getTutor().getTid());
			pstmt.setString(11, subject.getOthertutor().getTid());
			pstmt.setString(12,subject.getSubprog());
			pstmt.setString(13,subject.getSubdirection());
			pstmt.setString(14,subject.getSubsort());
			pstmt.setString(15,subject.getSubprogs());
			pstmt.setString(16,subject.getRemark());
			pstmt.setString(17,subject.getSubid());
			pstmt.executeUpdate();
		}catch(Exception e){
			throw new Exception("[�޸�ʧ��]"+e.getMessage());
		}finally{
			DatabaseConn.close(con, pstmt, rst);
		}
	}
	/**���ݽ�κͿ����ţ����ݴ���л�ÿ��������Ϣ*/
	public SubjectBean getSubjectInfoFromTemp(String subid)throws Exception{
		SubjectBean subject=new SubjectBean();
		Connection con=DatabaseConn.getConnection();
		String vsql="";
		PreparedStatement pstmt=null;
		ResultSet rst=null;
		TeacherBpo teabpo=new TeacherBpo();
		try{
			vsql="select * from tb_subjecttemp where subid=?";
			pstmt = con.prepareStatement(vsql);
			pstmt.setString(1, subid);
			rst=pstmt.executeQuery();
			if(rst.next()){
				subject.setSubid(String.valueOf(rst.getInt("subid")));
				subject.setSubname(rst.getString("subname"));
				subject.setSubprog(rst.getString("subprog"));
				subject.setOldargu(rst.getString("oldargu"));
				subject.setContent(rst.getString("content"));
				subject.setRequirement(rst.getString("requirement"));
				subject.setRefpapers(rst.getString("refpapers"));
				subject.setSubkind(rst.getString("subkind"));
				subject.setSubsort(rst.getString("subsort"));
				subject.setSubsource(rst.getString("subsource"));
				subject.setSubtype(rst.getString("subtype"));
				subject.setSubdirection(rst.getString("subdirection"));
				subject.setIsoutschool(rst.getInt("isoutschool"));
				String tid=rst.getString("tutorid");
				subject.setTutor(teabpo.getBytid(tid));
				
				//�õ����̱�
				String subprogs=rst.getString("subprogs");
				if(subprogs!=null){
					subprogs=subprogs.replace("\n", "");
					Type type = new TypeToken<List<ProgressBean>>(){}.getType();  
					Gson gson = new Gson();
					List<ProgressBean> processes=gson.fromJson(subprogs,type);
					subject.setProgress(processes);
				}else{
					subject.setProgress(new ArrayList<ProgressBean>());
				}
			}
		}catch(Exception e){
			throw new Exception("���ݴ���в�ѯ����ʧ�ܣ�"+e.getMessage());
		}finally{
			DatabaseConn.close(con, pstmt, rst);
		}
		return subject;
	}
	/**����ʱ�⿽�����⵽��ǰ��*/
	public void copyFromTempToCurrent(String subid) throws Exception {
		if(subid==null) throw new Exception("�����Ų���Ϊ�գ�");
		
		try {
			SubjectBean subject=this.getSubjectInfoFromTemp(subid);
			SubjectBpo subjectbpo=new SubjectBpo();
			subjectbpo.addinfo(subject);
		} catch (Exception e) {
			throw new Exception("[����ʧ��]"+e.getMessage());
		}
	}
	/**����ʱ��ת�ƿ��⵽��ǰ��*/
	public void TranferFromTempToCurrent(String subid) throws Exception {
		if(subid==null) throw new Exception("�����Ų���Ϊ�գ�");
		try {
			//�ȿ���
			this.copyFromTempToCurrent(subid);
			//��ɾ��
			this.delTempSubject(subid);
		} catch (Exception e) {
			throw new Exception("[ת��ʧ��]"+e.getMessage());
		}
	}
	/**�ӵ�ǰ�⿽�����⵽��ʱ��*/
	public void CopyFromCurrentToTemp(String subid) throws Exception {
		if(subid==null) throw new Exception("�����Ų���Ϊ�գ�");
		Connection con=DatabaseConn.getConnection();
		String vsql1="";
		PreparedStatement pstmt1=null;
		SubjectBpo subbpo=new SubjectBpo();
		String subprogs=subbpo.getProgressJson(subid);//�õ����ȱ�
		try {
			con.setAutoCommit(false);
			vsql1="insert into tb_subjecttemp(subname,oldargu,content,requirement,refpapers,subkind,subsource,subtype," +
					"isoutschool,tutorid,othertid,operatedtime,subprog,subdirection,subsort,subprogs,remark) " +
					"select subname,oldargu,content,requirement,refpapers,subkind,subsource,subtype," +
					"isoutschool,tutorid,othertid,operatedtime,subprog,subdirection,subsort,'"+subprogs+"','ת��' from tb_subject where subid=?";
			pstmt1=con.prepareStatement(vsql1);
			pstmt1.setString(1, subid);
			pstmt1.execute();
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw new Exception("[����ʧ��]"+e.getMessage());
		}finally{
			con.setAutoCommit(true);
			DatabaseConn.close(con, pstmt1, null);
		}
	}
	/**�ӵ�ǰ��ת�ƿ��⵽��ʱ��*/
	public void TransferFromCurrentToTemp(String subid) throws Exception {
		SubjectBpo subbpo=new SubjectBpo();
		this.CopyFromCurrentToTemp(subid);//�ȿ���
		subbpo.deleteinfo(subid);//��ɾ��
	}
	
	/**����ά����ʷ���еĽ��̱�*/
	public void UpdateProgHisBatch(String retrieveyear)throws Exception {
		List<SubjectHisBean> subshis=this.getSubjectHis("", retrieveyear, "");
		Connection con=DatabaseConn.getConnection();
		PreparedStatement pstmt=null;
		SubjectBpo subbpo=new SubjectBpo();
		try{
			con.setAutoCommit(false);
			String vsql="update tb_subjecthis set subprogs=? where subid=? and usedyear=?";
			pstmt = con.prepareStatement(vsql);
			Iterator<SubjectHisBean> it = subshis.iterator();
			while (it.hasNext()) {
				SubjectHisBean temp=(SubjectHisBean)it.next();
				pstmt.setString(1, subbpo.getProgressJson(temp.getSubid()));
				pstmt.setInt(2, Integer.valueOf(temp.getSubid()));
				pstmt.setString(3, retrieveyear);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			con.commit();
		}catch(Exception e){
			con.rollback();
			throw e;
		}finally{
			con.setAutoCommit(true);
			DatabaseConn.close(con, pstmt, null);
		}
	}
	/**����ά���ݴ���еĽ��̱�*/
	public void UpdateProgTempBatch(String retrieveyear)throws Exception {
		List<SubjectTempBean> substmp=this.getSubjectTemp("");
		Connection con=DatabaseConn.getConnection();
		PreparedStatement pstmt=null;
		SubjectBpo subbpo=new SubjectBpo();
		try{
			con.setAutoCommit(false);
			String vsql="update tb_subjecttemp set subprogs=? where remark=?";//remark����ǰ��usedyear:subid
			pstmt = con.prepareStatement(vsql);
			Iterator<SubjectTempBean> it = substmp.iterator();
			while (it.hasNext()) {
				SubjectTempBean temp=(SubjectTempBean)it.next();
				String remark=temp.getRemark();
				String year=remark.split(":")[0];
				if(year.equals(retrieveyear)){
					String oldsubid=remark.split(":")[1];
					pstmt.setString(1, subbpo.getProgressJson(oldsubid));
					pstmt.setString(2, remark);
					pstmt.addBatch();
				}
			}
			pstmt.executeBatch();
			con.commit();
		}catch(Exception e){
			con.rollback();
			throw e;
		}finally{
			con.setAutoCommit(true);
			DatabaseConn.close(con, pstmt, null);
		}
	}
	/**�����������Ϣ��������ʷ���ݴ�⣨���������̱�*/
	public void generatedHisAndTemp(String retrieveyear) throws Exception{
		if(retrieveyear==null||retrieveyear.length()!=4) throw new Exception("��ݳ��ȱ���Ϊ4λ��");
		Connection con=DatabaseConn.getConnection();
		CallableStatement callstmt=null;
		
		try {
			con.setAutoCommit(false);
			callstmt = con.prepareCall("{call generateHisAndTemp(?)}");  
			callstmt.setString(1, retrieveyear);
			callstmt.execute();
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		}finally{
			con.setAutoCommit(true);
			DatabaseConn.close(con, callstmt, null);
		}
		  
	}
	/**��յ�ǰ��*/
	public void clearCurrentLib() throws Exception{
		Connection con=DatabaseConn.getConnection();
		CallableStatement callstmt=null;
		try {
			con.setAutoCommit(false);
			callstmt = con.prepareCall("{call clearCurrentLib()}");  
			callstmt.execute();
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		}finally{
			con.setAutoCommit(true);
			DatabaseConn.close(con, callstmt, null);
		}
	}
	/**����ϵͳ����*/
	public void setSystemParam(String savepath,String startdate) throws Exception{
		File dirFile = new File(savepath);
		if(!dirFile.isDirectory()) throw new Exception(savepath+"����һ��Ŀ¼��");
		try{
			Date_String.toTimestamp(startdate);//ʱ���ʽ���ǡ�yyyy-MM-dd���������쳣��
		}catch(Exception e){
			throw e;
		}
		Connection con=DatabaseConn.getConnection();
		CallableStatement callstmt=null;
		try {
			con.setAutoCommit(false);
			callstmt = con.prepareCall("{call setSystemParam(?,?)}");
			callstmt.setString(1, savepath);
			callstmt.setString(2, startdate);
			callstmt.execute();
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		}finally{
			con.setAutoCommit(true);
			DatabaseConn.close(con, callstmt, null);
		}
	}
	/**ϵͳ��ʼ�� retrieveyear �鵵���*/
	public List<String> sysInit(String retrieveyear) throws Exception{
		SysarguBpo argubpo=new SysarguBpo();
		String initflag=argubpo.getSysargu("initflag").getArguvalue();
		if(initflag.equals("1")) throw new Exception("ϵͳ�Ѿ���ʼ�����������ظ�������");
		if(retrieveyear.equals("")) throw new Exception("�鵵��ݲ���Ϊ�գ�");
		List<String> results=new ArrayList<String>();
		//1�����ô洢���� ����ѡ��Ŀ���ת����ʷ��δѡ���ת���ݴ�
		try {
			this.generatedHisAndTemp(retrieveyear);
			results.add("��ǰ��ѡ������ת����ʷ��δѡ������ת���ݴ��");
		} catch (Exception e) {
			results.add("����ʷ���ݴ��ת�ƿ���ʱ��ʧ�ܣ�"+e.getMessage());
		}
		
		//2�������̱�ά������ʷ����ݴ��
		try {
			this.UpdateProgHisBatch(retrieveyear);
			this.UpdateProgTempBatch(retrieveyear);
			results.add("������̱���ά������ʷ����ݴ�⣡");
		} catch (Exception e) {
			results.add("ά��������̱���ʷ����ݴ��ʱʧ�ܣ�"+e.getMessage());
		}
		
		//3�����ô洢���̣���յ�ǰ��
		try {
			this.clearCurrentLib();
			results.add("��ǰ���������գ�");
		} catch (Exception e) {
			results.add("��յ�ǰ�����ʧ�ܣ�"+e.getMessage());
		}
		
		/*//4�����ô洢���̣�����ϵͳ����
		this.setSystemParam("D://bysj/graduate/", startdate);
		results.add("ϵͳ����������ɣ�");*/
		return results;
	}
}
