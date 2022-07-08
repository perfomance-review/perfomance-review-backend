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
import ru.hh.performance_review.dao.ComparePairDao;
import ru.hh.performance_review.dto.request.report.ReportRequestContextDto;
import ru.hh.performance_review.dto.response.UserResponseDto;
import ru.hh.performance_review.dto.response.report.ReportResponseContextDto;
import ru.hh.performance_review.exception.BusinessServiceException;
import ru.hh.performance_review.exception.InternalErrorCode;
import ru.hh.performance_review.service.PollService;
import ru.hh.performance_review.service.UserService;
import ru.hh.performance_review.service.report.ReportBuilder;
import ru.hh.performance_review.service.report.utils.FontUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportDocumentPollResultBuilder implements ReportBuilder {

    private final UserService userService;
    private final PollService pollService;
    private final ComparePairDao comparePairDao;
    private final static String REPORT_NAME = "Отчет \"Результаты опроса\"";
    private static final String[] POLLS_COLUMNS = {"Сотрудник", "Вопрос", "Полученный балл \n (оценка)"};
    private static final String MANAGER = "Руководитель";
    private static final String POLL_NAME = "Наименование опроса";
    private static final String POLL_DESCRIPTION = "Описание";
    private static final String POLL_CREATED = "Дата создания";
    private static final String POLL_DEADLINE = "Дата окончания (плановая)";
    private static final String POLL_STATUS = "Дата окончания (плановая)";

    @Override
    public ReportType getReportType() {
        return ReportType.POLL_RESULTS;
    }

    @Override
    public ReportResponseContextDto createReportResponseContextDto(ReportRequestContextDto reportRequestContextDto) {

        String managerId = reportRequestContextDto.getUserId();
        String pollId = reportRequestContextDto.getPollId();
        //   contentOfPollDao.getByPollId(UUID.fromString(pollId));
      //  List<ComparePair> comparePairs = comparePairDao.getRatingForAllByPollId(UUID.fromString(pollId));
        UserResponseDto userResponseDto = userService.getRespondentByUserId(managerId);
        //    PollByIdResponseDto poll = pollService.getPollById(pollId, managerId);
        //  PollsByUserIdResponseDto pollsByUserIdResponseDto = pollService.getAllPollsByManagerId(managerId);

        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet(REPORT_NAME);
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 6000);


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

        createHeaderRow(POLL_NAME, POLL_NAME, headerTableStyle, bodyStyle, pollNameRow);
        createHeaderRow(POLL_DESCRIPTION, POLL_DESCRIPTION, headerTableStyle, bodyStyle, pollDescriptionRow);
        createHeaderRow(POLL_CREATED, POLL_CREATED, headerTableStyle, bodyStyle, pollCreatedRow);
        createHeaderRow(POLL_DEADLINE, POLL_DEADLINE, headerTableStyle, bodyStyle, pollDeadlineRow);
        createHeaderRow(POLL_STATUS, POLL_STATUS, headerTableStyle, bodyStyle, pollStatusRow);

        for (int i = 0; i < POLLS_COLUMNS.length; i++) {
            Cell headerCell = headerTableRow.createCell(i);
            headerCell.setCellValue(POLLS_COLUMNS[i]);
            headerCell.setCellStyle(headerTableStyle);
        }

        //List<ReportCreatedPollsDto> reportCreatedPolls = getReportCreatedPolls(pollsByUserIdResponseDto);


      /*  for (int i = 0, c = 5; i < reportCreatedPolls.size(); i++, c++) {
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
            cell2.getRow().setHeightInPoints(cell2.getSheet().getDefaultRowHeightInPoints() * 3);

            Cell cell3 = row.createCell(3);
            cell3.setCellValue(FormatUtils.getLocalDateFormatIsoDate(reportCreatedPolls.get(i).getCreatedDt()));
            cell3.setCellStyle(bodyStyle);

            Cell cell4 = row.createCell(4);
            cell4.setCellValue(FormatUtils.getLocalDateFormatIsoDate(reportCreatedPolls.get(i).getDeadLineDt()));
            cell4.setCellStyle(bodyStyle);

            Cell cell5 = row.createCell(5);
            cell5.setCellValue(reportCreatedPolls.get(i).getStatus());
            cell5.setCellStyle(bodyStyle);
        }*/

        sheet.addMergedRegion(new CellRangeAddress(headerRow.getRowNum(), headerRow.getRowNum(), headerRow.getFirstCellNum(), 2));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 2));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 2));
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 2));
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 2));
        sheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 2));
        sheet.addMergedRegion(new CellRangeAddress(6, 6, 1, 2));


        for (int i = 0; i < POLLS_COLUMNS.length; i++) {
            sheet.autoSizeColumn(i);
        }

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

    private void createHeaderRow(String cellHeaderValue, String cellValue, CellStyle headerTableStyle, CellStyle bodyStyle, Row managerNameRow) {
        Cell cell0 = managerNameRow.createCell(0);
        cell0.setCellValue(cellHeaderValue);
        cell0.setCellStyle(headerTableStyle);
        Cell managerValueColumn = managerNameRow.createCell(1);
        managerValueColumn.setCellValue(cellValue);
        managerValueColumn.setCellStyle(bodyStyle);
        Cell cell2 = managerNameRow.createCell(2);
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
