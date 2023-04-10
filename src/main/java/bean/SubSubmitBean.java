package bean;

public class SubSubmitBean {
	String stuid;//学号
	String sname;
	String classname;
	String paperblindstatus;//状态文本信息，下同
	String paperstatus;
	String translationstatus;
	String sourcecodestatus;
	String validweekupnum;//已完成的周总结数
	
	public String getValidweekupnum() {
		return validweekupnum;
	}
	public void setValidweekupnum(String validweekupnum) {
		this.validweekupnum = validweekupnum;
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
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public String getPaperblindstatus() {
		return paperblindstatus;
	}
	public void setPaperblindstatus(String paperblindstatus) {
		this.paperblindstatus = paperblindstatus;
	}
	public String getPaperstatus() {
		return paperstatus;
	}
	public void setPaperstatus(String paperstatus) {
		this.paperstatus = paperstatus;
	}
	public String getTranslationstatus() {
		return translationstatus;
	}
	public void setTranslationstatus(String translationstatus) {
		this.translationstatus = translationstatus;
	}
	public String getSourcecodestatus() {
		return sourcecodestatus;
	}
	public void setSourcecodestatus(String sourcecodestatus) {
		this.sourcecodestatus = sourcecodestatus;
	}
}
