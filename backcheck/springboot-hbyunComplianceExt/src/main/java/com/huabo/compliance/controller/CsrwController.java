package com.huabo.compliance.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.compliance.entity.TblAttachment;
import com.huabo.compliance.entity.TblTestelement;
import com.huabo.compliance.entity.TblTestplan;
import com.huabo.compliance.entity.TblTesttask;
import com.huabo.compliance.entity.Tree;
import com.huabo.compliance.service.CsfaService;
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
 * @date:2022-09-13 13:42
 * @description:
 */
@RestController
@Slf4j
@Tag(name="内控测试-测试任务",description="内控测试-测试任务")
@RequestMapping(value = "/nbkz")
public class CsrwController {


	@Resource
	ITblTestplanService testplanService;
	@Autowired
	ITblStaffService iTblStaffService;


	@Autowired
	TblAssessService tblAssessService;

	@Resource
	CsfaService csfaService;

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

	/**
	 * 测试任务
	 *
	 * @param
	 * @return
	 */
	@OperationLog(
			success = "测试任务-主页查询成功",
			busType = "内控测试",
			fail = "测试任务-主页查询失败",
			operationType = OperationType.SELECT,
			subType = "测试任务"
	)
	@GetMapping(value = "/nkcs/impl/control_test_impl_list")
	@ResponseBody
	@Operation(summary = "测试任务-主页")
	public JsonBean nkcs_control_test_impl_list(
			@Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		// TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
		//TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 选则的机构
		// pageBean = service.findAllRw(null, pageNumber,
		// pageBean.getPageSize(), "", "",user.getStaffid().toString());
		// 查看测试任务
		IPage<TblTestplan> iPage;
		iPage = csrwService.findAllRwToOrg(pageNumber, user.getStaffid().toString(), user.getCurrentOrg().getOrgid().toString(), pageSize);

		Map<String, Object> mv = new LinkedHashMap<>();
		mv.put("pageBean", iPage);

		return new JsonBean(200, "success", mv);

	}


	@OperationLog(
			success = "测试任务-左侧树查询成功",
			busType = "内控测试",
			fail = "测试任务-左侧树查询失败",
			operationType = OperationType.SELECT,
			subType = "测试任务"
	)
	@GetMapping(value = "/csrw/gettree")
	@ResponseBody
	@Operation(summary = "测试任务-左侧树")
	public JsonBean csrw_getTree(@Parameter(name = "planid", description = "planid") @RequestParam(value = "planid", required = false) String planid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		Map<String, Object> mv = new LinkedHashMap<>();
		if (StringUtils.isNotBlank(planid)) {
			TblTestplan plan = this.testplanService.getById(new BigDecimal(planid));

			if (plan != null) {
				mv.put("panid", planid);
				mv.put("templId", plan.getTesttemid());
				if (StringUtils.isNotBlank(planid)) {
					TblTestplan testplan = this.testplanService.getById(new BigDecimal(planid));
					if (testplan != null && testplan.getReturnstatus() != null && testplan.getReturnstatus().toString().equals("1")) {
						//TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
						List<Tree> tree = this.csrwService
								.getTreeCSRWByChildsReturn(plan.getTesttemid().toString(), user.getStaffid().toString(), planid);
						if (tree != null && tree.size() > 0) {
							mv.put("tree", tree);
							return new JsonBean(200, "success", mv);
						} else {
							tree = this.csrwService.getTreeCSRWByChilds(plan.getTesttemid().toString(), user.getStaffid().toString(), planid);
							mv.put("tree", tree);
							return new JsonBean(200, "success", mv);
						}
					} else {

						List<Tree> tree = this.csrwService.getTreeCSRWByChilds(plan.getTesttemid().toString(), user.getStaffid().toString(), planid);
						mv.put("tree", tree);
						return new JsonBean(200, "success", mv);
					}
				}


			}
		}
		return new JsonBean(-1, "planid不存在", planid);
	}


