package compkey.process;

import compkey.util;
import org.ansj.splitWord.analysis.ToAnalysis;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import compkey.util.*;

public class ansjCutData {
    //分词清洗
    public static void cut_clean(String fileName) throws IOException{
        //定义输出流，写入搜索到的匹配数据
        OutputStreamWriter outStream = new OutputStreamWriter(new FileOutputStream(new File(String.format("src/main/resources/compkeyFiles/cutted_%s",fileName))), "UTF-8");
        BufferedWriter bw = new BufferedWriter(outStream);
        //定义变量
        String str = null;
        String stringValue = null;
        String index = null;

        try(Scanner sc = new Scanner(new FileReader(String.format("src/main/resources/compkeyFiles/%s",fileName)))) {
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
//                    System.out.println(list.get(i).toString());
                    flag1 = list.get(i).toString().indexOf('/');
                    if(flag1!=-1){
                        index = list.get(i).toString().substring(0,flag1);
                    }else {
                        index = list.get(i).toString();
                    }
                    if(index.length()>=2&& util.ifRemoveStopWords(index)){
                        bw.append(index);
                        bw.newLine();
                    }
                }
            }
        }
        bw.close();

        System.out.println("============分词完成===============");
    }

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();    //获取开始时间
        cut_clean("seedSearchResult.txt");
        long endTime = System.currentTimeMillis();    //获取结束时间
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
    }
}