
package com.zli19.onlineshoppingserver.entity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zhiku
 */
public class HttpRequest {
    private String method;
    private String path;
    private Map<String,String> parameters = new HashMap<>();
    private String protocol;
    private Map<String,String> header = new HashMap<>();
    private String body;

    public HttpRequest() {
    }
    
    public void parseRequest(InputStream is) throws IOException{
        byte[] buffer = new byte[1024];
        StringBuilder sb = new StringBuilder();
        
        // Use do while loop to make the server block at the read method 
        // in case the client haven't sent a message yet.
        do{
            int length = is.read(buffer);
            sb.append(new String(buffer, 0, length));
        }while(is.available() != 0);

        String[] headAndBody = sb.toString().split("\r\n\r\n");
        
        String[] lines = headAndBody[0].split("\r\n");
        
        String[] startLine = lines[0].split(" ");
        setMethod(startLine[0]);
        String[] pathAndParas = startLine[1].split("\\?");
        setPath(pathAndParas[0]);
        if(pathAndParas.length > 1){
            String result = URLDecoder.decode(pathAndParas[1],
                    Charset.defaultCharset());
            String[] paras = result.split("&");
            for(String pair : paras){
                String[] split = pair.split("=");
                addParameter(split[0], 
                        split.length > 1 ? split[1] : "");  
            }
        }
        setProtocol(startLine[2]);
        
        if(lines.length > 1){
            for(int i = 1; i < lines.length; i++){
                String[] line = lines[i].split(":");
                addHeader(line[0], line[1].strip());
            }   
        }
        if(headAndBody.length > 1){
            if("POST".equals(startLine[0])){
                String result = URLDecoder.decode(headAndBody[1],
                        Charset.defaultCharset());
                String[] paras = result.split("&");
                for(String pair : paras){
                    String[] split = pair.split("=");
                    addParameter(split[0],
                            split.length > 1 ? split[1] : "");  
                }
            }else{
                setBody(headAndBody[1]);
            }
        }
         
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setProtocol(String protocolVersion) {
        this.protocol = protocolVersion;
    }

    public void setHeader(Map<String, String> headers) {
        this.header = headers;
    }
    
    public void addHeader(String key, String value){
        this.header.put(key, value);
    }
    
    public String getHeaderValue(String key){
        return this.header.get(key);
    }

    public void setBody(String data) {
        this.body = data;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
    
    public void addParameter(String key, String value){
        this.parameters.put(key, value);
    }
    
    public String getParameterValue(String key){
        return this.parameters.get(key);
    }

    @Override
    public String toString() {
        return "HttpRequest{" + "method=" + method + ", path=" + path + ", parameters=" + parameters + ", protocolVersion=" + protocol + ", header=" + header + ", body=" + body + '}';
    }

    public String getCookieAttributeValue(String key) {
        String headerValue = this.getHeaderValue(key);
        if(headerValue == null){
            return null;
        }
        String[] pairs = headerValue.split(";");
        for(String pair : pairs){
            String[] entry = pair.split("=");
            if(key.equals(entry[0].strip())){
                return entry.length > 1 ? entry[1].strip() : "";
            }
        }
        return null;
    }

  

}
