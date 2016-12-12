package io.manasobi.reactive.streams.frp_02;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.function.BiFunction;
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
public class Main_reduceOp {

    public static void main(String[] args) {

        Publisher<Integer> pub = iterPub(Stream.iterate(1, i -> i + 1).limit(10).collect(toList()));
        Publisher<Integer> mapOp = mapOp(pub, e -> e * 10);
        Publisher<Integer> reduceOp = reduceOp(mapOp, 0, (a, b) -> a + b);
        reduceOp.subscribe(logSub());

    }

    public static <T> Publisher<T> iterPub(Iterable<T> iter) {
        return subscriber -> subscriber.onSubscribe(new SubscriptionAdapter(iter, subscriber));
    }

    public static <T> Publisher<T> mapOp(Publisher<T> pub, Function<T, T> func) {
        return subscriber ->
                pub.subscribe(new SubscriberAdapter<T>(subscriber) {
                    @Override
                    public void onNext(T t) {
                        subscriber.onNext(func.apply(t));
                    }
                });
    }

    public static <T> Publisher reduceOp(Publisher<T> pub, T init, BiFunction<T, T, T> func) {
        return subscriber ->
            pub.subscribe(new SubscriberAdapter<T>(subscriber) {

                T result = init;

                @Override
                public void onNext(T t) {
                    result = func.apply(result, t);
                }

                @Override
                public void onComplete() {
                    subscriber.onNext(result);
                    subscriber.onComplete();
                }
            });
    }

    private static Subscriber<Integer> logSub() {
        return new LogSubAdapter();
    }

}
