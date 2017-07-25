package com.wux.wenku.util;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by WuX on 2017/5/21.
 */

public class FileUtil {
    private static String SDPATH = Environment.getExternalStorageDirectory() + "/wenku8/";
    private  static String _Favorite = "favorite";

    /**
     * 保存文件作为缓存
     *
     * @param novel
     * @param chapter
     * @param content
     */
    public static void putContent(String novel, String chapter, String content) {
        try {
            if (!isFileExist(SDPATH + novel)) {
                File tempf = createSDDir(SDPATH + novel);
            }
            saveFile(SDPATH + novel + File.separator + chapter + ".txt", content);
//            File file = new File(SDPATH + novel + File.separator + chapter + ".txt");
//            if (!file.exists()) {
//                file.createNewFile();
//            }
//            FileOutputStream outStream = new FileOutputStream(SDPATH + novel + File.separator + chapter + ".txt", true);
//            OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
//            writer.write(content);
//            writer.write("/n");
//            writer.flush();
//            writer.close();//记得关闭
//            outStream.close();
        } catch (Exception e) {
            Log.e("m", "file write error:" + e);
        }
    }
    public static void saveFavorite(int bookcase,String content) {
        try {
            if (!isFileExist(SDPATH + _Favorite)) {
                File tempf = createSDDir(SDPATH + _Favorite);
            }
            String favPath = SDPATH + _Favorite + File.separator +  bookcase + ".txt";
            saveFile(favPath, content);
        } catch (Exception e) {
            Log.e("m", "file write error:" + e);
        }
    }
    /**
     * 保存文件
     * @param path
     * @param content
     * @throws Exception
     */
    private static void saveFile(String path, String content) throws Exception {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        FileOutputStream outStream = new FileOutputStream(path, true);
        OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
        writer.write(content);
        writer.write("/n");
        writer.flush();
        writer.close();//记得关闭
        outStream.close();
    }

    /**
     * 创建目录
     *
     * @param dirName
     * @return
     * @throws IOException
     */
    public static File createSDDir(String dirName) throws IOException {
        File dir = new File(dirName);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            dir.getAbsolutePath();
            dir.mkdir();
        }
        return dir;
    }

    /**
     * 判断文件是否存在
     *
     * @param novel
     * @param chapter
     * @return
     */
    public static boolean isFileExist(String novel, String chapter) {
        File file = new File(SDPATH + novel + File.separator + chapter + ".txt");
        file.isFile();
        return file.exists();
    }

    /**
     * 获取txt中小说内容
     *
     * @param novel
     * @param chapter
     * @return
     */
    public static String getContent(String novel, String chapter) {
        StringBuffer sb = new StringBuffer();
        try {
            File file = new File(SDPATH + novel + File.separator + chapter + ".txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString().replace(" ", "\n");
    }

    /**
     * 获取
     * @return
     */
    public static String getFavoriteHtml(int bookcase) {
        StringBuffer sb = new StringBuffer();
        try {
            String favPath = SDPATH + _Favorite + File.separator + bookcase + ".txt";
            if (!isFileExist(favPath)){
                return  null;
            }
            File file = new File(favPath);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString().replace(" ", "\n");
    }
    /**
     * 判断文件是否 存在
     *
     * @param fileName 文件全路径
     * @return
     */
    public static boolean isFileExist(String fileName) {
        File file = new File(fileName);
        file.isFile();
        return file.exists();
    }
}
