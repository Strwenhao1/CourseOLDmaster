package com.coursemis.view.activity;

import android.app.Application;
import android.os.Process;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.coursemis.model.LocationData;

public class Location extends Application {


	public LocationClient mLocationClient = null;
	public GeofenceClient mGeofenceClient;
	private String mData;
	public MyLocationListenner myListener = new MyLocationListenner();
	public TextView mTv;
	public NotifyLister mNotifyer=null;
	public Vibrator mVibrator01;
	public static String TAG = "LocTestDemo";

	@Override
	public void onCreate() {
		mLocationClient = new LocationClient( this );
		/**——————————————————————————————————————————————————————————————————
		 * 这里的AK和应用签名包名绑定，如果使用在自己的工程中需要替换为自己申请的Key
		 * ——————————————————————————————————————————————————————————————————
		 */
		mLocationClient.setAK("jpQW17lKZ16jtXrYl2IRaNl0baHBe6sk");
		mLocationClient.registerLocationListener( myListener );
		mGeofenceClient = new GeofenceClient(this);
		super.onCreate();
		Log.d(TAG, "... Application onCreate... pid=" + Process.myPid());
	}

	/**
	 * 显示请求字符串
	 * @param str
	 */
	public void logMsg(String str) {
		try {
			mData = str;
			if ( mTv != null )
				mTv.setText(mData);
				Toast.makeText(getApplicationContext(), "定位结束，请再次点击已关闭定位功能。",
					     Toast.LENGTH_SHORT).show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 监听函数，有更新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			LocationData.latitude=location.getLatitude();
			LocationData.longitude=location.getLongitude();
			LocationData.radius=location.getRadius();


			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				/**
				 * 格式化显示地址信息
				 */
				sb.append("\nAddress : ");
				sb.append(location.getAddrStr());
			}

			logMsg(sb.toString());
			Log.i(TAG, sb.toString());
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null){
				return ;
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			LocationData.latitude=poiLocation.getLatitude();
			LocationData.longitude=poiLocation.getLongitude();
			LocationData.radius=poiLocation.getRadius();
			Toast.makeText(Location.this,sb.toString(),Toast.LENGTH_SHORT).show();


			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			}
			if(poiLocation.hasPoi()){
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			}else{
				sb.append("noPoi information");
			}

			logMsg(sb.toString());
		}
	}

	public class NotifyLister extends BDNotifyListener {
		public void onNotify(BDLocation mlocation, float distance){
			mVibrator01.vibrate(1000);
		}
	}
}