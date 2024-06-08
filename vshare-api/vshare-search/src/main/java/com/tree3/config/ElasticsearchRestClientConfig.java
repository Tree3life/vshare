package com.tree3.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/16 16:31 </p>
 */
@Configuration
public class ElasticsearchRestClientConfig extends AbstractElasticsearchConfiguration {

    @Value("${esConfig.addr}")
    private String elasticsearchClientAddress;

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(elasticsearchClientAddress)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
}
