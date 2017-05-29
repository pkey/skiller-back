package lt.swedbank.beans.response.serializers;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lt.swedbank.beans.entity.UserSkill;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserSkillsSerializer extends StdSerializer<List<UserSkill>>{

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
                skills.add(skill);
            }
            gen.writeObject(skills);
    }
}
