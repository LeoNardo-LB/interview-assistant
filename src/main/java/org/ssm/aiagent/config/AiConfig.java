package org.ssm.aiagent.config;

import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.ssm.aiagent.config.AiProperties.GlobalConfig;
import org.ssm.aiagent.util.MyBeanUtils;

import java.time.Duration;

/**
 *
 *
 * @author Leonardo
 * @since 2025/10/12
 */
@Configuration
public class AiConfig {

    private final AiProperties aiProperties;

    public AiConfig(AiProperties aiProperties) {this.aiProperties = aiProperties;}

    @Bean
    @Primary
    public OpenAiChatModel defaultModel() {
        return OpenAiChatModel.builder()
                       .baseUrl(aiProperties.getGlobal().getBaseUrl())
                       .apiKey(aiProperties.getGlobal().getApikey())
                       .modelName(aiProperties.getGlobal().getModel())
                       .temperature(aiProperties.getGlobal().getTemperature())
                       .timeout(Duration.ofSeconds(aiProperties.getGlobal().getTimeout()))
                       .returnThinking(true)
                       .logRequests(true)
                       .logResponses(true)
                       .build();
    }

    @Bean
    public OpenAiChatModel reasonerModel() {
        GlobalConfig globalConfig = aiProperties.getGlobal();
        GlobalConfig config = MyBeanUtils.combineFields(globalConfig.getClass(), globalConfig, aiProperties.getReason());
        return OpenAiChatModel.builder()
                       .baseUrl(config.getBaseUrl())
                       .apiKey(config.getApikey())
                       .modelName(config.getModel())
                       .temperature(config.getTemperature())
                       .timeout(Duration.ofSeconds(config.getTimeout()))
                       .returnThinking(true)
                       .logRequests(true)
                       .logResponses(true)
                       .build();
    }

}
