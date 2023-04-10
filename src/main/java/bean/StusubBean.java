package bean;

public class StusubBean {
	private String stuid;
	private String subid;
	private String sname;
	private String subname;
	private SubjectBean subject;
	private String pickorder;
	private String pickflag;
	
	public StusubBean(){
		subject=new SubjectBean();
	}
	public String getPickflag() {
		if(pickflag==null) pickflag="";
		return pickflag;
	}
	public void setPickflag(String pickflag) {
		this.pickflag = pickflag;
	}
	public String getPickorder() {
		if(pickorder==null) pickorder="";
		return pickorder;
	}
	public void setPickorder(String pickorder) {
		this.pickorder = pickorder;
	}
	public String getStuid() {
		if(stuid==null) stuid="";
		return stuid;
	}
	public void setStuid(String stuid) {
		this.stuid = stuid;
	}
	public String getSubid() {
		if(subid==null) subid="";
		return subid;
	}
	public void setSubid(String subid) {
		this.subid = subid;
	}
	public SubjectBean getSubject() {
		return subject;
	}
	public void setSubject(SubjectBean subject) {
		this.subject = subject;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getSubname() {
		return subname;
	}
	public void setSubname(String subname) {
		this.subname = subname;
	}
	
}
