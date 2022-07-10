package ru.hh.performance_review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hh.performance_review.dao.CompetenceDao;
import ru.hh.performance_review.dto.CompetenceDto;
import ru.hh.performance_review.dto.response.CompetencesResponseDto;
import ru.hh.performance_review.mapper.CompetenceMapper;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CompetenceServiceImpl implements CompetenceService{

    private final CompetenceDao competenceDao;
    private final CompetenceMapper competenceMapper;

    @Override
    public CompetencesResponseDto getCompetences() {

        List<CompetenceDto> competences= competenceDao.getAll().stream()
                .map(competenceMapper::toCompetenceDto)
                .collect(Collectors.toList());

        return new CompetencesResponseDto(competences);
    }

}
