package bean;

public class ClassBean {
	
	private String classid;//�༶Ψһ��ʶ���Զ����
	private String classname;//�༶���ƣ�Ψһ
	private SpecialityBean speciality;//����רҵ���
	private String enrolyear;//��ѧ���
	private String gradyear;//��ҵ���
	private String specid;//רҵid
	
	public String getSpecid() {
		return specid;
	}
	public void setSpecid(String specid) {
		this.specid = specid;
	}
	public ClassBean(){
		speciality=new SpecialityBean();
	}
	public String getClassid() {
		return classid;
	}
	public void setClassid(String classid) {
		this.classid = classid;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getEnrolyear() {
		if(enrolyear==null) enrolyear="";
		return enrolyear;
	}
	public void setEnrolyear(String enrolyear) {
		this.enrolyear = enrolyear;
	}
	public String getGradyear() {
		if(gradyear==null) gradyear="";
		return gradyear;
	}
	public void setGradyear(String gradyear) {
		this.gradyear = gradyear;
	}
	public SpecialityBean getSpeciality() {
		return speciality;
	}
	public void setSpeciality(SpecialityBean speciality) {
		this.speciality = speciality;
	}
	
	

}
