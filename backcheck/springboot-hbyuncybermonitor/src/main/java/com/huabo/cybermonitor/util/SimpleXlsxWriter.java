package com.huabo.cybermonitor.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 零依赖 OOXML xlsx 生成工具
 * <p>
 * 使用 JDK 标准库 ZipOutputStream + 手写 OOXML 生成最小可用的 .xlsx 文件，
 * 完全不依赖 Apache POI / EasyExcel / commons-io，避开版本冲突问题。
 * </p>
 *
 * <p>所有单元格统一使用 inlineStr 模式（数字也按字符串导出），保持实现简单可靠。</p>
 *
 * @author huabo
 */
public final class SimpleXlsxWriter {

    private SimpleXlsxWriter() {}

    /**
     * 把二维表数据写成 .xlsx 字节数组。
     *
     * @param sheetName 工作表名称（不能为空）
     * @param rows      行数据，每行的元素将转换为字符串写入；null 元素按空串处理
     * @return xlsx 文件字节数组
     */
    public static byte[] write(String sheetName, List<List<Object>> rows) throws IOException {
        if (sheetName == null || sheetName.isEmpty()) {
            sheetName = "Sheet1";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            putEntry(zos, "[Content_Types].xml", contentTypesXml());
            putEntry(zos, "_rels/.rels", rootRelsXml());
            putEntry(zos, "xl/_rels/workbook.xml.rels", workbookRelsXml());
            putEntry(zos, "xl/workbook.xml", workbookXml(sheetName));
            putEntry(zos, "xl/styles.xml", stylesXml());
            putEntry(zos, "xl/worksheets/sheet1.xml", sheetXml(rows));
        }
        return baos.toByteArray();
    }

    private static void putEntry(ZipOutputStream zos, String name, String content) throws IOException {
        ZipEntry entry = new ZipEntry(name);
        zos.putNextEntry(entry);
        zos.write(content.getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

    private static String contentTypesXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">"
                + "<Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>"
                + "<Default Extension=\"xml\" ContentType=\"application/xml\"/>"
                + "<Override PartName=\"/xl/workbook.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml\"/>"
                + "<Override PartName=\"/xl/worksheets/sheet1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/>"
                + "<Override PartName=\"/xl/styles.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml\"/>"
                + "</Types>";
    }

    private static String rootRelsXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">"
                + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"xl/workbook.xml\"/>"
                + "</Relationships>";
    }

    private static String workbookRelsXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">"
                + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet1.xml\"/>"
                + "<Relationship Id=\"rId2\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles\" Target=\"styles.xml\"/>"
                + "</Relationships>";
    }

    private static String workbookXml(String sheetName) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<workbook xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\""
                + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\">"
                + "<sheets><sheet name=\"" + xmlEscape(sheetName) + "\" sheetId=\"1\" r:id=\"rId1\"/></sheets>"
                + "</workbook>";
    }

    private static String stylesXml() {
        // 最小化样式表：一种字体、一种填充、一种边框、一种单元格样式
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<styleSheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">"
                + "<fonts count=\"1\"><font><sz val=\"11\"/><name val=\"Calibri\"/></font></fonts>"
                + "<fills count=\"1\"><fill><patternFill patternType=\"none\"/></fill></fills>"
                + "<borders count=\"1\"><border/></borders>"
                + "<cellStyleXfs count=\"1\"><xf/></cellStyleXfs>"
                + "<cellXfs count=\"1\"><xf/></cellXfs>"
                + "</styleSheet>";
    }

    private static String sheetXml(List<List<Object>> rows) {
        StringBuilder sb = new StringBuilder(4096);
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        sb.append("<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">");
        sb.append("<sheetData>");
        if (rows != null) {
            for (int r = 0; r < rows.size(); r++) {
                List<Object> row = rows.get(r);
                if (row == null) row = java.util.Collections.emptyList();
                sb.append("<row r=\"").append(r + 1).append("\">");
                for (int c = 0; c < row.size(); c++) {
                    Object cell = row.get(c);
                    String value = cell == null ? "" : String.valueOf(cell);
                    String ref = colName(c) + (r + 1);
                    sb.append("<c r=\"").append(ref).append("\" t=\"inlineStr\">")
                            .append("<is><t xml:space=\"preserve\">")
                            .append(xmlEscape(value))
                            .append("</t></is></c>");
                }
                sb.append("</row>");
            }
        }
        sb.append("</sheetData></worksheet>");
        return sb.toString();
    }

    /** 列下标转字母，如 0->A, 25->Z, 26->AA */
    private static String colName(int index) {
        StringBuilder s = new StringBuilder();
        int n = index;
        do {
            s.insert(0, (char) ('A' + n % 26));
            n = n / 26 - 1;
        } while (n >= 0);
        return s.toString();
    }

    private static String xmlEscape(String s) {
        if (s == null || s.isEmpty()) return "";
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '<': sb.append("&lt;"); break;
                case '>': sb.append("&gt;"); break;
                case '&': sb.append("&amp;"); break;
                case '"': sb.append("&quot;"); break;
                case '\'': sb.append("&apos;"); break;
                default:
                    // 过滤掉 XML 1.0 不允许的控制字符
                    if (ch < 0x20 && ch != '\t' && ch != '\n' && ch != '\r') {
                        sb.append(' ');
                    } else {
                        sb.append(ch);
                    }
            }
        }
        return sb.toString();
    }
}
