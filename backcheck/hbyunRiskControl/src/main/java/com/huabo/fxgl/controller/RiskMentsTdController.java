package com.huabo.fxgl.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.RiskAssessmentstd;
import com.huabo.fxgl.service.IRiskAssessmentstdService;
import com.huabo.fxgl.service.IRiskInfludegreeService;
import com.huabo.fxgl.service.IRiskLevelmappingService;
import com.huabo.fxgl.service.IRiskPossibilityService;
import com.huabo.fxgl.vo.fieldActivationVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 风险设置 --风险评估标准
 *
 * @author CJ
 * @version 1.0
 * @since 2022-12-09
 */
@RestController
@RequestMapping(value = "/fxbz", method = { RequestMethod.GET, RequestMethod.POST })
@Tag(name="风险设置 --风险评估标准",description="风险设置 --风险评估标准")
@Slf4j
public class RiskMentsTdController {
	@Autowired
	private IRiskAssessmentstdService riskAssessmentstdService;

	@Autowired
	private IRiskPossibilityService iRiskPossibilityService;

	@Autowired
	private IRiskInfludegreeService iRiskInfludegreeService;

	@Autowired
	private IRiskLevelmappingService iRiskLevelmappingService;
	
	@Resource
    private UserProvider userProvider;

	@Value("${application.administrators:}")
	private String administrators;
	
