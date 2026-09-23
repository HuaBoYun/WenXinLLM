package com.huabo.cybermonitor.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.cybermonitor.entity.Staff;
import com.huabo.cybermonitor.entity.Tree;
import com.huabo.cybermonitor.service.IAccbookService;
import com.huabo.cybermonitor.service.IAttachmentService;
import com.huabo.cybermonitor.service.IBugCriterionService;
import com.huabo.cybermonitor.service.IMonitorModelService;
import com.huabo.cybermonitor.service.IMonitorModelresultService;
import com.huabo.cybermonitor.service.IMonitorModelsolutionService;
import com.huabo.cybermonitor.service.IMonitorSolutionModelService;
import com.huabo.cybermonitor.service.IMonitorSolutionService;
import com.huabo.cybermonitor.service.IMonitorSolutionresultService;
import com.huabo.cybermonitor.service.IOrganizationService;
import com.huabo.cybermonitor.service.IStaffService;
import com.huabo.cybermonitor.service.IWarningResultService;
import com.huabo.cybermonitor.service.IWorksheetService;
import com.huabo.cybermonitor.service.TreeService;
import com.huabo.cybermonitor.service.base.CJobTaskService;
import com.huabo.cybermonitor.task.base.JobTaskService;
import com.huabo.cybermonitor.util.ConstClass;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 监控执行模块
 * @author kangjx
 * @createTime 2022/7/27
 */
@RestController
@Tag(name="监控执行模块",description="监控执行模块")
@RequestMapping(value = "/cyber/ExecutionController")
public class ExecutionController {

	private static final Logger log = LoggerFactory.getLogger(ExecutionController.class);

    @Autowired
    IOrganizationService iOrganizationService;

    @Autowired
    IMonitorModelService iMonitorModelService;

    @Autowired
    IMonitorSolutionService iMonitorSolutionService;

    @Autowired
    CJobTaskService cJobTaskService;

    @Autowired
    IMonitorSolutionresultService iMonitorSolutionresultService;

    @Autowired
    JobTaskService jobTaskService;

    @Autowired
    IMonitorModelsolutionService iMonitorModelsolutionService;

    @Autowired
    IMonitorModelresultService iMonitorModelresultService;



    @Autowired
    IWarningResultService iWarningResultService;

    @Autowired
    IAccbookService iAccbookService;

    @Autowired
    IAttachmentService iAttachmentService;

    @Autowired
    IWorksheetService iWorksheetService;

    @Autowired
    IBugCriterionService iBugCriterionService;
    @Autowired
    IStaffService  iStaffService;


    @Autowired
    TreeService treeService;

    @Resource
    private UserProvider userProvider;


    @Autowired
    IMonitorSolutionModelService iMonitorSolutionModelService;
    @Operation(summary = "solutionguizeleft")
    @GetMapping(value = "/jkjg/solutionguizeleft")
    public JsonBean solutionguizeleft() throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        String tree = iOrganizationService.getOrgTree("solutionmgmt");
        Map<String, Object> mv = new HashMap<>();
        mv.put("tree", tree);
        mv.put("targetFrame", "mainFramex");
        return new JsonBean(200, "success", mv);
    }

    /**
     * 监控执行：指标预警-主页
     *
     * @param
     * @return
     */
    @Operation(summary = "kri_zhibiao_index")
    @GetMapping(value = "/jkjg/kri_zhibiao_index")
    public JsonBean kri_zhibiao_index(@Parameter(description="user") @RequestParam("user") Staff user) throws Exception {
        Map<String, Object> mv = new HashMap<>();
        TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        mv.put("orgId", user.getOrgid());
        return new JsonBean(200, "success", mv);
    }

    /**
     * 监控执行：指标预警-左边
     *
     * @param
     * @return
     */
    @Operation(summary = "solutionzhibiaoleft")
    @GetMapping(value = "/jkjg/solutionzhibiaoleft")
    public JsonBean solutionzhibiaoleft(@Parameter(name = "orgId", description = "orgId") @RequestParam("orgId") String orgId) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map<String, Object> mv = new HashMap<>(2);
        mv.put("targetFrame", "mainFramex");
        mv.put("orgid", orgId);
        return new JsonBean(200, "success", mv);
    }

    /**
     * 监控执行：指标预警-tree
     *
     * @param nodeId
     * @param type
     * @param orgId
     * @param
     * @return
     */
    @Operation(summary = "jk_zbyjfindOrganizationByTree")
    @GetMapping(value = "/jkjg/findOrganizationByTreeAll")
    public JsonBean jk_zbyjfindOrganizationByTree(@Parameter(name = "nodeId", description = "nodeId") @RequestParam("nodeId") BigDecimal nodeId,
                                                  @Parameter(name = "type", description = "type") @RequestParam("type") String type,
                                                  @Parameter(name = "orgId", description = "orgId") @RequestParam("orgId") BigDecimal orgId) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
    	
        String json = "";
        if (null == nodeId) {
            nodeId = orgId;
        }
        if (StringUtils.isNotBlank(type)) {
            List<Tree> list = iOrganizationService.getNodeAll(nodeId);
            for (Tree tree : list) {
                if (!tree.getIsParent()) {
                    tree.setTarget("mainFramex");
                    tree.setUrl("kri_zhibiao_info?orgId=" + tree.getId());
                }
            }
            json = JSONObject.toJSONString(list);
        } else {
            List<Tree> list = iOrganizationService.getNodeAll(nodeId);
            for (Tree tree : list) {
                setUrlByTree(tree, "kri_zhibiao_info?orgId=");
            }
            json = JSONObject.toJSONString(list);

        }
        return new JsonBean(200, "success", json);
    }

    private void setUrlByTree(Tree tree, String url) {
        for (Tree tre : tree.getChildren()) {
            if (tre.getChildren().size() > 0) {
                setUrlByTree(tre, url);
            }
            tre.setTarget("mainFramex");
            tre.setUrl(url + tre.getId());
        }
    }


    /**
     * 监控执行：指标预警-列表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/jkjg/kri_zhibiao_info")
    @Operation(summary = "监控执行：指标预警-列表")
    public JsonBean kri_zhibiao_info(Staff staff, BigDecimal orgId, Integer pageNumber,String choiceSearch) throws Exception {

    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        IPage ip = new Page(pageNumber, ConstClass.DEFAULT_SIZE);
        if (orgId == null) {
            orgId = staff.getOrgid();
        }
        Boolean isSelect = treeService.isSJByOrgId(staff.getOrgid().toString(),
                orgId.toString());
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("type",2);
        queryWrapper.eq("SOLUTIONSTATUS","启用");
        queryWrapper.orderByDesc("createdate");
        if (isSelect) {
            queryWrapper.eq("ORGID",orgId);

        }else {
            queryWrapper.eq("ORGID",staff.getOrgid());
        }
        iMonitorSolutionService.page(ip,queryWrapper);
        Map<String,Object> map = new HashMap<>(3);
        map.put("orgId", orgId);
        map.put("pageBean", ip);
        map.put("tblStaff", staff);

        //为页面查找区域显隐藏赋值

        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功",map);
    }

}
