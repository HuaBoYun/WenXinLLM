package com.huabo.monitor.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.TblAmorgProblem;
import com.huabo.monitor.entity.TblAssess;
import com.huabo.monitor.entity.TblAssessTarget;
import com.huabo.monitor.entity.TblAssessTargetVo;
import com.huabo.monitor.entity.TblAssessVo;
import com.huabo.monitor.entity.TblAssesslevel;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.entity.TblOrganization;
import com.huabo.monitor.entity.TblRiskAttWord;
import com.huabo.monitor.service.ITblAmorgProblemService;
import com.huabo.monitor.service.ITblAssessMarkService;
import com.huabo.monitor.service.ITblAssessTargetService;
import com.huabo.monitor.service.ITblAssesslevelService;
import com.huabo.monitor.service.ITblAssesstempleService;
import com.huabo.monitor.service.ITblAttachmentService;
import com.huabo.monitor.service.ITblRiskAttWordService;
import com.huabo.monitor.service.ITblStaffService;
import com.huabo.monitor.service.ITblTaskService;
import com.huabo.monitor.service.PjjgService;
import com.huabo.monitor.service.TblAssessService;
import com.huabo.monitor.util.ExportDoc;
import com.huabo.monitor.util.FiexibleNameAssignment;
import com.huabo.monitor.util.FileUtil;
import com.huabo.monitor.util.IPageResult;
import com.huabo.monitor.vo.param.fieldOrgStaffId;
import com.huabo.monitor.vo.param.fieldOrgStaffName;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author：yhr
 * @date:2022-08-31 10:51
 * @description:
 */
@RestController
@Slf4j
@Tag(name="评价管理-评价结果",description="评价管理-评价结果")
@RequestMapping(value = "/nbkz")
public class PjjgController {


    @Autowired
    TblAssessService tblAssessService;

    @Autowired
    ITblStaffService iTblStaffService;
    @Autowired
    ITblAssessMarkService iTblAssessMarkService;

    @Autowired
    ITblTaskService iTblTaskService;
    @Autowired
    ITblAssesstempleService iTblAssesstempleService;

    @Autowired
    PjjgService pjjgService;

    @Autowired
    ITblAssesslevelService iTblAssesslevelService;

    @Autowired
    ITblAssessTargetService iTblAssessTargetService;

    @Autowired
    ITblAmorgProblemService iTblAmorgProblemService;
    @Autowired
    ITblAttachmentService iTblAttachmentService;

    @Autowired
    ITblRiskAttWordService iTblRiskAttWordService;
    
    @Resource
    private UserProvider userProvider;

    @Value("${filedir}")
    String filedir;
	@Value("${application.administrators:}")
	private String administrators;


    @OperationLog(
            success = "评价结果-主页查询成功",
            busType = "内控设置",
            fail = "评价结果-主页查询失败",
            operationType = OperationType.SELECT,
            subType = "评价跟踪"
    )
    @GetMapping(value = "/pjgl/t08_proj_task_result")
    @Operation(summary = "评价结果-主页查询")
    public JsonBean initiatePjgl(@Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                 @Parameter(name = "assNumnber", description = "评价编号") @RequestParam(value = "assNumnber", required = false) String assNumnber,
                                 @Parameter(name = "assName", description = "项目名称") @RequestParam(value = "assName", required = false) String assName,
                                 @Parameter(name = "startDate", description = "开始时间:格式年-月-日") @RequestParam(value = "startDate", required = false) String startDate,
                                 @Parameter(name = "startDates", description = "--开始时间") @RequestParam(value = "startDates", required = false) String startDates,
                                 @Parameter(name = "endDate", description = "结束日期") @RequestParam(value = "endDate", required = false) String endDate,
                                 @Parameter(name = "endDates", description = "--结束日期") @RequestParam(value = "endDates", required = false) String endDates,
                                 @Parameter(name = "orgid", description = "单位id") @RequestParam(value = "orgid", required = false) BigDecimal orgid,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		Integer authorityType;
		if (JudgeRoleRight.judgeRoleRight(administrators, staff.getRoleNames())) {
			authorityType = 1;
		} else {
			authorityType = 0;
		}
     //   IPage<TblAssessVo> iPage = tblAssessService.initiatePjjg(pageNumber, assNumnber, assName, startDate, startDates, endDate, endDates, staff,authorityType);


        PageInfo<TblAssessVo> info=tblAssessService.initiatePjjgNew(pageNumber, assNumnber, assName, startDate, startDates, endDate, endDates, staff,authorityType,orgid);
        IPageResult<TblAssessVo> iPage=new IPageResult<TblAssessVo>().buildIpage(info);
        Map<String, Object> mv = new HashMap<>();
        mv.put("assNumnber", assNumnber);
        mv.put("assName", assName);
        mv.put("startDate", startDate);
        mv.put("startDates", startDates);
        mv.put("endDate", endDate);
        mv.put("endDates", endDates);
        mv.put("pageBean", iPage);
        return new JsonBean(200, "success", mv);
    }


