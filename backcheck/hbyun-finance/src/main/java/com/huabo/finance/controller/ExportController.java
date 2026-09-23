package com.huabo.finance.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.finance.service.ExportFileService;
import com.huabo.finance.vo.ExportRequestVo;

import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.http.server.HttpServerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/export")
@Tag(name="操作导出选中信息",description="操作导出选中信息")
public class ExportController {

	@Resource
	ExportFileService exportFileService;
	
	@Autowired
	private UserProvider userProvider;
	
    /**
     * A 操作导出列表信息为excel
     * @param token
     * @param diaryBookRequestVo
     * @return
     */
	@PostMapping("/generateFile")
    @Operation(summary="导出选中信息为Excel",description="日记账 - 列表分页查询")
    public JsonBean generateFile(HttpServletRequest request,HttpServletResponse response,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "exportRequestVo", description = "导出附件信息公用类", required = true) @RequestBody ExportRequestVo exportRequestVo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        return exportFileService.exportFileFunc(token,exportRequestVo,staff,response,request);
    }
}
