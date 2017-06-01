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



    @Autowired
    private SkillLevelRepository skillLevelRepository;

    public List<SkillLevelResponse> getAll() {
        List<SkillLevelResponse> skillLevelResponseList = new ArrayList<>();
        for (SkillLevel skillLevel : skillLevelRepository.findAll()
                ) {
            skillLevelResponseList.add(new SkillLevelResponse(skillLevel));
        }
        return skillLevelResponseList;
    }

    public SkillLevel getDefault() {
        Long defaultLevel = new Long(1);
        return skillLevelRepository.findByLevel(defaultLevel);
    }


}
