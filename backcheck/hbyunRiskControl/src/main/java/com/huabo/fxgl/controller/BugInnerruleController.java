package com.huabo.fxgl.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.huabo.fxgl.entity.BugInnerrule;
import com.huabo.fxgl.service.impl.BugInnerruleServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author LiYe
 * @since 2022-08-15
 */

@RestController
@Tag(name="缺陷管理",description="缺陷管理")
@RequestMapping(value = "/bugInnerrule", method = {RequestMethod.GET, RequestMethod.POST})
public class BugInnerruleController {
    @Autowired
    private BugInnerruleServiceImpl bugInnerruleService;


    /**
     * 保存缺陷关联内规 ok
     *
     * @param
     * @return
     * @auther mamingxu
     * @version 1.0.1
     * @since 2022/8/15
     */
    @OperationLog(
            success = "保存缺陷关联内规成功",
            busType = "缺陷管理",
            fail = "保存缺陷关联内规失败",
            operationType = OperationType.ADD,
            subType = "缺陷管理"
    )
    @RequestMapping(value = "/qxwt/add_inner_qxwt")
    @Operation(summary = "保存缺陷关联内规/qxwt/add_inner_qxwt")
    public String add_inner_qxwt(@Parameter(name = "conid", description = "conid") @RequestParam(required = false) String conid,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 @Parameter(name = "innrulid", description = "innrulid") @RequestParam(required = false) String outerid) {

        if (conid != null && outerid != null) {
            List<BugInnerrule> list = bugInnerruleService.isIfFlowInner(conid, outerid);
            if (list == null || list.size() == 0) {
                BugInnerrule ou = new BugInnerrule();
                ou.setBugid(new BigDecimal(conid));
                ou.setInnrulid(new BigDecimal(outerid));
                bugInnerruleService.saveOrUpdate(ou);
            }
        }
        return "";
    }

}
