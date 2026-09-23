package com.huabo.fxgl.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.SnowflakeIdWorker;
import com.hbfk.util.mibiao.SecretLabel;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Attachment;
import com.huabo.fxgl.service.IAttachmentService;
import com.huabo.fxgl.service.IBugAttService;
import com.huabo.fxgl.service.IRepAttService;
import com.huabo.fxgl.service.IRiskAssplanAttService;
import com.huabo.fxgl.service.IRiskeventAttService;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author LiYe
 * @since 2022-08-10
 * @since 2022-08-05
 */
@RestController
@RequestMapping(value = "/attachment", method = {RequestMethod.GET, RequestMethod.POST})
@Tag(name="附件API",description="附件API")
@Slf4j
public class AttachmentController {

    @Autowired
    private IAttachmentService attachmentService;
    @Autowired
    private IRiskAssplanAttService riskAssplanAttService;
    @Autowired
    private IRiskeventAttService riskeventAttService;
    @Autowired
    private IRepAttService repAttService;
    @Autowired
    private IBugAttService bugAttService;

    @Autowired(required = false)
    SecretLabel secretLabel;
    
    @Resource
    private UserProvider userProvider;

    private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(5, 5);


//    private final List<String> moduleList = Arrays.asList("PGJH,FXSJK,FXBG,ZDYBG,QXGL".split(","));

    
    @OperationLog(
			success = "【{{#moduleType}}】附件删除成功",
			busType = "附件操作",
			fail = "【{{#moduleType}}】附件删除失败",
			operationType = OperationType.DELETE,
			subType = "附件上传"
	)
    @RequestMapping(value = "/delete", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @Operation(summary = "删除附件接口")
    public JsonBean delete(HttpServletRequest request, HttpServletResponse response, Model map,
                                      @RequestParam(value = "type", required = false) Integer type,
                                      @Parameter(name="moduleType",description="模块类型",required=true) @RequestParam(name = "moduleType", required = false) String moduleType,
                                      @Parameter(name = "moduleId", description = "模块ID", required = true) @RequestParam(name = "moduleId", required = true) String moduleId,
                                      @Parameter(name = "attId", description = "附件ID", required = true) @RequestParam(name = "attId", required = true) String attId,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        String result = null;
        BigDecimal aid = null;

        JsonBean jsonBean = new JsonBean();

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(0, "用户已失效", null);
        }

        List<String> moduleList = Arrays.asList("PGJH,FXSJK,FXBG,ZDYBG,QXGL".split(","));
        if (StringUtils.isBlank(moduleType) || !moduleList.contains(moduleType)) {
            Map moduleMap = new HashMap();
            moduleMap.put("PGJH", "评估计划");
            moduleMap.put("FXSJK", "风险数据库");
            moduleMap.put("FXBG", "风险报告");
            moduleMap.put("ZDYBG", "自定义报告");
            moduleMap.put("QXGL", "缺陷管理");
            return new JsonBean(400, "参数错误：未提供正确的模块类别(moduleType)", moduleMap);
        }
        if (StringUtils.isBlank(attId)) {
            return new JsonBean(400, "参数错误：未提供正确的附件ID(attId)", null);
        }
        if (StringUtils.isBlank(moduleId)) {
            Map moduleMap = new HashMap();
            moduleMap.put("PGJH", "评估计划ID");
            moduleMap.put("FXSJK", "风险ID");
            moduleMap.put("FXBG", "风险报告ID");
            moduleMap.put("ZDYBG", "自定义报告ID");
            moduleMap.put("QXGL", "缺陷ID");
            return new JsonBean(400, "参数错误：未提供正确的模块ID(moduleId)", moduleMap);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("ATTID", attId);
        try {
            switch (moduleType) {
                case "PGJH": {
                    //删除风险计划映射的附件
                    queryWrapper.eq("ASSPLANID", moduleId);
                    riskAssplanAttService.remove(queryWrapper);
                    break;
                }
                case "FXSJK": {
                    //删除风险事件映射的附件
                    queryWrapper.eq("RISEVEID", moduleId);
                    riskeventAttService.remove(queryWrapper);
                    break;
                }
                case "FXCJ":
                case "ZDYBG": {
                    //删除风险报告映射的附件
                    queryWrapper.eq("REPORTID", moduleId);
                    repAttService.remove(queryWrapper);
                    break;
                }
                case "QXGL": {
                    //删除缺陷映射的附件
                    queryWrapper.eq("BUGID", moduleId);
                    bugAttService.remove(queryWrapper);
                    break;
                }
            }
            return new JsonBean(200, "删除附件成功！", null);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new JsonBean(500, "删除附件时出错！", ex.getMessage());
        }
    }

    @OperationLog(
			success = "【{{#moduleType}}】附件上传成功",
			busType = "附件操作",
			fail = "【{{#moduleType}}】附件上传失败",
			operationType = OperationType.UPLOAD,
			subType = "附件上传"
	)
    @RequestMapping(value = "/uploadFileAttInfo")
    @Operation(summary = "上传附件接口")
    public JsonBean uploadFileAttInfo(HttpServletRequest request, HttpServletResponse response, Model map, MultipartFile []file,
                                      @RequestParam(value = "type", required = false) Integer type,
                                      @Parameter(name="moduleType",description="模块类型",required=true) @RequestParam(name = "moduleType", required = false) String moduleType,
                                      //@Parameter(name = "moduleId", description = "模块ID", required = true) @RequestParam(name = "moduleId", required = true) String moduleId,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "formlevel", description = "表单密级信息 不为空则启动密标，为空则 不启动密标") String formlevel) throws Exception {
        JsonBean jsonBean = new JsonBean();
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(0, "用户已失效", null);
        }
        List<String> moduleList = Arrays.asList("PGJH,FXSJK,FXBG,ZDYBG,QXGL,FXCJ,ZDFX,YDPG".split(","));
        if (StringUtils.isBlank(moduleType) || !moduleList.contains(moduleType)) {
            Map moduleMap = new HashMap();
            moduleMap.put("PGJH", "评估计划");
            moduleMap.put("FXSJK", "风险数据库");
            moduleMap.put("FXBG", "风险报告");
            moduleMap.put("ZDYBG", "自定义报告");
            moduleMap.put("QXGL", "缺陷管理");
            moduleMap.put("FXCJ", "风险创建");
            moduleMap.put("YDPG", "月度评估");
            return new JsonBean(400, "参数错误：未提供正确的模块类别(moduleType)", moduleMap);
        }
        String attPath = "";
        Attachment tblAttachmentEntity = new Attachment();
        TblStaffUtil loginStaff = userProvider.get();
        if(loginStaff == null) {
            return new JsonBean(500, "用户已失效！", null);
        }
        for (MultipartFile multipartFile : file) {
            try {
                if(formlevel!= null && !formlevel.equals("")) {
                    BigDecimal attid = null;
                    Integer result = secretLabel.test(multipartFile);
//                    Integer result = 5;
                    System.out.println("是否是密标文件："+result);
                    if (result == 0){
                        return  new JsonBean(500, "包含非密标文件！", null);
                    }else {
                        //判断文件名是否包含密级信息
                        String str = multipartFile.getOriginalFilename();
//                        int index = multipartFile.getOriginalFilename().lastIndexOf(".");
                        if(str.length()>=4){
                            int index1 = str.indexOf("[");
                            int index2 = str.indexOf("]");

//                            String secretFlag = str.substring(index1, index2);
//                            System.out.println(secretFlag);
//                            String strs = "[非密][商密][秘密][机密][公开][内部][敏感][敏感信息][普通商密][核心商密]";
                            if(index1 !=-1 && index2 !=-1){
                                //获取密级级别
                                //真实内容（⬇）
                                String AttachmentLevel = secretLabel.secrectLabelInfo(multipartFile);
//                                String AttachmentLevel = "公开";
                                if (AttachmentLevel != null && !AttachmentLevel.equals("")){
                                    System.out.println("附件真实的密级级别："+AttachmentLevel);
                                    System.out.println("文件名标记的密级级别："+str.substring(index1+1, index2));
                                    if (!AttachmentLevel.equals(str.substring(index1+1, index2))){
                                        log.info("文件上传失败，文件名不包含正确密级信息！");
                                        return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
                                    }
                                }
                            }else {
                                log.info("文件上传失败，文件名不包含正确密级信息！");
                                return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
                            }
                        }else {
                            log.info("文件上传失败，文件名不包含正确密级信息！");
                            return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
                        }
                        //真实内容（⬇）
                        //获取密级级别
                        String AttachmentLevel = secretLabel.secrectLabelInfo(multipartFile);
//                        String AttachmentLevel = "公开";
                        //根据密级信息查询密级ID
                        BigDecimal attachmentLevelId = attachmentService.selectSecretLabel(AttachmentLevel);
                        attid = attachmentLevelId;
                        System.out.println(attid);
                        //根据表单密级信息，查询附件密级是否合理
                        Integer count = attachmentService.getAttachmentList(formlevel, attachmentLevelId.toString());
                        if (count == 0){
                            return  new JsonBean(500, "文件上传失败，附件密级大于表单密级！", null);
                        }
                        tblAttachmentEntity.setAttachmentlevel(attid);
                    }
                }

                InputStream inputStream = multipartFile.getInputStream();
                long imageName = snowflakeIdWorker.nextId(); //雪花算法生成图片名称
                String fileName = new String(multipartFile.getOriginalFilename().getBytes()); //重新编码
                String name=fileName.substring(fileName.lastIndexOf("."), fileName.length());
                attPath = FtpUtil.uploadFilePath(imageName+name+"", inputStream);
                if (StrUtil.isEmpty(attPath)) {
                    return  new JsonBean(500, "文件上传失败！", null);
                }
                tblAttachmentEntity.setAttpath(imageName+name);
                tblAttachmentEntity.setAttsize(new BigDecimal(multipartFile.getSize() / 1024));
                tblAttachmentEntity.setUploadtime(LocalDateTime.now());
                tblAttachmentEntity.setUploader(loginStaff.getRealname());
                tblAttachmentEntity.setAttname(fileName);
                attachmentService.save(tblAttachmentEntity);
            } catch (Exception e) {
                e.printStackTrace();
                return new JsonBean(500, "上传失败！", null);
            }
        }
        //返回当前添加的文件 前端回显
        return new JsonBean(200, "操作成功！", tblAttachmentEntity);
    }

