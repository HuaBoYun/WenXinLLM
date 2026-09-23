package com.huabo.fxgl.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Attachment;
import com.huabo.fxgl.entity.Find;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.RiskAssessmentstd;
import com.huabo.fxgl.entity.RiskAssplan;
import com.huabo.fxgl.entity.RiskAssplanRisk;
import com.huabo.fxgl.entity.RiskLevelmapping;
import com.huabo.fxgl.entity.RiskRiskmarking;
import com.huabo.fxgl.service.IAttachmentService;
import com.huabo.fxgl.service.IOrganizationService;
import com.huabo.fxgl.service.IRiskAssplanRiskService;
import com.huabo.fxgl.service.IRiskAssplanService;
import com.huabo.fxgl.service.IRiskInfludegreeService;
import com.huabo.fxgl.service.IRiskLevelmappingService;
import com.huabo.fxgl.service.IRiskRiskmarkingService;
import com.huabo.fxgl.util.FiexibleNameAssignment;
import com.huabo.fxgl.vo.fieldOrgStaffId;
import com.huabo.fxgl.vo.fieldOrgStaffName;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 风险管控-防线评估-风险任务
 * date:2022.8.8
 * author: xujiajun
 */
@RestController
@Tag(name="风险管控 - 风险评估 - 评估任务",description="风险管控 - 风险评估 - 评估任务")
@RequestMapping
@Slf4j
public class RiskAssTaskController {
    @Autowired
    private IRiskAssplanService riskAssplanService;
    @Autowired
    private IOrganizationService organizationService;
    @Autowired
    private IRiskAssplanRiskService riskAssplanRiskService;
    @Autowired
    private IRiskLevelmappingService riskLevelmappingService;
    @Autowired
    private IRiskInfludegreeService riskInfludegreeService;
    @Autowired
    private IRiskRiskmarkingService riskRiskmarkingService;

    @Autowired
    private IAttachmentService iAttachmentService;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 查询评估任务结果集
     * @param pageNo 默认页数
//     * @param find  公共类
//     * @param riskmarking
     * @param pageSize     每页数据条数
     * @param choiceSearch 查询框显示
     * author xujiajun
     * date 2022.8.9
     * @return
     */
    @OperationLog(
            success = "查询评估任务列表成功",
            busType = "风险评估",
            fail = "查询评估任务列表失败",
            operationType = OperationType.SELECT,
            subType = "评估计划"
    )
    @RequestMapping(value = "/plan/riresultlist",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "评估任务查询 /plan/riresultlist")
    public JsonBean riresultlist(
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1",value="pageNo",required=false) Integer pageNo,
            @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20",value="pageSize",required=false) Integer pageSize,
            @Parameter(name = "name", description = "查询条件-计划名称", required = false) @RequestParam(required = false) String name,
            @Parameter(name = "code", description = "查询条件-计划编号", required = false) @RequestParam(required = false) String code,
            @Parameter(name = "startDate", description = "查询条件-自评完成时间-开始日期", required = false) @RequestParam(required = false) String startDate,
            @Parameter(name = "endDate", description = "查询条件-自评完成时间-结束日期", required = false) @RequestParam(required = false) String endDate,
            @Parameter(name = "status", description = "查询条件-评估状态 0-未评估  1-评估中  2-已评估", required = false) @RequestParam(required = false) String status) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        //封装参数
        Find find = new Find();
        find.setName(name);
        find.setCode(code);
        find.setState(status);
        find.setStartDate(startDate);
        find.setEndDate(endDate);

        
        
