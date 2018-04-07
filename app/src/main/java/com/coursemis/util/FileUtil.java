package com.coursemis.util;

/**
* <p>project_name：contacts</p>
* <p>Description: </p>
* <p>Copyright: Copyright (c) 2011 by tl3shi.</p>
**/
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;


/**
* <p>Title: FileUtilBak.java</p>
* <p>Description: </p>
* @author <a href="mailto:tanglei3shi@163.com">Administrator</a>
* @date 2011-3-3 下午03:28:05
* @version 1.0
*/
public class FileUtil
{

	private String SDPATH;

	private int FILESIZE = 4*1024;

	public String getSDPATH(){
		return SDPATH;
	}

	public FileUtil(){
		//得到当前外部存储设备的目录( /SDCARD )
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	/**
	 * 在SD卡上创建文件
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public File createSDFile(String fileName) throws IOException{
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * @param dirName
	 * @return
	 */
	public File createSDDir(String dirName){
		File dir = new File(SDPATH + dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 * @param fileName
	 * @return
	 */
	public boolean isFileExist(String fileName){
		File file = new File(SDPATH + fileName);
		return file.exists();
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 * @param path
	 * @param fileName
	 * @param input
	 * @return
	 */
	public File write2SDFromInput(String path,String fileName,InputStream input){

		File file = null;
		OutputStream output = null;
		try {
			createSDDir(path);
			file = createSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte[] buffer = new byte[FILESIZE];
			while((input.read(buffer)) != -1){
				output.write(buffer);
			}
			output.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(null != output){
					output.close();
				}
				if(null != input){
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}


	/**
	 * 直接通过HTTP协议提交数据到服务器,实现表单提交功能
	 * @param actionUrl 上传路径
	 * @param params 请求参数 key为参数名,value为参数值
	 * @param files 上传文件
	 */
	public static String post(String actionUrl, Map<String, String> params, FormFile[] files) {
	    try {
	        String BOUNDARY = "---------7d4a6d158c9"; //数据分隔线
	        String MULTIPART_FORM_DATA = "multipart/form-data";

	        URL url = new URL(actionUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setConnectTimeout(6000);
	        conn.setDoInput(true);//允许输入
	        conn.setDoOutput(true);//允许输出
	        conn.setUseCaches(false);//不使用Cache
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Connection", "Keep-Alive");
	        conn.setRequestProperty("Charset", "UTF-8");
	        conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);

	        StringBuilder sb = new StringBuilder();

	        //上传的表单参数部分，格式请参考文章
	        for (Map.Entry<String, String> entry : params.entrySet()) {//构建表单字段内容
	            sb.append("--");
	            sb.append(BOUNDARY);
	            sb.append("\r\n");
	            sb.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\"\r\n\r\n");
	            sb.append(entry.getValue());
	            sb.append("\r\n");
	        }

	        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
	        outStream.write(sb.toString().getBytes());//发送表单字段数据

	       FileInputStream fis = null;
	        //上传的文件部分，格式请参考文章
	        for(FormFile file : files){
	            StringBuilder split = new StringBuilder();
	            split.append("--");
	            split.append(BOUNDARY);
	            split.append("\r\n");
	            split.append("Content-Disposition: form-data;name=\""+ file.getFormname()+"\";filename=\""+ file.getFilname() + "\"\r\n");
	            split.append("Content-Type: "+ file.getContentType()+"\r\n\r\n");
	            outStream.write(split.toString().getBytes());
	            outStream.write(file.getData(), 0, file.getData().length);
	            outStream.write("\r\n".getBytes());
	        }
	        byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();//数据结束标志
	        outStream.write(end_data);
	        outStream.flush();

	        int cah = conn.getResponseCode();
	       Log.i("userinfo", ""+cah);

	        if (cah != 200) throw new RuntimeException("请求url失败");
	        InputStream is = conn.getInputStream();
	        InputStreamReader isr = new InputStreamReader(is, "utf-8");//解决乱码问题，保证服务器端返回数据正常显示
	        BufferedReader br = new BufferedReader(isr);
	        String result = br.readLine();
	        outStream.close();
	        conn.disconnect();
	        return result;
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

	/**
	   * 读取源文件内容
	   * @param filename String 文件路径
	   * @throws IOException
	   * @return byte[] 文件内容
	   */
	  public static byte[] readFile(String filename) {
		    File file =new File(filename);
		    long len = file.length();
		    Log.i("userinfo", "file.length():"+len);
		    byte[] bytes = new byte[(int)len];

		    BufferedInputStream bufferedInputStream;
			try
			{
				bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
				int r = bufferedInputStream.read( bytes );
				if (r != len){ bufferedInputStream.close();}
			} catch (FileNotFoundException e1)
			{
				e1.printStackTrace();
			} catch (IOException e){
				e.printStackTrace();
			}

		    return bytes;

	  }


	/**
	 * 从Uri中获取文件路径
	 */
	public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
		final String scheme = uri.getScheme();
		String data = null;
		if ( scheme == null )
			data = uri.getPath();
		else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
			data = uri.getPath();
		} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
			Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
					if ( index > -1 ) {
						data = cursor.getString( index );
					}
				}
				cursor.close();
			}
		}
		return data;
	}


	/**
	 * 从相册中获取到的uri得到BitMap并进行压缩
	 *
	 * @param data1 uri
	 * @return 压缩后的BitMap
	 */
	public static Bitmap getBitMapFromUri(Context context ,Uri data1) {
		InputStream input = null;
		try {
			input = context.getContentResolver().openInputStream(data1);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			//options.inPreferredConfig = Bitmap.Config.RGB_565 ;
			Rect rect = new Rect(0, 0, 0, 0);

			Bitmap bitmap = BitmapFactory.decodeStream(input, rect, options);
			input.close();


			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
