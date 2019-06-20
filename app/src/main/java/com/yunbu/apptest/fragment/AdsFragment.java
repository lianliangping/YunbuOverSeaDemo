package com.yunbu.apptest.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mopub.common.MoPubReward;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubRewardedVideoListener;
import com.mopub.mobileads.MoPubRewardedVideoManager;
import com.mopub.mobileads.MoPubRewardedVideos;
import com.mopub.mobileads.MoPubView;
import com.mopub.nativeads.AdapterHelper;
import com.mopub.nativeads.FacebookAdRenderer;
import com.mopub.nativeads.GooglePlayServicesAdRenderer;
import com.mopub.nativeads.MediaViewBinder;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.ViewBinder;
import com.yunbu.apptest.R;
import com.yunbu.apptest.constants.Constants;

import java.util.Set;

/**
 * 广告页面
 */
public class AdsFragment extends Fragment implements View.OnClickListener {

    private Button btn_interstitial,btn_rewardVideo,btn_native,btn_banner,btn_hide_native,btn_request_native,btn_hide_banner;
    private LinearLayout ll_ads_view;

    private MoPubView moPubView;//banner
    private MoPubInterstitial interstitial;//插屏
    private MoPubNative moPubNative;//原生广告
    private NativeAd mNativeAd = null;//原生广告内容对象
    private RelativeLayout parentView;//原生广告容器
    private boolean isNativeAdLoaded = false;//原生广告是否加载完成
    private  AdapterHelper adapterHelper = null;//原生广告适配器

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ads_mopub,container,false);
        initView(view);
        initEvent();//第一次加载广告
        return view;
    }

    private void initView(View view){
        btn_interstitial = view.findViewById(R.id.btn_interstitial);
        btn_rewardVideo = view.findViewById(R.id.btn_rewardVideo);
        btn_native = view.findViewById(R.id.btn_native);
        btn_banner = view.findViewById(R.id.btn_banner);
        btn_hide_native = view.findViewById(R.id.btn_hide_native);
        btn_request_native = view.findViewById(R.id.btn_request_native);
        btn_hide_banner = view.findViewById(R.id.btn_hide_banner);
        ll_ads_view = view.findViewById(R.id.ll_ads_view);

        parentView = view.findViewById(getActivity().getResources().getIdentifier("native_container","id",getActivity().getPackageName()));

        //moPubView = view.findViewById(R.id.adview);
        moPubView = new MoPubView(getActivity());//动态生成banner视图
    }


    private void initEvent(){
        btn_interstitial.setOnClickListener(this);
        btn_rewardVideo.setOnClickListener(this);
        btn_native.setOnClickListener(this);
        btn_banner.setOnClickListener(this);
        btn_hide_native.setOnClickListener(this);
        btn_request_native.setOnClickListener(this);
        btn_hide_banner.setOnClickListener(this);

        initAndLoadInterstitial();//加载插屏
        initAndLoadRewardVedio();//加载激励视频
        initAndLoadNative();//加载原生广告
        initBanner();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_interstitial:
                if(interstitial.isReady()){
                    interstitial.show();
                }else{
                    showToast();
                }
                break;
            case R.id.btn_rewardVideo:
                if(MoPubRewardedVideos.hasRewardedVideo(Constants.Mopub_AdUnitId_RewardedVideo)){
                    MoPubRewardedVideos.showRewardedVideo(Constants.Mopub_AdUnitId_RewardedVideo);
                }else{
                    showToast();
                }
                break;
            case R.id.btn_native:
                if(isNativeAdLoaded){
                    View v1 = adapterHelper.getAdView(null,parentView, mNativeAd, new ViewBinder.Builder(0).build());

                    parentView.addView(v1);
                    parentView.setVisibility(View.VISIBLE);
                }else{
                    showToast();
                }
                break;
            case R.id.btn_hide_native:
                Log.d(Constants.TAG,"native hide");
                parentView.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_request_native:
                moPubNative.makeRequest();
                break;
            case R.id.btn_banner:
                showBanner();
                break;
            case R.id.btn_hide_banner:
                moPubView.setVisibility(View.INVISIBLE);
                break;
            default:

                break;
        }
    }

    private void showToast(){
        Toast.makeText(getActivity(),"广告还未加载完成请稍后再试",Toast.LENGTH_SHORT).show();
    }


    private void initBanner(){
        moPubView.setAdUnitId(Constants.Mopub_AdUnitId_Banner); // Enter your Ad Unit ID from www.mopub.com
        moPubView.setBannerAdListener(new MoPubView.BannerAdListener() {
            @Override
            public void onBannerLoaded(MoPubView banner) {
                Log.d(Constants.TAG,"onBannerLoaded,bannerId = "+banner.getAdUnitId());

            }

            @Override
            public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
                Log.d(Constants.TAG,"onBannerFailed,errorCode = "+errorCode.toString());
            }

            @Override
            public void onBannerClicked(MoPubView banner) {
                Log.d(Constants.TAG,"onBannerClicked,bannerId = "+banner.getAdUnitId());
            }

            @Override
            public void onBannerExpanded(MoPubView banner) {
                Log.d(Constants.TAG,"onBannerExpanded,bannerId = "+banner.getAdUnitId());
            }

            @Override
            public void onBannerCollapsed(MoPubView banner) {
                Log.d(Constants.TAG,"onBannerCollapsed");
            }
        });
        //控制banner显示位置
        ViewGroup viewGroup = (ViewGroup) getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,dip2px(getActivity(),50));//banner高度为50dp，宽度为充满屏幕
        layoutParams1.gravity = Gravity.BOTTOM;//显示在屏幕底部
        layoutParams1.setMargins(0,0,0,dip2px(getActivity(),30));//距离底部30dp
        FrameLayout banner_container = new FrameLayout(getActivity());
        viewGroup.addView(banner_container,layoutParams1);
        banner_container.addView(moPubView);
        moPubView.setClickable(false);


    }

    /**
     * 加载并展示banner
     */
    private void showBanner(){
        moPubView.setVisibility(View.VISIBLE);
        moPubView.loadAd();
    }

    /**
     * 加载插屏
     */
    private void initAndLoadInterstitial(){
        //插屏
        interstitial = new MoPubInterstitial(getActivity(),Constants.Mopub_AdUnitId_Interstitial);
        interstitial.setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
            @Override
            public void onInterstitialLoaded(MoPubInterstitial interstitial) {
                Log.d(Constants.TAG,"onInterstitialLoaded");
            }

            @Override
            public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
                Log.d(Constants.TAG,"onInterstitialFailed,errorCode = "+errorCode.toString());
            }

            @Override
            public void onInterstitialShown(MoPubInterstitial interstitial1) {
                Log.d(Constants.TAG,"onInterstitialShown");

            }

            @Override
            public void onInterstitialClicked(MoPubInterstitial interstitial) {
                Log.d(Constants.TAG,"onInterstitialClicked");
            }

            @Override
            public void onInterstitialDismissed(MoPubInterstitial interstitial1) {
                Log.d(Constants.TAG,"onInterstitialDismissed");
                interstitial.load();//插屏播放完成加载下一个插屏
            }
        });
        interstitial.load();//加载插屏
    }

    /**
     * 加载激励视频
     */
    private void initAndLoadRewardVedio(){
        //激励视频
        MoPubRewardedVideoManager.init(getActivity());//在Application中初始化需要加这行代码，在Activity中初始化请忽略

        MoPubRewardedVideos.setRewardedVideoListener(new MoPubRewardedVideoListener() {
            @Override
            public void onRewardedVideoLoadSuccess(@NonNull String adUnitId) {
                Log.d(Constants.TAG,"onRewardedVideoLoadSuccess");
            }

            @Override
            public void onRewardedVideoLoadFailure(@NonNull String adUnitId, @NonNull MoPubErrorCode errorCode) {
                Log.d(Constants.TAG,"onRewardedVideoLoadFailure!errorCode = "+errorCode.toString());
            }

            @Override
            public void onRewardedVideoStarted(@NonNull String adUnitId) {
                Log.d(Constants.TAG,"onRewardedVideoStarted");

            }

            @Override
            public void onRewardedVideoPlaybackError(@NonNull String adUnitId, @NonNull MoPubErrorCode errorCode) {
                Log.d(Constants.TAG,"onRewardedVideoPlaybackError!"+errorCode.toString());
            }

            @Override
            public void onRewardedVideoClicked(@NonNull String adUnitId) {
                Log.d(Constants.TAG,"onRewardedVideoClicked");
            }

            @Override
            public void onRewardedVideoClosed(@NonNull String adUnitId) {
                Log.d(Constants.TAG,"onRewardedVideoClosed");

            }

            @Override
            public void onRewardedVideoCompleted(@NonNull Set<String> adUnitIds, @NonNull MoPubReward reward) {
                Log.d(Constants.TAG,"onRewardedVideoCompleted");
                MoPubRewardedVideos.loadRewardedVideo(Constants.Mopub_AdUnitId_RewardedVideo);//激励视频播放完成后加载下一个激励视频
            }
        });
        MoPubRewardedVideos.loadRewardedVideo(Constants.Mopub_AdUnitId_RewardedVideo);
    }

    //加载原生广告
    private void initAndLoadNative(){
        //设置原生广告显示大小和位置
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) parentView.getLayoutParams();
        layoutParams.setMargins(dip2px(getActivity(),20),0,dip2px(getActivity(),20),0);
        WindowManager vm = getActivity().getWindowManager();
        int nativeWidth = vm.getDefaultDisplay().getWidth();//获取屏幕宽度
        int nativeHeight = vm.getDefaultDisplay().getHeight();//获取屏幕高度
        layoutParams.width = (int) (0.6*nativeWidth);//设置原生广告宽度
        layoutParams.height = (int) (0.25*nativeHeight);//设置原生广告高度
        parentView.setLayoutParams(layoutParams);


        moPubNative = new MoPubNative(getActivity(), Constants.Mopub_AdUnitId_Native, new MoPubNative.MoPubNativeNetworkListener() {
            @Override
            public void onNativeLoad(NativeAd nativeAd) {
                Log.d(Constants.TAG, "Native ad onNativeLoad");
                mNativeAd = nativeAd;
                isNativeAdLoaded = true;
                nativeAd.setMoPubNativeEventListener(new NativeAd.MoPubNativeEventListener() {
                    @Override
                    public void onImpression(View view) {
                        Log.d(Constants.TAG,"Native onImpression");
                    }

                    @Override
                    public void onClick(View view) {
                        Log.d(Constants.TAG,"Native onClick");
                    }
                });

                /*if(adapterHelper != null){
                    adapterHelper.getAdView(null,parentView,mNativeAd);

                }*/
            }

            @Override
            public void onNativeFail(NativeErrorCode errorCode) {
                Log.d(Constants.TAG, "Native ad onNativeFail!errorCode = "+errorCode.toString());
                //moPubNative.makeRequest();
            }
        });
        int layoutId = getActivity().getResources().getIdentifier("native_ad_list_item","layout",getActivity().getPackageName());
        int mainImageId = getActivity().getResources().getIdentifier("native_main_image","id",getActivity().getPackageName());
        int iconImageId = getActivity().getResources().getIdentifier("native_icon_image","id",getActivity().getPackageName());
        int titleId = getActivity().getResources().getIdentifier("native_title","id",getActivity().getPackageName());
        int textId = getActivity().getResources().getIdentifier("native_text","id",getActivity().getPackageName());
        int privacyInformationIconImageId = getActivity().getResources().getIdentifier("native_privacy_information_icon_image","id",getActivity().getPackageName());


        ViewBinder viewBinder  = new ViewBinder.Builder(layoutId)
                .mainImageId(mainImageId)
                .iconImageId(iconImageId)
                .titleId(titleId)
                .textId(textId)
                .privacyInformationIconImageId(privacyInformationIconImageId)
                .callToActionId(R.id.native_cta)
                .build();
       /* ViewBinder viewBinder  = new ViewBinder.Builder(R.layout.native_ad_list_item)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();*/
        // AdMob
        final GooglePlayServicesAdRenderer googlePlayServicesAdRenderer = new GooglePlayServicesAdRenderer(
                new MediaViewBinder.Builder(R.layout.admob_native)
                        .mediaLayoutId(R.id.admob_media) // bind to your `com.mopub.nativeads.MediaLayout` element
                        .iconImageId(R.id.admob_native_icon_image)
                        .titleId(R.id.admob_native_title)
                        .textId(R.id.admob_native_text)
                        .callToActionId(R.id.admob_native_cta)
                        .privacyInformationIconImageId(R.id.admob_native_privacy_information_icon_image)
                        .build());

        //Facebook
        FacebookAdRenderer facebookAdRenderer = new FacebookAdRenderer(
                new FacebookAdRenderer.FacebookViewBinder.Builder(R.layout.facebook_native)
                        .titleId(R.id.facebook_native_title)
                        .textId(R.id.facebook_native_text)
                        // Binding to new layouts from Facebook 4.99.0+
                        .mediaViewId(R.id.facebook_media)
                        .adIconViewId(R.id.facebook_native_icon_image)
                        .adChoicesRelativeLayoutId(R.id.native_ad_choices_relative_layout)
                        .advertiserNameId(R.id.facebook_native_title) // Bind either the titleId or advertiserNameId depending on the FB SDK version
                        // End of binding to new layouts
                        .callToActionId(R.id.facebook_native_cta)
                        .build());
        MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);
        moPubNative.registerAdRenderer(moPubStaticNativeAdRenderer);
        moPubNative.registerAdRenderer(googlePlayServicesAdRenderer);
        moPubNative.registerAdRenderer(facebookAdRenderer);
        moPubNative.makeRequest();//请求广告
        //渲染原生广告适配器
        adapterHelper = new AdapterHelper(getActivity(),0,3);
    }

    /**
     * dp转px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onDestroy() {
        moPubView.destroy();
        interstitial.destroy();
        moPubNative.destroy();
        super.onDestroy();
    }

}
