package bean;

public class ProgressBean {
	
	private String progid;//进度id,自动编号
	private String subid;//课题编号
	private String inorder;//内部顺序，同一课题有若干进度条目
	private String content;//工作内容
	private String startenddate;//开始结束时间
	private String checkopinion;//检查意见
	
	public String getStartenddate() {
		return startenddate;
	}
	public void setStartenddate(String startenddate) {
		this.startenddate = startenddate;
	}
	public String getContent() {
		if(content==null) content="";
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getInorder() {
		
		return inorder;
	}
	public void setInorder(String inorder) {
		this.inorder = inorder;
	}
	public String getProgid() {
		return progid;
	}
	public void setProgid(String progid) {
		this.progid = progid;
	}
	public String getSubid() {
		return subid;
	}
	public void setSubid(String subid) {
		this.subid = subid;
	}
	public String getCheckopinion() {
		if(checkopinion==null) checkopinion="";
		return checkopinion;
	}
	public void setCheckopinion(String checkopinion) {
		this.checkopinion = checkopinion;
	}
}
