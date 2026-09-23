package com.huabo.system.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblSystemDistribution;
import com.huabo.system.service.TblSystemDistributionService;
import com.huabo.system.vo.TblSystemDistributionVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 业务表单下发控制器
 * <p>提供业务单据下发事项的保存、查询、删除等接口</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping("/distribution")
@Tag(name="DistributionController",description="系统业务表单下发所有接口")
public class DistributionController {

	@Resource
	private TblSystemDistributionService tblSystemDistributionService;
	
	
	@PostMapping("/saveDistributionDg")
	@Operation(summary="业务单据下发事项保存多个接口")
	public JsonBean saveDistributionDg(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "tableId",description="单据类型tableId:  95-整改追责-整改方案 、97-整改追责-整改落实、666-审计立项建议通知，1511-立项建议表 、1386-需求建议表、1388-分管领导汇总，1387-服务需求表，1396-二级单位及成员单位离任审计，1495-未委托及预计离任，1395-三级单位离任审计，1497-工作方案，1524-审计项目表，627-审计项目制度，569272297635909-ip清单，1465-工程审计项目安排，1466-财务审计项目安排，579594840944709-财务督导分工，579594969821253-工程督导分工，800022-问题定责，80051-获奖通知",required=true) @RequestParam(value = "tableId", required = true) String tableId,
			@Parameter(name="jsondistribution",description="下发内容json",required=false) @RequestParam(value = "jsondistribution", required = false)  String jsondistribution) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblSystemDistributionService.saveDistributions(token,jsondistribution,tableId);
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	
	
	@PostMapping("/saveDistribution")
	@Operation(summary="业务单据下发事项保存接口")
	public JsonBean saveDistribution(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "tableId",description="单据类型tableId: 95-整改追责-整改方案 、97-整改追责-整改落实、666-审计立项建议通知，1511-立项建议表 、1386-需求建议表、1388-分管领导汇总，1387-服务需求表，1396-二级单位及成员单位离任审计，1495-未委托及预计离任,1395-三级单位离任审计",required=true) @RequestParam(value = "tableId", required = true) String tableId,
			TblSystemDistribution distribution) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblSystemDistributionService.saveDistribution(token,distribution,tableId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	@GetMapping("/getDistributionListPage")
	@Operation(summary="分页获取业务单据下发待处理信息")
	public JsonBean getDistributionListPage(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageSize",description="每页数量",required=false) @RequestParam(value = "pageSize", required = false,defaultValue = "5") Integer pageSize,
			@Parameter(name = "pageNumber",description="当前页",required=false) @RequestParam(value = "pageNumber", required = false,defaultValue = "1") Integer pageNumber,
			TblSystemDistributionVo distribution) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblSystemDistributionService.getDistributionListPage(token,distribution,pageSize,pageNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	@GetMapping("/getDistributionTypeList")
	@Operation(summary="获取所有类型分类信息")
	public JsonBean getDistributionTypeList(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			TblSystemDistributionVo distribution) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblSystemDistributionService.getDistributionType(token,distribution);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	
	@PostMapping("/modifyDistributionInfo")
	@Operation(summary="点击下发信息调用")
	public JsonBean modifyDistributionInfo(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			TblSystemDistribution distribution) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblSystemDistributionService.modifyDistributionInfo(token,distribution);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return jsonBean;
	}
	
	@PostMapping("/batchModifyDistributionInfo")
	@Operation(summary="点击下发信息调用")
	public JsonBean batchModifyDistributionInfo(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "ids",description="下发信息ID数组",required=true) @RequestParam(value = "ids", required = true) String[] ids) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblSystemDistributionService.batchModifyDistributionInfo(token,ids);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return jsonBean;
	}
	
	
	
	
	@PostMapping("/deleteDistribution")
	@Operation(summary="业务单据下发事项撤回删除接口")
	public JsonBean deleteDistribution(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "ids",description="撤回的事项id集合",required=true) @RequestParam(value = "ids", required = true) String ids,
			TblSystemDistribution distribution) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblSystemDistributionService.deleteDistribution(token, ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	
	
}
