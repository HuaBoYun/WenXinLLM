package com.huabo.audit.controller;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// import com.eetrust.label.LabelOperator;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.mibiao.SecretLabel;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.exception.CommercialException;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.service.TblAttachmentService;
import com.huabo.audit.util.R;
import com.huabo.audit.util.SnowflakeIdWorker;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author lyz
 * @description
 */
@RestController
@RequestMapping("/fileManage")
@Tag(name="审计文件管理接口",description="审计文件管理接口")
public class FileUploadController {
	
    @Autowired
    TblAttachmentService tblAttachmentService;
    
    @Resource
    private UserProvider userProvider;

    @Autowired(required = false)
    SecretLabel secretLabel;

    // @Resource
    // LabelOperator labelOperator;
    
    private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(5, 5);
    /**
     * @description 通用文件上传
     * @author lyz
     * @date 2022/4/15 14:57
     */
    @OperationLog(
            success = "附件上传",
            busType = "智能审计",
            fail = "附件上传",
            operationType = OperationType.UPLOAD,
            subType = "审计文件附件上传"
    )
    @PostMapping("/upload")
    @Operation(summary = "附件上传接口")
    public R fileUpload(HttpServletRequest request, 
    		@Parameter(name = "file", description = "附件上传entity", required = true) MultipartFile[]file,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "formlevel", description = "表单密级信息 不为空则启动密标，为空则 不启动密标") String formlevel) throws Exception {
        String attPath = "";
        TblAttachment tblAttachmentEntity = new TblAttachment();
        TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return R.fail("用户已失效！");
		}
        //判断是否启用密标 如果传递密级信息就是启用密标，不传表单密级信息，就不会启用密标
        if(formlevel!=null && !"".equals(formlevel) && secretLabel != null){
            for (MultipartFile multipartFile : file) {
                BigDecimal attid = null;
                try {
                    InputStream inputStream = multipartFile.getInputStream();
//                        Integer test = labelOperator.isLabelledFile(multipartFile.getInputStream());
                        Integer result = secretLabel.test(multipartFile);
//                        Integer result = 1;
                        System.out.println("是否是密标文件："+result);
                        if (result == 0){
                            return R.fail("文件上传失败，包含非密标文件");
                        }else {
                            //判断文件名是否包含密级信息
                            String str = multipartFile.getOriginalFilename();
//                            int index = multipartFile.getOriginalFilename().lastIndexOf(".");
                            if(str.length()>=4){
                                int index1 = str.indexOf("[");
                                int index2 = str.indexOf("]");
                                if(index1 !=-1 && index2 !=-1){
                                    //获取密级级别
                                    String AttachmentLevel = secretLabel.secrectLabelInfo(multipartFile);
                                    if (AttachmentLevel != null && !AttachmentLevel.equals("")){
                                        System.out.println("密级级别："+AttachmentLevel);
                                        System.out.println("密级级别："+str.substring(index1+1, index2));
                                        if (!AttachmentLevel.equals(str.substring(index1+1, index2))){
                                            return  R.fail("文件上传失败，附件名不包含正确密级信息！");
                                        }
                                    }
                                }else {
                                    return  R.fail("文件上传失败，附件名不包含正确密级信息！");
                                }
                                /*String secretFlag = str.substring(index1+1, index2);
                                System.out.println(secretFlag);*/
//                                String strs = "[非密][商密][秘密][机密][公开][内部][敏感][敏感信息][普通商密][核心商密]";
                                /*if(strs.indexOf(secretFlag)==-1){
                                    return  R.fail("文件上传失败，附件名不包含正确密级信息！");
                                }*/
                            }else {
                                return  R.fail("文件上传失败，附件名不包含正确密级信息！");
                            }
                            //获取密级级别
                            String AttachmentLevel = secretLabel.secrectLabelInfo(multipartFile);
                            //根据密级信息查询密级ID
                            BigDecimal attachmentLevelId = tblAttachmentService.selectSecretLabel(AttachmentLevel);
                            attid = attachmentLevelId;
                            System.out.println(attid);
                            //根据表单密级信息，查询附件密级是否合理
                            Integer count = tblAttachmentService.getAttachmentList(formlevel, attachmentLevelId.toString());
                            if (count == 0){
                                return R.fail("文件上传失败，附件密级大于表单密级！");
                            }
                        }
                    long imageName = snowflakeIdWorker.nextId(); //雪花算法生成图片名称
                    String fileName = new String(multipartFile.getOriginalFilename().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
//                String fileName = new String(multipartFile.getOriginalFilename().getBytes()); //重新编码
                    String name = fileName.substring(fileName.lastIndexOf("."), fileName.length());
                    attPath = FtpUtil.uploadFilePath(imageName + name, inputStream);
                    if (StrUtil.isEmpty(attPath)) {
                        return R.fail("文件上传失败");
                    }
                    //tblAttachmentEntity.setAttid(new BigDecimal(snowflakeIdWorker.nextId()));
                    //tblAttachmentEntity.setAttpath(attPath + imageName+name);
                    tblAttachmentEntity.setAttpath(imageName+name);
                    tblAttachmentEntity.setAttsize(multipartFile.getSize() / 1024);
                    tblAttachmentEntity.setUploadtime(new Date());
                    tblAttachmentEntity.setUploader(loginStaff.getRealname());
                    tblAttachmentEntity.setAttname(fileName);
                    tblAttachmentEntity.setAttid(RandomUtil.uuBigDecimalId());
                    tblAttachmentEntity.setAttachmentlevel(attid);
                    tblAttachmentService.save(tblAttachmentEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                    return R.fail("上传失败");
                }
            }
        }else {
            for (MultipartFile multipartFile : file) {
                try {
                    InputStream inputStream = multipartFile.getInputStream();
                    long imageName = snowflakeIdWorker.nextId(); //雪花算法生成图片名称
                    String fileName = new String(multipartFile.getOriginalFilename().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
//                String fileName = new String(multipartFile.getOriginalFilename().getBytes()); //重新编码
                    String name = fileName.substring(fileName.lastIndexOf("."), fileName.length());
                    attPath = FtpUtil.uploadFilePath(imageName + name, inputStream);
                    if (StrUtil.isEmpty(attPath)) {
                        return R.fail("文件上传失败");
                    }
                    //tblAttachmentEntity.setAttid(new BigDecimal(snowflakeIdWorker.nextId()));
                    //tblAttachmentEntity.setAttpath(attPath + imageName+name);
                    tblAttachmentEntity.setAttpath(imageName+name);
                    tblAttachmentEntity.setAttsize(multipartFile.getSize() / 1024);
                    tblAttachmentEntity.setUploadtime(new Date());
                    tblAttachmentEntity.setUploader(loginStaff.getRealname());
                    tblAttachmentEntity.setAttname(fileName);
                    tblAttachmentEntity.setAttid(RandomUtil.uuBigDecimalId());
                    tblAttachmentService.save(tblAttachmentEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                    return R.fail("上传失败");
                }
            }
        }
        //返回当前添加的文件 前端回显
        return R.success(tblAttachmentEntity);
    }

    
    
    
    
    
    
    /**
     * @description 根据ATTID  下载文件
     * @author lyz
     * @date 2022/4/18 17:58
     */
    @OperationLog(
            success = "附件下载",
            busType = "智能审计",
            fail = "附件下载",
            operationType = OperationType.DOWNLOAD,
            subType = "审计文件附件下载"
    )
    @RequestMapping(value = "/download", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "附件下载接口")
    public void fileDownLoad(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") BigDecimal attId) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			 System.out.println(R.fail("用户已失效！"));
		}
    	
		TblAttachment tblAttachmentEntity = tblAttachmentService.selectEntityById(attId);
        if (tblAttachmentEntity == null) {
            throw new CommercialException("附件不存在或已经删除");
        }
        com.hbfk.entity.TblAttachment tblAttachment = new com.hbfk.entity.TblAttachment();
        tblAttachment.setAttname(tblAttachmentEntity.getAttname());
        tblAttachment.setAttpath(tblAttachmentEntity.getAttpath());
        tblAttachment.setFileName(tblAttachmentEntity.getAttname());
        tblAttachment.setAttsize(tblAttachmentEntity.getAttsize());
        FtpUtil.downUploadFileNew(tblAttachment, response);
        System.out.println(R.success());
    }

    
    /**
     * @description 根据ATTID  下载文件
     * @author lyz
     * @date 2022/4/18 17:58
     */
    @OperationLog(
            success = "附件删除",
            busType = "智能审计",
            fail = "附件删除",
            operationType = OperationType.DELETE,
            subType = "审计文件附件删除"
    )
    @RequestMapping(value = "/delete", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "附件删除接口，不删除中间表关系")
    public void fileRemove(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") BigDecimal attId) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			 System.out.println(R.fail("用户已失效！"));
		}
    	
		TblAttachment tblAttachmentEntity = tblAttachmentService.selectEntityById(attId);
        if (tblAttachmentEntity == null) {
            throw new CommercialException("附件不存在或已经删除");
        }
        FtpUtil.removeFile(tblAttachmentEntity.getAttpath());
        this.tblAttachmentService.removeEntityById(attId);
        System.out.println(R.success());
    }
}
