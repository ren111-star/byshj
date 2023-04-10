package bean;

import lombok.Data;

@Data
public class SubspecBean {

	private String subid;
	private String specid;
	private String auditflag;
	private String auditoption;
	private String releaseflag;
	private String remark;
	
	public String getAuditflag() {
		if(auditflag==null) auditflag="";
		return auditflag;
	}
	public String getAuditoption() {
		if(auditoption==null) auditoption="";
		return auditoption;
	}
	public String getReleaseflag() {
		if(releaseflag==null) releaseflag="";
		return releaseflag;
	}
	public String getRemark() {
		if(remark==null) remark="";
		return remark;
	}
	public String getSpecid() {
		if(specid==null) specid="";
		return specid;
	}
	public String getSubid() {
		if(subid==null) subid="";
		return subid;
	}
	
}
