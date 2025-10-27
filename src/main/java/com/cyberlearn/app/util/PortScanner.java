package com.cyberlearn.app.util;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class PortScanner {
    public static class Result {
        public final int port;
        public final boolean open;

        public Result(int port, boolean open) {
            this.port = port;
            this.open = open;
        }
    }

    public static List<Result> scan(String host, int start, int end, int timeoutMs, int threads) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(Math.max(1, threads));
        List<Future<Result>> futures = new ArrayList<>();
        for (int p = start; p <= end; p++) {
            final int port = p;
            futures.add(pool.submit(() -> {
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(host, port), timeoutMs);
                    return new Result(port, true);
                } catch (Exception e) {
                    return new Result(port, false);
                }
            }));
        }
        List<Result> results = new ArrayList<>();
        for (Future<Result> f : futures) {
            try {
                results.add(f.get());
            } catch (ExecutionException e) {
                results.add(new Result(-1, false));
            }
        }
        pool.shutdownNow();
        return results;
    }
}
