package bean;

import lombok.Data;

@Data
public class MyWeekSumBean {
	private String stuid;
	private int weekorder;
	private String thiscontent;
	private String support;
	private String nextcontent;
	private String tutorreply;
	private String tutorreview;

	public String getThiscontent() {
		if(thiscontent==null) thiscontent="";
		return thiscontent;
	}
	public String getSupport() {
		if(support==null) support="";
		return support;
	}
	public String getNextcontent() {
		if(nextcontent==null) nextcontent="";
		return nextcontent;
	}
	public String getTutorreply() {
		if(tutorreply==null) tutorreply="";
		return tutorreply;
	}
	public String getTutorreview() {
		if(tutorreview==null) tutorreview="";
		return tutorreview;
	}
	
}
