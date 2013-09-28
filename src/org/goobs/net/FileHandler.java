package org.goobs.net;

import com.sun.net.httpserver.Headers;

import java.io.File;
import java.net.URLConnection;
import java.util.HashMap;
import java.io.*;

public class FileHandler implements WebServerHandler {
	public final File file;
	public final String mimeType;
	
	//private String fileContents = null;
	//private long lastModified = 0L;
	
	public FileHandler(String toServe){
    this(new File(toServe));
  }

	public FileHandler(File toServe){
		//--Ensure File
		this.file = toServe;
		if(!this.file.exists()){
			throw new IllegalArgumentException("File does not exist: " + file);
		}
		if(!this.file.canRead()){
			throw new IllegalArgumentException("File is not readable: " + file);
		}
		//--Guess Type
		String candidateType = URLConnection.guessContentTypeFromName(file.getName());
		if(this.file.getName().endsWith(".js")){
			this.mimeType = "text/javascript";
		} else if (this.file.getName().endsWith(".css"))
        {
            this.mimeType = "text/css";
        }else if(candidateType == null){
			this.mimeType = "text/plain";
		} else {
			this.mimeType = candidateType;
		}
	}

	@Override
	public void handle(HashMap<String, String> values, WebServer.HttpInfo info, OutputStream responseBody) throws IOException {
		/*if(fileContents == null || file.lastModified() > lastModified){
			this.fileContents = Util.slurp(this.file);
			this.lastModified = file.lastModified();
		}*/
        FileInputStream input = new FileInputStream(this.file);
        byte[] buf = new byte[1024];
        int count = 0;
        while ((count = input.read(buf)) >= 0) {
            responseBody.write(buf, 0, count);
        }
	}

	@Override
	public void setHeaders(Headers responseHeaders) {
		responseHeaders.set("Content-Type", mimeType);
	}
}
