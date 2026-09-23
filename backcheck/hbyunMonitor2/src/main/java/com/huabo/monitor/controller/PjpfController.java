package com.huabo.monitor.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.ElemGrade;
import com.huabo.monitor.entity.TblAssess;
import com.huabo.monitor.entity.TblAssessStaff;
import com.huabo.monitor.entity.TblAssessVo;
import com.huabo.monitor.entity.TblAssesselement;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.entity.TblFlow;
import com.huabo.monitor.entity.TblOrganization;
import com.huabo.monitor.service.ITblAmorgProblemService;
import com.huabo.monitor.service.ITblAssessMarkService;
import com.huabo.monitor.service.ITblAssessStaffService;
import com.huabo.monitor.service.ITblAssessTargetService;
import com.huabo.monitor.service.ITblAssesselementService;
import com.huabo.monitor.service.ITblAssesslevelService;
import com.huabo.monitor.service.ITblAssesstempleService;
import com.huabo.monitor.service.ITblAttachmentService;
import com.huabo.monitor.service.ITblFlowService;
import com.huabo.monitor.service.ITblStaffService;
import com.huabo.monitor.service.ITblTaskService;
import com.huabo.monitor.service.PjjgService;
import com.huabo.monitor.service.PjpfService;
import com.huabo.monitor.service.TblAssessService;
import com.huabo.monitor.util.ConstClass;
import com.huabo.monitor.util.IPageResult;
import com.huabo.monitor.vo.FileUploadRes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author meng
 * @date 2022-08-26 9:36
 * @description
 */
@RestController
@Slf4j
@Tag(name="评价管理-评价评分",description="评价管理-评价评分")
@RequestMapping(value = "/nbkz")
public class PjpfController {

 
    @Autowired
    PjpfService pjpfService;
    @Autowired
    TblAssessService assessService;

    @Autowired
    TblAssessService tblAssessService;

    @Autowired
    ITblStaffService iTblStaffService;
    @Autowired
    ITblAssessMarkService iTblAssessMarkService;

    @Autowired
    ITblTaskService iTblTaskService;
    @Autowired
    ITblAssesstempleService iTblAssesstempleService;

    @Autowired
    PjjgService pjjgService;

    @Autowired
    ITblAssesslevelService iTblAssesslevelService;

    @Autowired
    ITblAssessTargetService iTblAssessTargetService;

    @Autowired
    ITblAmorgProblemService iTblAmorgProblemService;
    @Autowired
    ITblAttachmentService iTblAttachmentService;
    @Autowired
    ITblAssesselementService iTblAssesselementService;
    @Autowired
    ITblFlowService iTblFlowService;
    @Autowired
    ITblAssessStaffService  iTblAssessStaffService;
    
    @Resource
    private UserProvider userProvider;



    @OperationLog(
            success = "评价评分-主页查询-发起查询成功",
            busType = "内控设置",
            fail = "评价评分-主页查询-发起查询失败",
            operationType = OperationType.SELECT,
            subType = "评价评分"
    )
    @GetMapping(value = "/pjgl/t08_proj_pingfen")
    @Operation(summary = "评价评分-主页查询-发起")
    public JsonBean t08_proj_pingfen(
            @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @Parameter(name="selectedPlans",description="评估项目的id") @RequestHeader("selectedPlans") BigDecimal selectedPlans,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (Objects.isNull(selectedPlans) || selectedPlans.equals("")) {
            return new JsonBean(400, "缺少评估人员", null);
        }

        //等价于 ==》 TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
        IPage iPage = new Page(pageNumber, ConstClass.DEFAULT_SIZE);
        iPage = pjpfService.getTblAssessGroupOrg(
                iPage,
                selectedPlans,
                user.getStaffid());
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("ASSID", selectedPlans);
        TblAssess tblAssess = pjpfService.getOne(wrapper);

        Map<String, Object> mv = new HashMap<>();
        mv.put("pageBean", iPage);
        mv.put("project", tblAssess);
        return new JsonBean(200, "success", mv);
    }

