package io.manasobi.reactive.streams.frp_02;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * Created by tw.jang on 2016-12-12.
 */

/*
    Reactive Streams -

*/
@Slf4j
public class PubSub_FRP_02 {

    public static void main(String[] args) {

        Iterable<Integer> intIter = Stream.iterate(1, i -> i + 1).limit(10).collect(toList());

        Publisher<Integer> pub = sub -> sub.onSubscribe(
            new Subscription() {
                @Override
                public void request(long n) {
                    try {
                        intIter.forEach(s -> sub.onNext(s));
                        sub.onComplete();
                    } catch (Exception e) {
                        e.printStackTrace();
                        sub.onError(e);
                    }
                }

                @Override
                public void cancel() {

                }
            }
        );

        Subscriber<Integer> sub = new Subscriber<Integer>() {
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

        pub.subscribe(sub);

    }

}
