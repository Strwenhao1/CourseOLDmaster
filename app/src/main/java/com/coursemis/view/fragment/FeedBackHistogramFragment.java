package com.coursemis.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coursemis.R;
import com.coursemis.model.Chart;
import com.coursemis.model.Course;
import com.coursemis.util.HttpUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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
 * 学生反馈中显示柱状图的界面
 * Created by zhxchao on 2018/3/17.
 */

public class FeedBackHistogramFragment extends BaseFragment {

    private static final String TITLE = "柱状图";

    private BarChart mHistogram;
    private Chart mChart;
    private int mShowCount = 0;
    private ViewPager mViewPager;

    @Override
    public void refresh(Course course) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_histogram, null);
        initView();
        initData();
        return mView;
    }


    private void initView() {
        mHistogram = (BarChart) mView.findViewById(R.id.histogram);
        mViewPager = (ViewPager) mView.findViewById(R.id.viewPager);
    }

    private void initData() {
        /*mHistogram.setDrawGridBackground(false);
        mHistogram.setDrawBorders(false);  //是否在折线图上添加边框
        mHistogram.setNoDataTextDescription("no data to display"); // 如果没有数据，显示
        mHistogram.setDrawGridBackground(false); // 是否显示表格颜色
        mHistogram.setGridBackgroundColor(Color.WHITE); // 表格的的颜色，在这里是是给颜色设置一个透明度
        mHistogram.setTouchEnabled(false); // 设置是否可以触摸
        mHistogram.setDragEnabled(false);// 是否可以拖拽
        mHistogram.setScaleEnabled(false);// 是否可以缩放
        mHistogram.setPinchZoom(true);//
        mHistogram.setDrawBarShadow(true);*/

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("courseid", mCourse.getCId() + "");
        Log.e("测试", mCourse.getCId() + "");
        params.put("action", "course_teacher");// /
        client.post(HttpUtil.server_evaluate_zhu_get, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, JSONObject arg1) {
                        Log.e("测试", "返回了结果");
                        // TODO Auto-generated method stub
                        int[] year_zhuxing = new int[arg1.optJSONArray(
                                "result").length()];
                        int[][] grade_zhuxing = new int[arg1.optJSONArray(
                                "result").length()][3];
                        for (int i = 0; i < arg1.optJSONArray(
                                "result").length(); i++) {
                            JSONObject object = arg1.optJSONArray(
                                    "result").optJSONObject(i);

                            year_zhuxing[i] = object.optInt("year");
                            Log.e("year\t" + i, object.optInt("year") + "");
                            grade_zhuxing[i][0] = object
                                    .optInt("grade1");
                            Log.e("grade1\t" + i, object.optInt("grade1") + "");
                            grade_zhuxing[i][1] = object
                                    .optInt("grade2");
                            Log.e("grade2\t" + i, object.optInt("grade2") + "");
                            grade_zhuxing[i][2] = object
                                    .optInt("grade3");
                            Log.e("grade3\t" + i, object.optInt("grade3") + "");
                        }
                        mChart = new Chart();
                        mChart.setGrade(grade_zhuxing);
                        mChart.setYear(year_zhuxing);
                        final ArrayList<BarChart> barCharts = new ArrayList<BarChart>();
                        for (int i = 0; i < mChart.getYear().length; i++) {
                            BarChart barChart = new BarChart(getActivity());
                            barCharts.add(barChart);
                        }
                        mViewPager.setAdapter(new PagerAdapter() {
                            @Override
                            public int getCount() {
                                return mChart.getYear().length;
                            }

                            @Override
                            public boolean isViewFromObject(View view, Object object) {
                                return view == object;
                            }

                            @Override
                            public Object instantiateItem(ViewGroup container, int position) {

                                BarChart barChart = barCharts.get(position);
                                barChart.setDrawGridBackground(false);
                                barChart.setDrawBorders(false);  //是否在折线图上添加边框
                                barChart.setNoDataTextDescription("no data to display"); // 如果没有数据，显示
                                barChart.setDrawGridBackground(false); // 是否显示表格颜色
                                barChart.setGridBackgroundColor(Color.WHITE); // 表格的的颜色，在这里是是给颜色设置一个透明度
                                barChart.setTouchEnabled(false); // 设置是否可以触摸
                                barChart.setDragEnabled(false);// 是否可以拖拽
                                barChart.setScaleEnabled(false);// 是否可以缩放
                                barChart.setPinchZoom(true);//
                                barChart.setDrawBarShadow(true);
                                barChart.setDescription("" + mChart.getYear()[position]);// 数据描述
                                updateBar(barChart);
                                mViewPager.addView(barChart);

                                return barChart;
                            }

                            @Override
                            public void destroyItem(ViewGroup container, int position, Object object) {
                                BarChart barChart = barCharts.get(position);
                                mViewPager.removeView(barChart);
                            }
                        });
                        super.onSuccess(arg0, arg1);
                    }

                    @Override
                    public void onSuccess(JSONObject response) {
                        super.onSuccess(response);
                    }
                });
    }

    public void updateBar() {
        // 设置数据
        BarData mBarData = getBarData();
        mHistogram.setData(mBarData);

        Legend mLegend = mHistogram.getLegend(); // 设置比例图标示
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.BLACK);// 颜色

        // X轴设定
        mHistogram.animateY(3000);
        mHistogram.invalidate();
    }

    public void updateBar(BarChart barChart) {
        // 设置数据
        BarData mBarData = getBarData();
        barChart.setData(mBarData);

        Legend mLegend = barChart.getLegend(); // 设置比例图标示
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.BLACK);// 颜色

        // X轴设定
        barChart.animateY(3000);
        barChart.invalidate();
    }

    private BarData getBarData() {
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < mChart.getGrade()[mShowCount].length; i++) {
            xValues.add(i + "");
        }

        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        for (int i = 0; i < mChart.getGrade()[mShowCount].length; i++) {
            Log.e("y轴", i + "");
            yValues.add(new BarEntry(mChart.getGrade()[mShowCount][i], i));
        }

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "评分");
        barDataSet.setColor(Color.rgb(114, 188, 223));
        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet); // add the datasets
        BarData barData = new BarData(xValues, barDataSets);
        return barData;
    }
}