        //查询风险任务
        PageInfo<RiskAssplan> pageBean = riskAssplanService.findUserAndRiskLevelById(staffUtil, null,  find, staffUtil.getCurrentOrg().getOrgid(),pageNo,pageSize);
        //根据评估状态排序
//        List<RiskAssplan> recordList = pageBean.getList();
//        for (RiskAssplan riskAssplan : recordList) {
//            // 获取风险评估计划中的风险标记列表
//            List<RiskAssplanRisk> assPlanRisks = riskAssplan.getRiskAssplanRiskList();
//            // 创建一个新的列表，用于存储风险标记
//            List<RiskRiskmarking> list = new ArrayList();
//            // 遍历风险评估计划的风险标记列表
//            for (RiskAssplanRisk riskAssPlanRisk : assPlanRisks) {
//                // 获取当前风险评估计划风险对象关联的风险标记集合
//                Set<RiskRiskmarking> markings = riskAssPlanRisk.getTblRiskRiskMarking();
//                // 如果风险标记集合不为空且有元素
//                if (markings != null && markings.size() > 0)
//                    // 将风险标记集合添加到列表中
//                    list.addAll(markings);
//                Collections.sort(list, new Comparator<RiskRiskmarking>() {
//                    @Override
//                    public int compare(RiskRiskmarking o1, RiskRiskmarking o2) {
//                        return o1.getAsssatus().compareTo(o2.getAsssatus());
//                    }
//                });
//            }
//            if (list.size() > 0) {
//                for (RiskRiskmarking riskMarking : list) {
//                    // 如果当前的风险标记对象（riskMarking）不为空
//                    if (riskMarking != null && riskMarking.getStaff().getStaffid().equals(staffUtil.getStaffid())) {
//                        // 更新风险评估计划（riskAssplan）的状态
//                        // 如果风险标记的评估状态（riskMarking.getAsssatus()）为null，则状态设置为"0"
//                        riskAssplan.setStatus(null == riskMarking.getAsssatus() ? String.valueOf(0) : riskMarking.getAsssatus().toString());
//                    }
//                }
//            }
//            
//            
//            // 设置风险评估计划的单位名称和评估状态
//          riskAssplan.setUnit(null != riskAssplan.getOrganization() ? riskAssplan.getOrganization().getOrgname() : null);
//          riskAssplan.setOrganization(null);
//          riskAssplan.setRiskAssplanRiskList(null);
//          if (null != riskAssplan.getStatus()) {
//              switch (riskAssplan.getStatus()) {
//                  case "0": riskAssplan.setStatus("未评估"); break;
//                  case "1": riskAssplan.setStatus("已保存"); break;
//                  case "2": riskAssplan.setStatus("已评估"); break;
//                  default: riskAssplan.setStatus("未知");
//              }
//          } else {
//              riskAssplan.setStatus("未知");
//          }
//          this.riskAssplanService.setPingguStatusByPlanId(riskAssplan);
//
//        }
        // 设置风险评估计划的单位名称和评估状态
//        for (RiskAssplan riskAssplan : recordList) {
////            System.out.println(riskAssplan.getAssplanid() + " / " + riskAssplan.getStatus());
//            riskAssplan.setUnit(null != riskAssplan.getOrganization() ? riskAssplan.getOrganization().getOrgname() : null);
//            riskAssplan.setOrganization(null);
//            riskAssplan.setRiskAssplanRiskList(null);
//            if (null != riskAssplan.getStatus()) {
//                switch (riskAssplan.getStatus()) {
//                    case "0": riskAssplan.setStatus("未评估"); break;
//                    case "1": riskAssplan.setStatus("已保存"); break;
//                    case "2": riskAssplan.setStatus("已评估"); break;
//                    default: riskAssplan.setStatus("未知");
//                }
//            } else {
//                riskAssplan.setStatus("未知");
////                System.out.println(riskAssplan);
//            }
//            this.riskAssplanService.setPingguStatusByPlanId(riskAssplan);
//        }
        JsonBean jsonBean = null;
        Map<String,Object> result = new HashMap<String,Object>(0);
        result.put("pageBean", pageBean);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }

    /**
     * 需要评估的风险
     * @param pageNo 默认页数
     * @param pageSize 每页数据条数
     * @param token
     * @param planId
     * @return
     * @throws Exception
     */
    @OperationLog(
            success = "查询评估任务明细列表成功",
            busType = "风险评估",
            fail = "查询评估任务明细列表失败",
            operationType = OperationType.SELECT,
            subType = "评估任务"
    )
    @RequestMapping(value = "/plan/ri_result_item_list",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "评估任务明细列表数据 /plan/ri_result_item_list")
    public JsonBean riResultItemList(@Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1",value="pageNo",required=false) Integer pageNo,
            @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20",value="pageSize",required=false) Integer pageSize,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="planId",description="评估任务主键, 必填项",required=true) @RequestParam(required = true,value="planId") String planId) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息

        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        log.info("---------------------------------staffId: " + staffUtil.getStaffid());
        IPage page = new Page(pageNo, pageSize);//设置分页
        IPage pageBean = riskAssplanRiskService.findRiskByAssplanidAndStaffid(new BigDecimal(planId), staffUtil.getStaffid(), page);//通过计划ID进入当前计划marking设置

        List<RiskAssplanRisk> recordList = pageBean.getRecords();

        for (RiskAssplanRisk risk : recordList) {
			for (RiskRiskmarking marking : risk.getTblRiskRiskMarking()) {
				if(marking.getStaff().getStaffid().compareTo(staffUtil.getStaffid())==0) {
					risk.setRiskmarking(marking);
				}

			}
		}

//        if (riskAssplan!=null&& riskAssplan.getPlanStatus()!=null) {
//            String sta = riskAssplan.getPlanStatus();
//            log.info("sta="+sta);
//            result.put("update",sta);
//        }
        JsonBean jsonBean = null;
        Map<String,Object> result = new HashMap<String,Object>(0);
        result.put("pageBean", pageBean);
        result.put("assplanid", planId);
//        result.put("update1", riskAssplan.getPlanStatus() != null ? riskAssplan.getPlanStatus() : String.valueOf(0));
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }

    @OperationLog(
            success = "评估结果明细列表数据查询处理成功",
            busType = "风险评估",
            fail = "评估结果明细列表数据查询处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @RequestMapping(value = "/plan/ri_result_process_detail",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "评估结果明细列表数据 /plan/ri_result_process_detail")
    public JsonBean ri_result_process_detail(@Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1",value="pageNo",required=false) Integer pageNo,
            @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20",value="pageSize",required=false) Integer pageSize,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="planId",description="评估任务主键, 必填项",required=true) @RequestParam(required = true,value="planId") String planId,
            @Parameter(name="level",description="风险等级 1到5",required=true) @RequestParam(required = true,value="level") String level) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        log.info("---------------------------------staffId: " + staffUtil.getStaffid());
        IPage page = new Page(pageNo, pageSize);//设置分页
        IPage pageBean = riskAssplanRiskService.findRiskByRiskAndLevel(new BigDecimal(planId), staffUtil.getStaffid(), page,level);//通过计划ID进入当前计划marking设置

        JsonBean jsonBean = null;
        Map<String,Object> result = new HashMap<String,Object>(0);
        result.put("pageBean", pageBean);
        result.put("assplanid", planId);
        result.put("level", level);
//        result.put("update1", riskAssplan.getPlanStatus() != null ? riskAssplan.getPlanStatus() : String.valueOf(0));
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }

    @OperationLog(
            success = "评估结果明细列表数据查询处理成功",
            busType = "风险评估",
            fail = "评估结果明细列表数据查询处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @RequestMapping(value = "/plan/ri_assessmet_result_disp",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "评估结果明细列表数据 /plan/ri_assessmet_result_disp")
    public JsonBean ri_assessmet_result_disp(@Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1",value="pageNo",required=false) Integer pageNo,
            @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20",value="pageSize",required=false) Integer pageSize,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="riskcheckbox",description="主键拼接",required=true) @RequestParam(required = true,value="riskcheckbox") String riskcheckbox) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String,Object> result = new HashMap<String,Object>(0);
        if(StringUtils.isNotBlank(riskcheckbox)){
			String[] str = riskcheckbox.split(",");
			IPage page = new Page(pageNo, pageSize);//设置分页
	        IPage<RiskAssplanRisk> pageBean = riskAssplanRiskService.fingRiskByAssIdAndRiskId(new BigDecimal(str[0]), new BigDecimal(str[1]), page);//通过计划ID进入当前计划marking设置
	        FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtils.isNotEmpty(pageBean.getRecords())){
				pageBean.getRecords().forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity.getRisk(),item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity, entity.getRisk() ); 
						item=null; // 处理并解除引用
						nameEntity=null; // 处理并解除引用
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
				
			}
	        result.put("pageBean", pageBean);
		}
        JsonBean jsonBean = null;
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }

    /**
     * 查看详情
     * @param planId
     * @param token
     * @return
     * @throws Exception
     */
    @OperationLog(
            success = "查看评估计划详情【{{#planId}}】成功",
            busType = "风险评估",
            fail = "查看评估计划详情【{{#planId}}】失败",
            operationType = OperationType.SELECT,
            subType = "评估计划"
    )
    @RequestMapping(value = "/pggl/riplaninfo",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "查看详情 /pggl/riplaninfo")
    public JsonBean riplanInfo(@Parameter(name="planId",description="评估主键, 必填项",required=true) @RequestParam(required = true) String planId,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        RiskAssplan riskAssplan = riskAssplanService.getById(planId);
        JsonBean jsonBean = null;
        Map<String,Object> result = new HashMap<String,Object>(0);
        if (null != riskAssplan) {
        	Set<Attachment> tblAttachments =iAttachmentService.getRiskAssplanAttList(planId);
            riskAssplan.setTblAttachments(tblAttachments);
            FiexibleNameAssignment ment=new FiexibleNameAssignment();
        	//对灵活字段中的姓名名称及机构名称赋值
			fieldOrgStaffId item=new fieldOrgStaffId();
			BeanUtils.copyProperties(riskAssplan,item); 
			fieldOrgStaffName nameEntity=ment.setOpenName(item);
			BeanUtils.copyProperties(nameEntity,riskAssplan ); 
            List<Organization> orgByUser = organizationService.findOrgByUser(staffUtil.getUsername());
            List<RiskAssplanRisk> listriskAssPlanRisk = riskAssplanRiskService.findRiskByRiskid(riskAssplan.getAssplanid());
//            result.put("orgByUser", orgByUser.size() == 1 ? orgByUser.get(0) : "");
//            result.put("staff", staffUtil);
			if(CollectionUtils.isNotEmpty(listriskAssPlanRisk)){
				listriskAssPlanRisk.forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId field=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,field); 
						fieldOrgStaffName nameEntity1=ment.setOpenName(field);
						BeanUtils.copyProperties(nameEntity1,entity ); 
						field=null; // 处理并解除引用
						nameEntity1=null; // 处理并解除引用
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
			}
            result.put("RiskAssplan", riskAssplan);
            result.put("listriskAssPlanRisk", listriskAssPlanRisk);
        }
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }

    /**
     * 保存评分结果
     * @param value
     * @param assplanid
     * @param token
     * @return
     * @throws Exception
     */
    @OperationLog(
            success = "评估任务-评估功能成功",
            busType = "风险评估",
            fail = "评估任务-评估失败",
            operationType = OperationType.UPDATE,
            subType = "评估任务"
    )
    @RequestMapping(value = "/plan/ri_result_save",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "保存评分 /plan/ri_result_save")
    public JsonBean ri_result_item_save(
    		@Parameter(name="value",description="评分机构集数组，发生频率：1-很低 2-较低 3-中等 4-较高 5-很高；严重程度：1-很低 2-较低 3-中等 4-较高 5-很高；默认值均为1",required=true) @RequestParam(required = true,value="value") String value,
    		@Parameter(name="assplanid",description="评估任务主键, 必填项",required=true) @RequestParam(required = true) String assplanid,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String ,Object> hashmap=new HashMap<String, Object>();
        if (StringUtils.isNotBlank(value)) {
            RiskAssplan assplan = riskAssplanService.getById(assplanid);
            RiskAssessmentstd riskAssessmentstd = assplan.getAssessmentstd();
            if (null != riskAssessmentstd) {
                String[] papm = value.split(",");
                int i = 0;
                for (int j = 0; j < papm.length / 3; j++) {
                    //获取markingid
                    BigDecimal decimal = new BigDecimal(papm[i]);
                    RiskRiskmarking riskmarking = riskRiskmarkingService.getById(decimal);
                    if(null == riskmarking) {
                    	continue;
                    }
                    try {
                    if (!BigDecimal.valueOf(2).equals(riskmarking.getAsssatus())) {
                        i += 1;
                        //风险严重程度
                        riskmarking.setSeverity(Integer.parseInt(papm[i]));
                        //查询风险影响程度id
                        BigDecimal d = riskInfludegreeService.getInflu(riskAssessmentstd.getAssstdid(), papm[i]);
                        //返回风险等级信息
                        RiskLevelmapping riskLevelmapping = riskLevelmappingService.getRiskLevelMappingBymentIdAndDegreeId(riskAssessmentstd.getAssstdid(), d.toString());
                        i += 1;
                        //设置发生频率
                        riskmarking.setFrequency(Integer.parseInt(papm[i]));
                        if (riskLevelmapping != null) {
                            //根据发生频率得出风险等级
                            riskmarking.setRisklevel(getFXDJ(riskLevelmapping, Integer.valueOf(papm[i])));
                        }
                        riskmarking.setAssdate(LocalDateTime.now());
                        riskmarking.setAsssatus(BigDecimal.valueOf(1));//已保存
                        riskRiskmarkingService.updateById(riskmarking);
                        i += 1;
                    } else {
                        i += 3;
                    }
                    } catch (Exception e) {
						// TODO: handle exception
                    	e.printStackTrace();
					}
                }
                return ResponseFormat.retParam(1, 200, null);
            }
        }
        return ResponseFormat.retParam(0, "保存失败", null);
    }

    /**
     * 定义方法以此确定风险等级 保存和提交评分时使用
     * @param levelMapping
     * @param level
     * @return
     */
    private String getFXDJ(RiskLevelmapping levelMapping, Integer level) {
        switch (level) {
            case 1:
                return levelMapping.getPoss1();
            case 2:
                return levelMapping.getPoss2();
            case 3:
                return levelMapping.getPoss3();
            case 4:
                return levelMapping.getPoss4();
            case 5:
                return levelMapping.getPoss5();
            default:
                return "1";
        }
    }

    /**
     * 提交评分结果
     * @param value
     * @param assplanid
     * @param token
     * @return
     * @throws Exception
     */
    @OperationLog(
            success = "提交评分成功",
            busType = "风险评估",
            fail = "提交评分失败",
            operationType = OperationType.UPDATE,
            subType = "评估任务"
    )
    @RequestMapping(value = "/plan/ri_result_submit",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "提交评分 /plan/ri_result_submit")
    public JsonBean ri_result_item_submit(
    		@Parameter(name="value",description="评分机构集数组，发生频率：1-很低 2-较低 3-中等 4-较高 5-很高；严重程度：1-很低 2-较低 3-中等 4-较高 5-很高；默认值均为1",required=true) @RequestParam(required = true,value="value") String value,
    		@Parameter(name="assplanid",description="评估任务主键, 必填项",required=true) @RequestParam(required = true) String assplanid,
                                        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        if (StringUtils.isNotBlank(value)) {
            String[] papm = value.split(",");
            int i = 0;
            RiskAssplan assplan = riskAssplanService.getById(assplanid);
            RiskAssessmentstd riskAssessmentstd = assplan.getAssessmentstd();
            if (null != riskAssessmentstd) {
                for (int j = 0; j < papm.length / 3; j++) {
                    //取markingid
                    BigDecimal d = new BigDecimal(papm[i]);
                    RiskRiskmarking riskmarking = riskRiskmarkingService.getById(d);
                    i += 1;
                    //风险严重程度
                    riskmarking.setSeverity(Integer.parseInt(papm[i]));
                    //查询风险影响程度id
                    BigDecimal decimal = riskInfludegreeService.getInflu(riskAssessmentstd.getAssstdid(), papm[i]);
                    //返回风险等级信息
                    RiskLevelmapping riskLevelmapping = riskLevelmappingService
                            .getRiskLevelMappingBymentIdAndDegreeId(riskAssessmentstd.getAssstdid(), decimal.toString());
                    i += 1;
                    //发生频率
                    riskmarking.setFrequency(Integer.parseInt(papm[i]));
                    if (riskLevelmapping != null) {
                        //根据发生频率得出风险等级
                        riskmarking.setRisklevel(getFXDJ(riskLevelmapping, Integer.valueOf(papm[i])));
                    }
                    riskmarking.setAssdate(LocalDateTime.now());
                    riskmarking.setAsssatus(BigDecimal.valueOf(2));
                    riskRiskmarkingService.updateById(riskmarking);
                    i += 1;
                }
                List<RiskRiskmarking> list = riskRiskmarkingService.checkSubmit(new BigDecimal(assplanid));
                if (list.size() == 0) {
                    log.info("风险已经全部评估完成,开始计算风险等级");
                    List<RiskAssplanRisk> assPlanRisks = riskAssplanRiskService.findRiskByRiskid(assplan.getAssplanid());
                    for (RiskAssplanRisk riskAssPlanRisk : assPlanRisks) {
                        //获取所有的风险
                        Set<RiskRiskmarking> markings = riskAssPlanRisk.getTblRiskRiskMarking();
                        Double frequency = 0d;
                        Double severity = 0d;
                        for (RiskRiskmarking riskMarking : markings) {
                            //获取所有评估人员通过所占权重对风险的评估
                            frequency += ((riskMarking.getFrequency()) * (riskMarking.getAssweight() / 100));
                            severity += ((riskMarking.getSeverity()) * (riskMarking.getAssweight() / 100));
                        }
                        //向上取整
                        int intFrequency = (int) Math.ceil(frequency);
                        int intSeverity = (int) Math.ceil(severity);
                        riskAssPlanRisk.setFrequency(BigDecimal.valueOf(intFrequency));
                        riskAssPlanRisk.setSeverity(BigDecimal.valueOf(intSeverity));
                        BigDecimal decimal = riskInfludegreeService.getInflu(riskAssessmentstd.getAssstdid(),
                                intSeverity + "");
                        RiskLevelmapping levelMapping = riskLevelmappingService.getRiskLevelMappingBymentIdAndDegreeId(
                                riskAssessmentstd.getAssstdid(), decimal.toString());
                        if (levelMapping != null) {
                            riskAssPlanRisk.setRisklevel(getFXDJ(levelMapping, intFrequency));
                            //设置更新时间
                            riskAssPlanRisk.setAssdate(LocalDateTime.now());
                            //计算更新风险等级
                            riskAssplanRiskService.updateById(riskAssPlanRisk);
                        }
                    }
                    assplan.setPlanStatus(RiskAssplan.PLANSTATUS_YWC);
                    //更新riskAssplan状态为已完成
                    riskAssplanService.updateById(assplan);
                    log.info("风险等级计算结束");
                } else {
                    log.info("还有" + list.size() + "位没有评估");
                    return ResponseFormat.retParam(1, "还有" + list.size() + "位没有评估", null);
                }
                return ResponseFormat.retParam(1, 200, null);
            }
        }
        return ResponseFormat.retParam(0, "保存失败", null);
    }
    
    
    //重大风险-月度评估：操作列增加
    @OperationLog(
            success = "查看该风险的所有风险评估信息成功",
            busType = "重大风险",
            fail = "查看该风险的所有风险评估信息失败",
            operationType = OperationType.SELECT,
            subType = "月度评估"
    )
    @RequestMapping(value = "/plan/resultProcessDetailMon",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "评估结果明细列表数据 /plan/resultProcessDetailMon")
    public JsonBean resultProcessDetailMon(@Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1",value="pageNo",required=false) Integer pageNo,
            @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20",value="pageSize",required=false) Integer pageSize,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "riskId", description = "风险主键, 必填项", required = true) @RequestParam(required = true,value="riskId") BigDecimal riskId) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        log.info("---------------------------------staffId: " + staffUtil.getStaffid());
        IPage page = new Page(pageNo, pageSize);//设置分页
        IPage pageBean = riskAssplanRiskService.findRiskResultById(riskId, staffUtil.getStaffid(), page);

        JsonBean jsonBean = null;
        Map<String,Object> result = new HashMap<String,Object>(0);
        result.put("pageBean", pageBean);
        result.put("riskId", riskId);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }

    
    
    
}
