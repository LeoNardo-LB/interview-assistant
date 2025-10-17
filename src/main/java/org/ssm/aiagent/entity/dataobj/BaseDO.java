package org.ssm.aiagent.entity.dataobj;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.ssm.aiagent.util.UserContext;

import java.time.Instant;

/**
 * @author Leonardo
 * @since 2025/7/14
 * 基础实体
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseDO {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Column(nullable = false, updatable = false)
    private Instant createTime;
    
    @Column(nullable = false)
    private Instant updateTime;
    
    @Column(nullable = false, updatable = false)
    private String createUser;
    
    @Column(nullable = false)
    private String updateUser;

    /**
     * 设置创建动作
     */
    @PrePersist
    public void onCreate() {
        createTime = Instant.now();
        updateTime = Instant.now();
        createUser = UserContext.getId();
        updateUser = UserContext.getId();
    }
    
    /**
     * 设置更新动作
     */
    @PreUpdate
    public void onUpdate() {
        updateTime = Instant.now();
        updateUser = UserContext.getId();
    }
    
}