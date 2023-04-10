package bean.sublib;

import base.SubjectBasePojo;

/**¿ÎÌâÀúÊ·bean*/
public class SubjectTempBean extends SubjectBasePojo{
	private String subprogs;
	private String remark;
	
	public String getSubprogs() {
		if(subprogs==null) subprogs="";
		return subprogs;
	}
	public void setSubprogs(String subprogs) {
		this.subprogs = subprogs;
	}
	public String getRemark() {
		if(remark==null) remark="";
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
