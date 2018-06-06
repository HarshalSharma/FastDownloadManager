package com.harshalworks.fdm;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.harshalworks.fdm.download.ProgressDetail;

public class CommandPromptRunner {

	public static void main(String args[]) {
		if (args.length < 3) {
			exitMessage();
			return;
		}
		URL url = null;
		String filename = null;
		int parts = 0;
		try {
			url = new URL(args[0]);
			filename = args[1];
			parts = Integer.parseInt(args[2]);
			if (Arrays.toString(args).contains("--l")) {
				System.out.println("Logging is Enabled.");
				FDMConstants.ENABLE_LOGGING = true;
			}
			System.out.println("Progress is Enabled.");
			FDMConstants.ENABLE_PROGRESS = true;
			ProgressDetail.isDownloading = true;
			System.out.println("Downloading..");
		} catch (Exception e) {
			exitMessage();
			return;
		}
		Manager manager = new Manager();
		try {
			new Thread(new Runnable() {
				
				String result;
				@Override
				public void run() {
					while(ProgressDetail.isDownloading){
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						result = ProgressDetail.getProgressString();
						if(result==null){
							System.out.printf("\r%s","Connecting..");													
						}else if(ProgressDetail.isDownloading){
							System.out.printf("\r%s",ProgressDetail.getProgressString());													
						}
					}
				}
			}).start();
			
			manager.downloadFile(url, filename, parts, 1, TimeUnit.DAYS);

		} catch (InterruptedException | IOException | ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("\nThank you for using FDM, please star us on github : https://github.com/HarshalSharma/FastDownloadManager");
	}

	private static void exitMessage() {
		System.out.println("Oops! Your provided arguments are not valid.");
		System.out.println("Usage: java -jar fdm.jar 'URL' 'FILE_NAME' 'PARTS'");
		System.out.println("Flags:");
		System.out.println("-l : print logs.");
	}

}
