package org.ssm.aiagent.repository;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.ssm.aiagent.entity.dataobj.SkillDetailDO;
import org.ssm.aiagent.entity.model.SkillDetail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 *
 * @author Leonardo
 * @since 2025/10/29
 */
@Repository
public class SkillDetailRepositoryEH {

    private final SkillDetailRepository skillDetailRepository;

    public SkillDetailRepositoryEH(SkillDetailRepository skillDetailRepository) {
        this.skillDetailRepository = skillDetailRepository;
    }

    /**
     * 查询树状结构数据
     * @param skillId 技能id
     * @return 树状结构数据
     */
    public List<SkillDetail> findTreeBySkillId(Long skillId) {
        List<SkillDetailDO> skillDetailDOS = skillDetailRepository.queryBySkillId(skillId);
        Map<Long, SkillDetail> idMap = skillDetailDOS.stream().map(aDo -> BeanUtil.toBean(aDo, SkillDetail.class))
                                               .collect(Collectors.toMap(SkillDetail::getId, skillDetail -> skillDetail));
        List<SkillDetail> roots = new ArrayList<>();
        for (SkillDetail skillDetail : idMap.values()) {
            if (skillDetail.getParentId() == 0L) {
                roots.add(skillDetail);
                continue;
            }
            SkillDetail parent = idMap.get(skillDetail.getParentId());
            if (parent != null) {
                // 优化：减少重复的 Optional 调用
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(skillDetail);
            }
        }
        return roots;
    }

    /**
     * 删除旧数据，保存新的树状结构数据
     * @param skillId      技能id
     * @param skillDetails 新的树状结构数据
     */
    @Transactional
    public void saveTreeCoverSkill(Long skillId, List<SkillDetail> skillDetails) {
        Set<SkillDetail> saves = new HashSet<>();
        skillDetailRepository.deleteBySkillId(skillId);
        for (SkillDetail skillDetail : skillDetails) {
            skillDetail.setPath("/" + skillDetail.getName());
            recursiveList(skillDetail, saves);
        }
        saves.forEach(skillDetail -> {
            skillDetail.setSkillId(skillId);
            SkillDetailDO skillDetailDO = BeanUtil.toBean(skillDetail, SkillDetailDO.class);
            skillDetailDO.onCreate();
            skillDetailRepository.insert(skillDetailDO.getId(), skillDetailDO.getCreateTime(), skillDetailDO.getUpdateTime(),
                    skillDetailDO.getCreateUser(), skillDetailDO.getUpdateUser(), skillDetailDO.getSkillId(), skillDetailDO.getParentId(),
                    skillDetailDO.getName(), skillDetailDO.getPath(), skillDetailDO.getDesc(), skillDetailDO.getImportance());
        });
    }

    private void recursiveList(SkillDetail skillDetail, Set<SkillDetail> saves) {
        saves.add(skillDetail);
        Optional.ofNullable(skillDetail.getChildren()).ifPresent(children -> {
            for (SkillDetail child : children) {
                child.setParentId(skillDetail.getId());
                child.setPath(skillDetail.getPath() + "/" + child.getName());
                Assert.isTrue(!saves.contains(child), "子元素嵌套");
                recursiveList(child, saves);
            }
        });
    }

}
