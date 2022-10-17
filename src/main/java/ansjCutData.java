import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.nlpcn.commons.lang.tire.domain.Forest;
import org.nlpcn.commons.lang.tire.library.Library;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ansjCutData {
    //按照行来读取数据
    static void DealFile() throws IOException{
        String fileName = "files/preResult.txt";
        try(Scanner sc = new Scanner(new FileReader(fileName))){
            while (sc.hasNextLine()){
                String str = sc.nextLine();
                System.out.println(NlpAnalysis.parse(str));
            }
        }
    }

    //停用词移除判断
    static boolean ifRemoveStopWords(String str) throws IOException{
        boolean flag = true;
        String value = null;
        String fileName = "src/main/resources/stopWords/cn_stopwords.txt";
        try (Scanner sc = new Scanner(new FileReader(fileName))){
            while (sc.hasNextLine()){
                value = sc.nextLine();
                if(Objects.equals(value, str)){
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    //采用自定义词典下的分词
    public static void localDic_cutAndclean() throws IOException{
        String fileName = "src/main/resources/files/cleanResult.txt";
        //定义输出流，写入搜索到的匹配数据
        OutputStreamWriter outStream = new OutputStreamWriter(new FileOutputStream(new File("src/main/resources/files/ansj_localDic_cutResult.txt")), "UTF-8");
        BufferedWriter bw = new BufferedWriter(outStream);
        //定义变量
        String str = null;
        String index = null;

        try(Scanner sc = new Scanner(new FileReader(fileName))) {
            //定义自定义词典并加载字典文件
            Forest rootForest = Library.makeForest("src/main/resources/library/userLibrary.dic");
            Forest resourceForest=Library.makeForest(Objects.requireNonNull(new FileInputStream(new File("src/main/resources/library/userLibrary.dic"))));
            while (sc.hasNextLine()) {
                str = sc.nextLine();
                System.out.println(str);
                Result result = ToAnalysis.parse(str, resourceForest);//传入forest
                List<Term> termList = result.getTerms();
                for(Term term:termList){
                    index = term.getName();
                    System.out.println(index);
                    if(index.length()>=2 && ifRemoveStopWords(index)){
                        bw.append(index);
                        bw.newLine();
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        bw.close();
    }

    //采用原始词典下的分词清洗
    static void cut_clean() throws IOException{
        String fileName = "src/main/resources/files/cleanResult1.txt";
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
        //测试分词时间
        long timeStart = System.currentTimeMillis();
        cut_clean();
        long timeEnd = System.currentTimeMillis();
        System.out.printf("采用ansj分词的执行时长：%d 毫秒.\n", (timeEnd - timeStart));

        //测试分词时间
//        long timeStart = System.currentTimeMillis();
//        localDic_cutAndclean();
//        long timeEnd = System.currentTimeMillis();
//        System.out.printf("采用ansj自定义词典分词的执行时长：%d 毫秒.\n", (timeEnd - timeStart));

    }
}