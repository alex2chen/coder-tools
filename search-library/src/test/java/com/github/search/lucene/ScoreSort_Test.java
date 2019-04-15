package com.github.search.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/18.
 */
public class ScoreSort_Test {
    @Test
    public void explain() throws IOException {
        Directory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig();
        config.setUseCompoundFile(true);
        IndexWriter writer = new IndexWriter(directory, config);
        String feildName = "title";
        Field f1 = new TextField(feildName, "life", Field.Store.YES);
        Field f2 = new TextField(feildName, "work", Field.Store.YES);
        Field f3 = new TextField(feildName, "easy for any of us", Field.Store.YES);
        TextField f4 = new TextField(feildName, "above believe us", Field.Store.YES);
        Document doc1 = new Document();
        Document doc2 = new Document();
        Document doc3 = new Document();
        Document doc4 = new Document();
        doc1.add(f1);
        doc2.add(f2);
        doc3.add(f3);
        doc4.add(f4);
        writer.addDocument(doc1);
        writer.addDocument(doc2);
        writer.addDocument(doc3);
        writer.addDocument(doc4);
        writer.close();
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        MoreLikeThis mlt = new MoreLikeThis(reader);
        Analyzer analyzer = new StandardAnalyzer();
        mlt.setAnalyzer(analyzer);//必须支持，否则java.lang.UnsupportedOperationException: To use MoreLikeThis without term vectors, you must provide an Analyzer
        mlt.setFieldNames(new String[]{feildName});//用于计算的字段
        mlt.setMinTermFreq(1); // 默认值是2
        mlt.setMinDocFreq(1); // 默认值是5
        int maxDoc = reader.maxDoc();
        System.out.println("numDocs:" + maxDoc);
        //Query query = mlt.like(docID);
        Query query = mlt.like(feildName, new StringReader("Life is not easy for any of us. We must work,and above all we must believe in ourselves .We must believe ..."));
        System.out.println("query:" + query);
        System.out.println("believe docFreq:" + reader.docFreq(new Term(feildName, "believe")));
        System.out.println("us docFreq:" + reader.docFreq(new Term(feildName, "us")));
        TopDocs topDocs = searcher.search(query, 10);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            Document document = searcher.doc(scoreDoc.doc);
            System.out.print("相关度：" + scoreDoc.score);
            System.out.print("  ");
            System.out.print(document);
            System.out.println();
        }
        reader.close();
        directory.close();
    }

    public class StringReader extends Reader {
        private int pos = 0, size = 0;
        private String s = null;

        public StringReader(String s) {
            setValue(s);
        }

        void setValue(String s) {
            this.s = s;
            this.size = s.length();
            this.pos = 0;
        }

        @Override
        public int read() {
            if (pos < size) {
                return s.charAt(pos++);
            } else {
                s = null;
                return -1;
            }
        }

        @Override
        public int read(char[] c, int off, int len) {
            if (pos < size) {
                len = Math.min(len, size - pos);
                s.getChars(pos, pos + len, c, off);
                pos += len;
                return len;
            } else {
                s = null;
                return -1;
            }
        }

        @Override
        public void close() {
            pos = size; // this prevents NPE when reading after close!
            s = null;
        }
    }
}
