package com.harshalworks.fdm.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.harshalworks.fdm.FDMConstants;

public class FileIO {

	public static long[][] getSizeDistribution(long fileSize,int parts){
		long[][] partitions = new long[parts][2];
		int i=0;
		long prev = 0, counter = 0;
		do{
			counter += fileSize/parts;
			if(i==parts-1){
				partitions[i][0] = prev;
				partitions[i][1] = fileSize;
			}
			else{
				partitions[i][0] = prev;
				partitions[i][1] = counter;
			}
			i++;
			prev = counter + 1;
		}while(counter<fileSize && i<parts);
		return partitions;
	}
	

	
	public static long getFileSize(URL url) throws IOException{
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		try{
			connection.connect();
			return connection.getContentLengthLong();
		}finally {
			connection.disconnect();			
		}
	}
	
	
	public static String getReadableFileSize(long bytes){
		if(bytes < 1024) return bytes + " Bytes";
		int exp = (int) (Math.log10(bytes)/ Math.log10(1024));
		String postSymbol[] = {"KiloBytes","MegaBytes","GigaBytes","TeraBytes","ExaBytes"};
		return String.format("%.2f %s", bytes/Math.pow(1024, exp),postSymbol[exp-1]);
	}
	
	
	public static FileTask getFileDetails(String fileName, String path)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		path = resolvePath(path + "/" + fileName + "." + FDMConstants.FILE_EXT);
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
		ois.close();
		return (FileTask) ois.readObject();		
	}

	
	public static void saveFileDetails(FileTask fileDetails, String path) throws IOException {
		path = resolvePath(path);
		FileOutputStream fos = new FileOutputStream(new File(path + "/" + fileDetails.getFileName() + "." + FDMConstants.FILE_EXT));
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(fileDetails);
		oos.close();
	}

	
	public static String resolvePath(String path) {
		if (path.length() > 0 && path.startsWith("~")) {
			path.replaceFirst("^~", System.getProperty("user.home"));
		}
		return path;
	}
	
	public static String timeToDownload(long millis){
		if(millis<1000){
			return millis + " milliseconds";
		}
		if(millis<6E4 && millis>=1000){
			return millis/1E3 + " seconds";
		}
		
		return (millis/1E3)/60 + " minutes";
	}

	
}
