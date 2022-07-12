package ru.hh.performance_review.dto.response.report;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Предназначена для вывода информации по отчету "Результаты опроса"
 */
@Data
@Accessors(chain = true)
public class ReportDocumentPollResultDto {

    private PollDto pollInfo;
    private List<QuestionUsersInfoDto> questionInfos;
}
