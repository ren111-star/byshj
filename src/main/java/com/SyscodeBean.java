package com;

public class SyscodeBean {
    private String codeid;//����Ψһ��ʶ
    private String codeno;//������
    private String codename;//��������
    private String codevalue;//����ֵ
    private String codecontent;//��������

    public String getCodecontent() {
        if (codecontent == null) codecontent = "";
        return codecontent;
    }

    public void setCodecontent(String codecontent) {
        this.codecontent = codecontent;
    }

    public String getCodeid() {
        if (codeid == null) codeid = "";
        return codeid;
    }

    public void setCodeid(String codeid) {
        this.codeid = codeid;
    }

    public String getCodename() {
        if (codename == null) codename = "";
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public String getCodeno() {
        return codeno;
    }

    public void setCodeno(String codeno) {
        this.codeno = codeno;
    }

    public String getCodevalue() {
        if (codevalue == null) codevalue = "";
        return codevalue;
    }

    public void setCodevalue(String codevalue) {
        this.codevalue = codevalue;
    }

}
