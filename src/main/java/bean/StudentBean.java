package bean;

import lombok.Data;

@Data
public class StudentBean {

    private String stuid;//ѧ�ţ�10λ�̶�����
    private String sname;//����
    private String ssex;//�Ա�
    private String classname;//�༶��
    private ClassBean classbean;//�༶bean
    private String email;
    private String telephone;//�绰
    private String remark;//��ע
    private String status;//ѧ��״̬
    private SubjectBean subject;//ѧ��ѡ��Ŀ����б�

    public StudentBean() {
        subject = new SubjectBean();
        classbean = new ClassBean();
    }

    public String getStatus() {
        if (status == null) status = "";
        return status;
    }

    public String getEmail() {
        if (email == null) email = "";
        return email;
    }

    public void setEmail(String email) {
        if (email == null) email = "";
        this.email = email;
    }

    public String getRemark() {
        if (remark == null) remark = "";
        return remark;
    }

    public String getSname() {
        if (sname == null) sname = "";
        return sname;
    }

    public String getSsex() {
        if (ssex == null) ssex = "";
        return ssex;
    }


    public String getTelephone() {
        if (telephone == null) telephone = "";
        return telephone;
    }

    public void setTelephone(String telephone) {
        if (telephone == null) telephone = "";
        this.telephone = telephone;
    }

    public String getClassname() {
        if (classname == null) classname = "";
        return classname;
    }

}
