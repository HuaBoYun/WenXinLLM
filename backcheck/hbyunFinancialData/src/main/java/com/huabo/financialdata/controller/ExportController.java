package com.huabo.financialdata.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblAttachment;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.vo.export.ExportRequestVo;
import com.huabo.financialdata.service.ExportFileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/export")
@Tag(name="操作导出选中信息",description="操作导出选中信息")
public class ExportController {

	@Resource
	ExportFileService exportFileService;
    /**
     * A 操作导出列表信息为excel
     * @param token
     * @param diaryBookRequestVo
     * @return
     */
	@PostMapping("/generateFile")
    @Operation(summary="导出选中信息为Excel",description="日记账 - 列表分页查询")
    public ApiResponse<TblAttachment> generateFile(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "exportRequestVo", description = "导出附件信息公用类", required = true) @RequestBody ExportRequestVo exportRequestVo) throws Exception {

        return exportFileService.exportFileFunc(token,exportRequestVo);
    }
	
    
    /**
     * 文件下载
     */
    @GetMapping(value = "/download")
    @Operation(summary = "文件下载接口")
    public void fileDownLoad(HttpServletResponse response,
                             @Parameter(name = "fileId", description = "文件ID", required = true) @RequestParam("fileId") String fileId)throws Exception {
    	exportFileService.fileDownLoad(response, fileId,false);
}
}
