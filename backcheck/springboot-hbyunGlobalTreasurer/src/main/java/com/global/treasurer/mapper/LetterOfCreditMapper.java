package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.dto.LetterOfCreditQueryDTO;
import com.global.treasurer.entity.TblLetterOfCredit;
import com.global.treasurer.vo.LetterOfCreditVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 信用证Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Mapper
public interface LetterOfCreditMapper extends BaseMapper<TblLetterOfCredit> {

    /**
     * 查询信用证列表
     *
     * @param queryDTO 查询条件
     * @return 信用证列表
     */
    List<LetterOfCreditVO> selectLetterOfCreditList(LetterOfCreditQueryDTO queryDTO);

    /**
     * 根据ID查询信用证详情
     *
     * @param lcId 信用证ID
     * @return 信用证详情
     */
    LetterOfCreditVO selectLetterOfCreditById(@Param("lcId") Long lcId);

    /**
     * 批量删除信用证
     *
     * @param lcIds 信用证ID数组
     * @return 影响行数
     */
    int deleteLetterOfCreditByIds(@Param("lcIds") Long[] lcIds);
}