    @OperationLog(
            success = "评价评分-主页查询成功",
            busType = "内控设置",
            fail = "评价评分-主页查询失败",
            operationType = OperationType.SELECT,
            subType = "评价评分"
    )
    @GetMapping(value = "/pjgl/proj_task_gradelist")
    @Operation(summary = "评价评分-主页查询")
    public JsonBean proj_initiatePjgl(
            @Parameter(name = "pageNumber", description = "pageNumber", required = true) @RequestParam(value = "pageNumber", required = true) Integer pageNumber,
            @Parameter(name = "projkey", description = "评价项目编号") @RequestParam(value = "projkey", required = false) String projkey,
            @Parameter(name = "projname", description = "评价项目名称") @RequestParam(value = "projname", required = false) String projname,
            @Parameter(name = "startDate", description = "开始时间:格式年-月-日") @RequestParam(value = "startDate", required = false) String startDate,
            @Parameter(name = "endDate", description = "结束日期") @RequestParam(value = "endDate", required = false) String endDate,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

//        IPage<TblAssessVo> iPage = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
//
//        iPage = pjpfService.findPageBean(
//                iPage,
//                staff.getStaffid(),
//                projkey,
//                projname,
//                startDate,
//                endDate
//        );
//
        PageInfo<TblAssessVo> pageInfo=pjpfService.findPageBeanNew(pageNumber,staff.getStaffid(), projkey, projname, startDate, endDate,staff);
        IPageResult<TblAssessVo> iPage=new IPageResult<TblAssessVo>().buildIpage(pageInfo);
        System.out.println("page = --->" + iPage);
        Map<String, Object> mv = new HashMap<>();
        mv.put("pageBean", iPage);
        return new JsonBean(200, "success", mv);

    }


    /**
     * 参评人 评分树形
     *
     * @param tmplId
     * @return
     */
    @OperationLog(
            success = "评价评分-左侧评分树形查询成功",
            busType = "内控设置",
            fail = "评价评分-左侧评分树形查询失败",
            operationType = OperationType.SELECT,
            subType = "评价评分"
    )
    @GetMapping(value = "/gzdg/leftTreeByPingfen")
    @Operation(summary = "评价评分-左侧评分树形")
    public JsonBean leftTreeByPingfen(
            @Parameter(name="tmplId",description="tmplId",required=false) @RequestParam(value = "tmplId", required = false) BigDecimal tmplId,
            @Parameter(name="assId",description="assId",required=false) @RequestParam(value = "assId", required = false) BigDecimal assId,
            @Parameter(name="orgId",description="orgId",required=false) @RequestParam(value = "orgId", required = false) BigDecimal orgId,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        // String tree =
        // this.tblAssesscategoryService.GetTree(tmplId,"/nbkz/pjgl/t08_proj_cat_list1?assId="
        // + assId + "&orgId=" + orgId);
        String tree = this.pjpfService.GetTree(tmplId,
                "/nbkz/pjgl/t08_proj_cat_list1?assId=" + assId + "&orgId=" + orgId, user.getStaffid().toString());
        return new JsonBean(200, "success", tree);
    }


    /**
     * 参评人 评分入口
     *
     * @param
     * @return
     */
    @OperationLog(
            success = "评价评分-评分入口查询成功",
            busType = "内控设置",
            fail = "评价评分-评分入口查询失败",
            operationType = OperationType.SELECT,
            subType = "评价评分"
    )
    @GetMapping(value = "/pjgl/index")
    @Operation(summary = "评价评分-评分入口")
    public JsonBean proj_index(
            @Parameter(name="assId",description="assId") @RequestParam(value = "assId") String assId,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        Map<String, Object> mv = new HashMap<>();


        String[] val = assId.split("#");
        mv.put("assid", val[0]);
        mv.put("orgId", val[1]);

        return new JsonBean(200, "success", mv);
    }


    /**
     * 评分树形页面跳转
     *
     * @param
     * @return
     */
    @OperationLog(
            success = "评价评分-评分树形页面跳转查询成功",
            busType = "内控设置",
            fail = "评价评分-评分树形页面跳转查询失败",
            operationType = OperationType.SELECT,
            subType = "评价评分"
    )
    @GetMapping(value = "/pjgl/menu_left_tree")
    @Operation(summary = "评价评分-评分树形页面跳转")
    public JsonBean proj_menu_left_tree(
            @Parameter(name="assId",description="assId",required=false) @RequestParam(value = "assId", required = false) BigDecimal assId,
            @Parameter(name="orgId",description="orgId",required=false) @RequestParam(value = "orgId", required = false) BigDecimal orgId,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblAssess assess = this.assessService.getById(assId);
        Map<String, Object> mv = new HashMap<>();
        mv.put("tmplId", assess.getAsstemid());
        mv.put("assId", assId);
        mv.put("orgId", orgId);
        return new JsonBean(200, "success", mv);
    }


