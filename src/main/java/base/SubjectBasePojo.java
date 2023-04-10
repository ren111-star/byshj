package base;

import bean.TeacherBean;

public class SubjectBasePojo {
    private String subid;//课题编号，自动分配
    private String subname;//课题名
    private String oldargu;//原始参数
    private String content;//工作内容
    private String requirement;//工作要求
    private String refpapers;//参考文献
    private String subtype;//课题类型
    private String subtypename;//课题类型代码内容
    private String subdirection;//课题方向代码
    private String subdirectionname;//课题方向代码内容
    private String subsort;//课题类别
    private String subsortname;//课题类别代码内容
    private String subkind;//课题性质
    private String subkindname;//课题性质代码内容
    private String subsource;//课题来源
    private String subsourcename;//课题来源代码内容
    private int isoutschool;//是否校外
    private TeacherBean tutor;//指导教师
    private TeacherBean othertutor;//其他指导教师
    private String subprog;//工作进度

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
