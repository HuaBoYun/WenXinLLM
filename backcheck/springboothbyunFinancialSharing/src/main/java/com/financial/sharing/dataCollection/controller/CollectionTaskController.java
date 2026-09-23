package com.financial.sharing.dataCollection.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.dataCollection.dto.CollectionTaskQueryParam;
import com.financial.sharing.dataCollection.entity.TblCollectionTask;
import com.financial.sharing.dataCollection.service.CollectionTaskService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 归集任务Controller
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@RestController
@RequestMapping("/financialSharing/collectionTask/api")
public class CollectionTaskController {

    @Autowired
    private CollectionTaskService collectionTaskService;

    /**
     * 分页查询归集任务
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @PostMapping("/queryPage")
    public MyJsonBean queryPage(@RequestBody CollectionTaskQueryParam param) {
        try {
            String orgId = UserUtils.getOrgId();
            return collectionTaskService.queryPage(param, orgId);
        } catch (Exception e) {
            log.error("分页查询归集任务失败", e);
            return MyJsonBean.errorData("分页查询归集任务失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询归集任务
     *
     * @param params 参数
     * @return 归集任务
     */
    @PostMapping("/queryById")
    public MyJsonBean queryById(@RequestBody Map<String, Object> params) {
        try {
            String taskId = (String) params.get("taskId");
            String orgId = UserUtils.getOrgId();

            TblCollectionTask task = collectionTaskService.queryById(taskId, orgId);
            if (task == null) {
                return MyJsonBean.errorData("归集任务不存在");
            }

            return MyJsonBean.successData(task);
        } catch (Exception e) {
            log.error("查询归集任务失败", e);
            return MyJsonBean.errorData("查询归集任务失败：" + e.getMessage());
        }
    }

    /**
     * 保存归集任务
     *
     * @param task 归集任务
     * @return 操作结果
     */
    @PostMapping("/saveCollectionTask")
    public MyJsonBean saveCollectionTask(@RequestBody TblCollectionTask task) {
        try {
            String orgId = UserUtils.getOrgId();
            String userId = UserUtils.getUserId();

            return collectionTaskService.saveCollectionTask(task, orgId, userId);
        } catch (Exception e) {
            log.error("保存归集任务失败", e);
            return MyJsonBean.errorData("保存归集任务失败：" + e.getMessage());
        }
    }

    /**
     * 删除归集任务
     *
     * @param params 参数
     * @return 操作结果
     */
    @PostMapping("/deleteCollectionTask")
    public MyJsonBean deleteCollectionTask(@RequestBody Map<String, Object> params) {
        try {
            String taskId = (String) params.get("taskId");
            String orgId = UserUtils.getOrgId();

            return collectionTaskService.deleteCollectionTask(taskId, orgId);
        } catch (Exception e) {
            log.error("删除归集任务失败", e);
            return MyJsonBean.errorData("删除归集任务失败：" + e.getMessage());
        }
    }

    /**
     * 启用/禁用归集任务
     *
     * @param params 参数
     * @return 操作结果
     */
    @PostMapping("/toggleEnabled")
    public MyJsonBean toggleEnabled(@RequestBody Map<String, Object> params) {
        try {
            String taskId = (String) params.get("taskId");
            String isEnabled = (String) params.get("isEnabled");
            String orgId = UserUtils.getOrgId();
            String userId = UserUtils.getUserId();

            return collectionTaskService.toggleEnabled(taskId, isEnabled, orgId, userId);
        } catch (Exception e) {
            log.error("启用/禁用归集任务失败", e);
            return MyJsonBean.errorData("启用/禁用归集任务失败：" + e.getMessage());
        }
    }

    /**
     * 立即执行归集任务
     *
     * @param params 参数
     * @return 操作结果
     */
    @PostMapping("/executeTask")
    public MyJsonBean executeTask(@RequestBody Map<String, Object> params) {
        try {
            String taskId = (String) params.get("taskId");
            String orgId = UserUtils.getOrgId();

            return collectionTaskService.executeTask(taskId, orgId);
        } catch (Exception e) {
            log.error("执行归集任务失败", e);
            return MyJsonBean.errorData("执行归集任务失败：" + e.getMessage());
        }
    }

    /**
     * 停止归集任务
     *
     * @param params 参数
     * @return 操作结果
     */
    @PostMapping("/stopTask")
    public MyJsonBean stopTask(@RequestBody Map<String, Object> params) {
        try {
            String taskId = (String) params.get("taskId");
            String orgId = UserUtils.getOrgId();

            return collectionTaskService.stopTask(taskId, orgId);
        } catch (Exception e) {
            log.error("停止归集任务失败", e);
            return MyJsonBean.errorData("停止归集任务失败：" + e.getMessage());
        }
    }

    /**
     * 计算下次执行时间
     *
     * @param task 归集任务
     * @return 下次执行时间
     */
    @PostMapping("/calculateNextExecuteTime")
    public MyJsonBean calculateNextExecuteTime(@RequestBody TblCollectionTask task) {
        try {
            return collectionTaskService.calculateNextExecuteTime(task);
        } catch (Exception e) {
            log.error("计算下次执行时间失败", e);
            return MyJsonBean.errorData("计算下次执行时间失败：" + e.getMessage());
        }
    }
}

