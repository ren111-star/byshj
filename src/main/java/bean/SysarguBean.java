package bean;

public class SysarguBean {
	private int arguid;
	private String arguname;
	private String argutype;
	private String arguvalue;
	private String remark;
	public int getArguid() {
		return arguid;
	}
	public void setArguid(int arguid) {
		this.arguid = arguid;
	}
	public String getArguname() {
		if(arguname==null)arguname="";
		return arguname;
	}
	public void setArguname(String arguname) {
		this.arguname = arguname;
	}
	public String getArgutype() {
		if(argutype==null)argutype="";
		return argutype;
	}
	public void setArgutype(String argutype) {
		this.argutype = argutype;
	}
	public String getArguvalue() {
		if(arguvalue==null)arguvalue="";
		return arguvalue;
	}
	public void setArguvalue(String arguvalue) {
		this.arguvalue = arguvalue;
	}
	public String getRemark() {
		if(remark==null)remark="";
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
