package com.huabo.fxgl.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hbfk.util.ResponseFormat;
import com.huabo.fxgl.vo.RISKTOP10;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.config.SysConfig;
import com.huabo.fxgl.entity.Attachment;
import com.huabo.fxgl.entity.Controlmatrix;
import com.huabo.fxgl.entity.Flow;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Risk;
import com.huabo.fxgl.entity.RiskCoping;
import com.huabo.fxgl.entity.RiskCopingCmatrix;
import com.huabo.fxgl.entity.Riskcategory;
import com.huabo.fxgl.entity.Staff;
import com.huabo.fxgl.entity.TblControlEntries;
import com.huabo.fxgl.mapper.TblControlEntriesMapper;
import com.huabo.fxgl.service.IAttachmentService;
import com.huabo.fxgl.service.IControlmatrixService;
import com.huabo.fxgl.service.IFlowService;
import com.huabo.fxgl.service.IOrganizationService;
import com.huabo.fxgl.service.IRiskCopingCmatrixService;
import com.huabo.fxgl.service.IRiskCopingService;
import com.huabo.fxgl.service.IRiskService;
import com.huabo.fxgl.service.IStaffService;
import com.huabo.fxgl.service.impl.RiskcategoryServiceImpl;
import com.huabo.fxgl.util.FiexibleNameAssignment;
import com.huabo.fxgl.vo.fieldOrgStaffId;
import com.huabo.fxgl.vo.fieldOrgStaffName;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 *  风险应对模块前端控制器
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@RestController
@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
@Tag(name="风险管控 - 风险应对管理",description="风险管控 - 风险应对管理")
public class RiskCopingController {

    @Autowired
    private RiskcategoryServiceImpl riskcategoryService;
    @Autowired
    private IRiskCopingCmatrixService riskCopingCmatrixService;
    @Autowired
    private IRiskService riskService;
    @Autowired
    private IRiskCopingService copingService;
    @Autowired
    private IOrganizationService organizationService;
    @Autowired
    private IStaffService staffService;
    @Autowired
    private IControlmatrixService controlmatrixService;
    @Autowired
    private IAttachmentService attachmentService;
    
    @Autowired
    private TblControlEntriesMapper  tblControlEntriesMapper;
    
    @Resource
    private UserProvider userProvider;

    @Autowired
    private IFlowService flowService;
	@Value("${application.administrators:}")
	private String administrators;

