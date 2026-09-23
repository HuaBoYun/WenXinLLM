package com.financial.sharing.dataCollection.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.financial.sharing.dataCollection.dto.DataQualityCheckQueryParam;
import com.financial.sharing.dataCollection.dto.ExecuteQualityCheckParam;
import com.financial.sharing.dataCollection.entity.TblDataQualityCheck;
import com.financial.sharing.dataCollection.entity.TblDataQualityCheckDetail;
import com.financial.sharing.dataCollection.service.DataQualityCheckService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.UserUtils;
import com.hbfk.util.user.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据质量检查Controller
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@RestController
@RequestMapping("/financialSharing/dataCollection/qualityCheck")
public class DataQualityCheckController {

    @Autowired
    private DataQualityCheckService dataQualityCheckService;

    @Autowired
    private UserProvider userProvider;

    /**
     * 分页查询质量检查记录
     */
    @PostMapping("/queryPage")
    public MyJsonBean queryPage(@RequestBody DataQualityCheckQueryParam param) {
        try {
            Long orgId = UserUtils.getOrgIdAsLong();
            IPage<TblDataQualityCheck> page = dataQualityCheckService.queryPage(param, orgId);
            return MyJsonBean.successData("查询成功", page);
        } catch (Exception e) {
            log.error("分页查询质量检查记录失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询质量检查记录
     */
    @PostMapping("/queryById")
    public MyJsonBean queryById(@RequestParam Long checkId) {
        try {
            Long orgId = UserUtils.getOrgIdAsLong();
            TblDataQualityCheck check = dataQualityCheckService.queryById(checkId, orgId);
            return MyJsonBean.successData("查询成功", check);
        } catch (Exception e) {
            log.error("查询质量检查记录失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询检查明细
     */
    @PostMapping("/queryDetails")
    public MyJsonBean queryDetails(@RequestParam Long checkId) {
        try {
            Long orgId = UserUtils.getOrgIdAsLong();
            List<TblDataQualityCheckDetail> details = dataQualityCheckService.queryDetails(checkId, orgId);
            return MyJsonBean.successData("查询成功", details);
        } catch (Exception e) {
            log.error("查询检查明细失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 执行质量检查
     */
    @PostMapping("/executeQualityCheck")
    public MyJsonBean executeQualityCheck(@RequestBody ExecuteQualityCheckParam param) {
        try {
            Long orgId = UserUtils.getOrgIdAsLong();
            String userId = UserUtils.getUserId();
            Long checkId = dataQualityCheckService.executeQualityCheck(param, orgId, userId);
            return MyJsonBean.successData("执行成功", checkId);
        } catch (Exception e) {
            log.error("执行质量检查失败", e);
            return MyJsonBean.errorData("执行失败: " + e.getMessage());
        }
    }

    /**
     * 删除质量检查记录
     */
    @PostMapping("/deleteCheck")
    public MyJsonBean deleteCheck(@RequestParam Long checkId) {
        try {
            Long orgId = UserUtils.getOrgIdAsLong();
            boolean success = dataQualityCheckService.deleteCheck(checkId, orgId);
            return success ? MyJsonBean.successData("删除成功") : MyJsonBean.errorData("删除失败");
        } catch (Exception e) {
            log.error("删除质量检查记录失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除质量检查记录
     */
    @PostMapping("/batchDeleteCheck")
    public MyJsonBean batchDeleteCheck(@RequestBody List<Long> checkIds) {
        try {
            Long orgId = UserUtils.getOrgIdAsLong();
            int count = dataQualityCheckService.batchDeleteCheck(checkIds, orgId);
            return MyJsonBean.successData("删除成功，共删除" + count + "条记录", count);
        } catch (Exception e) {
            log.error("批量删除质量检查记录失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }
}

