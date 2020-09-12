package socketio1.aio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HandlerExecutorPool {
    private ExecutorService executor;

    public HandlerExecutorPool(int maxPoolSize, int queueSize) {
        this.executor = new ThreadPoolExecutor(
                // 当前服务器最大的线程数
                Runtime.getRuntime().availableProcessors(),
                maxPoolSize,
                120L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize)
        );

    }
    public void execute(Runnable task) {
        this.executor.execute(task);
    }
}
