package org.ssm.aiagent.service;

import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;

/**
 *
 *
 * @author Leonardo
 * @since 2025/10/12
 */
public interface TestAiService {

    @SystemMessage(
            "你是一个用户信息解析工具，你擅长从用户输入的信息中解析用户的信息。如果无法解析某个字段的内容，则将那个字段置为空（null）就行")
    Result<User> parseUserInfo(String query);

}
