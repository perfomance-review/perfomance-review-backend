package ru.hh.performance_review.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hh.performance_review.dto.RequestDto;
import ru.hh.performance_review.dto.ResponseDto;

@Mapper(componentModel = "spring")
public interface SimpleExampleMapper {

    @Mapping(target = "firstName", source = "firstNm")
    @Mapping(target = "secondName", source = "secondNm")
    ResponseDto toResponseDto(RequestDto requestDto);

}