    /**
     * 需要评分的要数
     *
     * @param
     * @return
     */
    @OperationLog(
            success = "评价评分-点击左侧树显示右侧评分要素列表查询成功",
            busType = "内控设置",
            fail = "评价评分-点击左侧树显示右侧评分要素列表查询失败",
            operationType = OperationType.SELECT,
            subType = "评价评分"
    )
    @GetMapping(value = "/pjgl/t08_proj_cat_list1")
    @Operation(summary = "评价评分-点击左侧树显示右侧评分要素列表")
    public JsonBean proj_t08_proj_cat_list1(
            @Parameter(name="tmplId",description="tmplId",required=false) @RequestParam(value = "tmplId", required = false) BigDecimal tmplId,
            @Parameter(name="nodeId",description="nodeId",required=false) @RequestParam(value = "nodeId", required = false) BigDecimal nodeId,
            @Parameter(name="assId",description="assId",required=false) @RequestParam(value = "assId", required = false) BigDecimal assId,
            @Parameter(name="orgId",description="orgId",required=false) @RequestParam(value = "orgId", required = false) BigDecimal orgId,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> mv = new HashMap<>();
        TblOrganization organization = this.assessService.queryOrganizationById(orgId);
            List<ElemGrade> list = this.pjpfService.findElementByassIdAndUserId(nodeId, assId,
                    user.getStaffid(), orgId);
            mv.put("list", list);
            //request.getSession().setAttribute("pjpfysList", list);
        mv.put("organization", organization);
        mv.put("orgId", organization.getOrgid());
        mv.put("assid", assId);
        mv.put("nodeId", nodeId);
        return new JsonBean(200, "success", mv);
    }


    /**
     * 修改要素
     *
     * @param basicId
     * @return
     */
    @OperationLog(
            success = "评价评分-要素列表-要素详情查询成功",
            busType = "内控设置",
            fail = "评价评分-要素列表-要素详情查询失败",
            operationType = OperationType.SELECT,
            subType = "评价评分"
    )
    @GetMapping(value = "/gzdg/def_basic_modify")
    @Operation(summary = "评价评分-要素列表-要素详情")
    public JsonBean gzdg_def_basic_modify(
            @Parameter(name="basicId",description="asseleid",required=false) @RequestParam(value = "basicId", required = false) BigDecimal basicId,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblAssesselement assesselement = this.iTblAssesselementService.getById(basicId);

        Map<String, Object> mv = new HashMap<>();
        QueryWrapper<TblFlow> qw = new QueryWrapper();
        qw.eq("flowbysystem", "1").eq("fatherflowid", 0).eq("company", user.getCurrentOrg().getOrgid());
        List<TblFlow> flows = this.iTblFlowService.list(qw);

        mv.put("conTocat", assesselement.getBusinesstype());
        mv.put("flows", flows);

        mv.put("assesselement", assesselement);

        return new JsonBean(200, "success", mv);
    }


