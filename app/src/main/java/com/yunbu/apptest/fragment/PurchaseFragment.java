package com.yunbu.apptest.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.google.android.exoplayer2.util.Log;
import com.yifants.sdk.purchase.GoogleBillingUtil;
import com.yifants.sdk.purchase.GooglePurchase;
import com.yifants.sdk.purchase.VerifyPurchaseUtil;
import com.yunbu.apptest.R;

import java.util.List;
import java.util.Random;

public class PurchaseFragment extends Fragment implements View.OnClickListener, VerifyPurchaseUtil.OnVerifyPurchaseListener, GoogleBillingUtil.OnStartSetupFinishedListener, GoogleBillingUtil.OnQueryFinishedListener, GoogleBillingUtil.OnQueryUnConsumeOrderListener, GoogleBillingUtil.OnPurchaseFinishedListener, GoogleBillingUtil.OnConsumeFinishedListener, GoogleBillingUtil.OnQueryHistoryQurchaseListener {

    private final String TAG = "YunBu_PurchaseFragment";
    static final String SKU_COIN1 = "com.yunbu.apptest.100coins";
    static final String SKU_COIN2 = "com.yunbu.apptest.test";
    static final String SKU_COIN3 = "com.yunbu.apptest.200coins";
    /*static final String SKU_COIN4 = "g7_cpba1711005.coins.1999";
    static final String SKU_COIN5 = "g7_cpba1711005.coins.4999";
    static final String SKU_COIN6 = "g7_cpba1711005.coins.9999";

    static final String SKU_PACK1 = "g7_cpba1711005.starterpack.0099";
    static final String SKU_PACK2 = "g7_cpba1711005.salespack1.0499";
    static final String SKU_PACK3 = "g7_cpba1711005.salespack2.1999";
    static final String SKU_PACK4 = "g7_cpba1711005.salespack3.4999";*/

    private String[] inAppSKUS = new String[]{SKU_COIN1, SKU_COIN2, SKU_COIN3};
    boolean mAutoRenewEnabled = true;
    private String[] subsSKUS = new String[]{};//订阅ID
    private List<SkuDetails> list;

    GoogleBillingUtil googleBillingUtil = null;
    VerifyPurchaseUtil verifyPurchaseUtil = null;
    Button btn_buy;
    Button btn_query;

