package lt.swedbank.controllers.skill;

import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.response.SkillEntityResponse;
import lt.swedbank.services.skill.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/skill")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public @ResponseBody

    Iterable<SkillEntityResponse> getUser(@RequestHeader(value = "Authorization") String authToken) {
        return skillService.getSkillEntityResponseList();
    }


}
