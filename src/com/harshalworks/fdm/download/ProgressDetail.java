package com.harshalworks.fdm.download;

public class ProgressDetail {
	
	private static Object lock = new Object();
	
	public static long totalDownloadedBytes = 0;
	public static long totalBytes = Long.MAX_VALUE;
	public static boolean isDownloading = false;
	
	private static long lastAdded = -1;
	
	
	//bytes per milliseconds.
	private static double speed = 0;
	private static int per = 0;
	
	static long temp;
	public static void bytesDownloaded(long bytes){
		synchronized (lock) {			
		if(lastAdded==-1){
			lastAdded = System.currentTimeMillis();
		}else{
			temp = System.currentTimeMillis();
			speed = bytes/(temp-lastAdded + 1);
			lastAdded = System.currentTimeMillis();
		}
		
			totalDownloadedBytes += bytes;
			per = (int)(totalDownloadedBytes * 100.0 / totalBytes + 0.5);
			downloadString = String.format("(%d)%% downloaded | last speed:%s ",per,speed + " KB/s");
		}
	}
	
	static String downloadString;
	
	public static String getProgressString(){
		synchronized (lock) {
			return downloadString;
		}
	}	
}
