package bean.sublib;

import base.SubjectBasePojo;

/**¿ÎÌâÀúÊ·bean*/
public class SubjectHisBean extends SubjectBasePojo{
	private String usedyear;
	private String stuid;
	private String sname;
	private String subprogs;
	
	public String getSubprogs() {
		if(subprogs==null) subprogs="";
		return subprogs;
	}
	public void setSubprogs(String subprogs) {
		this.subprogs = subprogs;
	}
	public String getUsedyear() {
		return usedyear;
	}
	public void setUsedyear(String usedyear) {
		this.usedyear = usedyear;
	}
	public String getStuid() {
		return stuid;
	}
	public void setStuid(String stuid) {
		this.stuid = stuid;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
}
