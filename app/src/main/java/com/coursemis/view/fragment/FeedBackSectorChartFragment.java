package com.coursemis.view.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coursemis.R;
import com.coursemis.model.Chart;
import com.coursemis.model.Course;
import com.coursemis.util.HttpUtil;
import com.coursemis.view.activity.EvaluateGetActivity;
import com.coursemis.view.activity.PieChartActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * _oo0oo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * 0\  =  /0
 * ___/`---'\___
 * .' \\|     |// '.
 * / \\|||  :  |||// \
 * / _||||| -:- |||||- \
 * |   | \\\  -  /// |   |
 * | \_|  ''\---/''  |_/ |
 * \  .-\__  '-'  ___/-. /
 * ___'. .'  /--.--\  `. .'___
 * ."" '<  `.___\_<|>_/___.' >' "".
 * | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * \  \ `_.   \_ __\ /__ _/   .-` /  /
 * =====`-.____`.___ \_____/___.-`___.-'=====
 * `=---='
 * <p>
 * <p>
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Created by zhxchao on 2018/3/17.
 */

public class FeedBackSectorChartFragment extends BaseFragment {

    private PieChart mSectorChart;

    @Override
    public void refresh(Course course) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_sector_chart, null);
        initView() ;
        initData() ;
        return mView;
    }

    private void initData() {
        AsyncHttpClient client = new AsyncHttpClient() ;
        RequestParams params = new RequestParams();
        params.put("courseid", mCourse.getCId()+"");
        params.put("action", "course_teacher");// /
        client.post(HttpUtil.server_evaluate_Sms_get, params,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, JSONObject arg1) {
                        // TODO Auto-generated method stub
                        int[] personnum;
                        personnum = new int[arg1.optJSONArray(
                                "result").length()];
                        for (int i = 0; i < arg1.optJSONArray(
                                "result").length(); i++) {
                            JSONObject object = arg1.optJSONArray(
                                    "result").optJSONObject(i);

                            personnum[i] = object
                                    .optInt("personnum");
                        }
                                /*Intent achartIntent = new PieChartActivity()
                                        .execute(context, personnum);
                                startActivity(achartIntent);*/
                        Chart chart = new Chart() ;
                        chart.setPersonnum(personnum);
                        //设置数据
                        PieData pieData = getPieData(chart);
                        mSectorChart.setData(pieData);

                        Legend mLegend = mSectorChart.getLegend();  //设置比例图
                        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);  //最左边显示
                        mLegend.setForm(Legend.LegendForm.SQUARE);  //设置比例图的形状，默认是方形 SQUARE
                        mLegend.setXEntrySpace(7f);
                        mLegend.setYEntrySpace(5f);

                        mSectorChart.animateXY(1000, 1000);  //设置动画
                        mSectorChart.invalidate();
                        super.onSuccess(arg0, arg1);
                    }

                });
        initPieChart();
    }
    private void initPieChart() {
        mSectorChart.setDescription("饼图");
        mSectorChart.setHoleColorTransparent(true);
        mSectorChart.setHoleRadius(60f);  //半径
        mSectorChart.setTransparentCircleRadius(0f); // 半透明圈
        mSectorChart.setHoleRadius(0);  //实心圆

        mSectorChart.setDrawCenterText(true);  //饼状图中间可以添加文字
        mSectorChart.setDrawHoleEnabled(true);
        mSectorChart.setRotationAngle(90); // 初始旋转角度

        mSectorChart.setRotationEnabled(false); // 可以手动旋转
        mSectorChart.setUsePercentValues(true);  //显示成百分比
//        pieChart.setCenterText("PieChart");  //饼状图中间的文字


    }

    private PieData getPieData(Chart chart) {
        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容
        xValues.add("良好");
        xValues.add("一般");
        xValues.add("优秀");

        /**
         * 将一个饼形图分成六部分， 各个部分的数值比例为12:12:18:20:28:10
         * 所以 12代表的百分比就是12%
         * 在具体的实现过程中，这里是获取网络请求的list的数据
         */
        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据
        for (int i = 0; i < chart.getPersonnum().length; i++) {
            yValues.add(new Entry(chart.getPersonnum()[i], i));
        }

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, "评分");
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离

        // 饼图颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));
        colors.add(Color.rgb(255, 123, 124));
        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度
        PieData pieData = new PieData(xValues, pieDataSet);

        return pieData;
    }

    private void initView() {
        mSectorChart = (PieChart) mView.findViewById(R.id.sectorChart);
    }
}
