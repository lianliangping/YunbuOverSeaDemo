package com.yunbu.apptest.constants;

import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 此类定义了一些常量和静态方法
 */
public class Constants {

    public static final String TAG = "YunBuGame";
    //Ad SDK Type
    public static  final int SdkType_AdMopub = 1;
    public static  final int SdkType_AnaAdjust = 2;
    public static  final int SdkType_AnaGA = 3;
    public static  final int SdkType_Facebook_Login = 4;
    public static  final int SdkType_Facebook_Share = 5;
    public static  final int SdkType_Purshase = 6;

    //Unity
    public static final String Unity_GameId = "3081743";
    public static final String Unity_Rewarded_PlacementId = "Mopub_Rewardedvideo_02";
    public static final String Unity_Banner_PlacementId = "banner";
    public static final String Unity_Base_PlacementId = "video";

    /**
     * 测试Id
     * banner:b195f8dd8ded45fe847ad89ed1d016da
     * interstitial:24534e1901884e398f1253216226017e
     * RewardedVideo:920b6145fb1546cf8b5cf2ac34638bb7
     * Native:11a17b188668469fb0412708c3d16813
     *
     * 横幅：a3cf95ffb4d1425fa92e84a5fd1520d0
     * 插屏：918713d8acc741a2a94207b51b2edf3f
     * 激励：809cb952894b4c54af6a2fb1ad4ada59
     * 原生：0322ada99be24e5a96bf497d4d09307a
     */
    public static final String Mopub_AdUnitId_Banner = "a3cf95ffb4d1425fa92e84a5fd1520d0";
    public static final String Mopub_AdUnitId_Interstitial = "918713d8acc741a2a94207b51b2edf3f";
    public static final String Mopub_AdUnitId_RewardedVideo = "809cb952894b4c54af6a2fb1ad4ada59";
    public static final String Mopub_AdUnitId_Native = "0322ada99be24e5a96bf497d4d09307a";

    /**
     * 获取谷歌原子时间
     * @return
     */
    public static long getStandardTime() {
        long time = getWebsiteDatetime("https://time.google.com");
        Log.i(TAG,"google time = "+time);
        if (time <= 0) {
            time = getWebsiteDatetime("http://www.baidu.com");
            Log.i(TAG,"baidu time = "+time);
        }
        return time;
    }

    private static long getWebsiteDatetime(String webUrl) {
        try {
            URL url = new URL(webUrl);
            URLConnection uc = url.openConnection();
            uc.connect();
            return uc.getDate();
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
        }
        return 0;
    }

}
