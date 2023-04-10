package bean;

public class SpecialityBean {
	
	private String specid;//专业编号，3位字符
	private String specname;//专业名称
	private String specmagtid;//专业负责人id
	private String specmagtname;//专业负责人名
	public String getSpecmagtname() {
		return specmagtname;
	}
	public void setSpecmagtname(String specmagtname) {
		this.specmagtname = specmagtname;
	}
	public String getSpecid() {
		return specid;
	}
	public void setSpecid(String specid) {
		this.specid = specid;
	}
	public String getSpecmagtid() {
		if(specmagtid==null) specmagtid="";
		return specmagtid;
	}
	public void setSpecmagtid(String specmagtid) {
		this.specmagtid = specmagtid;
	}
	public String getSpecname() {
		if(specname==null) specname="";
		return specname;
	}
	public void setSpecname(String specname) {
		this.specname = specname;
	}
	
	

}
