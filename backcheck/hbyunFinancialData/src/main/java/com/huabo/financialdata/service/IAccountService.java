package com.huabo.financialdata.service;

import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.financialdata.entity.dto.accSubject.AccSubjectListPageQuery;
import com.huabo.financialdata.entity.vo.accBook.AccBookVO;
import com.huabo.financialdata.entity.vo.accSubject.AccSubjectPageInfoVO;

/**
 * 会计科目表 - 服务接口
 *
 * @author lee
 * @version 1.0.0
 **/
public interface IAccountService {

    /**
     * 会计科目表 - 分页查询
     *
     * @param dbName 数据库表面
     * @param query  请求参数
     * @return 获取分页结果
     */
    PageInfo<AccSubjectPageInfoVO> getListByPage(AccBookVO accBookVO,AccSubjectListPageQuery query);

}
