package com.financial.sharing.dataCollection.controller;

import com.financial.sharing.dataCollection.dto.CollectionLogQueryParam;
import com.financial.sharing.dataCollection.entity.TblCollectionLog;
import com.financial.sharing.dataCollection.service.CollectionLogService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.UserUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 归集日志Controller
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@RestController
@RequestMapping("/financialSharing/dataCollection/collectionLog")
@Tag(name = "归集日志管理", description = "归集日志管理接口")
public class CollectionLogController {

    @Autowired
    private CollectionLogService collectionLogService;

    /**
     * 分页查询归集日志
     */
    @PostMapping("/queryPage")
    @Operation(summary = "分页查询归集日志")
    public MyJsonBean queryPage(@RequestBody CollectionLogQueryParam param,
                                @Parameter(name = "token", description = "登录用户token", required = true)
                                @RequestHeader("token") String token) {
        try {
            String orgId = UserUtils.getOrgId();
            if (orgId == null) {
                return MyJsonBean.errorData("用户未登录或会话已失效，请重新登录");
            }
            return collectionLogService.queryPage(param, orgId);
        } catch (Exception e) {
            log.error("分页查询归集日志失败", e);
            return MyJsonBean.errorData("分页查询归集日志失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询归集日志
     */
    @PostMapping("/queryById")
    @Operation(summary = "根据ID查询归集日志")
    public MyJsonBean queryById(@RequestParam("logId") String logId,
                                @Parameter(name = "token", description = "登录用户token", required = true)
                                @RequestHeader("token") String token) {
        try {
            String orgId = UserUtils.getOrgId();
            if (orgId == null) {
                return MyJsonBean.errorData("用户未登录或会话已失效，请重新登录");
            }
            TblCollectionLog log = collectionLogService.queryById(logId, orgId);
            if (log == null) {
                return MyJsonBean.errorData("归集日志不存在");
            }

            return MyJsonBean.successData(log);
        } catch (Exception e) {
            log.error("查询归集日志失败", e);
            return MyJsonBean.errorData("查询归集日志失败：" + e.getMessage());
        }
    }

    /**
     * 删除归集日志
     */
    @PostMapping("/delete")
    @Operation(summary = "删除归集日志")
    public MyJsonBean delete(@RequestParam("logId") String logId,
                             @Parameter(name = "token", description = "登录用户token", required = true)
                             @RequestHeader("token") String token) {
        try {
            String orgId = UserUtils.getOrgId();
            if (orgId == null) {
                return MyJsonBean.errorData("用户未登录或会话已失效，请重新登录");
            }
            return collectionLogService.deleteCollectionLog(logId, orgId);
        } catch (Exception e) {
            log.error("删除归集日志失败", e);
            return MyJsonBean.errorData("删除归集日志失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除归集日志
     */
    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除归集日志")
    public MyJsonBean batchDelete(@RequestBody String[] logIds,
                                  @Parameter(name = "token", description = "登录用户token", required = true)
                                  @RequestHeader("token") String token) {
        try {
            String orgId = UserUtils.getOrgId();
            if (orgId == null) {
                return MyJsonBean.errorData("用户未登录或会话已失效，请重新登录");
            }
            return collectionLogService.batchDeleteCollectionLog(logIds, orgId);
        } catch (Exception e) {
            log.error("批量删除归集日志失败", e);
            return MyJsonBean.errorData("批量删除归集日志失败：" + e.getMessage());
        }
    }

    /**
     * 清理历史日志
     */
    @PostMapping("/cleanHistory")
    @Operation(summary = "清理历史日志")
    public MyJsonBean cleanHistory(@RequestParam("days") Integer days,
                                   @Parameter(name = "token", description = "登录用户token", required = true)
                                   @RequestHeader("token") String token) {
        try {
            String orgId = UserUtils.getOrgId();
            if (orgId == null) {
                return MyJsonBean.errorData("用户未登录或会话已失效，请重新登录");
            }
            return collectionLogService.cleanHistoryLogs(days, orgId);
        } catch (Exception e) {
            log.error("清理历史日志失败", e);
            return MyJsonBean.errorData("清理历史日志失败：" + e.getMessage());
        }
    }
}

