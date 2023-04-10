package bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClassBean {

    private String classid;//班级唯一标识，自动编号
    private String classname;//班级名称，唯一
    private SpecialityBean speciality;//所属专业编号
    private String enrolyear;//入学年份
    private String gradyear;//毕业年份
    private String specid;//专业id

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
