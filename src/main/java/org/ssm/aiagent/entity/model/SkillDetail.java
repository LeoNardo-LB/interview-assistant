package org.ssm.aiagent.entity.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 *
 * @author Leonardo
 * @since 2025/10/29
 */
@Getter
@Setter
public class SkillDetail extends BaseModel {

    private Long skillId;

    private Long parentId;

    private String path;

    private String name;

    private String desc;

    private Integer importance;

    private List<SkillDetail> children;

}
