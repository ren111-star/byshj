package bean;

import lombok.Data;

@Data
public class SubNumByTeaBean {
	String tid;//教师编号
	String tname;//教师名
	int submitsubnum;//提交的课题数目
	int reviewsubnum;//需审核的课题数目
	int unrevnum;//未被审核的课题数目

}
