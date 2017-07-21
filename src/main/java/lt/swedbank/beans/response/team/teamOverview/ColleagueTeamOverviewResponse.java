package lt.swedbank.beans.response.team.teamOverview;


import lt.swedbank.beans.entity.Team;
import lt.swedbank.beans.entity.User;
import lt.swedbank.beans.response.user.UserEntityResponse;
import lt.swedbank.beans.response.user.UserResponse;

import java.util.ArrayList;
import java.util.List;

public class ColleagueTeamOverviewResponse extends TeamOverviewResponse {

    public ColleagueTeamOverviewResponse(Team team) {
        super(team);
        users = convertToUserSkillResponse(team.getUsers());
    }

    private List<UserResponse> convertToUserSkillResponse(List<User> userList){

        if(userList == null){
            return new ArrayList<>();
        }

        List<UserResponse> userResponseList = new ArrayList<>();

        for (User user : userList
                ) {
            userResponseList.add(new UserEntityResponse(user));
        }

        return userResponseList;
    }
}
