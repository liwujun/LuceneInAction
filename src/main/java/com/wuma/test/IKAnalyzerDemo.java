package com.wuma.test;

/**
 * Created by liwujun
 * on 2016/6/23 at 15:16
 */
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKQueryParser;
import org.wltea.analyzer.lucene.IKSimilarity;

import java.io.IOException;

public class IKAnalyzerDemo {
    public IKAnalyzerDemo() {
    }

    public static void main(String[] args) {
        String fieldName = "text";
        String text = "IK Analyzer是一个结合词典分词和文法分词的中文分词开源工具包。它使用了全新的正向迭代最细粒度切分算法。";
        IKAnalyzer analyzer = new IKAnalyzer();
        RAMDirectory directory = null;
        IndexWriter iwriter = null;
        IndexSearcher isearcher = null;

        try {
            directory = new RAMDirectory();
            iwriter = new IndexWriter(directory, analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
            Document e = new Document();
            e.add(new Field("ID", "10000", Field.Store.YES, Field.Index.NOT_ANALYZED));
            e.add(new Field(fieldName, text, Field.Store.YES, Field.Index.ANALYZED));
            iwriter.addDocument(e);
            iwriter.close();
            isearcher = new IndexSearcher(directory);
            isearcher.setSimilarity(new IKSimilarity());
            String keyword = "中文分词工具包";
            Query query = IKQueryParser.parse(fieldName, keyword);
            TopDocs topDocs = isearcher.search(query, 5);
            System.out.println("命中：" + topDocs.totalHits);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;

            for (int i = 0; i < topDocs.totalHits; ++i) {
                Document targetDoc = isearcher.doc(scoreDocs[i].doc);
                System.out.println("内容：" + targetDoc.toString());
            }
        } catch (CorruptIndexException var32) {
            var32.printStackTrace();
        } catch (LockObtainFailedException var33) {
            var33.printStackTrace();
        } catch (IOException var34) {
            var34.printStackTrace();
        } finally {
            if (isearcher != null) {
                try {
                    isearcher.close();
                } catch (IOException var31) {
                    var31.printStackTrace();
                }
            }

            if (directory != null) {
                try {
                    directory.close();
                } catch (Exception var30) {
                    var30.printStackTrace();
                }
            }

        }

    }
}
