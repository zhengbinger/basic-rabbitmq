package com.rabbitmq.boot.controller;

import com.rabbitmq.boot.service.BootConsumer;
import com.rabbitmq.boot.service.BootProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengbing
 * @date 2020/12/11
 * @email mydreambing@126.com
 */
@RestController
public class TestController {

    @Autowired
    private BootProducer bootProducer;

    @Autowired
    private BootConsumer bootConsumer;

    @GetMapping("/test")
    public void test() {
        bootProducer.send2Topic();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