    /**
     * 评价最终等级
     *
     * @return
     * @author
     */
    @OperationLog(
            success = "评价结果-主页列表-点击评价编号(评价结果-评价)查询成功",
            busType = "内控设置",
            fail = "评价结果-主页列表-点击评价编号(评价结果-评价)查询失败",
            operationType = OperationType.SELECT,
            subType = "评价跟踪"
    )
    @GetMapping(value = "/pjgl/t08_proj_disp_gd")
    @Operation(summary = "评价结果-主页列表-点击评价编号(评价结果-评价)")
    public JsonBean t08_proj_disp_gds(
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @Parameter(name="assId",description="assId") @RequestParam(value = "assId") BigDecimal assId,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        TblAssess assess = this.tblAssessService.getById(assId);
      //  IPage<TblAssessTargetVo> pageBean = this.pjjgService.getOrgList(assId, pageNumber);
        IPageResult<TblAssessTargetVo> pageBean=new IPageResult<TblAssessTargetVo>().buildIpage(pjjgService.getOrgListNew(assId, pageNumber));
        Map<String, Object> mv = new HashMap<>();
        mv.put("project", assess);
        mv.put("assId", assId);
        mv.put("pageBean", pageBean);
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        QueryWrapper<TblAssesslevel> qw = new QueryWrapper<TblAssesslevel>();
        qw.eq("tblcomany", staff.getCurrentOrg().getOrgid());
        qw.orderByDesc("levellower");
        FiexibleNameAssignment ment=new FiexibleNameAssignment();
        List<TblAssesslevel> assesslevels = this.iTblAssesslevelService.list(qw);
        if(CollectionUtils.isNotEmpty(assesslevels)){
			assesslevels.forEach(entity->{
				try {
					//对灵活字段中的姓名名称及机构名称赋值
					fieldOrgStaffId item=new fieldOrgStaffId();
					BeanUtils.copyProperties(entity,item); 
					fieldOrgStaffName nameEntity=ment.setOpenName(item);
					BeanUtils.copyProperties(nameEntity,entity ); 
					item=null; // 处理并解除引用
					nameEntity=null; // 处理并解除引用
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			} );
        }
        mv.put("assesslevels", assesslevels);
        int major=tblAssessService.getMajorByAssid(assId,staff.getStaffid().toString());
        mv.put("major", major);
        return new JsonBean(200, "success", mv);
    }

    /**
     * 评价归档 - 评级校正跳转修改
     *
     * @param targetId
     * @param
     * @return
     */
    @OperationLog(
            success = "评价结果-评价归档- 评级校正查询成功",
            busType = "内控设置",
            fail = "评价结果-评价归档- 评级校正查询失败",
            operationType = OperationType.SELECT,
            subType = "评价跟踪"
    )
    @Operation(summary = "评价结果-评价归档- 评级校正")
    @GetMapping(value = "/pjgl/t08_appr_result_disp")
    public JsonBean t08_appr_result_disp(
            @Parameter(name="targetId",description="targetId") @RequestParam(value = "targetId") BigDecimal targetId,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        FiexibleNameAssignment ment=new FiexibleNameAssignment();
        Map<String, Object> mv = new HashMap<>();
        
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (null != targetId) {
            TblAssessTargetVo assessTarget = this.pjjgService.getMyOneTargetVo(targetId);

            QueryWrapper<TblAssesslevel> qw = new QueryWrapper<TblAssesslevel>();
            qw.eq("tblcomany", staff.getCurrentOrg().getOrgid());
            qw.orderByDesc("levellower");


            List<TblAssesslevel> assesslevels = this.iTblAssesslevelService.list(qw);
			if(CollectionUtils.isNotEmpty(assesslevels)){
				assesslevels.forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,entity ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
				
			}
            mv.put("assessTarget", assessTarget);
            mv.put("assesslevels", assesslevels);


        }

        return new JsonBean(200, "success", mv);
    }

    /**
     * 评价归档-评级校正保存
     *
     * @param targetId
     * @param
     * @param
     * @return
     */
    @OperationLog(
            success = "评价归档- 评级校正保存成功",
            busType = "内控设置",
            fail = "评价归档- 评级校正保存失败",
            operationType = OperationType.UPDATE,
            subType = "评价跟踪"
    )
    @Operation(summary = "评价归档- 评级校正保存")
    @PostMapping(value = "/pjgl/target_modife")
    public JsonBean target_modife(
            @Parameter(name="targetId",description="targetId") @RequestParam(value = "targetId") BigDecimal targetId,
            @Parameter(name="checkLevel",description="校正等级",required=false) @RequestParam(value = "checkLevel", required = false) String checkLevel,
            @Parameter(name="checkReason",description="校正原因",required=false) @RequestParam(value = "checkReason", required = false) String checkReason,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblAssessTarget assessTarget = this.iTblAssessTargetService.selectTblAssessTarget(targetId);
        assessTarget.setChecklevel(checkLevel);
        assessTarget.setCheckreason(checkReason);

        iTblAssessTargetService.updateById(assessTarget);
        return t08_appr_result_disp(targetId, token);

    }


    /**
     * 问题-列表
     *
     * @param
     * @param orgid
     * @param orgtype
     * @return
     * @author tangjiajun
     */
    @OperationLog(
            success = "评级校正-添加问题-列表查询成功",
            busType = "内控设置",
            fail = "评级校正-添加问题-列表查询失败",
            operationType = OperationType.SELECT,
            subType = "评价跟踪"
    )
    @Operation(summary = "评级校正-添加问题-列表")
    @GetMapping(value = "/pjgl/question_list_add")
    public JsonBean question_listQxwt_pj(
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @Parameter(name="orgid",description="orgid",required=false) @RequestParam(value = "orgid", required = false) String orgid,
            @Parameter(name = "orgtype", description = "如果传了orgid则这个可以不传,传值就要传正确") @RequestParam(value = "orgtype", required = false) String orgtype,
            @Parameter(name="targetId",description="targetId") @RequestParam(value = "targetId") BigDecimal targetId,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        //TblProblemService test = SpringContextHolder.getBean("TblProblemService");
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        String number = String.valueOf(pageNumber);

        if (StringUtils.isBlank(orgid)) {
            orgid = staff.getCurrentOrg().getOrgid().toString();
            orgtype = staff.getCurrentOrg().getOrgtype().toString();
        }
        TblOrganization org = this.tblAssessService.queryOrganizationById(new BigDecimal(orgid));
        if (StringUtils.isBlank(orgtype)) {
            orgtype = org.getOrgtype().toString();
        }

        if (number != null && number.length() > 0) {
            pageNumber = Integer.parseInt(number);
        }
        Map<String, Object> mv = new HashMap<>();
        // pageBean = test.findByPageBean(null, null, null, null, null,
        // pageNumber, pageBean.getPageSize(),orgid,orgtype);
        //pageBean = test.findByPageBean(pageNumber, pageBean.getPageSize(), orgid, orgtype);


        mv.put("pageBean", this.pjjgService.findByPageBean(pageNumber, orgid, orgtype));
        mv.put("orgid", orgid);
        mv.put("targetid", targetId);
        mv.put("orgtype", orgtype);
        return new JsonBean(200, "success", mv);
    }


    /**
     * 评价归档 - 评级校正添加问题
     *
     * @param
     * @return
     */
    @OperationLog(
            success = "评级校正-添加问题列表-选定添加成功",
            busType = "内控设置",
            fail = "评级校正-添加问题列表-选定添加失败",
            operationType = OperationType.ADD,
            subType = "评价跟踪"
    )
    @Operation(summary = "评级校正-添加问题列表-选定添加")
    @PostMapping(value = "/pjgl/add_problem")
    public JsonBean pjgl_addProblem(
            @Parameter(name="proid",description="proid") @RequestParam(value = "proid") String proid,
            @Parameter(name="targetid",description="targetid") @RequestParam(value = "targetid") BigDecimal targetid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        if (proid != null || targetid != null) {
            //TblAssessTarget assessTarget = this.iTblAssessTargetService.getById(targetid);
            QueryWrapper<TblAmorgProblem> qw = new QueryWrapper<TblAmorgProblem>();
            TblAmorgProblem tblAmorgProblem = new TblAmorgProblem();

            if (proid.indexOf("__b") >= 0) {

                String id = proid.replace("__b", "");
                qw.eq("amorgid", targetid).eq("bugid", id);
                TblAmorgProblem one = this.iTblAmorgProblemService.getOne(qw);
                if (one == null) {
                    tblAmorgProblem.setAmorgid(targetid);
                    tblAmorgProblem.setBugid(new BigDecimal(id));
                    iTblAmorgProblemService.save(tblAmorgProblem);
                }
            }
            if (proid.indexOf("__d") >= 0) {

                String id = proid.replace("__d", "");
                qw.eq("amorgid", targetid).eq("sheetid", id);
                TblAmorgProblem one = this.iTblAmorgProblemService.getOne(qw);
                if (one == null) {
                    tblAmorgProblem.setAmorgid(targetid);
                    tblAmorgProblem.setSheetid(new BigDecimal(id));
                    iTblAmorgProblemService.save(tblAmorgProblem);
                }
            }
            if (proid.indexOf("__f") >= 0) {


                String id = proid.replace("__f", "");
                qw.eq("amorgid", targetid).eq("riskid", id);
                TblAmorgProblem one = this.iTblAmorgProblemService.getOne(qw);
                if (one == null) {
                    tblAmorgProblem.setAmorgid(targetid);
                    tblAmorgProblem.setRiskid(new BigDecimal(id));
                    iTblAmorgProblemService.save(tblAmorgProblem);
                }
            }

        }
        return new JsonBean(200, "sucess", null);
    }

    /**
     * 评价归档 - 评级校正删除问题
     *
     * @param
     * @return
     */
    @OperationLog(
            success = "评级校正--删除问题成功",
            busType = "内控设置",
            fail = "评级校正--删除问题失败",
            operationType = OperationType.DELETE,
            subType = "评价跟踪"
    )
    @Operation(summary = "评级校正--删除问题(未测试)")
    @PostMapping(value = "/pjgl/delete_problem")
    public @ResponseBody
    JsonBean pjgl_delete_problem(
            @Parameter(name="proid",description="proid") @RequestParam(value = "proid") String proid,
            @Parameter(name="targetid",description="targetid") @RequestParam(value = "targetid") BigDecimal targetid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        if (proid != null || targetid != null) {
            QueryWrapper<TblAmorgProblem> qw = new QueryWrapper<TblAmorgProblem>();

            if (proid.indexOf("__b") >= 0) {
                String id = proid.replace("__b", "");
                qw.eq("amorgid", targetid).eq("bugid", id);
            }
            if (proid.indexOf("__d") >= 0) {
                String id = proid.replace("__d", "");
                qw.eq("amorgid", targetid).eq("sheetid", id);
            }
            if (proid.indexOf("__f") >= 0) {
                String id = proid.replace("__f", "");
                qw.eq("amorgid", targetid).eq("riskid", id);
            }
            // 删除
            iTblAmorgProblemService.remove(qw);
        }

        return new JsonBean(200, "sucess", null);
    }


    /**
     * 查看评价结果
     *
     * @return
     */
    @OperationLog(
            success = "评价结果-主页-点击结果查询成功",
            busType = "内控设置",
            fail = "评价结果-主页-点击结果查询失败",
            operationType = OperationType.SELECT,
            subType = "评价跟踪"
    )
    @Operation(summary = "评价结果-主页-点击结果")
    @GetMapping(value = "/pjgl/t08_proj_initiate_task_result")
    public JsonBean t08_proj_org_result(
            @Parameter(name="assId",description="assId") @RequestParam(value = "assId") BigDecimal assId,
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
     final IPage<TblAssessTargetVo> pageBean = this.pjjgService.findMarkByOrgGroupZuPing(assId, staff.getStaffid(), pageNumber,staff);
      //  PageInfo<TblAssessTargetVo> pageBean=pjjgService.findMarkByOrgGroupZuPingNew(assId, staff.getStaffid(), pageNumber);
      // IPageResult<TblAssessTargetVo> pageInfo=new IPageResult<TblAssessTargetVo>().buildIpage(pageBean);
        TblAssess assess = this.tblAssessService.getById(assId);
        int major=tblAssessService.getMajorByAssid(assId,staff.getStaffid().toString());
        Map<String, Object> mv = new HashMap<>();
        mv.put("pageBean", pageBean);
        mv.put("project", assess);
        mv.put("assId", assId);
        mv.put("major", major);
        return new JsonBean(200, "success", mv);

    }

    /**
     * 计算分数---评价对象
     *
     * @param assId
     * @return
     */
    @OperationLog(
            success = "评价结果-计算分数查询成功",
            busType = "内控设置",
            fail = "评价结果-计算分数查询失败",
            operationType = OperationType.SELECT,
            subType = "评价跟踪"
    )
    @Operation(summary = "评价结果-计算分数")
    @PostMapping(value = "/pjgl/calculate")
    public JsonBean calculate(
            @Parameter(name="assId",description="assId") @RequestParam(value = "assId") BigDecimal assId,
            @Parameter(name="orgid",description="orgid") @RequestParam(value = "orgid") BigDecimal orgid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	return this.pjjgService.calculate(assId, orgid,token);
    }

    /**
     * 评价结果 横向
     *
     * @param
     * @return
     */

    @OperationLog(
            success = "评价结果-点击机构-要素明细查询成功",
            busType = "内控设置",
            fail = "评价结果-点击机构-要素明细查询失败",
            operationType = OperationType.SELECT,
            subType = "评价跟踪"
    )
    @Operation(summary = "评价结果-点击机构-要素明细")
    @GetMapping(value = "/pjgl/t08_proj_organ_diap")
    public JsonBean proj_t08_proj_organ_diap(
            @Parameter(name="assId",description="assId") @RequestParam(value = "assId") BigDecimal assId,
            @Parameter(name="orgid",description="orgid") @RequestParam(value = "orgid") BigDecimal orgid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        return this.pjjgService.yaoSuMingXi(assId, orgid);
    }


    @OperationLog(
            success = "评价结果-要素明细-详情查询成功",
            busType = "内控设置",
            fail = "评价结果-要素明细-详情查询失败",
            operationType = OperationType.SELECT,
            subType = "评价跟踪"
    )
    @Operation(summary = "评价结果-要素明细-详情")
    @GetMapping(value = "/pjgl/info_pjjg")
    public JsonBean info_pjjg(
            @Parameter(name="assmarkid",description="assmarkid") @RequestParam(value = "assmarkid") BigDecimal assmarkid,
            @Parameter(name="asscatid",description="asscatid") @RequestParam(value = "asscatid") BigDecimal asscatid,
            @Parameter(name="lb",description="lb",required=false) @RequestParam(value = "lb", required = false) String lb,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        final List<Map<String, Object>> list = this.pjjgService.getPingJiaXiangQing(assmarkid, asscatid);

        List<TblAttachment> listAtt = new ArrayList<>();
        if (list != null && list.size() > 0) {
            // Object[] o = obj.get(0);

            for (Map<String, Object> map : list) {
                System.out.println(map.get("ASSELEID").getClass());
                if (map.get("ATTID") != null && map.get("ATTID").toString() != "") {
                    TblAttachment att = iTblAttachmentService.getOne((BigDecimal) map.get("ATTID"));
                    listAtt.add(att);
                }
            }
        }
        Map<String, Object> mv = new HashMap<>();
        mv.put("obj", list);
        mv.put("listAtt", listAtt);
        mv.put("lb", lb);
        return new JsonBean(200, "success", mv);
    }


    @OperationLog(
            success = "评价结果-要素明细-导出word成功",
            busType = "内控设置",
            fail = "评价结果-要素明细-导出word失败",
            operationType = OperationType.EXPORT,
            subType = "评价跟踪"
    )
    @Operation(summary = "评价结果-要素明细-导出word")
    @GetMapping(value = "/pgjg/expWordFile")
    public void expWordFile(
            HttpServletResponse response,
            @Parameter(name="assId",description="assId") @RequestParam(value = "assId") BigDecimal assId,
            @Parameter(name="orgid",description="orgid") @RequestParam(value = "orgid") BigDecimal orgid

    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ;
        }
        final Map<String, Object> dataMap = this.pjjgService.yaoSuMingXi2(assId, orgid);
       // String FREEMARKER_PATH = request.getSession().getServletContext().getRealPath("/template/doc");
        String fileNameStr = "pjjg.doc";
        String folderPath = this.filedir;
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        String fileName = "/" + uuid + ".doc"; //
        System.out.println("fileName---" + fileName);
        ExportDoc exp = new ExportDoc();
        exp.createDoc("pgjg.xml", fileName, dataMap);
        // 导出Word
        response.setContentType("application/octet-stream;charset=UTF-8");
        FileUtil.downLoad(fileName, response, false, fileNameStr);
        FileUtil.deleteFile(fileName);
    }


