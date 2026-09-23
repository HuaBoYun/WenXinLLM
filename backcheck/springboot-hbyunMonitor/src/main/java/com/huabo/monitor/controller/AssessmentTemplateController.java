package com.huabo.monitor.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.monitor.oracle.entity.*;
import com.huabo.monitor.service.OrganizationService;
import com.huabo.monitor.service.TblAssEleCategoryService;
import com.huabo.monitor.service.TblAssesscategoryService;
import com.huabo.monitor.service.TblAssesstempleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@RestController
@Api(value = "评价模板", tags = {"评价模板相关"})
@RequestMapping(value = "/nbkz")
@Slf4j
public class AssessmentTemplateController extends BaseController implements ServletConfigAware {

    private static final Logger logger = LoggerFactory.getLogger(AssessmentTemplateController.class);

    @Resource
    private TblAssesstempleService tblAssesstempleService;

    @Resource
    private OrganizationService organizationService;

    @Resource
    private TblAssesscategoryService tblAssesscategoryService;

    @Resource
    private TblAssEleCategoryService tblAssEleCategoryService;

    /**
     * 评价模板
     *
     * @return
     */
    @RequestMapping(value = "/gzdg/def_tmpl_list")
    public JsonBean getgzdgBasicList(HttpServletRequest request,
                                     @ApiParam(name = "token", value = "登录用户token", required = true) @RequestHeader("token") String token,
                                     @ApiParam(name = "pageNumber", value = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                     @ApiParam(name = "pageSize", value = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                     @ApiParam(name = "templeNumber", value = "模板编号", required = false) @RequestParam(value = "templeNumber", required = false) String templeNumber,
                                     @ApiParam(name = "templename", value = "模板名称", required = false) @RequestParam(value = "templename", required = false) String templename) {

        JsonBean jsonBean = null;
        try {
            TblAssesstemple tblAssesstemple = new TblAssesstemple();
            tblAssesstemple.setTempleNumber(templeNumber);
            tblAssesstemple.setTemplename(templename);

            jsonBean = tblAssesstempleService.findByPageBean(pageNumber, pageSize, tblAssesstemple);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;

    }

    /**
     * 新建模板第一步
     *
     * @return
     */
    @PostMapping(value = "/gzdg/def_tmpl_add")
    public JsonBean gzdg_def_tmpl_add(@RequestBody TblAssesstemple tblAssesstemple) {
        return tblAssesstempleService.add(tblAssesstemple);
    }

    @RequestMapping(value = "/gzdg/def_tmpl_copy", produces = "application/json; charset=utf-8")
    public @ResponseBody
    String tempCopy(HttpServletRequest request,
                    TblAssesstemple tblAssesstemple, BigDecimal beforeTempId) {
        if (StringUtils.isNotBlank(tblAssesstemple.getReorg())
                && StringUtils.isNotBlank(tblAssesstemple.getTempleNumber())) {
            String[] orgids = tblAssesstemple.getReorg().split(",");
            Set<TblOrganization> list = new HashSet<TblOrganization>();
            List<TblAssEleCategory> aec = new ArrayList<TblAssEleCategory>();
            for (String string : orgids) {
                TblOrganization organization = this.organizationService.findById(string);
                if (null != organization) {
                    list.add(organization);
                }
            }
            TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 选则的机构
            List<TblAssesstemple> assesstemples = this.tblAssesstempleService
                    .getTmplByNumber(tblAssesstemple.getTempleNumber(), attribute.getOrgid());
            if (assesstemples.size() == 0) {
                TblStaff tblStaff = (TblStaff) request.getSession().getAttribute("longUser");
                tblAssesstemple.setStaff(tblStaff);
                tblAssesstemple.setModifyDateTime(new Date());
                tblAssesstemple.setOrganizations(list);
                tblAssesstemple.setTblComany(attribute);
                // 原来模板
                // TblAssesstemple assesstemple =
                // this.tblAssesstempleService.findByid(beforeTempId);
                JsonBean tmplId = this.tblAssesstempleService.add(tblAssesstemple);
                TblAssesstemple tblAssesstempleCopy = this.tblAssesstempleService.findByid(tmplId);
                List<TblAssesscategory> treeRoot = tblAssesscategoryService.getTreeRoot(beforeTempId);
                for (TblAssesscategory tblAssesscategory : treeRoot) {
                    TblAssesscategory tblAssesscategorycopy = new TblAssesscategory();
                    tblAssesscategorycopy.setTblAssesstemple(tblAssesstempleCopy);
                    tblAssesscategorycopy.setCatDes(tblAssesscategory.getCatDes());
                    tblAssesscategorycopy.setCatname(tblAssesscategory.getCatname());
                    tblAssesscategorycopy.setCatweight(tblAssesscategory.getCatweight());
                    tblAssesscategorycopy.setTblAssesstemple(tblAssesstempleCopy);
                    Serializable serializable = this.tblAssesscategoryService.add(tblAssesscategorycopy);
                    TblAssesscategory addId = this.tblAssesscategoryService
                            .get(new BigDecimal(serializable.toString()));
                    Set<TblAssEleCategory> categories = tblAssesscategory.getAssEleCategories();
                    if (categories.size() > 0) {
                        for (TblAssEleCategory tblAssEleCategory : categories) {
                            TblAssesselement assesselement = tblAssEleCategory.getAssesselement();
                            TblAssEleCategory assEleCategory = new TblAssEleCategory();
                            assEleCategory.setAssesscategory(addId);
                            assEleCategory.setAssesselement(assesselement);
                            aec.add(assEleCategory);
                        }
                    }
                    List<TblAssesscategory> treeNode = tblAssesscategoryService
                            .getTreeByNodeId(tblAssesscategory.getAsscatid());
                    for (TblAssesscategory tblAssesscategory2 : treeNode) {
                        for (TblAssesscategory tblAssesscategory3 : treeNode) {
                            TblAssesscategory tblAssesscategorycopy1 = new TblAssesscategory();
                            tblAssesscategorycopy1.setTblAssesstemple(tblAssesstempleCopy);
                            tblAssesscategorycopy1.setCatDes(tblAssesscategory3.getCatDes());
                            tblAssesscategorycopy1.setCatname(tblAssesscategory3.getCatname());
                            tblAssesscategorycopy1.setCatweight(tblAssesscategory3.getCatweight());
                            tblAssesscategorycopy1.setTblAssesstemple(tblAssesstempleCopy);
                            tblAssesscategorycopy1.setFatherasscatid(new BigDecimal(serializable.toString()));
                            Serializable serializable1 = this.tblAssesscategoryService.add(tblAssesscategorycopy1);
                            TblAssesscategory assesscategory = this.tblAssesscategoryService
                                    .get(new BigDecimal(serializable1.toString()));
                            Set<TblAssEleCategory> categoriess = tblAssesscategory2.getAssEleCategories();
                            if (categoriess.size() > 0) {
                                for (TblAssEleCategory tblAssEleCategory : categoriess) {
                                    TblAssesselement assesselement = tblAssEleCategory.getAssesselement();
                                    TblAssEleCategory assEleCategory = new TblAssEleCategory();
                                    assEleCategory.setAssesscategory(assesscategory);
                                    assEleCategory.setAssesselement(assesselement);
                                    aec.add(assEleCategory);
                                }
                            }
                        }
                        treeNode = tblAssesscategoryService.getTreeByNodeId(tblAssesscategory2.getAsscatid());
                    }
                }
                this.tblAssEleCategoryService.saveList(aec);
                return JsonBean.success(tmplId.toString());
            } else {
                return JsonBean.error("模板编号不能有重复");
            }
        }
        return JsonBean.error("请完善信息！");
    }

    @RequestMapping(value = "/gzdg/upadteTmpl", produces = "application/json; charset=utf-8")
    public @ResponseBody
    String updateTmplCheck(JsonBean tmplId) {
        TblAssesstemple assesstemple = this.tblAssesstempleService.findByid(tmplId);
        if (null != assesstemple) {
            if (assesstemple.getTblAssesses().size() > 0) {
                return JsonBean.error("模板正在使用不能修改");
            } else {
                return JsonBean.success(tmplId.toString());
            }
        }
        return JsonBean.error("服务异常");
    }

    /**
     * 新建评价模板下一步
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/gzdg/def_tmpl_index", produces = "application/json; charset=utf-8")
    public @ResponseBody
    String gzdg_def_tmpl_index(TblAssesstemple tblAssesstemple, JsonBean tmplId,
                               HttpServletRequest request) {
        logger.info("内控合规--内控工具--评价模板--新建--保存---start");
        TblOrganization attribute1 = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
        if (null == tmplId) {
            if (StringUtils.isNotBlank(tblAssesstemple.getReorg())
                    && StringUtils.isNotBlank(tblAssesstemple.getTempleNumber())) {
                String[] orgids = tblAssesstemple.getReorg().split(",");
                Set<TblOrganization> list = new HashSet<TblOrganization>();
                for (String string : orgids) {
                    TblOrganization organization = this.organizationService.findById(string);
                    if (null != organization) {
                        list.add(organization);
                    }
                }
                logger.info("通过模板编号，公司id查询评价模板");
                // 通过模板编号，公司id查询评价模板
                List<TblAssesstemple> assesstemples = this.tblAssesstempleService
                        .getTmplByNumber(tblAssesstemple.getTempleNumber(), attribute1.getOrgid());
                if (assesstemples.size() == 0) {// 如果该编号在该公司不存在,保存
                    logger.info("保存评价模板");
                    TblStaff tblStaff = (TblStaff) request.getSession().getAttribute("longUser");
                    tblAssesstemple.setStaff(tblStaff);
                    tblAssesstemple.setModifyDateTime(new Date());
                    tblAssesstemple.setTblComany(attribute1);
                    tblAssesstemple.setOrganizations(list);
                    tmplId = this.tblAssesstempleService.add(tblAssesstemple);
                    return JsonBean.success(tmplId.toString());
                } else {
                    return JsonBean.error("模板编号不能有重复");
                }
            }
        } else {
            if (StringUtils.isNotBlank(tblAssesstemple.getReorg())) {
                String[] orgids = tblAssesstemple.getReorg().split(",");
                Set<TblOrganization> list = new HashSet<TblOrganization>();
                for (String string : orgids) {
                    TblOrganization organization = this.organizationService.findById(string);
                    if (null != organization) {
                        list.add(organization);
                    }
                }
                // TblOrganization attribute1 = (TblOrganization)
                // request.getSession().getAttribute("hbOrgEntity");
                TblStaff tblStaff = (TblStaff) request.getSession().getAttribute("longUser");
                tblAssesstemple.setStaff(tblStaff);
                tblAssesstemple.setTblComany(attribute1);
                tblAssesstemple.setOrganizations(list);
                tblAssesstemple.setModifyDateTime(new Date());
                this.tblAssesstempleService.modify(tblAssesstemple);
                return JsonBean.success(tmplId.toString());
            }
        }
        // mav.setViewName("nbkz/gzdg/def_tmpl_index");
        // mav.addObject("tmplId", tmplId);
        // return mav;
        return JsonBean.error("保存失败");
    }

    @RequestMapping(value = "/gzdg/def_tmpl_index1")
    public String def_tmpl_index1(HttpServletRequest request, BigDecimal tmplId, String view) {
//        mav.setViewName("nbkz/gzdg/def_tmpl_index");
//        mav.addObject("tmplId", tmplId);
//        mav.addObject("view", view);
//        // 为页面查找区域显隐藏赋值
//        String choiceSearch = request.getParameter("choiceSearch");
//        if (choiceSearch == null || "".equals(choiceSearch)) {
//            choiceSearch = "hide";
//        }
//        mav.addObject("choiceSearch", choiceSearch);
//        return mav;
        String result = null;
        Map<String, Object> resultMap = new HashMap<>(0);
        resultMap.put("code", 1);
        resultMap.put("msg", "访问接口成功");
//        resultMap.put("data", pageInfo);
        JSONObject jsonObjectMV = new JSONObject(resultMap);
        result = jsonObjectMV.toString();
        return result;
    }

    /**
     * 根据模板id查看cat
     *
     * @param tmplId
     * @return
     */
    @RequestMapping(value = "/gzdg/det_temp_list")
    public String add_teml_list(HttpServletRequest request, BigDecimal tmplId, BigDecimal nodeId, String view) {
        List<TblAssesscategory> assesscategories = this.tblAssesscategoryService.findByTempleId(tmplId, nodeId);
//        mav.addObject("tmplId", tmplId);
//        mav.addObject("nodeId", nodeId);
//        mav.addObject("view", view);
//        // 为页面查找区域显隐藏赋值
//        String choiceSearch = request.getParameter("choiceSearch");
//        if (choiceSearch == null || "".equals(choiceSearch)) {
//            choiceSearch = "hide";
//        }
//        mav.addObject("choiceSearch", choiceSearch);
//        if (null == nodeId) {
//            mav.addObject("factor", 1);
//        } else {
//            mav.addObject("factor", assesscategories.size());
//        }
//        if (assesscategories.size() != 0 || null == nodeId) {
//            mav.setViewName("nbkz/gzdg/1");
//            mav.addObject("assesscategories", assesscategories);
//            return mav;
//        } else {
//            List<TblAssEleCategory> assesscategory = this.tblAssEleCategoryService.getAssesscategoryByNodeId(nodeId);
//            if (assesscategory.size() == 0) {
//                mav.setViewName("nbkz/gzdg/1");
//                mav.addObject("assesscategories", null);
//                return mav;
//            } else {
//                mav.setViewName("nbkz/gzdg/def_basiccat_list");
//                mav.addObject("assesscategory", assesscategory);
//                return mav;
//            }
//        }
        String result = null;
        Map<String, Object> resultMap = new HashMap<>(0);
        resultMap.put("code", 1);
        resultMap.put("msg", "访问接口成功");
//        resultMap.put("data", pageInfo);
        JSONObject jsonObjectMV = new JSONObject(resultMap);
        result = jsonObjectMV.toString();
        return result;
    }

    private ServletConfig config;

    @Override
    public void setServletConfig(ServletConfig servletConfig) {
        this.config = servletConfig;
    }
}
