package com.wuma.LuceneInAction.Familiar;

import com.wuma.LuceneInAction.fileoper.FileUtil;
import com.wuma.LuceneInAction.util.TestUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKQueryParser;
import org.wltea.analyzer.lucene.IKSimilarity;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.apache.lucene.document.Field.Index.NOT_ANALYZED;

/**
 * Created by liwujun
 * on 2016/6/22 at 17:47
 */
public class IndexFile {
    static String path = "";
    static Directory directory;
    static String dipath = "";

    void setUp() throws IOException {
        directory = new RAMDirectory();
        IndexWriter writer = getWriter();
        List<File> filelist = FileUtil.getFileListInDirectory(dipath);
        for (int i = 0; i < filelist.size(); i++) {
            Document doc = new Document();
            doc.add(new Field("filepath", filelist.get(i).getAbsolutePath(),
                    Field.Store.YES,
                    NOT_ANALYZED));
            doc.add(new Field("filecontent", FileUtil.getFileContent(filelist.get(i)),
                    Field.Store.YES,
                    Field.Index.ANALYZED));
            writer.addDocument(doc);
        }
        writer.close();
    }

    public static void main(String[] args) throws IOException {
        System.out.println(args.length);
        if (args.length == 0) {
            System.out.println("need directory path.");
            return;
        } else {
            path = args[0];
            dipath = path;
            System.out.println("index directory :" + path);
        }
        IndexFile indexFile = new IndexFile();
        indexFile.setUp();
        int result = indexFile.getHitCount("filecontent", "各种原因");
        System.out.println("hit count:" + result);
    }

    int getHitCount(String fieldName, String searchString) throws IOException {
        IndexSearcher searcher = new IndexSearcher(directory);
        searcher.setSimilarity(new IKSimilarity());
//        Term t = new Term(fieldName, searchString);
//        Query query = new TermQuery(t);
        Query query = IKQueryParser.parse(fieldName, searchString);
        int hitCount = TestUtil.hitCount(searcher, query);
        TopDocs topDocs = searcher.search(query, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        for (int i = 0; i < topDocs.totalHits; ++i) {
            Document targetDoc = searcher.doc(scoreDocs[i].doc);
            System.out.println("内容：" + targetDoc.toString());
        }
        searcher.close();
        return hitCount;
    }

    IndexWriter getWriter() throws IOException {
        return new IndexWriter(directory, new IKAnalyzer(),
                IndexWriter.MaxFieldLength.UNLIMITED);
    }

}
