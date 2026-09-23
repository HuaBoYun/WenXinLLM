package com.huabo.know.service;

import com.hbfk.util.JsonBean;
import com.huabo.know.entity.TblZsgxTerms;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.know.vo.param.CreateTermsParam;
import com.huabo.know.vo.param.TermsListParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2024-04-03
 */
public interface TblZsgxTermsService extends IService<TblZsgxTerms> {

    /**
     * 查询合同要素库列表
     * @param token	用户登录token
     * @param termsListParam	检索入参
     * @return	合同要素库列表
     * @throws Exception
     */
    JsonBean getTermsList(String token, TermsListParam termsListParam) throws Exception;

    /**
     * 查询合同要素详情
     * @param token	用户登录token
     * @param id	条款ID
     * @return	合同要素详情
     */
    JsonBean getTermsInfo(String token, String id) throws Exception;

    TblZsgxTerms getById(String id);

    /**
     * 新建合同要素
     * @param token	用户登录token
     * @param createTermsParam	新建合同要素入参
     * @return	true
     */
    JsonBean createTerms(String token, CreateTermsParam createTermsParam) throws Exception;


    JsonBean download(String id, HttpServletResponse response) throws Exception;

    JsonBean zipDownload(List<String> ids, HttpServletResponse response) throws Exception;
}
