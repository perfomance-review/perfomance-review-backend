package ru.hh.performance_review.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.hh.performance_review.dto.response.compairofpoll.ComparePairsOfPollInfoDto;
import ru.hh.performance_review.model.ComparePair;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class})
public interface ComparePairOfPollMapper {

    @Mapping(target = "person1", source = "pair.person1")
    @Mapping(target = "person2", source = "pair.person2")
    ComparePairsOfPollInfoDto toComparePairOfPollDto(ComparePair pair);

    List<ComparePairsOfPollInfoDto> toComparePairsOfPollInfoDtos(List<ComparePair> comparePairList);
}
