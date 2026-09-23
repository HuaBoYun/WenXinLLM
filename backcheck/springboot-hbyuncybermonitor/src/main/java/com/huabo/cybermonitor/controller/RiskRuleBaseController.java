package com.huabo.cybermonitor.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.cybermonitor.entity.Attachment;
import com.huabo.cybermonitor.entity.TblOtherarticle;
import com.huabo.cybermonitor.entity.TblTesttaskAtt;
import com.huabo.cybermonitor.service.IAttachmentService;
import com.huabo.cybermonitor.service.ITblOtherarticleService;
import com.huabo.cybermonitor.service.ITblTesttaskAttService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@Tag(name="行业知识库",description="行业知识库")
@RequestMapping(value = "/cyber/RiskRuleBaseController")
public class RiskRuleBaseController {

	private static final Logger log = LoggerFactory.getLogger(RiskRuleBaseController.class);

    @Autowired
    ITblOtherarticleService iTblOtherarticleService;

    @Autowired
    IAttachmentService attachmentService;

    @Resource
    ITblTesttaskAttService iTblTesttaskAttService;
    
    @Resource
    private UserProvider userProvider;

    /**
     *行业知识库 -- 新增
     * @param tblOtherarticle
     * @param data
     * @param attids
     * @param token
     * @return
     * @throws Exception
     */
    @Operation(summary = "rule_base_add")
    @PostMapping(value = "/rule/rule_base_add")
    public JsonBean rule_base_add(@Parameter(name = "tblOtherarticle", description = "tblOtherarticle") @RequestBody TblOtherarticle tblOtherarticle,
                                  @Parameter(name = "data", description = "data") @RequestParam(value = "data") String data,
                                  @Parameter(name = "attids", description = "attids") @RequestParam(value = "attids") String attids,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        //判断是否有token,token是否是正确的
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotEmpty(data)) {
            try {
                tblOtherarticle.setPublishtime(sdf.parse(data));
            } catch (Exception e) {

            }
        }
        if (StringUtils.isNotBlank(attids)){
            String[] ids = attids.split(",");
            for (int i = 0; i < ids.length; i++) {
                Attachment att = attachmentService.getById(ids[i].trim());
                tblOtherarticle.getTblAttachments().add(att);
            }
        }
        iTblOtherarticleService.save(tblOtherarticle);
        return new JsonBean(200, "成功", "");
    }


    /**
     * 行业知识库 --列表
     * @param pageNumber
     * @param pageSize
     * @param articletitle
     * @param aruticleauther
     * @param modelType
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
            @Parameter(name = "articletitle", description = "articletitle") @RequestParam(value = "articletitle") String articletitle,
            @Parameter(name = "aruticleauther", description = "aruticleauther") @RequestParam(value = "aruticleauther") String aruticleauther,
            @Parameter(name = "modelType", description = "modelType") @RequestParam(value = "modelType") String modelType,
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
            IPage<TblOtherarticle> iPage = new Page<>(pageNumber, pageSize);
            QueryWrapper<TblOtherarticle> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ORGID", orgId);
            queryWrapper.eq("modelType", modelType);
            if (StringUtils.isNotBlank(articletitle)) {
                queryWrapper.like("articletitle", "%" + articletitle + "%");
            }
            if (StringUtils.isNotBlank(aruticleauther)) {
                queryWrapper.like("aruticleauther", "%" + aruticleauther + "%");
            }
            map.put("pageBean", iTblOtherarticleService.page(iPage, queryWrapper));

        } else {
            if (null == orgId) {
                //如果orgid为空这为他赋值
                orgId = staff.getCurrentOrg().getOrgid();
            }
            IPage<TblOtherarticle> iPage = new Page<>(pageNumber, pageSize);
            QueryWrapper<TblOtherarticle> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ORGID", orgId);
            queryWrapper.eq("modelType", modelType);
            if (StringUtils.isNotBlank(articletitle)) {
                queryWrapper.like("articletitle", "%" + articletitle + "%");
            }
            if (StringUtils.isNotBlank(aruticleauther)) {
                queryWrapper.like("aruticleauther", "%" + aruticleauther + "%");
            }
            queryWrapper.orderByDesc("ruleid");

            map.put("pageBean", iTblOtherarticleService.page(iPage, queryWrapper));
        }
        map.put("orgId", orgId);
        map.put("isAdd", isSelect);
        map.put("isDU", isDU);
        map.put("articletitle", articletitle);
        map.put("aruticleauther", aruticleauther);
        return new JsonBean(200, "成功", map);
    }

    /**
     * 行业知识库 ---跳转修改
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
        TblOtherarticle tblOtherarticle = iTblOtherarticleService.getById(selectedruleid);

        Map map = new HashMap();
        BigDecimal orgId = staff.getLinkDetp().getOrgid();
        map.put("orgId", orgId);
        map.put("orgName", staff.getLinkDetp().getOrgname());
        if (tblOtherarticle != null) {
            map.put("tblOtherarticle", tblOtherarticle);
        }
        return new JsonBean(200, "成功", map);
    }

    @Operation(summary = "showView")
    @GetMapping(value = "/rule/showView")
    public JsonBean showView(@Parameter(name = "selectedruleid", description = "selectedruleid") @RequestParam("selectedruleid") String selectedruleid,
                             @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam("choiceSearch") String choiceSearch,
                             @Parameter(name = "modelType", description = "modelType") @RequestParam("modelType") String modelType,
                             @Parameter(name = "orgId", description = "orgId") @RequestParam("orgId") String orgId,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        TblOtherarticle otherarticle = iTblOtherarticleService.getById(selectedruleid);
        Map map = new HashMap();
        map.put("otherarticle", otherarticle);
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        map.put("ty", modelType);
        map.put("orgId", orgId);
        return new JsonBean(200, "成功", map);
    }

    @Operation(summary = "toAddhyzsk")
    @GetMapping(value = "/rule/toAddhyzsk")
    public JsonBean toAddhyzsk(@Parameter(name = "selectedruleid", description = "selectedruleid") @RequestParam("selectedruleid") String selectedruleid,
                               @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam("choiceSearch") String choiceSearch,
                               @Parameter(name = "ty", description = "ty") @RequestParam("ty") String ty,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        TblOtherarticle tblOtherarticle = iTblOtherarticleService.getById(selectedruleid);
        List<Attachment> list = attachmentService.findTblAttachmentByOthartid(selectedruleid);
        Map map = new HashMap();
        BigDecimal orgId = staff.getLinkDetp().getOrgid();
        map.put("orgId", orgId);
        map.put("orgName", staff.getLinkDetp().getOrgname());
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch",choiceSearch);
        if (tblOtherarticle != null) {
            map.put("tblOtherarticle", tblOtherarticle);
            map.put("attachments",list);
        }
        map.put("ty",ty);
        return new JsonBean(200, "成功", map);
    }


    @Transactional
    @Operation(summary = "ruleModify")
    @PostMapping(value = "/rule/rule_modify")
    //solutionrules没有什么用
    public JsonBean ruleModify(@Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid") String orgid,
                               @Parameter(name = "othartid", description = "othartid") @RequestParam(value = "othartid") String othartid,
                               @Parameter(name = "articletitle", description = "articletitle") @RequestParam(value = "articletitle") String articletitle,
                               @Parameter(name = "articlestatus", description = "articlestatus") @RequestParam(value = "articlestatus") String articlestatus,
                               @Parameter(name = "aruticleauther", description = "aruticleauther") @RequestParam(value = "aruticleauther") String aruticleauther,
                               @Parameter(name = "articlebody", description = "articlebody") @RequestParam(value = "articlebody") String articlebody,
                               @Parameter(name = "modeltype", description = "modeltype") @RequestParam(value = "modeltype") String modeltype,
                               @Parameter(name = "startdate", description = "startdate") @RequestParam(value = "startdate") String startdate,
                               @Parameter(name = "attids", description = "attids") @RequestParam(value = "attids") String attids,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        TblOtherarticle  tblOtherarticle = iTblOtherarticleService.getById(othartid);
        tblOtherarticle.setArticletitle(articletitle);
        tblOtherarticle.setArticlestatus(articlestatus);
        tblOtherarticle.setAruticleauther(aruticleauther);
        tblOtherarticle.setArticlebody(articlebody);
        tblOtherarticle.setModeltype(modeltype);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isNotEmpty(startdate)) {
            try {
                tblOtherarticle.setPublishtime(sdf.parse(startdate));
            } catch (Exception e) {
            }
        }
        if (StringUtils.isNotBlank(attids)){
            String[] ids = attids.split(",");
            for (int i = 0; i < ids.length; i++) {
                Attachment att = attachmentService.getById(ids[i].trim());
                tblOtherarticle.getTblAttachments().add(att);
            }
        }
        /***
         othartid:801083
         articletitle:123123123
         articlestatus:草稿
         aruticleauther:张孝昆
         memo: 12312312333333
         orgid:112465
         articlebody:<p>1231212312312312312</p>
         modeltype:  znjk
         tblOrganization.orgid: 112465
         ty:znjk
         choiceSearch:hide
         startdate:2022-12-04
         attids:
         */
        Map map = new HashMap();
        try {
            iTblOtherarticleService.updateById(tblOtherarticle);
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
    public JsonBean ruleDel(@Parameter(name = "selectedruleids", description = "selectedruleids") @RequestBody String[] selectedruleids,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        if (selectedruleids != null) {
            for (int i = 0; i < selectedruleids.length; i++) {
                TblOtherarticle tblOtherarticle = iTblOtherarticleService.getById(selectedruleids[i]);
                if (Objects.isNull(tblOtherarticle)) {
                    return new JsonBean(500, "通过" + tblOtherarticle + "没有找到知识文章！！", null);
                }
                try {
                    iTblOtherarticleService.removeById(tblOtherarticle);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new JsonBean(500, e.getMessage(), "");
                }
            }
            return new JsonBean(200, "成功", map);
        }
        return new JsonBean(500, "请选择知识库id", null);
    }


    @Operation(summary = "知识库-附件保存")
    @PostMapping(value = "/rule/control_test_impl_upload")
    public JsonBean control_test_impl_upload(
            @Parameter(name="projectid",description="testtaskid") @RequestParam(value = "projectid")String projectid,
            @Parameter(name = "file", description = "附件上传entity", required = true) MultipartFile file,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        String fileName = file.getOriginalFilename();
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        String oldname = fileName.substring(0, fileName.lastIndexOf("."));
        String newname = fileName.replace(oldname, "00"+timeInMillis + "csrw");
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
        Attachment a = new Attachment();
        a.setAttname(fileName);
        a.setAttpath(newname);
        a.setAttsize(new BigDecimal(size / 1024));
        a.setUploader(staff.getUsername());
        a.setUploadtime(LocalDateTime.now());
        attachmentService.save(a);

        TblTesttaskAtt tblTesttaskAtt=new TblTesttaskAtt();
        tblTesttaskAtt.setAttid(a.getAttid());
        tblTesttaskAtt.setTesttaskid(new BigDecimal(projectid));
        iTblTesttaskAttService .save(tblTesttaskAtt);
        Map<String, Object> mv = new LinkedHashMap<>();
        mv.put("att",a);
        mv.put("taskAtt",tblTesttaskAtt);
        return new JsonBean(200, "success", mv);
    }

    @Operation(summary = "知识库-附件删除")
    @PostMapping(value = "/rule/control_test_impl_upload_del")
    public JsonBean control_test_impl_upload_del(
            @Parameter(name = "attid", description = "attid") @RequestParam(value = "attid")BigDecimal attid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        if (attid != null ) {
            attachmentService.removeById(attid);
        }
        return new JsonBean(200, "success", null);
    }



}
