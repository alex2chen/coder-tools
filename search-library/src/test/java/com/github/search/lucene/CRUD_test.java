package com.github.search.lucene;

import com.google.common.collect.Lists;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.javatuples.Triplet;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/17.
 */
public class CRUD_test {
    private static String directoryPath = "target";
    private static Directory directory;
    private static IndexWriter writer;
    private static IndexReader reader;
    private static IndexSearcher searcher;

    private static IndexWriter getWriter() throws IOException {
        IndexWriterConfig config = new IndexWriterConfig();
        //config.setCodec(new Lucene70Codec());
        IndexWriter writer = new IndexWriter(directory, config);//1.创建IndexWriter
        //fixed:org.apache.lucene.index.IndexNotFoundException: no segments* file found in MMapDirectory
        writer.commit();
        return writer;
    }

    @BeforeClass
    public static void init() throws IOException {
        directory = FSDirectory.open(Paths.get(directoryPath));
        writer = getWriter();
        reader = DirectoryReader.open(directory);
        searcher = new IndexSearcher(reader); //打开索引
    }

    @AfterClass
    public static void disabled() throws IOException {
        reader.close();
        writer.close();
        directory.close();
    }

    @Test
    public void createDocument() throws IOException {
        //支持分词索引，存储
        List<Triplet<String, String, Boolean>> feilds = Lists.newArrayList();
        feilds.add(new Triplet("id", "5", true));
        feilds.add(new Triplet("title", "Lucene - is greater search", false));
        feilds.add(new Triplet("content", "The Apache Lucene TM project develops open-source search software, including", true));
        Document doc = createDocument(feilds);
        writer.addDocument(doc);
        writer.commit();
    }

    @Test
    public void insertSortFeild() throws IOException {
        Document document = new Document();
        // 替换addStringField，addStringField，addTextField
        addIntPoint(document, "intValue", 3);
        writer.addDocument(document);
        document = new Document();
        addIntPoint(document, "intValue", 4);
        writer.addDocument(document);
        document = new Document();
        addIntPoint(document, "intValue", 5);
        writer.addDocument(document);
        writer.commit();
    }

    @Test
    public void sortSearch() throws IOException {
        //第三个参数为true，表示从大到小
        SortField intValues = new SortField("intValue", SortField.Type.INT, true);
        TopFieldDocs search = searcher.search(new MatchAllDocsQuery(), 10, new Sort(intValues));
        ScoreDoc[] scoreDocs = search.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println(searcher.doc(scoreDoc.doc));
        }
    }

    @Test
    public void searchByIntValue() throws IOException {
        String colName = "intValue";
        //精确查询
        Query query = IntPoint.newExactQuery(colName, 5);
        System.out.println(query.getClass().getName() + ":" + query);
        ScoreDoc[] scoreDocs = searcher.search(query, 10).scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("精确查询：" + searcher.doc(scoreDoc.doc));
        }
        //范围查询，不包含边界
        query = IntPoint.newRangeQuery(colName, Math.addExact(11, 1), Math.addExact(22, -1));
        scoreDocs = searcher.search(query, 10).scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("不包含边界：" + searcher.doc(scoreDoc.doc));
        }
        //范围查询，包含边界
        query = IntPoint.newRangeQuery(colName, 11, 22);
        scoreDocs = searcher.search(query, 10).scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("包含边界：" + searcher.doc(scoreDoc.doc));
        }
        //范围查询，左包含，右不包含
        query = IntPoint.newRangeQuery(colName, 11, Math.addExact(22, -1));
        scoreDocs = searcher.search(query, 10).scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("左包含右不包含：" + searcher.doc(scoreDoc.doc));
        }
        //集合查询
        query = IntPoint.newSetQuery(colName, 11, 22, 33);
        scoreDocs = searcher.search(query, 10).scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("集合查询：" + searcher.doc(scoreDoc.doc));
        }
    }

    @Test
    public void searchById() throws IOException {
        TopDocs search = searcher.search(new TermQuery(new Term("id", "5")), 10);
        ScoreDoc[] scoreDocs = search.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println(searcher.doc(scoreDoc.doc));
        }
    }

    @Test
    public void searchEq() throws IOException, QueryNodeException {
        String defaultFiledName = "id";
        //QueryParser qpHelper = new QueryParser(defaultFiledName, new StandardAnalyzer());
        StandardQueryParser qpHelper = new StandardQueryParser();
        //qpHelper.setAnalyzer(new StandardAnalyzer());
        Query query = qpHelper.parse("5 OR id:10", defaultFiledName);
        System.out.println(query.getClass().getName() + ":" + query);
        TopDocs search = searcher.search(query, 10);
        ScoreDoc[] scoreDocs = search.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println(searcher.doc(scoreDoc.doc));
        }
    }

    @Test
    public void deleteDocuments() throws IOException {
        writer.deleteDocuments(new Term("id", "3"));
        writer.commit();
    }

    private Document createDocument(List<Triplet<String, String, Boolean>> cols) {
        Document doc = new Document();
        //Field filePathField = new Field("id", "2", Field.Store.YES, Field.Index.NOT_ANALYZED);
        cols.forEach(x -> {
            TextField field = new TextField(x.getValue0(), x.getValue1(), x.getValue2() ? Field.Store.YES : Field.Store.NO);
            doc.add(field);
        });
        return doc;
    }

    private void addIntPoint(Document document, String name, int value) {
        Field field = new IntPoint(name, value);
        document.add(field);
        System.out.println(name + ",stored:" + field.fieldType().stored() + ",indexOptions:" + field.fieldType().indexOptions());
        //要排序，必须添加一个同名的NumericDocValuesField
        field = new NumericDocValuesField(name, value);
        System.out.println(name + ",stored:" + field.fieldType().stored() + ",indexOptions:" + field.fieldType().indexOptions());
        document.add(field);
        //要存储值，必须添加一个同名的StoredField
        field = new StoredField(name, value);
        System.out.println(name + ",stored:" + field.fieldType().stored() + ",indexOptions:" + field.fieldType().indexOptions());
        document.add(field);
    }

    private void addBinaryDocValuesField(Document document, String name, String value) {
        Field field = new BinaryDocValuesField(name, new BytesRef(value));
        document.add(field);
        //如果需要存储，加此句
        field = new StoredField(name, value);
        document.add(field);
    }

    private void addStringField(Document document, String name, String value) {
        Field field = new StringField(name, value, Field.Store.YES);
        document.add(field);
        field = new SortedDocValuesField(name, new BytesRef(value));
        document.add(field);
    }

    private void addTextField(Document document, String name, String value) {
        Field field = new TextField(name, value, Field.Store.YES);
        document.add(field);
        field = new SortedDocValuesField(name, new BytesRef(value));
        document.add(field);
    }
}
