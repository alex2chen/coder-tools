package com.github.search.es;

//import org.elasticsearch.action.bulk.BulkResponse;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.common.xcontent.XContentType;


import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.term.TermSuggestion;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: alex
 * @Description: REST Client
 * * Java Low Level REST Client: 低级别的REST客户端，通过http与集群交互，用户需自己编组请求JSON串，及解析响应JSON串。兼容所有ES版本。
 * * Java High Level REST Client: 高级别的REST客户端，基于低级别的REST客户端，增加了编组请求JSON串、解析响应JSON串等相关api。使用的版本需要保持和ES服务端的版本一致，否则会有版本问题。
 * * 官方API：https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/index.html
 * * lucene-guard 安全：https://search-guard.com/transport-client-authentication-authorization/
 * @Date: created in 2018/2/18.
 */
public class ESRest_test {
    private static RestHighLevelClient client;
    private String indexName = "twitter_v1";
    private String typeName = "userInfo";
    private static Header header = new BasicHeader("Authorization", "Basic " + Base64Utils.encodeToString("admin:admin".getBytes())); //Base64Utils.encodeToString("omsuser:kxtxomsuser".getBytes()));

    @BeforeClass
    public static void openClient() throws IOException {
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.1.42", 9200, "http"),
                        new HttpHost("192.168.1.43", 9200, "http"),
                        new HttpHost("192.168.1.44", 9200, "http"))
                        .setDefaultHeaders(new Header[]{header}));
    }

    @AfterClass
    public static void closeClient() throws IOException {
        client.close();
    }

    @Test
    public void deleteIndex() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        System.out.println("acknowledged = " + client.indices().delete(deleteIndexRequest).isAcknowledged());
    }

    @Test
    public void createIndex() throws IOException {
        //boolean result = client.indices().create(Requests.createIndexRequest(indexName)).isAcknowledged();
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        // 2、设置索引的settings
        request.settings(Settings.builder().put("index.number_of_shards", 3) // 分片数
                        .put("index.number_of_replicas", 2) // 副本数
//                .put("analysis.analyzer.default.tokenizer", "ik_smart") // 默认分词器
        );
        // 3、设置索引的mappings
        request.mapping("userInfo",
                "  {\n" +
                        "    \"userInfo\": {\n" +
                        "      \"properties\": {\n" +
                        "        \"message\": {\n" +
                        "          \"type\": \"text\"\n" +
                        "        }\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }",
                XContentType.JSON);
        // 4、 设置索引的别名
//        request.alias(new Alias("twitter"));
        // 5、 发送请求
        // 5.1 同步方式发送请求
        CreateIndexResponse response = client.indices().create(request);
        System.out.println("acknowledged = " + response.isAcknowledged());
        System.out.println("shardsAcknowledged = " + response.isShardsAcknowledged());
        // 5.1 异步方式发送请求
//        ActionListener<CreateIndexResponse> listener = new ActionListener<CreateIndexResponse>() {
//            @Override
//            public void onResponse(CreateIndexResponse createIndexResponse) {
//                // 6、处理响应
//                System.out.println("acknowledged = " + createIndexResponse.isAcknowledged());
//                System.out.println("shardsAcknowledged = " + createIndexResponse.isShardsAcknowledged());
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                System.out.println("创建索引异常：" + e.getMessage());
//            }
//        };
//        client.indices().createAsync(request, listener);
    }

    @Test
    public void insertDoc() throws IOException {
        IndexRequest request = new IndexRequest(indexName, typeName, "1");
        String jsonString = "{\"user\":\"kimchy\",\"age\":33,\"postDate\":\"2013-01-30\",\"message\":\"lucene and solr is bester lucene engine.\"}";
        System.out.println(jsonString);
        // 方式一：直接给JSON串
        request.source(jsonString, XContentType.JSON);

        // 方式二：以map对象来表示文档
//        Map<String, Object> jsonMap = new HashMap<>();
//        jsonMap.put("user", "kimchy");
//        jsonMap.put("postDate", new Date());
//        jsonMap.put("message", "trying out Elasticsearch");
//        request.source(jsonMap);

        //方式三：用XContentBuilder来构建文档
        XContentBuilder builder = XContentFactory.jsonBuilder();
//        builder.startObject()
//                .field("user", "kimchy")
//                .field("postDate", new Date())
//                .field("message", "trying out Elasticsearch")
//                .endObject();
//        request.source(builder);

        //方式四：直接用key-value对给出
//        request.source("user", "kimchy", "postDate", new Date(), "message", "trying out Elasticsearch");
        //其他的一些可选设置
//        request.routing("routing");  //设置routing值
//        request.timeout(TimeValue.timeValueSeconds(1));  //设置主分片等待时长
//        request.setRefreshPolicy("wait_for");  //设置重刷新策略
//        request.version(2);  //设置版本号
//        request.opType(DocWriteRequest.OpType.CREATE);  //操作类别

        IndexResponse indexResponse = null;
        try {
            // 同步方式
            indexResponse = client.index(request);
        } catch (ElasticsearchException e) {
            // 捕获，并处理异常
            //判断是否版本冲突、create但文档已存在冲突
            if (e.status() == RestStatus.CONFLICT) {
                System.out.println("冲突了，请在此写冲突处理逻辑！\n" + e.getDetailedMessage());
            }
            e.printStackTrace();
        }
        if (indexResponse != null) {
            String index = indexResponse.getIndex();
            String type = indexResponse.getType();
            String id = indexResponse.getId();
            long version = indexResponse.getVersion();
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                System.out.println("新增文档成功，处理逻辑代码写到这里。");
            } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                System.out.println("修改文档成功，处理逻辑代码写到这里。");
            }
            // 分片处理信息
            ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
            }
            // 如果有分片副本失败，可以获得失败原因信息
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    String reason = failure.reason();
                    System.out.println("副本失败原因：" + reason);
                }
            }
        }

    }

    @Test
    public void updateDoc() throws IOException {
        UpdateRequest request = new UpdateRequest(indexName, typeName, "2");
        String jsonString = "{" +
                "\"create\":\"2018-09-01\"," +
                "\"field\":\"daily test\"" +
                "}";
        request.doc(jsonString, XContentType.JSON);//部分更新
        UpdateResponse updateResponse = client.update(request);
        System.out.println(updateResponse.status());
    }

    @Test
    public void getDoc() {
        GetRequest request = new GetRequest(indexName, typeName, "1");
        // 2、可选的设置
        //request.routing("routing");
        //request.version(2);
        //request.fetchSourceContext(new FetchSourceContext(false)); //是否获取_source字段
        //选择返回的字段
        String[] includes = new String[]{"message", "*Date"};
        String[] excludes = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        request.fetchSourceContext(fetchSourceContext);

        // 取stored字段
//        request.storedFields("message");
//        GetResponse getResponse = client.get(request);
//        String message = getResponse.getField("message").getValue();

        GetResponse getResponse = null;
        try {
            getResponse = client.get(request);  //异步getAsync
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.NOT_FOUND) {
                System.out.println("没有找到该id的文档");
            }
            if (e.status() == RestStatus.CONFLICT) {
                System.out.println("获取时版本冲突了，请在此写冲突处理逻辑！");
            }
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //4、处理响应
        if (getResponse != null) {
            String index = getResponse.getIndex();
            String type = getResponse.getType();
            String id = getResponse.getId();
            if (getResponse.isExists()) { // 文档存在
                long version = getResponse.getVersion();
                String sourceAsString = getResponse.getSourceAsString(); //结果取成 String
                Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();  // 结果取成Map
                byte[] sourceAsBytes = getResponse.getSourceAsBytes();    //结果取成字节数组

                System.out.println("index:" + index + "  type:" + type + "  id:" + id);
                System.out.println(sourceAsString);

            } else {
                System.out.println("没有找到该id的文档");
            }
        }
    }

    @Test
    public void bulk() throws IOException {
        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest(indexName, typeName, "1").source(XContentType.JSON, "field", "foo"));//会覆盖
        request.add(new IndexRequest(indexName, typeName, "2").source(XContentType.JSON, "field", "bar"));
        request.add(new IndexRequest(indexName, typeName, "3").source(XContentType.JSON, "field", "baz"));

