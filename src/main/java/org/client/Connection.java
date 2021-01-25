package org.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;

public class Connection {
    public static void main(String[] args) {
        final int num = 10;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(num);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i=0; i<num; i++){
            final int j=i;
            executorService.submit(()->{
                try {
                    System.out.println(("j=" + j));
                    cyclicBarrier.await();
                    connection();
                } catch (IOException | InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+" "+j);
            });
        }
        executorService.shutdown();
    }
    private static void connection()  throws IOException {
        URL url = new URL("http://localhost:8080/Test");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");//设置参数类型是json格式
        connection.connect();

        String s = "{\"name\":\"ck\",\"age\":\"24\",\"sex\":\"男\"}";
        OutputStream out = connection.getOutputStream();
        out.write(s.getBytes());
        out.flush();
        out.close();

        String msg = "";
        int code = connection.getResponseCode();

        System.out.println(connection.getResponseMessage());
        if (code == 200) {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                msg += line + "\n";
            }
            reader.close();
        }
        // 5. 断开连接
        connection.disconnect();

        // 处理结果
        System.out.println(msg);
    }
}
