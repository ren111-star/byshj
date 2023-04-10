package bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubjectBean {
	
	private String subid;//课题编号，自动分配
	private String subname;//课题名
	private String usedyear;//使用年份
	private String oldargu;//原始参数
	private String content;//工作内容
	private String requirement;//工作要求
	private String refpapers;//参考文献
	private String subtype;//课题类型
	private String subtypename;//课题类型代码内容
	private String subdirection;//课题方向代码
	private String subdirectionname;//课题方向代码内容
	private String subsort;//课题类别
	private String subsortname;//课题类别代码内容
	private String subkind;//课题性质
	private String subkindname;//课题性质代码内容
	private String subsource;//课题来源
	private String subsourcename;//课题来源代码内容
	private int isoutschool;//是否校外
	private TeacherBean tutor;//指导教师
	private TeacherBean othertutor;//其他指导教师
	private String submitflag;//提交标志
	private String subprog;//工作进度
	private List<String> listspec;//课题适合专业列表
	private String status;//在整个生命周期中，课题包括未提交、审核中、审核通过、选题中、n学生初选、已选共6种状态
	private List<String> operations;//在不同的状态下可进行的操作集合
	private String stuid;//学生学号
	private List<SubspecBean> listsubspec;//专业选题情况
	private String specname;
	private String reviewopinion;//审核意见（课题盲审）
	private List<ProgressBean> progress;//进程表
	private List<SubSimBean> simSubsInHis;//历史中相似的课题
	private String reviewerForSub;//课题审核人（姓名）

	public String getSubprog() {
		if(subprog==null)subprog="";
		return subprog;
	}
	public SubjectBean(){
		listspec= new ArrayList<String>();
		operations=new ArrayList<String>();
		tutor=new TeacherBean();
		othertutor=new TeacherBean();
		listsubspec=new ArrayList<SubspecBean>();
		simSubsInHis=new ArrayList<SubSimBean>();
	}

	public String getStatus() {
		if(status==null) status="";
		return status;
	}
	public String getStuid() {
		if(stuid==null) stuid="";
		return stuid;
	}
	public String getContent() {
		if(content==null) content="";
		return content;
	}
	public String getOldargu() {
		if(oldargu==null) oldargu="";
		return oldargu;
	}
	public String getRefpapers() {
		if(refpapers==null) refpapers="";
		return refpapers;
	}
	public String getRequirement() {
		if(requirement==null) requirement="";
		return requirement;
	}
	public String getSubkind() {
		if(subkind==null) subkind="";
		return subkind;
	}
	public String getSubmitflag() {
		if(submitflag==null) submitflag="";
		return submitflag;
	}
	public String getSubname() {
		if(subname==null) subname="";
		return subname;
	}
	public String getSubsource() {
		if(subsource==null) subsource="";
		return subsource;
	}
	public String getSubtype() {
		if(subtype==null) subtype="";
		return subtype;
	}
	public String getUsedyear() {
		if(usedyear==null) usedyear="";
		return usedyear;
	}
	public String getSubkindname() {
		if(subkindname==null)subkindname="";
		return subkindname;
	}
	public String getSubsourcename() {
		if(subsourcename==null)subsourcename="";
		return subsourcename;
	}
	public String getSubtypename() {
		if(subtypename==null)subtypename="";
		return subtypename;
	}
	public String getSpecname() {
		if(specname==null) specname="";
		return specname;
	}
}
