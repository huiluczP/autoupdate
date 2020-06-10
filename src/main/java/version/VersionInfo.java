package version;

import bean.DownloadInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import http.HttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VersionInfo {
    private static String VERSION = "0.9.3";// 当前版本
    private static String VERSIONURL = "https://api.github.com/repos/carhartl/jquery-cookie/releases/latest";// 获取当前最新版本的地址
    private Map map;

    public String getVersionInfo(){
        String result = HttpRequest.sendGetRequest(VERSIONURL);
        if(result!=null){
            Map map = (Map) JSON.parse(result);
            this.map = map;
            return "Version info get success";
        }else{
            return "Version info get failed";
        }
    }

    public String getLatestVersion(){
        if(this.map!=null){
            return map.get("tag_name").toString();
        }else{
            return null;
        }
    }

    public ArrayList<DownloadInfo> getDownLoadUrl(){
        // 返回当前文件的下载列表
        ArrayList<DownloadInfo> downloadInfos = new ArrayList<DownloadInfo>();
        if(this.map!=null){
            List l =JSON.parseArray(map.get("assets").toString());
            for (Object s:l){
                Map simpleMap = (Map) JSON.parse(s.toString());
                downloadInfos.add(new DownloadInfo(simpleMap.get("name").toString(), simpleMap.get("browser_download_url").toString()));
            }
            return downloadInfos;
        }else{
            return downloadInfos;
        }
    }

    public String getCurrentVersion(){
        return VERSION;
    }
}
