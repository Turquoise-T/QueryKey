import java.io.*;

public class preDataProcess {
    public static void main(String[] args) throws IOException {
        //定义输入流，打开源数据集txt文件
        InputStreamReader inStream = new InputStreamReader(new FileInputStream(new File("files/user_tag_query.10W.TRAIN.txt")), "GBK");
        //定义输出流，写入预处理后的数据
        OutputStreamWriter outStream = new OutputStreamWriter(new FileOutputStream(new File("files/preResult.txt")), "UTF-8");
        BufferedReader br = new BufferedReader(inStream);
        BufferedWriter bw = new BufferedWriter(outStream);
        String valueString = null;
        char c[];

        //按行读取数据
        while ((valueString = br.readLine()) != null){
            c = valueString.toCharArray();
            //从第39个字符开始读取（忽略前面的用户信息）
            for(int i = 39; i < valueString.length(); i++) {
                bw.append(c[i]);
                if(c[i] == '\t') {
                    bw.newLine();
                }
            }
        }

        bw.close();
    }
}
