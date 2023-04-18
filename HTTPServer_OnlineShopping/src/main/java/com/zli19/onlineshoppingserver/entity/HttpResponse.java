
package com.zli19.onlineshoppingserver.entity;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zhiku
 */
public class HttpResponse {

    public static final String NOT_FOUND = "404 Not Found"; 
    public static final String NOT_IMPLEMENTED = "501 Not Implemented";
    private String status = "200 OK";       
    private String protocol = "HTTP/1.1";
    private Map<String,String> header = new HashMap<>();
    private byte[] body;
    

    public HttpResponse() {
    }
       

    public void writeResponse(OutputStream outputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(protocol)
                .append(" ")
                .append(status)
                .append("\r\t");
        
        for(Map.Entry<String, String>  entry : header.entrySet()){
            sb.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append("\r\t");
        }
        sb.append("\r\t");
        
        outputStream.write(sb.toString().getBytes());
        outputStream.flush();
        if(body != null){
            outputStream.write(body);
            outputStream.flush();
        }
    }

    public void set200OKResponse(byte[] content){
        this.addHeader("Content-Type", "text/html;charset=UTF-8");
        this.addHeader("Content-Length", Integer.toString(content.length));
        this.addHeader("Date", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        this.setBody(content);
    }
    
    public void set302FoundResponse(String url){
        this.setStatus("302 Found");
        this.addHeader("location", url);
//        this.addHeader("Date", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
    
    public void set404NotFoundResponse(byte[] content) {
        this.setStatus("404 Not Found");
        this.addHeader("Content-Type", "text/html;charset=UTF-8");
        this.addHeader("Content-Length", Integer.toString(content.length));
        this.addHeader("Date", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        this.setBody(content);
    }

    
    public Map<String, String> getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
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

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    
    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "HttpResponse{" + "status=" + status + ", protocol=" + protocol + ", header=" + header + ", body=" + body + '}';
    }


   
}
