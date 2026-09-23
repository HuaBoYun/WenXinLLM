package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.CashReceipt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 收款管理Mapper
 *
 * @author AI Developer
 * @date 2025-01-15
 */
@Mapper
public interface CashReceiptMapper extends BaseMapper<CashReceipt> {

    /**
     * 分页查询列表
     *
     * @param param 查询参数
     * @return 列表
     */
    List<CashReceipt> selectPage(@Param("param") Map<String, Object> param);

    /**
     * 批量删除（软删除）
     *
     * @param ids ID列表
     * @return 影响行数
     */
    int batchDelete(@Param("ids") List<Long> ids);

    /**
     * 批量更新状态
     *
     * @param ids ID列表
     * @param status 状态
     * @param updateByName 更新人
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") String status, @Param("updateByName") String updateByName);
}
