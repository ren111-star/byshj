package bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClassBean {

    private String classid;//�༶Ψһ��ʶ���Զ����
    private String classname;//�༶���ƣ�Ψһ
    private SpecialityBean speciality;//����רҵ���
    private String enrolyear;//��ѧ���
    private String gradyear;//��ҵ���
    private String specid;//רҵid

    public ClassBean() {
        speciality = new SpecialityBean();
    }

    public String getEnrolyear() {
        if (enrolyear == null) enrolyear = "";
        return enrolyear;
    }

    public String getGradyear() {
        if (gradyear == null) gradyear = "";
        return gradyear;
    }

}
