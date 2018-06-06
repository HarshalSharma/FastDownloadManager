package com.harshalworks.fdm.file;

import java.io.Serializable;
import java.util.List;

public class FileTask implements Serializable {

	private static final long serialVersionUID = 1L;

	String fileName;	
	long totalSize;
	boolean isDownloaded;
	boolean isPaused;
	boolean isMerged;
	List<FilePart> fileParts;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getTotalSize() {
		return totalSize;
	}
	
	public String getReadableTotalSize(){
		return FileIO.getReadableFileSize(totalSize);
	}

	public void setTotalSize(long totalSize) {		
		this.totalSize = totalSize;
	}

	public boolean isDownloaded() {
		return isDownloaded;
	}

	public void setDownloaded(boolean isDownloaded) {
		this.isDownloaded = isDownloaded;
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}

	public boolean isMerged() {
		return isMerged;
	}

	public void setMerged(boolean isMerged) {
		this.isMerged = isMerged;
	}

	public List<FilePart> getFileParts() {
		return fileParts;
	}

	public void setFileParts(List<FilePart> fileParts) {
		this.fileParts = fileParts;
	}

}
