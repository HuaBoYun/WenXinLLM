package com.huabo.audit.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class ExcelTemplateExportUtil {

    /**
     * 基于模板导出Excel
     * @param templatePath 模板文件路径（类路径下）
     * @param dataList 数据列表
     * @param outputStream 输出流
     * @param <T> 数据类型
     * @throws IOException
     */
    public static <T> void exportByTemplate(String templatePath, List<T> dataList, OutputStream outputStream) throws IOException {
        try (InputStream templateStream = ExcelTemplateExportUtil.class.getClassLoader().getResourceAsStream(templatePath)) {
            if (templateStream == null) {
                throw new IllegalArgumentException("模板文件不存在：" + templatePath);
            }

            Workbook workbook = new XSSFWorkbook(templateStream);
            Sheet sheet = workbook.getSheetAt(0);

            Map<Integer, List<String>> placeholderRows = parsePlaceholderRows(sheet);
            fillData(sheet, placeholderRows, dataList);

            workbook.write(outputStream);
        }
    }

    /**
     * 解析模板中的占位符行
     */
    private static Map<Integer, List<String>> parsePlaceholderRows(Sheet sheet) {
        Map<Integer, List<String>> placeholderRows = new HashMap<>();
        int lastRowNum = sheet.getLastRowNum();

        for (int i = 0; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            List<String> placeholders = new ArrayList<>();
            boolean hasPlaceholder = false;

            int lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                Cell cell = row.getCell(j);
                if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    String cellValue = cell.getStringCellValue();
                    if (cellValue != null && cellValue.contains("#{")) {
                        placeholders.add(cellValue);
                        hasPlaceholder = true;
                    } else {
                        placeholders.add(null);
                    }
                } else {
                    placeholders.add(null);
                }
            }

            if (hasPlaceholder) {
                placeholderRows.put(i, placeholders);
            }
        }

        return placeholderRows;
    }

    /**
     * 填充数据到模板（保留模板行，直接从模板行开始赋值）
     */
    private static <T> void fillData(Sheet sheet, Map<Integer, List<String>> placeholderRows, List<T> dataList) {
        if (placeholderRows.isEmpty() || dataList.isEmpty()) {
            return;
        }

        Integer templateRowIndex = placeholderRows.keySet().iterator().next();
        List<String> placeholders = placeholderRows.get(templateRowIndex);

        // 遍历数据列表
        for (int i = 0; i < dataList.size(); i++) {
            T data = dataList.get(i);
            int currentRowIndex = templateRowIndex + i; // 从模板行开始赋值

            Row currentRow;
            if (i == 0) {
                // 第一行使用模板行（清空占位符但保留样式）
                currentRow = sheet.getRow(currentRowIndex);
                clearTemplateRowPlaceholders(currentRow, placeholders);
            } else {
                // 后续行复制模板行样式
                currentRow = sheet.createRow(currentRowIndex);
                copyRow(sheet, sheet.getRow(templateRowIndex), currentRow);
            }

            // 填充单元格数据
            fillRowData(i+1,currentRow, placeholders, data);
        }
    }

    /**
     * 填充行数据
     */
    private static <T> void fillRowData(Integer sequence,Row row, List<String> placeholders, T data) {
        for (int j = 0; j < placeholders.size(); j++) {
            String placeholder = placeholders.get(j);
            if (placeholder == null) continue;

            String fieldName = placeholder.substring(2, placeholder.length() - 1).trim();

            //处理序号
            if ("sequence".equals(fieldName)) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    cell = row.createCell(j);
                }
                setCellValue(cell, sequence);
                continue;
            }

            Object value = getFieldValue(data, fieldName);
            Cell cell = row.getCell(j);
            if (cell == null) {
                cell = row.createCell(j);
            }
            setCellValue(cell, value);
        }
    }

    /**
     * 设置单元格值
     */
    private static void setCellValue(Cell cell, Object value) {
        if (value != null) {
            if (value instanceof String) {
                cell.setCellValue((String) value);
            } else if (value instanceof Number) {
                cell.setCellValue(((Number) value).doubleValue());
            } else if (value instanceof Boolean) {
                cell.setCellValue((Boolean) value);
            } else if (value instanceof Date) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                cell.setCellValue(sdf.format(value));
            } else if (value instanceof LocalDateTime) {
                cell.setCellValue(value.toString());
            } else {
                cell.setCellValue(value.toString());
            }
        }
    }

    /**
     * 清空模板行的占位符内容
     */
    private static void clearTemplateRowPlaceholders(Row templateRow, List<String> placeholders) {
        for (int j = 0; j < placeholders.size(); j++) {
            if (placeholders.get(j) != null) {
                Cell cell = templateRow.getCell(j);
                if (cell != null) {
                    cell.setCellValue("");
                }
            }
        }
    }

    /**
     * 复制行样式和属性
     */
    private static void copyRow(Sheet sheet, Row sourceRow, Row targetRow) {
        targetRow.setHeight(sourceRow.getHeight());

        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            Cell sourceCell = sourceRow.getCell(i);
            if (sourceCell != null) {
                Cell targetCell = targetRow.createCell(i);
                targetCell.setCellStyle(sourceCell.getCellStyle());
            }
        }

        // 复制合并单元格（如果需要）
//        copyMergedRegions(sheet, sourceRow.getRowNum(), targetRow.getRowNum());
    }

    /**
     * 复制合并单元格
     */
    private static void copyMergedRegions(Sheet sheet, int srcRowIndex, int destRowIndex) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            if (mergedRegion.getFirstRow() == srcRowIndex && mergedRegion.getLastRow() == srcRowIndex) {
                CellRangeAddress newMergedRegion = new CellRangeAddress(
                        destRowIndex,
                        destRowIndex,
                        mergedRegion.getFirstColumn(),
                        mergedRegion.getLastColumn()
                );
                sheet.addMergedRegion(newMergedRegion);
            }
        }
    }

    /**
     * 通过反射获取对象字段值
     */
    private static Object getFieldValue(Object obj, String fieldName) {
        try {
            Class<?> clazz = obj.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

    // 处理合并单元格
    private static void copyMergedRegions(Sheet srcSheet, Sheet destSheet, int srcRowIndex, int destRowIndex) {
        for (int i = 0; i < srcSheet.getNumMergedRegions(); i++) {
            CellRangeAddress mergedRegion = srcSheet.getMergedRegion(i);
            if (mergedRegion.getFirstRow() == srcRowIndex && mergedRegion.getLastRow() == srcRowIndex) {
                CellRangeAddress newMergedRegion = new CellRangeAddress(
                        destRowIndex,
                        destRowIndex,
                        mergedRegion.getFirstColumn(),
                        mergedRegion.getLastColumn()
                );
                destSheet.addMergedRegion(newMergedRegion);
            }
        }
    }
}
