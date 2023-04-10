package bean;

import lombok.Data;

@Data
public class ReviewSubjectBean {
	
	private String subid;//课题编号，自动分配
	private String subname;//课题名
	private String tutornames;//指导教师名[已拼装好的]
	private String reviewername;//课题审核人名
	private String reviewopinion;//审核意见（课题盲审）
}
