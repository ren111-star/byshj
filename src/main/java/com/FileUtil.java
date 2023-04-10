package com;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * ɾ���ļ���Ŀ¼
 */
public class FileUtil {
    /**
     * ɾ���ļ����������ļ����ļ���
     *
     * @param fileName Ҫɾ�����ļ���
     * @return ɾ���ɹ�����true�����򷵻�false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            //System.out.println("ɾ���ļ�ʧ��:" + fileName + "�����ڣ�");
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * ɾ�������ļ�
     *
     * @param fileName Ҫɾ�����ļ����ļ���
     * @return �����ļ�ɾ���ɹ�����true�����򷵻�false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        //	 ����ļ�·������Ӧ���ļ����ڣ�������һ���ļ�����ֱ��ɾ��
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                //System.out.println("ɾ�������ļ�" + fileName + "�ɹ���");
                return true;
            } else {
                //System.out.println("ɾ�������ļ�" + fileName + "ʧ�ܣ�");
                return false;
            }
        } else {
            //System.out.println("ɾ�������ļ�ʧ�ܣ�" + fileName + "�����ڣ�");
            return false;
        }
    }

    /**
     * ɾ��Ŀ¼��Ŀ¼�µ��ļ�
     *
     * @param dir Ҫɾ����Ŀ¼���ļ�·��
     * @return Ŀ¼ɾ���ɹ�����true�����򷵻�false
     */
    public static boolean deleteDirectory(String dir) {
        //	 ���dir�����ļ��ָ�����β���Զ�����ļ��ָ���
        if (!dir.endsWith(File.separator)) dir = dir + File.separator;
        File dirFile = new File(dir);
        //	 ���dir��Ӧ���ļ������ڣ����߲���һ��Ŀ¼�����˳�
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            //System.out.println("ɾ��Ŀ¼ʧ�ܣ�" + dir + "�����ڣ�");
            return true;
        }
        boolean flag = true;
        //	 ɾ���ļ����е������ļ�������Ŀ¼
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //	 ɾ�����ļ�
            if (files[i].isFile()) {
                flag = FileUtil.deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            //	 ɾ����Ŀ¼
            else if (files[i].isDirectory()) {
                flag = FileUtil.deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            //System.out.println("ɾ��Ŀ¼ʧ�ܣ�");
            return false;
        }
        //	ɾ����ǰĿ¼
        if (dirFile.delete()) {
            //System.out.println("ɾ��Ŀ¼" + dir + "�ɹ���");
            return true;
        } else {
            return false;
        }
    }

    //�½������ڵ�Ŀ¼
    public static void Mkdir(String path) throws Exception {
        File dir;
//	 �½��ļ����� 
        dir = new File(path);
        if (dir == null) throw new Exception("���ܴ�����Ŀ¼:" + path);
        if (dir.isFile()) throw new Exception("����ͬ���ļ�����:" + path);
        if (!dir.exists()) {
            boolean result = dir.mkdirs();
            if (result == false) {
                throw new Exception("���ܴ���Ŀ¼��ԭ������:" + dir.getAbsolutePath());
            }
            System.out.println(dir.getAbsolutePath());
        } else {
            throw new Exception("Ŀ¼�Ѵ���");
        }
    }

    //�½������ڵ�Ŀ¼,������Ҳ�ɹ�����
    public static void MkdirWithoutIfExisted(String path) throws Exception {
        File dir = new File(path);
        if (dir.isFile()) throw new Exception("����ͬ���ļ�����:" + path);
        if (!dir.exists()) {
            boolean result = dir.mkdirs();
            if (result == false) {
                throw new Exception("���ܴ���Ŀ¼��ԭ������:" + dir.getAbsolutePath());
            }
            System.out.println(dir.getAbsolutePath());
        }

    }

    //����ָ���ļ�Ŀ¼�������ļ�����������collection�з���
    public static Collection getFilesinDirectory(String dir) {
        Collection dirfiles = new ArrayList();
        //���dir�����ļ��ָ�����β���Զ�����ļ��ָ���
        if (!dir.endsWith(File.separator)) dir = dir + File.separator;
        File dirFile = new File(dir);
        //���dir��Ӧ���ļ������ڣ����߲���һ��Ŀ¼�����˳�
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("Ŀ¼��" + dir + "�����ڣ�");
            return null;
        }
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            FileBean temp = new FileBean();
            temp.setFileName(files[i].getName());
            temp.setFileSize(FormetFileSize(files[i].length()));
            dirfiles.add(temp);
        }
        return dirfiles;
    }

    //ת���ļ���С
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.0");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    //	�ж��ļ���ָ��Ŀ¼���Ƿ����
    public static boolean fileinDir(String filename, String dir) {
        boolean flag = false;
        Collection dirfiles = getFilesinDirectory(dir);
        Iterator iter = dirfiles.iterator();
        while (iter.hasNext()) {
            FileBean temp = (FileBean) iter.next();
            if (temp.getFileName().equals(filename)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
