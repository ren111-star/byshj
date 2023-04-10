package bean;

public class MyWeekSumBean {
	private String stuid;
	private int weekorder;
	private String thiscontent;
	private String support;
	private String nextcontent;
	private String tutorreply;
	private String tutorreview;
	
	public String getStuid() {
		return stuid;
	}
	public void setStuid(String stuid) {
		this.stuid = stuid;
	}
	public int getWeekorder() {
		return weekorder;
	}
	public void setWeekorder(int weekorder) {
		this.weekorder = weekorder;
	}
	public String getThiscontent() {
		if(thiscontent==null) thiscontent="";
		return thiscontent;
	}
	public void setThiscontent(String thiscontent) {
		this.thiscontent = thiscontent;
	}
	public String getSupport() {
		if(support==null) support="";
		return support;
	}
	public void setSupport(String support) {
		this.support = support;
	}
	public String getNextcontent() {
		if(nextcontent==null) nextcontent="";
		return nextcontent;
	}
	public void setNextcontent(String nextcontent) {
		this.nextcontent = nextcontent;
	}
	public String getTutorreply() {
		if(tutorreply==null) tutorreply="";
		return tutorreply;
	}
	public void setTutorreply(String tutorreply) {
		this.tutorreply = tutorreply;
	}
	public String getTutorreview() {
		if(tutorreview==null) tutorreview="";
		return tutorreview;
	}
	public void setTutorreview(String tutorreview) {
		this.tutorreview = tutorreview;
	}
	
}
