import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class cleanData {
    public static boolean ifClean(String str) throws IOException{
        //匹配数字
        Pattern p1 = Pattern.compile("([0-9]+)");
        Pattern p2 = Pattern.compile("http");
        Pattern p3 = Pattern.compile("https");
        Pattern p4 = Pattern.compile("([0-9]+).([0-9]+).([0-9]+).([0-9]+)");
        Pattern p5 = Pattern.compile("www");
        Pattern p6 = Pattern.compile("file");
        Matcher m1 = p1.matcher(str);
        Matcher m2 = p2.matcher(str);
        Matcher m3 = p3.matcher(str);
        Matcher m4 = p4.matcher(str);
        Matcher m5 = p5.matcher(str);
        Matcher m6 = p6.matcher(str);
        boolean flag1 = m1.matches();
        boolean flag2 = m2.find();
        boolean flag3 = m3.find();
        boolean flag4 = m4.matches();
        boolean flag5 = m3.find();
        boolean flag6 = m4.find();
        System.out.println(flag1||flag2||flag3||flag4||flag5||flag6);
        return flag1||flag2||flag3||flag4||flag5||flag6;
    }

    public static String replace(String str) throws IOException{
        String result = str.replace('/',' ');
        System.out.println(result);
        return result;
    }


    public static void main(String[] args) throws IOException {
        //定义输入流，打开源数据集txt文件
        InputStreamReader inStream = new InputStreamReader(new FileInputStream(new File("src/main/resources/files/searchResult/China.txt")), "UTF-8");
        //定义输出流，写入预处理后的数据
        OutputStreamWriter outStream = new OutputStreamWriter(new FileOutputStream(new File("src/main/resources/files/cleanResult.txt")), "UTF-8");
        BufferedReader br = new BufferedReader(inStream);
        BufferedWriter bw = new BufferedWriter(outStream);
        String valueString = null;
        //按行读取数据
        while ((valueString = br.readLine()) != null){
            valueString.trim();
            if(!ifClean(valueString)){
                replace(valueString);
                bw.append(valueString);
                bw.newLine();
            }
        }
        bw.flush();
        bw.close();
    }
}
