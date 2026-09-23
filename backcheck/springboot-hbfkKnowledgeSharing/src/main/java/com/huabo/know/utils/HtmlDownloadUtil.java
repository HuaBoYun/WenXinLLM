package com.huabo.know.utils;


import cn.hutool.core.io.FileUtil;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.ddr.poi.html.HtmlRenderPolicy;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * html下载
 */
public class HtmlDownloadUtil {

    public static void h2w(String html,String fileName, HttpServletResponse response) throws IOException {
        if (StringUtils.isBlank(html)) {
            return;
        }
        response.reset();
        // 处理文件名编码，确保中文名称不乱码
        String encodedFilename = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        response.addHeader("Content-Disposition", "attachment;filename=" + encodedFilename+".docx");
        response.setContentType("application/msword");
        response.setCharacterEncoding("utf-8");

        String content="<html><body>"+html+"</body></html>";

        HtmlRenderPolicy htmlRenderPolicy = new HtmlRenderPolicy();
        Configure configure = Configure.builder()
                .bind("htmlView", htmlRenderPolicy)
                .build();

        Map<String, Object> data = new HashMap<>();
        data.put("htmlView", content);
        XWPFTemplate.compile(FileUtil.class.getResourceAsStream("/word_out_template.docx"), configure).render(data).writeAndClose(response.getOutputStream());
    }

    public static void zipDownload(Map<String,String> contentMap, String zipName, HttpServletResponse response) throws IOException {
        response.setHeader("Content-disposition", "attachment;filename=" + zipName + ".zip");
        response.setContentType("application/zip");

        //输出文件
        response.setCharacterEncoding("utf-8");
        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());

        for (Map.Entry<String, String> entry : contentMap.entrySet()) {
            String fileName = entry.getKey();
            String html = entry.getValue();


            String content="<html><body>"+html+"</body></html>";

            HtmlRenderPolicy htmlRenderPolicy = new HtmlRenderPolicy();
            Configure configure = Configure.builder()
                    .bind("htmlView", htmlRenderPolicy)
                    .build();

            Map<String, Object> data = new HashMap<>();
            data.put("htmlView", content);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            XWPFTemplate.compile(FileUtil.class.getResourceAsStream("/word_out_template.docx"), configure).render(data).writeAndClose(out);

            byte[] bytes = out.toByteArray();
            addFileToZip(bytes, fileName, zipOut);
        }

        zipOut.flush();
        zipOut.close();
    }

    private static void addFileToZip(byte[] bytes, String fileName, ZipOutputStream zipOutputStream) throws IOException {
        ZipEntry zipEntry = new ZipEntry(fileName + ".docx");
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(bytes);
        zipOutputStream.closeEntry();
    }




    public static void html2word(String html,String fileName, HttpServletResponse response) throws Exception {
        response.reset();
        // 处理文件名编码，确保中文名称不乱码
        String encodedFilename = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        response.addHeader("Content-Disposition", "attachment;filename=" + encodedFilename+".docx");
        response.setContentType("application/msword");
        response.setCharacterEncoding("utf-8");


        html = html.replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"").replace("&amp;", "&");
        String content="<html><body>"+html+"</body></html>";

        //这里是必须要设置编码的，不然导出中文就会乱码。
        byte b[] = content.getBytes("utf-8");
        //将字节数组包装到流中
        ByteArrayInputStream bais = new ByteArrayInputStream(b);

        //生成word格式
        POIFSFileSystem poifs = new POIFSFileSystem();
        DirectoryEntry directory = poifs.getRoot();
        DocumentEntry documentEntry = directory.createDocument("WordDocument", bais);

        //写入内容
        poifs.writeFilesystem(response.getOutputStream());
        bais.close();
        poifs.close();
    }
}
