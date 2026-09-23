package com.huabo.audit.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblYqnsMyTaskReviewEntity;
import com.huabo.audit.oracle.service.TblYqnsMyTaskReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsMyTaskReviewController
 * @PACKAGE_NAME: com.huabo.audit.controller
 * @date 2023/10/10 14:00.
 * @version: V1.0
 * @description: 央企内审-审计实施-我的任务-审查
 */
@RestController
@Slf4j
@Tag(name="我的任务-审查",description="我的任务-审查")
@RequestMapping(value = "/audit/myTask/Review")
public class TblYqnsMyTaskReviewController {

    @Resource
    private TblYqnsMyTaskReviewService tblYqnsMyTaskReviewService;

    
    
    @Operation(summary = "我的任务-审查--获取审查列表")
    @GetMapping(value = "/getMyTaskReviewList", produces = "application/json;charset=utf-8")
    public JsonBean getMyTaskReviewList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "-我的任务-审查实体", required = true) TblYqnsMyTaskReviewEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsMyTaskReviewService.getMyTaskReviewList(token,vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("我的任务-审查 -- 获取-我的任务-审查附件列表接口异常", e);
            jsonBean=ResponseFormat.retParam(1,1000,e.getMessage());
			e.printStackTrace();
        }
        return jsonBean;
    }
    

    @Operation(summary = "我的任务-审查--获取审查附件列表")
    @GetMapping(value = "getIdbyattlist", produces = "application/json;charset=utf-8")
    public JsonBean getIdbyattlist(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "-我的任务-审查ID", required = false) @RequestParam(value = "id",required = false) String id,
            @Parameter(name = "attids", description = "-我的任务-上传附件ids", required = false) @RequestParam(value = "attids", required = false) String attids) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsMyTaskReviewService.getReviewattlistById(token,id,attids);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("我的任务-审查 -- 获取-我的任务-审查附件列表接口异常", e);
            jsonBean=ResponseFormat.retParam(1,1000,e.getMessage());
			e.printStackTrace();
        }
        return jsonBean;
    }
    
    

    @Operation(summary = "我的任务-审查--获取审查单个详情信息")
    @GetMapping(value = "getRecordsById", produces = "application/json;charset=utf-8")
    public JsonBean getRecordsById(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "-我的任务-审查ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsMyTaskReviewService.getReviewById(token,id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("我的任务-审查 -- 获取-我的任务-审查单个详情信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "我的任务-审查--新增/更新")
    @PostMapping(value = "saveOrUpdate", produces = "application/json;charset=utf-8")
    public JsonBean saveOrUpdate(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "vo", description = "我的任务-审查entity") TblYqnsMyTaskReviewEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsMyTaskReviewService.saveOrUpdate(token,vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施---审计项目情况表 -- 新增/更新接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }

    @Operation(summary = "我的任务-审查--新增/更新 明细")
    @PostMapping(value = "saveOrUpdateList", produces = "application/json;charset=utf-8")
    public JsonBean saveOrUpdateList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "vo", description = "我的任务-审查entityList") @RequestBody List<TblYqnsMyTaskReviewEntity> voList) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsMyTaskReviewService.saveOrUpdateList(token,voList);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施---审计项目情况表 -- 新增/更新明细接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }



    @Operation(summary = "我的任务-审查--删除（直接删除）")
    @GetMapping(value = "delete", produces = "application/json;charset=utf-8")
    public JsonBean delete(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "-我的任务-审查ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsMyTaskReviewService.delete(token,id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("我的任务-审查 -- 删除接口异常 ", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "我的任务-审查- 附件-删除（直接删除）")
    @GetMapping(value = "deleteFileAttach", produces = "application/json;charset=utf-8")
    public JsonBean deleteFileAttach(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "attId", description = "附件ID", required = true) @RequestParam(value = "attId") String attId) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsMyTaskReviewService.deleteFileAttach(token,attId);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("我的任务-审查- 附件 -- 删除接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    
}
