package com.coursemis.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;

import com.coursemis.R;
import com.coursemis.model.Chart;
import com.coursemis.view.myView.TitleView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

public class PieChartActivity extends TChartActivity{

    private PieChart mPieChart;
    private int [] mPersonnum ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView() ;
        initIntent() ;
        initTitle() ;
        initPieChart();
    }

    private void initIntent() {
        Intent intent = getIntent();
        mChart = (Chart) intent.getSerializableExtra("chart");
        mPersonnum = mChart.getPersonnum() ;
    }

    private void initTitle() {
        mTitleView.setTitle("饼状图");
        mTitleView.setLeftButton("返回", new TitleView.OnLeftButtonClickListener() {
            @Override
            public void onClick(View button) {
                PieChartActivity.this.finish();
            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity_piechart);
        mTitleView = (TitleView) findViewById(R.id.pie_chart_title);
        mPieChart = (PieChart) findViewById(R.id.pie_chart);
    }

    private void initPieChart() {
        mPieChart.setDescription("饼图");
        mPieChart.setHoleColorTransparent(true);
        mPieChart.setHoleRadius(60f);  //半径
        mPieChart.setTransparentCircleRadius(0f); // 半透明圈
        mPieChart.setHoleRadius(0);  //实心圆

        mPieChart.setDrawCenterText(true);  //饼状图中间可以添加文字
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setRotationAngle(90); // 初始旋转角度

        mPieChart.setRotationEnabled(true); // 可以手动旋转
        mPieChart.setUsePercentValues(true);  //显示成百分比
//        pieChart.setCenterText("PieChart");  //饼状图中间的文字

        //设置数据
        PieData pieData = getPieData();
        mPieChart.setData(pieData);

        Legend mLegend = mPieChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);  //最左边显示
        mLegend.setForm(Legend.LegendForm.SQUARE);  //设置比例图的形状，默认是方形 SQUARE
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);

        mPieChart.animateXY(1000, 1000);  //设置动画
        mPieChart.invalidate();
    }

    private PieData getPieData() {
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
        for (int i = 0; i < mPersonnum.length; i++) {
            yValues.add(new Entry(mPersonnum[i], i));
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


    /*    public Intent execute(Context context, int[] personnum) {
        int[] colors = new int[]{Color.RED, Color.YELLOW, Color.BLUE};
        DefaultRenderer renderer = buildCategoryRenderer(colors);
        CategorySeries categorySeries = new CategorySeries("grade shows");
        categorySeries.add("良好", personnum[0]);
        categorySeries.add("一般", personnum[1]);
        categorySeries.add("优秀 ", personnum[2]);  //  现在是在本地，到时改成传过的数据即可
        return ChartFactory.getPieChartIntent(context, categorySeries, renderer, null);
    }

    protected DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }*/
}
