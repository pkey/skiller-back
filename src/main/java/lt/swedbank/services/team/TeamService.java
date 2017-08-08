package lt.swedbank.services.team;

import lt.swedbank.beans.entity.*;
import lt.swedbank.beans.request.team.AddTeamRequest;
import lt.swedbank.beans.request.team.UpdateTeamRequest;
import lt.swedbank.beans.response.SkillEntityResponse;
import lt.swedbank.beans.response.SkillTemplateResponse;
import lt.swedbank.beans.response.team.TeamWithUsersResponse;
import lt.swedbank.beans.response.team.teamOverview.ColleagueTeamOverviewWithUsersResponse;
import lt.swedbank.beans.response.team.teamOverview.NonColleagueTeamOverviewWithUsersResponse;
import lt.swedbank.beans.response.user.UserWithSkillsResponse;
import lt.swedbank.events.team.TeamAddedEvent;
import lt.swedbank.events.team.TeamUpdatedEvent;
import lt.swedbank.exceptions.team.TeamNameAlreadyExistsException;
import lt.swedbank.exceptions.team.TeamNotFoundException;
import lt.swedbank.repositories.TeamRepository;
import lt.swedbank.services.department.DepartmentService;
import lt.swedbank.services.skill.SkillService;
import lt.swedbank.services.skill.SkillTemplateService;
import lt.swedbank.services.skill.UserSkillService;
import lt.swedbank.services.teamSkill.TeamSkillService;
import lt.swedbank.services.user.UserService;
import lt.swedbank.services.valueStream.ValueStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;
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
    @Autowired
    private TeamSkillService teamSkillService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    public Iterable<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public List<TeamWithUsersResponse> getAllTeamOverviewResponses() {

        List<TeamWithUsersResponse> teamWithUsersResponses = new ArrayList<>();

        for (Team team : teamRepository.findAll()) {
            teamWithUsersResponses.add(new ColleagueTeamOverviewWithUsersResponse(team,
                    getUserWithSkillResponseList(team.getUsers()),
                    getTeamSkillTemplateResponseList(team)));
        }

        return teamWithUsersResponses;
    }

    public Team getTeamById(Long id) {
        Team team = teamRepository.findOne(id);
        if (team == null) {
            throw new TeamNotFoundException();
        }
        return team;
    }


    public TeamWithUsersResponse getTeamOverview(Long teamId, Long currentUserId) {
        User user = userService.getUserById(currentUserId);
        Team currentTeam = getTeamById(teamId);
        List<User> userList = currentTeam.getUsers();

        userList.sort(Comparator.comparing(User::toString));

        if (user.getTeam().isPresent() && user.getTeam().get().getDepartment().equals(currentTeam.getDepartment())) {
            return new ColleagueTeamOverviewWithUsersResponse(currentTeam,
                    getUserWithSkillResponseList(currentTeam.getUsers()),
                    getTeamSkillTemplateResponseList(currentTeam));
        } else
            return new NonColleagueTeamOverviewWithUsersResponse(currentTeam,
                    getUserWithSkillResponseList(currentTeam.getUsers()),
                    getTeamSkillTemplateResponseList(currentTeam));

    }

    public List<UserWithSkillsResponse> getUserWithSkillResponseList(List<User> users) {
        if (users == null) {
            return new ArrayList<>();
        }
        return users.stream().map(user -> new UserWithSkillsResponse(user,
                userSkillService.getNormalUserSkillResponseList(user.getUserSkills()))).collect(Collectors.toList());
    }

    public TeamWithUsersResponse getMyTeam(Long currentUserId) {
        User user = userService.getUserById(currentUserId);
        Team team = user.getTeam().orElseThrow(TeamNotFoundException::new);
        List<User> userList = team.getUsers();
        userList.sort(Comparator.comparing(User::toString));

        return new ColleagueTeamOverviewWithUsersResponse(team, getUserWithSkillResponseList(userList), getTeamSkillTemplateResponseList(team));
    }

    public TeamWithUsersResponse addTeam(AddTeamRequest addTeamRequest) {
        assert addTeamRequest != null;

        if (teamRepository.findByName(addTeamRequest.getName()) != null) {
            throw new TeamNameAlreadyExistsException();
        }

        Team team = new Team(addTeamRequest.getName(), departmentService.getDepartmentById(addTeamRequest.getDepartmentId()));

        if (addTeamRequest.getUserIds() != null) {
            team.setUsers(userService.getUsersByIds(addTeamRequest.getUserIds()));
        }
        if (addTeamRequest.getStreamId() != null) {
            team.setValueStream(valueStreamService.getValueStreamById(addTeamRequest.getStreamId()));
        }

        if (addTeamRequest.getSkillIds() != null) {
            team.setSkillTemplate(skillTemplateService.createOrUpdateSkillTemplate(team, skillService.getSkillsByIds(addTeamRequest.getSkillIds())));
        }

        applicationEventPublisher.publishEvent(new TeamAddedEvent(team));

        return new TeamWithUsersResponse(teamRepository.save(team), getUserWithSkillResponseList(userService.getUsersByIds(addTeamRequest.getUserIds())), getTeamSkillTemplateResponseList(team));
    }

    public TeamWithUsersResponse updateTeam(Long id, UpdateTeamRequest updateTeamRequest) {
        assert id != null;
        assert updateTeamRequest != null;

        Team team = getTeamById(id);

        userService.updateUsersTeam(team, userService.getUsersByIds(updateTeamRequest.getUserIds()));

        assert updateTeamRequest.getUserIds() != null;
        team.setUsers(userService.getUsersByIds(updateTeamRequest.getUserIds()));

        assert updateTeamRequest.getDepartmentId() != null;
        team.setDepartment(departmentService.getDepartmentById(updateTeamRequest.getDepartmentId()));

        assert updateTeamRequest.getName() != null || updateTeamRequest.getName().isEmpty();
        team.setName(updateTeamRequest.getName());

        assert updateTeamRequest.getSkillIds() != null;
        team.setSkillTemplate(skillTemplateService.createOrUpdateSkillTemplate(team,
                skillService.getSkillsByIds(updateTeamRequest.getSkillIds())));

        if (updateTeamRequest.getStreamId() != null) {
            team.setValueStream(valueStreamService.getValueStreamById(updateTeamRequest.getStreamId()));
        }

        applicationEventPublisher.publishEvent(new TeamUpdatedEvent(team));

        return new TeamWithUsersResponse(teamRepository.save(team),
                getUserWithSkillResponseList(userService.getUsersByIds(updateTeamRequest.getUserIds())),
                getTeamSkillTemplateResponseList(team));
    }

    public Set<SkillTemplateResponse> getTeamSkillTemplateResponseList(Team team) {
        Optional<SkillTemplate> skillTemplateOptional = skillTemplateService.getSkillTemplateByTeamId(team.getId());

        TreeSet<SkillTemplateResponse> skillTemplateResponses = new TreeSet<>();
        if (skillTemplateOptional.isPresent()) {
            for (Skill skill : skillTemplateOptional.get().getSkills()) {
                TeamSkill teamSkill = teamSkillService.getCurrentTeamSkillByTeamAndSkill(team, skill);
                SkillTemplateResponse skillTemplateResponse = new SkillTemplateResponse(new SkillEntityResponse(skill),
                        teamSkill.getSkillCount(),
                        teamSkill.getSkillLevelAverage());

                skillTemplateResponses.add(skillTemplateResponse);
            }
        }
        return skillTemplateResponses;
    }
}
