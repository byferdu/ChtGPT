package com.ferdu.chtgpt.util.update;

import java.io.Serializable;
import java.util.Objects;

import cn.leancloud.LCObject;
import cn.leancloud.annotation.LCClassName;

@LCClassName("UpdateApk")
public class AppVersionInfoBean extends LCObject implements Serializable {

    private String title;
    private String content;
    private String url;
    private String md5;
    private String versionCode;

    private AppVersionInfoBean(String title, String content, String url, String md5, String versionCode) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.md5 = md5;
        this.versionCode = versionCode;
    }

    /**
     * 把response转换为AppVersionInfoBean。
     *
     * @param response 返回信息
     * @return AppVersionInfoBean
     */
    public static AppVersionInfoBean parse(LCObject response) {

            //JSONObject responseJson = new JSONObject(response);
        String title = Objects.requireNonNull(response.getServerData().get(UpdateApk.TITLE)).toString();
        String content = Objects.requireNonNull(response.getServerData().get(UpdateApk.CONTENT)).toString();
        String url = Objects.requireNonNull(response.getServerData().get(UpdateApk.URL)).toString();
        String md5 = Objects.requireNonNull(response.getServerData().get(UpdateApk.MD5)).toString();
        String versionCode = Objects.requireNonNull(response.getServerData().get(UpdateApk.VERSIONCODE)).toString();


            // 不应该在这里检测，检测属于使用这个bean，不适合在这里处理

            return new AppVersionInfoBean(title,content,url,md5,versionCode);
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public String getMd5() {
        return md5;
    }

    public String getVersionCode() {
        return versionCode;
    }
}