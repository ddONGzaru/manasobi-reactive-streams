package io.manasobi.reactive.streams.frp_03;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Created by tw.jang on 2016-12-12.
 */
@Slf4j
public class FluxSchedulerEx {

    // daemon 쓰레드는 user 쓰레드가 0이면 강제로 daemon 쓰레드를 종료한다.
    // user 쓰레드는 스스로는 종료하지 않는다.
    public static void main(String[] args) {

        /*Flux.range(1, 10)
            .publishOn(Schedulers.newSingle("pub"))
            .log()
            //.subscribeOn(Schedulers.newSingle("sub"))
            .subscribe(System.out::println);

        System.out.println("exit");*/

/*        Flux.interval(Duration.ofSeconds(1))
            .filter(i -> i % 2 == 0)
            .subscribe(e -> log.info(e.toString()));*/

        Flux.interval(Duration.ofMillis(100))
            .take(10)
            .subscribe(e -> log.info(e.toString()));

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.debug("exit");

    }
}