	@OperationLog(
			success = "测试任务-右侧列表查询成功",
			busType = "内控测试",
			fail = "测试任务-右侧列表查询失败",
			operationType = OperationType.SELECT,
			subType = "测试任务"
	)
	@GetMapping(value = "/csrw/def_list")
	@ResponseBody
	@Operation(summary = "测试任务-右侧列表")
	public JsonBean csrw_gzdg_def_list(
			@Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name="node",description="node",required=false) @RequestParam(value = "node", required = false) String node,
			@Parameter(name="templId",description="templId",required=false) @RequestParam(value = "templId", required = false) String templId,
			@Parameter(name="planid",description="planid",required=false) @RequestParam(value = "planid", required = false) String planid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		Map<String, Object> mv = new LinkedHashMap<>();
		IPage<Map<String, Object>> pageBean = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
		if (StringUtils.isNotBlank(planid)) {
			TblTestplan testplan = this.testplanService.getById(new BigDecimal(planid));
			if (testplan != null && testplan.getReturnstatus() != null && testplan.getReturnstatus().toString().equals("1")) {

				pageBean = this.csrwService.fingByTreeCSRWByUserReturn(node, templId, planid, user.getStaffid(), pageNumber);

				mv.put("pageBean", pageBean);
				mv.put("str", "1");
			} else {

				pageBean = this.csrwService.fingByTreeCSRWByUser(node, templId, planid, user.getStaffid(), pageNumber);
				mv.put("pageBean", pageBean);
				//mv.addObject("str", "1");
			}
			mv.put("plan", testplan);
		}

		mv.put("node", node);
		mv.put("templId", templId);
		mv.put("planid", planid);

