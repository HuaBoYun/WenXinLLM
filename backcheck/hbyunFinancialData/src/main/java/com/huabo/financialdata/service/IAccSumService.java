package com.huabo.financialdata.service;

import com.github.pagehelper.PageInfo;
import com.huabo.financialdata.entity.dto.AccSumListPageQuery;
import com.huabo.financialdata.entity.vo.AccSumListPageInfoVO;
import com.huabo.financialdata.entity.vo.accBook.AccBookVO;

/**
 * 科目余额-接口服务
 *
 * @author lee
 * @version 1.0.0
 **/
public interface IAccSumService {

    /**
     * 分页查询
     *
     * @param accBookVO 当前登录用户选中的账套
     * @param query     查询请求参数
     * @return 返回结果
     */
    PageInfo<AccSumListPageInfoVO> getListByPage(AccBookVO accBookVO, AccSumListPageQuery query);

}
