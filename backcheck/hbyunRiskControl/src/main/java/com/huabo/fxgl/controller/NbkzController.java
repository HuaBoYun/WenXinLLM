package com.huabo.fxgl.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Attachment;
import com.huabo.fxgl.entity.Find;
import com.huabo.fxgl.entity.NbkzRisk;
import com.huabo.fxgl.entity.NbkzRisktolerability;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Staff;
import com.huabo.fxgl.entity.Worksheet;
import com.huabo.fxgl.mapper.AttachmentMapper;
import com.huabo.fxgl.mapper.NbkzRisktolerabilityMapper;
import com.huabo.fxgl.service.IAttachmentService;
import com.huabo.fxgl.service.IBugInnerruleService;
import com.huabo.fxgl.service.IBugOuterruleService;
import com.huabo.fxgl.service.IBugService;
import com.huabo.fxgl.service.INbkzRisktolerabilityService;
import com.huabo.fxgl.service.IOrganizationService;
import com.huabo.fxgl.service.IReportService;
import com.huabo.fxgl.service.IRiskAttService;
import com.huabo.fxgl.service.IWorksheetService;
import com.huabo.fxgl.service.impl.NbkzRiskServiceImpl;
import com.huabo.fxgl.service.impl.StaffServiceImpl;
import com.huabo.fxgl.util.ExcelUtil;
import com.huabo.fxgl.util.HttpClient;
import com.huabo.fxgl.util.Tree;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zuoshun
 * @version V1.0
 * @Package com.huabo.fxgl.controller
 * @date 2022/8/15 9:23
 */

@Slf4j
@RestController
@RequestMapping(value = "/nbkz", method = {RequestMethod.GET, RequestMethod.POST})
@Tag(name="风险管控 {相关问题汇总、风险发现、风险报告}",description="风险管控 {相关问题汇总、风险发现、风险报告}")
public class NbkzController {
    @Autowired
    private NbkzRiskServiceImpl nbkzRiskService;
    @Autowired
    private NbkzRisktolerabilityMapper nbkzRisktolerabilityMapper;
    @Autowired
    private IOrganizationService organizationService;
    @Autowired
    private AttachmentMapper attachmentMapper;
    @Autowired
    private IAttachmentService attachmentService;
    @Autowired
    private StaffServiceImpl staffService;
    @Autowired
    private INbkzRisktolerabilityService nbkzRisktolerabilityService;
    @Autowired
    private IRiskAttService riskAttService;
    @Autowired
    private IBugInnerruleService bugInnerruleService;
    @Autowired
    private IBugOuterruleService bugOuterruleService;
    @Autowired
    private IWorksheetService worksheetService;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 11
     *
     * @param orgid
     * @param type
     * @param choiceSearch
     * @param token
     * @return
     * @throws Exception
     * @author yangzeguo
     * @version v1.0.1
     * @Description 风险管控-相关问题汇总-风险发现 —> 新建
     * @Date 2022/8/7
     */
    @OperationLog(
            success = "风险发现 —新建处理成功",
            busType = "风险管控",
            fail = "风险发现 —新建处理失败",
            operationType = OperationType.ADD,
            subType = "相关问题汇总"
    )
    @Operation(summary = "风险管控-相关问题汇总-风险发现 —新建/nbkz/wthz/risk_add")
    @RequestMapping(value = "/wthz/risk_add")
    public JsonBean risk_add(
            @Parameter(name = "orgid", description = "orgid") @RequestParam(required = false) String orgid,
            @Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
            @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(required = false) String choiceSearch,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        Staff staff = staffService.getById(staffUtil.getStaffid());
        Map result = new HashMap();
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        result.put("choiceSearch", choiceSearch);
        result.put("orgid", orgid);
        result.put("type", type);
        result.put("user", staff);
        return new JsonBean(200, "success", result);
    }

