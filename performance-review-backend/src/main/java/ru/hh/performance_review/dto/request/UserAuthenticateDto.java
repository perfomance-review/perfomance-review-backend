package ru.hh.performance_review.dto.request;

import lombok.Data;

@Data
public class UserAuthenticateDto {

    private String email;
    private String password;

    @Override
    public String toString() {
        return "{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
