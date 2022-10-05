import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StringFinder implements Runnable{
    private File file;
    private String keyword;
    private List<String> splitFile;

    public StringFinder(File file, String keyword){
        this.file = file;
        this.keyword = keyword;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String fileContent =
    }

    public List<List<String>> splitList(int listsNumber, List<String> msg){
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

    public static void main(String[] args) {
        String file = null;
        String keyword = null;
        File file1 = new File(file);
        StringFinder stringFinder = new StringFinder(file1, keyword);
        stringFinder.splitFile = stringFinder.splitList()
    }
}
