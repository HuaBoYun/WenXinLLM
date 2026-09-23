package com.financial.sharing.consolidationReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.consolidationReport.dto.EliminationVoucherQueryParam;
import com.financial.sharing.consolidationReport.entity.TblEliminationVoucher;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 抵消凭证Mapper接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface EliminationVoucherMapper extends BaseMapper<TblEliminationVoucher> {

    /**
     * 查询抵消凭证列表
     * 
     * @param param 查询参数
     * @return 抵消凭证列表
     */
    List<TblEliminationVoucher> selectVoucherList(@Param("param") EliminationVoucherQueryParam param);

    /**
     * 根据模型ID和期间删除抵消凭证
     * 
     * @param modelId 模型ID
     * @param period 期间
     * @return 删除数量
     */
    int deleteByModelIdAndPeriod(@Param("modelId") String modelId, @Param("period") String period);

    /**
     * 批量插入抵消凭证
     * 
     * @param list 抵消凭证列表
     * @return 插入数量
     */
    int batchInsert(@Param("list") List<TblEliminationVoucher> list);

    /**
     * 根据模型ID和期间查询凭证号列表
     * 
     * @param modelId 模型ID
     * @param period 期间
     * @return 凭证号列表
     */
    List<String> selectVoucherNoList(@Param("modelId") String modelId, @Param("period") String period);

    /**
     * 根据凭证号查询抵消凭证列表
     * 
     * @param voucherNo 凭证号
     * @return 抵消凭证列表
     */
    List<TblEliminationVoucher> selectByVoucherNo(@Param("voucherNo") String voucherNo);

    /**
     * 更新凭证状态
     * 
     * @param modelId 模型ID
     * @param period 期间
     * @param status 状态
     * @return 更新数量
     */
    int updateStatusByModelIdAndPeriod(@Param("modelId") String modelId, 
                                       @Param("period") String period, 
                                       @Param("status") String status);
}

