package bean;

import lombok.Data;

@Data
public class StusubBean {
	private String stuid;
	private String subid;
	private String sname;
	private String subname;
	private SubjectBean subject;
	private String pickorder;
	private String pickflag;

	public String getPickflag() {
		if(pickflag==null) pickflag="";
		return pickflag;
	}
	public String getPickorder() {
		if(pickorder==null) pickorder="";
		return pickorder;
	}
	public String getStuid() {
		if(stuid==null) stuid="";
		return stuid;
	}
	public String getSubid() {
		if(subid==null) subid="";
		return subid;
	}
	
}