    /**
     * 11
     *
     * @param riskid
     * @param type
     * @param backUrl
     * @param choiceSearch
     * @param orgid
     * @return
     * @author yangzeguo
     * @version v1.0.1
     * @Description 风险管控-相关问题汇总-风险发现 —> 新建 and 保存 对应页面里的部分信息
     * @Date 2022/8/7
     */
    @OperationLog(
            success = "风险发现 —> 新建 and 保存 对应页面里的部分信息处理成功",
            busType = "风险管控",
            fail = "风险发现 —> 新建 and 保存 对应页面里的部分信息处理失败",
            operationType = OperationType.ADD,
            subType = "相关问题汇总"
    )
    @Operation(summary = "风险管控-相关问题汇总-风险发现 —> 新建 and 保存 对应页面里的部分信息/nbkz/wthz/risk_modify")
    @RequestMapping(value = "/wthz/risk_modify")
    public JsonBean risk_modify(
            @Parameter(name = "riskid", description = "riskid") @RequestParam String riskid,
            @Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
            @Parameter(name = "backUrl", description = "backUrl") @RequestParam(required = false) String backUrl,
            @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(required = false) String choiceSearch,
            @Parameter(name = "orgid", description = "orgid") @RequestParam(required = false) String orgid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        String name = "";
        Map result = new HashMap();
        if (riskid != null && riskid.trim().length() > 0) {
            //根据riskid找到risk对象  riskid 134855测试数据
            NbkzRisk risk = nbkzRiskService.get(riskid);
            //根据riskid外键查到集合
            List<NbkzRisktolerability> list = nbkzRisktolerabilityMapper.findRiskTolerByRiskid(riskid);
            if (risk != null && risk.getSysorgid() != null) {
                //根据获取到的risk对象那找到reorg(相关部门)的id并分割
                String[] str = risk.getSysorgid().split(",");
                for (String string : str) {
                    //根据找到的reorg 的id 找到Tbl_Organization表中的数据
                    Organization org = organizationService.getById(string);
                    //获取相关部门的中文名字
                    name += org.getOrgname() + ",";
                }
            }
            result.put("list", list);
            //type类型
            if (risk != null && risk.getStype() != null) {
                type = risk.getStype();
            }
            //隐藏
            if (choiceSearch == null || "".equals(choiceSearch)) {
                choiceSearch = "hide";
            }
            result.put("choiceSearch", choiceSearch);
            result.put("risk", risk);
            Set<Attachment> riskAtts = risk.getAttachments();
            String attids = "";
            if (null != riskAtts && riskAtts.size() != 0) {
                Set<String> set = new HashSet<String>();
                for (Attachment tblAttachment : riskAtts) {
                    set.add(tblAttachment.getAttid().toString());
                }
                attids = String.join(", ", set);
            }
            result.put("attids", attids);
        }
        result.put("orgid", orgid);
        result.put("type", type);
        if (name != null && name.trim().length() > 0) {
            result.put("name", name.substring(0, name.length() - 1));
        }
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        result.put("choiceSearch", choiceSearch);
        result.put("backUrl", backUrl);
        return new JsonBean(200, "success", result);
    }

    /**
     * 11
     *
     * @param riskid
     * @param orgid
     * @return
     * @author yangzeguo
     * @version v1.0.1
     * @Description 风险管控-相关问题汇总-风险发现 —> 点击列表风险编号跳出的展示页面
     * @Date 2022/8/7
     */
    @OperationLog(
            success = "风险编号详细信息处理成功",
            busType = "风险管控",
            fail = "风险编号详细信息处理失败",
            operationType = OperationType.SELECT,
            subType = "风险发现"
    )
    @Operation(summary = "风险发现-风险编号详细信息/nbkz/wthz/risk_disp")
    @RequestMapping(value = "/wthz/risk_disp")
    public JsonBean risk_disp(
            @Parameter(name = "riskid", description = "riskid") @RequestParam String riskid,
            @Parameter(name = "orgid", description = "orgid") @RequestParam String orgid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        //根据riskid找到risk对象 riskid723501

        NbkzRisk risk = nbkzRiskService.get(riskid);
        String name = "";
        if (risk != null && risk.getSysorgid() != null) {
            //根据获取到的risk对象那找到reorg(相关部门)的id并分割 //198328
            String[] str = risk.getSysorgid().split(",");
            for (String string : str) {
                //根据找到的reorg 的id 找到Tbl_Organization表中的数据
                Organization org = organizationService.getById(string);
                //获取相关部门的中文名字
                name += org.getOrgname() + ",";
            }
        }
        //根据riskid外键查到集合
        List<NbkzRisktolerability> list = nbkzRisktolerabilityMapper.findRiskTolerByRiskid(riskid);
        Map result = new HashMap();
        if (name != null && name.trim().length() > 0) {
            result.put("name", name.substring(0, name.length() - 1));
        }
        result.put("orgid", orgid);
        result.put("risk", risk);
        result.put("type", risk.getStype());
        result.put("list", list);
        return new JsonBean(200, "success", result);
    }

    /**
     * 11
     *
     * @return
     * @author yangzeguo
     * @version v1.0.1
     * @Description 验证风险发现-编号重复
     * @Date 2022/8/7
     */
    @OperationLog(
            success = "编号重复查询处理成功",
            busType = "风险管控",
            fail = "编号重复查询处理失败",
            operationType = OperationType.SELECT,
            subType = "风险发现"
    )
    @Operation(summary = "验证风险发现-编号重复 /nbkz/wthz/is_risknumber")
    @RequestMapping(value = "/wthz/is_risknumber")
    public JsonBean is_risknumber(
            @Parameter(name = "risknumber", description = "risknumber") @RequestParam(required = false) String risknumber,
            @Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        String str = "";
        NbkzRisk nbkzRisk = nbkzRiskService.getBycode(risknumber, type, selectOrg.getOrgid().toString());
        if (nbkzRisk != null) {
            str = "0";
        }
        return new JsonBean(200, "success", str);
    }

    /**
     * 11
     *
     * @param toleid
     * @return
     * @author yangzeguo
     * @version v1.0.1
     * @Description 风险容忍度--删除
     * @Date 2022/8/7
     */
    @OperationLog(
            success = "风险容忍度--删除处理成功",
            busType = "风险管控",
            fail = "风险容忍度--删除处理失败",
            operationType = OperationType.DELETE,
            subType = "风险发现"
    )
    @Operation(summary = "风险容忍度--删除 /nbkz/wthz/fxrrd_delete")
    @RequestMapping(value = "/wthz/fxrrd_delete")
    public JsonBean fxrrd_delete(@Parameter(name = "toleid", description = "toleid") @RequestParam BigDecimal toleid,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        nbkzRisktolerabilityService.removeById(toleid);
//        nbkzRisktolerabilityMapper.deleteById(toleid); 1000035


        return new JsonBean(200, "success", null);
    }

