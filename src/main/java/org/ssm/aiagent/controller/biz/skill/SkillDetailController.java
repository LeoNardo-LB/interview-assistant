package org.ssm.aiagent.controller.biz.skill;

import com.alibaba.fastjson2.JSON;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.ssm.aiagent.entity.model.SkillDetail;
import org.ssm.aiagent.repository.SkillDetailRepositoryEH;

import java.io.IOException;
import java.util.List;

/**
 *
 *
 * @author Leonardo
 * @since 2025/10/30
 */
@RestController
@RequestMapping("skill/detail")
public class SkillDetailController {

    private final SkillDetailRepositoryEH skillDetailRepositoryEH;

    public SkillDetailController(SkillDetailRepositoryEH skillDetailRepositoryEH) {this.skillDetailRepositoryEH = skillDetailRepositoryEH;}

    @PostMapping("load")
    public void loadJavaSkill(MultipartFile multipartFile) throws IOException {
        byte[] bytes = multipartFile.getBytes();
        List<SkillDetail> skillDetails = JSON.parseArray(bytes, SkillDetail.class);
        skillDetailRepositoryEH.saveTreeCoverSkill(1L, skillDetails);
    }

    @GetMapping("tree")
    public List<SkillDetail> findTreeBySkillId(Long skillId) {
        return skillDetailRepositoryEH.findTreeBySkillId(skillId);
    }

}
