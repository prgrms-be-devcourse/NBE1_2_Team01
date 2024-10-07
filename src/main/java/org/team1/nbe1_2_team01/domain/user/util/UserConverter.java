package org.team1.nbe1_2_team01.domain.user.util;

import org.team1.nbe1_2_team01.domain.user.entity.Role;
import org.team1.nbe1_2_team01.domain.user.entity.User;
import org.team1.nbe1_2_team01.domain.user.service.response.UserBriefResponse;
import org.team1.nbe1_2_team01.domain.user.service.response.UserBriefWithRoleResponse;
import org.team1.nbe1_2_team01.domain.user.service.response.UserDetailsResponse;
import org.team1.nbe1_2_team01.domain.user.service.response.UserIdResponse;

public class UserConverter {
    public static UserIdResponse toUserIdResponse(User user){
        return new UserIdResponse(user.getId());
    }

    public static UserDetailsResponse toUserDetailsResponse(User user){
        String courseName = user.getRole().equals(Role.ADMIN) ? "관리자" : user.getCourse().getName();
        return new UserDetailsResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getName(),
                courseName);
    }

    public static UserBriefResponse toUserBriefResponse(User user) {
        return new UserBriefResponse(user.getId(), user.getUsername(), user.getName());
    }

    public static UserBriefWithRoleResponse toUserBriefWithRoleResponse(User user) {
        return new UserBriefWithRoleResponse(user.getId(), user.getUsername(), user.getName(), user.getRole());
    }
}
