import java.io.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class myCutThread implements Runnable{
    private CountDownLatch latch = null;
    private String name = null;

    //每个线程的构造函数，默认必传入的是线程锁和线程名（不需要别的操作），需要传入的其他参数参照latch的方式引入
    public myCutThread(CountDownLatch latch, String name){
        this.latch = latch;
        this.name = name;
    }
    @Override
    public void run() {
        //单个线程执行的任务在这里写



        latch.countDown();
    }

}
