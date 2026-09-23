package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.RevenueRecognitionEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 收入确认 Mapper接口 - Oracle/达梦版本
 *
 * @author system
 * @since 2024-12-19
 */
@Component("oracleRevenueRecognitionMapper")
public interface RevenueRecognitionMapper extends BaseMapper<RevenueRecognitionEntity> {

    /**
     * 查询收入确认列表
     *
     * @param param 查询参数
     * @return 收入确认列表
     */
    List<Map<String, Object>> selectRevenueRecognitionList(@Param("param") Map<String, Object> param);

    /**
     * 根据ID查询收入确认详情
     *
     * @param recognitionId 确认ID
     * @return 收入确认详情
     */
    Map<String, Object> selectRevenueRecognitionById(@Param("recognitionId") Long recognitionId);

    /**
     * 根据收入确认ID查询关联凭证
     *
     * @param recognitionId 确认ID
     * @return 凭证信息
     */
    Map<String, Object> selectVoucherByRecognitionId(@Param("recognitionId") Long recognitionId);
}

