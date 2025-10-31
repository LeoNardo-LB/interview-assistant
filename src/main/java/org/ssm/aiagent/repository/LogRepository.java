package org.ssm.aiagent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ssm.aiagent.entity.dataobj.LogDO;

/**
 * @author Leonardo
 * @since 2025/7/14
 * log 仓储
 */
@Repository
public interface LogRepository extends JpaRepository<LogDO, Long> {

}