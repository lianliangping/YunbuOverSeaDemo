package com.yunbu.apptest;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEventFailure;
import com.adjust.sdk.AdjustEventSuccess;
import com.adjust.sdk.AdjustSessionFailure;
import com.adjust.sdk.AdjustSessionSuccess;
import com.adjust.sdk.OnEventTrackingFailedListener;
import com.adjust.sdk.OnEventTrackingSucceededListener;
import com.adjust.sdk.OnSessionTrackingFailedListener;
import com.adjust.sdk.OnSessionTrackingSucceededListener;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.common.logging.MoPubLog;
import com.mopub.mobileads.GooglePlayServicesBanner;
import com.mopub.mobileads.GooglePlayServicesInterstitial;
import com.mopub.mobileads.GooglePlayServicesRewardedVideo;
import com.mopub.nativeads.GooglePlayServicesNative;
import com.onesignal.OneSignal;
import com.yunbu.apptest.constants.Constants;

public class ExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Adjust初始化
        String appToken = "43xexhzps6o0";
        String environment = AdjustConfig.ENVIRONMENT_SANDBOX;
        AdjustConfig config = new AdjustConfig(this, appToken, environment);
        config.setOnEventTrackingSucceededListener(new OnEventTrackingSucceededListener() {
            @Override
            public void onFinishedEventTrackingSucceeded(AdjustEventSuccess eventSuccessResponseData) {
                Log.d(Constants.TAG,"onFinishedEventTrackingSucceeded:"+eventSuccessResponseData.message);
                Toast.makeText(getApplicationContext(),"onFinishedEventTrackingSucceeded",Toast.LENGTH_LONG).show();
            }
        });
        config.setOnEventTrackingFailedListener(new OnEventTrackingFailedListener() {
            @Override
            public void onFinishedEventTrackingFailed(AdjustEventFailure eventFailureResponseData) {
                Log.d(Constants.TAG,"onFinishedEventTrackingFailed:"+eventFailureResponseData.message+";data = "+eventFailureResponseData.jsonResponse);
                Toast.makeText(getApplicationContext(),"onFinishedEventTrackingFailed",Toast.LENGTH_LONG).show();
            }
        });
        config.setOnSessionTrackingSucceededListener(new OnSessionTrackingSucceededListener() {
            @Override
            public void onFinishedSessionTrackingSucceeded(AdjustSessionSuccess sessionSuccessResponseData) {
                Log.d(Constants.TAG,"onFinishedSessionTrackingSucceeded:"+sessionSuccessResponseData.message);
                Toast.makeText(getApplicationContext(),"onFinishedSessionTrackingSucceeded",Toast.LENGTH_LONG).show();
            }
        });
        config.setOnSessionTrackingFailedListener(new OnSessionTrackingFailedListener() {
            @Override
            public void onFinishedSessionTrackingFailed(AdjustSessionFailure failureResponseData) {
                Log.d(Constants.TAG,"onFinishedSessionTrackingFailed:"+failureResponseData.message+";data = "+failureResponseData.jsonResponse);
                Toast.makeText(getApplicationContext(),"onFinishedSessionTrackingFailed",Toast.LENGTH_LONG).show();
            }
        });
        Adjust.onCreate(config);
        registerActivityLifecycleCallbacks(new AdjustLifecycleCallbacks());

        //Mopub init
        Bundle extras = new Bundle();
        extras.putString("npa", "1");
        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder(Constants.Mopub_AdUnitId_Banner)
                .withLogLevel(MoPubLog.LogLevel.DEBUG)
                .withLegitimateInterestAllowed(false)
                .withMediationSettings(new GooglePlayServicesBanner.GooglePlayServicesMediationSettings(extras),
                        new GooglePlayServicesInterstitial.GooglePlayServicesMediationSettings(extras),
                        new GooglePlayServicesRewardedVideo.GooglePlayServicesMediationSettings(extras),
                        new GooglePlayServicesNative.GooglePlayServicesMediationSettings(extras))
                .build();
        MoPub.initializeSdk(this, sdkConfiguration, new SdkInitializationListener() {
            @Override
            public void onInitializationFinished() {
                Log.d(Constants.TAG,"Mopub  init finish!");
                Toast.makeText(getApplicationContext(),"Mopub  init finish!",Toast.LENGTH_LONG).show();
            }
        });



        // OneSignal Initialization
        //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG,OneSignal.LOG_LEVEL.DEBUG);
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


    }
    private static final class AdjustLifecycleCallbacks implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            Adjust.onResume();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Adjust.onPause();
        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }

        //...
    }
}
