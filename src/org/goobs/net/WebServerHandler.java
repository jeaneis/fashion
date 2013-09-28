package org.goobs.net;

import com.sun.net.httpserver.Headers;

import java.util.HashMap;
import java.io.*;

public interface WebServerHandler {
	public void handle(HashMap<String,String> values, WebServer.HttpInfo info, OutputStream responseBody) throws IOException;
  public void setHeaders(Headers responseHeaders);
}
