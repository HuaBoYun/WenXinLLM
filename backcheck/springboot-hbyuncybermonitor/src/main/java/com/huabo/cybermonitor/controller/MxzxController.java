package com.huabo.cybermonitor.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.cybermonitor.entity.MonitorSolution;
import com.huabo.cybermonitor.service.IMonitorModelService;
import com.huabo.cybermonitor.service.IMonitorModelsolutionService;
import com.huabo.cybermonitor.service.IMonitorSolutionService;
import com.huabo.cybermonitor.service.IMonitorSolutionresultService;
import com.huabo.cybermonitor.service.IStaffService;
import com.huabo.cybermonitor.service.TreeService;
import com.huabo.cybermonitor.util.ConstClass;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;




/**
 * 监控执行-模型执行api接口
 *
 * @createTime 2022/8/13
 */
@RestController
@Slf4j
@Tag(name="监控执行-模型执行",description="监控执行-模型执行")
@RequestMapping(value = "/cyber/MxzxController")
@SuppressWarnings("all")
public class MxzxController {

	private static final Logger log = LoggerFactory.getLogger(MxzxController.class);

    @Autowired
    TreeService treeService;

    @Autowired
    IMonitorModelService iMonitorModelService;

    @Autowired
    IMonitorSolutionService iMonitorSolutionService;

    @Autowired
    IStaffService iStaffService;

    @Autowired
    IMonitorModelsolutionService iMonitorModelsolutionService;

    @Autowired
    IMonitorSolutionresultService iMonitorSolutionresultService;
    
    @Resource
    private UserProvider userProvider;


    /**
     * 监控执行-模型执行
     *
     * @return
     */
    @Operation(summary = "modelssolutionmgmt")
    @GetMapping(value = "/jkzx/modelssolutionmgmt")
    public JsonBean modelssolutionmgmt(
                                       @Parameter(description="orgid") @RequestParam(value="orgid",required = false) String orgid,
                                       @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value="pageNumber",required = false,defaultValue = "1") Integer pageNumber,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {

        try {
        	TblStaffUtil staff = userProvider.get();
     		if (staff == null) {
     			return ResponseFormat.retParam(0, 20006, null);
     		}
            orgid = StringUtils.isBlank(orgid)? staff.getLinkDetp().getOrgid().toString() : orgid;
            Boolean isSelect = treeService.isSJByOrgId(staff.getLinkDetp().getOrgid().toString(), orgid);
            IPage<MonitorSolution> ip = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
            QueryWrapper<MonitorSolution> qw = new QueryWrapper<>();
            qw.eq("type", 3);
            qw.eq("SOLUTIONSTATUS", "启用");
            qw.eq("ORGID", orgid);
            qw.orderByDesc("createdate");
            if (isSelect) {
                qw.eq("ORGID", orgid);
            } else {
                qw.eq("ORGID", staff.getLinkDetp().getOrgid());
            }
            iMonitorSolutionService.page(ip, qw);
            Map<String, Object> mv = new HashMap<>();
            mv.put("pageBean", ip);
            mv.put("orgId", orgid);
            mv.put("tblStaff", iStaffService.getById(staff.getStaffid()));
            return new JsonBean(200, "success", mv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(-1, "failure", "");
    }

}
