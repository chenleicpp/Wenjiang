package com.sanshisoft.wenjiang.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.sanshisoft.wenjiang.AppContext;
import com.sanshisoft.wenjiang.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateService extends Service {
	
	public static final String FOLDER_NAME = "Wenjiang/";
	
	private String downloadUrl = null;

	private File updateDir = null;
	private File updateFile = null;

	private NotificationManager updateNotificationManager = null;
	private Notification updateNotification = null;

	private PendingIntent updatePendingIntent;

	private final static int DOWNLOAD_COMPLETE = 0;
	private final static int DOWNLOAD_FAIL = 1;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@SuppressLint("NewApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		downloadUrl = intent.getStringExtra("download_url");

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			updateDir = new File(Environment.getExternalStorageDirectory(), FOLDER_NAME);
			updateFile = new File(updateDir.getPath(), AppContext.getInstance().getAppName() + ".apk");
		} else {
			updateDir=new File(this.getFilesDir().getPath());
			updateFile= new File(updateDir.getPath(), AppContext.getInstance().getAppName() + ".apk");
		}
			
		this.updateNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		this.updateNotification = new Notification();
		updatePendingIntent = PendingIntent.getActivity(this, 0, new Intent(), 0);

		updateNotification.icon = R.drawable.ic_noti;
		updateNotification.tickerText = "开始下载";
		updateNotification.setLatestEventInfo(this, "开始下载", "0%", updatePendingIntent);
		updateNotificationManager.notify(0, updateNotification);

		new Thread(new updateRunnable(downloadUrl)).start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	class updateRunnable implements Runnable {
		Message message = updateHandler.obtainMessage();
		private String downloadUrl;

		public updateRunnable(String downloadUrl) {
			super();
			this.downloadUrl = downloadUrl;
		}

		public void run() {
			message.what = DOWNLOAD_COMPLETE;
			try {
				if (!updateDir.exists()) {
					updateDir.mkdirs();
				}
				if (!updateFile.exists()) {
					updateFile.createNewFile();
				}
				long downloadSize = downloadUpdateFile(downloadUrl, updateFile);
				if (downloadSize > 0) {
					message.what = DOWNLOAD_COMPLETE;
					updateHandler.sendMessage(message);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				message.what = DOWNLOAD_FAIL;
				updateHandler.sendMessage(message);
			}
		}
	}

	public long downloadUpdateFile(String downloadUrl, File saveFile) throws Exception {
		int downloadCount = 0;
		int currentSize = 0;
		long totalSize = 0;
		int updateTotalSize = 0;

		HttpURLConnection httpConnection = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			URL url = new URL(downloadUrl);
			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");
			if (currentSize > 0) {
				httpConnection.setRequestProperty("RANGE", "bytes=" + currentSize + "-");
			}
			httpConnection.setConnectTimeout(10000);
			httpConnection.setReadTimeout(20000);
			updateTotalSize = httpConnection.getContentLength();

			if (httpConnection.getResponseCode() == 404) {
			}

			is = httpConnection.getInputStream();
			fos = new FileOutputStream(saveFile, false);
			byte buffer[] = new byte[4096];
			int readsize = 0;
			while ((readsize = is.read(buffer)) > 0) {
				fos.write(buffer, 0, readsize);
				totalSize += readsize;
				if ((downloadCount == 0) || (int) (totalSize * 100 / updateTotalSize) - 10 > downloadCount) {
					downloadCount += 10;
					updateNotification.setLatestEventInfo(UpdateService.this, "正在下载", (int) totalSize * 100
							/ updateTotalSize + "%", updatePendingIntent);
					updateNotificationManager.notify(0, updateNotification);
				}
			}
		} finally {
			if (httpConnection != null) {
				httpConnection.disconnect();
			}
			if (is != null) {
				is.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
		return totalSize;
	}

	private Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_COMPLETE:
				try {  
					Thread.sleep(100);
		            Process p = Runtime.getRuntime().exec("chmod 755 " + updateFile);  
		            p.waitFor();
		        } catch (IOException e) {  
		            e.printStackTrace();  
		        } catch (InterruptedException e) {  
		            e.printStackTrace();  
		        }  
				updateNotification.setLatestEventInfo(UpdateService.this, "下载完成", "100%", updatePendingIntent);
				updateNotificationManager.notify(0, updateNotification);

				updateNotificationManager.cancel(0);

				Uri uri = Uri.fromFile(updateFile);
				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
				UpdateService.this.startActivity(installIntent);
				stopSelf();
				break;
			case DOWNLOAD_FAIL:
				updateNotification.setLatestEventInfo(UpdateService.this, AppContext.getInstance().getAppName(), "下载失败",
						updatePendingIntent);
				updateNotificationManager.notify(0, updateNotification);
				stopSelf();
				break;
			default:
				stopSelf();
				break;
			}
		}
	};

}
