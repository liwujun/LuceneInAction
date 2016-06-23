package com.wuma.LuceneInAction.fileoper;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by liwujun
 * on 2016/6/23 at 9:50
 */
public class FileUtil {
    public static List<File> getFileListInDirectory(String path) {
        List<File> result = new ArrayList<File>();
        File file = new File(path);
        if (file.isDirectory()) {
            File[] fileshtm = file.listFiles();
            if (null != fileshtm && fileshtm.length > 0) {
                Arrays.sort(fileshtm, new Comparator<File>() {
                    public int compare(File o1, File o2) {
                        return (int) (o1.lastModified() - o2.lastModified());
                    }

                    public boolean equals(Object obj) {
                        return true;
                    }
                });
                for (File htm : fileshtm) {
                    if (!htm.isDirectory()) {
                        result.add(htm);

                    }
                }
            }
        }
        return result;
    }

    public static String getFileContent(File htm) {
        System.out.println(htm.getAbsolutePath());
        InputStream is = null;
        StringBuffer sb = new StringBuffer();
        String content = "";
        try {
            FileInputStream fis = new FileInputStream(htm);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            while ((content = br.readLine()) != null) {
                sb.append(content);
            }
            //output file content
            System.out.println(sb);
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
