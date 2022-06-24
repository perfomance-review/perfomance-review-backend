package ru.hh.performance_review.security.context;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class ContextPerformanceReviewDto {

    private String jwtToken;

    private Set<String> roles;

}
