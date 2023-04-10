package bean;

import lombok.Data;

@Data
public class ReviewPaperBaseInfoBean {
	String classname;//班级
	String stuid;//学号
	String sname;//姓名
	String subid;//课题号
	String subname;//课题名称
	String tutornames;//指导教师名
	String reviewername;//评阅人名
	String submitflag;//提交标志
}
