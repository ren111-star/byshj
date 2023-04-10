package base;

import bean.TeacherBean;

public class SubjectBasePojo {
    private String subid;//�����ţ��Զ�����
    private String subname;//������
    private String oldargu;//ԭʼ����
    private String content;//��������
    private String requirement;//����Ҫ��
    private String refpapers;//�ο�����
    private String subtype;//��������
    private String subtypename;//�������ʹ�������
    private String subdirection;//���ⷽ�����
    private String subdirectionname;//���ⷽ���������
    private String subsort;//�������
    private String subsortname;//��������������
    private String subkind;//��������
    private String subkindname;//�������ʴ�������
    private String subsource;//������Դ
    private String subsourcename;//������Դ��������
    private int isoutschool;//�Ƿ�У��
    private TeacherBean tutor;//ָ����ʦ
    private TeacherBean othertutor;//����ָ����ʦ
    private String subprog;//��������

    public SubjectBasePojo() {
        tutor = new TeacherBean();
        othertutor = new TeacherBean();
    }

    public String getSubsort() {
        return subsort;
    }

    public void setSubsort(String subsort) {
        this.subsort = subsort;
    }

    public String getSubsortname() {
        return subsortname;
    }

    public void setSubsortname(String subsortname) {
        this.subsortname = subsortname;
    }

    public String getSubdirection() {
        return subdirection;
    }

    public void setSubdirection(String subdirection) {
        this.subdirection = subdirection;
    }

    public String getSubdirectionname() {
        return subdirectionname;
    }

    public void setSubdirectionname(String subdirectionname) {
        this.subdirectionname = subdirectionname;
    }

    public String getSubprog() {
        if (subprog == null) subprog = "";
        return subprog;
    }

    public void setSubprog(String subprog) {
        this.subprog = subprog;
    }

    public TeacherBean getOthertutor() {
        return othertutor;
    }

    public void setOthertutor(TeacherBean othertutor) {
        this.othertutor = othertutor;
    }

    public TeacherBean getTutor() {
        return tutor;
    }

    public void setTutor(TeacherBean tutor) {
        this.tutor = tutor;
    }

    public String getContent() {
        if (content == null) content = "";
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOldargu() {
        if (oldargu == null) oldargu = "";
        return oldargu;
    }

    public void setOldargu(String oldargu) {
        this.oldargu = oldargu;
    }

    public String getRefpapers() {
        if (refpapers == null) refpapers = "";
        return refpapers;
    }

    public void setRefpapers(String refpapers) {
        this.refpapers = refpapers;
    }

    public String getRequirement() {
        if (requirement == null) requirement = "";
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getSubid() {
        return subid;
    }

    public void setSubid(String subid) {
        this.subid = subid;
    }

    public String getSubkind() {
        if (subkind == null) subkind = "";
        return subkind;
    }

    public void setSubkind(String subkind) {
        this.subkind = subkind;
    }

    public String getSubname() {
        if (subname == null) subname = "";
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public String getSubsource() {
        if (subsource == null) subsource = "";
        return subsource;
    }

    public void setSubsource(String subsource) {
        this.subsource = subsource;
    }

    public String getSubtype() {
        if (subtype == null) subtype = "";
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getSubkindname() {
        if (subkindname == null) subkindname = "";
        return subkindname;
    }

    public void setSubkindname(String subkindname) {
        this.subkindname = subkindname;
    }

    public String getSubsourcename() {
        if (subsourcename == null) subsourcename = "";
        return subsourcename;
    }

    public void setSubsourcename(String subsourcename) {
        this.subsourcename = subsourcename;
    }

    public String getSubtypename() {
        if (subtypename == null) subtypename = "";
        return subtypename;
    }

    public void setSubtypename(String subtypename) {
        this.subtypename = subtypename;
    }

    public int getIsoutschool() {
        return isoutschool;
    }

    public void setIsoutschool(int isoutschool) {
        this.isoutschool = isoutschool;
    }
}
