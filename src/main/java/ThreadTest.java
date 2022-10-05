import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest {
    private  List<String> msg = new ArrayList();
    private List<String>  spliteList = null;


    public ThreadTest(int  i) throws IOException {
        String valueString = null;
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream("src/main/resources/files/preResult.txt"));
        BufferedReader br = new BufferedReader(inputStreamReader);
        while ((valueString = br.readLine()) != null){
            msg.add(valueString.trim());
        }
        br.close();
    }

    public List<String> getMsg() {
        return msg;
    }

    public List<List<String>>  spliteList(int numThreads) throws IOException {

        int length = msg.size();
        // 计算可以分成多少组
        int num =  length/numThreads;
        List<List<String>> newList = new ArrayList<>(num);
        if(num == 0){
            newList.add(msg);
        }else{
            for (int i = 1; i <= numThreads; i++) {
                // 开始位置
                int fromIndex = (i-1)*num;
                // 结束位置
                int toIndex = i*num;
                if(i==numThreads){
                    toIndex = i*num+ length%numThreads;
                }

                newList.add(msg.subList(fromIndex,toIndex)) ;
            }
        }

        return  newList ;
    }

    public void customerMsg(List<String> splitMsgList){
        for(String str : splitMsgList){
            System.out.println(Thread.currentThread().getName()+"----"+str);
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) throws InterruptedException, IOException {
        int numThreads = Runtime.getRuntime().availableProcessors();
        //int numThreads = 10;
        //初始化数据
        ThreadTest test2 = new ThreadTest(113);
        //拆分数据
        List<List<String>>  tt = test2.spliteList(numThreads);
        CountDownLatch latch =  new CountDownLatch( numThreads );
        final ExecutorService exec = Executors.newFixedThreadPool(numThreads);
        //多线程运行
        long startTime=System.currentTimeMillis();
        List<String> keywords = new ArrayList<>();
        keywords.add("湖南");;
        int num=0;
        for(int i = 0; i < keywords.size(); i++) {
            String keyword = keywords.get(i);
            for (int ii = 0; ii < numThreads; ii++) {
                exec.submit(new MyThread(tt.get(ii), latch, "Threadzcy" + ii, keyword, String.format("%s%s", keyword, num)));
                num++;
            }

            latch.await();
            exec.shutdown();
            long endTime=System.currentTimeMillis(); //获取结束时间
            System.out.println("程序运行时间： "+(endTime-startTime)+"ms");

            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(String.format("src/main/resources/files/searchResult/%s_complete.txt", keyword))));
            for(int ii = 0; ii < numThreads; ii++){
                File file = new File(String.format("src/main/resources/files/searchResult/%s%s.txt",keyword, ii));
                FileInputStream inputStream = new FileInputStream(file);
                BufferedInputStream in = new BufferedInputStream(inputStream);

                int len = -1;
                byte[] bt = new byte[1024*1024]; //1M every time
                while((len = in.read(bt)) != -1){
                    out.write(bt, 0, len);
                }

                in.close();
                inputStream.close();
                out.flush();
                File file_delete = new File(String.format("src/main/resources/files/searchResult/%s%s.txt",keyword,ii));
                file_delete.delete();
            }
        }

//        long startTime1=System.currentTimeMillis();
//        List<String>  allMsgList = test2.getMsg();
//        for(String str : allMsgList){
//
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }
//        long endTime1=System.currentTimeMillis(); //获取结束时间
//        System.out.println("程序运行时间： "+(endTime1-startTime1)+"ms");

    }

}
