package bean;

public class ReviewPaperBean {
	String stuid;
	String subid;
	String subname;//��������
	float significance;//ѡ������
	float designcontent;//�������
	float composeability;//׫д����
	float translationlevel;//�������׼�����ˮƽ
	float innovative;//������
	String reviewopinion;//�������
	String reviewtime;
	String reviewerid;//������id
	String submitflag;//�ύ��־
	float sumgrade;//�ܳɼ�
	String docstatus;//��Ӧ��ä������·�� (�ϴ�״̬|�ϴ�·��)
	
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
