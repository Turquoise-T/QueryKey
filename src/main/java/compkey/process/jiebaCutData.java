package compkey.process;

import compkey.util;
import org.ansj.splitWord.analysis.ToAnalysis;
import com.huaban.analysis.jieba.JiebaSegmenter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class jiebaCutData {
    //分词清洗
    public static void cut_clean(String fileName) throws IOException{
        //定义输出流，写入搜索到的匹配数据
        OutputStreamWriter outStream = new OutputStreamWriter(new FileOutputStream(new File(String.format("src/main/resources/compkeyFiles/cutted_%s",fileName))), "UTF-8");
        BufferedWriter bw = new BufferedWriter(outStream);
        //定义变量
        String str = null;
        String stringValue = null;
        String index = null;
        //定义jieba分词器
        JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();

        try(Scanner sc = new Scanner(new FileReader(String.format("src/main/resources/compkeyFiles/%s",fileName)))) {
            while (sc.hasNextLine()) {
                str = sc.nextLine();
                List<String> strings = jiebaSegmenter.sentenceProcess(str);
                //遍历所有分词
                for(String s:strings){
                    if(s.length()>=2&&util.ifRemoveStopWords(s)){
                        bw.append(s);
                        bw.newLine();
                        System.out.println(s);
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
