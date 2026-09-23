package com.huabo.monitor.controller;

import com.hbfk.util.JsonBean;
import com.huabo.monitor.oracle.entity.TblTestTemplate;
import com.huabo.monitor.oracle.entity.TblTestplan;
import com.huabo.monitor.service.TblTestElementService;
import com.huabo.monitor.service.TblTestPlanService;
import com.huabo.monitor.service.TblTestTempTypeService;
import com.huabo.monitor.service.TblTestTemplateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;


@Controller
@RequestMapping(value = "/nbkz")
public class NKcsController extends BaseController {

    @Resource
    public TblTestTempTypeService tblTestTempTypeService;

    @Resource
    public TblTestTemplateService tblTestTemplateService;

    @Resource
    public TblTestElementService tblTestElementService;

    @Resource
    public TblTestPlanService tblTestPlanService;

    /**
     * 内控测试模板列表
     *
     * @return
     */
    @GetMapping("/nbkz/csmb/def_tmpl_list")
    @ApiOperation("测试模板")
    public JsonBean csfa_def_tmpl_list(
            @ApiParam(name = "token", value = "登录用户token", required = true) @RequestHeader("token") String token,
            @ApiParam(name = "pageNumber", value = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @ApiParam(name = "pageSize", value = "分页当前行数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "1") Integer pageSize,
            @ApiParam(name = "templeNumber", value = "测试模板编号", required = false) @RequestParam(value = "templeNumber", required = false) Integer templeNumber,
            @ApiParam(name = "templename", value = "测试模板编号", required = false) @RequestParam(value = "templename", required = false) String templename) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblTestTemplateService.selectList(token, pageNumber, pageSize, templeNumber, templename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    /**
     * 新建模板
     *
     * @return
     */
    @RequestMapping(value = "/csmb/def_tmpl_add")
    public JsonBean def_tmpl_add(@RequestBody TblTestTemplate template) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblTestTemplateService.add(template);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;

    }

//    @RequestMapping(value = "/csmb/upadteTmpl")
//    public @ResponseBody
//    String def_upadteTmpl(BigDecimal testtemid) {
//        if (null != testtemid) {
//            boolean isOk = this.tblTestPlanService.checkPlanByTempl(testtemid);
//            if (isOk) {
//                TblTestTemplate template = this.tblTestTemplateService.get(testtemid);
//                if (null != template) {
//                    return JsonBean.success(String.valueOf(testtemid));
//                }
//                return JsonBean.error("操作失败！");
//            }
//            return JsonBean.error("模板已经使用不能修改！");
//        }
//        return JsonBean.error("操作失败！");
//    }


    /**
     * 模板修改
     *
     * @param testtemid
     * @return
     */
    @RequestMapping(value = "/csmb/def_tmpl_update")
    public JsonBean def_tmpl_upate(@ApiParam(name = "testtemid", value = "测试模板id", required = false) @RequestParam(value = "testtemid", required = false) BigDecimal testtemid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblTestTemplateService.updateById(testtemid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 模板删除
     *
     * @param templeNumber
     * @return
     */
    @RequestMapping(value = "/csmb/tempdelete")
    public JsonBean tempdelete(@ApiParam(name = "templeNumber", value = "测试模板编号", required = false) @RequestParam(value = "templeNumber", required = false) String templeNumber) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblTestTemplateService.deleteById(templeNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 保存 基本信息
     *
     * @param tblTestTemplate
     * @return
     */
    @RequestMapping(value = "/csmb/def_tmpl_save")
    public JsonBean def_tmpl_save(@RequestBody TblTestTemplate tblTestTemplate) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblTestTemplateService.save(tblTestTemplate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
//    @RequestMapping(value = "/csmb/def_tmpl_save")
//    public @ResponseBody String def_tmpl_save(TblTestTemplate tblTestTemplate) {
//        TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
//        boolean isOk = this.tblTestTemplateService.checkCode(tblTestTemplate.getTempleNumber(),attribute.getOrgid().toString());
//        if (isOk) {
//            tblTestTemplate.setCreateDateTime(new Date());
//            tblTestTemplate.setStaff(getTblStaff());
//            tblTestTemplate.setSource("自建");
//            tblTestTemplate.setTblComany(getCurrentOrg().getOrgid().toString());
//            Serializable serializable = this.tblTestTemplateService.save(tblTestTemplate);
//            if (null != serializable) {
//                return JsonBean.success(serializable.toString());
//            }
//        } else {
//            return JsonBean.error("编号已存在！");
//        }
//        return JsonBean.error("保存失败！");
//    }
//
//    public TblStaff getTblStaff(){
//        TblStaff tblStaff = (TblStaff) request.getSession().getAttribute("longUser");
//        return tblStaff;
//    }

//    @RequestMapping(value = "/csmb/def_tmpl_modify")
//    public @ResponseBody String def_tmpl_modify(TblTestTemplate tblTestTemplate) {
//        TblTestTemplate template = this.tblTestTemplateService.get(tblTestTemplate.getTesttemid());
//        if (null != template) {
//            template.setTemplename(tblTestTemplate.getTemplename());
//            template.setTempleDesc(tblTestTemplate.getTempleDesc());
//            template.setTempleNumber(tblTestTemplate.getTempleNumber());
//            this.tblTestTemplateService.update(template);
//            return JsonBean.success();
//        }
//        return JsonBean.error("保存失败！");
//    }

    /**
     * 模板详细内容
     *
     * @param tmplId
     * @param view
     * @return
     */
//    @RequestMapping(value = "/csmb/def_tmpl_index")
//    public ModelAndView def_tmpl_index1(BigDecimal tmplId, String view) {
//        ModelAndView mv = new ModelAndView();
//        mv.setViewName("nbkz/csmb/cs_tmpl_index");
//        mv.addObject("tmplId", tmplId);
//        mv.addObject("view", view);
//        //为页面查找区域显隐藏赋值
//        String choiceSearch = request.getParameter("choiceSearch");
//        if(choiceSearch == null || "".equals(choiceSearch)) {
//            choiceSearch = "hide";
//        }
//        mv.addObject("choiceSearch", choiceSearch );
//        return mv;
//    }

    /**
     * 测试模板 头信息
     *
     * @param tmplId
     * @param view
     * @return
     */
//    @RequestMapping(value = "/csmb/def_head")
//    public ModelAndView gzdg_def_head(BigDecimal tmplId, String view) {
////        mav.setViewName("nbkz/csmb/cs_head");
////        mav.addObject("tmplId", tmplId);
////        mav.addObject("view", view);
////        return mav;
//        return null;
//    }


//    @RequestMapping(value = "/csjg/def_list_bxy")
//    public JsonBean def_list_bxy(
//            @ApiParam(name="token",value="登录用户token",required=true) @RequestHeader("token")String token,
//            @ApiParam(name="pageNumber",value="分页当前页数",required=false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
//            @ApiParam(name = "node",value = "节点",required = false)@RequestParam(value = "node",required = false,defaultValue = "") String node,
//            @ApiParam(name = "templId",value = "临时模板ID",required = false)@RequestParam(value = "templId",required = false) String templId,
//            @ApiParam(name = "planid",value = "计划id",required = false)@RequestParam(value = "planid",required = false) String planid) {
//        JsonBean jsonBean = null;
//        if (templId != null && node != null) {
//            jsonBean=tblTestElementService.fingByTreeCSJGB(node,pageNumber,planid,templId,planid);
//        } else {
//            jsonBean = new JsonBean();
//        }
//
//        return jsonBean;
//    }
    /**
     * 测试方案-计划查找
     * @return
     */
    @RequestMapping(value = "/nkcs/plan/ctrltest_plan_list")
    public JsonBean nkcs_ctrltest_plan_list(@ApiParam(name="plannumber",value = "计划编号",required = false)@RequestParam(value = "plannumber",required = false)String plannumber,
                                            @ApiParam(name="planname",value = "计划名称",required = false)@RequestParam(value = "planname",required = false)String planname,
                                            @ApiParam(name="planstatus",value = "计划状态",required = false)@RequestParam(value = "planstatus",required = false)String planstatus,
                                            @ApiParam(name="starttime_min",value = "最小开始时间",required = false)@RequestParam(value = "starttime_min",required = false) Date starttime_min,
                                            @ApiParam(name="starttime_max",value = "最大开始时间",required = false)@RequestParam(value = "starttime_max",required = false)Date starttime_max) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblTestPlanService.selectList(plannumber,planname,planstatus,starttime_min,starttime_max);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    /**
     * 计划保存
     * @param tblTestplan
     * @return
     */
    @RequestMapping(value = "/nkcs/plan/saves")
    public JsonBean nkcs_csjhSave(@RequestBody TblTestplan tblTestplan){
        JsonBean jsonBean = null;
        try {
            jsonBean = tblTestPlanService.save(tblTestplan);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 新增计划
     * @param tblTestplan
     * @return
     */
    @RequestMapping(value = "/nkcs/plan/add")
    public JsonBean nkcs_ctrltest_plan_add(@RequestBody TblTestplan tblTestplan) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblTestPlanService.add(tblTestplan);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 计划删除
     * @param plannumber
     * @return
     */
    @RequestMapping(value = "/nkcs/plan/delete")
    public JsonBean nkcs_ctrltest_plan_delete(@ApiParam(name="plannumber",value = "计划编号",required = false)@RequestParam(value = "plannumber",required = false)String plannumber) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblTestPlanService.deleteById(plannumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

}
