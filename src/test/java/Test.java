import bean.DownloadInfo;
import version.Update;
import version.VersionInfo;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

public class Test {
    public static void main(String[] args){

        VersionInfo info = new VersionInfo();
        String result = info.getVersionInfo();
        System.out.println(result);

        String version = info.getLatestVersion();
        System.out.println(version);

        ArrayList<DownloadInfo> infos = info.getDownLoadUrl();
        for(DownloadInfo i:infos){
            System.out.println(i.getName());
        }

        JProgressBar bar = new JProgressBar();
        String downloadResult = Update.download(infos, bar);
        System.out.println("download:" + downloadResult);
    }
}
