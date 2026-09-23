package com.huabo.audit.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsSjzgSjzgtj;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJZG_SJZGTJ(审计整改统计表)】的数据库操作Service
 */
public interface TblYqnsSjzgSjzgtjService extends IService<TblYqnsSjzgSjzgtj> {
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
    JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsSjzgSjzgtj vo) throws Exception;


    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean saveOrUpdate(String token, TblYqnsSjzgSjzgtj vo) throws Exception;

    /**
     * 详情
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean detail(String token, TblYqnsSjzgSjzgtj vo) throws Exception;

    /**
     * 一个多个删除
     *
     * @param token
     * @param vo ids[]
     * @return
     * @throws Exception
     */
    JsonBean delete(String token, TblYqnsSjzgSjzgtj vo) throws Exception;

    /**
     * 整改统计附件删除
     *
     * @param token
     * @param sjzgtjid 
     * @return
     * @throws Exception
     */
    public JsonBean deleteZgtjAttach(String token, String attid, BigDecimal sjzgtjid) throws Exception;

    /**
     * 整改情况说明附件删除
     *
     * @param token
     * @param gzzdqksmid 
     * @return
     * @throws Exception
     */
    public JsonBean deleteQksmAttach(String token, String attid, BigDecimal gzzdqksmid) throws Exception;


	JsonBean getStatisticsInfo(String token, BigDecimal projectId, String dqzjjjcgtype, String dqqtjjcgtype) throws Exception;

}
