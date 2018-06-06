package com.harshalworks.fdm.merge;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.logging.Logger;

import com.harshalworks.fdm.FDMConstants;
import com.harshalworks.fdm.file.FilePart;

public class Merger{

	private final static Logger _logger  = Logger.getLogger("FDM-MERGER");

	List<FilePart> list;
	String outputName;
	
	public Merger(List<FilePart> parts,String outputName) {
		this.list = parts;
		this.outputName = outputName;
	}

	public String merge() throws Exception {
		
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputName));
		BufferedInputStream bis;
		byte[] buffer = new byte[FDMConstants.MERGER_BUFFER_SIZE];
		String finalFile = outputName;
		int bytesRead;
		for (FilePart filePart : list) {
			File file = new File(filePart.getFilePartPath());
			bis = new BufferedInputStream(new FileInputStream(file));
			do{
				bytesRead = bis.read(buffer);
				if(bytesRead!=-1)
					bos.write(buffer,0,bytesRead);
			}while(bytesRead!=-1);
			bis.close();
			if(!file.delete()){_logger.warning("Could not delete file :" + filePart.getFilePartPath());}
		}
		
		bos.close();
		return finalFile;
	}

}
