package com.ferdu.chtgpt.util.update;

import android.os.Parcelable;

import cn.leancloud.LCObject;
import cn.leancloud.LCParcelableObject;
import cn.leancloud.annotation.LCClassName;

@LCClassName("UpdateApk")
public class UpdateApk extends LCObject {
    public static final Parcelable.Creator CREATOR = LCParcelableObject.LCObjectCreator.instance;
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String URL = "url";
    public static final String MD5 = "md5";
    public static final String VERSIONCODE = "versionCode";

    public String getTitle() {
        return getString(TITLE);
    }

    public void setTitle(String title) {
        put(TITLE, title);
    }

    public String getContent() {
        return getString(CONTENT);
    }

    public void setContent(String content) {
        put(CONTENT, content);
    }

    public String getUrl() {
        return getString(URL);
    }

    public void setUrl(String url) {
        put(URL, url);
    }

    public String getMd5() {
        return getString(MD5);
    }

    public void setMd5(String md5) {
        put(MD5, md5);
    }
    public String getCodeVersion() {
        return getString(VERSIONCODE);
    }

    public void setCodeVersion(String version) {
        put(VERSIONCODE, version);
    }

}
