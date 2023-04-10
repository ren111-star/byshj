package bean;

public class ClassBean {
	
	private String classid;//班级唯一标识，自动编号
	private String classname;//班级名称，唯一
	private SpecialityBean speciality;//所属专业编号
	private String enrolyear;//入学年份
	private String gradyear;//毕业年份
	private String specid;//专业id
	
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
