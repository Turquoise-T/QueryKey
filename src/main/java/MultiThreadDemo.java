import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class MultiThreadDemo {
    private List<String> msg = new ArrayList<>();
    private List<String> splitList = null;

    public List<String> getMsg(){
        return msg;
    }

    public MultiThreadDemo(int i){
        while(i>0){
            msg.add(i+"");
            i--;
        }
    }

    public List<List<String>> splitList(int listsNumber){
        /*
        * 本函数用于将从文件中得到的数据进行划分
        * listsNumber: int 要把数据分为几部分，对应后续处理的线程数
        * dataLists: List<List<String>>: 分割之后得到的文件，其中每一个list包含的是分割后的一部分文件
        * */
        int len = msg.size();
        int groupSize = len / listsNumber;
        List<List<String>> dataLists = new ArrayList<>(listsNumber);
        if(listsNumber == 0){
            dataLists.add(msg);
        }else{
            for(int i = 1; i < groupSize; i++){
                int begin = (i - 1) * listsNumber;
                int end = i * listsNumber;
                if(i == groupSize){
                    end = i * listsNumber + len % groupSize;
                }

                dataLists.add(msg.subList(begin, end));
            }
        }
        return dataLists;
    }

    public void customerMsg(List<String> splitMsgList){
        for(String str: splitMsgList){
            System.out.println(Thread.currentThread().getName() + "------" + str);
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException{
        int listsNum = 5;
        String keyword = null;
        MultiThreadDemo multiThreadDemo = new MultiThreadDemo(120);
        List<List<String>> tt = multiThreadDemo.splitList(listsNum);
        CountDownLatch latch = new CountDownLatch((listsNum));
        // thread pool
        final ExecutorService executorService = Executors.newFixedThreadPool(5);
        long startTime = System.currentTimeMillis();
        for(int i = 0; i < 5; i++){
            executorService.submit(new MyThread(tt.get(i), latch, "Thread"+i, keyword));
        }
        latch.await();
        executorService.shutdown();
        long endTime = System.currentTimeMillis();
        System.out.println("process running for: " + (endTime - startTime) + "ms");

    }
}

class MyThread implements Runnable {
    private List<String> msgList = null;
    private CountDownLatch latch = null;
    private String name;
    private String keyword;

    public MyThread(List<String> msgList, CountDownLatch latch, String name, String keyword){
        this.msgList = msgList;
        this.latch = latch;
        this.name = name;
        this.keyword = keyword;
    }

    @Override
    public void run() {
        for(String str : msgList){
            System.out.println(this.name + " is running");
            try{
                Thread.sleep(100);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        latch.countDown();
    }
}

