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
@Table(name = "`skill_details`")
public class SkillDetailDO extends BaseDO {

    private Long skillId;

    private Long parentId;

    private String name;

    private String path;

    @Column(columnDefinition = "TEXT", name = "`desc`")
    private String desc;

    private Integer importance;

}
