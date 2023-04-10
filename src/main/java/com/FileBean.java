package com;

public class FileBean {
    private String fileName;//所有文件名集合
    private String fileSize;//所有文件大小的集合(格式化后的大小GMK)

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
