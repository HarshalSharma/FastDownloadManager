package com.harshalworks.fdm;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.harshalworks.fdm.download.Downloader;
import com.harshalworks.fdm.download.ProgressDetail;
import com.harshalworks.fdm.file.FileIO;
import com.harshalworks.fdm.file.FilePart;
import com.harshalworks.fdm.file.FileTask;
import com.harshalworks.fdm.merge.Merger;

public class Manager {
	
	private final static Logger _logger  = Logger.getLogger("FDM-MANAGER");
	

	public void downloadFile(URL url, String fileName, int connections, long timeout, TimeUnit timeoutUnit)
			throws InterruptedException, IOException, ExecutionException {

		long start = System.currentTimeMillis();
		FileTask task = new FileTask();
		task.setFileName(fileName);
		ExecutorService workers = Executors.newFixedThreadPool(connections);

		try {

			long totalSize = FileIO.getFileSize(url);
			task.setTotalSize(totalSize);

			long[][] ranges = FileIO.getSizeDistribution(totalSize, connections);

			List<Downloader> downloadableParts = new ArrayList<>();

			for (int i = 0; i < ranges.length; i++) {
				downloadableParts.add(new Downloader(url, i, ranges[i][0], ranges[i][1], fileName + ".part" + i));
			}

			if(FDMConstants.ENABLE_PROGRESS){
				ProgressDetail.totalBytes = totalSize;
				ProgressDetail.totalDownloadedBytes = 0;
			}
			
			List<Future<FilePart>> results = workers.invokeAll(downloadableParts);
			workers.shutdown();
						
			workers.awaitTermination(timeout, timeoutUnit);
			
			List<FilePart> list = new ArrayList<>();
			for(Future<FilePart> result : results){
				list.add(result.get());
			}
			
			if(FDMConstants.ENABLE_LOGGING)
			_logger.info("Now merging these parts.....");

			new Merger(list, task.getFileName()).merge();
			
			task.setMerged(true);

			if(FDMConstants.ENABLE_LOGGING)
			_logger.info("File Merge Completed.");

			long end = System.currentTimeMillis();

			if(FDMConstants.ENABLE_LOGGING | FDMConstants.ENABLE_PROGRESS){
				ProgressDetail.isDownloading = false;
			    System.out.flush();
				System.out.println();				
				System.out.printf("Download competed in time: %s \n", FileIO.timeToDownload(end-start));				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(FDMConstants.ENABLE_PROGRESS){
				ProgressDetail.isDownloading = false;
			}
			if(FDMConstants.ENABLE_LOGGING)
			_logger.info("Program Finished!");
			// FileIO.saveFileDetails(task,fileName + ".dlTask");
		}

	}
	

}
