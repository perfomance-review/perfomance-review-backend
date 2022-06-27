package ru.hh.performance_review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.performance_review.dao.UserDao;
import ru.hh.performance_review.dto.RespondentDto;
import ru.hh.performance_review.dto.response.RespondentsResponseDto;
import ru.hh.performance_review.dto.response.UserResponseDto;
import ru.hh.performance_review.dto.response.UsersInfoResponseDto;
import ru.hh.performance_review.dto.response.UsersInfoResponseRawDto;
import ru.hh.performance_review.exception.BusinessServiceException;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.mapper.UserMapper;
import ru.hh.performance_review.model.RoleEnum;
import ru.hh.performance_review.model.User;
import ru.hh.performance_review.security.context.AuthUserInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserDao userDao;
  private final UserMapper userMapper;

  @Transactional(readOnly = true)
  @Override
  public String getAnyRespondentId() {
    return userDao.getAll().stream()
        .filter(o -> o.getRole().equals(RoleEnum.RESPONDENT))
        .map(o -> o.getUserId().toString())
        .findFirst()
        .orElse("");
  }


  @Override
  public UserResponseDto getRespondentByUserId(String userId) {
    //на преобразование к UUID поле userId проверено в валидационном классе
    User user = userDao.getByID(User.class,UUID.fromString(userId));
    if (user == null) {
       throw new BusinessServiceException(InternalErrorCode.UNKNOWN_USER, String.format("userId:%s", userId));
    }

    return userMapper.toUserResponseDto(user);
  }

  @Override
  public UsersInfoResponseDto getAllUsers() {
    List<UsersInfoResponseRawDto> responseDtoList = userDao.getAll().stream()
            .map(userMapper::toUsersInfoResponseRawDto)
            .collect(Collectors.toList())           ;
    return new UsersInfoResponseDto(responseDtoList);
  }


  @Override
  public AuthUserInfo getAuthUserByUserNameAndUserPassword(String userEmail, String userPassword) {
      Optional<User> userOptional = userDao.findByUserEmailAndUserPassword(userEmail, userPassword);
      return userMapper.toAuthUserInfo(userOptional.orElse(null));
  }

  @Override
  public User findByUserEmail(String userEmail) {
    return userDao.findByUserEmail(userEmail).orElse(null);
  }
  @Transactional(readOnly = true)
  @Override
  public RespondentsResponseDto getAllRespondentsForManager(String userId) {
    List<RespondentDto> respondents = userDao.getRespondentsByManagerId(UUID.fromString(userId)).stream()
            .map(userMapper::toRespondentDto)
            .collect(Collectors.toList());
    return new RespondentsResponseDto(respondents);
  }
}
