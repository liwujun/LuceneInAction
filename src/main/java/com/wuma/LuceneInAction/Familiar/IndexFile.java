package com.wuma.LuceneInAction.Familiar;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by liwujun
 * on 2016/6/22 at 17:47
 */
public class IndexFile {
    static String path = "";

    public static void main(String[] args) {
        System.out.println(args.length);
        if (args.length == 0) {
            System.out.println("need directory path.");
            return;
        } else {
            path = args[0];
            System.out.println("index directory :" + path);
        }
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
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }
}
