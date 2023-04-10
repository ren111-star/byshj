package bean;

import lombok.Data;

@Data
public class ReviewPaperBean {
	String stuid;
	String subid;
	String subname;//课题名称
	float significance;//选题意义
	float designcontent;//设计内容
	float composeability;//撰写能力
	float translationlevel;//查阅文献及翻译水平
	float innovative;//创新性
	String reviewopinion;//评阅意见
	String reviewtime;
	String reviewerid;//评阅人id
	String submitflag;//提交标志
	float sumgrade;//总成绩
	String docstatus;//对应的盲审论文路径 (上传状态|上传路径)
	public String getReviewopinion() {
		if(reviewopinion==null)reviewopinion="";
		return reviewopinion;
	}
	public String getReviewtime() {
		if(reviewtime==null)reviewtime="";
		return reviewtime;
	}
	
}
