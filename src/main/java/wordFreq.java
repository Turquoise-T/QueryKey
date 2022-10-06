import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.io.*;

public class wordFreq {
    public static void wordFrequency(String filename) throws IOException {
        //定义输出流，写入搜索到的匹配数据
        OutputStreamWriter outStream = new OutputStreamWriter(new FileOutputStream(new File(String.format("src/main/resources/files/wordFreResult/counted_%s",filename))), "UTF-8");
        BufferedWriter bw = new BufferedWriter(outStream);
        Map<String, Integer> map = new HashMap<>();
        String article = getString(filename);
        System.out.println(article);
//        String result = ToAnalysis.parse(article).toStringWithOutNature();
        String[] words = article.split("/");
        for(String word: words){
            String str = word.trim();
             //过滤空白字符
            if (str.equals(""))
                continue;
//                // 此处过滤长度为1的str
//            else if (str.length() < 2)
//                continue;
//                // 此处过滤停用词
//            else if (ifRemoveStopWords(str) == false)
//                continue;
            if (!map.containsKey(word)){
                map.put(word, 1);
            } else {
                int n = map.get(word);
                map.put(word, ++n);
            }
        }
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Integer> entry = iterator.next();
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        Map.Entry<String, Integer> entry;
        while ((entry = getMax(map)) != null){
            list.add(entry);
        }
        for (int i =0;i<list.size();i++){
            bw.append(list.get(i).toString());
            bw.newLine();
        }
        //关闭
        bw.close();
        System.out.println("====================统计结果========================");
        for (int i =0;i<25;i++){
            System.out.println("第"+(i+1)+": "+list.get(i));
        }
    }
    /**
     * 找出map中value最大的entry, 返回此entry, 并在map删除此entry
     * @param map
     * @return
     */
    public static Map.Entry<String, Integer> getMax(Map<String, Integer> map){
        if (map.size() == 0){
            return null;
        }
        Map.Entry<String, Integer> maxEntry = null;
        boolean flag = false;
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Integer> entry = iterator.next();
            if (!flag){
                maxEntry = entry;
                flag = true;
            }
            if (entry.getValue() > maxEntry.getValue()){
                maxEntry = entry;
            }
        }
        map.remove(maxEntry.getKey());
        return maxEntry;
    }
    /**
     * 停用词移除判断
     * @return boolean
     * @throws IOException
     */
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
    /**
     * 从文件中读取待分割的文章素材.
     * @return
     * @throws IOException
     */
    public static String getString(String filename) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(String.format("src/main/resources/files/%s",filename)));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder strBuilder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null){
            strBuilder.append(line+'/');
        }
        reader.close();
        inputStream.close();
        return strBuilder.toString();
    }

    public static void main(String[] args) throws IOException {
        wordFrequency("ansj_cutResult.txt");
        wordFrequency("jieba_cutResult.txt");
    }
}
