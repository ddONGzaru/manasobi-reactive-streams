package io.manasobi.reactive.streams.frp_02;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * Created by tw.jang on 2016-12-12.
 */

/*
    Reactive Streams - Operators

    Publisher -> [Data1] -> Op1 -> [Data2] -> Op2-> [Data3] -> Subscriber

    1. map (d1 -> f -> d2)
    pub -> [data1] -> mapOp -> [data2] -> logSub
                   <- subscribe(logSub)
                   -> onSubscribe(s)
                   -> onNext
                   -> onNext
                   -> onComplete

*/
@Slf4j
public class Main {

    public static void main(String[] args) {

        Publisher<Integer> pub = iterPub(Stream.iterate(1, i -> i + 1).limit(10).collect(toList()));
        Publisher<Integer> mapOp = mapOp(pub, e -> e * 10);
        mapOp.subscribe(logSub());

    }

    public static Publisher<Integer> iterPub(Iterable<Integer> iter) {
        return subscriber -> subscriber.onSubscribe(
            new Subscription() {
                @Override
                public void request(long n) {
                    try {
                        iter.forEach(s -> subscriber.onNext(s));
                        subscriber.onComplete();
                    } catch (Exception e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }
                }

                @Override
                public void cancel() {

                }
            }
        );
    }

    public static Publisher<Integer> mapOp(Publisher<Integer> pub, Function<Integer, Integer> func) {
        return subscriber ->
            pub.subscribe(new Subscriber<Integer>() {
                @Override
                public void onSubscribe(Subscription s) {
                    subscriber.onSubscribe(s);
                }

                @Override
                public void onNext(Integer integer) {
                    subscriber.onNext(func.apply(integer));
                }

                @Override
                public void onError(Throwable t) {
                    subscriber.onError(t);
                }

                @Override
                public void onComplete() {
                    subscriber.onComplete();
                }
            });

    }

    private static Subscriber<Integer> logSub() {
        return new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.info("Subscriber.onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                log.info("Subscriber.onNext :: {}", integer);
            }

            @Override
            public void onError(Throwable t) {
                log.info("Subscriber.onError :: {}", t.getMessage());
            }

            @Override
            public void onComplete() {
                log.info("Subscriber.onComplete");
            }
        };
    }

}
