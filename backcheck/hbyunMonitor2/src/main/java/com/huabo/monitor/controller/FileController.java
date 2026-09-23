package com.huabo.monitor.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.mapper.YhrPageMapper;
import com.huabo.monitor.service.ITblAttachmentService;
import com.huabo.monitor.service.ITblRiskAttWordService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author：yhr
 * @date:2022-09-13 10:39
 * @description:
 */
@Controller
@RequestMapping("/filec")
@Tag(name="文件操作",description="文件操作")
@Slf4j
public class FileController {

    @Autowired
    ITblRiskAttWordService iTblRiskAttWordService;
    @Autowired
    ITblAttachmentService iTblAttachmentService;
    
    @Resource
    private UserProvider userProvider;

    @Resource
    YhrPageMapper  yhrPageMapper;

    @OperationLog(
            success = "附件下载接口成功",
            busType = "内控测试",
            fail = "附件下载接口失败",
            operationType = OperationType.DOWNLOAD,
            subType = "文件操作"
    )
    @RequestMapping(value = "/download", method = {RequestMethod.GET})
    @Operation(summary = "附件下载接口")
    public void fileDownLoad(HttpServletRequest request, HttpServletResponse response,
 @Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
//        TblStaffUtil loginStaff = DealUserToken.parseUserToken(token);
//        if(loginStaff == null) {
//            System.out.println("用户已失效！");
//        }

    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return;
        }
        TblAttachment tblAttachmentEntity = iTblAttachmentService.getById(attId);
        if (tblAttachmentEntity == null) {

            throw new Exception("附件不存在或已经删除");
        }
        com.hbfk.entity.TblAttachment tblAttachment = new com.hbfk.entity.TblAttachment();
        tblAttachment.setAttname(tblAttachmentEntity.getAttname());
        tblAttachment.setAttpath(tblAttachmentEntity.getAttpath());
        tblAttachment.setFileName(tblAttachmentEntity.getAttname());
        tblAttachment.setAttsize(tblAttachmentEntity.getAttsize().doubleValue());
        FtpUtil.downUploadFileNew(tblAttachment, response);
        System.out.println("下载成功！");
    }


//    @RequestMapping(value = "/delete", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
//    @Operation(summary = "附件删除接口，不删除中间表关系")
//    public JsonBean fileRemove(HttpServletRequest request, HttpServletResponse response,
//                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
//                               @Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
//        TblStaffUtil loginStaff = DealUserToken.parseUserToken(token);
//        if(loginStaff == null) {
//            return ResponseFormat.retParam(0,20006,null);
//        }
//
//        TblAttachment tblAttachmentEntity = iTblAttachmentService.getById(attId);
//        if (tblAttachmentEntity == null) {
//            return ResponseFormat.retParam(0,203,null);
//        }
//        FtpUtil.removeFile(tblAttachmentEntity.getAttpath());
//        //this.tblAttachmentService.delete(attId);
//        return ResponseFormat.retParam(1,200,null);
//    }



    @OperationLog(
            success = "附件单查一条接口查询成功",
            busType = "内控测试",
            fail = "附件单查一条接口查询失败",
            operationType = OperationType.SELECT,
            subType = "文件操作"
    )
    @GetMapping(value = "/getAttMsg")
    @Operation(summary = "附件单查一条接口")
    @ResponseBody
    public JsonBean getAttMsg(

                             @Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }

        TblAttachment tblAttachmentEntity = iTblAttachmentService.getById(attId);

        Map<String, Object> mv = new HashMap<>();

        mv.put("att", tblAttachmentEntity);
        return new JsonBean(200, "success", mv);
    }



}
