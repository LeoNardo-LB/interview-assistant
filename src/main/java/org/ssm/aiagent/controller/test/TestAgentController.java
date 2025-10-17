package org.ssm.aiagent.controller.test;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.ssm.aiagent.repository.LogRepository;
import org.ssm.aiagent.service.AgentSseService;

import java.time.Duration;

/**
 *
 *
 * @author Leonardo
 * @since 2025/9/3
 */
@Slf4j
@RestController
public class TestAgentController extends TestController {

    private final AgentSseService               agentSseService;
    private final RedisTemplate<String, Object> redisTemplate;

    public TestAgentController(AgentSseService agentSseService, RedisTemplate<String, Object> redisTemplate, LogRepository logRepository) {
        super(logRepository);
        this.agentSseService = agentSseService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("agent/get/{sessionId}")
    public SseEmitter test(@PathVariable String sessionId) {
        return agentSseService.handleConnection(sessionId);
    }

    @GetMapping("agent/generate/{key}/{cycleCount}")
    public boolean generate(@PathVariable String key, @PathVariable Integer cycleCount) {
        new Thread(() -> {
            JSONObject jsonObject = new JSONObject();
            StreamOperations<String, Object, Object> opsForStream = redisTemplate.opsForStream();
            redisTemplate.delete(key);
            for (int i = 0; i < cycleCount; i++) {
                jsonObject.put("content", String.valueOf(i));
                opsForStream.add(key, jsonObject);
                redisTemplate.expire(key, Duration.ofMinutes(1));
                try {
                    Thread.sleep(RandomUtils.secure().randomInt(10, 200));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            jsonObject.put("content", "[DONE]");
            opsForStream.add(key, jsonObject);
        }).start();
        return true;
    }

}
