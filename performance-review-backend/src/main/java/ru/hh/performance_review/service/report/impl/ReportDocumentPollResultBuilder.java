package ru.hh.performance_review.service.report.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.consts.ReportType;
import ru.hh.performance_review.dto.request.report.ReportRequestContextDto;
import ru.hh.performance_review.dto.response.UserResponseDto;
import ru.hh.performance_review.dto.response.report.PollDto;
import ru.hh.performance_review.dto.response.report.QuestionUsersInfoDto;
import ru.hh.performance_review.dto.response.report.ReportDocumentPollResultDto;
import ru.hh.performance_review.dto.response.report.ReportResponseContextDto;
import ru.hh.performance_review.exception.BusinessServiceException;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.service.GradeService;
import ru.hh.performance_review.service.UserService;
import ru.hh.performance_review.service.report.ReportBuilder;
import ru.hh.performance_review.service.report.utils.FontUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportDocumentPollResultBuilder implements ReportBuilder {

    private final UserService userService;
    private final GradeService gradeService;
    private final static String REPORT_NAME = "Отчет \"Результаты опроса\"";
    private static final String[] POLLS_COLUMNS = {"Сотрудник", "Вопрос", "Полученный балл \n (оценка)"};
    private static final String MANAGER = "Руководитель";
    private static final String POLL_NAME = "Наименование опроса";
    private static final String POLL_DESCRIPTION = "Описание";
    private static final String POLL_CREATED = "Дата создания";
    private static final String POLL_DEADLINE = "Дата окончания (плановая)";
    private static final String POLL_STATUS = "Статус";

    @Override
    public ReportType getReportType() {
        return ReportType.POLL_RESULTS;
    }

    @Override
    public ReportResponseContextDto createReportResponseContextDto(ReportRequestContextDto reportRequestContextDto) {

        String managerId = reportRequestContextDto.getUserId();
        String pollId = reportRequestContextDto.getPollId();

        UserResponseDto userResponseDto = userService.getRespondentByUserId(managerId);
        ReportDocumentPollResultDto resultDto = gradeService.createReportDocumentPollResult(managerId, pollId);
        PollDto pollInfo = resultDto.getPollInfo();

        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet(REPORT_NAME);
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 12000);
        sheet.setColumnWidth(2, 5000);


        CellStyle headerNameStyle = FontUtils.buildHeaderNameStyle(workbook, 14);
        CellStyle headerTableStyle = FontUtils.buildHeaderTableStyleLeftAlign(workbook);
        CellStyle bodyStyle = FontUtils.buildBodyStyle(workbook, 11);

        Row headerRow = sheet.createRow(0);
        Row managerNameRow = sheet.createRow(1);
        Row pollNameRow = sheet.createRow(2);
        Row pollDescriptionRow = sheet.createRow(3);
        pollDescriptionRow.setHeightInPoints(sheet.getDefaultRowHeightInPoints() * 2);
        Row pollCreatedRow = sheet.createRow(4);
        Row pollDeadlineRow = sheet.createRow(5);
        Row pollStatusRow = sheet.createRow(6);
        createEmptyRow(sheet, 7);
        Row headerTableRow = sheet.createRow(8);
        headerTableRow.setHeight((short) 250);
        headerTableRow.setHeightInPoints(sheet.getDefaultRowHeightInPoints() * 2);

        Cell cellHeaderReport0 = headerRow.createCell(0);
        cellHeaderReport0.setCellValue(REPORT_NAME);
        cellHeaderReport0.setCellStyle(headerNameStyle);

        String fullName = getFullName(userResponseDto.getFirstName(), userResponseDto.getSecondName(), userResponseDto.getMiddleName());
        createHeaderRow(MANAGER, fullName, headerTableStyle, bodyStyle, managerNameRow);

        createHeaderRow(POLL_NAME, pollInfo.getName(), headerTableStyle, bodyStyle, pollNameRow);
        createHeaderRow(POLL_DESCRIPTION, pollInfo.getDescription(), headerTableStyle, bodyStyle, pollDescriptionRow);
        createHeaderRow(POLL_CREATED, pollInfo.getRecCreateDttm().format(DateTimeFormatter.ISO_DATE), headerTableStyle, bodyStyle, pollCreatedRow);
        createHeaderRow(POLL_DEADLINE, pollInfo.getDeadline().toString(), headerTableStyle, bodyStyle, pollDeadlineRow);
        createHeaderRow(POLL_STATUS, pollInfo.getStatus(), headerTableStyle, bodyStyle, pollStatusRow);

        for (int i = 0; i < POLLS_COLUMNS.length; i++) {
            Cell headerCell = headerTableRow.createCell(i);
            headerCell.setCellValue(POLLS_COLUMNS[i]);
            headerCell.setCellStyle(headerTableStyle);
        }

        List<QuestionUsersInfoDto> questionInfos = resultDto.getQuestionInfos();

        for (int i = 0, c = 9; i < questionInfos.size(); i++, c++) {
            Row row = sheet.createRow(c);

            QuestionUsersInfoDto usersInfoDto = questionInfos.get(i);
            Cell cell0 = row.createCell(0);
            cell0.setCellStyle(bodyStyle);
            cell0.setCellValue(usersInfoDto.getUserFullName());

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(usersInfoDto.getTextQuestion());
            cell1.getRow().setHeightInPoints(cell1.getSheet().getDefaultRowHeightInPoints() * 2);
            cell1.setCellStyle(bodyStyle);

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(usersInfoDto.getScore() == null ? "-" : usersInfoDto.getScore().toString());
            cell2.setCellStyle(bodyStyle);
        }

        sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(), headerRow.getRowNum(), headerRow.getFirstCellNum(), 2));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 2));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 2));
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 2));
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 2));
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 2));
        sheet.addMergedRegion(new CellRangeAddress(6, 6, 1, 2));

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            workbook.write(os);
            return ReportResponseContextDto.builder()
                    .reportBytes(os.toByteArray())
                    .reportName(getReportType().getReportName())
                    .build();

        } catch (IOException e) {
            log.error("", e);
            throw new BusinessServiceException(
                    String.format(InternalErrorCode.REPORT_GENERATE_ERROR.getErrorDescription(), "XLSX"),
                    InternalErrorCode.REPORT_GENERATE_ERROR);
        }

    }

    private void createHeaderRow(String cellHeaderValue, String cellValue, CellStyle headerTableStyle, CellStyle bodyStyle, Row row) {
        Cell cell0 = row.createCell(0);
        cell0.setCellValue(cellHeaderValue);
        cell0.setCellStyle(headerTableStyle);
        Cell cell1 = row.createCell(1);
        cell1.setCellValue(cellValue);
        cell1.setCellStyle(bodyStyle);
        Cell cell2 = row.createCell(2);
        cell2.setCellStyle(bodyStyle);
    }

    private void createEmptyRow(Sheet sheet, int rowId) {
        Row row = sheet.createRow(rowId);
        Cell blankCell = row.createCell(0);
        blankCell.setBlank();
    }


    private String getFullName(String fName, String sName, String mName) {
        String fN = StringUtils.trimToEmpty(fName);
        String sN = StringUtils.trimToEmpty(sName);
        String mN = StringUtils.trimToEmpty(mName);
        return String.format("%s %s %s", fN, sN, mN).trim();
    }


}
