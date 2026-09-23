package com.huabo.financialdata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.huabo.financialdata.entity.dto.accBook.AccountBookListQuery;
import com.huabo.financialdata.entity.entity.AccBook;
import com.huabo.financialdata.entity.vo.accBook.AccBookVO;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author lee
 */
public interface IAccBookService extends IService<AccBook> {

    /**
     * 分页查询
     *
     * @param query 请求参数
     * @return 返回结果
     */
    PageInfo<AccBookVO> getListByPage(AccountBookListQuery query);

    /**
     * 获取该组织下得账套ID集合
     *
     * @param orgId 组织ID
     * @return 返回该组织下得账套ID集合
     */
    List<String> getListByOrgId(BigDecimal orgId);

    /**
     * 获取登录账户已选中的账套信息
     *
     * @param staffId      登录用户ID
     * @param currentOrgId 组织ID
     * @return 返回已选择的账套信息
     */
    AccBookVO getSelectedBookByStaffId(BigDecimal staffId, BigDecimal currentOrgId);

    /**
     * 切换账簿
     *
     * @param staffId      登录用户Id
     * @param currentOrgId 当前用户选择的组织ID
     * @param bookId       选择的账簿ID
     * @return 返回切换账簿
     */
    Boolean chooseBook(BigDecimal staffId, BigDecimal currentOrgId, String bookId);

    /**
     * 财务决策大模型问询接口
     * @param token 	用户登录令牌
     * @param prompt	用户提问的问题
     * @return
     * @throws Exception
     */
	JsonBean generateFinance(String token, String prompt) throws Exception;


	JsonBean getCwztDbInfo(String token) throws Exception;

}
