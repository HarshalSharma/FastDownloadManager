package com.harshalworks.fdm.file;

import java.io.Serializable;

public class FilePart implements Comparable<FilePart>,Serializable{

	private static final long serialVersionUID = 1L;

	String filePartPath;
	int partNumber;
	
	long totalSize;
	long totalDownloaded;

	public FilePart(String filePartPath, int partNumber) {
		this.filePartPath = filePartPath;
		this.partNumber = partNumber;
	}
	public String getFilePartPath() {
		return filePartPath;
	}
	public void setFilePartPath(String filePartPath) {
		this.filePartPath = filePartPath;
	}
	public int getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(int partNumber) {
		this.partNumber = partNumber;
	}
	
	public long getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}
	public long getTotalDownloaded() {
		return totalDownloaded;
	}
	public void setTotalDownloaded(long totalDownloaded) {
		this.totalDownloaded = totalDownloaded;
	}
	
	
	@Override
	public int compareTo(FilePart o) {
		if(partNumber < o.partNumber)
			return -1;
		if(partNumber > o.partNumber)
			return 1;
		return 0;
	}

	
	@Override
	public String toString() {
		return filePartPath;
	}
	
	
	
}
