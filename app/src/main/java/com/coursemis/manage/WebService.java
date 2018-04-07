package com.coursemis.manage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class WebService {

	// --------------------------------------------------
	// ----- constants -----
	// --------------------------------------------------
	private static final String TAG = "WebService";

	public static final int CON_STATE = 11;
	public static final int Servre_Response = 13;
	public static final int AUDIOFILE = 100;
	public static final int IMGFILE = 101;

	private int timeoutConnection = 30000;
	private int timeoutSocket = 5000;

	private static CookieStore stored_cookie = null;

	// --------------------------------------------------
	// ----- properties -----
	// --------------------------------------------------
	private Handler uihandler = null;
	private int responseCode = 0;
	private final String requestURL;

	// --------------------------------------------------
	// ----- extends -----
	// --------------------------------------------------
	/**
	 * 提供网络访问功能的类--有网络访问需求的对象，要使用该类，就要先提供URL 然后，再根据提供的参数的不同 ，执行不同的网络请求 如：买家注册请求
	 * post(List<NameValuePair> params) 卖家注册请求post(MultipartEntity
	 * userDefienEntity)
	 */
	public WebService(String URL, Handler handler) {
		this.requestURL = URL;
		this.uihandler = handler;

	}

	// --------------------------------------------------
	// ----- functions -----
	// --------------------------------------------------
	/**
	 * 返回 --服务器响应的结果
	 */
	public String post(List<NameValuePair> params, String header) {
		return post(params, timeoutConnection, header);
	}

	/**
	 * 向服务器发起HttpPost请求
	 * 
	 * @param params
	 *            --发出去的参数的形式是键值对，NameValuePair
	 * @return 返回服务器相应的字符串
	 */
	public String post(List<NameValuePair> params, int httptimeout,
			String header) {

		if (this.requestURL == null) {
			return "Request fail: requestURL is null";
		}

		if (params == null) {
			return "Request fail: userDefine NameValuePair is null";
		}

		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, httptimeout);
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		final DefaultHttpClient httpclient = new DefaultHttpClient();
		final HttpPost httppost = new HttpPost(this.requestURL);

		if (header != null)
			httppost.addHeader("auth", header);

		String result = null;

		try {

			httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			final HttpResponse httpresponse = httpclient.execute(httppost);
			final HttpEntity httpentity = httpresponse.getEntity();
			responseCode = httpresponse.getStatusLine().getStatusCode();

			if (httpentity != null) {
				result = EntityUtils.toString(httpentity, "UTF-8");
			}

			// Log.i(TAG, "responseCode is :" + responseCode);
		} catch (final UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			TellConState(" in post(List<NameValuePair> params) UnsupportedEncodeingException");
			return null;
		} catch (final ClientProtocolException e) {

			e.printStackTrace();
			TellConState("in post(List<NameValuePair> params) ClientProtocolException");
			return null;

		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TellConState("in post(List<NameValuePair> params) IOException");
			return null;
		}

		return result;

	}

	/**
	 * 向服务器发起HttpPost请求
	 * 
	 * @param userDefineEntity
	 *            --发出去的参数是以文件的形式组织的，MultipartEntity,add的时候，都是文件类型mimeType
	 * @return 返回服务器的响应，字符串
	 * 
	 */
	public String post(MultipartEntity userDefineEntity, String header) {

		if (this.requestURL == null) {
			return "Request faile:requestURL is null";
		}

		if (userDefineEntity == null) {
			return "Request faile: userDefineEntity is null";
		}
		final HttpClient httpclient = new DefaultHttpClient();
		final HttpPost httppost = new HttpPost(this.requestURL);

		if (header != null)
			httppost.addHeader("auth", header);

		String result = null;

		try {

			httppost.setEntity(userDefineEntity);

			final HttpResponse httpresponse = httpclient.execute(httppost);

			final HttpEntity httpentity = httpresponse.getEntity();
			responseCode = httpresponse.getStatusLine().getStatusCode();

			Log.i(TAG, "responseCode is :" + responseCode);

			if (httpentity != null) {
				result = EntityUtils.toString(httpentity, "UTF-8");
			}

		} catch (final UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e(TAG,
					" in post(MultipartEntity) UnsupportedEncodeingException");
			TellConState(" in post(MultipartEntity) UnsupportedEncodeingException");
			e.printStackTrace();
			return null;
		} catch (final ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "in post(MultipartEntity) ClientProtocolException");
			TellConState("in post(MultipartEntity) ClientProtocolException");
			e.printStackTrace();
			return null;
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "in post(MultipartEntity) IOException");
			TellConState("in post(MultipartEntity) IOException");
			e.printStackTrace();
			return null;
		}

		TellConState("Finish Uploading");
		return result;

	}

	/**
	 * 返回某次请求的连接状态码
	 * 
	 */
	public int getResoponseCode() {
		return responseCode;
	}

	/**
	 * 使用WebService.requestURL的地址，该地址指向一个图片文件或者声音文件
	 * 
	 * @return --返回一个HttpEntity，包含的是图片数据或语音数据
	 * 
	 */
	private HttpEntity getEntity() {

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(WebService.this.requestURL);
		try {
			HttpResponse httpresponse = httpclient.execute(httppost);
			HttpEntity entity = httpresponse.getEntity();
			WebService.this.responseCode = httpresponse.getStatusLine()
					.getStatusCode();

			return entity;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "in getEntity(), ClientProtocolException");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "in getEntity(),IOException");
			return null;
		}

	}

	/**
	 * 用户给出要存放的路径，用来存放从服务器下载的文件
	 * 
	 * @param destFilePath
	 *            --将文件数据存放在该参数所指定的文件中
	 * @return 成功，则返回true;否则返回false
	 */
	// public boolean getFile(String destFilePath) {
	//
	// try {
	// File file = createFile(destFilePath);
	// HttpEntity entity = getEntity();
	// if (file == null) {
	// Log.e(TAG, "用户手机SD卡不可用");
	// return false;
	// }
	//
	// if (entity == null) {
	// Log.e(TAG, "服务器没有返回数据");
	// return false;
	// }
	//
	// OutputStream out = null;
	// out = new BufferedOutputStream(new FileOutputStream(file));
	//
	// entity.writeTo(out);
	// out.close();
	//
	// return true;
	//
	// } catch (DirectoryCreatedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// Log.e(TAG, "创建文件目录:" + destFilePath + ",失败");
	// return false;
	// } catch (FileNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// Log.e(TAG, "文件：" + destFilePath + ",不存在");
	// return false;
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// Log.e(TAG, "将来自服务器的 entity写到指定文件中，出现错误");
	// return false;
	// }
	//
	// }

	/**
	 * 检查当前手机的外部存储器是否可用---外部存储器未必都是SD卡，但大部分都是
	 */
	private boolean checkSDCard() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 根据路径，创建文件，用于写数据；创建成功，则可以返回，失败，则抛出异常或者返回null
	 * 
	 * @throws com.yanhuo_01.compoments.WebService.DirectoryCreatedException
	 * 
	 */
	// private File createFile(String destFilePath)
	// throws com.yanhuo_01.compoments.WebService.DirectoryCreatedException {
	//
	// if (checkSDCard()) {
	// File file = new File(destFilePath);
	// if (!(file.mkdirs() || file.isDirectory())) {
	// Log.e(TAG, "chiefImage directory created failure");
	// throw new DirectoryCreatedException("文件目录创建失败");
	//
	// }
	// return file;
	// } else {
	// return null;
	// }
	//
	// }

	/**
	 * 当没有成功创建目录的时候，抛出这个异常类的对象
	 */
	public class DirectoryCreatedException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String error = "";

		public DirectoryCreatedException(String error) {
			this.error = error;
		}

		public String getString() {
			return this.error;
		}
	}

	/**
	 * 返回异常的连接情况给界面
	 * 
	 */
	private void TellConState(String str) {
		Message mesg = this.uihandler.obtainMessage();
		mesg.what = WebService.CON_STATE;
		mesg.obj = str;

		this.uihandler.sendMessage(mesg);

	}

	/**
	 * 要添加一个Header
	 */
	public void performGet(String header) {

		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(requestURL);

		if (header != null)
			httpGet.addHeader("auth", header);

		String result = "";
		try {
			HttpResponse httpResponse = client.execute(httpGet);
			result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			Log.i(TAG, "result:" + result);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TellConState("ClientProtocolException");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TellConState("IOException");
		}

		TellConState(result);

	}
}
