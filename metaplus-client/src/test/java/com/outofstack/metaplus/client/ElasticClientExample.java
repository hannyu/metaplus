package com.outofstack.metaplus.client;

//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.json.jackson.JacksonJsonpMapper;
//import co.elastic.clients.transport.ElasticsearchTransport;
//import co.elastic.clients.transport.rest_client.RestClientTransport;
//import org.apache.http.Header;
//import org.apache.http.HttpHost;
//import org.apache.http.message.BasicHeader;
//import org.elasticsearch.client.RestClient;

public class ElasticClientExample {

//    public static void main(String[] args) throws IOException {
//
//        // URL and API key
//        String serverUrl = "https://localhost:9200";
//        String apiKey = "VnVhQ2ZHY0JDZGJrU...";
//
//        // Create the low-level client
//        RestClient restClient = RestClient
//                .builder(HttpHost.create(serverUrl))
//                .setDefaultHeaders(new Header[]{
//                        new BasicHeader("Authorization", "ApiKey " + apiKey)
//                })
//                .build();
//
//        // Create the transport with a Jackson mapper
//        ElasticsearchTransport transport = new RestClientTransport(
//                restClient, new JacksonJsonpMapper());
//
//        // And create the API client
//        ElasticsearchClient esClient = new ElasticsearchClient(transport);
//
//        // Use the client...
//
//        // Close the client, also closing the underlying transport object and network connections.
//        esClient.close();
//    }
}
