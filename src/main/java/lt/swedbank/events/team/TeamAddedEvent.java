package lt.swedbank.events.team;


import lombok.Data;
import lombok.NonNull;
import lt.swedbank.beans.entity.Team;

@Data
public class TeamAddedEvent {
    @NonNull
    private Team team;
}
