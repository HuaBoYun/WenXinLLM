package com.financial.sharing.consolidationReport.service;

import com.financial.sharing.consolidationReport.dto.EliminationVoucherQueryParam;
import com.financial.sharing.consolidationReport.dto.VoucherGenerateParam;
import com.financial.sharing.consolidationReport.entity.TblEliminationVoucher;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 抵消凭证Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface EliminationVoucherService {

    /**
     * 生成抵消凭证
     * 
     * @param param 生成参数
     * @return 生成结果
     */
    Map<String, Object> generateVouchers(VoucherGenerateParam param);

    /**
     * 查询抵消凭证列表(分页)
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<TblEliminationVoucher> getVoucherList(EliminationVoucherQueryParam param);

    /**
     * 根据凭证号查询抵消凭证列表
     * 
     * @param voucherNo 凭证号
     * @return 抵消凭证列表
     */
    List<TblEliminationVoucher> getVouchersByNo(String voucherNo);

    /**
     * 根据模型ID和期间查询凭证号列表
     * 
     * @param modelId 模型ID
     * @param period 期间
     * @return 凭证号列表
     */
    List<String> getVoucherNoList(String modelId, String period);

    /**
     * 删除抵消凭证
     * 
     * @param modelId 模型ID
     * @param period 期间
     */
    void deleteVouchers(String modelId, String period);

    /**
     * 确认抵消凭证
     * 
     * @param modelId 模型ID
     * @param period 期间
     */
    void confirmVouchers(String modelId, String period);
}

