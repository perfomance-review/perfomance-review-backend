package ru.hh.performance_review.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ResponseDto {

    private String firstName;
    private String secondName;
}
