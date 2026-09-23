package com.huabo.fxgl.controller;

import java.net.URLDecoder;
import java.time.LocalDateTime;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Attachment;
import com.huabo.fxgl.entity.Staff;
import com.huabo.fxgl.mapper.AttachmentMapper;
import com.huabo.fxgl.service.IStaffService;
import com.huabo.fxgl.service.impl.AttachmentServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zuoshun
 * @version V1.0
 * @Package com.huabo.fxgl.controller
 * @date 2022/8/20 18:22
 */

@Slf4j
@RestController
@RequestMapping(value = "/nbsj", method = {RequestMethod.GET, RequestMethod.POST})
@Tag(name="风险管控-相关问题汇总-风险发现-文件上传",description="风险管控-相关问题汇总-风险发现-文件上传")
public class NbsjController {
    @Autowired
    private AttachmentServiceImpl attachmentService;
    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private IStaffService staffService;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 1
     *
     * @param response
     * @param att
     * @param
     * @return
     * @author yangzeguo
     * @version v1.0.1
     * @Description 风险管控-相关问题总结-新建 修改- 上传
     * @Date 2022/8/13
     */
    @OperationLog(
            success = "风险管控-相关问题总结-新建 修改- 上传处理成功",
            busType = "风险管控",
            fail = "风险管控-相关问题总结-新建 修改- 上传处理失败",
            operationType = OperationType.ADD,
            subType = "相关问题汇总"
    )
    @Operation(summary = "风险管控-相关问题总结-新建 修改- 上传/nbsj/sjzb/xmzlzb_fj")
    @RequestMapping(value = "/sjzb/xmzlzb_fj", produces = "application/json; charset=utf-8")
    public JsonBean xmzlzb_fj(
            HttpServletResponse response,
            @Parameter(name = "att", description = "att") Attachment att,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        Staff staff = staffService.getById(staffUtil.getStaffid());
        response.reset();
        att.setAttname(URLDecoder.decode(att.getAttname(),"UTF-8"));
        att.setUploader(staff.getRealname());
        att.setUploadtime(LocalDateTime.now());
        attachmentService.save(att);
        return new JsonBean(200, "success", att);
    }

    /**
     * 1
     *
     * @param attid
     * @return
     * @author yangzeguo
     * @version v1.0.1
     * @Description 风险管控-相关问题总结-新建 修改- 删除
     * @Date 2022/8/13
     */
    @OperationLog(
            success = "风险管控-相关问题总结-新建 修改- 删除处理成功",
            busType = "风险管控",
            fail = "风险管控-相关问题总结-新建 修改- 删除处理失败",
            operationType = OperationType.DELETE,
            subType = "相关问题汇总"
    )
    @Operation(summary = "风险管控-相关问题总结-新建 修改- 删除 /nbsj/jhgl/sp_del_fj")
    @RequestMapping(value = "/jhgl/sp_del_fj", produces = "application/json; charset=utf-8")
    public JsonBean sp_del_fj(@Parameter(name = "attid", description = "attid") @RequestParam String attid,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        attachmentMapper.deleteById(attid);
        return new JsonBean(200, "success", null);
    }


    /*
     * @author zuoshun
     * @version v1.0.1
     * @Description 风险报告编制通过删除附件
     * @Date 2022/8/20
     * @param attid
     * @param token
     * @return com.hbfk.util.JsonBean
     * @url:
     **/
    @OperationLog(
            success = "风险报告编制删除附件处理成功",
            busType = "风险管控",
            fail = "风险报告编制删除附件处理失败",
            operationType = OperationType.DELETE,
            subType = "相关问题汇总"
    )
    @Operation(summary = "风险报告编制删除附件 /nbsj/jhgl/sp_del_fj")
    @RequestMapping("/jhgl/sp_del_fj")
    public JsonBean attachmentDelete(@RequestParam String attid,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token){
        return attachmentService.deleteByAttId(attid);
    }
}
