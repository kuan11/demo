package org.example2;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class HttpServerTest {
    public static void main(String[] args) throws Exception {
        // 创建 http 服务器, 绑定本地 8080 端口,请求队列； 队列设置成0则使用默认值：50
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

        // 创建上下文监听, 处理 URI 以 "/aa" 开头的请求
        httpServer.createContext("/Test", new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                
                InputStream in = httpExchange.getRequestBody();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in));
                String msg= "";
                String line;

                while ((line = reader.readLine()) != null) {
                    msg += line + "\n";
                }
                System.out.println(msg);

                String s = "{\"name\":\"kuan\",\"age\":\"24\",\"sex\":\"男\"}";
                // 响应内容
                byte[] respContents = s.getBytes();
                // 设置响应头
                //httpExchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
                httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
                // 设置响应code和内容长度
                httpExchange.sendResponseHeaders(200, respContents.length);
                // 设置响应内容
                httpExchange.getResponseBody().write(respContents);
                // 关闭处理器
                httpExchange.close();
            }
        });
        // 管理工作线程池
        /*
        ExecutorService executor = new ThreadPoolExecutor(10,200,60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        httpServer.setExecutor(executor);
        */
        ExecutorService service = Executors.newCachedThreadPool();
        httpServer.setExecutor(service);
        // 启动服务
        httpServer.start();
    }
}
