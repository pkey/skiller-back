package lt.swedbank.services.skill;

import lt.swedbank.beans.entity.SkillLevel;
import lt.swedbank.beans.response.SkillLevelResponse;
import lt.swedbank.repositories.SkillLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkillLevelService {

    public static int DEFAULT_SKILL_LEVEL = 1;

    @Autowired
    private SkillLevelRepository skillLevelRepository;

    public Iterable<SkillLevel> getAll(){
        return skillLevelRepository.findAll();
    }

    public SkillLevel getByLevel(Long level) {
        return skillLevelRepository.findByLevel(level);
    }

    public SkillLevel getById(Long id) {
        return skillLevelRepository.findOne(id);
    }

    public SkillLevel getDefault() {
        Long defaultLevel = new Long(DEFAULT_SKILL_LEVEL);
        return skillLevelRepository.findByLevel(defaultLevel);
    }

    public List<SkillLevelResponse> getSkillLevelResponseList() {
        List<SkillLevelResponse> skillLevelResponseList = new ArrayList<>();
        for (SkillLevel skillLevel : this.getAll()
                ) {
            skillLevelResponseList.add(new SkillLevelResponse(skillLevel));
        }
        return skillLevelResponseList;
    }





}
