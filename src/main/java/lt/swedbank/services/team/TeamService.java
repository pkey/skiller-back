package lt.swedbank.services.team;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.team.AddTeamRequest;
import lt.swedbank.beans.request.team.UpdateTeamRequest;
import lt.swedbank.beans.response.TeamSkillTemplateResponse;
import lt.swedbank.beans.response.team.TeamWithUsersResponse;
import lt.swedbank.beans.response.team.teamOverview.ColleagueTeamOverviewWithUsersResponse;
import lt.swedbank.beans.response.team.teamOverview.NonColleagueTeamOverviewWithUsersResponse;
import lt.swedbank.beans.response.user.UserWithSkillsResponse;
import lt.swedbank.exceptions.team.TeamNameAlreadyExistsException;
import lt.swedbank.exceptions.team.TeamNotFoundException;
import lt.swedbank.repositories.TeamRepository;
import lt.swedbank.services.department.DepartmentService;
import lt.swedbank.services.skill.SkillService;
import lt.swedbank.services.skill.SkillTemplateService;
import lt.swedbank.services.skill.UserSkillService;
import lt.swedbank.services.user.UserService;
import lt.swedbank.services.valueStream.ValueStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ValueStreamService valueStreamService;
    @Autowired
    private UserSkillService userSkillService;
    @Autowired
    private SkillService skillService;
    @Autowired
    private SkillTemplateService skillTemplateService;


    public Iterable<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public List<TeamWithUsersResponse> getAllTeamOverviewResponses() {

        List<TeamWithUsersResponse> teamWithUsersRespons = new ArrayList<>();

        for (Team team : teamRepository.findAll()) {
            teamWithUsersRespons.add(new ColleagueTeamOverviewWithUsersResponse(team,getUserWithSkillResponseList(team.getUsers()), getTeamSkillTemplateResponseList(team)));
        }

        return teamWithUsersRespons;
    }

    public Team getTeamById(Long id) {
        Team team = teamRepository.findOne(id);
        if(team == null) {
            throw new TeamNotFoundException();
        }
        return team;
    }


    public TeamWithUsersResponse getTeamOverview(Long teamId, Long currentUserId) {
        User user = userService.getUserById(currentUserId);
        Team team = getTeamById(teamId);
        List<User> userList = team.getUsers();

        userList.sort(Comparator.comparing(User::toString));

        if(user.getTeam() == null)
        {
            return new NonColleagueTeamOverviewWithUsersResponse(team,getUserWithSkillResponseList(team.getUsers()), getTeamSkillTemplateResponseList(team));
        }

        if(user.getTeam().getDepartment().getId().equals(team.getDepartment().getId()))
            return new ColleagueTeamOverviewWithUsersResponse(team,getUserWithSkillResponseList(team.getUsers()), getTeamSkillTemplateResponseList(team));
        else
            return new NonColleagueTeamOverviewWithUsersResponse(team,getUserWithSkillResponseList(team.getUsers()), getTeamSkillTemplateResponseList(team));
    }

    public List<UserWithSkillsResponse> getUserWithSkillResponseList(List<User> users)
    {
       if(users == null) {
            return new ArrayList<>();
        }
        return users.stream().map(user -> new UserWithSkillsResponse(user, userSkillService.getNormalUserSkillResponseList(user.getUserSkills()))).collect(Collectors.toList());
    }

    public TeamWithUsersResponse getMyTeam(Long currentUserId) {
        User user = userService.getUserById(currentUserId);
        Team team = getTeamById(user.getTeam().getId());
        List<User> userList = team.getUsers();
        userList.sort(Comparator.comparing(User::toString));
        team.setUsers(userList);
        return new ColleagueTeamOverviewWithUsersResponse(team,getUserWithSkillResponseList(team.getUsers()), getTeamSkillTemplateResponseList(team));
    }

    public TeamWithUsersResponse addTeam(AddTeamRequest addTeamRequest) {
        assert addTeamRequest != null;

        if (teamRepository.findByName(addTeamRequest.getName()) != null) {
            throw new TeamNameAlreadyExistsException();
        }

        Team team = new Team(addTeamRequest.getName());
        team.setDepartment(departmentService.getDepartmentById(addTeamRequest.getDepartmentId()));

        if (addTeamRequest.getUserIds() != null) {
            team.setUsers(userService.getUsersByIds(addTeamRequest.getUserIds()));
        }
        if (addTeamRequest.getStreamId() != null) {
            team.setValueStream(valueStreamService.getValueStreamById(addTeamRequest.getStreamId()));
        }

        teamRepository.save(team);

        if (addTeamRequest.getSkillIds() != null) {
            team.setSkillTemplate(skillTemplateService.createSkillTemplate(team, skillService.getSkillsByIds(addTeamRequest.getSkillIds())));
        }

        return new TeamWithUsersResponse(team, getUserWithSkillResponseList(userService.getUsersByIds(addTeamRequest.getUserIds())), getTeamSkillTemplateResponseList(team));
    }


    public TeamWithUsersResponse updateTeam(Long id, UpdateTeamRequest updateTeamRequest) {
        assert id != null;
        assert updateTeamRequest != null;

        Team team = getTeamById(id);

        assert updateTeamRequest.getUserIds() != null;
        team.setUsers(userService.getUsersByIds(updateTeamRequest.getUserIds()));

        assert updateTeamRequest.getDepartmentId() != null;
        team.setDepartment(departmentService.getDepartmentById(updateTeamRequest.getDepartmentId()));

        assert updateTeamRequest.getName() != null || updateTeamRequest.getName().isEmpty();
        team.setName(updateTeamRequest.getName());

        assert updateTeamRequest.getSkillIds() != null;
        team.setSkillTemplate(skillTemplateService.updateSkillTemplate(team.getSkillTemplate().getId(),
                skillService.getSkillsByIds(updateTeamRequest.getSkillIds())));

        if(updateTeamRequest.getStreamId() != null){
            team.setValueStream(valueStreamService.getValueStreamById(updateTeamRequest.getStreamId()));
        }


        return new TeamWithUsersResponse(teamRepository.save(team),
                getUserWithSkillResponseList(userService.getUsersByIds(updateTeamRequest.getUserIds())),
                getTeamSkillTemplateResponseList(team));
    }

    public SkillTemplate getTeamSkillTemplate(Team team)
    {
        return skillTemplateService.getByTeamId(team.getId());
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
}
