package bpo;

import bean.SubSubmitBean;
import com.DatabaseConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SubSubmitBpo {
	/**�ϴ��ĵ�ʱ�޸����ݿ⣬doctype�ֱ��Ӧpaper��paperblind������,״̬��Ϊ�����ϴ���1����*/
    public void updateDoc(String stuid,String filename,String doctype) throws Exception{
    	Connection con=DatabaseConn.getConnection();
		PreparedStatement pstmt=con.prepareStatement("update tb_subsubmit set "+doctype+"=?,"+doctype+"status='1' where stuid=?");
		pstmt.setString(1, filename);
		pstmt.setString(2, stuid);
		try{
			pstmt.executeUpdate();
		}catch(Exception e){
			throw e;
		}finally{
			DatabaseConn.close(con, pstmt, null);
		}
    }
    /**��ѯ�ĵ��ϴ�״̬��·�� ����ѧ��*/
    public String getUploadstatus(String stuid,String doctype)throws Exception{
    	Connection con=DatabaseConn.getConnection();
		PreparedStatement pstmt=con.prepareStatement("select "+doctype+"status,"+doctype+" from tb_subsubmit where stuid=?");
		pstmt.setString(1, stuid);
		ResultSet result=null;
		String doctype0="";
		String doctypestatus="";
		try{
			result=pstmt.executeQuery();
			if(result.next()){
				doctype0=result.getString(doctype);
				if(doctype0==null) doctype0="";
				doctypestatus=result.getString(doctype+"status");
			}
		}catch(Exception e){
			throw e;
		}finally{
			DatabaseConn.close(con, pstmt, result);
		}
    	return doctypestatus+"|"+doctype0;
    }
    /**��ѯ�ĵ��ϴ�״̬��·�� ���ݿ����*/
    public String getUploadstatusBySubid(String subid,String doctype)throws Exception{
    	SubjectBpo subjectbpo=new SubjectBpo();
    	String stuid=subjectbpo.getStudentBysubid(subid).getStuid();
		String result=this.getUploadstatus(stuid, doctype);
		return result;
    }
    /**�ύ�鵵���ύä��  �޸��ĵ�״̬*/
    public void submitDocForTea(String stuid, String doctype,String status)throws Exception{
    	Connection con=DatabaseConn.getConnection();
		PreparedStatement pstmt=con.prepareStatement("update tb_subsubmit set "+doctype+"status=? where stuid=?");
		pstmt.setString(1, status);
		pstmt.setString(2, stuid);
		try{
			pstmt.executeUpdate();
		}catch(Exception e){
			throw e;
		}finally{
			DatabaseConn.close(con, pstmt, null);
		}
    }
    /** �õ� ѧ�� �ĵ� ״̬*/
    public List<SubSubmitBean> getStuDocsBySpec(String specid,String classname,String sname) throws Exception{
    	Connection con=DatabaseConn.getConnection();
		List<SubSubmitBean> ret = new ArrayList<SubSubmitBean>();
		if(classname==null) classname="";
		if(sname==null) sname="";
		PreparedStatement pstmt = null;
		ResultSet rst=null;
		WeekSumBpo weekbpo=new WeekSumBpo();
		try{
			if(specid==null||specid.equals("")) throw new Exception("רҵ��Ų���Ϊ��!");
			
			String vsql="select a.stuid,sname,classname,paperblindstatus,paperstatus,translationstatus,sourcecodestatus "+
			  "from tb_student a, tb_subsubmit b where a.stuid=b.stuid and classname like ? and sname like ?"+
					"and classname in(select classname from tb_class where specid=?)";
			pstmt=con.prepareStatement(vsql);
			pstmt.setString(1, classname+"%");
			pstmt.setString(2, sname+"%");
			pstmt.setString(3, specid);
			rst=pstmt.executeQuery();
			while(rst.next()){
				SubSubmitBean temp=new SubSubmitBean();
				String stuid=rst.getString("stuid");
				temp.setStuid(stuid);
				temp.setSname(rst.getString("sname"));
				temp.setClassname(rst.getString("classname"));
				temp.setPaperblindstatus(this.getDocStatusText("paperblind", rst.getString("paperblindstatus")));
				temp.setPaperstatus(this.getDocStatusText("paper", rst.getString("paperstatus")));
				temp.setTranslationstatus(this.getDocStatusText("translation", rst.getString("translationstatus")));
				temp.setSourcecodestatus(this.getDocStatusText("sourcecode", rst.getString("sourcecodestatus")));
				
				temp.setValidweekupnum(String.valueOf(weekbpo.getWeekupNum(stuid)));
				
				ret.add(temp);
			}
		}catch(Exception e){
			throw e;
		}finally{
			DatabaseConn.close(con, pstmt, rst);
		}
		return ret;
    }
    public String getDocStatusText(String doctype, String statusvalue)throws Exception{
    	String statustext="";
    	
    	if(doctype.equals("paperblind")){
    	//0δ�ϴ�1���ϴ�2�ȴ�ä��3��ä��	
    		if(statusvalue.equals("0")){
    			statustext="δ�ϴ�";
    		}else if(statusvalue.equals("1")){
    			statustext="���ϴ�";
    		}else if(statusvalue.equals("2")){
    			statustext="�ȴ�ä��";
    		}else if(statusvalue.equals("3")){
    			statustext="��ä��";
    		}
    	}else{
    	 //0δ�ϴ�1���ϴ�2�ѹ鵵
    		if(statusvalue.equals("0")){
    			statustext="δ�ϴ�";
    		}else if(statusvalue.equals("1")){
    			statustext="���ϴ�";
    		}else if(statusvalue.equals("2")){
    			statustext="�ѹ鵵";
    		}
    	}
    	
    	return statustext;
    }
}
