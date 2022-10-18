import java.io.*;
import java.net.URLDecoder;

public class searchData {

    public static void search(String key,String filename) throws IOException {

        //定义输入流，打开源数据集txt文件
        InputStreamReader inStream = new InputStreamReader(new FileInputStream(new File("src/main/resources/files/cleanResult1.txt")), "UTF-8");
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
        long startTime = System.currentTimeMillis();    //获取开始时间
        search("图片","image");
        search("手机","phone");
        search("小说","novel");
        search("视频","video");
        search("大学","university");
        search("中国","China");
        search("电影","film");
        search("游戏","game");
        search("英语","English");
        search("电脑","computer");
        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
    }
}
