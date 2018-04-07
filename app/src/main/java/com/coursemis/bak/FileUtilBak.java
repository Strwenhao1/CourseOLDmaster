package com.coursemis.bak;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

/**
 * 只有HttpDownloaderBak调用的一个类
 */
public class FileUtilBak {
	private String SDCARDPATH;

	public String getSDCARDPATH() {
		return SDCARDPATH;
	}

	public FileUtilBak() {
		// 得到手机存储器目录---因为各个厂商的手机SDcard可能不一样，最好不要写死了
		SDCARDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	/**
	 * 在SDcard上创建文件
	 * 
	 * @param fileName
	 * @return File
	 */
	public File creatSDFile(String fileName) {
		File file = new File(SDCARDPATH + fileName);
		return file;
	}

	/**
	 * 在SDcard上创建目录
	 * 
	 * @param dirName
	 */
	public void createSDDir(String dirName) {
		File file = new File(SDCARDPATH + dirName);
		file.mkdir();
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param fileName
	 * @return boolean
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(SDCARDPATH + fileName);
		return file.exists();
	}

	/**
	 * @param path
	 *            存放目录
	 * @param fileName
	 *            文件名字
	 * @param input
	 *            数据来源
	 * @return
	 */
	public File writeToSDCard(String path, String fileName, InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			//创建路径
			createSDDir(path);
			//创建文件
			file = creatSDFile(path + fileName);
			output = new FileOutputStream(file);
			//以4K为单位，每次写4k
			byte buffer[] = new byte[4 * 1024];
			while ((input.read(buffer)) != -1) {
				output.write(buffer);
			}
			// 清除缓存
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
}
