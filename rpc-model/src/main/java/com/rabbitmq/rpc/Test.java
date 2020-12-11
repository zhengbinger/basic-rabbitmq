package com.rabbitmq.rpc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhengbing
 * @date 2020/12/10
 * @email mydreambing@126.com
 */
public class Test {

    private ExecutorService executorService = Executors.newFixedThreadPool(10);


    public static void main(String[] args) {

        try {
            RpcServer.execute();

         
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
