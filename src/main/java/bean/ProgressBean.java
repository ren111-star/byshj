package bean;

import lombok.Data;

@Data
public class ProgressBean {
	
	private String progid;//����id,�Զ����
	private String subid;//������
	private String inorder;//�ڲ�˳��ͬһ���������ɽ�����Ŀ
	private String content;//��������
	private String startenddate;//��ʼ����ʱ��
	private String checkopinion;//������

	public String getContent() {
		if(content==null) content="";
		return content;
	}
	public String getCheckopinion() {
		if(checkopinion==null) checkopinion="";
		return checkopinion;
	}
}