    /**
     * 保存风险应对
     */
    @OperationLog(
            success = "风险应对 - 保存处理成功",
            busType = "风险管控",
            fail = "风险应对 - 保存处理失败",
            operationType = OperationType.ADD,
            subType = "风险应对管理"
    )
    @Operation(summary = "风险应对 - 保存 /fxxt/fxyd/strategy_reply_update")
    @PostMapping(value = "/fxxt/fxyd/strategy_reply_update")
    public JsonBean updatestrategreply(
                                   @Parameter(name = "riskid", description = "风险ID, 必须传", required = true) @RequestParam(required = true) String riskid,
                                   @Parameter(name = "copingId", description = "风险应对ID", required = false) @RequestParam(required = false) String copingId,
                                   @Parameter(name = "copingPlot", description = "风险应对策略类型, 数字0-4", required = true) @RequestParam(required = true) String copingPlot,
                                   @Parameter(name = "riskHopeValue", description = "风险期望值, 数字", required = false) @RequestParam(required = true) String riskHopeValue,
                                   @Parameter(name = "userId", description = "风险应对负责人ID, 数字", required = true) @RequestParam(required = true) String userId,
                                   @Parameter(name = "yddes", description = "应对方案描述", required = false) @RequestParam(required = false) String yddes,
                                   @Parameter(name = "reporteds", description = "reporteds") @RequestParam(required = false) String reporteds,
                                   @Parameter(name = "removeReporteds", description = "removeReporteds 上传附件ids", required = false) @RequestParam String removeReporteds,
                                   @Parameter(name = "content", description = "富文本框", required = false) @RequestParam String content,
                                   @Parameter(name = "conmatid", description = "风险控制点ID, 新增忽略, 修改必传", required = false) @RequestParam(value="conmatid",required = false) String conmatid,
                                   @Parameter(name = "controlnumber", description = "*风险控制点编号", required = true) @RequestParam(value="controlnumber",required = true) String controlnumber,
                                   @Parameter(name = "controlmanager", description = "*控制责任人;传入用户真实姓名", required = true) @RequestParam(value="controlmanager",required = true) String controlmanager,
                                   @Parameter(name = "toplevelflowcat", description = "流程分类 传入流程名称", required = false) @RequestParam(value="toplevelflowcat",required = false) String toplevelflowcat,
                                   @Parameter(name = "controlfrequency", description = "控制频率", required = false) @RequestParam(value="controlfrequency",required = false) String controlfrequency,
                                   @Parameter(name = "controltype", description = "控制类型  1-预防性控制  2-发现性控制  3-纠正性控制", required = false) @RequestParam(value="controltype",required = false) String controltype,
                                   @Parameter(name = "controlmethod", description = "控制手段  1-手工，2-自动  3-依赖手工的自动化", required = false) @RequestParam(value="controlmethod",required = false) String controlmethod,
                                   @Parameter(name = "keycontrol", description = "是否关键控制  1-是  2-否", required = false) @RequestParam(value="keycontrol",required = false) String keycontrol,
                                   @Parameter(name = "effective", description = "控制是否有效  1-是  2-否", required = false) @RequestParam(value="effective",required = false) String effective,
                                   @Parameter(name = "controltest", description = "是否进行控制测试  1-是  2-否", required = false) @RequestParam(value="controltest",required = false) String controltest,
                                   @Parameter(name = "financialreportidentify", description = "财务报表认定  1-存在与发生   2-完整性  3-权利与义务  4-估价与分摊  5-表达与披露", required = false) @RequestParam(value="financialreportidentify",required = false) String financialreportidentify,
                                   @Parameter(name = "controldes", description = "*风险控制点描述", required = true) @RequestParam(value="controldes",required = true) String controldes,
                                   @Parameter(name = "conkzcs", description = "*控制措施", required = true) @RequestParam(value="conkzcs",required = true) String conkzcs,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(401, "未查到用户登录信息，用户登录信息或已过期", null);
        }
        Risk risk = null;
        if (StringUtils.isEmpty(riskid) || (risk = riskService.getById(riskid)) == null) {
            return new JsonBean(400, "未提供准确的riskid，无法继续", null);
        }
        QueryWrapper<RiskCoping> queryWrapper = new QueryWrapper<RiskCoping>();
        queryWrapper.eq("RISKID", risk.getRiskid());
        List<RiskCoping> list = copingService.list(queryWrapper);
        if (list != null && list.size()>0) {
            if(StringUtils.isNotEmpty(copingId) && StringUtils.isNotEmpty(riskHopeValue)){
                RiskCoping riskCoping =  copingService.getById(copingId);
                Staff tblStaff = staffService.getById(userId);
                if(null==tblStaff){
                    return new JsonBean(400, "风险应对负责人ID错误，无法继续", null);
                }
                riskCoping.setRiskhopevalue(Integer.valueOf(riskHopeValue));
                riskCoping.setCopinghead(userId.toString());
                riskCoping.setCopingplot(copingPlot);
                riskCoping.setYddes(yddes);
                riskCoping.setContent(content);
                System.out.println("1111111111111111111111111111111117777777777");
                copingService.updateById(riskCoping,reporteds);
                if (StringUtils.isNotEmpty(removeReporteds)){
                    attachmentService.removeByIds(Arrays.asList(removeReporteds.split(",")));
                }
                //放入控制措施信息 需求变更 改为独立保存
//                Controlmatrix controlmatrix = new Controlmatrix();
//                if(conmatid != null && !"".equals(conmatid)) {
//                    controlmatrix.setConmatid(new BigDecimal(conmatid));
//                }
//                controlmatrix.setControlnumber(controlnumber);
//                controlmatrix.setControlmanager(controlmanager);
//                controlmatrix.setToplevelflowcat(toplevelflowcat);
//                controlmatrix.setControlfrequency(controlfrequency);
//                controlmatrix.setControltype(controltype);
//                controlmatrix.setControlmethod(controlmethod);
//                controlmatrix.setKeycontrol(keycontrol);
//                controlmatrix.setEffective(effective);
//                controlmatrix.setControltest(controltest);
//                controlmatrix.setFinancialreportidentify(financialreportidentify);
//                controlmatrix.setConkzcs(conkzcs);
//                controlmatrix.setControldes(controldes);
//                //controlmatrix.setRiskcopingid(riskCoping.getRiskcopingid());
//                //监控措施新增
//                controlmatrixService.saveOrUpdate(controlmatrix);//保存控制点信息
                //riskCopingCmatrixService.save(new RiskCopingCmatrix(controlmatrix.getConmatid(),new BigDecimal(copingId)));//保存中间表数据
                return new JsonBean(200, "成功", riskCoping);
            }
        } else{
            RiskCoping riskCoping = new RiskCoping();
            riskCoping.setRiskhopevalue(Integer.valueOf(riskHopeValue));
            riskCoping.setCopinghead(userId);
            riskCoping.setCopingplot(copingPlot);
            riskCoping.setYddes(yddes);
            riskCoping.setContent(content);
            riskCoping.setRiskid(new BigDecimal(riskid));
            riskCoping.setUnit(staffUtil.getLinkOrg().getOrgid());
            riskCoping.setCreatedate(new Date());
            copingService.save(riskCoping,reporteds);
            //放入控制措施信息 需求变更 改为独立保存
//            Controlmatrix controlmatrix = new Controlmatrix();
//            if(conmatid != null && !"".equals(conmatid)) {
//                controlmatrix.setConmatid(new BigDecimal(conmatid));
//            }
//            controlmatrix.setControlnumber(controlnumber);
//            controlmatrix.setControlmanager(controlmanager);
//            controlmatrix.setToplevelflowcat(toplevelflowcat);
//            controlmatrix.setControlfrequency(controlfrequency);
//            controlmatrix.setControltype(controltype);
//            controlmatrix.setControlmethod(controlmethod);
//            controlmatrix.setKeycontrol(keycontrol);
//            controlmatrix.setEffective(effective);
//            controlmatrix.setControltest(controltest);
//            controlmatrix.setFinancialreportidentify(financialreportidentify);
//            controlmatrix.setConkzcs(conkzcs);
//            controlmatrix.setControldes(controldes);
//            //controlmatrix.setRiskcopingid(riskCoping.getRiskcopingid());
//            //监控措施新增
//            controlmatrixService.saveOrUpdate(controlmatrix);//保存控制点信息
//            riskCopingCmatrixService.save(new RiskCopingCmatrix(controlmatrix.getConmatid(),riskCoping.getRiskcopingid()));//保存中间表数据
            return new JsonBean(200, "成功", riskCoping);
        }
        return new JsonBean(400, "数据不完整，请核实后重试", null);
    }

	/**
	 * 保存/更新 风险应对-控制措施信息
	 */
    @OperationLog(
            success = "风险应对新增/修改",
            busType = "风险识别",
            fail = "风险应对新增/修改",
            operationType = OperationType.UPDATE,
            subType = "风险创建"
    )
	@Operation(summary = "风险应对 - 保存/更新 风险应对-控制措施信息 /fxxt/fxyd/strategy_reply_update/update_control_measures")
	@PostMapping(value = "/fxxt/fxyd/strategy_reply_update/update_control_measures")
	@Transactional(rollbackFor = Exception.class)
	public JsonBean saveOrUpdateControlMatrix(@RequestBody Controlmatrix param,@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		BigDecimal commatid = param.getConmatid();
		try {
		TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
	        if (staffUtil == null) {
	            return new JsonBean(401, "未查到用户登录信息，用户登录信息或已过期", null);
	        }
//		//管控措施自动修改添加到控制措施
//		if(StringUtils.isNotBlank(param.getExtJson())){
//			 StringBuffer conkzcsBuffer=new StringBuffer();
//	        JSONArray jsonArray = new JSONArray(param.getExtJson());
//	        for (int i = 0; i < jsonArray.size(); i++) {
//	            JSONObject jsonObject = jsonArray.getJSONObject(i);
//	            conkzcsBuffer.append("具体控制措施："+jsonObject.getStr("field1")+"\n");
//	            conkzcsBuffer.append("预计完成时间："+jsonObject.getStr("field2")+"\n");
//	            conkzcsBuffer.append("责任人："+jsonObject.getStr("field3")+"\n");
//	        }
//	        param.setConkzcs(conkzcsBuffer.toString());
//		}
		controlmatrixService.saveOrUpdate(param);
		if (Objects.isNull(commatid)){
			riskCopingCmatrixService.save(new RiskCopingCmatrix(param.getConmatid(), param.getRiskcopingid()));//保存中间表数据
		}
		//管控措施条目信息保存；
		List<TblControlEntries> entList = param.getEntries();
		 for(TblControlEntries e:entList){
			 if(e.getId()!=null&&e.getId().compareTo(new BigDecimal(0))!=0){
				 tblControlEntriesMapper.updateById(e);
			 }else{
				 e.setId(RandomUtil.uuBigDecimalId());
				 e.setConmatid(param.getConmatid());
				 e.setLinkDeptId(staffUtil.getLinkDetp().getOrgid());
				 e.setLinkOrgId(staffUtil.getLinkOrg().getOrgid());
				 e.setCreateTime(new Date());
				 e.setCreator(staffUtil.getStaffid());
				 tblControlEntriesMapper.insert(e);
			 }
		 }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new JsonBean(200, "成功", param);
	}
     
    

	/**
	 * 删除 风险应对-控制措施信息
	 */
    @OperationLog(
            success = "一体化管控措施【{{#id}}】删除成功",
            busType = "风险识别",
            fail = "一体化管控措施【{{#id}}】删除失败",
            operationType = OperationType.DELETE,
            subType = "风险创建"
    )
	@Operation(summary = "风险应对 - 删除 风险应对-控制措施信息 /fxxt/fxyd/strategy_reply_update/delete_control_measures")
	@DeleteMapping(value = "/fxxt/fxyd/strategy_reply_update/delete_control_measures/{id}")
	public JsonBean deleteControlMatrix(@PathVariable Integer id){
		controlmatrixService.removeById(id);
		tblControlEntriesMapper.deleteEntries(new BigDecimal(id));
		return new JsonBean(200, "成功", null);
	}

    /**
     * 风险应对详查询
     */
    @OperationLog(
            success = "查看风险应对",
            busType = "查看风险识别内容",
            fail = "查看风险应对内容",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @Operation(summary = "风险应对 - 详情查询 /fxxt/fxyd/strategy_reply_info")
    @RequestMapping(value = "/fxxt/fxyd/strategy_reply_info")
    public JsonBean strategy_reply_info(@Parameter(name = "riskid", description = "风险ID", required = true) @RequestParam(required = true) String riskid,
                                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(401, "未查到用户登录信息，用户登录信息或已过期", null);
        }
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        Risk risk = null;
        if (StringUtils.isEmpty(riskid) || (risk = riskService.getById(riskid)) == null) {
            return new JsonBean(400, "未提供准确的riskid，无法继续", null);
        }

        risk.setSsjgName(organizationService.getById(risk.getUnit()).getOrgname());
        risk.setLevel(riskService.getMaxLevelById(risk.getRiskid()));
        Risk risk2 =  riskService.getById(risk.getRiskid());
        if(risk2!=null&&risk2.getRiskextid()!=null){
        	risk.setRiskextname(riskService.getById(risk2.getRiskextid()).getRiskname());
        }
        FiexibleNameAssignment ment=new FiexibleNameAssignment();
    	//对灵活字段中的姓名名称及机构名称赋值
        if(risk!=null){
		fieldOrgStaffId item=new fieldOrgStaffId();
		BeanUtils.copyProperties(risk,item); 
		fieldOrgStaffName nameEntity=ment.setOpenName(item);
		BeanUtils.copyProperties(nameEntity,risk ); 
        }
        QueryWrapper<RiskCoping> queryWrapper = new QueryWrapper<RiskCoping>();
        queryWrapper.eq("RISKID", risk.getRiskid());
        List<RiskCoping> copings = copingService.list(queryWrapper);

        Map<String, Object> result = new HashMap<String, Object>();
        if(null!=copings && copings.size()>0){
            RiskCoping coping = copings.get(0);
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(coping,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,coping ); 
            result.put("copings", coping);
            if(StringUtils.isNotBlank(coping.getCopinghead())){
                result.put("userName", staffService.getById(new BigDecimal(coping.getCopinghead())).getRealname());
            }
            List<Controlmatrix> cons = controlmatrixService.findTblControlmatrixByRiskCoping(coping.getRiskcopingid().toString());
			if(CollectionUtils.isNotEmpty(cons)){
				cons.forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item1=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item1); 
						fieldOrgStaffName nameEntity1=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity1,entity ); 
						item1=null; // 处理并解除引用
						nameEntity1=null; // 处理并解除引用
						List<TblControlEntries> entitys=tblControlEntriesMapper.getList(entity.getConmatid());
						entity.setEntries(entitys);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
				
			}
			
            result.put("controls", cons);
            List<Attachment> atts = attachmentService.findAttachmentByCoping(coping.getRiskcopingid().toString());
            result.put("attachments", atts);
        }
        result.put("risk", risk);
        return new JsonBean(200, "成功", result);
    }

    
    @OperationLog(
            success = "审批查询风险应对",
            busType = "审批查看风险应对内容",
            fail = "审批查询风险应对",
            operationType = OperationType.SELECT,
            subType = "风险应对"
    )
    @Operation(summary = "风险应对 - 详情查询 /fxxt/fxyd/strategy_reply_spinfo")
    @RequestMapping(value = "/fxxt/fxyd/strategy_reply_spinfo")
    public JsonBean strategy_reply_spinfo(@Parameter(name = "riskcopingid", description = "应对ID", required = true) @RequestParam(required = true) String riskcopingid,
                                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(401, "未查到用户登录信息，用户登录信息或已过期", null);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        RiskCoping cop = copingService.getById(riskcopingid);
        if(cop!=null){
         Risk risk = null;
         if (cop.getRiskid()==null|| (risk = riskService.getById(cop.getRiskid())) == null) {
             return new JsonBean(400, "未提供准确的riskid，无法继续", null);
         }

        risk.setSsjgName(organizationService.getById(risk.getUnit()).getOrgname());
        risk.setLevel(riskService.getMaxLevelById(risk.getRiskid()));
        Risk risk2 =  riskService.getById(risk.getRiskid());
        if(risk2!=null&&risk2.getRiskextid()!=null){
        	risk.setRiskextname(riskService.getById(risk2.getRiskextid()).getRiskname());
        }
        FiexibleNameAssignment ment=new FiexibleNameAssignment();
    	//对灵活字段中的姓名名称及机构名称赋值
        if(risk!=null){
		fieldOrgStaffId item=new fieldOrgStaffId();
		BeanUtils.copyProperties(risk,item); 
		fieldOrgStaffName nameEntity=ment.setOpenName(item);
		BeanUtils.copyProperties(nameEntity,risk ); 
        }
        QueryWrapper<RiskCoping> queryWrapper = new QueryWrapper<RiskCoping>();
        queryWrapper.eq("RISKID", risk.getRiskid());
        List<RiskCoping> copings = copingService.list(queryWrapper);

    
        if(null!=copings && copings.size()>0){
            RiskCoping coping = copings.get(0);
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item=new fieldOrgStaffId();
						BeanUtils.copyProperties(coping,item); 
						fieldOrgStaffName nameEntity=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity,coping ); 
            result.put("copings", coping);
            if(StringUtils.isNotBlank(coping.getCopinghead())){
                result.put("userName", staffService.getById(new BigDecimal(coping.getCopinghead())).getRealname());
            }
            List<Controlmatrix> cons = controlmatrixService.findTblControlmatrixByRiskCoping(coping.getRiskcopingid().toString());
			if(CollectionUtils.isNotEmpty(cons)){
				cons.forEach(entity->{
					try {
						//对灵活字段中的姓名名称及机构名称赋值
						fieldOrgStaffId item1=new fieldOrgStaffId();
						BeanUtils.copyProperties(entity,item1); 
						fieldOrgStaffName nameEntity1=ment.setOpenName(item);
						BeanUtils.copyProperties(nameEntity1,entity ); 
						item1=null; // 处理并解除引用
						nameEntity1=null; // 处理并解除引用
						List<TblControlEntries> entitys=tblControlEntriesMapper.getList(entity.getConmatid());
						entity.setEntries(entitys);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
				
			}
            result.put("controls", cons);
            List<Attachment> atts = attachmentService.findAttachmentByCoping(coping.getRiskcopingid().toString());
            result.put("attachments", atts);
        }
        result.put("risk", risk);
        }
        return new JsonBean(200, "成功", result);
    }
    
    
    /**
     * 风险应对明细（通过主键查询）
     */
    @OperationLog(
            success = "风险应对 - 详情查询 处理成功",
            busType = "风险管控",
            fail = "风险应对 - 详情查询 处理失败",
            operationType = OperationType.SELECT,
            subType = "风险应对管理"
    )
    @Operation(summary = "风险应对 - 详情查询 /fxxt/fxyd/strategy_reply_info_byid")
    @RequestMapping(value = "/fxxt/fxyd/strategy_reply_info_byid")
    public JsonBean strategy_reply_info_byid(@Parameter(name = "riskcopingid", description = "风险应对ID", required = true) @RequestParam(required = true) String riskcopingid,
                                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(401, "未查到用户登录信息，用户登录信息或已过期", null);
        }
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        RiskCoping rc = copingService.getById(riskcopingid);
        String riskid = rc.getRiskid().toString();

        Risk risk = null;

		if (StringUtils.isEmpty(riskid) || (risk = riskService.getById(riskid)) == null) {
            return new JsonBean(400, "未提供准确的riskid，无法继续", null);
        }
        risk.setSsjgName(organizationService.getById(risk.getUnit()).getOrgname());
        risk.setLevel(riskService.getMaxLevelById(risk.getRiskid()));
        QueryWrapper<RiskCoping> queryWrapper = new QueryWrapper<RiskCoping>();
        queryWrapper.eq("RISKID", risk.getRiskid());
        List<RiskCoping> copings = copingService.list(queryWrapper);

        Map<String, Object> result = new HashMap<String, Object>();
        if(null!=copings && copings.size()>0){
            RiskCoping coping = copings.get(0);
            result.put("copings", coping);
            if(StringUtils.isNotBlank(coping.getCopinghead())){
                result.put("userName", staffService.getById(new BigDecimal(coping.getCopinghead())).getRealname());
            }
            List<Controlmatrix> cons = controlmatrixService.findTblControlmatrixByRiskCoping(coping.getRiskcopingid().toString());
            if(CollectionUtils.isNotEmpty(cons)){
				cons.forEach(entity->{
					try {
						List<TblControlEntries> entitys=tblControlEntriesMapper.getList(entity.getConmatid());
						entity.setEntries(entitys);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				} );
				
			}
            result.put("controls", cons);
            List<Attachment> atts = attachmentService.findAttachmentByCoping(coping.getRiskcopingid().toString());
            result.put("attachments", atts);
        }

        //当前风险关联的业务单元信息
        Flow flow = flowService.findFlowByRiskId(riskid.toString());
        risk.setFlow(flow);
        result.put("flow", flow);

        result.put("risk", risk);
        return new JsonBean(200, "成功", result);
    }

    @OperationLog(
            success = "风险应对 - 列表查询 处理成功",
            busType = "风险管控",
            fail = "风险应对 - 列表查询 处理失败",
            operationType = OperationType.SELECT,
            subType = "风险应对管理"
    )
    @RequestMapping(value = "/fxxt/fxyd/strategylist")
    @Operation(summary = "风险应对 - 列表查询 /fxxt/fxyd/strategylist")
    public JsonBean strategylist(@Parameter(name = "riskcatid", description = "风险类别ID", required = false) @RequestParam(required = false) String riskcatid,
                              @Parameter(name = "risknumber", description = "风险编号关键字") @RequestParam(required = false) String risknumber,
                              @Parameter(name = "riskname", description = "风险名称关键字") @RequestParam(required = false) String riskname,
							  @Parameter(name = "status", description = "审批状态") @RequestParam(required = false) String status,
                              @Parameter(name = "pageNo", description = "pageNo") @RequestParam(defaultValue = "1") Integer pageNo,
                              @Parameter(name = "pageSize", description = "pageSize") @RequestParam(defaultValue = "20") Integer pageSize,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(401, "未查到用户登录信息，用户登录信息或已过期", null);
        }
		Integer authorityType;
		if (JudgeRoleRight.judgeRoleRight(administrators, staffUtil.getRoleNames())) {
			authorityType = 1;
		} else {
			authorityType = 0;
		}
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        if (StringUtils.isEmpty(riskcatid)) {
            Riskcategory cat = riskcategoryService.findQYFXByOrgid(selectOrg.getOrgid().toString(), Riskcategory.FXSJK);
            if (cat != null) {
                riskcatid = cat.getRiskcatid().toString();
            }
        }

        List<Riskcategory> list = riskcategoryService.findRiskCateByRoot(new BigDecimal(riskcatid));
        String ids = "";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(list.get(i).getRiskcatid().toString());
            if (i < list.size()-1) {
                stringBuilder.append(",");
            }
        }
        ids = stringBuilder.toString();
        final String idstr=ids;
        List<BigDecimal> idsss=list.stream().map(Riskcategory::getRiskcatid).collect(Collectors.toList());
        Risk risk = new Risk();
        risk.setRiskname(riskname);
        risk.setRisknumber(risknumber);
		risk.setStatus(status);
		risk.setStaffid(staffUtil.getStaffid());
        com.github.pagehelper.PageInfo<Risk> pageInfo= riskService.findRiskByPGRisks(risk,authorityType,pageNo, pageSize,idsss,staffUtil);
        List<Risk> listData = new ArrayList<Risk>();
        List<Risk> listres = pageInfo.getList();
        for (int i = 0; i < listres.size(); i++) {
        	Risk tblRisk = listres.get(i);
            QueryWrapper<RiskCoping> queryWrapper = new QueryWrapper<RiskCoping>();
            queryWrapper.eq("RISKID", tblRisk.getRiskid());
            if (copingService.count(queryWrapper)==0) {
                tblRisk.setCopingPlot("0");
                tblRisk.setCopingStatus(null);
            } else {
                List<RiskCoping> copings = copingService.list(queryWrapper);
                if(Objects.isNull(copings.get(0).getStatus())){
                	tblRisk.setCopingStatus(null);
                }else{
                	tblRisk.setCopingStatus(copings.get(0).getStatus()+"");
                }
                if(StringUtils.isNotBlank(copings.get(0).getCopingplot())){
                	 switch (copings.get(0).getCopingplot()) {
//                 	case "0": tblRisk.setCopingPlot("0"); break;
                     case "1": tblRisk.setCopingPlot("承担"); break;
                     case "2": tblRisk.setCopingPlot("转移"); break;
                     case "3": tblRisk.setCopingPlot("规避"); break;
                     case "4": tblRisk.setCopingPlot("降低（风险控制）"); break;
                     default: tblRisk.setCopingPlot("0");
                 }
                }
               
            }
            String level = riskService.getMaxLevelById(tblRisk.getRiskid());
            if (level == null) {
                tblRisk.setLevel("未评估");
            } else {
                switch (level) {
                    case "1": tblRisk.setLevel("很低"); break;
                    case "2": tblRisk.setLevel("较低"); break;
                    case "3": tblRisk.setLevel("中等"); break;
                    case "4": tblRisk.setLevel("较高"); break;
                    case "5": tblRisk.setLevel("很高"); break;
                    default: tblRisk.setLevel("未评估");
                }
            }
            listData.add(tblRisk);
            /*if(!tblRisk.getLevel().equals("未评估")) {
            	listData.add(tblRisk);
                continue;
            }*/
        }
        FiexibleNameAssignment ment=new FiexibleNameAssignment();
		if(CollectionUtils.isNotEmpty(listData)){
			listData.forEach(entity->{
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
        pageInfo.setList(listData);
        Map<String, PageInfo<Risk>> result = new HashMap<String, PageInfo<Risk>>();
        result.put("data", pageInfo);
        return new JsonBean(200, "成功", result);
    }

    @OperationLog(
            success = "风险台账 - 列表查询 处理成功",
            busType = "风险管控",
            fail = "风险台账 - 列表查询 处理失败",
            operationType = OperationType.SELECT,
            subType = "风险应对管理"
    )
    @RequestMapping(value = "/fxxt/fxyd/riskTzlist")
    @Operation(summary = "风险台账 - 列表查询 /fxxt/fxyd/riskTzlist")
    public JsonBean riskTzlist(@Parameter(name = "riskcatid", description = "风险类别ID", required = false) @RequestParam(required = false) String riskcatid,
                              @Parameter(name = "risknumber", description = "风险编号关键字") @RequestParam(required = false) String risknumber,
                              @Parameter(name = "riskname", description = "风险名称关键字") @RequestParam(required = false) String riskname,
							  @Parameter(name = "status", description = "审批状态") @RequestParam(required = false) String status,
                              @Parameter(name = "pageNo", description = "pageNo") @RequestParam(defaultValue = "1") Integer pageNo,
                              @Parameter(name = "pageSize", description = "pageSize") @RequestParam(defaultValue = "20") Integer pageSize,
                              @Parameter(name = "startDate", description = "开始时间:格式年-月-日") @RequestParam(value = "startDate", required = false) String startDate,
                              @Parameter(name = "endDate", description = "结束日期") @RequestParam(value = "endDate", required = false) String endDate,
							  @Parameter(name = "linkDeptName", description = "查询条件-所属部门") @RequestParam(required = false) String linkDeptName,
							  @Parameter(name = "cxlevel", description = "查询条件-风险等级") @RequestParam(required = false) String cxlevel,
                              @Parameter(name = "riskcatidname", description = "风险类型", required = false) @RequestParam(required = false) String riskcatidname,
                              @Parameter(name = "riskcatname", description = "风险领域", required = false) @RequestParam(required = false) String riskcatname,
                              @Parameter(name = "pgStatus", description = "首页查询风险状态 ypg ygb pgz wpg ") @RequestParam(required = false) String pgStatus,
                              @Parameter(name = "gbStatus", description = "首页查询风险关闭状态 0 是 1 否") @RequestParam(required = false) BigDecimal gbStatus,
                              @Parameter(name = "zrbmName", description = "查询条件-牵头责任部门") @RequestParam(required = false) String zrbmName,
							  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(401, "未查到用户登录信息，用户登录信息或已过期", null);
        }
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        if (StringUtils.isEmpty(riskcatid)) {
            Riskcategory cat = riskcategoryService.findQYFXByOrgid(selectOrg.getOrgid().toString(), Riskcategory.FXSJK);
            if (cat != null) {
                riskcatid = cat.getRiskcatid().toString();
            }
        }

        List<Riskcategory> list = riskcategoryService.findRiskCateByRoot(new BigDecimal(riskcatid));
//        String ids = "";
//        StringBuilder stringBuilder = new StringBuilder();
//        for (int i = 0; i < list.size(); i++) {
//            stringBuilder.append(list.get(i).getRiskcatid().toString());
//            if (i < list.size()-1) {
//                stringBuilder.append(",");
//            }
//        }
        List<BigDecimal> idsss=list.stream().map(Riskcategory::getRiskcatid).collect(Collectors.toList());
       // ids = stringBuilder.toString();
     //  final String idstr=ids;
        Risk risk = new Risk();
        risk.setUnit(selectOrg.getOrgid().toString());
        risk.setRiskname(riskname);
        risk.setRisknumber(risknumber);
		risk.setStatus(status);
		risk.setStaffid(staffUtil.getStaffid());
		risk.setStartdate(startDate);
		risk.setEnddate(endDate);
		if(StringUtils.isNotBlank(linkDeptName)){
			risk.setLinkDeptName(linkDeptName);
		}
       if(StringUtils.isNotBlank(cxlevel)){
			risk.setCxlevel(cxlevel);
		}
        risk.setBelongsto(zrbmName);
		risk.setRiskstatus(gbStatus);    
		risk.setPgStatus(pgStatus);
        risk.setRiskcatidname(riskcatidname);
	    risk.setRiskcatname(riskcatname);
		 //用于判断是否为风险管理员；该角色能看到本公司所有的风险创建信息；
  		Integer authorityType=0;
  		if (JudgeRoleRight.judgeRoleRight(administrators, staffUtil.getRoleNames())) {
  			authorityType = 1;
  			risk.setUnit(staffUtil.getCurrentOrg().getOrgid()+"");
  		}
        com.github.pagehelper.PageInfo<Risk> pageInfo = riskService.findRiskByPGRisks(risk, authorityType,pageNo, pageSize,idsss, staffUtil);
        List<Risk> listData = new ArrayList<Risk>();
        List<Risk> listres = pageInfo.getList();
        for (int i = 0; i < listres.size(); i++) {
        	Risk tblRisk = listres.get(i);

            QueryWrapper<RiskCoping> queryWrapper = new QueryWrapper<RiskCoping>();
            queryWrapper.eq("RISKID", tblRisk.getRiskid());

            if (copingService.count(queryWrapper)==0) {
                tblRisk.setCopingPlot("0");
            } else {
                List<RiskCoping> copings = copingService.list(queryWrapper);
                if(StringUtils.isNotBlank(copings.get(0).getCopingplot())){
                switch (copings.get(0).getCopingplot()) {
//                	case "0": tblRisk.setCopingPlot("0"); break;
                    case "1": tblRisk.setCopingPlot("承担"); break;
                    case "2": tblRisk.setCopingPlot("转移"); break;
                    case "3": tblRisk.setCopingPlot("规避"); break;
                    case "4": tblRisk.setCopingPlot("降低（风险控制）"); break;
                    default: tblRisk.setCopingPlot("0");
                }
                }
            }
            String level = riskService.getMaxLevelById(tblRisk.getRiskid());
            if (level == null) {
                tblRisk.setLevel("未评估");
            } else {
                switch (level) {
                    case "1": tblRisk.setLevel("很低"); break;
                    case "2": tblRisk.setLevel("较低"); break;
                    case "3": tblRisk.setLevel("中等"); break;
                    case "4": tblRisk.setLevel("较高"); break;
                    case "5": tblRisk.setLevel("很高"); break;
                    default: tblRisk.setLevel("未评估");
                }
            }
            listData.add(tblRisk);
          /*  if(!tblRisk.getLevel().equals("未评估")) {
            	listData.add(tblRisk);
                continue;
            }*/
        }
       pageInfo.setList(listData);
        Map<String, PageInfo<Risk>> result = new HashMap<String, PageInfo<Risk>>();
        result.put("data", pageInfo);
        return new JsonBean(200, "成功", result);
    }

    @OperationLog(
            success = "更新一体化运行评价处理成功",
            busType = "风险管控",
            fail = "更新一体化运行评价处理失败",
            operationType = OperationType.UPDATE,
            subType = "风险应对管理"
    )
    @Operation(summary = "更新一体化运行评价")
    @RequestMapping(value = "/fxxt/fxyd/runeval")
    public JsonBean runeval(@Parameter(name = "riskcopingid", description = "风险应对ID", required = true) @RequestParam(required = true) BigDecimal riskcopingid,
    		@Parameter(name = "evalimp", description = "评价标准及要点", required = false) @RequestParam(required = false) String evalimp,
    		@Parameter(name = "evalfile", description = "文档", required = false) @RequestParam(required = false) String evalfile,
                                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(401, "未查到用户登录信息，用户登录信息或已过期", null);
        }

        RiskCoping riskCoping = new RiskCoping();
        riskCoping.setRiskcopingid(riskcopingid);
        riskCoping.setEvalimp(evalimp);
        riskCoping.setEvalfile(evalfile);
		copingService.updateByEval(riskCoping);

        return new JsonBean(200, "成功", null);
    }

    
    @OperationLog(
            success = "转换管控措施到新管控条目表中成功",
            busType = "风险管控",
            fail = "转换管控措施到新管控条目表中失败",
            operationType = OperationType.ADD,
            subType = "转换管控措施到新管控条目表中"
    )
    @Operation(summary = "转换管控措施到新管控条目表中")
    @RequestMapping(value = "/fxxt/fxyd/convertControlMeasures")
    public JsonBean convertControlMeasures(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
      try {
    	  copingService.convertControlMeasures(token);
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
        return new JsonBean(200, "成功", null);
    }

    
    
    @OperationLog(
            success = "风险台账 - 导出excel 处理成功",
            busType = "风险管控",
            fail = "风险台账 - 导出excel 处理失败",
            operationType = OperationType.SELECT,
            subType = "风险应对管理"
    )
    @RequestMapping(value = "/fxxt/fxyd/exportRiskTzlist")
    @Operation(summary = "风险台账 - 导出excel /fxxt/fxyd/exportRiskTzlist")
    public void exportRiskTzlist(HttpServletRequest request, HttpServletResponse response,
    		                  @Parameter(name = "riskcatid", description = "风险类别ID", required = false) @RequestParam(required = false) String riskcatid,
                              @Parameter(name = "risknumber", description = "风险编号关键字") @RequestParam(required = false) String risknumber,
                              @Parameter(name = "riskname", description = "风险名称关键字") @RequestParam(required = false) String riskname,
							  @Parameter(name = "status", description = "审批状态") @RequestParam(required = false) String status,
                              @Parameter(name = "startDate", description = "开始时间:格式年-月-日") @RequestParam(value = "startDate", required = false) String startDate,
                              @Parameter(name = "endDate", description = "结束日期") @RequestParam(value = "endDate", required = false) String endDate,
                              @Parameter(name = "linkDeptName", description = "查询条件-所属部门") @RequestParam(required = false) String linkDeptName,
							  @Parameter(name = "cxlevel", description = "查询条件-风险等级") @RequestParam(required = false) String cxlevel,
							  @Parameter(name = "riskcatidname", description = "风险类型", required = false) @RequestParam(required = false) String riskcatidname,
                              @Parameter(name = "riskcatname", description = "风险领域", required = false) @RequestParam(required = false) String riskcatname,
                              @Parameter(name = "pgStatus", description = "首页查询风险状态 ypg ygb pgz wpg ") @RequestParam(required = false) String pgStatus,
                              @Parameter(name = "gbStatus", description = "首页查询风险关闭状态 0 是 1 否") @RequestParam(required = false) BigDecimal gbStatus,
                              @Parameter(name = "zrbmName", description = "查询条件-牵头责任部门") @RequestParam(required = false) String zrbmName,
							  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
		try {
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        if (StringUtils.isEmpty(riskcatid)) {
            Riskcategory cat = riskcategoryService.findQYFXByOrgid(selectOrg.getOrgid().toString(), Riskcategory.FXSJK);
            if (cat != null) {
                riskcatid = cat.getRiskcatid().toString();
            }
        }
        List<Riskcategory> list = riskcategoryService.findRiskCateByRoot(new BigDecimal(riskcatid));
        List<BigDecimal> idsss=list.stream().map(Riskcategory::getRiskcatid).collect(Collectors.toList());
			Risk risk = new Risk();
			risk.setRiskname(riskname);
			risk.setRisknumber(risknumber);
			risk.setStatus(status);
			risk.setStaffid(staffUtil.getStaffid());
			risk.setStartdate(startDate);
			risk.setEnddate(endDate);
			risk.setBelongsto(zrbmName);
			risk.setRiskstatus(gbStatus);
			risk.setPgStatus(pgStatus);
			risk.setRiskcatidname(riskcatidname);
			risk.setRiskcatname(riskcatname);
		if(StringUtils.isNotBlank(linkDeptName)){
			risk.setLinkDeptName(linkDeptName);
		}
       if(StringUtils.isNotBlank(cxlevel)){
			risk.setCxlevel(cxlevel);
		}
		 //用于判断是否为风险管理员；该角色能看到本公司所有的风险创建信息；
  		Integer authorityType=0;
  		if (JudgeRoleRight.judgeRoleRight(administrators, staffUtil.getRoleNames())) {
  			authorityType = 1;
  			risk.setUnit(staffUtil.getCurrentOrg().getOrgid()+"");
  		}
		List<Object[]> pageInfo = riskService.exportFindRiskByPGRisks(risk, authorityType,idsss, staffUtil);
		  Map<String,Object> result = new HashMap<String,Object>(0);
          String name = new String("风险台账".getBytes(), "iso-8859-1");
          String date = String.valueOf(System.currentTimeMillis());
          String fileName = name + "_" + date + ".xlsx";
          response.setContentType("application/octet-stream;charset=UTF-8");
          response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
          ServletOutputStream outputStream = response.getOutputStream();
          String[] titles = {"风险编号", "风险名称","风险类型", "风险描述", "责任单位","责任部门","责任科室","牵头责任部门", "风险等级","风险关闭状态","创建时间", "创建人"};
          int[] cWiths=new int[]{5500,5500,3500,12500,3500,3500,3500,5500,5500,5500,3500,3500};
          ImportOrExportExcelUtil.exportExcelSetWith(titles, pageInfo, outputStream,cWiths, null);
      } catch (Exception e) {
          e.printStackTrace();
      }
    }

    
    
    
    @OperationLog(
            success = "列表查询 处理成功",
            busType = "风险数据",
            fail = "列表查询 处理失败",
            operationType = OperationType.SELECT,
            subType = "集团风险数据库"
    )
    @RequestMapping(value = "/fxxt/fxyd/riskTzlistGroup")
    @Operation(summary = "集团风险数据库 - 列表查询 /fxxt/fxyd/riskTzlistGroup")
    public JsonBean riskTzlistGroup(@Parameter(name = "riskcatid", description = "风险类别ID", required = false) @RequestParam(required = false) String riskcatid,
                              @Parameter(name = "risknumber", description = "风险编号关键字") @RequestParam(required = false) String risknumber,
                              @Parameter(name = "riskname", description = "风险名称关键字") @RequestParam(required = false) String riskname,
							  @Parameter(name = "status", description = "审批状态") @RequestParam(required = false) String status,
                              @Parameter(name = "pageNo", description = "pageNo") @RequestParam(defaultValue = "1") Integer pageNo,
                              @Parameter(name = "pageSize", description = "pageSize") @RequestParam(defaultValue = "20") Integer pageSize,
                              @Parameter(name = "startDate", description = "开始时间:格式年-月-日") @RequestParam(value = "startDate", required = false) String startDate,
                              @Parameter(name = "endDate", description = "结束日期") @RequestParam(value = "endDate", required = false) String endDate,
                              @Parameter(name = "orgid", description = "公司主键id", required = false)@RequestParam(value="orgid",required=false) String orgid,
                              @Parameter(name = "riskcatidname", description = "风险类型", required = false) @RequestParam(required = false) String riskcatidname,
                              @Parameter(name = "riskcatname", description = "风险领域", required = false) @RequestParam(required = false) String riskcatname,
                              @Parameter(name = "linkDeptName", description = "查询条件-所属部门") @RequestParam(required = false) String linkDeptName,
							  @Parameter(name = "cxlevel", description = "查询条件-风险等级") @RequestParam(required = false) String cxlevel,
                              @Parameter(name = "pgStatus", description = "首页查询风险状态 ypg ygb pgz wpg ") @RequestParam(required = false) String pgStatus,
                              @Parameter(name = "gbStatus", description = "首页查询风险关闭状态 0 是 1 否") @RequestParam(required = false) BigDecimal gbStatus,
                              @Parameter(name = "zrbmName", description = "查询条件-牵头责任部门") @RequestParam(required = false) String zrbmName,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(401, "未查到用户登录信息，用户登录信息或已过期", null);
        }
		Integer authorityType = 1;
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        Risk risk = new Risk();
        risk.setRiskname(riskname);
        risk.setRisknumber(risknumber);
		risk.setStatus(status);
		risk.setStaffid(staffUtil.getStaffid());
		risk.setStartdate(startDate);
		risk.setEnddate(endDate);
		risk.setPgStatus(pgStatus);
		risk.setBelongsto(zrbmName);
		risk.setRiskstatus(gbStatus);
		if(StringUtils.isBlank(orgid)){
			orgid=staffUtil.getCurrentOrg().getOrgid().toString();
		}
		 String groupCompanyID = SysConfig.get("groupCompanyID");
	  	Organization org=organizationService.findById(orgid);
		  //风险台账左侧树为公司部门树 ,需要判断传入的数据是公司ID还是部门ID
	  	if (StringUtils.isNotEmpty(orgid) &&org.getOrgtype().compareTo(new BigDecimal(0))!=0){
	    	List<BigDecimal> list=organizationService.getIdsByFathersId(new BigDecimal(orgid));
	    	if(list!=null&&list.size()>0){
	    		risk.setUnitIds(list);
	    	}else{
	    		risk.setUnit(orgid);
	    	}
	    }else{
	    	 List<BigDecimal> list=organizationService.getIdsByFatherId(new BigDecimal(orgid));
	    	if(list!=null&&list.size()>0){
	    		risk.setDeptIds(list);
	    	}else{
	    		risk.setLinkDeptId(new BigDecimal(orgid));
	    	}
	    }
	    risk.setRiskcatidname(riskcatidname);
	    risk.setRiskcatname(riskcatname);
		if(StringUtils.isNotBlank(linkDeptName)){
			risk.setLinkDeptName(linkDeptName);
		}
       if(StringUtils.isNotBlank(cxlevel)){

           if (cxlevel.contains(",")) {
               String[] cxlevelArray = cxlevel.split(",");
               risk.setCxlevelList(Arrays.asList(cxlevelArray));
           } else {
               risk.setCxlevelList(Collections.singletonList(cxlevel));
           }
           risk.setCxlevel(cxlevel.split(",")[0]);
		}
       risk.setPgStatus(pgStatus);
        com.github.pagehelper.PageInfo<Risk> pageInfo = riskService.findRiskByPGRisksGroup(risk, authorityType,pageNo, pageSize,staffUtil);
        List<Risk> listData = new ArrayList<Risk>();
        List<Risk> listres = pageInfo.getList();
        for (int i = 0; i < listres.size(); i++) {
        	Risk tblRisk = listres.get(i);
            QueryWrapper<RiskCoping> queryWrapper = new QueryWrapper<RiskCoping>();
            queryWrapper.eq("RISKID", tblRisk.getRiskid());
            if (copingService.count(queryWrapper)==0) {
                tblRisk.setCopingPlot("0");
            } else {
                List<RiskCoping> copings = copingService.list(queryWrapper);
                if(StringUtils.isNotBlank(copings.get(0).getCopingplot())){
                	 switch (copings.get(0).getCopingplot()) {
//                 	case "0": tblRisk.setCopingPlot("0"); break;
                     case "1": tblRisk.setCopingPlot("承担"); break;
                     case "2": tblRisk.setCopingPlot("转移"); break;
                     case "3": tblRisk.setCopingPlot("规避"); break;
                     case "4": tblRisk.setCopingPlot("降低（风险控制）"); break;
                     default: tblRisk.setCopingPlot("0");
                 }
                }
               
            }
            String level = riskService.getMaxLevelById(tblRisk.getRiskid());
            if (level == null) {
                tblRisk.setLevel("未评估");
            } else {
                switch (level) {
                    case "1": tblRisk.setLevel("很低"); break;
                    case "2": tblRisk.setLevel("较低"); break;
                    case "3": tblRisk.setLevel("中等"); break;
                    case "4": tblRisk.setLevel("较高"); break;
                    case "5": tblRisk.setLevel("很高"); break;
                    default: tblRisk.setLevel("未评估");
                }
            }
            listData.add(tblRisk);
          /*  if(!tblRisk.getLevel().equals("未评估")) {
            	listData.add(tblRisk);
                continue;
            }*/
        }
       pageInfo.setList(listData);
        Map<String, PageInfo<Risk>> result = new HashMap<String, PageInfo<Risk>>();
        result.put("data", pageInfo);
        return new JsonBean(200, "成功", result);
    }

    
    
    
    @RequestMapping(value = "/shouye/tjfxpgjgList")
    @Operation(summary = "首页跳转-统计风险评估结果")
    public JsonBean tjfxpgjgList(@Parameter(name = "riskcatid", description = "风险类别ID", required = false) @RequestParam(required = false) String riskcatid,
                              @Parameter(name = "risknumber", description = "风险编号关键字") @RequestParam(required = false) String risknumber,
                              @Parameter(name = "riskname", description = "风险名称关键字") @RequestParam(required = false) String riskname,
                              @Parameter(name = "pageNo", description = "pageNo") @RequestParam(defaultValue = "1") Integer pageNo,
                              @Parameter(name = "pageSize", description = "pageSize") @RequestParam(defaultValue = "20") Integer pageSize,
                              @Parameter(name = "orgid", description = "公司主键id", required = false)@RequestParam(value="orgid",required=false) String orgid,
                              @Parameter(name = "riskcatidname", description = "风险领域", required = false) @RequestParam(required = false) String riskcatidname,
                              @Parameter(name = "riskcatname", description = "风险类型", required = false) @RequestParam(required = false) String riskcatname,
                              @Parameter(name = "linkDeptName", description = "查询条件-所属部门") @RequestParam(required = false) String linkDeptName,
							  @Parameter(name = "cxlevel", description = "查询条件-风险等级") @RequestParam(required = false) String cxlevel,
                              @Parameter(name = "pgStatus", description = "首页查询风险状态 ypg ygb") @RequestParam(required = false) String pgStatus,
							  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(401, "未查到用户登录信息，用户登录信息或已过期", null);
        }
		Integer authorityType = 1;
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        Risk risk = new Risk();
        risk.setRiskname(riskname);
        risk.setRisknumber(risknumber);
		risk.setStaffid(staffUtil.getStaffid());
		if(StringUtils.isBlank(orgid)){
			orgid=staffUtil.getCurrentOrg().getOrgid().toString();
		}
		risk.setUnit(orgid);
	    risk.setRiskcatidname(riskcatidname);
	    risk.setRiskcatname(riskcatname);
		if(StringUtils.isNotBlank(linkDeptName)){
			risk.setLinkDeptName(linkDeptName);
		}
       if(StringUtils.isNotBlank(cxlevel)){
           if (cxlevel.contains(",")) {
               String[] cxlevelArray = cxlevel.split(",");
               risk.setCxlevelList(Arrays.asList(cxlevelArray));
           } else {
               risk.setCxlevelList(Collections.singletonList(cxlevel));
           }
           risk.setCxlevel(cxlevel.split(",")[0]);
		}
       risk.setPgStatus(pgStatus);
        com.github.pagehelper.PageInfo<Risk> pageInfo = riskService.getTjfxpgjgList(risk, authorityType,pageNo, pageSize,staffUtil);
        List<Risk> listData = new ArrayList<Risk>();
        List<Risk> listres = pageInfo.getList();
        for (int i = 0; i < listres.size(); i++) {
        	Risk tblRisk = listres.get(i);
            QueryWrapper<RiskCoping> queryWrapper = new QueryWrapper<RiskCoping>();
            queryWrapper.eq("RISKID", tblRisk.getRiskid());
            if (copingService.count(queryWrapper)==0) {
                tblRisk.setCopingPlot("0");
            } else {
                List<RiskCoping> copings = copingService.list(queryWrapper);
                if(StringUtils.isNotBlank(copings.get(0).getCopingplot())){
                	 switch (copings.get(0).getCopingplot()) {
//                 	case "0": tblRisk.setCopingPlot("0"); break;
                     case "1": tblRisk.setCopingPlot("承担"); break;
                     case "2": tblRisk.setCopingPlot("转移"); break;
                     case "3": tblRisk.setCopingPlot("规避"); break;
                     case "4": tblRisk.setCopingPlot("降低（风险控制）"); break;
                     default: tblRisk.setCopingPlot("0");
                 }
                }
               
            }
            String level = riskService.getMaxLevelById(tblRisk.getRiskid());
            if (level == null) {
                tblRisk.setLevel("未评估");
            } else {
                switch (level) {
                    case "1": tblRisk.setLevel("很低"); break;
                    case "2": tblRisk.setLevel("较低"); break;
                    case "3": tblRisk.setLevel("中等"); break;
                    case "4": tblRisk.setLevel("较高"); break;
                    case "5": tblRisk.setLevel("很高"); break;
                    default: tblRisk.setLevel("未评估");
                }
            }
            listData.add(tblRisk);
          /*  if(!tblRisk.getLevel().equals("未评估")) {
            	listData.add(tblRisk);
                continue;
            }*/
        }
       pageInfo.setList(listData);
        Map<String, PageInfo<Risk>> result = new HashMap<String, PageInfo<Risk>>();
        result.put("data", pageInfo);
        return new JsonBean(200, "成功", result);
    }

    
    

    @RequestMapping(value = "/shouye/getRiskPointTaskList")
    @Operation(summary = "首页跳转-风险点评估任务结构")
    public JsonBean getRiskPointTaskList(
                              @Parameter(name = "pageNo", description = "pageNo") @RequestParam(defaultValue = "1") Integer pageNo,
                              @Parameter(name = "pageSize", description = "pageSize") @RequestParam(defaultValue = "20") Integer pageSize,
                              @Parameter(name = "orgid", description = "公司主键id", required = false)@RequestParam(value="orgid",required=false) String orgid,
                              @Parameter(name = "pgStatus", description = "首页查询风险状态 ypg ygb") @RequestParam(required = false) String pgStatus,
							  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(401, "未查到用户登录信息，用户登录信息或已过期", null);
        }
		Integer authorityType = 1;
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        Risk risk = new Risk();
		risk.setStaffid(staffUtil.getStaffid());
		if(StringUtils.isBlank(orgid)){
			orgid=staffUtil.getCurrentOrg().getOrgid().toString();
		}
		risk.setUnit(orgid);
       risk.setPgStatus(pgStatus);
        com.github.pagehelper.PageInfo<Risk> pageInfo = riskService.getRiskPointTaskList(risk, authorityType,pageNo, pageSize,staffUtil);
        List<Risk> listData = new ArrayList<Risk>();
        List<Risk> listres = pageInfo.getList();
        for (int i = 0; i < listres.size(); i++) {
        	Risk tblRisk = listres.get(i);
            QueryWrapper<RiskCoping> queryWrapper = new QueryWrapper<RiskCoping>();
            queryWrapper.eq("RISKID", tblRisk.getRiskid());
            if (copingService.count(queryWrapper)==0) {
                tblRisk.setCopingPlot("0");
            } else {
                List<RiskCoping> copings = copingService.list(queryWrapper);
                if(StringUtils.isNotBlank(copings.get(0).getCopingplot())){
                	 switch (copings.get(0).getCopingplot()) {
//                 	case "0": tblRisk.setCopingPlot("0"); break;
                     case "1": tblRisk.setCopingPlot("承担"); break;
                     case "2": tblRisk.setCopingPlot("转移"); break;
                     case "3": tblRisk.setCopingPlot("规避"); break;
                     case "4": tblRisk.setCopingPlot("降低（风险控制）"); break;
                     default: tblRisk.setCopingPlot("0");
                 }
                }
               
            }
            String level = riskService.getMaxLevelById(tblRisk.getRiskid());
            if (level == null) {
                tblRisk.setLevel("未评估");
            } else {
                switch (level) {
                    case "1": tblRisk.setLevel("很低"); break;
                    case "2": tblRisk.setLevel("较低"); break;
                    case "3": tblRisk.setLevel("中等"); break;
                    case "4": tblRisk.setLevel("较高"); break;
                    case "5": tblRisk.setLevel("很高"); break;
                    default: tblRisk.setLevel("未评估");
                }
            }
            listData.add(tblRisk);
          /*  if(!tblRisk.getLevel().equals("未评估")) {
            	listData.add(tblRisk);
                continue;
            }*/
        }
       pageInfo.setList(listData);
        Map<String, PageInfo<Risk>> result = new HashMap<String, PageInfo<Risk>>();
        result.put("data", pageInfo);
        return new JsonBean(200, "成功", result);
    }

    
    @OperationLog(
            success = "导出excel 处理成功",
            busType = "风险数据",
            fail = "导出excel 处理失败",
            operationType = OperationType.SELECT,
            subType = "集团风险数据库"
    )
    @RequestMapping(value = "/fxxt/fxyd/exportRiskTzlistGroup")
    @Operation(summary = "集团风险数据库 - 导出excel /fxxt/fxyd/exportRiskTzlistGroup")
    public void exportRiskTzlistGroup(HttpServletRequest request, HttpServletResponse response,
    		                  @Parameter(name = "riskcatid", description = "风险类别ID", required = false) @RequestParam(required = false) String riskcatid,
                              @Parameter(name = "risknumber", description = "风险编号关键字") @RequestParam(required = false) String risknumber,
                              @Parameter(name = "riskname", description = "风险名称关键字") @RequestParam(required = false) String riskname,
							  @Parameter(name = "status", description = "审批状态") @RequestParam(required = false) String status,
                              @Parameter(name = "startDate", description = "开始时间:格式年-月-日") @RequestParam(value = "startDate", required = false) String startDate,
                              @Parameter(name = "endDate", description = "结束日期") @RequestParam(value = "endDate", required = false) String endDate,
                              @Parameter(name = "orgid", description = "公司主键id", required = false)@RequestParam(value="orgid",required=false) String orgid,
                              @Parameter(name = "riskcatidname", description = "风险类型", required = false) @RequestParam(required = false) String riskcatidname,
                              @Parameter(name = "riskcatname", description = "风险领域", required = false) @RequestParam(required = false) String riskcatname,
                              @Parameter(name = "linkDeptName", description = "查询条件-所属部门") @RequestParam(required = false) String linkDeptName,
							  @Parameter(name = "cxlevel", description = "查询条件-风险等级") @RequestParam(required = false) String cxlevel,
							  @Parameter(name = "pgStatus", description = "首页查询风险状态 ypg ygb pgz wpg ") @RequestParam(required = false) String pgStatus,
							  @Parameter(name = "gbStatus", description = "首页查询风险关闭状态 0 是 1 否") @RequestParam(required = false) BigDecimal gbStatus,
                              @Parameter(name = "zrbmName", description = "查询条件-牵头责任部门") @RequestParam(required = false) String zrbmName,
							  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
		try {
        Integer authorityType = 1;
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        Risk risk = new Risk();
        risk.setRiskname(riskname);
        risk.setRisknumber(risknumber);
		risk.setStatus(status);
		risk.setStaffid(staffUtil.getStaffid());
		risk.setStartdate(startDate);
		risk.setEnddate(endDate);
		risk.setBelongsto(zrbmName);
		risk.setRiskstatus(gbStatus);
		 risk.setPgStatus(pgStatus);
		if(StringUtils.isBlank(orgid)){
			orgid=staffUtil.getCurrentOrg().getOrgid().toString();
		}
		 String groupCompanyID = SysConfig.get("groupCompanyID");
		Organization org=organizationService.findById(orgid);
		  //风险台账左侧树为公司部门树 ,需要判断传入的数据是公司ID还是部门ID
		if (StringUtils.isNotEmpty(orgid) &&org.getOrgtype().compareTo(new BigDecimal(0))!=0){
	    	List<BigDecimal> list=organizationService.getIdsByFathersId(new BigDecimal(orgid));
	    	if(list!=null&&list.size()>0){
	    		risk.setUnitIds(list);
	    	}else{
	    		risk.setUnit(orgid);
	    	}
	    }else{
	    	 List<BigDecimal> list=organizationService.getIdsByFatherId(new BigDecimal(orgid));
	    	if(list!=null&&list.size()>0){
	    		risk.setDeptIds(list);
	    	}else{
	    		risk.setLinkDeptId(new BigDecimal(orgid));
	    	}
	    }
			if(StringUtils.isNotBlank(linkDeptName)){
				risk.setLinkDeptName(linkDeptName);
			}
	       if(StringUtils.isNotBlank(cxlevel)){
				risk.setCxlevel(cxlevel);
			}
		risk.setRiskcatidname(riskcatidname);
		risk.setRiskcatname(riskcatname);
		List<Object[]> pageInfo = riskService.exportFindRiskByPGRisksGroup(risk, authorityType, staffUtil);
		  Map<String,Object> result = new HashMap<String,Object>(0);
          String name = new String("集团风险数据库".getBytes(), "iso-8859-1");
          String date = String.valueOf(System.currentTimeMillis());
          String fileName = name + "_" + date + ".xlsx";
          response.setContentType("application/octet-stream;charset=UTF-8");
          response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
          ServletOutputStream outputStream = response.getOutputStream();
          String[] titles = {"风险编号", "风险名称","风险领域","风险类型", "风险描述", "所属公司","所属部门","牵头责任部门", "风险等级","风险关闭状态","创建时间", "创建人"};
          int[] cWiths=new int[]{6500,5500,5500,5500,12500,5500,3500,3500,5500,3500,3500,3500};
          ImportOrExportExcelUtil.exportExcelSetWith(titles, pageInfo, outputStream,cWiths,null); 
      } catch (Exception e) {
          e.printStackTrace();
      }
    }




    @OperationLog(
            success = "更改当前风险顺序",
            busType = "风险管控",
            fail = "更改当前风险顺序",
            operationType = OperationType.UPDATE,
            subType = "风险数据库"
    )
    @Operation(summary = "更改当前风险顺序")
    @RequestMapping(value = "/fxxt/fxyd/updateRiskOrder")
    public JsonBean updateRiskOrder(@Parameter(name = "riskId", description = "风险ID", required = true) @RequestParam(required = true) BigDecimal riskId,
                            @Parameter(name = "riskOrder", description = "文档", required = false) @RequestParam(required = false) BigDecimal riskOrder,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(401, "未查到用户登录信息，用户登录信息或已过期", null);
        }
        riskService.updateRiskOrder(riskId,riskOrder);
        return new JsonBean(200, "成功", riskOrder);
    }

    //获取风险TOP10
    @OperationLog(
            success = "获取风险TOP10",
            busType = "获取风险TOP10",
            fail = "获取风险TOP10",
            operationType = OperationType.SELECT,
            subType = "获取风险TOP10"
    )
    @RequestMapping(value = "/getRiskTopList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险TOP10")
    public JsonBean getRiskTopList(HttpServletRequest request,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token

    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = riskService.getRiskTopList();
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

}
