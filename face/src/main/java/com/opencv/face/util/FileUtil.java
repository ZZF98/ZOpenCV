package com.opencv.face.util;

import java.io.File;

/**
 * @ClassNmae: FileUtil
 * @Author : zzf
 * @CreateDate:2019/3/15$ 13:47$
 */
public class FileUtil {

    public static Integer fileNum(String path){
        File[] files=new File(path).listFiles();
        Integer sum=0;
        for (File file:files){
            if(file.isFile())
                sum++;
        }
       return sum;
    }
}
