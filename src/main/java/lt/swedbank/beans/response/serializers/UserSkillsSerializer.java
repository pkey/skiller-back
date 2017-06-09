package lt.swedbank.beans.response.serializers;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lt.swedbank.beans.entity.UserSkill;
import lt.swedbank.beans.entity.UserSkillLevel;
import lt.swedbank.beans.entity.Vote;
import lt.swedbank.services.skill.UserSkillLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

public class UserSkillsSerializer extends StdSerializer<List<UserSkill>> {

    public UserSkillsSerializer() {
        this(null);
    }

    public UserSkillsSerializer(Class<List<UserSkill>> t) {
        super(t);
    }

    @Override
    public void serialize(List<UserSkill> userSkills, JsonGenerator gen, SerializerProvider provider) throws IOException {

        List<Map<String, Object>> skills = new ArrayList<>();
        for (UserSkill userSkill : userSkills) {
            Map<String, Object> skill = new HashMap<>();
            skill.put("id", userSkill.getSkill().getId());
            skill.put("title", userSkill.getSkill().getTitle());
            skill.put("level", formatLevel(userSkill));
            skill.put("votes", formatVotes(userSkill));
            skills.add(skill);
        }
        gen.writeObject(skills);
    }

    private Map<String, Object> formatLevel(UserSkill userSkill){
        Map<String, Object> level = new HashMap<>();

        UserSkillLevel userSkillLevel = getCurrentUserSkillLevelFromList(userSkill.getUserSkillLevels());



        if(userSkillLevel == null)
            return null;

        level.put("id", userSkillLevel.getId());
        level.put("title", userSkillLevel.getSkillLevel().getTitle());

        return level;

    }

    private List<Map<String, Object>> formatVotes(UserSkill userSkill){
        List<Map<String, Object>> votes = new ArrayList<>();

        UserSkillLevel userSkillLevel = getCurrentUserSkillLevelFromList(userSkill.getUserSkillLevels());

        if(userSkillLevel == null)
            return null;

        for (Vote vote : userSkillLevel.getVotes()) {
            Map<String, Object> voteMap = new HashMap<>();

            voteMap.put("id", vote.getId());
            voteMap.put("message", vote.getMessage());
            voteMap.put("voter", vote.getVoter().getId());

            votes.add(voteMap);
        }

        return votes;

    }

    public UserSkillLevel getCurrentUserSkillLevelFromList(List<UserSkillLevel> userSkillLevelList){
        UserSkillLevel currentUserSkillLevel;

        if(userSkillLevelList == null){
            return null;
        }
        userSkillLevelList.sort(new Comparator<UserSkillLevel>() {
            @Override
            public int compare(UserSkillLevel o1, UserSkillLevel o2) {
                return o2.getValidFrom().compareTo(o1.getValidFrom());
            }
        });

        currentUserSkillLevel = userSkillLevelList.get(0);

        return currentUserSkillLevel;
    }

}
