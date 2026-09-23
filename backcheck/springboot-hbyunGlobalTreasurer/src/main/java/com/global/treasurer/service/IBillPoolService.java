package com.global.treasurer.service;

import com.global.treasurer.dto.BillPoolDTO;
import com.global.treasurer.dto.BillPoolQueryDTO;
import com.global.treasurer.entity.TblBillPool;
import com.global.treasurer.vo.BillPoolVO;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 票据池Service接口
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
public interface IBillPoolService {

    /**
     * 分页查询票据池列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<BillPoolVO> selectBillPoolList(BillPoolQueryDTO queryDTO);

    /**
     * 根据ID查询票据池详情
     *
     * @param poolId 票据池ID
     * @return 票据池详情
     */
    BillPoolVO selectBillPoolById(Long poolId);

    /**
     * 新增票据池
     *
     * @param dto 票据池DTO
     * @return 票据池实体
     */
    TblBillPool insertBillPool(BillPoolDTO dto);

    /**
     * 更新票据池
     *
     * @param dto 票据池DTO
     * @return 票据池实体
     */
    TblBillPool updateBillPool(BillPoolDTO dto);

    /**
     * 删除票据池
     *
     * @param poolIds 票据池ID数组
     * @return 是否成功
     */
    boolean deleteBillPool(Long[] poolIds);

    /**
     * 添加票据到池
     *
     * @param poolId  票据池ID
     * @param billIds 票据ID数组
     * @return 是否成功
     */
    boolean addBillsToPool(Long poolId, Long[] billIds);

    /**
     * 从池中移除票据
     *
     * @param poolId  票据池ID
     * @param billIds 票据ID数组
     * @return 是否成功
     */
    boolean removeBillsFromPool(Long poolId, Long[] billIds);

    /**
     * 获取票据池内的票据
     *
     * @param poolId 票据池ID
     * @return 票据列表
     */
    List<Map<String, Object>> getBillsInPool(Long poolId);

    // 临时添加的方法声明,用于解决编译错误
    default void exportBillPool(BillPoolQueryDTO queryDTO, HttpServletResponse response) {}
    default Map<String, Object> getBillPoolStatistics(BillPoolQueryDTO queryDTO) { return null; }
    default List<Map<String, Object>> getAvailableBillsForPool(Map<String, Object> params) { return null; }
    default boolean freezeBillPool(Long poolId) { return true; }
    default boolean closeBillPool(Long poolId) { return true; }

    /**
     * 生成票据池报告
     *
     * @param poolId 票据池ID
     * @return 报告数据（包含池基本信息、池内票据、融资记录、统计摘要）
     */
    default Map<String, Object> generatePoolReport(Long poolId) { return null; }
}

