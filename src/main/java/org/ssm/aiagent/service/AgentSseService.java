package org.ssm.aiagent.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class AgentSseService {

    public static final String SESSION_STREAM_PREFIX = "session:";
    public static final int    TOTAL_TIMEOUT         = 3600;
    public static final int    FETCH_COUNT           = 32;
    public static final int    FETCH_TIMEOUT         = 600;

    private final RedisTemplate<String, Object> redisTemplate;
    private final ExecutorService               executorService = Executors.newVirtualThreadPerTaskExecutor();

    public AgentSseService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 处理新客户端连接 - 统一逻辑
     */
    public SseEmitter handleConnection(String sessionId) {
        SseEmitter emitter = new SseEmitter(TOTAL_TIMEOUT * 1000L);
        registerForRealtime(sessionId, emitter);
        return emitter;
    }

    private void registerForRealtime(String sessionId, SseEmitter emitter) {
        log.info("Session [{}] connected, starting stream replay.", sessionId);
        String key = SESSION_STREAM_PREFIX + sessionId;
        StreamOperations<String, Object, Object> streamOps = redisTemplate.opsForStream();

        executorService.execute(() -> {
            String lastId = "0";
            boolean done = false;

            try {
                // Step1: 拉取所有历史消息（从 0 到最新）
                List<MapRecord<String, Object, Object>> history = streamOps.range(key, Range.unbounded());
                if (CollectionUtils.isNotEmpty(history)) {
                    for (MapRecord<String, Object, Object> record : history) {
                        Map<Object, Object> value = record.getValue();
                        String content = (String) value.get("content");
                        if ("[DONE]".equals(content)) {
                            emitter.complete();
                            return;
                        }
                        emitter.send(SseEmitter.event().data(value));
                        lastId = record.getId().getValue();
                    }
                }
                // Step2: 阻塞式监听新消息
                StreamReadOptions opt = StreamReadOptions.empty().count(FETCH_COUNT).block(Duration.ofSeconds(FETCH_TIMEOUT));
                while (!done) {
                    List<MapRecord<String, Object, Object>> msgs = streamOps.read(opt, StreamOffset.create(key, ReadOffset.from(lastId)));
                    // 阻塞超时，继续轮询（或可 break 退出）
                    if (CollectionUtils.isEmpty(msgs))
                        break;
                    for (MapRecord<String, Object, Object> record : msgs) {
                        Map<Object, Object> value = record.getValue();
                        String content = (String) value.get("content");
                        if ("[DONE]".equals(content)) {
                            emitter.complete();
                            done = true;
                            break;
                        }
                        try {
                            emitter.send(SseEmitter.event().data(value));
                            lastId = record.getId().getValue();
                        } catch (IOException e) {
                            // 客户端断开连接（网络关闭、浏览器关闭等）
                            log.warn("SSE client disconnected for session: {}", sessionId);
                            done = true;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Unexpected error in SSE stream for session: {}", sessionId, e);
                // 尝试完成 emitter（即使可能已断开）
                try {
                    emitter.completeWithError(e);
                } catch (Exception ignored) {
                    // 忽略二次异常
                    log.warn("Failed to complete SSE emitter for session: {}", sessionId, e);
                }
            } finally {
                // 确保最终完成（幂等）
                try {
                    emitter.complete();
                } catch (Exception ignored) {
                    // 可能已关闭，忽略
                }
            }
        });
    }

}