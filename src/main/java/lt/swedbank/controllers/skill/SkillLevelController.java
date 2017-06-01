package lt.swedbank.controllers.skill;

import lt.swedbank.beans.response.SkillLevelResponse;
import lt.swedbank.services.skill.SkillLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = "*")
@RequestMapping(value = "skill/levels")
public class SkillLevelController {

    @Autowired
    private SkillLevelService skillService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody
    List<SkillLevelResponse> getUser() {
        return skillService.getAll();
    }
}
