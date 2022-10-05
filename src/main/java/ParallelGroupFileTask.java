import java.io.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ParallelGroupFileTask implements Runnable{
    private final String filename;
    private final String key;
    private boolean finish;
    public ParallelGroupFileTask(String filename, String key) {
        this.filename = filename;
        this.key = key;
        this.finish = false;
    }

    @Override
    public void run() {
        int numThreads = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[numThreads];
        return;
    }

    private void read(String filename, String key) throws InterruptedException, IOException {
        //定义输入流，打开源数据集txt文件
        InputStreamReader inStream = new InputStreamReader(new FileInputStream(new File("src/main/resources/files/preResult.txt")), "UTF-8");
        //定义输出流，写入搜索到的匹配数据
        OutputStreamWriter outStream = new OutputStreamWriter(new FileOutputStream(new File(String.format("src/main/resources/files/searchResult/%s.txt",filename))), "UTF-8");
        BufferedReader br = new BufferedReader(inStream);
        BufferedWriter bw = new BufferedWriter(outStream);
        String valueString = null;
        int flag = -1;
        //按行读取数据
        while ((valueString = br.readLine()) != null){
            flag = valueString.indexOf(key);
            if (flag != -1){
                bw.append(valueString);
                bw.newLine();
            }
        }
        bw.close();
    }

    public boolean isFinish(String valueString, BufferedReader br) throws IOException {
        if((valueString = br.readLine()) == null) {
            return finish;
        }
        return false;
    }
}
