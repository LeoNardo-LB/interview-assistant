package org.ssm.aiagent.entity.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 *
 *
 * @author Leonardo
 * @since 2025/10/29
 */
@Getter
@Setter
public abstract class BaseModel {

    private Long id;

    private Instant createTime;

    private Instant updateTime;

    private String createUser;

    private String updateUser;

}
