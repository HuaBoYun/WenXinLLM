package com.huabo.bigmodel.controller;

import com.huabo.bigmodel.common.Result;
import com.huabo.bigmodel.service.FileParseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件解析控制器：接收前端上传的文档，抽取为纯文本返回，供大模型分析。
 * 仅做「上传 + 抽文本」，不落库、不转发，文本由前端拼进对话消息后发给咨询接口。
 * 支持格式：md / txt / doc / docx / xls / xlsx / pdf
 */
@Slf4j
@RestController
@RequestMapping("/v1/ai/file")
@Tag(name = "文件解析接口", description = "上传文档抽取纯文本，供AI咨询分析")
public class FileParseController {

    private final FileParseService fileParseService;

    public FileParseController(FileParseService fileParseService) {
        this.fileParseService = fileParseService;
    }

    /**
     * 解析上传文件为纯文本
     *
     * @param file 上传的文件
     * @return Result，data 含 fileName 和 content
     */
    @PostMapping("/parse")
    @Operation(summary = "解析文件为文本", description = "支持 md/txt/doc/docx/xls/xlsx/pdf")
    public Result<Map<String, Object>> parse(@RequestParam("file") MultipartFile file) {
        try {
            String content = fileParseService.parseToText(file);
            Map<String, Object> data = new HashMap<>();
            data.put("fileName", file.getOriginalFilename());
            data.put("content", content);
            data.put("length", content != null ? content.length() : 0);
            log.info("[FileParse] 解析成功: {}, 文本长度: {}",
                    file.getOriginalFilename(), content != null ? content.length() : 0);
            return Result.success("解析成功", data);
        } catch (IllegalArgumentException e) {
            log.warn("[FileParse] 解析失败（参数问题）: {}", e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("[FileParse] 解析异常", e);
            return Result.error("文件解析失败: " + e.getMessage());
        }
    }
}
