package bean;

public class StudentBean {
	
	private String stuid;//ѧ�ţ�10λ�̶�����
	private String sname;//����
	private String ssex;//�Ա�
	private String classname;//�༶��
	private ClassBean classbean;//�༶bean
	private String email;
	private String telphone;//�绰
	private String remark;//��ע
	private String status;//ѧ��״̬
	private SubjectBean subject;//ѧ��ѡ��Ŀ����б�
	
	public StudentBean(){
		subject=new SubjectBean();
		classbean=new ClassBean();
	}
	public String getStatus() {
		if(status==null) status="";
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmail() {
		if(email==null) email="";
		return email;
	}
	public void setEmail(String email) {
		if(email==null) email="";
		this.email = email;
	}
	public String getRemark() {
		if(remark==null) remark="";
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSname() {
		if(sname==null) sname="";
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getSsex() {
		if(ssex==null) ssex="";
		return ssex;
	}
	public void setSsex(String ssex) {
		this.ssex = ssex;
	}
	public String getStuid() {
		return stuid;
	}
	public void setStuid(String stuid) {
		this.stuid = stuid;
	}
	public String getTelphone() {
		if(telphone==null) telphone="";
		return telphone;
	}
	public void setTelphone(String telphone) {
		if(telphone==null) telphone="";
		this.telphone = telphone;
	}
	public SubjectBean getSubject() {
		return subject;
	}
	public void setSubject(SubjectBean subject) {
		this.subject = subject;
	}
	public ClassBean getClassbean() {
		return classbean;
	}
	public void setClassbean(ClassBean classbean) {
		this.classbean = classbean;
	}
	public String getClassname() {
		if(classname==null)classname="";
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}

}
