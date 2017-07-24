package lt.swedbank.beans.response.team.teamOverview;


import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.response.team.TeamResponse;
import lt.swedbank.beans.response.user.NonColleagueResponse;
import lt.swedbank.beans.response.user.UserResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NonColleagueTeamOverviewResponse extends TeamResponse {

    public NonColleagueTeamOverviewResponse(Team team) {
        super(team);
        users = ((null == team.getUsers()) ? new ArrayList<>() :
                team.getUsers().stream().map(NonColleagueResponse::new).collect(Collectors.toList()));
    }

    private List<UserResponse> convertToUserSkillResponse(List<User> userList){

        if(userList == null){
            return new ArrayList<>();
        }

        List<UserResponse> userResponseList = new ArrayList<>();

        for (User user : userList
                ) {
            userResponseList.add(new NonColleagueResponse(user));
        }

        return userResponseList;
    }
}
