package org.ssm.aiagent.entity.dataobj;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 * @author Leonardo
 * @since 2025/10/28
 */
@Getter
@Setter
@Entity
@Table(name = "`user`")
public class UserDO extends BaseDO {

    private String code;

    private String name;

    private String password;

}