	@OperationLog(
			success = "查询风险评估标准列表成功",
			busType = "风险评估",
			fail = "查询风险评估标准列表失败",
			operationType = OperationType.SELECT,
			subType = "评估标准"
	)
	@RequestMapping(value = "/v_list", method = { RequestMethod.GET }, produces = "application/json; charset=utf-8")
	@Operation(summary = "风险设置--风险评估标准列表页:/fxbz/v_list")
	public JsonBean law_regulations(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
			@Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
			@Parameter(name = "assNumber", description = "查询条件：编号", required = false) @RequestParam(value = "assNumber", required = false) String assNumber,
			@Parameter(name = "assName", description = "查询条件：名称", required = false) @RequestParam(value = "assName", required = false) String assName)
					throws Exception {
		TblStaffUtil staffUtil = userProvider.get();// 得到了当前登录的用户信息
		if (staffUtil == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		RiskAssessmentstd risk = new RiskAssessmentstd();
		//设置参数
		risk.setAssnumber(assNumber);
		risk.setAssname(assName);
		risk.setCompanyid(staffUtil.getCurrentOrg().getOrgid());
		  //用于判断是否为风险管理员；该角色能看到本公司所有的风险创建信息；
				Integer authorityType;
				if (JudgeRoleRight.judgeRoleRight(administrators, staffUtil.getRoleNames())) {
					authorityType = 1;
				} else {
					authorityType = 0;
				}
				//评估标准本身已经是按照公司显示数据了，不用根据风险管理员来判断了
		IPage page = new Page(pageNo, pageSize);// 分页设置
		PageInfo<RiskAssessmentstd> riskList = riskAssessmentstdService.findByAllList(risk,pageNo,pageSize,staffUtil,authorityType);
		Map<String, Object> result = new HashMap<String, Object>(0);
		result.put("assNumber", assNumber);
		result.put("risk", riskList);
		result.put("assName", assName);
		return ResponseFormat.retParam(1, 200, result);
	}


	  @OperationLog(
	 			success = "查询风险评估标准编号成功",
	 			busType = "风险评估",
	 			fail = "查询风险评估标准编号失败",
	 			operationType = OperationType.SELECT,
	 			subType = "评估标准"
	 	)
	@RequestMapping(value = "/get_riskpgbz_no",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "获取风险评估标准编号")
    public JsonBean get_riskpgbz_no(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    		) throws Exception {
        JsonBean jsonBean = this.riskAssessmentstdService.get_riskpgbz_no(token);

        return jsonBean;
    }


	  @OperationLog(
	 			success = "删除风险评估标准【{{#assstdid}}】成功",
	 			busType = "风险评估",
	 			fail = "删除风险评估标准【{{#assstdid}}】失败",
	 			operationType = OperationType.SELECT,
	 			subType = "评估标准"
	 	)
	@RequestMapping(value = "/delete", method = { RequestMethod.GET }, produces = "application/json; charset=utf-8")
	@Operation(summary = "风险设置--风险评估标准列表页-删除:/fxbz/delete")
	public JsonBean delete(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "assstdid", description = "风险主键", required = true) @RequestParam(value = "assstdid", required = true) String assstdid)
					throws Exception {
		TblStaffUtil staffUtil = userProvider.get();// 得到了当前登录的用户信息
		if (staffUtil == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		Map<String, Object> result = riskAssessmentstdService.deleteRisk(assstdid);
		return ResponseFormat.retParam(1, 200, result);
	}



	  @OperationLog(
	 			success = "新增风险评估标准【{{#assstdid}}】成功",
	 			busType = "风险评估",
	 			fail = "新增风险评估标准【{{#assstdid}}】失败",
	 			operationType = OperationType.ADD,
	 			subType = "评估标准"
	 	)
	@RequestMapping(value = "/savebz", method = { RequestMethod.POST }, produces = "application/json; charset=utf-8")
	@Operation(summary = "风险设置--风险评估标准列表页-新增保存:/fxbz/savebz")
	public JsonBean savebz(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "assNumber", description = "编号", required = false) @RequestParam(value = "assNumber", required = false) String assNumber,
			@Parameter(name = "assName", description = "名称", required = false) @RequestParam(value = "assName", required = false) String assName,
			@Parameter(name = "assDes", description = "描述", required = false) @RequestParam(value = "assDes", required = false) String assDes,
			  @Parameter(name = "secrectLevelId", description = "密级主键") @RequestParam(value = "secrectLevelId", required = false)BigDecimal secrectLevelId,
	            @Parameter(name = "staffScopeNames", description = "知悉范围名称") @RequestParam(value = "staffScopeNames", required = false)String staffScopeNames,
	            @Parameter(name = "staffScopeIds", description = "知悉范围id") @RequestParam(value = "staffScopeIds", required = false)String staffScopeIds,
	            @Parameter(name = "possibilityStr", description = "风险发生频率jsonArray，前端默认五条， 示例：[{\"possid\": \"主键ID\",\"rplevel\": \"等级\",\"possdes\": \"说明\"},{\"possid\": \"主键ID\",\"rplevel\": \"等级\",\"possdes\": \"登记\"}]", required = true) @RequestParam(value = "possibilityStr", required = true) String possibilityStr,
	            @Parameter(name = "infludegreeStr", description = "风险影响程度jsonArray，前端默认五条，示例：[{\"degreeid\": \"主键ID\",\"rilevel\": \"等级\",\"infludegreedes\": \"说明\"},{\"degreeid\": \"主键ID\",\"rilevel\": \"等级\",\"infludegreedes\": \"说明\"}]", required = true) @RequestParam(value = "infludegreeStr", required = true) String infludegreeStr,
	            @Parameter(name = "levelStr", description = "风险影响程度jsonArray，前端默认五条，示例：[{\"rlevelmapid\": \"主键ID\",\"poss1\": \"风险级别1\",\"poss2\": \"风险级别2\",\"poss3\": \"风险级别3\",\"poss4\": \"风险级别4\",\"poss5\": \"风险级别5\"},{\"rlevelmapid\": \"主键ID\",\"poss1\": \"风险级别1\",\"poss2\": \"风险级别2\",\"poss3\": \"风险级别3\",\"poss4\": \"风险级别4\",\"poss5\": \"风险级别5\"}]", required = true) @RequestParam(value = "levelStr", required = true) String levelStr,
				@ModelAttribute fieldActivationVo vo
			)throws Exception {
		TblStaffUtil staffUtil = userProvider.get();// 得到了当前登录的用户信息
		if (staffUtil == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		Map<String, Object> result = riskAssessmentstdService.saveBz(assNumber, assName, assDes, staffUtil,possibilityStr,infludegreeStr,levelStr,secrectLevelId,staffScopeNames,staffScopeIds,vo);
		return ResponseFormat.retParam(1, 200, result);
	}

	  @OperationLog(
	 			success = "查询风险评估标准详情【{{#assstdid}}】成功",
	 			busType = "风险评估",
	 			fail = "查询风险评估标准详情【{{#assstdid}}】失败",
	 			operationType = OperationType.SELECT,
	 			subType = "评估标准"
	 	)
	@RequestMapping(value = "/get", method = { RequestMethod.POST }, produces = "application/json; charset=utf-8")
	@Operation(summary = "风险设置--风险评估标准列表页-查看返回评估标准信息:/fxbz/get")
	public JsonBean savebz(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "assstdid", description = "主键", required = true) @RequestParam(value = "assstdid", required = true) BigDecimal assstdid)
					throws Exception {
		TblStaffUtil staffUtil = userProvider.get();// 得到了当前登录的用户信息
		if (staffUtil == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		Map<String, Object> result = riskAssessmentstdService.getRiskAssessMentstd(assstdid);
		return ResponseFormat.retParam(1, 200, result);
	}


	  @OperationLog(
	 			success = "修改风险评估标准【{{#assstdid}}】成功",
	 			busType = "风险评估",
	 			fail = "修改风险评估标准【{{#assstdid}}】失败",
	 			operationType = OperationType.UPDATE,
	 			subType = "评估标准"
	 	)
	@RequestMapping(value = "/updatebz", method = { RequestMethod.POST }, produces = "application/json; charset=utf-8")
	@Operation(summary = "风险设置--风险评估标准列表页-修改评估标准信息:/fxbz/updatebz")
	public JsonBean updatebz(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "assstdid", description = "主键", required = true) @RequestParam(value = "assstdid", required = true) BigDecimal assstdid,
			@Parameter(name = "assNumber", description = "编号", required = false) @RequestParam(value = "assNumber", required = false) String assNumber,
			@Parameter(name = "assName", description = "名称", required = false) @RequestParam(value = "assName", required = false) String assName,
			@Parameter(name = "assDes", description = "描述", required = false) @RequestParam(value = "assDes", required = false) String assDes,
			  @Parameter(name = "secrectLevelId", description = "密级主键") @RequestParam(value = "secrectLevelId", required = false)BigDecimal secrectLevelId,
	            @Parameter(name = "staffScopeNames", description = "知悉范围名称") @RequestParam(value = "staffScopeNames", required = false)String staffScopeNames,
	            @Parameter(name = "staffScopeIds", description = "知悉范围id") @RequestParam(value = "staffScopeIds", required = false)String staffScopeIds,
	            @Parameter(name = "possibilityStr", description = "风险发生频率jsonArray，前端默认五条， 示例：[{\"possid\": \"主键ID\",\"rplevel\": \"等级\",\"possdes\": \"说明\"},{\"possid\": \"主键ID\",\"rplevel\": \"等级\",\"possdes\": \"登记\"}]", required = true) @RequestParam(value = "possibilityStr", required = true) String possibilityStr,
	            @Parameter(name = "infludegreeStr", description = "风险影响程度jsonArray，前端默认五条，示例：[{\"degreeid\": \"主键ID\",\"rilevel\": \"等级\",\"infludegreedes\": \"说明\"},{\"degreeid\": \"主键ID\",\"rilevel\": \"等级\",\"infludegreedes\": \"说明\"}]", required = true) @RequestParam(value = "infludegreeStr", required = true) String infludegreeStr,
	            @Parameter(name = "levelStr", description = "风险影响程度jsonArray，前端默认五条，示例：[{\"rlevelmapid\": \"主键ID\",\"poss1\": \"风险级别1\",\"poss2\": \"风险级别2\",\"poss3\": \"风险级别3\",\"poss4\": \"风险级别4\",\"poss5\": \"风险级别5\"},{\"rlevelmapid\": \"主键ID\",\"poss1\": \"风险级别1\",\"poss2\": \"风险级别2\",\"poss3\": \"风险级别3\",\"poss4\": \"风险级别4\",\"poss5\": \"风险级别5\"}]", required = true) @RequestParam(value = "levelStr", required = true) String levelStr,
				@ModelAttribute fieldActivationVo vo
			)
					throws Exception {
		TblStaffUtil staffUtil = userProvider.get();// 得到了当前登录的用户信息
		if (staffUtil == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		RiskAssessmentstd risk = new RiskAssessmentstd();
		risk.setAssnumber(assNumber);
		risk.setAssname(assName);
		risk.setAssstdid(assstdid);
		risk.setAssdes(assDes);
		risk.setStaffScopeIds(staffScopeIds);
		risk.setStaffScopeNames(staffScopeNames);
		risk.setSecrectLevelId(secrectLevelId);
		risk.setCompanyid(staffUtil.getCurrentOrg().getOrgid());
		if(vo!=null){
			risk.setFieldActivationCopy(vo);
		}
		Map<String, Object> result = riskAssessmentstdService.updateRiskAssessMentstd(risk,possibilityStr,infludegreeStr,levelStr);
		return ResponseFormat.retParam(1, 200, result);
	}


}
