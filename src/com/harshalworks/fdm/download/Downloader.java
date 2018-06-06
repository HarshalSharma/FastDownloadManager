package com.harshalworks.fdm.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import com.harshalworks.fdm.FDMConstants;
import com.harshalworks.fdm.file.FilePart;

public class Downloader implements Callable<FilePart> {

	private final static Logger _logger  = Logger.getLogger("FDM-DOWNLOADER");
	
	URL url;
	long startByte;
	long endByte;
	String outputPath;
	int partNumber;
	
	public long totalBytes;
	
	public Downloader(URL url, int partNumber, long startByte, long endByte, String outputPath) {
		this.url = url;
		this.startByte = startByte;
		this.endByte = endByte;
		this.outputPath = outputPath;
	}

	@Override
	public FilePart call() throws Exception {
		// Create a connection to the given URL.
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// Setting request parameters to get only the specific part of the file.
		String byteRange = startByte + "-" + endByte;
		connection.setRequestProperty("Range", "bytes=" + byteRange);
		if(FDMConstants.ENABLE_LOGGING)
			_logger.info("Dowloading range " + byteRange + " for " + outputPath);

		FilePart part = new FilePart(outputPath, partNumber);
		part.setTotalSize(endByte-startByte);

		try {
			// making connection to the url.
			connection.connect();

			// Check if response code is valid and connection will send data.
			if (connection.getResponseCode() / 100 != 2) {
				throw new IOException("Could not connect to the stream, error code:" + connection.getResponseCode());
			}

			BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(outputPath));

			// making buffer to read file in chunks of this buffer.
			byte[] dataBuffer = new byte[FDMConstants.DOWNLOAD_BUFFER_SIZE];
			int bytes;
			int downloadedBytes = 0 ;
			do {
				bytes = bis.read(dataBuffer);
				if (bytes > 0) {
					downloadedBytes += bytes/8;
					
					// writing downloaded buffer to outputfile part.
					bos.write(dataBuffer,0,bytes);
					if(FDMConstants.ENABLE_PROGRESS){
						ProgressDetail.bytesDownloaded(bytes/8);
					}
				}				
			} while (bytes > 0);
			if(FDMConstants.ENABLE_LOGGING)
				_logger.info("Downloaded " + outputPath);
			bos.flush();
			bos.close();
			bis.close();
			part.setTotalDownloaded(downloadedBytes);
		} finally {
			connection.disconnect();
		}

		return part;
	}
	
	

}
