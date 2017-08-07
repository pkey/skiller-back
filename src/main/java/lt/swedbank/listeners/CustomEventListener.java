package lt.swedbank.listeners;

import lt.swedbank.beans.entity.Team;
import lt.swedbank.events.team.TeamAddedEvent;
import lt.swedbank.events.team.TeamUpdatedEvent;
import lt.swedbank.events.userSkill.UserSkillAddedEvent;
import lt.swedbank.events.userSkill.UserSkillRemovedEvent;
import lt.swedbank.events.userSkillLevel.UserSkillLevelRaisedEvent;
import lt.swedbank.services.teamSkill.TeamSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomEventListener {
    @Autowired
    TeamSkillService teamSkillService;

    @EventListener
    public void onTeamAddedEvent(TeamAddedEvent event) {
        teamSkillService.createTeamSkills(event.getTeam());
    }

    @EventListener
    public void onTeamUpdatedEvent(TeamUpdatedEvent event) {
        teamSkillService.updateTeamSkills(event.getTeam());
    }

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