    /**
     * 保存评分
     *
     * @param elements
     * @return
     */
    @OperationLog(
            success = "评价评分-评价-保存评价成功",
            busType = "内控设置",
            fail = "评价评分-评价-保存评价失败",
            operationType = OperationType.ADD,
            subType = "评价评分"
    )
    @PostMapping(value = "/pjgl/t08_proj_cat_list1_save")
    @Operation(summary = "评价评分-评价-保存评价")
    public String proj_t08_proj_cat_list1_save(
            @Parameter(name="elements",description="elements",required=true) @RequestParam(value = "elements", required = true) String elements,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return JsonBean.error("用户已失效");
        }
        if (StringUtils.isNotBlank(elements)) {
            String[] elementlist = elements.split("#N#");
            for (int i = 0; i < elementlist.length; i++) {
                if (!elementlist[i].equals("")) {
                    String[] element = elementlist[i].split("@");
                    QueryWrapper wrapper = new QueryWrapper();
                    wrapper.eq("ASSSTAFFID", element[0]);
                    TblAssessStaff assessStaff = this.iTblAssessStaffService.getOne(wrapper);
                    // TblAssesselement assesselement =
                    // assessStaff.getTblAssessMark().getTblAssesselement();
                    if (5 >= Double.parseDouble(element[1])) {
                        if (assessStaff.getStatus().equals(2)) {
                            return JsonBean.error("已经提交不能修改");
                        } else {
                            assessStaff.setAssdatetime(LocalDateTime.now());
                            assessStaff.setReason(element[2]);
                            assessStaff.setScore(new BigDecimal(element[1]));
                            assessStaff.setStatus(new BigDecimal(1));
                            // assessStaff.setExamination(element[3]);
                            this.iTblAssessStaffService.updateById(assessStaff);
                            return JsonBean.success();
                        }
                    }
                    return JsonBean.error("评分不能超过标准分！");
                }
            }
            return JsonBean.success();
        }
        return JsonBean.error("保存失败");
    }



    /**
     * 提交评分
     *
     * @param orgId
     * @param assId
     * @param
     * @return
     */
    @OperationLog(
            success = "评价评分-评价-全部提交成功",
            busType = "内控设置",
            fail = "评价评分-评价-全部提交失败",
            operationType = OperationType.ADD,
            subType = "评价评分"
    )
    @PostMapping(value = "/pjgl/sbumitProjCat")
    @Operation(summary = "评价评分-评价-全部提交")
    public  String sbumitProjCat(

            @Parameter(name="orgId",description="orgId",required=false) @RequestParam(value = "orgId", required = false) BigDecimal orgId,
            @Parameter(name="assId",description="assId",required=false) @RequestParam(value = "assId", required = false) BigDecimal assId,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return JsonBean.error("用户已失效");
        }
        List<TblAssessStaff> assessStaffs = this.pjpfService.checkSubmit(assId, orgId, user.getStaffid());
        if (assessStaffs.size() > 0) {
            return JsonBean.error("有空缺评分,不能全部提交");
        } else {
            return this.pjpfService.doSubmint(assId, orgId, user.getStaffid());
        }
    }



    /**
     * 评价评分添加附件
     *
     * @param
     * @param request
     * @param asseleid
     * @param
     * @return
     */
    @OperationLog(
            success = "评价评分-添加附件成功",
            busType = "内控设置",
            fail = "评价评分-添加附件失败",
            operationType = OperationType.UPLOAD,
            subType = "评价评分"
    )
    @PostMapping(value = "/pjgl/pjpf_upload")
    @Operation(summary = "评价评分-添加附件")
    public  JsonBean  pjpf_upload(
            HttpServletRequest request,
            @Parameter(name="asseleid",description="ASSSTAFFID",required=false) @RequestParam(value = "asseleid", required = false) String asseleid ,
            @Parameter(name = "file", description = "文件上传流实体", required = true) MultipartFile[] file,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil user = userProvider.get();
    	if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> mv = new LinkedHashMap<>();
            try {
            	List<FileUploadRes> res=iTblAttachmentService.fileUpload(file,true,null,null);
            	if (asseleid != null && asseleid.trim().length() > 0) {
            		if(res.size()>0){
                	 QueryWrapper wrapper = new QueryWrapper();
                     wrapper.eq("ASSSTAFFID", asseleid);
                    TblAssessStaff assessStaff = iTblAssessStaffService.getOne(wrapper);
                    assessStaff.setAttid(new BigDecimal(res.get(0).getAttid()));
                    iTblAssessStaffService.updateById(assessStaff);
                }
            	}
            	 mv.put("res",res.get(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        return new JsonBean(200, "上传成功", mv);
         
     

    }


    /**
     * 评价评分-删除附件
     *
     * @param
     * @param asseleid
     * @return
     */
    @OperationLog(
            success = "评价评分-删除附件成功",
            busType = "内控设置",
            fail = "评价评分-删除附件失败",
            operationType = OperationType.DELETE,
            subType = "评价评分"
    )
    @Operation(summary = "评价评分-删除附件")
    @PostMapping(value = "/pjgl/pjpf_fj_del")
    public @ResponseBody String pjpf_fj_del(
            @Parameter(name="asseleid",description="ASSSTAFFID",required=false) @RequestParam(value = "asseleid", required = false) String asseleid ,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="attid",description="attid",required=false) @RequestParam(value = "attid", required = false)String  attid
    ) throws Exception {

    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return JsonBean.error("用户已失效");
        }
        log.info("评价评分---删除附件");
        /*
         * String assid=request.getParameter("assid"); String
         * nodeId=request.getParameter("nodeId"); String
         * orgId=request.getParameter("orgId");
         */
        TblAttachment attachment = new TblAttachment();
        if (StringUtils.isNotEmpty(asseleid)) {
//            LambdaUpdateWrapper<TblAssessStaff> wrapper = new LambdaUpdateWrapper<>();
//
//            wrapper.set(TblAssessStaff::getAttid, null);
//
//            wrapper.eq(TblAssessStaff::getAssstaffid, new BigDecimal(asseleid));

            //TblAssessStaff assessStaff =iTblAssessStaffService.getById(new BigDecimal(asseleid));
            //assessStaff.setAttid(null);
            iTblAssessStaffService.updateAttidNullById( new BigDecimal(asseleid));
        }
        if (StringUtils.isNotEmpty(attid)) {
//        	 QueryWrapper wrapper = new QueryWrapper();
//             wrapper.eq("ATTID", attid);
//            attachment = iTblAttachmentService.getOne(wrapper);
            iTblAttachmentService.delEntity(new BigDecimal(attid));
//            if(attachment!=null){
//                FtpUtil.removeFile(attachment.getAttpath());
//            }
            return asseleid;
        }
        // return
        // "redirect:/nbkz/pjgl/t08_proj_cat_list1?nodeId="+nodeId+"&assId="+assid+"&orgId="+orgId;
        return "";
    }


    @OperationLog(
            success = "评价评分-评价-导出excel成功",
            busType = "内控设置",
            fail = "评价评分-评价-导出excel失败",
            operationType = OperationType.EXPORT,
            subType = "评价评分"
    )
    @GetMapping(value = "/pjgl/pfjg_export")
    @Operation(summary = "评价评分-评价-导出excel")
    public  String pfjg_export(
            @Parameter(name="orgId",description="orgId",required=false) @RequestParam(value = "orgId",required = false) String     orgId,
            @Parameter(name="assid",description="assid") @RequestParam(value = "assid")String     assid,
            @Parameter(name="nodeId",description="左侧树节点id") @RequestParam(value = "nodeId")String     nodeId,
            @Parameter(name = "token", description = "登录用户token") @RequestParam(value = "token") String token,
            HttpServletResponse response
    ) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return JsonBean.error("用户已失效");
        }
        if (StringUtils.isBlank(orgId)) {

            orgId = user.getCurrentOrg().getOrgid().toString();
        }

        log.info("内控合规---评价管理---评价评分---导出Excel");
        response.setContentType("application/octet-stream;charset=UTF-8");
        try {
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String("评价评分".getBytes(), "iso-8859-1") + ".xlsx");
            ServletOutputStream outputStream = response.getOutputStream();
            List<Object[]> objList = this.pjpfService.pjpfExport(new BigDecimal(nodeId),
                    new BigDecimal(assid),user.getStaffid() , new BigDecimal(orgId));
            String[] titles = { "要素编号", "要素名称", "标准分", "审查要点", "评价分", "评价依据", "附件名称" };
            ImportOrExportExcelUtil.exportExcel(titles, objList, outputStream, null);
        } catch (Exception e) {
            log.info("内控合规---评价管理---评价评分---导出Excel失败");
        }
        return null;
    }
    
    
   
    
    /**
     * 文件下载
     */
    @GetMapping(value = "/download")
    @Operation(summary = "文件下载接口")
    public void fileDownLoad(HttpServletResponse response,
                             @Parameter(name = "fileId", description = "文件ID", required = true) @RequestParam("fileId") String fileId)throws Exception {
    	iTblAttachmentService.fileDownLoad(response, fileId,false);
    }

    
}
