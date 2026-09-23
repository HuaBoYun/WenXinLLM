package com.huabo.financialdata.service;


import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.vo.diaryBook.DiaryBookRequestVo;
import com.huabo.financialdata.entity.vo.diaryBook.DiaryBookResponseVo;


/**
 * <p>
 * 日记账  接口类
 * </p>
 *
 * @author Mr.xiang
 * @since 2022-11-20
 */
public interface DiaryBookSerivce {

    //分页查询日记账信息
    ApiResponse<Map<String, Object>> getList(String token, DiaryBookRequestVo diaryBookRequestVo) throws Exception;
}
