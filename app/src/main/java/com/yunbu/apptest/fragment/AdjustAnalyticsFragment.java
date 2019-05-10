package com.yunbu.apptest.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.yunbu.apptest.R;

public class AdjustAnalyticsFragment extends Fragment implements View.OnClickListener {

    private Button btn_event,btn_purchase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics_adjust,container,false);
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view){

        btn_event = view.findViewById(R.id.btn_adjust_event);
        btn_purchase = view.findViewById(R.id.btn_adjus_purchase);

    }

    private void initEvent(){
        btn_event.setOnClickListener(this);
        btn_purchase.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_adjust_event:
                AdjustEvent adjustEvent = new AdjustEvent("fdc6pg");
                Adjust.trackEvent(adjustEvent);
                break;
            case R.id.btn_adjus_purchase:
                AdjustEvent adjustEvent1 = new AdjustEvent("nclhac");
                adjustEvent1.setRevenue(6.00,"USD");
                Adjust.trackEvent(adjustEvent1);
                break;
            default:

                break;
        }
    }
}
