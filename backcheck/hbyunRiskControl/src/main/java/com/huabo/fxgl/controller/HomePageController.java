package com.huabo.fxgl.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.dto.ZbVo;
import com.huabo.fxgl.entity.Risk;
import com.huabo.fxgl.entity.TblNbsjAuditStepEntity;
import com.huabo.fxgl.mapper.RiskeventMapper;
import com.huabo.fxgl.mapper.TblNbsjAuditStepEntityMapper;
import com.huabo.fxgl.mapper.YyCompanyMapper;
import com.huabo.fxgl.service.IControlmatrixService;
import com.huabo.fxgl.service.IRiskAssplanRiskService;
import com.huabo.fxgl.service.IRiskAssplanService;
import com.huabo.fxgl.service.IRiskCopingService;
import com.huabo.fxgl.service.IRiskLevelmappingService;
import com.huabo.fxgl.service.IRiskService;
import com.huabo.fxgl.service.IRiskeventService;
import com.huabo.fxgl.service.TblRiskReviewService;
import com.huabo.fxgl.vo.RiskPgVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
/**
 * 风险管控首页控制器
 * <p>提供风险管控模块首页的数据统计、概览等接口</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping(value = "/controlHome",method = {RequestMethod.GET, RequestMethod.POST})
@Tag(name="首页接口",description="首页接口")
@Slf4j
public class HomePageController {

    @Resource
    private RiskeventMapper riskeventMapper;
    @Resource
    private IRiskCopingService copingService;

    @Resource
    private YyCompanyMapper yyCompanyMapper;

    @Resource
    private IRiskService riskService;
    
    @Resource
    private IRiskeventService   iRiskeventService;

    @Resource
    private TblRiskReviewService tblRiskReviewService;
    @Autowired
    private IRiskAssplanService riskAssplanService;
    
    @Resource
    private IRiskAssplanRiskService riskAssplanRiskService;
    
    @Resource
    private IRiskLevelmappingService riskLevelmappingService;
    
    
    @Resource
    private IControlmatrixService controlmatrixService;
    
    @Resource
    private UserProvider userProvider;

	@Autowired
	private TblNbsjAuditStepEntityMapper tblNbsjAuditStepEntityMapper;

	@OperationLog(success = "风险评估热力图详情查询处理成功",
			busType = "首页接口",
			fail = "风险评估热力图详情查询处理失败",
			operationType = OperationType.SELECT,
			subType = "首页接口")
	@Operation(summary = "首页风险评估热力图查看详情")
	@RequestMapping(value = "pgrltxq",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
	public JsonBean pgrltxq(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
						  	@Parameter(name = "company", description = "公司查询条件 不传是集团，传是按照公司", required = false) @RequestParam(name = "company", required = false) BigDecimal company,
							@Parameter(name = "frequency", description = "频率", required = false) @RequestParam(name = "frequency", required = false) BigDecimal frequency,
							@Parameter(name = "severity", description = "严重程度", required = false) @RequestParam(name = "severity", required = false) BigDecimal severity) throws Exception {
		TblStaffUtil staffUtil = userProvider.get();
		List<RiskPgVo> pgs = tblNbsjAuditStepEntityMapper.selectASSRISKIDByUnit(frequency,severity,company);
		if (pgs == null || pgs.size() == 0) {
			return ResponseFormat.retParam(1, 200, new ArrayList<>());
		}
		Map<String,Object> map = new HashMap<>();
		map.put("RiskPg", pgs);
		map.put("total", pgs.size());
		return ResponseFormat.retParam(1, 200, map);
	}

	@OperationLog(success = "风险评估热力图查询处理成功",
			busType = "首页接口",
			fail = "风险评估热力图查询处理失败",
			operationType = OperationType.SELECT,
			subType = "首页接口")
	@Operation(summary = "首页风险评估热力图")
	@RequestMapping(value = "pgrlt",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
	public JsonBean pgrlt(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
						  @Parameter(name = "company", description = "公司查询条件 不传是集团，传是按照公司", required = false) @RequestParam(name = "company", required = false) BigDecimal company) throws Exception {
		TblStaffUtil staffUtil = userProvider.get();
		Map<String,Integer> map = new HashMap<>();
		for (int i = 1; i < 6; i++) {
			for (int j = 1; j < 6; j++) {
				List<RiskPgVo> pgs = tblNbsjAuditStepEntityMapper.selectASSRISKIDByUnit(new BigDecimal(i),new BigDecimal(j),company);
				map.put(i+"_"+j, pgs.size());
			}
		}
		return ResponseFormat.retParam(1, 200, map);
	}


	@OperationLog(
			success = "预警指标首页饼图查询处理成功",
			busType = "首页接口",
			fail = "预警指标首页饼图查询处理失败",
			operationType = OperationType.SELECT,
			subType = "首页接口"
	)
	@Operation(summary = "预警指标首页饼图")
	@RequestMapping(value = "yjsyBt",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
	public JsonBean yjsyBt(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staffUtil = userProvider.get();
		List<TblNbsjAuditStepEntity> tblNbsjAuditStepEntities = tblNbsjAuditStepEntityMapper.selectAllList(staffUtil.getStaffid());
		List<Map<String,Object>> maps = new ArrayList<>();
		for (TblNbsjAuditStepEntity tblNbsjAuditStepEntity : tblNbsjAuditStepEntities) {
			List<ZbVo> os = tblNbsjAuditStepEntityMapper.selectBySQL(tblNbsjAuditStepEntity.getSqlstr());
			Map<String,Object> map = new HashMap<>();
			for (ZbVo o : os) {
				if (o==null){
					continue;
				}
				if (maps.size()<1){
					map.put("stepTitle",tblNbsjAuditStepEntity.getSteptitle());
					map.put("threshold",o.get阈值());
					maps.add(map);
					continue;
				}
				if (maps.get(maps.size()-1).get("stepTitle").equals(tblNbsjAuditStepEntity.getSteptitle())){
					continue;
				}
				map.put("stepTitle",tblNbsjAuditStepEntity.getSteptitle());
				map.put("threshold",o.get阈值());
				maps.add(map);
			}
		}
		Integer red = 0;
		Integer yellow = 0;
		Integer green = 0;
		for (int i = 0; i < maps.size(); i++) {
			if (maps.get(i).get("threshold")!=null){
				if (maps.get(i).get("threshold").equals("红")){
					red++;
				}else if (maps.get(i).get("threshold").equals("黄")){
					yellow++;
				}else if (maps.get(i).get("threshold").equals("绿")){
					green++;
				}
			}
		}
		Map<String,Integer> map = new HashMap<>();
		map.put("red",red);
		map.put("yellow",yellow);
		map.put("green",green);
		map.put("total",maps.size());
		return ResponseFormat.retParam(1, 200, map);
	}

	@OperationLog(
			success = "预警指标查询处理成功",
			busType = "首页接口",
			fail = "预警指标查询处理失败",
			operationType = OperationType.SELECT,
			subType = "首页接口"
	)
	@Operation(summary = "预警指标处理")
	@RequestMapping(value = "yjzb",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
	public JsonBean yjzb(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staffUtil = userProvider.get();
		List<TblNbsjAuditStepEntity> tblNbsjAuditStepEntities = tblNbsjAuditStepEntityMapper.selectAllList(staffUtil.getStaffid());
		List<Map<String,Object>> maps = new ArrayList<>();
		for (TblNbsjAuditStepEntity tblNbsjAuditStepEntity : tblNbsjAuditStepEntities) {
			List<ZbVo> os = tblNbsjAuditStepEntityMapper.selectBySQL(tblNbsjAuditStepEntity.getSqlstr());
			Map<String,Object> map = new HashMap<>();
			for (ZbVo o : os) {
				if (o==null){
					continue;
				}
				if (maps.size()<1){
					map.put("stepTitle",tblNbsjAuditStepEntity.getSteptitle());
					map.put("quarter",o.get季度());
					map.put("threshold",o.get阈值());
					map.put("sqlStr",tblNbsjAuditStepEntity.getSqlstr());
					map.put("bookid",tblNbsjAuditStepEntity.getBookid());
					maps.add(map);
					continue;
				}
				if (maps.get(maps.size()-1).get("stepTitle").equals(tblNbsjAuditStepEntity.getSteptitle())){
					continue;
				}
				map.put("stepTitle",tblNbsjAuditStepEntity.getSteptitle());
				map.put("quarter",o.get季度());
				map.put("threshold",o.get阈值());
				map.put("sqlStr",tblNbsjAuditStepEntity.getSqlstr());
				map.put("bookid",tblNbsjAuditStepEntity.getBookid());
				maps.add(map);
			}
		}
		Map<String,Object> map = new HashMap<>();
		map.put("yjzb",maps);
		map.put("total",maps.size());
		return ResponseFormat.retParam(1, 200, map);
	}

    
    /**
     * 1.公司面临的风险数量，一般风险数量，重大风险数量；
     */
    @OperationLog(
            success = "公司/集团面临的风险数量，一般风险数量，重大风险数量查询处理成功",
            busType = "首页接口",
            fail = "公司/集团面临的风险数量，一般风险数量，重大风险数量查询处理失败",
            operationType = OperationType.SELECT,
            subType = "首页接口"
    )
    @Operation(summary = "1.公司/集团面临的风险数量，一般风险数量，重大风险数量；")
    @RequestMapping(value = "numberRisks",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    public JsonBean numberRisks(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "company", description = "公司查询条件 不传是集团，传是按照公司", required = false) @RequestParam(name = "company", required = false) BigDecimal company,
    		@Parameter(name = "year", description = "年度查询条件", required = false) @RequestParam(name = "year", required = false) String year
    		) throws Exception {
        HashMap<String, Integer> map = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();
            if(StringUtils.isBlank(year)){
	        	Calendar calendar = Calendar.getInstance();
	             year = calendar.get(Calendar.YEAR)+"";
	        }
            Integer risksYiBan = riskService.queryNumberRisksByType(company,year, Risk.YIBAN);
            Integer riskszhongDa = riskService.queryNumberRisksByType(company,year, Risk.ZHONGDA);
            map.put("risksYiBan",risksYiBan);
            map.put("riskszhongDa",riskszhongDa);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("1.公司/集团面临的风险数量，一般风险数量，重大风险数量，异常");

        }
        return ResponseFormat.retParam(1, 200, map);
    }
    
    
    
    /**
     * 1.全集团风险领域统计：status = 6 and reportstatus=1，按照集团中的风险领域统计
     */
    @OperationLog(
            success = "公司/全集团风险领域统计查询处理成功",
            busType = "首页接口",
            fail = "公司/全集团风险领域统计查询处理失败",
            operationType = OperationType.SELECT,
            subType = "首页接口"
    )
    @Operation(summary = "1.公司/全集团风险领域统计；")
    @RequestMapping(value = "riskCatnameRisks",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    public JsonBean riskCatnameRisks(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "company", description = "公司查询条件 不传是集团，传是按照公司", required = false) @RequestParam(name = "company", required = false) BigDecimal company,
    		@Parameter(name = "year", description = "年度查询条件", required = false) @RequestParam(name = "year", required = false) String year
    		) throws Exception {
        HashMap<String, Integer> map = new HashMap<>();
        Map<String,Object> result = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();
            result= riskService.riskCatnameRisks(token,company);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("1.公司/全集团风险领域统计，异常");
        }
        return ResponseFormat.retParam(1, 200, result);
    }
    
    @OperationLog(
            success = "风险数量，未审批数量，已审批数量查询处理成功",
            busType = "首页接口",
            fail = "风险数量，未审批数量，已审批数量查询处理失败",
            operationType = OperationType.SELECT,
            subType = "首页接口"
    )
    @Operation(summary = "风险数量，未审批数量，已审批数量统计；")
    @RequestMapping(value = "riskNumbers",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    public JsonBean riskNumbers(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "company", description = "公司查询条件 不传是集团，传是按照公司", required = false) @RequestParam(name = "company", required = false) BigDecimal company,
    		@Parameter(name = "year", description = "年度查询条件", required = false) @RequestParam(name = "year", required = false) String year
    		) throws Exception {
        HashMap<String, Integer> map = new HashMap<>();
        Map<String,Object> result = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();
            result= riskService.riskNumbers(token,company);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("1.公司/全集团风险领域统计，异常");
        }
        return ResponseFormat.retParam(1, 200, result);
    }
    
    @OperationLog(
            success = "各公司风险报告统计图查询处理成功",
            busType = "首页接口",
            fail = "各公司风险报告统计图查询处理失败",
            operationType = OperationType.SELECT,
            subType = "首页接口"
    )
    @Operation(summary = "各公司风险报告统计图")
    @RequestMapping(value = "reportByOrg",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    public JsonBean reportByOrg(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "year", description = "年度查询条件", required = false) @RequestParam(name = "year", required = false) String year
    		) throws Exception {
        HashMap<String, Integer> map = new HashMap<>();
        Map<String,Object> result = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();
            result= riskService.reportByOrg(year);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("各公司风险报告统计图，异常");
        }
        return ResponseFormat.retParam(1, 200, result);
    }
    
    
    
    /**
     * 2.公司风险事件数量，一般风险事件数量，重大风险事件数量；
     */
    @OperationLog(
            success = "公司/集团风险事件数量，一般风险事件数量，重大风险事件数量查询处理成功",
            busType = "首页接口",
            fail = "公司/集团风险事件数量，一般风险事件数量，重大风险事件数量查询处理失败",
            operationType = OperationType.SELECT,
            subType = "首页接口"
    )
    @Operation(summary = "2.公司/集团风险事件数量，一般风险事件数量，重大风险事件数量；")
    @RequestMapping(value = "numberEventsRisks",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    public JsonBean numberEventsRisks(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "company", description = "公司查询条件  不传是集团，传是按照公司", required = false) @RequestParam(name = "company", required = false) BigDecimal company,
    		@Parameter(name = "year", description = "年度查询条件", required = false) @RequestParam(name = "year", required = false) String year
    		) throws Exception {
        HashMap<String, Integer> map = new HashMap<>();
        try {
//        	  if(StringUtils.isBlank(year)){
//		        	Calendar calendar = Calendar.getInstance();
//		             year = calendar.get(Calendar.YEAR)+"";
//		        }
            TblStaffUtil staffUtil = userProvider.get();
            final Integer yiBan = riskeventMapper.queryRisksNumberByType(company,"1",year);//一般事件风险
            final Integer zhongDa = riskeventMapper.queryRisksNumberByType(company,"2",year);//重要事件风险
            map.put("yiBan",yiBan);
            map.put("zhongDa",zhongDa);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("2.公司/集团风险事件数量，一般风险事件数量，重大风险事件数量；，异常");

        }
        return ResponseFormat.retParam(1, 200, map);
    }

    /**
     * 3.公司交易对手数量，异常交易对手数量；
     */
    @OperationLog(
            success = "公司交易对手数量，异常交易对手数量查询处理成功",
            busType = "首页接口",
            fail = "公司交易对手数量，异常交易对手数量查询处理失败",
            operationType = OperationType.SELECT,
            subType = "首页接口"
    )
    @Operation(summary = "3.公司交易对手数量，异常交易对手数量；")
    @RequestMapping(value = "NumberOpponents",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    public JsonBean NumberOpponents(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        HashMap<String, Integer> map = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();
            int ZHENGCHANG = yyCompanyMapper.queryNormalNumber(staffUtil.getCurrentOrg().getOrgid());
            int YICHANG = yyCompanyMapper.queryAbnormalNumber(staffUtil.getCurrentOrg().getOrgid());
            map.put("ZHENGCHANG",ZHENGCHANG);
            map.put("YICHANG",YICHANG);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("3.公司交易对手数量，异常交易对手数量，异常");
        }
        return ResponseFormat.retParam(1, 200, map);
    }

    /**
     * 4.公司下属单位数量；
     */
  /*  @RequestMapping(value = "NumberOpponents")
    public JsonBean NumberOpponents(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        HashMap<String, Integer> map = new HashMap<>();
        TblStaffUtil staffUtil = userProvider.get();
        map.put("0",0);
        map.put("1",1);
        return ResponseFormat.retParam(1, 200, map);
    }*/
    
    
	   @OperationLog(
			success = "按照风险点统计评估任务完成情况成功",
			busType = "风控模块",
			fail = "按照风险点统计评估任务完成情况失败",
			operationType = OperationType.SELECT,
			subType = "首页分析"
	)
    @Operation(summary = "按照风险点统计评估任务完成情况")
    @RequestMapping(value = "getRiskPointTask",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    public JsonBean getRiskPointTask(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
  		  @Parameter(name = "company", description = "公司查询条件", required = false)@RequestParam(name="company",required = false)String company) throws Exception {
    	 Map<String,Object> result = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();
            result= riskAssplanService.getRiskPointTask(token,company);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("按照风险点统计评估任务完成情况，异常");
        }
        return ResponseFormat.retParam(1, 200, result);
    }
  
    @OperationLog(
 			success = "按照风险类型统计风险评估结果成功",
 			busType = "风控模块",
 			fail = "按照风险类型统计风险评估结果失败",
 			operationType = OperationType.SELECT,
 			subType = "首页分析"
 	)
     @Operation(summary = "按照风险类型统计风险评估结果")
     @RequestMapping(value = "getRiskCatnameAnalysis",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
     public JsonBean getRiskCatnameAnalysis(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
   		  @Parameter(name = "company", description = "公司查询条件", required = false)@RequestParam(name="company",required = false)String company) throws Exception {
     	 Map<String,Object> result = new HashMap<>();
         try {
             TblStaffUtil staffUtil = userProvider.get();
             result= riskAssplanRiskService.getRiskCatnameAnalysis(token,company);
         } catch (Exception e) {
             e.printStackTrace();
             log.info("按照风险类型统计风险评估结果，异常");
         }
         return ResponseFormat.retParam(1, 200, result);
     }
     
     @OperationLog(
		success = "按照风险点统计风险评估结果成功",
		busType = "风控模块",
		fail = "按照风险点统计风险评估结果失败",
		operationType = OperationType.SELECT,
		subType = "首页分析"
)
@Operation(summary = "按照风险点统计风险评估结果")
@RequestMapping(value = "getRiskAnalysis",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
public JsonBean getRiskAnalysis(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	  @Parameter(name = "company", description = "公司查询条件", required = false)@RequestParam(name="company",required = false)String company) throws Exception {
	 Map<String,Object> result = new HashMap<>();
  try {
      TblStaffUtil staffUtil = userProvider.get();
      result= riskAssplanRiskService.getRiskAnalysis(token,company);
  } catch (Exception e) {
      e.printStackTrace();
      log.info("按照风险点统计风险评估结果，异常");
  }
  return ResponseFormat.retParam(1, 200, result);
}


 @OperationLog(
success = "按照风险领域统计风险评估结果成功",
busType = "风控模块",
fail = "按照风险领域统计风险评估结果失败",
operationType = OperationType.SELECT,
subType = "首页分析"
)
	@Operation(summary = "按照风险领域统计风险评估结果")
	@RequestMapping(value = "getRiskAreasAnalysis", method = {
			RequestMethod.GET }, produces = "application/json; charset=utf-8")
	public JsonBean getRiskAreasAnalysis(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "company", description = "公司查询条件", required = false) @RequestParam(name = "company", required = false) String company)
					throws Exception {
		Map<String, Object> result = new HashMap<>();
		try {
			TblStaffUtil staffUtil = userProvider.get();
			result = riskAssplanRiskService.getRiskAreasAnalysis(token, company);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("按照风险领域统计风险评估结果，异常");
		}
		return ResponseFormat.retParam(1, 200, result);
	}
	
	@OperationLog(
	success = "按部门统计风险评估结果成功",
	busType = "风控模块",
	fail = "按部门统计风险评估结果失败",
	operationType = OperationType.SELECT,
	subType = "首页分析"
	)
		@Operation(summary = "按部门统计风险评估结果")
		@RequestMapping(value = "getRiskByDepartmentAnalysis", method = {
				RequestMethod.GET }, produces = "application/json; charset=utf-8")
		public JsonBean getRiskByDepartmentAnalysis(
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
				@Parameter(name = "company", description = "公司查询条件", required = false) @RequestParam(name = "company", required = false) String company)
						throws Exception {
			Map<String, Object> result = new HashMap<>();
			try {
				TblStaffUtil staffUtil = userProvider.get();
				result = riskAssplanRiskService.getRiskByDepartmentAnalysis(token, company);
			} catch (Exception e) {
				e.printStackTrace();
				log.info("按部门统计风险评估结果，异常");
			}
			return ResponseFormat.retParam(1, 200, result);
		}
		
		
		 @OperationLog(
		success = "按部门统计风险评估结果成功",
		busType = "风控模块",
		fail = "按部门统计风险评估结果失败",
		operationType = OperationType.SELECT,
		subType = "首页分析"
		)
			@Operation(summary = "按单位（公司）统计风险评估结果")
			@RequestMapping(value = "getRiskByCompanyAnalysis", method = {
					RequestMethod.GET }, produces = "application/json; charset=utf-8")
			public JsonBean getRiskByCompanyAnalysis(
					@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
					@Parameter(name = "company", description = "公司查询条件", required = false) @RequestParam(name = "company", required = false) String company)
							throws Exception {
				Map<String, Object> result = new HashMap<>();
				try {
					TblStaffUtil staffUtil = userProvider.get();
					result = riskAssplanRiskService.getRiskByCompanyAnalysis(token,company);
				} catch (Exception e) {
					e.printStackTrace();
					log.info("按单位（公司）统计风险评估结果，异常");
				}
				return ResponseFormat.retParam(1, 200, result);
			}
			
			 @OperationLog(
			success = "获取公司下拉成功",
			busType = "风控模块",
			fail = "获取公司下拉失败",
			operationType = OperationType.SELECT,
			subType = "首页分析"
			)
				@Operation(summary = "获取公司下拉")
				@RequestMapping(value = "getRiskCompanyList", method = {
						RequestMethod.GET }, produces = "application/json; charset=utf-8")
				public JsonBean getRiskCompanyList(
						@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token)
								throws Exception {
					Map<String, Object> result = new HashMap<>();
					try {
						TblStaffUtil staffUtil = userProvider.get();
						result = riskAssplanRiskService.getRiskCompanyList(token);
					} catch (Exception e) {
						e.printStackTrace();
						log.info("获取公司下拉，异常");
					}
					return ResponseFormat.retParam(1, 200, result);
				}
//-----------------------------集团风险分析接口
				@OperationLog(
						success = "1.全集团一般风险、重大风险趋势图",
						busType = "风控模块",
						fail = "1.全集团一般风险、重大风险趋势图",
						operationType = OperationType.SELECT,
						subType = "首页分析"
						)
							@Operation(summary = "1.全集团一般风险、重大风险趋势图")
							@RequestMapping(value = "getGroupRiskTrendChart", method = {
									RequestMethod.GET }, produces = "application/json; charset=utf-8")
							public JsonBean getGroupRiskTrendChart(
									@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
									@Parameter(name = "year", description = "年度查询条件", required = false) @RequestParam(name = "year", required = false) String year)
											throws Exception {
								Map<String, Object> result = new HashMap<>();
								try {
									TblStaffUtil staffUtil = userProvider.get();
									if(StringUtils.isBlank(year)){
							        	Calendar calendar = Calendar.getInstance();
							             year = calendar.get(Calendar.YEAR)+"";
							        }
									result = riskService.getGroupRiskTrendChart(token,year);
								} catch (Exception e) {
									e.printStackTrace();
									log.info("1.全集团一般风险、重大风险趋势图结果，异常");
								}
								return ResponseFormat.retParam(1, 200, result);
							}
				
				
				@OperationLog(
						success = "各公司风险数量统计/全集团按年度风险趋势图",
						busType = "风控模块",
						fail = "各公司风险数量统计//全集团按年度风险趋势图",
						operationType = OperationType.SELECT,
						subType = "首页分析"
						)
							@Operation(summary = "各公司风险数量统计//全集团按年度风险趋势图")
							@RequestMapping(value = "getCountByOrg", method = {
									RequestMethod.GET }, produces = "application/json; charset=utf-8")
							public JsonBean getCountByOrg(
									@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
									@Parameter(name = "year", description = "全集团按年度风险趋势图年度查询条件", required = false) @RequestParam(name = "year", required = false) String year,
									@Parameter(name = "type", description = "tj,1;qst,2", required = true) @RequestParam(name = "type", required = true) String type
									)
											throws Exception {
								Map<String, Object> result = new HashMap<>();
								try {
									TblStaffUtil staffUtil = userProvider.get();
									  if(StringUtils.isBlank(year)&&type.equals(2)){
								        	Calendar calendar = Calendar.getInstance();
								             year = calendar.get(Calendar.YEAR)+"";
								        }
									result = riskService.getCountByOrg(year);
								} catch (Exception e) {
									e.printStackTrace();
									log.info("各公司风险数量统计，异常");
								}
								return ResponseFormat.retParam(1, 200, result);
							}
				
				
				@OperationLog(
						success = "2.全集团按年度风险趋势图",
						busType = "风控模块",
						fail = "2.全集团按年度风险趋势图",
						operationType = OperationType.SELECT,
						subType = "首页分析"
						)
							@Operation(summary = "2.全集团按年度风险趋势图")
							@RequestMapping(value = "getAnnualRiskGroup", method = {
									RequestMethod.GET }, produces = "application/json; charset=utf-8")
							public JsonBean getAnnualRiskGroup(
									@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token)
											throws Exception {
								Map<String, Object> result = new HashMap<>();
								try {
									TblStaffUtil staffUtil = userProvider.get();
									result = riskService.getAnnualRiskGroup(token);
								} catch (Exception e) {
									e.printStackTrace();
									log.info("2.全集团按年度风险趋势图结果，异常");
								}
								return ResponseFormat.retParam(1, 200, result);
							}
				
				

				@OperationLog(
						success = "3.按公司风险应对统计图",
						busType = "风控模块",
						fail = "3.按公司风险应对统计图",
						operationType = OperationType.SELECT,
						subType = "首页分析"
						)
							@Operation(summary = "3.ongsi公司风险应对统计图")
							@RequestMapping(value = "getCompanyRiskResponse", method = {
									RequestMethod.GET }, produces = "application/json; charset=utf-8")
							public JsonBean getAGroup(
									@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
									@Parameter(name = "year", description = "年度查询条件", required = false) @RequestParam(name = "year", required = false) String year)
											throws Exception {
								Map<String, Object> result = new HashMap<>();
								try {
									  if(StringUtils.isBlank(year)){
								        	Calendar calendar = Calendar.getInstance();
								             year = calendar.get(Calendar.YEAR)+"";
								        }
									TblStaffUtil staffUtil = userProvider.get();
									result = copingService.getCompanyRiskResponse(token,year);
								} catch (Exception e) {
									e.printStackTrace();
									log.info("3.按公司风险应对统计图结果，异常");
								}
								return ResponseFormat.retParam(1, 200, result);
							}
				
				

				@OperationLog(
						success = "4.按公司风险审查统计图",
						busType = "风控模块",
						fail = "4.按公司风险审查统计图",
						operationType = OperationType.SELECT,
						subType = "首页分析"
						)
							@Operation(summary = "4.按公司风险审查统计图")
							@RequestMapping(value = "getCompanyRiskReview", method = {
									RequestMethod.GET }, produces = "application/json; charset=utf-8")
							public JsonBean getCompanyRiskReview(
									@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
									@Parameter(name = "year", description = "年度查询条件", required = false) @RequestParam(name = "year", required = false) String year
									)
								throws Exception {
								Map<String, Object> result = new HashMap<>();
								try {
									  if(StringUtils.isBlank(year)){
								        	Calendar calendar = Calendar.getInstance();
								             year = calendar.get(Calendar.YEAR)+"";
								        }
									TblStaffUtil staffUtil = userProvider.get();
									result = tblRiskReviewService.getCompanyRiskReview(token,year);
								} catch (Exception e) {
									e.printStackTrace();
									log.info("4.按公司风险审查统计图结果，异常");
								}
								return ResponseFormat.retParam(1, 200, result);
							}
				
				@OperationLog(
						success = "5.按公司风险事件统计图",
						busType = "风控模块",
						fail = "5.按公司风险事件统计图",
						operationType = OperationType.SELECT,
						subType = "首页分析"
						)
				@Operation(summary = "5.按公司风险事件统计图")
				@RequestMapping(value = "getCompanyRiskEventList", method = {
						RequestMethod.GET }, produces = "application/json; charset=utf-8")
				public JsonBean getCompanyRiskEventList(
						@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
						@Parameter(name = "year", description = "年度查询条件", required = false) @RequestParam(name = "year", required = false) String year
						)
					throws Exception {
					 Map<String, Object> map=new HashMap<String, Object>();
					try {
//						  if(StringUtils.isBlank(year)){
//					        	Calendar calendar = Calendar.getInstance();
//					             year = calendar.get(Calendar.YEAR)+"";
//					        }
						TblStaffUtil staffUtil = userProvider.get();
						map =iRiskeventService.getCompanyRiskEventList(token,year);
					} catch (Exception e) {
						e.printStackTrace();
						log.info("5.按公司风险事件统计图结果，异常");
					}
					return ResponseFormat.retParam(1, 200, map);
				}
				
				
				
	@OperationLog(success = "公司月度评估管控措施完成情况", busType = "风控模块", fail = "风险管控措施信息统计", operationType = OperationType.SELECT, subType = "首页分析")
	@Operation(summary = "公司月度评估管控措施完成情况")
	@RequestMapping(value = "getControlmatrixCount", method = {
			RequestMethod.GET }, produces = "application/json; charset=utf-8")
	public JsonBean getControlmatrixCount(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "orgid", description = "全集团不用传，分公司传", required = false) @RequestParam(name = "orgid", required = false) String orgid
)	throws Exception {
		Map<String, Object> result = new HashMap<>();
		try {
			TblStaffUtil staffUtil = userProvider.get();
			result = controlmatrixService.getControlmatrixCount(orgid);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("风险管控措施信息统计，异常");
		}
		return ResponseFormat.retParam(1, 200, result);
	}

	
	@OperationLog(success = "公司月度评估管控措施完成情况--跳转列表", busType = "风控模块", fail = "风险管控措施信息统计", operationType = OperationType.SELECT, subType = "首页分析")
	@Operation(summary = "公司月度评估管控措施完成情况--跳转列表")
	@RequestMapping(value = "getControlmatrixList", method = {
			RequestMethod.GET }, produces = "application/json; charset=utf-8")
	public JsonBean getControlmatrixList(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "orgid", description = "全集团不用传，分公司传", required = false) @RequestParam(name = "orgid", required = false) String orgid,
			@Parameter(name = "type", description = "wc ,wwc, yq", required = true) @RequestParam(name = "type", required = false) String type
			)	throws Exception { 
		Map<String, Object> result = new HashMap<>();
		try {
			TblStaffUtil staffUtil = userProvider.get();
			result = controlmatrixService.getControlmatrixList(orgid,type);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("公司月度评估管控措施完成情况--跳转列表，异常");
		}
		return ResponseFormat.retParam(1, 200, result);
	}
	
	
	//根据公司条件获取公司的未平估  已评估 关闭的数量
	@OperationLog(success = "风险评估热力图统计", busType = "风控模块", fail = "风险评估热力图统计", operationType = OperationType.SELECT, subType = "首页分析")
	@Operation(summary = "风险评估热力图统计")
	@RequestMapping(value = "getFxpgRlt", method = {
			RequestMethod.GET }, produces = "application/json; charset=utf-8")
	public JsonBean getFxpgRlt(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
)	throws Exception {
		Map<String, Object> result = new HashMap<>();
		try {
			TblStaffUtil staffUtil = userProvider.get();
			result = riskLevelmappingService.getFxpgRlt();
		} catch (Exception e) {
			e.printStackTrace();
			log.info("风险评估热力图统计，异常");
		}
		return ResponseFormat.retParam(1, 200, result);
	}
	
	
	  @OperationLog(
	            success = "查询热图列表成功",
	            busType = "风险评估",
	            fail = "查询热图列表失败",
	            operationType = OperationType.SELECT,
	            subType = "评估结果"
	    )
	    @RequestMapping(value = "/showPgjgRlt",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
	    @Operation(summary = "查询评估结果热图列表")
	    public JsonBean showPgjgRlt(
	    		@Parameter(name = "orgid", description = "传空为统计所有，传orgid 为对应公司", required = false)@RequestParam(value="orgid",required=false) String orgid,
	            @Parameter(name = "type", description = "传值：pgz、ypg、ygb", required = true) @RequestParam(value = "type",required = true) String type,
	            @Parameter(name = "value", description = "传值：pgz 1 / ypg 2  / ygb 0", required = true) @RequestParam(value = "value",required = true) String value,
	            @Parameter(name = "token", description = "登录用户token", required = true) @RequestParam(required = false) @RequestHeader("token") String token) throws Exception {
	        JsonBean jsonBean = null;
	        Map<String,Object> result = riskLevelmappingService.forSyCycle(orgid,type,value);
	        jsonBean = ResponseFormat.retParam(1, 200, result);
	        return jsonBean;
	    }

}
