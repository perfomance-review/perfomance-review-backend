package ru.hh.performance_review.service.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.consts.ReportType;
import ru.hh.performance_review.dto.request.report.ReportRequestContextDto;
import ru.hh.performance_review.dto.response.report.ReportResponseContextDto;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportDocumentServiceImpl implements ReportDocumentService {

    private final List<ReportBuilder> documentBuilders;
    private Map<ReportType, ReportBuilder> builderMap;

    @PostConstruct
    protected void initBuilderMap(){
        builderMap = documentBuilders.stream()
                .collect(Collectors.toMap(ReportBuilder::getReportType, Function.identity()));
    }

    @Override
    public ReportResponseContextDto createReportContext(ReportRequestContextDto reportRequestContextDto) {
        return builderMap.get(reportRequestContextDto.getReportType()).createReportResponseContextDto(reportRequestContextDto);
    }
}
