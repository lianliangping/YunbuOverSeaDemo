package com.yunbu.apptest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.gameanalytics.sdk.GameAnalytics;
import com.yunbu.apptest.R;
import com.yunbu.apptest.constants.Constants;
import com.yunbu.apptest.fragment.AdjustAnalyticsFragment;
import com.yunbu.apptest.fragment.AdsFragment;
import com.yunbu.apptest.fragment.FacebookLoginFragment;
import com.yunbu.apptest.fragment.FacebookShareFragment;
import com.yunbu.apptest.fragment.GameAnaluticsFragment;
import com.yunbu.apptest.fragment.PurchaseFragment;

public class SdkModuleActivity extends AppCompatActivity {

    //private VerifyPurchaseUtil verifyPurchaseUtil;
    //private GoogleBillingUtil googleBillingUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdk_module);
        initView();
        initEvent();
        initSDK();
        switchSdkType();


    }


    private void switchSdkType(){
        Intent intent = getIntent();
        int sdkType = intent.getIntExtra("sdkType",-1);
        Fragment fragment = null;
        switch (sdkType){
            case Constants.SdkType_AdMopub:
                fragment = new AdsFragment();
                break;
            case Constants.SdkType_AnaAdjust:
                fragment = new AdjustAnalyticsFragment();
                break;
            case Constants.SdkType_AnaGA:
                fragment = new GameAnaluticsFragment();
                break;
            case Constants.SdkType_Facebook_Login:
                fragment = new FacebookLoginFragment();
                break;
            case Constants.SdkType_Facebook_Share:
                fragment = new FacebookShareFragment();
                break;
            case Constants.SdkType_Purshase:
                fragment = new PurchaseFragment();
                break;
            default:

                break;
        }
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragment).commit();
    }

    private void initView(){

    }

    private void initEvent(){

    }



    private void initSDK(){

        //Purchase
        /*verifyPurchaseUtil = VerifyPurchaseUtil.getInstance()
                .setMaxVerifyTime(15) // 设置等待内购验证结果回调的最大时限,单位：秒 (不设置,默认10秒)
                .setOnVerifyPurchaseListener(onVerifyPurchaseListener) // 设置内购订单验证结果监听器
                .build(SdkModuleActivity.this); // 初始化配置
        googleBillingUtil = GoogleBillingUtil.getInstance()
                .setDebugAble(true) // 设置是否打开Debug查看日志，默认关闭 (Release版中一定要关闭)，日志过滤标记 ‘SDK_YiFans’
                .setInAppSKUS(inAppSKUS) // 设置内购sku ID集
                .setSubsSKUS(subsSKUS) // 设置订阅sku ID集
                .setAutoConsumeAsync(mAutoRenewEnabled) // 设置是否购买后自动消耗商品 【此接口的最佳使用方式参考下方接口说明】
                .setOnStartSetupFinishedListener(onStartSetupFinishedListener) // 设置初始化启动监听器
                .setOnQueryFinishedListener(onQueryFinishedListener) // 设置查询商品与服务详情信息结果监听器
                .setOnQueryUnConsumeOrderListener(onQueryUnConsumeOrderListener) // 查询已购买的且未被消耗的商品监听器
                .setOnPurchaseFinishedListener(onPurchaseFinishedListener) // 设置内购结果监听器
                .setOnConsumeFinishedListener(onConsumeFinishedListener) // 设置商品消耗监听器
                .setOnQueryHistoryQurchaseListener(onQueryHistoryQurchaseListener) // 查询当前Google账户在本App中对每个产品ID的最新'购买'与'订阅'的监听器
                .build(TestActivity.this); // 初始化配置*/

        //GameAnalytics
        GameAnalytics.setEnabledInfoLog(true);
        GameAnalytics.setEnabledVerboseLog(true);
        GameAnalytics.configureBuild("1.0.0");
        GameAnalytics.initializeWithGameKey(SdkModuleActivity.this,"88b4e4ea3536b77c5c28dac3c67001d2","6480d3527662d524c75b6204a6682f4219f2fbc6");


    }
}
