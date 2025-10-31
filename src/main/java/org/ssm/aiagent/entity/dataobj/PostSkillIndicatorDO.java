package org.ssm.aiagent.entity.dataobj;

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
@Table(name = "`post_skill_indicator`")
public class PostSkillIndicatorDO extends BaseDO {

    private Long postId;

    private Long skillId;

    private Integer score;

    private String explain;

}
