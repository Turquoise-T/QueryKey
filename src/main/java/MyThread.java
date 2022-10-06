import java.io.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MyThread implements Runnable{
    private List<String> msgList = null;
    private CountDownLatch latch = null;
    private String name;
    private String keyword;
    private String filename;

    public MyThread( List<String>  msgList,CountDownLatch latch ,String name, String keyword , String filename){
        this.msgList = msgList;
        this.latch = latch;
        this.name = name;
        this.keyword = keyword;
        this.filename = filename;
    }
    @Override
    public void run() {
        OutputStreamWriter outStream = null;
        try {
            outStream = new OutputStreamWriter(new FileOutputStream(new File(String.format("src/main/resources/files/threadSearchResult/%s.txt",filename))), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(outStream);
        int flag=0;
        for(String str : msgList){
            // System.out.println(name+"----"+str);
            flag++;
            if(str.contains(keyword)){
                try {
                    bw.append(str);
                    bw.newLine();
                    Thread.sleep(0);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        latch.countDown();
    }

}
