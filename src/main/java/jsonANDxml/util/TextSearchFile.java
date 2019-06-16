package jsonANDxml.util;

/**
 * Created by appledev131 on 8/29/16.
 */

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

//操作查找文件的类
public class TextSearchFile {
    static int countFiles = 0;// 声明统计文件个数的变量
    static int countFolders = 0;// 声明统计文件夹的变量

    public static File[] searchFile(File folder, final String keyWord) {// 递归查找包含关键字的文件

        File[] subFolders = folder.listFiles(new FileFilter() {// 运用内部匿名类获得文件
            @Override
            public boolean accept(File pathname) {// 实现FileFilter类的accept方法
                if (pathname.isFile())// 如果是文件
                    countFiles++;
                else
                    // 如果是目录
                    countFolders++;
                if (pathname.isDirectory()
                        || (pathname.isFile() && pathname.getName().toLowerCase().contains(keyWord.toLowerCase())))// 目录或文件包含关键字
                    return true;
                return false;
            }
        });

        List<File> result = new ArrayList<File>();// 声明一个集合
        for (int i = 0; i < subFolders.length; i++) {// 循环显示文件夹或文件
            if (subFolders[i].isFile()) {// 如果是文件则将文件添加到结果列表中
                result.add(subFolders[i]);
            } else {// 如果是文件夹，则递归调用本方法，然后把所有的文件加到结果列表中
                File[] foldResult = searchFile(subFolders[i], keyWord);
                for (int j = 0; j < foldResult.length; j++) {// 循环显示文件
                    result.add(foldResult[j]);// 文件保存到集合中
                }
            }
        }

        File files[] = new File[result.size()];// 声明文件数组，长度为集合的长度
        result.toArray(files);// 集合数组化
        return files;
    }

    public static void main(String[] args) {// java程序的主入口处
        File folder = new File(new String(System.getProperty("user.dir") + "/PageXML/AND/"));// 默认目录
        String keyword = ".xml";
        if (!folder.exists()) {// 如果文件夹不存在
            System.out.println("目录不存在：" + folder.getAbsolutePath());
            return;
        }
        long gstartTime = System.currentTimeMillis();
        File[] result = searchFile(folder, keyword);// 调用方法获得文件数组
        System.out.println("^^^^^^    get all files：" + (System.currentTimeMillis() - gstartTime) + " ms    ^^^^^^");
        System.out.println("在 " + folder + " 以及所有子文件时查找对象" + keyword);
        System.out.println("查找了" + countFiles + " 个文件，" + countFolders + " 个文件夹，共找到 " + result.length + " 个符合条件的文件：");
        LinkedHashMap<String, Path> file2path = new LinkedHashMap<String, Path>();
        for (int i = 0; i < result.length; i++) {// 循环显示文件
            File file = result[i];
            //   System.out.println(file.getName() + " only name");
            // System.out.println(file.getPath() + " only path");
            // System.out.println(file.getAbsolutePath() + " ");// 显示文件绝对路径
            //  System.out.println(file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf("/")+1));
            file2path.put(file.getName(), Paths.get(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("/") + 1)));

        }

    }

    public static List<File> run4files(Path path) {
        final List<File> files = new ArrayList<File>();
        SimpleFileVisitor<Path> finder = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                files.add(file.toFile());
                return super.visitFile(file, attrs);
            }
        };
        try {
            java.nio.file.Files.walkFileTree(path, finder);
        } catch (IOException e) {
            //ignore
        }
        System.out.printf("ͨ�� Java 7 NIO.2 ������ %s ���ļ���", files.size());
        return files;
    }
}