//        request.add(new DeleteRequest("mess", "_doc", "3"));
//        request.add(new UpdateRequest("mess", "_doc", "2").doc(XContentType.JSON, "other", "test"));
//        request.add(new IndexRequest("mess", "_doc", "4").source(XContentType.JSON, "field", "baz"));
        //可选的设置
//        request.timeout("2m");
//        request.setRefreshPolicy("wait_for");
//        request.waitForActiveShards(2);
        BulkResponse bulkResponse = client.bulk(request);
        if (bulkResponse != null) {
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                DocWriteResponse itemResponse = bulkItemResponse.getResponse();
                if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX
                        || bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE) {
                    IndexResponse indexResponse = (IndexResponse) itemResponse;
                    System.out.println("新增成功的处理," + indexResponse.getId());
                } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE) {
                    UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                    //TODO
                    System.out.println("修改成功的处理," + updateResponse.getId());
                } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.DELETE) {
                    DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
                    //TODO
                    System.out.println("删除成功的处理," + deleteResponse.getId());
                }
            }
        }
    }

    @Test
    public void search() throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.types(typeName);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //构造QueryBuilder
//        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("user", "kimchy")
//                .fuzziness(Fuzziness.AUTO)
//                .prefixLength(3)
//                .maxExpansions(10);
//        sourceBuilder.query(matchQueryBuilder);

        sourceBuilder.query(QueryBuilders.termQuery("age", 24));
        sourceBuilder.from(0);
        sourceBuilder.size(10);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        //是否返回_source字段
        //sourceBuilder.fetchSource(false);
        //设置返回哪些字段
