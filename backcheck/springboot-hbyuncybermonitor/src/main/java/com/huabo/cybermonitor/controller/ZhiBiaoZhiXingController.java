package com.huabo.cybermonitor.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.cybermonitor.mapper.YhrPageMapper;
import com.huabo.cybermonitor.service.IAttachmentService;
import com.huabo.cybermonitor.service.IBugCriterionService;
import com.huabo.cybermonitor.service.IIndicatorService;
import com.huabo.cybermonitor.service.IMonitorExeintervalService;
import com.huabo.cybermonitor.service.IMonitorModelService;
import com.huabo.cybermonitor.service.IMonitorModelsolutionService;
import com.huabo.cybermonitor.service.IMonitorRuleService;
import com.huabo.cybermonitor.service.IMonitorSolutionRuleService;
import com.huabo.cybermonitor.service.IMonitorSolutionService;
import com.huabo.cybermonitor.service.IOrganizationService;
import com.huabo.cybermonitor.service.IStaffService;
import com.huabo.cybermonitor.service.IWorksheetService;
import com.huabo.cybermonitor.service.TreeService;
import com.huabo.cybermonitor.task.ZNJKGZYJTask;
import com.huabo.cybermonitor.task.ZNJKMXYJTask;
import com.huabo.cybermonitor.task.ZNJKZBYJTask;
import com.huabo.cybermonitor.task.base.JobTaskService;
import com.huabo.cybermonitor.util.ConstClass;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;




@RestController
@Slf4j
@Tag(name="监控执行-指标执行",description="监控执行-指标执行")
@RequestMapping(value = "/cyber/ZhiBiaoZhiXingController")
@SuppressWarnings("all")
public class ZhiBiaoZhiXingController {

    private static final Logger logger = LoggerFactory.getLogger(ZhiBiaoZhiXingController.class);

    @Autowired
    IOrganizationService iOrganizationService;

    @Autowired
    IMonitorModelsolutionService iMonitorModelsolutionService;

    @Autowired
    IMonitorExeintervalService iMonitorExeintervalService;

    @Autowired
    IMonitorSolutionRuleService iMonitorSolutionRuleService;

    @Autowired
    IMonitorSolutionService iMonitorSolutionService;

    @Autowired
    IWorksheetService iWorksheetService;

    @Autowired
    IAttachmentService iAttachmentService;

    @Autowired
    public ZNJKZBYJTask znjkzbyjTask;

    @Autowired
    public ZNJKGZYJTask znjkgzyjTask;

    @Autowired
    public JobTaskService jobTaskService;

    @Autowired
    IStaffService iStaffService;

    @Autowired
    IBugCriterionService iBugCriterionService;

    @Autowired
    TreeService treeService;

    @Autowired
    IMonitorModelService iMonitorModelService;

    @Autowired
    IIndicatorService iIndicatorService;

    @Resource
    public ZNJKMXYJTask znjkmxyjTask;

    @Resource
    public IMonitorRuleService iMonitorRuleService;

    @Resource
    YhrPageMapper pageMapper;


    @Resource
    private UserProvider userProvider;
    
    /**
     * 监控执行：指标预警-列表
     *
     */
    @Operation(summary = "监控执行：指标执行 --右侧指标执行列表分页查询")
    @PostMapping(value = "/jkjg/kri_zhibiao_info")
    public JsonBean kri_zhibiao_info(@Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId", required = false) BigDecimal orgId,
                                     @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber",required = false,defaultValue = "1") Integer pageNumber,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        try {
        	TblStaffUtil staff = userProvider.get();
     		if (staff == null) {
     			return ResponseFormat.retParam(0, 20006, null);
     		}
            orgId = Objects.isNull(orgId)?staff.getLinkDetp().getOrgid(): orgId;

            Boolean isSelect = treeService.isSJByOrgId(staff.getLinkDetp().getOrgid().toString(), orgId.toString());

            StringBuilder sb=new  StringBuilder();

            sb.append("select t.*,REALNAME,USERNAME from TBL_MONITOR_SOLUTION  t left join TBL_STAFF on t.STAFFID = TBL_STAFF.STAFFID ")
                    .append(" where t.type=2 and t.SOLUTIONSTATUS='启用' ");
            if (isSelect) {
                sb.append(" and t.orgid="+orgId);

            } else {
                sb.append(" and t.orgid="+ staff.getLinkDetp().getOrgid());

            }
            sb.append(" order by t.createdate desc");

            IPage<Map<String,Object>>  page =new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);
            page=pageMapper.getPage(page,sb.toString());

            Map<String, Object> map = new HashMap<>(3);
            map.put("orgId", orgId);
            map.put("pageBean", page);
            map.put("tblStaff", staff);
            return new JsonBean(200, "success", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200,"failure","");
    }


    /**
     * 指标预警 --结果
     *
     * @author SongXiangYing
     * @date 2016年1月31日 上午2:18:03
     * @param pageNumber
     * @param request
     * @return
     */
    @Operation(summary = "指标执行 --结果")
    @GetMapping(value = "/zbjk/kri_yj_info_result")
    public JsonBean kri_info_yj_reult(
            @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId", required = false) BigDecimal orgId,
            @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber",required = false,defaultValue = "1") Integer pageNumber,
            @Parameter(name = "id", description = "id") @RequestParam(value = "id", required = false) BigDecimal id,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}

        String sql="select t.*,TBL_STAFF.REALNAME,TBL_STAFF.USERNAME from TBL_MONITOR_INDICATORRESULT  t left join  TBL_STAFF  on t.STAFFID = TBL_STAFF.STAFFID where t.RESULTID in (select max(RESULTID) from TBL_MONITOR_INDICATORRESULT  GROUP BY EXECUTEID ) and t.SOLUTIONRESULTID = "+id+" ORDER BY t.SAVETIME DESC";
        // pageBean = this.tblIndicatorService.findByYjId(new BigDecimal(id),
        // pageNumber, pageBean.getPageSize());


        IPage<Map<String,Object>>  page =new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);
        page=pageMapper.getPage(page,sql);

        Map<String, Object> map = new LinkedHashMap<>(3);
        map.put("orgId", orgId);
        map.put("id", id);
        map.put("pageBean", page);

        return new JsonBean(200, "success", map);
    }


}
