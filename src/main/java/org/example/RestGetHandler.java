package org.example;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class RestGetHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange he) throws IOException {
        String requestMethod = he.getRequestMethod();
        // 如果是get方法
        if ("GET".equalsIgnoreCase(requestMethod)) {
            // 获取响应头，接下来我们来设置响应头信息
            Headers responseHeaders = he.getResponseHeaders();
            // 以json形式返回，其他还有text/html等等
            responseHeaders.set("Content-Type", "application/json");
            // 设置响应码200和响应body长度，这里我们设0，没有响应体
            he.sendResponseHeaders(200, 0);
            // 获取响应体
            OutputStream responseBody = he.getResponseBody();
            // 获取请求头并打印
            Headers requestHeaders = he.getRequestHeaders();
            Set<String> keySet = requestHeaders.keySet();
            Iterator<String> iter = keySet.iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                List values = requestHeaders.get(key);
                String s = key + " = " + values.toString() + "\r\n";
                responseBody.write(s.getBytes());
            }
            // 关闭输出流
            responseBody.close();
        }
    }
}