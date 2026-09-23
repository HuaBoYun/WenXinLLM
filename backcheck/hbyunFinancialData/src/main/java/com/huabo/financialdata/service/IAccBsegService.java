package com.huabo.financialdata.service;

import com.github.pagehelper.PageInfo;
import com.huabo.financialdata.entity.dto.accSubject.AccSubjectListPageQuery;
import com.huabo.financialdata.entity.vo.accSubject.AccSubjectPageInfoVO;

/**
 * 按月明细表(每年度) - 服务接口
 *
 * @author lee
 * @version 1.0.0
 **/
public interface IAccBsegService {

    /**
     * 会计科目表 - 分页查询
     *
     * @param query 请求参数
     * @return 获取分页结果
     */
    PageInfo<AccSubjectPageInfoVO> getListByPage(String dbName, AccSubjectListPageQuery query);

}