//        String[] includeFields = new String[]{"title", "user", "innerObject.*"};
//        String[] excludeFields = new String[]{"_type"};
//        sourceBuilder.fetchSource(includeFields, excludeFields);
        //指定排序
//        sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
//        sourceBuilder.sort(new FieldSortBuilder("_uid").order(SortOrder.ASC));
        //设置返回 profile
//        sourceBuilder.profile(true);

        // 可选的设置
        //searchRequest.routing("routing");
        //高亮设置
//        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field("title");
//        highlightTitle.highlighterType("unified");
//        highlightBuilder.field(highlightTitle);
//        HighlightBuilder.Field highlightUser = new HighlightBuilder.Field("user");
//        highlightBuilder.field(highlightUser);
        //加入聚合
//        sourceBuilder.highlighter(highlightBuilder);
//        TermsAggregationBuilder aggregation = AggregationBuilders.terms("by_company").field("company.keyword");
//        aggregation.subAggregation(AggregationBuilders.avg("average_age").field("age"));
//        sourceBuilder.aggregation(aggregation);
        //做查询建议
//        SuggestionBuilder termSuggestionBuilder = SuggestBuilders.termSuggestion("user").text("kmichy");
//        SuggestBuilder suggestBuilder = new SuggestBuilder();
//        suggestBuilder.addSuggestion("suggest_user", termSuggestionBuilder);
//        sourceBuilder.suggest(suggestBuilder);
        //将请求体加入到请求中
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        //搜索结果状态信息
        RestStatus status = searchResponse.status();
        TimeValue took = searchResponse.getTook();
        Boolean terminatedEarly = searchResponse.isTerminatedEarly();
        boolean timedOut = searchResponse.isTimedOut();
        //分片搜索情况
        int totalShards = searchResponse.getTotalShards();
        int successfulShards = searchResponse.getSuccessfulShards();
        int failedShards = searchResponse.getFailedShards();
        for (ShardSearchFailure failure : searchResponse.getShardFailures()) {
            // failures should be handled here
        }
        // 获取聚合结果
