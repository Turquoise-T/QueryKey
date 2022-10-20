import java.io.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static compkey.util.getFilePath;

public class filterAnsj {
    public static void main(String[] args) throws IOException {
        filter();
        System.out.println(String.valueOf("8").matches("[\u4e00-\u9fa5]"));
    }

    public static void filter() throws IOException {
        Set<String> set = new HashSet<String>();
        FileInputStream inputStream = new FileInputStream(getFilePath("ansj_cutResult.txt"));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//        String everyStr = null;
//        while((everyStr = bufferedReader.readLine()) != null)
//        {
//            set.add(everyStr);
//        }
        String file = getFilePath("filterCut.txt");
        BufferedWriter bufw = new BufferedWriter(new FileWriter(file));
        String everyStr = null;
        // 创建set的copyiterator方法
        //Iterator it = set.iterator();
        while ((everyStr = bufferedReader.readLine())!=null){
//            System.out.println(everyStr);
            //String tmp = it.next().toString();
//            if(String.valueOf(tmp.charAt(0)).matches("[\u4e00-\u9fa5]")){//判断是不是汉字，不是汉字的全部给爷过滤掉
//                bufw.write(tmp);
//                bufw.newLine();
//                bufw.flush();
//            }
            if(String.valueOf(everyStr.charAt(0)).matches("[\u4e00-\u9fa5]")){//判断是不是汉字，不是汉字的全部给爷过滤掉
                bufw.write(everyStr);
                bufw.newLine();
                bufw.flush();
            }
//            if(String.valueOf(it.next().toString().charAt(0)).matches("[\u4e00-\u9fa5]")){//判断是不是汉字，不是汉字的全部给爷过滤掉
//                bufw.write(it.next().toString());
//                bufw.newLine();
//                bufw.flush();
//            }
        }
        bufw.close();
        System.out.println("==============分词结果去重，分词清洗完成==========");
    }


}
