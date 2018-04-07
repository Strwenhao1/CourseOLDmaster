package com.coursemis.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.coursemis.R;

public class MapActivity extends Activity{
	BMapManager mBMapMan = null;
	MapView mMapView = null;

        @Override
        public void onCreate(Bundle savedInstanceState){
        	super.onCreate(savedInstanceState);
        	//去除title   
        	requestWindowFeature(Window.FEATURE_NO_TITLE);   
        	//去掉Activity上面的状态栏
//        	getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        	mBMapMan=new BMapManager(getApplication());
        	mBMapMan.init("jpQW17lKZ16jtXrYl2IRaNl0baHBe6sk", null);
        	setContentView(R.layout.activity_map);

        	mMapView=(MapView)findViewById(R.id.bmapsView);
        	mMapView.setBuiltInZoomControls(true);





        	MapController mMapController=mMapView.getController();
        	GeoPoint point =new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));
        	mMapController.setCenter(point);
        	mMapController.setZoom(12);
        	MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mMapView);
        	LocationData locData = new LocationData();
        	//以下三个值是经纬度的值，目前这个值是水口附近的地理信息。 整合到整个工程代码中时 这三个值我会从跳转过来的activity中获取的。然后再地图中显示
        	locData.latitude = getIntent().getDoubleExtra("latitude",30.236372);
        	locData.longitude = getIntent().getDoubleExtra("longitude",120.050494);
        	locData.direction = getIntent().getFloatExtra("radius", 68.32508f);
        	myLocationOverlay.setData(locData);
        	mMapView.getOverlays().add(myLocationOverlay);
        	mMapView.refresh();
        	mMapView.getController().animateTo(new GeoPoint((int)(locData.latitude*1e6),
        	(int)(locData.longitude* 1e6)));   
        }
        
        @Override
        protected void onDestroy(){
                mMapView.destroy();
                if(mBMapMan!=null){
                        mBMapMan.destroy();
                        mBMapMan=null;
                }
                super.onDestroy();
        }
        @Override
        protected void onPause(){
                mMapView.onPause();
                if(mBMapMan!=null){
                       mBMapMan.stop();
                }
                super.onPause();
        }
        @Override
        protected void onResume(){
                mMapView.onResume();
                if(mBMapMan!=null){
                        mBMapMan.start();
                }
               super.onResume();
        }

        
} 
