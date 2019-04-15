package com.github.search.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/18.
 */
public class Search_test {
    private static String directoryPath = "target";
    private static Directory directory;
    private static IndexReader reader;
    private static IndexSearcher searcher;

    @BeforeClass
    public static void init() throws IOException {
        directory = FSDirectory.open(Paths.get(directoryPath));
        reader = DirectoryReader.open(directory);
        searcher = new IndexSearcher(reader);
        //目的是当index文件更新的时候，重新生成IndexReader，否则IndexReader不会更新，除非重启项目。
        IndexReader changeReader = DirectoryReader.openIfChanged((DirectoryReader) reader);
        if (changeReader != null) {
            reader.close();
            reader = changeReader;
            searcher = new IndexSearcher(reader); //打开索引
        }
    }

    @AfterClass
    public static void disabled() throws IOException {
        reader.close();
        directory.close();
    }

    @Test
    public void searchByTerm() throws IOException {
        // 搜索特定的项
        Query query = new TermQuery(new Term("id", "5"));
        showQueryResult(query, 5);
    }

    @Test
    public void searchByTermRange() {
        Query query = new TermRangeQuery("id", new BytesRef("1".getBytes()), new BytesRef("5".getBytes()), true, true);
        showQueryResult(query, 5);
    }

    @Test
    public void searchByPrefix() {
        Query query = new PrefixQuery(new Term("content", "Apache"));
        showQueryResult(query, 5);
    }

    @Test
    public void searchByWildcard() {
        Query query = new WildcardQuery(new Term("content", "Apache"));
        showQueryResult(query, 5);
    }

    @Test
    public void searchByBoolean() {
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        Query query1 = new TermQuery(new Term("id", "2"));
        Query query2 = new TermQuery(new Term("id", "3"));
        booleanQuery.add(query1, BooleanClause.Occur.MUST);
        booleanQuery.add(query2, BooleanClause.Occur.MUST);
        showQueryResult(booleanQuery.build(), 5);
    }

    @Test
    public void searchByFuzzy() {
        FuzzyQuery query = new FuzzyQuery(new Term("content", "search"), 20, 10);
        showQueryResult(query, 5);
    }

    @Test
    public void QueryByIntValue() throws IOException {
        String colName = "intValue";
        //精确查询
        Query query = IntPoint.newExactQuery(colName, 5);
        System.out.println(query.getClass().getName() + ":" + query);
        showQueryResult(query, 10);
        //范围查询，不包含边界
        query = IntPoint.newRangeQuery(colName, Math.addExact(11, 1), Math.addExact(22, -1));
        showQueryResult(query, 10);
        //范围查询，包含边界
        query = IntPoint.newRangeQuery(colName, 11, 22);
        showQueryResult(query, 10);
        //范围查询，左包含，右不包含
        query = IntPoint.newRangeQuery(colName, 11, Math.addExact(22, -1));
        showQueryResult(query, 10);
        //集合查询
        query = IntPoint.newSetQuery(colName, 11, 22, 33);
        showQueryResult(query, 10);
    }

    @Test
    public void queryParser() throws IOException, ParseException, QueryNodeException {
        String defaultFiled = "content";
        //用法1 传统解析器-单默认字段 QueryParser：
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser = new QueryParser(defaultFiled, analyzer);
        Query query = parser.parse("query String");
        //用法2  传统解析器-多默认字段  MultiFieldQueryParser：
        String[] multiDefaultFields = {"name", defaultFiled};
        MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(
                multiDefaultFields, analyzer);
        multiFieldQueryParser.setDefaultOperator(QueryParser.Operator.OR);// 设置默认的组合操作，默认是 OR
        query = multiFieldQueryParser.parse("笔记本电脑 AND price:1999900");

        //用法3  新解析框架的标准解析器
        StandardQueryParser qpHelper = new StandardQueryParser(analyzer);
        //qpHelper.setAllowLeadingWildcard(true);// 开启第一个字符的通配符匹配，默认关闭因为效率不高
        // qpHelper.setDefaultOperator(Operator.AND);// 改变空格的默认操作符，以下可以改成AND
        query = qpHelper.parse("(\"联想笔记本电脑\" OR simpleIntro:英特尔) AND type:电脑 AND price:1999900", defaultFiled);
        System.out.println(query);
        showQueryResult(query, 10);
    }

    /**
     * @param query
     * @param num   取回前N个文档
     */
    private void showQueryResult(Query query, Integer num) {
        TopDocs topDocs = null;
        try {
            topDocs = searcher.search(query, num);
            System.out.println("实际搜索到的记录数 => " + topDocs.totalHits);
            Document document = null;
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                document = searcher.doc(scoreDoc.doc);
                System.out.println("doc:" + document);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