    String sku;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_google,container,false);
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view){

        btn_buy = view.findViewById(R.id.btn_buy);
        btn_query = view.findViewById(R.id.btn_query);

        btn_buy.setOnClickListener(this);
        btn_query.setClickable(false);
        btn_query.setOnClickListener(this);

    }

    private void initEvent(){
        Random random = new Random();
        int pos = random.nextInt(inAppSKUS.length);
        sku = inAppSKUS[pos];

        verifyPurchaseUtil = VerifyPurchaseUtil.getInstance()
                .setMaxVerifyTime(15) // 设置等待内购验证结果回调的最大时限,单位：秒 (不设置,默认10秒)
                .setOnVerifyPurchaseListener(this) // 内购验证监听器
                .build(getActivity()); // 初始化配置

        googleBillingUtil = GoogleBillingUtil.getInstance()
                .setDebugAble(true) // 设置是否打开Debug查看日志，默认关闭 (Release版中一定要关闭)
                .setInAppSKUS(inAppSKUS) // 设置内购ID
                .setSubsSKUS(subsSKUS) // 设置订阅ID
                .setAutoConsumeAsync(mAutoRenewEnabled) // 设置自动消耗商品
                .setOnStartSetupFinishedListener(this) // 启动监听器
                .setOnQueryFinishedListener(this) // 查询已购买的商品监听器
                .setOnQueryUnConsumeOrderListener(this) // 查询已购买的且未被消耗的商品监听器
                .setOnPurchaseFinishedListener(this) // 内购结果监听器
                .setOnConsumeFinishedListener(this) // 消耗商品监听器
                .setOnQueryHistoryQurchaseListener(this) // 查询当前Google账户在本App中对每个产品ID的最新购买与订阅的监听器
                .build(getActivity());//初始化配置
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_buy:
                //内购
                Log.i(TAG, "start purchaseInApp " + sku);
                googleBillingUtil.purchaseInApp(getActivity(), sku);
                break;
            case R.id.btn_query:
                // 查询未被消耗的内购商品，异步结果回调OnQueryUnConsumeOrderListener
                googleBillingUtil.queryUnConsumeOrders(getActivity());
                googleBillingUtil.queryHistoryInApp();
                List<Purchase> purchaseList = googleBillingUtil.queryPurchasesInApp();
                Log.i(TAG, "queryPurchasesInApp purchasesList " + ((purchaseList != null && !purchaseList.isEmpty()) ? purchaseList.size() : " is empty!"));
                break;
            default:

                break;
        }
    }

    @Override
    public void onVerifyFinish(final GooglePurchase order) {
        Log.i(TAG, "onVerifyFinish 订单号：" + order.getOrderId());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "onVerifyFinish 订单号：" + order.getOrderId(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onVerifyError(final int responseCode, final GooglePurchase order) {
        Log.i(TAG, "onVerifyError 订单号：" + order.getOrderId() + "; responseCode: " + responseCode);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "onVerifyError 订单号：" + order.getOrderId() + "; responseCode: " + responseCode, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onSetupSuccess() {
        Log.i(TAG, "onSetupSuccess ");
        btn_query.setClickable(true);
    }

    @Override
    public void onSetupFail(int responseCode) {
        Log.i(TAG, "onSetupFail responseCode= " + responseCode);
    }

    @Override
    public void onSetupError() {
        Log.i(TAG, "onSetupError ");
    }

    @Override
    public void onQuerySuccess(String skuType, List<SkuDetails> list) {
        Log.i(TAG, "onQuerySuccess skuType= " + skuType);
        btn_buy.setClickable(true);
        this.list = list;
        if (list != null && !list.isEmpty()) {
            String skuName = null;
            for (SkuDetails details : list) {
                Log.i(TAG, "onQuerySuccess details: sku= " + details.getSku() + "; type= " + details.getType() + "; price= " + details.getPrice());
                if (sku.equals(details.getSku())) {
                    skuName = details.getTitle();
                }
            }

            if (!TextUtils.isEmpty(skuName)) {
                btn_buy.setText("InApp购买 " + skuName);
            }
        }
    }

    @Override
    public void onQueryFail(int responseCode, String skuType, List<SkuDetails> list) {
        Log.i(TAG, "onQueryFail skuType= " + skuType + "; responseCode= " + responseCode);
        btn_buy.setClickable(true);
    }

    @Override
    public void onQueryError() {
        Log.i(TAG, "onQueryError");
        btn_buy.setClickable(true);
    }

    @Override
    public void onQueryUnConsumeSuccess(int responseCode, List<GooglePurchase> orders) {
        Log.i(TAG, "onQueryUnConsumeSuccess responseCode: " + responseCode);
        if (orders != null && !orders.isEmpty()) {
            StringBuffer stringBuffer = new StringBuffer();
            for (GooglePurchase purchase : orders) {
                stringBuffer.append(purchase.getProductId() + "\n");
               /* if (SKU_COIN4.equals(purchase.getProductId()) || SKU_PACK2.equals(purchase.getProductId())) {
                    googleBillingUtil.consumeAsync(purchase.getPurchaseToken());
                }*/
            }
            Log.i(TAG, "未消耗的产品数量：" + orders.size() + "\n" + stringBuffer.toString());


        } else {
            Log.i(TAG, "未查询到相关商品");
        }
    }

    @Override
    public void onQueryUnConsumeFail(int responseCode, String msg) {
        Log.i(TAG, "onQueryUnConsumeFail responseCode: " + responseCode + "; msg: " + msg);
    }

    @Override
    public void onPurchaseSuccess(int responseCode, List<Purchase> list) {
        Log.i(TAG, "onPurchaseSuccess responseCode: " + responseCode);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "购买成功", Toast.LENGTH_LONG).show();
            }
        });
        verifyPurchaseUtil.verifyPurchase(responseCode, list);
    }

    @Override
    public void onPurchaseFail(final int responseCode, List<Purchase> list) {
        Log.i(TAG, "onPurchaseFail responseCode: " + responseCode);
        verifyPurchaseUtil.verifyPurchase(responseCode, list);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 一般发生在setAutoConsumeAsync(false) 时重复购买的时候
                if (responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
                    Toast.makeText(getActivity(), "你已经购买了本商品，请消耗后再买", Toast.LENGTH_LONG).show();
                    /**
                     * 这种情况下list 为空
                     *
                     * 最佳实践有：
                     * 1、本地记录每次购买成功的Purchase，当发生此种情况时对比本地的缓存的Purchase sku
                     * 2、调用 GoogleBillingUtil.queryPurchasesInApp()同步查询内购未消耗的商品，再进行sku比对
                     * 3、调用 GoogleBillingUtil.queryUnConsumeOrders(context)异步查询内购未消耗的未消耗的商品（验证通过的订单），再进行sku比对
                     *
                     * 通过使用上面三种方式中的一种或多种拿到相应的Purchase，从而使用其purchaseToken进行消耗处理
                     */
                }
            }
        });
    }

    @Override
    public void onPurchaseError() {
        Log.i(TAG, "onPurchaseError ");
    }

    @Override
    public void onConsumeSuccess(String purchaseToken) {
        Log.i(TAG, "onConsumeSuccess purchaseToken: " + purchaseToken);
    }

    @Override
    public void onConsumeFail(int responseCode, String purchaseToken) {
        Log.i(TAG, "onConsumeFail purchaseToken: " + purchaseToken + "; responseCode= " + responseCode);
    }

    @Override
    public void onPurchaseHistoryResponse(String skuType, int responseCode, List<Purchase> purchasesList) {
        Log.i(TAG, "onPurchaseHistoryResponse skuType: " + skuType + "; responseCode= " + responseCode
                + "; purchasesList" + ((purchasesList != null && !purchasesList.isEmpty()) ? purchasesList.size() : " is empty!"));
    }
}
