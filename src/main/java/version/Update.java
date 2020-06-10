package version;

import bean.DownloadInfo;
import http.HttpDownload;

import javax.swing.*;
import java.util.ArrayList;

public class Update {
    // 下载最新版本
    public static String download(ArrayList<DownloadInfo> downloadList, JProgressBar bar){
        StringBuffer sb = new StringBuffer();
        if(downloadList!=null){
            for(DownloadInfo info:downloadList){
                String fileName = info.getName();
                String url = info.getUrl();
                boolean downloadResult = HttpDownload.download(url, fileName, bar);
                if(downloadResult)
                    sb.append(fileName).append(" ");
            }
        }
        return sb.toString();
    }
}
