package com.huabo.fxgl.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Attachment;
import com.huabo.fxgl.entity.Find;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Worksheet;
import com.huabo.fxgl.service.IAttachmentService;
import com.huabo.fxgl.service.IAutonoInfoService;
import com.huabo.fxgl.service.IBugService;
import com.huabo.fxgl.service.IOrganizationService;
import com.huabo.fxgl.service.IStaffService;
import com.huabo.fxgl.service.IWorksheetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  风险管控 - 相关问题汇总 - 底稿汇总
 * </p>
 * @version 1.0.1
 * @author liHongXu
 * @since 2022-08-12
 */
@RestController
@RequestMapping(value = "/nbkz", method = {RequestMethod.GET, RequestMethod.POST})
@Tag(name="风险管控 - 相关问题汇总 - 底稿汇总",description="风险管控 - 相关问题汇总 - 底稿汇总")
@Slf4j
public class WorksheetController {

    @Autowired
    private IOrganizationService organizationService;

    @Autowired
    private IWorksheetService worksheetService;

    @Autowired
    private IAttachmentService attachmentService;

    @Autowired
    private IStaffService staffService;

    @Autowired
    private IAutonoInfoService autonoInfoService;

    @Autowired
    private IBugService bugService;
    
    @Resource
    private UserProvider userProvider;

    /**
     * <p>
     *  风险管控 - 相关问题汇总 - 底稿汇总 - 底稿搜索
     * </p>
     * @version 1.0.1
     * @author liHongXu
     * @since 2022-08-12
     */
    @OperationLog(
            success = "底稿搜索/nbkz/nkgj/project_standard_list_all查询成功",
            busType = "风险管控",
            fail = "底稿搜索/nbkz/nkgj/project_standard_list_all查询失败",
            operationType = OperationType.SELECT,
            subType = "相关问题汇总"
    )
    @RequestMapping(value = "/nkgj/project_standard_list_all")
    @Operation(summary = "底稿搜索/nbkz/nkgj/project_standard_list_all")
    public JsonBean project_standard_list_all(@Parameter(name = "pageNo", description = "pageNo") @RequestParam(defaultValue = "1") Integer pageNo,
                                              @Parameter(name = "pageSize", description = "pageSize") @RequestParam(defaultValue = "20") Integer pageSize,
                                              @Parameter(name = "find", description = "find") Find find,
                                              @Parameter(name = "orgid", description = "orgid") String orgid,
                                              @Parameter(name = "orgtype", description = "orgtype") @RequestParam(required = false) String orgtype,
                                              @Parameter(name = "type", description = "type") String type,
                                              @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(required = false) String choiceSearch,
                                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        IPage page = new Page(pageNo, pageSize);//分页设置
        IPage pageBean;
        JsonBean jsonBean = new JsonBean();
        Map result = new HashMap();

        if (orgid == null || "".equals(orgid)){
            orgid = selectOrg.getOrgid().toString();
            orgtype = selectOrg.getOrgtype().toString();
        }
        Organization org = organizationService.getById(orgid);
        Boolean isSelect = worksheetService.isSJByOrgId(staffOrg.getOrgid().toString());
//        mav = new ModelAndView("nbkz/nkgj/project_standard_list_all");
        if (isSelect) {
            if (staffOrg.getOrgid().toString().equals(orgid) && orgtype != null && orgtype != "") {
                orgid = selectOrg.getOrgid().toString();
                orgtype = selectOrg.getOrgtype().toString();
            }
            if (orgtype == null || orgtype.equals("")) {
                orgtype = org.getOrgtype() == null ? "0" : org.getOrgtype().toString();
            }
            pageBean = worksheetService.findAllTblWorksheetByorgid(orgid, orgtype, page, type, find);
            result.put("pageBean", pageBean);
        } else {
            if (staffOrg.getOrgid().toString().equals(orgid)) {
                orgid = staffOrg.getOrgid().toString();
                orgtype = staffOrg.getOrgtype().toString();
                pageBean = worksheetService.findAllTblWorksheetByorgid(orgid, orgtype, page, type, find);
                result.put("pageBean", pageBean);
            } else {
                pageBean = new Page();
                result.put("pageBean", pageBean);
            }
        }
//        request.getSession().setAttribute("dgglList", pageBean.getRecordList());
//        ModelAndView mv = new ModelAndView("nbkz/nkgj/project_standard_list_all");
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        result.put("choiceSearch", choiceSearch);
        result.put("type", type);
        result.put("orgid", orgid);
        result.put("pageBean", pageBean);
        jsonBean.setCode(200);
        jsonBean.setData(result);
        return jsonBean;
    }

