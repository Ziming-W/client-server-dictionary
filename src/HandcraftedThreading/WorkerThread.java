// Author: Ziming Wang (1180051) https://github.com/Ziming-W
package HandcraftedThreading;

import java.util.concurrent.BlockingQueue;

public class WorkerThread extends Thread{
    private BlockingQueue<Runnable> taskQueue;

    public WorkerThread(BlockingQueue<Runnable> taskQueue){
        super();
        this.taskQueue = taskQueue;
    }

    public void run(){
        while(!isInterrupted()){
            try{
                taskQueue.take().run();
            }
            catch(InterruptedException e){
                interrupt();
            }
        }
    }
}
