package ru.hh.performance_review.service.report.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.hh.performance_review.consts.ReportType;
import ru.hh.performance_review.dto.request.report.ReportRequestContextDto;
import ru.hh.performance_review.dto.response.PollsByUserIdResponseDto;
import ru.hh.performance_review.dto.response.UserResponseDto;
import ru.hh.performance_review.dto.response.report.ReportCreatedPollsDto;
import ru.hh.performance_review.dto.response.report.ReportResponseContextDto;
import ru.hh.performance_review.exception.BusinessServiceException;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.service.PollService;
import ru.hh.performance_review.service.UserService;
import ru.hh.performance_review.service.report.ReportBuilder;
import ru.hh.performance_review.service.report.utils.FontUtils;
import ru.hh.performance_review.service.report.utils.FormatUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportDocumentCreatedPollBuilder implements ReportBuilder {

    private final UserService userService;
    private final PollService pollService;
    private final static String REPORT_NAME = "Отчет \"Созданные опросы\"";
    private static final String[] POLLS_COLUMNS = {"№", "Наименование опроса", "Описание", "Дата создания", "Дата окончания \n (плановая)", "Статус"};
    private static final String MANAGER = "Руководитель";

    @Override
    public ReportType getReportType() {
        return ReportType.CREATED_POLLS;
    }

    @Override
    public ReportResponseContextDto createReportResponseContextDto(ReportRequestContextDto reportRequestContextDto) {
        String managerId  = reportRequestContextDto.getUserId();
        UserResponseDto userResponseDto = userService.getRespondentByUserId(managerId);
        PollsByUserIdResponseDto pollsByUserIdResponseDto = pollService.getAllPollsByManagerId(managerId);

        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet(REPORT_NAME);
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 6000);


        CellStyle headerNameStyle = FontUtils.buildHeaderNameStyle(workbook, 14);
        CellStyle headerManagerStyle = FontUtils.buildHeaderManagerStyle(workbook);
        CellStyle headerTableStyle = FontUtils.buildHeaderTableStyleCenterAlign(workbook);
        CellStyle bodyStyle = FontUtils.buildBodyStyle(workbook, 11);

        Row headerRow = sheet.createRow(0);
        createEmptyRow(sheet, 1);
        Row managerNameRow = sheet.createRow(2);
        createEmptyRow(sheet, 3);
        Row headerTableRow = sheet.createRow(4);
        headerTableRow.setHeight((short) 250);
        headerTableRow.setHeightInPoints(sheet.getDefaultRowHeightInPoints() * 2);

        Cell cellHeaderReport0 = headerRow.createCell(0);
        cellHeaderReport0.setCellValue(REPORT_NAME);
        cellHeaderReport0.setCellStyle(headerNameStyle);

        Cell managerNameCell0 = managerNameRow.createCell(0);
        Cell managerNameCell1 = managerNameRow.createCell(1);
        managerNameCell0.setCellValue(MANAGER);
        managerNameCell0.setCellStyle(headerManagerStyle);
        managerNameCell1.setCellStyle(headerManagerStyle);
        for (int i = 2; i < POLLS_COLUMNS.length; i++) {
            Cell managerCell = managerNameRow.createCell(i);
            managerCell.setCellStyle(bodyStyle);
        }
        Cell managerValueColumn = managerNameRow.createCell(2);
        managerValueColumn.setCellValue(getFullName(userResponseDto.getFirstName(), userResponseDto.getSecondName(), userResponseDto.getMiddleName()));
        managerValueColumn.setCellStyle(FontUtils.buildBodyStyle(workbook, 12));

        for (int i = 0; i < POLLS_COLUMNS.length; i++) {
            Cell headerCell = headerTableRow.createCell(i);
            headerCell.setCellValue(POLLS_COLUMNS[i]);
            headerCell.setCellStyle(headerTableStyle);
        }

        List<ReportCreatedPollsDto> reportCreatedPolls = getReportCreatedPolls(pollsByUserIdResponseDto);


        for (int i = 0, c = 5; i < reportCreatedPolls.size(); i++, c++) {
            Row row = sheet.createRow(c);

            Cell cell0 = row.createCell(0);
            cell0.setCellValue(i + 1);
            cell0.setCellStyle(bodyStyle);
            cell0.setCellType(CellType.NUMERIC);

            Cell cell1 = row.createCell(1);
            cell1.setCellValue(reportCreatedPolls.get(i).getName());
            cell1.setCellStyle(bodyStyle);

            Cell cell2 = row.createCell(2);
            cell2.setCellValue(reportCreatedPolls.get(i).getDescription());
            cell2.setCellStyle(bodyStyle);
            cell2.getRow().setHeightInPoints(cell2.getSheet().getDefaultRowHeightInPoints() * 2);

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(FormatUtils.getLocalDateFormatIsoDate(reportCreatedPolls.get(i).getCreatedDt()));
            cell3.setCellStyle(bodyStyle);

            Cell cell4 = row.createCell(4);
            cell4.setCellValue(FormatUtils.getLocalDateFormatIsoDate(reportCreatedPolls.get(i).getDeadLineDt()));
            cell4.setCellStyle(bodyStyle);

            Cell cell5 = row.createCell(5);
            cell5.setCellValue(reportCreatedPolls.get(i).getStatus());
            cell5.setCellStyle(bodyStyle);
        }

        sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(), headerRow.getRowNum(), headerRow.getFirstCellNum(), 5));
        sheet.addMergedRegion(new CellRangeAddress(managerNameRow.getRowNum(), managerNameRow.getRowNum(), headerRow.getFirstCellNum(), 1));
        sheet.addMergedRegion(new CellRangeAddress(managerNameRow.getRowNum(), managerNameRow.getRowNum(), 2, 5));

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

    private void createEmptyRow(Sheet sheet, int rowId) {
        Row row = sheet.createRow(rowId);
        Cell blankCell = row.createCell(0);
        blankCell.setBlank();
    }

    private List<ReportCreatedPollsDto> getReportCreatedPolls(PollsByUserIdResponseDto pollsByUserIdResponseDto) {
        return pollsByUserIdResponseDto.getPolls().stream()
                .map(poll -> new ReportCreatedPollsDto()
                        .setName(poll.getTitle())
                        .setDescription(poll.getDescription())
                        .setCreatedDt(poll.getCreatedDt())
                        .setDeadLineDt(poll.getDeadline())
                        .setStatus(poll.getStatus().getHumanValue())
                )
                .collect(Collectors.toList());
    }

    private String getFullName(String fName, String sName, String mName) {
        String fN = StringUtils.trimToEmpty(fName);
        String sN = StringUtils.trimToEmpty(sName);
        String mN = StringUtils.trimToEmpty(mName);
        return String.format("%s %s %s", fN, sN, mN).trim();
    }


}
