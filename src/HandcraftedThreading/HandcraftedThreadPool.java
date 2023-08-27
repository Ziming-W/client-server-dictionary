// Author: Ziming Wang (1180051) https://github.com/Ziming-W
package HandcraftedThreading;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class HandcraftedThreadPool {
    private BlockingQueue<Runnable> taskQueue;
    private WorkerThread[] workerPool;
    private final int poolSize;

    public HandcraftedThreadPool(int poolSize) {
        this.poolSize = poolSize;
        this.workerPool = new WorkerThread[poolSize];
        this.taskQueue = new LinkedBlockingQueue<>();
        for(int i = 0; i < poolSize; i++){
            workerPool[i] = new WorkerThread(taskQueue);
            new Thread(workerPool[i]).start();
        }
    }

    /**
     * submit a runnable task, put it into the task queue
     * @param task runnable task
     */
    public void submitTask(Runnable task){
        try{
            taskQueue.put(task);
        }
        catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    /**
     * shutdown all threads in the workerpool
     */
    public void shutdownAll(){
        for(WorkerThread workerthread:workerPool){
            workerthread.interrupt();
        }
    }
}
