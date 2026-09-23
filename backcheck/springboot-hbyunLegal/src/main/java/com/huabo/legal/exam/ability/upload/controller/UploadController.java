package com.huabo.legal.exam.ability.upload.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huabo.legal.exam.ability.Constant;
import com.huabo.legal.exam.ability.upload.dto.UploadReqDTO;
import com.huabo.legal.exam.ability.upload.dto.UploadRespDTO;
import com.huabo.legal.exam.ability.upload.service.UploadService;
import com.huabo.legal.exam.core.api.ApiRest;
import com.huabo.legal.exam.core.api.controller.BaseController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;

/**
 * 本地文件上传下载请求类
 * @author bool
 */
@Log4j2
@Tag(name="文件上传",description="文件上传")
@RestController
public class UploadController extends BaseController {

    @Autowired
    private UploadService uploadService;

    /**
     * 文件上传
     * @param reqDTO
     * @return
     */
    @PostMapping("/common/api/file/upload")
    @Operation(summary = "文件上传", description = "此接口较为特殊，参数都通过表单方式提交，而非JSON")
    public ApiRest<UploadRespDTO> upload(@ModelAttribute UploadReqDTO reqDTO) {
        // 上传并返回URL
        UploadRespDTO respDTO = uploadService.upload(reqDTO);
        return super.success(respDTO);
    }

    /**
     * 独立文件下载
     * @param request
     * @param response
     */
    @GetMapping(Constant.FILE_PREFIX+"**")
    @Operation(summary = "文件下载", description = "文件下载")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        uploadService.download(request, response);
    }
}
