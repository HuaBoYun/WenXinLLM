package com.huabo.compliance.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.compliance.entity.TblOrganization;
import com.huabo.compliance.entity.TblTestelement;
import com.huabo.compliance.entity.TblTestplan;
import com.huabo.compliance.entity.TblTesttask;
import com.huabo.compliance.entity.TblTesttempleVo;
import com.huabo.compliance.entity.Tree;
import com.huabo.compliance.service.CsfaService;
import com.huabo.compliance.service.ITblStaffService;
import com.huabo.compliance.service.ITblTestplanService;
import com.huabo.compliance.service.ITblTesttaskService;
import com.huabo.compliance.service.TblAssessService;
import com.huabo.compliance.service.TblTestElementService;
import com.huabo.compliance.util.ConstClass;
import com.huabo.compliance.util.DateUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Tag(name="内控测试-测试方案",description="内控测试-测试方案")
public class CsfaController {

	@Resource
	ITblTestplanService testplanService;
	@Autowired
	ITblStaffService iTblStaffService;


	@Autowired
	TblAssessService tblAssessService;

	@Resource
	CsfaService csfaService;

	@Resource
	ITblTesttaskService ITblTesttaskService;

	@Resource
	TblTestElementService tblTestelementService;
	
	@Resource
	private UserProvider userProvider;

