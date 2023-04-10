package bean;

import lombok.Data;

@Data
public class SubReviewBean {
	String tid;//审核教师号
	String subid;//课题编号
	String subname;//课题名称
	Integer simsubcount;//相似的历史课题数（与自己的课题比较）
	String reviewopinion;//审核意见（盲审）
}
