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



    public static void main(String[] args) throws IOException {
        search("湖南","hunan");
    }
}