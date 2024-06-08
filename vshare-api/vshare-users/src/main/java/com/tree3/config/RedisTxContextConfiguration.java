package com.tree3.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
//@EnableTransactionManagement
public class RedisTxContextConfiguration {
  @Autowired
  public RedisTxContextConfiguration(RedisTemplate redisTemplate) {
    // 使用Jackson2JsonRedisSerialize 替换默认序列化
    Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
    // 设置value的序列化规则和 key的序列化规则
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    //jackson2JsonRedisSerializer就是JSON序列号规则，
    redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

//        redisTemplate.setEnableTransactionSupport(true);
    //工厂创建redisTemplate对象之后在进行配置
    redisTemplate.afterPropertiesSet();
  }

//  @Bean
//  public StringRedisTemplate redisTemplate() {
//    StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory());
//    // explicitly enable transaction support
//    template.setEnableTransactionSupport(true);
//    return template;
//  }
//
//  @Bean
//  public RedisConnectionFactory redisConnectionFactory() {
//    // jedis || Lettuce
//  }
////配置事务管理器
//  @Bean
//  public PlatformTransactionManager transactionManager() throws SQLException {
//    return new DataSourceTransactionManager(dataSource());
//  }
//
//  @Bean
//  public DataSource dataSource() throws SQLException {
//    // ...
//  }
}