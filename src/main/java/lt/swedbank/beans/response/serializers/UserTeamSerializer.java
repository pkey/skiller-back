package lt.swedbank.beans.response.serializers;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lt.swedbank.beans.entity.Team;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserTeamSerializer extends StdSerializer<Team> {
    public UserTeamSerializer() {
        this(null);
    }

    public UserTeamSerializer(Class<Team> t) {
        super(t);
    }

    @Override
    public void serialize(Team value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        Map<String, Object> team = new HashMap<>();
        team.put("id", value.getId());
        team.put("name", value.getName());

        gen.writeObject(team);
    }
}
