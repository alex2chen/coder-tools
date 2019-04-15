package com.github.search.es;

/**
 * @Author: alex
 * @Description: 放弃spring-data-elasticsearch，6.2.4兼容性不行
 * @Date: created in 2018/2/18.
 */
public class EsTransportClient_test {
    /**
     * lucene guard: https://blog.csdn.net/eff666/article/details/52916355
     * 官方：https://search-guard.com/searchguard-elasicsearch-transport-clients/
     */
//    @Test
//    public void searchGuard() throws IOException {
//        String path = "C:\\Users\\fei.chen\\Documents\\demo\\src\\main\\resources\\";
//        String kf = path + "node-1-keystore.jks";//私钥证书位置，一般保存我们的私钥，用来加密解密或者为别人做签名
//        String tf = path + "truststore.jks";    //公钥证书位置
//        Settings settings = Settings.builder()
//                //.put("cluster.name", "oms-trans")
//                .put("client.transport.ignore_cluster_name", true)
//                .put("client.transport.sniff", "true")
//                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_ENABLED, true) //searchguard.ssl.transport.enabled
//                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_FILEPATH, kf) //searchguard.ssl.transport.keystore_filepath
//                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_KEYSTORE_PASSWORD, "Kxtxoms") //searchguard.ssl.transport.keystore_password
//                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_FILEPATH, tf)    //searchguard.ssl.transport.truststore_filepath
//                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_TRUSTSTORE_PASSWORD, "Kxtxoms") //searchguard.ssl.transport.truststore_password
//                .put(SSLConfigConstants.SEARCHGUARD_SSL_TRANSPORT_ENFORCE_HOSTNAME_VERIFICATION, false) //searchguard.ssl.transport.enforce_hostname_verification
////                .putList("searchguard.authcz.admin_dn", "CN=kxadmin,OU=IT,O=dev-oms,L=Shanghai,C=CN")
//                .build();
//        TransportClient client = new PreBuiltTransportClient(settings, SearchGuardSSLPlugin.class)
//                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.42"), 9300))
//                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.43"), 9300))
//                .addTransportAddress(new TransportAddress(InetAddress.getByName("192.168.1.44"), 9300));
//        client.threadPool().getThreadContext().putHeader("Authorization", "Basic " + Base64Utils.encodeToString("omsuser:kxtxomsuser".getBytes()));
//        System.out.println("ElasticsearchClient 启动成功");
//        ArticleMapper articleMapper = new ArticleMapper(client);
//        try {
//            System.out.println("索引是否存在:" + articleMapper.isIndexExist(ArticleMapper.article));
//            articleMapper.createMappingIndex();
//            articleMapper.addIndexAndDocument();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        //搜索数据
//        GetResponse response = client.prepareGet("test", "name", "1").execute().actionGet();
//        System.out.println(response.getSourceAsString());
//        client.close();
//    }

}
