package bean;

import com.SyscodeBean;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TeacherBean {

    private String tid;//��ʦ��ţ�6λ�ַ�
    private String tname;//��ʦ��
    private String tsex;//�Ա����
    private String tsexname;//�Ա�����
    private SpecialityBean specbean;//����רҵ
    private String tdept;//���ڽ�ѧ��λ����
    private String tdeptname;//��ѧ��λ����
    private String tpost;//ְ�ƴ���
    private String tpostname;//ְ������
    private String tdegree;//ѧλ����
    private String tdegreename;//ѧλ����
    private String studydirect;//�о�����
    private String email;
    private String telphone;//�绰
    private String remark;//��ע
    private List<RoleBean> listrole;//��ɫ�б�
    private ArrayList<SyscodeBean> subdirections;//���ⷽ��

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
