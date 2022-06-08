package ru.hh.performance_review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class UsersInfoResponseDto implements ResponseMessage {

    private List<UsersInfoResponseRawDto> usersInfo;

}