	@OperationLog(
			success = "测试方案-主页查询成功",
			busType = "内控测试",
			fail = "测试方案-主页查询失败",
			operationType = OperationType.SELECT,
			subType = "测试方案"
	)
	@GetMapping(value = "/nkcs/plan/ctrltest_plan_list")
	@Operation(summary = "测试方案-主页")
	public JsonBean nkcs_ctrltest_plan_list(
			@Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "plannumber", description = "plannumber") @RequestParam(value = "plannumber", required = false) String plannumber,
			@Parameter(name = "planname", description = "planname") @RequestParam(value = "planname", required = false) String planname,
			@Parameter(name = "starttime_min", description = "starttime_min") @RequestParam(value = "starttime_min", required = false) String starttime_min,
			@Parameter(name = "starttime_max", description = "starttime_max") @RequestParam(value = "starttime_max", required = false) String starttime_max,
			@Parameter(name = "status", description = "status") @RequestParam(value = "status", required = false) String status,
			@Parameter(name = "year", description = "year") @RequestParam(value = "year", required = false) String year,

			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
		TblTestplan plan = new TblTestplan();
		if (planname != null && planname.length() > 0) {
			plan.setPlanname(planname);
		}
		if (plannumber != null && plannumber.length() > 0) {
			plan.setPlannumber(plannumber);
		}
		if (status != null && status.length() > 0) {
			plan.setPlanstatus(status);
		}
		if (year != null && year.length() > 0) {
			plan.setPlanyear(year);
		}
		boolean bool = csfaService.isSJByOrgId(user.getLinkDetp().getOrgid().toString());
		IPage<TblTestplan> iPage;
		if (bool) {
			iPage = csfaService.findAll(plan, pageNumber, starttime_min, starttime_max, user.getCurrentOrg().getOrgid(), pageSize);
		} else {
			iPage = csfaService.findAllnoSj(plan, pageNumber, starttime_min, starttime_max, user.getStaffid().toString(), pageSize);
		}
		Map<String, Object> mv = new HashMap<>();
		mv.put("pageBean", iPage);
		mv.put("starttime_min", starttime_min);
		mv.put("starttime_max", starttime_max);
		mv.put("plan", plan);
		return ResponseFormat.retParam(1, 200, mv);
	}


	@OperationLog(
			success = "测试方案-主页-点击编号/点击修改 获取详情查询成功",
			busType = "内控测试",
			fail = "测试方案-主页-点击编号/点击修改 获取详情查询失败",
			operationType = OperationType.SELECT,
			subType = "测试方案"
	)
	@GetMapping(value = "/nkcs/plan/detail")
	@Operation(summary = "测试方案-主页-点击编号/点击修改 获取详情")
	public JsonBean nkcs_ctrltest_plan_detail(
			@Parameter(name = "selectProjectid", description = "testplanid") @RequestParam(value = "selectProjectid") BigDecimal selectProjectid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		Map<String, Object> mv = new HashMap<>();
		mv.put("test", csfaService.queryOneTestPlanVo(selectProjectid));
		return ResponseFormat.retParam(1, 200, mv);
	}

	@OperationLog(
			success = "测试方案-新建-得定制部门默认信息查询成功",
			busType = "内控测试",
			fail = "测试方案-新建-得定制部门默认信息查询失败",
			operationType = OperationType.SELECT,
			subType = "测试方案"
	)
	@Operation(summary = "测试方案-新建-得定制部门默认信息")
	@GetMapping(value = "/nkcs/plan/add")
	public JsonBean nkcs_ctrltest_plan_add(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		TblOrganization organization = this.tblAssessService.queryOrganizationById(staff.getLinkDetp().getOrgid());
		Map<String, Object> mv = new HashMap<>();
		mv.put("org", organization);
		return ResponseFormat.retParam(1, 200, mv);
	}


	/**
	 * 通用获取设置的编号
	 *
	 * @param
	 */
	@OperationLog(
			success = "测试方案-新建-编号生成查询成功",
			busType = "内控测试",
			fail = "测试方案-新建-编号生成查询失败",
			operationType = OperationType.SELECT,
			subType = "测试方案"
	)
	@GetMapping(value = "/csfa/code/findAutoNumber")
	@Operation(summary = "测试方案-新建-编号生成")
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
		return ResponseFormat.retParam(1, 200, flowNextId);
	}

	@OperationLog(
			success = "测试方案/内控报告-新建/修改页面-选择评价负责人列表查询成功",
			busType = "内控测试",
			fail = "测试方案/内控报告-新建/修改页面-选择评价负责人列表查询失败",
			operationType = OperationType.SELECT,
			subType = "测试方案"
	)
	@Operation(summary = "测试方案/内控报告-新建/修改页面-选择评价负责人列表")
	@GetMapping(value = "/user/list")
	public JsonBean userListss(
			@Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name="pid",description="左侧树节点id.不传时查根",required=false) @RequestParam(value = "pid", required = false) String pid,
			@Parameter(name="realname",description="用户真实名",required=false) @RequestParam(value = "realname", required = false) String realname)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

		BigDecimal orgid = staff.getCurrentOrg().getOrgid();// 选则的机构
		TblOrganization tblOrganization;
		Integer orgtype;
		if (StringUtils.isNotBlank(pid)) {
			tblOrganization = this.tblAssessService.queryOrganizationById(new BigDecimal(pid));
			orgid = tblOrganization.getOrgid();
			orgtype = tblOrganization.getOrgtype().intValue();
		} else {
			tblOrganization = this.tblAssessService.queryOrganizationById(orgid);
			orgtype = tblOrganization.getOrgtype().intValue();
		}

		IPage<Map<String, Object>> iPage = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);

		this.tblAssessService.queryAllPageBeanPid(iPage, orgid, orgtype, realname);

		Map<String, Object> mv = new LinkedHashMap<>();
		mv.put("pid", pid);
		mv.put("pageBean", iPage);
		return ResponseFormat.retParam(1, 200, mv);
	}


	/**
	 * 内控测试方案选择模板
	 *
	 * @return
	 */
	@OperationLog(
			success = "测试方案-新建/修改页面-选择测试模板分页列表(左侧机构树由前台负责)查询成功",
			busType = "内控测试",
			fail = "测试方案-新建/修改页面-选择测试模板分页列表(左侧机构树由前台负责)查询失败",
			operationType = OperationType.SELECT,
			subType = "测试方案"
	)
	@Operation(summary = "测试方案-新建/修改页面-选择测试模板分页列表(左侧机构树由前台负责)")
	@GetMapping(value = "/csfa/def_tmpl_list")
	public JsonBean csfa_def_tmpl_list(
			@Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,

			@Parameter(name = "templeNumber", description = "模板编号") @RequestParam(value = "templeNumber", required = false) String templeNumber,
			@Parameter(name = "templename", description = "模板名称") @RequestParam(value = "templename", required = false) String templename,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		IPage<TblTesttempleVo> ipage = this.csfaService.getTestTemp(staff.getCurrentOrg().getOrgid(), pageNumber, templeNumber, templename);

		Map<String, Object> mv = new LinkedHashMap<>();
		mv.put("templename", templename);
		mv.put("templeNumber", templeNumber);
		mv.put("pageBean", ipage);
		return ResponseFormat.retParam(1, 200, mv);
	}

	@OperationLog(
			success = "测试方案-添加页面-保存成功",
			busType = "内控测试",
			fail = "测试方案-添加页面-保存失败",
			operationType = OperationType.ADD,
			subType = "测试方案"
	)
	@Operation(summary = "测试方案-添加页面-保存")
	@PostMapping(value = "/nkcs/plan/save")
	public JsonBean nkcs_ctrltest_plan_save(
			@Parameter(name = "plannumber", description = "计划编号", required = true) @RequestParam(value = "plannumber", required = true) String plannumber,
			@Parameter(name = "planname", description = "计划名称", required = true) @RequestParam(value = "planname", required = true) String planname,
			@Parameter(name = "planyear", description = "计划年度", required = true) @RequestParam(value = "planyear", required = true) String planyear,

			@Parameter(name = "testtype", description = "测试类型") @RequestParam(value = "testtype", required = false) String testtype,
			@Parameter(name = "oid2", description = "计划制定部门id", required = true) @RequestParam(value = "oid2", required = true) String oid,
			@Parameter(name = "org2", description = "计划制定部门名称", required = true) @RequestParam(value = "org2", required = true) String org2,

			@Parameter(name = "starttime_min", description = "计划时间min") @RequestParam(value = "starttime_min", required = false) String starttime_min,
			@Parameter(name = "starttime_max", description = "计划时间max") @RequestParam(value = "starttime_max", required = false) String starttime_max,
			@Parameter(name = "staffid", description = "负责人id", required = true) @RequestParam(value = "staffid", required = true) String staffid,
			@Parameter(name = "staffname", description = "负责人名", required = true) @RequestParam(value = "staffname", required = true) String staffname,

			@Parameter(name = "cost", description = "开展费用") @RequestParam(value = "cost", required = false) String cost,
			@Parameter(name = "people", description = "投入人力") @RequestParam(value = "people", required = false) String people,
			@Parameter(name = "org3", description = "被测试机构名称") @RequestParam(value = "org3", required = false) String org3,
			@Parameter(name = "testtemid", description = "测试模板id", required = true) @RequestParam(value = "testtemid", required = true) String testtemid,

			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		TblTestplan plan = new TblTestplan();
		plan.setCreatid(staff.getStaffid());
		plan.setPlanname(planname);
		plan.setPlanyear(planyear);
		plan.setTesttype(testtype);
		plan.setTestedorgs(org3);
		plan.setPlanmadedep(org2);
		if (StringUtils.isNotBlank(testtemid)) {
			QueryWrapper<TblTestelement> wrapper = new QueryWrapper<>();
			wrapper.eq("templid", new BigDecimal(testtemid));
			final long count = tblTestelementService.count(wrapper);
			if (count <= 0) {
				return ResponseFormat.retParam(0, 90001, null);
			} else {
				plan.setTesttemid(new BigDecimal(testtemid));
			}
		}
		//        if (StringUtils.isNotBlank(oid)) {
		//            plan.setOrgid(new BigDecimal(oid));
		//        }
		plan.setOrgid(staff.getCurrentOrg().getOrgid());

		if (StringUtils.isNotBlank(cost)) {
			plan.setPlanfee(new BigDecimal(cost));
		}

		if (StringUtils.isNotBlank(staffid)) {
			plan.setStaffid(new BigDecimal(staffid));
		}
		plan.setPlanstatus("未启动");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (StringUtils.isNotBlank(starttime_min)) {
				plan.setStarttime(DateUtils.dateToLocalDateTime(formatter.parse(starttime_min)));
			}

			if (StringUtils.isNotBlank(starttime_max)) {
				plan.setEndtime(DateUtils.dateToLocalDateTime(formatter.parse(starttime_max)));
			}

		} catch (ParseException e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0, 90002, null);
		}
		plan.setPlanleader(staffname);
		plan.setNumberofpeople(people);
		plan.setPlannumber(plannumber);
		this.testplanService.save(plan);
		this.csfaService.delTesttasksByPanId(plan.getTestplanid());
		List<TblTestelement> list = tblTestelementService.findByPlanidAll(plan.getTestplanid().toString());
		if (list != null && list.size() > 0) {
			for (TblTestelement tblTestElement : list) {
				TblTesttask task = new TblTesttask();
				task.setElementid(tblTestElement.getElementid());
				task.setPlanid(plan.getTestplanid());
				task.setCompletestaus(new BigDecimal(0));
				ITblTesttaskService.saveTesttsak(task);
			}
		}


		return ResponseFormat.retParam(1, 200, plan);
	}

	@OperationLog(
			success = "测试方案-修改页面-修改保存成功",
			busType = "内控测试",
			fail = "测试方案-修改页面-修改保存失败",
			operationType = OperationType.UPDATE,
			subType = "测试方案"
	)
	@Operation(summary = "测试方案-修改页面-修改保存")
	@PostMapping(value = "/nkcs/plan/modify_save")
	public JsonBean nkcs_ctrltest_plan_modify_save(
			@Parameter(name = "selectProjectid", description = "testplanid") @RequestParam(value = "selectProjectid") BigDecimal selectProjectid,
			@Parameter(name = "plannumber", description = "计划编号", required = true) @RequestParam(value = "plannumber", required = true) String plannumber,
			@Parameter(name = "planname", description = "计划名称", required = true) @RequestParam(value = "planname", required = true) String planname,
			@Parameter(name = "planyear", description = "计划年度", required = true) @RequestParam(value = "planyear", required = true) String planyear,

			@Parameter(name = "testtype", description = "测试类型") @RequestParam(value = "testtype", required = false) String testtype,
			@Parameter(name = "oid2", description = "计划制定部门id", required = true) @RequestParam(value = "oid2", required = true) String oid,
			@Parameter(name = "org2", description = "计划制定部门名称", required = true) @RequestParam(value = "org2", required = true) String org2,

			@Parameter(name = "starttime_min", description = "计划时间min") @RequestParam(value = "starttime_min", required = false) String starttime_min,
			@Parameter(name = "starttime_max", description = "计划时间max") @RequestParam(value = "starttime_max", required = false) String starttime_max,
			@Parameter(name = "staffid", description = "负责人id", required = true) @RequestParam(value = "staffid", required = true) String staffid,
			@Parameter(name = "staffname", description = "负责人名", required = true) @RequestParam(value = "staffname", required = true) String staffname,

			@Parameter(name = "cost", description = "开展费用") @RequestParam(value = "cost", required = false) String cost,
			@Parameter(name = "people", description = "投入人力") @RequestParam(value = "people", required = false) String people,
			@Parameter(name = "org3", description = "被测试机构名称") @RequestParam(value = "org3", required = false) String org3,
			@Parameter(name = "testtemid", description = "测试模板id", required = true) @RequestParam(value = "testtemid", required = true) String testtemid,

			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		TblTestplan plan = this.testplanService.getById(selectProjectid);
		if (plan == null) {
			return ResponseFormat.retParam(0, 50001, null);
		}
		plan.setCreatid(staff.getStaffid());
		plan.setPlanname(planname);
		plan.setPlanyear(planyear);
		plan.setTesttype(testtype);
		plan.setTestedorgs(org3);
		plan.setPlanmadedep(org2);
		QueryWrapper<TblTestelement> wrapper = new QueryWrapper<>();
		wrapper.eq("templid", new BigDecimal(testtemid));
		final long count = tblTestelementService.count(wrapper);
		if (count <= 0) {
			return ResponseFormat.retParam(0, 90001, null);
		} else {
			plan.setTesttemid(new BigDecimal(testtemid));
		}

		if (StringUtils.isNotBlank(oid)) {
			plan.setOrgid(new BigDecimal(oid));
		}

		if (StringUtils.isNotBlank(cost)) {
			plan.setPlanfee(new BigDecimal(cost));
		}

		if (StringUtils.isNotBlank(staffid)) {
			plan.setStaffid(new BigDecimal(staffid));
		}

		plan.setPlanstatus("未启动");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {

			if (StringUtils.isNotBlank(starttime_min)) {
				plan.setStarttime(DateUtils.dateToLocalDateTime(formatter.parse(starttime_min)));
			}

			if (StringUtils.isNotBlank(starttime_max)) {
				plan.setEndtime(DateUtils.dateToLocalDateTime(formatter.parse(starttime_max)));
			}

		} catch (ParseException e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0, 90002, null);
		}
		plan.setPlanleader(staffname);
		plan.setNumberofpeople(people);
		plan.setPlannumber(plannumber);
		this.testplanService.update(plan);
		// 级联删除,添加testtask操作
		this.csfaService.delTesttasksByPanId(plan.getTestplanid());
		List<TblTestelement> list = tblTestelementService.findByPlanidAll(plan.getTestplanid().toString());
		if (list != null && list.size() > 0) {
			for (TblTestelement tblTestElement : list) {
				TblTesttask task = new TblTesttask();
				task.setElementid(tblTestElement.getElementid());
				task.setPlanid(plan.getTestplanid());
				task.setCompletestaus(new BigDecimal(0));
				ITblTesttaskService.saveTesttsak(task);
			}
		}
		return ResponseFormat.retParam(1, 200, plan);
	}


	@OperationLog(
			success = "测试方案-点击分配回传状态查询成功",
			busType = "内控测试",
			fail = "测试方案-点击分配回传状态查询失败",
			operationType = OperationType.SELECT,
			subType = "测试方案"
	)
	@GetMapping(value = "/nkcs/plan/isStatus", produces = "application/json; charset=utf-8")
	@Operation(summary = "测试方案-点击分配回传状态")
	public @ResponseBody
	JsonBean zgfp_fqStatus(
			@Parameter(name = "selectProjectid", description = "testplanid") @RequestParam(value = "selectProjectid") BigDecimal selectProjectid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		if (selectProjectid != null) {
			TblTestplan plan = this.testplanService.getById(selectProjectid);
			if (plan != null && plan.getPlanstatus().equals("未启动")) {
				return ResponseFormat.retParam(1, 200, null);
			} else {
				if (plan != null && plan.getPlanstatus().equals("完成")) {
					return ResponseFormat.retParam(0, 90003, null);
				} else {
					return ResponseFormat.retParam(0, 90004, null);
				}
			}
		} else {
			return ResponseFormat.retParam(0, 90006, null);
		}
	}

	@OperationLog(
			success = "测试方案-分配-左侧树查询成功",
			busType = "内控测试",
			fail = "测试方案-分配-左侧树查询失败",
			operationType = OperationType.SELECT,
			subType = "测试方案"
	)
	@Operation(summary = "测试方案-分配-左侧树")
	@GetMapping(value = "/csfa/gettree")
	public JsonBean csfa_getTree(@Parameter(name = "planid", description = "testplanid") @RequestParam(value = "planid") String planid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
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
		return ResponseFormat.retParam(1, 200, mv);
	}


	@OperationLog(
			success = "测试方案-分配-右侧列表查询成功",
			busType = "内控测试",
			fail = "测试方案-分配-右侧列表查询失败",
			operationType = OperationType.SELECT,
			subType = "测试方案"
	)
	@Operation(summary = "测试方案-分配-右侧列表")
	@GetMapping(value = "/csfa/def_list")
	public JsonBean csfa_gzdg_def_list(
			@Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "node", description = "左侧节点id") @RequestParam(value = "node", required = false) BigDecimal node,
			@Parameter(name = "templId", description = "templId") @RequestParam(value = "templId", required = false) BigDecimal templId,
			@Parameter(name = "planid", description = "planid") @RequestParam(value = "planid", required = false) BigDecimal planid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		IPage<Map<String, Object>> ipage = null;
		if (templId != null && planid != null) {
			ipage = this.csfaService.fingByTree(node, templId, planid, pageNumber);
		}
		Map<String, Object> mv = new LinkedHashMap<>();
		mv.put("node", node);
		mv.put("templId", templId);
		mv.put("planid", planid);
		mv.put("pageBean", ipage);
		return ResponseFormat.retParam(1, 200, mv);
	}


	@OperationLog(
			success = "测试方案-分配-设置人员右侧列表(左树由前台完成)查询成功",
			busType = "内控测试",
			fail = "测试方案-分配-设置人员右侧列表(左树由前台完成)查询失败",
			operationType = OperationType.SELECT,
			subType = "测试方案"
	)
	@Operation(summary = "测试方案-分配-设置人员右侧列表(左树由前台完成)")
	@GetMapping(value = "/csfa/list")
	public JsonBean userListss2(
			@Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name="pid",description="左侧树节点id.不传时查根",required=false) @RequestParam(value = "pid", required = false) String pid,
			@Parameter(name="realname",description="用户真实名模糊查询用",required=false) @RequestParam(value = "realname", required = false) String realname,
			@Parameter(name = "node", description = "之前的node值回传用") @RequestParam(value = "node", required = false) BigDecimal node,
			@Parameter(name = "templId", description = "之前的templId回传用") @RequestParam(value = "templId", required = false) BigDecimal templId,
			@Parameter(name = "planid", description = "之前的planid回传用") @RequestParam(value = "planid", required = false) BigDecimal planid,
			@Parameter(name = "task", description = "ELEMENTID多值用,隔开来自checkbox比如226760,226761回传用") @RequestParam(value = "task", required = false) String task)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		BigDecimal orgid = staff.getCurrentOrg().getOrgid();// 选则的机构
		TblOrganization tblOrganization;
		Integer orgtype;
		if (StringUtils.isNotBlank(pid)) {
			tblOrganization = this.tblAssessService.queryOrganizationById(new BigDecimal(pid));
			orgid = tblOrganization.getOrgid();
			orgtype = tblOrganization.getOrgtype().intValue();
		} else {
			tblOrganization = this.tblAssessService.queryOrganizationById(orgid);
			orgtype = tblOrganization.getOrgtype().intValue();
		}

		IPage<Map<String, Object>> iPage = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
		this.tblAssessService.queryAllPageBeanPid(iPage, orgid, orgtype, realname);
		Map<String, Object> mv = new LinkedHashMap<>();
		mv.put("pid", pid);
		mv.put("realname", realname);
		mv.put("node", node);
		mv.put("templId", templId);
		mv.put("planid", planid);
		mv.put("task", task);
		mv.put("pageBean", iPage);
		return ResponseFormat.retParam(1, 200, mv);
	}

	@OperationLog(
			success = "测试方案-分配-设置人员-选定成功",
			busType = "内控测试",
			fail = "测试方案-分配-设置人员-选定失败",
			operationType = OperationType.UPDATE,
			subType = "测试方案"
	)
	@Operation(summary = "测试方案-分配-设置人员-选定")
	@PostMapping(value = "/csfa/saveUser")
	public JsonBean start_isStatus(
			@Parameter(name = "task", description = "ELEMENTID多值用,隔开来自checkbox比如226760,226761回传用") @RequestParam(value = "task") String task,
			@Parameter(name = "planid", description = "planid") @RequestParam(value = "planid") String planid,
			@Parameter(name = "userid", description = "userid") @RequestParam(value = "userid") String userid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		Map<String, Object> mv = new LinkedHashMap<>();
		if (StringUtils.isNotBlank(task) && StringUtils.isNotBlank(planid)) {
			this.csfaService.updateTesttsak(task, planid, userid);
		} else {
			return ResponseFormat.retParam(0, 90006, null);
		}
		mv.put("task", task);
		mv.put("planid", planid);
		mv.put("userid", userid);
		return ResponseFormat.retParam(1, 200, mv);
	}


	/**
	 * 启动测试方案
	 *
	 * @param
	 * @return
	 */
	@OperationLog(
			success = "测试方案-启动成功",
			busType = "内控测试",
			fail = "测试方案-启动失败",
			operationType = OperationType.UPDATE,
			subType = "测试方案"
	)
	@PostMapping(value = "/nkcs/plan/start_isStatus", produces = "application/json; charset=utf-8")
	@Operation(summary = "测试方案-启动")
	public JsonBean start_isStatus(
			@Parameter(name = "selectProjectid", description = "testplanid") @RequestParam(value = "selectProjectid") BigDecimal selectProjectid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

		if (selectProjectid != null) {
			TblTestplan plan = this.testplanService.getById(selectProjectid);
			Integer wcCount = this.csfaService.findByPlan(selectProjectid);
			Integer wCount = this.csfaService.findByPlanwStra(selectProjectid);
			if (plan != null && plan.getPlanstatus().equals("未启动") && wcCount != null && wCount != null && wcCount.toString()
					.equals(wCount.toString())) {
				plan.setPlanstatus("已启动");
				testplanService.update(plan);
				return ResponseFormat.retParam(1, 200, null);
			} else {
				if (plan != null && plan.getPlanstatus().equals("完成")) {
					return ResponseFormat.retParam(0, 90003, null);
				} else if (wCount == null || !wcCount.toString().equals(wCount.toString())) {
					return ResponseFormat.retParam(0, 90008, null);
				} else {
					return ResponseFormat.retParam(0, 90005, null);
				}
			}
		} else {
			return ResponseFormat.retParam(0, 90006, null);

		}
	}


	@OperationLog(
			success = "测试方案-删除成功",
			busType = "内控测试",
			fail = "测试方案-删除失败",
			operationType = OperationType.DELETE,
			subType = "测试方案"
	)
	@Operation(summary = "测试方案-删除")
	@PostMapping(value = "/nkcs/plan/delete")
	public JsonBean nkcs_ctrltest_plan_delete(
			@Parameter(name = "selectProjectid", description = "testplanid") @RequestParam(value = "selectProjectid") BigDecimal selectProjectid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		TblTestplan plan = this.testplanService.getById(selectProjectid);
		if (plan != null && plan.getPlanstatus().equals("完成")) {
			return ResponseFormat.retParam(0, 90003, null);
		} else if (plan != null && plan.getPlanstatus().equals("已启动")) {
			return ResponseFormat.retParam(0, 90005, null);
		}
		if (selectProjectid != null) {
			this.csfaService.deleteTestPlan(selectProjectid);
		}
		return ResponseFormat.retParam(1, 200, null);
	}


}
