package com.coursemis.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.coursemis.R;
import com.coursemis.model.Chart;
import com.coursemis.view.myView.TitleView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class LineChartActivity extends TChartActivity {

    private LineChart mLineChart;
    private double[] mYears;
    private double[] mPoint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView() ;
        initIntent() ;
        initData() ;

    }

    private void initIntent() {
        Intent intent = getIntent();
        mChart = (Chart) intent.getSerializableExtra("chart");
        Log.e("测试",(mChart==null)+"") ;
        mYears = mChart.getYears() ;
        mPoint = mChart.getPoint() ;
    }

    private void initData() {
        //初始化标题
        initTitle() ;
        initChart();
    }

    private void initTitle() {
        mTitleView.setTitle("折线图");
        mTitleView.setLeftButton("返回", new TitleView.OnLeftButtonClickListener() {
            @Override
            public void onClick(View button) {
                LineChartActivity.this.finish();
            }
        });
    }

    private void initView() {
        setContentView(R.layout.activity__linechart);
        mTitleView = (TitleView) findViewById(R.id.line_chart_title);
        mLineChart = (LineChart) findViewById(R.id.line_chart);
    }
    private void initChart() {
        /**
         * ====================1.初始化-自由配置===========================
         */
        // 是否在折线图上添加边框
        mLineChart.setDrawGridBackground(false);
        mLineChart.setDrawBorders(false);
        // 设置右下角描述
        mLineChart.setDescription("");
        //设置透明度
        mLineChart.setAlpha(0.8f);
        //设置网格底下的那条线的颜色
        mLineChart.setBorderColor(Color.rgb(213, 216, 214));
        //设置高亮显示
        mLineChart.setHighlightEnabled(true);
        //设置是否可以触摸，如为false，则不能拖动，缩放等
        mLineChart.setTouchEnabled(true);
        //设置是否可以拖拽
        mLineChart.setDragEnabled(false);
        //设置是否可以缩放
        mLineChart.setScaleEnabled(false);
        //设置是否能扩大扩小
        mLineChart.setPinchZoom(false);
        /**
         * ====================2.布局点添加数据-自由布局===========================
         */
        // 加载数据
        LineData data = getLineData();
        mLineChart.setData(data);
        /**
         * ====================3.x，y动画效果和刷新图表等===========================
         */
        //从X轴进入的动画
        mLineChart.animateX(4000);
        mLineChart.animateY(3000);   //从Y轴进入的动画
        mLineChart.animateXY(3000, 3000);    //从XY轴一起进入的动画
        //设置最小的缩放
        mLineChart.setScaleMinima(0.5f, 1f);
        Legend l = mLineChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);  //设置图最下面显示的类型
        l.setTextSize(15);
        l.setTextColor(Color.rgb(104, 241, 175));
        l.setFormSize(30f);
        // 刷新图表
        mLineChart.invalidate();
    }

    private LineData getLineData() {
        /*String[] xx = {"2", "4", "6", "8", "10", "12", "14", "16", "18"};
        String[] yy = {"20", "80", "10", "60", "30", "70", "55", "22", "40"};
*/
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < mYears.length; i++) {
            xVals.add(mYears[i]+"");
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < mPoint.length; i++) {
            yVals.add(new Entry((float) mPoint[i], i));
        }

        LineDataSet set1 = new LineDataSet(yVals, "时间");
        set1.setDrawCubic(true);  //设置曲线为圆滑的线
        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(false);  //设置包括的范围区域填充颜色
        set1.setDrawCircles(true);  //设置有圆点
        set1.setLineWidth(2f);    //设置线的宽度
        set1.setCircleSize(5f);   //设置小圆的大小
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setColor(Color.rgb(104, 241, 175));    //设置曲线的颜色

        return new LineData(xVals, set1);
    }

    /*public Intent execute(Context context , double[] mYears, double [] points )
	{
		  String[] titles = new String[] { "各年份优秀率"};
		  List <double[]>x = new ArrayList<double[]>();
		  for(int i=0;i<titles.length;i++)
		  {
		  	//x.add(new double[]{2010,2011,2012,2013});///
			  x.add(mYears);
		  }
		  List<double[]>y=new ArrayList<double[]>();
		
		  //y.add(new double[] {80,90,100,90});
		  y.add(points);
		  
		  //设置画笔的颜色
		  int[] colors = new int[] { Color.BLUE};
		  //设置曲线节点处的形状
		  PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE};
		  //构造渲染器
		  XYMultipleSeriesRenderer renderer=buildRenderer(colors,styles,true);
		  int length=renderer.getSeriesRendererCount();
		  //拐点实心填充
		  for(int i=0;i<length;i++)
		  {
			  ((XYSeriesRenderer)renderer.getSeriesRendererAt(i)).setFillPoints(true);
			  
		  }
		  
		  //setChartSettings(renderer, "教师评价走势", "年份", "优秀率", 2010, 2013, 0, 100 , Color.LTGRAY, Color.LTGRAY);
		  setChartSettings(renderer, "教师评价走势", "年份", "优秀率", mYears[0]-1, mYears[mYears.length-1]+1, 0, 100 , Color.LTGRAY, Color.LTGRAY);
		  renderer.setXLabels(4);// 显示x轴的数字
		  renderer.setYLabels(9);
		  renderer.setShowGrid(true);//设置是否在图表中显示网格
		  renderer.setYLabelsAlign(Align.RIGHT);//设置刻度线与Y轴之间的相对位置关系
		  renderer.setZoomButtonsVisible(true);  // 是否显示右下角的放大缩小还原按钮
		  Intent intent=ChartFactory.getLineChartIntent(context,buildDataset(titles,x,y) , renderer, "教师评价走势");
		
		  
		  return intent;
	}
	//将颜色和  节点形状组合成渲染器
	protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles, boolean fill)
       {
           XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
           int length = colors.length;
           for (int i = 0; i < length; i++)
           {
               XYSeriesRenderer r = new XYSeriesRenderer();
               r.setColor(colors[i]);
               r.setPointStyle(styles[i]);
               r.setFillPoints(fill);
               renderer.addSeriesRenderer(r);
           }
           return renderer;
       }
	
	//设置x轴y轴的显示
	 protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title,
             String xTitle,String yTitle, double xMin,double xMax, double yMin, double yMax,
             int axesColor,int labelsColor)
	 			{
				renderer.setChartTitle(title);
				renderer.setXTitle(xTitle);
				renderer.setYTitle(yTitle);
				renderer.setXAxisMin(xMin);
				renderer.setXAxisMax(xMax);
				renderer.setYAxisMin(yMin);
				renderer.setYAxisMax(yMax);
				renderer.setAxesColor(axesColor);
				renderer.setLabelsColor(labelsColor);
	 			}
	 
	 
	 
	 protected XYMultipleSeriesDataset buildDataset(String[] titles,List xValues, List yValues)
		{
			XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
			
			int length = titles.length;                  //有几条线
			for (int i = 0; i < length; i++)
			{
				XYSeries series = new XYSeries(titles[i]);    //根据每条线的名称创建
				double[] xV = (double[]) xValues.get(i);                 //获取第i条线的数据
				double[] yV = (double[]) yValues.get(i);
				int seriesLength = xV.length;                 //有几个点
			
				for (int k = 0; k < seriesLength; k++)        //每条线里有几个点
				{
					series.add(xV[k], yV[k]);
				}
			
				dataset.addSeries(series);
			}
			
			return dataset;
		}*/

}
