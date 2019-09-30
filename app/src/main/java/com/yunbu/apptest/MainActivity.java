package com.yunbu.apptest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.yunbu.apptest.activity.SdkModuleActivity;
import com.yunbu.apptest.constants.Constants;

/**
 * 主界面，在这个页面可以选择不同的SDK界面
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_ads_heyzap, btn_ana_adjust, btn_ana_ga, btn_facebook_login, btn_facebook_share, btn_purchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.yunbu.apptest",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }*/
        initView();
        initEvent();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Constants.getStandardTime();
            }
        }).start();
    }

    private void initView() {
        btn_ads_heyzap = findViewById(R.id.btn_ads_heyzap);
        btn_ana_adjust = findViewById(R.id.btn_ana_adjust);
        btn_ana_ga = findViewById(R.id.btn_ana_ga);
        btn_facebook_login = findViewById(R.id.btn_facebook_login);
        btn_facebook_share = findViewById(R.id.btn_facebook_share);
        btn_purchase = findViewById(R.id.btn_purchase);

    }

    private void initEvent() {
        btn_ads_heyzap.setOnClickListener(this);
        btn_ana_adjust.setOnClickListener(this);
        btn_ana_ga.setOnClickListener(this);
        btn_facebook_login.setOnClickListener(this);
        btn_facebook_share.setOnClickListener(this);
        btn_purchase.setOnClickListener(this);
    }


   /* @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!isLoad){
            isLoad = true;
            //mGame.SetMoPubView(moPubView);
        }
    }*/



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ads_heyzap: turnToSdkModule(Constants.SdkType_AdMopub);

                break;
            case R.id.btn_ana_adjust:
                turnToSdkModule(Constants.SdkType_AnaAdjust);
                break;
            case R.id.btn_ana_ga:
                turnToSdkModule(Constants.SdkType_AnaGA);
                break;
            case R.id.btn_facebook_login:
                turnToSdkModule(Constants.SdkType_Facebook_Login);
                break;
            case R.id.btn_facebook_share:
                turnToSdkModule(Constants.SdkType_Facebook_Share);
                break;
            case R.id.btn_purchase:
                turnToSdkModule(Constants.SdkType_Purshase);
                break;
            default:

                break;
        }
    }

    private void turnToSdkModule(int sdkType) {
        Intent intent = new Intent(MainActivity.this, SdkModuleActivity.class);
        intent.putExtra("sdkType", sdkType);
        startActivity(intent);
    }
}
