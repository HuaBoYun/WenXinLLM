package com.huabo.compliance.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.compliance.entity.TblAttachment;
import com.huabo.compliance.entity.TblTestelement;
import com.huabo.compliance.entity.TblTestplan;
import com.huabo.compliance.entity.TblTesttask;
import com.huabo.compliance.entity.Tree;
import com.huabo.compliance.service.CsfaService;
import com.huabo.compliance.service.CsjgService;
import com.huabo.compliance.service.CsrwService;
import com.huabo.compliance.service.ITblAttachmentService;
import com.huabo.compliance.service.ITblStaffService;
import com.huabo.compliance.service.ITblTestplanService;
import com.huabo.compliance.service.ITblTesttaskAttService;
import com.huabo.compliance.service.ITblTesttaskService;
import com.huabo.compliance.service.TblAssessService;
import com.huabo.compliance.service.TblTestElementService;
import com.huabo.compliance.util.ConstClass;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author：yhr
 * @date:2022-09-15 13:00
 * @description:
 */
@RestController
@Slf4j
@Tag(name="内控测试-测试结果",description="内控测试-测试结果")
public class CsjgController {


	@Resource
	ITblTestplanService testplanService;
	@Autowired
	ITblStaffService iTblStaffService;


	@Autowired
	TblAssessService tblAssessService;

	@Resource
	CsfaService csfaService;
	@Resource
	CsjgService csjgService;

	@Resource
	TblTestElementService tblTestelementService;
	@Resource
	CsrwService csrwService;

	@Resource
	ITblTesttaskService testtaskService;

	@Resource
	ITblAttachmentService attachmentService;
	@Resource
	ITblTesttaskAttService testtaskAttService;
	
	@Resource
	private UserProvider userProvider;

	@OperationLog(
			success = "测试结果-主页查询成功",
			busType = "内控测试",
			fail = "测试结果-主页查询失败",
			operationType = OperationType.SELECT,
			subType = "测试结果"
	)
	@Operation(summary = "测试结果-主页")
	@GetMapping(value = "/nkcs/statistic/result_count_list")
	public JsonBean nkcs_result_count_list(
			@Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "分页当前行数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "planname", description = "planname") @RequestParam(value = "planname", required = false) String planname,
			@Parameter(name = "year", description = "year") @RequestParam(value = "year", required = false) String year,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

		TblTestplan plan = new TblTestplan();
		if (StringUtils.isNotBlank(planname)) {
			plan.setPlanname(planname);
		}

		if (StringUtils.isNotBlank(year)) {
			plan.setPlanyear(year);
		}

		//        TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 选则的机构
		//        TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");

		boolean bool = csfaService.isSJByOrgId(user.getLinkDetp().getOrgid().toString());
		IPage<Map<String, Object>> iPage;
		if (bool) {
			iPage = csjgService.findAllCSHZ(plan, pageNumber, user.getCurrentOrg().getOrgid(), pageSize);
		} else {
			iPage = csjgService.findAllWCSjCSHZ(plan, pageNumber, user.getStaffid(), pageSize);
		}


		Map<String, Object> mv = new HashMap<>();
		mv.put("pageBean", iPage);
		mv.put("plan", plan);
		return new JsonBean(200, "success", mv);


	}


	@OperationLog(
			success = "测试结果-主页列表点击控制数-左侧树(非json,子节点单独node)查询成功",
			busType = "内控测试",
			fail = "测试结果-主页列表点击控制数-左侧树(非json,子节点单独node)查询失败",
			operationType = OperationType.SELECT,
			subType = "测试结果"
	)
	@Operation(summary = "测试结果-主页列表点击控制数-左侧树(非json,子节点单独node)")
	@GetMapping(value = "/csjg/gettree_all")
	public JsonBean gettree_all(@Parameter(name = "planid", description = "testplanid") @RequestParam(value = "planid") String planid,

			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }


		Map<String, Object> mv = new LinkedHashMap<>();
		mv.put("planid", planid);

		if (StringUtils.isNotBlank(planid)) {
			TblTestplan plan = this.testplanService.getById(new BigDecimal(planid));

			if (plan != null) {
				mv.put("templId", plan.getTesttemid());
				List<Tree> tree = this.csfaService.getTreeC(plan.getTesttemid());
				mv.put("tree", tree);

			}
		}

