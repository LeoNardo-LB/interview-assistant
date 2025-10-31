package org.ssm.aiagent.entity.dataobj;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 * @author Leonardo
 * @since 2025/10/30
 */

@Getter
@Setter
@Entity
@Table(name = "`skill_question`")
public class SkillQuestionDO extends BaseDO {

    /**
     * 技能详情id
     */
    private Long skillDetailId;

    /**
     * 维度
     */
    private String dimension;

    /**
     * 问题内容
     */
    private String content;

}
