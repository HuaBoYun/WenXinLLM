package com.huabo.cybermonitor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huabo.cybermonitor.service.ITblReportService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;



@RestController
@Tag(name="规则监控-规则库",description="规则监控-规则库")
@RequestMapping(value = "/cyber/JkbgReportController")
public class JkbgReportController {

	private static final Logger log = LoggerFactory.getLogger(JkbgReportController.class);
    /**
     * @author tyb
     * @date 2016-1-20 下午11:13:04
     * 报告编制
     */
    @Autowired
    public ITblReportService iTblReportService;

   /* public PageBean pageBean;

    @Operation(summary = "rule_base_add")
    @PostMapping(value = "/rule/rule_base_add")
    public JsonBean rule_base_add(@Parameter(name = "indicator", description = "indicator") @RequestBody TblOrganization tblOrganization,
                                  @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber") Integer pageNumber,
                                  @Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize") Integer pageSize,
                                  @Parameter(name = "type", description = "type") @RequestBody() String type,
                                  @Parameter(name = "projectId", description = "projectId") @RequestBody() String projectId,
                                  @Parameter(name = "view", description = "view") @RequestBody() String view,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception{
        //判断是否有token,token是否是正确的
        if (!ConstClass.checkToken(token)) {
            return ConstClass.tokenFailure();
        }
        // 在token 取到当前用户

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
            IPage<MonitorRule> iPage = new Page<>(pageNumber, pageSize);
            QueryWrapper<MonitorRule> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("inruledb", MonitorRule.IS_HY1);
            queryWrapper.eq("ORGID", orgId);
            if (StringUtils.isNotBlank(rulecode)) {
                queryWrapper.like("rulecode", "%" + rulecode + "%");
            }
            if (StringUtils.isNotBlank(rulename)) {
                queryWrapper.like("rulename", "%" + rulename + "%");
            }
            map.put("pageBean", iMonitorRuleService.page(iPage, queryWrapper));

        } else {
            if (null == orgId) {
                orgId = staff.getCurrentOrg().getOrgid();
            }
            IPage<MonitorRule> iPage = new Page<>(pageNumber, pageSize);
            QueryWrapper<MonitorRule> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("inruledb", MonitorRule.IS_HY1);
            queryWrapper.eq("ORGID", orgId);
            if (StringUtils.isNotBlank(rulecode)) {
                queryWrapper.like("rulecode", "%" + rulecode + "%");
            }
            if (StringUtils.isNotBlank(rulename)) {
                queryWrapper.like("rulename", "%" + rulename + "%");
            }
            queryWrapper.orderByDesc("ruleid");

            map.put("pageBean", iMonitorRuleService.page(iPage, queryWrapper));
        }
        map.put("orgId", orgId);
        map.put("isAdd", isSelect);
        map.put("isDU", isDU);
        map.put("rulecode", rulecode);
        return new JsonBean(200, "成功", map);


    }

    public ModelAndView listnkbg(HttpServletRequest request, Find find) {
        String type = request.getParameter("type");
        String projectId = request.getParameter("projectId");
        String view = request.getParameter("view");
        String identifier = "";
        if (StringUtils.isNotEmpty(type)) {
            if ("fx".equals(type)) {
                logger.info("风险管理---上-风险报告---风险报告编制---列表页 ");
            } else if ("nk".equals(type)) {
                logger.info("内部控制---上-内控报告---评价报告编制---列表页 ");
            } else if ("fx_zdy".equals(type)) {
                logger.info("风险管理---上-风险报告---自定义报告编制---列表页 ");
            } else if ("nk_zdy".equals(type)) {
                logger.info("内部控制---上-内控报告---自定义报告编制---列表页 ");
            } else if ("znjk".equals(type.trim())) {
                logger.info("智能监控---上-监控报告---监控报告编制---列表页");
            } else if ("znjk_zdy".equals(type.trim())) {
                logger.info("智能监控---上-监控报告---自定义报告编制---列表页");
            } else if ("nbsj".equals(type.trim())) {
                logger.info("内部审计---上-审计报告---审计报告编制---列表页");
                try {
                    identifier = activityPluginsService.getoNState(ProcessEnum.SJ_SJBG.name());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                TblnbsjProject project = this.tblnbsjProjectService.getSelectProject();
                if (projectId != null && projectId.length() > 0) {
                    project = tblnbsjProjectService.getId(projectId.toString());
                }
                if (project != null) {
                    find.setId(project.getProjectid());
                }
            } else if ("nbsj_gzt".equals(type.trim())) {
                logger.info("内部审计---上-审计报告---审计报告编制---列表页");
                try {
                    identifier = activityPluginsService.getoNState(ProcessEnum.SJ_SJBG.name());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                TblnbsjProject project = this.tblnbsjProjectService.getSelectProject();
                if (projectId != null && projectId.length() > 0) {
                    project = tblnbsjProjectService.getId(projectId.toString());
                }
                if (project != null) {
                    find.setId(project.getProjectid());
                }
            } else if ("nbsj_zdy".equals(type.trim())) {
                TblnbsjProject project = this.tblnbsjProjectService.getSelectProject();
                if (projectId != null && projectId.length() > 0) {
                    project = tblnbsjProjectService.getId(projectId.toString());
                }
                if (project != null) {
                    find.setId(project.getProjectid());
                }
                logger.info("内部审计---上-审计报告---自定义报告编制---列表页");
            }
        }
        String num = request.getParameter("pageNumber");
        Integer pageNumber = Integer.parseInt(num == null ? "1" : num);
        TblOrganization organization = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
        pageBean = tblReportService.findAll(pageNumber, pageBean.getPageSize(), find, type, organization.getOrgid());
        ModelAndView mv = new ModelAndView("nbkz/nkbg/creport_list");
        mv.addObject("pageBean", pageBean);
        mv.addObject("view", find.getView());
        mv.addObject("find", find);
        mv.addObject("projectId", projectId);
        // 查询框代码
        String choiceSearch = request.getParameter("choiceSearch");
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        mv.addObject("choiceSearch", choiceSearch);
        String parameter = request.getParameter("fication");
        mv.addObject("fication", parameter);// 项目归档返回
        mv.addObject("identifier", identifier);
        mv.addObject("view", view);
        mv.addObject("type", type);
        return mv;
    }*/
}
