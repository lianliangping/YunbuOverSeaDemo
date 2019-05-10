package com.yunbu.apptest.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gameanalytics.sdk.GAErrorSeverity;
import com.gameanalytics.sdk.GAProgressionStatus;
import com.gameanalytics.sdk.GAResourceFlowType;
import com.gameanalytics.sdk.GameAnalytics;
import com.yunbu.apptest.R;

public class GameAnaluticsFragment extends Fragment implements View.OnClickListener {

        private Button btn_business,btn_resource_source,btn_resource_sink,btn_progression,btn_error,btn_design;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics_ga,container,false);
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view){
        btn_business = view.findViewById(R.id.btn_ga_business);
        btn_resource_source = view.findViewById(R.id.btn_ga_resource_source);
        btn_resource_sink = view.findViewById(R.id.btn_ga_resource_sink);
        btn_progression = view.findViewById(R.id.btn_ga_progression);
        btn_error = view.findViewById(R.id.btn_ga_error);
        btn_design = view.findViewById(R.id.btn_ga_design);
    }

    private void initEvent(){
        btn_business.setOnClickListener(this);
        btn_resource_source.setOnClickListener(this);
        btn_resource_sink.setOnClickListener(this);
        btn_progression.setOnClickListener(this);
        btn_error.setOnClickListener(this);
        btn_design.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ga_business:
                GameAnalytics.addBusinessEventWithCurrency("USD",1000,"crystal","100crystal","shop");
                break;
            case R.id.btn_ga_resource_source:
                GameAnalytics.addResourceEventWithFlowType(GAResourceFlowType.Source,"gold", (float) 6.00,"reward","videoAdReward");
                break;
            case R.id.btn_ga_resource_sink:
                GameAnalytics.addResourceEventWithFlowType(GAResourceFlowType.Sink,"gold", (float) 6.00,"AscendingOrder","FiveAscendingOrder");
                break;
            case R.id.btn_ga_progression:
                GameAnalytics.addProgressionEventWithProgressionStatus(GAProgressionStatus.Start,"10level");
                break;
            case R.id.btn_ga_error:
                GameAnalytics.addErrorEventWithSeverity(GAErrorSeverity.Debug,"something went bad in some of the smelly code!");
                break;
            case R.id.btn_ga_design:
                GameAnalytics.addDesignEventWithEventId("newUserTutorial:namedCharacter:complete");
                break;
            default:

                break;
        }
    }


}