		return new JsonBean(200, "success", mv);

	}


	@OperationLog(
			success = "测试任务-右侧列表-点击编号查看详情查询成功",
			busType = "内控测试",
			fail = "测试任务-右侧列表-点击编号查看详情查询失败",
			operationType = OperationType.SELECT,
			subType = "测试任务"
	)
	@GetMapping(value = "/csmb/elemendatail")
	@ResponseBody
	@Operation(summary = "测试任务-右侧列表-点击编号查看详情")
	public JsonBean elemendatail(
			@Parameter(name="elementId",description="elementId",required=false) @RequestParam(value = "elementId", required = false) BigDecimal elementId,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		Map<String, Object> mv = new LinkedHashMap<>();
		if (null != elementId) {
			TblTestelement element = this.tblTestelementService.getById(elementId);

			mv.put("element", element);
		}

		return new JsonBean(200, "success", mv);
	}


	@OperationLog(
			success = "测试任务-右侧列表-点击修改/测试查询查询成功",
			busType = "内控测试",
			fail = "测试任务-右侧列表-点击修改/测试查询查询失败",
			operationType = OperationType.SELECT,
			subType = "测试任务"
	)
	@GetMapping(value = "/csrw/addtask")
	@ResponseBody
	@Operation(summary = "测试任务-右侧列表-点击修改/测试查询")
	public JsonBean addtask(
			@Parameter(name="ementid",description="ementid") @RequestParam(value = "ementid") BigDecimal ementid,
			@Parameter(name="planid",description="planid") @RequestParam(value = "planid") BigDecimal planid,
			@Parameter(name="node",description="node",required=false) @RequestParam(value = "node", required = false) String node,
			@Parameter(name="templId",description="templId",required=false) @RequestParam(value = "templId", required = false) String templId,
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
			success = "测试任务-修改/测试页面-附件保存成功",
			busType = "内控测试",
			fail = "测试任务-修改/测试页面-附件保存失败",
			operationType = OperationType.UPLOAD,
			subType = "测试任务"
	)
	@Operation(summary = "测试任务-修改/测试页面-附件保存")
	@PostMapping(value = "/csrw/control_test_impl_upload")
	public JsonBean control_test_impl_upload(
			@Parameter(name="projectid",description="testtaskid") @RequestParam(value = "projectid") String projectid,
			@Parameter(name = "file", description = "附件上传entity", required = true) MultipartFile file,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		String fileName = file.getOriginalFilename();
		long timeInMillis = Calendar.getInstance().getTimeInMillis();
		String oldname = fileName.substring(0, fileName.lastIndexOf("."));
		String newname = fileName.replace(oldname, "00" + timeInMillis + "csrw");
		long size = file.getSize();
		try {
			boolean flag = FtpUtil.uploadFile(newname, file.getInputStream());
			if (flag) {
				log.info("上传成功");
			} else {
				log.info("上传失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TblAttachment a = new TblAttachment();
		a.setAttname(fileName);
		//        a.set
		//        TblTesttask task = new TblTesttask();
		//        task.setTesttaskid(new BigDecimal(projectid));
		//        a.getTblTesttasks().add(task);
		a.setAttpath(newname);
		a.setAttsize(new BigDecimal(size / 1024));
		a.setUploader(user.getUsername());
		a.setUploadtime(LocalDateTime.now());
		attachmentService.saveEntity(a);
		testtaskAttService.saveEntity(a.getAttid(), new BigDecimal(projectid));
		Map<String, Object> mv = new LinkedHashMap<>();
		mv.put("att", a);
		return new JsonBean(200, "上传成功", mv);
	}

	@OperationLog(
			success = "测试任务-修改/测试页面-附件删除成功",
			busType = "内控测试",
			fail = "测试任务-修改/测试页面-附件删除失败",
			operationType = OperationType.DELETE,
			subType = "测试任务"
	)
	@Operation(summary = "测试任务-修改/测试页面-附件删除")
	@PostMapping(value = "/csrw/control_test_impl_upload_del")
	public JsonBean control_test_impl_upload_del(@Parameter(name = "attid", description = "attid") @RequestParam(value = "attid") BigDecimal attid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

		if (attid != null) {
			attachmentService.deleteAttAndTaskAtt(attid);
		}
		return new JsonBean(200, "success", null);
	}


	/**
	 * 测试任务-保存测试信息
	 *
	 * @param
	 * @return
	 */
	@OperationLog(
			success = "测试任务-修改/测试页面-保存测试成功",
			busType = "内控测试",
			fail = "测试任务-修改/测试页面-保存测试失败",
			operationType = OperationType.UPDATE,
			subType = "测试任务"
	)
	@Operation(summary = "测试任务-修改/测试页面-保存测试")
	@PostMapping(value = "/csrw/control_test_impl_save")
	public JsonBean nkcs_control_test_impl_save(
			@Parameter(name = "testtaskid", description = "testtaskid") @RequestParam(value = "testtaskid", required = false) String testtaskid,
			@Parameter(name = "memo", description = "备注") @RequestParam(value = "memo", required = false) String memo,
			@Parameter(name = "procedures", description = "测试程序") @RequestParam(value = "procedures", required = false) String procedures,
			@Parameter(name = "testresult", description = "测试结果") @RequestParam(value = "testresult", required = false) String testresult,
			@Parameter(name = "testpointvalidity", description = "测试有效性value=1or2or3") @RequestParam(value = "testpointvalidity", required = false) String testpointvalidity,
			@Parameter(name = "planid", description = "planid") @RequestParam(value = "planid", required = false) BigDecimal planid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

		TblStaffUtil user = userProvider.get();
		if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		Map<String, Object> mv = new LinkedHashMap<>();
		Integer result = 0;
		TblTesttask newtask = new TblTesttask();
		if (StringUtils.isNotBlank(testtaskid)) {
			newtask = this.testtaskService.getById(testtaskid);
			newtask.setTestpointvalidity(testpointvalidity);
			newtask.setMemo(memo);
			newtask.setProcedures(procedures);
			newtask.setTestresult(testresult);
			newtask.setTeststatus("已完成");
			testtaskService.updateTesttsak(newtask);
			mv.put("testtask", newtask);
			result = 1;
		} else {
			testtaskService.saveTesttsak(newtask);
		}
		if (planid != null) {
			TblTestplan plan = testplanService.getById(planid);
			plan.setPlanstatus("执行中");
			testplanService.update(plan);
			mv.put("plan", plan);
		}

		return new JsonBean(200, "success", mv);
	}

	@OperationLog(
			success = "测试任务-全部提交成功",
			busType = "内控测试",
			fail = "测试任务-全部提交失败",
			operationType = OperationType.ADD,
			subType = "测试任务"
	)
	@Operation(summary = "测试任务-全部提交")
	@PostMapping(value = "/csrw/saveall", produces = "application/json; charset=utf-8")
	public @ResponseBody
	String saveall(@Parameter(name = "planid", description = "planid") @RequestParam(value = "planid", required = false) String planid,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

		TblStaffUtil user = userProvider.get();
		if (user == null) {
			return JsonBean.error("用户已失效");
        }
		if (StringUtils.isNotBlank(planid)) {
			return this.csrwService.saveAll(new BigDecimal(planid), user.getStaffid());
		}
		return JsonBean.error("保存失败");
	}


}
