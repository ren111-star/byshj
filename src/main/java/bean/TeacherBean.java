package bean;

import com.SyscodeBean;

import java.util.ArrayList;
import java.util.List;

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

    public ArrayList<SyscodeBean> getSubdirections() {
        return subdirections;
    }

    public void setSubdirections(ArrayList<SyscodeBean> subdirections) {
        this.subdirections = subdirections;
    }

    public List<RoleBean> getListrole() {
        return listrole;
    }

    public void setListrole(List<RoleBean> listrole) {
        this.listrole = listrole;
    }

    public SpecialityBean getSpecbean() {
        return specbean;
    }

    public void setSpecbean(SpecialityBean specbean) {
        this.specbean = specbean;
    }

    public TeacherBean() {
        specbean = new SpecialityBean();
        listrole = new ArrayList<RoleBean>();

    }

    public String getEmail() {
        if (email == null) email = "";
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        if (remark == null) remark = "";
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStudydirect() {
        if (studydirect == null) studydirect = "";
        return studydirect;
    }

    public void setStudydirect(String studydirect) {
        this.studydirect = studydirect;
    }

    public String getTdept() {
        if (tdept == null) tdept = "";
        return tdept;
    }

    public void setTdept(String tdept) {
        this.tdept = tdept;
    }

    public String getTelphone() {
        if (telphone == null) telphone = "";
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTname() {
        if (tname == null) tname = "";
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTpost() {
        if (tpost == null) tpost = "";
        return tpost;
    }

    public void setTpost(String tpost) {
        this.tpost = tpost;
    }

    public String getTsex() {
        if (tsex == null) tsex = "";
        return tsex;
    }

    public void setTsex(String tsex) {
        this.tsex = tsex;
    }

    public String getTdeptname() {
        if (tdeptname == null) tdeptname = "";
        return tdeptname;
    }

    public void setTdeptname(String tdeptname) {
        this.tdeptname = tdeptname;
    }

    public String getTpostname() {
        if (tpostname == null) tpostname = "";
        return tpostname;
    }

    public void setTpostname(String tpostname) {
        this.tpostname = tpostname;
    }

    public String getTsexname() {
        if (tsexname == null) tsexname = "";
        return tsexname;
    }

    public void setTsexname(String tsexname) {
        this.tsexname = tsexname;
    }

    public String getTdegree() {
        if (tdegree == null) tdegree = "";
        return tdegree;
    }

    public void setTdegree(String tdegree) {
        this.tdegree = tdegree;
    }

    public String getTdegreename() {
        if (tdegreename == null) tdegreename = "";
        return tdegreename;
    }

    public void setTdegreename(String tdegreename) {
        this.tdegreename = tdegreename;
    }
}
