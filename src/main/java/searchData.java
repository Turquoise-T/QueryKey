import java.io.*;
import java.net.URLDecoder;

public class searchData {

    public static void search(String key,String filename) throws IOException {

        //定义输入流，打开源数据集txt文件
        InputStreamReader inStream = new InputStreamReader(new FileInputStream(new File("src/main/resources/files/preResult.txt")), "UTF-8");
        //定义输出流，写入搜索到的匹配数据
        OutputStreamWriter outStream = new OutputStreamWriter(new FileOutputStream(new File(String.format("src/main/resources/files/searchResult/%s.txt",filename))), "UTF-8");
        BufferedReader br = new BufferedReader(inStream);
        BufferedWriter bw = new BufferedWriter(outStream);
        String valueString = null;
        int flag = -1;


        //多线程
//        int numThreads = Runtime.getRuntime().availableProcessors();
//        Thread[] threads = new Thread[numThreads];
//        ParallelGroupFileTask[] tasks = new ParallelGroupFileTask[numThreads];
//
//        for (int i = 0; i < numThreads; i++) {
//            tasks[i] = new ParallelGroupFileTask(filename, key);
//            threads[i] = new Thread(tasks[i]);
//            threads[i].start();
//        }
//        boolean finish = false;
//        int numFinished = 0;
//        while (!finish) {
//            numFinished = 0;
//            for (int i = 0; i < threads.length; i++) {
//                if (threads[i].getState() == Thread.State.TERMINATED) {
//                    numFinished++;
//                    if (tasks[i].isFinish(valueString,br)) {
//                        finish = true;
//                    }
//                }
//            }
//            if (numFinished == threads.length) {
//                finish = true;
//            }
//            if (numFinished != threads.length) {
//                for (Thread thread : threads) {
//                    thread.interrupt();
//                }
//            }
//        }


        //按行读取数据
        while ((valueString = br.readLine()) != null){
            flag = valueString.indexOf(key);
            if (flag != -1){
                bw.append(valueString);
                bw.newLine();
            }
        }
        bw.close();
        br.close();

    }


    public static void main(String[] args) throws IOException {
        search("湖南","hunan");
    }
}
