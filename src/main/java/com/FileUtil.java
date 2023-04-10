package com;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * 删除文件和目录
 */
public class FileUtil {
    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName 要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            //System.out.println("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        //	 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                //System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                //System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            //System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        //	 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) dir = dir + File.separator;
        File dirFile = new File(dir);
        //	 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            //System.out.println("删除目录失败：" + dir + "不存在！");
            return true;
        }
        boolean flag = true;
        //	 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //	 删除子文件
            if (files[i].isFile()) {
                flag = FileUtil.deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            //	 删除子目录
            else if (files[i].isDirectory()) {
                flag = FileUtil.deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            //System.out.println("删除目录失败！");
            return false;
        }
        //	删除当前目录
        if (dirFile.delete()) {
            //System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }

    //新建不存在的目录
    public static void Mkdir(String path) throws Exception {
        File dir;
//	 新建文件对象 
        dir = new File(path);
        if (dir == null) throw new Exception("不能创建空目录:" + path);
        if (dir.isFile()) throw new Exception("已有同名文件存在:" + path);
        if (!dir.exists()) {
            boolean result = dir.mkdirs();
            if (result == false) {
                throw new Exception("不能创建目录（原因不明）:" + dir.getAbsolutePath());
            }
            System.out.println(dir.getAbsolutePath());
        } else {
            throw new Exception("目录已存在");
        }
    }

    //新建不存在的目录,若存在也成功返回
    public static void MkdirWithoutIfExisted(String path) throws Exception {
        File dir = new File(path);
        if (dir.isFile()) throw new Exception("已有同名文件存在:" + path);
        if (!dir.exists()) {
            boolean result = dir.mkdirs();
            if (result == false) {
                throw new Exception("不能创建目录（原因不明）:" + dir.getAbsolutePath());
            }
            System.out.println(dir.getAbsolutePath());
        }

    }

    //返回指定文件目录中所有文件名，并放在collection中返回
    public static Collection getFilesinDirectory(String dir) {
        Collection dirfiles = new ArrayList();
        //如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) dir = dir + File.separator;
        File dirFile = new File(dir);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("目录：" + dir + "不存在！");
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

    //转换文件大小
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

    //	判断文件在指定目录中是否存在
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
