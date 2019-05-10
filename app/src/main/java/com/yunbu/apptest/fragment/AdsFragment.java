package com.yunbu.apptest.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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

public class AdsFragment extends Fragment implements View.OnClickListener {

    private Button btn_interstitial,btn_rewardVideo,btn_native,btn_banner;
    private MoPubView moPubView;
    private MoPubInterstitial interstitial;
    private MoPubNative moPubNative;
    private NativeAd mNativeAd = null;
    private RelativeLayout parentView;//原生广告容器
    private boolean isNativeAdLoaded = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ads_mopub,container,false);
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view){
        btn_interstitial = view.findViewById(R.id.btn_interstitial);
        btn_rewardVideo = view.findViewById(R.id.btn_rewardVideo);
        btn_native = view.findViewById(R.id.btn_native);
        btn_banner = view.findViewById(R.id.btn_banner);

        parentView = view.findViewById(R.id.native_container);

        //moPubView = view.findViewById(R.id.adview);
        moPubView = new MoPubView(getActivity());//动态生成banner视图


    }

    private void initEvent(){
        btn_interstitial.setOnClickListener(this);
        btn_rewardVideo.setOnClickListener(this);
        btn_native.setOnClickListener(this);
        btn_banner.setOnClickListener(this);

        loadInterstitial();//加载插屏
        loadRewardVedio();//加载激励视频
        loadNative();//加载原生广告
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

                    AdapterHelper adapterHelper = new AdapterHelper(getActivity(),0,3);
                    View v1 = adapterHelper.getAdView(null, parentView, mNativeAd, new ViewBinder.Builder(0).build());
                    mNativeAd.setMoPubNativeEventListener(new NativeAd.MoPubNativeEventListener() {
                        @Override
                        public void onImpression(View view) {
                            Log.d(Constants.TAG,"onImpression");
                        }

                        @Override
                        public void onClick(View view) {
                            Log.d(Constants.TAG,"onClick");
                        }
                    });
                    parentView.addView(v1);
                }else{
                    showToast();
                }
                break;
            case R.id.btn_banner:
                showBanner();
                /*Intent intent = new Intent(getActivity(),TestActivity.class);
                getActivity().startActivity(intent);*/
                break;
            default:

                break;
        }
    }

    private void showToast(){
        Toast.makeText(getActivity(),"广告还未加载完成请稍后再试",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        moPubView.destroy();
        interstitial.destroy();
        moPubNative.destroy();
        super.onDestroy();
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

        ViewGroup viewGroup = (ViewGroup) getActivity().getWindow().getDecorView();
        viewGroup.addView(moPubView);
        FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams1.gravity = Gravity.BOTTOM;
        layoutParams1.setMargins(0,0,0,30);
        moPubView.setLayoutParams(layoutParams1);
    }

    private void showBanner(){


        moPubView.loadAd();
    }

    private void loadInterstitial(){
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
                interstitial.load();
            }
        });
        interstitial.load();
    }

    private void loadRewardVedio(){
        //激励视频
        MoPubRewardedVideoManager.init(getActivity());

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
                MoPubRewardedVideos.loadRewardedVideo(Constants.Mopub_AdUnitId_RewardedVideo);
            }

            @Override
            public void onRewardedVideoCompleted(@NonNull Set<String> adUnitIds, @NonNull MoPubReward reward) {
                Log.d(Constants.TAG,"onRewardedVideoCompleted");
            }
        });
        MoPubRewardedVideos.loadRewardedVideo(Constants.Mopub_AdUnitId_RewardedVideo);
    }

    private void loadNative(){
        //原生
        moPubNative = new MoPubNative(getActivity(), Constants.Mopub_AdUnitId_Native, new MoPubNative.MoPubNativeNetworkListener() {
            @Override
            public void onNativeLoad(NativeAd nativeAd) {
                Log.d(Constants.TAG, "Native ad onNativeLoad");
                mNativeAd = nativeAd;
                isNativeAdLoaded = true;
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
        moPubNative.makeRequest();
    }
}
