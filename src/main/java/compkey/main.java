package compkey;

import java.io.IOException;
import compkey.CompKey;

public class main {
    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();    //获取开始时间
        CompKey.compkey("图片",5);
        long time1 = System.currentTimeMillis();    //获取结束时间
        System.out.println("程序运行时间：" + (time1 - startTime) + "ms");    //输出程序运行时间
    }
}
