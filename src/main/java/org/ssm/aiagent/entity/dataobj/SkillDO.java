package org.ssm.aiagent.entity.dataobj;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 * @author Leonardo
 * @since 2025/10/29
 */
@Getter
@Setter
@Entity
@Table(name = "`skill`")
public class SkillDO extends BaseDO {

    private String name;

    @Column(columnDefinition = "TEXT", name = "`desc`")
    private String desc;

}
