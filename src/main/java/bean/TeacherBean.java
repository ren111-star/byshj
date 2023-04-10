package bean;

import com.SyscodeBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TeacherBean {

    private String tid;//教师编号，6位字符
    private String tname;//教师名
    private String tsex;//性别代码
    private String tsexname;//性别名称
    private SpecialityBean specbean;//所属专业
    private String tdept;//所在教学单位代码
    private String tdeptname;//教学单位名称
    private String tpost;//职称代码
    private String tpostname;//职称名称
    private String tdegree;//学位代码
    private String tdegreename;//学位名称
    private String studydirect;//研究方向
    private String email;
    private String telphone;//电话
    private String remark;//备注
    private List<RoleBean> listrole;//角色列表
    private ArrayList<SyscodeBean> subdirections;//课题方向

    public TeacherBean() {
        specbean = new SpecialityBean();
        listrole = new ArrayList<RoleBean>();

    }

    public String getEmail() {
        if (email == null) email = "";
        return email;
    }

    public String getRemark() {
        if (remark == null) remark = "";
        return remark;
    }

    public String getStudydirect() {
        if (studydirect == null) studydirect = "";
        return studydirect;
    }

    public String getTdept() {
        if (tdept == null) tdept = "";
        return tdept;
    }
    public String getTelphone() {
        if (telphone == null) telphone = "";
        return telphone;
    }

    public String getTname() {
        if (tname == null) tname = "";
        return tname;
    }

    public String getTpost() {
        if (tpost == null) tpost = "";
        return tpost;
    }

    public String getTsex() {
        if (tsex == null) tsex = "";
        return tsex;
    }

    public String getTdeptname() {
        if (tdeptname == null) tdeptname = "";
        return tdeptname;
    }

    public String getTpostname() {
        if (tpostname == null) tpostname = "";
        return tpostname;
    }

    public String getTsexname() {
        if (tsexname == null) tsexname = "";
        return tsexname;
    }

    public String getTdegree() {
        if (tdegree == null) tdegree = "";
        return tdegree;
    }

    public String getTdegreename() {
        if (tdegreename == null) tdegreename = "";
        return tdegreename;
    }
}
