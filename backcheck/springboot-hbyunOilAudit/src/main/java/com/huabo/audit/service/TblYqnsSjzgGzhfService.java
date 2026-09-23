package com.huabo.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsSjzgGzhf;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJZG_GZHF(跟踪回访表)】的数据库操作Service
 */
public interface TblYqnsSjzgGzhfService extends IService<TblYqnsSjzgGzhf> {
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
    JsonBean list(String token, Integer pageNumber, Integer pageSize, TblYqnsSjzgGzhf vo) throws Exception;


    /**
     * 保存
     * 修改
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean saveOrUpdate(String token, TblYqnsSjzgGzhf vo) throws Exception;

    /**
     * 详情
     *
     * @param token
     * @param vo
     * @return
     * @throws Exception
     */
    JsonBean detail(String token, TblYqnsSjzgGzhf vo) throws Exception;

    /**
     * 一个多个删除
     *
     * @param token
     * @param vo ids[]
     * @return
     * @throws Exception
     */
    JsonBean delete(String token, TblYqnsSjzgGzhf vo) throws Exception;

}
