package bean;

import java.util.ArrayList;
import java.util.List;

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
	
	public List<SubSimBean> getSimSubsInHis() {
		return simSubsInHis;
	}
	public void setSimSubsInHis(List<SubSimBean> simSubsInHis) {
		this.simSubsInHis = simSubsInHis;
	}
	private String reviewerForSub;//��������ˣ�������
	
	public String getReviewerForSub() {
		return reviewerForSub;
	}
	public void setReviewerForSub(String reviewerForSub) {
		this.reviewerForSub = reviewerForSub;
	}
	public String getSubsort() {
		return subsort;
	}
	public void setSubsort(String subsort) {
		this.subsort = subsort;
	}
	public String getSubsortname() {
		return subsortname;
	}
	public void setSubsortname(String subsortname) {
		this.subsortname = subsortname;
	}
	public List<ProgressBean> getProgress() {
		return progress;
	}
	public void setProgress(List<ProgressBean> progress) {
		this.progress = progress;
	}
	public String getReviewopinion() {
		return reviewopinion;
	}
	public void setReviewopinion(String reviewopinion) {
		this.reviewopinion = reviewopinion;
	}
	public String getSubdirection() {
		return subdirection;
	}
	public void setSubdirection(String subdirection) {
		this.subdirection = subdirection;
	}
	public String getSubdirectionname() {
		return subdirectionname;
	}
	public void setSubdirectionname(String subdirectionname) {
		this.subdirectionname = subdirectionname;
	}
	public String getSubprog() {
		if(subprog==null)subprog="";
		return subprog;
	}
	public void setSubprog(String subprog) {
		this.subprog = subprog;
	}
	public SubjectBean(){
		listspec= new ArrayList<String>();
		operations=new ArrayList<String>();
		tutor=new TeacherBean();
		othertutor=new TeacherBean();
		listsubspec=new ArrayList<SubspecBean>();
		simSubsInHis=new ArrayList<SubSimBean>();
	}
	public List<SubspecBean> getListsubspec() {
		return listsubspec;
	}
	public void setListsubspec(List<SubspecBean> listsubspec) {
		this.listsubspec = listsubspec;
	}
	public TeacherBean getOthertutor() {
		return othertutor;
	}

	public void setOthertutor(TeacherBean othertutor) {
		this.othertutor = othertutor;
	}

	public TeacherBean getTutor() {
		return tutor;
	}

	public void setTutor(TeacherBean tutor) {
		this.tutor = tutor;
	}

	public List<String> getOperations() {
		return operations;
	}
	public void setOperations(List<String> operations) {
		this.operations = operations;
	}
	public String getStatus() {
		if(status==null) status="";
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStuid() {
		if(stuid==null) stuid="";
		return stuid;
	}
	public void setStuid(String stuid) {
		this.stuid = stuid;
	}
	public String getContent() {
		if(content==null) content="";
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getOldargu() {
		if(oldargu==null) oldargu="";
		return oldargu;
	}
	public void setOldargu(String oldargu) {
		this.oldargu = oldargu;
	}
	public String getRefpapers() {
		if(refpapers==null) refpapers="";
		return refpapers;
	}
	public void setRefpapers(String refpapers) {
		this.refpapers = refpapers;
	}
	public String getRequirement() {
		if(requirement==null) requirement="";
		return requirement;
	}
	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}
	public String getSubid() {
		return subid;
	}
	public void setSubid(String subid) {
		this.subid = subid;
	}
	public String getSubkind() {
		if(subkind==null) subkind="";
		return subkind;
	}
	public void setSubkind(String subkind) {
		this.subkind = subkind;
	}
	public String getSubmitflag() {
		if(submitflag==null) submitflag="";
		return submitflag;
	}
	public void setSubmitflag(String submitflag) {
		this.submitflag = submitflag;
	}
	public String getSubname() {
		if(subname==null) subname="";
		return subname;
	}
	public void setSubname(String subname) {
		this.subname = subname;
	}
	public String getSubsource() {
		if(subsource==null) subsource="";
		return subsource;
	}
	public void setSubsource(String subsource) {
		this.subsource = subsource;
	}
	public String getSubtype() {
		if(subtype==null) subtype="";
		return subtype;
	}
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
	public String getUsedyear() {
		if(usedyear==null) usedyear="";
		return usedyear;
	}
	public void setUsedyear(String usedyear) {
		this.usedyear = usedyear;
	}
	public List<String> getListspec() {
		return listspec;
	}
	public void setListspec(List<String> listspec) {
		this.listspec = listspec;
	}
	public String getSubkindname() {
		if(subkindname==null)subkindname="";
		return subkindname;
	}
	public void setSubkindname(String subkindname) {
		this.subkindname = subkindname;
	}
	public String getSubsourcename() {
		if(subsourcename==null)subsourcename="";
		return subsourcename;
	}
	public void setSubsourcename(String subsourcename) {
		this.subsourcename = subsourcename;
	}
	public String getSubtypename() {
		if(subtypename==null)subtypename="";
		return subtypename;
	}
	public void setSubtypename(String subtypename) {
		this.subtypename = subtypename;
	}
	public int getIsoutschool() {
		return isoutschool;
	}
	public void setIsoutschool(int isoutschool) {
		this.isoutschool = isoutschool;
	}
	public String getSpecname() {
		if(specname==null) specname="";
		return specname;
	}
	public void setSpecname(String specname) {
		this.specname = specname;
	}
}
