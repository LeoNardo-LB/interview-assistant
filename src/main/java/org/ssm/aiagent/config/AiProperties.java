package org.ssm.aiagent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 *
 * @author Leonardo
 * @since 2025/10/12
 */
@Configuration
@ConfigurationProperties(prefix = "ai")
@Data
public class AiProperties {

    private GlobalConfig global = new GlobalConfig();
    private ReasonConfig reason = new ReasonConfig();

    @Data
    public static class GlobalConfig {

        private String apikey;
        private String baseUrl;
        private String model;
        private Double temperature;
        private String maxTokens;
        private String topP;
        private String frequencyPenalty;
        private String presencePenalty;
        private Long   timeout = 600L;

    }

    @Data
    public static class ReasonConfig {

        private String apikey;
        private String baseUrl;
        private String model;
        private Double temperature;
        private String maxTokens;
        private String topP;
        private String frequencyPenalty;
        private String presencePenalty;
        private Long   timeout = 600L;

    }

}
