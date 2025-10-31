package org.ssm.aiagent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.ssm.aiagent.entity.dataobj.SkillDetailDO;

import java.time.Instant;
import java.util.List;

/**
 *
 *
 * @author Leonardo
 * @since 2025/10/29
 */
@Repository
public interface SkillDetailRepository extends JpaRepository<SkillDetailDO, Long> {

    void deleteBySkillId(Long skillId);

    List<SkillDetailDO> queryBySkillId(Long skillId);

    @Modifying
    @Query(nativeQuery = true, value = """
                insert into skill_details ("id", "create_time", "update_time", "create_user", "update_user", "skill_id", "parent_id", "name", "path", "desc", "importance")
                    values(:id, :createTime, :updateTime, :createUser, :updateUser, :skillId, :parentId, :name, :path, :desc, :importance)
            """)
    int insert(@Param("id") Long id,
               @Param("createTime") Instant createTime,
               @Param("updateTime") Instant updateTime,
               @Param("createUser") String createUser,
               @Param("updateUser") String updateUser,
               @Param("skillId") Long skillId,
               @Param("parentId") Long parentId,
               @Param("name") String name,
               @Param("path") String path,
               @Param("desc") String desc,
               @Param("importance") Integer importance);

}