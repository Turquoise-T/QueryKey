import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.ui.context.support.UiApplicationContextUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
public class cutThread {
    private  List<String> msg = new ArrayList();
    private List<String>  splitList = null;
    private ThreadPoolConfig threadPoolConfig = new ThreadPoolConfig();

    //本类的构造函数
    public cutThread(){

    }


    public static void main(String[] args) throws InterruptedException, IOException {
        //被注释掉的是动态获取电脑可用的最大线程数，没注释的是手动设置需要的线程大小
        //int numThreads = Runtime.getRuntime().availableProcessors();
        int numThreads =10;
        //拆分数据（声明锁的数量和线程池的大小）
        CountDownLatch latch =  new CountDownLatch( numThreads );
        ExecutorService exec = Executors.newFixedThreadPool(numThreads);


        long startTime1 = System.currentTimeMillis();    //获取开始时间
        for(int i = 0; i < keywords.size(); i++) {
            int num=0;

            String keyword = keywords.get(i);
            for (int ii = 0; ii < numThreads; ii++) {
                //里面还需要填入在MyThread构造函数中定义的其他参数
                exec.submit(latch, "Threadzcy" + ii);
                num++;
            }

            latch.await();
            //long endTime=System.currentTimeMillis(); //获取结束时间
            //System.out.println("程序运行时间： "+(endTime-startTime)+"ms");

            Thread.sleep(20);


            //下面写多线程执行完之后的其他操作
            if(exec.isTerminated() == false) {


            }
        }

        //关闭线程池的位置
        exec.shutdown();
        long endTime1 = System.currentTimeMillis();    //获取结束时间
        System.out.println("程序运行时间：" + (endTime1 - startTime1 - 200) + "ms");    //输出程序运行时间
    }

}
