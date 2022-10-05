import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class dealData {
    //停用词移除判断
    static boolean ifRemoveStopWords(String str) throws IOException{
        boolean flag = true;
        String value = null;
        String fileName = "src/main/resources/stopWords/cn_stopwords.txt";
        try (Scanner sc = new Scanner(new FileReader(fileName))){
            while (sc.hasNextLine()){
                value = sc.nextLine();
                if(value==str){
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    static void deal_clean() throws IOException{
        String fileName = "src/main/resources/files/cleanResult.txt";
        //定义输出流，写入搜索到的匹配数据
        OutputStreamWriter outStream = new OutputStreamWriter(new FileOutputStream(new File("src/main/resources/files/ansj_cutResult.txt")), "UTF-8");
        BufferedWriter bw = new BufferedWriter(outStream);
        //定义变量
        String str = null;
        String stringValue = null;
        String index = null;

        try(Scanner sc = new Scanner(new FileReader(fileName))) {
            while (sc.hasNextLine()) {
                str = sc.nextLine();
                stringValue = ToAnalysis.parse(str).toString();
                index = null;
                int flag1 = -1;
                int flag2 = -1;
                List list = new ArrayList();
                //遍历该行所有字符
                for(int i = 0; i < stringValue.length(); i++) {
                    flag1 = stringValue.indexOf(',',i);
                    if(flag1==-1){
                        break;
                    }
                    list.add(stringValue.substring(i,flag1));
                    i = flag1;
                }
                System.out.println(list);
                //遍历所有分词
                for(int i = 0;i<list.size();i++){
                    System.out.println(list.get(i).toString());
                    flag1 = list.get(i).toString().indexOf('/');
                    if(flag1!=-1){
                        index = list.get(i).toString().substring(0,flag1);
                    }else {
                        index = list.get(i).toString();
                    }
                    System.out.println(index);
                    if(index.length()>=2&&ifRemoveStopWords(index)){
                        bw.append(index);
                        bw.newLine();
                    }
                }
            }
        }
        bw.close();
    }
    public static void main(String[] args) throws IOException {
        deal_clean();
    }
}
