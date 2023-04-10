package bean;

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
	
	public String getStuid() {
		return stuid;
	}
	public void setStuid(String stuid) {
		this.stuid = stuid;
	}
	public String getSubid() {
		return subid;
	}
	public void setSubid(String subid) {
		this.subid = subid;
	}
	public float getSignificance() {
		return significance;
	}
	public void setSignificance(float significance) {
		this.significance = significance;
	}
	public float getDesigncontent() {
		return designcontent;
	}
	public void setDesigncontent(float designcontent) {
		this.designcontent = designcontent;
	}
	public float getComposeability() {
		return composeability;
	}
	public void setComposeability(float composeability) {
		this.composeability = composeability;
	}
	public float getTranslationlevel() {
		return translationlevel;
	}
	public void setTranslationlevel(float translationlevel) {
		this.translationlevel = translationlevel;
	}
	public float getInnovative() {
		return innovative;
	}
	public void setInnovative(float innovative) {
		this.innovative = innovative;
	}
	public String getReviewopinion() {
		if(reviewopinion==null)reviewopinion="";
		return reviewopinion;
	}
	public void setReviewopinion(String reviewopinion) {
		this.reviewopinion = reviewopinion;
	}
	public String getReviewtime() {
		if(reviewtime==null)reviewtime="";
		return reviewtime;
	}
	public void setReviewtime(String reviewtime) {
		this.reviewtime = reviewtime;
	}
	public String getReviewerid() {
		return reviewerid;
	}
	public void setReviewerid(String reviewerid) {
		this.reviewerid = reviewerid;
	}
	public String getSubname() {
		return subname;
	}
	public void setSubname(String subname) {
		this.subname = subname;
	}
	public String getSubmitflag() {
		return submitflag;
	}
	public void setSubmitflag(String submitflag) {
		this.submitflag = submitflag;
	}
	public float getSumgrade() {
		return sumgrade;
	}
	public void setSumgrade(float sumgrade) {
		this.sumgrade = sumgrade;
	}
	public String getDocstatus() {
		return docstatus;
	}
	public void setDocstatus(String docstatus) {
		this.docstatus = docstatus;
	}
	
}
