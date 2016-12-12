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
public class Main_mapOp {

    public static void main(String[] args) {

        Publisher<Integer> pub = iterPub(Stream.iterate(1, i -> i + 1).limit(10).collect(toList()));
        Publisher<Integer> mapOp = mapOp(pub, e -> e * 10);
        Publisher<Integer> mapOp2 = mapOp(mapOp, e -> e + 1);
        mapOp2.subscribe(logSub());

    }

    public static Publisher<Integer> iterPub(Iterable<Integer> iter) {
        return subscriber -> subscriber.onSubscribe(new SubscriptionAdapter(iter, subscriber));
    }

    public static Publisher<Integer> mapOp(Publisher<Integer> pub, Function<Integer, Integer> func) {
        return subscriber ->
            pub.subscribe(new SubscriberAdapter(subscriber) {
                @Override
                public void onNext(Integer i) {
                    subscriber.onNext(func.apply(i));
                }
            });
    }

    private static Subscriber<Integer> logSub() {
        return new LogSubAdapter();
    }

}
