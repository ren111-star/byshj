package bean;

import lombok.Data;

@Data
public class ProgressBean {
	
	private String progid;//进度id,自动编号
	private String subid;//课题编号
	private String inorder;//内部顺序，同一课题有若干进度条目
	private String content;//工作内容
	private String startenddate;//开始结束时间
	private String checkopinion;//检查意见

	public String getContent() {
		if(content==null) content="";
		return content;
	}
	public String getCheckopinion() {
		if(checkopinion==null) checkopinion="";
		return checkopinion;
	}
}
