package bean;

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
	public void setAuditflag(String auditflag) {
		this.auditflag = auditflag;
	}
	public String getAuditoption() {
		if(auditoption==null) auditoption="";
		return auditoption;
	}
	public void setAuditoption(String auditoption) {
		this.auditoption = auditoption;
	}
	public String getReleaseflag() {
		if(releaseflag==null) releaseflag="";
		return releaseflag;
	}
	public void setReleaseflag(String releaseflag) {
		this.releaseflag = releaseflag;
	}
	public String getRemark() {
		if(remark==null) remark="";
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSpecid() {
		if(specid==null) specid="";
		return specid;
	}
	public void setSpecid(String specid) {
		this.specid = specid;
	}
	public String getSubid() {
		if(subid==null) subid="";
		return subid;
	}
	public void setSubid(String subid) {
		this.subid = subid;
	}
	
}
