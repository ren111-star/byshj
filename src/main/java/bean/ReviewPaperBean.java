package bean;

import lombok.Data;

@Data
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
	public String getReviewopinion() {
		if(reviewopinion==null)reviewopinion="";
		return reviewopinion;
	}
	public String getReviewtime() {
		if(reviewtime==null)reviewtime="";
		return reviewtime;
	}
	
}
