package ru.hh.performance_review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.control.MappingControl;
import ru.hh.performance_review.dto.RespondentDto;
import ru.hh.performance_review.dto.UserPollByIdResponseDto;
import ru.hh.performance_review.dto.response.UserResponseDto;
import ru.hh.performance_review.dto.response.UsersInfoResponseRawDto;
import ru.hh.performance_review.dto.response.compairofpoll.UserInfoDto;
import ru.hh.performance_review.model.User;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toUserResponseDto(User user);

    @Mapping(target = "leaderId", source = "user.leader.userId")
    UsersInfoResponseRawDto toUsersInfoResponseRawDto(User user);

    UserPollByIdResponseDto toUserPollByIdResponseDto(User user);

    UserInfoDto toUserInfoDto(User user);

    RespondentDto toRespondentDto(User user);

    default String map(UUID value){
        return value.toString();
    }
}