		return new JsonBean(200, "success", mv);
	}

	@OperationLog(
			success = "测试结果-主页列表点击有效数-左侧树(json树)查询成功",
			busType = "内控测试",
			fail = "测试结果-主页列表点击有效数-左侧树(json树)查询失败",
			operationType = OperationType.SELECT,
			subType = "测试结果"
	)
	@Operation(summary = "测试结果-主页列表点击有效数-左侧树(json树)")
	@GetMapping(value = "/csjg/gettree_yx")
	public JsonBean gettree_yx(@Parameter(name = "planid", description = "testplanid") @RequestParam(value = "planid") String planid,

			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		Map<String, Object> mv = new LinkedHashMap<>();
		mv.put("planid", planid);
		if (StringUtils.isNotBlank(planid)) {
			TblTestplan plan = this.testplanService.getById(new BigDecimal(planid));

			if (plan != null) {
				mv.put("templId", plan.getTesttemid());
				List<Tree> tree = this.csjgService
						.getTreeLeftyx(plan.getTesttemid().toString(), planid, "/nbkz/csjg/def_list_yx", user.getStaffid().toString());
				mv.put("tree", tree);

			}
		}
		return new JsonBean(200, "success", mv);
	}

	@OperationLog(
			success = "测试结果-主页列表点击无效数-左侧树(json树)查询成功",
			busType = "内控测试",
			fail = "测试结果-主页列表点击无效数-左侧树(json树)查询失败",
			operationType = OperationType.SELECT,
			subType = "测试结果"
	)
	@Operation(summary = "测试结果-主页列表点击无效数-左侧树(json树)")
	@GetMapping(value = "/csjg/gettree_wx")
	public JsonBean gettree_wx(@Parameter(name = "planid", description = "testplanid") @RequestParam(value = "planid") String planid,

			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		Map<String, Object> mv = new LinkedHashMap<>();
		mv.put("planid", planid);
		if (StringUtils.isNotBlank(planid)) {
			TblTestplan plan = this.testplanService.getById(new BigDecimal(planid));

			if (plan != null) {
				mv.put("templId", plan.getTesttemid());
				List<Tree> tree = this.csjgService
						.getTreeLeftwx(plan.getTesttemid().toString(), planid, "/nbkz/csjg/def_list_wx", user.getStaffid().toString());
				mv.put("tree", tree);

			}
		}
		return new JsonBean(200, "success", mv);
	}

	@OperationLog(
			success = "测试结果-主页列表点击不适用数-左侧树(json树)查询成功",
			busType = "内控测试",
			fail = "测试结果-主页列表点击不适用数-左侧树(json树)查询失败",
			operationType = OperationType.SELECT,
			subType = "测试结果"
	)
	@Operation(summary = "测试结果-主页列表点击不适用数-左侧树(json树)")
	@GetMapping(value = "/csjg/gettree_bxy")
	public JsonBean gettree_bxy(@Parameter(name = "planid", description = "testplanid") @RequestParam(value = "planid") String planid,

			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		Map<String, Object> mv = new LinkedHashMap<>();
		mv.put("planid", planid);
		if (StringUtils.isNotBlank(planid)) {
			TblTestplan plan = this.testplanService.getById(new BigDecimal(planid));

			if (plan != null) {
				mv.put("templId", plan.getTesttemid());
				List<Tree> tree = this.csjgService
						.getTreeLeftbxy(plan.getTesttemid().toString(), planid, "/nbkz/csjg/def_list_bxy", user.getStaffid().toString());
				mv.put("tree", tree);

			}
		}
		return new JsonBean(200, "success", mv);
	}

	@OperationLog(
			success = "测试结果-控制数-点击左侧树(非json,子节点单独node)-右侧列表查询成功",
			busType = "内控测试",
			fail = "测试结果-控制数-点击左侧树(非json,子节点单独node)-右侧列表查询失败",
			operationType = OperationType.SELECT,
			subType = "测试结果"
	)
	@Operation(summary = "测试结果-控制数-点击左侧树(非json,子节点单独node)-右侧列表")
	@GetMapping(value = "/csjg/def_list_all")
	public JsonBean def_list_all(
			@Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "node", description = "左侧树id") @RequestParam(value = "node") String node,
			@Parameter(name = "templId", description = "templId") @RequestParam(value = "templId") String templId,
			@Parameter(name = "planid", description = "testplanid") @RequestParam(value = "planid") String planid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

		IPage<Map<String, Object>> page = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
		if (StringUtils.isNotBlank(templId) && StringUtils.isNotBlank(node)) {
			page = this.csjgService.fingByTreeCSRW(node, templId, planid, pageNumber);
		}
		Map<String, Object> mv = new LinkedHashMap<>();
		mv.put("node", node);
		mv.put("templId", templId);
		mv.put("planid", planid);
		mv.put("pageBean", page);
		return new JsonBean(200, "success", mv);
	}

	/**
	 * 测试汇总-详细结果
	 *
	 * @param pageNumber
	 * @param planid
	 * @return
	 */
	@OperationLog(
			success = "测试结果-主页列表-点击详细结果查询成功",
			busType = "内控测试",
			fail = "测试结果-主页列表-点击详细结果查询失败",
			operationType = OperationType.SELECT,
			subType = "测试结果"
	)
	@Operation(summary = "测试结果-主页列表-点击详细结果")
	@GetMapping(value = "/cshz/xxjg_list")
	public JsonBean xxjg_list(
			@Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name="planid",description="planid") @RequestParam(value = "planid") BigDecimal planid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		IPage<Map<String, Object>> page = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
		if (planid != null) {
			page = csjgService.findAllnoSjResult(planid, pageNumber);
		}

		Map<String, Object> mv = new LinkedHashMap<>();

		mv.put("planid", planid);
		mv.put("pageBean", page);
		return new JsonBean(200, "success", mv);
	}

	@OperationLog(
			success = "测试结果-有效树-点击左侧树(json树)-右侧列表查询成功",
			busType = "内控测试",
			fail = "测试结果-有效树-点击左侧树(json树)-右侧列表查询失败",
			operationType = OperationType.SELECT,
			subType = "测试结果"
	)
	@Operation(summary = "测试结果-有效树-点击左侧树(json树)-右侧列表")
	@GetMapping(value = "/csjg/def_list_yx")
	public JsonBean def_list_yx(
			@Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "node", description = "左侧树id") @RequestParam(value = "node") String node,
			@Parameter(name = "templId", description = "templId") @RequestParam(value = "templId", required = false) String templId,
			@Parameter(name = "planid", description = "testplanid") @RequestParam(value = "planid") String planid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

		IPage<Map<String, Object>> page = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);

		page = this.csjgService.fingByTreeCSJGY(node, templId, planid, pageNumber);

		Map<String, Object> mv = new LinkedHashMap<>();
		mv.put("node", node);
		mv.put("templId", templId);
		mv.put("planid", planid);
		mv.put("pageBean", page);
		return new JsonBean(200, "success", mv);
	}


	@OperationLog(
			success = "测试结果-无效数-点击左侧树(json树)-右侧列表查询成功",
			busType = "内控测试",
			fail = "测试结果-无效数-点击左侧树(json树)-右侧列表查询失败",
			operationType = OperationType.SELECT,
			subType = "测试结果"
	)
	@Operation(summary = "测试结果-无效数-点击左侧树(json树)-右侧列表")
	@GetMapping(value = "/csjg/def_list_wx")
	public JsonBean def_list_wx(
			@Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "node", description = "左侧树id") @RequestParam(value = "node") String node,
			@Parameter(name = "templId", description = "templId") @RequestParam(value = "templId") String templId,
			@Parameter(name = "planid", description = "testplanid") @RequestParam(value = "planid") String planid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

		IPage<Map<String, Object>> page = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
		if (StringUtils.isNotBlank(templId) && StringUtils.isNotBlank(node)) {
			page = this.csjgService.fingByTreeCSJGW(node, templId, planid, pageNumber);
		}
		Map<String, Object> mv = new LinkedHashMap<>();
		mv.put("node", node);
		mv.put("templId", templId);
		mv.put("planid", planid);
		mv.put("pageBean", page);
		return new JsonBean(200, "success", mv);
	}

	@OperationLog(
			success = "测试结果-不适用数-点击左侧树(json树)-右侧列表查询成功",
			busType = "内控测试",
			fail = "测试结果-不适用数-点击左侧树(json树)-右侧列表查询失败",
			operationType = OperationType.SELECT,
			subType = "测试结果"
	)
	@Operation(summary = "测试结果-不适用数-点击左侧树(json树)-右侧列表")
	@GetMapping(value = "/csjg/def_list_bxy")
	public JsonBean def_list_bxy(
			@Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "node", description = "左侧树id") @RequestParam(value = "node") String node,
			@Parameter(name = "templId", description = "templId") @RequestParam(value = "templId") String templId,
			@Parameter(name = "planid", description = "testplanid") @RequestParam(value = "planid") String planid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

		IPage<Map<String, Object>> page = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
		if (StringUtils.isNotBlank(templId) && StringUtils.isNotBlank(node)) {
			page = this.csjgService.fingByTreeCSJGB(node, templId, planid, pageNumber);
		}
		Map<String, Object> mv = new LinkedHashMap<>();
		mv.put("node", node);
		mv.put("templId", templId);
		mv.put("planid", planid);
		mv.put("pageBean", page);
		return new JsonBean(200, "success", mv);
	}


	@OperationLog(
			success = "测试结果-点击数量-点击左侧树-右侧列表-点击编号查询测试结果((通用))查询成功",
			busType = "内控测试",
			fail = "测试结果-点击数量-点击左侧树-右侧列表-点击编号查询测试结果((通用))查询失败",
			operationType = OperationType.SELECT,
			subType = "测试结果"
	)
	@GetMapping(value = "/csrw/control_test_impl_detail")
	@ResponseBody
	@Operation(summary = "测试结果-点击数量-点击左侧树-右侧列表-点击编号查询测试结果((通用))")
	public JsonBean control_test_impl_detail(
			@Parameter(name="elementId",description="elementId") @RequestParam(value = "elementId") BigDecimal ementid,
			@Parameter(name="planid",description="planid") @RequestParam(value = "planid") BigDecimal planid,
			@Parameter(name="node",description="node只做回传,可以不传",required=false) @RequestParam(value = "node", required = false) String node,
			@Parameter(name="templId",description="templId只做回传,可以不传",required=false) @RequestParam(value = "templId", required = false) String templId,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		Map<String, Object> mv = new LinkedHashMap<>();
		if (ementid != null && planid != null) {
			QueryWrapper<TblTesttask> qw = new QueryWrapper<>();
			qw.eq("elementid", ementid).eq("planid", planid);


			TblTesttask task = this.testtaskService.getOne(qw);
			TblTestelement element = this.tblTestelementService.getById(ementid);
			List<TblAttachment> atts = attachmentService.findtTblAttachmentByTask(task.getTesttaskid());
			mv.put("element", element);
			mv.put("task", task);
			mv.put("atts", atts);
		}

		mv.put("node", node);
		mv.put("templId", templId);
		mv.put("planid", planid);


		return new JsonBean(200, "success", mv);
	}

	@OperationLog(
			success = "测试结果-详细结果列表-点击数量-左侧树查询成功",
			busType = "内控测试",
			fail = "测试结果-详细结果列表-点击数量-左侧树查询失败",
			operationType = OperationType.SELECT,
			subType = "测试结果"
	)
	@Operation(summary = "测试结果-详细结果列表-点击数量-左侧树")
	@GetMapping(value = "/cshz/gettree")
	public JsonBean cshz_getTree(
			@Parameter(name="planid",description="testplanid") @RequestParam(value = "planid") String planid,
			@Parameter(name="userid",description="CPUSERID") @RequestParam(value = "userid") String userid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		Map<String, Object> mv = new LinkedHashMap<>();
		mv.put("planid", planid);
		mv.put("userid", userid);
		if (StringUtils.isNotBlank(planid)) {
			TblTestplan plan = this.testplanService.getById(new BigDecimal(planid));

			if (plan != null) {
				mv.put("templId", plan.getTesttemid());
				List<Tree> tree = this.csjgService.getTreeCSHZByChilds(plan.getTesttemid().toString(), userid, planid);
				mv.put("tree", tree);

			}
		}
		return new JsonBean(200, "success", mv);

	}


	@OperationLog(
			success = "测试结果-详细结果列表-点击数量-右侧列表查询成功",
			busType = "内控测试",
			fail = "测试结果-详细结果列表-点击数量-右侧列表查询失败",
			operationType = OperationType.SELECT,
			subType = "测试结果"
	)
	@Operation(summary = "测试结果-详细结果列表-点击数量-右侧列表")
	@GetMapping(value = "/cshz/def_list")
	public JsonBean cshz_gzdg_def_list(
			@Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "node", description = "左侧树id") @RequestParam(value = "node") String node,
			@Parameter(name = "templId", description = "templId") @RequestParam(value = "templId") String templId,
			@Parameter(name = "planid", description = "testplanid") @RequestParam(value = "planid") String planid,
			@Parameter(name = "userid", description = "CPUSERID") @RequestParam(value = "userid") String userid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

		IPage<Map<String, Object>> page = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
		if (StringUtils.isNotBlank(templId) && StringUtils.isNotBlank(node)) {
			page = this.csjgService.fingByTreeCSHZByUser(node, templId, planid, userid, pageNumber);
		}
		Map<String, Object> mv = new LinkedHashMap<>();
		mv.put("node", node);
		mv.put("templId", templId);
		mv.put("planid", planid);
		mv.put("userid", userid);
		mv.put("pageBean", page);

		return new JsonBean(200, "success", mv);
	}


	/**
	 * 测试汇总-保存退回意见
	 *
	 * @param
	 * @param planid
	 * @return
	 */
	@OperationLog(
			success = "测试结果-详细结果列表-点击数量-右侧列表-退回-保存成功",
			busType = "内控测试",
			fail = "测试结果-详细结果列表-点击数量-右侧列表-退回-保存失败",
			operationType = OperationType.ADD,
			subType = "测试结果"
	)
	@Operation(summary = "测试结果-详细结果列表-点击数量-右侧列表-退回-保存")
	@PostMapping(value = "/cshz/task_save")
	public JsonBean cshz_task_save(@Parameter(name = "userid", description = "userid") @RequestParam(value = "userid") String userid,
			@Parameter(name = "planid", description = "planid") @RequestParam(value = "planid") String planid,
			@Parameter(name = "taskid", description = "TESTTASKID来自行内数据") @RequestParam(value = "taskid") String taskid,
			@Parameter(name = "proposal", description = "退回意见") @RequestParam(value = "proposal", required = false) String proposal,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		if (StringUtils.isNotBlank(taskid)) {

			this.csjgService.saveBack(taskid, proposal, planid, userid);


		}
		Map<String, Object> mv = new LinkedHashMap<>();

		mv.put("taskid", taskid);
		mv.put("planid", planid);
		mv.put("userid", userid);

		return new JsonBean(200, "success", mv);
	}

	@OperationLog(
			success = "测试结果-详细结果列表-导出excel成功",
			busType = "内控测试",
			fail = "测试结果-详细结果列表-导出excel失败",
			operationType = OperationType.EXPORT,
			subType = "测试结果"
	)
	@Operation(summary = "测试结果-详细结果列表-导出excel")
	@GetMapping(value = "/nkcs/result_export", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String sjfx_export(@Parameter(name = "planid", description = "planid") @RequestParam(value = "planid") String planid, HttpServletResponse response) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return JsonBean.error("用户已失效");
        }
		log.info("内控合规---内控测试---测试结果汇总---导出Excel");
		response.setContentType("application/octet-stream;charset=UTF-8");
		try {
			String name = new String("测试结果".getBytes(), "iso-8859-1");
			String date = String.valueOf(System.currentTimeMillis());
			String fileName = "csjg.xlsx";
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			ServletOutputStream outputStream = response.getOutputStream();
			//查询所有测试结果
			List<Object[]> objList = this.csjgService.findElementByPlanid(planid);
			String[] titles = {"测试项目", "编号", "业务描述", "风险描述", "控制目标", "控制措施", "检查方法", "测试程序", "测试结果", "测试有效性"};
			ImportOrExportExcelUtil.exportExcel(titles, objList, outputStream, null);
		} catch (Exception e) {
			log.info("内控合规---内控测试---测试结果汇总---导出Excel失败");
		}
		return null;
	}

}
