package com;

public class FileBean {
    private String fileName;//�����ļ�������
    private String fileSize;//�����ļ���С�ļ���(��ʽ����Ĵ�СGMK)

    public String getFileName() {
        if (fileName == null) fileName = "";
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }


}
