package lt.swedbank.beans.response.serializers;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lt.swedbank.beans.entity.Skill;
import lt.swedbank.beans.entity.Team;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SkillSerializer extends StdSerializer<Skill> {


    public SkillSerializer() {
        this(null);
    }

    public SkillSerializer(Class<Skill> t) {
        super(t);
    }

    @Override
    public void serialize(Skill value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        Map<String, Object> skill = new HashMap<>();
        skill.put("id", value.getId());
        skill.put("title", value.getTitle());
        gen.writeObject(skill);
    }

}
