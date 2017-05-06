package com.wux.wenku.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by WuX on 2017/5/4.
 */

public class BitmapUtil {
    /**
     * 网络图片转成bitmap
     *
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url) {
        Bitmap bitmap = null;
        try {
            URL pictureUrl = new URL(url);
            InputStream in = pictureUrl.openStream();
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 保存图片
     *
     * @param bitmap
     */
    public static void savePicture(Bitmap bitmap) {
        File sd = Environment.getExternalStorageDirectory();
        String path = sd.getPath() + "/2";
        File file1 = new File(path);
        if (!file1.exists()) {
            file1.mkdir();
        }
        String pictureName = path + "/car.jpg";
        File file = new File(pictureName);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
//            Toast.makeText(this, " 保存成功", Toast.LENGTH_LONG).show();
            ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != bitmap && !bitmap.isRecycled()) {
                bitmap.recycle();
                ;
            }
        }
    }
}
