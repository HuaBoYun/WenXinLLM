package com.huabo.financialdata.service;

import com.github.pagehelper.PageInfo;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.vo.prjData.LrbRequestVo;
import com.huabo.financialdata.entity.vo.prjData.ZcfzbRequestVo;
import com.huabo.financialdata.entity.vo.prjData.ZcfzbResponseVo;

/**
 * <p>
 * 报表数据  接口类
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-12-01
 */
public interface PrjDataService {

    //报表数据  资产负债表  列表分页查询
    ApiResponse<PageInfo<ZcfzbResponseVo>> getZcfzbList(String token, ZcfzbRequestVo zcfzbRequestVo) throws Exception;

    //报表数据 - 利润表-- 列表分页查询
    ApiResponse<PageInfo<ZcfzbResponseVo>> getLrbList(String token, LrbRequestVo lrbRequestVo) throws Exception;
}