    /**
     * <p>
     *  风险管控 - 相关问题汇总 - 底稿汇总 - 底稿查看
     * </p>
     * @version 1.0.1
     * @author liHongXu
     * @since 2022-08-12
     */
    @OperationLog(
            success = "底稿查看/nbkz/nkgj/detial_all查询成功",
            busType = "风险管控",
            fail = "底稿查看/nbkz/nkgj/detial_all查询失败",
            operationType = OperationType.SELECT,
            subType = "相关问题汇总"
    )
    @RequestMapping(value = "/nkgj/detial_all")
    @Operation(summary = "底稿查看/nbkz/nkgj/detial_all")
    public JsonBean detial_all(@Parameter(name = "worksheetid", description = "worksheetid") @RequestParam(required = false) String worksheetid,
                               @Parameter(name = "czurl", description = "czurl") @RequestParam(required = false) String czurl,
                               @Parameter(name = "signId", description = "signId") @RequestParam(required = false) String signId,
                               @Parameter(name = "orgid", description = "orgid") @RequestParam(required = false) String orgid,
                               @Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
//        logger.info("风险管理---工作底稿---底稿管理---跳往新建页");
        JsonBean jsonBean = new JsonBean();
        Map result = new HashMap();
//        ModelAndView mv = new ModelAndView();
        if (StringUtils.isNotEmpty(worksheetid)) {
            log.info("------------------------" + worksheetid);
            Worksheet ws = worksheetService.getById(worksheetid);
            List<Attachment> fj = attachmentService.findAllByTblWorksheet(worksheetid);
            result.put("ws", ws);
            result.put("fj", fj);
        }
        result.put("czurl", czurl);
        result.put("signId", signId);
        result.put("orgid", orgid);
        result.put("type", type);
        jsonBean.setCode(200);
        jsonBean.setData(result);
        return jsonBean;
    }

    /**
     * <p>
     *  风险管控 - 相关问题汇总 - 底稿汇总 - 发起整改
     * </p>
     * @version 1.0.1
     * @author liHongXu
     * @since 2022-08-12
     */
    @OperationLog(
            success = "发起整改/nbkz/wthz/dg_zg成功",
            busType = "风险管控",
            fail = "发起整改/nbkz/wthz/dg_zg失败",
            operationType = OperationType.UPDATE,
            subType = "相关问题汇总"
    )
    @RequestMapping(value = "/wthz/dg_zg", produces = "application/json; charset=utf-8")
    @Operation(summary = "发起整改/nbkz/wthz/dg_zg")
    public String dg_zg(@Parameter(name = "dgid", description = "dgid") @RequestParam(required = false) String dgid,
                        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
//        logger.info("底稿汇总-发起整改");
        if (dgid != null && dgid.length() > 0) {
            Worksheet worksheet = worksheetService.getById(dgid);
            if (worksheet != null && worksheet.getRectification() != null && Integer.parseInt(worksheet.getRectification().toString())==  1) {
                return JsonBean.error("已发起整改");
            } else {
                worksheet.setRectification(new BigDecimal(Worksheet.YES_ZG));
                worksheetService.update(worksheet);
                return JsonBean.success();
            }
        } else {
            return JsonBean.error("请选择");
        }
    }

