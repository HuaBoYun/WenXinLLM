package com.huabo.audit.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsSjzgZgbg;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJZG_ZGBGINFO(审计整改报告表)】的数据库操作Service
 */
public interface TblYqnsSjzgZgbgService extends IService<TblYqnsSjzgZgbg> {
    /**
     * 查询
     *
     * @param token
     * @param pageNumber
     * @param pageSize
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsSjzgZgbg vo) throws Exception;


    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean saveOrUpdate(String token, TblYqnsSjzgZgbg vo) throws Exception;

    /**
     * 详情
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean detail(String token, TblYqnsSjzgZgbg vo) throws Exception;

    /**
     * 一个多个删除
     *
     * @param token
     * @param vo ids[]
     * @return
     * @throws Exception
     */
    JsonBean delete(String token, TblYqnsSjzgZgbg vo) throws Exception;

    /**
     * 附件删除
     * @param token
     * @param zgbgid 
     * @return
     * @throws Exception
     */
    JsonBean deleteAttach(String token, String attid, BigDecimal zgbgid) throws Exception;

}
