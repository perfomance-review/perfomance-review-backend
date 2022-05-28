package ru.hh.performance_review.mapper;

import org.mapstruct.Mapper;
import ru.hh.performance_review.dto.WinnerRawDto;
import ru.hh.performance_review.dto.request.UpdateWinnerRequestDto;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface GetWinnerMapper {

    //@Mapping(target = "isCompleted", source = "isCompleted", expression = "java( java.lang.Boolean.valueOf(requestDto.getIsCompleted()) )")
    WinnerRawDto toGetRatingRawDto(UpdateWinnerRequestDto requestDto);

    default UUID mapToUuid(String value) {
        return UUID.fromString(value);
    }
}
