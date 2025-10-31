package org.ssm.aiagent.entity.dataobj;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 *
 *
 * @author Leonardo
 * @since 2025/10/29
 */

@Getter
@Setter
@Entity
@Table(name = "`skill_assessment_evidence`")
public class SkillAssessmentEvidenceDO extends BaseDO {

    private Long assessmentId;

    private Long skillId;

    private Long skillDetailId;

    private Boolean positive;

    private Integer degree;

    private BigDecimal confidence;

    private String evidence;

    private String explain;

}
