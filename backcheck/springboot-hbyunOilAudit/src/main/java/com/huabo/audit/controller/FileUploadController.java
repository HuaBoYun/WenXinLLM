package com.huabo.audit.controller;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

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

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.FtpUtil;
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
    
    
    private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(5, 5);
    /**
     * @description 通用文件上传
     * @author lyz
     * @date 2022/4/15 14:57
     */
    @PostMapping("/upload")
    @Operation(summary = "附件上传接口")
    public R fileUpload(HttpServletRequest request, 
    		@Parameter(name = "file", description = "附件上传entity", required = true) MultipartFile[]file,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token) throws Exception {
        String attPath = "";
        TblAttachment tblAttachmentEntity = new TblAttachment();
        TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return R.fail("用户已失效！");
		}
        for (MultipartFile multipartFile : file) {
            try {
                InputStream inputStream = multipartFile.getInputStream();
                long imageName = snowflakeIdWorker.nextId(); //雪花算法生成图片名称
                String fileName = new String(multipartFile.getOriginalFilename().getBytes()); //重新编码
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
                tblAttachmentService.saveEntity(tblAttachmentEntity);
            } catch (Exception e) {
                e.printStackTrace();
                return R.fail("上传失败");
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
    @RequestMapping(value = "/download", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "附件下载接口")
    public void fileDownLoad(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
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
    @RequestMapping(value = "/delete", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "附件删除接口，不删除中间表关系")
    public void fileRemove(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			 System.out.println(R.fail("用户已失效！"));
		}
    	
		TblAttachment tblAttachmentEntity = tblAttachmentService.selectEntityById(attId);
        if (tblAttachmentEntity == null) {
            throw new CommercialException("附件不存在或已经删除");
        }
        FtpUtil.removeFile(tblAttachmentEntity.getAttpath());
        this.tblAttachmentService.removeEntityById(new BigDecimal(attId));
        System.out.println(R.success());
    }
}
