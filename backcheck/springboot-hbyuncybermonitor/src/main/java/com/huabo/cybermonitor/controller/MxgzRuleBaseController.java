package com.huabo.cybermonitor.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.huabo.cybermonitor.entity.MonitorModel;
import com.huabo.cybermonitor.service.IMonitorModelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@Tag(name="模型监控-模型库",description="模型监控-模型库")
@RequestMapping(value = "/cyber/MxgzRuleBaseController")
public class MxgzRuleBaseController {

	private static final Logger log = LoggerFactory.getLogger(MxgzRuleBaseController.class);

    @Autowired
    IMonitorModelService iMonitorModelService;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 行业模型库 -- 新增
     * @param monitorModel
     * @param date
     * @param token
     * @return
     * @throws Exception
     */
    @Operation(summary = "rule_base_add")
    @PostMapping(value = "/rule/rule_base_add")
    public JsonBean rule_base_add(@Parameter(name = "monitorModel", description = "monitorModel") @RequestBody MonitorModel monitorModel,
                                  @Parameter(name = "date", description = "date") @RequestParam(value = "date") String date,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        //判断是否有token,token是否是正确的
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        if (StringUtils.isNotEmpty(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                monitorModel.setCreatedate(LocalDateTime.parse(date));
            } catch (Exception e) {

            }
        }
        // 在token 取到当前用户
        monitorModel.setInmodeldb(MonitorModel.IS_HY1.toString());
        iMonitorModelService.save(monitorModel);
        return new JsonBean(200, "成功", "/znjk/gzjk/rule/rulesmgmt?orgId=" + monitorModel.getOrgid());
    }


    /**
     * 业模型库 --列表
     * @param pageNumber
     * @param pageSize
     * @param modelcode
     * @param modelname
     * @param orgId
     * @param token
     * @return
     * @throws Exception
     */
    @Operation(summary = "hy_rulesmgmt")
    @GetMapping(value = "/rule/hy_rulesmgmt")
    public JsonBean hy_rulesmgmt(
            @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber") Integer pageNumber,
            @Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize") Integer pageSize,
            @Parameter(name = "modelcode", description = "modelcode") @RequestParam(value = "modelcode") String modelcode,
            @Parameter(name = "modelname", description = "modelname") @RequestParam(value = "modelname") String modelname,
            @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId",required = false) BigDecimal orgId,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        //判断是否有token,token是否是正确的
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}