//        Aggregations aggregations = searchResponse.getAggregations();
//        Terms byCompanyAggregation = aggregations.get("by_company");
//        Terms.Bucket elasticBucket = byCompanyAggregation.getBucketByKey("Elastic");
//        Avg averageAge = elasticBucket.getAggregations().get("average_age");
//        double avg = averageAge.getValue();

        // 获取建议结果
//        Suggest suggest = searchResponse.getSuggest();
//        TermSuggestion termSuggestion = suggest.getSuggestion("suggest_user");
//        for (TermSuggestion.Entry entry : termSuggestion.getEntries()) {
//            for (TermSuggestion.Entry.Option option : entry) {
//                String suggestText = option.getText().string();
//            }
//        }
        //处理搜索命中文档结果
        SearchHits hits = searchResponse.getHits();
        long totalHits = hits.getTotalHits();
        float maxScore = hits.getMaxScore();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            // do something with the SearchHit
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            //取_source字段值
            String sourceAsString = hit.getSourceAsString(); //取成json串
            Map<String, Object> sourceAsMap = hit.getSourceAsMap(); // 取成map对象
            //从map中取字段值
//            String documentTitle = (String) sourceAsMap.get("title");
//            List<Object> users = (List<Object>) sourceAsMap.get("user");
//            Map<String, Object> innerObject = (Map<String, Object>) sourceAsMap.get("innerObject");

            System.out.println("index:" + index + "  type:" + type + "  id:" + id);
            System.out.println("搜索命中文档：" + sourceAsString);

            //取高亮结果
