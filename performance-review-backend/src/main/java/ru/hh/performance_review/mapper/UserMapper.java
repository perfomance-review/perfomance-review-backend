package ru.hh.performance_review.mapper;

import org.mapstruct.Mapper;
import ru.hh.performance_review.dto.UserPollByIdResponseDto;
import ru.hh.performance_review.dto.response.UserResponseDto;
import ru.hh.performance_review.dto.response.compairofpoll.UserInfoDto;
import ru.hh.performance_review.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toUserResponseDto(User user);

    UserPollByIdResponseDto toUserPollByIdResponseDto(User user);

    UserInfoDto toUserInfoDto(User user);
}