    /**
     * 11
     *
     * @param riskid
     * @param choiceSearch
     * @return
     * @author yangzeguo
     * @version v1.0.1
     * @Description 风险管控-相关问题汇总-风险发现 —> 删除
     * @Date 2022/8/8
     */
    @OperationLog(
            success = "风险发现 —> 删除处理成功",
            busType = "风险管控",
            fail = "风险发现 —> 删除处理失败",
            operationType = OperationType.DELETE,
            subType = "相关问题汇总"
    )
    @Operation(summary = "风险管控-相关问题汇总-风险发现 —> 删除/nbkz/wthz/risk_delete")
    @RequestMapping(value = "/wthz/risk_delete")
    public JsonBean risk_delete(
            @Parameter(name = "riskid", description = "riskid") @RequestParam(required = false) String riskid,
            @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(required = false) String choiceSearch,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        //根据id找到risk 124085
        NbkzRisk risk = nbkzRiskService.get(riskid);
        //找到TBL_ATTACHMENT 表中的对象 并删除 attid 124089
        List<Attachment> list = attachmentMapper.findtTblAttachmentByRisk(riskid);
        //删除中间表中的信息
        if (list != null && list.size() > 0) {
            for (Attachment riskAtt : list) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("attid", riskAtt.getAttid());
                riskAttService.remove(queryWrapper);
            }
        }
        if (list != null && list.size() > 0) {
            for (Attachment tblAttachment : list) {
                //根据TBL_ATTACHMENT中主键删除数据 attid
                attachmentMapper.deleteById(tblAttachment.getAttid());
            }
        }
        List<NbkzRisktolerability> risks = nbkzRisktolerabilityMapper.findRiskTolerByRiskid(riskid);
        if (risks != null && risks.size() > 0) {
            for (NbkzRisktolerability risktolerability : risks) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("riskid", risktolerability.getRiskid());
                nbkzRisktolerabilityService.remove(queryWrapper);
            }
        }
        nbkzRiskService.removeById(risk.getRiskid());
        // 为页面查找区域显隐藏赋值
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        // String url="redirect:/nbkz/wthz/risk_list?orgid="+orgid;
        return new JsonBean(200, "success", null);
    }

    /**
     * 11
     *
     * @param risk
     * @param starttime_min
     * @param starttime_max
     * @param type
     * @param belongsto
     * @param umpireid
     * @param attids
     * @param token
     * @return
     * @throws Exception
     * @author yangzeguo
     * @version v1.0.1
     * @Description 风险管控-相关问题汇总-风险发现 -保存
     * @Date 2022/8/7
     */
    @OperationLog(
            success = "风险发现 -保存处理成功",
            busType = "风险管控",
            fail = "风险发现 -保存处理失败",
            operationType = OperationType.ADD,
            subType = "相关问题汇总"
    )
    @Operation(summary = "风险管控-相关问题汇总-风险发现 -保存/nbkz/wthz/risk_save")
    @RequestMapping(value = "/wthz/risk_save")
    public JsonBean risk_add(
            // NbkzRisk risk,
            @Parameter(name = "risk", description = "risk") NbkzRisk risk,
            @Parameter(name = "starttime_min", description = "发生日期") @RequestParam String starttime_min,
            @Parameter(name = "starttime_max", description = "发现日期") @RequestParam String starttime_max,
            @Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
            @Parameter(name = "belongsto", description = "责任部门") @RequestParam String belongsto,
            @Parameter(name = "umpireid", description = "发现人") @RequestParam String umpireid,
            @Parameter(name = "attids", description = "保存附件") @RequestParam(required = false) String attids,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (starttime_min != null && starttime_min.trim().length() > 0) {
            try {
                risk.setOccureddate(sdf.parse(starttime_min).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                risk.setDiscovereddate(sdf.parse(starttime_max).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //测试数据 114306 责任部门
        if (belongsto != null && belongsto.trim().length() > 0) {
            Organization org = organizationService.getById(belongsto);
            risk.setTblOrganiDem(org);
        }
        // 发现部门（发现人）
        if (umpireid != null && umpireid.trim().length() > 0) {
            Staff user = staffService.getById(umpireid);
            risk.setTblStaff(user);
        }
        risk.setStype(type);
        nbkzRiskService.save(risk);
        // 保存附件
        if (StringUtils.isNotBlank(attids)) {
            String[] ids = attids.split(",");
            for (int i = 0; i < ids.length; i++) {
                //测试数据 主键attid
                Attachment att = attachmentService.getById(ids[i].trim());
                risk.getAttachments().add(att);
            }
        }
        return new JsonBean(200, "success", risk.getRiskid().toString());
    }

    /**
     * 11
     *
     * @param risktolerability
     * @return
     * @author yangzeguo
     * @version v1.0.1
     * @Description 风险容忍度--添加保存风险容忍度信息
     * @Date 2022/8/8
     */
    @OperationLog(
            success = "风险容忍度--添加处理成功",
            busType = "风险管控",
            fail = "风险容忍度--添加处理失败",
            operationType = OperationType.ADD,
            subType = "相关问题汇总"
    )
    @Operation(summary = "风险容忍度--添加/nbkz/wthz/fxrrd_save")
    @RequestMapping(value = "/wthz/fxrrd_save")
    public JsonBean fxrrd_save(
            @Parameter(name = "risktolerability", description = "risktolerability") NbkzRisktolerability risktolerability,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        nbkzRisktolerabilityService.save(risktolerability);
        return new JsonBean(200, "success", null);
    }


    /**
     * 11
     *
     * @param type
     * @param token
     * @return
     * @throws Exception
     * @author yangzeguo
     * @version v1.0.1
     * @Description 风险管控-相关问题汇总-风险发现  展示页面
     * @Date 2022/8/8
     */
    @OperationLog(
            success = "风险发现  展示页面查询处理成功",
            busType = "风险管控",
            fail = "风险发现  展示页面查询处理失败",
            operationType = OperationType.SELECT,
            subType = "相关问题汇总"
    )
    @Operation(summary = "风险管控-相关问题汇总-风险发现  展示页面 /nbkz/wthz/risk_index")
    @RequestMapping(value = "/wthz/risk_index")
    public JsonBean risk_index(
            @Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
            @Parameter(name = "token", description = "登录用户token", required = true)
            @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        //  Staff staff = staffService.getById(staffUtil.getStaffid());
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        Map result = new HashMap();
        result.put("type", type);
        result.put("orgid", staffOrg.getOrgid());
        result.put("orgtype", staffOrg.getOrgtype());
        return new JsonBean(200, "success", result);
    }

    /**
     * 11
     *
     * @param type
     * @param token
     * @return
     * @throws Exception
     * @author yangzeguo
     * @version v1.0.1
     * @Description 风险管控-相关问题汇总-风险发现  获取左侧部门树形结构
     * @Date 2022/8/8
     */
    @OperationLog(
            success = "风险发现  获取左侧部门树形结构处理成功",
            busType = "风险管控",
            fail = "风险发现  获取左侧部门树形结构处理失败",
            operationType = OperationType.SELECT,
            subType = "相关问题汇总"
    )
    @Operation(summary = "风险管控-相关问题汇总-风险发现  获取左侧部门树形结构 /nbkz/wthz/userleft")
    @RequestMapping(value = "/wthz/userleft")
    public JsonBean userLeft_risk(@Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
                                  @Parameter(name = "token", description = "登录用户token", required = true)
                                  @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        //ModelAndView mv = new ModelAndView("/nbkz/qxwt/risk_user_left");
        Map result = new HashMap();
        result.put("type", type);
        result.put("orgid", selectOrg.getOrgid());
        result.put("treeName", selectOrg.getOrgname());
        return new JsonBean(200, "success", result);
    }

    /**
     * 1
     *
     * @param controlId
     * @param innrulid
     * @return
     * @author yangzeguo
     * @version v1.0.1
     * @Description 风险管控-相关问题汇总-缺陷管理- 修改  新建 -内规删除
     * @Date 2022/8/8
     */
    @OperationLog(
            success = "缺陷管理- 修改  新建 -内规删除处理成功",
            busType = "风险管控",
            fail = "缺陷管理- 修改  新建 -内规删除处理失败",
            operationType = OperationType.DELETE,
            subType = "相关问题汇总"
    )
    @Operation(summary = "风险管控-相关问题汇总-缺陷管理- 修改  新建 -内规删除/nbkz/delete_qx_inner")
    @RequestMapping(value = "/delete_qx_inner")
    public String delete_internal_regulations_qx(
            @Parameter(name = "controlId", description = "controlId") @RequestParam(name = "controlId") BigDecimal controlId,
            @Parameter(name = "innrulid", description = "innrulid") @RequestParam(name = "innrulid") String innrulid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("BUGID", controlId);//126782
        queryWrapper.eq("INNRULID", innrulid);//1
        bugInnerruleService.remove(queryWrapper);
        return JsonBean.success();
    }

    /**
     * 1
     *
     * @param controlId
     * @param innerid
     * @return
     * @author yangzeguo
     * @version v1.0.1
     * @Description 风险管控-相关问题汇总-缺陷管理- 修改 新建- 外规删除
     * @Date 2022/8/8
     */
    @OperationLog(
            success = "缺陷管理- 修改 新建 -外规删除处理成功",
            busType = "风险管控",
            fail = "缺陷管理- 修改 新建 -外规删除处理失败",
            operationType = OperationType.DELETE,
            subType = "相关问题汇总"
    )
    @Operation(summary = "风险管控-相关问题汇总-缺陷管理- 修改 新建 -外规删除/nbkz/delete_qx_outer")
    @RequestMapping(value = "/delete_qx_outer")
    public String delete_internal_regulations_qx_outer(
            @Parameter(name = "controlId", description = "controlId") @RequestParam(name = "controlId") BigDecimal controlId,
            @Parameter(name = "outrulid", description = "outrulid") @RequestParam(name = "innrulid") BigDecimal innerid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("BUGID", controlId);//203919
        queryWrapper.eq("OUTRULID", innerid);//203820
        bugOuterruleService.remove(queryWrapper);
        return JsonBean.success();
    }


    @Autowired
    private IBugService bugService;

    /*status:ok pass+1
     * @author zuoshun
     * @version v1.0.1
     * @Description 对缺陷进行删除操作
     * @Date 2022/8/5
     * @param selectProjectid
     * @param wt
     * @param type
     * @param wtorgid
     * @param orgid 753181
     * @param choiceSearch
     * @return com.hbfk.util.JsonBean
     * @url: http://localhost:8081/nbkz/qxwt/defect_delete?selectProjectid=753181
     **/
    @OperationLog(
            success = "缺陷管理——删除处理成功",
            busType = "风险管控",
            fail = "缺陷管理——删除处理失败",
            operationType = OperationType.DELETE,
            subType = "相关问题汇总"
    )
    @Operation(summary = "相关问题汇总——缺陷管理——删除 /nbkz/qxwt/defect_delete")
    @RequestMapping("/qxwt/defect_delete")
    public JsonBean defectDelete(@Parameter(description="selectProjectid") @RequestParam(required = false) String selectProjectid,
                                 @Parameter(description="wt")@RequestParam(required = false) String wt,
                                 @Parameter(description="type")@RequestParam(required = false)  String type,
                                 @Parameter(description="wtorgid")@RequestParam(required = false) String wtorgid,
                                 @RequestParam(required = false) String orgid,
                                 @RequestParam(required = false) String choiceSearch){
      return   bugService.defectDelete(selectProjectid,wt,type,wtorgid,orgid,choiceSearch);
    }



    /*status=ok  pass+1
     * @author zuoshun
     * @version v1.0.1
     * @Description  获取左侧部门树形结构
     * @Date 2022/8/6
     * @param type
     * @param nodeId
     * @param hbOrgEntity
     * @return com.hbfk.util.JsonBean
     * @url:http://localhost:8081/nbkz/qxwt/userleft?nodeId=138309
     **/
    @OperationLog(
            success = "缺陷管理——组织结构查询处理成功",
            busType = "风险管控",
            fail = "缺陷管理——组织结构查询处理失败",
            operationType = OperationType.SELECT,
            subType = "相关问题汇总"
    )
    @Operation(summary = "相关问题汇总——缺陷管理——组织结构  /nbkz/qxwt/userleft")
    @RequestMapping("/qxwt/userleft")
    public JsonBean getAllDepartmentWithTree(@RequestParam(required = false) String type,
                                             @RequestParam(required = false) String nodeId,
                                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        log.info("nodeId={}",nodeId);
        return organizationService.getAllDepartmentWithTree(type,nodeId,token);
    }


    /* pass+1
     * @author zuoshun
     * @version v1.0.1
     * @Description 缺陷管理-->新建-->发现人-->组织架构
     * @Date 2022/8/13
     * @param nodeId
     * @param type
     * @param orgId
     * @param staff
     * @return com.hbfk.util.JsonBean
     * @url: http://localhost:8081/nbkz/user/lefts?nodeId=114308
     **/
    @OperationLog(
            success = "新建——发现人——组织架构查询处理成功",
            busType = "风险管控",
            fail = "新建——发现人——组织架构查询处理失败",
            operationType = OperationType.SELECT,
            subType = "相关问题汇总"
    )
    @Operation(summary = "缺陷管理——新建——发现人——组织架构 /nbkz/user/lefts")
    @RequestMapping("/user/lefts")
    public JsonBean userLeft(@RequestParam(required = false) String nodeId,
                             @RequestParam(required = false) String type,
                             @RequestParam(required = false) String orgId,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        return  organizationService.userLeft(nodeId,type,orgId,token);
    }





    @Autowired
    private IReportService reportService;

    /* status:ok  pass+1
     * @author zuoshun
     * @version v1.0.1
     * @Description
     * @Date 2022/8/9
     * @param data
     * @return java.lang.String
     * @url: http://localhost:8081/nbkz/nkbg/list?type=nbsj_zdy&startDate=2010-09-11&endDate=2022-08-15&orgid=1
     * http://localhost:8081/nbkz/nkbg/list?type=nbsj_zdy&startDate=2010-09-11&endDate=2022-08-15&pageNumber=1
     **/
    @OperationLog(
            success = "风险报告编制列表 & 自定义报告编制列表查询处理成功",
            busType = "风险管控",
            fail = "风险报告编制列表 & 自定义报告编制列表查询处理失败",
            operationType = OperationType.SELECT,
            subType = "相关问题汇总"
    )
    @Operation(summary = "风险报告编制列表 & 自定义报告编制列表 /nbkz/nkbg/list")
    @RequestMapping(value = "/nkbg/list")
    public JsonBean reportList(Find find,
                               @Parameter(name="type",description="fx或fx_zdy",required=true) @RequestParam(required = true) String type,
                               @RequestParam(required = false)String projectId,
                               @RequestParam(required = false)String view,
                               @RequestParam(required = false,defaultValue ="1" )Integer pageNumber,
                               @RequestParam(required = false,defaultValue ="20") Integer pageSize,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        return reportService.reportList(find,type,projectId,view,pageNumber,pageSize,token);
    }

    /**
     * 总公司查看分公司上报的信息
     * @param find
     * @param type
     * @param projectId
     * @param view
     * @param pageNumber
     * @param pageSize
     * @param token
     * @return
     * @throws Exception
     */
   /* @OperationLog(
            success = "总公司风险报告编制列表 & 总公司自定义报告编制列表查询处理成功",
            busType = "风险管控",
            fail = "总公司风险报告编制列表 & 总公司自定义报告编制列表 查询处理失败",
            operationType = OperationType.SELECT,
            subType = "相关问题汇总"
    )*/
    @Operation(summary = "总公司风险报告编制列表 & 总公司自定义报告编制列表 /nbkz/nkbg/companyList")
    @RequestMapping(value = "/nkbg/companyList")
    public JsonBean companyReportList(Find find,
                               @Parameter(name="type",description="fx或fx_zdy",required=true) @RequestParam(required = true) String type,
                               @RequestParam(required = false)String projectId,
                               @RequestParam(required = false)String view,
                               @RequestParam(required = false,defaultValue ="1" )Integer pageNumber,
                               @RequestParam(required = false,defaultValue ="20") Integer pageSize,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        return reportService.companyReportList(find,type,projectId,view,pageNumber,pageSize,token);
    }



    @OperationLog(
            success = "风险报告--上报到总公司处理成功",
            busType = "风险管控",
            fail = "风险报告--上报到总公司处理失败",
            operationType = OperationType.UPDATE,
            subType = "相关问题汇总"
    )
	@Operation(summary = "风险报告--上报到总公司")
    @ResponseBody
    @RequestMapping(value = "/nkbg/reportToLeader")
    public JsonBean reportToLeader(
    		@Parameter(name = "reportid", description = "报告id", required = false) @RequestParam(value = "reportid", required = false) BigDecimal reportid,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
                                    ) throws Exception {
        return reportService.reportToLeader(token,reportid);
    }




    /* pass+1
     * @author zuoshun
     * @version v1.0.1
     * @Description 风险报告删除
     * @Date 2022/8/9
     * @param ids
     * @return com.hbfk.util.JsonBean
     * @url: http://localhost:8081/nbkz/nkbg/delete?ids=114822&ids=128131&ids=773749
     **/
    @OperationLog(
            success = "风险报告编制和自定义风险报告编制--删除处理成功",
            busType = "风险管控",
            fail = "风险报告编制和自定义风险报告编制--删除处理失败",
            operationType = OperationType.DELETE,
            subType = "相关问题汇总"
    )
    @Operation(summary = "风险报告--风险报告编制和自定义风险报告编制--删除 /nbkz/nkbg/delete")
    @RequestMapping("/nkbg/delete")
    public JsonBean reportDelete(@Parameter(description = "ids")@RequestParam String []ids,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token){
        log.info(Arrays.toString(ids));
        return reportService.reportDelete(ids);
    }








    /* STATUS:OK  pass+1
     * @author zuoshun
     * @version v1.0.1
     * @Description 通过前端传入的pid-组织编号查询人员
     * @Date 2022/8/4
     * @param pid 组织编号
     * @param type
     * @param organization
     * @param pageNumber
     * @return com.hbfk.util.JsonBean
     * @url: http://localhost:8081/nbkz/user/list?pid=116832
     **/
    @OperationLog(
            success = "新建——选择发现人(风险报告编制-新建-选择报告人) 查询处理成功",
            busType = "风险管控",
            fail = "新建——选择发现人(风险报告编制-新建-选择报告人) 查询处理失败",
            operationType = OperationType.SELECT,
            subType = "缺陷管理"
    )
    @Operation(summary = "缺陷管理——新建——选择发现人(风险报告编制-新建-选择报告人) /nbkz/user/list")
    @RequestMapping("/user/list")
    public JsonBean userList(@Parameter(name="orgid",description="左侧组织列表选中的组织ID, 例如：审计监察部198328",required=false) @RequestParam(required = false)  String pid,
                             @RequestParam(required = false) String type,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                              @RequestParam(defaultValue = "1", required = false) Integer pageNo,
                              @RequestParam(defaultValue = "20", required = false)Integer pageSize) throws Exception {
        return staffService.userList(pid,type,token,pageNo,pageSize);
    }


    /* status:ok PASS+1
     * @author zuoshun
     * @version v1.0.1
     * @Description 缺陷全查展示操作
     * @Date 2022/8/5
     * @param orgid
     * @param orgtype
     * @param pageNumber
     * @param searchbegintime
     * @param searchendtime
     * @param plancode
     * @param plantype
     * @param state
     * @param buglevelquery
     * @param type
     * @return com.hbfk.util.JsonBean
     * @url: http://localhost:8081/qxwt/defect_list?orgid=115332&orgtype=0&type=fxgl&searchbegintime=2019-01-01&searchendtime=2019-01-01
     **/
    @OperationLog(
            success = "缺陷管理-缺陷列表查询处理成功",
            busType = "风险管控",
            fail = "缺陷管理-缺陷列表查询处理失败",
            operationType = OperationType.SELECT,
            subType = "缺陷管理"
    )
    @Operation(summary = "相关问题汇总--缺陷管理-缺陷列表 /nbkz/qxwt/defect_list")
    @RequestMapping(value = "/qxwt/defect_list")
    public JsonBean defectList(@RequestParam(required = false) String orgid,
                               @RequestParam(required = false) String orgtype,
                               @RequestParam(defaultValue = "1") Integer pageNo,
                               @RequestParam(defaultValue = "20") Integer pageSize,
                               @RequestParam(required = false) String searchbegintime,
                               @RequestParam(required = false)  String  searchendtime,
                               @RequestParam(required = false)  String plancode,
                               @RequestParam(required = false) String plantype,
                               @RequestParam(required = false) String state,
                               @RequestParam(required = false)  String buglevelquery,
                               @RequestParam(required = false) String type,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        return bugService.defectList(orgid,orgtype,pageNo,pageSize,searchbegintime,searchendtime,
                plancode,plantype,state,buglevelquery,type,token);
    }









    /* pass+1
     * @author zuoshun
     * @version v1.0.1
     * @Description  通过缺陷id查询该缺陷详情。
     * @Date 2022/8/10  115332
     * @param selectProjectid 115633
     * @param hbOrgEntity
     * @return com.hbfk.util.JsonBean
     * @url:http://localhost:8081/qxwt/defect_detail?selectProjectid=115633&orgid=115332
     **/
    @OperationLog(
            success = "缺陷管理——缺陷详情查询处理成功",
            busType = "风险管控",
            fail = "缺陷管理——缺陷详情查询处理失败",
            operationType = OperationType.SELECT,
            subType = "缺陷管理"
    )
    @Operation(summary = "缺陷管理——缺陷详情  /nbkz/qxwt/defect_detail")
    @RequestMapping(value = "/qxwt/defect_detail")
    public JsonBean defectDetail(@RequestParam String selectProjectid,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        return bugService.defectDetail(selectProjectid,token);
    }






    /*pass+1
    * @author zuoshun
    * @version v1.0.1
    * @Description  风险报告详情
    * @Date 2022/8/18
    * @param id 792954
    * @param type fx_zdy
    * @param token BF14600551D72E393B38CDE1A6710721
    * @return com.hbfk.util.JsonBean
    * @url:
    **/
    @OperationLog(
            success = "风险报告详情查询处理成功",
            busType = "风险管控",
            fail = "风险报告详情查询处理失败",
            operationType = OperationType.SELECT,
            subType = "缺陷管理"
    )
    @Operation(summary = "风险报告详情 /nbkz/nkbg/to_bg_info")
    @RequestMapping("/nkbg/to_bg_info")
    public JsonBean reportDetail(@Parameter(description="例如 792954") @RequestParam String id,
                                 @Parameter(description="fx_zdy") @RequestParam(required = false) String type,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token){
      return  reportService.reportDetail(id,type);
    }


    /**
     *
     * @param request
     * @param response
     * @param type
     * @param orgid
     * @param token
     * @return
     * @throws Exception
     */
    @OperationLog(
            success = "缺陷导出 excel 处理成功",
            busType = "风险管控",
            fail = "缺陷导出 excel 处理失败",
            operationType = OperationType.EXPORT,
            subType = "缺陷管理"
    )
    @Operation(summary = "缺陷导出 excel /nbkz/wthz/qxgl_export")
    @RequestMapping(value = "/wthz/qxgl_export")
    public @ResponseBody
    JsonBean sjfx_export(HttpServletRequest request, HttpServletResponse response,
                         @Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
                         @Parameter(name = "orgid", description = "orgid") @RequestParam(required = false) String orgid,
                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader(name = "token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        if (orgid == null || orgid == "") {
            // Organization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 选则的机构
            orgid = selectOrg.getOrgid().toString();
        }
        log.info("内控合规---问题汇总---缺陷管理---导出Excel");
        response.setContentType("application/binary;charset=UTF-8");
        try {
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String("缺陷管理".getBytes(), "iso-8859-1") + ".xlsx");
            ServletOutputStream outputStream = response.getOutputStream();
            List<Object[]> objList = bugService.qxglExport(orgid, type);
            String[] titles = {"缺陷编号", "缺陷级别", "发现日期", "发现人", "是否财务相关", "缺陷性质", "公司名称", "缺陷部门", "是否需要整改", "业务描述",
                    "缺陷描述"};
            // String[] titles = {"缺陷编号 ", "缺陷级别", "发现日期", "发现人", "是否财务相关",
            // "缺陷性质", "缺陷部门", "业务描述","缺陷描述","是否需要整改","不整改原因"};
            ImportOrExportExcelUtil.exportExcel(titles, objList, outputStream, null);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("内控合规---问题汇总---缺陷管理---导出Excel失败");
        }
        return new JsonBean(200, "success", null);
    }


    @OperationLog(
            success = "底稿汇总 - 导出处理成功",
            busType = "风险管控",
            fail = "底稿汇总 - 导出处理失败",
            operationType = OperationType.EXPORT,
            subType = "缺陷管理"
    )
    @RequestMapping(value = "/zgdg/exportAll")
    @Operation(summary = "风险管控 - 相关问题汇总 - 底稿汇总 - 导出/nbkz/zgdg/exportAll")
    public void exportAll(HttpServletResponse response,
                          @Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
                          @Parameter(name = "orgid", description = "orgid")@RequestParam(required = false) String orgid,
                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader(name="token") String token) throws Exception {
      TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
      TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
       TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        Boolean sjByOrgId = organizationService.isSJByOrgId(staffOrg.getOrgid().toString());
        List<Worksheet> list = null;
        if (sjByOrgId) {
            ///n 116821
            list = worksheetService.findAllTblWorksheetAlls(type, selectOrg.getOrgid().toString());
        } else {
            list = worksheetService.findAllTblWorksheetAll(type, orgid);
        }

        String[] cNames = { "底稿编号", "底稿名称", "审计目标", "审计对象", "拟稿人", "拟稿日期" };
        ExcelUtil exportUtil = new ExcelUtil("流程树", cNames);
        HSSFCell cell = null;
        HSSFCellStyle bodyStyle = exportUtil.getBodyStyle();

        for (int j = 0; j < list.size(); j++) {
            HSSFRow bodyRow = exportUtil.getNextRow(j + 1);
            Worksheet work = list.get(j);
            cell = bodyRow.createCell(0);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(work.getWorksheetnumber());
            cell = bodyRow.createCell(1);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(work.getWorksheetname());

            cell = bodyRow.createCell(2);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(work.getAudittarget());

            cell = bodyRow.createCell(3);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(work.getAuditedorg());
            cell = bodyRow.createCell(4);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(work.getRecorder());

            cell = bodyRow.createCell(5);
            cell.setCellStyle(bodyStyle);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            cell.setCellValue(sdf.format(work.getRecordingdate()));

        }
        exportUtil.writeOut(response, "dghz.xls");
    }


    @OperationLog(
            success = "缺陷管理左侧树默认加载处理成功",
            busType = "风险管控",
            fail = "缺陷管理左侧树默认加载处理失败",
            operationType = OperationType.SELECT,
            subType = "缺陷管理"
    )
    @RequestMapping(value = "/qxwt/findOrganizationByTreeAllbm")
    @Operation(summary = "风险管控 - 相关问题汇总-缺陷管理左侧树默认加载-findOrganizationByTreeAllbm ")
	public @ResponseBody String findOrganizationByTrees(
			@Parameter(name = "nodeId", description = "nodeId") @RequestParam(required = false) BigDecimal nodeId,
			@Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
			 HttpServletRequest request) {
		if (null == nodeId) {
			TblOrganizationUtil organization = (TblOrganizationUtil) request.getSession().getAttribute("hbOrgEntity");
			nodeId = organization.getOrgid();
		}
		String str;
		HashMap<String, Object> fields = new HashMap<String, Object>();
		fields.put("nodeId",nodeId);
		try {
			str = HttpClient.request(HttpClient.getDeptUrl, fields, null);
			if(!StringUtils.isNotBlank(str)) {
				List<Tree> list = this.organizationService.getNodeAllbm(nodeId);
				str = JSONObject.toJSONString(list);
			}
			return str;
		} catch (Exception e) {
			List<Tree> list = this.organizationService.getNodeAllbm(nodeId);
			str = JSONObject.toJSONString(list);
		}
		return str;
	}


    /**
	 * 部门树
	 */
    @OperationLog(
            success = "部门树查询处理成功",
            busType = "风险管控",
            fail = "部门树查询处理失败",
            operationType = OperationType.SELECT,
            subType = "缺陷管理"
    )
	@RequestMapping(value = "/comm/findOrganizationByTreeAllss", method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary = "部门树")
	public @ResponseBody String htdlfindOrganizationByTree(@Parameter(name = "nodeId", description = "nodeId", required = false)BigDecimal nodeId,
														   @Parameter(name = "type", description = "type", required = false)String type,
														   @Parameter(name = "orgId", description = "orgId", required = false)BigDecimal orgId,
														   @Parameter(name = "idname", description = "idname", required = false)String idname,
														   @Parameter(name = "textname", description = "textname", required = false)String textname,
														   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
														   @Parameter(name = "staffId", description = "登录用户主键", required = false)String staffId,
														   HttpServletRequest request) throws Exception {
		String json = "";
		if (null == nodeId) {
			nodeId = orgId;
			if (null == orgId) {
				TblStaffUtil user = userProvider.get();
				nodeId = user.getCurrentOrg().getOrgid();
			}
		}
		if (StringUtils.isNotBlank(type)) {
			List<Tree> list = this.organizationService.getTrees(nodeId);
			for (Tree tree : list) {
				if (!tree.getIsParent()) {
					tree.setTarget("mainFramex");
					tree.setUrl("/nbkz/pjlx/list?pid=" + tree.getId() + "&idname=" + idname + "&textname=" + textname);
				}
			}
			json = JSONObject.toJSONString(list);
		} else {
			List<Tree> list = this.organizationService.getNodeAlls(nodeId);
			for (Tree tree : list) {
				setUrlByTree(tree, "/nbkz/pjlx/list?idname=" + idname + "&textname=" + textname + "&pid=");
			}
			json = JSONObject.toJSONString(list);
		}
		return json;
	}
	private void setUrlByTree(Tree tree, String url) {
		for (Tree tre : tree.getChildren()) {
			/// if (!tre.getIsParent()) {
			tre.setTarget("mainFramex");
			tre.setUrl(url + tre.getId());
			// }
			if (tre.getChildren().size() > 0) {
				setUrlByTree(tre, url);
			}
		}
	}

	
 @OperationLog(
	success = "获取风险报告类型统计成功",
	busType = "风控模块",
	fail = "获取风险报告类型统计失败",
	operationType = OperationType.SELECT,
	subType = "首页分析"
) 
@Operation(summary = "风险分析-风险报告类型统计")
@RequestMapping(value = "/getRiskReportTypeCountByCompany",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
public JsonBean getRiskReportTypeCountByCompany(
		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
		@Parameter(name = "company", description = "公司查询条件") @RequestParam(required = false) String company) throws Exception {
 Map<String,Object> result = new HashMap<>();
try {
    result= reportService.getRiskReportTypeCountByCompany(token,company);
} catch (Exception e) {
    e.printStackTrace();
    log.info("风险分析-风险报告类型统计，异常");
}
return ResponseFormat.retParam(1, 200, result);
}
}
