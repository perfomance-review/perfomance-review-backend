package ru.hh.performance_review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.dao.UserDao;
import ru.hh.performance_review.model.RoleEnum;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
  private final UserDao userDao;
  @Override
  public String getAnyRespondentId() {
    return userDao.getAll().stream()
        .filter(o -> o.getRole().equals(RoleEnum.RESPONDENT))
        .map(o -> o.getUserId().toString())
        .findFirst()
        .orElse("");
  }
}
