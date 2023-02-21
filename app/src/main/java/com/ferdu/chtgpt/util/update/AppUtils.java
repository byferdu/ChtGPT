package com.ferdu.chtgpt.util.update;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AppUtils {

    /**
     * 获取当前App的版本号
     *
     * @return 版本号
     */
    public static String getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0.0";
    }

    /**
     * 版本号比较
     *
     * @param v1
     * @param v2
     * @return 0代表相等，1代表左边大，-1代表右边大
     * Utils.compareVersion("1.0.358_20180820090554","1.0.358_20180820090553")=1
     */
    public static int compareVersion(String v1, String v2) {
        if (v1.equals(v2)) {
            return 0;
        }
        if (v1.contains("v"))
            v1 = v1.replace("v", "");
        if (v2.contains("v"))
            v2 = v2.replace("v", "");
        String[] version1Array = v1.split("[._]");
        String[] version2Array = v2.split("[._]");
        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        long diff = 0;

        while (index < minLen
                && (diff = Long.parseLong(version1Array[index])
                - Long.parseLong(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Long.parseLong(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Long.parseLong(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    /**
     * 判断是否超过24小时
     *
     * @param time1
     * @param time2
     * @return boolean
     * @throws Exception
     */

    public static boolean judgmentDate(long time1, long time2) {

        long diff = Math.abs(time1 - time2);  // 计算时间差
        long hours = diff / (60 * 60 * 1000);  // 从毫秒转换为小时
        // 时间差超过 24 小时
        // 时间差未超过 24 小时
        return hours >= 24;
    }

    /**
     * MD5校验
     *
     * @param targetFile 要校验md5的文件
     * @return 文件的md5
     */
    public static String getFileMd5(File targetFile) {
        if (targetFile == null || !targetFile.isFile()) {
            return null;
        }
        MessageDigest digest;
        FileInputStream fis = null;
        byte[] buffer = new byte[1024];
        try {
            digest = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(targetFile);
            int bufferLen;
            while ((bufferLen = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bufferLen);
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        byte[] result = digest.digest();
        BigInteger bigInteger = new BigInteger(1, result);
        return String.format("%032x", bigInteger);
    }

    /**
     * 安装apk
     *
     * @param activity
     * @param apkFile
     */
    @SuppressLint({"SetWorldReadable", "SetWorldWritable"})
    public static void installApk(FragmentActivity activity, File apkFile) {
        //文件有所有者概念，现在是属于当前进程的，需要把这个文件暴露给系统安装程序（其他进程）去安装
        //因此，可能会存在权限问题，需要做下面的设置
        //如果文件是sdcard上的，就不需要这个操作了
        try {
            boolean b = apkFile.setExecutable(true, false);
            boolean b1 = apkFile.setReadable(true, false);
            boolean b2 = apkFile.setWritable(true, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri;

        // N FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(apkFile);
        }

        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        activity.startActivity(intent);

        // 0 INSTALL PERMISSION
        //在AndroidManifest中加入权限即可
    }
}