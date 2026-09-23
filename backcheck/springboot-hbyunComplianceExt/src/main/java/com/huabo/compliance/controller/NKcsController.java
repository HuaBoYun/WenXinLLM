package com.huabo.compliance.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.compliance.entity.TblTestTemplate;
import com.huabo.compliance.entity.TblTestelement;
import com.huabo.compliance.entity.TblTestplan;
import com.huabo.compliance.entity.TblTesttemplType;
import com.huabo.compliance.service.ITblTestplanService;
import com.huabo.compliance.service.TblAssessService;
import com.huabo.compliance.service.TblAutonoNumberService;
import com.huabo.compliance.service.TblTestElementService;
import com.huabo.compliance.service.TblTestTempTypeService;
import com.huabo.compliance.service.TblTestTemplateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping(value = "/nbkz")
@Tag(name="测试模板  urlpre-/nbkz",description="测试模板  urlpre-/nbkz")
public class NKcsController {

	@Resource
	public TblTestTempTypeService tblTestTempTypeService;

	@Resource
	public TblTestTemplateService tblTestTemplateService;

	@Resource
	public TblTestElementService tblTestElementService;

	@Resource
	ITblTestplanService tblTestPlanService;

	@Autowired
	TblAssessService tblAssessService;


	@Resource
	TblAutonoNumberService tblAtonoNumberService;
	
	@Resource
	private UserProvider userProvider;


