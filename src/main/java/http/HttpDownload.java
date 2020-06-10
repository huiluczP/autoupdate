package http;

import org.apache.http.*;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class HttpDownload {
    private static final int cache = 10 * 1024;
    private static final String splash;
    private static final String root;

    static {
        splash = "/";
        root = "download";
    }

    // 根据url下载文件，保存到filepath中
    public static boolean download(String url, String filename, JProgressBar bar) {
        System.out.println("start downloading");
        try {
            // cookie时间可能会出错，设置下
            CloseableHttpClient client= HttpClients.custom()
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setCookieSpec(CookieSpecs.STANDARD).build())
                    .build();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = client.execute(httpget);

            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            String filepath = getFilePath(filename);

            File file = new File(filepath);
            boolean makeDir = file.getParentFile().mkdir();
            System.out.println(file.getAbsolutePath());
            FileOutputStream fileOut = new FileOutputStream(file);

            // 根据实际运行效果 设置缓冲区大小
            byte[] buffer = new byte[cache];
            int ch = -1;
            while ((ch = is.read(buffer)) != -1) {
                // 假进度条
                int valueNow = bar.getValue();
                if(valueNow <= 80) {
                    bar.setValue(valueNow + 5);
                }else{
                    bar.setValue(valueNow + 2);
                }
                System.out.println("cache " + filename);
                fileOut.write(buffer, 0, ch);
            }
            bar.setValue(100);
            is.close();
            fileOut.flush();
            fileOut.close();
            System.out.println(filename + " download success");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 获取下载路径
    private static String getFilePath(String fileName) {
        String filepath = root + splash;
        filepath += fileName;
        return filepath;
    }

    public static void main(String[] args) {
        String url = "https://codeload.github.com/douban/douban-client/legacy.zip/master";
        String fileName = "legacy.zip";
        JProgressBar bar = new JProgressBar();
        HttpDownload.download(url, fileName, bar);
    }
}
