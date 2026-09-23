package com.huabo.financialdata.service.impl;

import com.github.pagehelper.PageInfo;
import com.huabo.financialdata.entity.dto.accSubject.AccSubjectListPageQuery;
import com.huabo.financialdata.entity.vo.accSubject.AccSubjectPageInfoVO;
import com.huabo.financialdata.mapper.AccBsegMapper;
import com.huabo.financialdata.service.IAccBsegService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 按月明细表(每年度) - 服务接口
 *
 * @author lee
 * @version 1.0.0
 **/
@Service
public class AccBsegServiceImpl implements IAccBsegService {

    @Resource
    private AccBsegMapper accBsegMapper;

    /**
     * 会计科目表 - 分页查询
     *
     * @param query 请求参数
     * @return 获取分页结果
     */
    @Override
    public PageInfo<AccSubjectPageInfoVO> getListByPage(String dbName, AccSubjectListPageQuery query) {
        // accBsegMapper.selectByCondition()
        return null;
    }
}
