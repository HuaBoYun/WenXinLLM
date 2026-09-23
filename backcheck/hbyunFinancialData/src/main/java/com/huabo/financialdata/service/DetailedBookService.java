package com.huabo.financialdata.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.vo.detailedBook.DetailedBookRequestVo;
import com.huabo.financialdata.entity.vo.detailedBook.DetailedBookResponseVo;

/**
 * <p>
 * 明细账  接口服务
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-11-19
 */
public interface DetailedBookService {

    //查询明细账列表信息
    ApiResponse<Map<String, Object>> getList(String token, DetailedBookRequestVo detailedBookRequestVo) throws Exception;
}
