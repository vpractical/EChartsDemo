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

import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Orient;
import com.github.abel533.echarts.code.Position;
import com.github.abel533.echarts.code.SelectedMode;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.code.X;
import com.github.abel533.echarts.data.PieData;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Pie;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 参考：https://github.com/tianyalu/EchartsDemo.git
 *
 * 要用webview的时候，记得最好 另外单独开一个进程 去使用webview 并且当这个 进程结束时，请手动调用System.exit(0)。
 * 这是目前对于webview 内存泄露 最好的解决方案
 */
public class ECharts2Activity extends AppCompatActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, ECharts2Activity.class));
    }

    private WebView wvHistogram, wvLine, wvPie, wvRadar;
    private Gson gson = new Gson();

    private List<String> x = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
    private List<String> pv = Arrays.asList("180", "310", "192", "110", "104", "141", "172");
    private List<String> uv = Arrays.asList("120", "200", "150", "80", "70", "110", "130");


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
    private void init(final WebView wv, final String option) {
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("file:///android_asset/web/echarts2.html");
        wv.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                wv.loadUrl("javascript:setOption(" + option + ")");
            }
        });
    }

    /**
     * 柱状图
     */
    public void histogramInit(View view) {
        init(wvHistogram, createBar());
    }

    /**
     * 折线图
     */
    public void lineInit(View view) {
        init(wvLine, createLine());
    }


    /**
     * 饼状图
     */
    public void pieInit(View view) {
        init(wvPie, createPie());
    }

    /**
     * 雷达图
     */
    public void pieRadar(View view) {
        init(wvRadar, createRadar());
    }

    private String createBar() {
        Option option = new Option();
        //图标左上角的说明文字
        option.title().text("pvuv数");
        //点击选中时的样式,axis是选中轴的线,item是点
        option.tooltip().trigger(Trigger.axis);
        //每组数据的说明颜色图标,标记哪种颜色对应哪种数据
        option.legend().data("pv","uv");
        //防止标签溢出
        option.grid().containLabel(true);
        //图标边界距离,show是图标右侧封闭线
        option.grid().show(false).left(6).right(6).bottom(30);

        //bar就是柱子
        Bar ba = new Bar();
        //柱子名字，选中时的tip上说明用
        ba.setName("pv");
        ba.setData(pv);
        //柱子宽度,默认是略小于每个x单位长
//        ba.barWidth(50);
        //柱子上显示的文本标签的样式位置
        ba.label().normal().show(true).position(Position.inside).color("white");
        //stack值相同时，柱子叠加高度，不同时，并列显示在一个x轴单位内
        ba.stack("a");

        Bar bb = new Bar("uv");
        bb.setData(uv);
        bb.label().normal().show(true).setPosition(Position.top); //label样式
        bb.stack("a");

        CategoryAxis xAxis = new CategoryAxis();
        //值在每个x单位长的靠左还是中间位置
        xAxis.boundaryGap(true);
        xAxis.setData(x);
        //是否显示坐标轴刻度
        xAxis.axisTick().show(true);
        //x轴每几个单位长度显示一次标签
        xAxis.axisLabel().interval(0);
        option.xAxis(xAxis);

        ValueAxis yAxis = new ValueAxis();
        option.yAxis(yAxis);
        option.series(ba, bb);
        return gson.toJson(option);
    }

    private String createLine() {
        Option option = new Option();
        option.title().text("pvuv数");
        option.tooltip().trigger(Trigger.axis);
        option.legend().data("pv","uv");
        option.grid().containLabel(true); //防止标签溢出
        option.grid().show(false).left(6).right(6).bottom(30);

        Line pvLine = new Line();
        pvLine.setName("pv");
        pvLine.setData(pv);
        //是否平滑的线
        pvLine.setSmooth(true);
        pvLine.label().normal().show(true).setPosition(Position.top);

        Line uvLine = new Line();
        uvLine.setName("uv");
        uvLine.setData(uv);
        uvLine.setSmooth(false);
        uvLine.label().normal().show(true).setPosition(Position.bottom);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.boundaryGap(true); //值是在中间还是在原点
        xAxis.setData(x);
        option.xAxis(xAxis);

        ValueAxis yAxis = new ValueAxis();
        option.yAxis(yAxis);
        option.series(pvLine,uvLine);
        return gson.toJson(option);
    }

    private String createPie() {
        Option option = new Option();
        option.title().text("pvuv数").top(20).left(X.center);
        //饼图雷达图tip内容格式 : a（系列名称），b（数据项名称），c（数值）, d（百分比）
        option.tooltip().trigger(Trigger.axis).formatter("{a} <br/>{b} : {c} ({d}%)");
        option.legend().orient(Orient.vertical).right(6).top(6).bottom(6).data(x);

        Pie piePv = new Pie("pv");
        piePv.setData(genPiePv());
        // 半径，相对于容器窄边一半的百分比
        piePv.radius("30%");
        piePv.selectedMode(SelectedMode.single);
        //圆心位置相对于容器的百分比
        piePv.center(new String[]{ "40%", "50%"});

        Pie pieUv = new Pie("uv");
        pieUv.setData(genPieUv());
        // 半径，相对于容器窄边一半的百分比
        pieUv.radius(new String[]{"40%","50%"});
        //圆心位置相对于容器的百分比
        pieUv.center(new String[]{ "40%", "50%"});

        option.series(piePv,pieUv);
        return gson.toJson(option);
    }
    private String createRadar() {
        Option option = new Option();
        option.title().text("pvuv数").top(20).left(X.center);
        //饼图雷达图tip内容格式 : a（系列名称），b（数据项名称），c（数值）, d（百分比）
        option.tooltip().trigger(Trigger.axis).formatter("{a} <br/>{b} : {c} ({d}%)");
        option.legend().orient(Orient.vertical).right(6).top(6).bottom(6).data(x);


        Pie piePv = new Pie("pv");
        piePv.setData(genPiePv());
        // 半径，相对于容器窄边一半的百分比
        piePv.radius("30%");
        piePv.selectedMode(SelectedMode.single);
        //圆心位置相对于容器的百分比
        piePv.center(new String[]{ "40%", "50%"});

        Pie pieUv = new Pie("uv");
        pieUv.setData(genPieUv());
        // 半径，相对于容器窄边一半的百分比
        pieUv.radius(new String[]{"40%","50%"});
        //圆心位置相对于容器的百分比
        pieUv.center(new String[]{ "40%", "50%"});

        option.series(piePv,pieUv);
        return gson.toJson(option);
    }


    private List<PieData> genPiePv(){
        List<PieData> list = new ArrayList<>();
        for(int i = 0; i < x.size(); i++){
            PieData pieData = new PieData(x.get(i), pv.get(i));
            list.add(pieData);
        }
        return list;
    }

    private List<PieData> genPieUv(){
        List<PieData> list = new ArrayList<>();
        for(int i = 0; i < x.size(); i++){
            PieData pieData = new PieData(x.get(i), uv.get(i));
            list.add(pieData);
        }
        return list;
    }
}