//            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
//            HighlightField highlight = highlightFields.get("title");
//            Text[] fragments = highlight.fragments();
//            String fragmentString = fragments[0].string();
        }
    }

    @Test
    public void highlightQuery() {
        try {

            SearchRequest searchRequest = new SearchRequest(indexName);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("message", "lucene solr");
            sourceBuilder.query(matchQueryBuilder);
            // 高亮设置
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.requireFieldMatch(false).field("message").field("content").preTags("<strong>").postTags("</strong>");
            //不同字段可有不同设置，如不同标签
//            HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field("title");
//            highlightTitle.preTags("<strong>").postTags("</strong>");
//            highlightBuilder.field(highlightTitle);
//            HighlightBuilder.Field highlightContent = new HighlightBuilder.Field("content");
//            highlightContent.preTags("<b>").postTags("</b>");
//            highlightBuilder.field(highlightContent).requireFieldMatch(false);
            sourceBuilder.highlighter(highlightBuilder);
            searchRequest.source(sourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest);
            if (RestStatus.OK.equals(searchResponse.status())) {
                //处理搜索命中文档结果
                SearchHits hits = searchResponse.getHits();
                long totalHits = hits.getTotalHits();
                SearchHit[] searchHits = hits.getHits();
                for (SearchHit hit : searchHits) {
                    String index = hit.getIndex();
                    String type = hit.getType();
                    String id = hit.getId();
                    float score = hit.getScore();
                    System.out.println("搜索结果，score=" + score);
                    //取_source字段值
                    //String sourceAsString = hit.getSourceAsString(); //取成json串
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap(); // 取成map对象
                    //从map中取字段值
                    /*String title = (String) sourceAsMap.get("title");
                    String content  = (String) sourceAsMap.get("content"); */
                    System.out.println("index:" + index + "  type:" + type + "  id:" + id);
                    System.out.println("sourceMap : " + sourceAsMap);
                    //取高亮结果
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    HighlightField highlight = highlightFields.get("message");
                    if (highlight != null) {
                        Text[] fragments = highlight.fragments();  //多值的字段会有多个值
                        if (fragments != null) {
                            String fragmentString = fragments[0].string();
                            System.out.println("title highlight : " + fragmentString);
                            //可用高亮字符串替换上面sourceAsMap中的对应字段返回到上一级调用
                            //sourceAsMap.put("title", fragmentString);
                        }
                    }

                    highlight = highlightFields.get("content");
                    if (highlight != null) {
                        Text[] fragments = highlight.fragments();  //多值的字段会有多个值
                        if (fragments != null) {
                            String fragmentString = fragments[0].string();
                            System.out.println("content highlight : " + fragmentString);
                            //可用高亮字符串替换上面sourceAsMap中的对应字段返回到上一级调用
                            //sourceAsMap.put("content", fragmentString);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void suggestQuery() throws IOException {
        System.out.println("词项建议:词项建议拼写检查，检查用户的拼写是否错误，如果有错给用户推荐正确的词，appel->apple");
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);
        SuggestionBuilder termSuggestionBuilder = SuggestBuilders.termSuggestion("user").text("li");
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("suggest_user", termSuggestionBuilder);
        sourceBuilder.suggest(suggestBuilder);
        searchRequest.source(sourceBuilder);
        //3、发送请求
        SearchResponse searchResponse = client.search(searchRequest);
        //搜索结果状态信息
        if (RestStatus.OK.equals(searchResponse.status())) {
            // 获取建议结果
            Suggest suggest = searchResponse.getSuggest();
            TermSuggestion termSuggestion = suggest.getSuggestion("suggest_user");
            for (TermSuggestion.Entry entry : termSuggestion.getEntries()) {
                System.out.println("text: " + entry.getText().string());
                for (TermSuggestion.Entry.Option option : entry) {
                    String suggestText = option.getText().string();
                    System.out.println("   suggest option : " + suggestText);
                }
            }
        }
        System.out.println("--------------------------------------");
        System.out.println("自动补全，根据用户的输入联想到可能的词或者短语");
        searchRequest = new SearchRequest(indexName);
        sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);
        termSuggestionBuilder = SuggestBuilders.completionSuggestion("suggest").prefix("lucene s").skipDuplicates(true);
        suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("song-suggest", termSuggestionBuilder);
        sourceBuilder.suggest(suggestBuilder);
        searchRequest.source(sourceBuilder);
        searchResponse = client.search(searchRequest);
        if (RestStatus.OK.equals(searchResponse.status())) {
            // 获取建议结果
            Suggest suggest = searchResponse.getSuggest();
            CompletionSuggestion termSuggestion = suggest.getSuggestion("song-suggest");
            for (CompletionSuggestion.Entry entry : termSuggestion.getEntries()) {
                System.out.println("text: " + entry.getText().string());
                for (CompletionSuggestion.Entry.Option option : entry) {
                    String suggestText = option.getText().string();
                    System.out.println("   suggest option : " + suggestText);
                }
            }
        }
    }

    @Test
    public void aggregationQuery() {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);
        //加入聚合
        //字段值项分组聚合
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("by_age").field("age").order(BucketOrder.aggregation("average_balance", true));
        //计算每组的平均balance指标
        aggregation.subAggregation(AggregationBuilders.avg("average_balance").field("balance"));
        sourceBuilder.aggregation(aggregation);
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse searchResponse = client.search(searchRequest);
            //搜索结果状态信息
            if (RestStatus.OK.equals(searchResponse.status())) {
                // 获取聚合结果
                Aggregations aggregations = searchResponse.getAggregations();
                Terms byAgeAggregation = aggregations.get("by_age");
                System.out.println("aggregation by_age 结果");
                System.out.println("docCountError: " + byAgeAggregation.getDocCountError());
                System.out.println("sumOfOtherDocCounts: " + byAgeAggregation.getSumOfOtherDocCounts());
                System.out.println("------------------------------------");
                for (Terms.Bucket buck : byAgeAggregation.getBuckets()) {
                    System.out.println("key: " + buck.getKeyAsNumber());
                    System.out.println("docCount: " + buck.getDocCount());
                    System.out.println("docCountError: " + buck.getDocCountError());
                    //取子聚合
                    Avg averageBalance = buck.getAggregations().get("average_balance");

                    System.out.println("average_balance: " + averageBalance.getValue());
                    System.out.println("------------------------------------");
                }
                //直接用key 来去分组
                /*Bucket elasticBucket = byCompanyAggregation.getBucketByKey("24");
                Avg averageAge = elasticBucket.getAggregations().get("average_age");
                double avg = averageAge.getValue();*/
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
