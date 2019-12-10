package com.y.echartsdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * webview问题：https://www.jianshu.com/p/d2f5ae6b4927
 * ECharts配置项：https://www.echartsjs.com/zh/option.html#title.text
 */
public class EChartsActivity extends AppCompatActivity {

    public static void start(Context context){
        context.startActivity(new Intent(context,EChartsActivity.class));
    }

    private WebView wvHistogram,wvLine,wvPie,wvRadar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echarts);

        wvHistogram = findViewById(R.id.wvHistogram);
        wvLine = findViewById(R.id.wvLine);
        wvPie = findViewById(R.id.wvPie);
        wvRadar = findViewById(R.id.wvRadar);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init(final WebView wv,final String method){
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("file:///android_asset/web/echarts.html");
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {

                wv.loadUrl("javascript:" + method + "();");
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    /**
     * 柱状图
     */
    public void histogramInit(View view) {
        init(wvHistogram,"createBarChart");
    }

    /**
     * 折线图
     */
    public void lineInit(View view) {
        init(wvLine,"createLineChart");
    }

    /**
     * 饼状图
     */
    public void pieInit(View view) {
        init(wvPie,"createPieChart");
    }

    /**
     * 雷达图
     */
    public void pieRadar(View view) {
        init(wvRadar,"createRadarChart");
    }
}
