package com.huabo.financialdata.service;

import com.github.pagehelper.PageInfo;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.vo.accSumTotal.AccSumTotalRequestVo;
import com.huabo.financialdata.entity.vo.accSumTotal.AccSumTotalResponseVo;

/**
 * 总分类账-接口服务
 *
 * @author Mr.xiang
 * @since 2022-10-18
 **/
public interface AccSumTotalService {

    //查询总分类账信息
    ApiResponse<PageInfo<AccSumTotalResponseVo>> getList(String token, AccSumTotalRequestVo accSumTotalRequestVo);

}
