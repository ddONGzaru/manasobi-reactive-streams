package io.manasobi.reactive.streams.frp_04;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by tw.jang on 2016-12-20.
 *
 * 토비의 봄 TV 8회 - 스프링 리액티브 웹 개발 4부. 자바와 스프링의 비동기 개발 기술 살펴보기.
 *
 */

@Slf4j
@SpringBootApplication
@EnableAsync
public class FutureExSpring {

    @Component
    public static class MyService {

        @Async
        public Future<String> hello() throws InterruptedException {

            log.info("hello()");

            TimeUnit.SECONDS.sleep(1);
            return new AsyncResult<>("HELLO");
        }
    }

    public static void main(String[] args) {
        try(ConfigurableApplicationContext c = SpringApplication.run(FutureExSpring.class, args)) {

        }
    }

    @Autowired
    MyService myService;

    @Bean
    ApplicationRunner run() {
        return args -> {
            log.info("run()");
            Future<String> result = myService.hello();
            log.info("result:: {}", result.get());
            log.info("Exit...");
        };
    }


}
