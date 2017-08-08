package lt.swedbank.listeners.userSkill;

import lt.swedbank.beans.entity.Team;
import lt.swedbank.events.userSkill.UserSkillAddedEvent;
import lt.swedbank.events.userSkill.UserSkillRemovedEvent;
import lt.swedbank.events.userSkillLevel.UserSkillLevelRaisedEvent;
import lt.swedbank.services.teamSkill.TeamSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import java.util.Optional;

public class UserSkillEventListener {
    @Autowired
    private TeamSkillService teamSkillService;

    @EventListener
    public void onUserSkillAddedEvent(UserSkillAddedEvent userSkillAddedEvent) {
        Optional<Team> team = userSkillAddedEvent.getUserSkill().getUser().getTeam();
        team.ifPresent(t -> teamSkillService.updateTeamSkill(t,
                userSkillAddedEvent.getUserSkill().getSkill()));
    }

    @EventListener
    public void onUserSkillRemovedEvent(UserSkillRemovedEvent userSkillRemovedEvent) {
        Optional<Team> team = userSkillRemovedEvent.getUserSkill().getUser().getTeam();
        team.ifPresent(t -> teamSkillService.updateTeamSkill(t,
                userSkillRemovedEvent.getUserSkill().getSkill()));
    }

    @EventListener
    public void onUserSkillLevelRaisedEvent(UserSkillLevelRaisedEvent userSkillRaisedEvent) {
        Optional<Team> team = userSkillRaisedEvent.getUserSkillLevel().getUserSkill().getUser().getTeam();
        team.ifPresent(t -> teamSkillService.updateTeamSkill(t,
                userSkillRaisedEvent.getUserSkillLevel().getUserSkill().getSkill()));
    }
}
