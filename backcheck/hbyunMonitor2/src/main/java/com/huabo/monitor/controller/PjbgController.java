package com.huabo.monitor.controller;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.mibiao.SecretLabel;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.entity.TblNbsjBugEntity;
import com.huabo.monitor.entity.TblReport;
import com.huabo.monitor.entity.TblReportBug;
import com.huabo.monitor.service.ITblAttachmentService;
import com.huabo.monitor.service.ITblReportBugService;
import com.huabo.monitor.service.ITblReportService;
import com.huabo.monitor.service.PjbgService;
import com.huabo.monitor.util.DateUtils;
import com.huabo.monitor.util.ExportDoc;
import com.huabo.monitor.util.FiexibleNameAssignment;
import com.huabo.monitor.util.FileUtil;
import com.huabo.monitor.util.IPageResult;
import com.huabo.monitor.vo.param.fieldActivationVo;
import com.huabo.monitor.vo.param.fieldOrgStaffId;
import com.huabo.monitor.vo.param.fieldOrgStaffName;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Tag(name="内控合规报告-评价报告",description="内控合规报告-评价报告")
@RequestMapping(value = "/nbkz/pjbg")
public class PjbgController {

    @Resource
    PjbgService  pjbgService;

    @Resource
    ITblAttachmentService iTblAttachmentService;

    @Resource
    ITblReportService iTblReportService;
    
    @Resource
    ITblReportBugService tblReportBugService;
    
    @Resource
    private UserProvider userProvider;

    @Autowired
    SecretLabel secretLabel;

    String type="nk";

 //   @Value("${filedir}")
    String filedir;

	@Value("${application.administrators:}")
	private String administrators;


   /*
    public JsonBean listnkbg(
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @Parameter(name="type",description="报告类型默认nk",required=false) @RequestParam(value = "type", required = false, defaultValue = "nk") String type,
            @Parameter(name = "name", description = "报告名称") @RequestParam(value = "name", required = false)String name,
            @Parameter(name = "startDate", description = "开始日期") @RequestParam(value = "startDate", required = false)String startDate,
            @Parameter(name = "endDate", description = "结束日期") @RequestParam(value = "endDate", required = false)String endDate,
            @Parameter(name = "fication", description = "fication") @RequestParam(value = "fication", required = false) String fication,
            @Parameter(name = "view", description = "view") @RequestParam(value = "view", required = false) String view,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        if (!ConstClass.checkToken(token)) {
            return ConstClass.tokenFailure();
        }
        TblStaffUtil user = DealUserToken.parseUserToken(token);
        IPage<TblReport> page = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
        page = pjbgService.findAll(pageNumber,name,startDate,endDate, type, user.getCurrentOrg().getOrgid());

        Map<String, Object> mv = new LinkedHashMap<>();
        mv.put("type", type);
        mv.put("name", name);
        mv.put("startDate", startDate);
        mv.put("endDate", endDate);
        mv.put("fication", fication);
        mv.put("view", view);
        mv.put("pageBean", page);

        return new JsonBean(200, "success", mv);

    }
*/

    @OperationLog(
            success = "评价报告-主页查询成功",
            busType = "内控设置",
            fail = "评价报告-主页查询失败",
            operationType = OperationType.SELECT,
            subType = "评价报告"
    )
    @Operation(summary = "评价报告-主页")
    @GetMapping(value = "/nkbg/list")

