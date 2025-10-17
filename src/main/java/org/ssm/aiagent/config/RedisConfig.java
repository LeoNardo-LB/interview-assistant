package org.ssm.aiagent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;

/**
 * @author Leonardo
 * @since 2025/9/3
 */
@Configuration
public class RedisConfig {
    
    private static final String STREAM_KEY = "ai-responses-stream";
    
    private static final String CONSUMER_GROUP = "sse-service-group";
    
    private static final String CONSUMER_NAME = "consumer-1";
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        return template;
    }
    
    @Bean
    public StreamMessageListenerContainer<String, ObjectRecord<String, String>> streamMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory) {
        // 1. 配置监听容器选项
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options
                = StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
                          .pollTimeout(Duration.ofSeconds(5)) // 阻塞 poll 的超时时间
                          .targetType(String.class) // 消息体类型
                          .build();
        // 2. 创建监听容器
        return StreamMessageListenerContainer.create(redisConnectionFactory, options);
    }

}
