import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class dealData {
    //按照行来读取数据
    static void readFile() throws IOException{
        String fileName = "src/main/resources/files/preResult.txt";
        try(Scanner sc = new Scanner(new FileReader(fileName))){
            while (sc.hasNextLine()){
                String str = sc.nextLine();
                System.out.println(ToAnalysis.parse(str));
            }
        }
    }
    public static void main(String[] args) throws IOException {
        readFile();
    }
}
