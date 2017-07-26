package lt.swedbank.services.team;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.team.AddTeamRequest;
import lt.swedbank.beans.response.TeamSkillTemplateResponse;
import lt.swedbank.beans.response.team.TeamResponse;
import lt.swedbank.beans.response.team.teamOverview.ColleagueTeamOverviewResponse;
import lt.swedbank.beans.response.team.teamOverview.NonColleagueTeamOverviewResponse;
import lt.swedbank.beans.response.user.UserWithSkillsResponse;
import lt.swedbank.exceptions.team.TeamNameAlreadyExistsException;
import lt.swedbank.exceptions.team.TeamNotFoundException;
import lt.swedbank.repositories.SkillTemplateRepository;
import lt.swedbank.repositories.TeamRepository;
import lt.swedbank.services.department.DepartmentService;
import lt.swedbank.services.skill.UserSkillService;
import lt.swedbank.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private SkillTemplateRepository skillTemplateRepository;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserSkillService userSkillService;


    public Iterable<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public List<TeamResponse> getAllTeamOverviewResponses() {

        List<TeamResponse> TeamResponses = new ArrayList<>();

        for (Team team : teamRepository.findAll()) {
            TeamResponses.add(new ColleagueTeamOverviewResponse(team,getUserWithSkillResponseList(team.getUsers()), getTeamSkillTemplateResponseList(team)));
        }

        return TeamResponses;
    }

    public Team getTeamById(Long id) {
        Team team = teamRepository.findOne(id);
        if(team == null) {
            throw new TeamNotFoundException();
        }
        return team;
    }


    public TeamResponse getTeamOverview(Long teamId, Long currentUserId) {
        User user = userService.getUserById(currentUserId);
        Team team = getTeamById(teamId);

        if(user.getTeam() == null)
        {
            return new NonColleagueTeamOverviewResponse(team,getUserWithSkillResponseList(team.getUsers()), getTeamSkillTemplateResponseList(team));
        }

        if(user.getTeam().getDepartment().getId().equals(team.getDepartment().getId()))
            return new ColleagueTeamOverviewResponse(team,getUserWithSkillResponseList(team.getUsers()), getTeamSkillTemplateResponseList(team));
        else
            return new NonColleagueTeamOverviewResponse(team,getUserWithSkillResponseList(team.getUsers()), getTeamSkillTemplateResponseList(team));
    }

    public List<UserWithSkillsResponse> getUserWithSkillResponseList(List<User> users)
    {
        return users.stream().map(user -> new UserWithSkillsResponse(user, userSkillService.getNormalUserSkillResponseList(user.getUserSkills()))).collect(Collectors.toList());
    }


    public TeamResponse getMyTeam(Long currentUserId) {
        User user = userService.getUserById(currentUserId);
        Team team = getTeamById(user.getTeam().getId());
        return new ColleagueTeamOverviewResponse(team,getUserWithSkillResponseList(team.getUsers()), getTeamSkillTemplateResponseList(team));
    }

    public SkillTemplate getTeamSkillTemplate(Team team)
    {
        return skillTemplateRepository.findOneByTeam(team);
    }

    public List<TeamSkillTemplateResponse> getTeamSkillTemplateResponseList(Team team){
        List<TeamSkillTemplateResponse> teamSkillTemplateResponseList = new ArrayList<>();

        if(getTeamSkillTemplate(team)== null)
        {
            return new ArrayList<>();
        }

        for (Skill skill: getTeamSkillTemplate(team).getSkills()
             ) {
            TeamSkillTemplateResponse teamSkillTemplateResponse =
                    new TeamSkillTemplateResponse(skill, getSkillCountInTeam(team, skill), getAverageSkillLevelInTeam(team, skill));
            teamSkillTemplateResponseList.add(teamSkillTemplateResponse);
        }
        return teamSkillTemplateResponseList;
    }

    public double getAverageSkillLevelInTeam(Team team, Skill skill)
    {
        List<User> users = (List<User>) userService.getAllByTeam(team);
        int counter = 0;
        double sum = 0;
        for (User user: users
                ) {
            for (UserSkill userSkill: user.getUserSkills()
                    ) {
                if(userSkill.getSkill().equals(skill)) {
                    counter++;
                    sum+=userSkillService.getCurrentSkillLevel(userSkill).getSkillLevel().getLevel();
                }
            }
        }
        if(counter == 0)
        {
            return 0;
        }
        return sum/counter;
    }

    public int getSkillCountInTeam(Team team, Skill skill)
    {
        List<User> users = (List<User>) userService.getAllByTeam(team);
        int counter = 0;
        for (User user: users
             ) {
            for (UserSkill userSkill: user.getUserSkills()
                 ) {
                if(userSkill.getSkill().equals(skill)) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public TeamResponse addTeam(AddTeamRequest addTeamRequest) {
        assert addTeamRequest != null;

        Team team = new Team(addTeamRequest.getName());
        team.setDepartment(departmentService.getDepartmentById(addTeamRequest.getDepartmentId()));
        team.setUsers(userService.getUsersByIds(addTeamRequest.getUserIds()));

        if (teamRepository.findByName(addTeamRequest.getName()) != null)
            throw new TeamNameAlreadyExistsException();

        return new TeamResponse(teamRepository.save(team), getUserWithSkillResponseList(team.getUsers()), getTeamSkillTemplateResponseList(team));
    }

}