    /**
     * @Title: pfjg_export @Description: 评价结果--详情，导出评分列表 @param request @param
     * response @return String @throws
     */
    @OperationLog(
            success = "评价结果-要素明细-导出excel成功",
            busType = "内控设置",
            fail = "评价结果-要素明细-导出excel失败",
            operationType = OperationType.EXPORT,
            subType = "评价跟踪"
    )
    @Operation(summary = "评价结果-要素明细-导出excel")
    @GetMapping(value = "/pjgl/ysmx_export")
    public @ResponseBody
    String ysmx_export(
            @Parameter(name="assid",description="assid") @RequestParam(value = "assid") BigDecimal assid,
            HttpServletResponse response) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return null;
        }
        log.info("内控合规---评价结果---详情---导出Excel");
        response.setContentType("application/octet-stream;charset=UTF-8");
        try {
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String("要素明细".getBytes(), "iso-8859-1") + ".xlsx");
            ServletOutputStream outputStream = response.getOutputStream();
            List<Object[]> objList = this.pjjgService.ysmxExport(assid);
            String[] titles = {"要素编号", "要素名称", "标准分", "审查要点", "评价分", "评价人", "评价依据", "附件名称"};
            ImportOrExportExcelUtil.exportExcel(titles, objList, outputStream, null);
        } catch (Exception e) {
            log.info("内控合规---评价结果---详情---导出Excel失败");
        }
        return null;
    }


    @OperationLog(
            success = "评价结果-预览成功",
            busType = "内控设置",
            fail = "评价结果-预览失败",
            operationType = OperationType.DOWNLOAD,
            subType = "评价跟踪"
    )
    @Operation(summary = "评价结果-预览")
    @GetMapping(value = "/preview")
    public void risk_preview(
            @Parameter(name="reportType",description="reportType") @RequestParam(value = "reportType") String reportType,
            @Parameter(name="id",description="id") @RequestParam(value = "id") String id,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            HttpServletResponse response) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return;
        }

        if (StringUtils.isBlank(reportType)) {
            log.info(staff.getCurrentOrg().getOrgname() + "报告生成type为空");

        }

        TblRiskAttWord trraw = iTblRiskAttWordService.getFile(reportType, staff.getCurrentOrg().getOrgid().toString(), id);
        FtpUtil.pdfPreview(trraw.getFilepath(), trraw.getFilename(), response);

    }

    @OperationLog(
            success = "评价结果-预览-判断文件是否存在200:存在 -1:有问题查询成功",
            busType = "内控设置",
            fail = "评价结果-预览-判断文件是否存在200:存在 -1:有问题查询失败",
            operationType = OperationType.SELECT,
            subType = "评价跟踪"
    )
    @Operation(summary = "评价结果-预览-判断文件是否存在200:存在 -1:有问题")
    @GetMapping(value = "/isfile")

    public JsonBean isfile(
            @Parameter(name="reportType",description="reportType") @RequestParam(value = "reportType") String reportType,
           @Parameter(name="id",description="id") @RequestParam(value = "id") String id,
           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token)
            throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        boolean bool = false;

        if (StringUtils.isBlank(reportType)) {
            log.info(staff.getCurrentOrg().getOrgname() + "报告生成type为空");
            return new JsonBean(-1,staff.getCurrentOrg().getOrgname() + "报告生成type为空",-1);
        }

        TblRiskAttWord trraw = iTblRiskAttWordService.getFile(reportType, staff.getCurrentOrg().getOrgid().toString(), id);
        if (trraw == null) {
            return new JsonBean(-1,"error",-1);
        }
        System.out.println(trraw.getFilepath());
        bool = FtpUtil.isFTPFileExistByFilePath( trraw.getFilepath());
        System.out.println(bool);
        if (bool) {
            return new JsonBean(200,"文件"+trraw.getFilepath()+"存在",0);
        } else {
            return new JsonBean(-1,"文件不存在",-1);
        }

    }
    @OperationLog(
            success = "评价结果-生成报告(涉及太多远程环境,功能无法实现)查询成功",
            busType = "内控设置",
            fail = "评价结果-生成报告(涉及太多远程环境,功能无法实现)查询失败",
            operationType = OperationType.SELECT,
            subType = "评价跟踪"
    )
    @Operation(summary = "评价结果-生成报告(涉及太多远程环境,功能无法实现)")
    @GetMapping(value = "/risk_generate_report")
    public JsonBean risk_generate_report(
            @Parameter(name="reportType",description="reportType") @RequestParam(value = "reportType") String reportType,
            @Parameter(name="id",description="id") @RequestParam(value = "id") String id,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token)
            throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        return new JsonBean(-1,"功能未实现",null);
    }

}

