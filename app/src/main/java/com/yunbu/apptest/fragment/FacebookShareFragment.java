package com.yunbu.apptest.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.yunbu.apptest.R;
import com.yunbu.apptest.constants.Constants;

public class FacebookShareFragment extends Fragment implements View.OnClickListener {

    private ShareButton btn_facebook_share_link;
    //private ShareDialog shareDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facebook_share,container,false);

        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view){
        btn_facebook_share_link = view.findViewById(R.id.btn_facebook_share_link);
        //shareDialog = new ShareDialog(getActivity());
    }

    private void initEvent(){
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();
        btn_facebook_share_link.setShareContent(content);
        CallbackManager callbackManager = CallbackManager.Factory.create();
        btn_facebook_share_link.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d(Constants.TAG,"Sharer Success!result = "+result.toString());
                Toast.makeText(getActivity(),"Sharer success!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Log.d(Constants.TAG,"Sharer onCancel! ");
                Toast.makeText(getActivity(),"Sharer onCancel!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(Constants.TAG,"Sharer onError!error = "+error.toString());
                Toast.makeText(getActivity(),"Sharer onError!",Toast.LENGTH_SHORT).show();
            }
        });
        /*shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d(Constants.TAG,"Sharer Success!result = "+result.toString());
                Toast.makeText(getActivity(),"Sharer success!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Log.d(Constants.TAG,"Sharer onCancel! ");
                Toast.makeText(getActivity(),"Sharer onCancel!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(Constants.TAG,"Sharer onError!error = "+error.toString());
                Toast.makeText(getActivity(),"Sharer onError!",Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_facebook_share_link:
                Log.d(Constants.TAG,"click111111111111");
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://developers.facebook.com"))
                        .build();
                //shareDialog.show(content);
                break;
            default:

                break;
        }
    }
}