        Map map = new HashMap();
        Boolean isSelect = false;
        boolean isDU = false;
        //根据用户当前所在的公司的Orgid和用户隶属的公司的orgid相比较
        if (staff.getCurrentOrg().getOrgid().equals(staff.getLinkOrg().getOrgid())) {
            if (null == orgId) {
                //如果orgid为空这为他赋值
                //Organization byId = iOrganizationService.getById(staff.getOrgid());
                orgId = staff.getLinkDetp().getOrgid();
            }
            IPage<MonitorModel> iPage = new Page<>(pageNumber, pageSize);
            QueryWrapper<MonitorModel> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("inmodeldb", MonitorModel.IS_HY1);
            queryWrapper.eq("ORGID", orgId);
            if (StringUtils.isNotBlank(modelcode)) {
                queryWrapper.like("modelcode", "%" + modelcode + "%");
            }
            if (StringUtils.isNotBlank(modelname)) {
                queryWrapper.like("modelname", "%" + modelname + "%");
            }
            map.put("pageBean", iMonitorModelService.page(iPage, queryWrapper));

        } else {
            if (null == orgId) {
                //如果orgid为空这为他赋值
                orgId = staff.getCurrentOrg().getOrgid();
            }
            IPage<MonitorModel> iPage = new Page<>(pageNumber, pageSize);
            QueryWrapper<MonitorModel> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("inmodeldb", MonitorModel.IS_HY1);
            queryWrapper.eq("ORGID", orgId);
            if (StringUtils.isNotBlank(modelcode)) {
                queryWrapper.like("modelcode", "%" + modelcode + "%");
            }
            if (StringUtils.isNotBlank(modelname)) {
                queryWrapper.like("modelname", "%" + modelname + "%");
            }
            queryWrapper.orderByDesc("ruleid");

            map.put("pageBean", iMonitorModelService.page(iPage, queryWrapper));
        }
        map.put("orgId", orgId);
        map.put("isAdd", isSelect);
        map.put("isDU", isDU);
        map.put("modelcode", modelcode);
        map.put("modelname", modelname);
        return new JsonBean(200, "成功", map);
    }

    /**
     * 规则库 ---跳转修改
     *
     * @param
     * @return
     */
    @Operation(summary = "toRuleModify")
    @GetMapping(value = "/rule/to_rule_modify")
    public JsonBean toRuleModify(@Parameter(name = "selectedruleid", description = "selectedruleid") @RequestParam("selectedruleid") String selectedruleid,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorModel monitorModel = iMonitorModelService.getById(selectedruleid);
        Map map = new HashMap();
        //Organization byId = iOrganizationService.getById(staff.getStaffid());
        //BigDecimal orgId = byId.getOrgid();
        BigDecimal orgId = staff.getLinkDetp().getOrgid();
        map.put("orgId", orgId);
        map.put("orgName", staff.getLinkDetp().getOrgname());
        if (monitorModel != null) {
            map.put("monitorModel", monitorModel);
        }
        return new JsonBean(200, "成功", map);
    }

    /**
     * 行业模型库 --修改保存操作 rule solutionrules
     * @param orgid
     * @param modelid
     * @param modelcode
     * @param modelname
     * @param modelcategory
     * @param modelstatus
     * @param modelreminder
     * @param connectionstrings
     * @param modeldes
     * @param solutionrules
     * @param modelstep1
     * @param modelstep2
     * @param modelstep3
     * @param modelstep4
     * @param modelstep5
     * @param creater
     * @param choiceSearch
     * @param date
     * @param token
     * @return
     */
    @Transactional
    @Operation(summary = "ruleModify")
    @PostMapping(value = "/rule/rule_modify")
    //solutionrules没有什么用
    public JsonBean ruleModify(@Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid") String orgid,
                               @Parameter(name = "modelid", description = "modelid") @RequestParam(value = "modelid") String modelid,
                               @Parameter(name = "modelcode", description = "modelcode") @RequestParam(value = "modelcode") String modelcode,
                               @Parameter(name = "modelname", description = "modelname") @RequestParam(value = "modelname") String modelname,
                               @Parameter(name = "modelcategory", description = "modelcategory") @RequestParam(value = "modelcategory") String modelcategory,
                               @Parameter(name = "modelstatus", description = "modelstatus") @RequestParam(value = "modelstatus") String modelstatus,
                               @Parameter(name = "modelreminder", description = "modelreminder") @RequestParam(value = "modelreminder") String modelreminder,
                               @Parameter(name = "connectionstrings", description = "connectionstrings") @RequestParam(value = "connectionstrings") String connectionstrings,
                               @Parameter(name = "modeldes", description = "modeldes") @RequestParam(value = "modeldes") String modeldes,
                               @Parameter(name = "solutionrules", description = "solutionrules") @RequestParam(value = "solutionrules") String solutionrules,
                               @Parameter(name = "modelstep1", description = "modelstep1") @RequestParam(value = "modelstep1") String modelstep1,
                               @Parameter(name = "modelstep2", description = "modelstep2") @RequestParam(value = "modelstep2") String modelstep2,
                               @Parameter(name = "modelstep3", description = "modelstep3") @RequestParam(value = "modelstep3") String modelstep3,
                               @Parameter(name = "modelstep4", description = "modelstep4") @RequestParam(value = "modelstep4") String modelstep4,
                               @Parameter(name = "modelstep5", description = "modelstep5") @RequestParam(value = "modelstep5") String modelstep5,
                               @Parameter(name = "creater", description = "creater") @RequestParam(value = "creater") String creater,
                               @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch") String choiceSearch,
                               @Parameter(name = "date", description = "date") @RequestParam(value = "date") String date,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        /* 修改结束 */
        MonitorModel  monitorModel = iMonitorModelService.getById(modelid);
        monitorModel.setModelcode(modelcode);
        monitorModel.setModelname(modelname);
        monitorModel.setModelcategory(modelcategory);
        monitorModel.setModelstatus(modelstatus);
        monitorModel.setModelreminder(modelreminder);
        monitorModel.setConnectionstrings(connectionstrings);
        monitorModel.setModeldes(modeldes);
        monitorModel.setModelstep1(modelstep1);
        monitorModel.setModelstep1(modelstep2);
        monitorModel.setModelstep1(modelstep3);
        monitorModel.setModelstep1(modelstep4);
        monitorModel.setModelstep1(modelstep5);
        if (StringUtils.isNotEmpty(date)) {
            try {
                monitorModel.setCreatedate(LocalDateTime.parse(date));
            } catch (Exception e) {
            }
        }
        Map map = new HashMap();
        try {
            iMonitorModelService.updateById(monitorModel);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(500, "sql语句出错", "");
        }
        map.put("orgId", orgid);
        return new JsonBean(200, "成功", map);
    }



    /**
     * 规则管理 --删除
     *
     * @param
     * @return
     * @author SongXiangYing
     * @date 2016年1月19日 下午7:24:42
     */
    @Operation(summary = "ruleDel")
    @GetMapping(value = "/rule/rule_del")
    @Transactional
    public JsonBean ruleDel(@Parameter(name = "selectedruleid", description = "selectedruleid") @RequestParam(value = "selectedruleid") String selectedruleid,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        if (selectedruleid != null) {
            MonitorModel monitorModel = iMonitorModelService.getById(selectedruleid);
            if (Objects.isNull(monitorModel)) {
                return new JsonBean(500, "通过" + selectedruleid + "没有找到规则！！", null);
            }
            try {
                iMonitorModelService.removeById(monitorModel);
            } catch (Exception e) {
                e.printStackTrace();
                return new JsonBean(500, e.getMessage(), "");
            }
            return new JsonBean(200, "成功", map);
        }
        return new JsonBean(500, "请选择规则id", null);
    }





}
