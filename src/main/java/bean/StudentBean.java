package bean;

import lombok.Data;

@Data
public class StudentBean {

    private String stuid;//学号，10位固定长度
    private String sname;//姓名
    private String ssex;//性别
    private String classname;//班级名
    private ClassBean classbean;//班级bean
    private String email;
    private String telphone;//电话
    private String remark;//备注
    private String status;//学生状态
    private SubjectBean subject;//学生选择的课题列表

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


    public String getTelphone() {
        if (telphone == null) telphone = "";
        return telphone;
    }

    public void setTelphone(String telphone) {
        if (telphone == null) telphone = "";
        this.telphone = telphone;
    }

    public String getClassname() {
        if (classname == null) classname = "";
        return classname;
    }

}