	@OperationLog(
			success = "测试模板下发功能成功",
			busType = "内控测试",
			fail = "测试模板下发功能失败",
			operationType = OperationType.ADD,
			subType = "测试模板"
	)
	@PostMapping(value = "/csmb/saveissued")
	@Operation(summary = "测试模板下发功能")
	public JsonBean saveissued(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "tempIds", description = "测试模板主键 多个用,分割", required = true) @RequestParam(value = "tempIds", required = true) String tempIds,
			@Parameter(name = "orgIds", description = "下发公司主键 多个用,分割 ", required = true) @RequestParam(value = "orgIds", required = true) String orgIds) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblTestTemplateService.saveissued(token, tempIds, orgIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	/**
	 * 内控测试模板列表
	 *
	 * @return
	 */
	@OperationLog(
			success = "测试模板-列表查询成功",
			busType = "内控测试",
			fail = "测试模板-列表查询失败",
			operationType = OperationType.SELECT,
			subType = "测试模板"
	)
	@GetMapping("/csmb/def_tmpl_list")
	@Operation(summary = "测试模板-列表")
	public JsonBean csfa_def_tmpl_list(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "分页当前行数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "templeNumber", description = "测试模板编号", required = false) @RequestParam(value = "templeNumber", required = false) String templeNumber,
			@Parameter(name = "templename", description = "测试模板名字", required = false) @RequestParam(value = "templename", required = false) String templename)
			throws Exception {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblTestTemplateService.selectList(token, pageNumber, pageSize, templeNumber, templename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	/**
	 * 通用获取设置的编号
	 *
	 * @param
	 */
	@OperationLog(
			success = "测试模板-新建-编号生成查询成功",
			busType = "内控测试",
			fail = "测试模板-新建-编号生成查询失败",
			operationType = OperationType.SELECT,
			subType = "测试模板"
	)
	@GetMapping(value = "/csmb/getAutoNumber")
	@Operation(summary = "测试模板-新建-编号生成")
	public JsonBean findAutoNumber(HttpServletResponse response,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name="tblName",description="tblName") @RequestParam(value = "tblName") String tblName,
			@Parameter(name="column",description="column") @RequestParam(value = "column") String column,
			@Parameter(name="orgCol",description="orgCol") @RequestParam(value = "orgCol") String orgCol,
			@Parameter(name="noId",description="noId") @RequestParam(value = "noId") Integer noId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
        }
		String flowNextId = null;
		try {
			flowNextId = tblAssessService.findFlowNextId(tblName, column, orgCol, staff.getCurrentOrg().getOrgid(), noId, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.reset();
		return new JsonBean(200, "success", flowNextId);
	}


	/**
	 * 通用获取设置的编号 一节点所在的表存在组织Id 并且 在该一级节点下的二级、三级节点等等所在的表，都存在一级节点的主键列，
	 *
	 * @param o
	 */
	@OperationLog(
			success = "测试模板-新建-分类编号生成查询成功",
			busType = "内控测试",
			fail = "测试模板-新建-分类编号生成查询失败",
			operationType = OperationType.SELECT,
			subType = "测试模板"
	)
	@GetMapping(value = "/code/findAutoNextNumberForLevel")
	@Operation(summary = "测试模板-新建-分类编号生成")
	public JsonBean findAutoNextNumberForLevel(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name="chTblName",description="chTblName") @RequestParam(value = "chTblName") String chTblName,
			@Parameter(name="chNumberCol",description="chNumberCol") @RequestParam(value = "chNumberCol") String chNumberCol,
			@Parameter(name="chOrgCol",description="chOrgCol",required=false) @RequestParam(value = "chOrgCol", required = false) String chOrgCol,
			@Parameter(name="noId",description="noId") @RequestParam(value = "noId") Integer noId,
			@Parameter(name="parentTblName",description="parentTblName") @RequestParam(value = "parentTblName") String parentTblName,
			@Parameter(name="parentIdCol",description="parentIdCol") @RequestParam(value = "parentIdCol") String parentIdCol,
			@Parameter(name = "parentId", description = "测试模板 tmplId") @RequestParam(value = "parentId", required = true) String parentId,
			@Parameter(name="grandFatherTblName",description="grandFatherTblName") @RequestParam(value = "grandFatherTblName") String grandFatherTblName,
			@Parameter(name="grandFatherIdCol",description="grandFatherIdCol") @RequestParam(value = "grandFatherIdCol") String grandFatherIdCol,
			@Parameter(name="grandFatherOrgCol",description="grandFatherOrgCol") @RequestParam(value = "grandFatherOrgCol") String grandFatherOrgCol,
			@Parameter(name="chirldIdCol",description="chirldIdCol") @RequestParam(value = "chirldIdCol") String chirldIdCol,
			@Parameter(name="parentNumberCol",description="parentNumberCol") @RequestParam(value = "parentNumberCol") String parentNumberCol) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
        }
		String flowNextId = null;
		try {
			flowNextId = tblAtonoNumberService
					.findNumberLevelNextId(chTblName, chNumberCol, chOrgCol, staff.getCurrentOrg().getOrgid(), noId, parentTblName, parentIdCol,
							parentId, grandFatherTblName, grandFatherIdCol, grandFatherOrgCol, chirldIdCol, parentNumberCol, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.reset();
		return new JsonBean(200, "success", flowNextId);
	}


	@OperationLog(
			success = "测试模板-业务内容-业务编号生成查询成功",
			busType = "内控测试",
			fail = "测试模板-业务内容-业务编号生成查询失败",
			operationType = OperationType.SELECT,
			subType = "测试模板"
	)
	@GetMapping(value = "/code/findRootNumberByParentIdLevel")
	@Operation(summary = "测试模板-业务内容-业务编号生成")
	public JsonBean findRootNumberByParentIdLevel(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name="chilNumberCol",description="chilNumberCol") @RequestParam(value = "chilNumberCol") String chilNumberCol,
			@Parameter(name="chilTblName",description="chilTblName") @RequestParam(value = "chilTblName") String chilTblName,
			@Parameter(name="chilParentCol",description="chilParentCol") @RequestParam(value = "chilParentCol") String chilParentCol,
			@Parameter(name="parentIdCol",description="parentIdCol") @RequestParam(value = "parentIdCol") String parentIdCol,
			@Parameter(name="parentTblName",description="parentTblName") @RequestParam(value = "parentTblName") String parentTblName,
			@Parameter(name="parnetOrgCol",description="parnetOrgCol") @RequestParam(value = "parnetOrgCol") String parnetOrgCol,
			@Parameter(name="parentNumberCol",description="parentNumberCol",required=false) @RequestParam(value = "parentNumberCol", required = false) String parentNumberCol,
			@Parameter(name = "parentId", description = "测试模板 tmplId") @RequestParam(value = "parentId") String parentId,
			@Parameter(name="noId",description="noId") @RequestParam(value = "noId") Integer noId,
			@Parameter(name="middleTblname",description="middleTblname") @RequestParam(value = "middleTblname") String middleTblname,
			@Parameter(name="middleChilCol",description="middleChilCol") @RequestParam(value = "middleChilCol") String middleChilCol,
			@Parameter(name="middleParentCol",description="middleParentCol") @RequestParam(value = "middleParentCol") String middleParentCol,
			@Parameter(name="middleIdCol",description="middleIdCol") @RequestParam(value = "middleIdCol") String middleIdCol,
			@Parameter(name="middleNumberCol",description="middleNumberCol") @RequestParam(value = "middleNumberCol") String middleNumberCol) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
        }
		String flowNextId = null;
		try {
			flowNextId = tblAtonoNumberService
					.findRootNumberByParentIdLevel(chilNumberCol, chilTblName, chilParentCol, parentIdCol, parentTblName, parnetOrgCol,
							parentNumberCol, parentId, staff.getCurrentOrg().getOrgid(), noId, middleTblname, middleChilCol, middleParentCol,
							middleIdCol, middleNumberCol, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.reset();
		return new JsonBean(200, "success", flowNextId);
	}

	/**
	 * 保存 基本信息
	 *
	 * @param tblTestTemplate
	 * @return
	 */
	@OperationLog(
			success = "测试模板-新增保存信息成功",
			busType = "内控测试",
			fail = "测试模板-新增保存信息失败",
			operationType = OperationType.ADD,
			subType = "测试模板"
	)
	@PostMapping(value = "/csmb/def_tmpl_save")
	@Operation(summary = "测试模板-新增保存信息")
	public JsonBean def_tmpl_save(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "templeNumber", description = "测试模板编号", required = true) @RequestParam(value = "templeNumber", required = true) String templeNumber,
			@Parameter(name = "templename", description = "模板名称", required = true) @RequestParam(value = "templename", required = true) String templename,
			@Parameter(name = "memo", description = "备注", required = false) @RequestParam(value = "memo", required = false) String memo,
			@Parameter(name = "templeDesc", description = "模板说明", required = false) @RequestParam(value = "templeDesc", required = false) String templeDesc) {
		JsonBean jsonBean = null;
		try {
			TblTestTemplate tblTestTemplate = new TblTestTemplate();
			tblTestTemplate.setTempleNumber(templeNumber);
			tblTestTemplate.setTemplename(templename);
			tblTestTemplate.setMemo(memo);
			tblTestTemplate.setTempleDesc(templeDesc);
			jsonBean = tblTestTemplateService.save(token, tblTestTemplate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "测试模板-修改保存信息成功",
			busType = "内控测试",
			fail = "测试模板-修改保存信息失败",
			operationType = OperationType.UPDATE,
			subType = "测试模板"
	)
	@PostMapping(value = "/csmb/def_tmpl_modify")
	@Operation(summary = "测试模板-修改保存信息")
	public JsonBean def_tmpl_modify(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "testtemid", description = "测试模板主键", required = true) @RequestParam(value = "testtemid", required = true) BigDecimal testtemid,
			@Parameter(name = "templeNumber", description = "测试模板编号", required = true) @RequestParam(value = "templeNumber", required = true) String templeNumber,
			@Parameter(name = "templename", description = "模板名称", required = true) @RequestParam(value = "templename", required = true) String templename,
			@Parameter(name = "memo", description = "备注", required = false) @RequestParam(value = "memo", required = false) String memo,
			@Parameter(name = "templeDesc", description = "模板说明", required = false) @RequestParam(value = "templeDesc", required = false) String templeDesc) {
		JsonBean jsonBean = null;
		try {
			TblTestTemplate tblTestTemplate = new TblTestTemplate();
			tblTestTemplate.setTesttemid(testtemid);
			tblTestTemplate.setTempleNumber(templeNumber);
			tblTestTemplate.setTemplename(templename);
			tblTestTemplate.setMemo(memo);
			tblTestTemplate.setTempleDesc(templeDesc);
			jsonBean = tblTestTemplateService.modify(token, tblTestTemplate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	/**
	 * 通用获取设置的编号
	 *
	 * @param
	 */
	@OperationLog(
			success = "测试模板-通过Id获取信息查询成功",
			busType = "内控测试",
			fail = "测试模板-通过Id获取信息查询失败",
			operationType = OperationType.SELECT,
			subType = "测试模板"
	)
	@GetMapping(value = "/csmb/def_tmpl_info")
	@Operation(summary = "测试模板-通过Id获取信息")
	public JsonBean def_tmpl_info(HttpServletResponse response,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "testtemid", description = "测试模板主键", required = true) @RequestParam(value = "testtemid", required = true) BigDecimal testtemid)
			throws Exception {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblTestTemplateService.getInfo(token, testtemid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	/**
	 * 模板删除
	 *
	 * @param nodeId
	 * @return
	 */
	@OperationLog(
			success = "测试模板-删除成功",
			busType = "内控测试",
			fail = "测试模板-删除失败",
			operationType = OperationType.DELETE,
			subType = "测试模板"
	)
	@PostMapping(value = "/csmb/tempdelete")
	@Operation(summary = "测试模板-删除")
	public JsonBean tempdelete(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "templId", description = "测试模板主键", required = true) @RequestParam(value = "templId", required = true) Integer templId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblTestTemplateService.remove(token, templId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}


	/**
	 * 保存树结构数据
	 *
	 * @param type
	 * @param tmplId
	 * @return
	 */
	@OperationLog(
			success = "测试模板-测试类型新增保存成功",
			busType = "内控测试",
			fail = "测试模板-测试类型新增保存失败",
			operationType = OperationType.ADD,
			subType = "测试模板"
	)
	@PostMapping(value = "/csmb/type_save")
	@Operation(summary = "测试模板-测试类型新增保存")
	public JsonBean type_save(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "typename", description = "分类名称", required = true) @RequestParam(value = "typename", required = true) String typename,
			@Parameter(name = "typedesc", description = "分类描述", required = false) @RequestParam(value = "typedesc", required = false) String typedesc,
			@Parameter(name = "parentid", description = "父级分类主键", required = false) @RequestParam(value = "parentid", required = false) BigDecimal parentid,
			@Parameter(name = "testtempletaid", description = "测试模板主键", required = true) @RequestParam(value = "testtempletaid", required = true) BigDecimal testtempletaid,
			@Parameter(name = "typecode", description = "分类编码", required = true) @RequestParam(value = "typecode", required = true) String typecode) {
		JsonBean jsonBean = null;
		try {
			TblTesttemplType type = new TblTesttemplType();
			type.setParentid(parentid);
			type.setTesttempletaid(testtempletaid);
			type.setTypecode(typecode);
			type.setTypedesc(typedesc);
			type.setTypeid(parentid);
			type.setTypename(typename);

			jsonBean = tblTestTempTypeService.save(token, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "测试模板-测试类型修改保存成功",
			busType = "内控测试",
			fail = "测试模板-测试类型修改保存失败",
			operationType = OperationType.UPDATE,
			subType = "测试模板"
	)
	@PostMapping(value = "/csmb/type_modify")
	@Operation(summary = "测试模板-测试类型修改保存")
	public JsonBean type_modify(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "typeid", description = "测试模板分类主键", required = true) @RequestParam(value = "typeid", required = true) BigDecimal typeid,
			@Parameter(name = "typename", description = "分类名称", required = true) @RequestParam(value = "typename", required = true) String typename,
			@Parameter(name = "typedesc", description = "分类描述", required = false) @RequestParam(value = "typedesc", required = false) String typedesc,
			@Parameter(name = "parentid", description = "父级分类主键", required = false) @RequestParam(value = "parentid", required = false) BigDecimal parentid,
			@Parameter(name = "testtempletaid", description = "测试模板主键", required = true) @RequestParam(value = "testtempletaid", required = true) BigDecimal testtempletaid,
			@Parameter(name = "typecode", description = "分类编码", required = true) @RequestParam(value = "typecode", required = true) String typecode) {
		JsonBean jsonBean = null;
		try {
			TblTesttemplType type = new TblTesttemplType();
			type.setParentid(parentid);
			type.setTesttempletaid(testtempletaid);
			type.setTypecode(typecode);
			type.setTypedesc(typedesc);
			type.setTypeid(parentid);
			type.setTypename(typename);
			type.setTypeid(typeid);

			jsonBean = tblTestTempTypeService.modify(token, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "测试模板-测试类型删除成功",
			busType = "内控测试",
			fail = "测试模板-测试类型删除失败",
			operationType = OperationType.DELETE,
			subType = "测试模板"
	)
	@GetMapping(value = "/csmb/type_remove")
	@Operation(summary = "测试模板-测试类型删除")
	public JsonBean type_remove(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "typeid", description = "测试模板分类主键", required = true) @RequestParam(value = "typeid", required = true) BigDecimal typeid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblTestTempTypeService.remove(token, typeid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "测试模板-测试类型通过Id获取信息查询成功",
			busType = "内控测试",
			fail = "测试模板-测试类型通过Id获取信息查询失败",
			operationType = OperationType.SELECT,
			subType = "测试模板"
	)
	@GetMapping(value = "/csmb/type_info")
	@Operation(summary = "测试模板-测试类型通过Id获取信息")
	public JsonBean type_info(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "typeid", description = "测试模板分类主键", required = true) @RequestParam(value = "typeid", required = true) BigDecimal typeid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblTestTempTypeService.getInfo(token, typeid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "测试模板-测试类型通过tree数据查询成功",
			busType = "内控测试",
			fail = "测试模板-测试类型通过tree数据查询失败",
			operationType = OperationType.SELECT,
			subType = "测试模板"
	)
	@GetMapping(value = "/csmb/type_list")
	@Operation(summary = "测试模板-测试类型通过tree数据")
	public JsonBean type_list(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "testtempletaid", description = "测试模板主键", required = true) @RequestParam(value = "testtempletaid", required = true) BigDecimal testtempletaid,
			@Parameter(name = "parentId", description = "测试模板父级分类主键", required = false) @RequestParam(value = "parentId", required = false) BigDecimal parentId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblTestTempTypeService.getTreeList(token, parentId, testtempletaid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	/**
	 * 保存 基本信息
	 *
	 * @param tblTestTemplate
	 * @return
	 */
	@OperationLog(
			success = "测试模板-业务信息-新增保存信息成功",
			busType = "内控测试",
			fail = "测试模板-业务信息-新增保存信息失败",
			operationType = OperationType.ADD,
			subType = "测试模板"
	)
	@PostMapping(value = "/csmb/elementssave")
	@Operation(summary = "测试模板-业务信息-新增保存信息")
	public JsonBean elementssave(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "risktype", description = "风险描述", required = true) @RequestParam(value = "risktype", required = true) String risktype,
			@Parameter(name = "checkmethod", description = "检查方法", required = true) @RequestParam(value = "checkmethod", required = true) String checkmethod,
			@Parameter(name = "controlmethod", description = "控制方法  自动、手动", required = false) @RequestParam(value = "controlmethod", required = false) String controlmethod,
			@Parameter(name = "controltype", description = "控制类型  预防性、发现性", required = true) @RequestParam(value = "controltype", required = true) String controltype,
			@Parameter(name = "controlreq", description = "控制频率  随时、日、周、月度、季度、年度", required = true) @RequestParam(value = "controlreq", required = true) String controlreq,
			@Parameter(name = "material", description = "检查材料", required = false) @RequestParam(value = "material", required = false) String material,
			@Parameter(name = "elementcode", description = "编号", required = true) @RequestParam(value = "elementcode", required = true) String elementcode,
			@Parameter(name = "businessdesc", description = "业务描述", required = true) @RequestParam(value = "businessdesc", required = true) String businessdesc,
			@Parameter(name = "controltarget", description = "控制目标", required = true) @RequestParam(value = "controltarget", required = true) String controltarget,
			@Parameter(name = "controlmeasures", description = "控制措施", required = true) @RequestParam(value = "controlmeasures", required = true) String controlmeasures,
			@Parameter(name = "typeid", description = "测试模板分类主键", required = true) @RequestParam(value = "typeid", required = true) BigDecimal typeid,
			@Parameter(name = "templid", description = "测试模板主键", required = true) @RequestParam(value = "templid", required = true) BigDecimal templid) {
		JsonBean jsonBean = null;
		try {
			TblTestelement newEle = new TblTestelement();
			newEle.setBusinessdesc(businessdesc);
			newEle.setCheckmethod(checkmethod);
			newEle.setControlmeasures(controlmeasures);
			newEle.setControlmethod(controlmethod);
			newEle.setControlreq(controlreq);
			newEle.setControltarget(controltarget);
			newEle.setControltype(controltype);
			newEle.setElementcode(elementcode);
			newEle.setMaterial(material);
			newEle.setRisktype(risktype);
			newEle.setTemplid(templid);
			newEle.setTypeid(typeid);

			jsonBean = tblTestElementService.save(token, newEle);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "测试模板-业务信息-修改保存信息成功",
			busType = "内控测试",
			fail = "测试模板-业务信息-修改保存信息失败",
			operationType = OperationType.UPDATE,
			subType = "测试模板"
	)
	@PostMapping(value = "/csmb/elementmodify")
	@Operation(summary = "测试模板-业务信息-修改保存信息")
	public JsonBean elementmodify(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "risktype", description = "风险描述", required = true) @RequestParam(value = "risktype", required = true) String risktype,
			@Parameter(name = "checkmethod", description = "检查方法", required = true) @RequestParam(value = "checkmethod", required = true) String checkmethod,
			@Parameter(name = "controlmethod", description = "控制方法  自动、手动", required = false) @RequestParam(value = "controlmethod", required = false) String controlmethod,
			@Parameter(name = "controltype", description = "控制类型  预防性、发现性", required = true) @RequestParam(value = "controltype", required = true) String controltype,
			@Parameter(name = "controlreq", description = "控制频率  随时、日、周、月度、季度、年度", required = true) @RequestParam(value = "controlreq", required = true) String controlreq,
			@Parameter(name = "material", description = "检查材料", required = false) @RequestParam(value = "material", required = false) String material,
			@Parameter(name = "elementcode", description = "编号", required = true) @RequestParam(value = "elementcode", required = true) String elementcode,
			@Parameter(name = "businessdesc", description = "业务描述", required = true) @RequestParam(value = "businessdesc", required = true) String businessdesc,
			@Parameter(name = "controltarget", description = "控制目标", required = true) @RequestParam(value = "controltarget", required = true) String controltarget,
			@Parameter(name = "controlmeasures", description = "控制措施", required = true) @RequestParam(value = "controlmeasures", required = true) String controlmeasures,
			@Parameter(name = "typeid", description = "测试模板分类主键", required = true) @RequestParam(value = "typeid", required = true) BigDecimal typeid,
			@Parameter(name = "templid", description = "测试模板主键", required = true) @RequestParam(value = "templid", required = true) BigDecimal templid,
			@Parameter(name = "elementid", description = "业务信息主键", required = true) @RequestParam(value = "elementid", required = true) BigDecimal elementid) {
		JsonBean jsonBean = null;
		try {
			TblTestelement newEle = new TblTestelement();
			newEle.setBusinessdesc(businessdesc);
			newEle.setCheckmethod(checkmethod);
			newEle.setControlmeasures(controlmeasures);
			newEle.setControlmethod(controlmethod);
			newEle.setControlreq(controlreq);
			newEle.setControltarget(controltarget);
			newEle.setControltype(controltype);
			newEle.setElementcode(elementcode);
			newEle.setMaterial(material);
			newEle.setRisktype(risktype);
			newEle.setTemplid(templid);
			newEle.setTypeid(typeid);
			newEle.setElementid(elementid);
			jsonBean = tblTestElementService.modify(token, newEle);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "测试模板-业务信息-删除成功",
			busType = "内控测试",
			fail = "测试模板-业务信息-删除失败",
			operationType = OperationType.DELETE,
			subType = "测试模板"
	)
	@GetMapping(value = "/csmb/elementremove")
	@Operation(summary = "测试模板-业务信息-删除")
	public JsonBean elementremove(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "elementid", description = "业务信息主键", required = true) @RequestParam(value = "elementid", required = true) BigDecimal elementid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblTestElementService.remvoe(token, elementid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "测试模板-业务信息-列表分页数据查询成功",
			busType = "内控测试",
			fail = "测试模板-业务信息-列表分页数据查询失败",
			operationType = OperationType.SELECT,
			subType = "测试模板"
	)
	@GetMapping(value = "/csmb/elementList")
	@Operation(summary = "测试模板-业务信息-列表分页数据")
	public JsonBean elementList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "分页当前行数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "typeid", description = "测试模板分类主键", required = false) @RequestParam(value = "typeid", required = false) BigDecimal typeid,
			@Parameter(name = "templid", description = "测试模板主键", required = true) @RequestParam(value = "templid", required = true) BigDecimal templid,
			@Parameter(name = "elementcode", description = "编号", required = false) @RequestParam(value = "elementcode", required = false) String elementcode,
			@Parameter(name = "businessdesc", description = "业务描述", required = false) @RequestParam(value = "businessdesc", required = false) String businessdesc) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblTestElementService.getPageInfo(token, pageNumber, pageSize, typeid, templid, elementcode, businessdesc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "测试模板-业务信息-列表分页数据查询成功",
			busType = "内控测试",
			fail = "测试模板-业务信息-列表分页数据查询失败",
			operationType = OperationType.SELECT,
			subType = "测试模板"
	)
	@GetMapping(value = "/csmb/elementInfo")
	@Operation(summary = "测试模板-业务信息-列表分页数据")
	public JsonBean elementInfo(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "elementid", description = "业务信息主键", required = true) @RequestParam(value = "elementid", required = true) BigDecimal elementid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblTestElementService.getInfo(token, elementid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}


	/**
	 * 测试方案-计划查找
	 *
	 * @param plannumber
	 * @param planname
	 * @param planstatus
	 * @param starttime_min
	 * @param starttime_max
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@OperationLog(
			success = "测试方案-计划查找查询成功",
			busType = "内控测试",
			fail = "测试方案-计划查找查询失败",
			operationType = OperationType.SELECT,
			subType = "测试模板"
	)
	@RequestMapping(value = "/nkcs/plan/ctrltest_plan_list")
	@Operation(summary = "测试方案-计划查找")
	public JsonBean nkcs_ctrltest_plan_list(
			@Parameter(name = "plannumber", description = "计划编号", required = false) @RequestParam(value = "plannumber", required = false) String plannumber,
			@Parameter(name = "planname", description = "计划名称", required = false) @RequestParam(value = "planname", required = false) String planname,
			@Parameter(name = "planstatus", description = "计划状态", required = false) @RequestParam(value = "planstatus", required = false) String planstatus,
			@Parameter(name = "starttime_min", description = "最小开始时间", required = false) @RequestParam(value = "starttime_min", required = false) Date starttime_min,
			@Parameter(name = "starttime_max", description = "最大开始时间", required = false) @RequestParam(value = "starttime_max", required = false) Date starttime_max,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "分页当前行数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "1") Integer pageSize,
			@RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
        }
		JsonBean jsonBean = null;
		try {
			jsonBean = tblTestPlanService.selectList(plannumber, planname, planstatus, starttime_min, starttime_max, pageNumber, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}


	/**
	 * 计划保存
	 *
	 * @param tblTestplan
	 * @return
	 */
	@OperationLog(
			success = "计划保存成功",
			busType = "内控测试",
			fail = "计划保存失败",
			operationType = OperationType.ADD,
			subType = "测试模板"
	)
	@RequestMapping(value = "/nkcs/plan/saves")
	@Operation(summary = "计划保存")
	public JsonBean nkcs_csjhSave(@RequestBody TblTestplan tblTestplan, @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
        }
		JsonBean jsonBean = null;
		try {
			tblTestPlanService.save(tblTestplan);
			jsonBean = ResponseFormat.retParam(0, 10002, "success");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	/**
	 * 新增计划
	 *
	 * @param tblTestplan
	 * @return
	 */
	@OperationLog(
			success = "新增计划成功",
			busType = "内控测试",
			fail = "新增计划失败",
			operationType = OperationType.ADD,
			subType = "测试模板"
	)
	@RequestMapping(value = "/nkcs/plan/add")
	@Operation(summary = "新增计划")
	public JsonBean nkcs_ctrltest_plan_add(@RequestBody TblTestplan tblTestplan, @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
        }
		JsonBean jsonBean = null;
		try {
			tblTestPlanService.save(tblTestplan);
			jsonBean = ResponseFormat.retParam(0, 10002, "success");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	/**
	 * 计划删除
	 *
	 * @param testplanid
	 * @return
	 */
	@OperationLog(
			success = "计划删除成功",
			busType = "内控测试",
			fail = "计划删除失败",
			operationType = OperationType.DELETE,
			subType = "测试模板"
	)
	@RequestMapping(value = "/nkcs/plan/delete")
	@Operation(summary = "计划删除")
	public JsonBean nkcs_ctrltest_plan_delete(
			@Parameter(name = "testplanid", description = "计划编号", required = false) @RequestParam(value = "testplanid", required = false) BigDecimal testplanid,
			@RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
        }

		JsonBean jsonBean = null;
		try {
			tblTestPlanService.deleteById(testplanid);
			jsonBean = ResponseFormat.retParam(0, 10002, "success");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	/**
	 * 计划修改/分配
	 *
	 * @param testplanid
	 * @return
	 */
	@OperationLog(
			success = "计划修改/分配成功",
			busType = "内控测试",
			fail = "计划修改/分配失败",
			operationType = OperationType.UPDATE,
			subType = "测试模板"
	)
	@RequestMapping(value = "/nkcs/plan/isStatus")
	@Operation(summary = "计划修改/分配")
	public JsonBean zgfp_fqStatus(
			@Parameter(name = "testplanid", description = "计划编号", required = false) @RequestParam(value = "testplanid", required = false) BigDecimal testplanid,
			@RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
        }

		JsonBean jsonBean = null;
		try {
			jsonBean = ResponseFormat.retParam(0, 10002, tblTestPlanService.updateById(testplanid));


			jsonBean = tblTestPlanService.updateById(testplanid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	/**
	 * 启动测试方案
	 *
	 * @param testplanid
	 * @return
	 */
	@OperationLog(
			success = "启动测试方案成功",
			busType = "内控测试",
			fail = "启动测试方案失败",
			operationType = OperationType.UPDATE,
			subType = "测试模板"
	)
	@RequestMapping(value = "/nkcs/plan/start_isStatus")
	@Operation(summary = "启动测试方案")
	public JsonBean start_isStatus(
			@Parameter(name = "testplanid", description = "测试计划id", required = false) @RequestParam(value = "testplanid", required = false) BigDecimal testplanid,
			@RequestHeader("token") String token) throws Exception {

		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
        }
		JsonBean jsonBean = null;
		try {
			jsonBean = tblTestPlanService.updateById(testplanid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	/**
	 * 测试任务--查看测试任务
	 *
	 * @param testplanid
	 * @return
	 */
	@OperationLog(
			success = "测试任务--查看测试任务成功",
			busType = "内控测试",
			fail = "测试任务--查看测试任务失败",
			operationType = OperationType.SELECT,
			subType = "测试模板"
	)
	@RequestMapping(value = "/csrw/gettree")
	@Operation(summary = "测试任务--查看测试任务")
	public JsonBean csrw_getTree(
			@Parameter(name = "testplanid", description = "测试计划id", required = false) @RequestParam(value = "testplanid", required = false) BigDecimal testplanid,
			@RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
        }
		JsonBean jsonBean = null;
		try {
			jsonBean = ResponseFormat.retParam(1, 200, tblTestPlanService.getById(testplanid));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}


	/**
	 * 测试任务--修改
	 *
	 * @param testplanid
	 * @return
	 */
	@OperationLog(
			success = "测试任务--修改成功",
			busType = "内控测试",
			fail = "测试任务--修改失败",
			operationType = OperationType.UPDATE,
			subType = "测试模板"
	)
	@RequestMapping(value = "/csrw/update")
	public JsonBean csrw_update(
			@Parameter(name = "testplanid", description = "测试计划id", required = false) @RequestParam(value = "testplanid", required = false) BigDecimal testplanid,
			@RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
        }
		JsonBean jsonBean = null;
		try {

			jsonBean = tblTestPlanService.updateById(testplanid);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	/**
	 * 测试任务--全部提交
	 *
	 * @param plans
	 * @return
	 */
	@OperationLog(
			success = "测试任务--全部提交成功",
			busType = "内控测试",
			fail = "测试任务--全部提交失败",
			operationType = OperationType.ADD,
			subType = "测试模板"
	)
	@RequestMapping(value = "/csrw/saveAll")
	public JsonBean csrw_saveAll(List<String> plans, @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
        }
		JsonBean jsonBean = null;
		try {
			jsonBean = tblTestPlanService.saveAll(plans);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	/**
	 * 测试跟踪-查找
	 *
	 * @param plannumber
	 * @param planname
	 * @param planstatus
	 * @param starttime_min
	 * @param starttime_max
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@OperationLog(
			success = "测试跟踪-查找成功",
			busType = "内控测试",
			fail = "测试跟踪-查找失败",
			operationType = OperationType.SELECT,
			subType = "测试模板"
	)
	@RequestMapping(value = "/nkcs/impl/control_test_track_list")
	public JsonBean control_test_track_list(
			@Parameter(name = "plannumber", description = "计划编号", required = false) @RequestParam(value = "plannumber", required = false) String plannumber,
			@Parameter(name = "planname", description = "计划名称", required = false) @RequestParam(value = "planname", required = false) String planname,
			@Parameter(name = "planstatus", description = "计划状态", required = false) @RequestParam(value = "planstatus", required = false) String planstatus,
			@Parameter(name = "starttime_min", description = "最小开始时间", required = false) @RequestParam(value = "starttime_min", required = false) Date starttime_min,
			@Parameter(name = "starttime_max", description = "最大开始时间", required = false) @RequestParam(value = "starttime_max", required = false) Date starttime_max,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "分页当前行数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "1") Integer pageSize,
			@RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
        }
		JsonBean jsonBean = null;
		try {
			jsonBean = tblTestPlanService.selectList(plannumber, planname, planstatus, starttime_min, starttime_max, pageNumber, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}


	@OperationLog(
			success = "nkcs/statistic/result_count_list成功",
			busType = "内控测试",
			fail = "nkcs/statistic/result_count_list失败",
			operationType = OperationType.SELECT,
			subType = "测试模板"
	)
	@RequestMapping(value = "/nkcs/statistic/result_count_list")
	public JsonBean nkcs_result_count_list(
			@Parameter(name = "planname", description = "计划名称", required = false) @RequestParam(value = "planname", required = false) String planname,
			@Parameter(name = "planyear", description = "计划名称", required = false) @RequestParam(value = "planyear", required = false) String planyear,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "分页当前行数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
        }
		JsonBean jsonBean = null;
		try {
			jsonBean = tblTestPlanService.select(planname, planyear, pageNumber, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
}
