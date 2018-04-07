package com.coursemis.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.coursemis.R;

public class LocationActivity extends Activity {

	private LocationManager manager;
	private Location currentLocation;
	private String best;
	private String latitude=null;
	private String longitude=null;
	@Override
	protected void onResume() {
		super.onResume();
		// 取得最佳的定位提供者
		Criteria criteria = new Criteria();
		best = manager.getBestProvider(criteria, true);
		// 更新位置频率的条件
		int minTime = 5000; // 毫秒
		float minDistance = 5; // 公尺
		if (best != null) { // 取得快取的最后位置,如果有的话
			currentLocation = manager.getLastKnownLocation(best);
			manager.requestLocationUpdates(best, minTime,
					minDistance, listener);
		}
		else { // 取得快取的最后位置,如果有的话
			currentLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					minTime, minDistance, listener);
		}
		updatePosition(); // 更新位置
	}
	@Override
	protected void onPause() {
		super.onPause();
		manager.removeUpdates(listener);
	}
	// 更新现在的位置
	private void updatePosition() {
		TextView output;
		output = (TextView) findViewById(R.id.student_location);
		if (currentLocation == null) {
			output.setText("取得定位信息中...");
		} else {
			output.setText(getLocationInfo(currentLocation));
		}
	}
	// 创建定位服务的监听者对象
	private LocationListener listener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			currentLocation = location;
			updatePosition();
		}
		@Override
		public void onProviderDisabled(String provider) { }
		@Override
		public void onProviderEnabled(String provider) { }
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) { }
	};




	// 取得定位信息
	public String getLocationInfo(Location location) {
		StringBuffer str = new StringBuffer();
		str.append("定位提供者(Provider): "+location.getProvider());
		str.append("\n纬度(Latitude): " + Double.toString(location.getLatitude()));
		str.append("\n经度(Longitude): " + Double.toString(location.getLongitude()));
		str.append("\n高度(Altitude): " + Double.toString(location.getAltitude()));
		latitude = Double.toString(location.getLatitude())+"";
		longitude=Double.toString(location.getLongitude())+"";
		return str.toString();
	}
	// 启动Google地图
	public void button1_Click(View view) {
		// 取得经纬度坐标
		float latitude = (float) currentLocation.getLatitude();
		float longitude = (float) currentLocation.getLongitude();
		// 创建URI字符串
		String uri = String.format("geo:%f,%f?z=18", latitude, longitude);
		// 创建Intent对象
		Intent geoMap = new Intent(Intent.ACTION_VIEW,Uri.parse(uri));
		startActivity(geoMap);  // 启动活动
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		// 取得系统服务的LocationManager对象
		manager = (LocationManager)getSystemService(LOCATION_SERVICE);
		// 检查是否有启用GPS
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			// 显示对话框启用GPS
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("定位管理")
					.setMessage("GPS目前状态是尚未启用.\n"
							+"请问你是否現在就设置启用GPS?")
					.setPositiveButton("启用", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 使用Intent对象启动设置程序式来更改GPS设置
							Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(i);
						}
					})
					.setNegativeButton("不启用", null).create().show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location, menu);
		return true;
	}

}
