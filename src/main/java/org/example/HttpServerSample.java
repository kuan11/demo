package org.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

/**
 * Hello world!
 *
 */
public class HttpServerSample {

    private static void serverStart() throws IOException {
        HttpServerProvider provider = HttpServerProvider.provider();
        // 监听端口8080,连接排队队列，如果队列中的连接超过这个数的话就会拒绝连接
        HttpServer httpserver =provider.createHttpServer(new InetSocketAddress(8080), 100);
        // 监听路径为test，请求处理后回调RestGetHandler里的handle方法
        httpserver.createContext("/test", new RestGetHandler());
        // 管理工作线程池
        ExecutorService executor = new ThreadPoolExecutor(10,200,60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        httpserver.setExecutor(executor);
        httpserver.start();
        System.out.println("server started");
    }

    public static void main(String[] args) throws IOException {
        serverStart();
    }
}