    /**
     * 文件下载
     */
    
    
    
    
    @OperationLog(
			success = "【{{#id}}】附件下载成功",
			busType = "附件操作",
			fail = "【{{#id}}】附件下载失败",
			operationType = OperationType.DOWNLOAD,
			subType = "附件上传"
	)
    @Operation(summary="附件文件下载")
    @RequestMapping(value = "/download", produces = "application/json; charset=utf-8",method = {GET})
    public void fileDownLoad(@RequestParam("id") String id, HttpServletResponse httpServletResponse) {
        Attachment tblAttachmentEntity = attachmentService.getById(id);
        FtpUtil.downUploadFileNew(tblAttachmentEntity.toTblAttachment(), httpServletResponse);
    }

    /**
     * 文件删除
     */
    
    @OperationLog(
			success = "【{{#attid}}】附件删除成功",
			busType = "附件操作",
			fail = "【{{#attid}}】附件删除失败",
			operationType = OperationType.DELETE,
			subType = "附件上传"
	)
    @Operation(summary="附件文件删除")
    @RequestMapping(value = "/deleteById", produces = "application/json; charset=utf-8",method = {GET})
    public JsonBean deleteById(@RequestParam("attid") String attid, HttpServletResponse httpServletResponse,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(0, "用户已失效", null);
        }
        try {
            attachmentService.removeById(attid);
            attachmentService.delete(attid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "操作成功", null);
    }
}
