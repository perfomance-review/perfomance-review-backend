package ru.hh.performance_review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.hh.performance_review.dto.RespondentDto;
import ru.hh.performance_review.dto.UserPollByIdResponseDto;
import ru.hh.performance_review.dto.response.UserResponseDto;
import ru.hh.performance_review.dto.response.UsersInfoResponseRawDto;
import ru.hh.performance_review.dto.response.compairofpoll.UserInfoDto;
import ru.hh.performance_review.model.RoleEnum;
import ru.hh.performance_review.model.User;
import ru.hh.performance_review.security.context.AuthUserInfo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toUserResponseDto(User user);

    @Mapping(target = "leaderId", source = "user.leader.userId")
    UsersInfoResponseRawDto toUsersInfoResponseRawDto(User user);

    UserPollByIdResponseDto toUserPollByIdResponseDto(User user);

    UserInfoDto toUserInfoDto(User user);

    RespondentDto toRespondentDto(User user);

    @Mapping(target = "id", source = "user.userId")
    @Mapping(target = "accessToken", ignore = true)
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.secondName")
    @Mapping(target = "middleName", source = "user.middleName")
    @Mapping(target = "roles", source = "user.role", qualifiedByName = "getRoles")
    AuthUserInfo toAuthUserInfo(User user);

    @Named("getRoles")
    default Set<String> getRoles(RoleEnum role) {
        return new HashSet<>(Collections.singletonList(role.name()));
    }

    default String map(UUID value) {
        return value.toString();
    }
}
