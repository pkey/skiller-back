package lt.swedbank.services.overview;

import lt.swedbank.services.skill.UserSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OverviewService {

    @Autowired
    private UserSkillService userSkillService;

//    public Double getUserAverageSkillLevel(List<User> users, Skill skill) {
//        int counter = 0;
//        double sum = 0;
//        for (User user : users
//                ) {
//            for (UserSkill userSkill : user.getUserSkills()
//                    ) {
//                if (userSkill.getSkill().equals(skill)) {
//                    counter++;
//                    sum += userSkillService.getCurrentSkillLevel(userSkill).getSkillLevel().getLevel();
//                }
//            }
//        }
//        if (counter == 0) {
//            return 0d;
//        }
//        return sum / counter;
//    }
//
//    public Integer getUserSkillCount(List<User> users, Skill skill) {
//        int counter = 0;
//        for (User user : users
//                ) {
//            for (UserSkill userSkill : user.getUserSkills()
//                    ) {
//                if (userSkill.getSkill().equals(skill)) {
//                    counter++;
//                }
//            }
//        }
//        return counter;
//    }

}
