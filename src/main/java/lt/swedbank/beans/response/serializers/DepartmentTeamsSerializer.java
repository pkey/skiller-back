package lt.swedbank.beans.response.serializers;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lt.swedbank.beans.entity.Team;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepartmentTeamsSerializer extends StdSerializer<List<Team>>{

    public DepartmentTeamsSerializer() {
        this(null);
    }

    public DepartmentTeamsSerializer(Class<List<Team>> t) {
        super(t);
    }

    @Override
    public void serialize(List<Team> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        List<Map<String, Object>> teams = new ArrayList<>();
        for(Team team : value){
            Map<String, Object> teamMap = new HashMap<>();
            teamMap.put("id", team.getId());
            teamMap.put("name", team.getName());
            teams.add(teamMap);
        }
        gen.writeObject(teams);
    }
}
