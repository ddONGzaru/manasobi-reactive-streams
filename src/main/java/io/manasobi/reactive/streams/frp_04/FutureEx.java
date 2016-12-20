package io.manasobi.reactive.streams.frp_04;

import jdk.nashorn.internal.codegen.CompilerConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * Created by tw.jang on 2016-12-20.
 */
@Slf4j
public class FutureEx {

    // Future
    /*public static void main(String[] args)  throws Exception{

        log.info("Start...");

        ExecutorService es = Executors.newCachedThreadPool();

        Future<String> future = es.submit(() -> {
                                    TimeUnit.SECONDS.sleep(2);
                                    log.info("Async");
                                    return "Hello";
                                });

        log.info("Exit");

        log.info(future.get());

        es.shutdown();

    }*/

    // Callback - 1
    /*public static void main(String[] args) throws Exception {

        log.info("Start...");

        ExecutorService es = Executors.newCachedThreadPool();

        FutureTask<String> f = new FutureTask<String>(() -> {
            TimeUnit.SECONDS.sleep(2);
            log.info("Async");
            return "Hello";
        }) {
            @Override
            protected void done() {
                try {
                    log.info(get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        es.execute(f);
        es.shutdown();

        log.info("Exit");
    }*/

    // Callback - 2
    interface SuccessCallback {
        void onSuccess(String result);
    }

    interface ExceptionCallback {
        void onError(Throwable t);
    }

    public static class CallbackFutureTask extends FutureTask<String> {

        private SuccessCallback sc;
        private ExceptionCallback ec;

        public CallbackFutureTask(Callable<String> callable, SuccessCallback sc, ExceptionCallback ec) {
            super(callable);
            this.sc = Objects.requireNonNull(sc);
            this.ec = Objects.requireNonNull(ec);
        }

        @Override
        protected void done() {
            try {
                sc.onSuccess(get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                ec.onError(e.getCause());
            }
        }
    }

    public static void main(String[] args) throws Exception {

        log.info("Start...");

        ExecutorService es = Executors.newCachedThreadPool();

        CallbackFutureTask f = new CallbackFutureTask(() -> {
                TimeUnit.SECONDS.sleep(2);
                if (1 == 1) throw new RuntimeException("Async ERROR!!!");
                log.info("Async");
                return "Hello";
            },
            System.out::println,
            e -> System.out.println("Error: " + e)
        );

        es.execute(f);
        es.shutdown();

        log.info("Exit");
    }

}
