package com.huabo.bigmodel.service.impl;

import com.huabo.bigmodel.service.FileParseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 文件解析服务实现：按扩展名分流，抽取文档纯文本。
 */
@Slf4j
@Service
public class FileParseServiceImpl implements FileParseService {

    @Override
    public String parseToText(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件为空");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new IllegalArgumentException("文件名为空");
        }
        String ext = getExtension(fileName);
        log.info("[FileParse] 开始解析文件: {}, 扩展名: {}", fileName, ext);

        try (InputStream in = file.getInputStream()) {
            switch (ext) {
                case "md":
                case "txt":
                    return new String(file.getBytes(), StandardCharsets.UTF_8);
                case "doc":
                    return parseDoc(in);
                case "docx":
                    return parseDocx(in);
                case "xls":
                    return parseExcel(new HSSFWorkbook(in));
                case "xlsx":
                    return parseExcel(new XSSFWorkbook(in));
                case "pdf":
                    return parsePdf(in);
                default:
                    throw new IllegalArgumentException("不支持的文件格式: " + ext
                            + "（支持 md/txt/doc/docx/xls/xlsx/pdf）");
            }
        }
    }

    private String getExtension(String fileName) {
        int idx = fileName.lastIndexOf('.');
        return idx >= 0 ? fileName.substring(idx + 1).toLowerCase() : "";
    }

    /** 解析 .doc（POI HWPF） */
    private String parseDoc(InputStream in) throws Exception {
        try (WordExtractor extractor = new WordExtractor(in)) {
            return extractor.getText();
        }
    }

    /** 解析 .docx（POI XWPF） */
    private String parseDocx(InputStream in) throws Exception {
        try (XWPFDocument doc = new XWPFDocument(in);
             XWPFWordExtractor extractor = new XWPFWordExtractor(doc)) {
            return extractor.getText();
        }
    }

    /** 解析 Excel（xls/xlsx 通用），按「单元格\t单元格」逐行输出 */
    private String parseExcel(Workbook workbook) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (Workbook wb = workbook) {
            for (int s = 0; s < wb.getNumberOfSheets(); s++) {
                Sheet sheet = wb.getSheetAt(s);
                sb.append("【工作表：").append(sheet.getSheetName()).append("】\n");
                for (Row row : sheet) {
                    StringBuilder line = new StringBuilder();
                    for (Cell cell : row) {
                        if (line.length() > 0) {
                            line.append("\t");
                        }
                        line.append(getCellValue(cell));
                    }
                    if (line.length() > 0) {
                        sb.append(line).append("\n");
                    }
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    /** 解析 .pdf（PDFBox） */
    private String parsePdf(InputStream in) throws Exception {
        try (PDDocument document = PDDocument.load(in)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
}
