package lt.swedbank.beans.response.team.teamOverview;


import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.response.TeamSkillTemplateResponse;
import lt.swedbank.beans.response.team.TeamResponse;

import java.util.List;

public class ColleagueTeamOverviewResponse extends TeamResponse {

    public ColleagueTeamOverviewResponse(Team team, List<TeamSkillTemplateResponse> teamSkillTemplateResponseList) {
        super(team, teamSkillTemplateResponseList);
    }

    public ColleagueTeamOverviewResponse(Team team) {
        super(team);
    }
}
