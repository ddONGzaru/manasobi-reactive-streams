package io.manasobi.reactive.streams.frp_02;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.util.Collection;

/**
 * Created by tw.jang on 2016-12-12.
 */
@Slf4j
public class ReactorEx {

    public static void main(String[] args) {

        Flux.<Integer>create(e -> {
            e.next(1);
            e.next(2);
            e.next(3);
            e.complete();
        })
        .log("  pub :: ")
        .map(i -> i * 10)
        .filter(i -> i > 10)
        .reduce(0, (a, b) -> a + b)
        .log("mapOp :: ")
        .subscribe(s -> log.info("{}", s));

        Flux.<Integer>create(e -> {
            e.next(1);
            e.next(2);
            e.next(3);
            e.complete();
        })
        .log("  pub :: ")
        .buffer(2)
        .subscribe(s -> {
            s.stream().forEach(System.out::println);
            if (s instanceof Collection) System.out.println("T"); else System.out.println("F");
        });
    }
}
