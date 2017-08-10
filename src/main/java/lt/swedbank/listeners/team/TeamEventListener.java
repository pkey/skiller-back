package lt.swedbank.listeners.team;

import lt.swedbank.events.team.TeamAddedEvent;
import lt.swedbank.events.team.TeamUpdatedEvent;
import lt.swedbank.services.teamSkill.TeamSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TeamEventListener {

    @Autowired
    private TeamSkillService teamSkillService;

    @EventListener
    public void onTeamAddedEvent(TeamAddedEvent event) {
        teamSkillService.createTeamSkills(event.getTeam());
    }

    @EventListener
    public void onTeamUpdatedEvent(TeamUpdatedEvent event) {
        teamSkillService.updateTeamSkills(event.getTeam());
    }
}