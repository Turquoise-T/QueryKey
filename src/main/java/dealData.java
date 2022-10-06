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

public class dealData {
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

    public static void localDic(){
        try {
            //读取的是根目录下的
            Forest rootForest = Library.makeForest("src/main/resources/library/userLibrary.dic");
            System.out.println(rootForest.toMap());
            String str = "阿里巴巴是我喜欢的企业！初音未来是我最喜欢的虚拟歌手！";
            //加载字典文件,取的是resource下的
            Forest resourceForest=Library.makeForest(Objects.requireNonNull(dealData.class.getResourceAsStream("library/userLibrary.dic")));
            Result result=ToAnalysis.parse(str, resourceForest);//传入forest
            List<Term> termList = result.getTerms();
            for(Term term:termList){
                System.out.println(term.getName()+term.getNatureStr());
            }
        }catch (Exception e){
            e.printStackTrace();
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
        //deal_clean();
        localDic();

//        String str = "这里作者使用了注意力的方法进行了特征融合，在这里作者主要参考的是使用SENet网络的结构。这是因为SENet计算attention的方式就是把每个通道的像素值做一个平均之后，然后经过一系列操作后，用sigmoid函数归一化。这样的操作对于大尺度的目标还是有效果的，但是对于小目标效果就不太好，所以的话本文就提出了多尺度的方法来计算。其实我个人也进行了一些注意力的特征融合操作。其实效果还是不错的。\n";
//        System.out.println(ToAnalysis.parse(str));
//        System.out.println(NlpAnalysis.parse(str));
//        Forest dic1 = new Forest();
//        Library.insertWord(dic1,new Value("注意力","define","1000"));
//        System.out.println(NlpAnalysis.parse(str,dic1));
    }
}
