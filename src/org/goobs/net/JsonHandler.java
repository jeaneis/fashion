package org.goobs.net;

import com.sun.net.httpserver.Headers;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.io.*;

/**
 * @author Gabor Angeli (angeli at cs.stanford)
 */
public abstract class JsonHandler implements WebServerHandler {
  public abstract String handleJSON(HashMap<String,String> values, WebServer.HttpInfo info);

  @Override
  public final void handle(HashMap<String, String> values, WebServer.HttpInfo info, OutputStream responseBody) throws IOException {
    String callback = null;
    if(values.containsKey("callback")){
      callback = values.get("callback");
      values.remove("callback");
    }else if(values.containsKey("jsoncallback")){
      callback = values.get("jsoncallback");
      values.remove("jsoncallback");
    }

    String response;
    if (callback == null) {
      response = handleJSON(values, info);
    } else {
      response = ""+callback+"(\n"+handleJSON(values, info)+"\n)";
    }
    responseBody.write(response.getBytes());
  }

  @Override
  public void setHeaders(Headers responseHeaders) {
    responseHeaders.set("Content-Type", "application/json");
    responseHeaders.set("Cache-Control", "no-cache");
    responseHeaders.set("Access-Control-Allow-Origin", "*");
    responseHeaders.set("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
    responseHeaders.set("Access-Control-Max-Age", "1000");
    responseHeaders.set("Access-Control-Allow-Headers", "origin, x-csrftoken, content-type, accept, X-Requested-With");
  }

	protected static class JSONBuilder{
		StringBuilder b = new StringBuilder().append("{\n");
		public JSONBuilder(){ }
		
		@SuppressWarnings("unchecked")
		private void handle(StringBuilder b, Object o){
			if(o == null){
				b.append("undefined");
			} else if(o instanceof CanJSON){
				b.append(((CanJSON) o).toJSON());
			} else if(o instanceof Map){
				JSONBuilder sub = new JSONBuilder();
				for(Entry e: ((Map<Object,Object>) o).entrySet()){
					sub.append(e.getKey().toString(), e.getValue());
				}
				b.append(sub.toString().replaceAll("\"","\\\""));
			} else if(o instanceof Number){
				b.append(o.toString());
			} else {
				b.append("\"").append(o.toString()).append("\"");
			}
		}

		public JSONBuilder append(String key, Object value){
			b.append("\t\"").append(key).append("\": ");
			handle(b,value);
			b.append(",\n");
			return this;
		}
		public <E> JSONBuilder appendList(String key, Iterable<E> values){
			b.append("  \"").append(key).append("\": [\n\t\t");
			for(E o : values){
				handle(b,o);
				b.append(",\n");
			}
			b.append("\t],\n");
			return this;
		}

		public String toString(){
			return b.toString() + "}";
		}
	}
	
	protected static String JSON(Object... alternatingArgs){
		JSONBuilder b = new JSONBuilder();
		for(int i=0; i<alternatingArgs.length; i += 2){
			b.append(alternatingArgs[i].toString(), alternatingArgs[i+1]);
		}
		return b.toString();
	}

	protected static String ERROR = JSON("msg", "general JSON error");
	
	protected static String error(String msg){ return JSON("msg", msg); }
	protected static String error(Exception e){ return JSON("msg", ""+e.getClass().getSimpleName() + " " + e.getMessage()); }
}