    /**
     * <p>
     *  风险管理---工作底稿---底稿管理---跳往新建页
     * </p>
     * @version 1.0.1
     * @author liHongXu
     * @since 2022-08-16
     */
    @OperationLog(
            success = "风险管理---工作底稿---底稿管理---跳往新建页/nbkz/nkgj/add查询成功",
            busType = "风险管控",
            fail = "风险管理---工作底稿---底稿管理---跳往新建页/nbkz/nkgj/add查询失败",
            operationType = OperationType.SELECT,
            subType = "相关问题汇总"
    )
    @RequestMapping(value = "/nkgj/add")
    @Operation(summary = "风险管理---工作底稿---底稿管理---跳往新建页/nbkz/nkgj/add")
    public JsonBean nkgj_project_standard_list_add(@Parameter(name = "worksheetid", description = "worksheetid") @RequestParam(required = false) String worksheetid,
                                                   @Parameter(name = "czurl", description = "czurl")@RequestParam(required = false) String czurl,
                                                   @Parameter(name = "signId", description = "signId") @RequestParam(required = false) String signId,
                                                   @Parameter(name = "backUrl", description = "backUrl") @RequestParam(required = false) String backUrl,
                                                   @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(required = false) String choiceSearch,
                                                   @Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
                                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
//        logger.info("风险管理---工作底稿---底稿管理---跳往新建页");
        JsonBean jsonBean = new JsonBean();
        Map result = new HashMap();
        if (StringUtils.isNotEmpty(worksheetid)) {

            Worksheet ws = worksheetService.getById(worksheetid);
            //120169
            List<Attachment> fj = attachmentService.findAllByTblWorksheet(worksheetid);
            result.put("ws", ws);
            result.put("fj", fj);
        }
        result.put("czurl", czurl);
        result.put("signId", signId);
        result.put("type",type);
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        result.put("choiceSearch", choiceSearch);
        try {
            if (backUrl != null && backUrl.trim().length() > 0) {
                backUrl = URLDecoder.decode(backUrl, "UTF-8");
            }
            result.put("backUrl", backUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        jsonBean.setCode(200);
        jsonBean.setData(result);
        return jsonBean;
    }

    /**
     * <p>
     *  风险管理---工作底稿---底稿管理---新建---验证底稿编号是否存在
     * </p>
     * @version 1.0.1
     * @author liHongXu
     * @since 2022-08-16
     */
    @OperationLog(
            success = "验证底稿编号是否存在/nbkz/nkgj/gzdg_validate_number查询成功",
            busType = "风险管控",
            fail = "验证底稿编号是否存在/nbkz/nkgj/gzdg_validate_number查询失败",
            operationType = OperationType.SELECT,
            subType = "相关问题汇总"
    )
    @RequestMapping(value = "/nkgj/gzdg_validate_number")
    @Operation(summary = "验证底稿编号是否存在/nbkz/nkgj/gzdg_validate_number")
    public @ResponseBody String gzdg_validate_number(@Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
                                                     @Parameter(name = "num", description = "num") @RequestParam(required = false) String num,
                                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        if (type != null){
            return worksheetService.findByTblWorkSheetnumber(num, type, selectOrg.getOrgid());
        }else {
            return worksheetService.findByTblWorkSheetnumber(num, null, selectOrg.getOrgid());
        }
    }


    /**
     * <p>
     *  风险管理---工作底稿---底稿管理---新建页---删除附件
     * </p>
     * @version 1.0.1
     * @author liHongXu
     * @since 2022-08-16
     */
    @OperationLog(
            success = "删除附件/nbkz/nkgj/dggl_fj_del成功",
            busType = "风险管控",
            fail = "删除附件/nbkz/nkgj/dggl_fj_del失败",
            operationType = OperationType.DELETE,
            subType = "相关问题汇总"
    )
    @RequestMapping(value = "/nkgj/dggl_fj_del")
    @Operation(summary = "删除附件/nbkz/nkgj/dggl_fj_del")
    public JsonBean dggl_fj_del(@Parameter(name = "worksheetid", description = "worksheetid")@RequestParam(required = false) String worksheetid,
                                    @Parameter(name = "attid", description = "attid") @RequestParam(required = false) String attid,
                                    @Parameter(name = "czurl", description = "czurl") @RequestParam(required = false) String czurl,
                                    @Parameter(name = "signId", description = "signId")@RequestParam(required = false) String signId,
                                    @Parameter(name = "backUrl", description = "backUrl") @RequestParam(required = false) String backUrl,
                                    @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam(required = false) String choiceSearch,
                                    @Parameter(name = "type", description = "type")@RequestParam(required = false) String type,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
//        logger.info("风险管理---工作底稿---底稿管理---新建页---删除附件");
//        String attid = request.getParameter("attid");
        Attachment attachment = new Attachment();
        if (StringUtils.isNotEmpty(attid)) {
            //113232测试数据
            attachment = attachmentService.getById(attid);
        }
        if (StringUtils.isNotEmpty(worksheetid)) {
            //521634 测试数据
            Worksheet ws = worksheetService.getById(worksheetid);
            ws.getAttachments().remove(attachment);
            worksheetService.saveOrUpdate(ws);
        } else {
            attachmentService.removeById(attachment.getAttid());
        }
        return  new JsonBean(200,"success",nkgj_project_standard_list_add(worksheetid,czurl,signId,backUrl,choiceSearch,type,token));
    }

    /**
     * <p>
     *    风险管控 - 相关问题汇总 - 底稿汇总 - 整体页面
     * </p>
     * @version 1.0.1
     * @author liHongXu
     * @since 2022-08-16
     */
    @OperationLog(
            success = "整体页面/nbkz/nkgj/dg_index查询成功",
            busType = "风险管控",
            fail = "整体页面/nbkz/nkgj/dg_index查询失败",
            operationType = OperationType.SELECT,
            subType = "相关问题汇总"
    )
    @RequestMapping(value = "/nkgj/dg_index")
    @Operation(summary = "整体页面/nbkz/nkgj/dg_index")
    public JsonBean dg_index(@Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception{
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        JsonBean jsonBean = new JsonBean();
        Map result = new HashMap();
        result.put("type", type);
        result.put("orgid",staffOrg.getOrgid());
        result.put("orgtype",staffOrg.getOrgtype());
        jsonBean.setCode(200);
        jsonBean.setData(result);
        return jsonBean;
    }

    /**
     * <p>
     *   风险管控 - 相关问题汇总 - 底稿汇总 - 左侧树
     * </p>
     *
     *@author LiHongXu
     *@version 1.0.1
     *@since 2022-08-16
     */
    @OperationLog(
            success = "底稿汇总 - 左侧树/nbkz/nkgj/dg_userleft查询成功",
            busType = "风险管控",
            fail = "底稿汇总 - 左侧树/nbkz/nkgj/dg_userleft查询失败",
            operationType = OperationType.SELECT,
            subType = "相关问题汇总"
    )
    @RequestMapping(value = "/nkgj/dg_userleft")
    @Operation(summary = "底稿汇总 - 左侧树/nbkz/nkgj/dg_userleft")
    public JsonBean dg_userleft(@Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        JsonBean jsonBean = new JsonBean();
        Map result = new HashMap();
        result.put("orgid",selectOrg.getOrgid());
        result.put("treeName",selectOrg.getOrgname());
        result.put("type",type);
        jsonBean.setCode(200);
        jsonBean.setData(result);
        return jsonBean;
    }

    /**
     * <p>
     *   风险管控 - 风险识别 - 风险创建 - 业务编号默认值
     * </p>
     *
     *@author LiHongXu
     *@version 1.0.1
     *@since 2022-08-17
     */
    @OperationLog(
            success = "业务编号默认值/nbkz/code/findAutoNumberByChoice查询成功",
            busType = "风险管控",
            fail = "业务编号默认值/nbkz/code/findAutoNumberByChoice查询失败",
            operationType = OperationType.SELECT,
            subType = "相关问题汇总"
    )
    @RequestMapping(value = "/code/findAutoNumberByChoice")
    @Operation(summary = "业务编号默认值/nbkz/code/findAutoNumberByChoice")
    public String findAutoNumberByChoice(@Parameter(name = "tblName", description = "tblName") @RequestParam(required = false) String tblName,
                                         @Parameter(name = "column", description = "column") @RequestParam(required = false) String column,
                                         @Parameter(name = "orgCol", description = "orgCol") @RequestParam(required = false) String orgCol,
                                         @Parameter(name = "noId", description = "noId") @RequestParam(required = false) Integer noId,
                                         @Parameter(name = "chChoiceCol", description = "chChoiceCol") @RequestParam(required = false) String chChoiceCol,
                                         @Parameter(name = "choiceVal", description = "choiceVal") @RequestParam(required = false) String choiceVal,
                                         @Parameter(name = "bjf", description = "bjf") @RequestParam(required = false) String bjf,
                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        String flowNextId = null;
        try {
            flowNextId = autonoInfoService.findFlowNextId(tblName, column, orgCol, selectOrg.getOrgid(), noId, chChoiceCol, choiceVal, bjf);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        response.reset();
        return flowNextId;
    }

    /**
     * <p>
     *    风险管控 - 风险识别 - 风险创建 - 风险编号默认值
     * </p>
     *
     *@author LiHongXu
     *@version 1.0.1
     *@since 2022-08-17
     */
    @OperationLog(
            success = "风险编号默认值/nbkz/code/findNumberLevelNexidByParent查询成功",
            busType = "风险管控",
            fail = "风险编号默认值/nbkz/code/findNumberLevelNexidByParent查询失败",
            operationType = OperationType.SELECT,
            subType = "相关问题汇总"
    )
    @GetMapping(value = "/code/findNumberLevelNexidByParent")
    @Operation(summary = "风险编号默认值/nbkz/code/findNumberLevelNexidByParent")
    public String findNumberLevelNexidByParent(HttpServletRequest request,HttpServletResponse response,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "noId", description = "编号ID 风险创建传入 253", required = true) @RequestParam(value = "noId", required = true)Integer noId,
			@Parameter(name = "parentNumberCol", description = "父表的编号列  风险创建编号传入  RISKCATNUMBER", required = true) @RequestParam(value = "parentNumberCol", required = true)String parentNumberCol ,
			@Parameter(name = "parentTblName", description = "父表名 风险创建传入 TBL_RISKCATEGORY", required = true) @RequestParam(value = "parentTblName", required = true)String parentTblName ,
			@Parameter(name = "parentIdCol", description = "父表主键列表名  风险创建传入 RISKCATID", required = true) @RequestParam(value = "parentIdCol", required = true)String parentIdCol,
			@Parameter(name = "parentId", description = "父级节点Id  风险创建传入 riskcatid", required = true) @RequestParam(value = "parentId", required = true)String parentId,
			@Parameter(name = "chilNumberCol", description = "字表需要查询编号列  风险创建传入RISKNUMBER", required = true) @RequestParam(value = "chilNumberCol", required = true)String chilNumberCol ,
			@Parameter(name = "chilTblName", description = "字表表名 风险创建传入TBL_RISK", required = true) @RequestParam(value = "chilTblName", required = true)String chilTblName ,
			@Parameter(name = "chilOrgCol", description = "字表公司列名  风险创建传入 UNIT", required = true) @RequestParam(value = "chilOrgCol", required = true)String chilOrgCol,
			@Parameter(name = "choiceCol", description = "获取编号 额外需要的查询条件列", required = false) @RequestParam(value = "choiceCol", required = false)String choiceCol,
			@Parameter(name = "choiceVal", description = "获取编号 额外需要的查询条件值", required = false) @RequestParam(value = "choiceVal", required = false)String choiceVal) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        String flowNextId = null;
        try {
            Map<String, String> choiceMap = new HashMap<String, String>(0);
            if (choiceVal != null) {
                choiceMap.put(choiceCol, choiceVal);
            }
            flowNextId = autonoInfoService.findNumberLevelNexidByParent(noId, parentNumberCol, parentTblName,
                    parentIdCol, parentId, chilNumberCol, chilTblName, chilOrgCol, selectOrg.getOrgid(), choiceMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        response.reset();
        return flowNextId;
    }

    /**
     * <p>
     *      风险管控 - 风险识别 - 风险创建 - 风险类型编号默认值
     * </p>
     *
     *@author LiHongXu
     *@version 1.0.1
     *@since 2022-08-17
     */
    @OperationLog(
            success = "风险类型编号默认值/nbkz/code/findRootNumberByParentId查询成功",
            busType = "风险管控",
            fail = "风险类型编号默认值/nbkz/code/findRootNumberByParentId查询失败",
            operationType = OperationType.SELECT,
            subType = "相关问题汇总"
    )
    @RequestMapping(value = "/code/findRootNumberByParentId")
    @Operation(summary = "风险类型编号默认值/nbkz/code/findRootNumberByParentId")
    public String findRootNumberByParentId(@Parameter(name = "chilNumberCol", description = "chilNumberCol") @RequestParam(required = false) String chilNumberCol,
                                           @Parameter(name = "chilTblName", description = "chilTblName") @RequestParam(required = false) String chilTblName,
                                           @Parameter(name = "chilParentCol", description = "chilParentCol") @RequestParam(required = false) String chilParentCol,
                                           @Parameter(name = "parentIdCol", description = "parentIdCol") @RequestParam(required = false) String parentIdCol,
                                           @Parameter(name = "parentTblName", description = "parentTblName") @RequestParam(required = false) String parentTblName,
                                           @Parameter(name = "parnetOrgCol", description = "parnetOrgCol") @RequestParam(required = false) String parnetOrgCol,
                                           @Parameter(name = "noId", description = "noId") @RequestParam(required = false) Integer noId,
                                           @Parameter(name = "middleTblname", description = "middleTblname") @RequestParam(required = false) String middleTblname,
                                           @Parameter(name = "middleChilCol", description = "middleChilCol") @RequestParam(required = false) String middleChilCol,
                                           @Parameter(name = "middleParentCol", description = "middleParentCol") @RequestParam(required = false) String middleParentCol,
                                           @Parameter(name = "fltype", description = "fltype") @RequestParam(required = false) String fltype,
                                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        String flowNextId = null;
        try {
            flowNextId = autonoInfoService.findRootNumberByParentId(chilNumberCol, chilTblName, chilParentCol,
                    parentIdCol, parentTblName, parnetOrgCol, selectOrg.getOrgid(), noId, middleTblname, middleChilCol,
                    middleParentCol, fltype);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        response.reset();
        return flowNextId;
    }
}
