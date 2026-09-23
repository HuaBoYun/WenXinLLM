package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.VoucherUnpostRecordEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 凭证反过账记录 Mapper接口
 *
 * @author system
 * @since 2026-01-03
 */
public interface VoucherUnpostRecordMapper extends BaseMapper<VoucherUnpostRecordEntity> {

    /**
     * 分页查询反过账记录列表
     *
     * @param param 查询参数
     * @return 反过账记录列表
     */
    List<Map<String, Object>> selectUnpostRecordPage(@Param("param") Map<String, Object> param);

    /**
     * 查询反过账记录总数
     *
     * @param param 查询参数
     * @return 记录总数
     */
    Long selectUnpostRecordCount(@Param("param") Map<String, Object> param);

    /**
     * 根据凭证ID查询反过账记录
     *
     * @param voucherId 凭证ID
     * @return 反过账记录列表
     */
    List<VoucherUnpostRecordEntity> selectByVoucherId(@Param("voucherId") Long voucherId);

    /**
     * 插入反过账记录
     *
     * @param record 反过账记录
     * @return 插入结果
     */
    int insertUnpostRecord(@Param("record") VoucherUnpostRecordEntity record);

    /**
     * 更新反过账记录状态（重新过账时使用）
     *
     * @param recordId 记录ID
     * @param status 新状态
     * @param repostBy 重新过账人ID
     * @param repostByName 重新过账人姓名
     * @return 更新结果
     */
    int updateUnpostStatus(@Param("recordId") Long recordId,
                          @Param("status") String status,
                          @Param("repostBy") Long repostBy,
                          @Param("repostByName") String repostByName);
}

