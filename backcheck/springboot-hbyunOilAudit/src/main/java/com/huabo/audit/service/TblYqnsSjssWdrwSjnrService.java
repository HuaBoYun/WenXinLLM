package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsSjssWdrwSjnr;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Administrator
 * @description 针对表【TBL_YQNS_SJSS_WDRW_SJNR(审减内容表)】的数据库操作Service
 */
public interface TblYqnsSjssWdrwSjnrService extends IService<TblYqnsSjssWdrwSjnr> {
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
    JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsSjssWdrwSjnr vo) throws Exception;

    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    @Transactional
    JsonBean saveOrUpdate(String token, TblYqnsSjssWdrwSjnr vo) throws Exception;

    /**
     * 详情
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean detail(String token, TblYqnsSjssWdrwSjnr vo) throws Exception;

    /**
     * 一个多个删除
     *
     * @param token
     * @param vo    ids[]
     * @return
     * @throws Exception
     */
    @Transactional
    JsonBean delete(String token, TblYqnsSjssWdrwSjnr vo) throws Exception;

}