    public JsonBean listnkbg(
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @Parameter(name = "name", description = "报告名称") @RequestParam(value = "name", required = false)String name,
            @Parameter(name = "startDate", description = "开始日期") @RequestParam(value = "startDate", required = false)String startDate,
            @Parameter(name = "endDate", description = "结束日期") @RequestParam(value = "endDate", required = false)String endDate,
            @Parameter(name = "year", description = "年度") @RequestParam(value = "year", required = false)String year,
            @Parameter(name = "reporttype", description = "报告类型") @RequestParam(value = "reporttype", required = false)String reporttype,
            @Parameter(name = "orgid", description = "报告单位") @RequestParam(value = "orgid", required = false)BigDecimal orgid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		Integer authorityType;
		if (JudgeRoleRight.judgeRoleRight(administrators, user.getRoleNames())) {
			authorityType = 1;
		} else {
			authorityType = 0;
		}
		
		BigDecimal orgidParam = user.getCurrentOrg().getOrgid();
		if(orgid != null) {
			orgidParam = orgid;
		}
		
       // IPage<TblReport> page = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
       // page = pjbgService.findAll(pageNumber,name,startDate,endDate, type, user.getCurrentOrg().getOrgid(),user.getStaffid(),authorityType);
       PageInfo<TblReport> page=pjbgService.findAllNewPage(pageNumber, name, startDate, endDate, type, orgidParam,
    		   user.getStaffid(), authorityType,user,year,reporttype);
       IPageResult<TblReport> pageInfo=new IPageResult<TblReport>().buildIpage(page);

       Map<String, Object> mv = new LinkedHashMap<>();

        mv.put("name", name);
        mv.put("startDate", startDate);
        mv.put("endDate", endDate);

        mv.put("pageBean", pageInfo);

        return new JsonBean(200, "success", mv);

    }
    @OperationLog(
            success = "评价/自定义报告-添加页面-附件上传成功",
            busType = "内控设置",
            fail = "评价/自定义报告-添加页面-附件上传失败",
            operationType = OperationType.UPLOAD,
            subType = "评价报告"
    )
    @PostMapping(value = "/addupload")
    @Operation(summary = "评价/自定义报告-添加页面-附件上传")
    public  JsonBean pjpf_upload(
            HttpServletRequest request,

            @Parameter(name = "file", description = "附件上传entity", required = true) MultipartFile file,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "formlevel", description = "表单密级信息 不为空则启动密标，为空则 不启动密标") String formlevel
    ) throws Exception {

    	TblStaffUtil user = userProvider.get();
        TblAttachment a = new TblAttachment();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        if(formlevel!=null && !"".equals(formlevel)) {
            BigDecimal attid = null;
            Integer result = secretLabel.test(file);
//            Integer result = 5;
            System.out.println("是否是密标文件：" + result);
            if (result == 0) {
                log.info("文件上传失败，包含非密标文件");
                return  new JsonBean(500, "包含非密标文件！", null);
            } else {
                //判断文件名是否包含密级信息
                String str = file.getOriginalFilename();
//                int index = file.getOriginalFilename().lastIndexOf(".");
                if(str.length()>=4){
                    int index1 = str.indexOf("[");
                    int index2 = str.indexOf("]");
                    if(index1 !=-1 && index2 !=-1){
                        String secretFlag = str.substring(index1+1, index2);
                        System.out.println(secretFlag);
                        String AttachmentLevel = secretLabel.secrectLabelInfo(file);
//                        String AttachmentLevel = "机密";
                        if (AttachmentLevel != null && !AttachmentLevel.equals("")){
                            System.out.println("密级级别："+AttachmentLevel);
                            System.out.println("密级级别："+str.substring(index1+1, index2));
                            if (!AttachmentLevel.equals(str.substring(index1+1, index2))){
                                return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
                            }
                        }
                    }else {
                        log.info("文件上传失败，文件名不包含正确密级信息！");
                        return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
                    }
                   /* String strs = "[非密][商密][秘密][机密][公开][内部][敏感][敏感信息][普通商密][核心商密]";
                    if(strs.indexOf(secretFlag)==-1){
                        log.info("文件上传失败，文件名不包含正确密级信息！");
                        return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
                    }*/
                }else {
                    log.info("文件上传失败，文件名不包含正确密级信息！");
                    return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
                }

                //获取密级级别
                String AttachmentLevel = secretLabel.secrectLabelInfo(file);
//                String AttachmentLevel = "机密";
                //根据密级信息查询密级ID
                BigDecimal attachmentLevelId = iTblAttachmentService.selectSecretLabel(AttachmentLevel);
                attid = attachmentLevelId;
                System.out.println(attid);
                a.setAttachmentlevel(attid);
                //根据表单密级信息，查询附件密级是否合理
                Integer count = iTblAttachmentService.getAttachmentList(formlevel, attachmentLevelId.toString());
                if (count == 0) {
                    log.info("文件上传失败，附件密级大于表单密级！");
                    return  new JsonBean(500, "文件上传失败，附件密级大于表单密级！", null);
                }
            }
        }

        String fileName = file.getOriginalFilename();
        System.out.println("fileName---"+fileName);
        Map<String, Object> mv = new LinkedHashMap<>();
        if (fileName != null && fileName != "") {
            long timeInMillis = Calendar.getInstance().getTimeInMillis();
            String oldname = fileName.substring(0, fileName.lastIndexOf("."));
            String newname = fileName.replace(oldname, "00"+timeInMillis);
            System.out.println(file.getInputStream());
            long size = file.getSize();
            try {
                boolean flag = FtpUtil.uploadFile(newname, file.getInputStream());
                if (flag) {
                    log.info("上传成功");
                } else {
                    log.info("上传失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            a.setAttname(fileName);
            a.setAttpath(newname);
            a.setAttsize(new BigDecimal(size / 1024));
            a.setUploader(user.getUsername());
            a.setUploadtime(LocalDateTime.now());
            iTblAttachmentService.save(a);

            mv.put("attid",a.getAttid());
            mv.put("Attachment",a);

        }
        return new JsonBean(200, "上传成功", mv);

    }

    @OperationLog(
            success = "评价/自定义报告-修改页面上传成功",
            busType = "内控设置",
            fail = "评价/自定义报告-修改页面上传失败",
            operationType = OperationType.UPLOAD,
            subType = "评价报告"
    )
    @PostMapping(value = "/updateupload")
    @Operation(summary = "评价/自定义报告-修改页面(附件单独操作直接对应数据,保存修改时不修改附件)-附件上传；保存决策附件saveDecisionDocuments")
    public  JsonBean pjpf_updateupload(
            HttpServletRequest request,
            @Parameter(name = "isDecision", description = "是否为决策文件", required = false) @RequestParam(value = "isDecision", required = false) String isDecision,
            @Parameter(name = "reportid", description = "报告reportid") @RequestParam(value = "reportid") String reportid,
            @Parameter(name = "file", description = "附件上传entity", required = true) MultipartFile file,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        String fileName = file.getOriginalFilename();
        System.out.println("fileName---"+fileName);
        Map<String, Object> mv = new LinkedHashMap<>();
        if (fileName != null && fileName != "") {
            long timeInMillis = Calendar.getInstance().getTimeInMillis();
            String oldname = fileName.substring(0, fileName.lastIndexOf("."));
            String newname = fileName.replace(oldname, "00"+timeInMillis);
            System.out.println(file.getInputStream());
            long size = file.getSize();
            try {
                boolean flag = FtpUtil.uploadFile(newname, file.getInputStream());
                if (flag) {
                    log.info("上传成功");
                } else {
                    log.info("上传失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            TblAttachment a = new TblAttachment();
            a.setAttname(fileName);
            a.setAttpath(newname);
            a.setAttsize(new BigDecimal(size / 1024));
            a.setUploader(user.getUsername());
            a.setUploadtime(LocalDateTime.now());
            //iTblAttachmentService.save(a);
            // 添加附件 和  中间表
            this.pjbgService.saveRepAtt(a,reportid,isDecision);

            mv.put("attid",a.getAttid());
            mv.put("reportid",reportid);
            mv.put("Attachment",a);

        }
        return new JsonBean(200, "上传成功并加入关系表", mv);

    }




    /**
     * 如果报告名称有重复的返回1, 并且弹出"报告已经存在",并且不保存重复报告的信息
     *
     * @param
     * @return
     */
    @OperationLog(
            success = "评价/自定义报告-添加页面-保存(code:-1 报告名存在,code:200 成功)成功",
            busType = "内控设置",
            fail = "评价/自定义报告-添加页面-保存(code:-1 报告名存在,code:200 成功)失败",
            operationType = OperationType.ADD,
            subType = "评价报告"
    )
    @PostMapping(value = "/isExistAdd")
    @Operation(summary = "评价/自定义报告-添加页面-保存(code:-1 报告名存在,code:200 成功)")
    public JsonBean isAddExistByname(
            @Parameter(name = "type", description = "报告类型:评价=nk,自定义=nk_zdy") @RequestParam(value = "type") String type,
            @Parameter(name = "reportname", description = "报告名称") @RequestParam(value = "reportname")String reportname,
            @Parameter(name = "startdate", description = "报告日期") @RequestParam(value = "startdate")String startdate,
            @Parameter(name = "attids", description = "附件ids-多值逗号分割") @RequestParam(value = "attids", required = false)String attids,
            @Parameter(name = "reporttype", description = "报告类型") @RequestParam(value = "reporttype")String reporttype,
            @Parameter(name = "reportmode", description = "报告方式") @RequestParam(value = "reportmode")String reportmode,
            @Parameter(name = "reporter", description = "报告人realname") @RequestParam(value = "reporter")String reporter,
           // @Parameter(name = "oid", description = "报告部门id") @RequestParam(value = "oid")String  oid,
            @Parameter(name = "reportdepartment", description = "报告部门名称") @RequestParam(value = "reportdepartment")String  reportdepartment,
            @Parameter(name = "secrectLevelId", description = "密级主键") @RequestParam(value = "secrectLevelId", required = false)BigDecimal secrectLevelId,
            @Parameter(name = "staffScopeNames", description = "知悉范围名称") @RequestParam(value = "staffScopeNames", required = false)String staffScopeNames,
            @Parameter(name = "staffScopeIds", description = "知悉范围id") @RequestParam(value = "staffScopeIds", required = false)String staffScopeIds,
            @Parameter(name = "bugIds", description = "选中的缺陷id使用,分割", required = false) @RequestParam(required = false) String bugIds,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody fieldActivationVo  desc) throws Exception {

    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        LocalDateTime  lt=DateUtils.StringToLocalDateTime(startdate,"yyyy-MM-dd");
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        QueryWrapper<TblReport> qw=new QueryWrapper<>();
        qw.eq("reportname",reportname);
        qw.eq("reporttime", lt);
        long count = this.iTblReportService.count(qw);
        Integer existByNum = 0;

        if (count > 0) {
            return new JsonBean(0, "报告已存在",null );
        } else {
            Map<String, Object> mv = new LinkedHashMap<>();
            TblReport report=new TblReport();
            report.setType(type);
            report.setReportname(reportname);
            report.setReporttime(sdf.parse(startdate));
            report.setReporttype(reporttype);
            report.setReportmode(reportmode);
            report.setReporter(reporter);
            report.setReportdepartment(reportdepartment);
            report.setRepdesc(desc.getDesc());
            report.setCreatestaffid(user.getStaffid());
            report.setOrgid(user.getLinkOrg().getOrgid());
            report.setSecrectLevelId(secrectLevelId);
            report.setStaffScopeIds(staffScopeIds);
            report.setStaffScopeNames(staffScopeNames);
            report.setLinkDeptId(user.getLinkDetp().getOrgid());
            report.setCreatetime(new Date());
            if (desc != null) {
            	report.setFieldActivationCopy(desc);
            }
            this.pjbgService.saveReport(report,attids);
            if (StringUtils.isNotBlank(bugIds) && null != report.getReportid()) {
            	String[] ids = bugIds.split(",");
                    for (String otherid : ids) {
                        QueryWrapper queryWrapper = new QueryWrapper();
                        queryWrapper.eq("REPORTID", report.getReportid());
                        queryWrapper.eq("BUGID", otherid);
                        if (tblReportBugService.count(queryWrapper) == 0) {
                            TblReportBug entity = new TblReportBug();
                            entity.setBugid(new BigDecimal(otherid));
                            entity.setReportid(report.getReportid());
                            tblReportBugService.saveEntity(entity);
                        }
                }
            }
            mv.put("reportid",report.getReportid());
            mv.put("report",report);

            List<TblAttachment> list=pjbgService.getRepAttByReportId(report.getReportid());
            mv.put("atts",list);
            return new JsonBean(200, "保存成功", mv);
        }


    }


    @OperationLog(
            success = "评价/自定义报告-点击修改/详情查询成功",
            busType = "内控设置",
            fail = "评价/自定义报告-点击修改/详情查询失败",
            operationType = OperationType.SELECT,
            subType = "评价报告"
    )
    @Operation(summary = "评价/自定义报告-点击修改/详情查询")
    @GetMapping(value = "/nkbg/modify")
    public JsonBean modifynkbg(
            @Parameter(name = "id", description = "reportid") @RequestParam(value = "id") String id,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
          TblReport report = this.iTblReportService.getById(id);
        if(report==null){
            return new JsonBean(-1, "数据不存在,请正确传值", "");
        }
        FiexibleNameAssignment ment=new FiexibleNameAssignment();
		// 对灵活字段中的姓名名称及机构名称赋值
		fieldOrgStaffId item = new fieldOrgStaffId();
		BeanUtils.copyProperties(report, item);
		fieldOrgStaffName nameEntity = ment.setOpenName(item);
		BeanUtils.copyProperties(nameEntity, report);
        List<TblAttachment> list=pjbgService.getRepAttByReportId(report.getReportid());
        //关联内控评价缺陷
        List<TblNbsjBugEntity> listPjqx=pjbgService.selectNbsjBugList(report.getReportid());
		if(CollectionUtils.isNotEmpty(listPjqx)){
			listPjqx.forEach(entity->{
				try {
					//对灵活字段中的姓名名称及机构名称赋值
					fieldOrgStaffId	 item1=new fieldOrgStaffId();
					BeanUtils.copyProperties(entity,item1); 
					fieldOrgStaffName  nameEntity1=ment.setOpenName(item1);
					BeanUtils.copyProperties(nameEntity1,entity ); 
					item1=null; // 处理并解除引用
					nameEntity1=null; // 处理并解除引用
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			} );
			
		}
        Map<String, Object> mv = new LinkedHashMap<>();
        mv.put("report",report);
        mv.put("atts",list);
        mv.put("listPjqx",listPjqx);
        return new JsonBean(200, "success", mv);
    }


    @OperationLog(
            success = "评价/自定义报告-添加页面-附件删除成功",
            busType = "内控设置",
            fail = "评价/自定义报告-添加页面-附件删除失败",
            operationType = OperationType.DELETE,
            subType = "评价报告"
    )
    @Operation(summary = "评价/自定义报告-添加页面-附件删除")
    @PostMapping(value = "/add/sp_del_fj")
    public JsonBean addsp_del_fj(
            @Parameter(name = "attid", description = "单个附件id") @RequestParam(value = "attid") BigDecimal attid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        this.iTblAttachmentService.delEntity(attid);
        return new JsonBean(200, "success", "删除成功");
    }


    @OperationLog(
            success = "评价/自定义报告-修改页面(附件单独操作直接对应数据,保存修改时不修改附件)-附件删除成功",
            busType = "内控设置",
            fail = "评价/自定义报告-修改页面(附件单独操作直接对应数据,保存修改时不修改附件)-附件删除失败",
            operationType = OperationType.DELETE,
            subType = "评价报告"
    )
    @Operation(summary = "评价/自定义报告-修改页面(附件单独操作直接对应数据,保存修改时不修改附件)-附件删除")
    @PostMapping(value = "/update/sp_del_fj")
    public JsonBean updatesp_del_fj(

            @Parameter(name = "attid", description = "单个附件id") @RequestParam(value = "attid") BigDecimal attid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        this.pjbgService.delAttAndRepAtt(attid,null);
        return new JsonBean(200, "success", "删除附件成功");
    }

    /**
     * 如果报告名称有重复的返回1, 并且弹出"报告已经存在",并且不保存重复报告的信息
     *
     * @param
     * @return
     */
    @OperationLog(
            success = "评价/自定义报告-修改页面-保存成功",
            busType = "内控设置",
            fail = "评价/自定义报告-修改页面-保存失败",
            operationType = OperationType.ADD,
            subType = "评价报告"
    )
    @PostMapping(value = "/isExistUpdate")
    @Operation(summary = "评价/自定义报告-修改页面-保存(code:-1 报告名存在,code:200 成功,修改时不传附件信息)")
    public JsonBean isUpdateExistByname(
            @Parameter(name = "reportid", description = "reportid") @RequestParam(value = "reportid") BigDecimal reportid,
            @Parameter(name = "attids", description = "附件ids-多值逗号分割") @RequestParam(value = "attids", required = false)String attids,
            @Parameter(name = "type", description = "报告类型:评价=nk,自定义=nk_zdy") @RequestParam(value = "type") String type,
            @Parameter(name = "reportname", description = "报告名称") @RequestParam(value = "reportname")String reportname,
            @Parameter(name = "startdate", description = "报告日期") @RequestParam(value = "startdate")String startdate,
            @Parameter(name = "reporttype", description = "报告类型") @RequestParam(value = "reporttype")String reporttype,
            @Parameter(name = "reportmode", description = "报告方式") @RequestParam(value = "reportmode")String reportmode,
            @Parameter(name = "reporter", description = "报告人realname") @RequestParam(value = "reporter")String reporter,
            // @Parameter(name = "oid", description = "报告部门id") @RequestParam(value = "oid")String  oid,
            @Parameter(name = "reportdepartment", description = "报告部门名称") @RequestParam(value = "reportdepartment")String  reportdepartment,
            @Parameter(name = "secrectLevelId", description = "密级主键") @RequestParam(value = "secrectLevelId", required = false)BigDecimal secrectLevelId,
            @Parameter(name = "staffScopeNames", description = "知悉范围名称") @RequestParam(value = "staffScopeNames", required = false)String staffScopeNames,
            @Parameter(name = "staffScopeIds", description = "知悉范围id") @RequestParam(value = "staffScopeIds", required = false)String staffScopeIds,
            @Parameter(name = "bugIds", description = "选中的缺陷id使用,分割", required = false) @RequestParam(required = false) String bugIds,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,@RequestBody fieldActivationVo desc
    ) throws Exception {

    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        LocalDateTime  lt=DateUtils.StringToLocalDateTime(startdate,"yyyy-MM-dd");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        QueryWrapper<TblReport> qw=new QueryWrapper<>();
        qw.eq("reportname",reportname);
        qw.eq("reporttime", lt);
        long count = this.iTblReportService.count(qw);
        Integer existByNum = 0;

            Map<String, Object> mv = new LinkedHashMap<>();
            TblReport report=this.iTblReportService.getById(reportid);
            report.setType(type);
            report.setReportname(reportname);
            report.setReporttime(sdf.parse(startdate));
            report.setReporttype(reporttype);
            report.setReportmode(reportmode);
            report.setReporter(reporter);
            report.setReportdepartment(reportdepartment);
            report.setRepdesc(desc.getDesc());
            report.setOrgid(user.getLinkOrg().getOrgid());
            report.setSecrectLevelId(secrectLevelId);
            report.setStaffScopeIds(staffScopeIds);
            report.setStaffScopeNames(staffScopeNames);
            if (desc != null) {
            	report.setFieldActivationCopy(desc);
            }
            if (StringUtils.isNotBlank(bugIds) && null != report.getReportid()) {
            	tblReportBugService.deleteByReportId(report.getReportid());
            	String[] ids = bugIds.split(",");
                    for (String otherid : ids) {
                        QueryWrapper queryWrapper = new QueryWrapper();
                        queryWrapper.eq("REPORTID", report.getReportid());
                        queryWrapper.eq("BUGID", otherid);
                        if (tblReportBugService.count(queryWrapper) == 0) {
                            TblReportBug entity = new TblReportBug();
                            entity.setBugid(new BigDecimal(otherid));
                            entity.setReportid(report.getReportid());
                            tblReportBugService.saveEntity(entity);
                        }
                }
            }
            this.pjbgService.saveReport(report,attids);
            mv.put("reportid",report.getReportid());
            mv.put("report",report);
            List<TblAttachment> list=pjbgService.getRepAttByReportId(report.getReportid());
            mv.put("atts",list);
            return new JsonBean(200, "保存成功", mv);
    }




    @OperationLog(
            success = "评价/自定义报告-导出word成功",
            busType = "内控设置",
            fail = "评价/自定义报告-导出word失败",
            operationType = OperationType.EXPORT,
            subType = "评价报告"
    )
    @Operation(summary = "评价/自定义报告-导出word")
    @GetMapping(value = "/sjss/expReportFile")
    public void expWordFile(
            HttpServletResponse response,
            @Parameter(name = "reportid", description = "reportid") @RequestParam(value = "reportid") BigDecimal reportid
    ) throws Exception  {
    	
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return;
        }

        TblReport report = this.iTblReportService.getById(reportid);
        String fileNameStr = report.getReportname()+".doc";
        String folderPath = this.filedir;
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        String fileName =  "/" + uuid + ".doc";
        System.out.println("fileName---" + fileName);
        ExportDoc exportDoc=new ExportDoc();
        Map dataMap=new HashMap();
        dataMap.put("repdesc",report.getRepdesc());
        if(report.getRepdesc()!=null&&StringUtils.isNotBlank(report.getRepdesc())){
        	 exportDoc.createDoc("static.ftl", fileName, dataMap);
             // 导出Word
             FileUtil.downLoad(fileName, response, false, fileNameStr);
             FileUtil.deleteFile(fileName);
        }

    }

    @OperationLog(
            success = "评价/自定义报告-删除成功",
            busType = "内控设置",
            fail = "评价/自定义报告-删除失败",
            operationType = OperationType.DELETE,
            subType = "评价报告"
    )
    @Operation(summary = "评价/自定义报告-删除")
    @PostMapping(value = "/nkbg/delete")
    public JsonBean deletenkbg(
            @Parameter(name = "ids", description = "报告ids-多值用逗号分割") @RequestParam(value = "ids") String ids,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        this.pjbgService.delReport(ids);

        return new JsonBean(200, "删除成功", "删除成功");
    }

    

    @OperationLog(
            success = "评价报告关联评价缺陷",
            busType = "内控设置",
            fail = "评价报告关联评价缺陷",
            operationType = OperationType.ADD,
            subType = "评价报告"
    )
    @RequestMapping(value = "/bugadd", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    @Operation(summary = "评价报告关联评价缺陷/nbkz/pjbg/bugadd")
    public JsonBean bugadd(@Parameter(name = "bugIds", description = "选中的缺陷id使用,分割", required = true) @RequestParam(required = true) String bugIds,
                          @Parameter(name = "reportId", description = "报告id", required = true) @RequestParam(required = true) BigDecimal reportId,
                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (StringUtils.isNotBlank(bugIds) && null != reportId) {
        	String[] ids = bugIds.split(",");
            TblReport report=this.iTblReportService.getById(reportId);
                for (String otherid : ids) {
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("REPORTID", reportId);
                    queryWrapper.eq("BUGID", otherid);
                    if (tblReportBugService.count(queryWrapper) == 0) {
                        TblReportBug entity = new TblReportBug();
                        entity.setBugid(new BigDecimal(otherid));
                        entity.setReportid(reportId);
                        tblReportBugService.saveEntity(entity);
                    }
            }
            return ResponseFormat.retParam(1, 200, null);
        }
        return ResponseFormat.retParam(0, "保存失败", null);
    }


    @OperationLog(
            success = "删除评价报告关联评价缺陷",
            busType = "内控设置",
            fail = "删除评价报告关联评价缺陷",
            operationType = OperationType.DELETE,
            subType = "评价报告"
    )
    @RequestMapping(value = "/deleteReportBug", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    @Operation(summary = "删除评价报告关联评价缺陷/nbkz/pjbg/deleteReportBug")
    public JsonBean deleteReportBug(@Parameter(name = "bugId", description = "选中的缺陷id", required = true) @RequestParam(required = true) BigDecimal bugId,
                          @Parameter(name = "reportId", description = "报告id", required = true) @RequestParam(required = true) BigDecimal reportId,
                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (null != (bugId) && null != reportId) {
         
           tblReportBugService.deleteReportBug(bugId,reportId);
            
            return ResponseFormat.retParam(1, 200, null);
        }
        return ResponseFormat.retParam(0, "保存失败", null);
    }

    
}
