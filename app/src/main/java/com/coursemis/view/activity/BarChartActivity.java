package com.coursemis.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.coursemis.R;
import com.coursemis.listener.TimeClickListener;
import com.coursemis.model.Chart;
import com.coursemis.view.myView.TitleView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class BarChartActivity extends TChartActivity {

    private BarChart mBarChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntent() ;
        initView() ;
        initData() ;
        initTitle() ;
    }

    private void initIntent() {
        Intent intent = getIntent();
        mChart = (Chart) intent.getSerializableExtra("chart");
    }

    private void initTitle() {
        mTitleView.setTitle(mChart.getYear()[mShowCount]+"");
        mTitleView.setLeftButton("返回", new TitleView.OnLeftButtonClickListener() {
            @Override
            public void onClick(View button) {
                BarChartActivity.this.finish();
            }
        });
        mTitleView.setTitle(new TimeClickListener(BarChartActivity.this,mChart.getYear()));
    }

    private void initView() {
        setContentView(R.layout.activity_barchart);
        mBarChart = (BarChart) findViewById(R.id.barChart);
        mTitleView = (TitleView) findViewById(R.id.barChart_title);
    }

    private void initData() {
        mBarChart.setDrawGridBackground(false);
        mBarChart.setDrawBorders(false);  //是否在折线图上添加边框
        mBarChart.setDescription(""+mChart.getYear()[mShowCount]);// 数据描述
        mBarChart.setNoDataTextDescription("no data to display"); // 如果没有数据，显示
        mBarChart.setDrawGridBackground(false); // 是否显示表格颜色
        mBarChart.setGridBackgroundColor(Color.WHITE); // 表格的的颜色，在这里是是给颜色设置一个透明度

        mBarChart.setTouchEnabled(false); // 设置是否可以触摸
        mBarChart.setDragEnabled(false);// 是否可以拖拽
        mBarChart.setScaleEnabled(false);// 是否可以缩放
        mBarChart.setPinchZoom(true);//
        mBarChart.setDrawBarShadow(true);

        updateBar();
    }
    @Override
    public void updateBar() {
        // 设置数据
        BarData mBarData = getBarData();
        mBarChart.setData(mBarData);

        Legend mLegend = mBarChart.getLegend(); // 设置比例图标示
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.BLACK);// 颜色

        // X轴设定
        mBarChart.animateY(3000);
        mBarChart.invalidate();
    }

    private BarData getBarData() {
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < mChart.getGrade()[mShowCount].length; i++) {
            xValues.add(i+"");
        }

        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        for (int i = 0; i < mChart.getGrade()[mShowCount].length; i++) {
            Log.e("y轴",i+"") ;
            yValues.add(new BarEntry(mChart.getGrade()[mShowCount][i],i));
        }

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "评分");
        barDataSet.setColor(Color.rgb(114, 188, 223));
        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet); // add the datasets
        BarData barData = new BarData(xValues, barDataSets);
        return barData;
    }

    /*private static final int SERIES_NR = 3;

    private int[] years;

    public Intent execute(Context context, int[][] grade, int[] year) {
        years = new int[year.length];
        years = year;
        return ChartFactory.getBarChartIntent(context, getBarDemoDataset(grade, year), getBarDemoRenderer(), Type.DEFAULT);
    }


    private XYMultipleSeriesDataset getBarDemoDataset(int[][] grade, int[] year) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        //final int nr = 10;
        final int nr = grade.length;
        //int grade[]={10,20,30};
        String[] str = {"优秀", "良好", "一般"};
        for (int i = 0; i < SERIES_NR; i++) {
            CategorySeries series = new CategorySeries(str[i]);
            //加
            for (int j = 0, k = 0; j < year[year.length - 1]; j++) {

                if (j == year[k]) {
                    series.add(grade[k][i]);
                    k++;
                } else {
                    series.add(0);
                }
            }

//    	  for ( int k = 0; k < nr; k++) {
//    		  series.add(grade[k][i]);
//    	  }
            dataset.addSeries(series.toXYSeries());
        }
        return dataset;
    }

    public XYMultipleSeriesRenderer getBarDemoRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        SimpleSeriesRenderer r = new SimpleSeriesRenderer();
        r.setColor(Color.BLUE);
        renderer.addSeriesRenderer(r);
        r = new SimpleSeriesRenderer();
        r.setColor(Color.GREEN);
        renderer.addSeriesRenderer(r);
        r = new SimpleSeriesRenderer();
        r.setColor(Color.RED);
        renderer.addSeriesRenderer(r);
        setChartSettings(renderer);
        return renderer;
    }

    private void setChartSettings(XYMultipleSeriesRenderer renderer) {
        renderer.setChartTitle("教师评价表");
        renderer.setXTitle("年份");
        renderer.setYTitle("人数");
        renderer.setXLabels(4);// 显示x轴的数字
        //renderer.setXAxisMin(1);
        //renderer.setXAxisMax(5);
        renderer.setXAxisMin(years[0]);
        renderer.setXAxisMax(years[years.length - 1]);
        renderer.setYAxisMin(0);
        renderer.setYAxisMax(100);
        renderer.setBarSpacing(0.5);  //设置柱状图中柱子之间的间隔
    }*/
}
