package lt.swedbank.events.team;

import lombok.Data;
import lombok.NonNull;
import lt.swedbank.beans.entity.Team;

@Data
public class TeamUpdatedEvent {
    @NonNull
    private Team team;
}
