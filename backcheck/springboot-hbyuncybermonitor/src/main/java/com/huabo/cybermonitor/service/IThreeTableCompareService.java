package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.ThreeTableCompare;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.ThreeTableCompareDetailVO;
import com.huabo.cybermonitor.vo.ThreeTableCompareQueryVO;
import com.huabo.cybermonitor.vo.ThreeTableDispatchVO;

import javax.servlet.http.HttpServletResponse;

/**
 * 三表比对 Service 接口
 * @author system
 * @date 2025-01-01
 */
public interface IThreeTableCompareService extends IService<ThreeTableCompare> {

    /**
     * 分页查询三表比对列表
     */
    PageResult<ThreeTableCompare> getList(ThreeTableCompareQueryVO queryVO);

    /**
     * 获取三表比对详情（含差异分析明细）
     */
    ThreeTableCompareDetailVO getDetail(String id);

    /**
     * 导出差异报告（Excel）
     */
    void exportDiffReport(ThreeTableCompareQueryVO queryVO, HttpServletResponse response);

    /**
     * 提交差异核查派单
     */
    void submitDispatch(ThreeTableDispatchVO dispatchVO);
}
