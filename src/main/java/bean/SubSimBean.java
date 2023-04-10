/*仅是为相似度计算结果定义的结构*/
package bean;

import lombok.Data;

@Data
public class SubSimBean {
	private String subname;//课题名
	private String tutorname;//指导教师名
	private Float similard;//相似度
	private String usedyear;//被使用的年份

	
}
