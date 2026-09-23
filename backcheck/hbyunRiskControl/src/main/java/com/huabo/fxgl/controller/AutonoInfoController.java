package com.huabo.fxgl.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.service.IAutonoInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiYe
 * @since 2022-08-15
 */
@Slf4j
@Tag(name="风险管控-风险评估-评估计划",description="风险管控-风险评估-评估计划")
@RestController
@RequestMapping(value = "/nbkz", method = {RequestMethod.GET, RequestMethod.POST})
public class AutonoInfoController {


    @Autowired
    private IAutonoInfoService iAutonoInfoService;
    
    @Resource
    private UserProvider userProvider;

    /**
     *  通用自动返回编号
     * @param token
     * @param tblName
     * @param column
     * @param orgCol
     * @param noId
     * @return
     * @throws Exception
     * @auther guanhongyi
     * @date  2022/8/13
     * @version 1.0.1
     */

	@OperationLog(
			success = "通用自动返回编号成功",
			busType = "风险评估",
			fail = "通用自动返回编号失败",
			operationType = OperationType.SELECT,
			subType = "评估计划"
	)
	@Operation(summary = "通用自动返回编号 /nbkz/code/findAutoNumber")
	@GetMapping(value = "/code/findAutoNumber")
	public String findAutoNumber(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "tblName", description = "tblName 表名", required = true) @RequestParam(value = "tblName", required = true) String tblName,
			@Parameter(name = "column", description = "column 字段", required = true) @RequestParam(value = "column", required = true) String column,
			@Parameter(name = "orgCol", description = "orgCol 组织id", required = true) @RequestParam(value = "orgCol", required = true) String orgCol,
			@Parameter(name = "noId", description = "noId TBL_AutoNoInfo主键", required = true) @RequestParam(value = "noId", required = true) Integer noId)
					throws Exception {
		log.info("{}", token);
		log.info("{}", tblName);
		log.info("{}", column);
		log.info("{}", orgCol);
		log.info("{}", noId);
		TblStaffUtil staffUtil = userProvider.get();// 得到了当前登录的用户信息
		TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); // 当前用户选择的组织
		// 获取下一个评估计划编号
		String flowNextId = null;
		try {
			flowNextId = iAutonoInfoService.selectFlowNextId(tblName, column, orgCol, selectOrg.getOrgid(), noId, null,
					null, null);
			log.info("control参数");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flowNextId;
	}


}
