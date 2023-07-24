package pl.zabicki.billing.elasticsearch.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableElasticsearchRepositories(basePackages = "pl.zabicki.billing.elasticsearch.repository")
//@ComponentScan(basePackages = {"pl.zabicki.billing.elasticsearch"})
public class ESConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder(new HttpHost("localhost", 9200)).build();
    }

    @Bean
    public ElasticsearchTransport elasticsearchTransport() {
      return new RestClientTransport(restClient(), new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        return new ElasticsearchClient(elasticsearchTransport());
    }
}
