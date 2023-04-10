package bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubjectBean {
	
	private String subid;//�����ţ��Զ�����
	private String subname;//������
	private String usedyear;//ʹ�����
	private String oldargu;//ԭʼ����
	private String content;//��������
	private String requirement;//����Ҫ��
	private String refpapers;//�ο�����
	private String subtype;//��������
	private String subtypename;//�������ʹ�������
	private String subdirection;//���ⷽ�����
	private String subdirectionname;//���ⷽ���������
	private String subsort;//�������
	private String subsortname;//��������������
	private String subkind;//��������
	private String subkindname;//�������ʴ�������
	private String subsource;//������Դ
	private String subsourcename;//������Դ��������
	private int isoutschool;//�Ƿ�У��
	private TeacherBean tutor;//ָ����ʦ
	private TeacherBean othertutor;//����ָ����ʦ
	private String submitflag;//�ύ��־
	private String subprog;//��������
	private List<String> listspec;//�����ʺ�רҵ�б�
	private String status;//���������������У��������δ�ύ������С����ͨ����ѡ���С�nѧ����ѡ����ѡ��6��״̬
	private List<String> operations;//�ڲ�ͬ��״̬�¿ɽ��еĲ�������
	private String stuid;//ѧ��ѧ��
	private List<SubspecBean> listsubspec;//רҵѡ�����
	private String specname;
	private String reviewopinion;//������������ä��
	private List<ProgressBean> progress;//���̱�
	private List<SubSimBean> simSubsInHis;//��ʷ�����ƵĿ���
	private String reviewerForSub;//��������ˣ�������

	public String getSubprog() {
		if(subprog==null)subprog="";
		return subprog;
	}
	public SubjectBean(){
		listspec= new ArrayList<String>();
		operations=new ArrayList<String>();
		tutor=new TeacherBean();
		othertutor=new TeacherBean();
		listsubspec=new ArrayList<SubspecBean>();
		simSubsInHis=new ArrayList<SubSimBean>();
	}

	public String getStatus() {
		if(status==null) status="";
		return status;
	}
	public String getStuid() {
		if(stuid==null) stuid="";
		return stuid;
	}
	public String getContent() {
		if(content==null) content="";
		return content;
	}
	public String getOldargu() {
		if(oldargu==null) oldargu="";
		return oldargu;
	}
	public String getRefpapers() {
		if(refpapers==null) refpapers="";
		return refpapers;
	}
	public String getRequirement() {
		if(requirement==null) requirement="";
		return requirement;
	}
	public String getSubkind() {
		if(subkind==null) subkind="";
		return subkind;
	}
	public String getSubmitflag() {
		if(submitflag==null) submitflag="";
		return submitflag;
	}
	public String getSubname() {
		if(subname==null) subname="";
		return subname;
	}
	public String getSubsource() {
		if(subsource==null) subsource="";
		return subsource;
	}
	public String getSubtype() {
		if(subtype==null) subtype="";
		return subtype;
	}
	public String getUsedyear() {
		if(usedyear==null) usedyear="";
		return usedyear;
	}
	public String getSubkindname() {
		if(subkindname==null)subkindname="";
		return subkindname;
	}
	public String getSubsourcename() {
		if(subsourcename==null)subsourcename="";
		return subsourcename;
	}
	public String getSubtypename() {
		if(subtypename==null)subtypename="";
		return subtypename;
	}
	public String getSpecname() {
		if(specname==null) specname="";
		return specname;
	}
}
