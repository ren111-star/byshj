package bean;

import lombok.Data;

@Data
public class SysarguBean {
	private int arguid;
	private String arguname;
	private String argutype;
	private String arguvalue;
	private String remark;
	public String getArguname() {
		if(arguname==null)arguname="";
		return arguname;
	}
	public String getArgutype() {
		if(argutype==null)argutype="";
		return argutype;
	}
	public String getArguvalue() {
		if(arguvalue==null)arguvalue="";
		return arguvalue;
	}
	public String getRemark() {
		if(remark==null)remark="";
		return remark;
	}
	
}
